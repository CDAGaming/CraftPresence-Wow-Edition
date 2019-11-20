package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.CommandUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.commands.CommandsGui;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class MainGui extends GuiScreen {
    private final GuiScreen parentScreen, currentScreen;
    private ExtendedButtonControl generalSet, biomeSet, dimensionSet, serverSet, statusSet, advancedSet, accessibilitySet, presenceSet, proceedButton, aboutButton, commandGUIButton, resetConfigButton;

    public MainGui(GuiScreen parentScreen) {
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

        generalSet = new ExtendedButtonControl(100, calc1, CraftPresence.GUIS.getButtonY(1), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.title.general"));
        biomeSet = new ExtendedButtonControl(200, calc2, CraftPresence.GUIS.getButtonY(1), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.title.biomemessages"));
        dimensionSet = new ExtendedButtonControl(300, calc1, CraftPresence.GUIS.getButtonY(2), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.title.dimensionmessages"));
        serverSet = new ExtendedButtonControl(400, calc2, CraftPresence.GUIS.getButtonY(2), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.title.servermessages"));
        statusSet = new ExtendedButtonControl(500, calc1, CraftPresence.GUIS.getButtonY(3), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.title.statusmessages"));
        advancedSet = new ExtendedButtonControl(600, calc2, CraftPresence.GUIS.getButtonY(3), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.title.advanced"));
        accessibilitySet = new ExtendedButtonControl(700, calc1, CraftPresence.GUIS.getButtonY(4), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.title.accessibility"));
        presenceSet = new ExtendedButtonControl(800, calc2, CraftPresence.GUIS.getButtonY(4), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.title.presencesettings"));

        proceedButton = new ExtendedButtonControl(900, (width / 2) - 90, (height - 30), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"));
        aboutButton = new ExtendedButtonControl(1000, (width - 30), 10, 20, 20, "?");
        commandGUIButton = new ExtendedButtonControl(1100, (width - 105), (height - 30), 95, 20, ModUtils.TRANSLATOR.translate("gui.config.title.commands"));
        resetConfigButton = new ExtendedButtonControl(1200, 10, (height - 30), 95, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.reset"));

        buttonList.add(generalSet);
        buttonList.add(biomeSet);
        buttonList.add(dimensionSet);
        buttonList.add(serverSet);
        buttonList.add(statusSet);
        buttonList.add(advancedSet);
        buttonList.add(accessibilitySet);
        buttonList.add(presenceSet);
        buttonList.add(proceedButton);
        buttonList.add(aboutButton);
        buttonList.add(commandGUIButton);
        buttonList.add(resetConfigButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);

        biomeSet.enabled = CraftPresence.CONFIG.showCurrentBiome;
        dimensionSet.enabled = CraftPresence.CONFIG.showCurrentDimension;
        serverSet.enabled = CraftPresence.CONFIG.showGameState;
        statusSet.enabled = CraftPresence.CONFIG.showGameState;
        commandGUIButton.enabled = CraftPresence.CONFIG.enableCommands;

        proceedButton.displayString = CraftPresence.CONFIG.hasChanged ? ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.save") : ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back");

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, generalSet)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.title.general")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, biomeSet)) {
            if (!biomeSet.enabled) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.hoverMessage.access", ModUtils.TRANSLATOR.translate("gui.config.name.general.showbiome"))), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            } else {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.title.biomemessages")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, dimensionSet)) {
            if (!dimensionSet.enabled) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.hoverMessage.access", ModUtils.TRANSLATOR.translate("gui.config.name.general.showdimension"))), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            } else {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.title.dimensionmessages")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, serverSet)) {
            if (!serverSet.enabled) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.hoverMessage.access", ModUtils.TRANSLATOR.translate("gui.config.name.general.showstate"))), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            } else {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.title.servermessages")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, statusSet)) {
            if (!statusSet.enabled) {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.hoverMessage.access", ModUtils.TRANSLATOR.translate("gui.config.name.general.showstate"))), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            } else {
                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.title.statusmessages")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
            }
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, advancedSet)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.title.advanced")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, accessibilitySet)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.title.accessibility")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, presenceSet)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.null")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == generalSet.id) {
            mc.displayGuiScreen(new GeneralSettingsGui(currentScreen));
        } else if (button.id == biomeSet.id) {
            mc.displayGuiScreen(new BiomeSettingsGui(currentScreen));
        } else if (button.id == dimensionSet.id) {
            mc.displayGuiScreen(new DimensionSettingsGui(currentScreen));
        } else if (button.id == serverSet.id) {
            mc.displayGuiScreen(new ServerSettingsGui(currentScreen));
        } else if (button.id == statusSet.id) {
            mc.displayGuiScreen(new StatusMessagesGui(currentScreen));
        } else if (button.id == advancedSet.id) {
            mc.displayGuiScreen(new AdvancedSettingsGui(currentScreen));
        } else if (button.id == accessibilitySet.id) {
            mc.displayGuiScreen(new AccessibilitySettingsGui(currentScreen));
        } else if (button.id == presenceSet.id) {
            mc.displayGuiScreen(new PresenceSettingsGui(currentScreen));
        } else if (button.id == proceedButton.id) {
            if (CraftPresence.CONFIG.hasChanged) {
                CraftPresence.CONFIG.updateConfig();
                CraftPresence.CONFIG.read(false);
                CommandUtils.reloadData(true);
                if (CraftPresence.CONFIG.hasClientPropertiesChanged) {
                    CommandUtils.rebootRPC();
                    CraftPresence.CONFIG.hasClientPropertiesChanged = false;
                }
                CraftPresence.CONFIG.hasChanged = false;
            }

            CraftPresence.GUIS.configGUIOpened = false;
            if (mc.player != null) {
                mc.player.closeScreen();
            } else {
                mc.displayGuiScreen(parentScreen);
            }
        } else if (button.id == aboutButton.id) {
            mc.displayGuiScreen(new AboutGui(currentScreen));
        } else if (button.id == commandGUIButton.id) {
            mc.displayGuiScreen(new CommandsGui(currentScreen));
        } else if (button.id == resetConfigButton.id) {
            CraftPresence.CONFIG.setupInitialValues();
            CraftPresence.CONFIG.hasChanged = true;
            CraftPresence.CONFIG.hasClientPropertiesChanged = true;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            if (CraftPresence.CONFIG.hasChanged || CraftPresence.CONFIG.hasClientPropertiesChanged) {
                CraftPresence.CONFIG.setupInitialValues();
                CraftPresence.CONFIG.read(false);
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
