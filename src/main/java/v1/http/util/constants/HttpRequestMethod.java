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
public enum HttpRequestMethod {
    GET("GET"), 
    POST("POST"), 
    HEAD("HEAD"), 
    PUT("PUT"), 
    DELETE("DELETE"), 
    OPTIONS("OPTIONS"), 
    TRACE("TRACE"), 
    CONNECT("CONNECT"), 
    PATCH("PATCH");
    private final byte[] byteValue;

    private HttpRequestMethod(final String s) {
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
