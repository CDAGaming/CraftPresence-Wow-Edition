package com.gitlab.cdagaming.craftpresence.handler.server;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.CommandHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAsset;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.ArrayList;
import java.util.List;

public class ServerHandler {
    public List<String> knownAddresses = new ArrayList<>();
    private String currentServer_IP;
    private String currentServer_Name;
    private String currentServer_MOTD;
    private int currentPlayers;
    private int maxPlayers;
    private int serverIndex;

    private void emptyServerData() {
        currentServer_IP = null;
        currentServer_MOTD = null;
        currentServer_Name = null;
        currentPlayers = 0;
        maxPlayers = 0;
    }

    @SubscribeEvent
    public void serverDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        emptyServerData();
        CommandHandler.setMainMenuPresence();

        final ServerList serverList = new ServerList(Minecraft.getMinecraft());
        serverList.loadServerList();

        if (serverList.countServers() != serverIndex || CraftPresence.CONFIG.serverMessages.length != serverIndex) {
            getServerAddresses();
        }
    }

    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        if (CraftPresence.CONFIG.showGameState && !minecraft.isSingleplayer() && minecraft.getConnection() != null) {
            int newCurrentPlayers = minecraft.getConnection().getPlayerInfoMap().size();
            int newMaxPlayers = minecraft.getConnection().currentServerMaxPlayers;

            if (newCurrentPlayers != currentPlayers || newMaxPlayers != maxPlayers) {
                if (CraftPresence.CLIENT.GAME_STATE.contains(I18n.format("craftpresence.defaults.placeholder.players", currentPlayers, maxPlayers))) {
                    CraftPresence.CLIENT.GAME_STATE = CraftPresence.CLIENT.GAME_STATE.replace(I18n.format("craftpresence.defaults.placeholder.players", currentPlayers, maxPlayers), I18n.format("craftpresence.defaults.placeholder.players", newCurrentPlayers, newMaxPlayers));
                    CraftPresence.CLIENT.SMALLIMAGETEXT = CraftPresence.CLIENT.GAME_STATE;
                    CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
                }
                currentPlayers = newCurrentPlayers;
                maxPlayers = newMaxPlayers;
            }
        }
    }

    @SubscribeEvent
    public void serverConnect(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        if (CraftPresence.CONFIG.showGameState && !minecraft.isSingleplayer()) {
            final ServerData serverData = minecraft.getCurrentServerData();
            if (serverData != null) {
                currentServer_IP = serverData.serverIP;
                currentServer_Name = serverData.serverName;
                currentServer_MOTD = StringHandler.stripColors(serverData.serverMOTD);
                if (StringHandler.isNullOrEmpty(currentServer_Name)) {
                    currentServer_Name = CraftPresence.CONFIG.defaultServerName;
                }
                if (StringHandler.isNullOrEmpty(currentServer_MOTD) || currentServer_MOTD.equalsIgnoreCase(I18n.format("multiplayer.status.cannot_connect")) || currentServer_MOTD.equalsIgnoreCase(I18n.format("multiplayer.status.pinging"))) {
                    currentServer_MOTD = CraftPresence.CONFIG.defaultServerMOTD;
                }
                updateServerPresence();
            }
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
        CraftPresence.CLIENT.GAME_STATE = currentServerMSG.replace("&ip&", StringHandler.formatIP(currentServer_IP)).replace("&name&", currentServer_Name).replace("&motd&", currentServer_MOTD).replace("&players&", I18n.format("craftpresence.defaults.placeholder.players", currentPlayers, maxPlayers));

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
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
    }

    public void getServerAddresses() {
        final ServerList serverList = new ServerList(Minecraft.getMinecraft());
        serverList.loadServerList();
        int currentIndex = -1;
        serverIndex = serverList.countServers();

        while (currentIndex != serverIndex) {
            try {
                currentIndex++;
                final ServerData data = serverList.getServerData(currentIndex);
                knownAddresses.add(data.serverIP);
            } catch (Exception ignored) {
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
