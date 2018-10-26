package com.gitlab.cdagaming.craftpresence.handler.world;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class BiomeHandler {
    public boolean isInUse = false, enabled = false;

    public List<String> BIOME_NAMES = new ArrayList<>();
    private List<Biome> BIOME_TYPES = new ArrayList<>();
    private List<Integer> BIOME_IDS = new ArrayList<>();
    private String CURRENT_BIOME_NAME;
    private Integer CURRENT_BIOME_ID;

    private boolean queuedForUpdate = false;

    public void emptyData() {
        BIOME_NAMES.clear();
        BIOME_IDS.clear();
        BIOME_TYPES.clear();
        clearClientData();
    }

    public void clearClientData() {
        CURRENT_BIOME_NAME = null;
        CURRENT_BIOME_ID = null;

        queuedForUpdate = false;
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

        if (enabled && CraftPresence.player != null) {
            isInUse = true;
            updateBiomeData();
        }

        if (isInUse) {
            if (enabled && CraftPresence.player == null) {
                clearClientData();
            } else if (!enabled) {
                emptyData();
            }
        }
    }

    private void updateBiomeData() {
        final Biome newBiome = CraftPresence.player.world.getBiome(CraftPresence.player.getPosition());
        final String newBiomeName = newBiome.getBiomeName();
        final Integer newBiomeID = Biome.getIdForBiome(newBiome);
        if (!newBiomeName.equals(CURRENT_BIOME_NAME) || !newBiomeID.equals(CURRENT_BIOME_ID)) {
            CURRENT_BIOME_NAME = newBiomeName;
            CURRENT_BIOME_ID = newBiomeID;
            queuedForUpdate = true;

            if (!BIOME_TYPES.contains(newBiome)) {
                getBiomes();
            }
        }

        if (queuedForUpdate) {
            updateBiomePresence();
        }
    }

    private void updateBiomePresence() {
        final String defaultBiomeMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.biomeMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String currentBiomeMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.biomeMessages, CURRENT_BIOME_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultBiomeMSG);
        final String formattedBiomeMSG = currentBiomeMSG.replace("&biome&", CURRENT_BIOME_NAME).replace("&id&", CURRENT_BIOME_ID.toString());

        if (!CraftPresence.GUIS.isInUse && !CraftPresence.SERVER.isInUse) {
            CraftPresence.CLIENT.GAME_STATE = formattedBiomeMSG;
            CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
            queuedForUpdate = false;
        } else {
            queuedForUpdate = true;
        }
    }

    private List<Biome> getBiomeTypes() {
        List<Biome> biomeTypes = new ArrayList<>();

        for (Biome biome : Biome.REGISTRY) {
            if (biome != null) {
                biomeTypes.add(biome);
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
            if (!StringHandler.isNullOrEmpty(biomeMessage)) {
                final String[] part = biomeMessage.split(CraftPresence.CONFIG.splitCharacter);
                if (!StringHandler.isNullOrEmpty(part[0]) && !BIOME_NAMES.contains(part[0])) {
                    BIOME_NAMES.add(part[0]);
                }
            }
        }
    }
}
