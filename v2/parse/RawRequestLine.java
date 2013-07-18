/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parse;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 *
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
                            //who the hell would use these?)
                        }
                    }
                } else {
                    if (ch >= 65) {
                        if (ch <= 90) {
                            //uppercase letter
                            //frequency(2nd) Most clients capitalize the first letter of every field name as well as the first letter that follows any internal hyphens
                            source.put(source.position() - 1, (byte) (ch | 0b00010000));
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
                                    //used in some commonly occuring headers but rarely used more than once per header 
                                } else {
                                    if (ch == 13) {
                                        if (source.hasRemaining() && source.get() == 10) {
                                            //CRLF (after CRLF or LF)
                                            //frequency(5th) must occur once per message
                                            return map;
                                        } else {
                                            return null;
                                            //cant use CR by itself here
                                        }
                                    } else {
                                        //
                                        // ~ The Obscurity Zone ~
                                        //
                                        if (ch >= 48) {
                                            if (ch <= 57) {
                                                //digits
                                                //frequency(6th) used in some obscure non-standard headers like Cookies2:
                                            } else {
                                                //
                                                //valid but obscure characters above 57
                                                //
                                                if (ch >= 94) {
                                                    //underscore, carat, grave
                                                    //Note: 97 and above already handled
                                                } else {
                                                    return null;
                                                    //not a token
                                                }
                                            }
                                        } else {
                                            //
                                            //valid, but obscure characters below 48 (not hyphen)
                                            //
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
                                                            //not a token
                                                            return null;
                                                        }
                                                    }
                                                } else {
                                                    if (ch == 33) {
                                                        //exclamation point (heh)
                                                    } else {
                                                        //not a token
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
}
