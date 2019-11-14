package com.gitlab.cdagaming.craftpresence.utils;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.curse.CurseUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAsset;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAssetUtils;
import com.gitlab.cdagaming.craftpresence.utils.mcupdater.MCUpdaterUtils;
import com.gitlab.cdagaming.craftpresence.utils.multimc.MultiMCUtils;
import com.gitlab.cdagaming.craftpresence.utils.technic.TechnicUtils;

public class CommandUtils {
    public static void reloadData(final boolean forceUpdateRPC) {
        ModUtils.TRANSLATOR.tick();
        CraftPresence.SYSTEM.tick();

        CraftPresence.KEYBINDINGS.onTick();
        CraftPresence.BIOMES.onTick();
        CraftPresence.DIMENSIONS.onTick();
        CraftPresence.GUIS.onTick();
        CraftPresence.ENTITIES.onTick();
        CraftPresence.SERVER.onTick();

        if (forceUpdateRPC) {
            if (CraftPresence.DIMENSIONS.isInUse) {
                CraftPresence.DIMENSIONS.updateDimensionPresence();
            }
            if (CraftPresence.GUIS.isInUse) {
                CraftPresence.GUIS.updateGUIPresence();
            }
            if (CraftPresence.ENTITIES.isInUse) {
                CraftPresence.ENTITIES.updateEntityPresence();
            }
            if (CraftPresence.SERVER.isInUse) {
                CraftPresence.SERVER.updateServerPresence();
            }
            if (CraftPresence.BIOMES.isInUse) {
                CraftPresence.BIOMES.updateBiomePresence();
            }
        }
    }

    public static void rebootRPC() {
        CraftPresence.CLIENT.shutDown();
        if (!CraftPresence.CLIENT.CLIENT_ID.equals(CraftPresence.CONFIG.clientID)) {
            DiscordAssetUtils.emptyData();
            CraftPresence.CLIENT.CLIENT_ID = CraftPresence.CONFIG.clientID;
        }
        DiscordAssetUtils.loadAssets();
        CraftPresence.CLIENT.init();
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
    }

    public static void init() {
        if (CraftPresence.CONFIG.detectCurseManifest && !CraftPresence.packFound) {
            CurseUtils.loadManifest();
        }
        if (CraftPresence.CONFIG.detectMultiMCManifest && !CraftPresence.packFound) {
            MultiMCUtils.loadInstance();
        }
        if (CraftPresence.CONFIG.detectMCUpdaterInstance && !CraftPresence.packFound) {
            MCUpdaterUtils.loadInstance();
        }
        if (CraftPresence.CONFIG.detectTechnicPack && !CraftPresence.packFound) {
            TechnicUtils.loadPack();
        }
        DiscordAssetUtils.loadAssets();
    }

    public static void setMainMenuPresence() {
        CraftPresence.CLIENT.STATUS = "ready";
        CraftPresence.CLIENT.SMALLIMAGEKEY = "";
        CraftPresence.CLIENT.SMALLIMAGETEXT = "";
        CraftPresence.CLIENT.GAME_STATE = !CraftPresence.GUIS.isInUse ? "" : CraftPresence.CLIENT.GAME_STATE;
        CraftPresence.CLIENT.PARTY_ID = "";
        CraftPresence.CLIENT.PARTY_SIZE = 0;
        CraftPresence.CLIENT.PARTY_MAX = 0;
        CraftPresence.CLIENT.JOIN_SECRET = "";
        CraftPresence.CLIENT.DETAILS = CraftPresence.SERVER.enabled && !CraftPresence.GUIS.isInUse ? CraftPresence.CONFIG.mainmenuMSG.replace("&ign&", CraftPresence.CONFIG.playerPlaceholderMSG.replace("&name&", ModUtils.USERNAME)).replace("&mods&", CraftPresence.CONFIG.modsPlaceholderMSG.replace("&modcount&", Integer.toString(FileUtils.getModCount()))) : "";
        CraftPresence.CLIENT.setImage(CraftPresence.CONFIG.defaultIcon, DiscordAsset.AssetType.LARGE);
        CraftPresence.CLIENT.LARGEIMAGETEXT = ModUtils.TRANSLATOR.translate("craftpresence.defaults.state.mcversion", ModUtils.MCVersion);
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
    }

    public static Boolean isOnMainMenuPresence() {
        return !CraftPresence.CONFIG.hasChanged &&
                StringUtils.isNullOrEmpty(CraftPresence.CLIENT.SMALLIMAGEKEY) &&
                StringUtils.isNullOrEmpty(CraftPresence.CLIENT.SMALLIMAGETEXT) &&
                (CraftPresence.GUIS.isInUse || StringUtils.isNullOrEmpty(CraftPresence.CLIENT.GAME_STATE)) &&
                StringUtils.isNullOrEmpty(CraftPresence.CLIENT.PARTY_ID) &&
                CraftPresence.CLIENT.PARTY_SIZE == 0 &&
                CraftPresence.CLIENT.PARTY_MAX == 0 &&
                StringUtils.isNullOrEmpty(CraftPresence.CLIENT.JOIN_SECRET) &&
                (StringUtils.isNullOrEmpty(CraftPresence.CLIENT.DETAILS) ||
                        CraftPresence.CLIENT.DETAILS.equals(CraftPresence.SERVER.enabled && !CraftPresence.GUIS.enabled ? CraftPresence.CONFIG.mainmenuMSG.replace("&ign&", CraftPresence.CONFIG.playerPlaceholderMSG.replace("&name&", ModUtils.USERNAME)).replace("&mods&", CraftPresence.CONFIG.modsPlaceholderMSG.replace("&modcount&", Integer.toString(FileUtils.getModCount()))) : "")
                ) &&
                (!StringUtils.isNullOrEmpty(CraftPresence.CLIENT.LARGEIMAGEKEY) &&
                        CraftPresence.CLIENT.LARGEIMAGEKEY.equals(CraftPresence.CONFIG.defaultIcon)
                ) &&
                (!StringUtils.isNullOrEmpty(CraftPresence.CLIENT.LARGEIMAGETEXT) &&
                        CraftPresence.CLIENT.LARGEIMAGETEXT.equals(ModUtils.TRANSLATOR.translate("craftpresence.defaults.state.mcversion", ModUtils.MCVersion))
                ) &&
                (!StringUtils.isNullOrEmpty(CraftPresence.CLIENT.STATUS) && CraftPresence.CLIENT.STATUS.equalsIgnoreCase("ready"));
    }
}
