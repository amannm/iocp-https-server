/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oauth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;

/**
 *
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

        //https://www.facebook.com/dialog/oauth?
        //display=popup
//&domain=www.quora.com
//&scope=email%2Cpublish_actions
//&api_key=136609459636
//&app_id=136609459636
//&locale=en_US
//&sdk=joey
//&client_id=136609459636
//&redirect_uri=http%3A%2F%2Fstatic.ak.facebook.com%2Fconnect%2Fxd_arbiter.php%3Fversion%3D17%23cb%3Dffeb7f2d%26origin%3Dhttp%253A%252F%252Fwww.quora.com%252Ff345a3dde4%26domain%3Dwww.quora.com%26relation%3Dopener%26frame%3Df3b282ebd&origin=1
//&response_type=token%2Csigned_request
        static {
            try {
                requestHead =
                        ("POST /oauth/access_token HTTP/1.1\r\n"
                        + "Host: graph.facebook.com\r\n"
                        + "Authorization: Basic czZCaGRSa3F0MzpnWDFmQmF0M2JW\r\n"
                        + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\r\n").getBytes("US-ASCII");
                requestBody =
                        ("&grant_type=authorization_code"
                        + "redirect_uri=https%3A%2F%2Fwww.ontopad.com%2Fauthorization%2Ffacebook"
                        + "&code=").getBytes("US-ASCII");

            } catch (UnsupportedEncodingException ex) {
            }
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
