package systems.cauldron.http.handler;

import systems.cauldron.http.util.ResponseBuilder;
import systems.cauldron.http.v2.parse.RawRequestLine;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Amann on 5/27/2015.
 */
public class HttpRedirectHandler implements Consumer<AsynchronousSocketChannel> {

    private static final Logger LOG = Logger.getLogger(HttpRedirectHandler.class.getName());

    private final String redirectHost;

    public HttpRedirectHandler(String redirectHost) {
        this.redirectHost = redirectHost;
    }

    @Override
    public void accept(AsynchronousSocketChannel channel) {
        try {
            LOG.info(channel.getLocalAddress() + " << new connection: " + channel.getRemoteAddress());
            ByteBuffer in = ByteBuffer.allocateDirect(4096);
            Integer bytesRead = channel.read(in).get(3, TimeUnit.SECONDS);
            if (bytesRead != -1) {
                in.flip();
                RawRequestLine line = new RawRequestLine(in);
                ByteBuffer bb = ByteBuffer.allocateDirect(4096);
                ResponseBuilder.create(bb)
                        .setStatus(301)
                        .withDate()
                        .withRedirect(redirectHost, line.getPath())
                        .build();
                channel.write(bb).get(3, TimeUnit.SECONDS);
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "handler encountered exception", ex);
        }
    }
}