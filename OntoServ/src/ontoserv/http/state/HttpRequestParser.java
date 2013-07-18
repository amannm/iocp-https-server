/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontoserv.http.state;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontoserv.http.header.HttpHeaderWriter;
import ontoserv.http.header.HttpResponseWriter;
import ontoserv.http.util.constants.ASCII;
import ontoserv.http.util.constants.CacheControl;
import ontoserv.http.header.CacheControlWriter;
import ontoserv.http.util.constants.HttpHeaderConstants;
import ontoserv.http.util.constants.HttpProtocol;
import ontoserv.http.util.constants.HttpRequestMethod;
import ontoserv.http.util.constants.OntopadConstants;

/**
 *
 * @author Administrator
 */
//request line ends with buffer position on first char of field
public class HttpRequestParser {

    private Stack<HttpRequestTask> taskStack = new Stack<>();
    int start = 0;
    int valueStart = 0;
    int valueEnd = 0;
    String currentFieldName = null;
    List<String> currentFieldValues = new ArrayList<>();
    Map<String, String[]> headerMap = new HashMap<>();
    private final AsynchronousSocketChannel channel;
    private final ByteBuffer buffer;
    private final HttpRequest request = new HttpRequest();
    
    private final List<HttpHeaderWriter> responseHeaderList = new ArrayList<>();

    public HttpRequestParser(AsynchronousSocketChannel channel, ByteBuffer buffer) {
        this.channel = channel;
        this.buffer = buffer;
        responseHeaderList.add(new CacheControlWriter(buffer) {
            @Override
            public void configure() {
                putMaxAge(3600);
                putMustRevalidate();
            }
        });
        taskStack.add(new MethodStartFinder());
        channel.read(buffer, 3, TimeUnit.SECONDS, null, new HttpRequestTask() {
            @Override
            public void process() {
                while (!taskStack.isEmpty()) {
                    taskStack.pop().process();
                }
            }
        });
    }

    public abstract class HttpRequestTask implements CompletionHandler<Integer, Void> {

        public abstract void process();

        @Override
        public void completed(Integer result, Void attachment) {
            if (result > 0) {
                buffer.flip();
                process();
            } else {
                try {
                    channel.close();
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        }

        @Override
        public void failed(Throwable exc, Void attachment) {
            exc.printStackTrace(System.err);
        }
    }

    private class MethodStartFinder extends HttpRequestTask {

        @Override
        public void process() {
            while (buffer.hasRemaining()) {
                byte ch = buffer.get();
                if (ch == ASCII.CR || ch == ASCII.LF) {
                } else {
                    int sta = buffer.position() - 1;
                    taskStack.add(new MethodEndFinder(sta));
                    return;
                }
            }
            buffer.clear();
            channel.read(buffer, 3, TimeUnit.SECONDS, null, this);
        }
    }

    private class MethodEndFinder extends HttpRequestTask {

        private int st;

        public MethodEndFinder(int s) {
            this.st = s;
        }

        @Override
        public void process() {
            while (buffer.hasRemaining()) {
                if (buffer.get() == ASCII.SP) {
                    request.setMethod(parseMethod(buffer, st));
                    taskStack.add(new PathStartFinder());
                    return;
                }
            }
            buffer.limit(buffer.capacity());
            channel.read(buffer, 3, TimeUnit.SECONDS, null, this);
        }
    }

    private class PathStartFinder extends HttpRequestTask {

        @Override
        public void process() {
            while (buffer.hasRemaining()) {
                byte ch = buffer.get();
                if (ch == ASCII.SP || ch == ASCII.HT) {
                    continue;
                } else {
                    int sta = buffer.position() - 1;
                    taskStack.add(new PathEndFinder(sta));
                    return;
                }
            }
            buffer.clear();
            channel.read(buffer, 3, TimeUnit.SECONDS, null, this);
        }
    }

    private class PathEndFinder extends HttpRequestTask {

        private int st;

        public PathEndFinder(int s) {
            this.st = s;
        }

        @Override
        public void process() {
            while (buffer.hasRemaining()) {
                if (buffer.get() == ASCII.SP) {
                    request.setPath(parsePath(buffer, st));
                    taskStack.add(new ProtocolStartFinder());
                    return;
                }
            }
            buffer.limit(buffer.capacity());
            channel.read(buffer, 3, TimeUnit.SECONDS, null, this);
        }
    }

    private class ProtocolStartFinder extends HttpRequestTask {

        @Override
        public void process() {
            while (buffer.hasRemaining()) {
                byte ch = buffer.get();
                if (ch == ASCII.SP || ch == ASCII.HT) {
                    continue;
                } else {
                    start = buffer.position() - 1;
                    taskStack.add(new ProtocolEndFinder());
                    return;
                }
            }
            buffer.clear();
            channel.read(buffer, 3, TimeUnit.SECONDS, null, this);
        }
    }

    private class ProtocolEndFinder extends HttpRequestTask {

        @Override
        public void process() {
            while (buffer.hasRemaining()) {
                byte ch = buffer.get();
                if (ch == ASCII.CR) {
                    request.setProtocol(parseProtocol(buffer, start));
                    taskStack.add(new HandleRequestLineEndCR());
                    return;
                } else {
                    if (ch == ASCII.LF) {
                        request.setProtocol(parseProtocol(buffer, start));
                        taskStack.add(new HandleHeaderFieldLineStart());
                        return;
                    }
                }
            }
            buffer.limit(buffer.capacity());
            channel.read(buffer, 3, TimeUnit.SECONDS, null, this);
        }
    }

    private class HandleRequestLineEndCR extends HttpRequestTask {

        @Override
        public void process() {
            if (buffer.hasRemaining()) {
                if (buffer.get() == ASCII.LF) {
                    taskStack.add(new HandleHeaderFieldLineStart());
                } else {
                    throw new RuntimeException();
                }
            } else {
                buffer.limit(buffer.capacity());
                channel.read(buffer, 3, TimeUnit.SECONDS, null, this);
            }
        }
    }

    private class HandleHeaderFieldLineStart extends HttpRequestTask {

        @Override
        public void process() {
            if (buffer.hasRemaining()) {
                switch (buffer.get()) {
                    case ASCII.CR:
                        taskStack.add(new HandleHeaderFieldLineStartCR());
                        break;
                    case ASCII.LF:
                        taskStack.add(new SeekToEnd());
                        break;
                    default:
                        start = buffer.position() - 1;
                        taskStack.add(new SeekToHeaderFieldNameEnd());
                }
            } else {
                buffer.limit(buffer.capacity());
                channel.read(buffer, 3, TimeUnit.SECONDS, null, this);
            }
        }
    }

    private class HandleHeaderFieldLineStartCR extends HttpRequestTask {

        @Override
        public void process() {
            if (buffer.hasRemaining()) {
                if (buffer.get() == ASCII.LF) {
                    taskStack.add(new SeekToEnd());
                } else {
                    throw new RuntimeException();
                }
            } else {
                buffer.limit(buffer.capacity());
                channel.read(buffer, 3, TimeUnit.SECONDS, null, this);
            }
        }
    }

    private class SeekToHeaderFieldNameEnd extends HttpRequestTask {

        @Override
        public void process() {
            while (buffer.hasRemaining()) {
                if (buffer.get() == ASCII.Colon) {
                    currentFieldName = extractFieldName(buffer, start);
                    taskStack.add(new SkipToHeaderFieldValueStart());
                    return;
                }
            }
            buffer.limit(buffer.capacity());
            channel.read(buffer, 3, TimeUnit.SECONDS, null, this);
        }
    }

    private class SkipToHeaderFieldValueStart extends HttpRequestTask {

        @Override
        public void process() {
            while (buffer.hasRemaining()) {
                byte ch = buffer.get();
                if (ch == ASCII.SP || ch == ASCII.HT) {
                    continue;
                } else {
                    valueStart = buffer.position() - 1;
                    taskStack.add(new SeekToHeaderFieldValueEnd());
                    return;
                }
            }
            buffer.clear();
            channel.read(buffer, 3, TimeUnit.SECONDS, null, this);
        }
    }

    private class SeekToHeaderFieldValueEnd extends HttpRequestTask {

        @Override
        public void process() {
            while (buffer.hasRemaining()) {
                byte ch = buffer.get();
                if (ch == ASCII.CR) {
                    valueEnd = buffer.position() - 1;
                    taskStack.add(new HandleHeaderFieldLineEndCR());
                    return;
                } else {
                    if (ch == ASCII.LF) {
                        valueEnd = buffer.position() - 1;
                        taskStack.add(new HandleHeaderFieldLineEndLF());
                        return;
                    } else {
                        if (ch == ASCII.Comma) {
                            valueEnd = buffer.position() - 1;
                            currentFieldValues.add(extractFieldValue(buffer, valueStart, valueEnd));
                            taskStack.add(new SkipToHeaderFieldValueStart());
                            return;
                        }
                    }
                }
            }
            buffer.limit(buffer.capacity());
            channel.read(buffer, 3, TimeUnit.SECONDS, null, this);
        }
    }

    private class HandleHeaderFieldLineEndCR extends HttpRequestTask {

        @Override
        public void process() {
            if (buffer.hasRemaining()) {
                if (buffer.get() == ASCII.LF) {
                    taskStack.add(new HandleHeaderFieldLineEndLF());
                } else {
                    throw new RuntimeException();
                }
            } else {
                buffer.limit(buffer.capacity());
                channel.read(buffer, 3, TimeUnit.SECONDS, null, this);
            }
        }
    }

    private class HandleHeaderFieldLineEndLF extends HttpRequestTask {

        @Override
        public void process() {
            if (buffer.hasRemaining()) {
                switch (buffer.get()) {
                    case ASCII.CR:
                        currentFieldValues.add(extractFieldValue(buffer, valueStart, valueEnd));
                        String[] arr = new String[currentFieldValues.size()];
                        headerMap.put(currentFieldName, currentFieldValues.toArray(arr));
                        currentFieldValues.clear();
                        taskStack.add(new HandleHeaderFieldLineStartCR());
                        break;
                    case ASCII.LF:
                        currentFieldValues.add(extractFieldValue(buffer, valueStart, valueEnd));
                        arr = new String[currentFieldValues.size()];
                        headerMap.put(currentFieldName, currentFieldValues.toArray(arr));
                        currentFieldValues.clear();
                        taskStack.add(new SeekToEnd());
                        break;
                    case ASCII.SP:
                    case ASCII.HT:
                        taskStack.add(new SkipToHeaderFieldValueStart());
                        break;
                    default:
                        currentFieldValues.add(extractFieldValue(buffer, valueStart, valueEnd));
                        arr = new String[currentFieldValues.size()];
                        headerMap.put(currentFieldName, currentFieldValues.toArray(arr));
                        currentFieldValues.clear();
                        start = buffer.position() - 1;
                        taskStack.add(new SeekToHeaderFieldNameEnd());
                }
            } else {
                buffer.limit(buffer.capacity());
                channel.read(buffer, 3, TimeUnit.SECONDS, null, this);
            }
        }
    }

    private class SeekToEnd extends HttpRequestTask {

        @Override
        public void process() {
            request.setHeaderMap(headerMap);
            HttpResponseWriter res = new HttpResponseWriter(channel, ByteBuffer.allocateDirect(4096));
            switch (request.getPath().getPath()) {
                case "/favicon.ico":
                    res.putStatus(200);
                    res.putDate();
                    res.putIconContentType();
                    res.putConnectionClose();
                    CacheControl ma = CacheControl.MaxAge;

                    res.setCacheControl(new CacheControl[]{});
                    res.send(OntopadConstants.favicon);
                    break;
                case "/":
                    res.putStatus(200);
                    res.putDate();
                    res.putDefaultContentType();
                    res.putConnectionClose();
                    res.putContentEncoding(HttpHeaderConstants.gzip);
                    res.putDefaultMaxAge();
                    res.send(OntopadConstants.gzipDefaultContent);
                    break;
                default:
                    res.putStatus(404);
                    res.putDate();
                    res.putConnectionClose();
            }
        }
    }

    private static HttpRequestMethod parseMethod(ByteBuffer requestBuffer, int nameStart) {
        int current = requestBuffer.position();
        requestBuffer.position(nameStart);
        switch (current - 1 - nameStart) {
            case 3:
                // <editor-fold desc="GET or PUT">
                if (requestBuffer.get() == ASCII.G) {
                    if (requestBuffer.get() == ASCII.E
                            && requestBuffer.get() == ASCII.T) {
                        requestBuffer.position(current);
                        return HttpRequestMethod.GET;
                    }
                } else {
                    if (requestBuffer.get() == ASCII.P) {
                        if (requestBuffer.get() == ASCII.U
                                && requestBuffer.get() == ASCII.T) {
                            requestBuffer.position(current);

                            return HttpRequestMethod.PUT;
                        }
                    }
                }
                throw new RuntimeException();
            // </editor-fold>
            case 4:
                // <editor-fold desc="POST or HEAD">
                if (requestBuffer.get() == ASCII.P) {
                    if (requestBuffer.get() == ASCII.O
                            && requestBuffer.get() == ASCII.S
                            && requestBuffer.get() == ASCII.T) {
                        requestBuffer.position(current);
                        return HttpRequestMethod.POST;
                    }
                } else {
                    if (requestBuffer.get() == ASCII.H) {
                        if (requestBuffer.get() == ASCII.E
                                && requestBuffer.get() == ASCII.A
                                && requestBuffer.get() == ASCII.D) {
                            requestBuffer.position(current);
                            return HttpRequestMethod.HEAD;
                        }
                    }
                }
                throw new RuntimeException();
            // </editor-fold>
            case 5:
                // <editor-fold desc="TRACE or PATCH">
                if (requestBuffer.get() == ASCII.T) {
                    if (requestBuffer.get() == ASCII.R
                            && requestBuffer.get() == ASCII.A
                            && requestBuffer.get() == ASCII.C
                            && requestBuffer.get() == ASCII.E) {
                        requestBuffer.position(current);
                        return HttpRequestMethod.TRACE;
                    }
                } else {
                    if (requestBuffer.get() == ASCII.P) {
                        if (requestBuffer.get() == ASCII.A
                                && requestBuffer.get() == ASCII.T
                                && requestBuffer.get() == ASCII.C
                                && requestBuffer.get() == ASCII.H) {
                            requestBuffer.position(current);
                            return HttpRequestMethod.PATCH;
                        }
                    }
                }
                throw new RuntimeException();
            // </editor-fold>
            case 6:
                // <editor-fold desc="DELETE">
                if (requestBuffer.get() == ASCII.D) {
                    if (requestBuffer.get() == ASCII.E
                            && requestBuffer.get() == ASCII.L
                            && requestBuffer.get() == ASCII.E
                            && requestBuffer.get() == ASCII.T
                            && requestBuffer.get() == ASCII.E) {
                        requestBuffer.position(current);
                        return HttpRequestMethod.DELETE;
                    }
                }
                throw new RuntimeException();
            // </editor-fold>
            case 7:
                // <editor-fold desc="OPTIONS or CONNECT">
                if (requestBuffer.get() == ASCII.O) {
                    if (requestBuffer.get() == ASCII.P
                            && requestBuffer.get() == ASCII.T
                            && requestBuffer.get() == ASCII.I
                            && requestBuffer.get() == ASCII.O
                            && requestBuffer.get() == ASCII.N
                            && requestBuffer.get() == ASCII.S) {
                        requestBuffer.position(current);
                        return HttpRequestMethod.OPTIONS;
                    }
                } else {
                    if (requestBuffer.get() == ASCII.C) {
                        if (requestBuffer.get() == ASCII.O
                                && requestBuffer.get() == ASCII.N
                                && requestBuffer.get() == ASCII.N
                                && requestBuffer.get() == ASCII.E
                                && requestBuffer.get() == ASCII.C
                                && requestBuffer.get() == ASCII.T) {
                            requestBuffer.position(current);
                            return HttpRequestMethod.CONNECT;
                        }
                    }
                }
            // </editor-fold>
            default:
                throw new RuntimeException();
        }
    }

    private static URI parsePath(ByteBuffer requestBuffer, int nameStart) {
        int current = requestBuffer.position();
        requestBuffer.position(nameStart);
        byte[] uriBytes = new byte[current - 1 - nameStart];
        requestBuffer.get(uriBytes);
        requestBuffer.position(current);
        try {
            return new URI(new String(uriBytes, "US-ASCII"));
        } catch (UnsupportedEncodingException | URISyntaxException ex) {
            return null;
        }
    }

    private static HttpProtocol parseProtocol(ByteBuffer requestBuffer, int nameStart) {
        int current = requestBuffer.position();
        requestBuffer.position(nameStart);
        if (current - requestBuffer.position() >= 9) {
            if (requestBuffer.get() == ASCII.H
                    && requestBuffer.get() == ASCII.T
                    && requestBuffer.get() == ASCII.T
                    && requestBuffer.get() == ASCII.P
                    && requestBuffer.get() == ASCII.Slash
                    && requestBuffer.get() == ASCII.One
                    && requestBuffer.get() == ASCII.Period) {
                byte ch = requestBuffer.get();
                if (ch == ASCII.One) {
                    requestBuffer.position(current);
                    return HttpProtocol.HTTP11;
                } else {
                    if (ch == ASCII.Zero) {
                        requestBuffer.position(current);
                        return HttpProtocol.HTTP10;
                    }
                }
            }
        }
        return null;
    }

    private static String extractFieldValue(ByteBuffer requestBuffer, int valueStart, int valueEnd) {
        int current = requestBuffer.position();
        requestBuffer.position(valueStart);
        byte[] fieldValueBytes = new byte[valueEnd - valueStart];
        requestBuffer.get(fieldValueBytes);
        requestBuffer.position(current);
        try {
            return new String(fieldValueBytes, "US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String extractFieldName(ByteBuffer requestBuffer, int nameStart) {
        int current = requestBuffer.position();
        requestBuffer.position(nameStart);
        byte[] fieldNameBytes = new byte[current - 1 - nameStart];
        requestBuffer.get(fieldNameBytes);
        requestBuffer.position(current);
        try {
            return new String(fieldNameBytes, "US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void validate(byte ch) {
        if (ch >= 97) {
            if (ch <= 122) {
                //lowercase letter
            } else {
                if (ch == 126 || ch == 124) {
                    //tilde, bar
                } else {
                    throw new RuntimeException("Encountered invalid token character: " + ch);
                }
            }
        } else {
            if (ch >= 65) {
                if (ch <= 90) {
                    //uppercase letter
                    buffer.put(buffer.position() - 1, (byte) (ch | 0b00100000));
                } else {
                    if (ch == 94 || ch == 95 || ch == 96) {
                        //underscore, carat, grave
                    } else {
                        throw new RuntimeException("Encountered invalid token character: " + ch);
                    }
                }
            } else {
                if (ch == 58) {
                    //colon
                    //temp.limit(buffer.position() - 1);
                    //
                    return;
                } else {
                    if (ch == 45) {
                        //hyphen
                    } else {
                        if (ch == 13) {
                            if (buffer.hasRemaining() && buffer.get() == 10) {
                                //CR LF (after CRLF or LF)
                                return;
                            } else {
                                throw new RuntimeException("Encoutnered invalid token character: " + ch);
                            }
                        } else {
                            if (ch >= 48) {
                                if (ch <= 57) {
                                    //digits
                                } else {
                                    throw new RuntimeException("Encoutnered invalid token character: " + ch);
                                }
                            } else {
                                //valid, but obscure characters below 48 (not hyphen)
                                if (ch == 10) {
                                    //LF (after CRLF or LF)
                                    return;
                                } else {
                                    if (ch >= 35) {
                                        if (ch <= 39) {
                                            //number sign, dollar sign, percent, ampersand, singlequote
                                        } else {
                                            if (ch == 46 || ch == 43 || ch == 42) {
                                                //period, plus, asterisk
                                            } else {
                                                throw new RuntimeException("Encoutnered invalid token character: " + ch);
                                            }
                                        }
                                    } else {
                                        if (ch == 33) {
                                            //exclamation point (heh)
                                        } else {
                                            throw new RuntimeException("Encoutnered invalid token character: " + ch);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
