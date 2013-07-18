/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontoserv.http.util.constants;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Administrator
 */
public enum HttpProtocol {

    HTTP11("HTTP/1.1"),
    HTTP10("HTTP/1.0"),
    HTTP09("HTTP/1.0");
    private final byte[] byteValue;

    private HttpProtocol(final String s) {
        try {
            this.byteValue = s.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public byte[] getBytes() {
        return this.byteValue;
    }
}
