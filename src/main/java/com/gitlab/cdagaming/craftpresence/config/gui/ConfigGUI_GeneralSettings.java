package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUICheckBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigGUI_GeneralSettings extends GuiScreen {
    private final GuiScreen parentscreen, currentscreen;
    private GuiButton proceedButton, defaultIconButton;
    private GUICheckBox detectCurseManifestButton, detectMultiMCManifestButton,
            detectTechnicPackButton, showTimeButton,
            showBiomeButton, showDimensionButton, showStateButton;
    private GuiTextField clientID;

    ConfigGUI_GeneralSettings(GuiScreen parentScreen) {
        mc = Minecraft.getMinecraft();
        currentscreen = this;
        parentscreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);

        clientID = new GuiTextField(110, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        clientID.setText(CraftPresence.CONFIG.clientID);
        clientID.setMaxStringLength(18);

        int calc1 = (sr.getScaledWidth() / 2) - 130;
        int calc2 = (sr.getScaledWidth() / 2) + 3;

        defaultIconButton = new GuiButton(100, (sr.getScaledWidth() / 2) - 90, CraftPresence.GUIS.getButtonY(2), 180, 20, I18n.format("gui.config.name.general.defaulticon"));
        detectCurseManifestButton = new GUICheckBox(200, calc1, CraftPresence.GUIS.getButtonY(3), I18n.format("gui.config.name.general.detectcursemanifest"), CraftPresence.CONFIG.detectCurseManifest);
        detectMultiMCManifestButton = new GUICheckBox(300, calc2, CraftPresence.GUIS.getButtonY(3), I18n.format("gui.config.name.general.detectmultimcmanifest"), CraftPresence.CONFIG.detectMultiMCManifest);
        detectTechnicPackButton = new GUICheckBox(400, calc1, CraftPresence.GUIS.getButtonY(4) - 10, I18n.format("gui.config.name.general.detecttechnicpack"), CraftPresence.CONFIG.detectTechnicPack);
        showTimeButton = new GUICheckBox(500, calc2, CraftPresence.GUIS.getButtonY(4) - 10, I18n.format("gui.config.name.general.showtime"), CraftPresence.CONFIG.showTime);
        showBiomeButton = new GUICheckBox(600, calc1, CraftPresence.GUIS.getButtonY(5) - 20, I18n.format("gui.config.name.general.showbiome"), CraftPresence.CONFIG.showCurrentBiome);
        showDimensionButton = new GUICheckBox(700, calc2, CraftPresence.GUIS.getButtonY(5) - 20, I18n.format("gui.config.name.general.showdimension"), CraftPresence.CONFIG.showCurrentDimension);
        showStateButton = new GUICheckBox(800, calc1, CraftPresence.GUIS.getButtonY(6) - 30, I18n.format("gui.config.name.general.showstate"), CraftPresence.CONFIG.showGameState);
        proceedButton = new GuiButton(900, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 30), 180, 20, "Back");

        buttonList.add(defaultIconButton);
        buttonList.add(detectCurseManifestButton);
        buttonList.add(detectMultiMCManifestButton);
        buttonList.add(detectTechnicPackButton);
        buttonList.add(showTimeButton);
        buttonList.add(showBiomeButton);
        buttonList.add(showDimensionButton);
        buttonList.add(showStateButton);
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String title = "CraftPresence - " + I18n.format("gui.config.title.general");
        String clientIDText = I18n.format("gui.config.name.general.clientid");
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();
        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);
        drawString(fontRenderer, clientIDText, (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        clientID.drawTextBox();

        proceedButton.enabled = !StringHandler.isNullOrEmpty(clientID.getText()) && clientID.getText().length() == 18 && !clientID.getText().matches(".*[a-z].*") && !clientID.getText().matches(".*[A-Z].*");

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Client ID Label
        if (CraftPresence.GUIS.isMouseOverElement(mouseX, mouseY, (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, fontRenderer.getStringWidth(clientIDText), 20)) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.general.clientid")), mouseX, mouseY);
        }
        if (defaultIconButton.isMouseOver()) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.general.defaulticon")), mouseX, mouseY);
        }
        if (detectCurseManifestButton.isMouseOver()) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.general.detectcursemanifest")), mouseX, mouseY);
        }
        if (detectMultiMCManifestButton.isMouseOver()) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.general.detectmultimcmanifest")), mouseX, mouseY);
        }
        if (detectTechnicPackButton.isMouseOver()) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.general.detecttechnicpack")), mouseX, mouseY);
        }
        if (showTimeButton.isMouseOver()) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.general.showtime")), mouseX, mouseY);
        }
        if (showBiomeButton.isMouseOver()) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.general.showbiome")), mouseX, mouseY);
        }
        if (showDimensionButton.isMouseOver()) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.general.showdimension")), mouseX, mouseY);
        }
        if (showStateButton.isMouseOver()) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.general.showstate")), mouseX, mouseY);
        }
        if (proceedButton.isMouseOver() && !proceedButton.enabled) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.hoverMessage.defaultempty")), mouseX, mouseY);
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
            mc.displayGuiScreen(parentscreen);
        } else if (button.id == defaultIconButton.id) {
            mc.displayGuiScreen(new ConfigGUI_Selector(currentscreen, CraftPresence.CONFIG.NAME_defaultIcon, "CraftPresence - Select an Icon", DiscordAssetHandler.ICON_LIST, null));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            mc.displayGuiScreen(parentscreen);
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
