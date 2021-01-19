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
public class StatusMessagesGui extends PaginatedScreen {
    private ExtendedTextControl mainMenuMessage, loadingMessage, lanMessage, singlePlayerMessage, packMessage,
            outerPlayerMessage, innerPlayerMessage, playerCoordsMessage, playerHealthMessage,
            playerAmountMessage, playerItemsMessage, worldMessage, modsMessage, viveCraftMessage, fallbackPackPlaceholderMessage;

    StatusMessagesGui(GuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initializeUi() {
        // Page 1 Items
        mainMenuMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                ), startPage
        );
        lanMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(2),
                        180, 20
                ), startPage
        );
        singlePlayerMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(3),
                        180, 20
                ), startPage
        );
        packMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(4),
                        180, 20
                ), startPage
        );
        modsMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(5),
                        180, 20
                ), startPage
        );
        viveCraftMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(6),
                        180, 20
                ), startPage
        );

        // Page 2 Items
        outerPlayerMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                ), startPage + 1
        );
        innerPlayerMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(2),
                        180, 20
                ), startPage + 1
        );
        playerCoordsMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(3),
                        180, 20
                ), startPage + 1
        );
        playerHealthMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(4),
                        180, 20
                ), startPage + 1
        );
        playerAmountMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(5),
                        180, 20
                ), startPage + 1
        );
        worldMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(6),
                        180, 20
                ), startPage + 1
        );

        // Page 3 Items
        loadingMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                ), startPage + 2
        );
        playerItemsMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(2),
                        180, 20
                ), startPage + 2
        );
        fallbackPackPlaceholderMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(3),
                        180, 20
                ), startPage + 2
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

        super.initializeUi();

        backButton.setOnClick(
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
                }
        );
        backButton.setOnHover(
                () -> {
                    if (!backButton.isControlEnabled()) {
                        CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.message.hover.empty.default")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        );
                    }
                }
        );
    }

    @Override
    public void preRender() {
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

        renderString(mainTitle, (width / 2f) - (StringUtils.getStringWidth(mainTitle) / 2f), 10, 0xFFFFFF);
        renderString(subTitle, (width / 2f) - (StringUtils.getStringWidth(subTitle) / 2f), 20, 0xFFFFFF);

        renderString(mainMenuText, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1, 5), 0xFFFFFF, startPage);
        renderString(lanText, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2, 5), 0xFFFFFF, startPage);
        renderString(singlePlayerText, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(3, 5), 0xFFFFFF, startPage);
        renderString(packText, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(4, 5), 0xFFFFFF, startPage);
        renderString(modsText, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(5, 5), 0xFFFFFF, startPage);
        renderString(viveCraftText, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(6, 5), 0xFFFFFF, startPage);

        renderString(outerPlayerText, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1, 5), 0xFFFFFF, startPage + 1);
        renderString(innerPlayerText, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2, 5), 0xFFFFFF, startPage + 1);
        renderString(playerCoordsText, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(3, 5), 0xFFFFFF, startPage + 1);
        renderString(playerHealthText, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(4, 5), 0xFFFFFF, startPage + 1);
        renderString(playerAmountText, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(5, 5), 0xFFFFFF, startPage + 1);
        renderString(worldDataText, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(6, 5), 0xFFFFFF, startPage + 1);

        renderString(loadingText, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1, 5), 0xFFFFFF, startPage + 2);
        renderString(playerItemsText, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2, 5), 0xFFFFFF, startPage + 2);
        renderString(fallbackPackPlaceholderText, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(3, 5), 0xFFFFFF, startPage + 2);

        super.preRender();

        backButton.setControlEnabled(!StringUtils.isNullOrEmpty(mainMenuMessage.getText())
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
                && !StringUtils.isNullOrEmpty(playerItemsMessage.getText()));
    }

    @Override
    public void postRender() {
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
        if (currentPage == startPage) {
            // Hovering over Main Menu Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1, 5), StringUtils.getStringWidth(mainMenuText), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.main_menu_message")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over LAN Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2, 5), StringUtils.getStringWidth(lanText), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.lan_message")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over Single Player Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(3, 5), StringUtils.getStringWidth(singlePlayerText), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.single_player_message")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over Pack Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(4, 5), StringUtils.getStringWidth(packText), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.pack_message")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over Mods Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(5, 5), StringUtils.getStringWidth(modsText), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.mods_message")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over Vivecraft Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(6, 5), StringUtils.getStringWidth(viveCraftText), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.special.vivecraft_message")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
        }

        if (currentPage == startPage + 1) {
            // Hovering over Outer Player Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1, 5), StringUtils.getStringWidth(outerPlayerText), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.player_message.out")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over Inner Player Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2, 5), StringUtils.getStringWidth(innerPlayerText), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.player_message.in")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over Player Coords Message
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(3, 5), StringUtils.getStringWidth(playerCoordsText), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.player_coordinate_message")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over Player Health Message
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(4, 5), StringUtils.getStringWidth(playerHealthText), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.player_health_message")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over Player Amount Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(5, 5), StringUtils.getStringWidth(playerAmountText), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.player_amount_message")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over World Data Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(6, 5), StringUtils.getStringWidth(worldDataText), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.world_message")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
        }

        if (currentPage == startPage + 2) {
            // Hovering over Loading Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1, 5), StringUtils.getStringWidth(loadingText), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.loading_message")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over Player Items Message Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2, 5), StringUtils.getStringWidth(playerItemsText), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.placeholder.player_item_message")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
            // Hovering over Fallback Pack Placeholder Label
            if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 160, CraftPresence.GUIS.getButtonY(3, 5), StringUtils.getStringWidth(fallbackPackPlaceholderText), getFontHeight())) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.status_messages.fallback.pack_placeholder_message")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
            }
        }
    }
}
