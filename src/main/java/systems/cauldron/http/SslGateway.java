package systems.cauldron.http;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import java.io.Closeable;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SslGateway implements Closeable {

    private static final Logger LOG = Logger.getLogger(SslGateway.class.getName());

    private static SSLContext sslContext;

    static {
        try {
            String key1 = "helloworld";
            String key2 = "helloworld";
            KeyStore ksKeys = KeyStore.getInstance("JKS");
            ksKeys.load(new FileInputStream(Paths.get(System.getProperty("user.home"), "keystore.jks").toFile()), key1.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ksKeys, key2.toCharArray());

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    private final AsynchronousSocketChannel channel;
    private final SSLEngine engine;
    private final ByteBuffer net = ByteBuffer.allocateDirect(32768);
    private final ByteBuffer app = ByteBuffer.allocateDirect(32768);

    public SslGateway(AsynchronousSocketChannel channel) throws InterruptedException, ExecutionException, TimeoutException, SSLException {
        this.channel = channel;
        this.engine = sslContext.createSSLEngine("localhost", 8181);
        engine.setUseClientMode(false);
        engine.setWantClientAuth(false);
        engine.setEnableSessionCreation(true);
        handshake(channel);
    }

    public boolean receive() throws SSLException, InterruptedException, ExecutionException, TimeoutException {

        net.clear();
        Integer length = channel.read(net).get(3, TimeUnit.SECONDS);
        net.flip();

        if (length == -1) {
            return false;
        }

        app.clear();
        do {
            SSLEngineResult result = engine.unwrap(net, app);
            switch (result.getStatus()) {
                case OK:
                    break;
                case CLOSED:
                    engine.closeInbound();
                    return false;
                case BUFFER_UNDERFLOW:
                case BUFFER_OVERFLOW:
                    throw new RuntimeException("incoming buffer size mismatch: " + result.getStatus());
            }
        } while (net.hasRemaining());
        app.flip();
        return true;

    }

    public boolean transmit() throws SSLException, InterruptedException, ExecutionException, TimeoutException {

        if (!app.hasRemaining()) {
            return false;
        }

        net.clear();
        do {
            SSLEngineResult result = engine.wrap(app, net);
            switch (result.getStatus()) {
                case OK:
                    break;
                case CLOSED:
                    throw new AssertionError("unexpected CLOSED SSLEngineResult from wrap operation");
                case BUFFER_UNDERFLOW:
                case BUFFER_OVERFLOW:
                    throw new RuntimeException("outgoing buffer size mismatch: " + result.getStatus());
            }
        } while (app.hasRemaining());
        net.flip();

        channel.write(net).get(3, TimeUnit.SECONDS);
        return true;

    }

    public ByteBuffer getBuffer() {
        return this.app;
    }


    private void handshake(AsynchronousSocketChannel channel) throws InterruptedException, ExecutionException, TimeoutException, SSLException {

        engine.beginHandshake();

        net.limit(0);

        while (true) {
            switch (engine.getHandshakeStatus()) {
                case NEED_UNWRAP:
                    do {
                        if (!net.hasRemaining()) {
                            net.clear();
                            channel.read(net).get(3, TimeUnit.SECONDS);
                            net.flip();
                        }
                        SSLEngineResult result = engine.unwrap(net, app);
                        checkHandshakeResult(result);
                        runRemainingTasks(engine);
                    } while (engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP);
                    break;
                case NEED_WRAP:
                    app.flip();
                    net.clear();
                    do {
                        SSLEngineResult result = engine.wrap(app, net);
                        checkHandshakeResult(result);
                        runRemainingTasks(engine);
                    } while (engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_WRAP);
                    net.flip();
                    channel.write(net).get(3, TimeUnit.SECONDS);
                    break;
                case NEED_TASK:
                    runTask(engine);
                    break;
                case NOT_HANDSHAKING:
                    return;
                case FINISHED:
                    throw new AssertionError("call to SSLEngine.getHandshakeStatus() should never return FINISHED");
                case NEED_UNWRAP_AGAIN:
                    throw new UnsupportedOperationException("DTLS handshake not supported");
            }
        }
    }

    private static void checkHandshakeResult(SSLEngineResult result) {
        switch (result.getStatus()) {
            case OK:
                break;
            case CLOSED:
            case BUFFER_UNDERFLOW:
            case BUFFER_OVERFLOW:
                throw new RuntimeException("SSL handshake error: " + result.getStatus());
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

    @Override
    public void close() {
        engine.closeOutbound();
    }
}
