/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v1.http.util;

import java.nio.channels.CompletionHandler;

/**
 * @author Administrator
 */
public abstract class SimpleCompletionHandler implements CompletionHandler<Integer, Void> {

    @Override
    public void failed(Throwable exc, Void attachment) {
        exc.printStackTrace(System.err);
    }
}
