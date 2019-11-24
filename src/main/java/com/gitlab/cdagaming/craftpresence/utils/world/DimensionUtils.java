package com.gitlab.cdagaming.craftpresence.utils.world;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.FileUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.google.common.collect.Lists;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DimensionUtils {
    public boolean isInUse = false, enabled = false;
    public List<String> DIMENSION_NAMES = Lists.newArrayList();

    private String CURRENT_DIMENSION_NAME, CURRENT_DIMENSION_NAME_ID;
    private List<Integer> DIMENSION_IDS = Lists.newArrayList();
    private List<DimensionType> DIMENSION_TYPES = Lists.newArrayList();
    private Integer CURRENT_DIMENSION_ID;

    private void emptyData() {
        DIMENSION_NAMES.clear();
        DIMENSION_IDS.clear();
        DIMENSION_TYPES.clear();
        clearClientData();
    }

    public void clearClientData() {
        CURRENT_DIMENSION_NAME = null;
        CURRENT_DIMENSION_ID = null;

        isInUse = false;
        CraftPresence.CLIENT.initArgumentData("&DIMENSION&");
        CraftPresence.CLIENT.initIconData("&DIMENSION&");
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
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
        final String newDimensionName = StringUtils.formatDimensionName(newDimensionType.getName(), false);

        final String newDimension_primaryNameID = StringUtils.formatDimensionName(newDimensionType.getName(), true);
        final String newDimension_alternativeNameID = StringUtils.formatDimensionName(newProvider.getClass().getSimpleName(), true);
        final String newDimension_nameID = !DIMENSION_NAMES.isEmpty() && DIMENSION_NAMES.contains(newDimension_alternativeNameID) ? newDimension_alternativeNameID : newDimension_primaryNameID;

        final Integer newDimensionID = newDimensionType.getId();

        if (!newDimension_nameID.equals(CURRENT_DIMENSION_NAME_ID) || !newDimensionID.equals(CURRENT_DIMENSION_ID)) {
            CURRENT_DIMENSION_NAME = !StringUtils.isNullOrEmpty(newDimensionName) ? newDimensionName : newDimension_nameID;
            CURRENT_DIMENSION_NAME_ID = newDimension_nameID;
            CURRENT_DIMENSION_ID = newDimensionID;

            if (!DIMENSION_NAMES.contains(newDimension_nameID)) {
                DIMENSION_NAMES.add(newDimension_nameID);
            }
            if (!DIMENSION_TYPES.contains(newDimensionType)) {
                DIMENSION_TYPES.add(newDimensionType);
            }
            if (!DIMENSION_IDS.contains(newDimensionID)) {
                DIMENSION_IDS.add(newDimensionID);
            }

            updateDimensionPresence();
        }
    }

    public void updateDimensionPresence() {
        // Form Dimension Argument List
        List<Tuple<String, String>> dimensionArgs = Lists.newArrayList();

        dimensionArgs.add(new Tuple<>("&DIMENSION&", CURRENT_DIMENSION_NAME));
        dimensionArgs.add(new Tuple<>("&ID&", CURRENT_DIMENSION_ID.toString()));

        // Add All Generalized Arguments, if any
        if (!CraftPresence.CLIENT.generalArgs.isEmpty()) {
            dimensionArgs.addAll(CraftPresence.CLIENT.generalArgs);
        }

        final String defaultDimensionMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String currentDimensionMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, CURRENT_DIMENSION_NAME_ID, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultDimensionMSG);
        final String currentDimensionIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, CURRENT_DIMENSION_NAME_ID, 0, 2, CraftPresence.CONFIG.splitCharacter, CURRENT_DIMENSION_NAME_ID);
        final String formattedIconKey = StringUtils.formatPackIcon(currentDimensionIcon.replace(" ", "_"));

        final String CURRENT_DIMENSION_ICON = formattedIconKey.replace("&icon&", CraftPresence.CONFIG.defaultDimensionIcon);
        final String CURRENT_DIMENSION_MESSAGE = StringUtils.sequentialReplaceAnyCase(currentDimensionMSG, dimensionArgs);

        CraftPresence.CLIENT.syncArgument("&DIMENSION&", CURRENT_DIMENSION_MESSAGE, false);
        CraftPresence.CLIENT.syncArgument("&DIMENSION&", CURRENT_DIMENSION_ICON, true);
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
    }

    private List<DimensionType> getDimensionTypes() {
        List<DimensionType> dimensionTypes = Lists.newArrayList();
        Map<?, ?> reflectedDimensionTypes = (Map<?, ?>) StringUtils.lookupObject(DimensionType.class, null, "dimensionTypes");

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
                for (Class classObj : FileUtils.getClassNamesMatchingSuperType(WorldProvider.class, "net.minecraft", "com.gitlab.cdagaming.craftpresence")) {
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
                if (!DIMENSION_NAMES.contains(StringUtils.formatDimensionName(TYPE.getName(), true))) {
                    DIMENSION_NAMES.add(StringUtils.formatDimensionName(TYPE.getName(), true));
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
            if (!StringUtils.isNullOrEmpty(dimensionMessage)) {
                final String[] part = dimensionMessage.split(CraftPresence.CONFIG.splitCharacter);
                if (!StringUtils.isNullOrEmpty(part[0]) && !DIMENSION_NAMES.contains(StringUtils.formatDimensionName(part[0], true))) {
                    DIMENSION_NAMES.add(StringUtils.formatDimensionName(part[0], true));
                }
            }
        }
    }
}
