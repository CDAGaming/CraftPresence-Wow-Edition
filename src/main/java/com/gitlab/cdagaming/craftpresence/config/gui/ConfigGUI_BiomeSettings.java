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

public class ConfigGUI_BiomeSettings extends GuiScreen {
    private final GuiScreen parentScreen, currentScreen;
    private GUIExtendedButton proceedButton, biomeMessagesButton;
    private GuiTextField defaultMessage;

    private String defaultBiomeMSG;

    ConfigGUI_BiomeSettings(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        currentScreen = this;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        defaultBiomeMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.biomeMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);

        defaultMessage = new GuiTextField(110, fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        defaultMessage.setText(defaultBiomeMSG);

        biomeMessagesButton = new GUIExtendedButton(100, (width / 2) - 90, CraftPresence.GUIS.getButtonY(2), 180, 20, Constants.TRANSLATOR.translate("gui.config.name.biomemessages.biomemessages"));
        proceedButton = new GUIExtendedButton(900, (width / 2) - 90, (height - 30), 180, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.back"));

        buttonList.add(biomeMessagesButton);
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        final String mainTitle = Constants.TRANSLATOR.translate("gui.config.title");
        final String subTitle = Constants.TRANSLATOR.translate("gui.config.title.biomemessages");
        final String defaultMessageText = Constants.TRANSLATOR.translate("gui.config.defaultMessage.biome");

        drawString(fontRenderer, mainTitle, (width / 2) - (fontRenderer.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(fontRenderer, subTitle, (width / 2) - (fontRenderer.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);
        drawString(fontRenderer, defaultMessageText, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        defaultMessage.drawTextBox();

        proceedButton.enabled = !StringHandler.isNullOrEmpty(defaultMessage.getText());
        biomeMessagesButton.enabled = CraftPresence.CONFIG.showCurrentBiome;

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Default Biome Message Label
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(1) + 5, fontRenderer.getStringWidth(defaultMessageText), fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.title.biomemessages")), mouseX, mouseY, width, height, -1, fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, biomeMessagesButton)) {
            if (!biomeMessagesButton.enabled) {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.hoverMessage.access", Constants.TRANSLATOR.translate("gui.config.name.biomemessages.biomemessages"))), mouseX, mouseY, width, height, -1, fontRenderer, true);
            } else {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.biomemessages.biomemessages")), mouseX, mouseY, width, height, -1, fontRenderer, true);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, proceedButton) && !proceedButton.enabled) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.hoverMessage.defaultempty")), mouseX, mouseY, width, height, -1, fontRenderer, true);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == proceedButton.id) {
            if (!defaultMessage.getText().equals(defaultBiomeMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                StringHandler.setConfigPart(CraftPresence.CONFIG.biomeMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMessage.getText());
            }
            mc.displayGuiScreen(parentScreen);
        } else if (button.id == biomeMessagesButton.id) {
            mc.displayGuiScreen(new ConfigGUI_Selector(currentScreen, CraftPresence.CONFIG.NAME_biomeMessages, Constants.TRANSLATOR.translate("gui.config.title.selector.biome"), CraftPresence.BIOMES.BIOME_NAMES, null, null));
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
