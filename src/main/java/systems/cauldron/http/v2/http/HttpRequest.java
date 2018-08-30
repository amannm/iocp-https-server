/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v2.http;

import systems.cauldron.http.v2.connection.Cookie;
import systems.cauldron.http.v2.content.*;
import systems.cauldron.http.v2.parse.HttpProtocol;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 */
public interface HttpRequest extends HttpMessage, Entity {

    public HttpRequestMethod getMethod();

    public String getPath();

    public HttpProtocol getProtocol();
    /*
     request-header = 
     | Accept                   ; Section 14.1
     | Accept-Charset           ; Section 14.2
     | Accept-Encoding          ; Section 14.3
     | Accept-Language          ; Section 14.4
     | Authorization            ; Section 14.8
     | Expect                   ; Section 14.20
     | From                     ; Section 14.22
     | Host                     ; Section 14.23
     | If-Match                 ; Section 14.24
     | If-Modified-Since        ; Section 14.25
     | If-None-Match            ; Section 14.26
     | If-Range                 ; Section 14.27
     | If-Unmodified-Since      ; Section 14.28
     | Max-Forwards             ; Section 14.31
     | Proxy-Authorization      ; Section 14.34
     | Range                    ; Section 14.35
     | Referer                  ; Section 14.36
     | TE                       ; Section 14.39
     | User-Agent               ; Section 14.43
     */

    public List<QualityAdapter<ExtensionAdapter<MediaType>>> getAcceptableMediaType();

    public List<QualityAdapter<Charset>> getAcceptableCharsets();

    public List<QualityAdapter<Encoding>> getAcceptableEncoding();

    public List<QualityAdapter<LanguageTag>> getAcceptableLanguage();

    public Date getAcceptableDatetime();

    public String getAuthorization();

    public List<Cookie> getCookies();

    public ExtensionAdapter<String> getExpect();

    public String getFrom();

    public String getHost();

    public Date getIfModifiedSince();

    public Date getIfUnmodifiedSince();

    public String getIfMatch();

    public String getIfNoneMatch();

    public String getIfRange();

    public Integer getMaxForwards();

    public String getProxyAuthorization();

    public ByteRange getRange();

    public String getReferer();

    public TE getTE();

    public HttpRequestHeaderField getTrailer();

    public String getUserAgent();
}
