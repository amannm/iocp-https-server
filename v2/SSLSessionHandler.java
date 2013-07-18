/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontopadsimple;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;

/**
 *
 * @author admin
 */
public class SSLSessionHandler implements CompletionHandler<AsynchronousSocketChannel, SSLContext> {

    private void doHandshake(SSLEngine engine) {
        while (true) {
            switch (engine.getHandshakeStatus()) {
                case NEED_UNWRAP:
                    System.err.println("NEED UNWRAP");
                    do {
                        System.err.println("Consumed " + result.bytesConsumed());
                        while (engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                            System.err.println("Run Task");
                            Runnable task = engine.getDelegatedTask();
                            if (task != null) {
                                task.run();
                            }
                        }
                    } while (engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP);

                    break;
                case NEED_WRAP:
                    System.err.println("NEED WRAP");
                    net.clear();
                    do {
                        app.flip();
                        SSLEngineResult result = engine.wrap(app, net);
                        if (result.getStatus() != SSLEngineResult.Status.OK) {
                            System.err.println("Wrapping issue: " + result.getStatus());
                            return false;
                        }
                        app.compact();
                        System.err.println("Produced: " + result.bytesProduced());
                        while (engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                            System.err.println("NEED TASK");
                            Runnable task = engine.getDelegatedTask();
                            if (task != null) {
                                task.run();
                            }
                        }
                    } while (engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_WRAP);

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
    public void completed(AsynchronousSocketChannel channel, SSLContext sslContext) {
        SSLEngine engine = sslContext.createSSLEngine();
        engine.setUseClientMode(false);
        engine.setWantClientAuth(false);
        engine.setEnableSessionCreation(true);

    }

    @Override
    public void failed(Throwable exc, SSLContext attachment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
