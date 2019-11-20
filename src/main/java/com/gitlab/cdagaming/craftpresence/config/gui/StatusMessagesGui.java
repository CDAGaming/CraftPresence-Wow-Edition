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
            playerMSG, playerAmountMSG, gameTimeMSG, modsMSG, viveCraftMSG;

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

        // Page 2 Items
        packMSG = new GuiTextField(140, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        playerMSG = new GuiTextField(150, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(2), 180, 20);
        playerAmountMSG = new GuiTextField(160, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(3), 180, 20);
        gameTimeMSG = new GuiTextField(170, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(4), 180, 20);
        modsMSG = new GuiTextField(180, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(5), 180, 20);
        viveCraftMSG = new GuiTextField(190, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(6), 180, 20);

        mainMenuMSG.setText(CraftPresence.CONFIG.mainmenuMSG);
        lanMSG.setText(CraftPresence.CONFIG.lanMSG);
        singleplayerMSG.setText(CraftPresence.CONFIG.singleplayerMSG);
        packMSG.setText(CraftPresence.CONFIG.packPlaceholderMSG);
        playerMSG.setText(CraftPresence.CONFIG.playerPlaceholderMSG);
        playerAmountMSG.setText(CraftPresence.CONFIG.playerAmountPlaceholderMSG);
        gameTimeMSG.setText(CraftPresence.CONFIG.gameTimePlaceholderMSG);
        modsMSG.setText(CraftPresence.CONFIG.modsPlaceholderMSG);
        viveCraftMSG.setText(CraftPresence.CONFIG.vivecraftMessage);

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
        final String playerText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.placeholder.playermsg");
        final String playerAmountText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.placeholder.playeramountmsg");
        final String gameTimeText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.placeholder.gametimemsg");
        final String modsText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.placeholder.modsmsg");
        final String viveCraftText = ModUtils.TRANSLATOR.translate("gui.config.name.statusmessages.special.vivecraftmsg");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringUtils.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);

        if (pageNumber == 0) {
            drawString(mc.fontRenderer, mainMenuText, (width / 2) - 145, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, lanText, (width / 2) - 145, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, singlePlayerText, (width / 2) - 145, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);

            mainMenuMSG.drawTextBox();
            lanMSG.drawTextBox();
            singleplayerMSG.drawTextBox();
        }

        if (pageNumber == 1) {
            drawString(mc.fontRenderer, packText, (width / 2) - 145, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, playerText, (width / 2) - 145, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, playerAmountText, (width / 2) - 145, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, gameTimeText, (width / 2) - 145, CraftPresence.GUIS.getButtonY(4) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, modsText, (width / 2) - 145, CraftPresence.GUIS.getButtonY(5) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, viveCraftText, (width / 2) - 145, CraftPresence.GUIS.getButtonY(6) + 5, 0xFFFFFF);

            packMSG.drawTextBox();
            playerMSG.drawTextBox();
            playerAmountMSG.drawTextBox();
            gameTimeMSG.drawTextBox();
            modsMSG.drawTextBox();
            viveCraftMSG.drawTextBox();
        }

        previousPageButton.enabled = pageNumber != 0;
        nextPageButton.enabled = pageNumber != 1;
        proceedButton.enabled = !StringUtils.isNullOrEmpty(mainMenuMSG.getText()) && !StringUtils.isNullOrEmpty(singleplayerMSG.getText()) && !StringUtils.isNullOrEmpty(packMSG.getText()) && !StringUtils.isNullOrEmpty(playerMSG.getText()) && !StringUtils.isNullOrEmpty(playerAmountMSG.getText()) && !StringUtils.isNullOrEmpty(gameTimeMSG.getText()) && !StringUtils.isNullOrEmpty(viveCraftMSG.getText());

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (pageNumber == 0) {
            // Hovering over Main Menu Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 145, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(mainMenuText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.mainmenumsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over LAN Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 145, CraftPresence.GUIS.getButtonY(2) + 5, StringUtils.getStringWidth(lanText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.lanmsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Single Player Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 145, CraftPresence.GUIS.getButtonY(3) + 5, StringUtils.getStringWidth(singlePlayerText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.singleplayermsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }

        if (pageNumber == 1) {
            // Hovering over TechnicPack Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 145, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(packText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.placeholder.packmsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Player Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 145, CraftPresence.GUIS.getButtonY(2) + 5, StringUtils.getStringWidth(playerText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.placeholder.playermsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Player Amount Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 145, CraftPresence.GUIS.getButtonY(3) + 5, StringUtils.getStringWidth(playerAmountText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.placeholder.playeramountmsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Game Time Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 145, CraftPresence.GUIS.getButtonY(4) + 5, StringUtils.getStringWidth(gameTimeText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.placeholder.gametimemsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Mods Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 145, CraftPresence.GUIS.getButtonY(5) + 5, StringUtils.getStringWidth(modsText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.placeholder.modsmsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
            // Hovering over Vivecraft Message Label
            if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 145, CraftPresence.GUIS.getButtonY(6) + 5, StringUtils.getStringWidth(viveCraftText), mc.fontRenderer.FONT_HEIGHT)) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.statusmessages.special.vivecraftmsg")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
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
            if (!mainMenuMSG.getText().equals(CraftPresence.CONFIG.mainmenuMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.mainmenuMSG = mainMenuMSG.getText();
            }
            if (!lanMSG.getText().equals(CraftPresence.CONFIG.lanMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.lanMSG = lanMSG.getText();
            }
            if (!singleplayerMSG.getText().equals(CraftPresence.CONFIG.singleplayerMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.singleplayerMSG = singleplayerMSG.getText();
            }
            if (!packMSG.getText().equals(CraftPresence.CONFIG.packPlaceholderMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.packPlaceholderMSG = packMSG.getText();
            }
            if (!playerMSG.getText().equals(CraftPresence.CONFIG.playerPlaceholderMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.playerPlaceholderMSG = playerMSG.getText();
            }
            if (!playerAmountMSG.getText().equals(CraftPresence.CONFIG.playerAmountPlaceholderMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.playerAmountPlaceholderMSG = playerAmountMSG.getText();
            }
            if (!gameTimeMSG.getText().equals(CraftPresence.CONFIG.gameTimePlaceholderMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.gameTimePlaceholderMSG = gameTimeMSG.getText();
            }
            if (!modsMSG.getText().equals(CraftPresence.CONFIG.modsPlaceholderMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.modsPlaceholderMSG = modsMSG.getText();
            }
            if (!viveCraftMSG.getText().equals(CraftPresence.CONFIG.vivecraftMessage)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.vivecraftMessage = viveCraftMSG.getText();
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
        }

        if (pageNumber == 1) {
            packMSG.textboxKeyTyped(typedChar, keyCode);
            playerMSG.textboxKeyTyped(typedChar, keyCode);
            playerAmountMSG.textboxKeyTyped(typedChar, keyCode);
            gameTimeMSG.textboxKeyTyped(typedChar, keyCode);
            modsMSG.textboxKeyTyped(typedChar, keyCode);
            viveCraftMSG.textboxKeyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (pageNumber == 0) {
            mainMenuMSG.mouseClicked(mouseX, mouseY, mouseButton);
            lanMSG.mouseClicked(mouseX, mouseY, mouseButton);
            singleplayerMSG.mouseClicked(mouseX, mouseY, mouseButton);
        }

        if (pageNumber == 1) {
            packMSG.mouseClicked(mouseX, mouseY, mouseButton);
            playerMSG.mouseClicked(mouseX, mouseY, mouseButton);
            playerAmountMSG.mouseClicked(mouseX, mouseY, mouseButton);
            gameTimeMSG.mouseClicked(mouseX, mouseY, mouseButton);
            modsMSG.mouseClicked(mouseX, mouseY, mouseButton);
            viveCraftMSG.mouseClicked(mouseX, mouseY, mouseButton);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        if (pageNumber == 0) {
            mainMenuMSG.updateCursorCounter();
            lanMSG.updateCursorCounter();
            singleplayerMSG.updateCursorCounter();
        }

        if (pageNumber == 1) {
            packMSG.updateCursorCounter();
            playerMSG.updateCursorCounter();
            playerAmountMSG.updateCursorCounter();
            gameTimeMSG.updateCursorCounter();
            modsMSG.updateCursorCounter();
            viveCraftMSG.updateCursorCounter();
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
