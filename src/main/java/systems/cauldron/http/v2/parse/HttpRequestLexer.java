///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package v2.parse;
//
//import http.HttpHeader;
//import http.HttpRequestMethod;
//import http.HttpRequestHeaderField;
//import java.io.UnsupportedEncodingException;
//import java.nio.ByteBuffer;
//import java.nio.charset.Charset;
//import java.text.DecimalFormat;
//import java.text.NumberFormat;
//import java.text.ParsePosition;
//import java.util.Arrays;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
///**
// *
// * @author admin
// */
//public class HttpRequestLexer {
//
//    private static final int CR = 13;
//    private static final int LF = 10;
//    private static final int SP = 32;
//    private static final int HT = 9;
//    private static final int COLON = 58;
//
//    public static RawHttpRequest process(byte[] data) throws UnsupportedEncodingException {
//
//        //Initialize variables and empty request object
//        RawHttpRequest request = new RawHttpRequest();
//        int limit = data.length;
//        int start = 0;
//
//        //Move start past any preceding CR and LF
//        while (start < limit) {
//            if (data[start] != LF && data[start] != CR) {
//                break;
//            }
//            start++;
//        }
//
//        //Move end to next space character
//        int end = start + 1;
//        while (end < limit) {
//            if (data[end] == SP) {
//                break;
//            }
//            end++;
//        }
//
//        int length = end - start;
//
//        HttpRequestMethod method;
//        switch (length) {
//            case 3:
//                //GET & PUT
//                switch (data[start]) {
//                    case 71:
//                        if (data[start + 1] == 69
//                                && data[start + 2] == 84) {
//                            method = HttpRequestMethod.GET;
//                        } else {
//                            return null;
//                        }
//                        break;
//                    case 80:
//                        if (data[start + 1] == 85
//                                && data[start + 2] == 84) {
//                            method = HttpRequestMethod.PUT;
//                        } else {
//                            return null;
//                        }
//                        break;
//                    default:
//                        return null;
//                }
//                break;
//            case 4:
//                //POST & HEAD
//                switch (data[start]) {
//                    case 80:
//                        if (data[start + 1] == 79
//                                && data[start + 2] == 83
//                                && data[start + 3] == 84) {
//                            method = HttpRequestMethod.POST;
//                        } else {
//                            return null;
//                        }
//                        break;
//                    case 72:
//                        if (data[start + 1] == 69
//                                && data[start + 2] == 65
//                                && data[start + 3] == 68) {
//                            method = HttpRequestMethod.HEAD;
//                        } else {
//                            return null;
//                        }
//                        break;
//                    default:
//                        return null;
//                }
//                break;
//            case 5:
//                //TRACE & PATCH
//                switch (data[start]) {
//                    case 84:
//                        if (data[start + 1] == 82
//                                && data[start + 2] == 65
//                                && data[start + 3] == 67
//                                && data[start + 4] == 69) {
//                            method = HttpRequestMethod.TRACE;
//                        } else {
//                            return null;
//                        }
//                        break;
//                    case 80:
//                        if (data[start + 1] == 65
//                                && data[start + 2] == 84
//                                && data[start + 3] == 67
//                                && data[start + 4] == 72) {
//                            method = HttpRequestMethod.PATCH;
//                        } else {
//                            return null;
//                        }
//                        break;
//                    default:
//                        return null;
//                }
//                break;
//            case 6:
//                //DELETE
//                if (data[start + 1] == 68
//                        && data[start + 2] == 69
//                        && data[start + 3] == 76
//                        && data[start + 4] == 69
//                        && data[start + 5] == 84
//                        && data[start + 6] == 69) {
//                    method = HttpRequestMethod.DELETE;
//                } else {
//                    return null;
//                }
//                break;
//            case 7:
//                //CONNECT & OPTIONS
//                switch (data[start]) {
//                    case 79:
//                        if (data[start + 1] == 80
//                                && data[start + 2] == 84
//                                && data[start + 3] == 73
//                                && data[start + 4] == 79
//                                && data[start + 5] == 78
//                                && data[start + 6] == 83) {
//                            method = HttpRequestMethod.OPTIONS;
//                        } else {
//                            return null;
//                        }
//                        break;
//                    case 67:
//                        if (data[start + 1] == 79
//                                && data[start + 2] == 78
//                                && data[start + 3] == 78
//                                && data[start + 4] == 69
//                                && data[start + 5] == 67
//                                && data[start + 6] == 84) {
//                            method = HttpRequestMethod.CONNECT;
//                        } else {
//                            return null;
//                        }
//                        break;
//                    default:
//                        return null;
//                }
//            default:
//                return null;
//
//        }
//
//
//        //Move start past whitespace
//        start = end + 1;
//        while (start < limit) {
//            if (data[start] != SP && data[start] != HT) {
//                break;
//            }
//            start++;
//        }
//
//        //Move end to next space character
//        end = start + 1;
//        while (end < limit) {
//            if (data[end] == SP) {
//                break;
//            }
//            end++;
//        }
//
//        //Assign Path
//        request.setPath(new String(data, start, end - start, "US-ASCII"));
//
//        //Move start past whitespace
//        start = end + 1;
//        while (start < limit) {
//            if (data[start] != SP && data[start] != HT) {
//                break;
//            }
//            start++;
//        }
//
//        //Move end to next CRLF/LF and extract protocol string
//        end = start;
//        while (end < limit) {
//            if (data[end] == LF) {
//                request.setProtocol(parseProtocolString(data, start, end));
//                break;
//            } else {
//                if (data[end] == CR) {
//                    end++;
//                    if (end < limit) {
//                        if (data[end] == LF) {
//                            request.setProtocol(parseProtocolString(data, start, end));
//                            break;
//                        }
//                    }
//                }
//            }
//            end++;
//        }
//
//        while (end < limit) {
//
//            //Stop parsing headers when a second CRLF / LF occurs immediately after another CRLF / LF
//            start = end + 1;
//            if (start < limit) {
//                if (data[start] == LF) {
//                    break;
//                } else {
//                    if (data[start] == CR) {
//                        start++;
//                        if (start < limit) {
//                            if (data[start] == LF) {
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//
//            //Move end to next colon character
//            end = start;
//            while (end < limit) {
//                if (data[end] == COLON) {
//                    break;
//                }
//                end++;
//            }
//
//            //Extract header field name
//            byte[] headerFieldName = Arrays.copyOfRange(data, start, end);
//
//            //Move start past whitespace
//            start = end + 1;
//            while (start < limit) {
//                if (data[start] != SP && data[start] != HT) {
//                    break;
//                }
//                start++;
//            }
//
//            //Extract header field value and assign header pair to request object
//            end = start;
//            while (end < limit) {
//                if (data[end] == LF) {
//                    request.addHeader(new HttpHeader(headerFieldName, Arrays.copyOfRange(data, start, end)));
//                    break;
//                } else {
//                    if (data[end] == CR) {
//                        end++;
//                        if (end < limit) {
//                            if (data[end] == LF) {
//                                request.addHeader(new HttpHeader(headerFieldName, Arrays.copyOfRange(data, start, end)));
//                                break;
//                            }
//                        }
//                    }
//                }
//                end++;
//            }
//        }
//
//        //Extract and assign request content
//        start++;
//        if (start < limit) {
//            if (request.getHeaderField(HttpRequestHeaderField.)) {
//                request.setBody(new String(data, start, limit - start, "US-ASCII"));
//            }
//        } else {
//            request.setBody(null);
//        }
//
//        return request;
//    }
//
//    private static Charset parseCharset(String s) {
//        //1#( ( charset | "*" )[ ";" "q" "=" qvalue ] )
//        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
//        SortedMap<Float, String> map = new TreeMap<>();
//        String[] strings = s.split(",");
//        for (String str : strings) {
//            int end = str.indexOf(';');
//            if (end != -1 && end + 3 < str.length() && str.charAt(end + 1) == 'q' && str.charAt(end + 2) == '=') {
//                String charsetName = str.substring(0, end);
//                Float weight = format.parse(str, new ParsePosition(end + 3)).floatValue();
//                map.put(weight, charsetName);
//            } else {
//                map.put(1.0f, str);
//            }
//        }
//        return Charset.availableCharsets().get(s);
//    }
//
//    private static HttpProtocol parseProtocolString(byte[] data, int start, int end) {
//        int length = end - start;
//        if (length == 8) {
//            if (data[start] == 72
//                    && data[start + 1] == 84
//                    && data[start + 2] == 84
//                    && data[start + 3] == 80
//                    && data[start + 4] == 47
//                    && data[start + 5] == 49
//                    && data[start + 6] == 46
//                    && data[start + 7] == 49) {
//                return HttpProtocol.HTTP_1_1;
//            }
//        }
//        return null;
//    }
//
//    private static HttpRequestMethod parseMethodString(String s) {
//        switch (s) {
//            case "GET":
//                return HttpRequestMethod.GET;
//            case "POST":
//                return HttpRequestMethod.POST;
//            case "HEAD":
//                return HttpRequestMethod.HEAD;
//            case "PUT":
//                return HttpRequestMethod.PUT;
//            case "DELETE":
//                return HttpRequestMethod.DELETE;
//            case "OPTIONS":
//                return HttpRequestMethod.OPTIONS;
//            case "TRACE":
//                return HttpRequestMethod.TRACE;
//            case "CONNECT":
//                return HttpRequestMethod.CONNECT;
//            default:
//                return null;
//        }
//    }
//
//    private static HttpRequestHeaderField parseHeaderFieldString(String s) {
//        switch (s) {
//            case "accept":
//                return HttpRequestHeaderField.accept;
//            case "accept-charset":
//                return HttpRequestHeaderField.accept_charset;
//            case "accept-encoding":
//                return HttpRequestHeaderField.accept_encoding;
//            case "accept-language":
//                return HttpRequestHeaderField.accept_language;
//            case "authorization":
//                return HttpRequestHeaderField.authorization;
//            case "cache-control":
//                return HttpRequestHeaderField.cache_control;
//            case "connection":
//                return HttpRequestHeaderField.connection;
//            case "cookie":
//                return HttpRequestHeaderField.cookie;
//            case "content-length":
//                return HttpRequestHeaderField.content_length;
//            case "content-md5":
//                return HttpRequestHeaderField.content_md5;
//            case "content-type":
//                return HttpRequestHeaderField.content_type;
//            case "date":
//                return HttpRequestHeaderField.date;
//            case "expect":
//                return HttpRequestHeaderField.expect;
//            case "from":
//                return HttpRequestHeaderField.from;
//            case "host":
//                return HttpRequestHeaderField.host;
//            case "if-match":
//                return HttpRequestHeaderField.if_match;
//            case "if-modified-since":
//                return HttpRequestHeaderField.if_modified_since;
//            case "if-none-match":
//                return HttpRequestHeaderField.if_none_match;
//            case "if-range":
//                return HttpRequestHeaderField.if_range;
//            case "if-unmodified-since":
//                return HttpRequestHeaderField.if_unmodified_since;
//            case "max-forwards":
//                return HttpRequestHeaderField.max_forwards;
//            case "pragma":
//                return HttpRequestHeaderField.pragma;
//            case "proxy-authorization":
//                return HttpRequestHeaderField.proxy_authorization;
//            case "range":
//                return HttpRequestHeaderField.range;
//            case "referer":
//                return HttpRequestHeaderField.referer;
//            case "te":
//                return HttpRequestHeaderField.te;
//            case "upgrade":
//                return HttpRequestHeaderField.upgrade;
//            case "user-agent":
//                return HttpRequestHeaderField.user_agent;
//            case "via":
//                return HttpRequestHeaderField.via;
//            case "warning":
//                return HttpRequestHeaderField.warning;
//            default:
//                return null;
//        }
//    }
//}