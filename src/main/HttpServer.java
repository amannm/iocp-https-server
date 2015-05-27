import v2.parse.RawRequestLine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Amann on 5/27/2015.
 */
public class HttpServer implements Runnable {

    private final AsynchronousChannelGroup pool;
    private final int port;
    private final String html;

    public HttpServer(AsynchronousChannelGroup pool, int port, String html) {
        this.pool = pool;
        this.port = port;
        this.html = html;
    }

    @Override
    public void run() {
        try (final AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(pool)) {
            server.bind(new InetSocketAddress(port));
            System.err.println("HTTP >> Starting TCP/IP listener on local port " + port);
            while (true) {
                Future<AsynchronousSocketChannel> futureChannel = server.accept();
                try (final AsynchronousSocketChannel socketChannel = futureChannel.get()) {
                    System.err.println("HTTP >> Incoming connection from " + socketChannel.getRemoteAddress() + " ... ");
                    ByteBuffer in = ByteBuffer.allocateDirect(4096);
                    Future<Integer> future = socketChannel.read(in);
                    if (future.get(3, TimeUnit.SECONDS) > 0) {
                        System.err.println("HTTP >> Request recieved");
                        in.flip();
                        RawRequestLine line = new RawRequestLine(in);
                        ByteBuffer bb = ByteBuffer.allocateDirect(1024);
                        ResponseBuilder response = new ResponseBuilder(bb);
                        response.setStatus(301);
                        response.putDate();
                        response.putHTTPSRedirect(line.getPath());
                        response.build();
                        future = socketChannel.write(bb);
                        future.get(3, TimeUnit.SECONDS);
                    } else {
                        System.err.println("HTTP >> Empty request recieved");
                    }
                } catch (TimeoutException ex) {
                    System.err.println("HTTP >> Client timed out: " + ex.getMessage());
                } catch (InterruptedException ex) {
                    System.err.println("HTTP >> Connection interrupted: " + ex.getMessage());
                } catch (ExecutionException ex) {
                    System.err.println("HTTP >> Execution Exception: " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.err.println("HTTP >> FATAL EXCEPTION: " + ex.getMessage());
            System.exit(0);
        }
    }
}