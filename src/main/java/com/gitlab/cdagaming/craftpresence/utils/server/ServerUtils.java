/*
 * MIT License
 *
 * Copyright (c) 2018 - 2019 CDAGaming (cstack2011@yahoo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.gitlab.cdagaming.craftpresence.utils.server;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.NetHandlerPlayClient;

import java.util.List;

/**
 * Server Utilities used to Parse Server Data and handle related RPC Events
 *
 * @author CDAGaming
 */
public class ServerUtils {
    /**
     * Whether this module is active and currently in use
     */
    public boolean isInUse = false;

    /**
     * Whether this module is allowed to start and enabled
     */
    public boolean enabled = false;

    /**
     * A List of the detected Server Addresses
     */
    public List<String> knownAddresses = Lists.newArrayList();

    /**
     * The IP Address of the Current Server the Player is in
     */
    private String currentServer_IP;

    /**
     * The Name of the Current Server the Player is in
     */
    private String currentServer_Name;

    /**
     * The Message of the Day of the Current Server the Player is in
     */
    private String currentServer_MOTD;

    /**
     * The Current Server RPC Message being used, with Arguments
     */
    private String currentServerMSG;

    /**
     * The Current Formatted World Time, as a String
     */
    private String timeString;

    /**
     * The Current World's Difficulty
     */
    private String currentDifficulty;

    /**
     * The Current World's Name
     */
    private String currentWorldName;

    /**
     * The Amount of Players in the Current Server the Player is in
     */
    private int currentPlayers;

    /**
     * The Maximum Amount of Players allowed in the Current Server the Player is in
     */
    private int maxPlayers;

    /**
     * The amount of Currently detected Server Addresses
     */
    private int serverIndex;

    /**
     * Mapping storing the Current X and Z Position of the Player in a World
     */
    private Tuple<Double, Double> currentCoordinates = new Tuple<>(0.0D, 0.0D);

    /**
     * Mapping storing the Current and Maximum Health the Player currently has in a World
     */
    private Tuple<Float, Float> currentHealth = new Tuple<>(0.0f, 0.0f);

    /**
     * The Current Server Connection Data and Info
     */
    private ServerData currentServerData;

    /**
     * The Queued Server Connection Data and Info to Join, if any
     */
    private ServerData requestedServerData;

    /**
     * The Player's Current Connection Data
     */
    private NetHandlerPlayClient currentConnection;

    /**
     * If the RPC needs to be Updated or Re-Synchronized<p>
     * Needed here for Multiple-Condition RPC Triggers
     */
    private boolean queuedForUpdate = false;

    /**
     * If in Progress of Joining a World/Server from another World/Server
     */
    private boolean joinInProgress = false;

    /**
     * If the Current Server is on a LAN-Based Connection (A Local Network Game)
     */
    private boolean isOnLAN = false;

    /**
     * Clears FULL Data from this Module
     */
    private void emptyData() {
        knownAddresses.clear();
        clearClientData();
    }

    /**
     * Clears Runtime Client Data from this Module (PARTIAL Clear)
     */
    public void clearClientData() {
        currentServer_IP = null;
        currentServer_MOTD = null;
        currentServer_Name = null;
        currentServerData = null;
        currentConnection = null;
        currentCoordinates = new Tuple<>(0.0D, 0.0D);
        currentHealth = new Tuple<>(0.0f, 0.0f);
        currentDifficulty = null;
        currentWorldName = null;
        timeString = null;
        currentPlayers = 0;
        maxPlayers = 0;

        queuedForUpdate = false;
        isOnLAN = false;
        isInUse = false;

        if (!joinInProgress) {
            requestedServerData = null;
        }

        CraftPresence.CLIENT.initArgumentData("&SERVER&");
        CraftPresence.CLIENT.initIconData("&SERVER&");
    }

    /**
     * Module Event to Occur on each tick within the Application
     */
    public void onTick() {
        joinInProgress = StringUtils.isNullOrEmpty(CraftPresence.CLIENT.STATUS) || (CraftPresence.CLIENT.STATUS.equalsIgnoreCase("joinGame") || CraftPresence.CLIENT.STATUS.equalsIgnoreCase("spectateGame"));
        enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.CONFIG.showGameState : enabled;
        final boolean needsUpdate = enabled && knownAddresses.isEmpty();

        if (needsUpdate) {
            getServerAddresses();
        }

        if (enabled) {
            if (CraftPresence.player != null && !joinInProgress) {
                isInUse = true;
                updateServerData();
            } else {
                clearClientData();
            }
        } else {
            emptyData();
        }

        if (joinInProgress && requestedServerData != null) {
            joinServer(requestedServerData);
        }
    }

    /**
     * Synchronizes Data related to this module, if needed
     */
    private void updateServerData() {
        final ServerData newServerData = CraftPresence.instance.getCurrentServerData();
        final NetHandlerPlayClient newConnection = CraftPresence.instance.getConnection();

        if (!joinInProgress) {
            final int newCurrentPlayers = newConnection != null ? newConnection.getPlayerInfoMap().size() : 1;
            final int newMaxPlayers = newConnection != null && newConnection.currentServerMaxPlayers >= newCurrentPlayers ? newConnection.currentServerMaxPlayers : newCurrentPlayers + 1;
            final boolean newLANStatus = (CraftPresence.instance.isSingleplayer() && newCurrentPlayers > 1) || (newServerData != null && newServerData.isOnLAN());

            final String newServer_IP = newServerData != null && !StringUtils.isNullOrEmpty(newServerData.serverIP) ? newServerData.serverIP : "127.0.0.1";
            final String newServer_Name = newServerData != null && !StringUtils.isNullOrEmpty(newServerData.serverName) ? newServerData.serverName : CraftPresence.CONFIG.defaultServerName;
            final String newServer_MOTD = !isOnLAN && !CraftPresence.instance.isSingleplayer() && (newServerData != null && !StringUtils.isNullOrEmpty(newServerData.serverMOTD)) &&
                    !(newServerData.serverMOTD.equalsIgnoreCase(ModUtils.TRANSLATOR.translate("craftpresence.multiplayer.status.cannot_connect")) ||
                            newServerData.serverMOTD.equalsIgnoreCase(ModUtils.TRANSLATOR.translate("craftpresence.multiplayer.status.cannot_resolve")) ||
                            newServerData.serverMOTD.equalsIgnoreCase(ModUtils.TRANSLATOR.translate("craftpresence.multiplayer.status.polling")) ||
                            newServerData.serverMOTD.equalsIgnoreCase(ModUtils.TRANSLATOR.translate("craftpresence.multiplayer.status.pinging"))) ? StringUtils.stripColors(newServerData.serverMOTD) : CraftPresence.CONFIG.defaultServerMOTD;

            if (newLANStatus != isOnLAN || ((newServerData != null && !newServerData.equals(currentServerData)) ||
                    (newServerData == null && currentServerData != null)) ||
                    (newConnection != null && !newConnection.equals(currentConnection)) || !newServer_IP.equals(currentServer_IP) ||
                    (!StringUtils.isNullOrEmpty(newServer_MOTD) && !newServer_MOTD.equals(currentServer_MOTD)) ||
                    (!StringUtils.isNullOrEmpty(newServer_Name) && !newServer_Name.equals(currentServer_Name))) {
                currentServer_IP = newServer_IP;
                currentServer_MOTD = newServer_MOTD;
                currentServer_Name = newServer_Name;
                currentServerData = newServerData;
                currentConnection = newConnection;
                isOnLAN = newLANStatus;
                queuedForUpdate = true;

                if (!StringUtils.isNullOrEmpty(currentServer_IP) && !knownAddresses.contains(currentServer_IP.contains(":") ? currentServer_IP : StringUtils.formatIP(currentServer_IP, false))) {
                    knownAddresses.add(currentServer_IP.contains(":") ? currentServer_IP : StringUtils.formatIP(currentServer_IP, false));
                }

                final ServerList serverList = new ServerList(CraftPresence.instance);
                serverList.loadServerList();
                if (serverList.countServers() != serverIndex || CraftPresence.CONFIG.serverMessages.length != serverIndex) {
                    getServerAddresses();
                }
            }

            // NOTE: Universal Events
            if (!StringUtils.isNullOrEmpty(currentServerMSG)) {
                // &time& Argument = Current Time in World
                if (currentServerMSG.toLowerCase().contains("&time&")) {
                    final String newGameTime = CraftPresence.player != null ? getTimeString(CraftPresence.player.world.getWorldTime()) : null;
                    if (!StringUtils.isNullOrEmpty(newGameTime) && !newGameTime.equals(timeString)) {
                        timeString = newGameTime;
                        queuedForUpdate = true;
                    }
                }

                // &PLAYERINFO& Sub-Arguments
                if (currentServerMSG.toLowerCase().contains("&playerinfo&")) {
                    // &coords& Argument = Current Coordinates of Player
                    if (CraftPresence.CONFIG.playerPlaceholderMSG.toLowerCase().contains("&coords&")) {
                        final Tuple<Double, Double> newCoordinates = CraftPresence.player != null ? new Tuple<>(StringUtils.roundDouble(CraftPresence.player.posX, 3), StringUtils.roundDouble(CraftPresence.player.posZ, 3)) : new Tuple<>(0.0D, 0.0D);
                        if (!newCoordinates.equals(currentCoordinates)) {
                            currentCoordinates = newCoordinates;
                            queuedForUpdate = true;
                        }
                    }

                    // &health& Argument = Current and Maximum Health of Player
                    if (CraftPresence.CONFIG.playerPlaceholderMSG.toLowerCase().contains("&health&")) {
                        final Tuple<Float, Float> newHealth = CraftPresence.player != null ? new Tuple<>(CraftPresence.player.getHealth(), CraftPresence.player.getMaxHealth()) : new Tuple<>(0.0f, 0.0f);
                        if (!newHealth.equals(currentHealth)) {
                            currentHealth = newHealth;
                            queuedForUpdate = true;
                        }
                    }
                }

                // &WORLDINFO& Sub-Arguments
                if (currentServerMSG.toLowerCase().contains("&worldinfo")) {
                    // &difficulty& Argument = Current Difficulty of the World
                    if (CraftPresence.CONFIG.worldPlaceholderMSG.toLowerCase().contains("&difficulty")) {
                        final String newDifficulty = CraftPresence.player != null ? CraftPresence.player.world.getDifficulty().name() : "";
                        if (!newDifficulty.equals(currentDifficulty)) {
                            currentDifficulty = newDifficulty;
                            queuedForUpdate = true;
                        }
                    }

                    // &worldname& Argument = Current Name of the World
                    if (CraftPresence.CONFIG.worldPlaceholderMSG.toLowerCase().contains("&worldname&")) {
                        final String newWorldName = CraftPresence.player != null ? CraftPresence.player.world.getWorldInfo().getWorldName() : "";
                        if (!newWorldName.equals(currentWorldName)) {
                            currentWorldName = newWorldName;
                            queuedForUpdate = true;
                        }
                    }
                }

                // &players& Argument = Current and Maximum Allowed Players in Server/World
                if ((currentServerMSG.toLowerCase().contains("&players&") || CraftPresence.CONFIG.enableJoinRequest) && (newCurrentPlayers != currentPlayers || newMaxPlayers != maxPlayers)) {
                    currentPlayers = newCurrentPlayers;
                    maxPlayers = newMaxPlayers;
                    queuedForUpdate = true;
                }
            }
        }

        if (queuedForUpdate) {
            updateServerPresence();
        }
    }

    /**
     * Converts a Raw World Time Long into a Readable 24-Hour Time String
     *
     * @param worldTime The raw World Time
     * @return The converted and readable 24-hour time string
     */
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

    /**
     * Creates a Secret Key to use in Sending Requested Server Data from Discord Join Requests
     *
     * @return The Parsable Secret Key
     */
    private String makeSecret() {
        String formattedKey = CraftPresence.CLIENT.CLIENT_ID + "";
        boolean containsServerName = false;
        boolean containsServerIP = false;

        if (!StringUtils.isNullOrEmpty(currentServer_Name)) {
            formattedKey += "-" + currentServer_Name.toLowerCase();
            containsServerName = true;
        }
        if (!StringUtils.isNullOrEmpty(currentServer_IP)) {
            formattedKey += "-" + currentServer_IP.toLowerCase();
            containsServerIP = true;
        }

        formattedKey += ";" + containsServerName + ";" + containsServerIP;
        return formattedKey;
    }

    /**
     * Verifies the Inputted secret Key, and upon match, Form Server Data to join a Server
     *
     * @param secret The secret key to test against for validity
     */
    public void verifyAndJoin(final String secret) {
        String[] boolParts = secret.split(";");
        String[] stringParts = boolParts[0].split("-");
        boolean containsValidClientID = StringUtils.elementExists(stringParts, 0) && (stringParts[0].length() == 18 && StringUtils.getValidLong(stringParts[0]).getFirst());
        boolean containsServerName = StringUtils.elementExists(boolParts, 1) && StringUtils.elementExists(stringParts, 1) && Boolean.parseBoolean(boolParts[1]);
        boolean containsServerIP = StringUtils.elementExists(boolParts, 2) && StringUtils.elementExists(stringParts, 2) && Boolean.parseBoolean(boolParts[2]);
        String serverName = containsServerName ? stringParts[1] : CraftPresence.CONFIG.defaultServerName;
        String serverIP = containsServerIP ? stringParts[2] : "";
        boolean isValidSecret = boolParts.length <= 4 && stringParts.length <= 3 && containsValidClientID;

        if (isValidSecret) {
            if (CraftPresence.CONFIG.enableJoinRequest) {
                requestedServerData = new ServerData(serverName, serverIP, false);
            } else {
                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.warning.config.disabled.enablejoinrequest"));
            }
        } else {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.discord.join", secret));
        }
    }

    /**
     * Joins a Server/World based on Server Data requested
     *
     * @param serverData The Requested Server Data to Join
     */
    private void joinServer(final ServerData serverData) {
        try {
            if (CraftPresence.player != null) {
                CraftPresence.player.world.sendQuittingDisconnectingPacket();
                CraftPresence.instance.loadWorld(null);
            }
            CraftPresence.GUIS.openScreen(new GuiConnecting(CraftPresence.instance.currentScreen != null ? CraftPresence.instance.currentScreen : new GuiMainMenu(), CraftPresence.instance, serverData));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            requestedServerData = null;
        }
    }

    /**
     * Updates RPC Data related to this Module
     */
    public void updateServerPresence() {
        // Form General Argument Lists & Sub Argument Lists
        List<Tuple<String, String>> playerDataArgs = Lists.newArrayList(), worldDataArgs = Lists.newArrayList();

        List<Tuple<String, String>> coordinateArgs = Lists.newArrayList(), healthArgs = Lists.newArrayList();

        coordinateArgs.add(new Tuple<>("&xPosition&", currentCoordinates.getFirst().toString()));
        coordinateArgs.add(new Tuple<>("&zPosition&", currentCoordinates.getSecond().toString()));

        healthArgs.add(new Tuple<>("&CURRENT&", currentHealth.getFirst().toString()));
        healthArgs.add(new Tuple<>("&MAX&", currentHealth.getSecond().toString()));

        // Player Data Arguments
        playerDataArgs.add(new Tuple<>("&COORDS&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.playerCoordinatePlaceholderMSG, coordinateArgs)));
        playerDataArgs.add(new Tuple<>("&HEALTH&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.playerHealthPlaceholderMSG, healthArgs)));

        // World Data Arguments
        worldDataArgs.add(new Tuple<>("&DIFFICULTY&", !StringUtils.isNullOrEmpty(currentDifficulty) ? currentDifficulty : ""));
        worldDataArgs.add(new Tuple<>("&WORLDNAME&", !StringUtils.isNullOrEmpty(currentWorldName) ? currentWorldName : ""));
        worldDataArgs.add(new Tuple<>("&WORLDTIME&", !StringUtils.isNullOrEmpty(timeString) ? timeString : ""));

        if (!CraftPresence.instance.isSingleplayer() && currentServerData != null) {
            String CURRENT_SERVER_ICON;

            // Form Tuple List of Argument for Servers/LAN Games
            List<Tuple<String, String>> serverArgs = Lists.newArrayList(), playerAmountArgs = Lists.newArrayList();

            // Player Amount Arguments
            playerAmountArgs.add(new Tuple<>("&CURRENT&", Integer.toString(currentPlayers)));
            playerAmountArgs.add(new Tuple<>("&MAX&", Integer.toString(maxPlayers)));

            // Server Data Arguments
            serverArgs.add(new Tuple<>("&IP&", StringUtils.formatIP(currentServer_IP, false)));
            serverArgs.add(new Tuple<>("&NAME&", currentServer_Name));
            serverArgs.add(new Tuple<>("&MOTD&", currentServer_MOTD));
            serverArgs.add(new Tuple<>("&PLAYERS&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.playerAmountPlaceholderMSG, playerAmountArgs)));
            serverArgs.add(new Tuple<>("&PLAYERINFO&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.playerPlaceholderMSG, playerDataArgs)));
            serverArgs.add(new Tuple<>("&WORLDINFO&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.worldPlaceholderMSG, worldDataArgs)));

            // Add All Generalized Arguments, if any
            if (!CraftPresence.CLIENT.generalArgs.isEmpty()) {
                serverArgs.addAll(CraftPresence.CLIENT.generalArgs);
            }

            if (isOnLAN) {
                // NOTE: LAN-Only Presence Updates
                final String alternateServerIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, currentServer_Name, 0, 2, CraftPresence.CONFIG.splitCharacter, currentServer_Name);
                final String currentServerIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, StringUtils.formatIP(currentServer_IP, false), 0, 2, CraftPresence.CONFIG.splitCharacter, alternateServerIcon);
                final String formattedServerIconKey = StringUtils.formatPackIcon(currentServerIcon.replace(" ", "_"));

                CURRENT_SERVER_ICON = formattedServerIconKey.replace("&icon&", CraftPresence.CONFIG.defaultServerIcon);

                currentServerMSG = CraftPresence.CONFIG.lanMSG;
            } else {
                // NOTE: Server-Only Presence Updates
                final String defaultServerMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                final String alternateServerMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, currentServer_Name, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultServerMSG);
                final String alternateServerIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, currentServer_Name, 0, 2, CraftPresence.CONFIG.splitCharacter, currentServer_Name);

                currentServerMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, StringUtils.formatIP(currentServer_IP, false), 0, 1, CraftPresence.CONFIG.splitCharacter, alternateServerMSG);
                final String currentServerIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, StringUtils.formatIP(currentServer_IP, false), 0, 2, CraftPresence.CONFIG.splitCharacter, alternateServerIcon);
                final String formattedServerIconKey = StringUtils.formatPackIcon(currentServerIcon.replace(" ", "_"));

                CURRENT_SERVER_ICON = formattedServerIconKey.replace("&icon&", CraftPresence.CONFIG.defaultServerIcon);

                if (CraftPresence.CONFIG.enableJoinRequest) {
                    if (!StringUtils.isNullOrEmpty(currentServer_Name) && !currentServer_Name.equalsIgnoreCase(CraftPresence.CONFIG.defaultServerName)) {
                        CraftPresence.CLIENT.PARTY_ID = "Join Server: " + currentServer_Name;
                    } else {
                        CraftPresence.CLIENT.PARTY_ID = "Join Server: " + currentServer_IP;
                    }
                    CraftPresence.CLIENT.JOIN_SECRET = makeSecret();
                    CraftPresence.CLIENT.PARTY_SIZE = currentPlayers;
                    CraftPresence.CLIENT.PARTY_MAX = maxPlayers;
                }
            }

            CraftPresence.CLIENT.syncArgument("&SERVER&", StringUtils.sequentialReplaceAnyCase(currentServerMSG, serverArgs), false);
            CraftPresence.CLIENT.syncArgument("&SERVER&", CraftPresence.CLIENT.imageOf(CURRENT_SERVER_ICON, CraftPresence.CONFIG.defaultServerIcon, true), true);
            queuedForUpdate = false;
        } else if (CraftPresence.instance.isSingleplayer()) {
            // Form SinglePlayer Tuple Argument List
            List<Tuple<String, String>> soloArgs = Lists.newArrayList();

            soloArgs.add(new Tuple<>("&PLAYERINFO&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.playerPlaceholderMSG, playerDataArgs)));
            soloArgs.add(new Tuple<>("&WORLDINFO&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.worldPlaceholderMSG, worldDataArgs)));

            // Add All Generalized Arguments, if any
            if (!CraftPresence.CLIENT.generalArgs.isEmpty()) {
                soloArgs.addAll(CraftPresence.CLIENT.generalArgs);
            }

            // NOTE: SinglePlayer-Only Presence Updates
            currentServerMSG = CraftPresence.CONFIG.singleplayerMSG;

            CraftPresence.CLIENT.syncArgument("&SERVER&", StringUtils.sequentialReplaceAnyCase(currentServerMSG, soloArgs), false);
            CraftPresence.CLIENT.initIconData("&SERVER&");
            queuedForUpdate = false;
        }
    }

    /**
     * Retrieves and Synchronizes detected Server Addresses from the Server List
     */
    public void getServerAddresses() {
        final ServerList serverList = new ServerList(CraftPresence.instance);
        serverList.loadServerList();
        serverIndex = serverList.countServers();

        for (int currentIndex = 0; currentIndex < serverIndex; currentIndex++) {
            final ServerData data = serverList.getServerData(currentIndex);
            if (!StringUtils.isNullOrEmpty(data.serverIP) && !knownAddresses.contains(data.serverIP.contains(":") ? data.serverIP : StringUtils.formatIP(data.serverIP, false))) {
                knownAddresses.add(data.serverIP.contains(":") ? data.serverIP : StringUtils.formatIP(data.serverIP, false));
            }
        }

        for (String serverMessage : CraftPresence.CONFIG.serverMessages) {
            if (!StringUtils.isNullOrEmpty(serverMessage)) {
                final String[] part = serverMessage.split(CraftPresence.CONFIG.splitCharacter);
                if (!StringUtils.isNullOrEmpty(part[0]) && !knownAddresses.contains(part[0])) {
                    knownAddresses.add(part[0]);
                }
            }
        }
    }
}
