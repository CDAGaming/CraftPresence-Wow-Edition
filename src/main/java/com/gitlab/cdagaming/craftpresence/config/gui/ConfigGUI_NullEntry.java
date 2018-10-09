package com.gitlab.cdagaming.craftpresence.config.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class ConfigGUI_NullEntry extends GuiScreen {
    private final GuiScreen parentscreen;
    private GuiButton backButton;

    ConfigGUI_NullEntry(GuiScreen parentScreen) {
        mc = Minecraft.getMinecraft();
        parentscreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);
        backButton = new GuiButton(700, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 30), 180, 20, "Back");
        buttonList.add(backButton);
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String title = "CraftPresence - Error";
        String[] notice = ("This GUI is not Implemented just yet!\n\n" +
                "Please Check Back Later..").split("\n");
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();
        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);
        for (int i = 0; i < notice.length; i++) {
            String string = notice[i];
            drawString(fontRenderer, string, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(string) / 2), 80 + (i * 10), 0xFFFFFF);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == backButton.id) {
            mc.displayGuiScreen(parentscreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            mc.displayGuiScreen(parentscreen);
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
