package com.gitlab.cdagaming.craftpresence.handler;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.resources.I18n;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class URLHandler {
    private static final String USER_AGENT = Constants.MODID + "/" + Constants.VERSION_ID;
    private static Gson GSON = new GsonBuilder().create();

    public static String getURLText(final URL url) throws Exception {
        final BufferedReader in = getURLReader(url);
        final StringBuilder response = new StringBuilder();
        String inputLine;
        while (!StringHandler.isNullOrEmpty((inputLine = in.readLine()))) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public static BufferedReader getURLReader(final String url) throws Exception {
        return getURLReader(new URL(url));
    }

    public static void acceptCertificates() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // N/A
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // N/A
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception ex) {
            Constants.LOG.error(I18n.format("craftpresence.logger.error.https"));
        }
    }

    public static BufferedReader getURLReader(final URL url) throws Exception {
        return new BufferedReader(new InputStreamReader(getURLStream(url)));
    }

    public static InputStream getURLStream(final URL url) throws Exception {
        final URLConnection connection = url.openConnection();
        connection.addRequestProperty("User-Agent", USER_AGENT);
        return (connection.getInputStream());
    }

    public static <T> T getJSONFromURL(String url, Class<T> clazz) throws Exception {
        return getJSONFromURL(new URL(url), clazz);
    }

    public static <T> T getJSONFromURL(URL url, Class<T> clazz) throws Exception {
        InputStreamReader reader = new InputStreamReader(getURLStream(url));
        return GSON.fromJson(reader, clazz);
    }
}
