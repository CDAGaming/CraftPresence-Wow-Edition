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
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAssetUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import net.minecraft.client.gui.GuiScreen;

public class DynamicEditorGui extends ExtendedScreen {
    private final String configOption;
    private final boolean isNewValue, isDefaultValue;
    private ExtendedButtonControl proceedButton;
    private ExtendedTextControl specificMessageInput, newValueName;
    private String attributeName, specificMessage, defaultMessage, mainTitle, removeMessage;

    DynamicEditorGui(GuiScreen parentScreen, String attributeName, String configOption) {
        super(parentScreen);
        this.configOption = configOption;
        this.attributeName = attributeName;
        isNewValue = StringUtils.isNullOrEmpty(attributeName);
        isDefaultValue = !StringUtils.isNullOrEmpty(attributeName) && "default".equals(attributeName);
    }

    @Override
    public void initializeUi() {
        if (isNewValue) {
            mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.editor.add.new");
            if (parentScreen instanceof BiomeSettingsGui) {
                specificMessage = defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.biomeMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
            } else if (parentScreen instanceof DimensionSettingsGui) {
                specificMessage = defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
            } else if (parentScreen instanceof ServerSettingsGui) {
                specificMessage = defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
            } else if (parentScreen instanceof AdvancedSettingsGui) {
                if (configOption.equals(CraftPresence.CONFIG.NAME_guiMessages)) {
                    specificMessage = defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.guiMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                } else if (configOption.equals(CraftPresence.CONFIG.NAME_itemMessages)) {
                    specificMessage = defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                } else if (configOption.equals(CraftPresence.CONFIG.NAME_entityTargetMessages)) {
                    specificMessage = defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityTargetMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                } else if (configOption.equals(CraftPresence.CONFIG.NAME_entityAttackingMessages)) {
                    specificMessage = defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityAttackingMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                } else if (configOption.equals(CraftPresence.CONFIG.NAME_entityRidingMessages)) {
                    specificMessage = defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityRidingMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                }
            }
        } else {
            if (parentScreen instanceof BiomeSettingsGui) {
                mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.biome.edit_specific_biome", attributeName);
                defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.biomeMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                specificMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.biomeMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMessage);
            } else if (parentScreen instanceof DimensionSettingsGui) {
                mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.dimension.edit_specific_dimension", attributeName);
                defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                specificMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMessage);
            } else if (parentScreen instanceof ServerSettingsGui) {
                mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.server.edit_specific_server", attributeName);
                defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                specificMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMessage);
            } else if (parentScreen instanceof AdvancedSettingsGui) {
                if (configOption.equals(CraftPresence.CONFIG.NAME_guiMessages)) {
                    mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.gui.edit_specific_gui", attributeName);
                    defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.guiMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                    specificMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.guiMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMessage);
                } else if (configOption.equals(CraftPresence.CONFIG.NAME_itemMessages)) {
                    mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.item.edit_specific_item", attributeName);
                    defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                    specificMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMessage);
                } else if (configOption.equals(CraftPresence.CONFIG.NAME_entityTargetMessages)) {
                    mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.entity.edit_specific_entity", attributeName);
                    defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityTargetMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                    specificMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityTargetMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMessage);
                } else if (configOption.equals(CraftPresence.CONFIG.NAME_entityAttackingMessages)) {
                    mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.entity.edit_specific_entity", attributeName);
                    defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityAttackingMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                    specificMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityAttackingMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMessage);
                } else if (configOption.equals(CraftPresence.CONFIG.NAME_entityRidingMessages)) {
                    mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.entity.edit_specific_entity", attributeName);
                    defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityRidingMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                    specificMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityRidingMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMessage);
                }
            }
        }

        removeMessage = ModUtils.TRANSLATOR.translate("gui.config.message.remove");

        specificMessageInput = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                )
        );
        specificMessageInput.setText(specificMessage);

        if ((parentScreen instanceof DimensionSettingsGui || parentScreen instanceof ServerSettingsGui) && !isNewValue) {
            // Adding Specific Icon Button
            addControl(
                    new ExtendedButtonControl(
                            (width / 2) - 90, CraftPresence.GUIS.getButtonY(2),
                            180, 20,
                            ModUtils.TRANSLATOR.translate("gui.config.message.button.icon.change"),
                            () -> {
                                if (parentScreen instanceof DimensionSettingsGui) {
                                    final String defaultIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 2, CraftPresence.CONFIG.splitCharacter, CraftPresence.CONFIG.defaultDimensionIcon);
                                    final String specificIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, attributeName, 0, 2, CraftPresence.CONFIG.splitCharacter, defaultIcon);
                                    CraftPresence.GUIS.openScreen(new SelectorGui(currentScreen, CraftPresence.CONFIG.NAME_dimensionMessages, ModUtils.TRANSLATOR.translate("gui.config.title.selector.icon"), DiscordAssetUtils.ICON_LIST, specificIcon, attributeName, true));
                                } else if (parentScreen instanceof ServerSettingsGui) {
                                    final String defaultIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 2, CraftPresence.CONFIG.splitCharacter, CraftPresence.CONFIG.defaultServerIcon);
                                    final String specificIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 2, CraftPresence.CONFIG.splitCharacter, defaultIcon);
                                    CraftPresence.GUIS.openScreen(new SelectorGui(currentScreen, CraftPresence.CONFIG.NAME_serverMessages, ModUtils.TRANSLATOR.translate("gui.config.title.selector.icon"), DiscordAssetUtils.ICON_LIST, specificIcon, attributeName, true));
                                }
                            }
                    )
            );
        }
        if (isNewValue) {
            newValueName = addControl(
                    new ExtendedTextControl(
                            getFontRenderer(),
                            (width / 2) + 3, CraftPresence.GUIS.getButtonY(3),
                            180, 20
                    )
            );
        }

        proceedButton = addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, (height - 30),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.back"),
                        () -> {
                            if (!specificMessageInput.getText().equals(specificMessage) || (isNewValue && !StringUtils.isNullOrEmpty(newValueName.getText()) && !specificMessageInput.getText().equals(defaultMessage)) || (isDefaultValue && !StringUtils.isNullOrEmpty(specificMessageInput.getText()) && !specificMessageInput.getText().equals(specificMessage))) {
                                if (isNewValue && !StringUtils.isNullOrEmpty(newValueName.getText())) {
                                    attributeName = newValueName.getText();
                                }
                                CraftPresence.CONFIG.hasChanged = true;
                                if (parentScreen instanceof BiomeSettingsGui) {
                                    CraftPresence.CONFIG.biomeMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.biomeMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, specificMessageInput.getText());
                                } else if (parentScreen instanceof DimensionSettingsGui) {
                                    CraftPresence.CONFIG.dimensionMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.dimensionMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, specificMessageInput.getText());
                                } else if (parentScreen instanceof ServerSettingsGui) {
                                    CraftPresence.CONFIG.serverMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, specificMessageInput.getText());
                                } else if (parentScreen instanceof AdvancedSettingsGui) {
                                    if (configOption.equals(CraftPresence.CONFIG.NAME_guiMessages)) {
                                        CraftPresence.CONFIG.guiMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.guiMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, specificMessageInput.getText());
                                    } else if (configOption.equals(CraftPresence.CONFIG.NAME_itemMessages)) {
                                        CraftPresence.CONFIG.itemMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.itemMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, specificMessageInput.getText());
                                    } else if (configOption.equals(CraftPresence.CONFIG.NAME_entityTargetMessages)) {
                                        CraftPresence.CONFIG.entityTargetMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.entityTargetMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, specificMessageInput.getText());
                                    } else if (configOption.equals(CraftPresence.CONFIG.NAME_entityAttackingMessages)) {
                                        CraftPresence.CONFIG.entityAttackingMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.entityAttackingMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, specificMessageInput.getText());
                                    } else if (configOption.equals(CraftPresence.CONFIG.NAME_entityRidingMessages)) {
                                        CraftPresence.CONFIG.entityRidingMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.entityRidingMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, specificMessageInput.getText());
                                    }
                                }
                            }
                            if (StringUtils.isNullOrEmpty(specificMessageInput.getText()) || (specificMessageInput.getText().equalsIgnoreCase(defaultMessage) && !specificMessage.equals(defaultMessage) && !isDefaultValue)) {
                                if (isNewValue && !StringUtils.isNullOrEmpty(newValueName.getText())) {
                                    attributeName = newValueName.getText();
                                }
                                CraftPresence.CONFIG.hasChanged = true;
                                if (parentScreen instanceof BiomeSettingsGui) {
                                    CraftPresence.CONFIG.biomeMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.biomeMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                    CraftPresence.BIOMES.BIOME_NAMES.remove(attributeName);
                                    CraftPresence.BIOMES.getBiomes();
                                } else if (parentScreen instanceof DimensionSettingsGui) {
                                    CraftPresence.CONFIG.dimensionMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.dimensionMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                    CraftPresence.DIMENSIONS.DIMENSION_NAMES.remove(attributeName);
                                    CraftPresence.DIMENSIONS.getDimensions();
                                } else if (parentScreen instanceof ServerSettingsGui) {
                                    CraftPresence.CONFIG.serverMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.serverMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                    CraftPresence.SERVER.knownAddresses.remove(attributeName);
                                    CraftPresence.SERVER.getServerAddresses();
                                } else if (parentScreen instanceof AdvancedSettingsGui) {
                                    if (configOption.equals(CraftPresence.CONFIG.NAME_guiMessages)) {
                                        CraftPresence.CONFIG.guiMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.guiMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                        CraftPresence.GUIS.GUI_NAMES.remove(attributeName);
                                        CraftPresence.GUIS.getGUIs();
                                    } else if (configOption.equals(CraftPresence.CONFIG.NAME_itemMessages)) {
                                        CraftPresence.CONFIG.itemMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.itemMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                        CraftPresence.TILE_ENTITIES.TILE_ENTITY_NAMES.remove(attributeName);
                                        CraftPresence.TILE_ENTITIES.getEntities();
                                    } else if (configOption.equals(CraftPresence.CONFIG.NAME_entityTargetMessages)) {
                                        CraftPresence.CONFIG.entityTargetMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.entityTargetMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                        CraftPresence.ENTITIES.ENTITY_NAMES.remove(attributeName);
                                        CraftPresence.ENTITIES.getEntities();
                                    } else if (configOption.equals(CraftPresence.CONFIG.NAME_entityAttackingMessages)) {
                                        CraftPresence.CONFIG.entityAttackingMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.entityAttackingMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                        CraftPresence.ENTITIES.ENTITY_NAMES.remove(attributeName);
                                        CraftPresence.ENTITIES.getEntities();
                                    } else if (configOption.equals(CraftPresence.CONFIG.NAME_entityRidingMessages)) {
                                        CraftPresence.CONFIG.entityRidingMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.entityRidingMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                        CraftPresence.ENTITIES.ENTITY_NAMES.remove(attributeName);
                                        CraftPresence.ENTITIES.getEntities();
                                    }
                                }
                            }
                            CraftPresence.GUIS.openScreen(parentScreen);
                        },
                        () -> {
                            if (!proceedButton.isControlEnabled()) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.empty.default")
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        getFontRenderer(),
                                        true
                                );
                            }
                        }
                )
        );

        super.initializeUi();
    }

    @Override
    public void preRender() {
        final String messageText = ModUtils.TRANSLATOR.translate("gui.config.message.editor.message");
        final String valueNameText = ModUtils.TRANSLATOR.translate("gui.config.message.editor.value.name");

        renderString(mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);
        renderString(messageText, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        if (isNewValue) {
            renderString(valueNameText, (width / 2) - 130, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);
        } else {
            renderString(removeMessage, (width / 2) - (StringUtils.getStringWidth(removeMessage) / 2), (height - 45), 0xFFFFFF);
        }

        proceedButton.setControlMessage(
                !specificMessageInput.getText().equals(specificMessage) ||
                        (isNewValue && !StringUtils.isNullOrEmpty(newValueName.getText()) && !specificMessageInput.getText().equals(defaultMessage)) ||
                        (isDefaultValue && !StringUtils.isNullOrEmpty(specificMessageInput.getText()) && !specificMessageInput.getText().equals(specificMessage)) ?
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.continue") : ModUtils.TRANSLATOR.translate("gui.config.message.button.back")
        );

        proceedButton.setControlEnabled(!(StringUtils.isNullOrEmpty(specificMessageInput.getText()) && isDefaultValue));
    }

    @Override
    public void postRender() {
        final String messageText = ModUtils.TRANSLATOR.translate("gui.config.message.editor.message");
        final String valueNameText = ModUtils.TRANSLATOR.translate("gui.config.message.editor.value.name");
        // Hovering over Message Label
        if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 130, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(messageText), getFontHeight())) {
            if (parentScreen instanceof BiomeSettingsGui) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.biome_messages.biome_messages")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            } else if (parentScreen instanceof DimensionSettingsGui) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.dimension_messages.dimension_messages")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            } else if (parentScreen instanceof ServerSettingsGui) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.server_messages.server_messages")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            } else if (parentScreen instanceof AdvancedSettingsGui) {
                if (configOption.equals(CraftPresence.CONFIG.NAME_guiMessages)) {
                    CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.gui_messages")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
                } else if (configOption.equals(CraftPresence.CONFIG.NAME_itemMessages)) {
                    CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(
                            ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.item_messages",
                                    ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                            CraftPresence.TILE_ENTITIES.generatePlaceholderString(attributeName, CraftPresence.TILE_ENTITIES.getListFromName(attributeName))))
                    ), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
                } else if (configOption.equals(CraftPresence.CONFIG.NAME_entityTargetMessages)) {
                    CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(
                            ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.entity_target_messages",
                                    ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                            CraftPresence.ENTITIES.generatePlaceholderString(attributeName, CraftPresence.ENTITIES.getListFromName(attributeName))))
                    ), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
                } else if (configOption.equals(CraftPresence.CONFIG.NAME_entityAttackingMessages)) {
                    CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(
                            ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.entity_attacking_messages",
                                    ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                            CraftPresence.ENTITIES.generatePlaceholderString(attributeName, CraftPresence.ENTITIES.getListFromName(attributeName))))
                    ), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
                } else if (configOption.equals(CraftPresence.CONFIG.NAME_entityRidingMessages)) {
                    CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(
                            ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.entity_riding_messages",
                                    ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                            CraftPresence.ENTITIES.generatePlaceholderString(attributeName, CraftPresence.ENTITIES.getListFromName(attributeName))))
                    ), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
                }
            }
        }

        // Hovering over Value Name Label
        if (isNewValue && CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 130, CraftPresence.GUIS.getButtonY(3) + 5, StringUtils.getStringWidth(valueNameText), getFontHeight())) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.hover.value.name")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
        }
    }
}
