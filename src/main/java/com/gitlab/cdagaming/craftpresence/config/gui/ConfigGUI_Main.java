package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.CommandHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigGUI_Main extends GuiScreen {
    private final GuiScreen parentScreen, currentScreen;
    private EntityPlayer player;
    private GuiButton generalSet, biomeSet, dimensionSet, serverSet, statusSet, advancedSet, proceedButton, aboutButton;

    public ConfigGUI_Main(GuiScreen parentScreen) {
        mc = Minecraft.getMinecraft();
        player = mc.player;
        currentScreen = this;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);

        int calc1 = (sr.getScaledWidth() / 2) - 183;
        int calc2 = (sr.getScaledWidth() / 2) + 3;

        generalSet = new GuiButton(100, calc1, CraftPresence.GUIS.getButtonY(1), 180, 20, I18n.format("gui.config.title.general"));
        biomeSet = new GuiButton(200, calc2, CraftPresence.GUIS.getButtonY(1), 180, 20, I18n.format("gui.config.title.biomemessages"));
        dimensionSet = new GuiButton(300, calc1, CraftPresence.GUIS.getButtonY(2), 180, 20, I18n.format("gui.config.title.dimensionmessages"));
        serverSet = new GuiButton(400, calc2, CraftPresence.GUIS.getButtonY(2), 180, 20, I18n.format("gui.config.title.servermessages"));
        statusSet = new GuiButton(500, calc1, CraftPresence.GUIS.getButtonY(3), 180, 20, I18n.format("gui.config.title.statusmessages"));
        advancedSet = new GuiButton(600, calc2, CraftPresence.GUIS.getButtonY(3), 180, 20, I18n.format("gui.config.title.advanced"));

        proceedButton = new GuiButton(700, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 30), 180, 20, "Back");
        aboutButton = new GuiButton(800, 10, (sr.getScaledHeight() - 30), 20, 20, "?");

        buttonList.add(generalSet);
        buttonList.add(biomeSet);
        buttonList.add(dimensionSet);
        buttonList.add(serverSet);
        buttonList.add(statusSet);
        buttonList.add(advancedSet);
        buttonList.add(proceedButton);
        buttonList.add(aboutButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();

        final String title = I18n.format("gui.config.title");

        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);

        biomeSet.enabled = CraftPresence.CONFIG.showCurrentBiome;
        dimensionSet.enabled = CraftPresence.CONFIG.showCurrentDimension;
        serverSet.enabled = CraftPresence.CONFIG.showGameState;
        statusSet.enabled = CraftPresence.CONFIG.showGameState;

        proceedButton.displayString = CraftPresence.CONFIG.hasChanged ? I18n.format("gui.config.buttonMessage.save") : I18n.format("gui.config.buttonMessage.back");

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (generalSet.isMouseOver()) {
            drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.title.general")), mouseX, mouseY);
        }
        if (biomeSet.isMouseOver()) {
            if (!biomeSet.enabled) {
                drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.general.showbiome"))), mouseX, mouseY);
            } else {
                drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.title.biomemessages")), mouseX, mouseY);
            }
        }
        if (dimensionSet.isMouseOver()) {
            if (!dimensionSet.enabled) {
                drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.general.showdimension"))), mouseX, mouseY);
            } else {
                drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.title.dimensionmessages")), mouseX, mouseY);
            }
        }
        if (serverSet.isMouseOver()) {
            if (!serverSet.enabled) {
                drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.general.showstate"))), mouseX, mouseY);
            } else {
                drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.title.servermessages")), mouseX, mouseY);
            }
        }
        if (statusSet.isMouseOver()) {
            if (!statusSet.enabled) {
                drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.general.showstate"))), mouseX, mouseY);
            } else {
                drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.title.statusmessages")), mouseX, mouseY);
            }
        }
        if (advancedSet.isMouseOver()) {
            drawHoveringText(StringHandler.splitTextByNewLine(I18n.format("gui.config.comment.title.advanced")), mouseX, mouseY);
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
            boolean rebooted = false;
            if (CraftPresence.CONFIG.hasChanged) {
                CraftPresence.CONFIG.updateConfig();
                CraftPresence.CONFIG.read();
                if (CraftPresence.CONFIG.hasClientPropertiesChanged) {
                    CommandHandler.rebootRPC();
                    rebooted = true;
                    CraftPresence.CONFIG.hasClientPropertiesChanged = false;
                }
                if (player != null && CraftPresence.CONFIG.rebootOnWorldLoad) {
                    if (!rebooted) {
                        CommandHandler.rebootRPC();
                    }
                    CraftPresence.CONFIG.rebootOnWorldLoad = false;
                }
                CraftPresence.CONFIG.hasChanged = false;
            }

            if (player != null) {
                player.closeScreen();
            } else {
                mc.displayGuiScreen(parentScreen);
            }
        } else if (button.id == aboutButton.id) {
            mc.displayGuiScreen(new ConfigGUI_About(currentScreen));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 && (CraftPresence.CONFIG.hasChanged || CraftPresence.CONFIG.hasClientPropertiesChanged || CraftPresence.CONFIG.rebootOnWorldLoad)) {
            CraftPresence.CONFIG.setupInitialValues();
            CraftPresence.CONFIG.read();
            CraftPresence.CONFIG.hasChanged = false;
            CraftPresence.CONFIG.hasClientPropertiesChanged = false;
            CraftPresence.CONFIG.rebootOnWorldLoad = false;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
