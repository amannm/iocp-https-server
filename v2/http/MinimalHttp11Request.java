/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import cache.CacheDirective;
import connection.Connection;
import connection.Cookie;
import content.ByteRange;
import content.ContentRange;
import content.Encoding;
import content.ExtensionAdapter;
import content.LanguageTag;
import content.MediaType;
import content.QualityAdapter;
import content.TE;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import parse.HttpProtocol;
import parse.RawHttpRequest;

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
