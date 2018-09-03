package systems.cauldron.http;

import org.junit.Assert;
import org.junit.Test;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.stream.Collectors;

public class ServerTest {

    @Test
    public void canRedirectToHttpsAndServeStaticHtml() throws IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException {
        String testHostname = "localhost";
        int testHttpPort = 8080;
        int testHttpsPort = 8181;
        String testContent = "<!doctype html><html><head><title>Hello World</title></head><body>Hey</body></html>";

        Server testServer = new Server();
        testServer.bind(testHttpPort, new HttpRedirectHandler("https://" + testHostname + ":" + testHttpsPort));
        testServer.bind(testHttpsPort, new HttpsHtmlHandler(testContent));

        Runtime.getRuntime().addShutdownHook(new Thread(testServer::stop));
        testServer.start();

        String content = getHttpContent(testHostname, testHttpPort);
        Assert.assertEquals(testContent, content);

        testServer.stop();
    }

    private static String getHttpsContent(String location) throws IOException, NoSuchAlgorithmException, KeyManagementException {

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };


        URL url = tryConstructUrl(location);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setHostnameVerifier(allHostsValid);
        int responseCode = connection.getResponseCode();
        switch (responseCode) {
            case 200:
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    return reader.lines().collect(Collectors.joining("\n"));
                }
            default:
                throw new RuntimeException("unexpected response: " + responseCode);
        }
    }

    private static String getHttpContent(String hostname, int port) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        URL url = tryConstructUrl("http://" + hostname + ":" + port);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        int responseCode = connection.getResponseCode();
        switch (responseCode) {
            case 301:
                String target = connection.getHeaderField("Location");
                if (target != null) {
                    return getHttpsContent(target);
                }
            default:
                throw new RuntimeException("unexpected response: " + responseCode);
        }
    }

    private static URL tryConstructUrl(String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
