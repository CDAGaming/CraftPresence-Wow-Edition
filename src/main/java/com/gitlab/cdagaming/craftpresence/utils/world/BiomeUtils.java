package com.gitlab.cdagaming.craftpresence.utils.world;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.utils.FileUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.google.common.collect.Lists;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class BiomeUtils {
    public boolean isInUse = false, enabled = false;

    public List<String> BIOME_NAMES = Lists.newArrayList();
    private List<Biome> BIOME_TYPES = Lists.newArrayList();
    private List<Integer> BIOME_IDS = Lists.newArrayList();
    private String CURRENT_BIOME_NAME;
    private Integer CURRENT_BIOME_ID;

    private void emptyData() {
        BIOME_NAMES.clear();
        BIOME_IDS.clear();
        BIOME_TYPES.clear();
        clearClientData();
    }

    public void clearClientData() {
        CURRENT_BIOME_NAME = null;
        CURRENT_BIOME_ID = null;

        isInUse = false;
    }

    public void onTick() {
        enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.CONFIG.showCurrentBiome && !CraftPresence.CONFIG.showGameState : enabled;
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

    public void updateBiomePresence() {
        final String defaultBiomeMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.biomeMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String currentBiomeMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.biomeMessages, CURRENT_BIOME_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultBiomeMSG);
        final String currentBiomeIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.biomeMessages, CURRENT_BIOME_NAME, 0, 2, CraftPresence.CONFIG.splitCharacter, CURRENT_BIOME_NAME);
        final String formattedIconKey = StringUtils.formatPackIcon(currentBiomeIcon.replace(" ", "_"));

        final String CURRENT_BIOME_ICON = formattedIconKey.replace("&icon&", CraftPresence.CONFIG.defaultDimensionIcon);
        final String CURRENT_BIOME_MESSAGE = StringUtils.formatWord(currentBiomeMSG.replace("&biome&", CURRENT_BIOME_NAME).replace("&id&", CURRENT_BIOME_ID.toString()));

        CraftPresence.CLIENT.syncPlaceholder("&BIOME&", CURRENT_BIOME_MESSAGE, false);
        CraftPresence.CLIENT.syncPlaceholder("&BIOME&", CURRENT_BIOME_ICON, true);
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
    }

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
            for (Class classObj : FileUtils.getClassNamesMatchingSuperType(Biome.class, "net.minecraft", "com.gitlab.cdagaming.craftpresence")) {
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
