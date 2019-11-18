package com.gitlab.cdagaming.craftpresence.utils;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.curse.CurseUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAssetUtils;
import com.gitlab.cdagaming.craftpresence.utils.mcupdater.MCUpdaterUtils;
import com.gitlab.cdagaming.craftpresence.utils.multimc.MultiMCUtils;
import com.gitlab.cdagaming.craftpresence.utils.technic.TechnicUtils;

public class CommandUtils {
    public static boolean isInMainMenu = false;

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
        clearPartyData(false, false);

        CraftPresence.CLIENT.syncArgument("&MAINMENU&", CraftPresence.CONFIG.mainmenuMSG.replace("&ign&", CraftPresence.CONFIG.playerPlaceholderMSG.replace("&name&", ModUtils.USERNAME)).replace("&mods&", CraftPresence.CONFIG.modsPlaceholderMSG.replace("&modcount&", Integer.toString(FileUtils.getModCount()))), false);
        CraftPresence.CLIENT.syncArgument("&MAINMENU&", CraftPresence.CONFIG.defaultIcon, true);
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());

        isInMainMenu = true;
    }

    public static void clearPartyData(boolean clearRequesterData, boolean updateRPC) {
        if (clearRequesterData) {
            CraftPresence.awaitingReply = false;
            CraftPresence.CLIENT.REQUESTER_USER = null;
            CraftPresence.SYSTEM.TIMER = 0;
        }
        CraftPresence.CLIENT.JOIN_SECRET = null;
        CraftPresence.CLIENT.PARTY_ID = null;
        CraftPresence.CLIENT.PARTY_SIZE = 0;
        CraftPresence.CLIENT.PARTY_MAX = 0;
        if (updateRPC) {
            CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
        }
    }
}
