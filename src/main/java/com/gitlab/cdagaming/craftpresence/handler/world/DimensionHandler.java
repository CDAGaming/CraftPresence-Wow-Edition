package com.gitlab.cdagaming.craftpresence.handler.world;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAsset;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DimensionHandler {
    public boolean enabled = false;
    public List<String> DIMENSION_NAMES = new ArrayList<>();
    private List<Integer> DIMENSION_IDS = new ArrayList<>();
    private List<DimensionType> DIMENSION_TYPES = new ArrayList<>();
    private String CURRENT_DIMENSION_NAME, formattedMSG, formattedIconKey;
    private Integer CURRENT_DIMENSION_ID;

    public void emptyData() {
        CURRENT_DIMENSION_NAME = null;
        CURRENT_DIMENSION_ID = null;

        DIMENSION_NAMES.clear();
        DIMENSION_IDS.clear();
        DIMENSION_TYPES.clear();
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.CONFIG.showCurrentDimension : enabled;
        final Minecraft minecraft = Minecraft.getMinecraft();
        final EntityPlayer player = minecraft.player;
        final boolean isPlayerAvailable = player != null;
        final boolean needsUpdate = enabled &&
                (StringHandler.isNullOrEmpty(CURRENT_DIMENSION_NAME) ||
                        StringHandler.isNullOrEmpty(CURRENT_DIMENSION_ID.toString()) ||
                        DIMENSION_NAMES.isEmpty() || DIMENSION_IDS.isEmpty()
                );
        final boolean isIncorrectPresence = enabled &&
                (!CraftPresence.CLIENT.DETAILS.equals(formattedMSG) ||
                        !CraftPresence.CLIENT.LARGEIMAGEKEY.equals(formattedIconKey) ||
                        !CraftPresence.CLIENT.LARGEIMAGETEXT.equals(formattedMSG)
                );
        final boolean removeDimensionData = !enabled &&
                (CraftPresence.CLIENT.DETAILS.equals(formattedMSG) &&
                        CraftPresence.CLIENT.LARGEIMAGEKEY.equals(formattedIconKey) &&
                        CraftPresence.CLIENT.LARGEIMAGETEXT.equals(formattedMSG)
                );

        if (enabled) {
            if (needsUpdate || isIncorrectPresence) {
                if (getDimensionTypes() != DIMENSION_TYPES) {
                    getDimensions();
                }
                if (isPlayerAvailable) {
                    updateDimensionData(player.world);
                }
            }
        } else if (removeDimensionData) {
            emptyData();
            formattedMSG = null;
            formattedIconKey = null;
            CraftPresence.CLIENT.DETAILS = "";
            CraftPresence.CLIENT.LARGEIMAGEKEY = CraftPresence.CONFIG.defaultIcon;
            CraftPresence.CLIENT.LARGEIMAGETEXT = I18n.format("craftpresence.defaults.state.mcversion", Constants.MCVersion);
            CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
        }
    }

    private List<DimensionType> getDimensionTypes() {
        List<DimensionType> dimensionTypes = new ArrayList<>();
        Collections.addAll(dimensionTypes, DimensionType.values());

        return dimensionTypes;
    }

    private void getCurrentDimensionData(final World world) {
        if (world != null) {
            CURRENT_DIMENSION_NAME = world.provider.getDimensionType().getName();
            CURRENT_DIMENSION_ID = world.provider.getDimensionType().getId();
        }
    }

    private void updateDimensionData(final World world) {
        if (world != null) {
            getCurrentDimensionData(world);
            updateDimensionPresence();
        }
    }

    private void updateDimensionPresence() {
        final String defaultDimensionMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String currentDimensionMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, CURRENT_DIMENSION_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultDimensionMSG);
        final String defaultDimensionIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 2, CraftPresence.CONFIG.splitCharacter, CraftPresence.CONFIG.defaultDimensionIcon);
        final String currentDimensionIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, CURRENT_DIMENSION_NAME, 0, 2, CraftPresence.CONFIG.splitCharacter, defaultDimensionIcon);

        formattedIconKey = StringHandler.formatPackIcon(currentDimensionIcon.replace(" ", "_"));
        formattedMSG = currentDimensionMSG.replace("&dimension&", StringHandler.formatWord(CURRENT_DIMENSION_NAME).replace("The", "")).replace("&id&", CURRENT_DIMENSION_ID.toString());
        CraftPresence.CLIENT.DETAILS = formattedMSG;

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

        if (!CraftPresence.CONFIG.enablePERItem) {
            CraftPresence.CLIENT.LARGEIMAGETEXT = CraftPresence.CLIENT.DETAILS;
        }

        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
    }

    public void getDimensions() {
        for (DimensionType TYPE : getDimensionTypes()) {
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
