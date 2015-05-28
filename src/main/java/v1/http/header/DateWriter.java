/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v1.http.header;

import v1.http.util.constants.ASCII;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
        headerStart = "cache-control: ".getBytes(StandardCharsets.US_ASCII);
    }
    public DateWriter(ByteBuffer buffer) {
        super(buffer);
    }



    @Override
    public void write() {
        buffer.put(headerStart);
        buffer.put(httpDateFormat.format(new Date()).getBytes(StandardCharsets.US_ASCII));
        buffer.put(ASCII.CRLF);
    }

    @Override
    public void writeLineStart() {

    }

    @Override
    public void writeValue() {

    }

    @Override
    public void writeLineEnd() {

    }
}
