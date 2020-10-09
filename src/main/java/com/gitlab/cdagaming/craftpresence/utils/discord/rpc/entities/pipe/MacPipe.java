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

import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCClient;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.Callback;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class MacPipe extends UnixPipe {

    MacPipe(IPCClient ipcClient, HashMap<String, Callback> callbacks, String location) throws IOException {
        super(ipcClient, callbacks, location);
    }

    private void registerCommand(String applicationId, String command) {
        String home = System.getenv("HOME");
        if (home == null)
            throw new RuntimeException("Unable to find user HOME directory");

        String path = home + "/Library/Application Support/discord";

        if (!this.mkdir(path))
            throw new RuntimeException("Failed to create directory '" + path + "'");

        path += "/games";

        if (!this.mkdir(path))
            throw new RuntimeException("Failed to create directory '" + path + "'");

        path += "/" + applicationId + ".json";

        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write("{\"command\": \"" + command + "\"}");
        } catch (Exception ex) {
            throw new RuntimeException("Failed to write fame info into '" + path + "'");
        }
    }

    private void registerUrl(String applicationId) {
        throw new UnsupportedOperationException("OSX URL registration is not handled yet");
        /* TODO
        char url[256];
        snprintf(url, sizeof(url), "discord-%s", applicationId);
        CFStringRef cfURL = CFStringCreateWithCString(NULL, url, kCFStringEncodingUTF8);

        NSString* myBundleId = [[NSBundle mainBundle] bundleIdentifier];
        if (!myBundleId) {
            fprintf(stderr, "No bundle id found\n");
            return;
        }

        NSURL* myURL = [[NSBundle mainBundle] bundleURL];
        if (!myURL) {
            fprintf(stderr, "No bundle url found\n");
            return;
        }

        OSStatus status = LSSetDefaultHandlerForURLScheme(cfURL, (__bridge CFStringRef)myBundleId);
        if (status != noErr) {
            fprintf(stderr, "Error in LSSetDefaultHandlerForURLScheme: %d\n", (int)status);
            return;
        }

        status = LSRegisterURL((__bridge CFURLRef)myURL, true);
        if (status != noErr) {
            fprintf(stderr, "Error in LSRegisterURL: %d\n", (int)status);
        }
         */
    }

    @Override
    public void registerApp(String applicationId, String command) {
        try {
            if (command != null)
                this.registerCommand(applicationId, command);
            else
                this.registerUrl(applicationId);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to register " + (command == null ? "url" : "command"), ex);
        }
    }

    @Override
    public void registerSteamGame(String applicationId, String steamId) {
        this.registerApp(applicationId, "steam://rungameid/" + steamId);
    }
}
