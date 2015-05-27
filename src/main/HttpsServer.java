import v2.parse.RawRequestLine;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Amann on 5/27/2015.
 */

public class HttpsServer implements Runnable {

    private final AsynchronousChannelGroup pool;
    private final int port;
    private final byte[] html;


    private static final ConcurrentLinkedQueue<ByteBuffer> bufferPool = new ConcurrentLinkedQueue<>();
    static {
        for (int i = 0; i < 1000; i++) {
            bufferPool.add(ByteBuffer.allocateDirect(4096));
        }
    }


    private static SSLContext sslContext;
    static {
        try {
            String keyStorePath = "C:/keystore.jks";
            String key1 = "helloworld";
            String key2 = "helloworld";
            KeyStore ksKeys = KeyStore.getInstance("JKS");
            ksKeys.load(new FileInputStream(keyStorePath), key1.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ksKeys, key2.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ksKeys);

            sslContext = SSLContext.getInstance("TLS");
            //sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            sslContext.init(kmf.getKeyManagers(), null, null);

        } catch (KeyStoreException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public HttpsServer(AsynchronousChannelGroup pool, int port, String html) {
        this.pool = pool;
        this.port = port;
        this.html = html.getBytes(StandardCharsets.US_ASCII);
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
                                if (result.getStatus() != SSLEngineResult.Status.OK) {
                                    throw new IOException(result.getStatus() + " happened during decription");
                                }
                                System.err.println("HTTPS << " + clientString + " -- Consumed: " + result.bytesConsumed());
                                System.err.println("HTTPS << " + clientString + " -- Produced: " + result.bytesProduced());
                            } while (net.hasRemaining());
                            app.flip();
                            RawRequestLine rrl = new RawRequestLine(app);
                            System.err.print("  [" + StandardCharsets.US_ASCII.decode(rrl.getMethod()) + "]-");
                            System.err.print("[" + StandardCharsets.US_ASCII.decode(rrl.getPath()) + "]-");
                            System.err.print("[" + StandardCharsets.US_ASCII.decode(rrl.getProtocol()) + "]\n");
                            Map<byte[], ByteBuffer> headers = rrl.parseHeaders();
                            for (Map.Entry<byte[], ByteBuffer> entry : headers.entrySet()) {
                                System.err.print("  [" + new String(entry.getKey(), "US-ASCII") + "]->");
                                System.err.print("[" + StandardCharsets.US_ASCII.decode(entry.getValue()) + "]\n");
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
                                if (result.getStatus() != SSLEngineResult.Status.OK) {
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

    public boolean doHandshake(AsynchronousSocketChannel channel, final SSLEngine engine, final ByteBuffer net, final ByteBuffer app) throws InterruptedException, ExecutionException, TimeoutException, SSLException {
        System.out.println("Starting handshake");
        engine.beginHandshake();
        channel.read(net).get(3, TimeUnit.SECONDS);
        while (true) {
            switch (engine.getHandshakeStatus()) {
                case NEED_UNWRAP:
                    System.out.println("NEED UNWRAP");
                    do {
                        net.flip();
                        SSLEngineResult result = engine.unwrap(net, app);
                        if (result.getStatus() != SSLEngineResult.Status.OK) {
                            System.out.println("Unwrapping issue: " + result.getStatus());
                            return false;
                        }
                        while (engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                            System.out.println("Run Task");
                            Runnable task = engine.getDelegatedTask();
                            if (task != null) {
                                task.run();
                            }
                        }
                        net.compact();
                    } while (engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP);

                    break;
                case NEED_WRAP:
                    System.out.println("NEED WRAP");
                    net.clear();
                    do {
                        app.flip();
                        SSLEngineResult result = engine.wrap(app, net);
                        if (result.getStatus() != SSLEngineResult.Status.OK) {
                            System.out.println("Wrapping issue: " + result.getStatus());
                            return false;
                        }
                        app.compact();
                        System.err.println("Produced: " + result.bytesProduced());
                        while (engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                            System.out.println("NEED TASK");
                            Runnable task = engine.getDelegatedTask();
                            if (task != null) {
                                task.run();
                            }
                        }
                    } while (engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_WRAP);

                    System.out.println("Sending Bytes");
                    net.flip();
                    channel.write(net).get(3, TimeUnit.SECONDS);
                    net.compact();
                    break;
                case NEED_TASK:
                    System.out.println("NEED TASK");
                    Runnable task = engine.getDelegatedTask();
                    if (task != null) {
                        task.run();
                    }
                    break;
                case FINISHED:
                    System.out.println("FINISHED");
                    return true;
                case NOT_HANDSHAKING:
                    System.out.println("NOT HANDSHAKING");
                    return true;
            }
        }
    }

}