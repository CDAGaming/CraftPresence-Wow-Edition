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
    public void initGui() {
        final int calc1 = (width / 2) - 183;
        final int calc2 = (width / 2) + 3;

        // Adding Tooltip Background Button
        addControl(
                new ExtendedButtonControl(
                        calc1, CraftPresence.GUIS.getButtonY(1),
                        180, 20,
                        CraftPresence.CONFIG.NAME_tooltipBGColor.replaceAll("_", " "),
                        () -> CraftPresence.GUIS.openScreen(new ColorEditorGui(currentScreen, CraftPresence.CONFIG.NAME_tooltipBGColor))
                )
        );
        // Adding Tooltip Border Color Button
        addControl(
                new ExtendedButtonControl(
                        calc2, CraftPresence.GUIS.getButtonY(1),
                        180, 20,
                        CraftPresence.CONFIG.NAME_tooltipBorderColor.replaceAll("_", " "),
                        () -> CraftPresence.GUIS.openScreen(new ColorEditorGui(currentScreen, CraftPresence.CONFIG.NAME_tooltipBorderColor))
                )
        );
        // Adding Gui Background Color Button
        addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, CraftPresence.GUIS.getButtonY(2),
                        180, 20,
                        CraftPresence.CONFIG.NAME_guiBGColor.replaceAll("_", " "),
                        () -> CraftPresence.GUIS.openScreen(new ColorEditorGui(currentScreen, CraftPresence.CONFIG.NAME_guiBGColor))
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
                        CraftPresence.CONFIG.stripTranslationColors
                )
        );
        showLoggingInChatButton = addControl(
                new CheckBoxControl(
                        calc2, CraftPresence.GUIS.getButtonY(4) + 10,
                        ModUtils.TRANSLATOR.translate("gui.config.name.accessibility.showlogginginchat"),
                        CraftPresence.CONFIG.showLoggingInChat
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
                        ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"),
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

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        preDraw();

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.accessibility");

        final String languageIDTitle = ModUtils.TRANSLATOR.translate("gui.config.name.accessibility.languageid");

        final String keyBindingTitle = ModUtils.TRANSLATOR.translate("key.craftpresence.category");
        final String configKeyBindingTitle = ModUtils.TRANSLATOR.translate("key.craftpresence.config_keybind");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringUtils.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);

        drawString(mc.fontRenderer, languageIDTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);

        drawString(mc.fontRenderer, keyBindingTitle, (width / 2) - (StringUtils.getStringWidth(keyBindingTitle) / 2), CraftPresence.GUIS.getButtonY(5) + 10, 0xFFFFFF);
        drawString(mc.fontRenderer, configKeyBindingTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(6) + 5, 0xFFFFFF);

        proceedButton.enabled = !StringUtils.isNullOrEmpty(languageIDText.getText());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (entryData != null) {
            setKeyData(keyCode);
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    private void setupEntryData(final ExtendedButtonControl button) {
        if (entryData == null) {
            // Setup for Key Entry and Save Backup of Prior Setting, if a valid Key Button
            if (button.getOptionalArgs() != null) {
                entryData = new Tuple<>(button, button.getOptionalArgs()[0]);

                backupKeyString = button.displayString;
                button.displayString = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.enterkey");
            }
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
