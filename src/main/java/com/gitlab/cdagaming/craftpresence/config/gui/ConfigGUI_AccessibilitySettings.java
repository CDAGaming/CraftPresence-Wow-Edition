package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUIExtendedButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigGUI_AccessibilitySettings extends GuiScreen {
    private final GuiScreen parentScreen, currentScreen;
    private boolean entryMode = false;
    private String currentKeyCode;
    private GuiTextField languageIDText;
    private GUIExtendedButton proceedButton, tooltipBGButton, tooltipBorderButton, guiBGButton, configKeyBindingButton;

    ConfigGUI_AccessibilitySettings(GuiScreen parentScreen) {
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

        tooltipBGButton = new GUIExtendedButton(100, calc1, CraftPresence.GUIS.getButtonY(1), 180, 20, CraftPresence.CONFIG.NAME_tooltipBGColor.replaceAll("_", " "));
        tooltipBorderButton = new GUIExtendedButton(200, calc2, CraftPresence.GUIS.getButtonY(1), 180, 20, CraftPresence.CONFIG.NAME_tooltipBorderColor.replaceAll("_", " "));
        guiBGButton = new GUIExtendedButton(300, (width / 2) - 90, CraftPresence.GUIS.getButtonY(2), 180, 20, CraftPresence.CONFIG.NAME_guiBGColor.replaceAll("_", " "));

        languageIDText = new GuiTextField(400, mc.fontRenderer, calc2, CraftPresence.GUIS.getButtonY(3), 180, 20);
        languageIDText.setText(CraftPresence.CONFIG.languageID);

        configKeyBindingButton = new GUIExtendedButton(500, calc2 + 50, CraftPresence.GUIS.getButtonY(5), 90, 20, !StringHandler.isNullOrEmpty(Keyboard.getKeyName(Integer.parseInt(currentKeyCode))) ? Keyboard.getKeyName(Integer.parseInt(currentKeyCode)) : currentKeyCode);

        proceedButton = new GUIExtendedButton(700, (width / 2) - 90, (height - 30), 180, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.back"));

        buttonList.add(tooltipBGButton);
        buttonList.add(tooltipBorderButton);
        buttonList.add(guiBGButton);
        buttonList.add(configKeyBindingButton);
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

        final String mainTitle = Constants.TRANSLATOR.translate("gui.config.title");
        final String subTitle = Constants.TRANSLATOR.translate("gui.config.title.accessibility");

        final String languageIDTitle = Constants.TRANSLATOR.translate("gui.config.name.accessibility.languageid");

        final String keyBindingTitle = Constants.TRANSLATOR.translate("key.craftpresence.category");
        final String configKeyBindingTitle = Constants.TRANSLATOR.translate("key.craftpresence.config_keybind");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringHandler.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringHandler.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);

        drawString(mc.fontRenderer, languageIDTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);

        drawString(mc.fontRenderer, keyBindingTitle, (width / 2) - (StringHandler.getStringWidth(keyBindingTitle) / 2), CraftPresence.GUIS.getButtonY(4) + 10, 0xFFFFFF);
        drawString(mc.fontRenderer, configKeyBindingTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(5) + 5, 0xFFFFFF);

        languageIDText.drawTextBox();

        proceedButton.enabled = !StringHandler.isNullOrEmpty(languageIDText.getText());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == tooltipBGButton.id) {
            mc.displayGuiScreen(new ConfigGUI_ColorEditor(currentScreen, CraftPresence.CONFIG.NAME_tooltipBGColor));
        } else if (button.id == tooltipBorderButton.id) {
            mc.displayGuiScreen(new ConfigGUI_ColorEditor(currentScreen, CraftPresence.CONFIG.NAME_tooltipBorderColor));
        } else if (button.id == guiBGButton.id) {
            mc.displayGuiScreen(new ConfigGUI_ColorEditor(currentScreen, CraftPresence.CONFIG.NAME_guiBGColor));
        } else if (button.id == configKeyBindingButton.id && !entryMode) {
            configKeyBindingButton.displayString = Constants.TRANSLATOR.translate("gui.config.editorMessage.enterkey");
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
            mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (entryMode) {
            configKeyBindingButton.displayString = !StringHandler.isNullOrEmpty(Keyboard.getKeyName(keyCode)) ? Keyboard.getKeyName(keyCode) : Integer.toString(keyCode);
            currentKeyCode = Integer.toString(keyCode);
            entryMode = false;
        } else if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parentScreen);
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
