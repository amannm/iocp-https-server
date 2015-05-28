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
public enum HttpContentCoding {

    gzip("gzip"),
    compress("compress"),
    deflate("deflate"),
    identity("identity"),
    sdhc("sdhc");
    private final byte[] byteValue;

    private HttpContentCoding(final String s) {
        this.byteValue = s.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] getBytes() {
        return this.byteValue;
    }
}
