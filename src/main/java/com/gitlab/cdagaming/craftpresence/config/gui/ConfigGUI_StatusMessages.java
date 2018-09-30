package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
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
    private GuiTextField mainMenuMSG, singleplayerMSG, loadingMSG;

    ConfigGUI_StatusMessages(GuiScreen parentScreen) {
        mc = Minecraft.getMinecraft();
        parentscreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);

        mainMenuMSG = new GuiTextField(110, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 15);
        singleplayerMSG = new GuiTextField(120, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(2), 180, 15);
        loadingMSG = new GuiTextField(130, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(3), 180, 15);

        mainMenuMSG.setText(CraftPresence.CONFIG.mainmenuMSG);
        singleplayerMSG.setText(CraftPresence.CONFIG.singleplayerMSG);
        loadingMSG.setText(CraftPresence.CONFIG.loadingMSG);

        proceedButton = new GuiButton(900, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 30), 180, 20, "Back");
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String title = "CraftPresence - " + I18n.format("gui.config.title.statusmessages");
        String mainMenuText = I18n.format("gui.config.name.statusmessages.mainmenumsg");
        String singlePlayerText = I18n.format("gui.config.name.statusmessages.singleplayermsg");
        String loadingText = I18n.format("gui.config.name.statusmessages.loadingmsg");
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();
        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);
        drawString(fontRenderer, mainMenuText, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(1) + 2, 0xFFFFFF);
        drawString(fontRenderer, singlePlayerText, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(2) + 2, 0xFFFFFF);
        drawString(fontRenderer, loadingText, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(3) + 2, 0xFFFFFF);
        mainMenuMSG.drawTextBox();
        singleplayerMSG.drawTextBox();
        loadingMSG.drawTextBox();

        proceedButton.enabled = !StringHandler.isNullOrEmpty(mainMenuMSG.getText()) || !StringHandler.isNullOrEmpty(singleplayerMSG.getText()) || !StringHandler.isNullOrEmpty(loadingMSG.getText());

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Main Menu Message Label
        if (CraftPresence.GUIS.isMouseOverElement(mouseX, mouseY, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(1) + 2, fontRenderer.getStringWidth(mainMenuText), 20)) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.statusmessages.mainmenumsg").split("\n")), mouseX, mouseY);
        }
        // Hovering over Single Player Message Label
        if (CraftPresence.GUIS.isMouseOverElement(mouseX, mouseY, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(2) + 2, fontRenderer.getStringWidth(singlePlayerText), 20)) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.statusmessages.singleplayermsg").split("\n")), mouseX, mouseY);
        }
        // Hovering over Loading Message Label
        if (CraftPresence.GUIS.isMouseOverElement(mouseX, mouseY, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(3) + 2, fontRenderer.getStringWidth(loadingText), 20)) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.statusmessages.loadingmsg").split("\n")), mouseX, mouseY);
        }
        if (proceedButton.isMouseOver() && !proceedButton.enabled) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.hoverMessage.defaultempty").split("\n")), mouseX, mouseY);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == proceedButton.id) {
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
            if (!loadingMSG.getText().equals(CraftPresence.CONFIG.loadingMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.loadingMSG = loadingMSG.getText();
            }
            mc.displayGuiScreen(parentscreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        mainMenuMSG.textboxKeyTyped(typedChar, keyCode);
        singleplayerMSG.textboxKeyTyped(typedChar, keyCode);
        loadingMSG.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mainMenuMSG.mouseClicked(mouseX, mouseY, mouseButton);
        singleplayerMSG.mouseClicked(mouseX, mouseY, mouseButton);
        loadingMSG.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        mainMenuMSG.updateCursorCounter();
        singleplayerMSG.updateCursorCounter();
        loadingMSG.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
