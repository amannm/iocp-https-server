package systems.cauldron.http.util;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.nio.charset.StandardCharsets;

/**
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
        access_control_allow_origin = "Access-Control-Allow-Origin: ".getBytes(StandardCharsets.US_ASCII);
        accept_ranges = "Accept-Ranges: ".getBytes(StandardCharsets.US_ASCII);
        age = "Age: ".getBytes(StandardCharsets.US_ASCII);
        allow = "Allow: ".getBytes(StandardCharsets.US_ASCII);
        cache_control = "Cache-Control: ".getBytes(StandardCharsets.US_ASCII);
        connection = "Connection: ".getBytes(StandardCharsets.US_ASCII);
        content_encoding = "Content-Encoding: ".getBytes(StandardCharsets.US_ASCII);
        content_language = "Content-Language: ".getBytes(StandardCharsets.US_ASCII);
        content_length = "Content-Length: ".getBytes(StandardCharsets.US_ASCII);
        content_location = "Content-Location: ".getBytes(StandardCharsets.US_ASCII);
        content_md5 = "Content-Type: ".getBytes(StandardCharsets.US_ASCII);
        content_disposition = "Content-Disposition: ".getBytes(StandardCharsets.US_ASCII);
        content_range = "Content-Range: ".getBytes(StandardCharsets.US_ASCII);
        content_type = "Content-Type: ".getBytes(StandardCharsets.US_ASCII);
        date = "Date: ".getBytes(StandardCharsets.US_ASCII);
        etag = "ETag: ".getBytes(StandardCharsets.US_ASCII);
        expires = "Expires: ".getBytes(StandardCharsets.US_ASCII);
        last_modified = "Last-Modified: ".getBytes(StandardCharsets.US_ASCII);
        link = "Link: ".getBytes(StandardCharsets.US_ASCII);
        location = "Location: ".getBytes(StandardCharsets.US_ASCII);
        p3p = "P3P: ".getBytes(StandardCharsets.US_ASCII);
        pragma = "Pragma: ".getBytes(StandardCharsets.US_ASCII);
        proxy_authenticate = "Proxy-Authenticate: ".getBytes(StandardCharsets.US_ASCII);
        refresh = "Refresh: ".getBytes(StandardCharsets.US_ASCII);
        retry_after = "Retry-After: ".getBytes(StandardCharsets.US_ASCII);
        server = "Server: ".getBytes(StandardCharsets.US_ASCII);
        set_cookie = "Set-Cookie: ".getBytes(StandardCharsets.US_ASCII);
        strict_transport_security = "Strict-Transport-Security: ".getBytes(StandardCharsets.US_ASCII);
        trailer = "Trailer: ".getBytes(StandardCharsets.US_ASCII);
        transfer_encoding = "Transfer-Encoding: ".getBytes(StandardCharsets.US_ASCII);
        vary = "Vary: ".getBytes(StandardCharsets.US_ASCII);
        via = "Via: ".getBytes(StandardCharsets.US_ASCII);
        warning = "Warning: ".getBytes(StandardCharsets.US_ASCII);
        www_authenticate = "WWW-Authenticate: ".getBytes(StandardCharsets.US_ASCII);
    }
}
