/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v2.parse;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author admin
 */
public class HttpDate {
    public static final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

    static {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public static byte[] getDateBytes() {
        return format.format(new Date()).getBytes(StandardCharsets.US_ASCII);
    }
}
