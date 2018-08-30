package systems.cauldron.http.util;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.nio.charset.StandardCharsets;

/**
 * @author admin
 */
public class HeaderConstants {

    public static byte[] close;
    public static byte[] keepAlive;

    static {
        close = "close".getBytes(StandardCharsets.US_ASCII);
        keepAlive = "keep-alive".getBytes(StandardCharsets.US_ASCII);
    }
}
