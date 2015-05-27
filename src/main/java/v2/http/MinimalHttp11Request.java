/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v2.http;

import v2.parse.HttpProtocol;

/**
 *
 * @author admin
 */
public class MinimalHttp11Request {

    private HttpRequestMethod method;
    private String path;
    private HttpProtocol protocol;
    private String host;

    public HttpRequestMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public HttpProtocol getProtocol() {
        return protocol;
    }
    
    public String getHost() {
        return host;
    }
}
