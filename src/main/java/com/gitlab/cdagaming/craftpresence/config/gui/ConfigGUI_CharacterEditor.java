package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUIExtendedButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigGUI_CharacterEditor extends GuiScreen {
    private final GuiScreen parentScreen;
    private GUIExtendedButton backButton, saveButton, syncAllButton, syncSingleButton;
    private GuiTextField charInput, charWidth;
    private String lastScannedCharacter;

    ConfigGUI_CharacterEditor(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        charInput = new GuiTextField(100, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        charWidth = new GuiTextField(110, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(2), 180, 20);
        charInput.setMaxStringLength(1);
        charWidth.setMaxStringLength(2);

        syncAllButton = new GUIExtendedButton(200, (width / 2) - 90, (height - 30), 180, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.sync.all"));
        syncSingleButton = new GUIExtendedButton(300, (width / 2) - 90, syncAllButton.y - 25, 180, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.sync.single", charInput.getText()));

        backButton = new GUIExtendedButton(700, 5, (height - 30), 100, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.back"));
        saveButton = new GUIExtendedButton(800, width - 105, (height - 30), 100, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.save"));

        buttonList.add(syncAllButton);
        buttonList.add(syncSingleButton);
        buttonList.add(backButton);
        buttonList.add(saveButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

        final String mainTitle = Constants.TRANSLATOR.translate("gui.config.title.editor.character");
        final String charInputTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.charinput");
        final String charWidthTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.charwidth");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringHandler.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);
        drawString(mc.fontRenderer, charInputTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        drawString(mc.fontRenderer, charWidthTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(2) + 10, 0xFFFFFF);

        charInput.drawTextBox();
        charWidth.drawTextBox();

        if (StringHandler.isNullOrEmpty(charInput.getText())) {
            charWidth.setText("");
            charWidth.setEnabled(false);

            syncSingleButton.enabled = false;
        }

        if (!charInput.getText().equals(lastScannedCharacter)) {
            lastScannedCharacter = charInput.getText();

            if (!StringHandler.isNullOrEmpty(lastScannedCharacter)) {
                charWidth.setText(Integer.toString(StringHandler.getStringWidth(lastScannedCharacter)));
                charWidth.setEnabled(true);
                syncSingleButton.enabled = true;
            }
        }

        syncSingleButton.displayString = Constants.TRANSLATOR.translate("gui.config.buttonMessage.sync.single", charInput.getText());
        syncSingleButton.visible = syncSingleButton.enabled;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == backButton.id) {
            mc.displayGuiScreen(parentScreen);
        } else if (button.id == saveButton.id) {
            // Save Single Value
        } else if (button.id == syncAllButton.id) {
            // Sync All Values to FontRenderer Defaults
        } else if (button.id == syncSingleButton.id) {
            // Sync Single Value to FontRender Defaults
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parentScreen);
        }

        charInput.textboxKeyTyped(typedChar, keyCode);
        charWidth.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        charInput.mouseClicked(mouseX, mouseY, mouseButton);
        charWidth.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        charInput.updateCursorCounter();
        charWidth.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
