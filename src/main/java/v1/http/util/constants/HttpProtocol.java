/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v1.http.util.constants;

import java.nio.charset.StandardCharsets;

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
        this.byteValue = s.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] getBytes() {
        return this.byteValue;
    }
}
