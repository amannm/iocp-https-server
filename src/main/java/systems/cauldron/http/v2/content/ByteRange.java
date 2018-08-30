/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v2.content;

import java.util.ArrayList;

/**
 * @author admin
 */
public class ByteRange {

    private final int start;
    private final int end;

    /*
     ranges-specifier = byte-ranges-specifier
     byte-ranges-specifier = bytes-unit "=" byte-range-set
     byte-range-set  = 1#( byte-range-spec | suffix-byte-range-spec )
     byte-range-spec = first-byte-pos "-" [last-byte-pos]
     first-byte-pos  = 1*DIGIT
     last-byte-pos   = 1*DIGIT
     */
    public static ByteRange[] parse(String input) {
        String[] parts = null;
        if (input.length() > 7 && input.charAt(0) == 'b'
                && input.charAt(1) == 'y'
                && input.charAt(2) == 't'
                && input.charAt(3) == 'e'
                && input.charAt(4) == 's'
                && input.charAt(5) == '=') {
            parts = input.substring(6).split(",");
        }
        ArrayList<ByteRange> byteRanges = new ArrayList<>();
        for (String s : parts) {
            String[] result = s.split("-");
            switch (result.length) {
                case 1:
                    byteRanges.add(new ByteRange(Integer.parseInt(parts[0])));
                case 2:
                    byteRanges.add(new ByteRange(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            }
        }
        return (ByteRange[]) byteRanges.toArray();
    }

    public ByteRange(final int start, final int end) {
        this.end = end;
        this.start = start;
    }

    /*
     suffix-byte-range-spec = "-" suffix-length
     suffix-length = 1*DIGIT
     */
    public ByteRange(final int end) {
        this.start = -1;
        this.end = end;
    }
}
