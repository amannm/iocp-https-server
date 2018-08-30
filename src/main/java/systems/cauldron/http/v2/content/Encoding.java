/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v2.content;

/**
 * @author admin
 */
public enum Encoding {
    chunked,
    identity,
    gzip,
    compress,
    deflate,
}
