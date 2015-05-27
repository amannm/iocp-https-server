/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v1.http.util.constants;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author admin
 */
public class HttpStatus {

    public static final byte[][] status = new byte[600][];

    static {
        try {
            status[100] = "HTTP/1.1 100 Continue\r\n".getBytes(StandardCharsets.US_ASCII);
            status[101] = "HTTP/1.1 101 Switching Protocols\r\n".getBytes(StandardCharsets.US_ASCII);
            status[200] = "HTTP/1.1 200 OK\r\n".getBytes(StandardCharsets.US_ASCII);
            status[201] = "HTTP/1.1 201 Created\r\n".getBytes(StandardCharsets.US_ASCII);
            status[202] = "HTTP/1.1 202 Accepted\r\n".getBytes(StandardCharsets.US_ASCII);
            status[203] = "HTTP/1.1 203 Non-Authoritative Information\r\n".getBytes(StandardCharsets.US_ASCII);
            status[204] = "HTTP/1.1 204 No Content\r\n".getBytes(StandardCharsets.US_ASCII);
            status[205] = "HTTP/1.1 205 Reset Content\r\n".getBytes(StandardCharsets.US_ASCII);
            status[206] = "HTTP/1.1 206 Partial Content\r\n".getBytes(StandardCharsets.US_ASCII);
            status[300] = "HTTP/1.1 300 Multiple Choices\r\n".getBytes(StandardCharsets.US_ASCII);
            status[301] = "HTTP/1.1 301 Moved Permanently\r\n".getBytes(StandardCharsets.US_ASCII);
            status[302] = "HTTP/1.1 302 Found\r\n".getBytes(StandardCharsets.US_ASCII);
            status[303] = "HTTP/1.1 303 See Other\r\n".getBytes(StandardCharsets.US_ASCII);
            status[304] = "HTTP/1.1 304 Not Modified\r\n".getBytes(StandardCharsets.US_ASCII);
            status[305] = "HTTP/1.1 305 Use Proxy\r\n".getBytes(StandardCharsets.US_ASCII);
            status[307] = "HTTP/1.1 307 Temporary Redirect\r\n".getBytes(StandardCharsets.US_ASCII);
            status[400] = "HTTP/1.1 400 Bad Request\r\n".getBytes(StandardCharsets.US_ASCII);
            status[401] = "HTTP/1.1 401 Unauthorized\r\n".getBytes(StandardCharsets.US_ASCII);
            status[402] = "HTTP/1.1 402 Payment Required\r\n".getBytes(StandardCharsets.US_ASCII);
            status[403] = "HTTP/1.1 403 Forbidden\r\n".getBytes(StandardCharsets.US_ASCII);
            status[404] = "HTTP/1.1 404 Not Found\r\n".getBytes(StandardCharsets.US_ASCII);
            status[405] = "HTTP/1.1 405 Method Not Allowed\r\n".getBytes(StandardCharsets.US_ASCII);
            status[406] = "HTTP/1.1 406 Not Acceptable\r\n".getBytes(StandardCharsets.US_ASCII);
            status[407] = "HTTP/1.1 407 Proxy Authentication Required\r\n".getBytes(StandardCharsets.US_ASCII);
            status[408] = "HTTP/1.1 408 Request Timeout\r\n".getBytes(StandardCharsets.US_ASCII);
            status[409] = "HTTP/1.1 409 Conflict\r\n".getBytes(StandardCharsets.US_ASCII);
            status[410] = "HTTP/1.1 410 Gone\r\n".getBytes(StandardCharsets.US_ASCII);
            status[411] = "HTTP/1.1 411 Length Required\r\n".getBytes(StandardCharsets.US_ASCII);
            status[412] = "HTTP/1.1 412 Precondition Failed\r\n".getBytes(StandardCharsets.US_ASCII);
            status[413] = "HTTP/1.1 413 Request Entity Too Large\r\n".getBytes(StandardCharsets.US_ASCII);
            status[414] = "HTTP/1.1 414 Request-URI Too Large\r\n".getBytes(StandardCharsets.US_ASCII);
            status[415] = "HTTP/1.1 415 Unsupported Media Type\r\n".getBytes(StandardCharsets.US_ASCII);
            status[416] = "HTTP/1.1 416 Requested range not satisfiable\r\n".getBytes(StandardCharsets.US_ASCII);
            status[417] = "HTTP/1.1 417 Expectation Failed\r\n".getBytes(StandardCharsets.US_ASCII);
            status[500] = "HTTP/1.1 500 Internal Server Error\r\n".getBytes(StandardCharsets.US_ASCII);
            status[501] = "HTTP/1.1 501 Not Implemented\r\n".getBytes(StandardCharsets.US_ASCII);
            status[502] = "HTTP/1.1 502 Bad Gateway\r\n".getBytes(StandardCharsets.US_ASCII);
            status[503] = "HTTP/1.1 503 Service Unavailable\r\n".getBytes(StandardCharsets.US_ASCII);
            status[504] = "HTTP/1.1 504 Gateway Time-out\r\n".getBytes(StandardCharsets.US_ASCII);
            status[505] = "HTTP/1.1 505 HTTP Version not supported\r\n".getBytes(StandardCharsets.US_ASCII);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
