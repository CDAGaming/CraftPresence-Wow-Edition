/*
 * MIT License
 *
 * Copyright (c) 2018 - 2019 CDAGaming (cstack2011@yahoo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.gitlab.cdagaming.craftpresence.utils.world;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.FileUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.google.common.collect.Lists;
import net.minecraft.world.biome.Biome;

import java.util.List;

/**
 * Biome Utilities used to Parse Biome Data and handle related RPC Events
 *
 * @author CDAGaming
 */
public class BiomeUtils {
    /**
     * Whether this module is active and currently in use
     */
    public boolean isInUse = false;

    /**
     * Whether this module is allowed to start and enabled
     */
    public boolean enabled = false;

    /**
     * A List of the detected Biome Names
     */
    public List<String> BIOME_NAMES = Lists.newArrayList();

    /**
     * A List of the detected Biome Type's
     */
    private List<Biome> BIOME_TYPES = Lists.newArrayList();

    /**
     * A List of the detected Biome ID's
     */
    private List<Integer> BIOME_IDS = Lists.newArrayList();

    /**
     * The Name of the Current Biome the Player is in
     */
    private String CURRENT_BIOME_NAME;

    /**
     * The ID Number for the Current Biome the Player is in
     */
    private Integer CURRENT_BIOME_ID;

    /**
     * Clears FULL Data from this Module
     */
    private void emptyData() {
        BIOME_NAMES.clear();
        BIOME_IDS.clear();
        BIOME_TYPES.clear();
        clearClientData();
    }

    /**
     * Clears Runtime Client Data from this Module (PARTIAL Clear)
     */
    public void clearClientData() {
        CURRENT_BIOME_NAME = null;
        CURRENT_BIOME_ID = null;

        isInUse = false;
        CraftPresence.CLIENT.initArgumentData("&BIOME&");
        CraftPresence.CLIENT.initIconData("&BIOME&");
    }

    /**
     * Module Event to Occur on each tick within the Application
     */
    public void onTick() {
        enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.CONFIG.showCurrentBiome : enabled;
        final boolean needsUpdate = enabled && (
                BIOME_NAMES.isEmpty() || BIOME_IDS.isEmpty() || BIOME_TYPES.isEmpty()
        );

        if (needsUpdate) {
            getBiomes();
        }

        if (enabled) {
            if (CraftPresence.player != null) {
                isInUse = true;
                updateBiomeData();
            } else {
                clearClientData();
            }
        } else {
            emptyData();
        }
    }

    /**
     * Synchronizes Data related to this module, if needed
     */
    private void updateBiomeData() {
        final Biome newBiome = CraftPresence.player.world.getBiome(CraftPresence.player.getPosition());
        final String newBiomeName = newBiome.getBiomeName();
        final Integer newBiomeID = Biome.getIdForBiome(newBiome);

        if (!newBiomeName.equals(CURRENT_BIOME_NAME) || !newBiomeID.equals(CURRENT_BIOME_ID)) {
            CURRENT_BIOME_NAME = newBiomeName;
            CURRENT_BIOME_ID = newBiomeID;

            if (!BIOME_NAMES.contains(newBiomeName)) {
                BIOME_NAMES.add(newBiomeName);
            }
            if (!BIOME_IDS.contains(newBiomeID)) {
                BIOME_IDS.add(newBiomeID);
            }
            if (!BIOME_TYPES.contains(newBiome)) {
                BIOME_TYPES.add(newBiome);
            }

            updateBiomePresence();
        }
    }

    /**
     * Updates RPC Data related to this Module
     */
    public void updateBiomePresence() {
        // Form Biome Argument List
        List<Tuple<String, String>> biomeArgs = Lists.newArrayList();

        biomeArgs.add(new Tuple<>("&BIOME&", CURRENT_BIOME_NAME));
        biomeArgs.add(new Tuple<>("&ID&", CURRENT_BIOME_ID.toString()));

        // Add All Generalized Arguments, if any
        if (!CraftPresence.CLIENT.generalArgs.isEmpty()) {
            biomeArgs.addAll(CraftPresence.CLIENT.generalArgs);
        }

        final String defaultBiomeMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.biomeMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String currentBiomeMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.biomeMessages, CURRENT_BIOME_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultBiomeMSG);

        final String CURRENT_BIOME_MESSAGE = StringUtils.sequentialReplaceAnyCase(currentBiomeMSG, biomeArgs);

        CraftPresence.CLIENT.syncArgument("&BIOME&", CURRENT_BIOME_MESSAGE, false);
        CraftPresence.CLIENT.initIconData("&BIOME&");
    }

    /**
     * Retrieves a List of detected Biome Types
     *
     * @return The detected Biome Types found
     */
    private List<Biome> getBiomeTypes() {
        List<Biome> biomeTypes = Lists.newArrayList();

        if (Biome.REGISTRY != null) {
            for (Biome biome : Biome.REGISTRY) {
                if (biome != null && !biomeTypes.contains(biome)) {
                    biomeTypes.add(biome);
                }
            }
        }

        if (biomeTypes.isEmpty()) {
            // Fallback: Use Manual Class Lookup
            for (Class<?> classObj : FileUtils.getClassNamesMatchingSuperType(Biome.class, "net.minecraft", "com.gitlab.cdagaming.craftpresence")) {
                if (classObj != null) {
                    try {
                        Biome biomeObj = (Biome) classObj.newInstance();
                        if (biomeObj != null && !biomeTypes.contains(biomeObj)) {
                            biomeTypes.add(biomeObj);
                        }
                    } catch (Exception ignored) {
                        // Ignore Any Exceptions
                    } catch (Error ignored) {
                        // Ignore Any Errors
                    }
                }
            }
        }

        return biomeTypes;
    }

    /**
     * Updates and Initializes Module Data, based on found Information
     */
    public void getBiomes() {
        for (Biome biome : getBiomeTypes()) {
            if (biome != null) {
                if (!BIOME_NAMES.contains(biome.getBiomeName())) {
                    BIOME_NAMES.add(biome.getBiomeName());
                }
                if (!BIOME_IDS.contains(Biome.getIdForBiome(biome))) {
                    BIOME_IDS.add(Biome.getIdForBiome(biome));
                }
                if (!BIOME_TYPES.contains(biome)) {
                    BIOME_TYPES.add(biome);
                }
            }
        }

        for (String biomeMessage : CraftPresence.CONFIG.biomeMessages) {
            if (!StringUtils.isNullOrEmpty(biomeMessage)) {
                final String[] part = biomeMessage.split(CraftPresence.CONFIG.splitCharacter);
                if (!StringUtils.isNullOrEmpty(part[0]) && !BIOME_NAMES.contains(part[0])) {
                    BIOME_NAMES.add(part[0]);
                }
            }
        }
    }
}
