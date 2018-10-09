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
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.ArrayList;
import java.util.List;

public class DimensionHandler {
    public List<String> DIMENSION_NAMES = new ArrayList<>();
    private List<Integer> DIMENSION_IDS = new ArrayList<>();
    private String CURRENT_DIMENSION_NAME, formattedMSG, formattedIconKey;
    private Integer CURRENT_DIMENSION_ID;

    public void emptyData() {
        DIMENSION_NAMES.clear();
        DIMENSION_IDS.clear();
    }

    @SubscribeEvent
    public void onDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        CURRENT_DIMENSION_NAME = null;
        formattedMSG = null;
        formattedIconKey = null;
        CURRENT_DIMENSION_ID = null;
    }

    @SubscribeEvent
    public void worldLoad(final EntityJoinWorldEvent event) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        final EntityPlayer player = minecraft.player;
        final boolean isPlayerAvailable = event.getEntity() != null && event.getEntity() == player;

        if (isPlayerAvailable && !CraftPresence.CONFIG.hasChanged) {
            if (CraftPresence.CONFIG.showCurrentDimension) {
                updateDimensionData(event.getEntity().world);
            } else {
                CraftPresence.CLIENT.DETAILS = "";
                CraftPresence.CLIENT.LARGEIMAGEKEY = CraftPresence.CONFIG.defaultIcon;
                CraftPresence.CLIENT.LARGEIMAGETEXT = I18n.format("craftpresence.defaults.state.mcversion", Constants.MCVersion);
                CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
            }
        }
    }

    @SubscribeEvent
    public void onTick(final TickEvent.PlayerTickEvent event) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        final EntityPlayer player = minecraft.player;
        final boolean isPlayerAvailable = event.player != null && event.player == player;
        final boolean isIncorrectPresence = CraftPresence.CONFIG.showCurrentDimension &&
                (!CraftPresence.CLIENT.DETAILS.equals(formattedMSG) ||
                        !CraftPresence.CLIENT.LARGEIMAGEKEY.equals(formattedIconKey) ||
                        !CraftPresence.CLIENT.LARGEIMAGETEXT.equals(formattedMSG)
                );
        final boolean removeDimensionData = !CraftPresence.CONFIG.showCurrentDimension &&
                (CraftPresence.CLIENT.DETAILS.equals(formattedMSG) &&
                        CraftPresence.CLIENT.LARGEIMAGEKEY.equals(formattedIconKey) &&
                        CraftPresence.CLIENT.LARGEIMAGETEXT.equals(formattedMSG)
                );

        if (isPlayerAvailable && !CraftPresence.CONFIG.hasChanged) {
            if (isIncorrectPresence) {
                updateDimensionData(event.player.world);
            } else if (removeDimensionData) {
                CraftPresence.CLIENT.DETAILS = "";
                CraftPresence.CLIENT.LARGEIMAGEKEY = CraftPresence.CONFIG.defaultIcon;
                CraftPresence.CLIENT.LARGEIMAGETEXT = I18n.format("craftpresence.defaults.state.mcversion", Constants.MCVersion);
                CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
            }
        }
    }

    private void getCurrentDimensionData(final World world) {
        if (CraftPresence.CONFIG.showCurrentDimension && !CraftPresence.CONFIG.hasChanged && world != null) {
            CURRENT_DIMENSION_NAME = world.provider.getDimensionType().getName();
            CURRENT_DIMENSION_ID = world.provider.getDimensionType().getId();
        }
    }

    private void updateDimensionData(final World world) {
        if (CraftPresence.CONFIG.showCurrentDimension && !CraftPresence.CONFIG.hasChanged && world != null) {
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
        for (DimensionType TYPE : DimensionType.values()) {
            if (TYPE != null) {
                if (!DIMENSION_NAMES.contains(TYPE.getName())) {
                    DIMENSION_NAMES.add(TYPE.getName());
                }
                if (!DIMENSION_IDS.contains(TYPE.getId())) {
                    DIMENSION_IDS.add(TYPE.getId());
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
