package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigGUI_ServerSettings extends GuiScreen {
    private final GuiScreen parentScreen, currentScreen;
    private GuiButton proceedButton, serverMessagesButton, defaultIconButton;
    private GuiTextField defaultMOTD, defaultName, defaultMSG;

    private String defaultServerMSG;

    ConfigGUI_ServerSettings(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        currentScreen = this;
        this.parentScreen = parentScreen;
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
        proceedButton = new GuiButton(900, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 30), 180, 20, I18n.format("gui.config.buttonMessage.back"));

        buttonList.add(serverMessagesButton);
        buttonList.add(defaultIconButton);
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();

        final String title = "CraftPresence - " + I18n.format("gui.config.title.servermessages");
        final String serverNameText = I18n.format("gui.config.name.servermessages.servername");
        final String serverMOTDText = I18n.format("gui.config.name.servermessages.servermotd");
        final String defaultMessageText = I18n.format("gui.config.defaultMessage.server");

        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);
        drawString(fontRenderer, serverNameText, (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        drawString(fontRenderer, serverMOTDText, (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);
        drawString(fontRenderer, defaultMessageText, (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);

        defaultName.drawTextBox();
        defaultMOTD.drawTextBox();
        defaultMSG.drawTextBox();

        proceedButton.enabled = !StringHandler.isNullOrEmpty(defaultMSG.getText()) || !StringHandler.isNullOrEmpty(defaultName.getText()) || !StringHandler.isNullOrEmpty(defaultMOTD.getText());
        serverMessagesButton.enabled = CraftPresence.CONFIG.showGameState;

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Default Server Name Label
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, fontRenderer.getStringWidth(serverNameText), fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.servermessages.servername")), mouseX, mouseY, width, height, -1, fontRenderer);
        }
        // Hovering over Default Server MOTD Label
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(2) + 5, fontRenderer.getStringWidth(serverMOTDText), fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.servermessages.servermotd")), mouseX, mouseY, width, height, -1, fontRenderer);
        }
        // Hovering over Default Server Message Label
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(3) + 5, fontRenderer.getStringWidth(defaultMessageText), fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.title.servermessages")), mouseX, mouseY, width, height, -1, fontRenderer);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, serverMessagesButton)) {
            if (!serverMessagesButton.enabled) {
                CraftPresence.GUIS.drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.servermessages.servermessages"))), mouseX, mouseY, width, height, -1, fontRenderer);
            } else {
                CraftPresence.GUIS.drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.servermessages.servermessages")), mouseX, mouseY, width, height, -1, fontRenderer);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, defaultIconButton)) {
            CraftPresence.GUIS.drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.servermessages.servericon")), mouseX, mouseY, width, height, -1, fontRenderer);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, proceedButton) && !proceedButton.enabled) {
            CraftPresence.GUIS.drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.hoverMessage.defaultempty")), mouseX, mouseY, width, height, -1, fontRenderer);
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
            mc.displayGuiScreen(parentScreen);
        } else if (button.id == serverMessagesButton.id) {
            mc.displayGuiScreen(new ConfigGUI_Selector(currentScreen, CraftPresence.CONFIG.NAME_serverMessages, I18n.format("gui.config.title.selector.server"), CraftPresence.SERVER.knownAddresses, null, null));
        } else if (button.id == defaultIconButton.id) {
            mc.displayGuiScreen(new ConfigGUI_Selector(currentScreen, CraftPresence.CONFIG.NAME_defaultServerIcon, I18n.format("gui.config.title.selector.icon"), DiscordAssetHandler.ICON_LIST, CraftPresence.CONFIG.defaultServerIcon, null));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parentScreen);
        }
        defaultName.textboxKeyTyped(typedChar, keyCode);
        defaultMOTD.textboxKeyTyped(typedChar, keyCode);
        defaultMSG.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        defaultName.mouseClicked(mouseX, mouseY, mouseButton);
        defaultMOTD.mouseClicked(mouseX, mouseY, mouseButton);
        defaultMSG.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
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
