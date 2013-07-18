/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parse;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class HttpDate {
        public static final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    static {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    
    public static byte[] getDateBytes() {
        try {
            return format.format(new Date()).getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }
}
