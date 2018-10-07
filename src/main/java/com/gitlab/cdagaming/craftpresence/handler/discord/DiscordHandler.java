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
import com.gitlab.cdagaming.craftpresence.handler.multimc.InstanceHandler;
import com.gitlab.cdagaming.craftpresence.handler.technic.PackHandler;
import com.sun.jna.NativeLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;
import java.io.File;

public class DiscordHandler {
    public final DiscordEventHandlers handlers = new DiscordEventHandlers();
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
    private long END_TIMESTAMP;
    private String MATCH_SECRET;
    private String SPECTATE_SECRET;
    private byte INSTANCE;

    public DiscordHandler(final String clientID) {
        CLIENT_ID = clientID;
    }

    public synchronized void init() {
        handlers.errored = (err, err1) -> handlers.errored.accept(err, err1);
        handlers.disconnected = (err, err1) -> handlers.disconnected.accept(err, err1);
        //handlers.ready = (user) -> handlers.ready.accept(user);
        handlers.joinGame = (secret) -> CraftPresence.SERVER.verifyAndJoin(secret);
        handlers.joinRequest = (user) -> handlers.joinRequest.accept(user);
        handlers.spectateGame = (secret) -> handlers.spectateGame.accept(secret);

        NativeLibrary.addSearchPath("discord-rpc", new File(Constants.MODID).getAbsolutePath());
        DiscordRPC.INSTANCE.Discord_Initialize(CLIENT_ID, handlers, true, null);
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutDown));

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                DiscordRPC.INSTANCE.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000L);
                } catch (Exception ignored) {
                }
            }
        }, "RPC-Callback-Handler").start();

        Constants.LOG.info(I18n.format("craftpresence.logger.info.load", !StringHandler.isNullOrEmpty(CLIENT_ID) ? CLIENT_ID : "450485984333660181"));
    }

    public void updateTimestamp() {
        if (CraftPresence.CONFIG.showTime) {
            START_TIMESTAMP = System.currentTimeMillis() / 1000L;
        }
    }

    public void updatePresence(@Nonnull final DiscordRichPresence presence) {
        if (Constants.BRAND.contains("vivecraft") && DiscordAssetHandler.contains("vivecraft")) {
            CraftPresence.packFound = true;
            presence.details = presence.details + " | " + CraftPresence.CONFIG.vivecraftMessage;
        } else if (ManifestHandler.manifest != null && !StringHandler.isNullOrEmpty(ManifestHandler.manifest.name)) {
            presence.details = presence.details + " | " + CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringHandler.formatWord(ManifestHandler.manifest.name));
        } else if (!StringHandler.isNullOrEmpty(InstanceHandler.get("name"))) {
            presence.details = presence.details + " | " + CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringHandler.formatWord(InstanceHandler.get("name")));
        } else if (!StringHandler.isNullOrEmpty(PackHandler.PACK_NAME)) {
            presence.details = presence.details + " | " + CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringHandler.formatWord(PackHandler.PACK_NAME));
        }

        if ((StringHandler.isNullOrEmpty(presence.smallImageKey) && StringHandler.isNullOrEmpty(presence.smallImageText)) || (CraftPresence.CONFIG.overwriteServerIcon)) {
            if (Constants.BRAND.contains("vivecraft") && DiscordAssetHandler.contains("vivecraft")) {
                CraftPresence.packFound = true;
                presence.smallImageKey = "vivecraft";
                presence.smallImageText = CraftPresence.CONFIG.vivecraftMessage;
            } else if (ManifestHandler.manifest != null && !StringHandler.isNullOrEmpty(ManifestHandler.manifest.name)) {
                final String iconKey = StringHandler.formatPackIcon(ManifestHandler.manifest.name);
                if (DiscordAssetHandler.contains(iconKey)) {
                    presence.smallImageKey = iconKey;
                    presence.smallImageText = CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringHandler.formatWord(ManifestHandler.manifest.name));
                } else {
                    Constants.LOG.error(I18n.format("craftpresence.logger.error.discord.assets.load.null", iconKey));
                }
            } else if (!StringHandler.isNullOrEmpty(InstanceHandler.get("name")) && !StringHandler.isNullOrEmpty(InstanceHandler.get("iconKey"))) {
                final String iconKey = StringHandler.formatPackIcon(InstanceHandler.get("iconKey"));
                if (DiscordAssetHandler.contains(iconKey)) {
                    presence.smallImageKey = iconKey;
                    presence.smallImageText = CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", StringHandler.formatWord(InstanceHandler.get("name")));
                } else {
                    Constants.LOG.error(I18n.format("craftpresence.logger.error.discord.assets.load.null", iconKey));
                }
            } else if (!StringHandler.isNullOrEmpty(PackHandler.PACK_NAME) && !StringHandler.isNullOrEmpty(PackHandler.ICON_NAME)) {
                if (DiscordAssetHandler.contains(PackHandler.ICON_NAME)) {
                    presence.smallImageKey = PackHandler.ICON_NAME;
                    presence.smallImageText = CraftPresence.CONFIG.packPlaceholderMSG.replace("&name&", PackHandler.PACK_NAME);
                } else {
                    Constants.LOG.error(I18n.format("craftpresence.logger.error.discord.assets.load.null", PackHandler.ICON_NAME));
                }
            }
        }

        DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);
    }

    public synchronized void shutDown() {
        CraftPresence.BIOMES.emptyData();
        CraftPresence.DIMENSIONS.emptyData();
        CraftPresence.ENTITIES.emptyData();
        CraftPresence.GUIS.emptyData();

        DiscordRPC.INSTANCE.Discord_ClearPresence();
        DiscordRPC.INSTANCE.Discord_Shutdown();

        Constants.LOG.info(I18n.format("craftpresence.logger.info.shutdown"));
    }

    public void setImage(@Nonnull final String key, @Nonnull final DiscordAsset.AssetType type) {
        final String formattedKey = StringHandler.formatPackIcon(key);
        if (DiscordAssetHandler.contains(formattedKey)) {
            if (type.equals(DiscordAsset.AssetType.LARGE)) {
                LARGEIMAGEKEY = formattedKey;
            }

            if (type.equals(DiscordAsset.AssetType.SMALL)) {
                SMALLIMAGEKEY = formattedKey;
            }
        } else {
            Constants.LOG.error(I18n.format("craftpresence.logger.error.discord.assets.empty", formattedKey, type.name()));
            final Minecraft mc = Minecraft.getMinecraft();
            final EntityPlayer player = mc.player;
            final String defaultIcon = StringHandler.formatPackIcon(CraftPresence.CONFIG.defaultIcon);
            if (type.equals(DiscordAsset.AssetType.LARGE)) {
                if (player != null) {
                    final String dimIcon = StringHandler.formatPackIcon(CraftPresence.CONFIG.defaultDimensionIcon.replace("&icon&", CraftPresence.CONFIG.defaultIcon));
                    if (DiscordAssetHandler.contains(dimIcon)) {
                        LARGEIMAGEKEY = dimIcon;
                    } else {
                        Constants.LOG.error(I18n.format("craftpresence.logger.error.discord.asset.failure"));
                    }
                } else {
                    LARGEIMAGEKEY = defaultIcon;
                }
            }

            if (type.equals(DiscordAsset.AssetType.SMALL)) {
                if (!mc.isSingleplayer() && player != null) {
                    final String serverIcon = StringHandler.formatPackIcon(CraftPresence.CONFIG.defaultServerIcon.replace("&icon&", CraftPresence.CONFIG.defaultIcon));
                    if (DiscordAssetHandler.contains(serverIcon)) {
                        SMALLIMAGEKEY = serverIcon;
                    } else {
                        Constants.LOG.error(I18n.format("craftpresence.logger.error.discord.asset.failure"));
                    }
                } else {
                    SMALLIMAGEKEY = defaultIcon;
                }
            }
        }
    }

    public DiscordRichPresence buildRichPresence() {
        return new DiscordRichPresence(GAME_STATE, DETAILS, START_TIMESTAMP, END_TIMESTAMP, LARGEIMAGEKEY, LARGEIMAGETEXT, SMALLIMAGEKEY, SMALLIMAGETEXT, PARTY_ID, PARTY_SIZE, PARTY_MAX, MATCH_SECRET, JOIN_SECRET, SPECTATE_SECRET, INSTANCE);
    }
}
