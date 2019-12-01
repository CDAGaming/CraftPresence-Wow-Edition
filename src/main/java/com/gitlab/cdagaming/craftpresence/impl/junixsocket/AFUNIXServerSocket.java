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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * The server part of an AF_UNIX domain socket.
 *
 * @author Christian Kohlschütter
 */
public class AFUNIXServerSocket extends ServerSocket {
    private final AFUNIXSocketImpl implementation;

    private AFUNIXSocketAddress boundEndpoint;

    AFUNIXServerSocket(Mode mode) throws IOException {
        super();
        setReuseAddress(true);

        this.implementation = (mode == Mode.RMI) ? new AFUNIXSocketImpl.ForRMI() : new AFUNIXSocketImpl();
        NativeUnixSocketHelper.initServerImpl(this, implementation);

        NativeUnixSocketHelper.setCreatedServer(this);
    }

    @Override
    public void bind(SocketAddress endpoint, int backlog) throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (isBound()) {
            throw new SocketException("Already bound");
        }
        if (!(endpoint instanceof AFUNIXSocketAddress)) {
            throw new IOException("Can only bind to endpoints of type " + AFUNIXSocketAddress.class
                    .getName());
        }

        implementation.bind(endpoint, getReuseAddress() ? -1 : 0);
        boundEndpoint = (AFUNIXSocketAddress) endpoint;

        implementation.listen(backlog);
    }

    @Override
    public boolean isBound() {
        return boundEndpoint != null;
    }

    @Override
    public boolean isClosed() {
        return super.isClosed() || (isBound() && !implementation.getFD().valid());
    }

    @Override
    public AFUNIXSocket accept() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        AFUNIXSocket as = newSocketInstance();
        implementation.accept(as.impl);
        as.addr = boundEndpoint;
        NativeUnixSocketHelper.setConnected(as);
        return as;
    }

    private AFUNIXSocket newSocketInstance() throws IOException {
        return AFUNIXSocket.newInstance();
    }

    @Override
    public String toString() {
        if (!isBound()) {
            return "AFUNIXServerSocket[unbound]";
        }
        return "AFUNIXServerSocket[" + boundEndpoint.toString() + "]";
    }

    @Override
    public void close() throws IOException {
        if (isClosed()) {
            return;
        }

        super.close();
        implementation.close();
    }

    private enum Mode {
        DEFAULT, RMI
    }

}