/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v1.http.util.constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPOutputStream;
//import ontoserv.connection.HTTPChannel;

/**
 * @author admin
 */
public class OntopadConstants {

    public static byte[] defaultContent;
    public static byte[] gzipDefaultContent;
    public static byte[] favicon;
    public static byte[] gzipFavicon;

    static {
        try {
            defaultContent = "<!doctype html><html><head><title>Ontopad</title><style>html, body {height:100%; width: 100%; margin:0; padding:0; background: #303030}#outer {position: absolute; width: 100%; height: 100%; display: table;}#inner {display: table-cell; vertical-align: middle; text-align: center;}#definition {background: #404040; border-top: solid 0.2em #505050; border-bottom: solid 0.2em #505050; padding-left: 1em; padding-right: 1em; font-family: georgia, arial, verdana, sans-serif; font-size: 180%;}#definition h1 {color: #a0a0e0;}#definition p {color: #e0e0e0;}</style></head><body><div id=\"outer\"><div id=\"inner\"><div id=\"definition\"><h1>on·tol·o·gy</h1><p>a particular theory about the kinds of things that have existence</p></div></div></div></body></html>".getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace(System.err);
        }
    }

    static {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            GZIPOutputStream gos = new GZIPOutputStream(stream);
            gos.write(defaultContent);
            stream.close();
            gos.close();
            gzipDefaultContent = stream.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }


    static {
        try {
            favicon = new byte[8192];
            OntopadConstants.class.getResourceAsStream("favicon.ico").read(favicon);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
