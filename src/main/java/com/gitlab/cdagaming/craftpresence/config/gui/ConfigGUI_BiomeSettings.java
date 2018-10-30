package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigGUI_BiomeSettings extends GuiScreen {
    private final GuiScreen parentScreen, currentScreen;
    private GuiButton proceedButton, biomeMessagesButton;
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
        ScaledResolution sr = new ScaledResolution(mc);

        defaultBiomeMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.biomeMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);

        defaultMessage = new GuiTextField(110, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        defaultMessage.setText(defaultBiomeMSG);

        biomeMessagesButton = new GuiButton(100, (sr.getScaledWidth() / 2) - 90, CraftPresence.GUIS.getButtonY(2), 180, 20, I18n.format("gui.config.name.biomemessages.biomemessages"));
        proceedButton = new GuiButton(900, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 30), 180, 20, I18n.format("gui.config.buttonMessage.back"));

        buttonList.add(biomeMessagesButton);
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();

        final String title = "CraftPresence - " + I18n.format("gui.config.title.biomemessages");
        final String defaultMessageText = I18n.format("gui.config.defaultMessage.biome");

        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);
        drawString(fontRenderer, defaultMessageText, (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        defaultMessage.drawTextBox();

        proceedButton.enabled = !StringHandler.isNullOrEmpty(defaultMessage.getText());
        biomeMessagesButton.enabled = CraftPresence.CONFIG.showCurrentBiome;

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Default Biome Message Label
        if (CraftPresence.GUIS.isMouseOverElement(mouseX, mouseY, (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, fontRenderer.getStringWidth(defaultMessageText), fontRenderer.FONT_HEIGHT)) {
            drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.title.biomemessages")), mouseX, mouseY);
        }
        if (biomeMessagesButton.isMouseOver()) {
            if (!biomeMessagesButton.enabled) {
                drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.biomemessages.biomemessages"))), mouseX, mouseY);
            } else {
                drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.biomemessages.biomemessages")), mouseX, mouseY);
            }
        }
        if (proceedButton.isMouseOver() && !proceedButton.enabled) {
            drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.hoverMessage.defaultempty")), mouseX, mouseY);
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
            mc.displayGuiScreen(new ConfigGUI_Selector(currentScreen, CraftPresence.CONFIG.NAME_biomeMessages, I18n.format("gui.config.title.selector.biome"), CraftPresence.BIOMES.BIOME_NAMES, null, null));
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
