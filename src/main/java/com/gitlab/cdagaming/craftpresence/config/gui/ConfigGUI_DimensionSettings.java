package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUIExtendedButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigGUI_DimensionSettings extends GuiScreen {
    private final GuiScreen parentScreen, currentScreen;
    private GUIExtendedButton proceedButton, dimensionMessagesButton, defaultIconButton;
    private GuiTextField defaultMessage;

    private String defaultDimensionMSG;

    ConfigGUI_DimensionSettings(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        currentScreen = this;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        defaultDimensionMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);

        defaultMessage = new GuiTextField(110, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        defaultMessage.setText(defaultDimensionMSG);

        dimensionMessagesButton = new GUIExtendedButton(100, (width / 2) - 90, CraftPresence.GUIS.getButtonY(2), 180, 20, Constants.TRANSLATOR.translate("gui.config.name.dimensionmessages.dimensionmessages"));
        defaultIconButton = new GUIExtendedButton(110, (width / 2) - 90, CraftPresence.GUIS.getButtonY(3), 180, 20, Constants.TRANSLATOR.translate("gui.config.name.dimensionmessages.dimensionicon"));
        proceedButton = new GUIExtendedButton(900, (width / 2) - 90, (height - 30), 180, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.back"));

        buttonList.add(dimensionMessagesButton);
        buttonList.add(defaultIconButton);
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        final String mainTitle = Constants.TRANSLATOR.translate("gui.config.title");
        final String subTitle = Constants.TRANSLATOR.translate("gui.config.title.dimensionmessages");
        final String defaultMessageText = Constants.TRANSLATOR.translate("gui.config.defaultMessage.dimension");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringHandler.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringHandler.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);
        drawString(mc.fontRenderer, defaultMessageText, (width / 2) - 140, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        defaultMessage.drawTextBox();

        proceedButton.enabled = !StringHandler.isNullOrEmpty(defaultMessage.getText());
        dimensionMessagesButton.enabled = CraftPresence.CONFIG.showCurrentDimension;

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Default Dimension Message Label
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 140, CraftPresence.GUIS.getButtonY(1) + 5, StringHandler.getStringWidth(defaultMessageText), mc.fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.title.dimensionmessages")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, dimensionMessagesButton)) {
            if (!dimensionMessagesButton.enabled) {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.hoverMessage.access", Constants.TRANSLATOR.translate("gui.config.name.dimensionmessages.dimensionmessages"))), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            } else {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.dimensionmessages.dimensionmessages")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, defaultIconButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.dimensionmessages.dimensionicon")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, proceedButton) && !proceedButton.enabled) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.hoverMessage.defaultempty")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == proceedButton.id) {
            if (!defaultMessage.getText().equals(defaultDimensionMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                StringHandler.setConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMessage.getText());
            }
            mc.displayGuiScreen(parentScreen);
        } else if (button.id == dimensionMessagesButton.id) {
            mc.displayGuiScreen(new ConfigGUI_Selector(currentScreen, CraftPresence.CONFIG.NAME_dimensionMessages, Constants.TRANSLATOR.translate("gui.config.title.selector.dimension"), CraftPresence.DIMENSIONS.DIMENSION_NAMES, null, null));
        } else if (button.id == defaultIconButton.id) {
            mc.displayGuiScreen(new ConfigGUI_Selector(currentScreen, CraftPresence.CONFIG.NAME_defaultDimensionIcon, Constants.TRANSLATOR.translate("gui.config.title.selector.icon"), DiscordAssetHandler.ICON_LIST, CraftPresence.CONFIG.defaultDimensionIcon, null));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parentScreen);
        }
        defaultMessage.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        defaultMessage.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        defaultMessage.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
