/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v1.http.util.constants;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Administrator
 */
public enum HttpStrictTransportSecurityHeader {
    MaxAge("max-age"),
    IncludeSubDomains("includesubdomains");    
    private final byte[] byteValue;

    private HttpStrictTransportSecurityHeader(final String s) {
        try {
            this.byteValue = s.getBytes(StandardCharsets.US_ASCII);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public byte[] getBytes() {
        return this.byteValue;
    }
}
