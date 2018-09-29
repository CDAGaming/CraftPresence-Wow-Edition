package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigGUI_ServerSettings extends GuiScreen {
    private final GuiScreen parentscreen;
    private GuiButton proceedButton, serverMessagesButton, defaultIconButton;
    private GuiTextField defaultMOTD, defaultName, defaultMSG;

    private String defaultServerMSG;

    ConfigGUI_ServerSettings(GuiScreen parentScreen) {
        mc = Minecraft.getMinecraft();
        parentscreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);
        defaultServerMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);

        defaultName = new GuiTextField(100, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        defaultName.setText(CraftPresence.CONFIG.defaultServerName);
        defaultMOTD = new GuiTextField(110, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(2), 180, 20);
        defaultMOTD.setText(CraftPresence.CONFIG.defaultServerMOTD);
        defaultMSG = new GuiTextField(120, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(3), 180, 20);
        defaultMSG.setText(defaultServerMSG);

        serverMessagesButton = new GuiButton(130, (sr.getScaledWidth() / 2) - 90, CraftPresence.GUIS.getButtonY(4), 180, 20, I18n.format("gui.config.name.servermessages.servermessages"));
        defaultIconButton = new GuiButton(140, (sr.getScaledWidth() / 2) - 90, CraftPresence.GUIS.getButtonY(5), 180, 20, I18n.format("gui.config.name.servermessages.servericon"));
        proceedButton = new GuiButton(900, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 30), 180, 20, "Back");

        buttonList.add(serverMessagesButton);
        buttonList.add(defaultIconButton);
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String title = "CraftPresence - " + I18n.format("gui.config.title.servermessages");
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();

        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);
        drawString(fontRenderer, I18n.format("gui.config.name.servermessages.servername"), (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        drawString(fontRenderer, I18n.format("gui.config.name.servermessages.servermotd"), (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);
        drawString(fontRenderer, "Default Server Message", (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);

        defaultName.drawTextBox();
        defaultMOTD.drawTextBox();
        defaultMSG.drawTextBox();

        proceedButton.enabled = !StringHandler.isNullOrEmpty(defaultMSG.getText()) || !StringHandler.isNullOrEmpty(defaultName.getText()) || !StringHandler.isNullOrEmpty(defaultMOTD.getText());
        serverMessagesButton.enabled = CraftPresence.CONFIG.showGameState;

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (serverMessagesButton.isMouseOver()) {
            if (!serverMessagesButton.enabled) {
                drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.servermessages.servermessages")).split("\n")), mouseX, mouseY);
            } else {
                drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.servermessages.servermessages").split("\n")), mouseX, mouseY);
            }
        }
        if (defaultIconButton.isMouseOver()) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.servermessages.servericon").split("\n")), mouseX, mouseY);
        }
        if (proceedButton.isMouseOver() && !proceedButton.enabled) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.hoverMessage.defaultempty").split("\n")), mouseX, mouseY);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == proceedButton.id) {
            if (!defaultName.getText().equals(CraftPresence.CONFIG.defaultServerName)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.defaultServerName = defaultName.getText();
            }
            if (!defaultMOTD.getText().equals(CraftPresence.CONFIG.defaultServerMOTD)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.defaultServerMOTD = defaultMOTD.getText();
            }
            if (!defaultMSG.getText().equals(defaultServerMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                StringHandler.setConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMSG.getText());
            }
            mc.displayGuiScreen(parentscreen);
        } else if (button.id == serverMessagesButton.id) {
            mc.displayGuiScreen(new ConfigGUI_Selector(this, CraftPresence.CONFIG.NAME_serverMessages, "CraftPresence - Select Server IP", CraftPresence.SERVER.knownAddresses, null));
        } else if (button.id == defaultIconButton.id) {
            mc.displayGuiScreen(new ConfigGUI_Selector(this, CraftPresence.CONFIG.NAME_defaultServerIcon, "CraftPresence - Select an Icon", DiscordAssetHandler.ICON_LIST, null));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        defaultName.textboxKeyTyped(typedChar, keyCode);
        defaultMOTD.textboxKeyTyped(typedChar, keyCode);
        defaultMSG.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        defaultName.mouseClicked(mouseX, mouseY, mouseButton);
        defaultMOTD.mouseClicked(mouseX, mouseY, mouseButton);
        defaultMSG.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        defaultName.updateCursorCounter();
        defaultMOTD.updateCursorCounter();
        defaultMSG.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
