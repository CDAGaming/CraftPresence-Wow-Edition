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

package com.gitlab.cdagaming.craftpresence.utils.server;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.Pair;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.DiscordStatus;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.PartyPrivacy;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.List;
import java.util.Map;

/**
 * Server Utilities used to Parse Server Data and handle related RPC Events
 *
 * @author CDAGaming
 */
@SuppressWarnings("DuplicatedCode")
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
     * The Current Player Map, if available
     */
    public List<NetworkPlayerInfo> currentPlayerList = Lists.newArrayList();

    /**
     * A List of the detected Server Addresses
     */
    public List<String> knownAddresses = Lists.newArrayList();

    /**
     * A List of the detected Server Data from NBT
     */
    public Map<String, ServerData> knownServerData = Maps.newHashMap();

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
    private String currentServerMessage;

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
     * Mapping storing the Current X, Y and Z Position of the Player in a World
     * Format: Position (X, Y, Z)
     */
    private Tuple<Double, Double, Double> currentCoordinates = new Tuple<>(0.0D, 0.0D, 0.0D);

    /**
     * Mapping storing the Current and Maximum Health the Player currently has in a World
     */
    private Pair<Double, Double> currentHealth = new Pair<>(0.0D, 0.0D);

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
        currentPlayerList.clear();
        knownAddresses.clear();
        knownServerData.clear();
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
        currentCoordinates = new Tuple<>(0.0D, 0.0D, 0.0D);
        currentHealth = new Pair<>(0.0D, 0.0D);
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

        CraftPresence.CLIENT.initArgument("&SERVER&");
    }

    /**
     * Module Event to Occur on each tick within the Application
     */
    public void onTick() {
        joinInProgress = CraftPresence.CLIENT.STATUS == DiscordStatus.JoinGame || CraftPresence.CLIENT.STATUS == DiscordStatus.SpectateGame;
        enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.CONFIG.detectWorldData : enabled;
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
            final List<NetworkPlayerInfo> newPlayerList = newConnection != null ? Lists.newArrayList(newConnection.getPlayerInfoMap()) : Lists.newArrayList();
            final int newCurrentPlayers = newConnection != null ? newConnection.getPlayerInfoMap().size() : 1;
            final int newMaxPlayers = newConnection != null && newConnection.currentServerMaxPlayers >= newCurrentPlayers ? newConnection.currentServerMaxPlayers : newCurrentPlayers + 1;
            final boolean newLANStatus = (CraftPresence.instance.isSingleplayer() && newCurrentPlayers > 1) || (newServerData != null && newServerData.isOnLAN());

            final String newServer_IP = newServerData != null && !StringUtils.isNullOrEmpty(newServerData.serverIP) ? newServerData.serverIP : "127.0.0.1";
            final String newServer_Name = newServerData != null && !StringUtils.isNullOrEmpty(newServerData.serverName) ? newServerData.serverName : CraftPresence.CONFIG.defaultServerName;
            final String newServer_MOTD = !isOnLAN && !CraftPresence.instance.isSingleplayer() && (newServerData != null && !StringUtils.isNullOrEmpty(newServerData.serverMOTD)) &&
                    !(newServerData.serverMOTD.equalsIgnoreCase(ModUtils.TRANSLATOR.translate("craftpresence.multiplayer.status.cannot_connect")) ||
                            newServerData.serverMOTD.equalsIgnoreCase(ModUtils.TRANSLATOR.translate("craftpresence.multiplayer.status.cannot_resolve")) ||
                            newServerData.serverMOTD.equalsIgnoreCase(ModUtils.TRANSLATOR.translate("craftpresence.multiplayer.status.polling")) ||
                            newServerData.serverMOTD.equalsIgnoreCase(ModUtils.TRANSLATOR.translate("craftpresence.multiplayer.status.pinging"))) ? StringUtils.stripColors(newServerData.serverMOTD) : CraftPresence.CONFIG.defaultServerMotd;

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

                if (!StringUtils.isNullOrEmpty(currentServer_IP) && !knownAddresses.contains(currentServer_IP.contains(":") ? currentServer_IP : StringUtils.formatAddress(currentServer_IP, false))) {
                    knownAddresses.add(currentServer_IP.contains(":") ? currentServer_IP : StringUtils.formatAddress(currentServer_IP, false));
                }

                final ServerList serverList = new ServerList(CraftPresence.instance);
                serverList.loadServerList();
                if (serverList.countServers() != serverIndex || CraftPresence.CONFIG.serverMessages.length != serverIndex) {
                    getServerAddresses();
                }
            }

            // NOTE: Universal + Custom Events
            if (!StringUtils.isNullOrEmpty(currentServerMessage)) {
                // &PLAYERINFO& Sub-Arguments
                if (currentServerMessage.toLowerCase().contains("&playerinfo&")) {
                    // &coords& Argument = Current Coordinates of Player
                    if (CraftPresence.CONFIG.innerPlayerPlaceholderMessage.toLowerCase().contains("&coords&")) {
                        final double newX = StringUtils.roundDouble(CraftPresence.player != null ? CraftPresence.player.posX : 0.0D, CraftPresence.CONFIG.roundSize);
                        final double newY = StringUtils.roundDouble(CraftPresence.player != null ? CraftPresence.player.posY : 0.0D, CraftPresence.CONFIG.roundSize);
                        final double newZ = StringUtils.roundDouble(CraftPresence.player != null ? CraftPresence.player.posZ : 0.0D, CraftPresence.CONFIG.roundSize);
                        final Tuple<Double, Double, Double> newCoordinates = new Tuple<>(newX, newY, newZ);
                        if (!newCoordinates.equals(currentCoordinates)) {
                            currentCoordinates = newCoordinates;
                            queuedForUpdate = true;
                        }
                    }

                    // &health& Argument = Current and Maximum Health of Player
                    if (CraftPresence.CONFIG.innerPlayerPlaceholderMessage.toLowerCase().contains("&health&")) {
                        final Pair<Double, Double> newHealth = CraftPresence.player != null ? new Pair<>(StringUtils.roundDouble(CraftPresence.player.getHealth(), 0), StringUtils.roundDouble(CraftPresence.player.getMaxHealth(), 0)) : new Pair<>(0.0D, 0.0D);
                        if (!newHealth.equals(currentHealth)) {
                            currentHealth = newHealth;
                            queuedForUpdate = true;
                        }
                    }
                }

                // &WORLDINFO& Sub-Arguments
                if (currentServerMessage.toLowerCase().contains("&worldinfo&")) {
                    // &difficulty& Argument = Current Difficulty of the World
                    if (CraftPresence.CONFIG.worldPlaceholderMessage.toLowerCase().contains("&difficulty&")) {
                        final String newDifficulty = CraftPresence.player != null ?
                                (CraftPresence.player.world.getWorldInfo().isHardcoreModeEnabled() ? ModUtils.TRANSLATOR.translate("craftpresence.defaults.mode.hardcore") : CraftPresence.player.world.getDifficulty().name()) :
                                "";
                        if (!newDifficulty.equals(currentDifficulty)) {
                            currentDifficulty = newDifficulty;
                            queuedForUpdate = true;
                        }
                    }

                    // &worldname& Argument = Current Name of the World
                    if (CraftPresence.CONFIG.worldPlaceholderMessage.toLowerCase().contains("&worldname&")) {
                        final String newWorldName = CraftPresence.player != null ? CraftPresence.player.world.getWorldInfo().getWorldName() : "";
                        if (!newWorldName.equals(currentWorldName)) {
                            currentWorldName = newWorldName;
                            queuedForUpdate = true;
                        }
                    }

                    // &worldtime& Argument = Current Time in World
                    if (CraftPresence.CONFIG.worldPlaceholderMessage.toLowerCase().contains("&worldtime&")) {
                        final String newGameTime = CraftPresence.player != null ? getTimeString(CraftPresence.player.world.getWorldTime()) : null;
                        if (!StringUtils.isNullOrEmpty(newGameTime) && !newGameTime.equals(timeString)) {
                            timeString = newGameTime;
                            queuedForUpdate = true;
                        }
                    }
                }

                // &players& Argument = Current and Maximum Allowed Players in Server/World
                if ((currentServerMessage.toLowerCase().contains("&players&") || CraftPresence.CONFIG.enableJoinRequest) && (newCurrentPlayers != currentPlayers || newMaxPlayers != maxPlayers)) {
                    currentPlayers = newCurrentPlayers;
                    maxPlayers = newMaxPlayers;
                    queuedForUpdate = true;
                }
            }

            // Update Player List as needed, and Sync with Entity System if enabled
            if (!newPlayerList.equals(currentPlayerList)) {
                currentPlayerList = newPlayerList;

                if (CraftPresence.ENTITIES.enabled) {
                    CraftPresence.ENTITIES.getEntities();
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
                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.warning.config.disabled.enable_join_request"));
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
        List<Pair<String, String>> playerDataArgs = Lists.newArrayList(), worldDataArgs = Lists.newArrayList();

        List<Pair<String, String>> coordinateArgs = Lists.newArrayList(), healthArgs = Lists.newArrayList();

        coordinateArgs.add(new Pair<>("&xPosition&", currentCoordinates.getFirst().toString()));
        coordinateArgs.add(new Pair<>("&yPosition&", currentCoordinates.getSecond().toString()));
        coordinateArgs.add(new Pair<>("&zPosition&", currentCoordinates.getThird().toString()));

        healthArgs.add(new Pair<>("&CURRENT&", currentHealth.getFirst().toString()));
        healthArgs.add(new Pair<>("&MAX&", currentHealth.getSecond().toString()));

        // Player Data Arguments
        playerDataArgs.add(new Pair<>("&COORDS&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.playerCoordinatePlaceholderMessage, coordinateArgs)));
        playerDataArgs.add(new Pair<>("&HEALTH&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.playerHealthPlaceholderMessage, healthArgs)));

        // World Data Arguments
        worldDataArgs.add(new Pair<>("&DIFFICULTY&", !StringUtils.isNullOrEmpty(currentDifficulty) ? currentDifficulty : ""));
        worldDataArgs.add(new Pair<>("&WORLDNAME&", !StringUtils.isNullOrEmpty(currentWorldName) ? currentWorldName : ""));
        worldDataArgs.add(new Pair<>("&WORLDTIME&", !StringUtils.isNullOrEmpty(timeString) ? timeString : ""));

        if (!CraftPresence.instance.isSingleplayer() && currentServerData != null) {
            String CURRENT_SERVER_ICON;

            // Form Pair List of Argument for Servers/LAN Games
            List<Pair<String, String>> serverArgs = Lists.newArrayList(), playerAmountArgs = Lists.newArrayList();

            // Player Amount Arguments
            playerAmountArgs.add(new Pair<>("&CURRENT&", Integer.toString(currentPlayers)));
            playerAmountArgs.add(new Pair<>("&MAX&", Integer.toString(maxPlayers)));

            // Server Data Arguments
            serverArgs.add(new Pair<>("&IP&", StringUtils.formatAddress(currentServer_IP, false)));
            serverArgs.add(new Pair<>("&NAME&", currentServer_Name));
            serverArgs.add(new Pair<>("&MOTD&", currentServer_MOTD));
            serverArgs.add(new Pair<>("&PLAYERS&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.playerAmountPlaceholderMessage, playerAmountArgs)));
            serverArgs.add(new Pair<>("&PLAYERINFO&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.innerPlayerPlaceholderMessage, playerDataArgs)));
            serverArgs.add(new Pair<>("&WORLDINFO&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.worldPlaceholderMessage, worldDataArgs)));

            // Add All Generalized Arguments, if any
            if (!CraftPresence.CLIENT.generalArgs.isEmpty()) {
                serverArgs.addAll(CraftPresence.CLIENT.generalArgs);
            }

            if (isOnLAN) {
                // NOTE: LAN-Only Presence Updates
                final String alternateServerIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, currentServer_Name, 0, 2, CraftPresence.CONFIG.splitCharacter, currentServer_Name);
                final String currentServerIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, StringUtils.formatAddress(currentServer_IP, false), 0, 2, CraftPresence.CONFIG.splitCharacter, alternateServerIcon);
                final String formattedServerIconKey = StringUtils.formatAsIcon(currentServerIcon.replace(" ", "_"));

                CURRENT_SERVER_ICON = formattedServerIconKey.replace("&icon&", CraftPresence.CONFIG.defaultServerIcon);

                currentServerMessage = CraftPresence.CONFIG.lanMessage;
            } else {
                // NOTE: Server-Only Presence Updates
                final String defaultServerMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                final String alternateServerMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, currentServer_Name, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultServerMessage);
                final String alternateServerIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, currentServer_Name, 0, 2, CraftPresence.CONFIG.splitCharacter, currentServer_Name);

                currentServerMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, StringUtils.formatAddress(currentServer_IP, false), 0, 1, CraftPresence.CONFIG.splitCharacter, alternateServerMessage);
                final String currentServerIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, StringUtils.formatAddress(currentServer_IP, false), 0, 2, CraftPresence.CONFIG.splitCharacter, alternateServerIcon);
                final String formattedServerIconKey = StringUtils.formatAsIcon(currentServerIcon.replace(" ", "_"));

                CURRENT_SERVER_ICON = formattedServerIconKey.replace("&icon&", CraftPresence.CONFIG.defaultServerIcon);

                // If join requests are enabled, parse the appropriate data
                // to form party information.
                //
                // Note: The party privacy level is appended by modulus division to prevent
                // it being anything other then valid privacy levels
                if (CraftPresence.CONFIG.enableJoinRequest) {
                    if (!StringUtils.isNullOrEmpty(currentServer_Name) && !currentServer_Name.equalsIgnoreCase(CraftPresence.CONFIG.defaultServerName)) {
                        CraftPresence.CLIENT.PARTY_ID = "Join Server: " + currentServer_Name;
                    } else {
                        CraftPresence.CLIENT.PARTY_ID = "Join Server: " + currentServer_IP;
                    }
                    CraftPresence.CLIENT.JOIN_SECRET = makeSecret();
                    CraftPresence.CLIENT.PARTY_SIZE = currentPlayers;
                    CraftPresence.CLIENT.PARTY_MAX = maxPlayers;
                    CraftPresence.CLIENT.PARTY_PRIVACY = PartyPrivacy.from(CraftPresence.CONFIG.partyPrivacyLevel % 2);
                }
            }

            CraftPresence.CLIENT.syncArgument("&SERVER&", StringUtils.sequentialReplaceAnyCase(currentServerMessage, serverArgs), false);
            CraftPresence.CLIENT.syncArgument("&SERVER&", CraftPresence.CLIENT.imageOf(CURRENT_SERVER_ICON, CraftPresence.CONFIG.defaultServerIcon, true), true);
            queuedForUpdate = false;
        } else if (CraftPresence.instance.isSingleplayer()) {
            // Form SinglePlayer Pair Argument List
            List<Pair<String, String>> soloArgs = Lists.newArrayList();

            soloArgs.add(new Pair<>("&PLAYERINFO&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.innerPlayerPlaceholderMessage, playerDataArgs)));
            soloArgs.add(new Pair<>("&WORLDINFO&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.worldPlaceholderMessage, worldDataArgs)));

            // Add All Generalized Arguments, if any
            if (!CraftPresence.CLIENT.generalArgs.isEmpty()) {
                soloArgs.addAll(CraftPresence.CLIENT.generalArgs);
            }

            // NOTE: SinglePlayer-Only Presence Updates
            currentServerMessage = CraftPresence.CONFIG.singlePlayerMessage;

            CraftPresence.CLIENT.syncArgument("&SERVER&", StringUtils.sequentialReplaceAnyCase(currentServerMessage, soloArgs), false);
            CraftPresence.CLIENT.initArgument(true, "&SERVER&");
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
            if (!StringUtils.isNullOrEmpty(data.serverIP) && !knownAddresses.contains(data.serverIP.contains(":") ? StringUtils.formatAddress(data.serverIP, false) : data.serverIP)) {
                knownAddresses.add(data.serverIP.contains(":") ? StringUtils.formatAddress(data.serverIP, false) : data.serverIP);
            }

            if (!StringUtils.isNullOrEmpty(data.serverIP) && !knownServerData.keySet().contains(data.serverIP)) {
                knownServerData.put(data.serverIP, data);
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

    /**
     * Retrieves server data for the specified address, if available
     *
     * @param serverAddress The Server's identifying address
     * @return Server data for the specified address, if available
     */
    public ServerData getDataFromName(final String serverAddress) {
        return knownServerData.getOrDefault(serverAddress, null);
    }
}
