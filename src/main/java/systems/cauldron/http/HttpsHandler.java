package systems.cauldron.http;

import systems.cauldron.http.util.ResponseBuilder;
import systems.cauldron.http.v2.parse.RawRequestLine;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Amann on 5/27/2015.
 */

public class HttpsHandler implements Consumer<AsynchronousSocketChannel> {

    private static final Logger LOG = Logger.getLogger(HttpsHandler.class.getName());

    private final byte[] html;

    public HttpsHandler(String html) {
        this.html = html.getBytes(StandardCharsets.US_ASCII);
    }

    @Override
    public void accept(AsynchronousSocketChannel channel) {
        try {
            SslProcessor processor = new SslProcessor(channel);
            if (processor.receive()) {
                ByteBuffer buffer = processor.getBuffer();
                RawRequestLine rrl = new RawRequestLine(buffer);
                LOG.info(rrl.toString());
                buffer.clear();
                ResponseBuilder.create(buffer)
                        .setStatus(200)
                        .withDate()
                        .withConnectionClose()
                        .withDefaultContentType()
                        .build(html);
                processor.transmit();
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "channel encountered exception", ex);
        }
    }
}