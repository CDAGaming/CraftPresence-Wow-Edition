package com.gitlab.cdagaming.craftpresence.handler.server;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.CommandHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAsset;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
    private String timeString;
    private int currentPlayers;
    private int maxPlayers;
    private int serverIndex;

    public void emptyData() {
        knownAddresses.clear();
    }

    @SubscribeEvent
    public void onDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        currentServer_IP = null;
        currentServer_MOTD = null;
        currentServer_Name = null;
        timeString = null;
        currentPlayers = 0;
        maxPlayers = 0;
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
        final EntityPlayer player = minecraft.player;
        if (CraftPresence.CONFIG.showGameState) {
            if (!minecraft.isSingleplayer() && minecraft.getConnection() != null) {
                final int newCurrentPlayers = minecraft.getConnection().getPlayerInfoMap().size();
                final int newMaxPlayers = minecraft.getConnection().currentServerMaxPlayers;

                if (newCurrentPlayers != currentPlayers || newMaxPlayers != maxPlayers) {
                    final String currentPlayerAmountMessage = CraftPresence.CONFIG.playerAmountPlaceholderMSG.replace("&current&", Integer.toString(currentPlayers)).replace("&max&", Integer.toString(maxPlayers));
                    final String newPlayerAmountMessage = CraftPresence.CONFIG.playerAmountPlaceholderMSG.replace("&current&", Integer.toString(newCurrentPlayers)).replace("&max&", Integer.toString(newMaxPlayers));
                    if (CraftPresence.CLIENT.GAME_STATE.contains(currentPlayerAmountMessage)) {
                        CraftPresence.CLIENT.GAME_STATE = CraftPresence.CLIENT.GAME_STATE.replace(currentPlayerAmountMessage, newPlayerAmountMessage);
                        CraftPresence.CLIENT.SMALLIMAGETEXT = CraftPresence.CLIENT.GAME_STATE;
                        CraftPresence.CLIENT.PARTY_SIZE = newCurrentPlayers;
                        CraftPresence.CLIENT.PARTY_MAX = newMaxPlayers;
                        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
                    }
                    currentPlayers = newCurrentPlayers;
                    maxPlayers = newMaxPlayers;
                }
            }
            if (player != null) {
                final String gameTime = getTimeString(player.world.getWorldTime());
                if (StringHandler.isNullOrEmpty(timeString)) {
                    timeString = gameTime;
                }
                if (!gameTime.equals(timeString)) {
                    final String currentGameTimeMessage = CraftPresence.CONFIG.gameTimePlaceholderMSG.replace("&worldtime&", timeString);
                    final String newGameTimeMessage = CraftPresence.CONFIG.gameTimePlaceholderMSG.replace("&worldtime&", gameTime);
                    if (CraftPresence.CLIENT.GAME_STATE.contains(currentGameTimeMessage)) {
                        CraftPresence.CLIENT.GAME_STATE = CraftPresence.CLIENT.GAME_STATE.replace(currentGameTimeMessage, newGameTimeMessage);
                        CraftPresence.CLIENT.SMALLIMAGETEXT = CraftPresence.CLIENT.GAME_STATE;
                        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
                    }
                    timeString = gameTime;
                }
            }
        }
        if (CraftPresence.CONFIG.rebootOnWorldLoad) {
            CommandHandler.rebootRPC();
            CraftPresence.CONFIG.rebootOnWorldLoad = false;
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

    @SubscribeEvent
    public void serverConnect(final EntityJoinWorldEvent event) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        final EntityPlayer player = minecraft.player;

        if (player != null && event.getEntity() == player) {
            if (CraftPresence.CONFIG.showGameState) {
                timeString = getTimeString(player.world.getWorldTime());
                if (!minecraft.isSingleplayer()) {
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
                } else {
                    CraftPresence.CLIENT.GAME_STATE = CraftPresence.CONFIG.singleplayerMSG.replace("&ign&", CraftPresence.CONFIG.playerPlaceholderMSG.replace("&name&", Constants.USERNAME)).replace("&time&", CraftPresence.CONFIG.gameTimePlaceholderMSG.replace("&worldtime&", timeString));
                    CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
                }
            }
        }
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
