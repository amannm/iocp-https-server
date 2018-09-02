package systems.cauldron.http.v2.parse;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 */
public class RawRequestLine {

    private ByteBuffer method;
    private ByteBuffer path;
    private ByteBuffer protocol;
    private ByteBuffer source;

    public RawRequestLine(final ByteBuffer source) {
        this.source = source;
        method = source.duplicate();
        while (source.hasRemaining()) {
            byte ch = source.get();
            if (ch != ASCII.CR && ch != ASCII.LF) {
                method.position(source.position() - 1);
                break;
            }
        }
        while (source.hasRemaining()) {
            byte ch = source.get();
            if (ch == ASCII.SP) {
                method.limit(source.position() - 1);
                break;
            }
        }

        //*(SP / HR) request-uri SP
        path = source.duplicate();
        while (source.hasRemaining()) {
            byte ch = source.get();
            if (ch != ASCII.SP && ch != ASCII.HT) {
                path.position(source.position() - 1);
                break;
            }
        }
        while (source.hasRemaining()) {
            byte ch = source.get();
            if (ch == ASCII.SP) {
                path.limit(source.position() - 1);
                break;
            }
        }

        protocol = source.duplicate();
        while (source.hasRemaining()) {
            byte ch = source.get();
            if (ch != ASCII.SP && ch != ASCII.HT) {
                protocol.position(source.position() - 1);
                break;
            }
        }
        while (source.hasRemaining()) {
            byte ch = source.get();
            if (ch == ASCII.LF) {
                protocol.limit(source.position() - 1);
                break;
            } else {
                if (ch == ASCII.CR && source.hasRemaining() && source.get() == ASCII.LF) {
                    protocol.limit(source.position() - 2);
                    break;
                }
            }
        }
    }

    public ByteBuffer getPath() {
        return path;
    }

    public ByteBuffer getMethod() {
        return method;
    }

    public ByteBuffer getProtocol() {
        return protocol;
    }

    public Map<byte[], ByteBuffer> parseHeaders() {
        Map<byte[], ByteBuffer> map = new HashMap<>();
        do {
            ByteBuffer fieldName = source.duplicate();
            while (source.hasRemaining()) {
                byte ch = source.get();
                if (ch >= 97) {
                    if (ch <= 122) {
                        //lowercase letter
                        //frequency(1st) the majority of characters in a field name by far
                    } else {
                        if (ch == 126 || ch == 124) {
                            //tilde, bar
                        } else {
                            return null;
                        }
                    }
                } else {
                    if (ch >= 65) {
                        if (ch <= 90) {
                            //uppercase letter
                            //frequency(2nd) Most clients capitalize the first letter of every field name as well as the first letter that follows any internal hyphens
                            source.put(source.position() - 1, (byte) Character.toLowerCase(ch));
                        } else {
                            if (ch >= 94) {
                                //underscore, carat, grave
                            } else {
                                return null;
                            }
                        }
                    } else {
                        if (ch == 58) {
                            //colon
                            //frequency(3rd) Always at least one in each header line
                            fieldName.limit(source.position() - 1);
                            break;
                        } else {
                            if (ch == 45) {
                                //hyphen
                                //frequency(4th)
                                //used in some commonly occurring headers but rarely used more than once per header
                            } else {
                                if (ch == 13) {
                                    if (source.hasRemaining() && source.get() == 10) {
                                        //CRLF (after CRLF or LF)
                                        //frequency(5th) must occur once per message
                                        return map;
                                    } else {
                                        //cant use CR by itself here
                                        return null;
                                    }
                                } else {
                                    if (ch >= 48) {
                                        if (ch <= 57) {
                                            //digits
                                            //frequency(6th) used in some obscure non-standard headers like Cookies2:
                                        } else {
                                            return null;
                                        }
                                    } else {
                                        //characters below 48
                                        if (ch == 10) {
                                            //LF (after CRLF or LF)
                                            //frequency(6th) probably like 99% clients use CRLF properly
                                            return map;
                                        } else {
                                            if (ch >= 35) {
                                                if (ch <= 39) {
                                                    //number sign, dollar sign, percent, ampersand, singlequote
                                                } else {
                                                    if (ch == 46 || ch == 43 || ch == 42) {
                                                        //period, plus, asterisk
                                                    } else {
                                                        return null;
                                                    }
                                                }
                                            } else {
                                                if (ch == 33) {
                                                    //exclamation point (heh)
                                                } else {
                                                    return null;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ByteBuffer fieldValue = source.duplicate();
            while (source.hasRemaining()) {
                byte ch = source.get();
                if (ch != ASCII.SP && ch != ASCII.HT) {
                    fieldValue.position(source.position() - 1);
                    break;
                }
            }
            while (source.hasRemaining()) {
                byte ch = source.get();
                if (ch == ASCII.LF) {
                    fieldValue.limit(source.position() - 1);
                    byte[] key = new byte[fieldName.remaining()];
                    fieldName.get(key);
                    map.put(key, fieldValue);
                    break;
                } else {
                    if (ch == ASCII.CR && source.hasRemaining() && source.get() == ASCII.LF) {
                        fieldValue.limit(source.position() - 2);
                        byte[] key = new byte[fieldName.remaining()];
                        fieldName.get(key);
                        map.put(key, fieldValue);
                        break;
                    }
                }
            }
        } while (source.hasRemaining());
        return map;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("  [").append(StandardCharsets.US_ASCII.decode(getMethod())).append("]-").append("[").append(StandardCharsets.US_ASCII.decode(getPath())).append("]-").append("[").append(StandardCharsets.US_ASCII.decode(getProtocol())).append("]\n");
        Map<byte[], ByteBuffer> headers = parseHeaders();
        for (Map.Entry<byte[], ByteBuffer> entry : headers.entrySet()) {
            builder.append("  [").append(new String(entry.getKey(), StandardCharsets.US_ASCII)).append("]->").append("[").append(StandardCharsets.US_ASCII.decode(entry.getValue())).append("]\n");
        }
        return builder.toString();
    }
}
