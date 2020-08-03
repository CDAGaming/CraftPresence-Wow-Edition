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
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.google.common.collect.Lists;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Entity Utilities used to Parse Entity Data and handle related RPC Events
 *
 * @author CDAGaming
 */
public class EntityUtils {
    /**
     * A List of the detected Entity Class Names
     */
    private final List<String> ENTITY_CLASSES = Lists.newArrayList();
    /**
     * Whether this module is active and currently in use
     */
    public boolean isInUse = false;
    /**
     * Whether this module is allowed to start and enabled
     */
    public boolean enabled = false;
    /**
     * The Player's Currently Targeted Entity Name, if any
     */
    public String CURRENT_TARGET_NAME;
    /**
     * The Player's Currently Attacking Entity Name, if any
     */
    public String CURRENT_ATTACKING_NAME;
    /**
     * The Player's Currently Riding Entity Name, if any
     */
    public String CURRENT_RIDING_NAME;
    /**
     * A List of the detected Entity Names
     */
    public List<String> ENTITY_NAMES = Lists.newArrayList();
    /**
     * The Player's Currently Targeted Entity's Nbt Tags, if any
     */
    public List<String> CURRENT_TARGET_TAGS = Lists.newArrayList();
    /**
     * The Player's Currently Attacking Entity's Nbt Tags, if any
     */
    public List<String> CURRENT_ATTACKING_TAGS = Lists.newArrayList();
    /**
     * The Player's Currently Riding Entity's Nbt Tags, if any
     */
    public List<String> CURRENT_RIDING_TAGS = Lists.newArrayList();

    /**
     * The Player's Current Target Entity, if any
     */
    private Entity CURRENT_TARGET;

    /**
     * The Player's Currently Attacking Entity, if any
     */
    private Entity CURRENT_ATTACKING;

    /**
     * The Player's Current Riding Entity, if any
     */
    private Entity CURRENT_RIDING;

    /**
     * The Player's Current Targeted Entity's Tag, if any
     */
    private NBTTagCompound CURRENT_TARGET_TAG;

    /**
     * The Player's Current Attacking Entity's Tag, if any
     */
    private NBTTagCompound CURRENT_ATTACKING_TAG;

    /**
     * The Player's Current Riding Entity's Tag, if any
     */
    private NBTTagCompound CURRENT_RIDING_TAG;

    /**
     * If the Player doesn't have an Entity in Critical Slots such as Targeted or Riding
     */
    private boolean allEntitiesEmpty = false;

    /**
     * If this Module's Runtime Data is currently Cleared
     */
    private boolean currentlyCleared = true;

    /**
     * Clears FULL Data from this Module
     */
    private void emptyData() {
        ENTITY_NAMES.clear();
        ENTITY_CLASSES.clear();
        clearClientData();
    }

    /**
     * Clears Runtime Client Data from this Module (PARTIAL Clear)
     */
    public void clearClientData() {
        CURRENT_TARGET = null;
        CURRENT_ATTACKING = null;
        CURRENT_RIDING = null;
        CURRENT_TARGET_NAME = null;
        CURRENT_ATTACKING_NAME = null;
        CURRENT_RIDING_NAME = null;
        CURRENT_TARGET_TAG = null;
        CURRENT_ATTACKING_TAG = null;
        CURRENT_RIDING_TAG = null;

        CURRENT_TARGET_TAGS.clear();
        CURRENT_ATTACKING_TAGS.clear();
        CURRENT_RIDING_TAGS.clear();

        allEntitiesEmpty = true;
        isInUse = false;
        currentlyCleared = true;
    }

    /**
     * Module Event to Occur on each tick within the Application
     */
    public void onTick() {
        enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.CONFIG.enablePEREntity : enabled;
        final boolean needsUpdate = enabled && (ENTITY_NAMES.isEmpty() || ENTITY_CLASSES.isEmpty());

        if (needsUpdate) {
            getEntities();
        }

        if (enabled) {
            if (CraftPresence.player != null) {
                isInUse = true;
                updateEntityData();
            } else if (isInUse) {
                clearClientData();
            }
        } else {
            emptyData();
        }
    }

    /**
     * Synchronizes Data related to this module, if needed
     */
    private void updateEntityData() {
        final Entity NEW_CURRENT_TARGET = CraftPresence.instance.objectMouseOver.entityHit;
        final Entity NEW_CURRENT_ATTACKING = CraftPresence.player.getAttackingEntity();
        final Entity NEW_CURRENT_RIDING = CraftPresence.player.getRidingEntity();

        String NEW_CURRENT_TARGET_NAME, NEW_CURRENT_ATTACKING_NAME, NEW_CURRENT_RIDING_NAME;

        // Note: Unlike getEntities, this does NOT require Server Module to be enabled
        // Users are still free to manually add UUID's as they please for this module
        if (NEW_CURRENT_TARGET instanceof EntityPlayer) {
            final EntityPlayer NEW_CURRENT_PLAYER_TARGET = (EntityPlayer) NEW_CURRENT_TARGET;
            NEW_CURRENT_TARGET_NAME = StringUtils.stripColors(NEW_CURRENT_PLAYER_TARGET.getGameProfile().getId().toString());
        } else {
            NEW_CURRENT_TARGET_NAME = NEW_CURRENT_TARGET != null ?
                    StringUtils.stripColors(NEW_CURRENT_TARGET.getDisplayName().getFormattedText()) : "";
        }

        if (NEW_CURRENT_ATTACKING instanceof EntityPlayer) {
            final EntityPlayer NEW_CURRENT_PLAYER_ATTACKING = (EntityPlayer) NEW_CURRENT_ATTACKING;
            NEW_CURRENT_ATTACKING_NAME = StringUtils.stripColors(NEW_CURRENT_PLAYER_ATTACKING.getGameProfile().getId().toString());
        } else {
            NEW_CURRENT_ATTACKING_NAME = NEW_CURRENT_ATTACKING != null ?
                    StringUtils.stripColors(NEW_CURRENT_ATTACKING.getDisplayName().getFormattedText()) : "";
        }

        if (NEW_CURRENT_RIDING instanceof EntityPlayer) {
            final EntityPlayer NEW_CURRENT_PLAYER_RIDING = (EntityPlayer) NEW_CURRENT_RIDING;
            NEW_CURRENT_RIDING_NAME = StringUtils.stripColors(NEW_CURRENT_PLAYER_RIDING.getGameProfile().getId().toString());
        } else {
            NEW_CURRENT_RIDING_NAME = NEW_CURRENT_RIDING != null ?
                    StringUtils.stripColors(NEW_CURRENT_RIDING.getDisplayName().getFormattedText()) : "";
        }

        final boolean hasTargetChanged = (NEW_CURRENT_TARGET != null &&
                !NEW_CURRENT_TARGET.equals(CURRENT_TARGET) || !NEW_CURRENT_TARGET_NAME.equals(CURRENT_TARGET_NAME)) ||
                (NEW_CURRENT_TARGET == null && CURRENT_TARGET != null);
        final boolean hasAttackingChanged = (NEW_CURRENT_ATTACKING != null &&
                !NEW_CURRENT_ATTACKING.equals(CURRENT_ATTACKING) || !NEW_CURRENT_ATTACKING_NAME.equals(CURRENT_ATTACKING_NAME)) ||
                (NEW_CURRENT_ATTACKING == null && CURRENT_ATTACKING != null);
        final boolean hasRidingChanged = (NEW_CURRENT_RIDING != null &&
                !NEW_CURRENT_RIDING.equals(CURRENT_RIDING) || !NEW_CURRENT_RIDING_NAME.equals(CURRENT_RIDING_NAME)) ||
                (NEW_CURRENT_RIDING == null && CURRENT_RIDING != null);

        if (hasTargetChanged) {
            CURRENT_TARGET = NEW_CURRENT_TARGET;
            CURRENT_TARGET_TAG = CURRENT_TARGET != null ? CURRENT_TARGET.writeToNBT(new NBTTagCompound()) : null;
            final List<String> NEW_CURRENT_TARGET_TAGS = CURRENT_TARGET_TAG != null ? Lists.newArrayList(CURRENT_TARGET_TAG.getKeySet()) : Lists.newArrayList();

            if (!NEW_CURRENT_TARGET_TAGS.equals(CURRENT_TARGET_TAGS)) {
                CURRENT_TARGET_TAGS = NEW_CURRENT_TARGET_TAGS;
            }
            CURRENT_TARGET_NAME = NEW_CURRENT_TARGET_NAME;
        }

        if (hasAttackingChanged) {
            CURRENT_ATTACKING = NEW_CURRENT_ATTACKING;
            CURRENT_ATTACKING_TAG = CURRENT_ATTACKING != null ? CURRENT_ATTACKING.writeToNBT(new NBTTagCompound()) : null;
            final List<String> NEW_CURRENT_ATTACKING_TAGS = CURRENT_ATTACKING_TAG != null ? Lists.newArrayList(CURRENT_ATTACKING_TAG.getKeySet()) : Lists.newArrayList();

            if (!NEW_CURRENT_ATTACKING_TAGS.equals(CURRENT_ATTACKING_TAGS)) {
                CURRENT_ATTACKING_TAGS = NEW_CURRENT_ATTACKING_TAGS;
            }
            CURRENT_ATTACKING_NAME = NEW_CURRENT_ATTACKING_NAME;
        }

        if (hasRidingChanged) {
            CURRENT_RIDING = NEW_CURRENT_RIDING;
            CURRENT_RIDING_TAG = CURRENT_RIDING != null ? CURRENT_RIDING.writeToNBT(new NBTTagCompound()) : null;
            final List<String> NEW_CURRENT_RIDING_TAGS = CURRENT_RIDING_TAG != null ? Lists.newArrayList(CURRENT_RIDING_TAG.getKeySet()) : Lists.newArrayList();

            if (!NEW_CURRENT_RIDING_TAGS.equals(CURRENT_RIDING_TAGS)) {
                CURRENT_RIDING_TAGS = NEW_CURRENT_RIDING_TAGS;
            }
            CURRENT_RIDING_NAME = NEW_CURRENT_RIDING_NAME;
        }

        if (hasTargetChanged || hasAttackingChanged || hasRidingChanged) {
            allEntitiesEmpty = CURRENT_TARGET == null && CURRENT_ATTACKING == null && CURRENT_RIDING == null;
            updateEntityPresence();
        }
    }

    /**
     * Updates RPC Data related to this Module
     */
    public void updateEntityPresence() {
        // Retrieve Messages
        final String defaultEntityTargetMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.entityTargetMessages,
                "default", 0, 1, CraftPresence.CONFIG.splitCharacter,
                null);
        final String defaultEntityAttackingMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.entityAttackingMessages,
                "default", 0, 1, CraftPresence.CONFIG.splitCharacter,
                null);
        final String defaultEntityRidingMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.entityRidingMessages,
                "default", 0, 1, CraftPresence.CONFIG.splitCharacter,
                null);

        final String targetEntityMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.entityTargetMessages,
                CURRENT_TARGET_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter,
                defaultEntityTargetMSG);
        final String attackingEntityMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.entityAttackingMessages,
                CURRENT_ATTACKING_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter,
                defaultEntityAttackingMSG);
        final String ridingEntityMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.entityRidingMessages,
                CURRENT_RIDING_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter,
                defaultEntityRidingMSG);

        // Form Entity Argument List
        final List<Tuple<String, String>> entityTargetArgs = Lists.newArrayList(), entityAttackingArgs = Lists.newArrayList(), entityRidingArgs = Lists.newArrayList();

        entityTargetArgs.add(new Tuple<>("&entity&", CURRENT_TARGET_NAME));
        entityAttackingArgs.add(new Tuple<>("&entity&", CURRENT_ATTACKING_NAME));
        entityRidingArgs.add(new Tuple<>("&entity&", CURRENT_RIDING_NAME));

        // Extend Arguments, if tags available
        if (!CURRENT_TARGET_TAGS.isEmpty()) {
            for (String tagName : CURRENT_TARGET_TAGS) {
                entityTargetArgs.add(new Tuple<>("&" + tagName + "&", CURRENT_TARGET_TAG.getTag(tagName).toString()));
            }
        }

        if (!CURRENT_ATTACKING_TAGS.isEmpty()) {
            for (String tagName : CURRENT_ATTACKING_TAGS) {
                entityAttackingArgs.add(new Tuple<>("&" + tagName + "&", CURRENT_ATTACKING_TAG.getTag(tagName).toString()));
            }
        }

        if (!CURRENT_RIDING_TAGS.isEmpty()) {
            for (String tagName : CURRENT_RIDING_TAGS) {
                entityRidingArgs.add(new Tuple<>("&" + tagName + "&", CURRENT_RIDING_TAG.getTag(tagName).toString()));
            }
        }

        // Add All Generalized Arguments, if any
        if (!CraftPresence.CLIENT.generalArgs.isEmpty()) {
            entityTargetArgs.addAll(CraftPresence.CLIENT.generalArgs);
            entityAttackingArgs.addAll(CraftPresence.CLIENT.generalArgs);
            entityRidingArgs.addAll(CraftPresence.CLIENT.generalArgs);
        }

        final String CURRENT_TARGET_MESSAGE = StringUtils.sequentialReplaceAnyCase(targetEntityMSG, entityTargetArgs);
        final String CURRENT_ATTACKING_MESSAGE = StringUtils.sequentialReplaceAnyCase(attackingEntityMSG, entityAttackingArgs);
        final String CURRENT_RIDING_MESSAGE = StringUtils.sequentialReplaceAnyCase(ridingEntityMSG, entityRidingArgs);

        // NOTE: Only Apply if Entities are not Empty, otherwise Clear Argument
        if (!allEntitiesEmpty) {
            CraftPresence.CLIENT.syncArgument("&TARGETENTITY&", CURRENT_TARGET_MESSAGE, false);
            CraftPresence.CLIENT.syncArgument("&ATTACKINGENTITY&", CURRENT_ATTACKING_MESSAGE, false);
            CraftPresence.CLIENT.syncArgument("&RIDINGENTITY&", CURRENT_RIDING_MESSAGE, false);
        } else if (!currentlyCleared) {
            CraftPresence.CLIENT.initArgument("&TARGETENTITY&", "&ATTACKINGENTITY&", "&RIDINGENTITY&");
        }
    }

    /**
     * Retrieves a List of Tags from a Entity name, if currently equipped
     *
     * @param name The Entity Name
     * @return A List of Tags from a Entity name, if currently equipped
     */
    public List<String> getListFromName(final String name) {
        return name.equalsIgnoreCase(CURRENT_TARGET_NAME) ? CURRENT_TARGET_TAGS
                : name.equalsIgnoreCase(CURRENT_ATTACKING_NAME) ? CURRENT_ATTACKING_TAGS
                : name.equalsIgnoreCase(CURRENT_RIDING_NAME) ? CURRENT_RIDING_TAGS : Lists.newArrayList();
    }

    /**
     * Generates Entity Tag Placeholder String
     *
     * @param name The Entity Name
     * @param tags A List of the tags associated with the Entity
     *
     * @return The Resulting Entity Tag Placeholder String
     */
    public String generatePlaceholderString(final String name, final List<String> tags) {
        final StringBuilder finalString = new StringBuilder();
        if (!tags.isEmpty()) {
            for (String tagName : tags) {
                finalString.append("\n - &").append(tagName).append("&");

                if (ModUtils.IS_DEV) {
                    // If in Debug Mode, also append the Tag's value to the placeholder String
                    final String tagValue =
                            tags.equals(CURRENT_TARGET_TAGS) ? CURRENT_TARGET_TAG.getTag(tagName).toString() :
                                    tags.equals(CURRENT_ATTACKING_TAGS) ? CURRENT_ATTACKING_TAG.getTag(tagName).toString() :
                                            tags.equals(CURRENT_RIDING_TAGS) ? CURRENT_RIDING_TAG.getTag(tagName).toString() : null;

                    if (!StringUtils.isNullOrEmpty(tagValue)) {
                        finalString.append(" (Value -> ").append(tagValue).append(")");
                    }
                }
            }
        }
        return ((!StringUtils.isNullOrEmpty(name) ? name : "None") + " " + (!StringUtils.isNullOrEmpty(finalString.toString()) ? finalString.toString() : "\\n - N/A"));
    }

    /**
     * Retrieves and Synchronizes detected Entities
     */
    public void getEntities() {
        if (!EntityList.getEntityNameList().isEmpty()) {
            for (ResourceLocation entityLocation : EntityList.getEntityNameList()) {
                if (entityLocation != null) {
                    final String entityName = !StringUtils.isNullOrEmpty(EntityList.getTranslationName(entityLocation)) ? EntityList.getTranslationName(entityLocation) : "generic";
                    final Class<?> entityClass = EntityList.getClass(entityLocation);
                    if (entityClass != null) {
                        if (!ENTITY_NAMES.contains(entityName)) {
                            ENTITY_NAMES.add(entityName);
                        }
                        if (!ENTITY_CLASSES.contains(entityClass.getName())) {
                            ENTITY_CLASSES.add(entityClass.getName());
                        }
                    }
                }
            }
        }

        // If Server Data is enabled, allow UUIDs to count as entities
        if (CraftPresence.SERVER.enabled) {
            for (NetworkPlayerInfo playerInfo : CraftPresence.SERVER.currentPlayerList) {
                final String uuidString = playerInfo.getGameProfile().getId().toString();
                if (!StringUtils.isNullOrEmpty(uuidString) && !ENTITY_NAMES.contains(uuidString)) {
                    ENTITY_NAMES.add(uuidString);
                }
            }
        }

        for (String entityTargetMessage : CraftPresence.CONFIG.entityTargetMessages) {
            if (!StringUtils.isNullOrEmpty(entityTargetMessage)) {
                final String[] part = entityTargetMessage.split(CraftPresence.CONFIG.splitCharacter);
                if (!StringUtils.isNullOrEmpty(part[0]) && !ENTITY_NAMES.contains(part[0])) {
                    ENTITY_NAMES.add(part[0]);
                }
            }
        }

        for (String entityAttackingMessage : CraftPresence.CONFIG.entityAttackingMessages) {
            if (!StringUtils.isNullOrEmpty(entityAttackingMessage)) {
                final String[] part = entityAttackingMessage.split(CraftPresence.CONFIG.splitCharacter);
                if (!StringUtils.isNullOrEmpty(part[0]) && !ENTITY_NAMES.contains(part[0])) {
                    ENTITY_NAMES.add(part[0]);
                }
            }
        }

        for (String entityRidingMessage : CraftPresence.CONFIG.entityRidingMessages) {
            if (!StringUtils.isNullOrEmpty(entityRidingMessage)) {
                final String[] part = entityRidingMessage.split(CraftPresence.CONFIG.splitCharacter);
                if (!StringUtils.isNullOrEmpty(part[0]) && !ENTITY_NAMES.contains(part[0])) {
                    ENTITY_NAMES.add(part[0]);
                }
            }
        }

        verifyEntities();
    }

    /**
     * Verifies, Synchronizes and Removes any Invalid Items and Blocks from their Lists
     */
    private void verifyEntities() {
        // Add Verification here as needed
    }
}
