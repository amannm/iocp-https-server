package systems.cauldron.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    private static ExecutorService exe;
    private static AsynchronousChannelGroup group;

    static {
        exe = Executors.newFixedThreadPool(4);
        try {
            group = AsynchronousChannelGroup.withThreadPool(exe);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void start() {
        exe.execute(() -> run(8080, new HttpRedirectHandler("https://localhost:8181")));
        exe.execute(() -> run(8181, new HttpsHtmlHandler("<!doctype html><html><head><title>Hello World</title></head><body>Hey</body></html>")));
    }

    private static void run(int port, Consumer<AsynchronousSocketChannel> handler) {
        LOG.info("starting TCP/IP listener on local port " + port);
        try (final AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(group)) {
            server.bind(new InetSocketAddress(port));
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
