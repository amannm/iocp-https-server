/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v2.parse;

import v2.http.HttpRequestMethod;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author admin
 */
public class RawHttpRequest {

    private final byte[] source;
    private final Demarcation method;
    private final Demarcation path;
    private final Demarcation protocol;
    private final Map<Demarcation, Demarcation> headerMap = new HashMap<>();
    private final Demarcation content;

    public RawHttpRequest(final byte[] source) throws Exception {
        this.source = source;
        int limit = source.length;
        int start, end;

        //*(CR / LF) method-name SP
        for (start = 0; start < limit || (source[start] != ASCII.LF && source[start] != ASCII.CR); start++) {
        }
        for (end = start + 1; end < limit || (source[end] == ASCII.SP); end++) {
        }
        this.method = new Demarcation(start, end);

        //*(SP / HR) request-uri SP
        for (start = end + 1; start < limit || (source[start] != ASCII.SP && source[start] != ASCII.HT); start++) {
        }
        for (end = start + 1; end < limit || (source[end] == ASCII.SP); end++) {
        }
        this.path = new Demarcation(start, end);

        //*(SP / HR) protocol-name ((CR LF) / LF)
        for (start = end + 1; start < limit || (source[start] != ASCII.SP && source[start] != ASCII.HT); start++) {
        }
        for (end = start;; end++) {
            if (end >= limit) {
                this.protocol = null;
                break;
            } else {
                if (source[end] == ASCII.LF) {
                    this.protocol = new Demarcation(start, end);
                    break;
                } else {
                    if (source[start] == ASCII.CR && ++start < limit) {
                        if (source[start] == ASCII.LF) {
                            this.protocol = new Demarcation(start, end - 1);
                            break;
                        }
                    }
                }
            }
        }

        //*(field-name ':' field-value ((CR LF) / LF)) ((CR LF) / LF)
        while (end < limit) {
            //Stop parsing headers when a second CRLF / LF occurs immediately after another CRLF / LF 
            //Otherwise demarcate a header field name
            start = end + 1;
            if (start < limit) {
                if (source[start] == ASCII.LF) {
                    break;
                } else {
                    if (source[start] == ASCII.CR && ++start < limit) {
                        if (source[start] == ASCII.LF) {
                            break;
                        }
                    }
                }
            }
            for (end = start; end < limit || source[end] == ASCII.Colon; end++) {
            }
            Demarcation headerFieldName = new Demarcation(start, end);

            //Demarcate Header Value & put resulting pair into map
            for (start = end + 1; start < limit || (source[start] != ASCII.SP && source[start] != ASCII.HT); start++) {
            }
            for (end = start; end < limit; end++) {
                if (source[end] == ASCII.LF) {
                    break;
                } else {
                    if (source[start] == ASCII.CR && ++start < limit) {
                        if (source[start] == ASCII.LF) {
                            break;
                        }
                    }
                }
            }
            headerMap.put(headerFieldName, new Demarcation(start, end));
        }

        //Demarcate remaining bytes as message content
        if (++start < limit) {
            content = new Demarcation(start, limit);
        } else {
            content = null;
        }
    }

    public String parsePath() {
        try {
            return new String(source, path.start, path.end, "US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            return new String(source, path.start, path.end);
        }
    }

    public HttpProtocol parseProtocol() {
        int length = method.end - method.start;
        if (length == 8) {
            if (source[method.start] == 72 && source[method.start + 1] == 84 && source[method.start + 2] == 84 && source[method.start + 3] == 80 && source[method.start + 4] == 47 && source[method.start + 5] == 49 && source[method.start + 6] == 46 && source[method.start + 7] == 49) {
                return HttpProtocol.HTTP_1_1;
            }
        }
        return null;
    }

    public HttpRequestMethod parseMethod() {
        switch (method.end - method.start) {
            case 3:
                //GET & PUT
                switch (source[method.start]) {
                    case 71:
                        if (source[method.start + 1] == 69 && source[method.start + 2] == 84) {
                            return HttpRequestMethod.GET;
                        }
                        break;
                    case 80:
                        if (source[method.start + 1] == 85 && source[method.start + 2] == 84) {
                            return HttpRequestMethod.PUT;
                        }
                        break;
                }
                break;
            case 4:
                //POST & HEAD
                switch (source[method.start]) {
                    case 80:
                        if (source[method.start + 1] == 79 && source[method.start + 2] == 83 && source[method.start + 3] == 84) {
                            return HttpRequestMethod.POST;
                        }
                        break;
                    case 72:
                        if (source[method.start + 1] == 69 && source[method.start + 2] == 65 && source[method.start + 3] == 68) {
                            return HttpRequestMethod.HEAD;
                        }
                        break;
                }
                break;
            case 5:
                //TRACE & PATCH
                switch (source[method.start]) {
                    case 84:
                        if (source[method.start + 1] == 82 && source[method.start + 2] == 65 && source[method.start + 3] == 67 && source[method.start + 4] == 69) {
                            return HttpRequestMethod.TRACE;
                        }
                        break;
                    case 80:
                        if (source[method.start + 1] == 65 && source[method.start + 2] == 84 && source[method.start + 3] == 67 && source[method.start + 4] == 72) {
                            return HttpRequestMethod.PATCH;
                        }
                        break;
                }
                break;
            case 6:
                //DELETE
                if (source[method.start + 1] == 68 && source[method.start + 2] == 69 && source[method.start + 3] == 76 && source[method.start + 4] == 69 && source[method.start + 5] == 84 && source[method.start + 6] == 69) {
                    return HttpRequestMethod.DELETE;
                }
                break;
            case 7:
                // OPTIONS & CONNECT
                switch (source[method.start]) {
                    case 79:
                        if (source[method.start + 1] == 80 && source[method.start + 2] == 84 && source[method.start + 3] == 73 && source[method.start + 4] == 79 && source[method.start + 5] == 78 && source[method.start + 6] == 83) {
                            return HttpRequestMethod.OPTIONS;
                        }
                        break;
                    case 67:
                        if (source[method.start + 1] == 79 && source[method.start + 2] == 78 && source[method.start + 3] == 78 && source[method.start + 4] == 69 && source[method.start + 5] == 67 && source[method.start + 6] == 84) {
                            return HttpRequestMethod.CONNECT;
                        }
                        break;
                }
        }
        return null;
    }
}
