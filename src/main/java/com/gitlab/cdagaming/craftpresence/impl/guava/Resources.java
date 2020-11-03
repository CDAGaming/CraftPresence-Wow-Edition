/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.gitlab.cdagaming.craftpresence.impl.guava;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides utility methods for working with resources in the classpath. Note that even though these
 * methods use {@link URL} parameters, they are usually not appropriate for HTTP or other
 * non-classpath resources.
 *
 * <p>All method parameters must be non-null unless documented otherwise.
 *
 * @author Chris Nokleberg
 * @author Ben Yu
 * @author Colin Decker
 * @since 1.0
 */
public final class Resources {
    private Resources() {
    }

    /**
     * Returns a {@link ByteSource} that reads from the given URL.
     *
     * @since 14.0
     */
    public static ByteSource asByteSource(URL url) {
        return new UrlByteSource(url);
    }

    /**
     * Returns a {@link CharSource} that reads from the given URL using the given character set.
     *
     * @since 14.0
     */
    public static CharSource asCharSource(URL url, Charset charset) {
        return asByteSource(url).asCharSource(charset);
    }

    /**
     * Reads all bytes from a URL into a byte array.
     *
     * @param url the URL to read from
     * @return a byte array containing all the bytes from the URL
     * @throws IOException if an I/O error occurs
     */
    public static byte[] toByteArray(URL url) throws IOException {
        return asByteSource(url).read();
    }

    /**
     * Reads all characters from a URL into a {@link String}, using the given character set.
     *
     * @param url     the URL to read from
     * @param charset the charset used to decode the input stream; see {@link Charsets} for helpful
     *                predefined constants
     * @return a string containing all the characters from the URL
     * @throws IOException if an I/O error occurs.
     */
    public static String toString(URL url, Charset charset) throws IOException {
        return asCharSource(url, charset).read();
    }

    /**
     * Copies all bytes from a URL to an output stream.
     *
     * @param from the URL to read from
     * @param to   the output stream
     * @throws IOException if an I/O error occurs
     */
    public static void copy(URL from, OutputStream to) throws IOException {
        asByteSource(from).copyTo(to);
    }

    /**
     * A byte source that reads from a URL using {@link URL#openStream()}.
     */
    private static final class UrlByteSource extends ByteSource {

        private final URL url;

        private UrlByteSource(URL url) {
            this.url = checkNotNull(url);
        }

        @Override
        public InputStream openStream() throws IOException {
            return url.openStream();
        }

        @Override
        public String toString() {
            return "Resources.asByteSource(" + url + ")";
        }
    }
}
