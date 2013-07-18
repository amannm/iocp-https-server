/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontoserv.http.header;

import java.nio.ByteBuffer;
import ontoserv.http.util.constants.ASCII;

/**
 *
 * @author Administrator
 */
public abstract class HttpHeaderWriter {

    protected ByteBuffer buffer;
    protected byte[] separator;
    protected HttpHeaderWriter(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public void write() {
        writeLineStart();
        writeValue();
        
        buffer.position(buffer.position() - 2);
        buffer.put(ASCII.CRLF);
        writeLineEnd();
    }

    public abstract void writeLineStart();
    public abstract void writeValue();
    public abstract void writeLineEnd();
}
