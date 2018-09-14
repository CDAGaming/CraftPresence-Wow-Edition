package com.gitlab.cdagaming.craftpresence.handler.world;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.ArrayList;
import java.util.List;

public class BiomeHandler {
    public List<String> BIOME_NAMES = new ArrayList<>();
    private List<Integer> BIOME_IDS = new ArrayList<>();
    private String CURRENT_BIOME_NAME;
    private Integer CURRENT_BIOME_ID;

    public void emptyData() {
        BIOME_NAMES.clear();
        BIOME_IDS.clear();
    }

    @SubscribeEvent
    public void onDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        CURRENT_BIOME_NAME = null;
        CURRENT_BIOME_ID = null;
    }

    @SubscribeEvent
    public void onChunkChange(final ChunkEvent.Load chunk) {
        final EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null && CraftPresence.CONFIG.showCurrentBiome && !CraftPresence.CONFIG.showGameState) {
            getCurrentBiomeData(player, chunk.getChunk());
            updateBiomePresence();
        }
    }

    private void getCurrentBiomeData(final EntityPlayer player, final Chunk chunk) {
        final Biome CURRENT_BIOME = chunk.getBiome(player.getPosition(), chunk.getWorld().getBiomeProvider());
        CURRENT_BIOME_NAME = CURRENT_BIOME.getBiomeName();
        CURRENT_BIOME_ID = Biome.getIdForBiome(CURRENT_BIOME);
    }

    private void updateBiomePresence() {
        final String defaultBiomeMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.biomeMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String currentBiomeMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.biomeMessages, CURRENT_BIOME_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultBiomeMSG);
        CraftPresence.CLIENT.GAME_STATE = currentBiomeMSG.replace("&biome&", CURRENT_BIOME_NAME).replace("&id&", CURRENT_BIOME_ID.toString());
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
    }

    public void getBiomes() {
        for (Biome biome : Biome.REGISTRY) {
            if (!BIOME_NAMES.contains(biome.getBiomeName())) {
                BIOME_NAMES.add(biome.getBiomeName());
            }
            if (!BIOME_IDS.contains(Biome.getIdForBiome(biome))) {
                BIOME_IDS.add(Biome.getIdForBiome(biome));
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
