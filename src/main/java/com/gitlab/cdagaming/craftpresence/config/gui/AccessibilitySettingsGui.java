/*
 * MIT License
 *
 * Copyright (c) 2018 - 2021 CDAGaming (cstack2011@yahoo.com)
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
import com.gitlab.cdagaming.craftpresence.config.ConfigUtils;
import com.gitlab.cdagaming.craftpresence.impl.Pair;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.CheckBoxControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.impl.ColorEditorGui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class AccessibilitySettingsGui extends ExtendedScreen {

    // Pair Format: buttonToModify, Config Field to Edit
    // (Store a Backup of Prior Text just in case)
    private String backupKeyString;
    private Pair<ExtendedButtonControl, String> entryData = null;

    private ExtendedTextControl languageIdText;
    private CheckBoxControl showBackgroundAsDarkButton, stripTranslationColorsButton, showLoggingInChatButton, stripExtraGuiElementsButton;
    private ExtendedButtonControl proceedButton;

    AccessibilitySettingsGui(GuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initializeUi() {
        final int calc1 = (width / 2) - 183;
        final int calc2 = (width / 2) + 3;

        // Adding Tooltip Background Button
        addControl(
                new ExtendedButtonControl(
                        calc1, CraftPresence.GUIS.getButtonY(1),
                        180, 20,
                        CraftPresence.CONFIG.NAME_tooltipBackgroundColor.replaceAll("_", " "),
                        () -> CraftPresence.GUIS.openScreen(
                                new ColorEditorGui(
                                        currentScreen, CraftPresence.CONFIG.NAME_tooltipBackgroundColor,
                                        (pageNumber, screenInstance) -> {
                                            if (pageNumber == 0 && !screenInstance.currentNormalHexValue.equals(CraftPresence.CONFIG.tooltipBackgroundColor)) {
                                                CraftPresence.CONFIG.hasChanged = true;
                                                CraftPresence.CONFIG.tooltipBackgroundColor = screenInstance.currentNormalHexValue;
                                            } else if (pageNumber == 1) {
                                                final String adjustValue = screenInstance.usingExternalTexture ? CraftPresence.CONFIG.tooltipBackgroundColor : CraftPresence.CONFIG.tooltipBackgroundColor.replace(CraftPresence.CONFIG.splitCharacter, ":");
                                                if (!screenInstance.currentNormalTexturePath.equals(adjustValue)) {
                                                    CraftPresence.CONFIG.hasChanged = true;
                                                    CraftPresence.CONFIG.tooltipBackgroundColor = adjustValue;
                                                }
                                            }
                                        },
                                        (screenInstance) -> {
                                            if (StringUtils.isValidColorCode(CraftPresence.CONFIG.tooltipBackgroundColor)) {
                                                screenInstance.startingHexValue = CraftPresence.CONFIG.tooltipBackgroundColor;
                                            } else if (!StringUtils.isNullOrEmpty(CraftPresence.CONFIG.tooltipBackgroundColor)) {
                                                screenInstance.startingTexturePath = CraftPresence.CONFIG.tooltipBackgroundColor;
                                            }
                                        }
                                )
                        ),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.accessibility.tooltip_background_color")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );
        // Adding Tooltip Border Color Button
        addControl(
                new ExtendedButtonControl(
                        calc2, CraftPresence.GUIS.getButtonY(1),
                        180, 20,
                        CraftPresence.CONFIG.NAME_tooltipBorderColor.replaceAll("_", " "),
                        () -> CraftPresence.GUIS.openScreen(
                                new ColorEditorGui(
                                        currentScreen, CraftPresence.CONFIG.NAME_tooltipBorderColor,
                                        (pageNumber, screenInstance) -> {
                                            if (pageNumber == 0 && !screenInstance.currentNormalHexValue.equals(CraftPresence.CONFIG.tooltipBorderColor)) {
                                                CraftPresence.CONFIG.hasChanged = true;
                                                CraftPresence.CONFIG.tooltipBorderColor = screenInstance.currentNormalHexValue;
                                            } else if (pageNumber == 1) {
                                                final String adjustValue = screenInstance.usingExternalTexture ? CraftPresence.CONFIG.tooltipBorderColor : CraftPresence.CONFIG.tooltipBorderColor.replace(CraftPresence.CONFIG.splitCharacter, ":");
                                                if (!screenInstance.currentNormalTexturePath.equals(adjustValue)) {
                                                    CraftPresence.CONFIG.hasChanged = true;
                                                    CraftPresence.CONFIG.tooltipBorderColor = adjustValue;
                                                }
                                            }
                                        },
                                        (screenInstance) -> {
                                            if (StringUtils.isValidColorCode(CraftPresence.CONFIG.tooltipBorderColor)) {
                                                screenInstance.startingHexValue = CraftPresence.CONFIG.tooltipBorderColor;
                                            } else if (!StringUtils.isNullOrEmpty(CraftPresence.CONFIG.tooltipBorderColor)) {
                                                screenInstance.startingTexturePath = CraftPresence.CONFIG.tooltipBorderColor;
                                            }
                                        }
                                )
                        ),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.accessibility.tooltip_border_color")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );
        // Adding Gui Background Color Button
        addControl(
                new ExtendedButtonControl(
                        calc1, CraftPresence.GUIS.getButtonY(2),
                        180, 20,
                        CraftPresence.CONFIG.NAME_guiBackgroundColor.replaceAll("_", " "),
                        () -> CraftPresence.GUIS.openScreen(
                                new ColorEditorGui(
                                        currentScreen, CraftPresence.CONFIG.NAME_guiBackgroundColor,
                                        (pageNumber, screenInstance) -> {
                                            if (pageNumber == 0 && !screenInstance.currentNormalHexValue.equals(CraftPresence.CONFIG.guiBackgroundColor)) {
                                                CraftPresence.CONFIG.hasChanged = true;
                                                CraftPresence.CONFIG.guiBackgroundColor = screenInstance.currentNormalHexValue;
                                            } else if (pageNumber == 1) {
                                                final String adjustValue = screenInstance.usingExternalTexture ? CraftPresence.CONFIG.guiBackgroundColor : CraftPresence.CONFIG.guiBackgroundColor.replace(CraftPresence.CONFIG.splitCharacter, ":");
                                                if (!screenInstance.currentNormalTexturePath.equals(adjustValue)) {
                                                    CraftPresence.CONFIG.hasChanged = true;
                                                    CraftPresence.CONFIG.guiBackgroundColor = adjustValue;
                                                }
                                            }
                                        },
                                        (screenInstance) -> {
                                            if (StringUtils.isValidColorCode(CraftPresence.CONFIG.guiBackgroundColor)) {
                                                screenInstance.startingHexValue = CraftPresence.CONFIG.guiBackgroundColor;
                                            } else if (!StringUtils.isNullOrEmpty(CraftPresence.CONFIG.guiBackgroundColor)) {
                                                screenInstance.startingTexturePath = CraftPresence.CONFIG.guiBackgroundColor;
                                            }
                                        }
                                )
                        ),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.accessibility.gui_background_color")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );
        // Adding Button Background Color Button
        addControl(
                new ExtendedButtonControl(
                        calc2, CraftPresence.GUIS.getButtonY(2),
                        180, 20,
                        CraftPresence.CONFIG.NAME_buttonBackgroundColor.replaceAll("_", " "),
                        () -> CraftPresence.GUIS.openScreen(
                                new ColorEditorGui(
                                        currentScreen, CraftPresence.CONFIG.NAME_buttonBackgroundColor,
                                        (pageNumber, screenInstance) -> {
                                            if (pageNumber == 0 && !screenInstance.currentNormalHexValue.equals(CraftPresence.CONFIG.buttonBackgroundColor)) {
                                                CraftPresence.CONFIG.hasChanged = true;
                                                CraftPresence.CONFIG.buttonBackgroundColor = screenInstance.currentNormalHexValue;
                                            } else if (pageNumber == 1) {
                                                final String adjustValue = screenInstance.usingExternalTexture ? CraftPresence.CONFIG.buttonBackgroundColor : CraftPresence.CONFIG.buttonBackgroundColor.replace(CraftPresence.CONFIG.splitCharacter, ":");
                                                if (!screenInstance.currentNormalTexturePath.equals(adjustValue)) {
                                                    CraftPresence.CONFIG.hasChanged = true;
                                                    CraftPresence.CONFIG.buttonBackgroundColor = adjustValue;
                                                }
                                            }
                                        },
                                        (screenInstance) -> {
                                            if (StringUtils.isValidColorCode(CraftPresence.CONFIG.buttonBackgroundColor)) {
                                                screenInstance.startingHexValue = CraftPresence.CONFIG.buttonBackgroundColor;
                                            } else if (!StringUtils.isNullOrEmpty(CraftPresence.CONFIG.buttonBackgroundColor)) {
                                                screenInstance.startingTexturePath = CraftPresence.CONFIG.buttonBackgroundColor;
                                            }
                                        }
                                )
                        ),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.accessibility.button_background_color")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );

        languageIdText = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        calc2, CraftPresence.GUIS.getButtonY(3),
                        180, 20
                )
        );
        languageIdText.setText(CraftPresence.CONFIG.languageId);

        showBackgroundAsDarkButton = addControl(
                new CheckBoxControl(
                        calc1, CraftPresence.GUIS.getButtonY(4),
                        ModUtils.TRANSLATOR.translate("gui.config.name.accessibility.show_background_as_dark"),
                        CraftPresence.CONFIG.showBackgroundAsDark,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.accessibility.show_background_as_dark")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );
        stripTranslationColorsButton = addControl(
                new CheckBoxControl(
                        calc2, CraftPresence.GUIS.getButtonY(4),
                        ModUtils.TRANSLATOR.translate("gui.config.name.accessibility.strip_translation_colors"),
                        CraftPresence.CONFIG.stripTranslationColors,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.accessibility.strip_translation_colors")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );
        showLoggingInChatButton = addControl(
                new CheckBoxControl(
                        calc1, CraftPresence.GUIS.getButtonY(5, -10),
                        ModUtils.TRANSLATOR.translate("gui.config.name.accessibility.show_logging_in_chat"),
                        CraftPresence.CONFIG.showLoggingInChat,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.accessibility.show_logging_in_chat")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );
        stripExtraGuiElementsButton = addControl(
                new CheckBoxControl(
                        calc2, CraftPresence.GUIS.getButtonY(5, -10),
                        ModUtils.TRANSLATOR.translate("gui.config.name.accessibility.strip_extra_gui_elements"),
                        CraftPresence.CONFIG.stripExtraGuiElements,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.accessibility.strip_extra_gui_elements")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );

        // KeyCode Buttons
        final ExtendedButtonControl configKeyCodeButton = addControl(
                new ExtendedButtonControl(
                        calc2 + 20, CraftPresence.GUIS.getButtonY(7),
                        120, 20,
                        CraftPresence.KEYBINDINGS.getKeyName(CraftPresence.CONFIG.configKeyCode),
                        "configKeyCode"
                )
        );
        configKeyCodeButton.setOnClick(() -> setupEntryData(configKeyCodeButton));

        proceedButton = addControl(
                new ExtendedButtonControl(
                        10, (height - 30),
                        95, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.back"),
                        () -> {
                            if (entryData == null) {
                                if (!languageIdText.getText().equals(CraftPresence.CONFIG.languageId)) {
                                    CraftPresence.CONFIG.hasChanged = true;
                                    CraftPresence.CONFIG.languageId = languageIdText.getText();
                                }
                                if (showBackgroundAsDarkButton.isChecked() != CraftPresence.CONFIG.showBackgroundAsDark) {
                                    CraftPresence.CONFIG.hasChanged = true;
                                    CraftPresence.CONFIG.showBackgroundAsDark = showBackgroundAsDarkButton.isChecked();
                                }
                                if (stripTranslationColorsButton.isChecked() != CraftPresence.CONFIG.stripTranslationColors) {
                                    CraftPresence.CONFIG.hasChanged = true;
                                    CraftPresence.CONFIG.stripTranslationColors = stripTranslationColorsButton.isChecked();
                                }
                                if (showLoggingInChatButton.isChecked() != CraftPresence.CONFIG.showLoggingInChat) {
                                    CraftPresence.CONFIG.hasChanged = true;
                                    CraftPresence.CONFIG.showLoggingInChat = showLoggingInChatButton.isChecked();
                                }
                                if (stripExtraGuiElementsButton.isChecked() != CraftPresence.CONFIG.stripExtraGuiElements) {
                                    CraftPresence.CONFIG.hasChanged = true;
                                    CraftPresence.CONFIG.stripExtraGuiElements = stripExtraGuiElementsButton.isChecked();
                                }
                                CraftPresence.GUIS.openScreen(parentScreen);
                            }
                        }
                )
        );

        super.initializeUi();
    }

    @Override
    public void preRender() {
        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.accessibility");

        final String languageIdTitle = ModUtils.TRANSLATOR.translate("gui.config.name.accessibility.language_id");

        final String keyBindingTitle = ModUtils.TRANSLATOR.translate("key.craftpresence.category");
        final String configKeyBindingTitle = ModUtils.TRANSLATOR.translate("key.craftpresence.config_keycode.name");

        renderString(mainTitle, (width / 2f) - (StringUtils.getStringWidth(mainTitle) / 2f), 10, 0xFFFFFF);
        renderString(subTitle, (width / 2f) - (StringUtils.getStringWidth(subTitle) / 2f), 20, 0xFFFFFF);

        renderString(languageIdTitle, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(3, 5), 0xFFFFFF);

        renderString(keyBindingTitle, (width / 2f) - (StringUtils.getStringWidth(keyBindingTitle) / 2f), CraftPresence.GUIS.getButtonY(6, 10), 0xFFFFFF);
        renderString(configKeyBindingTitle, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(7, 5), 0xFFFFFF);

        proceedButton.setControlEnabled(!StringUtils.isNullOrEmpty(languageIdText.getText()));
    }

    @Override
    public void postRender() {
        final String languageIdTitle = ModUtils.TRANSLATOR.translate("gui.config.name.accessibility.language_id");
        final String configKeyBindingTitle = ModUtils.TRANSLATOR.translate("key.craftpresence.config_keycode.name");
        // Hovering over Language Id Label
        if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 130, CraftPresence.GUIS.getButtonY(3, 5), StringUtils.getStringWidth(languageIdTitle), getFontHeight())) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.accessibility.language_id")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
        }

        // Hovering over Config Keybinding Label
        if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 130, CraftPresence.GUIS.getButtonY(7, 5), StringUtils.getStringWidth(configKeyBindingTitle), getFontHeight())) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("key.craftpresence.config_keycode.description")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (entryData != null) {
            setKeyData(keyCode);
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    /**
     * Setup for Key Entry and Save Backup of Prior Setting, if a valid Key Button
     *
     * @param button The Pressed upon KeyCode Button
     */
    private void setupEntryData(final ExtendedButtonControl button) {
        if (entryData == null && button.getOptionalArgs() != null) {
            entryData = new Pair<>(button, button.getOptionalArgs()[0]);

            backupKeyString = button.getControlMessage();
            button.setControlMessage(ModUtils.TRANSLATOR.translate("gui.config.message.editor.enter_key"));
        }
    }

    /**
     * Sets a New KeyCode for the currently queued entry data
     * Entry Data format -> buttonToModify, Config Field to Edit
     *
     * @param keyCode The New KeyCode for modifying
     */
    private void setKeyData(final int keyCode) {
        int keyToSubmit = keyCode;

        // Ensure a Valid KeyCode is entered
        if (!CraftPresence.KEYBINDINGS.isValidKeyCode(keyToSubmit)) {
            keyToSubmit = Keyboard.KEY_NONE;
        }

        final String formattedKey = CraftPresence.KEYBINDINGS.getKeyName(keyToSubmit);

        // If KeyCode Field to modify is not null or empty, attempt to queue change
        try {
            StringUtils.updateField(ConfigUtils.class, CraftPresence.CONFIG, new Tuple<>(entryData.getSecond(), keyToSubmit, null));
            CraftPresence.CONFIG.hasChanged = true;

            entryData.getFirst().setControlMessage(formattedKey);
        } catch (Exception | Error ex) {
            entryData.getFirst().setControlMessage(backupKeyString);
            ex.printStackTrace();
        }

        // Clear Data
        backupKeyString = null;
        entryData = null;
    }
}
