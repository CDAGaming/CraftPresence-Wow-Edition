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
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gitlab.cdagaming.craftpresence.utils;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.Pair;
import com.gitlab.cdagaming.craftpresence.integrations.curse.CurseUtils;
import com.gitlab.cdagaming.craftpresence.integrations.mcupdater.MCUpdaterUtils;
import com.gitlab.cdagaming.craftpresence.integrations.multimc.MultiMCUtils;
import com.gitlab.cdagaming.craftpresence.integrations.technic.TechnicUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAssetUtils;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Command Utilities for Synchronizing and Initializing Data
 *
 * @author CDAGaming
 */
public class CommandUtils {
    /**
     * Whether you are on the Main Menu in Minecraft
     */
    public static boolean isInMainMenu = false;

    /**
     * Whether you are on the Loading Stage in Minecraft
     */
    public static boolean isLoadingGame = false;

    /**
     * Reloads and Synchronizes Data, as needed, and performs onTick Events
     *
     * @param forceUpdateRPC Whether to Force an Update to the RPC Data
     */
    public static void reloadData(final boolean forceUpdateRPC) {
        ModUtils.TRANSLATOR.onTick();
        CraftPresence.SYSTEM.onTick();
        CraftPresence.KEYBINDINGS.onTick();
        CraftPresence.GUIS.onTick();

        if (CraftPresence.SYSTEM.HAS_LOADED && CraftPresence.SYSTEM.HAS_GAME_LOADED) {
            CraftPresence.BIOMES.onTick();
            CraftPresence.DIMENSIONS.onTick();
            CraftPresence.TILE_ENTITIES.onTick();
            CraftPresence.ENTITIES.onTick();
            CraftPresence.SERVER.onTick();

            if (forceUpdateRPC) {
                if (CraftPresence.DIMENSIONS.isInUse) {
                    CraftPresence.DIMENSIONS.updateDimensionPresence();
                }
                if (CraftPresence.GUIS.isInUse) {
                    CraftPresence.GUIS.updateGUIPresence();
                }
                if (CraftPresence.TILE_ENTITIES.isInUse) {
                    CraftPresence.TILE_ENTITIES.updateEntityPresence();
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

    /**
     * Restarts and Initializes the RPC Data
     */
    public static void rebootRPC() {
        CraftPresence.CLIENT.shutDown();

        if (!CraftPresence.CLIENT.CLIENT_ID.equals(CraftPresence.CONFIG.clientId)) {
            DiscordAssetUtils.emptyData();
            CraftPresence.CLIENT.CLIENT_ID = CraftPresence.CONFIG.clientId;
        } else {
            DiscordAssetUtils.clearClientData();
        }
        DiscordAssetUtils.loadAssets(CraftPresence.CONFIG.clientId, true);
        // TODO: Add CONFIG.resetTimeOnInit
        CraftPresence.CLIENT.init(false);
    }

    /**
     * Initializes Essential Data<p>
     * (In this case, Pack Data and Available RPC Icons)
     */
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
        DiscordAssetUtils.loadAssets(CraftPresence.CONFIG.clientId, true);
    }

    /**
     * Synchronizes RPC Data towards that of being in a Loading State
     */
    public static void setLoadingPresence() {
        // Form Argument Lists
        List<Pair<String, String>> loadingArgs = Lists.newArrayList();

        // Add All Generalized Arguments, if any
        if (!CraftPresence.CLIENT.generalArgs.isEmpty()) {
            loadingArgs.addAll(CraftPresence.CLIENT.generalArgs);
        }

        CraftPresence.CLIENT.clearPartyData(true, false);

        CraftPresence.CLIENT.syncArgument("&MAINMENU&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.loadingMessage, loadingArgs), false);
        CraftPresence.CLIENT.syncArgument("&MAINMENU&", CraftPresence.CLIENT.imageOf(CraftPresence.CONFIG.defaultIcon, "", false), true);

        isLoadingGame = true;
    }

    /**
     * Synchronizes RPC Data towards that of being in the Main Menu
     */
    public static void setMainMenuPresence() {
        // Form Argument Lists
        List<Pair<String, String>> mainMenuArgs = Lists.newArrayList();

        // Add All Generalized Arguments, if any
        if (!CraftPresence.CLIENT.generalArgs.isEmpty()) {
            mainMenuArgs.addAll(CraftPresence.CLIENT.generalArgs);
        }

        // Clear Loading Game State, if applicable
        if (isLoadingGame) {
            CraftPresence.CLIENT.initArgument("&MAINMENU&");

            isLoadingGame = false;
        }

        CraftPresence.CLIENT.syncArgument("&MAINMENU&", StringUtils.sequentialReplaceAnyCase(CraftPresence.CONFIG.mainMenuMessage, mainMenuArgs), false);
        CraftPresence.CLIENT.syncArgument("&MAINMENU&", CraftPresence.CLIENT.imageOf(CraftPresence.CONFIG.defaultIcon, "", false), true);

        isInMainMenu = true;
    }
}
