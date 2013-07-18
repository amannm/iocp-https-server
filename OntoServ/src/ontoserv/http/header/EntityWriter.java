/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontoserv.http.header;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import ontoserv.http.util.constants.ASCII;

/**
 *
 * @author Administrator
 */
public class EntityWriter {

    public EntityWriter(ByteBuffer buffer) {
    }
    
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

    @Override
    public void configure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void write() {
        buffer.put(headerStart);
        configure();
        buffer.put(ASCII.CRLF);
    }
}
