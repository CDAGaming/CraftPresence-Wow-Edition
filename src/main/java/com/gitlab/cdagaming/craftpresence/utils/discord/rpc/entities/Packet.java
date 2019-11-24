/*
 * Copyright 2017 John Grosh (john.a.grosh@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities;

import com.gitlab.cdagaming.craftpresence.utils.FileUtils;
import com.google.gson.JsonObject;

import java.nio.ByteBuffer;

/**
 * A data-packet received from Discord via an {@link com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCClient IPCClient}.<br>
 * These can be handled via an implementation of {@link com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCListener IPCListener}.
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class Packet {
    private final OpCode op;
    private final JsonObject data;

    /**
     * Constructs a new Packet using an {@link OpCode} and {@link JsonObject}.
     *
     * @param op   The OpCode value of this new Packet.
     * @param data The JSONObject payload of this new Packet.
     */
    public Packet(OpCode op, JsonObject data) {
        this.op = op;
        this.data = data;
    }

    /**
     * Converts this {@link Packet} to a {@code byte} array.
     *
     * @return This Packet as a {@code byte} array.
     */
    public byte[] toBytes() {
        byte[] d = data.toString().getBytes();
        ByteBuffer packet = ByteBuffer.allocate(d.length + 2 * Integer.BYTES);
        packet.putInt(Integer.reverseBytes(op.ordinal()));
        packet.putInt(Integer.reverseBytes(d.length));
        packet.put(d);
        return packet.array();
    }

    /**
     * Gets the {@link OpCode} value of this {@link Packet}.
     *
     * @return This Packet's OpCode.
     */
    public OpCode getOp() {
        return op;
    }

    /**
     * Gets the raw {@link JsonObject} value as a part of this {@link Packet}.
     *
     * @return The JSONObject value of this Packet.
     */
    public JsonObject getRawJson() {
        return data;
    }

    /**
     * Gets the parsed {@link JsonObject} value as a part of this {@link Packet} from getRawJson.
     *
     * @return The parsed/formatted JSONObject value of this Packet.
     */
    public JsonObject getJson() {
        return FileUtils.parseJson(data.toString());
    }

    @Override
    public String toString() {
        return "Pkt: " + getOp() + getJson().toString();
    }

    /**
     * Discord response OpCode values that are
     * sent with response data to and from Discord
     * and the {@link com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCClient IPCClient}
     * connected.
     */
    public enum OpCode {
        HANDSHAKE, FRAME, CLOSE, PING, PONG
    }
}
