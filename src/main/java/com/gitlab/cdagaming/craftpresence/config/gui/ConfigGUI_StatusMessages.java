package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigGUI_StatusMessages extends GuiScreen {
    private final GuiScreen parentscreen;
    private GuiButton proceedButton;
    private GuiTextField mainMenuMSG, singleplayerMSG, loadingMSG, packMSG, playerMSG, playerAmountMSG, gameTimeMSG, viveCraftMSG;

    ConfigGUI_StatusMessages(GuiScreen parentScreen) {
        mc = Minecraft.getMinecraft();
        parentscreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);

        mainMenuMSG = new GuiTextField(110, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(1) - 18, 180, 15);
        singleplayerMSG = new GuiTextField(120, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(2) - 18, 180, 15);
        loadingMSG = new GuiTextField(130, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(3) - 18, 180, 15);
        packMSG = new GuiTextField(140, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(4) - 18, 180, 15);
        playerMSG = new GuiTextField(150, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(5) - 18, 180, 15);
        playerAmountMSG = new GuiTextField(160, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(6) - 18, 180, 15);
        gameTimeMSG = new GuiTextField(170, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(7) - 18, 180, 15);
        viveCraftMSG = new GuiTextField(180, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(8) - 18, 180, 15);

        mainMenuMSG.setText(CraftPresence.CONFIG.mainmenuMSG);
        singleplayerMSG.setText(CraftPresence.CONFIG.singleplayerMSG);
        loadingMSG.setText(CraftPresence.CONFIG.loadingMSG);
        packMSG.setText(CraftPresence.CONFIG.packPlaceholderMSG);
        playerMSG.setText(CraftPresence.CONFIG.playerPlaceholderMSG);
        playerAmountMSG.setText(CraftPresence.CONFIG.playerAmountPlaceholderMSG);
        gameTimeMSG.setText(CraftPresence.CONFIG.gameTimePlaceholderMSG);
        viveCraftMSG.setText(CraftPresence.CONFIG.vivecraftMessage);

        proceedButton = new GuiButton(900, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 25), 180, 20, "Back");
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String title = "CraftPresence - " + I18n.format("gui.config.title.statusmessages");
        String mainMenuText = I18n.format("gui.config.name.statusmessages.mainmenumsg");
        String singlePlayerText = I18n.format("gui.config.name.statusmessages.singleplayermsg");
        String loadingText = I18n.format("gui.config.name.statusmessages.loadingmsg");
        String packText = I18n.format("gui.config.name.statusmessages.placeholder.packmsg");
        String playerText = I18n.format("gui.config.name.statusmessages.placeholder.playermsg");
        String playerAmountText = I18n.format("gui.config.name.statusmessages.placeholder.playeramountmsg");
        String gameTimeText = I18n.format("gui.config.name.statusmessages.placeholder.gametimemsg");
        String viveCraftText = I18n.format("gui.config.name.statusmessages.special.vivecraftmsg");
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();
        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 5, 0xFFFFFF);
        drawString(fontRenderer, mainMenuText, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(1) - 16, 0xFFFFFF);
        drawString(fontRenderer, singlePlayerText, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(2) - 16, 0xFFFFFF);
        drawString(fontRenderer, loadingText, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(3) - 16, 0xFFFFFF);
        drawString(fontRenderer, packText, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(4) - 16, 0xFFFFFF);
        drawString(fontRenderer, playerText, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(5) - 16, 0xFFFFFF);
        drawString(fontRenderer, playerAmountText, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(6) - 16, 0xFFFFFF);
        drawString(fontRenderer, gameTimeText, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(7) - 16, 0xFFFFFF);
        drawString(fontRenderer, viveCraftText, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(8) - 16, 0xFFFFFF);

        mainMenuMSG.drawTextBox();
        singleplayerMSG.drawTextBox();
        loadingMSG.drawTextBox();
        packMSG.drawTextBox();
        playerMSG.drawTextBox();
        playerAmountMSG.drawTextBox();
        gameTimeMSG.drawTextBox();
        viveCraftMSG.drawTextBox();

        proceedButton.enabled = !StringHandler.isNullOrEmpty(mainMenuMSG.getText()) || !StringHandler.isNullOrEmpty(singleplayerMSG.getText()) || !StringHandler.isNullOrEmpty(loadingMSG.getText()) || !StringHandler.isNullOrEmpty(packMSG.getText()) || !StringHandler.isNullOrEmpty(playerMSG.getText()) || !StringHandler.isNullOrEmpty(playerAmountMSG.getText()) || !StringHandler.isNullOrEmpty(gameTimeMSG.getText()) || !StringHandler.isNullOrEmpty(viveCraftMSG.getText());

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Main Menu Message Label
        if (CraftPresence.GUIS.isMouseOverElement(mouseX, mouseY, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(1) - 16, fontRenderer.getStringWidth(mainMenuText), 20)) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.statusmessages.mainmenumsg")), mouseX, mouseY);
        }
        // Hovering over Single Player Message Label
        if (CraftPresence.GUIS.isMouseOverElement(mouseX, mouseY, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(2) - 16, fontRenderer.getStringWidth(singlePlayerText), 20)) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.statusmessages.singleplayermsg")), mouseX, mouseY);
        }
        // Hovering over Loading Message Label
        if (CraftPresence.GUIS.isMouseOverElement(mouseX, mouseY, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(3) - 16, fontRenderer.getStringWidth(loadingText), 20)) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.statusmessages.loadingmsg")), mouseX, mouseY);
        }
        // Hovering over Pack Message Label
        if (CraftPresence.GUIS.isMouseOverElement(mouseX, mouseY, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(4) - 16, fontRenderer.getStringWidth(packText), 20)) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.statusmessages.placeholder.packmsg")), mouseX, mouseY);
        }
        // Hovering over Player Message Label
        if (CraftPresence.GUIS.isMouseOverElement(mouseX, mouseY, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(5) - 16, fontRenderer.getStringWidth(playerText), 20)) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.statusmessages.placeholder.playermsg")), mouseX, mouseY);
        }
        // Hovering over Player Amount Message Label
        if (CraftPresence.GUIS.isMouseOverElement(mouseX, mouseY, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(6) - 16, fontRenderer.getStringWidth(playerAmountText), 20)) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.statusmessages.placeholder.playeramountmsg")), mouseX, mouseY);
        }
        // Hovering over Game Time Message Label
        if (CraftPresence.GUIS.isMouseOverElement(mouseX, mouseY, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(7) - 16, fontRenderer.getStringWidth(gameTimeText), 20)) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.statusmessages.placeholder.gametimemsg")), mouseX, mouseY);
        }
        // Hovering over Vivecraft Message Label
        if (CraftPresence.GUIS.isMouseOverElement(mouseX, mouseY, (sr.getScaledWidth() / 2) - 145, CraftPresence.GUIS.getButtonY(8) - 16, fontRenderer.getStringWidth(viveCraftText), 20)) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.statusmessages.special.vivecraftmsg")), mouseX, mouseY);
        }
        if (proceedButton.isMouseOver() && !proceedButton.enabled) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.hoverMessage.defaultempty")), mouseX, mouseY);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == proceedButton.id) {
            if (!mainMenuMSG.getText().equals(CraftPresence.CONFIG.mainmenuMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.mainmenuMSG = mainMenuMSG.getText();
            }
            if (!singleplayerMSG.getText().equals(CraftPresence.CONFIG.singleplayerMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.singleplayerMSG = singleplayerMSG.getText();
            }
            if (!loadingMSG.getText().equals(CraftPresence.CONFIG.loadingMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.loadingMSG = loadingMSG.getText();
            }
            if (!packMSG.getText().equals(CraftPresence.CONFIG.packPlaceholderMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.packPlaceholderMSG = packMSG.getText();
            }
            if (!playerMSG.getText().equals(CraftPresence.CONFIG.playerPlaceholderMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.playerPlaceholderMSG = playerMSG.getText();
            }
            if (!playerAmountMSG.getText().equals(CraftPresence.CONFIG.playerAmountPlaceholderMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.playerAmountPlaceholderMSG = playerAmountMSG.getText();
            }
            if (!gameTimeMSG.getText().equals(CraftPresence.CONFIG.gameTimePlaceholderMSG)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.gameTimePlaceholderMSG = gameTimeMSG.getText();
            }
            if (!viveCraftMSG.getText().equals(CraftPresence.CONFIG.vivecraftMessage)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.vivecraftMessage = viveCraftMSG.getText();
            }
            mc.displayGuiScreen(parentscreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            mc.displayGuiScreen(parentscreen);
        }
        mainMenuMSG.textboxKeyTyped(typedChar, keyCode);
        singleplayerMSG.textboxKeyTyped(typedChar, keyCode);
        loadingMSG.textboxKeyTyped(typedChar, keyCode);
        packMSG.textboxKeyTyped(typedChar, keyCode);
        playerMSG.textboxKeyTyped(typedChar, keyCode);
        playerAmountMSG.textboxKeyTyped(typedChar, keyCode);
        gameTimeMSG.textboxKeyTyped(typedChar, keyCode);
        viveCraftMSG.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        mainMenuMSG.mouseClicked(mouseX, mouseY, mouseButton);
        singleplayerMSG.mouseClicked(mouseX, mouseY, mouseButton);
        loadingMSG.mouseClicked(mouseX, mouseY, mouseButton);
        packMSG.mouseClicked(mouseX, mouseY, mouseButton);
        playerMSG.mouseClicked(mouseX, mouseY, mouseButton);
        playerAmountMSG.mouseClicked(mouseX, mouseY, mouseButton);
        gameTimeMSG.mouseClicked(mouseX, mouseY, mouseButton);
        viveCraftMSG.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        mainMenuMSG.updateCursorCounter();
        singleplayerMSG.updateCursorCounter();
        loadingMSG.updateCursorCounter();
        packMSG.updateCursorCounter();
        playerMSG.updateCursorCounter();
        playerAmountMSG.updateCursorCounter();
        gameTimeMSG.updateCursorCounter();
        viveCraftMSG.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
