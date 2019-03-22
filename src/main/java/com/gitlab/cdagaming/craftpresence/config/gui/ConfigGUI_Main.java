package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.CommandHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.commands.CommandsGUI;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUIExtendedButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigGUI_Main extends GuiScreen {
    private final GuiScreen parentScreen, currentScreen;
    private GUIExtendedButton generalSet, biomeSet, dimensionSet, serverSet, statusSet, advancedSet, proceedButton, aboutButton, commandGUIButton;

    public ConfigGUI_Main(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        currentScreen = this;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        CraftPresence.GUIS.configGUIOpened = true;
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);

        int calc1 = (sr.getScaledWidth() / 2) - 183;
        int calc2 = (sr.getScaledWidth() / 2) + 3;

        generalSet = new GUIExtendedButton(100, calc1, CraftPresence.GUIS.getButtonY(1), 180, 20, Constants.TRANSLATOR.translate("gui.config.title.general"));
        biomeSet = new GUIExtendedButton(200, calc2, CraftPresence.GUIS.getButtonY(1), 180, 20, Constants.TRANSLATOR.translate("gui.config.title.biomemessages"));
        dimensionSet = new GUIExtendedButton(300, calc1, CraftPresence.GUIS.getButtonY(2), 180, 20, Constants.TRANSLATOR.translate("gui.config.title.dimensionmessages"));
        serverSet = new GUIExtendedButton(400, calc2, CraftPresence.GUIS.getButtonY(2), 180, 20, Constants.TRANSLATOR.translate("gui.config.title.servermessages"));
        statusSet = new GUIExtendedButton(500, calc1, CraftPresence.GUIS.getButtonY(3), 180, 20, Constants.TRANSLATOR.translate("gui.config.title.statusmessages"));
        advancedSet = new GUIExtendedButton(600, calc2, CraftPresence.GUIS.getButtonY(3), 180, 20, Constants.TRANSLATOR.translate("gui.config.title.advanced"));

        proceedButton = new GUIExtendedButton(700, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 30), 180, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.back"));
        aboutButton = new GUIExtendedButton(800, 10, (sr.getScaledHeight() - 30), 20, 20, "?");
        commandGUIButton = new GUIExtendedButton(900, (sr.getScaledWidth() - 120), (sr.getScaledHeight() - 30), 85, 20, Constants.TRANSLATOR.translate("gui.config.title.commands"));

        buttonList.add(generalSet);
        buttonList.add(biomeSet);
        buttonList.add(dimensionSet);
        buttonList.add(serverSet);
        buttonList.add(statusSet);
        buttonList.add(advancedSet);
        buttonList.add(proceedButton);
        buttonList.add(aboutButton);
        buttonList.add(commandGUIButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();

        final String title = Constants.TRANSLATOR.translate("gui.config.title");

        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);

        biomeSet.enabled = CraftPresence.CONFIG.showCurrentBiome;
        dimensionSet.enabled = CraftPresence.CONFIG.showCurrentDimension;
        serverSet.enabled = CraftPresence.CONFIG.showGameState;
        statusSet.enabled = CraftPresence.CONFIG.showGameState;
        commandGUIButton.enabled = CraftPresence.CONFIG.enableCommands;

        proceedButton.displayString = CraftPresence.CONFIG.hasChanged ? Constants.TRANSLATOR.translate("gui.config.buttonMessage.save") : Constants.TRANSLATOR.translate("gui.config.buttonMessage.back");

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, generalSet)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.title.general")), mouseX, mouseY, width, height, -1, fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, biomeSet)) {
            if (!biomeSet.enabled) {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.hoverMessage.access", Constants.TRANSLATOR.translate("gui.config.name.general.showbiome"))), mouseX, mouseY, width, height, -1, fontRenderer, true);
            } else {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.title.biomemessages")), mouseX, mouseY, width, height, -1, fontRenderer, true);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, dimensionSet)) {
            if (!dimensionSet.enabled) {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.hoverMessage.access", Constants.TRANSLATOR.translate("gui.config.name.general.showdimension"))), mouseX, mouseY, width, height, -1, fontRenderer, true);
            } else {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.title.dimensionmessages")), mouseX, mouseY, width, height, -1, fontRenderer, true);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, serverSet)) {
            if (!serverSet.enabled) {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.hoverMessage.access", Constants.TRANSLATOR.translate("gui.config.name.general.showstate"))), mouseX, mouseY, width, height, -1, fontRenderer, true);
            } else {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.title.servermessages")), mouseX, mouseY, width, height, -1, fontRenderer, true);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, statusSet)) {
            if (!statusSet.enabled) {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.hoverMessage.access", Constants.TRANSLATOR.translate("gui.config.name.general.showstate"))), mouseX, mouseY, width, height, -1, fontRenderer, true);
            } else {
                CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.title.statusmessages")), mouseX, mouseY, width, height, -1, fontRenderer, true);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, advancedSet)) {
            CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.comment.title.advanced")), mouseX, mouseY, width, height, -1, fontRenderer, true);
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
