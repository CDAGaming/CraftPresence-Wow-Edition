package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUIExtendedButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.net.URI;
import java.util.List;

public class ConfigGUI_About extends GuiScreen {
    private static final String SOURCE_URL = "https://gitlab.com/CDAGaming/CraftPresence";
    private final GuiScreen parentScreen;
    private GUIExtendedButton viewSource, backButton;

    ConfigGUI_About(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);

        backButton = new GUIExtendedButton(700, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 30), 180, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.back"));
        viewSource = new GUIExtendedButton(810, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 55), 180, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.viewsource"));

        buttonList.add(backButton);
        buttonList.add(viewSource);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();

        final String title = Constants.TRANSLATOR.translate("gui.config.title.about.config");
        final List<String> notice = StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.message.credits"));

        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);
        if (notice != null && !notice.isEmpty()) {
            for (int i = 0; i < notice.size(); i++) {
                final String string = notice.get(i);
                drawString(fontRenderer, string, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(string) / 2), 80 + (i * 10), 0xFFFFFF);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == backButton.id) {
            mc.displayGuiScreen(parentScreen);
        } else if (button.id == viewSource.id) {
            try {
                Desktop.getDesktop().browse(new URI(SOURCE_URL));
            } catch (Exception ex) {
                Constants.LOG.error(Constants.TRANSLATOR.translate("craftpresence.logger.error.web", SOURCE_URL));
                ex.printStackTrace();
            }
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
