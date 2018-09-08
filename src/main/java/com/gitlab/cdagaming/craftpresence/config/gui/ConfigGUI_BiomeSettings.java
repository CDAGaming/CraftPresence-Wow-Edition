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

public class ConfigGUI_BiomeSettings extends GuiScreen {
    private final GuiScreen parentscreen;
    private GuiButton backButton, editSpecificBiomeButton;
    private GuiTextField defaultMessage;

    private String defaultBiomeMSG;

    ConfigGUI_BiomeSettings(GuiScreen parentScreen) {
        mc = Minecraft.getMinecraft();
        parentscreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);
        defaultBiomeMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.biomeMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);

        defaultMessage = new GuiTextField(110, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        defaultMessage.setText(defaultBiomeMSG);

        editSpecificBiomeButton = new GuiButton(100, (sr.getScaledWidth() / 2) - 90, CraftPresence.GUIS.getButtonY(2), 180, 20, I18n.format("gui.config.name.biomemessages.biomemessages"));
        backButton = new GuiButton(900, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 30), 180, 20, "Back");

        buttonList.add(editSpecificBiomeButton);
        buttonList.add(backButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String title = "CraftPresence - " + I18n.format("gui.config.title.biomemessages");
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();
        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);
        drawString(fontRenderer, "Default Message", (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        defaultMessage.drawTextBox();

        backButton.enabled = !StringHandler.isNullOrEmpty(defaultMessage.getText());

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (editSpecificBiomeButton.isMouseOver()) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.biomemessages.biomemessages").split("\n")), mouseX, mouseY);
        }
        if (backButton.isMouseOver() && !backButton.enabled) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.hoverMessage.defaultempty").split("\n")), mouseX, mouseY);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == backButton.id) {
            mc.displayGuiScreen(parentscreen);
        } else if (button.id == editSpecificBiomeButton.id) {
            mc.displayGuiScreen(new ConfigGUI_Selector(this, CraftPresence.CONFIG.NAME_biomeMessages, "CraftPresence - Select a Biome", CraftPresence.BIOMES.BIOME_NAMES, null));
        } else {
            mc.displayGuiScreen(new ConfigGUI_NullEntry(parentscreen));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        defaultMessage.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        defaultMessage.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        defaultMessage.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);

        if (!defaultMessage.getText().equals(defaultBiomeMSG)) {
            CraftPresence.CONFIG.hasChanged = true;
            CraftPresence.CONFIG.hasClientPropertiesChanged = true;
            StringHandler.setConfigPart(CraftPresence.CONFIG.biomeMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMessage.getText());
        }
    }
}
