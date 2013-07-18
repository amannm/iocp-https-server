/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontopadsimple;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import parse.RawRequestLine;

public class OntopadSimple {

    public static byte[] html;
    private static Charset headerCharset;

    static {
        try {
            headerCharset = Charset.forName("US-ASCII");
            html = "<!doctype html><html><head><title>Ontopad</title><style>html, body {height:100%; width: 100%; margin:0; padding:0; background: #303030}#outer {position: absolute; width: 100%; height: 100%; display: table;}#inner {display: table-cell; vertical-align: middle; text-align: center;}#definition {background: #404040; border-top: solid 0.2em #505050; border-bottom: solid 0.2em #505050; padding-left: 1em; padding-right: 1em; font-family: georgia, arial, verdana, sans-serif; font-size: 180%;}#definition h1 {color: #a0a0e0;}#definition p {color: #e0e0e0;}</style></head><body><div id=\"outer\"><div id=\"inner\"><div id=\"definition\"><h1>on·tol·o·gy</h1><p>a particular theory about the kinds of things that have existence</p></div></div></div></body></html>".getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
        }
    }
    private static final ConcurrentLinkedQueue<ByteBuffer> bufferPool = new ConcurrentLinkedQueue<>();

    static {
        for (int i = 0; i < 1000; i++) {
            bufferPool.add(ByteBuffer.allocateDirect(4096));
        }
    }
    private static SSLContext sslContext;

    public static void start() throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException, Exception {

        KeyStore ksKeys = KeyStore.getInstance("JKS");
        ksKeys.load(new FileInputStream("C:/ontopad.jks"), "zygospermatics".toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ksKeys, "zygosperm1".toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ksKeys);

        sslContext = SSLContext.getInstance("TLS");
        //sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        sslContext.init(kmf.getKeyManagers(), null, null);

    }
    public static ExecutorService exe;
    public static AsynchronousChannelGroup group;

    static {
        exe = Executors.newFixedThreadPool(4);
    }

    public static void main(String[] args) {
        try {
            start();
        } catch (KeyStoreException ex) {
            Logger.getLogger(OntopadSimple.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OntopadSimple.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(OntopadSimple.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(OntopadSimple.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(OntopadSimple.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(OntopadSimple.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(OntopadSimple.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            group = AsynchronousChannelGroup.withThreadPool(exe);
            exe.execute(new HttpServer(group, 8080));
            exe.execute(new HttpsServer(group, 1234));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private static class HttpsServer implements Runnable {

        private final AsynchronousChannelGroup pool;
        private final int port;

        public HttpsServer(AsynchronousChannelGroup pool, int port) {
            this.pool = pool;
            this.port = port;
        }

        public boolean doHandshake(AsynchronousSocketChannel channel, final SSLEngine engine, final ByteBuffer net, final ByteBuffer app) throws InterruptedException, ExecutionException, TimeoutException, SSLException {

            System.err.println("Starting handshake");
            engine.beginHandshake();
            Future<Integer> futureClientHello = channel.read(net);//.get(3, TimeUnit.SECONDS);
            if (futureClientHello.get(3, TimeUnit.SECONDS) > 0) {

                do {
                    net.flip();
                    SSLEngineResult result = engine.unwrap(net, app);
                    if (result.getStatus() != Status.OK) {
                        return false;
                    }
                    while (result.getHandshakeStatus() == HandshakeStatus.NEED_TASK) {
                        engine.getDelegatedTask().run();
                    }
                    net.compact();
                } while (engine.getHandshakeStatus() == HandshakeStatus.NEED_UNWRAP);
                net.clear();
                do {
                    app.flip();
                    SSLEngineResult result = engine.unwrap(app, net);
                    if (result.getStatus() != Status.OK) {
                        return false;
                    }
                    while (result.getHandshakeStatus() == HandshakeStatus.NEED_TASK) {
                        engine.getDelegatedTask().run();
                    }
                    app.compact();
                } while (engine.getHandshakeStatus() == HandshakeStatus.NEED_UNWRAP);

            }

            while (true) {
                switch (engine.getHandshakeStatus()) {
                    case NEED_UNWRAP:
                        System.err.println("NEED UNWRAP");
                        do {
                            System.err.println("Consumed " + result.bytesConsumed());
                            while (engine.getHandshakeStatus() == HandshakeStatus.NEED_TASK) {
                                System.err.println("Run Task");
                                Runnable task = engine.getDelegatedTask();
                                if (task != null) {
                                    task.run();
                                }
                            }
                        } while (engine.getHandshakeStatus() == HandshakeStatus.NEED_UNWRAP);

                        break;
                    case NEED_WRAP:
                        System.err.println("NEED WRAP");
                        net.clear();
                        do {
                            app.flip();
                            SSLEngineResult result = engine.wrap(app, net);
                            if (result.getStatus() != Status.OK) {
                                System.err.println("Wrapping issue: " + result.getStatus());
                                return false;
                            }
                            app.compact();
                            System.err.println("Produced: " + result.bytesProduced());
                            while (engine.getHandshakeStatus() == HandshakeStatus.NEED_TASK) {
                                System.err.println("NEED TASK");
                                Runnable task = engine.getDelegatedTask();
                                if (task != null) {
                                    task.run();
                                }
                            }
                        } while (engine.getHandshakeStatus() == HandshakeStatus.NEED_WRAP);

                        System.err.println("Sending Bytes");
                        net.flip();
                        channel.write(net).get(3, TimeUnit.SECONDS);
                        net.compact();
                        break;
                    case NEED_TASK:
                        System.err.println("NEED TASK");
                        Runnable task = engine.getDelegatedTask();
                        if (task != null) {
                            task.run();
                        }
                        break;
                    case FINISHED:
                        System.err.println("FINISHED");
                        return true;
                    case NOT_HANDSHAKING:
                        System.err.println("NOT HANDSHAKING");
                        return true;
                }
            }
        }

        @Override
        public void run() {
            //event handler or listener
            try (final AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(pool)) {
                server.bind(new InetSocketAddress(port));
                System.err.println("HTTPS >> Starting TCP/IP listener on local port " + port);
                while (true) {
                    Future<AsynchronousSocketChannel> futureChannel = server.accept();
                    final ByteBuffer app = ByteBuffer.allocateDirect(32768);
                    final ByteBuffer net = ByteBuffer.allocateDirect(32768);
                    try (final AsynchronousSocketChannel channel = futureChannel.get()) {
                        String clientString = channel.getRemoteAddress().toString();
                        System.err.println("\nHTTPS " + clientString + " << New connection");
                        SSLEngine engine = sslContext.createSSLEngine();
                        engine.setUseClientMode(false);
                        engine.setWantClientAuth(false);
                        engine.setEnableSessionCreation(true);
                        System.err.print("HTTPS " + clientString + " << >> TLS handshaking...");
                        if (doHandshake(channel, engine, net, app)) {
                            net.clear();
                            Integer bytesRead = channel.read(net).get(3, TimeUnit.SECONDS);
                            net.flip();
                            if (bytesRead != -1) {
                                System.err.println("HTTPS << " + clientString + " -- Received: " + bytesRead);
                                do {
                                    SSLEngineResult result = engine.unwrap(net, app);
                                    if (result.getStatus() != Status.OK) {
                                        throw new IOException(result.getStatus() + " happened during decription");
                                    }
                                    System.err.println("HTTPS << " + clientString + " -- Consumed: " + result.bytesConsumed());
                                    System.err.println("HTTPS << " + clientString + " -- Produced: " + result.bytesProduced());
                                } while (net.hasRemaining());
                                app.flip();
                                RawRequestLine rrl = new RawRequestLine(app);
                                System.err.print("  [" + headerCharset.decode(rrl.getMethod()) + "]-");
                                System.err.print("[" + headerCharset.decode(rrl.getPath()) + "]-");
                                System.err.print("[" + headerCharset.decode(rrl.getProtocol()) + "]\n");
                                Map<byte[], ByteBuffer> headers = rrl.parseHeaders();
                                for (Map.Entry<byte[], ByteBuffer> entry : headers.entrySet()) {
                                    System.err.print("  [" + new String(entry.getKey(), "US-ASCII") + "]->");
                                    System.err.print("[" + headerCharset.decode(entry.getValue()) + "]\n");
                                }
                                ByteBuffer conn = headers.get(HttpRequestHeader.connection);
                                if (conn != null) {
                                }
                                app.clear();
                                ResponseBuilder response = new ResponseBuilder(app);
                                response.setStatus(200);
                                response.putDate();
                                response.putConnectionClose();
                                response.putDefaultContentType();
                                response.build(html);
                                net.clear();
                                do {
                                    SSLEngineResult result = engine.wrap(app, net);
                                    if (result.getStatus() != Status.OK) {
                                        throw new IOException(result.getStatus() + " happened during encryption");
                                    }
                                    System.err.println("HTTPS >> " + clientString + " -- Consumed: " + result.bytesConsumed());
                                    System.err.println("HTTPS >> " + clientString + " -- Produced: " + result.bytesProduced());
                                } while (app.hasRemaining());
                                net.flip();
                                Integer bytesSent = channel.write(net).get(3, TimeUnit.SECONDS);
                                System.err.println("HTTPS >> " + clientString + " -- Sent: " + bytesSent);
                            }
                            System.err.println("HTTPS << " + clientString + " -- Client signalled EOF. Terminating Connection...");
                        } else {
                            System.err.println("HTTPS << " + clientString + " -- SSL handshake failed. Terminating Connection...");
                        }
                    }
                }
            } catch (TimeoutException ex) {
                System.err.println("HTTPS << Client timed out: " + ex.getMessage());
                ex.printStackTrace(System.err);
            } catch (InterruptedException ex) {
                System.err.println("HTTPS -- Connection interrupted: " + ex.getMessage());
                ex.printStackTrace(System.err);
            } catch (ExecutionException ex) {
                System.err.println("HTTPS >> Execution Exception: " + ex.getMessage());
                ex.printStackTrace(System.err);
            } catch (IOException ex) {
                System.err.println("HTTPS >> IO Exception: " + ex.getMessage());
                ex.printStackTrace(System.err);
            }
        }
    }

    private static class HttpServer implements Runnable {

        private final AsynchronousChannelGroup pool;
        private final int port;

        public HttpServer(AsynchronousChannelGroup pool, int port) {
            this.pool = pool;
            this.port = port;
        }

        @Override
        public void run() {
            try (final AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(pool)) {
                server.bind(new InetSocketAddress(port));
                System.err.println("HTTP >> Starting TCP/IP listener on local port " + port);
                while (true) {
                    Future<AsynchronousSocketChannel> futureChannel = server.accept();
                    try (final AsynchronousSocketChannel socketChannel = futureChannel.get()) {
                        System.err.println("HTTP >> Incoming connection from " + socketChannel.getRemoteAddress() + " ... ");
                        ByteBuffer in = ByteBuffer.allocateDirect(4096);
                        Future<Integer> future = socketChannel.read(in);
                        if (future.get(3, TimeUnit.SECONDS) > 0) {
                            System.err.println("HTTP >> Request recieved");
                            in.flip();
                            RawRequestLine line = new RawRequestLine(in);
                            ByteBuffer bb = ByteBuffer.allocateDirect(1024);
                            ResponseBuilder response = new ResponseBuilder(bb);
                            response.setStatus(301);
                            response.putDate();
                            response.putHTTPSRedirect(line.getPath());
                            response.build();
                            future = socketChannel.write(bb);
                            future.get(3, TimeUnit.SECONDS);
                        } else {
                            System.err.println("HTTP >> Empty request recieved");
                        }
                    } catch (TimeoutException ex) {
                        System.err.println("HTTP >> Client timed out: " + ex.getMessage());
                    } catch (InterruptedException ex) {
                        System.err.println("HTTP >> Connection interrupted: " + ex.getMessage());
                    } catch (ExecutionException ex) {
                        System.err.println("HTTP >> Execution Exception: " + ex.getMessage());
                    }
                }
            } catch (IOException ex) {
                System.err.println("HTTP >> FATAL EXCEPTION: " + ex.getMessage());
                System.exit(0);
            }
        }
    }
}
