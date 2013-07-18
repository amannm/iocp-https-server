/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontopadsimple;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import parse.ASCII;
import parse.HttpDate;

/**
 *
 * @author admin
 */
public class ResponseBuilder {

    private final ByteBuffer source;
    private static byte[] defaultContentType;
    private static byte[] defaultConnection;
    private static byte[] httpsSiteURI;

    static {
        try {
            httpsSiteURI = "https://www.ontopad.com".getBytes("US-ASCII");
            defaultContentType = "text/html; charset=UTF-8".getBytes("US-ASCII");
            defaultConnection = "close".getBytes("US-ASCII");
            defaultConnection = "keep-alive".getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace(System.err);
        }
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
        try {
            source.put(HttpResponseHeader.content_length).put(ASCII.Colon).put(ASCII.SP).put(String.valueOf(content.length).getBytes("US-ASCII")).put(ASCII.CRLF);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace(System.err);
        }
        source.put(ASCII.CRLF);
        source.put(content);
        source.flip();
    }

    public void build() {
        source.put(ASCII.CRLF);
        source.flip();
    }
}
