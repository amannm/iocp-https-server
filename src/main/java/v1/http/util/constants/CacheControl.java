package v1.http.util.constants;

import java.nio.charset.StandardCharsets;

public class CacheControl {

    private static final byte[] nameBytes;

    static {
        nameBytes = "cache-control".getBytes(StandardCharsets.US_ASCII);
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
            this.byteValue = s.getBytes(StandardCharsets.US_ASCII);
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
            this.byteValue = s.getBytes(StandardCharsets.US_ASCII);
        }

        public byte[] getBytes() {
            return this.byteValue;
        }
    }

    public void appendValue(int n) {

    }
}
