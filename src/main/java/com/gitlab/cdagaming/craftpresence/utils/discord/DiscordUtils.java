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
package com.gitlab.cdagaming.craftpresence.utils.discord;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.CommandUtils;
import com.gitlab.cdagaming.craftpresence.utils.FileUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.curse.CurseUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAssetUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCClient;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.RichPresence;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.User;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.pipe.PipeStatus;
import com.gitlab.cdagaming.craftpresence.utils.mcupdater.MCUpdaterUtils;
import com.gitlab.cdagaming.craftpresence.utils.multimc.MultiMCUtils;
import com.gitlab.cdagaming.craftpresence.utils.technic.TechnicUtils;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Variables and Methods used to update the RPC Presence States to display within Discord
 *
 * @author CDAGaming
 */
public class DiscordUtils {
    /**
     * A Mapping of the Arguments available to use as RPC Message Placeholders
     */
    private final List<Tuple<String, String>> messageData = Lists.newArrayList();
    /**
     * A Mapping of the Arguments available to use as Icon Key Placeholders
     */
    private final List<Tuple<String, String>> iconData = Lists.newArrayList();
    /**
     * A Mapping of the Arguments attached to the &MODS& RPC Message placeholder
     */
    private final List<Tuple<String, String>> modsArgs = Lists.newArrayList();
    /**
     * A Mapping of the Arguments attached to the &IGN& RPC Message Placeholder
     */
    private final List<Tuple<String, String>> playerInfoArgs = Lists.newArrayList();
    /**
     * The Current User, tied to the Rich Presence
     */
    public User CURRENT_USER;
    /**
     * The Join Request User Data, if any
     */
    public User REQUESTER_USER;
    /**
     * The current RPC Status (Ex: ready, errored, disconnected)
     */
    public String STATUS;
    /**
     * The Current Message tied to the Party/Game Status Field of the RPC
     */
    public String GAME_STATE;
    /**
     * The Current Message tied to the current action / Details Field of the RPC
     */
    public String DETAILS;
    /**
     * The Current Small Image Icon being displayed in the RPC, if any
     */
    public String SMALLIMAGEKEY;
    /**
     * The Current Message tied to the Small Image, if any
     */
    public String SMALLIMAGETEXT;
    /**
     * The Current Large Image Icon being displayed in the RPC, if any
     */
    public String LARGEIMAGEKEY;
    /**
     * The Current Message tied to the Large Image, if any
     */
    public String LARGEIMAGETEXT;
    /**
     * The 18-character Client ID Number, tied to the game profile data attached to the RPC
     */
    public String CLIENT_ID;
    /**
     * The Current Starting Unix Timestamp from Epoch, used for Elapsed Time
     */
    public long START_TIMESTAMP;
    /**
     * The Party Session ID that's tied to the RPC, if any
     */
    public String PARTY_ID;
    /**
     * The Current Size of the Party Session, if in a Party
     */
    public int PARTY_SIZE;
    /**
     * The Maximum Size of the Party Session, if in a Party
     */
    public int PARTY_MAX;
    /**
     * The Current Party Join Secret Key, if in a Party
     */
    public String JOIN_SECRET;
    /**
     * The Current Ending Unix Timestamp from Epoch, used for Time Until if combined with {@link DiscordUtils#START_TIMESTAMP}
     */
    public long END_TIMESTAMP;
    /**
     * The Current Match Secret Key tied to the RPC, if any
     */
    public String MATCH_SECRET;
    /**
     * The Current Spectate Secret Key tied to the RPC, if any
     */
    public String SPECTATE_SECRET;
    /**
     * The Instance Code attached to the RPC, if any
     */
    public byte INSTANCE;
    /**
     * A Mapping of the General RPC Arguments allowed in adjusting Presence Messages
     */
    public List<Tuple<String, String>> generalArgs = Lists.newArrayList();
    /**
     * An Instance of the {@link IPCClient}, responsible for sending and receiving RPC Events
     */
    public IPCClient ipcInstance;
    /**
     * Whether Discord is currently awaiting a response to a Ask to Join or Spectate Request, if any
     */
    public boolean awaitingReply = false;
    /**
     * A Mapping of the Last Requested Image Data
     * <p>Used to prevent sending duplicate packets and cache data for repeated images in other areas
     * <p>Format: lastAttemptedKey, lastResultingKey
     */
    private Tuple<String, String> lastRequestedImageData = new Tuple<>();
    /**
     * An Instance containing the Current Rich Presence Data
     * <p>Also used to prevent sending duplicate packets with the same presence data, if any
     */
    private RichPresence currentPresence;

    /**
     * Setup any Critical Methods needed for the RPC
     * <p>In this case, ensures a Thread is in place to shut down the RPC onExit
     */
    public synchronized void setup() {
        final Thread shutdownThread = new Thread("CraftPresence-ShutDown-Handler") {
            @Override
            public void run() {
                CraftPresence.closing = true;
                CraftPresence.timerObj.cancel();

                shutDown();
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

    /**
     * Initializes and Synchronizes Initial Rich Presence Data
     */
    public synchronized void init() {
        try {
            // Create IPC Instance and Listener and Make a Connection if possible
            ipcInstance = new IPCClient(Long.parseLong(CLIENT_ID), ModUtils.IS_DEV);
            ipcInstance.setListener(new ModIPCListener());
            ipcInstance.connect();

            // Subscribe to RPC Events after Connection
            ipcInstance.subscribe(IPCClient.Event.ACTIVITY_JOIN);
            ipcInstance.subscribe(IPCClient.Event.ACTIVITY_JOIN_REQUEST);
            ipcInstance.subscribe(IPCClient.Event.ACTIVITY_SPECTATE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Initialize and Sync any Pre-made Arguments (And Reset Related Data)
        initArgumentData("&MAINMENU&", "&BRAND&", "&MCVERSION&", "&IGN&", "&MODS&", "&PACK&", "&DIMENSION&", "&BIOME&", "&SERVER&", "&GUI&", "&TILEENTITY&", "&TARGETENTITY&", "&ATTACKINGENTITY&", "&RIDINGENTITY&");
        initIconData("&DEFAULT&", "&MAINMENU&", "&PACK&", "&DIMENSION&", "&SERVER&");

        // Ensure Main Menu RPC Resets properly
        CommandUtils.isInMainMenu = false;

        // Add Any Generalized Argument Data needed
        modsArgs.add(new Tuple<>("&MODCOUNT&", Integer.toString(FileUtils.getModCount())));
        playerInfoArgs.add(new Tuple<>("&NAME&", ModUtils.USERNAME));

        generalArgs.add(new Tuple<>("&MCVERSION&", ModUtils.TRANSLATOR.translate("craftpresence.defaults.state.mcversion", ModUtils.MCVersion)));
        generalArgs.add(new Tuple<>("&BRAND&", ModUtils.BRAND));
        generalArgs.add(new Tuple<>("&MODS&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.modsPlaceholderMSG, modsArgs)));
        generalArgs.add(new Tuple<>("&IGN&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.outerPlayerPlaceholderMSG, playerInfoArgs)));

        for (Tuple<String, String> generalArgument : generalArgs) {
            // For each General (Can be used Anywhere) Argument
            // Ensure they sync as Formatter Arguments too
            syncArgument(generalArgument.getFirst(), generalArgument.getSecond(), false);
        }

        // Sync the Default Icon Argument
        syncArgument("&DEFAULT&", CraftPresence.CONFIG.defaultIcon, true);

        syncPackArguments();
    }

    /**
     * Updates the Starting Unix Timestamp, if allowed
     */
    public void updateTimestamp() {
        if (CraftPresence.CONFIG.showTime) {
            START_TIMESTAMP = CraftPresence.SYSTEM.CURRENT_TIMESTAMP / 1000L;
        }
    }

    /**
     * Synchronizes the Specified Argument as an RPC Message or an Icon Placeholder
     *
     * @param argumentName The Specified Argument to Synchronize for
     * @param insertString The String to attach to the Specified Argument
     * @param isIconData   Whether the Argument is an RPC Message or an Icon Placeholder
     */
    public void syncArgument(String argumentName, String insertString, boolean isIconData) {
        // Remove and Replace Placeholder Data, if the placeholder needs Updates
        if (!StringUtils.isNullOrEmpty(argumentName)) {
            if (isIconData) {
                if (iconData.removeIf(e -> e.getFirst().equalsIgnoreCase(argumentName) && !e.getSecond().equalsIgnoreCase(insertString))) {
                    iconData.add(new Tuple<>(argumentName, insertString));
                }
            } else {
                if (messageData.removeIf(e -> e.getFirst().equalsIgnoreCase(argumentName) && !e.getSecond().equalsIgnoreCase(insertString))) {
                    messageData.add(new Tuple<>(argumentName, insertString));
                }
            }
        }
    }

    /**
     * Initialize the Specified RPC Arguments as Empty Data
     *
     * @param args The Arguments to Initialize
     */
    public void initArgumentData(String... args) {
        // Initialize Specified Arguments to Empty Data
        for (String argumentName : args) {
            messageData.removeIf(e -> e.getFirst().equalsIgnoreCase(argumentName));
            messageData.add(new Tuple<>(argumentName, ""));
        }
    }

    /**
     * Initialize the Specified Icon Arguments as Empty Data
     *
     * @param args The Arguments to Initialize
     */
    public void initIconData(String... args) {
        // Initialize Specified Icon Arguments to Empty Data
        for (String iconArgumentName : args) {
            iconData.removeIf(e -> e.getFirst().equalsIgnoreCase(iconArgumentName));
            iconData.add(new Tuple<>(iconArgumentName, ""));
        }
    }

    /**
     * Synchronizes the &PACK& Argument, based on any found Launcher Pack/Instance Data
     */
    private void syncPackArguments() {
        // Add &PACK& Placeholder to ArgumentData
        String foundPackName = "", foundPackIcon = "";

        if (ModUtils.BRAND.contains("vivecraft")) {
            CraftPresence.packFound = true;

            foundPackName = CraftPresence.CONFIG.vivecraftMessage;
            foundPackIcon = "vivecraft";
        } else if (CurseUtils.manifest != null && !StringUtils.isNullOrEmpty(CurseUtils.manifest.name)) {
            foundPackName = CurseUtils.manifest.name;
            foundPackIcon = foundPackName;
        } else if (!StringUtils.isNullOrEmpty(MultiMCUtils.INSTANCE_NAME)) {
            foundPackName = MultiMCUtils.INSTANCE_NAME;
            foundPackIcon = MultiMCUtils.ICON_KEY;
        } else if (MCUpdaterUtils.instance != null && !StringUtils.isNullOrEmpty(MCUpdaterUtils.instance.getPackName())) {
            foundPackName = MCUpdaterUtils.instance.getPackName();
            foundPackIcon = foundPackName;
        } else if (!StringUtils.isNullOrEmpty(TechnicUtils.PACK_NAME)) {
            foundPackName = TechnicUtils.PACK_NAME;
            foundPackIcon = TechnicUtils.ICON_NAME;
        }

        syncArgument("&PACK&", StringUtils.formatWord(StringUtils.replaceAnyCase(CraftPresence.CONFIG.packPlaceholderMSG, "&NAME&", !StringUtils.isNullOrEmpty(foundPackName) ? foundPackName : ""), !CraftPresence.CONFIG.formatWords), false);
        syncArgument("&PACK&", !StringUtils.isNullOrEmpty(foundPackIcon) ? StringUtils.formatPackIcon(foundPackIcon) : "", true);
    }

    /**
     * Synchronizes and Updates the Rich Presence Data, if needed and connected
     *
     * @param presence The New Presence Data to apply
     */
    public void updatePresence(final RichPresence presence) {
        if (presence != null &&
                (currentPresence == null || !presence.toJson().toString().equals(currentPresence.toJson().toString())) &&
                ipcInstance.getStatus() == PipeStatus.CONNECTED) {
            ipcInstance.sendRichPresence(presence);
            currentPresence = presence;
        }
    }

    /**
     * Attempts to lookup the specified Image, and if not existent, use the alternative String, and null if allowed
     *
     * @param evalString        The Specified Icon Key to search for from the {@link DiscordUtils#CLIENT_ID} Assets
     * @param alternativeString The Alternative Icon Key to use if unable to locate the Original Icon Key
     * @param allowNull         If allowed to return null if unable to find any matches, otherwise uses the Default Icon in Config
     * @return The found or alternative matching Icon Key
     */
    public String imageOf(final String evalString, final String alternativeString, final boolean allowNull) {
        // Ensures Assets were fully synced from the Client ID before running
        if (DiscordAssetUtils.syncCompleted) {
            if (StringUtils.isNullOrEmpty(lastRequestedImageData.getFirst()) || !lastRequestedImageData.getFirst().equalsIgnoreCase(evalString)) {
                final String defaultIcon = DiscordAssetUtils.contains(CraftPresence.CONFIG.defaultIcon) ? CraftPresence.CONFIG.defaultIcon : DiscordAssetUtils.getRandomAsset();
                lastRequestedImageData.setFirst(evalString);

                String finalKey = evalString;

                if (!DiscordAssetUtils.contains(finalKey)) {
                    ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.discord.assets.fallback", evalString, alternativeString));
                    ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.info.discord.assets.request", evalString));
                    if (DiscordAssetUtils.contains(alternativeString)) {
                        ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.info.discord.assets.fallback", evalString, alternativeString));
                        finalKey = alternativeString;
                    } else {
                        if (allowNull) {
                            finalKey = "";
                        } else {
                            ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.discord.assets.default", evalString));
                            finalKey = defaultIcon;
                        }
                    }
                }

                lastRequestedImageData.setSecond(finalKey);
                return finalKey;
            } else {
                return lastRequestedImageData.getSecond();
            }
        } else {
            return "";
        }
    }

    /**
     * Clears Related Party Session Information from the RPC, and updates if needed
     *
     * @param clearRequesterData Whether to clear Ask to Join / Spectate Request Data
     * @param updateRPC          Whether to immediately update the RPC following changes
     */
    public void clearPartyData(boolean clearRequesterData, boolean updateRPC) {
        if (clearRequesterData) {
            awaitingReply = false;
            REQUESTER_USER = null;
            CraftPresence.SYSTEM.TIMER = 0;
        }
        JOIN_SECRET = null;
        PARTY_ID = null;
        PARTY_SIZE = 0;
        PARTY_MAX = 0;
        if (updateRPC) {
            updatePresence(buildRichPresence());
        }
    }

    /**
     * Shutdown the RPC and close related resources, as well as Clearing any remaining Runtime Client Data
     */
    public synchronized void shutDown() {
        try {
            ipcInstance.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Clear User Data before final clear and shutdown
        STATUS = "disconnected";
        currentPresence = null;
        clearPartyData(true, false);
        CURRENT_USER = null;

        lastRequestedImageData = new Tuple<>();

        CraftPresence.DIMENSIONS.clearClientData();
        CraftPresence.TILE_ENTITIES.clearClientData();
        CraftPresence.ENTITIES.clearClientData();
        CraftPresence.BIOMES.clearClientData();
        CraftPresence.SERVER.clearClientData();
        CraftPresence.GUIS.clearClientData();

        ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.shutdown"));
    }

    /**
     * Builds a New Instance of {@link RichPresence} based on Queued Data
     *
     * @return A New Instance of {@link RichPresence}
     */
    public RichPresence buildRichPresence() {
        // Format Presence based on Arguments available in argumentData
        DETAILS = StringUtils.formatWord(StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.detailsMSG, messageData), !CraftPresence.CONFIG.formatWords);
        GAME_STATE = StringUtils.formatWord(StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.gameStateMSG, messageData), !CraftPresence.CONFIG.formatWords);

        final String baseLargeImage = StringUtils.removeMatches(StringUtils.getMatches("&([^\\s]+?)&", CraftPresence.CONFIG.largeImageKey), iconData, 1);
        LARGEIMAGEKEY = StringUtils.sequentialReplaceAnyCase(baseLargeImage, iconData);

        final String baseSmallImage = StringUtils.removeMatches(StringUtils.getMatches("&([^\\s]+?)&", CraftPresence.CONFIG.smallImageKey), iconData, 1);
        SMALLIMAGEKEY = StringUtils.sequentialReplaceAnyCase(baseSmallImage, iconData);

        LARGEIMAGETEXT = StringUtils.formatWord(StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.largeImageMSG, messageData), !CraftPresence.CONFIG.formatWords);
        SMALLIMAGETEXT = StringUtils.formatWord(StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.smallImageMSG, messageData), !CraftPresence.CONFIG.formatWords);

        RichPresence newRPCData = new RichPresence.Builder()
                .setState(GAME_STATE)
                .setDetails(DETAILS)
                .setStartTimestamp(START_TIMESTAMP)
                .setEndTimestamp(END_TIMESTAMP)
                .setLargeImage(LARGEIMAGEKEY, LARGEIMAGETEXT)
                .setSmallImage(SMALLIMAGEKEY, SMALLIMAGETEXT)
                .setParty(PARTY_ID, PARTY_SIZE, PARTY_MAX)
                .setMatchSecret(MATCH_SECRET)
                .setJoinSecret(JOIN_SECRET)
                .setSpectateSecret(SPECTATE_SECRET)
                .build();

        // Format Data to UTF_8 after Sent to RPC (RPC has it's own Encoding)
        GAME_STATE = StringUtils.getConvertedString(GAME_STATE, "UTF-8", false);
        DETAILS = StringUtils.getConvertedString(DETAILS, "UTF-8", false);

        LARGEIMAGEKEY = StringUtils.getConvertedString(LARGEIMAGEKEY, "UTF-8", false);
        SMALLIMAGEKEY = StringUtils.getConvertedString(SMALLIMAGEKEY, "UTF-8", false);

        LARGEIMAGETEXT = StringUtils.getConvertedString(LARGEIMAGETEXT, "UTF-8", false);
        SMALLIMAGETEXT = StringUtils.getConvertedString(SMALLIMAGETEXT, "UTF-8", false);

        return newRPCData;
    }
}
