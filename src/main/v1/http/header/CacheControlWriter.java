/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v1.http.header;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import v1.http.util.constants.ASCII;

/**
 *
 * @author Administrator
 */
public abstract class CacheControlWriter extends HttpHeaderWriter{

    private static final byte[] HeaderStart;
    private static final byte[] Public;
    private static final byte[] Private;
    private static final byte[] NoCache;
    private static final byte[] NoStore;
    private static final byte[] NoTransform;
    private static final byte[] MaxAge;
    private static final byte[] SharedMaxAge;
    private static final byte[] MustRevalidate;
    private static final byte[] ProxyRevalidate;
    private static final byte[] MaxStale;
    private static final byte[] MinFresh;
    private static final byte[] OnlyIfCached;

    static {
        try {
            HeaderStart = "cache-control: ".getBytes("US-ASCII");

            MaxAge = "max-age=".getBytes("US-ASCII");
            SharedMaxAge = "s-maxage=".getBytes("US-ASCII");

            MustRevalidate = "must-revalidate".getBytes("US-ASCII");
            ProxyRevalidate = "proxy-revalidate".getBytes("US-ASCII");

            MaxStale = "max-stale".getBytes("US-ASCII");
            MinFresh = "min-fresh=".getBytes("US-ASCII");
            OnlyIfCached = "only-if-cached".getBytes("US-ASCII");

            Public = "public".getBytes("US-ASCII");
            Private = "private".getBytes("US-ASCII");

            NoCache = "no-cache".getBytes("US-ASCII");
            NoStore = "no-store".getBytes("US-ASCII");
            NoTransform = "no-transform".getBytes("US-ASCII");

        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public CacheControlWriter(ByteBuffer buffer) {
        super(buffer);
    }

    @Override
    public void write() {
        buffer.put(HeaderStart);
        buffer.position(buffer.position() - 2);
        buffer.put(ASCII.CRLF);
    }

    protected void putMaxAge(int duration) {
        buffer.put(MaxAge).put(ASCII.valueOf(duration)).put(ASCII.CommaSP);
    }

    protected void putSharedMaxAge(int duration) {
        buffer.put(SharedMaxAge).put(ASCII.valueOf(duration)).put(ASCII.CommaSP);
    }

    protected void putMinFresh(int duration) {
        buffer.put(MinFresh).put(ASCII.valueOf(duration)).put(ASCII.CommaSP);
    }

    protected void putMaxStale() {
        buffer.put(MaxStale).put(ASCII.CommaSP);
    }

    protected void putMaxStale(int duration) {
        buffer.put(MaxStale).put(ASCII.valueOf(duration)).put(ASCII.CommaSP);
    }

    protected void putOnlyIfCached() {
        buffer.put(OnlyIfCached).put(ASCII.CommaSP);
    }

    protected void putPublic() {
        buffer.put(Public).put(ASCII.CommaSP);
    }

    protected void putPrivate() {
        buffer.put(Private).put(ASCII.CommaSP);
    }

    protected void putNoCache() {
        buffer.put(NoCache).put(ASCII.CommaSP);
    }

    protected void putNoStore() {
        buffer.put(NoStore).put(ASCII.CommaSP);
    }

    protected void putNoTransform() {
        buffer.put(NoTransform).put(ASCII.CommaSP);
    }

    protected void putMustRevalidate() {
        buffer.put(MustRevalidate).put(ASCII.CommaSP);
    }

    protected void putProxyRevalidate() {
        buffer.put(ProxyRevalidate).put(ASCII.CommaSP);
    }
}
