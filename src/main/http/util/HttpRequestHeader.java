package http.util;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.nio.charset.StandardCharsets;

/**
 * @author admin
 */
public class HttpRequestHeader {

    public static byte[] accept;
    public static byte[] accept_charset;
    public static byte[] accept_encoding;
    public static byte[] accept_language;
    public static byte[] accept_datetime;
    public static byte[] authorization;
    public static byte[] cache_control;
    public static byte[] connection;
    public static byte[] cookie;
    public static byte[] content_length;
    public static byte[] content_md5;
    public static byte[] content_type;
    public static byte[] date;
    public static byte[] expect;
    public static byte[] from;
    public static byte[] host;
    public static byte[] if_match;
    public static byte[] if_modified_since;
    public static byte[] if_none_match;
    public static byte[] if_range;
    public static byte[] if_unmodified_since;
    public static byte[] max_forwards;
    public static byte[] pragma;
    public static byte[] proxy_authorization;
    public static byte[] range;
    public static byte[] referer;
    public static byte[] te;
    public static byte[] upgrade;
    public static byte[] user_agent;
    public static byte[] via;
    public static byte[] warning;

    static {
        accept = "accept".getBytes(StandardCharsets.US_ASCII);
        accept_charset = "accept-charset".getBytes(StandardCharsets.US_ASCII);
        accept_encoding = "accept-encoding".getBytes(StandardCharsets.US_ASCII);
        accept_language = "accept-language".getBytes(StandardCharsets.US_ASCII);
        accept_datetime = "accept-datetime".getBytes(StandardCharsets.US_ASCII);
        authorization = "authorization".getBytes(StandardCharsets.US_ASCII);
        cache_control = "cache-control".getBytes(StandardCharsets.US_ASCII);
        connection = "connection".getBytes(StandardCharsets.US_ASCII);
        cookie = "cookie".getBytes(StandardCharsets.US_ASCII);
        content_length = "content-length".getBytes(StandardCharsets.US_ASCII);
        content_md5 = "content-md5".getBytes(StandardCharsets.US_ASCII);
        content_type = "content-type".getBytes(StandardCharsets.US_ASCII);
        date = "date".getBytes(StandardCharsets.US_ASCII);
        expect = "expect".getBytes(StandardCharsets.US_ASCII);
        from = "from".getBytes(StandardCharsets.US_ASCII);
        host = "host".getBytes(StandardCharsets.US_ASCII);
        if_match = "if-match".getBytes(StandardCharsets.US_ASCII);
        if_modified_since = "if-modified-since".getBytes(StandardCharsets.US_ASCII);
        if_none_match = "if-none-match".getBytes(StandardCharsets.US_ASCII);
        if_range = "if-range".getBytes(StandardCharsets.US_ASCII);
        if_unmodified_since = "if-unmodified-since".getBytes(StandardCharsets.US_ASCII);
        max_forwards = "max-forwards".getBytes(StandardCharsets.US_ASCII);
        pragma = "pragma".getBytes(StandardCharsets.US_ASCII);
        proxy_authorization = "proxy-authorization".getBytes(StandardCharsets.US_ASCII);
        range = "range".getBytes(StandardCharsets.US_ASCII);
        referer = "referer".getBytes(StandardCharsets.US_ASCII);
        te = "te".getBytes(StandardCharsets.US_ASCII);
        upgrade = "upgrade".getBytes(StandardCharsets.US_ASCII);
        user_agent = "user-agent".getBytes(StandardCharsets.US_ASCII);
        via = "via".getBytes(StandardCharsets.US_ASCII);
        warning = "warning".getBytes(StandardCharsets.US_ASCII);
    }
}
