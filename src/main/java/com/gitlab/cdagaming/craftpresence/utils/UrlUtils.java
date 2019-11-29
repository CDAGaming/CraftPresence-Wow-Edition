/*
 * MIT License
 *
 * Copyright (c) 2018 - 2019 CDAGaming (cstack2011@yahoo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.gitlab.cdagaming.craftpresence.utils;

import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * URL Utilities for parsing URL and relative Json Data
 *
 * @author CDAGaming
 */
public class UrlUtils {
    /**
     * The User Agent to Identify As when Accessing other URLs
     */
    private static final String USER_AGENT = ModUtils.MODID + "/" + ModUtils.MCVersion;

    /**
     * The GSON Json Builder to Use while Parsing Json
     */
    private static Gson GSON = new GsonBuilder().create();

    /**
     * Retrieve Output from a URL as a readable String
     *
     * @param url The URL to Access
     * @return The Output from the url as a String
     * @throws Exception If connection or Input is unable to be established
     */
    public static String getURLText(final URL url) throws Exception {
        final BufferedReader in = getURLReader(url);
        final StringBuilder response = new StringBuilder();
        String inputLine;
        while (!StringUtils.isNullOrEmpty((inputLine = in.readLine()))) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    /**
     * Retrieve a {@link BufferedReader} to read a response from a URL
     *
     * @param url The URL to access (To be converted to a URL)
     * @return a {@link BufferedReader} to read an output response from
     * @throws Exception If a connection is unable to be established
     */
    public static BufferedReader getURLReader(final String url) throws Exception {
        return getURLReader(new URL(url));
    }

    /**
     * Retrieve a {@link BufferedReader} to read a response from a URL
     *
     * @param url The URL to access
     * @return a {@link BufferedReader} to read an output response from
     * @throws Exception If a connection is unable to be established
     */
    public static BufferedReader getURLReader(final URL url) throws Exception {
        return new BufferedReader(getURLStreamReader(url));
    }

    /**
     * Retrieve an {@link InputStream} from a URL
     *
     * @param url The URL to access
     * @return an {@link InputStream} from the URL
     * @throws Exception If a connection is unable to be established
     */
    public static InputStream getURLStream(final URL url) throws Exception {
        final URLConnection connection = url.openConnection();
        connection.addRequestProperty("User-Agent", USER_AGENT);
        return (connection.getInputStream());
    }

    /**
     * Retrieve an {@link InputStreamReader} from a URL
     *
     * @param url The URL to access
     * @return an {@link InputStreamReader} from the URL
     * @throws Exception If a connection is unable to be established
     */
    public static InputStreamReader getURLStreamReader(final URL url) throws Exception {
        return new InputStreamReader(getURLStream(url), StandardCharsets.UTF_8);
    }

    /**
     * Converts a URLs Output into Formatted Json
     *
     * @param url         The URL to access (To be converted into a URL)
     * @param targetClass The target class to base parsing on
     * @param <T>         The data type for the resulting Json
     * @return The URL's Output, as Formatted Json
     * @throws Exception If a connection is unable to be established or parsing fails
     */
    public static <T> T getJSONFromURL(String url, Class<T> targetClass) throws Exception {
        return getJSONFromURL(new URL(url), targetClass);
    }

    /**
     * Converts a URLs Output into Formatted Json
     *
     * @param url         The URL to access
     * @param targetClass The target class to base parsing on
     * @param <T>         The data type for the resulting Json
     * @return The URL's Output, as Formatted Json
     * @throws Exception If a connection is unable to be established or parsing fails
     */
    public static <T> T getJSONFromURL(URL url, Class<T> targetClass) throws Exception {
        return GSON.fromJson(getURLStreamReader(url), targetClass);
    }
}
