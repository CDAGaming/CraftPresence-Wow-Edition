package com.gitlab.cdagaming.craftpresence.handler.world;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.FileHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAsset;
import com.google.common.collect.Lists;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DimensionHandler {
    public boolean isInUse = false, enabled = false;

    public String CURRENT_DIMENSION_NAME, CURRENT_DIMENSION_NAME_ID;
    public List<String> DIMENSION_NAMES = Lists.newArrayList();
    private List<Integer> DIMENSION_IDS = Lists.newArrayList();
    private List<DimensionType> DIMENSION_TYPES = Lists.newArrayList();
    private Integer CURRENT_DIMENSION_ID;

    private boolean queuedForUpdate = false;

    private void emptyData() {
        DIMENSION_NAMES.clear();
        DIMENSION_IDS.clear();
        DIMENSION_TYPES.clear();
        clearClientData();
    }

    public void clearClientData() {
        CURRENT_DIMENSION_NAME = null;
        CURRENT_DIMENSION_ID = null;

        queuedForUpdate = false;
        isInUse = false;
    }

    public void onTick() {
        enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.CONFIG.showCurrentDimension : enabled;
        final boolean needsUpdate = enabled && (
                DIMENSION_NAMES.isEmpty() || DIMENSION_IDS.isEmpty() || DIMENSION_TYPES.isEmpty()
        );

        if (needsUpdate) {
            getDimensions();
        }

        if (enabled) {
            if (CraftPresence.player != null) {
                isInUse = true;
                updateDimensionData();
            } else {
                clearClientData();
            }
        } else {
            emptyData();
        }
    }

    private void updateDimensionData() {
        final WorldProvider newProvider = CraftPresence.player.world.provider;
        final DimensionType newDimensionType = newProvider.getDimensionType();
        final String newDimensionName = StringHandler.formatDimensionName(newDimensionType.getName(), false);

        final String newDimension_primaryNameID = StringHandler.formatDimensionName(newDimensionType.getName(), true);
        final String newDimension_alternativeNameID = StringHandler.formatDimensionName(newProvider.getClass().getSimpleName(), true);
        final String newDimension_nameID = !DIMENSION_NAMES.isEmpty() && DIMENSION_NAMES.contains(newDimension_alternativeNameID) ? newDimension_alternativeNameID : newDimension_primaryNameID;

        final Integer newDimensionID = newDimensionType.getId();

        if (!newDimension_nameID.equals(CURRENT_DIMENSION_NAME_ID) || !newDimensionID.equals(CURRENT_DIMENSION_ID)) {
            CURRENT_DIMENSION_NAME = !StringHandler.isNullOrEmpty(newDimensionName) ? newDimensionName : newDimension_nameID;
            CURRENT_DIMENSION_NAME_ID = newDimension_nameID;
            CURRENT_DIMENSION_ID = newDimensionID;
            queuedForUpdate = true;

            if (!DIMENSION_NAMES.contains(newDimension_nameID)) {
                DIMENSION_NAMES.add(newDimension_nameID);
            }
            if (!DIMENSION_TYPES.contains(newDimensionType)) {
                DIMENSION_TYPES.add(newDimensionType);
            }
            if (!DIMENSION_IDS.contains(newDimensionID)) {
                DIMENSION_IDS.add(newDimensionID);
            }
        }

        if (queuedForUpdate) {
            updateDimensionPresence();
        }
    }

    public void updateDimensionPresence() {
        final String defaultDimensionMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String currentDimensionMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, CURRENT_DIMENSION_NAME_ID, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultDimensionMSG);
        final String currentDimensionIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, CURRENT_DIMENSION_NAME_ID, 0, 2, CraftPresence.CONFIG.splitCharacter, CURRENT_DIMENSION_NAME_ID);
        final String formattedIconKey = StringHandler.formatPackIcon(currentDimensionIcon.replace(" ", "_"));

        CraftPresence.CLIENT.setImage(formattedIconKey.replace("&icon&", CraftPresence.CONFIG.defaultDimensionIcon), DiscordAsset.AssetType.LARGE);

        CraftPresence.CLIENT.DETAILS = StringHandler.formatWord(currentDimensionMSG.replace("&dimension&", StringHandler.formatWord(CURRENT_DIMENSION_NAME)).replace("&id&", CURRENT_DIMENSION_ID.toString()));
        if (!CraftPresence.ENTITIES.isInUse || CraftPresence.ENTITIES.allItemsEmpty) {
            CraftPresence.CLIENT.LARGEIMAGETEXT = CraftPresence.CLIENT.DETAILS;
            queuedForUpdate = false;
        } else {
            queuedForUpdate = true;
        }
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
    }

    private List<DimensionType> getDimensionTypes() {
        List<DimensionType> dimensionTypes = Lists.newArrayList();
        Map<?, ?> reflectedDimensionTypes = (Map<?, ?>) StringHandler.lookupObject(DimensionType.class, null, "dimensionTypes");

        Collections.addAll(dimensionTypes, DimensionType.values());

        if (dimensionTypes.isEmpty()) {
            // Fallback 1: Use Reflected Dimension Types
            if (reflectedDimensionTypes != null) {
                for (Object objectType : reflectedDimensionTypes.values()) {
                    DimensionType type = (objectType instanceof DimensionType) ? (DimensionType) objectType : null;

                    if (type != null && !dimensionTypes.contains(type)) {
                        dimensionTypes.add(type);
                    }
                }
            } else {
                // Fallback 2: Use Manual Class Lookup
                for (Class classObj : FileHandler.getClassNamesMatchingSuperType(WorldProvider.class, "net.minecraft", "com.gitlab.cdagaming.craftpresence")) {
                    if (classObj != null) {
                        try {
                            WorldProvider providerObj = (WorldProvider) classObj.newInstance();
                            if (providerObj != null && !dimensionTypes.contains(providerObj.getDimensionType())) {
                                dimensionTypes.add(providerObj.getDimensionType());
                            }
                        } catch (Exception ignored) {
                            // Ignore Any Exceptions
                        } catch (Error ignored) {
                            // Ignore Any Errors
                        }
                    }
                }
            }
        }

        return dimensionTypes;
    }

    public void getDimensions() {
        for (DimensionType TYPE : getDimensionTypes()) {
            if (TYPE != null) {
                if (!DIMENSION_NAMES.contains(StringHandler.formatDimensionName(TYPE.getName(), true))) {
                    DIMENSION_NAMES.add(StringHandler.formatDimensionName(TYPE.getName(), true));
                }
                if (!DIMENSION_IDS.contains(TYPE.getId())) {
                    DIMENSION_IDS.add(TYPE.getId());
                }
                if (!DIMENSION_TYPES.contains(TYPE)) {
                    DIMENSION_TYPES.add(TYPE);
                }
            }
        }

        for (String dimensionMessage : CraftPresence.CONFIG.dimensionMessages) {
            if (!StringHandler.isNullOrEmpty(dimensionMessage)) {
                final String[] part = dimensionMessage.split(CraftPresence.CONFIG.splitCharacter);
                if (!StringHandler.isNullOrEmpty(part[0]) && !DIMENSION_NAMES.contains(StringHandler.formatDimensionName(part[0], true))) {
                    DIMENSION_NAMES.add(StringHandler.formatDimensionName(part[0], true));
                }
            }
        }
    }
}
