/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v1.http.header;

import v1.http.util.constants.ASCII;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author Administrator
 */
public abstract class CacheControlWriter extends HttpHeaderWriter {

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
        HeaderStart = "cache-control: ".getBytes(StandardCharsets.US_ASCII);

        MaxAge = "max-age=".getBytes(StandardCharsets.US_ASCII);
        SharedMaxAge = "s-maxage=".getBytes(StandardCharsets.US_ASCII);

        MustRevalidate = "must-revalidate".getBytes(StandardCharsets.US_ASCII);
        ProxyRevalidate = "proxy-revalidate".getBytes(StandardCharsets.US_ASCII);

        MaxStale = "max-stale".getBytes(StandardCharsets.US_ASCII);
        MinFresh = "min-fresh=".getBytes(StandardCharsets.US_ASCII);
        OnlyIfCached = "only-if-cached".getBytes(StandardCharsets.US_ASCII);

        Public = "public".getBytes(StandardCharsets.US_ASCII);
        Private = "private".getBytes(StandardCharsets.US_ASCII);

        NoCache = "no-cache".getBytes(StandardCharsets.US_ASCII);
        NoStore = "no-store".getBytes(StandardCharsets.US_ASCII);
        NoTransform = "no-transform".getBytes(StandardCharsets.US_ASCII);
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
