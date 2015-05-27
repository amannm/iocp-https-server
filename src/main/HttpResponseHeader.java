/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.UnsupportedEncodingException;

/**
 *
 * @author admin
 */
public class HttpResponseHeader {

    public static byte[] access_control_allow_origin;
    public static byte[] accept_ranges;
    public static byte[] age;
    public static byte[] allow;
    public static byte[] cache_control;
    public static byte[] connection;
    public static byte[] content_encoding;
    public static byte[] content_language;
    public static byte[] content_length;
    public static byte[] content_location;
    public static byte[] content_md5;
    public static byte[] content_disposition;
    public static byte[] content_range;
    public static byte[] content_type;
    public static byte[] date;
    public static byte[] etag;
    public static byte[] expires;
    public static byte[] last_modified;
    public static byte[] link;
    public static byte[] location;
    public static byte[] p3p;
    public static byte[] pragma;
    public static byte[] proxy_authenticate;
    public static byte[] refresh;
    public static byte[] retry_after;
    public static byte[] server;
    public static byte[] set_cookie;
    public static byte[] strict_transport_security;
    public static byte[] trailer;
    public static byte[] transfer_encoding;
    public static byte[] vary;
    public static byte[] via;
    public static byte[] warning;
    public static byte[] www_authenticate;

    static {
        try {
            access_control_allow_origin = "Access-Control-Allow-Origin: ".getBytes("US-ASCII");
            accept_ranges = "Accept-Ranges: ".getBytes("US-ASCII");
            age = "Age: ".getBytes("US-ASCII");
            allow = "Allow: ".getBytes("US-ASCII");
            cache_control = "Cache-Control: ".getBytes("US-ASCII");
            connection = "Connection: ".getBytes("US-ASCII");
            content_encoding = "Content-Encoding: ".getBytes("US-ASCII");
            content_language = "Content-Language: ".getBytes("US-ASCII");
            content_length = "Content-Length: ".getBytes("US-ASCII");
            content_location = "Content-Location: ".getBytes("US-ASCII");
            content_md5 = "Content-Type: ".getBytes("US-ASCII");
            content_disposition = "Content-Disposition: ".getBytes("US-ASCII");
            content_range = "Content-Range: ".getBytes("US-ASCII");
            content_type = "Content-Type: ".getBytes("US-ASCII");
            date = "Date: ".getBytes("US-ASCII");
            etag = "ETag: ".getBytes("US-ASCII");
            expires = "Expires: ".getBytes("US-ASCII");
            last_modified = "Last-Modified: ".getBytes("US-ASCII");
            link = "Link: ".getBytes("US-ASCII");
            location = "Location: ".getBytes("US-ASCII");
            p3p = "P3P: ".getBytes("US-ASCII");
            pragma = "Pragma: ".getBytes("US-ASCII");
            proxy_authenticate = "Proxy-Authenticate: ".getBytes("US-ASCII");
            refresh = "Refresh: ".getBytes("US-ASCII");
            retry_after = "Retry-After: ".getBytes("US-ASCII");
            server = "Server: ".getBytes("US-ASCII");
            set_cookie = "Set-Cookie: ".getBytes("US-ASCII");
            strict_transport_security = "Strict-Transport-Security: ".getBytes("US-ASCII");
            trailer = "Trailer: ".getBytes("US-ASCII");
            transfer_encoding = "Transfer-Encoding: ".getBytes("US-ASCII");
            vary = "Vary: ".getBytes("US-ASCII");
            via = "Via: ".getBytes("US-ASCII");
            warning = "Warning: ".getBytes("US-ASCII");
            www_authenticate = "WWW-Authenticate: ".getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
