package com.gitlab.cdagaming.craftpresence.handler.server;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.FileHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAsset;
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
    public String currentServer_IP;
    private String lastAttemptedSecret, currentServer_Name, currentServer_MOTD, currentServerMSG, timeString;
    private int currentPlayers, maxPlayers, serverIndex;
    private ServerData currentServerData;

    private boolean queuedForUpdate = false, connectToServer = false;

    public void emptyData() {
        knownAddresses.clear();
        clearClientData();
    }

    public void clearClientData() {
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

        if (enabled && CraftPresence.player != null && !connectToServer) {
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

        if (connectToServer) {
            joinServer();
            connectToServer = false;
        }
    }

    private void updateServerData() {
        // NOTE: Server-Only Events
        if (!CraftPresence.instance.isSingleplayer() && !connectToServer) {
            currentServerData = CraftPresence.instance.getCurrentServerData();
            final NetHandlerPlayClient connection = CraftPresence.instance.getConnection();

            if (currentServerData != null && connection != null) {
                final String newServer_IP = currentServerData.serverIP;
                final String newServer_Name = !StringHandler.isNullOrEmpty(currentServerData.serverName) ? currentServerData.serverName : CraftPresence.CONFIG.defaultServerName;
                final String newServer_MOTD = !StringHandler.isNullOrEmpty(currentServerData.serverMOTD) && !(currentServerData.serverMOTD.equalsIgnoreCase(I18n.format("multiplayer.status.cannot_connect")) || currentServerData.serverMOTD.equalsIgnoreCase(I18n.format("multiplayer.status.pinging"))) ? StringHandler.stripColors(currentServerData.serverMOTD) : CraftPresence.CONFIG.defaultServerMOTD;
                final int newCurrentPlayers = CraftPresence.instance.getConnection().getPlayerInfoMap().size();
                final int newMaxPlayers = CraftPresence.instance.getConnection().currentServerMaxPlayers;

                if (!newServer_IP.equals(currentServer_IP) || !newServer_MOTD.equals(currentServer_MOTD) || !newServer_Name.equals(currentServer_Name)) {
                    currentServer_IP = newServer_IP;
                    currentServer_MOTD = newServer_MOTD;
                    currentServer_Name = newServer_Name;
                    queuedForUpdate = true;
                }

                if (newCurrentPlayers != currentPlayers || newMaxPlayers != maxPlayers) {
                    currentPlayers = newCurrentPlayers;
                    maxPlayers = newMaxPlayers;
                    if ((!StringHandler.isNullOrEmpty(currentServerMSG) && currentServerMSG.toLowerCase().contains("&players&")) || CraftPresence.CONFIG.enableJoinRequest) {
                        queuedForUpdate = true;
                    }
                }

                final ServerList serverList = new ServerList(CraftPresence.instance);
                serverList.loadServerList();
                if (serverList.countServers() != serverIndex || CraftPresence.CONFIG.serverMessages.length != serverIndex || !knownAddresses.contains(StringHandler.formatIP(currentServer_IP, false))) {
                    getServerAddresses();
                }
            }
        }

        // NOTE: Universal Events (SinglePlayer and MultiPlayer)
        if (CraftPresence.player != null) {
            final String gameTime = getTimeString(CraftPresence.player.world.getWorldTime());

            if (!gameTime.equals(timeString)) {
                timeString = gameTime;
                if (!StringHandler.isNullOrEmpty(currentServerMSG) && currentServerMSG.toLowerCase().contains("&time&")) {
                    queuedForUpdate = true;
                }
            }
        }

        if (queuedForUpdate) {
            if (!CraftPresence.instance.isSingleplayer()) {
                updateServerPresence();
            } else {
                CraftPresence.CLIENT.GAME_STATE = CraftPresence.CONFIG.singleplayerMSG.replace("&ign&", CraftPresence.CONFIG.playerPlaceholderMSG.replace("&name&", Constants.USERNAME)).replace("&time&", CraftPresence.CONFIG.gameTimePlaceholderMSG.replace("&worldtime&", timeString)).replace("&mods&", CraftPresence.CONFIG.modsPlaceholderMSG.replace("&modcount&", Integer.toString(FileHandler.getModCount())));
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
        String formattedKey = CraftPresence.CLIENT.CLIENT_ID + "";
        boolean containsServerName = false;
        boolean containsServerIP = false;

        if (!StringHandler.isNullOrEmpty(currentServer_Name)) {
            formattedKey += "-" + currentServer_Name.toLowerCase();
            containsServerName = true;
        }
        if (!StringHandler.isNullOrEmpty(currentServer_IP)) {
            formattedKey += "-" + currentServer_IP.toLowerCase();
            containsServerIP = true;
        }

        formattedKey += ";" + containsServerName + ";" + containsServerIP;
        return formattedKey;
    }

    public void verifyAndJoin(final String secret) {
        lastAttemptedSecret = secret;
        String[] boolParts = secret.split(";");
        String[] stringParts = boolParts[0].split("-");
        boolean containsValidClientID = StringHandler.elementExists(stringParts, 0) && (stringParts[0].length() == 18 && !stringParts[0].matches(".*[a-z].*") && !stringParts[0].matches(".*[A-Z].*"));
        boolean containsServerName = StringHandler.elementExists(boolParts, 1) && StringHandler.elementExists(stringParts, 1) && Boolean.parseBoolean(boolParts[1]);
        boolean containsServerIP = StringHandler.elementExists(boolParts, 2) && StringHandler.elementExists(stringParts, 2) && Boolean.parseBoolean(boolParts[2]);
        String serverName = containsServerName ? stringParts[1] : CraftPresence.CONFIG.defaultServerName;
        String serverIP = containsServerIP ? stringParts[2] : "";
        boolean isValidSecret = boolParts.length <= 4 && stringParts.length <= 3 && containsValidClientID;

        if (isValidSecret) {
            if (CraftPresence.CONFIG.enableJoinRequest) {
                currentServerData = new ServerData(serverName, serverIP, false);
                connectToServer = true;
            } else {
                Constants.LOG.error(I18n.format("craftpresence.logger.warning.config.disabled.enablejoinrequest"));
            }
        } else {
            Constants.LOG.error(I18n.format("craftpresence.logger.error.discord.join", secret));
        }
    }

    private void joinServer() {
        try {
            CraftPresence.instance.displayGuiScreen(new GuiConnecting(CraftPresence.instance.currentScreen, CraftPresence.instance, currentServerData));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            CraftPresence.CLIENT.handlers.joinGame.accept(lastAttemptedSecret);
        }
    }

    private void updateServerPresence() {
        final String defaultServerMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String alternateServerMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, currentServer_Name, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultServerMSG);
        final String alternateServerIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, currentServer_Name, 0, 2, CraftPresence.CONFIG.splitCharacter, currentServer_Name);

        currentServerMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, StringHandler.formatIP(currentServer_IP, false), 0, 1, CraftPresence.CONFIG.splitCharacter, alternateServerMSG);
        final String currentServerIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, StringHandler.formatIP(currentServer_IP, false), 0, 2, CraftPresence.CONFIG.splitCharacter, alternateServerIcon);
        final String formattedServerIconKey = StringHandler.formatPackIcon(currentServerIcon.replace(" ", "_"));

        CraftPresence.CLIENT.GAME_STATE = currentServerMSG.replace("&ip&", StringHandler.formatIP(currentServer_IP, false)).replace("&name&", currentServer_Name).replace("&motd&", currentServer_MOTD).replace("&players&", CraftPresence.CONFIG.playerAmountPlaceholderMSG.replace("&current&", Integer.toString(currentPlayers)).replace("&max&", Integer.toString(maxPlayers))).replace("&ign&", CraftPresence.CONFIG.playerPlaceholderMSG.replace("&name&", Constants.USERNAME)).replace("&time&", CraftPresence.CONFIG.gameTimePlaceholderMSG.replace("&worldtime&", timeString)).replace("&mods&", CraftPresence.CONFIG.modsPlaceholderMSG.replace("&modcount&", Integer.toString(FileHandler.getModCount())));
        if (CraftPresence.CONFIG.enableJoinRequest) {
            if (!StringHandler.isNullOrEmpty(currentServer_Name) && !currentServer_Name.equalsIgnoreCase(CraftPresence.CONFIG.defaultServerName)) {
                CraftPresence.CLIENT.PARTY_ID = "Join Server: " + currentServer_Name;
            } else {
                CraftPresence.CLIENT.PARTY_ID = "Join Server: " + currentServer_IP;
            }
            CraftPresence.CLIENT.JOIN_SECRET = makeSecret();
            CraftPresence.CLIENT.PARTY_SIZE = currentPlayers;
            CraftPresence.CLIENT.PARTY_MAX = maxPlayers;
        }

        if (!CraftPresence.CONFIG.overwriteServerIcon || !CraftPresence.packFound) {
            CraftPresence.CLIENT.setImage(formattedServerIconKey.replace("&icon&", CraftPresence.CONFIG.defaultServerIcon), DiscordAsset.AssetType.SMALL);
            CraftPresence.CLIENT.SMALLIMAGETEXT = CraftPresence.CLIENT.GAME_STATE;
        }

        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
        queuedForUpdate = false;
    }

    public void getServerAddresses() {
        final ServerList serverList = new ServerList(CraftPresence.instance);
        serverList.loadServerList();
        serverIndex = serverList.countServers();

        for (int currentIndex = 0; currentIndex < serverIndex; currentIndex++) {
            final ServerData data = serverList.getServerData(currentIndex);
            if (!StringHandler.isNullOrEmpty(data.serverIP) && !knownAddresses.contains(StringHandler.formatIP(data.serverIP, false))) {
                knownAddresses.add(StringHandler.formatIP(data.serverIP, false));
            }
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
