package com.gitlab.cdagaming.craftpresence.handler.server;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAsset;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.List;

public class ServerHandler {
    public boolean enabled = false, isInUse = false;

    public List<String> knownAddresses = new ArrayList<>();
    private String currentServer_IP;
    private String currentServer_Name;
    private String currentServer_MOTD;
    private String timeString;
    private int currentPlayers;
    private int maxPlayers;
    private int serverIndex;

    private boolean queuedForUpdate = false;

    public void emptyData() {
        knownAddresses.clear();
        clearClientData();
    }

    private void clearClientData() {
        currentServer_IP = null;
        currentServer_MOTD = null;
        currentServer_Name = null;
        timeString = null;
        currentPlayers = 0;
        maxPlayers = 0;

        queuedForUpdate = false;
        isInUse = false;
    }

    public void onTick() {
        enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.CONFIG.showGameState : enabled;
        final boolean needsUpdate = enabled && knownAddresses.isEmpty();

        if (needsUpdate) {
            getServerAddresses();
        }

        if (enabled && CraftPresence.player != null) {
            updateServerData();
            isInUse = true;
        }

        if (isInUse) {
            if (enabled && CraftPresence.player == null) {
                clearClientData();
            } else if (!enabled) {
                emptyData();
            }
        }
    }

    private void updateServerData() {
        // NOTE: Server-Only Events
        if (!CraftPresence.instance.isSingleplayer()) {
            final ServerData serverData = CraftPresence.instance.getCurrentServerData();
            final NetHandlerPlayClient connection = CraftPresence.instance.getConnection();

            if (serverData != null && connection != null) {
                final String newServer_IP = serverData.serverIP;
                final String newServer_Name = serverData.serverName;
                final String newServer_MOTD = StringHandler.stripColors(serverData.serverMOTD);
                final int newCurrentPlayers = CraftPresence.instance.getConnection().getPlayerInfoMap().size();
                final int newMaxPlayers = CraftPresence.instance.getConnection().currentServerMaxPlayers;

                if (StringHandler.isNullOrEmpty(newServer_Name)) {
                    currentServer_Name = CraftPresence.CONFIG.defaultServerName;
                }
                if (StringHandler.isNullOrEmpty(newServer_MOTD) || newServer_MOTD.equalsIgnoreCase(I18n.format("multiplayer.status.cannot_connect")) || newServer_MOTD.equalsIgnoreCase(I18n.format("multiplayer.status.pinging"))) {
                    currentServer_MOTD = CraftPresence.CONFIG.defaultServerMOTD;
                }

                if (!newServer_IP.equals(currentServer_IP) || !newServer_MOTD.equals(currentServer_MOTD) || !newServer_Name.equals(currentServer_Name)) {
                    currentServer_IP = newServer_IP;
                    currentServer_MOTD = newServer_MOTD;
                    currentServer_Name = newServer_Name;
                    queuedForUpdate = true;
                }

                if (newCurrentPlayers != currentPlayers || newMaxPlayers != maxPlayers) {
                    currentPlayers = newCurrentPlayers;
                    maxPlayers = newMaxPlayers;
                    queuedForUpdate = true;
                }

                final ServerList serverList = new ServerList(CraftPresence.instance);
                serverList.loadServerList();
                if (serverList.countServers() != serverIndex || CraftPresence.CONFIG.serverMessages.length != serverIndex || !knownAddresses.contains(StringHandler.formatIP(currentServer_IP))) {
                    getServerAddresses();
                }
            }
        }

        // NOTE: Universal Events
        if (CraftPresence.player != null) {
            final String gameTime = getTimeString(CraftPresence.player.world.getWorldTime());

            if (!gameTime.equals(timeString)) {
                timeString = gameTime;
                queuedForUpdate = true;
            }
        }

        if (queuedForUpdate) {
            if (!CraftPresence.instance.isSingleplayer()) {
                updateServerPresence();
            } else {
                CraftPresence.CLIENT.GAME_STATE = CraftPresence.CONFIG.singleplayerMSG.replace("&ign&", CraftPresence.CONFIG.playerPlaceholderMSG.replace("&name&", Constants.USERNAME)).replace("&time&", CraftPresence.CONFIG.gameTimePlaceholderMSG.replace("&worldtime&", timeString));
                CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
                queuedForUpdate = false;
            }
        }
    }

    private String getTimeString(long worldTime) {
        long hour = 0;
        long minute = 0;
        long dayLength = 24000;
        long remainingTicks = worldTime % dayLength;

        while (remainingTicks >= 1000) {
            remainingTicks -= 1000;
            hour++;
            if (hour > 24)
                hour -= 24;
        }
        remainingTicks *= 3;
        while (remainingTicks >= 50) {
            remainingTicks -= 50;
            minute++;
        }

        String formattedHour = String.valueOf(hour).length() == 1 ? String.format("%02d", hour) : String.valueOf(hour);
        String formattedMinute = String.valueOf(minute).length() == 1 ? String.format("%02d", minute) : String.valueOf(minute);

        return formattedHour + ":" + formattedMinute;
    }

    private String makeSecret() {
        long currentTimeStamp = System.currentTimeMillis() / 1000L;
        String formattedKey = currentTimeStamp + "";
        boolean containsServerName;
        boolean containsServerIP;

        if (!StringHandler.isNullOrEmpty(currentServer_Name) && !currentServer_Name.equalsIgnoreCase(CraftPresence.CONFIG.defaultServerName)) {
            formattedKey += "-" + currentServer_Name.toLowerCase();
            containsServerName = true;
            containsServerIP = false;
        } else {
            formattedKey += "-" + currentServer_IP.toLowerCase();
            containsServerName = false;
            containsServerIP = true;
        }
        formattedKey += ";" + containsServerName + ";" + containsServerIP;
        return formattedKey;
    }

    // TODO: Verify that this Works once Discord allows all javaw.exe's for MC
    public void verifyAndJoin(final String secret) {
        String[] boolParts = secret.split(";");
        String[] stringParts = secret.split("-");
        boolean containsValidTimeStamp = StringHandler.elementExists(stringParts, 0) && stringParts[0].length() <= 11;
        boolean containsServerName = StringHandler.elementExists(boolParts, 1) && StringHandler.elementExists(stringParts, 1) && Boolean.parseBoolean(boolParts[1]);
        boolean containsServerIP = StringHandler.elementExists(boolParts, 2) && StringHandler.elementExists(stringParts, 1) && Boolean.parseBoolean(boolParts[2]);
        String serverName = containsServerName && !containsServerIP ? stringParts[1] : CraftPresence.CONFIG.defaultServerName;
        String serverIP = containsServerIP && !containsServerName ? stringParts[1] : "";
        boolean isValidSecret = boolParts.length <= 4 && stringParts.length <= 3 && maxPlayers != 0 && containsValidTimeStamp;

        if (isValidSecret) {
            ServerData serverData = new ServerData(serverName, serverIP, false);
            Minecraft mc = Minecraft.getMinecraft();

            mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), mc, serverData));
            CraftPresence.CLIENT.handlers.joinGame.accept(secret);
        } else {
            Constants.LOG.error("Secret Key Invalid, Join Request Rejected: " + secret);
        }
    }

    private void updateServerPresence() {
        final String defaultServerMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String defaultServerIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 2, CraftPresence.CONFIG.splitCharacter, CraftPresence.CONFIG.defaultServerIcon);
        final String alternateServerMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, currentServer_Name, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultServerMSG);
        final String alternateServerIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, currentServer_Name, 0, 2, CraftPresence.CONFIG.splitCharacter, defaultServerIcon);

        final String currentServerMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, StringHandler.formatIP(currentServer_IP), 0, 1, CraftPresence.CONFIG.splitCharacter, alternateServerMSG);
        final String currentServerIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, StringHandler.formatIP(currentServer_IP), 0, 2, CraftPresence.CONFIG.splitCharacter, alternateServerIcon);
        final String formattedServerIconKey = StringHandler.formatPackIcon(currentServerIcon.replace(" ", "_"));

        CraftPresence.CLIENT.GAME_STATE = currentServerMSG.replace("&ip&", StringHandler.formatIP(currentServer_IP)).replace("&name&", currentServer_Name).replace("&motd&", currentServer_MOTD).replace("&players&", CraftPresence.CONFIG.playerAmountPlaceholderMSG.replace("&current&", Integer.toString(currentPlayers)).replace("&max&", Integer.toString(maxPlayers))).replace("&ign&", CraftPresence.CONFIG.playerPlaceholderMSG.replace("&name&", Constants.USERNAME)).replace("&time&", CraftPresence.CONFIG.gameTimePlaceholderMSG.replace("&worldtime&", timeString));
        if (!StringHandler.isNullOrEmpty(currentServer_Name) && !currentServer_Name.equalsIgnoreCase(CraftPresence.CONFIG.defaultServerName)) {
            CraftPresence.CLIENT.PARTY_ID = "Join Server: " + currentServer_Name;
        } else {
            CraftPresence.CLIENT.PARTY_ID = "Join Server: " + currentServer_IP;
        }
        CraftPresence.CLIENT.JOIN_SECRET = makeSecret();

        if (!CraftPresence.CONFIG.overwriteServerIcon || !CraftPresence.packFound) {
            if (DiscordAssetHandler.contains(formattedServerIconKey) && !currentServerIcon.equals(defaultServerIcon)) {
                CraftPresence.CLIENT.setImage(formattedServerIconKey.replace("&icon&", CraftPresence.CONFIG.defaultServerIcon), DiscordAsset.AssetType.SMALL);
            } else {
                boolean matched = false;
                for (String ipPart : currentServer_IP.split("\\.")) {
                    final String formattedKey = StringHandler.formatPackIcon(ipPart);
                    if (DiscordAssetHandler.contains(formattedKey)) {
                        CraftPresence.CLIENT.setImage(formattedKey, DiscordAsset.AssetType.SMALL);
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    CraftPresence.CLIENT.setImage(formattedServerIconKey.replace("&icon&", CraftPresence.CONFIG.defaultServerIcon), DiscordAsset.AssetType.SMALL);
                }
            }
            CraftPresence.CLIENT.SMALLIMAGETEXT = CraftPresence.CLIENT.GAME_STATE;
        }

        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
        queuedForUpdate = false;
    }

    public void getServerAddresses() {
        final ServerList serverList = new ServerList(Minecraft.getMinecraft());
        serverList.loadServerList();
        int currentIndex = 0;
        serverIndex = serverList.countServers();

        while (currentIndex < serverIndex) {
            final ServerData data = serverList.getServerData(currentIndex);
            if (!StringHandler.isNullOrEmpty(data.serverIP) && !knownAddresses.contains(StringHandler.formatIP(data.serverIP))) {
                knownAddresses.add(StringHandler.formatIP(data.serverIP));
            }
            currentIndex++;
        }

        for (String serverMessage : CraftPresence.CONFIG.serverMessages) {
            if (!StringHandler.isNullOrEmpty(serverMessage)) {
                final String[] part = serverMessage.split(CraftPresence.CONFIG.splitCharacter);
                if (!StringHandler.isNullOrEmpty(part[0]) && !knownAddresses.contains(part[0])) {
                    knownAddresses.add(part[0]);
                }
            }
        }
    }
}
