/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v1.http.util.constants;

import java.io.UnsupportedEncodingException;

/*
 OCTET          = <any 8-bit sequence of data>
 CHAR           = <any US-ASCII character (octets 0 - 127)>
 UPALPHA        = <any US-ASCII uppercase letter "A".."Z">
 LOALPHA        = <any US-ASCII lowercase letter "a".."z">
 ALPHA          = UPALPHA | LOALPHA
 DIGIT          = <any US-ASCII digit "0".."9">
 CTL            = <any US-ASCII control character (octets 0 - 31) and DEL (127)>
 CR             = <US-ASCII CR, carriage return (13)>
 LF             = <US-ASCII LF, linefeed (10)>
 SP             = <US-ASCII SP, space (32)>
 HT             = <US-ASCII HT, horizontal-tab (9)>
 <">            = <US-ASCII double-quote mark (34)>
       
 CRLF           = CR LF
 LWS            = [CRLF] 1*( SP | HT )
 TEXT           = <any OCTET except CTLs, but including LWS>
 HEX            = "A" | "B" | "C" | "D" | "E" | "F" | "a" | "b" | "c" | "d" | "e" | "f" | DIGIT
       
       
 token          = 1*<any CHAR except CTLs or separators>
 separators     = "(" | ")" | "<" | ">" | "@" | "," | ";" | ":" |
 * "\" | <"> | "/" | "[" | "]" | "?" | "=" | "{" | "}" | SP | HT
       
 comment        = "(" *( ctext | quoted-pair | comment ) ")"
 ctext          = <any TEXT excluding "(" and ")">
       
 quoted-string  = ( <"> *(qdtext | quoted-pair ) <"> )
 qdtext         = <any TEXT except <">>
       
 quoted-pair    = "\" CHAR
 * 
 * 9 hor tab
 * 32 space
 * 
 * 34 doublequote
 * 
 * 40 left paren
 * 41 right paren
 * 
 * 44 comma
 * 
 * 47 forward slash
 * 
 * 58 colon
 * 59 semicolon
 * 60 less than
 * 61 equals
 * 62 greater than
 * 63 question
 * 64 at
 * 
 * --  
 * 91 l brack                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
 * 92 backslash
 * 93 r brack
 * 
 * --
 * 123 left brach
 * --
 * 125 right braces
 * 
 * 
 * --
 * 127 del
 * 
 * 
 * 
 * 33 exclamation
 * --
 * 35 number sign
 * 36 dollar
 * 37 percent
 * 38 ampersand
 * 39 single quote
 * --
 * 42 asterisk
 * 43 plus


 * 45 hyphen
 * 46 period
 * 
 * 48-57 digit
 * 
 * 65-90 uppercase letters
 * 
 * 94 carat
 * 95 underscore
 * 96 grave
 * 97-122 lowercase chars
 * 
 * 124 bar
 * 
 * 126 tilde
 * 
 *
 */
public class ASCII {

    public static final byte G = 71;
    public static final byte E = 69;
    public static final byte T = 84;
    public static final byte P = 80;
    public static final byte O = 79;
    public static final byte S = 83;
    public static final byte U = 85;
    public static final byte H = 72;
    public static final byte A = 65;
    public static final byte D = 68;
    public static final byte L = 76;
    public static final byte R = 82;
    public static final byte C = 67;
    public static final byte CR = 13;
    public static final byte LF = 10;
    public static final byte SP = 32;
    public static final byte HT = 9;
    public static final byte Comma = 44;
    public static final byte Colon = 58;
    public static final byte[] CRLF = new byte[]{CR, LF};
    public static final byte[] CommaSP = new byte[]{Comma, SP};
    public static final byte I = 73;
    public static final byte N = 78;
    public static final byte One = 49;
    public static final byte Period = 46;
    public static final byte Zero = 48;
    public static final byte Slash = 47;
    public static final byte Z = 90;
    public static final byte Equals = 61;
    public static final byte[] HourInSeconds;
    public static final byte[] DayInSeconds;
    public static final byte[] WeekInSeconds;
    public static final byte[] MonthInSeconds;
    public static final byte[] YearInSeconds;

    public static boolean isUppercaseLetter(byte ch) {
        return ch > 64 && ch < 91;
    }

    public static byte[] valueOf(int n) {
        if (n > 999999999) {
            byte[] arr = new byte[10];
            arr[9] = (byte) (48 + (n % 10));
            n /= 10;
            arr[8] = (byte) (48 + (n % 10));
            n /= 10;
            arr[7] = (byte) (48 + (n % 10));
            n /= 10;
            arr[6] = (byte) (48 + (n % 10));
            n /= 10;
            arr[5] = (byte) (48 + (n % 10));
            n /= 10;
            arr[4] = (byte) (48 + (n % 10));
            n /= 10;
            arr[3] = (byte) (48 + (n % 10));
            n /= 10;
            arr[2] = (byte) (48 + (n % 10));
            n /= 10;
            arr[1] = (byte) (48 + (n % 10));
            n /= 10;
            arr[0] = (byte) (48 + n);
            return arr;
        }
        if (n > 99999999) {
            byte[] arr = new byte[9];
            arr[8] = (byte) (48 + (n % 10));
            n /= 10;
            arr[7] = (byte) (48 + (n % 10));
            n /= 10;
            arr[6] = (byte) (48 + (n % 10));
            n /= 10;
            arr[5] = (byte) (48 + (n % 10));
            n /= 10;
            arr[4] = (byte) (48 + (n % 10));
            n /= 10;
            arr[3] = (byte) (48 + (n % 10));
            n /= 10;
            arr[2] = (byte) (48 + (n % 10));
            n /= 10;
            arr[1] = (byte) (48 + (n % 10));
            n /= 10;
            arr[0] = (byte) (48 + n);
            return arr;
        }
        if (n > 9999999) {
            byte[] arr = new byte[8];
            arr[7] = (byte) (48 + (n % 10));
            n /= 10;
            arr[6] = (byte) (48 + (n % 10));
            n /= 10;
            arr[5] = (byte) (48 + (n % 10));
            n /= 10;
            arr[4] = (byte) (48 + (n % 10));
            n /= 10;
            arr[3] = (byte) (48 + (n % 10));
            n /= 10;
            arr[2] = (byte) (48 + (n % 10));
            n /= 10;
            arr[1] = (byte) (48 + (n % 10));
            n /= 10;
            arr[0] = (byte) (48 + n);
            return arr;
        }
        if (n > 999999) {
            byte[] arr = new byte[7];
            arr[6] = (byte) (48 + (n % 10));
            n /= 10;
            arr[5] = (byte) (48 + (n % 10));
            n /= 10;
            arr[4] = (byte) (48 + (n % 10));
            n /= 10;
            arr[3] = (byte) (48 + (n % 10));
            n /= 10;
            arr[2] = (byte) (48 + (n % 10));
            n /= 10;
            arr[1] = (byte) (48 + (n % 10));
            n /= 10;
            arr[0] = (byte) (48 + n);
            return arr;
        }
        if (n > 99999) {
            byte[] arr = new byte[6];
            arr[5] = (byte) (48 + (n % 10));
            n /= 10;
            arr[4] = (byte) (48 + (n % 10));
            n /= 10;
            arr[3] = (byte) (48 + (n % 10));
            n /= 10;
            arr[2] = (byte) (48 + (n % 10));
            n /= 10;
            arr[1] = (byte) (48 + (n % 10));
            n /= 10;
            arr[0] = (byte) (48 + n);
            return arr;
        }
        if (n > 9999) {
            byte[] arr = new byte[5];
            arr[4] = (byte) (48 + (n % 10));
            n /= 10;
            arr[3] = (byte) (48 + (n % 10));
            n /= 10;
            arr[2] = (byte) (48 + (n % 10));
            n /= 10;
            arr[1] = (byte) (48 + (n % 10));
            n /= 10;
            arr[0] = (byte) (48 + n);
            return arr;
        }
        if (n > 999) {
            byte[] arr = new byte[4];
            arr[3] = (byte) (48 + (n % 10));
            n /= 10;
            arr[2] = (byte) (48 + (n % 10));
            n /= 10;
            arr[1] = (byte) (48 + (n % 10));
            n /= 10;
            arr[0] = (byte) (48 + n);
            return arr;
        }
        if (n > 99) {
            byte[] arr = new byte[3];
            arr[2] = (byte) (48 + (n % 10));
            n /= 10;
            arr[1] = (byte) (48 + (n % 10));
            n /= 10;
            arr[0] = (byte) (48 + n);
            return arr;
        }
        if (n > 9) {
            byte[] arr = new byte[2];
            arr[1] = (byte) (48 + (n % 10));
            n /= 10;
            arr[0] = (byte) (48 + n);
            return arr;
        }
        byte[] arr = new byte[1];
        arr[0] = (byte) (48 + n);
        return arr;
    }

    static {
        try {
            HourInSeconds = "3600".getBytes(StandardCharsets.US_ASCII);
            DayInSeconds = "86400".getBytes(StandardCharsets.US_ASCII);
            WeekInSeconds = "604800".getBytes(StandardCharsets.US_ASCII);
            MonthInSeconds = "2592000".getBytes(StandardCharsets.US_ASCII);
            YearInSeconds = "31536000".getBytes(StandardCharsets.US_ASCII);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }

    }
}
