/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v2.oauth;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author admin
 */
public class AsynchronousFacebookAccessTokenRequest {

    private SSLEngineResult.HandshakeStatus hs;
    private SSLContext sslContext;

    //open connection
    //do handshake
    //
    private static class HttpsServer implements Runnable {

        private SSLEngine engine;
        private final AsynchronousChannelGroup pool;
        private final int port;
        private SSLEngineResult.HandshakeStatus hs;
        private static byte[] requestHead;
        private static byte[] requestBody;
        private static byte[] codeKey;
        private static byte[] redirectURIKey;
        private static byte[] urlEncodedRedirectURI;


        static {
            String encodedAuthorizationString = "";
            String encodedRedirectUri = "https%3A%2F%2Fwww.example.com%2Fauthorization%2Ffacebook";
            requestHead =
                    ("POST /oauth/access_token HTTP/1.1\r\n"
                            + "Host: graph.facebook.com\r\n"
                            + "Authorization: Basic " + encodedAuthorizationString + "\r\n"
                            + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\r\n").getBytes(StandardCharsets.US_ASCII);
            requestBody =
                    ("&grant_type=authorization_code"
                            + "redirect_uri=" + encodedRedirectUri
                            + "&code=").getBytes(StandardCharsets.US_ASCII);
        }

        public HttpsServer(AsynchronousChannelGroup pool, int port, SSLEngine engine) {
            this.pool = pool;
            this.port = port;
            this.engine = engine;
            this.engine.setUseClientMode(true);
            this.engine.setEnableSessionCreation(true);
        }

        private void consumeNetBuffer(ByteBuffer net, ByteBuffer app) throws SSLException {
            do {
                SSLEngineResult result = engine.unwrap(net, app);
                if (result.getStatus() == SSLEngineResult.Status.OK) {
                    hs = result.getHandshakeStatus();
                    while (hs == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                        engine.getDelegatedTask().run();
                        hs = engine.getHandshakeStatus();
                    }
                } else {
                    throw new SSLException(result.getStatus().toString() + " result from wrap operation during handshake");
                }
            } while (hs == SSLEngineResult.HandshakeStatus.NEED_UNWRAP);
        }

        private void produceNetBuffer(ByteBuffer net, ByteBuffer app) throws SSLException {
            net.clear();
            while (hs == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
                SSLEngineResult result = engine.wrap(app, net);
                if (result.getStatus() != SSLEngineResult.Status.OK) {
                    throw new SSLException(result.getStatus().toString() + " result from wrap operation during handshake");
                }
                hs = result.getHandshakeStatus();
            }
        }

        @Override
        public void run() {
            try (AsynchronousSocketChannel channel = AsynchronousSocketChannel.open(pool)) {
                Future<Void> future = channel.connect(new InetSocketAddress(port));
                try {
                    ByteBuffer app = ByteBuffer.allocateDirect(32768);
                    ByteBuffer net = ByteBuffer.allocateDirect(32768);
                    engine.beginHandshake();
                    produceNetBuffer(net, app);
                    net.flip();
                    future.get(3, TimeUnit.SECONDS);
                    Future<Integer> clientHelloFuture = channel.write(net);
                    if (clientHelloFuture.get(3, TimeUnit.SECONDS) > 0) {
                        //ClientHello ---->
                        net.compact();
                        net.clear();
                        Future<Integer> serverHelloFuture = channel.read(net);
                        if (serverHelloFuture.get(3, TimeUnit.SECONDS) > 0) {
                            //<---- ServerHello/Certificate/ServerHelloDone (ServerHello/ChangeCipherSpec/Finished)
                            net.flip();
                            consumeNetBuffer(net, app);
                            net.compact();
                            net.clear();
                            produceNetBuffer(net, app);
                            net.flip();
                            Future<Integer> keyExchangeOut = channel.write(net);
                            if (keyExchangeOut.get(3, TimeUnit.SECONDS) > 0) {
                                //ClientKeyExchange/ChangeCipherSpec/Finished (ChangeCipherSpec/Finished/HTTP-Request) ---->
                                net.compact();
                                net.clear();
                                Future<Integer> keyExchangeIn = channel.read(net);
                                if (keyExchangeIn.get(3, TimeUnit.SECONDS) > 0) {
                                    //<---- ChangeCipherSpec/Finished (HTTP-Response)
                                    net.flip();
                                    consumeNetBuffer(net, app);
                                    net.compact();
                                    net.clear();
                                }
                            }
                        }
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                }
            } catch (IOException ex) {
            }

        }
    }
}
