/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontoserv.http.util.constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author Administrator
 */
public class SSLConstants {

    public static SSLContext sslContext;

    static {
        try {
            KeyStore ksKeys = KeyStore.getInstance("JKS");
            ksKeys.load(new FileInputStream("C:/ontopad.jks"), "zygospermatics".toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ksKeys, "zygosperm1".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ksKeys);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        } catch (KeyManagementException | UnrecoverableKeyException | IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException ex) {
            //logger.severe(ex.getMessage());
            sslContext = null;
        }
    }
}
