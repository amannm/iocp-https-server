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

    private static ExecutorService server;
    private static AsynchronousChannelGroup workers;

    static {
        server = Executors.newFixedThreadPool(2);
        try {
            workers = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(16));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static CountDownLatch readyLatch;

    public static void main(String... args) throws InterruptedException {
        readyLatch = new CountDownLatch(2);
        server.execute(() -> run(8080, new HttpRedirectHandler("https://localhost:8181")));
        server.execute(() -> run(8181, new HttpsHtmlHandler("<!doctype html><html><head><title>Hello World</title></head><body>Hey</body></html>")));
        readyLatch.await(5L, TimeUnit.SECONDS);
    }

    public static void start(String host, int httpPort, int httpsPort, String content) throws InterruptedException {
        readyLatch = new CountDownLatch(2);
        server.execute(() -> run(httpPort, new HttpRedirectHandler("https://" + host + ":" + httpsPort)));
        server.execute(() -> run(httpsPort, new HttpsHtmlHandler(content)));
        readyLatch.await(5L, TimeUnit.SECONDS);
    }

    public static void stop() {
        workers.shutdown();
        server.shutdown();
    }

    private static void run(int port, Consumer<AsynchronousSocketChannel> handler) {
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
