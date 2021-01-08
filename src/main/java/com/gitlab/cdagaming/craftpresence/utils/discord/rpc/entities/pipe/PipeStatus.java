/*
 * MIT License
 *
 * Copyright (c) 2018 - 2021 CDAGaming (cstack2011@yahoo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.pipe;

import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCClient;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCListener;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.DiscordBuild;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.Packet;

/**
 * Constants representing various status that an {@link IPCClient} can have.
 */
public enum PipeStatus {
    /**
     * Status for when the IPCClient when no attempt to connect has been made.
     * <p>
     * All IPCClients are created starting with this status,
     * and it never returns for the lifespan of the client.
     */
    UNINITIALIZED,

    /**
     * Status for when the Pipe is attempting to connect.
     * <p>
     * This will become set whenever the #connect() method is called.
     */
    CONNECTING,

    /**
     * Status for when the Pipe is connected with Discord.
     * <p>
     * This is only present when the connection is healthy, stable,
     * and reading good data without exception.<br>
     * If the environment becomes out of line with these principles
     * in any way, the IPCClient in question will become
     * {@link PipeStatus#DISCONNECTED}.
     */
    CONNECTED,

    /**
     * Status for when the pipe status is beginning to close.
     * <p>
     * The status that immediately follows is always {@link PipeStatus#CLOSED}
     */
    CLOSING,

    /**
     * Status for when the Pipe has received an {@link Packet.OpCode#CLOSE}.
     * <p>
     * This signifies that the reading thread has safely and normally shut
     * and the client is now inactive.
     */
    CLOSED,

    /**
     * Status for when the Pipe has unexpectedly disconnected, either because
     * of an exception, and/or due to bad data.
     * <p>
     * When the status of an Pipe becomes this, a call to
     * {@link IPCListener#onDisconnect(IPCClient, Throwable)} will be made if one
     * has been provided to the IPCClient.
     * <p>
     * Note that the IPCClient will be inactive with this status, after which a
     * call to {@link IPCClient#connect(DiscordBuild...)} can be made to "reconnect" the
     * IPCClient.
     */
    DISCONNECTED
}