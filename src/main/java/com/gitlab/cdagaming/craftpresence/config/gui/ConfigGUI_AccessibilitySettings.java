package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUIExtendedButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class ConfigGUI_AccessibilitySettings extends GuiScreen {
    private final GuiScreen parentScreen, currentScreen;
    private GUIExtendedButton backButton, tooltipBGButton, tooltipBorderButton, guiBGButton;

    ConfigGUI_AccessibilitySettings(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        currentScreen = this;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        int calc1 = (width / 2) - 183;
        int calc2 = (width / 2) + 3;

        tooltipBGButton = new GUIExtendedButton(100, calc1, CraftPresence.GUIS.getButtonY(1), 180, 20, CraftPresence.CONFIG.NAME_tooltipBGColor.replaceAll("_", " "));
        tooltipBorderButton = new GUIExtendedButton(200, calc2, CraftPresence.GUIS.getButtonY(1), 180, 20, CraftPresence.CONFIG.NAME_tooltipBorderColor.replaceAll("_", " "));
        guiBGButton = new GUIExtendedButton(300, (width / 2) - 90, CraftPresence.GUIS.getButtonY(2), 180, 20, CraftPresence.CONFIG.NAME_guiBGColor.replaceAll("_", " "));

        backButton = new GUIExtendedButton(700, (width / 2) - 90, (height - 30), 180, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.back"));

        buttonList.add(tooltipBGButton);
        buttonList.add(tooltipBorderButton);
        buttonList.add(guiBGButton);
        buttonList.add(backButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

        final String mainTitle = Constants.TRANSLATOR.translate("gui.config.title");
        final String subTitle = Constants.TRANSLATOR.translate("gui.config.title.accessibility");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringHandler.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringHandler.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);

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
        } else if (button.id == backButton.id) {
            mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
