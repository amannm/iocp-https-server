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
public enum HttpStrictTransportSecurityHeader {
    MaxAge("max-age"),
    IncludeSubDomains("includesubdomains");    
    private final byte[] byteValue;

    private HttpStrictTransportSecurityHeader(final String s) {
        this.byteValue = s.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] getBytes() {
        return this.byteValue;
    }
}
