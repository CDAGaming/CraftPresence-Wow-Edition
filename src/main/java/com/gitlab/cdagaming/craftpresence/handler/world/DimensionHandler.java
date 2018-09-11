package com.gitlab.cdagaming.craftpresence.handler.world;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.CommandHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAsset;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.ArrayList;
import java.util.List;

public class DimensionHandler {
    public List<String> DIMENSION_NAMES = new ArrayList<>();
    private List<Integer> DIMENSION_IDS = new ArrayList<>();
    private String CURRENT_DIMENSION_NAME;
    private Integer CURRENT_DIMENSION_ID;

    public void emptyData() {
        DIMENSION_NAMES.clear();
        DIMENSION_IDS.clear();
        CURRENT_DIMENSION_NAME = null;
        CURRENT_DIMENSION_ID = null;
    }

    @SubscribeEvent
    public void worldLoad(final EntityJoinWorldEvent event) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        final EntityPlayer player = minecraft.player;
        if (player != null && event.getEntity() == player) {
            if (CraftPresence.CONFIG.showGameState && !CraftPresence.CONFIG.enablePERItem && minecraft.isSingleplayer()) {
                CraftPresence.CLIENT.GAME_STATE = CraftPresence.CONFIG.singleplayerMSG;
            }
            if (CraftPresence.CONFIG.rebootOnWorldLoad) {
                CommandHandler.rebootRPC();
                CraftPresence.CONFIG.rebootOnWorldLoad = false;
            }
            updateDimensionData(player.world);
        }
    }

    @SubscribeEvent
    public void onDimensionChange(final PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.player != null && event.player == Minecraft.getMinecraft().player) {
            updateDimensionData(event.player.world);
        }
    }

    private void getCurrentDimensionData(final World world) {
        if (world != null) {
            CURRENT_DIMENSION_NAME = world.provider.getDimensionType().getName();
            CURRENT_DIMENSION_ID = world.provider.getDimensionType().getId();
        }
    }

    private void updateDimensionData(final World world) {
        if (CraftPresence.CONFIG.showCurrentDimension) {
            getCurrentDimensionData(world);
            updateDimensionPresence();
        }
    }

    private void updateDimensionPresence() {
        final String defaultDimensionMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String currentDimensionMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, CURRENT_DIMENSION_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultDimensionMSG);
        final String defaultDimensionIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 2, CraftPresence.CONFIG.splitCharacter, CraftPresence.CONFIG.defaultDimensionIcon);

        final String currentDimensionIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, CURRENT_DIMENSION_NAME, 0, 2, CraftPresence.CONFIG.splitCharacter, defaultDimensionIcon);
        final String formattedDimensionIcon = StringHandler.formatPackIcon(currentDimensionIcon.replace(" ", "_"));
        CraftPresence.CLIENT.DETAILS = currentDimensionMSG.replace("&dimension&", StringHandler.formatWord(CURRENT_DIMENSION_NAME).replace("The", "")).replace("&id&", CURRENT_DIMENSION_ID.toString());

        if (DiscordAssetHandler.contains(formattedDimensionIcon) && !currentDimensionIcon.equals(defaultDimensionIcon)) {
            CraftPresence.CLIENT.setImage(formattedDimensionIcon.replace("&icon&", CraftPresence.CONFIG.defaultDimensionIcon), DiscordAsset.AssetType.LARGE);
        } else {
            boolean matched = false;
            for (String dimension : DIMENSION_NAMES) {
                final String formattedKey = StringHandler.formatPackIcon(dimension);
                if (DiscordAssetHandler.contains(formattedKey)) {
                    CraftPresence.CLIENT.setImage(formattedKey, DiscordAsset.AssetType.LARGE);
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                CraftPresence.CLIENT.setImage(formattedDimensionIcon.replace("&icon&", CraftPresence.CONFIG.defaultDimensionIcon), DiscordAsset.AssetType.LARGE);
            }
        }

        if (!CraftPresence.CONFIG.enablePERItem) {
            CraftPresence.CLIENT.LARGEIMAGETEXT = CraftPresence.CLIENT.DETAILS;
        }

        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
    }

    public void getDimensions() {
        for (DimensionType TYPE : DimensionType.values()) {
            if (!DIMENSION_NAMES.contains(TYPE.getName())) {
                DIMENSION_NAMES.add(TYPE.getName());
            }
            if (!DIMENSION_IDS.contains(TYPE.getId())) {
                DIMENSION_IDS.add(TYPE.getId());
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
