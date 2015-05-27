package http.util;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import v2.parse.ASCII;
import v2.parse.HttpDate;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author admin
 */
public class ResponseBuilder {

    private final ByteBuffer source;
    private static byte[] defaultContentType;
    private static byte[] defaultConnection;
    private static byte[] httpsSiteURI;

    static {
        httpsSiteURI = "https://www.test.com".getBytes(StandardCharsets.US_ASCII);
        defaultContentType = "text/html; charset=UTF-8".getBytes(StandardCharsets.US_ASCII);
        defaultConnection = "close".getBytes(StandardCharsets.US_ASCII);
        defaultConnection = "keep-alive".getBytes(StandardCharsets.US_ASCII);
    }

    public ResponseBuilder(final ByteBuffer source) {
        this.source = source;
    }

    public void setStatus(int status) {
        source.put(HttpStatus.status[status]);
    }

    public void putHTTPSRedirect(ByteBuffer path) {
        source.put(HttpResponseHeader.location).put(ASCII.Colon).put(ASCII.SP).put(httpsSiteURI).put(path).put(ASCII.CRLF);
    }

    public void putConnectionClose() {
        source.put(HttpResponseHeader.connection).put(ASCII.Colon).put(ASCII.SP).put(defaultConnection).put(ASCII.CRLF);
    }

    public void putConnectionKeepAlive() {
        source.put(HttpResponseHeader.connection).put(ASCII.Colon).put(ASCII.SP).put(defaultConnection).put(ASCII.CRLF);
    }

    public void putDefaultContentType() {
        source.put(HttpResponseHeader.content_type).put(ASCII.Colon).put(ASCII.SP).put(defaultContentType).put(ASCII.CRLF);

    }

    public void putDate() {
        source.put(HttpResponseHeader.date).put(ASCII.Colon).put(ASCII.SP).put(HttpDate.getDateBytes()).put(ASCII.CRLF);
    }

    public void build(byte[] content) {
        source.put(HttpResponseHeader.content_length).put(ASCII.Colon).put(ASCII.SP).put(String.valueOf(content.length).getBytes(StandardCharsets.US_ASCII)).put(ASCII.CRLF);
        source.put(ASCII.CRLF);
        source.put(content);
        source.flip();
    }

    public void build() {
        source.put(ASCII.CRLF);
        source.flip();
    }
}
