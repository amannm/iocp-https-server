/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v1.connection;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class TCPChannel implements ProtocolChannel {

    private ProtocolChannel subChannel;
    private AsynchronousSocketChannel channel;
    private String address;

    public TCPChannel(ProtocolChannel subChannel) {
        this.subChannel = subChannel;
    }

    public void setChannel(AsynchronousSocketChannel channel) {
        this.channel = channel;
        try {
            address = channel.getRemoteAddress().toString();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public void init(ProtocolChannel channel) {
        subChannel.init(this);
    }

    @Override
    public <A> void read(ByteBuffer dst, A attachment, CompletionHandler<Integer, ? super A> handler) {
        channel.read(dst, 3, TimeUnit.SECONDS, attachment, handler);
    }

    @Override
    public Future<Integer> read(ByteBuffer dst) {
        return channel.read(dst);
    }

    @Override
    public <A> void write(ByteBuffer src, A attachment, CompletionHandler<Integer, ? super A> handler) {
        channel.write(src, 3, TimeUnit.SECONDS, attachment, handler);
    }

    @Override
    public Future<Integer> write(ByteBuffer src) {
        return channel.write(src);
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    @Override
    public void log(String text) {
        System.out.println(address + " > " + text);
    }
}
