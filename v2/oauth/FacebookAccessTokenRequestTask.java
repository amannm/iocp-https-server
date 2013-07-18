/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oauth;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author admin
 */
public class FacebookAccessTokenRequestTask implements Future<FacebookAccessToken> {

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FacebookAccessToken get() throws InterruptedException, ExecutionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FacebookAccessToken get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
