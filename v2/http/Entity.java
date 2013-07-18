/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import http.HttpRequestMethod;
import content.ByteRange;
import content.ContentRange;
import content.Encoding;
import content.ExtensionAdapter;
import content.LanguageTag;
import content.MediaType;
import java.util.Date;

/**
 *
 * @author admin
 */
public interface Entity {
    /*
     entity-header  = 
     | Allow                    ; Section 14.7
     | Content-Encoding         ; Section 14.11
     | Content-Language         ; Section 14.12
     | Content-Length           ; Section 14.13
     | Content-Location         ; Section 14.14
     | Content-MD5              ; Section 14.15
     | Content-Range            ; Section 14.16
     | Content-Type             ; Section 14.17
     | Expires                  ; Section 14.21
     | Last-Modified            ; Section 14.29
     | extension-header
     */

    public HttpRequestMethod getAllow();

    public Encoding getContentEncoding();

    public LanguageTag getContentLanguage();

    public Integer getContentLength();

    public String getContentLocation();

    public String getContentMD5();

    public ContentRange getContentRange();

    public MediaType getContentType();

    public Date getExpires();

    public Date getLastModified();
}
