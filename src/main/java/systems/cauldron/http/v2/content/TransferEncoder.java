/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v2.content;

/**
 * @author admin
 */
public class TransferEncoder {
    /*
            length := 0
       read chunk-size, chunk-extension (if any) and CRLF
       while (chunk-size > 0) {
          read chunk-data and CRLF
          append chunk-data to entity-body
          length := length + chunk-size
          read chunk-size and CRLF
       }
       read entity-header
       while (entity-header not empty) {
          append entity-header to existing header fields
          read entity-header
       }
       Content-Length := length
       Remove "chunked" from Transfer-Encoding

     */
}
