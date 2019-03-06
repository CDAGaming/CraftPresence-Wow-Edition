package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUICheckBox;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUIExtendedButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigGUI_GeneralSettings extends GuiScreen {
    private final GuiScreen parentScreen, currentScreen;
    private GUIExtendedButton proceedButton, defaultIconButton;
    private GUICheckBox detectCurseManifestButton, detectMultiMCManifestButton,
            detectMCUpdaterInstanceButton, detectTechnicPackButton, showTimeButton,
            showBiomeButton, showDimensionButton, showStateButton, enableJoinRequestButton;
    private GuiTextField clientID;

    ConfigGUI_GeneralSettings(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        currentScreen = this;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);

        clientID = new GuiTextField(110, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        clientID.setText(CraftPresence.CONFIG.clientID);
        clientID.setMaxStringLength(18);

        int calc1 = (sr.getScaledWidth() / 2) - 145;
        int calc2 = (sr.getScaledWidth() / 2) + 18;

        defaultIconButton = new GUIExtendedButton(100, (sr.getScaledWidth() / 2) - 90, CraftPresence.GUIS.getButtonY(2), 180, 20, I18n.format("gui.config.name.general.defaulticon"));
        detectCurseManifestButton = new GUICheckBox(200, calc1, CraftPresence.GUIS.getButtonY(3), I18n.format("gui.config.name.general.detectcursemanifest"), CraftPresence.CONFIG.detectCurseManifest);
        detectMultiMCManifestButton = new GUICheckBox(300, calc2, CraftPresence.GUIS.getButtonY(3), I18n.format("gui.config.name.general.detectmultimcmanifest"), CraftPresence.CONFIG.detectMultiMCManifest);
        detectMCUpdaterInstanceButton = new GUICheckBox(400, calc1, CraftPresence.GUIS.getButtonY(4) - 10, I18n.format("gui.config.name.general.detectmcupdaterinstance"), CraftPresence.CONFIG.detectMCUpdaterInstance);
        detectTechnicPackButton = new GUICheckBox(500, calc2, CraftPresence.GUIS.getButtonY(4) - 10, I18n.format("gui.config.name.general.detecttechnicpack"), CraftPresence.CONFIG.detectTechnicPack);
        showTimeButton = new GUICheckBox(600, calc1, CraftPresence.GUIS.getButtonY(5) - 20, I18n.format("gui.config.name.general.showtime"), CraftPresence.CONFIG.showTime);
        showBiomeButton = new GUICheckBox(700, calc2, CraftPresence.GUIS.getButtonY(5) - 20, I18n.format("gui.config.name.general.showbiome"), CraftPresence.CONFIG.showCurrentBiome);
        showDimensionButton = new GUICheckBox(800, calc1, CraftPresence.GUIS.getButtonY(6) - 30, I18n.format("gui.config.name.general.showdimension"), CraftPresence.CONFIG.showCurrentDimension);
        showStateButton = new GUICheckBox(900, calc2, CraftPresence.GUIS.getButtonY(6) - 30, I18n.format("gui.config.name.general.showstate"), CraftPresence.CONFIG.showGameState);
        enableJoinRequestButton = new GUICheckBox(1000, calc1, CraftPresence.GUIS.getButtonY(7) - 40, I18n.format("gui.config.name.general.enablejoinrequest"), CraftPresence.CONFIG.enableJoinRequest);
        proceedButton = new GUIExtendedButton(1100, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 30), 180, 20, I18n.format("gui.config.buttonMessage.back"));

        buttonList.add(defaultIconButton);
        buttonList.add(detectCurseManifestButton);
        buttonList.add(detectMultiMCManifestButton);
        buttonList.add(detectMCUpdaterInstanceButton);
        buttonList.add(detectTechnicPackButton);
        buttonList.add(showTimeButton);
        buttonList.add(showBiomeButton);
        buttonList.add(showDimensionButton);
        buttonList.add(showStateButton);
        buttonList.add(enableJoinRequestButton);
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();

        final String title = "CraftPresence - " + I18n.format("gui.config.title.general");
        final String clientIDText = I18n.format("gui.config.name.general.clientid");

        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);
        drawString(fontRenderer, clientIDText, (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        clientID.drawTextBox();

        proceedButton.enabled = !StringHandler.isNullOrEmpty(clientID.getText()) && clientID.getText().length() == 18 && StringHandler.isValidLong(clientID.getText());

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Client ID Label
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (sr.getScaledWidth() / 2f) - 130, CraftPresence.GUIS.getButtonY(1) + 5, fontRenderer.getStringWidth(clientIDText), fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.general.clientid")), mouseX, mouseY, width, height, -1, fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, defaultIconButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.general.defaulticon")), mouseX, mouseY, width, height, -1, fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, detectCurseManifestButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.general.detectcursemanifest")), mouseX, mouseY, width, height, -1, fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, detectMultiMCManifestButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.general.detectmultimcmanifest")), mouseX, mouseY, width, height, -1, fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, detectMCUpdaterInstanceButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.general.detectmcupdaterinstance")), mouseX, mouseY, width, height, -1, fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, detectTechnicPackButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.general.detecttechnicpack")), mouseX, mouseY, width, height, -1, fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, showTimeButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.general.showtime")), mouseX, mouseY, width, height, -1, fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, showBiomeButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.general.showbiome")), mouseX, mouseY, width, height, -1, fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, showDimensionButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.general.showdimension")), mouseX, mouseY, width, height, -1, fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, showStateButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.general.showstate")), mouseX, mouseY, width, height, -1, fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, enableJoinRequestButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.general.enablejoinrequest")), mouseX, mouseY, width, height, -1, fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, proceedButton) && !proceedButton.enabled) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(I18n.format("gui.config.hoverMessage.defaultempty")), mouseX, mouseY, width, height, -1, fontRenderer, true);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == proceedButton.id) {
            if (!clientID.getText().equals(CraftPresence.CONFIG.clientID)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.clientID = clientID.getText();
            }
            if (detectCurseManifestButton.isChecked() != CraftPresence.CONFIG.detectCurseManifest) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.detectCurseManifest = detectCurseManifestButton.isChecked();
            }
            if (detectMultiMCManifestButton.isChecked() != CraftPresence.CONFIG.detectMultiMCManifest) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.detectMultiMCManifest = detectMultiMCManifestButton.isChecked();
            }
            if (detectMCUpdaterInstanceButton.isChecked() != CraftPresence.CONFIG.detectMCUpdaterInstance) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.detectMCUpdaterInstance = detectMCUpdaterInstanceButton.isChecked();
            }
            if (detectTechnicPackButton.isChecked() != CraftPresence.CONFIG.detectTechnicPack) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.detectTechnicPack = detectTechnicPackButton.isChecked();
            }
            if (showTimeButton.isChecked() != CraftPresence.CONFIG.showTime) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.showTime = showTimeButton.isChecked();
            }
            if (showBiomeButton.isChecked() != CraftPresence.CONFIG.showCurrentBiome) {
                CraftPresence.CONFIG.hasChanged = true;
                if (CraftPresence.BIOMES.BIOME_NAMES.isEmpty()) {
                    CraftPresence.BIOMES.getBiomes();
                    CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                } else {
                    CraftPresence.CONFIG.rebootOnWorldLoad = true;
                }
                CraftPresence.CONFIG.showCurrentBiome = showBiomeButton.isChecked();
            }
            if (showDimensionButton.isChecked() != CraftPresence.CONFIG.showCurrentDimension) {
                CraftPresence.CONFIG.hasChanged = true;
                if (CraftPresence.DIMENSIONS.DIMENSION_NAMES.isEmpty()) {
                    CraftPresence.DIMENSIONS.getDimensions();
                    CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                } else {
                    CraftPresence.CONFIG.rebootOnWorldLoad = true;
                }
                CraftPresence.CONFIG.showCurrentDimension = showDimensionButton.isChecked();
            }
            if (showStateButton.isChecked() != CraftPresence.CONFIG.showGameState) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.showGameState = showStateButton.isChecked();
            }
            if (enableJoinRequestButton.isChecked() != CraftPresence.CONFIG.enableJoinRequest) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.enableJoinRequest = enableJoinRequestButton.isChecked();
            }
            mc.displayGuiScreen(parentScreen);
        } else if (button.id == defaultIconButton.id) {
            mc.displayGuiScreen(new ConfigGUI_Selector(currentScreen, CraftPresence.CONFIG.NAME_defaultIcon, "CraftPresence - Select an Icon", DiscordAssetHandler.ICON_LIST, CraftPresence.CONFIG.defaultIcon, null));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parentScreen);
        }
        clientID.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        clientID.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        clientID.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
