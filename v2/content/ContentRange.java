/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package content;

/*
 Content-Range = "Content-Range" ":" content-range-spec
 content-range-spec      = byte-content-range-spec
 byte-content-range-spec = bytes-unit SP byte-range-resp-spec "/" ( instance-length | "*" )
 byte-range-resp-spec = (first-byte-pos "-" last-byte-pos) | "*"
 instance-length           = 1*DIGIT
 */
public class ContentRange {

    private final ByteRange range;
    private final Integer instanceLength;

    public static ContentRange parse(String input) {
        if (input.length() > 7 && input.charAt(0) == 'b'
                && input.charAt(1) == 'y'
                && input.charAt(2) == 't'
                && input.charAt(3) == 'e'
                && input.charAt(4) == 's'
                && input.charAt(5) == ' ') {
            String[] result = input.substring(7).split("/");
            if (result.length == 2) {
                ByteRange range = null;
                if ("*".equals(result[0])) {
                    range = new ByteRange(0, Integer.MAX_VALUE);
                } else {
                    String[] asdf = result[0].split("-");
                    if (asdf.length == 2) {
                        range = new ByteRange(Integer.parseInt(asdf[0]), Integer.parseInt(asdf[0]));
                    }
                }
                if (range != null) {
                    if ("*".equals(result[1])) {
                        return new ContentRange(range, Integer.MAX_VALUE);
                    } else {
                        return new ContentRange(range, Integer.parseInt(result[1]));
                    }
                }
            }
        }
        return null;
    }

    public ContentRange(final ByteRange range, final Integer instanceLength) {
        this.range = range;
        this.instanceLength = instanceLength;
    }
}
