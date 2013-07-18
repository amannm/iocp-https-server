/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontoserv.http.util.constants;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author admin
 */
public enum HttpResponseHeader {

    AccessControlAllowOrigin("access-control-allow-origin"),
    AcceptRanges("accept-ranges"),
    Age("age"),
    Allow("allow"),
    CacheControl("cache-control"),
    Connection("connection"),
    ContentEncoding("content-encoding"),
    ContentLanguage("content-language"),
    ContentLength("content-length"),
    ContentLocation("content-location"),
    ContentMD5("content-md5"),
    ContentDisposition("content-disposition"),
    ContentRange("content-range"),
    ContentType("content-type"),
    Date("date"),
    Etag("etag"),
    Expires("expires"),
    LastModified("last-modified"),
    Link("link"),
    Location("location"),
    P3P("p3p"),
    Pragma("pragma"),
    ProxyAuthenticate("proxy-authenticate"),
    Refresh("refresh"),
    RetryAfter("retry-after"),
    Server("server"),
    SetCookie("set-cookie"),
    StrictTransportSecurity("strict-transport-security"),
    Trailer("trailer"),
    TransferEncoding("transfer-encoding"),
    Vary("vary"),
    Via("via"),
    Warning("warning"),
    WWWAuthenticate("www-authenticate");
    private final byte[] byteValue;

    private HttpResponseHeader(final String s) {
        try {
            this.byteValue = s.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public byte[] getBytes() {
        return this.byteValue;
    }
}
