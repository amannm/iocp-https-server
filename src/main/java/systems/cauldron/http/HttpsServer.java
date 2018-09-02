package systems.cauldron.http;

import systems.cauldron.http.util.ResponseBuilder;
import systems.cauldron.http.v2.parse.RawRequestLine;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Amann on 5/27/2015.
 */

public class HttpsServer implements Runnable {


    private static final Logger LOG = Logger.getLogger(HttpsServer.class.getName());

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
            String key1 = "helloworld";
            String key2 = "helloworld";
            KeyStore ksKeys = KeyStore.getInstance("JKS");
            ksKeys.load(new FileInputStream(Paths.get(System.getProperty("user.home"), "keystore.jks").toFile()), key1.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ksKeys, key2.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ksKeys);

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            //sslContext.init(kmf.getKeyManagers(), null, null);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }


    public HttpsServer(AsynchronousChannelGroup pool, int port, String html) {
        this.pool = pool;
        this.port = port;
        this.html = html.getBytes(StandardCharsets.US_ASCII);
    }


    @Override
    public void run() {
        try (final AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(pool)) {
            server.bind(new InetSocketAddress(port));
            LOG.info("HTTPS >> Starting TCP/IP listener on local port " + port);
            while (true) {
                Future<AsynchronousSocketChannel> futureChannel = server.accept();
                final ByteBuffer app = ByteBuffer.allocateDirect(32768);
                final ByteBuffer net = ByteBuffer.allocateDirect(32768);
                try (final AsynchronousSocketChannel channel = futureChannel.get()) {
                    String clientString = channel.getRemoteAddress().toString();
                    LOG.info("\nHTTPS " + clientString + " << New connection");
                    SSLEngine engine = sslContext.createSSLEngine();
                    engine.setUseClientMode(false);
                    engine.setWantClientAuth(false);
                    engine.setEnableSessionCreation(true);
                    LOG.info("HTTPS " + clientString + " << >> TLS handshaking...");
                    if (doHandshake(channel, engine, net, app)) {
                        net.clear();
                        Integer bytesRead = channel.read(net).get(3, TimeUnit.SECONDS);
                        net.flip();
                        if (bytesRead != -1) {
                            do {
                                SSLEngineResult result = engine.unwrap(net, app);
                                checkResult(result);
                            } while (net.hasRemaining());
                            app.flip();
                            RawRequestLine rrl = new RawRequestLine(app);
                            LOG.info(rrl.toString());
                            //TODO: ByteBuffer conn = headers.get(HttpRequestHeader.connection);
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
                                checkResult(result);
                            } while (app.hasRemaining());
                            net.flip();
                            Integer bytesSent = channel.write(net).get(3, TimeUnit.SECONDS);
                            LOG.info("HTTPS >> " + clientString + " -- Sent: " + bytesSent);
                        }
                        LOG.info("HTTPS << " + clientString + " -- Client signalled EOF. Terminating Connection...");
                    } else {
                        LOG.info("HTTPS << " + clientString + " -- SSL handshake failed. Terminating Connection...");
                    }
                }
            }
        } catch (TimeoutException ex) {
            LOG.info("HTTPS << Client timed out: " + ex.getMessage());
            ex.printStackTrace(System.err);
        } catch (InterruptedException ex) {
            LOG.info("HTTPS -- Connection interrupted: " + ex.getMessage());
            ex.printStackTrace(System.err);
        } catch (ExecutionException ex) {
            LOG.info("HTTPS >> Execution Exception: " + ex.getMessage());
            ex.printStackTrace(System.err);
        } catch (IOException ex) {
            LOG.info("HTTPS >> IO Exception: " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }

    public boolean doHandshake(AsynchronousSocketChannel channel, final SSLEngine engine, final ByteBuffer net, final ByteBuffer app) throws InterruptedException, ExecutionException, TimeoutException, SSLException {
        engine.beginHandshake();
        while (true) {
            switch (engine.getHandshakeStatus()) {
                case NEED_UNWRAP:
                    net.clear();
                    channel.read(net).get(3, TimeUnit.SECONDS);
                    do {
                        net.flip();
                        SSLEngineResult result = engine.unwrap(net, app);
                        checkResult(result);
                        runRemainingTasks(engine);
                        net.compact();
                    } while (engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP);
                    break;
                case NEED_WRAP:
                    net.clear();
                    do {
                        app.flip();
                        SSLEngineResult result = engine.wrap(app, net);
                        checkResult(result);
                        runRemainingTasks(engine);
                        app.compact();
                    } while (engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_WRAP);
                    net.flip();
                    channel.write(net).get(3, TimeUnit.SECONDS);
                    break;
                case NEED_TASK:
                    runTask(engine);
                    break;
                case NOT_HANDSHAKING:
                    return true;
                case FINISHED:
                    throw new AssertionError("call to SSLEngine.getHandshakeStatus() should never return FINISHED");
                case NEED_UNWRAP_AGAIN:
                    throw new UnsupportedOperationException("DTLS handshake not supported");
            }
        }
    }

    private static void checkResult(SSLEngineResult result) {
        if (result.getStatus() != SSLEngineResult.Status.OK) {
            throw new RuntimeException("SSL handshake error: " + result.getStatus());
        } else {
            LOG.info(result.bytesConsumed() + " >> HTTPS >> " + result.bytesProduced());
        }
    }

    private static void runRemainingTasks(SSLEngine engine) {
        while (engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
            runTask(engine);
        }
    }

    private static void runTask(SSLEngine engine) {
        Runnable task = engine.getDelegatedTask();
        if (task != null) {
            task.run();
        }
    }

}