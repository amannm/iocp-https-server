/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class HeaderConstants {

    public static byte[] close;
    public static byte[] keepAlive;

    static {
        try {
            close = "close".getBytes("US-ASCII");
            keepAlive = "keep-alive".getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
