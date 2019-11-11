package com.gitlab.cdagaming.craftpresence.handler;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class URLHandler {
    private static final String USER_AGENT = Constants.MODID + "/" + Constants.MCVersion;
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

    public static BufferedReader getURLReader(final URL url) throws Exception {
        return new BufferedReader(getURLStreamReader(url));
    }

    public static InputStream getURLStream(final URL url) throws Exception {
        final URLConnection connection = url.openConnection();
        connection.addRequestProperty("User-Agent", USER_AGENT);
        return (connection.getInputStream());
    }

    public static InputStreamReader getURLStreamReader(final URL url) throws Exception {
        return new InputStreamReader(getURLStream(url), StandardCharsets.UTF_8);
    }

    public static <T> T getJSONFromURL(String url, Class<T> clazz) throws Exception {
        return getJSONFromURL(new URL(url), clazz);
    }

    public static <T> T getJSONFromURL(URL url, Class<T> clazz) throws Exception {
        return GSON.fromJson(getURLStreamReader(url), clazz);
    }
}
