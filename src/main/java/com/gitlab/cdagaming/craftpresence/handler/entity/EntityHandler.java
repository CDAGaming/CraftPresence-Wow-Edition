package com.gitlab.cdagaming.craftpresence.handler.entity;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityHandler {
    public List<String> ENTITY_NAMES = new ArrayList<>();
    private List<String> BLOCK_NAMES = new ArrayList<>();
    private List<String> BLOCK_CLASSES = new ArrayList<>();
    private List<String> ITEM_NAMES = new ArrayList<>();
    private List<String> ITEM_CLASSES = new ArrayList<>();
    private List<String> ENTITY_CLASSES = new ArrayList<>();

    private String CURRENT_MAINHAND_ITEM_NAME;
    private String CURRENT_OFFHAND_ITEM_NAME;

    private ItemStack CURRENT_MAINHAND_ITEM;
    private ItemStack CURRENT_OFFHAND_ITEM;

    public void emptyData() {
        BLOCK_NAMES.clear();
        BLOCK_CLASSES.clear();
        ITEM_NAMES.clear();
        ITEM_CLASSES.clear();
        ENTITY_NAMES.clear();
        ENTITY_CLASSES.clear();
    }

    @SubscribeEvent
    public void onDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        CURRENT_MAINHAND_ITEM = ItemStack.EMPTY;
        CURRENT_OFFHAND_ITEM = ItemStack.EMPTY;
        CURRENT_MAINHAND_ITEM_NAME = null;
        CURRENT_OFFHAND_ITEM_NAME = null;
    }

    @SubscribeEvent
    public void updateEntityData(final TickEvent.PlayerTickEvent event) {
        final EntityPlayer player = Minecraft.getMinecraft().player;
        if (CraftPresence.CONFIG.enablePERItem && player != null) {
            getCurrentlyHeldItem(player);
            updateEntityPresence();
        }
    }

    private boolean isEmpty(final ItemStack itemStack) {
        return itemStack.isEmpty() || itemStack.getItem() == Items.AIR;
    }

    private void getCurrentlyHeldItem(final EntityPlayer player) {
        if (player != null) {
            final ItemStack NEW_CURRENT_MAINHAND_ITEM = player.getHeldItemMainhand();
            final ItemStack NEW_CURRENT_OFFHAND_ITEM = player.getHeldItemOffhand();
            final String NEW_CURRENT_MAINHAND_ITEM_NAME = StringHandler.stripColors(NEW_CURRENT_MAINHAND_ITEM.getDisplayName());
            final String NEW_CURRENT_OFFHAND_ITEM_NAME = StringHandler.stripColors(NEW_CURRENT_OFFHAND_ITEM.getDisplayName());

            if (!NEW_CURRENT_MAINHAND_ITEM.equals(CURRENT_MAINHAND_ITEM) && !NEW_CURRENT_MAINHAND_ITEM_NAME.equals(CURRENT_MAINHAND_ITEM_NAME)) {
                CURRENT_MAINHAND_ITEM = NEW_CURRENT_MAINHAND_ITEM;
                CURRENT_MAINHAND_ITEM_NAME = NEW_CURRENT_MAINHAND_ITEM_NAME;
            }
            if (!NEW_CURRENT_OFFHAND_ITEM.equals(CURRENT_OFFHAND_ITEM) && !NEW_CURRENT_OFFHAND_ITEM_NAME.equals(CURRENT_OFFHAND_ITEM_NAME)) {
                CURRENT_OFFHAND_ITEM = NEW_CURRENT_OFFHAND_ITEM;
                CURRENT_OFFHAND_ITEM_NAME = NEW_CURRENT_OFFHAND_ITEM_NAME;
            }
        }
    }

    private void updateEntityPresence() {
        final String defaultItemMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.itemMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String offHandItemMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.itemMessages, CURRENT_OFFHAND_ITEM_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultItemMSG);
        final String offHandSelector = !isEmpty(CURRENT_OFFHAND_ITEM) || !StringHandler.isNullOrEmpty(CURRENT_OFFHAND_ITEM_NAME) ? CURRENT_OFFHAND_ITEM_NAME : "";
        final String mainHandSelector = !isEmpty(CURRENT_MAINHAND_ITEM) || !StringHandler.isNullOrEmpty(CURRENT_MAINHAND_ITEM_NAME) ? CURRENT_MAINHAND_ITEM_NAME : offHandSelector;
        final String mainItemMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.itemMessages, CURRENT_MAINHAND_ITEM_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultItemMSG);
        final String formattedOffHandItemMSG = offHandItemMSG.replace("&main&", offHandSelector).replace("&offhand&", "");

        if (CraftPresence.CONFIG.showCurrentDimension && (isEmpty(CURRENT_MAINHAND_ITEM) && isEmpty(CURRENT_OFFHAND_ITEM))) {
            CraftPresence.CLIENT.LARGEIMAGETEXT = CraftPresence.CLIENT.DETAILS;
        } else {
            CraftPresence.CLIENT.LARGEIMAGETEXT = mainItemMSG.replace("&main&", mainHandSelector).replace("&offhand&", formattedOffHandItemMSG);
        }
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
    }

    public void getEntities() {
        for (Block block : Block.REGISTRY) {
            if (block != null) {
                NonNullList<ItemStack> subtypes = NonNullList.create();
                for (CreativeTabs tab : CreativeTabs.CREATIVE_TAB_ARRAY) {
                    if (tab != null) {
                        block.getSubBlocks(tab, subtypes);
                    }
                }

                if (!BLOCK_NAMES.contains(block.getLocalizedName())) {
                    BLOCK_NAMES.add(block.getLocalizedName());
                }
                if (!BLOCK_CLASSES.contains(block.getClass().getName())) {
                    BLOCK_CLASSES.add(block.getClass().getName());
                }

                if (!subtypes.isEmpty()) {
                    for (ItemStack itemStack : subtypes) {
                        if (!BLOCK_NAMES.contains(itemStack.getDisplayName())) {
                            BLOCK_NAMES.add(itemStack.getDisplayName());
                        }
                        if (!BLOCK_CLASSES.contains(itemStack.getItem().getClass().getName())) {
                            BLOCK_CLASSES.add(itemStack.getItem().getClass().getName());
                        }
                    }
                }
            }
        }

        for (Item item : Item.REGISTRY) {
            if (item != null) {
                NonNullList<ItemStack> subtypes = NonNullList.create();
                for (CreativeTabs tab : CreativeTabs.CREATIVE_TAB_ARRAY) {
                    if (tab != null) {
                        item.getSubItems(tab, subtypes);
                    }
                }

                if (!ITEM_NAMES.contains(item.getItemStackDisplayName(item.getDefaultInstance()))) {
                    ITEM_NAMES.add(item.getItemStackDisplayName(item.getDefaultInstance()));
                }
                if (!ITEM_CLASSES.contains(item.getClass().getName())) {
                    ITEM_CLASSES.add(item.getClass().getName());
                }

                if (!subtypes.isEmpty()) {
                    for (ItemStack itemStack : subtypes) {
                        if (!ITEM_NAMES.contains(itemStack.getDisplayName())) {
                            ITEM_NAMES.add(itemStack.getDisplayName());
                        }
                        if (!ITEM_CLASSES.contains(itemStack.getItem().getClass().getName())) {
                            ITEM_CLASSES.add(itemStack.getItem().getClass().getName());
                        }
                    }
                }
            }
        }

        for (String itemMessage : CraftPresence.CONFIG.itemMessages) {
            if (!StringHandler.isNullOrEmpty(itemMessage)) {
                final String[] part = itemMessage.split(CraftPresence.CONFIG.splitCharacter);
                if (!StringHandler.isNullOrEmpty(part[0])) {
                    if (!ITEM_NAMES.contains(part[0])) {
                        ITEM_NAMES.add(part[0]);
                    }
                    if (!BLOCK_NAMES.contains(part[0])) {
                        BLOCK_NAMES.add(part[0]);
                    }
                }
            }
        }

        verifyEntities();
    }

    private void verifyEntities() {
        List<String> removingBlocks = new ArrayList<>();
        List<String> removingItems = new ArrayList<>();
        for (String itemName : ITEM_NAMES) {
            if (itemName.contains("tile.") || itemName.contains("item.") || itemName.contains(".") || itemName.contains(".name")) {
                removingItems.add(itemName);
            }
        }

        for (String blockName : BLOCK_NAMES) {
            if (blockName.contains("tile.") || blockName.contains("item.") || blockName.contains(".") || blockName.contains(".name")) {
                removingBlocks.add(blockName);
            }
        }

        Constants.LOG.error(" " + removingBlocks + " " + removingItems);
        ITEM_NAMES.removeAll(removingItems);
        BLOCK_NAMES.removeAll(removingBlocks);

        ENTITY_NAMES.addAll(BLOCK_NAMES);
        ENTITY_NAMES.addAll(ITEM_NAMES);
        ENTITY_CLASSES.addAll(BLOCK_CLASSES);
        ENTITY_CLASSES.addAll(ITEM_CLASSES);
    }
}
