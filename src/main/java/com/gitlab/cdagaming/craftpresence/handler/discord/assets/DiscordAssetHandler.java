package com.gitlab.cdagaming.craftpresence.handler.discord.assets;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.URLHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;

import java.util.*;

public class DiscordAssetHandler {
    public static List<String> SMALL_ICONS = new ArrayList<>();
    public static List<String> LARGE_ICONS = new ArrayList<>();
    public static List<String> ICON_LIST = new ArrayList<>();

    private static List<String> SMALL_IDS = new ArrayList<>();
    private static List<String> LARGE_IDS = new ArrayList<>();
    private static List<String> ICON_IDS = new ArrayList<>();

    private static Map<String, DiscordAsset> ASSET_LIST = new HashMap<>();

    public static boolean contains(final String key) {
        final String formattedKey = StringHandler.formatPackIcon(key.replace(" ", "_"));
        return ASSET_LIST.containsKey(formattedKey);
    }

    public static DiscordAsset get(final String key) {
        final String formattedKey = StringHandler.formatPackIcon(key.replace(" ", "_"));
        return contains(formattedKey) ? ASSET_LIST.get(formattedKey) : null;
    }

    public static String getKey(final String key) {
        final String formattedKey = StringHandler.formatPackIcon(key.replace(" ", "_"));
        return contains(formattedKey) ? ASSET_LIST.get(formattedKey).getName() : null;
    }

    public static String getID(final String key) {
        final String formattedKey = StringHandler.formatPackIcon(key.replace(" ", "_"));
        return contains(formattedKey) ? ASSET_LIST.get(formattedKey).getId() : null;
    }

    public static DiscordAsset.AssetType getType(final String key) {
        final String formattedKey = key.replace(" ", "_").toLowerCase();
        return contains(formattedKey) ? ASSET_LIST.get(formattedKey).getType() : DiscordAsset.AssetType.LARGE;
    }

    public static String getRandomAsset(final String alternativeKey) {
        final String formattedKey = StringHandler.formatPackIcon(alternativeKey);
        int randomInteger = MathHelper.getInt(new Random(), 0, ICON_LIST.size());
        return StringHandler.elementExists(ICON_LIST, randomInteger) ? ICON_LIST.get(randomInteger) : formattedKey;
    }

    public static void loadAssets() {
        Constants.LOG.info(I18n.format("craftpresence.logger.info.discord.assets.load", !StringHandler.isNullOrEmpty(CraftPresence.CONFIG.clientID) ? CraftPresence.CONFIG.clientID : "450485984333660181"));
        Constants.LOG.info(I18n.format("craftpresence.logger.info.discord.assets.load.credits"));
        ASSET_LIST = new HashMap<>();

        try {
            final String url = "https://discordapp.com/api/oauth2/applications/" + (!StringHandler.isNullOrEmpty(CraftPresence.CONFIG.clientID) ? CraftPresence.CONFIG.clientID : "450485984333660181") + "/assets";
            final DiscordAsset[] assets = URLHandler.getJSONFromURL(url, DiscordAsset[].class);

            if (assets != null) {
                for (DiscordAsset asset : assets) {
                    if (asset.getType().equals(DiscordAsset.AssetType.LARGE)) {
                        if (!LARGE_ICONS.contains(asset.getName())) {
                            LARGE_ICONS.add(asset.getName());
                        }
                        if (!LARGE_IDS.contains(asset.getId())) {
                            LARGE_IDS.add(asset.getId());
                        }
                    }
                    if (asset.getType().equals(DiscordAsset.AssetType.SMALL)) {
                        if (!SMALL_ICONS.contains(asset.getName())) {
                            SMALL_ICONS.add(asset.getName());
                        }
                        if (!SMALL_IDS.contains(asset.getId())) {
                            SMALL_IDS.add(asset.getId());
                        }
                    }
                    if (!ICON_LIST.contains(asset.getName())) {
                        ICON_LIST.add(asset.getName());
                    }
                    if (!ASSET_LIST.containsKey(asset.getName())) {
                        ASSET_LIST.put(asset.getName(), asset);
                    }
                    if (!ICON_IDS.contains(asset.getId())) {
                        ICON_IDS.add(asset.getId());
                    }
                }
            }
        } catch (Exception ex) {
            Constants.LOG.error(I18n.format("craftpresence.logger.error.discord.assets.load"));
            ex.printStackTrace();
        } finally {
            verifyConfigAssets();
            Constants.LOG.info(I18n.format("craftpresence.logger.info.discord.assets.detected", String.valueOf(ASSET_LIST.size())));
        }
    }

    private static void verifyConfigAssets() {
        boolean needsFullUpdate = false;
        for (String property : CraftPresence.CONFIG.properties.stringPropertyNames()) {
            if ((property.equals(CraftPresence.CONFIG.NAME_defaultIcon) || property.equals(CraftPresence.CONFIG.NAME_defaultDimensionIcon) || property.equals(CraftPresence.CONFIG.NAME_defaultServerIcon)) && !contains(CraftPresence.CONFIG.properties.getProperty(property))) {
                final String alternateKey = !property.equals(CraftPresence.CONFIG.NAME_defaultIcon) ? CraftPresence.CONFIG.properties.getProperty(CraftPresence.CONFIG.NAME_defaultIcon) : null;
                final String newAsset = getRandomAsset(alternateKey);
                Constants.LOG.error(I18n.format("craftpresence.logger.error.config.invalidicon.pre", CraftPresence.CONFIG.properties.getProperty(property), property));
                CraftPresence.CONFIG.properties.setProperty(property, newAsset);
                needsFullUpdate = true;
                Constants.LOG.error(I18n.format("craftpresence.logger.error.config.invalidicon.post", property, newAsset));
            }
        }

        if (needsFullUpdate) {
            CraftPresence.CONFIG.save(CraftPresence.CONFIG.properties);
            CraftPresence.CONFIG.read();
        }
    }
}
