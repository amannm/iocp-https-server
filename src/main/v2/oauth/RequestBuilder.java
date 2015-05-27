/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v2.oauth;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import HttpRequestHeader;
import v2.parse.ASCII;


/**
 *
 * @author admin
 */
public class RequestBuilder {

    private ByteBuffer source = ByteBuffer.allocateDirect(4096);
    private static byte[] protocol;
    private static byte[] bearerAuthorization;
    private static byte[] basicAuthorization;
    private static byte[] username;
    private static byte[] password;
    
    private static byte[] basicAuthorizationToken;

    static {
        try {
            protocol = "HTTP/1.1".getBytes("US-ASCII");
            bearerAuthorization = "Bearer ".getBytes("US-ASCII");
            basicAuthorization =  "Basic ".getBytes("US-ASCII");
            basicAuthorizationToken = "fka0w94fkw049fkaw09fkwa049f".getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public RequestBuilder(byte[] method, byte[] host, byte[] path) {
        this.source.put(method).put(ASCII.SP).put(path).put(ASCII.SP).put(protocol).put(ASCII.CRLF);
        this.source.put(HttpRequestHeader.host).put(host).put(ASCII.CRLF);
    }
    
    public void putBasicAuthorization() {
        source.put(HttpRequestHeader.authorization).put(basicAuthorization).put(basicAuthorizationToken).put(v2.parse.ASCII.CRLF);
        
    }
}
