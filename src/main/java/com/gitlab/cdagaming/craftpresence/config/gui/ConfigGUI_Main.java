package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.CommandHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.commands.CommandsGUI;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUIExtendedButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigGUI_Main extends GuiScreen {
    private final GuiScreen parentScreen, currentScreen;
    private GUIExtendedButton generalSet, biomeSet, dimensionSet, serverSet, statusSet, advancedSet, accessibilitySet, proceedButton, aboutButton, commandGUIButton, resetConfigButton;

    public ConfigGUI_Main(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        currentScreen = this;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        CraftPresence.GUIS.configGUIOpened = true;
        Keyboard.enableRepeatEvents(true);

        int calc1 = (width / 2) - 183;
        int calc2 = (width / 2) + 3;

        generalSet = new GUIExtendedButton(100, calc1, CraftPresence.GUIS.getButtonY(1), 180, 20, Constants.TRANSLATOR.translate("gui.config.title.general"));
        biomeSet = new GUIExtendedButton(200, calc2, CraftPresence.GUIS.getButtonY(1), 180, 20, Constants.TRANSLATOR.translate("gui.config.title.biomemessages"));
        dimensionSet = new GUIExtendedButton(300, calc1, CraftPresence.GUIS.getButtonY(2), 180, 20, Constants.TRANSLATOR.translate("gui.config.title.dimensionmessages"));
        serverSet = new GUIExtendedButton(400, calc2, CraftPresence.GUIS.getButtonY(2), 180, 20, Constants.TRANSLATOR.translate("gui.config.title.servermessages"));
        statusSet = new GUIExtendedButton(500, calc1, CraftPresence.GUIS.getButtonY(3), 180, 20, Constants.TRANSLATOR.translate("gui.config.title.statusmessages"));
        advancedSet = new GUIExtendedButton(600, calc2, CraftPresence.GUIS.getButtonY(3), 180, 20, Constants.TRANSLATOR.translate("gui.config.title.advanced"));
        accessibilitySet = new GUIExtendedButton(700, (width / 2) - 90, CraftPresence.GUIS.getButtonY(4), 180, 20, Constants.TRANSLATOR.translate("gui.config.title.accessibility"));

        proceedButton = new GUIExtendedButton(800, (width / 2) - 90, (height - 30), 180, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.back"));
        aboutButton = new GUIExtendedButton(900, (width - 30), 10, 20, 20, "?");
        commandGUIButton = new GUIExtendedButton(1000, (width - 105), (height - 30), 95, 20, Constants.TRANSLATOR.translate("gui.config.title.commands"));
        resetConfigButton = new GUIExtendedButton(1100, 10, (height - 30), 95, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.reset"));

        buttonList.add(generalSet);
        buttonList.add(biomeSet);
        buttonList.add(dimensionSet);
        buttonList.add(serverSet);
        buttonList.add(statusSet);
        buttonList.add(advancedSet);
        buttonList.add(accessibilitySet);
        buttonList.add(proceedButton);
        buttonList.add(aboutButton);
        buttonList.add(commandGUIButton);
        buttonList.add(resetConfigButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

        final String mainTitle = Constants.TRANSLATOR.translate("gui.config.title");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringHandler.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);

        biomeSet.enabled = CraftPresence.CONFIG.showCurrentBiome;
        dimensionSet.enabled = CraftPresence.CONFIG.showCurrentDimension;
        serverSet.enabled = CraftPresence.CONFIG.showGameState;
        statusSet.enabled = CraftPresence.CONFIG.showGameState;
        commandGUIButton.enabled = CraftPresence.CONFIG.enableCommands;

        proceedButton.displayString = CraftPresence.CONFIG.hasChanged ? Constants.TRANSLATOR.translate("gui.config.buttonMessage.save") : Constants.TRANSLATOR.translate("gui.config.buttonMessage.back");

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, generalSet)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.title.general")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, biomeSet)) {
            if (!biomeSet.enabled) {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.hoverMessage.access", Constants.TRANSLATOR.translate("gui.config.name.general.showbiome"))), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            } else {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.title.biomemessages")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, dimensionSet)) {
            if (!dimensionSet.enabled) {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.hoverMessage.access", Constants.TRANSLATOR.translate("gui.config.name.general.showdimension"))), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            } else {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.title.dimensionmessages")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, serverSet)) {
            if (!serverSet.enabled) {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.hoverMessage.access", Constants.TRANSLATOR.translate("gui.config.name.general.showstate"))), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            } else {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.title.servermessages")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, statusSet)) {
            if (!statusSet.enabled) {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.hoverMessage.access", Constants.TRANSLATOR.translate("gui.config.name.general.showstate"))), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            } else {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.title.statusmessages")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, advancedSet)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.title.advanced")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, accessibilitySet)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.title.accessibility")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == generalSet.id) {
            mc.displayGuiScreen(new ConfigGUI_GeneralSettings(currentScreen));
        } else if (button.id == biomeSet.id) {
            mc.displayGuiScreen(new ConfigGUI_BiomeSettings(currentScreen));
        } else if (button.id == dimensionSet.id) {
            mc.displayGuiScreen(new ConfigGUI_DimensionSettings(currentScreen));
        } else if (button.id == serverSet.id) {
            mc.displayGuiScreen(new ConfigGUI_ServerSettings(currentScreen));
        } else if (button.id == statusSet.id) {
            mc.displayGuiScreen(new ConfigGUI_StatusMessages(currentScreen));
        } else if (button.id == advancedSet.id) {
            mc.displayGuiScreen(new ConfigGUI_AdvancedSettings(currentScreen));
        } else if (button.id == accessibilitySet.id) {
            mc.displayGuiScreen(new ConfigGUI_AccessibilitySettings(currentScreen));
        } else if (button.id == proceedButton.id) {
            if (CraftPresence.CONFIG.hasChanged) {
                CraftPresence.CONFIG.updateConfig();
                CraftPresence.CONFIG.read();
                CommandHandler.reloadData(true);
                if (CraftPresence.CONFIG.hasClientPropertiesChanged) {
                    CommandHandler.rebootRPC();
                    CraftPresence.CONFIG.hasClientPropertiesChanged = false;
                }
                CraftPresence.CONFIG.hasChanged = false;
            }

            if (mc.player != null) {
                CraftPresence.GUIS.configGUIOpened = false;
                mc.player.closeScreen();
            } else {
                CraftPresence.GUIS.configGUIOpened = false;
                mc.displayGuiScreen(parentScreen);
            }
        } else if (button.id == aboutButton.id) {
            mc.displayGuiScreen(new ConfigGUI_About(currentScreen));
        } else if (button.id == commandGUIButton.id) {
            mc.displayGuiScreen(new CommandsGUI(currentScreen));
        } else if (button.id == resetConfigButton.id) {
            CraftPresence.CONFIG.hasChanged = false;
            CraftPresence.CONFIG.hasClientPropertiesChanged = false;
            CraftPresence.CONFIG.setupInitialValues();
            CraftPresence.CONFIG.updateConfig();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            if (CraftPresence.CONFIG.hasChanged || CraftPresence.CONFIG.hasClientPropertiesChanged) {
                CraftPresence.CONFIG.setupInitialValues();
                CraftPresence.CONFIG.read();
                CraftPresence.CONFIG.hasChanged = false;
                CraftPresence.CONFIG.hasClientPropertiesChanged = false;
            }
            CraftPresence.GUIS.configGUIOpened = false;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
