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

package com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.pipe;

import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCClient;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.Callback;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.Packet;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class WindowsPipe extends Pipe {
    public RandomAccessFile file;

    WindowsPipe(IPCClient ipcClient, HashMap<String, Callback> callbacks, String location) {
        super(ipcClient, callbacks);
        try {
            this.file = new RandomAccessFile(location, "rw");
        } catch (FileNotFoundException e) {
            this.file = null;
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        file.write(b);
    }

    @Override
    @SuppressWarnings("BusyWait")
    public Packet read() throws IOException, JsonParseException {
        while ((status == PipeStatus.CONNECTED || status == PipeStatus.CLOSING) && file.length() == 0) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
        }

        if (status == PipeStatus.DISCONNECTED)
            throw new IOException("Disconnected!");

        if (status == PipeStatus.CLOSED)
            return new Packet(Packet.OpCode.CLOSE, null, ipcClient.getEncoding());

        Packet.OpCode op = Packet.OpCode.values()[Integer.reverseBytes(file.readInt())];
        int len = Integer.reverseBytes(file.readInt());
        byte[] d = new byte[len];

        file.readFully(d);

        return receive(op, d);
    }

    @Override
    public void close() throws IOException {
        if (ipcClient.isDebugMode()) {
            ModUtils.LOG.debugInfo("Closing IPC pipe...");
        }

        status = PipeStatus.CLOSING;
        send(Packet.OpCode.CLOSE, new JsonObject(), null);
        status = PipeStatus.CLOSED;
        file.close();
    }

    @Override
    public void registerApp(String applicationId, String command) {
        String javaLibraryPath = System.getProperty("java.home");
        File javaExeFile = new File(javaLibraryPath.split(";")[0] + "/bin/java.exe");
        File javawExeFile = new File(javaLibraryPath.split(";")[0] + "/bin/javaw.exe");
        String javaExePath = javaExeFile.exists() ? javaExeFile.getAbsolutePath() : javawExeFile.exists() ? javawExeFile.getAbsolutePath() : null;

        if (javaExePath == null)
            throw new RuntimeException("Unable to find java path");

        String openCommand;

        if (command != null)
            openCommand = command;
        else
            openCommand = javaExePath;

        String protocolName = "discord-" + applicationId;
        String protocolDescription = "URL:Run game " + applicationId + " protocol";
        String keyName = "Software\\Classes\\" + protocolName;
        String iconKeyName = keyName + "\\DefaultIcon";
        String commandKeyName = keyName + "\\DefaultIcon";

        try {
            Advapi32Util.registryCreateKey(WinReg.HKEY_CURRENT_USER, keyName);
            Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, keyName, "", protocolDescription);
            Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, keyName, "URL Protocol", "\0");

            Advapi32Util.registryCreateKey(WinReg.HKEY_CURRENT_USER, iconKeyName);
            Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, iconKeyName, "", javaExePath);

            Advapi32Util.registryCreateKey(WinReg.HKEY_CURRENT_USER, commandKeyName);
            Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, commandKeyName, "", openCommand);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to modify Discord registry keys", ex);
        }
    }

    @Override
    public void registerSteamGame(String applicationId, String steamId) {
        try {
            String steamPath = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, "Software\\\\Valve\\\\Steam", "SteamExe");
            if (steamPath == null)
                throw new RuntimeException("Steam exe path not found");

            steamPath = steamPath.replaceAll("/", "\\");

            String command = "\"" + steamPath + "\" steam://rungameid/" + steamId;

            this.registerApp(applicationId, command);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to register Steam game", ex);
        }
    }

}
