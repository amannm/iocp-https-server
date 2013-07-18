package ontoserv.http.util.constants;

import java.io.UnsupportedEncodingException;

public class CacheControl {

    private static final byte[] nameBytes;

    static {
        try {
            nameBytes = "cache-control".getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static byte[] getNameBytes() {
        return nameBytes;
    }

    public enum RequestDirective {

        NoCache("no-cache"),
        NoStore("no-store"),
        MaxAge("max-age"),
        MaxStale("max-stale"),
        MinFresh("min-fresh"),
        NoTransform("no-transform"),
        OnlyIfCached("only-if-cached");
        private final byte[] byteValue;

        private RequestDirective(final String s) {
            try {
                this.byteValue = s.getBytes("US-ASCII");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        }

        public byte[] getBytes() {
            return this.byteValue;
        }
    }

    public enum ResponseDirective {

        Public("public"),
        Private("private"),
        NoCache("no-cache"),
        NoStore("no-store"),
        NoTransform("no-transform"),
        MustRevalidate("must-revalidate"),
        ProxyRevalidate("proxy-revalidate"),
        MaxAge("max-age"),
        SharedMaxAge("s-maxage");
        private final byte[] byteValue;

        private ResponseDirective(final String s) {
            try {
                this.byteValue = s.getBytes("US-ASCII");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        }

        public byte[] getBytes() {
            return this.byteValue;
        }
    }

    public void appendValue(int n) {

    }
}
