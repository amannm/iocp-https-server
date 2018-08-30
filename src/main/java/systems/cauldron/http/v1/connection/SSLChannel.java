/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v1.connection;

import systems.cauldron.http.v1.http.util.constants.SSLConstants;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class SSLChannel implements ProtocolChannel {

    private static final Logger logger = Logger.getLogger("SSL Handler");

    private ProtocolChannel superChannel;
    private ProtocolChannel subChannel;

    public SSLChannel(ProtocolChannel subChannel) {
        this.subChannel = subChannel;
    }

    private SSLEngine engine;
    private ByteBuffer netInbound;
    private ByteBuffer netOutbound;

    @Override
    public void init(final ProtocolChannel superChannel) {
        this.superChannel = superChannel;
        engine = SSLConstants.sslContext.createSSLEngine();
        engine.setUseClientMode(false);
        engine.setEnableSessionCreation(true);
        netInbound = ByteBuffer.allocate(32768);
        netOutbound = ByteBuffer.allocate(32768);
        final ByteBuffer appInbound = ByteBuffer.allocate(16384);
        final ByteBuffer appOutbound = ByteBuffer.allocate(16384);
        superChannel.read(netInbound, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer result, Void attachment) {
                log("READ (" + result + ")");
                if (result == -1) {
                    log("Unexpected EOF reached. Closing connection");
                    try {
                        superChannel.close();
                    } catch (IOException ex) {
                        log("Exception: " + ex.toString());
                    }
                } else {
                    netInbound.flip();
                    try {
                        SSLEngineResult res = engine.unwrap(netInbound, appInbound);
                        HandshakeStatus hs = res.getHandshakeStatus();
                        log("UNWRAP (" + res.bytesConsumed() + ")");
                        while (true) {
                            switch (hs) {
                                case NEED_UNWRAP:
                                    if (netInbound.hasRemaining()) {
                                        res = engine.unwrap(netInbound, appInbound);
                                        hs = res.getHandshakeStatus();
                                        log("UNWRAP (" + res.bytesConsumed() + ")");
                                        break;
                                    } else {
                                        if (netOutbound.remaining() != netOutbound.capacity()) {
                                            netOutbound.flip();
                                            superChannel.write(netOutbound, this, new CompletionHandler<Integer, CompletionHandler<Integer, Void>>() {
                                                @Override
                                                public void completed(Integer result, CompletionHandler<Integer, Void> attachment) {
                                                    log("WRITE (" + result + ")");
                                                    netOutbound.clear();
                                                    netInbound.clear();
                                                    superChannel.read(netInbound, null, attachment);
                                                }

                                                @Override
                                                public void failed(Throwable exc, CompletionHandler<Integer, Void> attachment) {
                                                    log("Exception: " + exc.toString());
                                                }
                                            });
                                            return;
                                        } else {
                                            netInbound.clear();
                                            superChannel.read(netInbound, null, this);
                                            return;
                                        }
                                    }
                                case NEED_WRAP:
                                    res = engine.wrap(appOutbound, netOutbound);
                                    hs = res.getHandshakeStatus();
                                    log("WRAP (" + res.bytesProduced() + ")");
                                    break;
                                case NEED_TASK:
                                    Runnable task = engine.getDelegatedTask();
                                    if (task != null) {
                                        task.run();
                                    }
                                    hs = engine.getHandshakeStatus();
                                    log("TASK");
                                    break;
                                case FINISHED:
                                    if (netOutbound.remaining() != netOutbound.capacity()) {
                                        netOutbound.flip();
                                        log("WRITE (FINISHED)");
                                        superChannel.write(netOutbound, null, new CompletionHandler<Integer, Void>() {
                                            @Override
                                            public void completed(Integer result, Void attachment) {
                                                log("Finished full handshake");
                                                netOutbound.clear();
                                                netInbound.clear();
                                                subChannel.init(SSLChannel.this);
                                            }

                                            @Override
                                            public void failed(Throwable exc, Void attachment) {
                                                log("Exception: " + exc.toString());
                                            }
                                        });
                                        return;
                                    } else {
                                        log("Finished abbreviated handshake");
                                        netInbound.compact();
                                        subChannel.init(SSLChannel.this);
                                        return;
                                    }
                                case NOT_HANDSHAKING:
                                    throw new SSLException("NOT HANDSHAKING");
                            }
                        }
                    } catch (SSLException exc) {
                        log("Exception: " + exc.toString());
                    }
                }
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                log("Exception: " + exc.toString());
            }
        });
    }

    @Override
    public <A> void read(final ByteBuffer dst, final A attachment, final CompletionHandler<Integer, ? super A> handler) {
        if (netInbound.position() != 0) {
            netInbound.flip();
            try {
                SSLEngineResult rs = engine.unwrap(netInbound, dst);
                netInbound.compact();
                if (rs.getStatus() == Status.OK) {
                    handler.completed(rs.bytesConsumed(), attachment);
                } else {
                    log(rs.getStatus().toString());
                }
            } catch (SSLException ex) {
                log("Exception during read: " + ex.toString());
            }
        } else {
            superChannel.read(netInbound, null, new CompletionHandler<Integer, Void>() {
                @Override
                public void completed(Integer result, Void nothing) {
                    if (result == -1) {
                        handler.completed(-1, attachment);
                    } else {
                        netInbound.flip();
                        try {
                            SSLEngineResult rs = engine.unwrap(netInbound, dst);
                            netInbound.compact();
                            if (rs.getStatus() == Status.OK) {
                                handler.completed(rs.bytesConsumed(), attachment);
                            } else {
                                log(rs.getStatus().toString());
                            }
                        } catch (SSLException ex) {
                            log("Exception during read: " + ex.toString());
                        }
                    }
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    log("Exception during read: " + exc.toString());
                }
            });
        }
    }

    @Override
    public <A> void write(final ByteBuffer src, final A attachment, final CompletionHandler<Integer, ? super A> handler) {
        netOutbound.clear();
        try {
            final SSLEngineResult rs = engine.wrap(src, netOutbound);
            netOutbound.flip();
            superChannel.write(netOutbound, null, new CompletionHandler<Integer, Void>() {
                @Override
                public void completed(Integer result, Void nothing) {
                    netOutbound.clear();
                    handler.completed(rs.bytesProduced(), attachment);
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    log("Exception: " + exc.toString());
                }
            });
        } catch (SSLException ex) {
            log("Exception: " + ex.toString());
        }
    }

    @Override
    public Future<Integer> read(ByteBuffer dst) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Future<Integer> write(ByteBuffer src) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() throws IOException {
        engine.closeOutbound();
        superChannel.close();
    }

    @Override
    public boolean isOpen() {
        return superChannel.isOpen();
    }

    @Override
    public void log(String text) {
        superChannel.log(engine.getSession().getProtocol() + " > " + text);
    }
}