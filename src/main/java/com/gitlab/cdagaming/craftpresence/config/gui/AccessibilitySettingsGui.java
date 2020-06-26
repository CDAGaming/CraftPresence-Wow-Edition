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
package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.config.ConfigUtils;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.CheckBoxControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class AccessibilitySettingsGui extends ExtendedScreen {

    // Tuple Format: buttonToModify, Config Field to Edit
    // (Store a Backup of Prior Text just in case)
    private String backupKeyString;
    private Tuple<ExtendedButtonControl, String> entryData = null;

    private ExtendedTextControl languageIDText;
    private CheckBoxControl stripTranslationColorsButton, showLoggingInChatButton;
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
                        CraftPresence.CONFIG.NAME_tooltipBGColor.replaceAll("_", " "),
                        () -> CraftPresence.GUIS.openScreen(new ColorEditorGui(currentScreen, CraftPresence.CONFIG.NAME_tooltipBGColor)),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.accessibility.tooltipbgcolor")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
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
                        () -> CraftPresence.GUIS.openScreen(new ColorEditorGui(currentScreen, CraftPresence.CONFIG.NAME_tooltipBorderColor)),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.accessibility.tooltipbordercolor")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );
        // Adding Gui Background Color Button
        addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, CraftPresence.GUIS.getButtonY(2),
                        180, 20,
                        CraftPresence.CONFIG.NAME_guiBGColor.replaceAll("_", " "),
                        () -> CraftPresence.GUIS.openScreen(new ColorEditorGui(currentScreen, CraftPresence.CONFIG.NAME_guiBGColor)),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.accessibility.guibgcolor")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );

        languageIDText = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        calc2, CraftPresence.GUIS.getButtonY(3),
                        180, 20
                )
        );
        languageIDText.setText(CraftPresence.CONFIG.languageID);

        stripTranslationColorsButton = addControl(
                new CheckBoxControl(
                        calc1, CraftPresence.GUIS.getButtonY(4) + 10,
                        ModUtils.TRANSLATOR.translate("gui.config.name.accessibility.striptranslationcolors"),
                        CraftPresence.CONFIG.stripTranslationColors,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.accessibility.striptranslationcolors")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );
        showLoggingInChatButton = addControl(
                new CheckBoxControl(
                        calc2, CraftPresence.GUIS.getButtonY(4) + 10,
                        ModUtils.TRANSLATOR.translate("gui.config.name.accessibility.showlogginginchat"),
                        CraftPresence.CONFIG.showLoggingInChat,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.accessibility.showlogginginchat")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );

        // KeyCode Buttons
        final ExtendedButtonControl configKeyCodeButton = addControl(
                new ExtendedButtonControl(
                        calc2 + 50, CraftPresence.GUIS.getButtonY(6),
                        90, 20,
                        CraftPresence.KEYBINDINGS.getKeyName(CraftPresence.CONFIG.configKeyCode),
                        "configKeyCode"
                )
        );
        configKeyCodeButton.setOnClick(() -> setupEntryData(configKeyCodeButton));

        proceedButton = addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, (height - 30),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.back"),
                        () -> {
                            if (entryData == null) {
                                if (!languageIDText.getText().equals(CraftPresence.CONFIG.languageID)) {
                                    CraftPresence.CONFIG.hasChanged = true;
                                    CraftPresence.CONFIG.languageID = languageIDText.getText();
                                }
                                if (stripTranslationColorsButton.isChecked() != CraftPresence.CONFIG.stripTranslationColors) {
                                    CraftPresence.CONFIG.hasChanged = true;
                                    CraftPresence.CONFIG.stripTranslationColors = stripTranslationColorsButton.isChecked();
                                }
                                if (showLoggingInChatButton.isChecked() != CraftPresence.CONFIG.showLoggingInChat) {
                                    CraftPresence.CONFIG.hasChanged = true;
                                    CraftPresence.CONFIG.showLoggingInChat = showLoggingInChatButton.isChecked();
                                }
                                CraftPresence.GUIS.openScreen(parentScreen);
                            }
                        }
                )
        );

        super.initializeUi();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        preDraw();

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.accessibility");

        final String languageIDTitle = ModUtils.TRANSLATOR.translate("gui.config.name.accessibility.languageid");

        final String keyBindingTitle = ModUtils.TRANSLATOR.translate("key.craftpresence.category");
        final String configKeyBindingTitle = ModUtils.TRANSLATOR.translate("key.craftpresence.config_keybind.name");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringUtils.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);

        drawString(mc.fontRenderer, languageIDTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);

        drawString(mc.fontRenderer, keyBindingTitle, (width / 2) - (StringUtils.getStringWidth(keyBindingTitle) / 2), CraftPresence.GUIS.getButtonY(5) + 10, 0xFFFFFF);
        drawString(mc.fontRenderer, configKeyBindingTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(6) + 5, 0xFFFFFF);

        proceedButton.enabled = !StringUtils.isNullOrEmpty(languageIDText.getText());

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Language Id Label
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(3) + 5, StringUtils.getStringWidth(languageIDTitle), mc.fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.accessibility.languageid")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }

        // Hovering over Config Keybinding Label
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(6) + 5, StringUtils.getStringWidth(configKeyBindingTitle), mc.fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("key.craftpresence.config_keybind.description")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
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
            entryData = new Tuple<>(button, button.getOptionalArgs()[0]);

            backupKeyString = button.displayString;
            button.displayString = ModUtils.TRANSLATOR.translate("gui.config.message.editor.enterkey");
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

        final String parsedKey = Integer.toString(keyToSubmit);
        final String formattedKey = CraftPresence.KEYBINDINGS.getKeyName(parsedKey);

        // If KeyCode Field to modify is not null or empty, attempt to queue change
        try {
            StringUtils.updateField(ConfigUtils.class, CraftPresence.CONFIG, new Tuple<>(entryData.getSecond(), parsedKey));
            CraftPresence.CONFIG.hasChanged = true;

            entryData.getFirst().displayString = formattedKey;
        } catch (Exception | Error ex) {
            entryData.getFirst().displayString = backupKeyString;
            ex.printStackTrace();
        }

        // Clear Data
        backupKeyString = null;
        entryData = null;
    }
}
