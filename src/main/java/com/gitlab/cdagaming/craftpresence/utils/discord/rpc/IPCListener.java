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

package com.gitlab.cdagaming.craftpresence.utils.discord.rpc;

import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.Packet;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.User;
import com.google.gson.JsonObject;

/**
 * An implementable listener used to handle events caught by an {@link IPCClient}.
 * <p>
 * Can be attached to an IPCClient using {@link IPCClient#setListener(IPCListener)}.
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public interface IPCListener {
    /**
     * Fired whenever an {@link IPCClient} sends a {@link Packet} to Discord.
     *
     * @param client The IPCClient sending the Packet.
     * @param packet The Packet being sent.
     */
    void onPacketSent(IPCClient client, Packet packet);

    /**
     * Fired whenever an {@link IPCClient} receives a {@link Packet} to Discord.
     *
     * @param client The IPCClient receiving the Packet.
     * @param packet The Packet being received.
     */
    void onPacketReceived(IPCClient client, Packet packet);

    /**
     * Fired whenever a RichPresence activity informs us that
     * a user has clicked a "join" button.
     *
     * @param client The IPCClient receiving the event.
     * @param secret The secret of the event, determined by the implementation and specified by the user.
     */
    void onActivityJoin(IPCClient client, String secret);

    /**
     * Fired whenever a RichPresence activity informs us that
     * a user has clicked a "spectate" button.
     *
     * @param client The IPCClient receiving the event.
     * @param secret The secret of the event, determined by the implementation and specified by the user.
     */
    void onActivitySpectate(IPCClient client, String secret);

    /**
     * Fired whenever a RichPresence activity informs us that
     * a user has clicked a "ask to join" button.
     * <p>
     * As opposed to {@link #onActivityJoin(IPCClient, String)},
     * this also provides packaged {@link User} data.
     *
     * @param client The IPCClient receiving the event.
     * @param secret The secret of the event, determined by the implementation and specified by the user.
     * @param user   The user who clicked the clicked the event, containing data on the account.
     */
    void onActivityJoinRequest(IPCClient client, String secret, User user);

    /**
     * Fired whenever an {@link IPCClient} is ready and connected to Discord.
     *
     * @param client The now ready IPCClient.
     */
    void onReady(IPCClient client);

    /**
     * Fired whenever an {@link IPCClient} has closed.
     *
     * @param client The now closed IPCClient.
     * @param json   A {@link JsonObject} with close data.
     */
    void onClose(IPCClient client, JsonObject json);

    /**
     * Fired whenever an {@link IPCClient} has disconnected,
     * either due to bad data or an exception.
     *
     * @param client The now closed IPCClient.
     * @param t      A {@link Throwable} responsible for the disconnection.
     */
    void onDisconnect(IPCClient client, Throwable t);
}
