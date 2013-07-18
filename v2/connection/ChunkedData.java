/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

/**
 *
 * @author admin
 */
public class ChunkedData {
    /*
     Chunked-Body   = *chunk last-chunk trailer CRLF
     chunk          = chunk-size [ chunk-extension ] CRLF chunk-data CRLF
     chunk-size     = 1*HEX
     last-chunk     = 1*("0") [ chunk-extension ] CRLF
     chunk-extension= *( ";" chunk-ext-name [ "=" chunk-ext-val ] )
     chunk-ext-name = token
     chunk-ext-val  = token | quoted-string
     chunk-data     = chunk-size(OCTET)
     trailer        = *(entity-header CRLF)
     */
}
