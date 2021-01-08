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

package com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities;

import com.gitlab.cdagaming.craftpresence.impl.DataConsumer;

/**
 * A callback for asynchronous logic when dealing processes that
 * would normally block the calling thread.
 * <p>
 * This is most visibly implemented in {@link com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCClient IPCClient}.
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class Callback {
    private final DataConsumer<Packet> success;
    private final DataConsumer<String> failure;

    /**
     * Constructs an empty Callback.
     */
    public Callback() {
        this(null, null);
    }

    /**
     * Constructs a Callback with a success {@link DataConsumer} that
     * occurs when the process it is attached to executes without
     * error.
     *
     * @param success The Consumer to launch after a successful process.
     */
    public Callback(DataConsumer<Packet> success) {
        this(success, null);
    }

    /**
     * Constructs a Callback with a success {@link DataConsumer} <i>and</i>
     * a failure {@link DataConsumer} that occurs when the process it is
     * attached to executes without or with error (respectively).
     *
     * @param success The Consumer to launch after a successful process.
     * @param failure The Consumer to launch if the process has an error.
     */
    public Callback(DataConsumer<Packet> success, DataConsumer<String> failure) {
        this.success = success;
        this.failure = failure;
    }

    /**
     * Gets whether or not this Callback is "empty" which is more precisely
     * defined as not having a specified success {@link DataConsumer} and/or a
     * failure {@link DataConsumer}.<br>
     * This is only true if the Callback is constructed with the parameter-less
     * constructor ({@link #Callback()}) or another constructor that leaves
     * one or both parameters {@code null}.
     *
     * @return {@code true} if and only if the Callback is "empty"
     */
    public boolean isEmpty() {
        return success == null && failure == null;
    }

    /**
     * Launches the success {@link DataConsumer}.
     *
     * @param packet The packet to execute after success
     */
    public void succeed(Packet packet) {
        if (success != null)
            success.accept(packet);
    }

    /**
     * Launches the failure {@link DataConsumer} with the
     * provided message.
     *
     * @param message The message to launch the failure consumer with.
     */
    public void fail(String message) {
        if (failure != null)
            failure.accept(message);
    }
}
