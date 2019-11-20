package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAssetUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.CheckBoxControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GeneralSettingsGui extends GuiScreen {
    private final GuiScreen parentScreen, currentScreen;
    private ExtendedButtonControl proceedButton, defaultIconButton;
    private CheckBoxControl detectCurseManifestButton, detectMultiMCManifestButton,
            detectMCUpdaterInstanceButton, detectTechnicPackButton, showTimeButton,
            showBiomeButton, showDimensionButton, showStateButton, enableJoinRequestButton;
    private GuiTextField clientID;

    GeneralSettingsGui(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        currentScreen = this;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        clientID = new GuiTextField(110, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        clientID.setText(CraftPresence.CONFIG.clientID);
        clientID.setMaxStringLength(18);

        int calc1 = (width / 2) - 145;
        int calc2 = (width / 2) + 18;

        defaultIconButton = new ExtendedButtonControl(100, (width / 2) - 90, CraftPresence.GUIS.getButtonY(2), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.name.general.defaulticon"));
        detectCurseManifestButton = new CheckBoxControl(200, calc1, CraftPresence.GUIS.getButtonY(3), ModUtils.TRANSLATOR.translate("gui.config.name.general.detectcursemanifest"), CraftPresence.CONFIG.detectCurseManifest);
        detectMultiMCManifestButton = new CheckBoxControl(300, calc2, CraftPresence.GUIS.getButtonY(3), ModUtils.TRANSLATOR.translate("gui.config.name.general.detectmultimcmanifest"), CraftPresence.CONFIG.detectMultiMCManifest);
        detectMCUpdaterInstanceButton = new CheckBoxControl(400, calc1, CraftPresence.GUIS.getButtonY(4) - 10, ModUtils.TRANSLATOR.translate("gui.config.name.general.detectmcupdaterinstance"), CraftPresence.CONFIG.detectMCUpdaterInstance);
        detectTechnicPackButton = new CheckBoxControl(500, calc2, CraftPresence.GUIS.getButtonY(4) - 10, ModUtils.TRANSLATOR.translate("gui.config.name.general.detecttechnicpack"), CraftPresence.CONFIG.detectTechnicPack);
        showTimeButton = new CheckBoxControl(600, calc1, CraftPresence.GUIS.getButtonY(5) - 20, ModUtils.TRANSLATOR.translate("gui.config.name.general.showtime"), CraftPresence.CONFIG.showTime);
        showBiomeButton = new CheckBoxControl(700, calc2, CraftPresence.GUIS.getButtonY(5) - 20, ModUtils.TRANSLATOR.translate("gui.config.name.general.showbiome"), CraftPresence.CONFIG.showCurrentBiome);
        showDimensionButton = new CheckBoxControl(800, calc1, CraftPresence.GUIS.getButtonY(6) - 30, ModUtils.TRANSLATOR.translate("gui.config.name.general.showdimension"), CraftPresence.CONFIG.showCurrentDimension);
        showStateButton = new CheckBoxControl(900, calc2, CraftPresence.GUIS.getButtonY(6) - 30, ModUtils.TRANSLATOR.translate("gui.config.name.general.showstate"), CraftPresence.CONFIG.showGameState);
        enableJoinRequestButton = new CheckBoxControl(1000, calc1, CraftPresence.GUIS.getButtonY(7) - 40, ModUtils.TRANSLATOR.translate("gui.config.name.general.enablejoinrequest"), CraftPresence.CONFIG.enableJoinRequest);
        proceedButton = new ExtendedButtonControl(1100, (width / 2) - 90, (height - 30), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"));

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
        CraftPresence.GUIS.drawBackground(width, height);

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.general");
        final String clientIDText = ModUtils.TRANSLATOR.translate("gui.config.name.general.clientid");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringUtils.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);
        drawString(mc.fontRenderer, clientIDText, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        clientID.drawTextBox();

        proceedButton.enabled = !StringUtils.isNullOrEmpty(clientID.getText()) && clientID.getText().length() == 18 && StringUtils.getValidLong(clientID.getText()).getFirst();

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Client ID Label
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(clientIDText), mc.fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.general.clientid")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, defaultIconButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.general.defaulticon")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, detectCurseManifestButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.general.detectcursemanifest")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, detectMultiMCManifestButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.general.detectmultimcmanifest")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, detectMCUpdaterInstanceButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.general.detectmcupdaterinstance")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, detectTechnicPackButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.general.detecttechnicpack")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, showTimeButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.general.showtime")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, showBiomeButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.general.showbiome")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, showDimensionButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.general.showdimension")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, showStateButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.general.showstate")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, enableJoinRequestButton)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.general.enablejoinrequest")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, proceedButton) && !proceedButton.enabled) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.hoverMessage.defaultempty")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
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
                }
                CraftPresence.CONFIG.showCurrentBiome = showBiomeButton.isChecked();
            }
            if (showDimensionButton.isChecked() != CraftPresence.CONFIG.showCurrentDimension) {
                CraftPresence.CONFIG.hasChanged = true;
                if (CraftPresence.DIMENSIONS.DIMENSION_NAMES.isEmpty()) {
                    CraftPresence.DIMENSIONS.getDimensions();
                    CraftPresence.CONFIG.hasClientPropertiesChanged = true;
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
            CraftPresence.GUIS.openScreen(parentScreen);
        } else if (button.id == defaultIconButton.id) {
            CraftPresence.GUIS.openScreen(new SelectorGui(currentScreen, CraftPresence.CONFIG.NAME_defaultIcon, ModUtils.TRANSLATOR.translate("gui.config.title.selector.icon"), DiscordAssetUtils.ICON_LIST, CraftPresence.CONFIG.defaultIcon, null, true));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            CraftPresence.GUIS.openScreen(parentScreen);
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
