package com.gitlab.cdagaming.craftpresence.handler.world;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAsset;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import net.minecraft.world.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class DimensionHandler {
    public boolean isInUse = false;

    public List<String> DIMENSION_NAMES = new ArrayList<>();
    private List<Integer> DIMENSION_IDS = new ArrayList<>();
    private List<DimensionType> DIMENSION_TYPES = new ArrayList<>();
    private String CURRENT_DIMENSION_NAME;
    private Integer CURRENT_DIMENSION_ID;

    private boolean enabled = false, queuedForUpdate = false;

    public void emptyData() {
        DIMENSION_NAMES.clear();
        DIMENSION_IDS.clear();
        DIMENSION_TYPES.clear();
        clearClientData();
    }

    private void clearClientData() {
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

        if (enabled && CraftPresence.player != null) {
            updateDimensionData();
            isInUse = true;
        }

        if (isInUse) {
            if (enabled && CraftPresence.player == null) {
                clearClientData();
            } else if (!enabled) {
                emptyData();
            }
        }
    }

    private void updateDimensionData() {
        final DimensionType newDimensionType = CraftPresence.player.world.provider.getDimensionType();
        final String newDimensionName = newDimensionType.getName();
        final Integer newDimensionID = newDimensionType.getId();
        if (!newDimensionName.equals(CURRENT_DIMENSION_NAME) || !newDimensionID.equals(CURRENT_DIMENSION_ID)) {
            CURRENT_DIMENSION_NAME = newDimensionName;
            CURRENT_DIMENSION_ID = newDimensionID;
            queuedForUpdate = true;
        }

        if (!DIMENSION_TYPES.contains(newDimensionType)) {
            getDimensions();
        }

        if (queuedForUpdate || !CraftPresence.ENTITIES.isInUse) {
            updateDimensionPresence();
        }
    }

    private void updateDimensionPresence() {
        final String defaultDimensionMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String currentDimensionMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, CURRENT_DIMENSION_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultDimensionMSG);
        final String defaultDimensionIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 2, CraftPresence.CONFIG.splitCharacter, CraftPresence.CONFIG.defaultDimensionIcon);
        final String currentDimensionIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, CURRENT_DIMENSION_NAME, 0, 2, CraftPresence.CONFIG.splitCharacter, defaultDimensionIcon);

        String formattedIconKey = StringHandler.formatPackIcon(currentDimensionIcon.replace(" ", "_"));

        if (DiscordAssetHandler.contains(formattedIconKey) && !currentDimensionIcon.equals(defaultDimensionIcon)) {
            CraftPresence.CLIENT.setImage(formattedIconKey.replace("&icon&", CraftPresence.CONFIG.defaultDimensionIcon), DiscordAsset.AssetType.LARGE);
        } else {
            boolean matched = false;
            for (String dimension : DIMENSION_NAMES) {
                formattedIconKey = StringHandler.formatPackIcon(dimension);
                if (DiscordAssetHandler.contains(formattedIconKey) && CURRENT_DIMENSION_NAME.equalsIgnoreCase(dimension)) {
                    CraftPresence.CLIENT.setImage(formattedIconKey, DiscordAsset.AssetType.LARGE);
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                formattedIconKey = defaultDimensionIcon;
                CraftPresence.CLIENT.setImage(formattedIconKey.replace("&icon&", CraftPresence.CONFIG.defaultIcon), DiscordAsset.AssetType.LARGE);
            }
        }

        CraftPresence.CLIENT.DETAILS = currentDimensionMSG.replace("&dimension&", StringHandler.formatWord(CURRENT_DIMENSION_NAME).replace("The", "")).replace("&id&", CURRENT_DIMENSION_ID.toString());
        if (!CraftPresence.ENTITIES.isInUse) {
            CraftPresence.CLIENT.LARGEIMAGETEXT = CraftPresence.CLIENT.DETAILS;
            queuedForUpdate = false;
        } else {
            queuedForUpdate = true;
        }
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
    }

    public void getDimensions() {
        for (DimensionType TYPE : DimensionType.values()) {
            if (TYPE != null) {
                if (!DIMENSION_NAMES.contains(TYPE.getName())) {
                    DIMENSION_NAMES.add(TYPE.getName());
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
                if (!StringHandler.isNullOrEmpty(part[0]) && !DIMENSION_NAMES.contains(part[0])) {
                    DIMENSION_NAMES.add(part[0]);
                }
            }
        }
    }
}
