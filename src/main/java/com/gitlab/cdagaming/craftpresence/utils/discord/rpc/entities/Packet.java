/*
 * MIT License
 *
 * Copyright (c) 2018 - 2020 CDAGaming (cstack2011@yahoo.com)
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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
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
    private final String encoding;

    /**
     * Constructs a new Packet using an {@link OpCode} and {@link JsonObject}.
     *
     * @param op       The OpCode value of this new Packet.
     * @param data     The JSONObject payload of this new Packet.
     * @param encoding encoding to send packets as
     */
    public Packet(OpCode op, JsonObject data, String encoding) {
        this.op = op;
        this.data = data;
        this.encoding = encoding;
    }

    /**
     * Constructs a new Packet using an {@link OpCode} and {@link JsonObject}.
     *
     * @param op   The OpCode value of this new Packet.
     * @param data The JSONObject payload of this new Packet.
     */
    @Deprecated
    public Packet(OpCode op, JsonObject data) {
        this(op, data, "UTF-8");
    }

    /**
     * Converts this {@link Packet} to a {@code byte} array.
     *
     * @return This Packet as a {@code byte} array.
     */
    public byte[] toBytes() {
        String s = data.toString();

        byte[] d;
        try {
            d = s.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            d = s.getBytes();
        }

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
     * Gets the Raw {@link JsonObject} value as a part of this {@link Packet}.
     *
     * @return The JSONObject value of this Packet.
     */
    public JsonObject getJson() {
        return data;
    }

    /**
     * Gets the Parsed {@link JsonObject} value as a part of this {@link Packet}.
     *
     * @return The JSONObject value of this Packet.
     */
    public JsonObject getParsedJson() {
        try {
            final JsonParser jsonParser = new JsonParser();
            return jsonParser.parse(data.getAsJsonPrimitive("").getAsString()).getAsJsonObject();
        } catch (Exception ex) {
            return data;
        }
    }

    @Override
    public String toString() {
        return "Pkt:" + getOp() + getParsedJson().toString();
    }

    public String toDecodedString() {
        try {
            return "Pkt:" + getOp() + new String(getParsedJson().toString().getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            return "Pkt:" + getOp() + getParsedJson().toString();
        }
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
