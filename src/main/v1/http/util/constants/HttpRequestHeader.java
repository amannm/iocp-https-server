/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v1.http.util.constants;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public enum HttpRequestHeader {

    Accept("accept"),
    AcceptCharset("accept-charset"),
    AcceptEncoding("accept-encoding"),
    AcceptLanguage("accept-language"),
    AcceptDateTime("accept-datetime"),
    Authorization("authorization"),
    CacheControl("cache-control"),
    Connection("connection"),
    Cookie("cookie"),
    ContentLength("content-length"),
    ContentMD5("content-md5"),
    ContentType("content-type"),
    Date("date"),
    Expect("expect"),
    From("from"),
    Host("host"),
    IfMatch("if-match"),
    IfModifiedSince("if-modified-since"),
    IfNoneMatch("if-none-match"),
    IfRange("if-range"),
    IfUnmodifiedSince("if-unmodified-since"),
    MaxForwards("max-forwards"),
    Pragma("pragma"),
    ProxyAuthorization("proxy-authorization"),
    Range("range"),
    Referer("referer"),
    TE("te"),
    Upgrade("upgrade"),
    UserAgent("user-agent"),
    Warning("via"),
    warning("warning");
    
    private final byte[] byteValue;

    private HttpRequestHeader(final String s) {
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
