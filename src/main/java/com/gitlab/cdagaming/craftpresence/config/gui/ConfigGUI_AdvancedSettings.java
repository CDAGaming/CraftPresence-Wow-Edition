package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUICheckBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigGUI_AdvancedSettings extends GuiScreen {
    private final GuiScreen parentscreen, currentscreen;
    private GuiButton proceedButton, guiMessagesButton, itemMessagesButton;
    private GUICheckBox enableCommandsButton, enablePerGUIButton,
            enablePerItemButton, overwriteServerIconButton;
    private GuiTextField splitCharacter;

    ConfigGUI_AdvancedSettings(GuiScreen parentScreen) {
        mc = Minecraft.getMinecraft();
        currentscreen = this;
        parentscreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);

        splitCharacter = new GuiTextField(110, fontRenderer, (sr.getScaledWidth() / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        splitCharacter.setText(CraftPresence.CONFIG.splitCharacter);
        splitCharacter.setMaxStringLength(1);

        int calc1 = (sr.getScaledWidth() / 2) - 130;
        int calc2 = (sr.getScaledWidth() / 2) + 3;

        guiMessagesButton = new GuiButton(120, calc1 - 15, CraftPresence.GUIS.getButtonY(2), 160, 20, I18n.format("gui.config.name.advanced.guimessages"));
        itemMessagesButton = new GuiButton(130, calc2 + 15, CraftPresence.GUIS.getButtonY(2), 160, 20, I18n.format("gui.config.name.advanced.itemmessages"));

        enableCommandsButton = new GUICheckBox(200, calc1, CraftPresence.GUIS.getButtonY(3), I18n.format("gui.config.name.advanced.enablecommands"), CraftPresence.CONFIG.enableCommands);
        enablePerGUIButton = new GUICheckBox(300, calc2, CraftPresence.GUIS.getButtonY(3), I18n.format("gui.config.name.advanced.enablepergui"), CraftPresence.CONFIG.enablePERGUI);
        enablePerItemButton = new GUICheckBox(400, calc1, CraftPresence.GUIS.getButtonY(4) - 10, I18n.format("gui.config.name.advanced.enableperitem"), CraftPresence.CONFIG.enablePERItem);
        overwriteServerIconButton = new GUICheckBox(500, calc2, CraftPresence.GUIS.getButtonY(4) - 10, I18n.format("gui.config.name.advanced.overwriteservericon"), CraftPresence.CONFIG.overwriteServerIcon);
        proceedButton = new GuiButton(900, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 30), 180, 20, "Back");

        buttonList.add(guiMessagesButton);
        buttonList.add(itemMessagesButton);
        buttonList.add(enableCommandsButton);
        buttonList.add(enablePerGUIButton);
        buttonList.add(enablePerItemButton);
        buttonList.add(overwriteServerIconButton);
        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String title = "CraftPresence - " + I18n.format("gui.config.title.advanced");
        String splitCharacterText = I18n.format("gui.config.name.advanced.splitcharacter");
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();
        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);
        drawString(fontRenderer, splitCharacterText, (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        splitCharacter.drawTextBox();

        proceedButton.enabled = !StringHandler.isNullOrEmpty(splitCharacter.getText()) && splitCharacter.getText().length() == 1 && !splitCharacter.getText().matches(".*[a-z].*") && !splitCharacter.getText().matches(".*[A-Z].*") && !splitCharacter.getText().matches(".*[0-9].*");
        guiMessagesButton.enabled = CraftPresence.CONFIG.enablePERGUI;
        itemMessagesButton.enabled = CraftPresence.CONFIG.enablePERItem;

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Split Character Message Label
        if (CraftPresence.GUIS.isMouseOverElement(mouseX, mouseY, (sr.getScaledWidth() / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, fontRenderer.getStringWidth(splitCharacterText), 20)) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.advanced.splitcharacter")), mouseX, mouseY);
        }
        if (enableCommandsButton.isMouseOver()) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.advanced.enablecommands")), mouseX, mouseY);
        }
        if (enablePerGUIButton.isMouseOver()) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.advanced.enablepergui")), mouseX, mouseY);
        }
        if (enablePerItemButton.isMouseOver()) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.advanced.enableperitem")), mouseX, mouseY);
        }
        if (overwriteServerIconButton.isMouseOver()) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.advanced.overwriteservericon")), mouseX, mouseY);
        }
        if (guiMessagesButton.isMouseOver()) {
            if (!guiMessagesButton.enabled) {
                drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.advanced.guimessages"))), mouseX, mouseY);
            } else {
                drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.advanced.guimessages")), mouseX, mouseY);
            }
        }
        if (itemMessagesButton.isMouseOver()) {
            if (!itemMessagesButton.enabled) {
                drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.advanced.itemmessages"))), mouseX, mouseY);
            } else {
                drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.comment.advanced.itemmessages")), mouseX, mouseY);
            }
        }
        if (proceedButton.isMouseOver() && !proceedButton.enabled) {
            drawHoveringText(CraftPresence.GUIS.formatText(I18n.format("gui.config.hoverMessage.defaultempty")), mouseX, mouseY);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == proceedButton.id) {
            if (!splitCharacter.getText().equals(CraftPresence.CONFIG.splitCharacter)) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.splitCharacter = splitCharacter.getText();
            }
            if (enableCommandsButton.isChecked() != CraftPresence.CONFIG.enableCommands) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.enableCommands = enableCommandsButton.isChecked();
            }
            if (enablePerGUIButton.isChecked() != CraftPresence.CONFIG.enablePERGUI) {
                CraftPresence.CONFIG.hasChanged = true;
                if (CraftPresence.GUIS.GUI_NAMES.isEmpty()) {
                    CraftPresence.GUIS.getGUIs();
                    CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                } else {
                    CraftPresence.CONFIG.rebootOnWorldLoad = true;
                }
                CraftPresence.CONFIG.enablePERGUI = enablePerGUIButton.isChecked();
            }
            if (enablePerItemButton.isChecked() != CraftPresence.CONFIG.enablePERItem) {
                CraftPresence.CONFIG.hasChanged = true;
                if (CraftPresence.ENTITIES.ENTITY_NAMES.isEmpty()) {
                    CraftPresence.ENTITIES.getEntities();
                    CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                } else {
                    CraftPresence.CONFIG.rebootOnWorldLoad = true;
                }
                CraftPresence.CONFIG.enablePERItem = enablePerItemButton.isChecked();
            }
            if (overwriteServerIconButton.isChecked() != CraftPresence.CONFIG.overwriteServerIcon) {
                CraftPresence.CONFIG.hasChanged = true;
                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                CraftPresence.CONFIG.overwriteServerIcon = overwriteServerIconButton.isChecked();
            }
            mc.displayGuiScreen(parentscreen);
        } else if (button.id == guiMessagesButton.id) {
            mc.displayGuiScreen(new ConfigGUI_Selector(currentscreen, CraftPresence.CONFIG.NAME_guiMessages, "CraftPresence - Select a GUI", CraftPresence.GUIS.GUI_NAMES, null));
        } else if (button.id == itemMessagesButton.id) {
            mc.displayGuiScreen(new ConfigGUI_Selector(currentscreen, CraftPresence.CONFIG.NAME_itemMessages, "CraftPresence - Select an Entity/Item", CraftPresence.ENTITIES.ENTITY_NAMES, null));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        splitCharacter.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        splitCharacter.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        splitCharacter.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
