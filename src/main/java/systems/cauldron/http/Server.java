package systems.cauldron.http;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class Server {

    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    private ExecutorService listenerPool;
    private AsynchronousChannelGroup workers;
    private Map<Integer, Consumer<AsynchronousSocketChannel>> handlers;

    public static void main(String[] args) throws IOException, InterruptedException {

        String defaultHostname = "localhost";
        int defaultHttpPort = 8080;
        int defaultHttpsPort = 8181;
        String defaultContent = "<!doctype html><html><head><title>Default Page</title></head><body>Server is running...</body></html>";

        Server defaultServer = new Server();
        defaultServer.bind(defaultHttpPort, new HttpRedirectHandler("https://" + defaultHostname + ":" + defaultHttpsPort));
        defaultServer.bind(defaultHttpsPort, new HttpsHtmlHandler(defaultContent));

        Runtime.getRuntime().addShutdownHook(new Thread(defaultServer::stop));
        defaultServer.start();

    }

    public Server() {
        handlers = new HashMap<>();
    }

    public void bind(int port, Consumer<AsynchronousSocketChannel> handler) {
        if (handlers.containsKey(port)) {
            throw new RuntimeException("handler already registered at port " + port);
        }
        handlers.put(port, handler);
    }

    public void start() throws InterruptedException, IOException {
        workers = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(16));
        listenerPool = Executors.newFixedThreadPool(handlers.size());
        CountDownLatch readyLatch = new CountDownLatch(handlers.size());
        handlers.forEach((port, handler) -> listenerPool.execute(new Listener(workers, readyLatch, port, handler)));
        readyLatch.await(5L, TimeUnit.SECONDS);
    }

    public void stop() {
        workers.shutdown();
        listenerPool.shutdown();
    }


}
