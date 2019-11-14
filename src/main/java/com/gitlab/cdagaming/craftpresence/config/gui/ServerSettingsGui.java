package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAssetUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ServerSettingsGui extends GuiScreen {
    private final GuiScreen parentScreen, currentScreen;
    private ExtendedButtonControl proceedButton, serverMessagesButton, defaultIconButton;
    private GuiTextField defaultMOTD, defaultName, defaultMSG;

    private String defaultServerMSG;

    ServerSettingsGui(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        currentScreen = this;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        defaultServerMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);

        defaultName = new GuiTextField(100, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        defaultName.setText(CraftPresence.CONFIG.defaultServerName);
        defaultMOTD = new GuiTextField(110, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(2), 180, 20);
        defaultMOTD.setText(CraftPresence.CONFIG.defaultServerMOTD);
        defaultMSG = new GuiTextField(120, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(3), 180, 20);
        defaultMSG.setText(defaultServerMSG);

        serverMessagesButton = new ExtendedButtonControl(130, (width / 2) - 90, CraftPresence.GUIS.getButtonY(4), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.name.servermessages.servermessages"));
        defaultIconButton = new ExtendedButtonControl(140, (width / 2) - 90, CraftPresence.GUIS.getButtonY(5), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.name.servermessages.servericon"));
        proceedButton = new ExtendedButtonControl(900, (width / 2) - 90, (height - 30), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"));

        buttonList.add(serverMessagesButton);
        buttonList.add(defaultIconButton);
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.servermessages");
        final String serverNameText = ModUtils.TRANSLATOR.translate("gui.config.name.servermessages.servername");
        final String serverMOTDText = ModUtils.TRANSLATOR.translate("gui.config.name.servermessages.servermotd");
        final String defaultMessageText = ModUtils.TRANSLATOR.translate("gui.config.defaultMessage.server");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringUtils.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);
        drawString(mc.fontRenderer, serverNameText, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        drawString(mc.fontRenderer, serverMOTDText, (width / 2) - 130, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);
        drawString(mc.fontRenderer, defaultMessageText, (width / 2) - 130, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);

        defaultName.drawTextBox();
        defaultMOTD.drawTextBox();
        defaultMSG.drawTextBox();

        proceedButton.enabled = !StringUtils.isNullOrEmpty(defaultMSG.getText()) || !StringUtils.isNullOrEmpty(defaultName.getText()) || !StringUtils.isNullOrEmpty(defaultMOTD.getText());
        serverMessagesButton.enabled = CraftPresence.SERVER.enabled;

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Default Server Name Label
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(serverNameText), mc.fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.servermessages.servername")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        // Hovering over Default Server MOTD Label
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(2) + 5, StringUtils.getStringWidth(serverMOTDText), mc.fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.servermessages.servermotd")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        // Hovering over Default Server Message Label
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(3) + 5, StringUtils.getStringWidth(defaultMessageText), mc.fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.title.servermessages")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, serverMessagesButton)) {
            if (!serverMessagesButton.enabled) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.hoverMessage.access", ModUtils.TRANSLATOR.translate("gui.config.name.servermessages.servermessages"))), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            } else {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.servermessages.servermessages")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, defaultIconButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.servermessages.servericon")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, proceedButton) && !proceedButton.enabled) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.hoverMessage.defaultempty")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
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
                StringUtils.setConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMSG.getText());
            }
            mc.displayGuiScreen(parentScreen);
        } else if (button.id == serverMessagesButton.id) {
            mc.displayGuiScreen(new SelectorGui(currentScreen, CraftPresence.CONFIG.NAME_serverMessages, ModUtils.TRANSLATOR.translate("gui.config.title.selector.server"), CraftPresence.SERVER.knownAddresses, null, null, true));
        } else if (button.id == defaultIconButton.id) {
            mc.displayGuiScreen(new SelectorGui(currentScreen, CraftPresence.CONFIG.NAME_defaultServerIcon, ModUtils.TRANSLATOR.translate("gui.config.title.selector.icon"), DiscordAssetUtils.ICON_LIST, CraftPresence.CONFIG.defaultServerIcon, null, true));
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
