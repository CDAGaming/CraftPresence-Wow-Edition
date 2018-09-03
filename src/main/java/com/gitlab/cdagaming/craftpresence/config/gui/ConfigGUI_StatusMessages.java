package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigGUI_StatusMessages extends GuiScreen {
    private final GuiScreen parentscreen;
    private GuiButton proceedButton;
    private GuiTextField mainMenuMSG, singleplayerMSG;

    ConfigGUI_StatusMessages(GuiScreen parentScreen) {
        mc = Minecraft.getMinecraft();
        parentscreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);

        mainMenuMSG = new GuiTextField(110, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        singleplayerMSG = new GuiTextField(120, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(2), 180, 20);

        mainMenuMSG.setText(CraftPresence.CONFIG.mainmenuMSG);
        singleplayerMSG.setText(CraftPresence.CONFIG.singleplayerMSG);

        proceedButton = new GuiButton(900, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 30), 180, 20, "Back");
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String title = "CraftPresence - " + I18n.format("gui.config.title.statusmessages");
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();
        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);
        drawString(fontRenderer, I18n.format("gui.config.name.statusmessages.mainmenumsg"), (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        drawString(fontRenderer, I18n.format("gui.config.name.statusmessages.singleplayermsg"), (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);
        mainMenuMSG.drawTextBox();
        singleplayerMSG.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        // Back button
        if (button.id == proceedButton.id) {
            mc.displayGuiScreen(parentscreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        mainMenuMSG.textboxKeyTyped(typedChar, keyCode);
        singleplayerMSG.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mainMenuMSG.mouseClicked(mouseX, mouseY, mouseButton);
        singleplayerMSG.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        mainMenuMSG.updateCursorCounter();
        singleplayerMSG.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);

        if (!mainMenuMSG.getText().equals(CraftPresence.CONFIG.mainmenuMSG)) {
            CraftPresence.CONFIG.hasChanged = true;
            CraftPresence.CONFIG.hasClientPropertiesChanged = true;
            CraftPresence.CONFIG.mainmenuMSG = mainMenuMSG.getText();
        }

        if (!singleplayerMSG.getText().equals(CraftPresence.CONFIG.singleplayerMSG)) {
            CraftPresence.CONFIG.hasChanged = true;
            CraftPresence.CONFIG.hasClientPropertiesChanged = true;
            CraftPresence.CONFIG.singleplayerMSG = singleplayerMSG.getText();
        }
    }
}
