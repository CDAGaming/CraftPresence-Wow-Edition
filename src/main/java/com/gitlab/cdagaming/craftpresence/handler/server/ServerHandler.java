package com.gitlab.cdagaming.craftpresence.handler.server;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.CommandHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAsset;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ServerHandler {
    private String server_IP;
    private String server_Name;
    private String server_MOTD;
    private int currentPlayers;
    private int maxPlayers;

    private void emptyServerData() {
        server_IP = null;
        server_MOTD = null;
        server_Name = null;
        currentPlayers = 0;
        maxPlayers = 0;
    }

    @SubscribeEvent
    public void serverDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        emptyServerData();
        CommandHandler.setMainMenuPresence();
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
                server_IP = serverData.serverIP;
                server_Name = serverData.serverName;
                server_MOTD = StringHandler.stripColors(serverData.serverMOTD);
                if (StringHandler.isNullOrEmpty(server_Name)) {
                    server_Name = CraftPresence.CONFIG.defaultServerName;
                }
                if (StringHandler.isNullOrEmpty(server_MOTD) || server_MOTD.equalsIgnoreCase(I18n.format("multiplayer.status.cannot_connect")) || server_MOTD.equalsIgnoreCase(I18n.format("multiplayer.status.pinging"))) {
                    server_MOTD = CraftPresence.CONFIG.defaultServerMOTD;
                }
                updateServerPresence();
            }
        }
    }

    private void updateServerPresence() {
        final String defaultServerMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String defaultServerIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 2, CraftPresence.CONFIG.splitCharacter, CraftPresence.CONFIG.defaultServerIcon);
        final String alternateServerMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, server_Name, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultServerMSG);
        final String alternateServerIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, server_Name, 0, 2, CraftPresence.CONFIG.splitCharacter, server_Name);
        final String primaryServerMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, StringHandler.formatIP(server_IP), 0, 1, CraftPresence.CONFIG.splitCharacter, alternateServerMSG);
        final String primaryServerIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, StringHandler.formatIP(server_IP), 0, 2, CraftPresence.CONFIG.splitCharacter, server_IP);
        boolean matched = false;

        CraftPresence.CLIENT.GAME_STATE = primaryServerMSG.replace("&ip&", StringHandler.formatIP(server_IP)).replace("&name&", server_Name).replace("&motd&", server_MOTD).replace("&players&", I18n.format("craftpresence.defaults.placeholder.players", currentPlayers, maxPlayers));
        for (String ipPart : server_IP.split("\\.")) {
            final String formattedKey = StringHandler.formatPackIcon(ipPart);
            if (DiscordAssetHandler.contains(formattedKey)) {
                matched = true;
                CraftPresence.CLIENT.setImage(formattedKey, DiscordAsset.AssetType.SMALL);
                break;
            }
        }

        if (!matched) {
            final String formattedPrimaryKey = StringHandler.formatPackIcon(primaryServerIcon.replace(" ", "_"));
            final String formattedAlternateKey = StringHandler.formatPackIcon(alternateServerIcon.replace(" ", "_"));
            final String formattedDefaultKey = StringHandler.formatPackIcon(defaultServerIcon.replace(" ", "_"));
            if (DiscordAssetHandler.contains(formattedPrimaryKey)) {
                CraftPresence.CLIENT.setImage(formattedPrimaryKey, DiscordAsset.AssetType.SMALL);
            } else if (DiscordAssetHandler.contains(formattedAlternateKey)) {
                CraftPresence.CLIENT.setImage(formattedAlternateKey, DiscordAsset.AssetType.SMALL);
            } else {
                CraftPresence.CLIENT.setImage(formattedDefaultKey, DiscordAsset.AssetType.SMALL);
            }
        }

        CraftPresence.CLIENT.SMALLIMAGETEXT = CraftPresence.CLIENT.GAME_STATE;
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
    }
}
