/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontopadsimple;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import javax.net.ssl.SSLException;

/**
 *
 * @author admin
 */
public final class AsynchronousSSLChannel implements AsynchronousByteChannel {

    private AsynchronousByteChannel channel;
    private HandshakeStatus hs;
    private SSLEngine engine;

    public void doHandshake(AsynchronousByteChannel channel, SSLContext sslContext) throws InterruptedException, ExecutionException, TimeoutException, SSLException {
        this.channel = channel;
        this.engine = sslContext.createSSLEngine();
        this.engine.setUseClientMode(false);
        this.engine.setWantClientAuth(false);
        this.engine.setEnableSessionCreation(true);
        engine.beginHandshake();
        hs = engine.getHandshakeStatus();
        ByteBuffer app = ByteBuffer.allocateDirect(32768);
        ByteBuffer net = ByteBuffer.allocateDirect(32768);
        while (true) {
            switch (hs) {
                case NEED_UNWRAP:
                    Future<Integer> futureHello = channel.read(net);
                    //System.err.println("Receiving handshake record");
                    if (futureHello.get(3, TimeUnit.SECONDS) > 0) {
                        net.flip();
                        do {
                            //System.err.println("Unwrapping");
                            SSLEngineResult result = engine.unwrap(net, app);
                            if (result.getStatus() != SSLEngineResult.Status.OK) {
                                //System.err.println("Unwrap buffer problem during handshake: " + result.getStatus().toString());
                                return;
                            }
                            hs = result.getHandshakeStatus();
                            if (hs == HandshakeStatus.NEED_TASK) {
                                //System.err.println("Running task");
                                engine.getDelegatedTask().run();
                                hs = engine.getHandshakeStatus();
                            }
                        } while (hs == HandshakeStatus.NEED_UNWRAP);
                        net.compact();
                        net.clear();
                    } else {
                        //System.err.println("Empty record received during handshake");
                    }
                    break;

                case NEED_WRAP:
                    do {
                        //System.err.println("Wrapping");
                        SSLEngineResult result = engine.wrap(app, net);
                        if (result.getStatus() != SSLEngineResult.Status.OK) {
                            throw new SSLException(result.getStatus().toString() + " result from wrap operation during handshake");
                        }
                        hs = result.getHandshakeStatus();
                    } while (hs == HandshakeStatus.NEED_WRAP);
                    net.flip();
                    Future<Integer> futureFart = channel.write(net);
                    //System.err.println("Sending handshake record");
                    if (futureFart.get(3, TimeUnit.SECONDS) > 0) {
                        net.clear();
                    } else {
                        //System.err.println("Empty record sent during handshake");
                    }
                    break;
                case NEED_TASK:
                    //System.err.println("Running task");
                    engine.getDelegatedTask().run();
                    hs = engine.getHandshakeStatus();
                    break;
                case FINISHED:
                    //System.err.println("Finished handshaking");
                    return;
                case NOT_HANDSHAKING:
                    //System.err.println("Attempted to handshake when not handshaking");
                    return;
            }
        }
    }

    private void sendNetBuffer(ByteBuffer net) throws InterruptedException, ExecutionException, TimeoutException {
        net.flip();
        Future<Integer> futureFart = channel.write(net);
        Integer bytesSent = futureFart.get(3, TimeUnit.SECONDS);
        net.clear();
    }

    private void receiveNetBuffer(ByteBuffer net) throws InterruptedException, ExecutionException, TimeoutException {
        Future<Integer> futureHello = channel.read(net);
        Integer bytesReceived = futureHello.get(3, TimeUnit.SECONDS);
        net.flip();
    }

    private void consumeNetBuffer(ByteBuffer net, ByteBuffer app) throws SSLException {
        do {
            SSLEngineResult result = engine.unwrap(net, app);
            if (result.getStatus() == SSLEngineResult.Status.OK) {
                hs = result.getHandshakeStatus();
                while (hs == HandshakeStatus.NEED_TASK) {
                    engine.getDelegatedTask().run();
                    hs = engine.getHandshakeStatus();
                }
            } else {
                throw new SSLException(result.getStatus().toString() + " result from wrap operation during handshake");
            }
        } while (hs == HandshakeStatus.NEED_UNWRAP);
    }

    private void produceNetBuffer(ByteBuffer net, ByteBuffer app) throws SSLException {
        net.clear();
        while (hs == HandshakeStatus.NEED_WRAP) {
            SSLEngineResult result = engine.wrap(app, net);
            if (result.getStatus() != SSLEngineResult.Status.OK) {
                throw new SSLException(result.getStatus().toString() + " result from wrap operation during handshake");
            }
            hs = result.getHandshakeStatus();
        }
    }

    @Override
    public void close() throws IOException {
        engine.closeOutbound();
        channel.close();
    }

    @Override
    public Future<Integer> read(final ByteBuffer buffero) {
        final ByteBuffer tempo = ByteBuffer.allocateDirect(32768);
        final Future<Integer> futureResult = channel.read(tempo);

        return new Future<Integer>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return futureResult.cancel(mayInterruptIfRunning);
            }

            @Override
            public boolean isCancelled() {
                return futureResult.isCancelled();
            }

            @Override
            public boolean isDone() {
                return futureResult.isDone();
            }

            @Override
            public Integer get() throws InterruptedException, ExecutionException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Integer get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                Integer numBytes = futureResult.get(3, TimeUnit.SECONDS);
                tempo.flip();
                while (tempo.hasRemaining()) {
                    try {
                        SSLEngineResult res = engine.unwrap(tempo, buffero);
                        if (res.getStatus() != Status.OK) {
                            System.err.println(res.getStatus().toString() + " while unwrapping application data");
                            return -1;
                        }
                    } catch (SSLException ex) {
                        System.err.println(ex.getMessage() + " while unwrapping application data");
                        return -1;
                    }
                }
                buffero.flip();
                return buffero.remaining();
            }
        };
    }

    @Override
    public <A> void read(ByteBuffer dst, A attachment, CompletionHandler<Integer, ? super A> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <A> void write(ByteBuffer src, A attachment, CompletionHandler<Integer, ? super A> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Future<Integer> write(ByteBuffer src) {
        ByteBuffer dst;
        if (src.hasRemaining()) {
            dst = ByteBuffer.allocateDirect(16921);
            try {
                SSLEngineResult res = engine.wrap(src, dst);
                if (res.getStatus() != Status.OK) {
                    dst.clear();
                }
            } catch (SSLException ex) {
                dst.clear();
                ex.printStackTrace(System.out);
            }
        } else {
            dst = ByteBuffer.allocateDirect(0);
        }
        dst.flip();
        return channel.write(dst);
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }
}
