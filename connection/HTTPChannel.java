//package ontoserv.connection;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.channels.CompletionHandler;
//import java.util.concurrent.Future;
//import ontoserv.http.parsing.MethodNameParser;
//
//public class HTTPChannel implements ProtocolChannel {
//
//    private ProtocolChannel channel;
//    private ByteBuffer requestBuffer = ByteBuffer.allocateDirect(4096);
//    
//    @Override
//    public void init(final ProtocolChannel channel) {
//        this.channel = channel;
//        channel.read(requestBuffer, null, new MethodNameParser());
//    }
//
//    @Override
//    public <A> void read(ByteBuffer dst, A attachment, CompletionHandler<Integer, ? super A> handler) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public Future<Integer> read(ByteBuffer dst) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public <A> void write(ByteBuffer src, A attachment, CompletionHandler<Integer, ? super A> handler) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public Future<Integer> write(ByteBuffer src) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void close() throws IOException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public boolean isOpen() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void log(String text) {
//        channel.log(protocol + " > " + method + " > " + path + " > " + text);
//    }
//}
