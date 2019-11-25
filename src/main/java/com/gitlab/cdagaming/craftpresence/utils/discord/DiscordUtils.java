package com.gitlab.cdagaming.craftpresence.utils.discord;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.CommandUtils;
import com.gitlab.cdagaming.craftpresence.utils.FileUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.curse.CurseUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAsset;
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

public class DiscordUtils {
    public User CURRENT_USER, REQUESTER_USER;
    public String STATUS;
    public String GAME_STATE;
    public String DETAILS;
    public String SMALLIMAGEKEY;
    public String SMALLIMAGETEXT;
    public String LARGEIMAGEKEY;
    public String LARGEIMAGETEXT;
    public String CLIENT_ID;
    public long START_TIMESTAMP;
    public String PARTY_ID;
    public int PARTY_SIZE;
    public int PARTY_MAX;
    public String JOIN_SECRET;
    public long END_TIMESTAMP;
    public String MATCH_SECRET;
    public String SPECTATE_SECRET;
    public byte INSTANCE;
    public List<Tuple<String, String>> generalArgs = Lists.newArrayList();

    public IPCClient ipcInstance;
    public RichPresence currentPresence;

    private List<Tuple<String, String>> messageData = Lists.newArrayList(), iconData = Lists.newArrayList(),
            modsArgs = Lists.newArrayList(), playerInfoArgs = Lists.newArrayList();
    private String lastImageRequested, lastImageTypeRequested, lastClientIDRequested;

    public synchronized void setup() {
        final Thread shutdownThread = new Thread("CraftPresence-ShutDown-Handler") {
            @Override
            public void run() {
                CraftPresence.closing = true;
                CraftPresence.timerObj.cancel();

                shutDown();
            }
        };
        shutdownThread.setDaemon(true);
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

    public synchronized void init() {
        try {
            // Create IPC Instance and Listener and Make a Connection if possible
            ipcInstance = new IPCClient(Long.parseLong(CLIENT_ID));
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
        initArgumentData("&MAINMENU&", "&MCVERSION&", "&IGN&", "&MODS&", "&PACK&", "&DIMENSION&", "&BIOME&", "&SERVER&", "&GUI&", "&ENTITY&");
        initIconData("&MAINMENU&", "&MCVERSION&", "&IGN&", "&MODS&", "&PACK&", "&DIMENSION&", "&BIOME&", "&SERVER&", "&GUI&", "&ENTITY&");

        // Ensure Main Menu RPC Resets properly
        CommandUtils.isInMainMenu = false;

        // Add Any Generalized Argument Data needed
        modsArgs.add(new Tuple<>("&MODCOUNT&", Integer.toString(FileUtils.getModCount())));
        playerInfoArgs.add(new Tuple<>("&NAME&", ModUtils.USERNAME));

        generalArgs.add(new Tuple<>("&MCVERSION&", ModUtils.TRANSLATOR.translate("craftpresence.defaults.state.mcversion", ModUtils.MCVersion)));
        generalArgs.add(new Tuple<>("&MODS&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.modsPlaceholderMSG, modsArgs)));
        generalArgs.add(new Tuple<>("&IGN&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.playerPlaceholderMSG, playerInfoArgs)));

        for (Tuple<String, String> generalArgument : generalArgs) {
            // For each General (Can be used Anywhere) Argument
            // Ensure they sync as Formatter Arguments too
            syncArgument(generalArgument.getFirst(), generalArgument.getSecond(), false);
        }

        syncPackArguments();
    }

    public void updateTimestamp() {
        if (CraftPresence.CONFIG.showTime) {
            START_TIMESTAMP = CraftPresence.SYSTEM.CURRENT_TIMESTAMP / 1000L;
        }
    }

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

    public void initArgumentData(String... args) {
        // Initialize Available Arguments to Empty Data
        for (String argumentName : args) {
            messageData.removeIf(e -> e.getFirst().equalsIgnoreCase(argumentName));
            messageData.add(new Tuple<>(argumentName, ""));
        }
    }

    public void initIconData(String... args) {
        // Initialize available Icon Arguments to Empty Data
        for (String iconArgumentName : args) {
            iconData.removeIf(e -> e.getFirst().equalsIgnoreCase(iconArgumentName));
            iconData.add(new Tuple<>(iconArgumentName, ""));
        }
    }

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

        syncArgument("&PACK&", StringUtils.formatWord(StringUtils.replaceAnyCase(CraftPresence.CONFIG.packPlaceholderMSG, "&NAME&", !StringUtils.isNullOrEmpty(foundPackName) ? foundPackName : "")), false);
        syncArgument("&PACK&", !StringUtils.isNullOrEmpty(foundPackIcon) ? StringUtils.formatPackIcon(foundPackIcon) : "", true);
    }

    public void updatePresence(final RichPresence presence) {
        if (presence != null &&
                (currentPresence == null || !presence.toJson().toString().equals(currentPresence.toJson().toString())) &&
                ipcInstance.getStatus() == PipeStatus.CONNECTED) {
            ipcInstance.sendRichPresence(presence);
            currentPresence = presence;
        }
    }

    public void clearPartyData(boolean clearRequesterData, boolean updateRPC) {
        if (clearRequesterData) {
            CraftPresence.awaitingReply = false;
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

        CraftPresence.DIMENSIONS.clearClientData();
        CraftPresence.ENTITIES.clearClientData();
        CraftPresence.BIOMES.clearClientData();
        CraftPresence.SERVER.clearClientData();
        CraftPresence.GUIS.clearClientData();

        ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.shutdown"));
    }

    private void setImage(final String key, final DiscordAsset.AssetType type) {
        if (!StringUtils.isNullOrEmpty(key) && type != null) {
            final String formattedKey = StringUtils.formatPackIcon(key);

            if (((StringUtils.isNullOrEmpty(lastImageRequested) || !lastImageRequested.equals(key)) || (StringUtils.isNullOrEmpty(lastImageTypeRequested) || !lastImageTypeRequested.equals(type.name()))) || (StringUtils.isNullOrEmpty(lastClientIDRequested) || !lastClientIDRequested.equals(CLIENT_ID))) {
                lastClientIDRequested = CLIENT_ID;
                lastImageRequested = key;
                lastImageTypeRequested = type.name();

                if (DiscordAssetUtils.contains(formattedKey)) {
                    if (type.equals(DiscordAsset.AssetType.LARGE)) {
                        LARGEIMAGEKEY = formattedKey;
                    }

                    if (type.equals(DiscordAsset.AssetType.SMALL)) {
                        SMALLIMAGEKEY = formattedKey;
                    }
                } else {
                    ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.discord.assets.fallback", formattedKey, type.name()));
                    ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.discord.assets.request", formattedKey, type.name()));

                    final String defaultIcon = StringUtils.formatPackIcon(CraftPresence.CONFIG.defaultIcon);

                    if (type.equals(DiscordAsset.AssetType.LARGE)) {
                        ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.discord.assets.default", formattedKey, type.name()));
                        LARGEIMAGEKEY = defaultIcon;
                    }

                    if (type.equals(DiscordAsset.AssetType.SMALL)) {
                        ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.discord.assets.default", formattedKey, type.name()));
                        SMALLIMAGEKEY = defaultIcon;
                    }
                }
            }
        }
    }

    public RichPresence buildRichPresence() {
        // Format Presence based on Arguments available in argumentData
        DETAILS = StringUtils.formatWord(StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.detailsMSG, messageData));
        GAME_STATE = StringUtils.formatWord(StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.gameStateMSG, messageData));

        //setImage(StringUtils.sequentialReplaceAnyCase(StringUtils.removeMatches(StringUtils.getMatches("^&([^\\s]+?)&", CraftPresence.CONFIG.largeImageKey), 1, true), iconData), DiscordAsset.AssetType.LARGE);
        //setImage(StringUtils.sequentialReplaceAnyCase(StringUtils.removeMatches(StringUtils.getMatches("^&([^\\s]+?)&", CraftPresence.CONFIG.smallImageKey), 1, true), iconData), DiscordAsset.AssetType.SMALL);

        LARGEIMAGETEXT = StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.largeImageMSG, messageData);
        SMALLIMAGETEXT = StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.smallImageMSG, messageData);

        // Format Data to UTF_8
        GAME_STATE = StringUtils.getUnicodeString(GAME_STATE);
        DETAILS = StringUtils.getUnicodeString(DETAILS);

        LARGEIMAGEKEY = StringUtils.getUnicodeString(LARGEIMAGEKEY);
        SMALLIMAGEKEY = StringUtils.getUnicodeString(SMALLIMAGEKEY);

        LARGEIMAGETEXT = StringUtils.getUnicodeString(LARGEIMAGETEXT);
        SMALLIMAGETEXT = StringUtils.getUnicodeString(SMALLIMAGETEXT);

        return new RichPresence.Builder()
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
    }
}
