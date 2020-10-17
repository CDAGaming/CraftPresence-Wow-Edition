/*
 * MIT License
 *
 * Copyright (c) 2018 - 2020 CDAGaming (cstack2011@yahoo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class PresenceSettingsGui extends ExtendedScreen {
    private int pageNumber;
    private ExtendedTextControl detailsFormat, gameStateFormat, largeImageFormat, smallImageFormat,
            smallImageKeyFormat, largeImageKeyFormat;
    private ExtendedButtonControl nextPageButton, previousPageButton;

    PresenceSettingsGui(GuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initializeUi() {
        // Page 1 Items
        detailsFormat = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                )
        );
        gameStateFormat = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(2),
                        180, 20
                )
        );
        largeImageFormat = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(3),
                        180, 20
                )
        );
        smallImageFormat = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(4),
                        180, 20
                )
        );

        detailsFormat.setText(CraftPresence.CONFIG.detailsMessage);
        gameStateFormat.setText(CraftPresence.CONFIG.gameStateMessage);
        largeImageFormat.setText(CraftPresence.CONFIG.largeImageMessage);
        smallImageFormat.setText(CraftPresence.CONFIG.smallImageMessage);

        // Page 2 Items
        smallImageKeyFormat = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                )
        );
        largeImageKeyFormat = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(2),
                        180, 20
                )
        );

        smallImageKeyFormat.setText(CraftPresence.CONFIG.smallImageKey);
        largeImageKeyFormat.setText(CraftPresence.CONFIG.largeImageKey);

        final ExtendedButtonControl backButton = addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, (height - 30),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.back"),
                        () -> {
                            if (!detailsFormat.getText().equals(CraftPresence.CONFIG.detailsMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.detailsMessage = detailsFormat.getText();
                            }
                            if (!gameStateFormat.getText().equals(CraftPresence.CONFIG.gameStateMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.gameStateMessage = gameStateFormat.getText();
                            }
                            if (!largeImageFormat.getText().equals(CraftPresence.CONFIG.largeImageMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.largeImageMessage = largeImageFormat.getText();
                            }
                            if (!smallImageFormat.getText().equals(CraftPresence.CONFIG.smallImageMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.smallImageMessage = smallImageFormat.getText();
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
                            CraftPresence.GUIS.openScreen(parentScreen);
                        }
                )
        );

        previousPageButton = addControl(
                new ExtendedButtonControl(
                        backButton.x - 23, (height - 30),
                        20, 20,
                        "<",
                        () -> {
                            if (pageNumber != 0) {
                                pageNumber--;
                            }
                        }
                )
        );
        nextPageButton = addControl(
                new ExtendedButtonControl(
                        (backButton.x + backButton.getControlWidth()) + 3, (height - 30),
                        20, 20,
                        ">",
                        () -> {
                            if (pageNumber != 1) {
                                pageNumber++;
                            }
                        }
                )
        );

        super.initializeUi();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        preDraw();

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.presence_settings");
        final String detailsFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.details_message");
        final String gameStateFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.game_state_message");
        final String largeImageFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.large_image_message");
        final String smallImageFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.small_image_message");
        final String smallImageKeyFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.small_image_key");
        final String largeImageKeyFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.large_image_key");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringUtils.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);

        if (pageNumber == 0) {
            drawString(mc.fontRenderer, detailsFormatTitle, (width / 2) - 160, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, gameStateFormatTitle, (width / 2) - 160, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, largeImageFormatTitle, (width / 2) - 160, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, smallImageFormatTitle, (width / 2) - 160, CraftPresence.GUIS.getButtonY(4) + 5, 0xFFFFFF);
        }

        detailsFormat.setVisible(pageNumber == 0);
        detailsFormat.setEnabled(detailsFormat.getVisible());

        gameStateFormat.setVisible(pageNumber == 0);
        gameStateFormat.setEnabled(gameStateFormat.getVisible());

        largeImageFormat.setVisible(pageNumber == 0);
        largeImageFormat.setEnabled(largeImageFormat.getVisible());

        smallImageFormat.setVisible(pageNumber == 0);
        smallImageFormat.setEnabled(smallImageFormat.getVisible());

        if (pageNumber == 1) {
            drawString(mc.fontRenderer, smallImageKeyFormatTitle, (width / 2) - 160, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, largeImageKeyFormatTitle, (width / 2) - 160, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);
        }

        smallImageKeyFormat.setVisible(pageNumber == 1);
        smallImageKeyFormat.setEnabled(smallImageKeyFormat.getVisible());

        largeImageKeyFormat.setVisible(pageNumber == 1);
        largeImageKeyFormat.setEnabled(largeImageKeyFormat.getVisible());

        previousPageButton.enabled = pageNumber != 0;
        nextPageButton.enabled = pageNumber != 1;

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (pageNumber == 0) {
            // Hovering over Details Format Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(detailsFormatTitle), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.presence.generalArgs")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Game State Format Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2) + 5, StringUtils.getStringWidth(gameStateFormatTitle), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.presence.generalArgs")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Large Image Format Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(3) + 5, StringUtils.getStringWidth(largeImageFormatTitle), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.presence.generalArgs")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Small Image Format Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(4) + 5, StringUtils.getStringWidth(smallImageFormatTitle), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.presence.generalArgs")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }

        if (pageNumber == 1) {
            // Hovering over Small Image Key Format Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(smallImageKeyFormatTitle), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.presence.iconArgs")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Large Image Key Format Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2) + 5, StringUtils.getStringWidth(largeImageKeyFormatTitle), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.presence.iconArgs")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_UP && pageNumber != 0) {
            pageNumber--;
        }

        if (keyCode == Keyboard.KEY_DOWN && pageNumber != 1) {
            pageNumber++;
        }

        super.keyTyped(typedChar, keyCode);
    }
}
