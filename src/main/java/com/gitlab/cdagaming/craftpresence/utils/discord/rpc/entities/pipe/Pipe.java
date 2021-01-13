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

import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.FileUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCClient;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCListener;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.Callback;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.DiscordBuild;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.Packet;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.User;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.exceptions.NoDiscordClientException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public abstract class Pipe {
    private static final int VERSION = 1;
    // a list of system property keys to get IPC file from different unix systems.
    private final static String[] unixPaths = {"XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP"};
    final IPCClient ipcClient;
    private final HashMap<String, Callback> callbacks;
    PipeStatus status = PipeStatus.CONNECTING;
    IPCListener listener;
    private DiscordBuild build;
    private User currentUser;

    Pipe(IPCClient ipcClient, HashMap<String, Callback> callbacks) {
        this.ipcClient = ipcClient;
        this.callbacks = callbacks;
    }

    public static Pipe openPipe(IPCClient ipcClient, long clientId, HashMap<String, Callback> callbacks,
                                DiscordBuild... preferredOrder) throws NoDiscordClientException {

        if (preferredOrder == null || preferredOrder.length == 0)
            preferredOrder = new DiscordBuild[]{DiscordBuild.ANY};

        Pipe pipe = null;

        // store some files so we can get the preferred client
        Pipe[] open = new Pipe[DiscordBuild.values().length];
        for (int i = 0; i < 10; i++) {
            try {
                String location = getPipeLocation(i);
                if (ipcClient.isDebugMode()) {
                    ModUtils.LOG.debugInfo(String.format("Searching for IPC: %s", location));
                }
                pipe = createPipe(ipcClient, callbacks, location);

                if (pipe != null) {
                    JsonObject finalObject = new JsonObject();

                    finalObject.addProperty("v", VERSION);
                    finalObject.addProperty("client_id", Long.toString(clientId));

                    pipe.send(Packet.OpCode.HANDSHAKE, finalObject, null);

                    Packet p = pipe.read(); // this is a valid client at this point

                    final JsonObject parsedData = p.getJson();
                    final JsonObject data = parsedData.getAsJsonObject("data");
                    final JsonObject userData = data.getAsJsonObject("user");

                    pipe.build = DiscordBuild.from(data
                            .getAsJsonObject("config")
                            .get("api_endpoint").getAsString());

                    pipe.currentUser = new User(
                            userData.getAsJsonPrimitive("username").getAsString(),
                            userData.getAsJsonPrimitive("discriminator").getAsString(),
                            Long.parseLong(userData.getAsJsonPrimitive("id").getAsString()),
                            userData.has("avatar") && userData.get("avatar").isJsonPrimitive() ? userData.getAsJsonPrimitive("avatar").getAsString() : null
                    );

                    if (ipcClient.isDebugMode()) {
                        ModUtils.LOG.debugInfo(String.format("Found a valid client (%s) with packet: %s", pipe.build.name(), p.toString()));
                        ModUtils.LOG.debugInfo(String.format("Found a valid user (%s) with id: %s", pipe.currentUser.getName(), pipe.currentUser.getId()));
                    }

                    // we're done if we found our first choice
                    if (pipe.build == preferredOrder[0] || DiscordBuild.ANY == preferredOrder[0]) {
                        if (ipcClient.isDebugMode()) {
                            ModUtils.LOG.debugInfo(String.format("Found preferred client: %s", pipe.build.name()));
                        }
                        break;
                    }

                    open[pipe.build.ordinal()] = pipe; // didn't find first choice yet, so store what we have
                    open[DiscordBuild.ANY.ordinal()] = pipe; // also store in 'any' for use later

                    pipe.build = null;
                    pipe = null;
                }
            } catch (IOException | JsonParseException ex) {
                pipe = null;
            }
        }

        if (pipe == null) {
            // we already know we don't have our first pick
            // check each of the rest to see if we have that
            for (int i = 1; i < preferredOrder.length; i++) {
                DiscordBuild cb = preferredOrder[i];
                if (ipcClient.isDebugMode()) {
                    ModUtils.LOG.debugInfo(String.format("Looking for client build: %s", cb.name()));
                }

                if (open[cb.ordinal()] != null) {
                    pipe = open[cb.ordinal()];
                    open[cb.ordinal()] = null;
                    if (cb == DiscordBuild.ANY) // if we pulled this from the 'any' slot, we need to figure out which build it was
                    {
                        for (int k = 0; k < open.length; k++) {
                            if (open[k] == pipe) {
                                pipe.build = DiscordBuild.values()[k];
                                open[k] = null; // we don't want to close this
                            }
                        }
                    } else pipe.build = cb;

                    if (ipcClient.isDebugMode()) {
                        ModUtils.LOG.debugInfo(String.format("Found preferred client: %s", pipe.build.name()));
                    }
                    break;
                }
            }
            if (pipe == null) {
                throw new NoDiscordClientException();
            }
        }
        // close unused files, except skip 'any' because its always a duplicate
        for (int i = 0; i < open.length; i++) {
            if (i == DiscordBuild.ANY.ordinal())
                continue;
            if (open[i] != null) {
                try {
                    open[i].close();
                } catch (IOException ex) {
                    // This isn't really important to applications and better
                    // as debug info
                    if (ipcClient.isDebugMode()) {
                        ModUtils.LOG.debugError(String.format("Failed to close an open IPC pipe: %s", ex));
                    }
                }
            }
        }

        pipe.status = PipeStatus.CONNECTED;

        return pipe;
    }

    private static Pipe createPipe(IPCClient ipcClient, HashMap<String, Callback> callbacks, String location) {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            WindowsPipe attemptedPipe = new WindowsPipe(ipcClient, callbacks, location);
            return attemptedPipe.file != null ? attemptedPipe : null;
        } else if (osName.contains("linux") || osName.contains("mac")) {
            try {
                return osName.contains("mac") ? new MacPipe(ipcClient, callbacks, location) : new UnixPipe(ipcClient, callbacks, location);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Unsupported OS: " + osName);
        }
    }

    /**
     * Generates a nonce.
     *
     * @return A random {@link UUID}.
     */
    private static String generateNonce() {
        return UUID.randomUUID().toString();
    }

    /**
     * Finds the IPC location in the current system.
     *
     * @param index The index to try getting the IPC at.
     * @return The IPC location.
     */
    private static String getPipeLocation(int index) {
        String tmpPath = null, pipePath = "discord-ipc-" + index;
        if (System.getProperty("os.name").contains("Win"))
            return "\\\\?\\pipe\\" + pipePath;
        for (String str : unixPaths) {
            tmpPath = System.getenv(str);
            if (tmpPath != null)
                break;
        }
        if (tmpPath == null) {
            tmpPath = "/tmp";
        } else {
            String snapPath = tmpPath + "/snap.discord", flatpakPath = tmpPath + "/app/com.discordapp.Discord";

            File snapFile = new File(snapPath),
                    flatpakFile = new File(flatpakPath);

            if (snapFile.exists() && snapFile.isDirectory()) {
                tmpPath = snapPath;
            } else if (flatpakFile.exists() && flatpakFile.isDirectory()) {
                tmpPath = flatpakPath;
            }
        }
        return tmpPath + "/" + pipePath;
    }

    /**
     * Sends json with the given {@link Packet.OpCode}.
     *
     * @param op       The {@link Packet.OpCode} to send data with.
     * @param data     The data to send.
     * @param callback callback for the response
     */
    public void send(Packet.OpCode op, JsonObject data, Callback callback) {
        try {
            String nonce = generateNonce();
            data.addProperty("nonce", nonce);
            Packet p = new Packet(op, data, ipcClient.getEncoding());
            if (callback != null && !callback.isEmpty())
                callbacks.put(nonce, callback);
            write(p.toBytes());
            if (ipcClient.isDebugMode()) {
                ModUtils.LOG.debugInfo(String.format("Sent packet: %s", p.toDecodedString()));
            }

            if (listener != null)
                listener.onPacketSent(ipcClient, p);
        } catch (IOException ex) {
            ModUtils.LOG.error("Encountered an IOException while sending a packet and disconnected!");
            status = PipeStatus.DISCONNECTED;
        }
    }

    /**
     * Receives a {@link Packet} with the given {@link Packet.OpCode} and byte data.
     *
     * @param op   The {@link Packet.OpCode} to receive data with.
     * @param data The data to parse with.
     */
    public Packet receive(Packet.OpCode op, byte[] data) {
        JsonObject packetData = FileUtils.parseJson(new String(data));
        Packet p = new Packet(op, packetData, ipcClient.getEncoding());

        if (ipcClient.isDebugMode()) {
            ModUtils.LOG.debugInfo(String.format("Received packet: %s", p.toString()));
        }

        if (listener != null)
            listener.onPacketReceived(ipcClient, p);
        return p;
    }

    /**
     * Blocks until reading a {@link Packet} or until the
     * read thread encounters bad data.
     *
     * @return A valid {@link Packet}.
     * @throws IOException        If the pipe breaks.
     * @throws JsonParseException If the read thread receives bad data.
     */
    public abstract Packet read() throws IOException, JsonParseException;

    public abstract void write(byte[] b) throws IOException;

    public abstract void registerApp(String applicationId, String command);

    public abstract void registerSteamGame(String applicationId, String steamId);

    public PipeStatus getStatus() {
        return status;
    }

    public void setStatus(PipeStatus status) {
        this.status = status;
    }

    public void setListener(IPCListener listener) {
        this.listener = listener;
    }

    public abstract void close() throws IOException;

    public DiscordBuild getDiscordBuild() {
        return build;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
