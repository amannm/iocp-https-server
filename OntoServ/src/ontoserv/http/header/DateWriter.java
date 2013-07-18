/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontoserv.http.header;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import ontoserv.http.util.constants.ASCII;

/**
 *
 * @author Administrator
 */
public class DateWriter extends HttpHeaderWriter { 

    private static final SimpleDateFormat httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    static {
        httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    private static final byte[] headerStart;
    static {
        try {
            headerStart = "cache-control: ".getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
    public DateWriter(ByteBuffer buffer) {
        super(buffer);
    }

    @Override
    public void configure() {
    }

    @Override
    public void write() {
        buffer.put(headerStart);
        try {
            buffer.put(httpDateFormat.format(new Date()).getBytes("US-ASCII"));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        buffer.put(ASCII.CRLF);
    }
}
