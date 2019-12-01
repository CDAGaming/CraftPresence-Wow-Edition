/*
 * junixsocket
 *
 * Copyright 2009-2019 Christian Kohlschütter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gitlab.cdagaming.craftpresence.impl.junixsocket;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Describes an {@link InetSocketAddress} that actually uses AF_UNIX sockets instead of AF_INET.
 * <p>
 * The ability to specify a port number is not specified by AF_UNIX sockets, but we need it
 * sometimes, for example for RMI-over-AF_UNIX.
 *
 * @author Christian Kohlschütter
 */
public final class AFUNIXSocketAddress extends InetSocketAddress {
    private static final long serialVersionUID = 1L;
    private final byte[] bytes;

    /**
     * Creates a new {@link AFUNIXSocketAddress} that points to the AF_UNIX socket specified by the
     * given file.
     *
     * @param socketFile The socket to connect to.
     * @throws IOException if the operation fails.
     */
    public AFUNIXSocketAddress(final File socketFile) throws IOException {
        this(socketFile, 0);
    }

    /**
     * Creates a new {@link AFUNIXSocketAddress} that points to the AF_UNIX socket specified by the
     * given file, assigning the given port to it.
     *
     * @param socketFile The socket to connect to.
     * @param port       The port associated with this socket, or {@code 0} when no port should be assigned.
     * @throws IOException if the operation fails.
     */
    AFUNIXSocketAddress(final File socketFile, int port) throws IOException {
        this(socketFile.getCanonicalPath().getBytes(Charset.defaultCharset()), port);
    }

    /**
     * Creates a new {@link AFUNIXSocketAddress} that points to the AF_UNIX socket specified by the
     * given byte sequence, assigning the given port to it.
     * <p>
     * NOTE: By specifying a byte array that starts with a zero byte, you indicate that the abstract
     * namespace is to be used. This feature is not available on all target platforms.
     *
     * @param socketAddress The socket address (as bytes).
     * @param port          The port associated with this socket, or {@code 0} when no port should be assigned.
     * @throws IOException if the operation fails.
     *                     Reference: AFUNIXSocketAddress#inAbstractNamespace(String, int)
     */
    private AFUNIXSocketAddress(final byte[] socketAddress, int port) throws IOException {
        super(0);
        if (port != 0) {
            NativeUnixSocketHelper.setPort1(this, port);
        }

        if (socketAddress.length == 0) {
            throw new SocketException("Illegal address length: " + socketAddress.length);
        }

        this.bytes = socketAddress.clone();
    }

    private static String prettyPrint(byte[] data) {
        final int dataLength = data.length;
        StringBuilder sb = new StringBuilder(dataLength + 16);
        for (byte c : data) {
            if (c >= 32 && c < 127) {
                sb.append((char) c);
            } else {
                sb.append("\\x");
                sb.append(String.format(Locale.ENGLISH, "%02x", c));
            }
        }
        return sb.toString();
    }

    byte[] getBytes() {
        return bytes; // NOPMD
    }

    @Override
    public String toString() {
        return getClass().getName() + "[port=" + getPort() + ";address=" + prettyPrint(bytes) + "]";
    }
}