package systems.cauldron.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    private ExecutorService handlers;
    private AsynchronousChannelGroup workers;
    private CountDownLatch readyLatch;

    public Server() {
        handlers = Executors.newFixedThreadPool(2);
        try {
            workers = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(16));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        readyLatch = new CountDownLatch(2);
    }


    public void start(String hostname, int httpPort, int httpsPort, String content) throws InterruptedException {
        handlers.execute(() -> run(httpPort, new HttpRedirectHandler("https://" + hostname + ":" + httpsPort)));
        handlers.execute(() -> run(httpsPort, new HttpsHtmlHandler(content)));
        readyLatch.await(5L, TimeUnit.SECONDS);
    }

    public void stop() {
        workers.shutdown();
        handlers.shutdown();
    }

    private void run(int port, Consumer<AsynchronousSocketChannel> handler) {
        LOG.info("starting TCP/IP listener on local port " + port);
        try (final AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(workers)) {
            server.bind(new InetSocketAddress(port));
            readyLatch.countDown();
            while (true) {
                Future<AsynchronousSocketChannel> futureChannel = server.accept();
                try (final AsynchronousSocketChannel channel = futureChannel.get()) {
                    LOG.info(channel.getLocalAddress() + " << new connection: " + channel.getRemoteAddress());
                    handler.accept(channel);
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "channel encountered exception", ex);
                }
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "server encountered exception", ex);
        }
    }


}
