package com.gitlab.cdagaming.craftpresence.utils.discord;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.commands.CommandsGui;
import com.gitlab.cdagaming.craftpresence.utils.curse.CurseUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAsset;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAssetUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.DiscordEventHandlers;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.DiscordRPC;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.DiscordRichPresence;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.DiscordUser;
import com.gitlab.cdagaming.craftpresence.utils.mcupdater.MCUpdaterUtils;
import com.gitlab.cdagaming.craftpresence.utils.multimc.MultiMCUtils;
import com.gitlab.cdagaming.craftpresence.utils.technic.TechnicUtils;
import com.sun.jna.NativeLibrary;

import java.io.File;
import java.util.ArrayList;

public class DiscordUtils {
    static {
        NativeLibrary.addSearchPath("discord-rpc", new File(ModUtils.MODID).getAbsolutePath());
    }

    private final DiscordEventHandlers handlers = new DiscordEventHandlers();
    private ArrayList<Tuple<String, String>> messageData = new ArrayList<>(), iconData = new ArrayList<>();

    public DiscordUser CURRENT_USER, REQUESTER_USER;
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

    private String lastImageRequested, lastImageTypeRequested, lastClientIDRequested;
    private int lastErrorCode, lastDisconnectErrorCode;

    public synchronized void setup() {
        Thread shutdownThread = new Thread("CraftPresence-ShutDown-Handler") {
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
        handlers.errored = (errorCode, message) -> {
            if (StringUtils.isNullOrEmpty(STATUS) || (!StringUtils.isNullOrEmpty(STATUS) && (!STATUS.equalsIgnoreCase("errored") || lastErrorCode != errorCode))) {
                STATUS = "errored";
                lastErrorCode = errorCode;
                shutDown();
                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.rpc", errorCode, message));
            }
        };

        handlers.disconnected = (errorCode, message) -> {
            if (StringUtils.isNullOrEmpty(STATUS) || (!StringUtils.isNullOrEmpty(STATUS) && (!STATUS.equalsIgnoreCase("disconnected") || lastDisconnectErrorCode != errorCode))) {
                STATUS = "disconnected";
                lastDisconnectErrorCode = errorCode;
                shutDown();
            }
        };

        handlers.ready = user -> {
            if ((StringUtils.isNullOrEmpty(STATUS) || CURRENT_USER == null) || (!StringUtils.isNullOrEmpty(STATUS) && (!STATUS.equalsIgnoreCase("ready") || !CURRENT_USER.equals(user)))) {
                STATUS = "ready";
                CURRENT_USER = user;
                ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.load", CLIENT_ID, CURRENT_USER.username));
            }
        };

        handlers.joinGame = secret -> {
            if (StringUtils.isNullOrEmpty(STATUS) || (!StringUtils.isNullOrEmpty(STATUS) && !STATUS.equalsIgnoreCase("joinGame"))) {
                STATUS = "joinGame";
                CraftPresence.SERVER.verifyAndJoin(secret);
            }
        };

        handlers.joinRequest = request -> {
            if (StringUtils.isNullOrEmpty(STATUS) || (!StringUtils.isNullOrEmpty(STATUS) && (!STATUS.equalsIgnoreCase("joinRequest") || !REQUESTER_USER.equals(request)))) {
                CraftPresence.SYSTEM.TIMER = 30;
                STATUS = "joinRequest";
                REQUESTER_USER = request;

                if (!(CraftPresence.instance.currentScreen instanceof CommandsGui)) {
                    CraftPresence.instance.displayGuiScreen(new CommandsGui(CraftPresence.instance.currentScreen));
                }
                CommandsGui.executeCommand("request");
            }
        };

        handlers.spectateGame = secret -> {
            if (StringUtils.isNullOrEmpty(STATUS) || (!StringUtils.isNullOrEmpty(STATUS) && !STATUS.equalsIgnoreCase("spectateGame"))) {
                STATUS = "spectateGame";
            }
        };

        DiscordRPC.INSTANCE.Discord_Initialize(CLIENT_ID, handlers, true, null);
    }

    public void updateTimestamp() {
        if (CraftPresence.CONFIG.showTime) {
            START_TIMESTAMP = CraftPresence.SYSTEM.CURRENT_TIMESTAMP / 1000L;
        }
    }

    public void syncPlaceholder(String placeHolderName, String insertString, boolean isIconData) {
        // Remove and Replace Placeholder Data, if the placeholder needs Updates
        if (!StringUtils.isNullOrEmpty(placeHolderName)) {
            if (isIconData) {
                if (iconData.removeIf(e -> e.getFirst().equalsIgnoreCase(placeHolderName) && !e.getSecond().equalsIgnoreCase(insertString))) {
                    iconData.add(new Tuple<>(placeHolderName, insertString));
                }
            } else {
                if (messageData.removeIf(e -> e.getFirst().equalsIgnoreCase(placeHolderName) && !e.getSecond().equalsIgnoreCase(insertString))) {
                    messageData.add(new Tuple<>(placeHolderName, insertString));
                }
            }
        }
    }

    public void updatePresence(final DiscordRichPresence presence) {
        if (presence != null) {
            // Format Presence based on Arguments available in argumentData
            presence.details = StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.detailsMSG, messageData);
            presence.state = StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.gameStateMSG, messageData);

            // Attack Pack Data, if any, to Presence Details
            // TODO: Adapt towards newer systems
            /*if (ModUtils.BRAND.contains("vivecraft")) {
                CraftPresence.packFound = true;
                presence.details = presence.details + (!StringUtils.isNullOrEmpty(presence.details) ? " | " : "") + CraftPresence.CONFIG.vivecraftMessage;
            } else if (CurseUtils.manifest != null && !StringUtils.isNullOrEmpty(CurseUtils.manifest.name)) {
                presence.details = presence.details + (!StringUtils.isNullOrEmpty(presence.details) ? " | " : "") + CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringUtils.formatWord(CurseUtils.manifest.name));
            } else if (!StringUtils.isNullOrEmpty(MultiMCUtils.INSTANCE_NAME)) {
                presence.details = presence.details + (!StringUtils.isNullOrEmpty(presence.details) ? " | " : "") + CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringUtils.formatWord(MultiMCUtils.INSTANCE_NAME));
            } else if (MCUpdaterUtils.instance != null && !StringUtils.isNullOrEmpty(MCUpdaterUtils.instance.getPackName())) {
                presence.details = presence.details + (!StringUtils.isNullOrEmpty(presence.details) ? " | " : "") + CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringUtils.formatWord(MCUpdaterUtils.instance.getPackName()));
            } else if (!StringUtils.isNullOrEmpty(TechnicUtils.PACK_NAME)) {
                presence.details = presence.details + (!StringUtils.isNullOrEmpty(presence.details) ? " | " : "") + CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringUtils.formatWord(TechnicUtils.PACK_NAME));
            }*/

            // Set smallImageKey to Pack Data, if any
            // TODO: Adapt towards new systems
            /*if ((StringUtils.isNullOrEmpty(presence.smallImageKey) && StringUtils.isNullOrEmpty(presence.smallImageText)) || (CraftPresence.CONFIG.overwriteServerIcon)) {
                if (ModUtils.BRAND.contains("vivecraft") && DiscordAssetUtils.contains("vivecraft")) {
                    presence.smallImageKey = "vivecraft";
                    presence.smallImageText = CraftPresence.CONFIG.vivecraftMessage;
                } else if (CurseUtils.manifest != null && !StringUtils.isNullOrEmpty(CurseUtils.manifest.name)) {
                    final String iconKey = StringUtils.formatPackIcon(CurseUtils.manifest.name);
                    if (DiscordAssetUtils.contains(iconKey)) {
                        presence.smallImageKey = iconKey;
                        presence.smallImageText = CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringUtils.formatWord(CurseUtils.manifest.name));
                    }
                } else if (!StringUtils.isNullOrEmpty(MultiMCUtils.INSTANCE_NAME) && !StringUtils.isNullOrEmpty(MultiMCUtils.ICON_KEY) && DiscordAssetUtils.contains(MultiMCUtils.ICON_KEY)) {
                    presence.smallImageKey = MultiMCUtils.ICON_KEY;
                    presence.smallImageText = CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringUtils.formatWord(MultiMCUtils.INSTANCE_NAME));
                } else if (!StringUtils.isNullOrEmpty(TechnicUtils.PACK_NAME) && !StringUtils.isNullOrEmpty(TechnicUtils.ICON_NAME) && DiscordAssetUtils.contains(TechnicUtils.ICON_NAME)) {
                    presence.smallImageKey = TechnicUtils.ICON_NAME;
                    presence.smallImageText = CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", TechnicUtils.PACK_NAME);
                }
            }*/

            DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);
        }
    }

    public synchronized void shutDown() {
        DiscordRPC.INSTANCE.Discord_ClearPresence();
        DiscordRPC.INSTANCE.Discord_Shutdown();

        CraftPresence.DIMENSIONS.clearClientData();
        CraftPresence.ENTITIES.clearClientData();
        CraftPresence.BIOMES.clearClientData();
        CraftPresence.SERVER.clearClientData();
        CraftPresence.GUIS.clearClientData();

        STATUS = "disconnected";
        lastDisconnectErrorCode = 0;
        lastErrorCode = 0;
        CraftPresence.SYSTEM.TIMER = 0;
        CURRENT_USER = null;
        REQUESTER_USER = null;

        ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.shutdown"));
    }

    public void setImage(final String key, final DiscordAsset.AssetType type) {
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

    public DiscordRichPresence buildRichPresence() {
        return new DiscordRichPresence(GAME_STATE, DETAILS, START_TIMESTAMP, END_TIMESTAMP, LARGEIMAGEKEY, LARGEIMAGETEXT, SMALLIMAGEKEY, SMALLIMAGETEXT, PARTY_ID, PARTY_SIZE, PARTY_MAX, MATCH_SECRET, JOIN_SECRET, SPECTATE_SECRET, INSTANCE);
    }
}
