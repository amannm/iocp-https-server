/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v1.connection;

import java.nio.channels.AsynchronousByteChannel;

/**
 *
 * @author admin
 */
public interface ProtocolChannel extends AsynchronousByteChannel {
    public void init(ProtocolChannel superChannel);
    public void log(String text);
}
