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
        this.byteValue = s.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] getBytes() {
        return this.byteValue;
    }
}
