/*
 * MIT License
 *
 * Copyright (c) 2018 - 2020 CDAGaming (cstack2011@yahoo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.gitlab.cdagaming.craftpresence.utils.entity;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

/**
 * Entity Utilities used to Parse Entity Data and handle related RPC Events
 *
 * @author CDAGaming
 */
public class EntityUtils {
    /**
     * A List of the detected Block Names
     */
    private final List<String> BLOCK_NAMES = Lists.newArrayList();
    /**
     * A List of the detected Block Class Names
     */
    private final List<String> BLOCK_CLASSES = Lists.newArrayList();
    /**
     * A List of the detected Item Names
     */
    private final List<String> ITEM_NAMES = Lists.newArrayList();
    /**
     * A List of the detected Item Class Names
     */
    private final List<String> ITEM_CLASSES = Lists.newArrayList();
    /**
     * A List of the detected Entity (Blocks + Items) Class Names
     */
    private final List<String> ENTITY_CLASSES = Lists.newArrayList();
    /**
     * An Instance of an Empty Item
     */
    private final Item EMPTY_ITEM = null;
    /**
     * An Instance of an Empty ItemStack
     */
    private final ItemStack EMPTY_STACK = new ItemStack(EMPTY_ITEM);
    /**
     * Whether this module is active and currently in use
     */
    public boolean isInUse = false;
    /**
     * Whether this module is allowed to start and enabled
     */
    public boolean enabled = false;
    /**
     * A List of the detected Entity (Blocks + Items) Names
     */
    public List<String> ENTITY_NAMES = Lists.newArrayList();
    /**
     * The Player's Current Main Hand Item, if any
     */
    private ItemStack CURRENT_MAINHAND_ITEM;

    /**
     * The Player's Current Off Hand Item, if any
     */
    private ItemStack CURRENT_OFFHAND_ITEM;

    /**
     * The Player's Currently Equipped Helmet, if any
     */
    private ItemStack CURRENT_HELMET;

    /**
     * The Player's Currently Equipped ChestPlate, if any
     */
    private ItemStack CURRENT_CHEST;

    /**
     * The Player's Currently Equipped Leggings, if any
     */
    private ItemStack CURRENT_LEGS;

    /**
     * The Player's Currently Equipped Boots, if any
     */
    private ItemStack CURRENT_BOOTS;

    /**
     * The Player's Current Main Hand Item Name, if any
     */
    private String CURRENT_MAINHAND_ITEM_NAME;

    /**
     * The Player's Current Off Hand Item Name, if any
     */
    private String CURRENT_OFFHAND_ITEM_NAME;

    /**
     * The Player's Currently Equipped Helmet Name, if any
     */
    private String CURRENT_HELMET_NAME;

    /**
     * The Player's Currently Equipped ChestPlate Name, if any
     */
    private String CURRENT_CHEST_NAME;

    /**
     * The Player's Currently Equipped Leggings Name, if any
     */
    private String CURRENT_LEGS_NAME;

    /**
     * The Player's Currently Equipped Boots Name, if any
     */
    private String CURRENT_BOOTS_NAME;

    /**
     * If the Player doesn't have any Items in the Critical Slots such as Main & Off Hand or Armor
     */
    private boolean allItemsEmpty = false;

    /**
     * If this Module's Runtime Data is currently Cleared
     */
    private boolean currentlyCleared = true;

    /**
     * Clears FULL Data from this Module
     */
    private void emptyData() {
        BLOCK_NAMES.clear();
        BLOCK_CLASSES.clear();
        ITEM_NAMES.clear();
        ITEM_CLASSES.clear();
        ENTITY_NAMES.clear();
        ENTITY_CLASSES.clear();
        clearClientData();
    }

    /**
     * Clears Runtime Client Data from this Module (PARTIAL Clear)
     */
    public void clearClientData() {
        CURRENT_MAINHAND_ITEM = EMPTY_STACK;
        CURRENT_OFFHAND_ITEM = EMPTY_STACK;
        CURRENT_MAINHAND_ITEM_NAME = null;
        CURRENT_OFFHAND_ITEM_NAME = null;

        CURRENT_HELMET = EMPTY_STACK;
        CURRENT_CHEST = EMPTY_STACK;
        CURRENT_LEGS = EMPTY_STACK;
        CURRENT_BOOTS = EMPTY_STACK;
        CURRENT_HELMET_NAME = null;
        CURRENT_CHEST_NAME = null;
        CURRENT_LEGS_NAME = null;
        CURRENT_BOOTS_NAME = null;

        allItemsEmpty = true;
        isInUse = false;
        currentlyCleared = true;
    }

    /**
     * Module Event to Occur on each tick within the Application
     */
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

    /**
     * Determines whether the Specified Item classifies as NULL or EMPTY
     *
     * @param item The Item to Evaluate
     * @return {@code true} if the Item classifies as NULL or EMPTY
     */
    private boolean isEmpty(final Item item) {
        return item == null || isEmpty(getDefaultInstance(item));
    }

    /**
     * Determines whether the Specified Block classifies as NULL or EMPTY
     *
     * @param block The Block to evaluate
     * @return {@code true} if the Block classifies as NULL or EMPTY
     */
    private boolean isEmpty(final Block block) {
        return block == null || isEmpty(Item.getItemFromBlock(block));
    }

    /**
     * Returns the Default Variant of the Specified Item
     *
     * @param itemIn The Item to evaluate
     * @return The default variant of the item
     */
    private ItemStack getDefaultInstance(final Item itemIn) {
        return new ItemStack(itemIn);
    }

    /**
     * Determines whether the Specified ItemStack classifies as NULL or EMPTY
     *
     * @param itemStack The ItemStack to evaluate
     * @return {@code true} if the ItemStack classifies as NULL or EMPTY
     */
    private boolean isEmpty(final ItemStack itemStack) {
        if (itemStack == null || itemStack.equals(EMPTY_STACK)) {
            return true;
        } else if (itemStack.getItem() != EMPTY_ITEM && itemStack.getItem() != Items.AIR) {
            if (itemStack.getCount() <= 0) {
                return true;
            } else {
                return itemStack.getItemDamage() < -32768 || itemStack.getItemDamage() > 65535;
            }
        } else {
            return true;
        }
    }

    /**
     * Synchronizes Data related to this module, if needed
     */
    private void updateEntityData() {
        final ItemStack NEW_CURRENT_MAINHAND_ITEM = CraftPresence.player.getHeldItemMainhand();
        final ItemStack NEW_CURRENT_OFFHAND_ITEM = CraftPresence.player.getHeldItemOffhand();
        final ItemStack NEW_CURRENT_HELMET = CraftPresence.player.inventory.armorInventory.get(3);
        final ItemStack NEW_CURRENT_CHEST = CraftPresence.player.inventory.armorInventory.get(2);
        final ItemStack NEW_CURRENT_LEGS = CraftPresence.player.inventory.armorInventory.get(1);
        final ItemStack NEW_CURRENT_BOOTS = CraftPresence.player.inventory.armorInventory.get(0);

        final String NEW_CURRENT_MAINHAND_ITEM_NAME = !isEmpty(NEW_CURRENT_MAINHAND_ITEM) ?
                StringUtils.stripColors(NEW_CURRENT_MAINHAND_ITEM.getDisplayName()) : "";
        final String NEW_CURRENT_OFFHAND_ITEM_NAME = !isEmpty(NEW_CURRENT_OFFHAND_ITEM) ?
                StringUtils.stripColors(NEW_CURRENT_OFFHAND_ITEM.getDisplayName()) : "";
        final String NEW_CURRENT_HELMET_NAME = !isEmpty(NEW_CURRENT_HELMET) ?
                StringUtils.stripColors(NEW_CURRENT_HELMET.getDisplayName()) : "";
        final String NEW_CURRENT_CHEST_NAME = !isEmpty(NEW_CURRENT_CHEST) ?
                StringUtils.stripColors(NEW_CURRENT_CHEST.getDisplayName()) : "";
        final String NEW_CURRENT_LEGS_NAME = !isEmpty(NEW_CURRENT_LEGS) ?
                StringUtils.stripColors(NEW_CURRENT_LEGS.getDisplayName()) : "";
        final String NEW_CURRENT_BOOTS_NAME = !isEmpty(NEW_CURRENT_BOOTS) ?
                StringUtils.stripColors(NEW_CURRENT_BOOTS.getDisplayName()) : "";

        final boolean hasMainHandChanged = (!isEmpty(NEW_CURRENT_MAINHAND_ITEM) &&
                !NEW_CURRENT_MAINHAND_ITEM.equals(CURRENT_MAINHAND_ITEM) || !NEW_CURRENT_MAINHAND_ITEM_NAME.equals(CURRENT_MAINHAND_ITEM_NAME)) ||
                (isEmpty(NEW_CURRENT_MAINHAND_ITEM) && !isEmpty(CURRENT_MAINHAND_ITEM));
        final boolean hasOffHandChanged = (!isEmpty(NEW_CURRENT_OFFHAND_ITEM) &&
                !NEW_CURRENT_OFFHAND_ITEM.equals(CURRENT_OFFHAND_ITEM) || !NEW_CURRENT_OFFHAND_ITEM_NAME.equals(CURRENT_OFFHAND_ITEM_NAME)) ||
                (isEmpty(NEW_CURRENT_OFFHAND_ITEM) && !isEmpty(CURRENT_OFFHAND_ITEM));
        final boolean hasHelmetChanged = (!isEmpty(NEW_CURRENT_HELMET) &&
                !NEW_CURRENT_HELMET.equals(CURRENT_HELMET) || !NEW_CURRENT_HELMET_NAME.equals(CURRENT_HELMET_NAME)) ||
                (isEmpty(NEW_CURRENT_HELMET) && !isEmpty(CURRENT_HELMET));
        final boolean hasChestChanged = (!isEmpty(NEW_CURRENT_CHEST) &&
                !NEW_CURRENT_CHEST.equals(CURRENT_CHEST) || !NEW_CURRENT_CHEST_NAME.equals(CURRENT_CHEST_NAME)) ||
                (isEmpty(NEW_CURRENT_CHEST) && !isEmpty(CURRENT_CHEST));
        final boolean hasLegsChanged = (!isEmpty(NEW_CURRENT_LEGS) &&
                !NEW_CURRENT_LEGS.equals(CURRENT_LEGS) || !NEW_CURRENT_LEGS_NAME.equals(CURRENT_LEGS_NAME)) ||
                (isEmpty(NEW_CURRENT_LEGS) && !isEmpty(CURRENT_LEGS));
        final boolean hasBootsChanged = (!isEmpty(NEW_CURRENT_BOOTS) &&
                !NEW_CURRENT_BOOTS.equals(CURRENT_BOOTS) || !NEW_CURRENT_BOOTS_NAME.equals(CURRENT_BOOTS_NAME)) ||
                (isEmpty(NEW_CURRENT_BOOTS) && !isEmpty(CURRENT_BOOTS));

        if (hasMainHandChanged || hasOffHandChanged ||
                hasHelmetChanged || hasChestChanged ||
                hasLegsChanged || hasBootsChanged) {
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
            updateEntityPresence();
        }
    }

    /**
     * Updates RPC Data related to this Module
     */
    public void updateEntityPresence() {
        // Retrieve Messages
        final String defaultItemMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages,
                "default", 0, 1, CraftPresence.CONFIG.splitCharacter,
                null);

        final String offHandItemMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages,
                CURRENT_OFFHAND_ITEM_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter,
                CURRENT_OFFHAND_ITEM_NAME);
        final String mainItemMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages,
                CURRENT_MAINHAND_ITEM_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter,
                defaultItemMSG);

        final String helmetMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages,
                CURRENT_HELMET_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter,
                CURRENT_HELMET_NAME);
        final String chestMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages,
                CURRENT_CHEST_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter,
                CURRENT_CHEST_NAME);
        final String legsMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages,
                CURRENT_LEGS_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter,
                CURRENT_LEGS_NAME);
        final String bootsMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages,
                CURRENT_BOOTS_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter,
                CURRENT_BOOTS_NAME);

        // Form Entity/Item Argument List
        List<Tuple<String, String>> entityArgs = Lists.newArrayList();

        entityArgs.add(new Tuple<>("&MAIN&", !StringUtils.isNullOrEmpty(CURRENT_MAINHAND_ITEM_NAME) ?
                StringUtils.replaceAnyCase(mainItemMSG, "&item&", CURRENT_MAINHAND_ITEM_NAME) : ""));
        entityArgs.add(new Tuple<>("&OFFHAND&", !StringUtils.isNullOrEmpty(CURRENT_OFFHAND_ITEM_NAME) ?
                StringUtils.replaceAnyCase(offHandItemMSG, "&item&", CURRENT_OFFHAND_ITEM_NAME) : ""));
        entityArgs.add(new Tuple<>("&HELMET&", !StringUtils.isNullOrEmpty(CURRENT_HELMET_NAME) ?
                StringUtils.replaceAnyCase(helmetMSG, "&item&", CURRENT_HELMET_NAME) : ""));
        entityArgs.add(new Tuple<>("&CHEST&", !StringUtils.isNullOrEmpty(CURRENT_CHEST_NAME) ?
                StringUtils.replaceAnyCase(chestMSG, "&item&", CURRENT_CHEST_NAME) : ""));
        entityArgs.add(new Tuple<>("&LEGS&", !StringUtils.isNullOrEmpty(CURRENT_LEGS_NAME) ?
                StringUtils.replaceAnyCase(legsMSG, "&item&", CURRENT_LEGS_NAME) : ""));
        entityArgs.add(new Tuple<>("&BOOTS&", !StringUtils.isNullOrEmpty(CURRENT_BOOTS_NAME) ?
                StringUtils.replaceAnyCase(bootsMSG, "&item&", CURRENT_BOOTS_NAME) : ""));

        // Add All Generalized Arguments, if any
        if (!CraftPresence.CLIENT.generalArgs.isEmpty()) {
            entityArgs.addAll(CraftPresence.CLIENT.generalArgs);
        }

        final String CURRENT_ITEM_MESSAGE = StringUtils.sequentialReplaceAnyCase(defaultItemMSG, entityArgs);

        // NOTE: Only Apply if Items are not Empty, otherwise Clear Argument
        if (!allItemsEmpty) {
            CraftPresence.CLIENT.syncArgument("&ENTITY&", CURRENT_ITEM_MESSAGE, false);
        } else if (!currentlyCleared) {
            CraftPresence.CLIENT.initArgumentData("&ENTITY&");
            CraftPresence.CLIENT.initIconData("&ENTITY&");
        }
    }

    /**
     * Retrieves and Synchronizes detected Entities
     */
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
            if (!StringUtils.isNullOrEmpty(itemMessage)) {
                final String[] part = itemMessage.split(CraftPresence.CONFIG.splitCharacter);
                if (!StringUtils.isNullOrEmpty(part[0])) {
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

    /**
     * Verifies, Synchronizes and Removes any Invalid Items and Blocks from their Lists
     */
    private void verifyEntities() {
        List<String> removingBlocks = Lists.newArrayList();
        List<String> removingItems = Lists.newArrayList();
        for (String itemName : ITEM_NAMES) {
            if (!StringUtils.isNullOrEmpty(itemName)) {
                final String lowerItemName = itemName.toLowerCase();
                if (lowerItemName.contains("tile.") || lowerItemName.contains("item.") || lowerItemName.contains(".") || lowerItemName.contains(".name")) {
                    removingItems.add(itemName);
                }
            }
        }

        for (String blockName : BLOCK_NAMES) {
            if (!StringUtils.isNullOrEmpty(blockName)) {
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
