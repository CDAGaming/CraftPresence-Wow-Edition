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

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

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
    private ExtendedTextControl mainMenuMSG, loadingMSG, lanMSG, singleplayerMSG, packMSG,
            outerPlayerMSG, innerPlayerMSG, playerCoordsMSG, playerHealthMSG,
            playerAmountMSG, worldMSG, modsMSG, viveCraftMSG;

    StatusMessagesGui(GuiScreen parentScreen) {
        super(parentScreen);
        this.pageNumber = 0;
    }

    @Override
    public void initGui() {
        // Page 1 Items
        mainMenuMSG = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                )
        );
        lanMSG = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(2),
                        180, 20
                )
        );
        singleplayerMSG = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(3),
                        180, 20
                )
        );
        packMSG = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(4),
                        180, 20
                )
        );
        modsMSG = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(5),
                        180, 20
                )
        );
        viveCraftMSG = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(6),
                        180, 20
                )
        );

        // Page 2 Items
        outerPlayerMSG = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                )
        );
        innerPlayerMSG = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(2),
                        180, 20
                )
        );
        playerCoordsMSG = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(3),
                        180, 20
                )
        );
        playerHealthMSG = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(4),
                        180, 20
                )
        );
        playerAmountMSG = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(5),
                        180, 20
                )
        );
        worldMSG = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(6),
                        180, 20
                )
        );

        // Page 3 Items
        loadingMSG = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                )
        );

        // Page 1 setText
        mainMenuMSG.setText(CraftPresence.CONFIG.mainmenuMSG);
        lanMSG.setText(CraftPresence.CONFIG.lanMSG);
        singleplayerMSG.setText(CraftPresence.CONFIG.singleplayerMSG);
        packMSG.setText(CraftPresence.CONFIG.packPlaceholderMSG);
        modsMSG.setText(CraftPresence.CONFIG.modsPlaceholderMSG);
        viveCraftMSG.setText(CraftPresence.CONFIG.vivecraftMessage);

        // Page 2 setText
        outerPlayerMSG.setText(CraftPresence.CONFIG.outerPlayerPlaceholderMSG);
        innerPlayerMSG.setText(CraftPresence.CONFIG.innerPlayerPlaceholderMSG);
        playerCoordsMSG.setText(CraftPresence.CONFIG.playerCoordinatePlaceholderMSG);
        playerHealthMSG.setText(CraftPresence.CONFIG.playerHealthPlaceholderMSG);
        playerAmountMSG.setText(CraftPresence.CONFIG.playerAmountPlaceholderMSG);
        worldMSG.setText(CraftPresence.CONFIG.worldPlaceholderMSG);

        // Page 3 setText
        loadingMSG.setText(CraftPresence.CONFIG.loadingMSG);

        proceedButton = addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, (height - 30),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"),
                        () -> {
                            // Page 1 Saving
                            if (!mainMenuMSG.getText().equals(CraftPresence.CONFIG.mainmenuMSG)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.mainmenuMSG = mainMenuMSG.getText();
                            }
                            if (!lanMSG.getText().equals(CraftPresence.CONFIG.lanMSG)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.lanMSG = lanMSG.getText();
                            }
                            if (!singleplayerMSG.getText().equals(CraftPresence.CONFIG.singleplayerMSG)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.singleplayerMSG = singleplayerMSG.getText();
                            }
                            if (!packMSG.getText().equals(CraftPresence.CONFIG.packPlaceholderMSG)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.packPlaceholderMSG = packMSG.getText();
                            }
                            if (!modsMSG.getText().equals(CraftPresence.CONFIG.modsPlaceholderMSG)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.modsPlaceholderMSG = modsMSG.getText();
                            }
                            if (!viveCraftMSG.getText().equals(CraftPresence.CONFIG.vivecraftMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.vivecraftMessage = viveCraftMSG.getText();
                            }

                            // Page 2 Saving
                            if (!outerPlayerMSG.getText().equals(CraftPresence.CONFIG.outerPlayerPlaceholderMSG)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.outerPlayerPlaceholderMSG = outerPlayerMSG.getText();
                            }
                            if (!innerPlayerMSG.getText().equals(CraftPresence.CONFIG.innerPlayerPlaceholderMSG)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.innerPlayerPlaceholderMSG = innerPlayerMSG.getText();
                            }
                            if (!playerCoordsMSG.getText().equals(CraftPresence.CONFIG.playerCoordinatePlaceholderMSG)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.playerCoordinatePlaceholderMSG = playerCoordsMSG.getText();
                            }
                            if (!playerHealthMSG.getText().equals(CraftPresence.CONFIG.playerHealthPlaceholderMSG)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.playerHealthPlaceholderMSG = playerHealthMSG.getText();
                            }
                            if (!playerAmountMSG.getText().equals(CraftPresence.CONFIG.playerAmountPlaceholderMSG)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.playerAmountPlaceholderMSG = playerAmountMSG.getText();
                            }
                            if (!worldMSG.getText().equals(CraftPresence.CONFIG.worldPlaceholderMSG)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.worldPlaceholderMSG = worldMSG.getText();
                            }

                            // Page 3 Saving
                            if (!loadingMSG.getText().equals(CraftPresence.CONFIG.loadingMSG)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.loadingMSG = loadingMSG.getText();
                            }
                            CraftPresence.GUIS.openScreen(parentScreen);
                        },
                        () -> {
                            if (!proceedButton.enabled) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.hoverMessage.defaultempty")
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
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
                        (proceedButton.x + proceedButton.getWidth()) + 3, (height - 30),
                        20, 20,
                        ">",
                        () -> {
                            if (pageNumber != 2) {
                                pageNumber++;
                            }
                        }
                )
        );

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        preDraw();

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.statusmessages");

        final String mainMenuText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.mainmenumsg");
        final String loadingText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.loadingmsg");
        final String lanText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.lanmsg");
        final String singlePlayerText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.singleplayermsg");
        final String packText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.placeholder.packmsg");
        final String modsText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.placeholder.modsmsg");
        final String viveCraftText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.special.vivecraftmsg");

        final String outerPlayerText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.placeholder.playermsg.out");
        final String innerPlayerText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.placeholder.playermsg.in");
        final String playerCoordsText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.placeholder.playercoordinatemsg");
        final String playerHealthText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.placeholder.playerhealthmsg");
        final String playerAmountText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.placeholder.playeramountmsg");
        final String worldDataText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.placeholder.worldmsg");

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

        mainMenuMSG.setVisible(pageNumber == 0);
        mainMenuMSG.setEnabled(mainMenuMSG.getVisible());

        lanMSG.setVisible(pageNumber == 0);
        lanMSG.setEnabled(lanMSG.getVisible());

        singleplayerMSG.setVisible(pageNumber == 0);
        singleplayerMSG.setEnabled(singleplayerMSG.getVisible());

        packMSG.setVisible(pageNumber == 0);
        packMSG.setEnabled(packMSG.getVisible());

        modsMSG.setVisible(pageNumber == 0);
        modsMSG.setEnabled(modsMSG.getVisible());

        viveCraftMSG.setVisible(pageNumber == 0);
        viveCraftMSG.setEnabled(viveCraftMSG.getVisible());

        if (pageNumber == 1) {
            drawString(mc.fontRenderer, outerPlayerText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, innerPlayerText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, playerCoordsText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, playerHealthText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(4) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, playerAmountText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(5) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, worldDataText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(6) + 5, 0xFFFFFF);
        }

        outerPlayerMSG.setVisible(pageNumber == 1);
        outerPlayerMSG.setEnabled(outerPlayerMSG.getVisible());

        innerPlayerMSG.setVisible(pageNumber == 1);
        innerPlayerMSG.setEnabled(innerPlayerMSG.getVisible());

        playerCoordsMSG.setVisible(pageNumber == 1);
        playerCoordsMSG.setEnabled(playerCoordsMSG.getVisible());

        playerHealthMSG.setVisible(pageNumber == 1);
        playerHealthMSG.setEnabled(playerHealthMSG.getVisible());

        playerAmountMSG.setVisible(pageNumber == 1);
        playerAmountMSG.setEnabled(playerAmountMSG.getVisible());

        worldMSG.setVisible(pageNumber == 1);
        worldMSG.setEnabled(worldMSG.getVisible());

        if (pageNumber == 2) {
            drawString(mc.fontRenderer, loadingText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        }

        loadingMSG.setVisible(pageNumber == 2);
        loadingMSG.setEnabled(loadingMSG.getVisible());

        previousPageButton.enabled = pageNumber != 0;
        nextPageButton.enabled = pageNumber != 2;
        proceedButton.enabled = !StringUtils.isNullOrEmpty(mainMenuMSG.getText())
                && !StringUtils.isNullOrEmpty(lanMSG.getText())
                && !StringUtils.isNullOrEmpty(singleplayerMSG.getText())
                && !StringUtils.isNullOrEmpty(packMSG.getText())
                && !StringUtils.isNullOrEmpty(modsMSG.getText())
                && !StringUtils.isNullOrEmpty(viveCraftMSG.getText())
                && !StringUtils.isNullOrEmpty(outerPlayerMSG.getText())
                && !StringUtils.isNullOrEmpty(innerPlayerMSG.getText())
                && !StringUtils.isNullOrEmpty(playerCoordsMSG.getText())
                && !StringUtils.isNullOrEmpty(playerHealthMSG.getText())
                && !StringUtils.isNullOrEmpty(playerAmountMSG.getText())
                && !StringUtils.isNullOrEmpty(worldMSG.getText())
                && !StringUtils.isNullOrEmpty(loadingMSG.getText());

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (pageNumber == 0) {
            // Hovering over Main Menu Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(mainMenuText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.mainmenumsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over LAN Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2) + 5, StringUtils.getStringWidth(lanText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.lanmsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Single Player Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(3) + 5, StringUtils.getStringWidth(singlePlayerText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.singleplayermsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Pack Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(4) + 5, StringUtils.getStringWidth(packText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.placeholder.packmsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Mods Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(5) + 5, StringUtils.getStringWidth(modsText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.placeholder.modsmsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Vivecraft Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(6) + 5, StringUtils.getStringWidth(viveCraftText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.special.vivecraftmsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }

        if (pageNumber == 1) {
            // Hovering over Outer Player Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(outerPlayerText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.placeholder.playermsg.out")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Inner Player Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(2) + 5, StringUtils.getStringWidth(innerPlayerText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.placeholder.playermsg.in")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Player Coords Message
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(3) + 5, StringUtils.getStringWidth(playerCoordsText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.placeholder.playercoordinatemsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Player Health Message
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(4) + 5, StringUtils.getStringWidth(playerHealthText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.placeholder.playerhealthmsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Player Amount Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(5) + 5, StringUtils.getStringWidth(playerAmountText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.placeholder.playeramountmsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over World Data Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(6) + 5, StringUtils.getStringWidth(worldDataText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.placeholder.worldmsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }

        if (pageNumber == 2) {
            // Hovering over Loading Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 160, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(loadingText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.loadingmsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
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
