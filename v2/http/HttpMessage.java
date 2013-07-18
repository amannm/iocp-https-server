/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import cache.CacheDirective;
import connection.Connection;
import content.ExtensionAdapter;
import java.util.Date;
import parse.HttpProtocol;

/**
 *
 * @author admin
 */
public interface HttpMessage {
    /*
     general-header = 
     | Cache-Control            ; Section 14.9
     | Connection               ; Section 14.10
     | Date                     ; Section 14.18
     | Pragma                   ; Section 14.32
     | Trailer                  ; Section 14.40
     | Transfer-Encoding        ; Section 14.41
     | Upgrade                  ; Section 14.42
     | Via                      ; Section 14.45
     | Warning                  ; Section 14.46
     */

    public ExtensionAdapter<CacheDirective> getCacheRequestDirective();

    public Connection getConnection();

    public Date getDate();

    public ExtensionAdapter<String> getPragma();

  

    public String getTransferEncoding();

    public HttpProtocol[] getUpgrade();

    public String[] getVia();

    public String getWarning();
}
