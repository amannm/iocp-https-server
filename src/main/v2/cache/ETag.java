/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v2.cache;

/**
 *
 * @author admin
 */
public class ETag {
    /*
     entity-tag = [ weak ] opaque-tag
     weak       = "W/"
     opaque-tag = quoted-string
     */

    boolean weak;
    String opaqueTag;
}
