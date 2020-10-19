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

public class StatusMessagesGui extends ExtendedScreen {
    private int pageNumber;
    private ExtendedButtonControl proceedButton, nextPageButton, previousPageButton;
    private ExtendedTextControl mainMenuMessage, loadingMessage, lanMessage, singlePlayerMessage, packMessage,
            outerPlayerMessage, innerPlayerMessage, playerCoordsMessage, playerHealthMessage,
            playerAmountMessage, playerItemsMessage, worldMessage, modsMessage, viveCraftMessage, fallbackPackPlaceholderMessage;

    StatusMessagesGui(GuiScreen parentScreen) {
        super(parentScreen);
        this.pageNumber = 0;
    }

    @Override
    public void initializeUi() {
        // Page 1 Items
        mainMenuMessage = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                )
        );
        lanMessage = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(2),
                        180, 20
                )
        );
        singlePlayerMessage = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(3),
                        180, 20
                )
        );
        packMessage = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(4),
                        180, 20
                )
        );
        modsMessage = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(5),
                        180, 20
                )
        );
        viveCraftMessage = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(6),
                        180, 20
                )
        );

        // Page 2 Items
        outerPlayerMessage = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                )
        );
        innerPlayerMessage = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(2),
                        180, 20
                )
        );
        playerCoordsMessage = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(3),
                        180, 20
                )
        );
        playerHealthMessage = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(4),
                        180, 20
                )
        );
        playerAmountMessage = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(5),
                        180, 20
                )
        );
        worldMessage = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(6),
                        180, 20
                )
        );

        // Page 3 Items
        loadingMessage = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                )
        );
        playerItemsMessage = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(2),
                        180, 20
                )
        );
        fallbackPackPlaceholderMessage = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(3),
                        180, 20
                )
        );

        // Page 1 setText
        mainMenuMessage.setText(CraftPresence.CONFIG.mainMenuMessage);
        lanMessage.setText(CraftPresence.CONFIG.lanMessage);
        singlePlayerMessage.setText(CraftPresence.CONFIG.singlePlayerMessage);
        packMessage.setText(CraftPresence.CONFIG.packPlaceholderMessage);
        modsMessage.setText(CraftPresence.CONFIG.modsPlaceholderMessage);
        viveCraftMessage.setText(CraftPresence.CONFIG.vivecraftMessage);

        // Page 2 setText
        outerPlayerMessage.setText(CraftPresence.CONFIG.outerPlayerPlaceholderMessage);
        innerPlayerMessage.setText(CraftPresence.CONFIG.innerPlayerPlaceholderMessage);
        playerCoordsMessage.setText(CraftPresence.CONFIG.playerCoordinatePlaceholderMessage);
        playerHealthMessage.setText(CraftPresence.CONFIG.playerHealthPlaceholderMessage);
        playerAmountMessage.setText(CraftPresence.CONFIG.playerAmountPlaceholderMessage);
        worldMessage.setText(CraftPresence.CONFIG.worldPlaceholderMessage);

        // Page 3 setText
        loadingMessage.setText(CraftPresence.CONFIG.loadingMessage);
        playerItemsMessage.setText(CraftPresence.CONFIG.playerItemsPlaceholderMessage);
        fallbackPackPlaceholderMessage.setText(CraftPresence.CONFIG.fallbackPackPlaceholderMessage);

        proceedButton = addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, (height - 30),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.back"),
                        () -> {
                            // Page 1 Saving
                            if (!mainMenuMessage.getText().equals(CraftPresence.CONFIG.mainMenuMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.mainMenuMessage = mainMenuMessage.getText();
                            }
                            if (!lanMessage.getText().equals(CraftPresence.CONFIG.lanMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.lanMessage = lanMessage.getText();
                            }
                            if (!singlePlayerMessage.getText().equals(CraftPresence.CONFIG.singlePlayerMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.singlePlayerMessage = singlePlayerMessage.getText();
                            }
                            if (!packMessage.getText().equals(CraftPresence.CONFIG.packPlaceholderMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.packPlaceholderMessage = packMessage.getText();
                            }
                            if (!modsMessage.getText().equals(CraftPresence.CONFIG.modsPlaceholderMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.modsPlaceholderMessage = modsMessage.getText();
                            }
                            if (!viveCraftMessage.getText().equals(CraftPresence.CONFIG.vivecraftMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.vivecraftMessage = viveCraftMessage.getText();
                            }

                            // Page 2 Saving
                            if (!outerPlayerMessage.getText().equals(CraftPresence.CONFIG.outerPlayerPlaceholderMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.outerPlayerPlaceholderMessage = outerPlayerMessage.getText();
                            }
                            if (!innerPlayerMessage.getText().equals(CraftPresence.CONFIG.innerPlayerPlaceholderMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.innerPlayerPlaceholderMessage = innerPlayerMessage.getText();
                            }
                            if (!playerCoordsMessage.getText().equals(CraftPresence.CONFIG.playerCoordinatePlaceholderMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.playerCoordinatePlaceholderMessage = playerCoordsMessage.getText();
                            }
                            if (!playerHealthMessage.getText().equals(CraftPresence.CONFIG.playerHealthPlaceholderMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.playerHealthPlaceholderMessage = playerHealthMessage.getText();
                            }
                            if (!playerAmountMessage.getText().equals(CraftPresence.CONFIG.playerAmountPlaceholderMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.playerAmountPlaceholderMessage = playerAmountMessage.getText();
                            }
                            if (!worldMessage.getText().equals(CraftPresence.CONFIG.worldPlaceholderMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.worldPlaceholderMessage = worldMessage.getText();
                            }

                            // Page 3 Saving
                            if (!loadingMessage.getText().equals(CraftPresence.CONFIG.loadingMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.loadingMessage = loadingMessage.getText();
                            }
                            if (!playerItemsMessage.getText().equals(CraftPresence.CONFIG.playerItemsPlaceholderMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.playerItemsPlaceholderMessage = playerItemsMessage.getText();
                            }
                            if (!fallbackPackPlaceholderMessage.getText().equals(CraftPresence.CONFIG.fallbackPackPlaceholderMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.fallbackPackPlaceholderMessage = fallbackPackPlaceholderMessage.getText();
                            }
                            CraftPresence.GUIS.openScreen(parentScreen);
                        },
                        () -> {
                            if (!proceedButton.enabled) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.empty.default")
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        mc.fontRenderer,
                                        true
                                );
                            }
                        }
                )
        );

        previousPageButton = addControl(
                new ExtendedButtonControl(
                        proceedButton.x - 23, (height - 30),
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
                        (proceedButton.x + proceedButton.getControlWidth()) + 3, (height - 30),
                        20, 20,
                        ">",
                        () -> {
                            if (pageNumber != 2) {
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
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.status_messages");

        final String mainMenuText = ModUtils.TRANSLATOR.translate("gui.config.name.status_messages.main_menu_message");
        final String loadingText = ModUtils.TRANSLATOR.translate("gui.config.name.status_messages.loading_message");
        final String lanText = ModUtils.TRANSLATOR.translate("gui.config.name.status_messages.lan_message");
        final String singlePlayerText = ModUtils.TRANSLATOR.translate("gui.config.name.status_messages.single_player_message");
        final String packText = ModUtils.TRANSLATOR.translate("gui.config.name.status_messages.placeholder.pack_message");
        final String modsText = ModUtils.TRANSLATOR.translate("gui.config.name.status_messages.placeholder.mods_message");
        final String viveCraftText = ModUtils.TRANSLATOR.translate("gui.config.name.status_messages.special.vivecraft_message");

        final String outerPlayerText = ModUtils.TRANSLATOR.translate("gui.config.name.status_messages.placeholder.player_message.out");
        final String innerPlayerText = ModUtils.TRANSLATOR.translate("gui.config.name.status_messages.placeholder.player_message.in");
        final String playerCoordsText = ModUtils.TRANSLATOR.translate("gui.config.name.status_messages.placeholder.player_coordinate_message");
        final String playerHealthText = ModUtils.TRANSLATOR.translate("gui.config.name.status_messages.placeholder.player_health_message");
        final String playerAmountText = ModUtils.TRANSLATOR.translate("gui.config.name.status_messages.placeholder.player_amount_message");
        final String playerItemsText = ModUtils.TRANSLATOR.translate("gui.config.name.status_messages.placeholder.player_item_message");
        final String worldDataText = ModUtils.TRANSLATOR.translate("gui.config.name.status_messages.placeholder.world_message");

        final String fallbackPackPlaceholderText = ModUtils.TRANSLATOR.translate("gui.config.name.status_messages.fallback.pack_placeholder_message");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringUtils.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);

        if (pageNumber == 0) {
            drawString(mc.fontRenderer, mainMenuText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, lanText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, singlePlayerText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, packText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(4) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, modsText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(5) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, viveCraftText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(6) + 5, 0xFFFFFF);
        }

        mainMenuMessage.setVisible(pageNumber == 0);
        mainMenuMessage.setEnabled(mainMenuMessage.getVisible());

        lanMessage.setVisible(pageNumber == 0);
        lanMessage.setEnabled(lanMessage.getVisible());

        singlePlayerMessage.setVisible(pageNumber == 0);
        singlePlayerMessage.setEnabled(singlePlayerMessage.getVisible());

        packMessage.setVisible(pageNumber == 0);
        packMessage.setEnabled(packMessage.getVisible());

        modsMessage.setVisible(pageNumber == 0);
        modsMessage.setEnabled(modsMessage.getVisible());

        viveCraftMessage.setVisible(pageNumber == 0);
        viveCraftMessage.setEnabled(viveCraftMessage.getVisible());

        if (pageNumber == 1) {
            drawString(mc.fontRenderer, outerPlayerText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, innerPlayerText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, playerCoordsText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, playerHealthText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(4) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, playerAmountText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(5) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, worldDataText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(6) + 5, 0xFFFFFF);
        }

        outerPlayerMessage.setVisible(pageNumber == 1);
        outerPlayerMessage.setEnabled(outerPlayerMessage.getVisible());

        innerPlayerMessage.setVisible(pageNumber == 1);
        innerPlayerMessage.setEnabled(innerPlayerMessage.getVisible());

        playerCoordsMessage.setVisible(pageNumber == 1);
        playerCoordsMessage.setEnabled(playerCoordsMessage.getVisible());

        playerHealthMessage.setVisible(pageNumber == 1);
        playerHealthMessage.setEnabled(playerHealthMessage.getVisible());

        playerAmountMessage.setVisible(pageNumber == 1);
        playerAmountMessage.setEnabled(playerAmountMessage.getVisible());

        worldMessage.setVisible(pageNumber == 1);
        worldMessage.setEnabled(worldMessage.getVisible());

        if (pageNumber == 2) {
            drawString(mc.fontRenderer, loadingText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, playerItemsText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, fallbackPackPlaceholderText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);
        }

        loadingMessage.setVisible(pageNumber == 2);
        loadingMessage.setEnabled(loadingMessage.getVisible());

        playerItemsMessage.setVisible(pageNumber == 2);
        playerItemsMessage.setEnabled(playerItemsMessage.getVisible());

        fallbackPackPlaceholderMessage.setVisible(pageNumber == 2);
        fallbackPackPlaceholderMessage.setEnabled(fallbackPackPlaceholderMessage.getVisible());

        previousPageButton.enabled = pageNumber != 0;
        nextPageButton.enabled = pageNumber != 2;
        proceedButton.enabled = !StringUtils.isNullOrEmpty(mainMenuMessage.getText())
                && !StringUtils.isNullOrEmpty(lanMessage.getText())
                && !StringUtils.isNullOrEmpty(singlePlayerMessage.getText())
                && !StringUtils.isNullOrEmpty(packMessage.getText())
                && !StringUtils.isNullOrEmpty(modsMessage.getText())
                && !StringUtils.isNullOrEmpty(viveCraftMessage.getText())
                && !StringUtils.isNullOrEmpty(outerPlayerMessage.getText())
                && !StringUtils.isNullOrEmpty(innerPlayerMessage.getText())
                && !StringUtils.isNullOrEmpty(playerCoordsMessage.getText())
                && !StringUtils.isNullOrEmpty(playerHealthMessage.getText())
                && !StringUtils.isNullOrEmpty(playerAmountMessage.getText())
                && !StringUtils.isNullOrEmpty(worldMessage.getText())
                && !StringUtils.isNullOrEmpty(loadingMessage.getText())
                && !StringUtils.isNullOrEmpty(playerItemsMessage.getText());

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (pageNumber == 0) {
            // Hovering over Main Menu Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(mainMenuText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.main_menu_message")), mouseX, mouseY, width, height, getWrapWidth(), mc.fontRenderer, true);
            }
            // Hovering over LAN Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2) + 5, StringUtils.getStringWidth(lanText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.lan_message")), mouseX, mouseY, width, height, getWrapWidth(), mc.fontRenderer, true);
            }
            // Hovering over Single Player Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(3) + 5, StringUtils.getStringWidth(singlePlayerText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.single_player_message")), mouseX, mouseY, width, height, getWrapWidth(), mc.fontRenderer, true);
            }
            // Hovering over Pack Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(4) + 5, StringUtils.getStringWidth(packText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.pack_message")), mouseX, mouseY, width, height, getWrapWidth(), mc.fontRenderer, true);
            }
            // Hovering over Mods Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(5) + 5, StringUtils.getStringWidth(modsText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.mods_message")), mouseX, mouseY, width, height, getWrapWidth(), mc.fontRenderer, true);
            }
            // Hovering over Vivecraft Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(6) + 5, StringUtils.getStringWidth(viveCraftText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.special.vivecraft_message")), mouseX, mouseY, width, height, getWrapWidth(), mc.fontRenderer, true);
            }
        }

        if (pageNumber == 1) {
            // Hovering over Outer Player Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(outerPlayerText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.player_message.out")), mouseX, mouseY, width, height, getWrapWidth(), mc.fontRenderer, true);
            }
            // Hovering over Inner Player Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2) + 5, StringUtils.getStringWidth(innerPlayerText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.player_message.in")), mouseX, mouseY, width, height, getWrapWidth(), mc.fontRenderer, true);
            }
            // Hovering over Player Coords Message
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(3) + 5, StringUtils.getStringWidth(playerCoordsText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.player_coordinate_message")), mouseX, mouseY, width, height, getWrapWidth(), mc.fontRenderer, true);
            }
            // Hovering over Player Health Message
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(4) + 5, StringUtils.getStringWidth(playerHealthText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.player_health_message")), mouseX, mouseY, width, height, getWrapWidth(), mc.fontRenderer, true);
            }
            // Hovering over Player Amount Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(5) + 5, StringUtils.getStringWidth(playerAmountText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.player_amount_message")), mouseX, mouseY, width, height, getWrapWidth(), mc.fontRenderer, true);
            }
            // Hovering over World Data Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(6) + 5, StringUtils.getStringWidth(worldDataText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.world_message")), mouseX, mouseY, width, height, getWrapWidth(), mc.fontRenderer, true);
            }
        }

        if (pageNumber == 2) {
            // Hovering over Loading Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(loadingText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.loading_message")), mouseX, mouseY, width, height, getWrapWidth(), mc.fontRenderer, true);
            }
            // Hovering over Player Items Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2) + 5, StringUtils.getStringWidth(playerItemsText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.player_item_message")), mouseX, mouseY, width, height, getWrapWidth(), mc.fontRenderer, true);
            }
            // Hovering over Fallback Pack Placeholder Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(3) + 5, StringUtils.getStringWidth(fallbackPackPlaceholderText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.fallback.pack_placeholder_message")), mouseX, mouseY, width, height, getWrapWidth(), mc.fontRenderer, true);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_UP && pageNumber != 0) {
            pageNumber--;
        }

        if (keyCode == Keyboard.KEY_DOWN && pageNumber != 2) {
            pageNumber++;
        }

        super.keyTyped(typedChar, keyCode);
    }
}
