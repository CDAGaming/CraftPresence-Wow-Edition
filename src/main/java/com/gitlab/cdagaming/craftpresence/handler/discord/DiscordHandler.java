package com.gitlab.cdagaming.craftpresence.handler.discord;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.curse.ManifestHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAsset;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.rpc.DiscordEventHandlers;
import com.gitlab.cdagaming.craftpresence.handler.discord.rpc.DiscordRPC;
import com.gitlab.cdagaming.craftpresence.handler.discord.rpc.DiscordRichPresence;
import com.gitlab.cdagaming.craftpresence.handler.discord.rpc.DiscordUser;
import com.gitlab.cdagaming.craftpresence.handler.multimc.InstanceHandler;
import com.gitlab.cdagaming.craftpresence.handler.technic.PackHandler;
import com.sun.jna.NativeLibrary;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.ClientCommandHandler;

import javax.annotation.Nonnull;
import java.io.File;

public class DiscordHandler {
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

    private final DiscordEventHandlers handlers = new DiscordEventHandlers();
    private String lastImageRequested, lastImageTypeRequested, lastClientIDRequested;
    private int lastErrorCode, lastDisconnectErrorCode;
    private Thread callbackThread = null;

    public synchronized void setup() {
        NativeLibrary.addSearchPath("discord-rpc", new File(Constants.MODID).getAbsolutePath());
        Thread shutdownThread = new Thread("CraftPresence-ShutDown-Handler") {
            @Override
            public void run() {
                shutDown();
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

    private synchronized void setupThreads() {
        callbackThread = new Thread("RPC-Callback-Handler") {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    DiscordRPC.INSTANCE.Discord_RunCallbacks();
                    try {
                        Thread.sleep(2000L);
                    } catch (Exception ignored) {
                    }
                }
            }
        };
        callbackThread.start();
    }

    public synchronized void init() {
        handlers.errored = new DiscordEventHandlers.OnStatus() {
            @Override
            public void accept(int errorCode, String message) {
                if (StringHandler.isNullOrEmpty(STATUS) || (!StringHandler.isNullOrEmpty(STATUS) && (!STATUS.equalsIgnoreCase("errored") || lastErrorCode != errorCode))) {
                    STATUS = "errored";
                    lastErrorCode = errorCode;
                    shutDown();
                    Constants.LOG.error(I18n.format("craftpresence.logger.error.rpc", errorCode, message));
                }
            }
        };

        handlers.disconnected = new DiscordEventHandlers.OnStatus() {
            @Override
            public void accept(int errorCode, String message) {
                if (StringHandler.isNullOrEmpty(STATUS) || (!StringHandler.isNullOrEmpty(STATUS) && (!STATUS.equalsIgnoreCase("disconnected") || lastDisconnectErrorCode != errorCode))) {
                    STATUS = "disconnected";
                    lastDisconnectErrorCode = errorCode;
                    shutDown();
                }
            }
        };

        handlers.ready = new DiscordEventHandlers.OnReady() {
            @Override
            public void accept(DiscordUser user) {
                if ((StringHandler.isNullOrEmpty(STATUS) || CURRENT_USER == null) || (!StringHandler.isNullOrEmpty(STATUS) && (!STATUS.equalsIgnoreCase("ready") || !CURRENT_USER.equals(user)))) {
                    STATUS = "ready";
                    CURRENT_USER = user;
                    Constants.LOG.info(I18n.format("craftpresence.logger.info.load", CLIENT_ID, CURRENT_USER.username));
                }
            }
        };

        handlers.joinGame = new DiscordEventHandlers.OnGameUpdate() {
            @Override
            public void accept(String secret) {
                if (StringHandler.isNullOrEmpty(STATUS) || (!StringHandler.isNullOrEmpty(STATUS) && !STATUS.equalsIgnoreCase("joinGame"))) {
                    STATUS = "joinGame";
                    CraftPresence.SERVER.verifyAndJoin(secret);
                }
            }
        };

        handlers.joinRequest = new DiscordEventHandlers.OnJoinRequest() {
            @Override
            public void accept(DiscordUser request) {
                if (StringHandler.isNullOrEmpty(STATUS) || (!StringHandler.isNullOrEmpty(STATUS) && (!STATUS.equalsIgnoreCase("joinRequest") || !REQUESTER_USER.equals(request)))) {
                    CraftPresence.TIMER = 30;
                    STATUS = "joinRequest";
                    REQUESTER_USER = request;
                    ClientCommandHandler.instance.executeCommand(CraftPresence.player, "/" + Constants.MODID + " request");
                }
            }
        };

        handlers.spectateGame = new DiscordEventHandlers.OnGameUpdate() {
            @Override
            public void accept(String secret) {
                if (StringHandler.isNullOrEmpty(STATUS) || (!StringHandler.isNullOrEmpty(STATUS) && !STATUS.equalsIgnoreCase("spectateGame"))) {
                    STATUS = "spectateGame";
                }
            }
        };

        DiscordRPC.INSTANCE.Discord_Initialize(CLIENT_ID, handlers, true, null);
        DiscordRPC.INSTANCE.Discord_UpdateHandlers(handlers);
        setupThreads();
    }

    public void updateTimestamp() {
        if (CraftPresence.CONFIG.showTime) {
            START_TIMESTAMP = System.currentTimeMillis() / 1000L;
        }
    }

    public void updatePresence(@Nonnull final DiscordRichPresence presence) {
        if (Constants.BRAND.contains("vivecraft")) {
            CraftPresence.packFound = true;
            presence.details = presence.details + (!StringHandler.isNullOrEmpty(presence.details) ? " | " : "") + CraftPresence.CONFIG.vivecraftMessage;
        } else if (ManifestHandler.manifest != null && !StringHandler.isNullOrEmpty(ManifestHandler.manifest.name)) {
            presence.details = presence.details + (!StringHandler.isNullOrEmpty(presence.details) ? " | " : "") + CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringHandler.formatWord(ManifestHandler.manifest.name));
        } else if (!StringHandler.isNullOrEmpty(InstanceHandler.INSTANCE_NAME)) {
            presence.details = presence.details + (!StringHandler.isNullOrEmpty(presence.details) ? " | " : "") + CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringHandler.formatWord(InstanceHandler.INSTANCE_NAME));
        } else if (!StringHandler.isNullOrEmpty(PackHandler.PACK_NAME)) {
            presence.details = presence.details + (!StringHandler.isNullOrEmpty(presence.details) ? " | " : "") + CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringHandler.formatWord(PackHandler.PACK_NAME));
        }

        if ((StringHandler.isNullOrEmpty(presence.smallImageKey) && StringHandler.isNullOrEmpty(presence.smallImageText)) || (CraftPresence.CONFIG.overwriteServerIcon)) {
            if (Constants.BRAND.contains("vivecraft") && DiscordAssetHandler.contains("vivecraft")) {
                presence.smallImageKey = "vivecraft";
                presence.smallImageText = CraftPresence.CONFIG.vivecraftMessage;
            } else if (ManifestHandler.manifest != null && !StringHandler.isNullOrEmpty(ManifestHandler.manifest.name)) {
                final String iconKey = StringHandler.formatPackIcon(ManifestHandler.manifest.name);
                if (DiscordAssetHandler.contains(iconKey)) {
                    presence.smallImageKey = iconKey;
                    presence.smallImageText = CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringHandler.formatWord(ManifestHandler.manifest.name));
                }
            } else if (!StringHandler.isNullOrEmpty(InstanceHandler.INSTANCE_NAME) && !StringHandler.isNullOrEmpty(InstanceHandler.ICON_KEY) && DiscordAssetHandler.contains(InstanceHandler.ICON_KEY)) {
                presence.smallImageKey = InstanceHandler.ICON_KEY;
                presence.smallImageText = CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringHandler.formatWord(InstanceHandler.INSTANCE_NAME));
            } else if (!StringHandler.isNullOrEmpty(PackHandler.PACK_NAME) && !StringHandler.isNullOrEmpty(PackHandler.ICON_NAME) && DiscordAssetHandler.contains(PackHandler.ICON_NAME)) {
                presence.smallImageKey = PackHandler.ICON_NAME;
                presence.smallImageText = CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", PackHandler.PACK_NAME);
            }
        }

        presence.largeImageKey = StringHandler.isNullOrEmpty(presence.largeImageKey) ? CraftPresence.CONFIG.defaultIcon : presence.largeImageKey;
        presence.largeImageText = StringHandler.isNullOrEmpty(presence.largeImageText) ? I18n.format("craftpresence.defaults.state.mcversion", Constants.MCVersion) : presence.largeImageText;

        DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);
    }

    public synchronized void shutDown() {
        callbackThread.interrupt();

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
        CraftPresence.TIMER = 0;
        CURRENT_USER = null;
        REQUESTER_USER = null;

        Constants.LOG.info(I18n.format("craftpresence.logger.info.shutdown"));
    }

    public void setImage(@Nonnull final String key, @Nonnull final DiscordAsset.AssetType type) {
        final String formattedKey = StringHandler.formatPackIcon(key);

        if (((StringHandler.isNullOrEmpty(lastImageRequested) || !lastImageRequested.equals(key)) || (StringHandler.isNullOrEmpty(lastImageTypeRequested) || !lastImageTypeRequested.equals(type.name()))) || (StringHandler.isNullOrEmpty(lastClientIDRequested) || !lastClientIDRequested.equals(CLIENT_ID))) {
            lastClientIDRequested = CLIENT_ID;
            lastImageRequested = key;
            lastImageTypeRequested = type.name();

            if (DiscordAssetHandler.contains(formattedKey)) {
                if (type.equals(DiscordAsset.AssetType.LARGE)) {
                    LARGEIMAGEKEY = formattedKey;
                }

                if (type.equals(DiscordAsset.AssetType.SMALL)) {
                    SMALLIMAGEKEY = formattedKey;
                }
            } else {
                Constants.LOG.error(I18n.format("craftpresence.logger.error.discord.assets.fallback", formattedKey, type.name()));
                Constants.LOG.info(I18n.format("craftpresence.logger.info.discord.assets.request", formattedKey, type.name()));

                final String defaultIcon = StringHandler.formatPackIcon(CraftPresence.CONFIG.defaultIcon);
                final String defaultDimensionIcon = StringHandler.formatPackIcon(StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 2, CraftPresence.CONFIG.splitCharacter, CraftPresence.CONFIG.defaultDimensionIcon).replace("&icon&", defaultIcon));
                final String defaultServerIcon = StringHandler.formatPackIcon(StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 2, CraftPresence.CONFIG.splitCharacter, CraftPresence.CONFIG.defaultServerIcon).replace("&icon&", defaultIcon));

                if (type.equals(DiscordAsset.AssetType.LARGE)) {
                    if (CraftPresence.DIMENSIONS.isInUse) {
                        // NOTE: Fallback for when Using showCurrentDimension is the Dimension Name
                        final String formattedCurrentDIMNameIcon = StringHandler.formatPackIcon(CraftPresence.DIMENSIONS.CURRENT_DIMENSION_NAME);
                        if (DiscordAssetHandler.contains(formattedCurrentDIMNameIcon)) {
                            Constants.LOG.info(I18n.format("craftpresence.logger.info.discord.assets.fallback", formattedKey, type.name(), formattedCurrentDIMNameIcon));
                            LARGEIMAGEKEY = formattedCurrentDIMNameIcon;
                        } else {
                            Constants.LOG.error(I18n.format("craftpresence.logger.error.discord.assets.default", formattedKey, type.name()));
                            LARGEIMAGEKEY = DiscordAssetHandler.contains(defaultDimensionIcon) ? defaultDimensionIcon : defaultIcon;
                        }
                    } else {
                        Constants.LOG.error(I18n.format("craftpresence.logger.error.discord.assets.default", formattedKey, type.name()));
                        LARGEIMAGEKEY = defaultIcon;
                    }
                }

                if (type.equals(DiscordAsset.AssetType.SMALL)) {
                    if (CraftPresence.SERVER.isInUse) {
                        // NOTE: Fallback for when On a Server is The Parts of Your IP
                        boolean matched = false;
                        for (String ipPart : CraftPresence.SERVER.currentServer_IP.split("\\.")) {
                            final String formattedIPPart = StringHandler.formatPackIcon(ipPart);
                            if (DiscordAssetHandler.contains(formattedIPPart)) {
                                Constants.LOG.info(I18n.format("craftpresence.logger.info.discord.assets.fallback", formattedKey, type.name(), formattedIPPart));
                                SMALLIMAGEKEY = formattedIPPart;
                                matched = true;
                                break;
                            }
                        }

                        if (!matched) {
                            Constants.LOG.error(I18n.format("craftpresence.logger.error.discord.assets.default", formattedKey, type.name()));
                            SMALLIMAGEKEY = DiscordAssetHandler.contains(defaultServerIcon) ? defaultServerIcon : defaultIcon;
                        }
                    } else {
                        Constants.LOG.error(I18n.format("craftpresence.logger.error.discord.assets.default", formattedKey, type.name()));
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
