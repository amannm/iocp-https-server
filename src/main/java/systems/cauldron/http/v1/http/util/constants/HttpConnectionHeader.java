/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v1.http.util.constants;

import java.nio.charset.StandardCharsets;

/**
 * @author Administrator
 */
public enum HttpConnectionHeader {

    KeepAlive("keep-alive"),
    Close("close");
    private final byte[] byteValue;

    private HttpConnectionHeader(final String s) {
        this.byteValue = s.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] getBytes() {
        return this.byteValue;
    }
}
