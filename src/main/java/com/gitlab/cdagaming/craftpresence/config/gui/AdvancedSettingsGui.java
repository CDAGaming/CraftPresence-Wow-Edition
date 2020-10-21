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
                        mc.fontRenderer,
                        (width / 2) - 60, CraftPresence.GUIS.getButtonY(1),
                        45, 20
                )
        );
        splitCharacter.setText(CraftPresence.CONFIG.splitCharacter);
        splitCharacter.setMaxStringLength(1);

        refreshRate = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
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
                        () -> CraftPresence.GUIS.openScreen(new SelectorGui(currentScreen, CraftPresence.CONFIG.NAME_guiMessages, ModUtils.TRANSLATOR.translate("gui.config.title.selector.gui"), CraftPresence.GUIS.GUI_NAMES, null, null, true)),
                        () -> {
                            if (!guiMessagesButton.enabled) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.enable_per_gui"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        mc.fontRenderer,
                                        true);
                            } else {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.gui_messages")
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        mc.fontRenderer,
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
                        () -> CraftPresence.GUIS.openScreen(new SelectorGui(currentScreen, CraftPresence.CONFIG.NAME_itemMessages, ModUtils.TRANSLATOR.translate("gui.config.title.selector.item"), CraftPresence.TILE_ENTITIES.TILE_ENTITY_NAMES, null, null, true)),
                        () -> {
                            if (!itemMessagesButton.enabled) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.enable_per_item"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        mc.fontRenderer,
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
                                        mc.fontRenderer,
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
                        () -> CraftPresence.GUIS.openScreen(new SelectorGui(currentScreen, CraftPresence.CONFIG.NAME_entityTargetMessages, ModUtils.TRANSLATOR.translate("gui.config.title.selector.entity"), CraftPresence.ENTITIES.ENTITY_NAMES, null, null, true)),
                        () -> {
                            if (!entityTargetMessagesButton.enabled) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.enable_per_entity"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        mc.fontRenderer,
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
                                        mc.fontRenderer,
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
                        () -> CraftPresence.GUIS.openScreen(new SelectorGui(currentScreen, CraftPresence.CONFIG.NAME_entityAttackingMessages, ModUtils.TRANSLATOR.translate("gui.config.title.selector.entity"), CraftPresence.ENTITIES.ENTITY_NAMES, null, null, true)),
                        () -> {
                            if (!entityAttackingMessagesButton.enabled) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.enable_per_entity"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        mc.fontRenderer,
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
                                        mc.fontRenderer,
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
                        () -> CraftPresence.GUIS.openScreen(new SelectorGui(currentScreen, CraftPresence.CONFIG.NAME_entityRidingMessages, ModUtils.TRANSLATOR.translate("gui.config.title.selector.entity"), CraftPresence.ENTITIES.ENTITY_NAMES, null, null, true)),
                        () -> {
                            if (!entityRidingMessagesButton.enabled) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.advanced.enable_per_entity"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        mc.fontRenderer,
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
                                        mc.fontRenderer,
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
                                mc.fontRenderer,
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
                                mc.fontRenderer,
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
                                mc.fontRenderer,
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
                                mc.fontRenderer,
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
                                mc.fontRenderer,
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
                                mc.fontRenderer,
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
                                mc.fontRenderer,
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
                                mc.fontRenderer,
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
                            if (!proceedButton.enabled) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.empty.default")
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        mc.fontRenderer,
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

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringUtils.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);
        drawString(mc.fontRenderer, splitCharacterText, (width / 2) - 145, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        drawString(mc.fontRenderer, refreshRateText, (width / 2) + 18, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);

        proceedButton.enabled = !StringUtils.isNullOrEmpty(splitCharacter.getText()) && splitCharacter.getText().length() == 1 && !splitCharacter.getText().matches(".*[a-z].*") && !splitCharacter.getText().matches(".*[A-Z].*") && !splitCharacter.getText().matches(".*[0-9].*") && StringUtils.getValidInteger(refreshRate.getText()).getFirst();

        guiMessagesButton.enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.GUIS.enabled : guiMessagesButton.enabled;
        itemMessagesButton.enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.TILE_ENTITIES.enabled : itemMessagesButton.enabled;
        entityTargetMessagesButton.enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.ENTITIES.enabled : entityTargetMessagesButton.enabled;
        entityAttackingMessagesButton.enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.ENTITIES.enabled : entityAttackingMessagesButton.enabled;
        entityRidingMessagesButton.enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.ENTITIES.enabled : entityRidingMessagesButton.enabled;
    }

    @Override
    public void postRender() {
        final String splitCharacterText = ModUtils.TRANSLATOR.translate("gui.config.name.advanced.split_character");
        final String refreshRateText = ModUtils.TRANSLATOR.translate("gui.config.name.advanced.refresh_rate");
        // Hovering over Split Character Message Label
        if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 145, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(splitCharacterText), mc.fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.split_character")), getMouseX(), getMouseY(), width, height, getWrapWidth(), mc.fontRenderer, true);
        }

        // Hovering over Refresh Rate Message Label
        if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) + 18, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(refreshRateText), mc.fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.advanced.refresh_rate")), getMouseX(), getMouseY(), width, height, getWrapWidth(), mc.fontRenderer, true);
        }
    }
}
