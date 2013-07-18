/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontopadsimple;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;

/**
 *
 * @author admin
 */
public class DecryptionHandler implements CompletionHandler<Integer, SSLEngineResult> {

    private final SSLEngine engine;
    private final ByteBuffer unencrypted;
    private final ByteBuffer encrypted;

    public DecryptionHandler(final SSLEngine engine, final ByteBuffer encrypted, final ByteBuffer unencrypted) {
        this.engine = engine;
        this.unencrypted = unencrypted;
        this.encrypted = encrypted;
    }

    @Override
    public void completed(Integer bytesRead, SSLEngineResult result) {
        if (bytesRead > 0) {
            do {
                encrypted.flip();
                try {
                    result = engine.unwrap(encrypted, unencrypted);
                } catch (SSLException ex) {
                    ex.printStackTrace(System.err);
                }
                encrypted.compact();
            } while (encrypted.hasRemaining());
        }
    }

    @Override
    public void failed(Throwable exc, SSLEngineResult result) {
        exc.printStackTrace(System.err);
    }
}
