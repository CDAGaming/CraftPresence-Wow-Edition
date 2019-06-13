package com.gitlab.cdagaming.craftpresence.handler.entity;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class EntityHandler {
    public boolean isInUse = false, allItemsEmpty = false, enabled = false;

    public List<String> ENTITY_NAMES = Lists.newArrayList();
    private List<String> BLOCK_NAMES = Lists.newArrayList();
    private List<String> BLOCK_CLASSES = Lists.newArrayList();
    private List<String> ITEM_NAMES = Lists.newArrayList();
    private List<String> ITEM_CLASSES = Lists.newArrayList();
    private List<String> ENTITY_CLASSES = Lists.newArrayList();

    private ItemStack EMPTY = new ItemStack((Item) null);
    private ItemStack CURRENT_MAINHAND_ITEM;
    private ItemStack CURRENT_OFFHAND_ITEM;
    private ItemStack CURRENT_HELMET;
    private ItemStack CURRENT_CHEST;
    private ItemStack CURRENT_LEGS;
    private ItemStack CURRENT_BOOTS;

    private String CURRENT_MAINHAND_ITEM_NAME;
    private String CURRENT_OFFHAND_ITEM_NAME;
    private String CURRENT_HELMET_NAME;
    private String CURRENT_CHEST_NAME;
    private String CURRENT_LEGS_NAME;
    private String CURRENT_BOOTS_NAME;

    private boolean queuedForUpdate = false;

    private void emptyData() {
        BLOCK_NAMES.clear();
        BLOCK_CLASSES.clear();
        ITEM_NAMES.clear();
        ITEM_CLASSES.clear();
        ENTITY_NAMES.clear();
        ENTITY_CLASSES.clear();
        clearClientData();
    }

    public void clearClientData() {
        CURRENT_MAINHAND_ITEM = EMPTY;
        CURRENT_OFFHAND_ITEM = EMPTY;
        CURRENT_MAINHAND_ITEM_NAME = null;
        CURRENT_OFFHAND_ITEM_NAME = null;

        CURRENT_HELMET = EMPTY;
        CURRENT_CHEST = EMPTY;
        CURRENT_LEGS = EMPTY;
        CURRENT_BOOTS = EMPTY;
        CURRENT_HELMET_NAME = null;
        CURRENT_CHEST_NAME = null;
        CURRENT_LEGS_NAME = null;
        CURRENT_BOOTS_NAME = null;

        queuedForUpdate = false;
        allItemsEmpty = true;
        isInUse = false;
    }

    public void onTick() {
        enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.CONFIG.enablePERItem : enabled;
        isInUse = enabled && CraftPresence.player != null;
        final boolean needsUpdate = enabled && (ENTITY_NAMES.isEmpty() || ENTITY_CLASSES.isEmpty());

        if (needsUpdate) {
            getEntities();
        }

        if (isInUse) {
            updateEntityData();
            if (enabled && (CraftPresence.player == null || allItemsEmpty)) {
                clearClientData();
            } else if (!enabled) {
                emptyData();
            }
        }
    }

    private boolean isEmpty(final Item item) {
        return item == null || isEmpty(getDefaultInstance(item));
    }

    private boolean isEmpty(final Block block) {
        return block == null || isEmpty(Item.getItemFromBlock(block));
    }

    private ItemStack getDefaultInstance(final Item itemIn) {
        return new ItemStack(itemIn);
    }

    private boolean isEmpty(final ItemStack itemStack) {
        if (itemStack == null || itemStack == EMPTY) {
            return true;
        } else if (itemStack.getItem() != null && itemStack.getItem() != Items.AIR) {
            if (itemStack.getCount() <= 0) {
                return true;
            } else {
                return itemStack.getItemDamage() < -32768 || itemStack.getItemDamage() > 65535;
            }
        } else {
            return true;
        }
    }

    private void updateEntityData() {
        final ItemStack NEW_CURRENT_MAINHAND_ITEM = CraftPresence.player.getHeldItemMainhand();
        final ItemStack NEW_CURRENT_OFFHAND_ITEM = CraftPresence.player.getHeldItemOffhand();
        final ItemStack NEW_CURRENT_HELMET = CraftPresence.player.inventory.armorInventory.get(3);
        final ItemStack NEW_CURRENT_CHEST = CraftPresence.player.inventory.armorInventory.get(2);
        final ItemStack NEW_CURRENT_LEGS = CraftPresence.player.inventory.armorInventory.get(1);
        final ItemStack NEW_CURRENT_BOOTS = CraftPresence.player.inventory.armorInventory.get(0);

        final String NEW_CURRENT_MAINHAND_ITEM_NAME = !isEmpty(NEW_CURRENT_MAINHAND_ITEM) ?
                StringHandler.stripColors(NEW_CURRENT_MAINHAND_ITEM.getDisplayName()) : "";
        final String NEW_CURRENT_OFFHAND_ITEM_NAME = !isEmpty(NEW_CURRENT_OFFHAND_ITEM) ?
                StringHandler.stripColors(NEW_CURRENT_OFFHAND_ITEM.getDisplayName()) : "";
        final String NEW_CURRENT_HELMET_NAME = !isEmpty(NEW_CURRENT_HELMET) ?
                StringHandler.stripColors(NEW_CURRENT_HELMET.getDisplayName()) : "";
        final String NEW_CURRENT_CHEST_NAME = !isEmpty(NEW_CURRENT_CHEST) ?
                StringHandler.stripColors(NEW_CURRENT_CHEST.getDisplayName()) : "";
        final String NEW_CURRENT_LEGS_NAME = !isEmpty(NEW_CURRENT_LEGS) ?
                StringHandler.stripColors(NEW_CURRENT_LEGS.getDisplayName()) : "";
        final String NEW_CURRENT_BOOTS_NAME = !isEmpty(NEW_CURRENT_BOOTS) ?
                StringHandler.stripColors(NEW_CURRENT_BOOTS.getDisplayName()) : "";

        final boolean hasMainHandChanged = (!isEmpty(NEW_CURRENT_MAINHAND_ITEM) && !NEW_CURRENT_MAINHAND_ITEM.equals(CURRENT_MAINHAND_ITEM) || !NEW_CURRENT_MAINHAND_ITEM_NAME.equals(CURRENT_MAINHAND_ITEM_NAME)) || (isEmpty(NEW_CURRENT_MAINHAND_ITEM) && !isEmpty(CURRENT_MAINHAND_ITEM));
        final boolean hasOffHandChanged = (!isEmpty(NEW_CURRENT_OFFHAND_ITEM) && !NEW_CURRENT_OFFHAND_ITEM.equals(CURRENT_OFFHAND_ITEM) || !NEW_CURRENT_OFFHAND_ITEM_NAME.equals(CURRENT_OFFHAND_ITEM_NAME)) || (isEmpty(NEW_CURRENT_OFFHAND_ITEM) && !isEmpty(CURRENT_OFFHAND_ITEM));
        final boolean hasHelmetChanged = (!isEmpty(NEW_CURRENT_HELMET) && !NEW_CURRENT_HELMET.equals(CURRENT_HELMET) || !NEW_CURRENT_HELMET_NAME.equals(CURRENT_HELMET_NAME)) || (isEmpty(NEW_CURRENT_HELMET) && !isEmpty(CURRENT_HELMET));
        final boolean hasChestChanged = (!isEmpty(NEW_CURRENT_CHEST) && !NEW_CURRENT_CHEST.equals(CURRENT_CHEST) || !NEW_CURRENT_CHEST_NAME.equals(CURRENT_CHEST_NAME)) || (isEmpty(NEW_CURRENT_CHEST) && !isEmpty(CURRENT_CHEST));
        final boolean hasLegsChanged = (!isEmpty(NEW_CURRENT_LEGS) && !NEW_CURRENT_LEGS.equals(CURRENT_LEGS) || !NEW_CURRENT_LEGS_NAME.equals(CURRENT_LEGS_NAME)) || (isEmpty(NEW_CURRENT_LEGS) && !isEmpty(CURRENT_LEGS));
        final boolean hasBootsChanged = (!isEmpty(NEW_CURRENT_BOOTS) && !NEW_CURRENT_BOOTS.equals(CURRENT_BOOTS) || !NEW_CURRENT_BOOTS_NAME.equals(CURRENT_BOOTS_NAME)) || (isEmpty(NEW_CURRENT_BOOTS) && !isEmpty(CURRENT_BOOTS));

        if (hasMainHandChanged || hasOffHandChanged || hasHelmetChanged || hasChestChanged || hasLegsChanged || hasBootsChanged) {
            CURRENT_MAINHAND_ITEM = NEW_CURRENT_MAINHAND_ITEM;
            CURRENT_OFFHAND_ITEM = NEW_CURRENT_OFFHAND_ITEM;
            CURRENT_HELMET = NEW_CURRENT_HELMET;
            CURRENT_CHEST = NEW_CURRENT_CHEST;
            CURRENT_LEGS = NEW_CURRENT_LEGS;
            CURRENT_BOOTS = NEW_CURRENT_BOOTS;

            CURRENT_MAINHAND_ITEM_NAME = NEW_CURRENT_MAINHAND_ITEM_NAME;
            CURRENT_OFFHAND_ITEM_NAME = NEW_CURRENT_OFFHAND_ITEM_NAME;
            CURRENT_HELMET_NAME = NEW_CURRENT_HELMET_NAME;
            CURRENT_CHEST_NAME = NEW_CURRENT_CHEST_NAME;
            CURRENT_LEGS_NAME = NEW_CURRENT_LEGS_NAME;
            CURRENT_BOOTS_NAME = NEW_CURRENT_BOOTS_NAME;

            allItemsEmpty = isEmpty(CURRENT_MAINHAND_ITEM) && isEmpty(CURRENT_OFFHAND_ITEM) && isEmpty(CURRENT_HELMET) && isEmpty(CURRENT_CHEST) && isEmpty(CURRENT_LEGS) && isEmpty(CURRENT_BOOTS);
            queuedForUpdate = true;
        }

        if (queuedForUpdate) {
            updateEntityPresence();
        }
    }

    public void updateEntityPresence() {
        final String defaultItemMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.itemMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);

        final String offHandItemMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.itemMessages, CURRENT_OFFHAND_ITEM_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, CURRENT_OFFHAND_ITEM_NAME);
        final String offHandSelector = !isEmpty(CURRENT_OFFHAND_ITEM) && !StringHandler.isNullOrEmpty(CURRENT_OFFHAND_ITEM_NAME) ? CURRENT_OFFHAND_ITEM_NAME : "";
        final String formattedOffHandItemMSG = offHandItemMSG.replace("&main&", offHandSelector).replace("&offhand&", "").replace("&helmet&", "").replace("&chest&", "").replace("&legs&", "").replace("&boots&", "");

        final String mainHandSelector = !isEmpty(CURRENT_MAINHAND_ITEM) && !StringHandler.isNullOrEmpty(CURRENT_MAINHAND_ITEM_NAME) ? CURRENT_MAINHAND_ITEM_NAME : offHandSelector;
        final String mainItemMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.itemMessages, CURRENT_MAINHAND_ITEM_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultItemMSG);

        final String helmetSelector = !isEmpty(CURRENT_HELMET) && !StringHandler.isNullOrEmpty(CURRENT_HELMET_NAME) ? CURRENT_HELMET_NAME : "";
        final String helmetMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.itemMessages, CURRENT_HELMET_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, CURRENT_HELMET_NAME);
        final String formattedHelmetMSG = helmetMSG.replace("&main&", helmetSelector).replace("&offhand&", "").replace("&helmet&", "").replace("&chest&", "").replace("&legs&", "").replace("&boots&", "");

        final String chestSelector = !isEmpty(CURRENT_CHEST) && !StringHandler.isNullOrEmpty(CURRENT_CHEST_NAME) ? CURRENT_CHEST_NAME : "";
        final String chestMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.itemMessages, CURRENT_CHEST_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, CURRENT_CHEST_NAME);
        final String formattedChestMSG = chestMSG.replace("&main&", chestSelector).replace("&offhand&", "").replace("&helmet&", "").replace("&chest&", "").replace("&legs&", "").replace("&boots&", "");

        final String legsSelector = !isEmpty(CURRENT_LEGS) && !StringHandler.isNullOrEmpty(CURRENT_LEGS_NAME) ? CURRENT_LEGS_NAME : "";
        final String legsMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.itemMessages, CURRENT_LEGS_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, CURRENT_LEGS_NAME);
        final String formattedLegsMSG = legsMSG.replace("&main&", legsSelector).replace("&offhand&", "").replace("&helmet&", "").replace("&chest&", "").replace("&legs&", "").replace("&boots&", "");

        final String bootsSelector = !isEmpty(CURRENT_BOOTS) && !StringHandler.isNullOrEmpty(CURRENT_BOOTS_NAME) ? CURRENT_BOOTS_NAME : "";
        final String bootsMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.itemMessages, CURRENT_BOOTS_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, CURRENT_BOOTS_NAME);
        final String formattedBootsMSG = bootsMSG.replace("&main&", bootsSelector).replace("&offhand&", "").replace("&helmet&", "").replace("&chest&", "").replace("&legs&", "").replace("&boots&", "");

        // NOTE: Overrides Dimensions unless All Items are Empty
        if (!allItemsEmpty) {
            CraftPresence.CLIENT.LARGEIMAGETEXT = mainItemMSG.replace("&main&", mainHandSelector).replace("&offhand&", formattedOffHandItemMSG).replace("&helmet&", formattedHelmetMSG).replace("&chest&", formattedChestMSG).replace("&legs&", formattedLegsMSG).replace("&boots&", formattedBootsMSG);
        } else if (CraftPresence.DIMENSIONS.isInUse) {
            CraftPresence.CLIENT.LARGEIMAGETEXT = CraftPresence.CLIENT.DETAILS;
        }
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
        queuedForUpdate = false;
    }

    public void getEntities() {
        for (Block block : Block.REGISTRY) {
            if (!isEmpty(block)) {
                NonNullList<ItemStack> subtypes = NonNullList.create();
                for (CreativeTabs tab : CreativeTabs.CREATIVE_TAB_ARRAY) {
                    if (tab != null) {
                        block.getSubBlocks(tab, subtypes);
                    }
                }

                if (!subtypes.isEmpty()) {
                    for (ItemStack itemStack : subtypes) {
                        if (!isEmpty(itemStack)) {
                            if (!BLOCK_NAMES.contains(itemStack.getDisplayName())) {
                                BLOCK_NAMES.add(itemStack.getDisplayName());
                            }
                            if (!BLOCK_CLASSES.contains(itemStack.getItem().getClass().getName())) {
                                BLOCK_CLASSES.add(itemStack.getItem().getClass().getName());
                            }
                        }
                    }
                } else {
                    if (!BLOCK_NAMES.contains(block.getLocalizedName())) {
                        BLOCK_NAMES.add(block.getLocalizedName());
                    }
                    if (!BLOCK_CLASSES.contains(block.getClass().getName())) {
                        BLOCK_CLASSES.add(block.getClass().getName());
                    }
                }
            }
        }

        for (Item item : Item.REGISTRY) {
            if (!isEmpty(item)) {
                NonNullList<ItemStack> subtypes = NonNullList.create();
                for (CreativeTabs tab : CreativeTabs.CREATIVE_TAB_ARRAY) {
                    if (tab != null) {
                        item.getSubItems(tab, subtypes);
                    }
                }

                if (!subtypes.isEmpty()) {
                    for (ItemStack itemStack : subtypes) {
                        if (!isEmpty(itemStack)) {
                            if (!ITEM_NAMES.contains(itemStack.getDisplayName())) {
                                ITEM_NAMES.add(itemStack.getDisplayName());
                            }
                            if (!ITEM_CLASSES.contains(itemStack.getItem().getClass().getName())) {
                                ITEM_CLASSES.add(itemStack.getItem().getClass().getName());
                            }
                        }
                    }
                } else {
                    if (!ITEM_NAMES.contains(item.getItemStackDisplayName(getDefaultInstance(item)))) {
                        ITEM_NAMES.add(item.getItemStackDisplayName(getDefaultInstance(item)));
                    }
                    if (!ITEM_CLASSES.contains(item.getClass().getName())) {
                        ITEM_CLASSES.add(item.getClass().getName());
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
        List<String> removingBlocks = Lists.newArrayList();
        List<String> removingItems = Lists.newArrayList();
        for (String itemName : ITEM_NAMES) {
            if (!StringHandler.isNullOrEmpty(itemName)) {
                final String lowerItemName = itemName.toLowerCase();
                if (lowerItemName.contains("tile.") || lowerItemName.contains("item.") || lowerItemName.contains(".") || lowerItemName.contains(".name")) {
                    removingItems.add(itemName);
                }
            }
        }

        for (String blockName : BLOCK_NAMES) {
            if (!StringHandler.isNullOrEmpty(blockName)) {
                final String lowerBlockName = blockName.toLowerCase();
                if (lowerBlockName.contains("tile.") || lowerBlockName.contains("item.") || lowerBlockName.contains(".") || lowerBlockName.contains(".name")) {
                    removingBlocks.add(blockName);
                }
            }
        }

        ITEM_NAMES.removeAll(removingItems);
        ITEM_NAMES.removeAll(BLOCK_NAMES);
        BLOCK_NAMES.removeAll(ITEM_NAMES);
        BLOCK_NAMES.removeAll(removingBlocks);

        ENTITY_NAMES.addAll(BLOCK_NAMES);
        ENTITY_NAMES.addAll(ITEM_NAMES);

        ENTITY_CLASSES.addAll(BLOCK_CLASSES);
        ENTITY_CLASSES.addAll(ITEM_CLASSES);
    }
}
