package systems.cauldron.http.util;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import systems.cauldron.http.v2.parse.ASCII;
import systems.cauldron.http.v2.parse.HttpDate;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author admin
 */
public class ResponseBuilder {

    private final ByteBuffer source;
    private static byte[] defaultContentType;
    private static byte[] defaultConnection;

    static {
        defaultContentType = "text/html; charset=UTF-8".getBytes(StandardCharsets.US_ASCII);
        defaultConnection = "close".getBytes(StandardCharsets.US_ASCII);
        defaultConnection = "keep-alive".getBytes(StandardCharsets.US_ASCII);
    }

    private ResponseBuilder(final ByteBuffer source) {
        this.source = source;
    }

    public static ResponseBuilder create(ByteBuffer source) {
        return new ResponseBuilder(source);
    }

    public ResponseBuilder setStatus(int status) {
        source.put(HttpStatus.status[status]);
        return this;
    }

    public ResponseBuilder withRedirect(String host, ByteBuffer path) {
        source.put(HttpResponseHeader.location).put(host.getBytes(StandardCharsets.US_ASCII)).put(path).put(ASCII.CRLF);
        return this;
    }

    public ResponseBuilder withConnectionClose() {
        source.put(HttpResponseHeader.connection).put(defaultConnection).put(ASCII.CRLF);
        return this;
    }

    public ResponseBuilder withConnectionKeepAlive() {
        source.put(HttpResponseHeader.connection).put(defaultConnection).put(ASCII.CRLF);
        return this;
    }

    public ResponseBuilder withDefaultContentType() {
        source.put(HttpResponseHeader.content_type).put(defaultContentType).put(ASCII.CRLF);
        return this;
    }

    public ResponseBuilder withDate() {
        source.put(HttpResponseHeader.date).put(HttpDate.getDateBytes()).put(ASCII.CRLF);
        return this;
    }

    public void build(byte[] content) {
        source.put(HttpResponseHeader.content_length).put(String.valueOf(content.length).getBytes(StandardCharsets.US_ASCII)).put(ASCII.CRLF);
        source.put(ASCII.CRLF);
        source.put(content);
        source.flip();
    }

    public void build() {
        source.put(ASCII.CRLF);
        source.flip();
    }
}
