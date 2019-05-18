package com.gitlab.cdagaming.craftpresence.handler.discord.assets;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.URLHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class DiscordAssetHandler {
    public static List<String> SMALL_ICONS = Lists.newArrayList();
    public static List<String> LARGE_ICONS = Lists.newArrayList();
    public static List<String> ICON_LIST = Lists.newArrayList();

    private static List<String> SMALL_IDS = Lists.newArrayList();
    private static List<String> LARGE_IDS = Lists.newArrayList();
    private static List<String> ICON_IDS = Lists.newArrayList();

    private static Map<String, DiscordAsset> ASSET_LIST = Maps.newHashMap();

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

    public static void emptyData() {
        ASSET_LIST.clear();
        SMALL_ICONS.clear();
        SMALL_IDS.clear();
        LARGE_ICONS.clear();
        LARGE_IDS.clear();
        ICON_LIST.clear();
        ICON_IDS.clear();
    }

    public static String getRandomAsset() {
        try {
            final Random randomObj = new Random();
            return ICON_LIST.get(randomObj.nextInt(ICON_LIST.size()));
        } catch (Exception ex) {
            Constants.LOG.error(Constants.TRANSLATOR.translate("craftpresence.logger.error.config.invalidicon.empty"));
            ex.printStackTrace();
            return "";
        }
    }

    public static void loadAssets() {
        Constants.LOG.info(Constants.TRANSLATOR.translate("craftpresence.logger.info.discord.assets.load", CraftPresence.CONFIG.clientID));
        Constants.LOG.info(Constants.TRANSLATOR.translate("craftpresence.logger.info.discord.assets.load.credits"));
        ASSET_LIST = Maps.newHashMap();

        try {
            final String url = "https://discordapp.com/api/oauth2/applications/" + CraftPresence.CONFIG.clientID + "/assets";
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
            Constants.LOG.error(Constants.TRANSLATOR.translate("craftpresence.logger.error.discord.assets.load"));
            ex.printStackTrace();
        } finally {
            verifyConfigAssets();
            Constants.LOG.info(Constants.TRANSLATOR.translate("craftpresence.logger.info.discord.assets.detected", String.valueOf(ASSET_LIST.size())));
        }
    }

    private static void verifyConfigAssets() {
        boolean needsFullUpdate = false;
        for (String property : CraftPresence.CONFIG.properties.stringPropertyNames()) {
            if ((property.equals(CraftPresence.CONFIG.NAME_defaultIcon) || property.equals(CraftPresence.CONFIG.NAME_defaultDimensionIcon) || property.equals(CraftPresence.CONFIG.NAME_defaultServerIcon)) && !contains(CraftPresence.CONFIG.properties.getProperty(property))) {
                final String newAsset = contains(CraftPresence.CONFIG.defaultIcon) ? CraftPresence.CONFIG.defaultIcon : getRandomAsset();
                Constants.LOG.error(Constants.TRANSLATOR.translate("craftpresence.logger.error.config.invalidicon.pre", CraftPresence.CONFIG.properties.getProperty(property), property));
                CraftPresence.CONFIG.properties.setProperty(property, newAsset);
                needsFullUpdate = true;
                Constants.LOG.error(Constants.TRANSLATOR.translate("craftpresence.logger.error.config.invalidicon.post", property, newAsset));
            }
        }

        if (needsFullUpdate) {
            CraftPresence.CONFIG.save();
            CraftPresence.CONFIG.read(false);
        }
    }
}
