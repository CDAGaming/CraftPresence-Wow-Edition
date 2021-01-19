/*
 * MIT License
 *
 * Copyright (c) 2018 - 2021 CDAGaming (cstack2011@yahoo.com)
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
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.integrations.PaginatedScreen;
import net.minecraft.client.gui.GuiScreen;

@SuppressWarnings("DuplicatedCode")
public class PresenceSettingsGui extends PaginatedScreen {
    private ExtendedTextControl detailsFormat, gameStateFormat, largeImageFormat, smallImageFormat,
            smallImageKeyFormat, largeImageKeyFormat;

    PresenceSettingsGui(GuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initializeUi() {
        // Page 1 Items
        detailsFormat = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                ), startPage
        );
        gameStateFormat = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(2),
                        180, 20
                ), startPage
        );
        largeImageFormat = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(3),
                        180, 20
                ), startPage
        );
        smallImageFormat = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(4),
                        180, 20
                ), startPage
        );

        detailsFormat.setText(CraftPresence.CONFIG.detailsMessage);
        gameStateFormat.setText(CraftPresence.CONFIG.gameStateMessage);
        largeImageFormat.setText(CraftPresence.CONFIG.largeImageMessage);
        smallImageFormat.setText(CraftPresence.CONFIG.smallImageMessage);

        // Page 2 Items
        smallImageKeyFormat = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                ), startPage + 1
        );
        largeImageKeyFormat = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(2),
                        180, 20
                ), startPage + 1
        );

        smallImageKeyFormat.setText(CraftPresence.CONFIG.smallImageKey);
        largeImageKeyFormat.setText(CraftPresence.CONFIG.largeImageKey);

        super.initializeUi();

        backButton.setOnClick(
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
        );
    }

    @Override
    public void preRender() {
        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.presence_settings");
        final String detailsFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.details_message");
        final String gameStateFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.game_state_message");
        final String largeImageFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.large_image_message");
        final String smallImageFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.small_image_message");
        final String smallImageKeyFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.small_image_key");
        final String largeImageKeyFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.large_image_key");

        renderString(mainTitle, (width / 2f) - (StringUtils.getStringWidth(mainTitle) / 2f), 10, 0xFFFFFF);
        renderString(subTitle, (width / 2f) - (StringUtils.getStringWidth(subTitle) / 2f), 20, 0xFFFFFF);

        renderString(detailsFormatTitle, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1, 5), 0xFFFFFF, startPage);
        renderString(gameStateFormatTitle, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2, 5), 0xFFFFFF, startPage);
        renderString(largeImageFormatTitle, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(3, 5), 0xFFFFFF, startPage);
        renderString(smallImageFormatTitle, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(4, 5), 0xFFFFFF, startPage);

        renderString(smallImageKeyFormatTitle, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1, 5), 0xFFFFFF, startPage + 1);
        renderString(largeImageKeyFormatTitle, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2, 5), 0xFFFFFF, startPage + 1);

        super.preRender();
    }

    @Override
    public void postRender() {
        final String detailsFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.details_message");
        final String gameStateFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.game_state_message");
        final String largeImageFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.large_image_message");
        final String smallImageFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.small_image_message");
        final String smallImageKeyFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.small_image_key");
        final String largeImageKeyFormatTitle = ModUtils.TRANSLATOR.translate("gui.config.name.display.large_image_key");
        if (currentPage == startPage) {
            // Hovering over Details Format Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1, 5), StringUtils.getStringWidth(detailsFormatTitle), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.presence.generalArgs")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over Game State Format Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2, 5), StringUtils.getStringWidth(gameStateFormatTitle), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.presence.generalArgs")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over Large Image Format Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(3, 5), StringUtils.getStringWidth(largeImageFormatTitle), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.presence.generalArgs")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over Small Image Format Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(4, 5), StringUtils.getStringWidth(smallImageFormatTitle), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.presence.generalArgs")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
        }

        if (currentPage == (startPage + 1)) {
            // Hovering over Small Image Key Format Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1, 5), StringUtils.getStringWidth(smallImageKeyFormatTitle), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.presence.iconArgs")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over Large Image Key Format Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2, 5), StringUtils.getStringWidth(largeImageKeyFormatTitle), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.presence.iconArgs")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
        }
    }
}
