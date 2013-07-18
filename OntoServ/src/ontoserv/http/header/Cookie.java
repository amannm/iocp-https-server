/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontoserv.http.header;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Administrator
 */
public class Cookie {

    ByteBuffer buffer;
    private static final SimpleDateFormat httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    static {
        httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    private static final byte[] headerStart;
    private static final byte[] Expires;
    private static final byte[] MaxAge;
    private static final byte[] Domain;
    private static final byte[] Path;
    private static final byte[] Secure;
    private static final byte[] HttpOnly;

    static {
        try {
            headerStart = "Set-Cookie: ".getBytes("US-ASCII");
            Expires = "; Expires=".getBytes("US-ASCII");
            MaxAge = "; Max-Age=".getBytes("US-ASCII");
            Domain = "; Domain=".getBytes("US-ASCII");
            Path = "; Path=".getBytes("US-ASCII");
            Secure = "; Secure".getBytes("US-ASCII");
            HttpOnly = "; HttpOnly".getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void putExpires(Calendar date) {
        buffer.put(Expires).put(date.)
    }
    //cookie-header = "Cookie:" OWS cookie-string OWS
    //cookie-string = cookie-pair *( ";" SP cookie-pair )
    //set-cookie-header = "Set-Cookie:" SP set-cookie-string
    //set-cookie-string = cookie-pair *( ";" SP cookie-av )
    //cookie-pair       = cookie-name "=" cookie-value
    //cookie-name       = token
    //cookie-value      = *cookie-octet / ( DQUOTE *cookie-octet DQUOTE )
    //cookie-octet      = %x21 / %x23-2B / %x2D-3A / %x3C-5B / %x5D-7E
    //cookie-av         = expires-av / max-age-av / domain-av / path-av / secure-av / httponly-av / extension-av
    //expires-av        = "Expires=" rfc1123-date
    //max-age-av        = "Max-Age=" %x31-39 *DIGIT
    //domain-av         = "Domain=" rfc1035-rfc1123-subdomain
    //path-av           = "Path=" <any CHAR except CTLs or ";">
    //secure-av         = "Secure"
    //httponly-av       = "HttpOnly"
    //extension-av      = <any CHAR except CTLs or ";">
}
