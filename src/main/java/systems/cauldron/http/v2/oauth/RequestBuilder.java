/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v2.oauth;

import systems.cauldron.http.v2.parse.ASCII;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


/**
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
        protocol = "HTTP/1.1".getBytes(StandardCharsets.US_ASCII);
        bearerAuthorization = "Bearer ".getBytes(StandardCharsets.US_ASCII);
        basicAuthorization = "Basic ".getBytes(StandardCharsets.US_ASCII);
        basicAuthorizationToken = "fka0w94fkw049fkaw09fkwa049f".getBytes(StandardCharsets.US_ASCII);
    }

    public RequestBuilder(byte[] method, byte[] host, byte[] path) {
        this.source.put(method).put(ASCII.SP).put(path).put(ASCII.SP).put(protocol).put(ASCII.CRLF);
        //this.source.put(http.constants.HttpRequestHeader.host).put(host).put(ASCII.CRLF);
    }

    public void putBasicAuthorization() {
        //source.put(http.constants.HttpRequestHeader.authorization).put(basicAuthorization).put(basicAuthorizationToken).put(v2.parse.ASCII.CRLF);

    }
}
