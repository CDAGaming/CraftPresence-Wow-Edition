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

public class ConfigGUI_DimensionSettings extends GuiScreen {
    private final GuiScreen parentscreen;
    private GuiButton proceedButton, dimensionMessagesButton, defaultIconButton;
    private GuiTextField defaultMessage;

    private String defaultDimensionMSG;

    ConfigGUI_DimensionSettings(GuiScreen parentScreen) {
        mc = Minecraft.getMinecraft();
        parentscreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);
        defaultDimensionMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);

        defaultMessage = new GuiTextField(110, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        defaultMessage.setText(defaultDimensionMSG);

        dimensionMessagesButton = new GuiButton(100, (sr.getScaledWidth() / 2) - 90, CraftPresence.GUIS.getButtonY(2), 180, 20, I18n.format("gui.config.name.dimensionmessages.dimensionmessages"));
        defaultIconButton = new GuiButton(110, (sr.getScaledWidth() / 2) - 90, CraftPresence.GUIS.getButtonY(3), 180, 20, I18n.format("gui.config.name.dimensionmessages.dimensionicon"));
        proceedButton = new GuiButton(900, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 30), 180, 20, "Back");

        buttonList.add(dimensionMessagesButton);
        buttonList.add(defaultIconButton);
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String title = "CraftPresence - " + I18n.format("gui.config.title.dimensionmessages");
        String defaultMessageText = "Default Dimension Message";
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();
        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);
        drawString(fontRenderer, defaultMessageText, (sr.getScaledWidth() / 2) - 140, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        defaultMessage.drawTextBox();

        proceedButton.enabled = !StringHandler.isNullOrEmpty(defaultMessage.getText());
        dimensionMessagesButton.enabled = CraftPresence.CONFIG.showCurrentDimension;

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Default Dimension Message Label
        if (CraftPresence.GUIS.isMouseOverElement(mouseX, mouseY, (sr.getScaledWidth() / 2) - 140, CraftPresence.GUIS.getButtonY(1) + 5, fontRenderer.getStringWidth(defaultMessageText), 20)) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.title.dimensionmessages")), mouseX, mouseY);
        }
        if (dimensionMessagesButton.isMouseOver()) {
            if (!dimensionMessagesButton.enabled) {
                drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.dimensionmessages.dimensionmessages"))), mouseX, mouseY);
            } else {
                drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.dimensionmessages.dimensionmessages")), mouseX, mouseY);
            }
        }
        if (defaultIconButton.isMouseOver()) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.dimensionmessages.dimensionicon")), mouseX, mouseY);
        }
        if (proceedButton.isMouseOver() && !proceedButton.enabled) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.hoverMessage.defaultempty")), mouseX, mouseY);
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
            mc.displayGuiScreen(parentscreen);
        } else if (button.id == dimensionMessagesButton.id) {
            mc.displayGuiScreen(new ConfigGUI_Selector(this, CraftPresence.CONFIG.NAME_dimensionMessages, "CraftPresence - Select Dimension", CraftPresence.DIMENSIONS.DIMENSION_NAMES, null));
        } else if (button.id == defaultIconButton.id) {
            mc.displayGuiScreen(new ConfigGUI_Selector(this, CraftPresence.CONFIG.NAME_defaultDimensionIcon, "CraftPresence - Select an Icon", DiscordAssetHandler.ICON_LIST, null));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        defaultMessage.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        defaultMessage.mouseClicked(mouseX, mouseY, mouseButton);
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
