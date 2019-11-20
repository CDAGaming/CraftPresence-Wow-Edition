package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.CheckBoxControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class AccessibilitySettingsGui extends GuiScreen {
    private final GuiScreen parentScreen, currentScreen;
    private boolean entryMode = false;
    private String currentKeyCode;
    private GuiTextField languageIDText;
    private CheckBoxControl stripTranslationColorsButton, showLoggingInChatButton;
    private ExtendedButtonControl proceedButton, tooltipBGButton, tooltipBorderButton, guiBGButton, configKeyBindingButton;

    AccessibilitySettingsGui(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        currentScreen = this;
        this.parentScreen = parentScreen;

        currentKeyCode = CraftPresence.CONFIG.configKeyCode;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        int calc1 = (width / 2) - 183;
        int calc2 = (width / 2) + 3;

        tooltipBGButton = new ExtendedButtonControl(100, calc1, CraftPresence.GUIS.getButtonY(1), 180, 20, CraftPresence.CONFIG.NAME_tooltipBGColor.replaceAll("_", " "));
        tooltipBorderButton = new ExtendedButtonControl(200, calc2, CraftPresence.GUIS.getButtonY(1), 180, 20, CraftPresence.CONFIG.NAME_tooltipBorderColor.replaceAll("_", " "));
        guiBGButton = new ExtendedButtonControl(300, (width / 2) - 90, CraftPresence.GUIS.getButtonY(2), 180, 20, CraftPresence.CONFIG.NAME_guiBGColor.replaceAll("_", " "));

        languageIDText = new GuiTextField(400, mc.fontRenderer, calc2, CraftPresence.GUIS.getButtonY(3), 180, 20);
        languageIDText.setText(CraftPresence.CONFIG.languageID);

        stripTranslationColorsButton = new CheckBoxControl(500, calc1, CraftPresence.GUIS.getButtonY(4) + 10, ModUtils.TRANSLATOR.translate("gui.config.name.accessibility.striptranslationcolors"), CraftPresence.CONFIG.stripTranslationColors);

        showLoggingInChatButton = new CheckBoxControl(600, calc2, CraftPresence.GUIS.getButtonY(4) + 10, ModUtils.TRANSLATOR.translate("gui.config.name.accessibility.showlogginginchat"), CraftPresence.CONFIG.showLoggingInChat);

        configKeyBindingButton = new ExtendedButtonControl(700, calc2 + 50, CraftPresence.GUIS.getButtonY(6), 90, 20, !StringUtils.isNullOrEmpty(Keyboard.getKeyName(Integer.parseInt(currentKeyCode))) ? Keyboard.getKeyName(Integer.parseInt(currentKeyCode)) : currentKeyCode);

        proceedButton = new ExtendedButtonControl(800, (width / 2) - 90, (height - 30), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"));

        buttonList.add(tooltipBGButton);
        buttonList.add(tooltipBorderButton);
        buttonList.add(guiBGButton);
        buttonList.add(stripTranslationColorsButton);
        buttonList.add(showLoggingInChatButton);
        buttonList.add(configKeyBindingButton);
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

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

        languageIDText.drawTextBox();

        proceedButton.enabled = !StringUtils.isNullOrEmpty(languageIDText.getText());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == tooltipBGButton.id) {
            CraftPresence.GUIS.openScreen(new ColorEditorGui(currentScreen, CraftPresence.CONFIG.NAME_tooltipBGColor));
        } else if (button.id == tooltipBorderButton.id) {
            CraftPresence.GUIS.openScreen(new ColorEditorGui(currentScreen, CraftPresence.CONFIG.NAME_tooltipBorderColor));
        } else if (button.id == guiBGButton.id) {
            CraftPresence.GUIS.openScreen(new ColorEditorGui(currentScreen, CraftPresence.CONFIG.NAME_guiBGColor));
        } else if (button.id == configKeyBindingButton.id && !entryMode) {
            configKeyBindingButton.displayString = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.enterkey");
            entryMode = true;
        } else if (button.id == proceedButton.id && !entryMode) {
            if (!currentKeyCode.equals(CraftPresence.CONFIG.configKeyCode)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.configKeyCode = currentKeyCode;
            }
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

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (entryMode) {
            int keyToSubmit = keyCode;
            if (!CraftPresence.KEYBINDINGS.isValidKeyCode(keyToSubmit)) {
                keyToSubmit = Keyboard.KEY_NONE;
            }

            configKeyBindingButton.displayString = !StringUtils.isNullOrEmpty(Keyboard.getKeyName(keyToSubmit)) ? Keyboard.getKeyName(keyToSubmit) : Integer.toString(keyToSubmit);
            currentKeyCode = Integer.toString(keyToSubmit);
            entryMode = false;
        } else if (keyCode == Keyboard.KEY_ESCAPE) {
            CraftPresence.GUIS.openScreen(parentScreen);
        }
        languageIDText.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        languageIDText.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        languageIDText.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
