package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class PresenceSettingsGui extends GuiScreen {
    private final GuiScreen parentScreen;
    private int pageNumber;
    private GuiTextField detailsFormat, gameStateFormat, largeImageFormat, smallImageFormat,
            smallImageKeyFormat, largeImageKeyFormat;
    private ExtendedButtonControl backButton, nextPageButton, previousPageButton;

    PresenceSettingsGui(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        pageNumber = 0;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        // Page 1 Items
        detailsFormat = new GuiTextField(110, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        gameStateFormat = new GuiTextField(120, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(2), 180, 20);
        largeImageFormat = new GuiTextField(130, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(3), 180, 20);
        smallImageFormat = new GuiTextField(140, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(4), 180, 20);

        detailsFormat.setText(CraftPresence.CONFIG.detailsMSG);
        gameStateFormat.setText(CraftPresence.CONFIG.gameStateMSG);
        largeImageFormat.setText(CraftPresence.CONFIG.largeImageMSG);
        smallImageFormat.setText(CraftPresence.CONFIG.smallImageMSG);

        // Page 2 Items
        smallImageKeyFormat = new GuiTextField(150, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        largeImageKeyFormat = new GuiTextField(160, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(2), 180, 20);

        smallImageKeyFormat.setText(CraftPresence.CONFIG.smallImageKey);
        largeImageKeyFormat.setText(CraftPresence.CONFIG.largeImageKey);

        backButton = new ExtendedButtonControl(700, (width / 2) - 90, (height - 30), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"));

        previousPageButton = new ExtendedButtonControl(800, backButton.x - 23, (height - 30), 20, 20, "<");
        nextPageButton = new ExtendedButtonControl(900, (backButton.x + backButton.getWidth()) + 3, (height - 30), 20, 20, ">");

        buttonList.add(previousPageButton);
        buttonList.add(nextPageButton);
        buttonList.add(backButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.presencesettings");
        final String detailsFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.detailsmsg");
        final String gameStateFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.gamestatemsg");
        final String largeImageFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.largeimagemsg");
        final String smallImageFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.smallimagemsg");
        final String smallImageKeyFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.smallimagekey");
        final String largeImageKeyFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.largeimagekey");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringUtils.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);

        if (pageNumber == 0) {
            drawString(mc.fontRenderer, detailsFormatTitle, (width / 2) - 145, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, gameStateFormatTitle, (width / 2) - 145, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, largeImageFormatTitle, (width / 2) - 145, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, smallImageFormatTitle, (width / 2) - 145, CraftPresence.GUIS.getButtonY(4) + 5, 0xFFFFFF);

            detailsFormat.drawTextBox();
            gameStateFormat.drawTextBox();
            largeImageFormat.drawTextBox();
            smallImageFormat.drawTextBox();
        }

        if (pageNumber == 1) {
            drawString(mc.fontRenderer, smallImageKeyFormatTitle, (width / 2) - 145, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, largeImageKeyFormatTitle, (width / 2) - 145, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);

            smallImageKeyFormat.drawTextBox();
            largeImageKeyFormat.drawTextBox();
        }

        previousPageButton.enabled = pageNumber != 0;
        nextPageButton.enabled = pageNumber != 1;

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (pageNumber == 0) {
            // Hovering over Details Format Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 145, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(detailsFormatTitle), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.null")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Game State Format Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 145, CraftPresence.GUIS.getButtonY(2) + 5, StringUtils.getStringWidth(gameStateFormatTitle), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.null")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Large Image Format Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 145, CraftPresence.GUIS.getButtonY(3) + 5, StringUtils.getStringWidth(largeImageFormatTitle), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.null")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }

            // Hovering over Small Image Format Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 145, CraftPresence.GUIS.getButtonY(4) + 5, StringUtils.getStringWidth(smallImageFormatTitle), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.null")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }

        if (pageNumber == 1) {
            // Hovering over Small Image Key Format Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 145, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(smallImageKeyFormatTitle), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.null")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Large Image Key Format Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 145, CraftPresence.GUIS.getButtonY(2) + 5, StringUtils.getStringWidth(largeImageKeyFormatTitle), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.null")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == previousPageButton.id && pageNumber != 0) {
            pageNumber--;
        }
        if (button.id == nextPageButton.id && pageNumber != 1) {
            pageNumber++;
        }

        if (button.id == backButton.id) {
            if (!detailsFormat.getText().equals(CraftPresence.CONFIG.detailsMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.detailsMSG = detailsFormat.getText();
            }
            if (!gameStateFormat.getText().equals(CraftPresence.CONFIG.gameStateMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.gameStateMSG = gameStateFormat.getText();
            }
            if (!largeImageFormat.getText().equals(CraftPresence.CONFIG.largeImageMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.largeImageMSG = largeImageFormat.getText();
            }
            if (!smallImageFormat.getText().equals(CraftPresence.CONFIG.smallImageMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.smallImageMSG = smallImageFormat.getText();
            }
            if (!largeImageKeyFormat.getText().equals(CraftPresence.CONFIG.largeImageKey)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.largeImageKey = largeImageKeyFormat.getText();
            }
            if (!smallImageKeyFormat.getText().equals(CraftPresence.CONFIG.smallImageKey)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.smallImageKey = smallImageKeyFormat.getText();
            }
            mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parentScreen);
        }

        if (keyCode == Keyboard.KEY_LEFT && pageNumber != 0) {
            pageNumber--;
        }

        if (keyCode == Keyboard.KEY_RIGHT && pageNumber != 1) {
            pageNumber++;
        }

        if (pageNumber == 0) {
            detailsFormat.textboxKeyTyped(typedChar, keyCode);
            gameStateFormat.textboxKeyTyped(typedChar, keyCode);
            largeImageFormat.textboxKeyTyped(typedChar, keyCode);
            smallImageFormat.textboxKeyTyped(typedChar, keyCode);
        }

        if (pageNumber == 1) {
            smallImageKeyFormat.textboxKeyTyped(typedChar, keyCode);
            largeImageKeyFormat.textboxKeyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (pageNumber == 0) {
            detailsFormat.mouseClicked(mouseX, mouseY, mouseButton);
            gameStateFormat.mouseClicked(mouseX, mouseY, mouseButton);
            largeImageFormat.mouseClicked(mouseX, mouseY, mouseButton);
            smallImageFormat.mouseClicked(mouseX, mouseY, mouseButton);
        }

        if (pageNumber == 1) {
            smallImageKeyFormat.mouseClicked(mouseX, mouseY, mouseButton);
            largeImageKeyFormat.mouseClicked(mouseX, mouseY, mouseButton);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        if (pageNumber == 0) {
            detailsFormat.updateCursorCounter();
            gameStateFormat.updateCursorCounter();
            largeImageFormat.updateCursorCounter();
            smallImageFormat.updateCursorCounter();
        }

        if (pageNumber == 1) {
            smallImageKeyFormat.updateCursorCounter();
            largeImageKeyFormat.updateCursorCounter();
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
