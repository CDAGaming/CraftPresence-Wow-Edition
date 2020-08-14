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

package com.gitlab.cdagaming.craftpresence.utils.discord;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.commands.CommandsGui;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCClient;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCListener;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.Packet;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.User;
import com.google.gson.JsonObject;

/**
 * Listener to Interpret Discord IPC Events, on received
 * <p>See {@link IPCListener} for more Info
 *
 * @author CDAGaming
 */
public class ModIPCListener implements IPCListener {
    @Override
    public void onActivityJoin(IPCClient client, String secret) {
        // On Accepting and Queuing a Join Request
        if (StringUtils.isNullOrEmpty(CraftPresence.CLIENT.STATUS) || (!StringUtils.isNullOrEmpty(CraftPresence.CLIENT.STATUS) && !CraftPresence.CLIENT.STATUS.equalsIgnoreCase("joinGame"))) {
            CraftPresence.CLIENT.STATUS = "joinGame";
            CraftPresence.SERVER.verifyAndJoin(secret);
        }
    }

    @Override
    public void onActivitySpectate(IPCClient client, String secret) {
        // Spectating Game, Unimplemented for now
        if (StringUtils.isNullOrEmpty(CraftPresence.CLIENT.STATUS) || (!StringUtils.isNullOrEmpty(CraftPresence.CLIENT.STATUS) && !CraftPresence.CLIENT.STATUS.equalsIgnoreCase("spectateGame"))) {
            CraftPresence.CLIENT.STATUS = "spectateGame";
        }
    }

    @Override
    public void onActivityJoinRequest(IPCClient client, String secret, User user) {
        // On Receiving a New Join Request
        if (StringUtils.isNullOrEmpty(CraftPresence.CLIENT.STATUS) || (!StringUtils.isNullOrEmpty(CraftPresence.CLIENT.STATUS) && (!CraftPresence.CLIENT.STATUS.equalsIgnoreCase("joinRequest") || !CraftPresence.CLIENT.REQUESTER_USER.equals(user)))) {
            CraftPresence.SYSTEM.TIMER = 30;
            CraftPresence.CLIENT.STATUS = "joinRequest";
            CraftPresence.CLIENT.REQUESTER_USER = user;

            if (!(CraftPresence.instance.currentScreen instanceof CommandsGui)) {
                CraftPresence.GUIS.openScreen(new CommandsGui(CraftPresence.instance.currentScreen));
            }
            CommandsGui.executeCommand("request");
        }
    }

    @Override
    public void onClose(IPCClient client, JsonObject json) {
        // N/A
    }

    @Override
    public void onDisconnect(IPCClient client, Throwable t) {
        if (StringUtils.isNullOrEmpty(CraftPresence.CLIENT.STATUS) || (!StringUtils.isNullOrEmpty(CraftPresence.CLIENT.STATUS) && !CraftPresence.CLIENT.STATUS.equalsIgnoreCase("disconnected"))) {
            CraftPresence.CLIENT.STATUS = "disconnected";
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.rpc", t.getMessage()));
            CraftPresence.CLIENT.shutDown();
        }
    }

    @Override
    public void onPacketReceived(IPCClient client, Packet packet) {
        // N/A
    }

    @Override
    public void onPacketSent(IPCClient client, Packet packet) {
        // N/A
    }

    @Override
    public void onReady(IPCClient client) {
        if (StringUtils.isNullOrEmpty(CraftPresence.CLIENT.STATUS) || (!StringUtils.isNullOrEmpty(CraftPresence.CLIENT.STATUS) && !CraftPresence.CLIENT.STATUS.equalsIgnoreCase("ready"))) {
            CraftPresence.CLIENT.STATUS = "ready";
            CraftPresence.CLIENT.CURRENT_USER = client.getCurrentUser();
            ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.load", CraftPresence.CLIENT.CLIENT_ID, CraftPresence.CLIENT.CURRENT_USER != null ? CraftPresence.CLIENT.CURRENT_USER.getName() : "null"));
        }
    }
}
