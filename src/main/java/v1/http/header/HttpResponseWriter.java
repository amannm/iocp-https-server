/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v1.http.header;

import v1.http.util.SimpleCompletionHandler;
import v1.http.util.constants.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author admin
 */
public class HttpResponseWriter implements Flushable, Closeable {

    private final AsynchronousSocketChannel channel;
    private final ByteBuffer source;
    private HttpContentCoding encoding;
    private static byte[] baseURI;
    public static final SimpleDateFormat httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

    static {
        httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            baseURI = "http://www.ontopad.com".getBytes(StandardCharsets.US_ASCII);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }

    }

    public HttpResponseWriter(final AsynchronousSocketChannel channel, final ByteBuffer source) {
        this.channel = channel;
        this.source = source;
    }

    public void putStatus(int status) {
        source.put(HttpStatus.status[status]);
    }

    public void putLocation(byte[] path) {
        source.put(HttpResponseHeader.Location.getBytes()).put(ASCII.Colon).put(ASCII.SP)
                .put(baseURI).put(path).put(ASCII.CRLF);
    }

    public void putConnection(HttpConnectionHeader header) {
        source.put(HttpResponseHeader.Connection.getBytes()).put(ASCII.Colon).put(ASCII.SP)
                .put(header.getBytes()).put(ASCII.CRLF);
    }

    public void putContentType(byte[] type) {
        source.put(HttpResponseHeader.ContentType.getBytes()).put(ASCII.Colon).put(ASCII.SP)
                .put(type).put(ASCII.CRLF);
    }

    public void putStrictTransportSecurity() {
        source.put(HttpResponseHeader.StrictTransportSecurity.getBytes()).put(ASCII.Colon).put(ASCII.SP)
                .put(HttpStrictTransportSecurityHeader.MaxAge.getBytes()).put(ASCII.Equals).put(ASCII.HourInSeconds).put(ASCII.CRLF);
    }

    public void putStrictTransportSecurity(int duration) {
        source.put(HttpResponseHeader.StrictTransportSecurity.getBytes()).put(ASCII.Colon).put(ASCII.SP)
                .put(HttpStrictTransportSecurityHeader.MaxAge.getBytes()).put(ASCII.Equals).put(ASCII.valueOf(duration)).put(ASCII.CRLF);
    }

    public void putDate() {
        try {
            source.put(HttpResponseHeader.Date.getBytes()).put(ASCII.Colon).put(ASCII.SP)
                    .put(httpDateFormat.format(new Date()).getBytes(StandardCharsets.US_ASCII)).put(ASCII.CRLF);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void putContentEncoding(HttpContentCoding encoding) {
        this.encoding = encoding;
        source.put(HttpResponseHeader.ContentEncoding.getBytes()).put(ASCII.Colon).put(ASCII.SP)
                .put(encoding.getBytes()).put(ASCII.CRLF);

    }

    public void putCacheControl(CacheControl.ResponseDirective... directives) {
        if (directives != null && directives.length != 0) {
            source.put(CacheControl.getNameBytes()).put(ASCII.Colon).put(ASCII.SP);
            source.put(directives[0].getBytes());
            for (int i = 1; i < directives.length; i++) {
                source.put(ASCII.Comma).put(ASCII.SP).put(directives[i].getBytes());
            }
        }
    }

    public void putContent(byte[] content) {
        if (encoding == HttpContentCoding.gzip) {
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                try (GZIPOutputStream gos = new GZIPOutputStream(stream)) {
                    gos.write(content);
                }
                content = stream.toByteArray();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        source.put(HttpResponseHeader.ContentLength.getBytes()).put(ASCII.Colon).put(ASCII.SP).put(ASCII.valueOf(content.length)).put(ASCII.CRLF);
        source.put(ASCII.CRLF);
        source.put(content);
    }

    @Override
    public void flush() throws IOException {
        source.flip();
        channel.write(source, 3, TimeUnit.SECONDS, null, new SimpleCompletionHandler() {
            @Override
            public void completed(Integer result, Void attachment) {
            }
        });
    }

    @Override
    public void close() throws IOException {
        source.flip();
        channel.write(source, 3, TimeUnit.SECONDS, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer result, Void attachment) {
                try {
                    channel.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace(System.err);
                try {
                    channel.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
