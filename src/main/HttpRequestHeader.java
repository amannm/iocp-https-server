/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
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
        try {
            accept = "accept".getBytes("US-ASCII");
            accept_charset = "accept-charset".getBytes("US-ASCII");
            accept_encoding = "accept-encoding".getBytes("US-ASCII");
            accept_language = "accept-language".getBytes("US-ASCII");
            accept_datetime = "accept-datetime".getBytes("US-ASCII");
            authorization = "authorization".getBytes("US-ASCII");
            cache_control = "cache-control".getBytes("US-ASCII");
            connection = "connection".getBytes("US-ASCII");
            cookie = "cookie".getBytes("US-ASCII");
            content_length = "content-length".getBytes("US-ASCII");
            content_md5 = "content-md5".getBytes("US-ASCII");
            content_type = "content-type".getBytes("US-ASCII");
            date = "date".getBytes("US-ASCII");
            expect = "expect".getBytes("US-ASCII");
            from = "from".getBytes("US-ASCII");
            host = "host".getBytes("US-ASCII");
            if_match = "if-match".getBytes("US-ASCII");
            if_modified_since = "if-modified-since".getBytes("US-ASCII");
            if_none_match = "if-none-match".getBytes("US-ASCII");
            if_range = "if-range".getBytes("US-ASCII");
            if_unmodified_since = "if-unmodified-since".getBytes("US-ASCII");
            max_forwards = "max-forwards".getBytes("US-ASCII");
            pragma = "pragma".getBytes("US-ASCII");
            proxy_authorization = "proxy-authorization".getBytes("US-ASCII");
            range = "range".getBytes("US-ASCII");
            referer = "referer".getBytes("US-ASCII");
            te = "te".getBytes("US-ASCII");
            upgrade = "upgrade".getBytes("US-ASCII");
            user_agent = "user-agent".getBytes("US-ASCII");
            via = "via".getBytes("US-ASCII");
            warning = "warning".getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
