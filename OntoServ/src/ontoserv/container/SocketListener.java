/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontoserv.container;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class SocketListener {

    private static final Logger logger = Logger.getGlobal();
    private String name;
    private InetSocketAddress address;
    private AsynchronousServerSocketChannel server;
    private AsynchronousChannelGroup group;
    private Map<UUID, CompletionHandler<AsynchronousSocketChannel, ?>> handlerMap;

    public SocketListener(String name, InetSocketAddress address, AsynchronousChannelGroup group) throws IOException {
        this.name = name;
        this.address = address;
        this.group = group;
        this.server = AsynchronousServerSocketChannel.open(group);
        this.handlerMap = new HashMap<>();
    }

    public UUID registerListener(CompletionHandler<AsynchronousSocketChannel, ? extends Object> handler) {
        UUID uuid = UUID.randomUUID();
        handlerMap.put(uuid, handler);
        return uuid;
    }

    public void unregisterListener(UUID uuid) {
        handlerMap.remove(uuid);
    }

    public void start() throws IOException {
        server.bind(address);
        for (CompletionHandler handler : handlerMap.values()) {
            server.accept(server, handler);
        }
        logger.log(Level.INFO, "{0} @ {1} - Started", new Object[]{name, address});
    }

    public void stop() throws IOException {
        server.close();
        logger.log(Level.INFO, "{0} @ {1} - Stopped", new Object[]{name, address});
    }

    public void restart() throws IOException {
        server.close();
        server = AsynchronousServerSocketChannel.open(group);
        logger.log(Level.INFO, "{0} @ {1} - Restarted", new Object[]{name, address});
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public AsynchronousChannelGroup getGroup() {
        return group;
    }

    public void setGroup(AsynchronousChannelGroup group) {
        this.group = group;
    }
}
