package systems.cauldron.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Listener implements Runnable {

    protected static final Logger LOG = Logger.getLogger(Listener.class.getName());

    private final AsynchronousChannelGroup workers;
    private final CountDownLatch readyLatch;
    private final int port;
    private final Consumer<AsynchronousSocketChannel> handler;

    public Listener(AsynchronousChannelGroup workers, CountDownLatch readyLatch, int port, Consumer<AsynchronousSocketChannel> handler) {
        this.workers = workers;
        this.readyLatch = readyLatch;
        this.port = port;
        this.handler = handler;
    }

    @Override
    public void run() {
        LOG.info("starting TCP/IP listener on local port " + port);
        try (final AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open(workers)) {
            listener.bind(new InetSocketAddress(port));
            readyLatch.countDown();
            listener.accept(null, new CompletionHandler<>() {

                @Override
                public void completed(AsynchronousSocketChannel channel, Object attachment) {
                    listener.accept(null, this);
                    handler.accept(channel);
                }

                @Override
                public void failed(Throwable ex, Object attachment) {
                    LOG.log(Level.SEVERE, "listener encountered exception", ex);
                }
            });
            workers.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException ex) {
            LOG.log(Level.SEVERE, "listener encountered exception", ex);
        }
    }
}
