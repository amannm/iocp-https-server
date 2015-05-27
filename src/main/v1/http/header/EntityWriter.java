/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v1.http.header;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import v1.http.util.constants.ASCII;

/**
 *
 * @author Administrator
 */
public class EntityWriter extends HttpHeaderWriter {


    private static final byte[] Allow;
    private static final byte[] ContentEncoding;
    private static final byte[] ContentLanguage;
    private static final byte[] ContentLength;
    private static final byte[] ContentLocation;
    private static final byte[] ContentMD5;
    private static final byte[] ContentRange;
    private static final byte[] ContentType;
    private static final byte[] ContentExpires;
    private static final byte[] LastModified;
    static {
        try {
            Allow = "allow: ".getBytes("US-ASCII");
            ContentEncoding = "content-encoding: ".getBytes("US-ASCII");
            ContentLanguage = "content-language: ".getBytes("US-ASCII");
            ContentLength = "content-length: ".getBytes("US-ASCII");
            ContentLocation = "content-location: ".getBytes("US-ASCII");
            ContentMD5 = "content-md5: ".getBytes("US-ASCII");
            ContentRange = "content-range: ".getBytes("US-ASCII");
            ContentType = "content-type: ".getBytes("US-ASCII");
            ContentExpires = "content-expires: ".getBytes("US-ASCII");
            LastModified = "last-modified: ".getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected EntityWriter(ByteBuffer buffer) {
        super(buffer);
    }


    @Override
    public void write() {
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
