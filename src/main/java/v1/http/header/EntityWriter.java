/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v1.http.header;

import v1.http.util.constants.ASCII;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

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
        Allow = "allow: ".getBytes(StandardCharsets.US_ASCII);
        ContentEncoding = "content-encoding: ".getBytes(StandardCharsets.US_ASCII);
        ContentLanguage = "content-language: ".getBytes(StandardCharsets.US_ASCII);
        ContentLength = "content-length: ".getBytes(StandardCharsets.US_ASCII);
        ContentLocation = "content-location: ".getBytes(StandardCharsets.US_ASCII);
        ContentMD5 = "content-md5: ".getBytes(StandardCharsets.US_ASCII);
        ContentRange = "content-range: ".getBytes(StandardCharsets.US_ASCII);
        ContentType = "content-type: ".getBytes(StandardCharsets.US_ASCII);
        ContentExpires = "content-expires: ".getBytes(StandardCharsets.US_ASCII);
        LastModified = "last-modified: ".getBytes(StandardCharsets.US_ASCII);
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
