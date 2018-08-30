/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v2.http;

import systems.cauldron.http.v2.cache.CacheDirective;
import systems.cauldron.http.v2.content.ExtensionAdapter;

/**
 * @author admin
 */
public interface HttpResponse extends HttpMessage, Entity {

    public String getVersion();

    public Integer getStatusCode();

    public String getReason();
    /*
     Response      = 
     | Status-Line               ; Section 6.1
     *(( general-header        ; Section 4.5
     | response-header        ; Section 6.2
     | entity-header ) CRLF)  ; Section 7.1
     CRLF
     [ message-body ]          ; Section 7.2
     */
    //Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
    /*
     response-header = 
     | Accept-Ranges           ; Section 14.5
     | Age                     ; Section 14.6
     | ETag                    ; Section 14.19
     | Location                ; Section 14.30
     | Proxy-Authenticate      ; Section 14.33
     | Retry-After             ; Section 14.37
     | Server                  ; Section 14.38
     | Vary                    ; Section 14.44
     | WWW-Authenticate        ; Section 14.47
     */
//Content-Disposition

    public String getAcceptRanges();

    public Integer getAge();

    public String getETag();

    public String getLocation();

    public String getProxyAuthenticate();

    public Integer getRetryAfter();

    public String getVary();

    public String getWWWAuthenticate();

    public String getContentDisposition();

    public HttpResponseHeaderField getTrailer();

    public ExtensionAdapter<CacheDirective> getCacheResponseDirective();
}
