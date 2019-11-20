package com.gitlab.cdagaming.craftpresence.utils;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.curse.CurseUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAssetUtils;
import com.gitlab.cdagaming.craftpresence.utils.mcupdater.MCUpdaterUtils;
import com.gitlab.cdagaming.craftpresence.utils.multimc.MultiMCUtils;
import com.gitlab.cdagaming.craftpresence.utils.technic.TechnicUtils;
import com.google.common.collect.Lists;

import java.util.List;

public class CommandUtils {
    public static boolean isInMainMenu = false;

    public static void reloadData(final boolean forceUpdateRPC) {
        ModUtils.TRANSLATOR.onTick();
        CraftPresence.SYSTEM.onTick();
        CraftPresence.KEYBINDINGS.onTick();
        CraftPresence.GUIS.onTick();

        if (!isInMainMenu) {
            CraftPresence.BIOMES.onTick();
            CraftPresence.DIMENSIONS.onTick();
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
        // Form Argument Lists
        List<Tuple<String, String>> playerDataArgs = Lists.newArrayList(), mainMenuArgs = Lists.newArrayList(), modsArgs = Lists.newArrayList();

        // Player Data Arguments
        playerDataArgs.add(new Tuple<>("&NAME&", ModUtils.USERNAME));

        // Mods Arguments
        modsArgs.add(new Tuple<>("&MODCOUNT&", Integer.toString(FileUtils.getModCount())));

        // Main Menu Arguments
        mainMenuArgs.add(new Tuple<>("&IGN&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.playerPlaceholderMSG, playerDataArgs)));
        mainMenuArgs.add(new Tuple<>("&MODS&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.modsPlaceholderMSG, modsArgs)));

        CraftPresence.CLIENT.STATUS = "ready";
        CraftPresence.CLIENT.clearPartyData(true, false);

        CraftPresence.CLIENT.syncArgument("&MAINMENU&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.mainmenuMSG, mainMenuArgs), false);
        CraftPresence.CLIENT.syncArgument("&MAINMENU&", CraftPresence.CONFIG.defaultIcon, true);
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());

        isInMainMenu = true;
    }
}
