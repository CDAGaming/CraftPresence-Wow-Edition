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

public class StatusMessagesGui extends GuiScreen {
    private final GuiScreen parentScreen;
    private int pageNumber;
    private ExtendedButtonControl proceedButton, nextPageButton, previousPageButton;
    private GuiTextField mainMenuMSG, lanMSG, singleplayerMSG, packMSG,
            outerPlayerMSG, innerPlayerMSG, playerCoordsMSG, playerHealthMSG,
            playerAmountMSG, worldMSG, modsMSG, viveCraftMSG;

    StatusMessagesGui(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        pageNumber = 0;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        // Page 1 Items
        mainMenuMSG = new GuiTextField(110, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        lanMSG = new GuiTextField(120, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(2), 180, 20);
        singleplayerMSG = new GuiTextField(130, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(3), 180, 20);
        packMSG = new GuiTextField(140, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(4), 180, 20);
        modsMSG = new GuiTextField(150, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(5), 180, 20);
        viveCraftMSG = new GuiTextField(160, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(6), 180, 20);

        // Page 2 Items
        outerPlayerMSG = new GuiTextField(170, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        innerPlayerMSG = new GuiTextField(180, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(2), 180, 20);
        playerCoordsMSG = new GuiTextField(190, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(3), 180, 20);
        playerHealthMSG = new GuiTextField(200, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(4), 180, 20);
        playerAmountMSG = new GuiTextField(210, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(5), 180, 20);
        worldMSG = new GuiTextField(220, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(6), 180, 20);

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

        proceedButton = new ExtendedButtonControl(700, (width / 2) - 90, (height - 30), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"));

        previousPageButton = new ExtendedButtonControl(800, proceedButton.x - 23, (height - 30), 20, 20, "<");
        nextPageButton = new ExtendedButtonControl(900, (proceedButton.x + proceedButton.getWidth()) + 3, (height - 30), 20, 20, ">");

        buttonList.add(previousPageButton);
        buttonList.add(nextPageButton);
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.statusmessages");

        final String mainMenuText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.mainmenumsg");
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

            mainMenuMSG.drawTextBox();
            lanMSG.drawTextBox();
            singleplayerMSG.drawTextBox();
            packMSG.drawTextBox();
            modsMSG.drawTextBox();
            viveCraftMSG.drawTextBox();
        }

        if (pageNumber == 1) {
            drawString(mc.fontRenderer, outerPlayerText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, innerPlayerText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, playerCoordsText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, playerHealthText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(4) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, playerAmountText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(5) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, worldDataText, (width / 2) - 160, CraftPresence.GUIS.getButtonY(6) + 5, 0xFFFFFF);

            outerPlayerMSG.drawTextBox();
            innerPlayerMSG.drawTextBox();
            playerCoordsMSG.drawTextBox();
            playerHealthMSG.drawTextBox();
            playerAmountMSG.drawTextBox();
            worldMSG.drawTextBox();
        }

        previousPageButton.enabled = pageNumber != 0;
        nextPageButton.enabled = pageNumber != 1;
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
                && !StringUtils.isNullOrEmpty(worldMSG.getText());

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

        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, proceedButton) && !proceedButton.enabled) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.hoverMessage.defaultempty")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
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

        if (button.id == proceedButton.id) {
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
            CraftPresence.GUIS.openScreen(parentScreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            CraftPresence.GUIS.openScreen(parentScreen);
        }

        if (keyCode == Keyboard.KEY_UP && pageNumber != 0) {
            pageNumber--;
        }

        if (keyCode == Keyboard.KEY_DOWN && pageNumber != 1) {
            pageNumber++;
        }

        if (pageNumber == 0) {
            mainMenuMSG.textboxKeyTyped(typedChar, keyCode);
            lanMSG.textboxKeyTyped(typedChar, keyCode);
            singleplayerMSG.textboxKeyTyped(typedChar, keyCode);
            packMSG.textboxKeyTyped(typedChar, keyCode);
            modsMSG.textboxKeyTyped(typedChar, keyCode);
            viveCraftMSG.textboxKeyTyped(typedChar, keyCode);
        }

        if (pageNumber == 1) {
            outerPlayerMSG.textboxKeyTyped(typedChar, keyCode);
            innerPlayerMSG.textboxKeyTyped(typedChar, keyCode);
            playerCoordsMSG.textboxKeyTyped(typedChar, keyCode);
            playerHealthMSG.textboxKeyTyped(typedChar, keyCode);
            playerAmountMSG.textboxKeyTyped(typedChar, keyCode);
            worldMSG.textboxKeyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (pageNumber == 0) {
            mainMenuMSG.mouseClicked(mouseX, mouseY, mouseButton);
            lanMSG.mouseClicked(mouseX, mouseY, mouseButton);
            singleplayerMSG.mouseClicked(mouseX, mouseY, mouseButton);
            packMSG.mouseClicked(mouseX, mouseY, mouseButton);
            modsMSG.mouseClicked(mouseX, mouseY, mouseButton);
            viveCraftMSG.mouseClicked(mouseX, mouseY, mouseButton);
        }

        if (pageNumber == 1) {
            outerPlayerMSG.mouseClicked(mouseX, mouseY, mouseButton);
            innerPlayerMSG.mouseClicked(mouseX, mouseY, mouseButton);
            playerCoordsMSG.mouseClicked(mouseX, mouseY, mouseButton);
            playerHealthMSG.mouseClicked(mouseX, mouseY, mouseButton);
            playerAmountMSG.mouseClicked(mouseX, mouseY, mouseButton);
            worldMSG.mouseClicked(mouseX, mouseY, mouseButton);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        if (pageNumber == 0) {
            mainMenuMSG.updateCursorCounter();
            lanMSG.updateCursorCounter();
            singleplayerMSG.updateCursorCounter();
            packMSG.updateCursorCounter();
            modsMSG.updateCursorCounter();
            viveCraftMSG.updateCursorCounter();
        }

        if (pageNumber == 1) {
            outerPlayerMSG.updateCursorCounter();
            innerPlayerMSG.updateCursorCounter();
            playerCoordsMSG.updateCursorCounter();
            playerHealthMSG.updateCursorCounter();
            playerAmountMSG.updateCursorCounter();
            worldMSG.updateCursorCounter();
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
