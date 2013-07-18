/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontoserv.connection;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;

/**
 *
 * @author admin
 */
public interface ProtocolChannel extends AsynchronousByteChannel {
    public void init(ProtocolChannel superChannel);
    public void log(String text);
}
