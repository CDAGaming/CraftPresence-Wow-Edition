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
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.CheckBoxControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ScrollableListControl.RenderType;
import com.gitlab.cdagaming.craftpresence.utils.gui.impl.CharacterEditorGui;
import com.gitlab.cdagaming.craftpresence.utils.gui.impl.DynamicEditorGui;
import com.gitlab.cdagaming.craftpresence.utils.gui.impl.SelectorGui;

import net.minecraft.client.gui.GuiScreen;

import java.util.Arrays;

public class AdvancedSettingsGui extends ExtendedScreen {
    private ExtendedButtonControl proceedButton, guiMessagesButton, itemMessagesButton, entityTargetMessagesButton, entityAttackingMessagesButton, entityRidingMessagesButton;
    private CheckBoxControl enableCommandsButton, enablePerGuiButton,
            enablePerItemButton, enablePerEntityButton, renderTooltipsButton, formatWordsButton, debugModeButton, verboseModeButton;
    private ExtendedTextControl splitCharacter, refreshRate;

    AdvancedSettingsGui(GuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initializeUi() {
        splitCharacter = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) - 60, CraftPresence.GUIS.getButtonY(1),
                        45, 20
                )
        );
        splitCharacter.setText(CraftPresence.CONFIG.splitCharacter);
        splitCharacter.setMaxStringLength(1);

        refreshRate = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 103, CraftPresence.GUIS.getButtonY(1),
                        45, 20
                )
        );
        refreshRate.setText(Integer.toString(CraftPresence.CONFIG.refreshRate));
        refreshRate.setMaxStringLength(3);

        final int calc1 = (width / 2) - 160;
        final int calc2 = (width / 2) + 3;

        guiMessagesButton = addControl(
                new ExtendedButtonControl(
                        calc1, CraftPresence.GUIS.getButtonY(2),
                        160, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.gui_messages"),
                        () -> CraftPresence.GUIS.openScreen(
                                new SelectorGui(
                                        currentScreen, 
                                        ModUtils.TRANSLATOR.translate("gui.config.title.selector.gui"), CraftPresence.GUIS.GUI_NAMES, 
                                        null, null, 
                                        true, true, RenderType.None,
                                        null,
                                        (currentValue, parentScreen) -> {
                                                // Event to occur when Setting Dynamic/Specific Data
                                                CraftPresence.GUIS.openScreen(
                                                        new DynamicEditorGui(
                                                                parentScreen, currentValue, null,
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when initializing existing data
                                                                        screenInstance.mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.gui.edit_specific_gui", attributeName);
                                                                        screenInstance.defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.guiMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                                                                        screenInstance.specificMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.guiMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, screenInstance.defaultMessage);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when adjusting set data
                                                                        CraftPresence.CONFIG.hasChanged = true;
                                                                        CraftPresence.CONFIG.guiMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.guiMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, inputText);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when removing set data
                                                                        CraftPresence.CONFIG.guiMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.guiMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                                                        CraftPresence.GUIS.GUI_NAMES.remove(attributeName);
                                                                        CraftPresence.GUIS.getScreens();
                                                                }, null,
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when Hovering over Message Label
                                                                        CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.gui_messages")), screenInstance.getMouseX(), screenInstance.getMouseY(), screenInstance.width, screenInstance.height, screenInstance.getWrapWidth(), screenInstance.getFontRenderer(), true);
                                                                }
                                                        )
                                                );
                                        },
                                        (parentScreen) -> {
                                                CraftPresence.GUIS.openScreen(
                                                        new DynamicEditorGui(
                                                                parentScreen, null, 
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when initializing new data
                                                                        screenInstance.specificMessage = screenInstance.defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.guiMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                                                                }, null,
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when adjusting set data
                                                                        CraftPresence.CONFIG.hasChanged = true;
                                                                        CraftPresence.CONFIG.guiMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.guiMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, inputText);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when removing set data
                                                                        CraftPresence.CONFIG.guiMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.guiMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                                                        CraftPresence.GUIS.GUI_NAMES.remove(attributeName);
                                                                        CraftPresence.GUIS.getScreens();
                                                                }, null,
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when Hovering over Message Label
                                                                        CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.gui_messages")), screenInstance.getMouseX(), screenInstance.getMouseY(), screenInstance.width, screenInstance.height, screenInstance.getWrapWidth(), screenInstance.getFontRenderer(), true);
                                                                }
                                                        )
                                                );
                                        }
                                )
                        ),
                        () -> {
                            if (!guiMessagesButton.isControlEnabled()) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.enable_per_gui"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        getFontRenderer(),
                                        true);
                            } else {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.gui_messages")
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
        itemMessagesButton = addControl(
                new ExtendedButtonControl(
                        calc2, CraftPresence.GUIS.getButtonY(2),
                        160, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.item_messages"),
                        () -> CraftPresence.GUIS.openScreen(
                                new SelectorGui(
                                        currentScreen, 
                                        ModUtils.TRANSLATOR.translate("gui.config.title.selector.item"), CraftPresence.TILE_ENTITIES.TILE_ENTITY_NAMES, 
                                        null, null, 
                                        true, true, RenderType.ItemData,
                                        null,
                                        (currentValue, parentScreen) -> {
                                                // Event to occur when Setting Dynamic/Specific Data
                                                CraftPresence.GUIS.openScreen(
                                                        new DynamicEditorGui(
                                                                parentScreen, currentValue, null,
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when initializing existing data
                                                                        screenInstance.mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.item.edit_specific_item", attributeName);
                                                                        screenInstance.defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                                                                        screenInstance.specificMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, screenInstance.defaultMessage);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when adjusting set data
                                                                        CraftPresence.CONFIG.hasChanged = true;
                                                                        CraftPresence.CONFIG.itemMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.itemMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, inputText);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when removing set data
                                                                        CraftPresence.CONFIG.itemMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.itemMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                                                        CraftPresence.TILE_ENTITIES.TILE_ENTITY_NAMES.remove(attributeName);
                                                                        CraftPresence.TILE_ENTITIES.getEntities();
                                                                }, null,
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when Hovering over Message Label
                                                                        CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(
                                                                                ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.item_messages",
                                                                                        ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                                                                                CraftPresence.TILE_ENTITIES.generatePlaceholderString(attributeName, CraftPresence.TILE_ENTITIES.getListFromName(attributeName))))
                                                                        ), screenInstance.getMouseX(), screenInstance.getMouseY(), screenInstance.width, screenInstance.height, screenInstance.getWrapWidth(), screenInstance.getFontRenderer(), true);
                                                                }
                                                        )
                                                );
                                        },
                                        (parentScreen) -> {
                                                CraftPresence.GUIS.openScreen(
                                                        new DynamicEditorGui(
                                                                parentScreen, null, 
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when initializing new data
                                                                        screenInstance.specificMessage = screenInstance.defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                                                                }, null,
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when adjusting set data
                                                                        CraftPresence.CONFIG.hasChanged = true;
                                                                        CraftPresence.CONFIG.itemMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.itemMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, inputText);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when removing set data
                                                                        CraftPresence.CONFIG.itemMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.itemMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                                                        CraftPresence.TILE_ENTITIES.TILE_ENTITY_NAMES.remove(attributeName);
                                                                        CraftPresence.TILE_ENTITIES.getEntities();
                                                                }, null,
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when Hovering over Message Label
                                                                        CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(
                                                                                ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.item_messages",
                                                                                        ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                                                                                CraftPresence.TILE_ENTITIES.generatePlaceholderString(attributeName, CraftPresence.TILE_ENTITIES.getListFromName(attributeName))))
                                                                        ), screenInstance.getMouseX(), screenInstance.getMouseY(), screenInstance.width, screenInstance.height, screenInstance.getWrapWidth(), screenInstance.getFontRenderer(), true);
                                                                }
                                                        )
                                                );
                                        }
                                )
                        ),
                        () -> {
                            if (!itemMessagesButton.isControlEnabled()) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.enable_per_item"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        getFontRenderer(),
                                        true
                                );
                            } else {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.item_messages",
                                                        ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                                                CraftPresence.ENTITIES.generatePlaceholderString("", CraftPresence.TILE_ENTITIES.getListFromName(""))))
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
        entityTargetMessagesButton = addControl(
                new ExtendedButtonControl(
                        calc1, CraftPresence.GUIS.getButtonY(3),
                        160, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.entity_target_messages"),
                        () -> CraftPresence.GUIS.openScreen(
                                new SelectorGui(
                                        currentScreen, 
                                        ModUtils.TRANSLATOR.translate("gui.config.title.selector.entity"), CraftPresence.ENTITIES.ENTITY_NAMES, 
                                        null, null, 
                                        true, true, RenderType.EntityData,
                                        null,
                                        (currentValue, parentScreen) -> {
                                                // Event to occur when Setting Dynamic/Specific Data
                                                CraftPresence.GUIS.openScreen(
                                                        new DynamicEditorGui(
                                                                parentScreen, currentValue, null,
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when initializing existing data
                                                                        screenInstance.mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.entity.edit_specific_entity", attributeName);
                                                                        screenInstance.defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityTargetMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                                                                        screenInstance.specificMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityTargetMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, screenInstance.defaultMessage);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when adjusting set data
                                                                        CraftPresence.CONFIG.hasChanged = true;
                                                                        CraftPresence.CONFIG.entityTargetMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.entityTargetMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, inputText);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when removing set data
                                                                        CraftPresence.CONFIG.entityTargetMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.entityTargetMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                                                        CraftPresence.ENTITIES.ENTITY_NAMES.remove(attributeName);
                                                                        CraftPresence.ENTITIES.getEntities();
                                                                }, null,
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when Hovering over Message Label
                                                                        CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(
                                                                                ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.entity_target_messages",
                                                                                        ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                                                                                CraftPresence.ENTITIES.generatePlaceholderString(attributeName, CraftPresence.ENTITIES.getListFromName(attributeName))))
                                                                        ), screenInstance.getMouseX(), screenInstance.getMouseY(), screenInstance.width, screenInstance.height, screenInstance.getWrapWidth(), screenInstance.getFontRenderer(), true);
                                                                }
                                                        )
                                                );
                                        },
                                        (parentScreen) -> {
                                                CraftPresence.GUIS.openScreen(
                                                        new DynamicEditorGui(
                                                                parentScreen, null, 
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when initializing new data
                                                                        screenInstance.specificMessage = screenInstance.defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityTargetMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                                                                }, null,
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when adjusting set data
                                                                        CraftPresence.CONFIG.hasChanged = true;
                                                                        CraftPresence.CONFIG.entityTargetMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.entityTargetMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, inputText);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when removing set data
                                                                        CraftPresence.CONFIG.entityTargetMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.entityTargetMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                                                        CraftPresence.ENTITIES.ENTITY_NAMES.remove(attributeName);
                                                                        CraftPresence.ENTITIES.getEntities();
                                                                }, null,
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when Hovering over Message Label
                                                                        CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(
                                                                                ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.entity_target_messages",
                                                                                        ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                                                                                CraftPresence.ENTITIES.generatePlaceholderString(attributeName, CraftPresence.ENTITIES.getListFromName(attributeName))))
                                                                        ), screenInstance.getMouseX(), screenInstance.getMouseY(), screenInstance.width, screenInstance.height, screenInstance.getWrapWidth(), screenInstance.getFontRenderer(), true);
                                                                }
                                                        )
                                                );
                                        }
                                )
                        ),
                        () -> {
                            if (!entityTargetMessagesButton.isControlEnabled()) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.enable_per_entity"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        getFontRenderer(),
                                        true
                                );
                            } else {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.entity_target_messages",
                                                        ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                                                CraftPresence.ENTITIES.generatePlaceholderString(CraftPresence.ENTITIES.CURRENT_TARGET_NAME, CraftPresence.ENTITIES.CURRENT_TARGET_TAGS)))
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
        entityAttackingMessagesButton = addControl(
                new ExtendedButtonControl(
                        calc2, CraftPresence.GUIS.getButtonY(3),
                        160, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.entity_attacking_messages"),
                        () -> CraftPresence.GUIS.openScreen(
                                new SelectorGui(
                                        currentScreen, 
                                        ModUtils.TRANSLATOR.translate("gui.config.title.selector.entity"), CraftPresence.ENTITIES.ENTITY_NAMES, 
                                        null, null, 
                                        true, true, RenderType.EntityData,
                                        null,
                                        (currentValue, parentScreen) -> {
                                                // Event to occur when Setting Dynamic/Specific Data
                                                CraftPresence.GUIS.openScreen(
                                                        new DynamicEditorGui(
                                                                parentScreen, currentValue, null,
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when initializing existing data
                                                                        screenInstance.mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.entity.edit_specific_entity", attributeName);
                                                                        screenInstance.defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityAttackingMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                                                                        screenInstance.specificMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityAttackingMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, screenInstance.defaultMessage);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when adjusting set data
                                                                        CraftPresence.CONFIG.hasChanged = true;
                                                                        CraftPresence.CONFIG.entityAttackingMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.entityAttackingMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, inputText);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when removing set data
                                                                        CraftPresence.CONFIG.entityAttackingMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.entityAttackingMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                                                        CraftPresence.ENTITIES.ENTITY_NAMES.remove(attributeName);
                                                                        CraftPresence.ENTITIES.getEntities();
                                                                }, null,
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when Hovering over Message Label
                                                                        CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(
                                                                                ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.entity_attacking_messages",
                                                                                        ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                                                                                CraftPresence.ENTITIES.generatePlaceholderString(attributeName, CraftPresence.ENTITIES.getListFromName(attributeName))))
                                                                        ), screenInstance.getMouseX(), screenInstance.getMouseY(), screenInstance.width, screenInstance.height, screenInstance.getWrapWidth(), screenInstance.getFontRenderer(), true);
                                                                }
                                                        )
                                                );
                                        },
                                        (parentScreen) -> {
                                                CraftPresence.GUIS.openScreen(
                                                        new DynamicEditorGui(
                                                                parentScreen, null, 
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when initializing new data
                                                                        screenInstance.specificMessage = screenInstance.defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityAttackingMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                                                                }, null,
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when adjusting set data
                                                                        CraftPresence.CONFIG.hasChanged = true;
                                                                        CraftPresence.CONFIG.entityAttackingMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.entityAttackingMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, inputText);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when removing set data
                                                                        CraftPresence.CONFIG.entityAttackingMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.entityAttackingMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                                                        CraftPresence.ENTITIES.ENTITY_NAMES.remove(attributeName);
                                                                        CraftPresence.ENTITIES.getEntities();
                                                                }, null,
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when Hovering over Message Label
                                                                        CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(
                                                                                ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.entity_attacking_messages",
                                                                                        ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                                                                                CraftPresence.ENTITIES.generatePlaceholderString(attributeName, CraftPresence.ENTITIES.getListFromName(attributeName))))
                                                                        ), screenInstance.getMouseX(), screenInstance.getMouseY(), screenInstance.width, screenInstance.height, screenInstance.getWrapWidth(), screenInstance.getFontRenderer(), true);
                                                                }
                                                        )
                                                );
                                        }
                                )
                        ),
                        () -> {
                            if (!entityAttackingMessagesButton.isControlEnabled()) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.enable_per_entity"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        getFontRenderer(),
                                        true
                                );
                            } else {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.entity_attacking_messages",
                                                        ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                                                CraftPresence.ENTITIES.generatePlaceholderString(CraftPresence.ENTITIES.CURRENT_ATTACKING_NAME, CraftPresence.ENTITIES.CURRENT_ATTACKING_TAGS)))
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
        entityRidingMessagesButton = addControl(
                new ExtendedButtonControl(
                        calc1, CraftPresence.GUIS.getButtonY(4),
                        160, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.entity_riding_messages"),
                        () -> CraftPresence.GUIS.openScreen(
                                new SelectorGui(
                                        currentScreen, 
                                        ModUtils.TRANSLATOR.translate("gui.config.title.selector.entity"), CraftPresence.ENTITIES.ENTITY_NAMES, 
                                        null, null, 
                                        true, true, RenderType.EntityData,
                                        null,
                                        (currentValue, parentScreen) -> {
                                                // Event to occur when Setting Dynamic/Specific Data
                                                CraftPresence.GUIS.openScreen(
                                                        new DynamicEditorGui(
                                                                parentScreen, currentValue, null,
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when initializing existing data
                                                                        screenInstance.mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.entity.edit_specific_entity", attributeName);
                                                                        screenInstance.defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityRidingMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                                                                        screenInstance.specificMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityRidingMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, screenInstance.defaultMessage);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when adjusting set data
                                                                        CraftPresence.CONFIG.hasChanged = true;
                                                                        CraftPresence.CONFIG.entityRidingMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.entityRidingMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, inputText);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when removing set data
                                                                        CraftPresence.CONFIG.entityRidingMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.entityRidingMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                                                        CraftPresence.ENTITIES.ENTITY_NAMES.remove(attributeName);
                                                                        CraftPresence.ENTITIES.getEntities();
                                                                }, null,
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when Hovering over Message Label
                                                                        CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(
                                                                                ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.entity_riding_messages",
                                                                                        ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                                                                                CraftPresence.ENTITIES.generatePlaceholderString(attributeName, CraftPresence.ENTITIES.getListFromName(attributeName))))
                                                                        ), screenInstance.getMouseX(), screenInstance.getMouseY(), screenInstance.width, screenInstance.height, screenInstance.getWrapWidth(), screenInstance.getFontRenderer(), true);
                                                                }
                                                        )
                                                );
                                        },
                                        (parentScreen) -> {
                                                CraftPresence.GUIS.openScreen(
                                                        new DynamicEditorGui(
                                                                parentScreen, null, 
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when initializing new data
                                                                        screenInstance.specificMessage = screenInstance.defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.entityRidingMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                                                                }, null,
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when adjusting set data
                                                                        CraftPresence.CONFIG.hasChanged = true;
                                                                        CraftPresence.CONFIG.entityRidingMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.entityRidingMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, inputText);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                        // Event to occur when removing set data
                                                                        CraftPresence.CONFIG.entityRidingMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.entityRidingMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                                                        CraftPresence.ENTITIES.ENTITY_NAMES.remove(attributeName);
                                                                        CraftPresence.ENTITIES.getEntities();
                                                                }, null,
                                                                (attributeName, screenInstance) -> {
                                                                        // Event to occur when Hovering over Message Label
                                                                        CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(
                                                                                ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.entity_riding_messages",
                                                                                        ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                                                                                CraftPresence.ENTITIES.generatePlaceholderString(attributeName, CraftPresence.ENTITIES.getListFromName(attributeName))))
                                                                        ), screenInstance.getMouseX(), screenInstance.getMouseY(), screenInstance.width, screenInstance.height, screenInstance.getWrapWidth(), screenInstance.getFontRenderer(), true);
                                                                }
                                                        )
                                                );
                                        }
                                )
                        ),
                        () -> {
                            if (!entityRidingMessagesButton.isControlEnabled()) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.enable_per_entity"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        getFontRenderer(),
                                        true
                                );
                            } else {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.entity_riding_messages",
                                                        ModUtils.TRANSLATOR.translate("gui.config.message.tags",
                                                                CraftPresence.ENTITIES.generatePlaceholderString(CraftPresence.ENTITIES.CURRENT_RIDING_NAME, CraftPresence.ENTITIES.CURRENT_RIDING_TAGS)))
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

        // Adding Character Editor Button
        addControl(
                new ExtendedButtonControl(
                        calc2, CraftPresence.GUIS.getButtonY(4),
                        160, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.char.editor"),
                        () -> CraftPresence.GUIS.openScreen(new CharacterEditorGui(currentScreen))
                )
        );

        enableCommandsButton = addControl(
                new CheckBoxControl(
                        calc1, CraftPresence.GUIS.getButtonY(5),
                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.enable_commands"),
                        CraftPresence.CONFIG.enableCommands,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.enable_commands")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );
        enablePerGuiButton = addControl(
                new CheckBoxControl(
                        calc2, CraftPresence.GUIS.getButtonY(5),
                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.enable_per_gui"),
                        CraftPresence.CONFIG.enablePerGui,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.enable_per_gui")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );
        enablePerItemButton = addControl(
                new CheckBoxControl(
                        calc1, CraftPresence.GUIS.getButtonY(6) - 10,
                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.enable_per_item"),
                        CraftPresence.CONFIG.enablePerItem,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.enable_per_item")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );
        enablePerEntityButton = addControl(
                new CheckBoxControl(
                        calc2, CraftPresence.GUIS.getButtonY(6) - 10,
                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.enable_per_entity"),
                        CraftPresence.CONFIG.enablePerEntity,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.enable_per_entity")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );
        renderTooltipsButton = addControl(
                new CheckBoxControl(
                        calc1, CraftPresence.GUIS.getButtonY(7) - 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.render_tooltips"),
                        CraftPresence.CONFIG.renderTooltips,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.render_tooltips")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );
        formatWordsButton = addControl(
                new CheckBoxControl(
                        calc2, CraftPresence.GUIS.getButtonY(7) - 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.format_words"),
                        CraftPresence.CONFIG.formatWords,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.format_words")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );
        debugModeButton = addControl(
                new CheckBoxControl(
                        calc1, CraftPresence.GUIS.getButtonY(8) - 30,
                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.debug_mode"),
                        CraftPresence.CONFIG.debugMode,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.debug_mode", CraftPresence.isDevStatusOverridden)
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );
        verboseModeButton = addControl(
                new CheckBoxControl(
                        calc2, CraftPresence.GUIS.getButtonY(8) - 30,
                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.verbose_mode"),
                        CraftPresence.CONFIG.verboseMode,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.verbose_mode", CraftPresence.isVerboseStatusOverridden)
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );
        proceedButton = addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, (height - 30),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.back"),
                        () -> {
                            if (!splitCharacter.getText().equals(CraftPresence.CONFIG.splitCharacter)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.queuedSplitCharacter = splitCharacter.getText();
                            }
                            if (!refreshRate.getText().equals(Integer.toString(CraftPresence.CONFIG.refreshRate))) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.refreshRate = StringUtils.getValidInteger(refreshRate.getText()).getSecond();
                            }
                            if (enableCommandsButton.isChecked() != CraftPresence.CONFIG.enableCommands) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.enableCommands = enableCommandsButton.isChecked();
                            }
                            if (enablePerGuiButton.isChecked() != CraftPresence.CONFIG.enablePerGui) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.enablePerGui = enablePerGuiButton.isChecked();
                            }
                            if (enablePerItemButton.isChecked() != CraftPresence.CONFIG.enablePerItem) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.enablePerItem = enablePerItemButton.isChecked();
                            }
                            if (enablePerEntityButton.isChecked() != CraftPresence.CONFIG.enablePerEntity) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.enablePerEntity = enablePerEntityButton.isChecked();
                            }
                            if (renderTooltipsButton.isChecked() != CraftPresence.CONFIG.renderTooltips) {
                                CraftPresence.CONFIG.hasChanged = true;
                                if (renderTooltipsButton.isChecked() && (Arrays.equals(StringUtils.MC_CHAR_WIDTH, new int[256]) || Arrays.equals(StringUtils.MC_GLYPH_WIDTH, new byte[65536]))) {
                                    ModUtils.loadCharData(true, "UTF-8");
                                }
                                CraftPresence.CONFIG.renderTooltips = renderTooltipsButton.isChecked();
                            }
                            if (formatWordsButton.isChecked() != CraftPresence.CONFIG.formatWords) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.formatWords = formatWordsButton.isChecked();
                            }
                            if (debugModeButton.isChecked() != CraftPresence.CONFIG.debugMode) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.debugMode = debugModeButton.isChecked();
                            }
                            if (verboseModeButton.isChecked() != CraftPresence.CONFIG.verboseMode) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.verboseMode = verboseModeButton.isChecked();
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
        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.advanced");
        final String splitCharacterText = ModUtils.TRANSLATOR.translate("gui.config.name.advanced.split_character");
        final String refreshRateText = ModUtils.TRANSLATOR.translate("gui.config.name.advanced.refresh_rate");

        renderString(mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        renderString(subTitle, (width / 2) - (StringUtils.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);
        renderString(splitCharacterText, (width / 2) - 145, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        renderString(refreshRateText, (width / 2) + 18, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);

        proceedButton.setControlEnabled(
                !StringUtils.isNullOrEmpty(splitCharacter.getText()) && splitCharacter.getText().length() == 1 &&
                        !splitCharacter.getText().matches(".*[a-z].*") && !splitCharacter.getText().matches(".*[A-Z].*") &&
                        !splitCharacter.getText().matches(".*[0-9].*") && StringUtils.getValidInteger(refreshRate.getText()).getFirst()
        );

        guiMessagesButton.setControlEnabled(!CraftPresence.CONFIG.hasChanged ? CraftPresence.GUIS.enabled : guiMessagesButton.isControlEnabled());
        itemMessagesButton.setControlEnabled(!CraftPresence.CONFIG.hasChanged ? CraftPresence.TILE_ENTITIES.enabled : itemMessagesButton.isControlEnabled());
        entityTargetMessagesButton.setControlEnabled(!CraftPresence.CONFIG.hasChanged ? CraftPresence.ENTITIES.enabled : entityTargetMessagesButton.isControlEnabled());
        entityAttackingMessagesButton.setControlEnabled(!CraftPresence.CONFIG.hasChanged ? CraftPresence.ENTITIES.enabled : entityAttackingMessagesButton.isControlEnabled());
        entityRidingMessagesButton.setControlEnabled(!CraftPresence.CONFIG.hasChanged ? CraftPresence.ENTITIES.enabled : entityRidingMessagesButton.isControlEnabled());
    }

    @Override
    public void postRender() {
        final String splitCharacterText = ModUtils.TRANSLATOR.translate("gui.config.name.advanced.split_character");
        final String refreshRateText = ModUtils.TRANSLATOR.translate("gui.config.name.advanced.refresh_rate");
        // Hovering over Split Character Message Label
        if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 145, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(splitCharacterText), getFontHeight())) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.split_character")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
        }

        // Hovering over Refresh Rate Message Label
        if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) + 18, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(refreshRateText), getFontHeight())) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.refresh_rate")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
        }
    }
}
