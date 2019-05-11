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
import java.util.Arrays;
import java.util.List;

public class ConfigGUI_CharacterEditor extends GuiScreen {
    private final GuiScreen parentScreen;
    private GUIExtendedButton backButton, saveButton, syncAllButton, syncSingleButton, resetCharsButton;
    private GuiTextField charInput, charWidth;
    private String lastScannedString;
    private char lastScannedChar;

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

        resetCharsButton = new GUIExtendedButton(200, (width / 2) - 90, (height - 30), 180, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.reset"));
        syncAllButton = new GUIExtendedButton(300, (width / 2) - 90, resetCharsButton.y - 25, 180, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.sync.all"));
        syncSingleButton = new GUIExtendedButton(400, (width / 2) - 90, syncAllButton.y - 25, 180, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.sync.single", charInput.getText()));

        backButton = new GUIExtendedButton(700, 5, (height - 30), 100, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.back"));
        saveButton = new GUIExtendedButton(800, width - 105, (height - 30), 100, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.save"));

        buttonList.add(resetCharsButton);
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
        final List<String> notice = StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.message.character.notice"));

        if (notice != null && !notice.isEmpty()) {
            for (int i = 0; i < notice.size(); i++) {
                final String string = notice.get(i);
                drawString(mc.fontRenderer, string, (width / 2) - (StringHandler.getStringWidth(string) / 2), (height / 2) + (i * 10), 0xFFFFFF);
            }
        }

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

        if (!charInput.getText().equals(lastScannedString)) {
            lastScannedString = charInput.getText();

            if (!StringHandler.isNullOrEmpty(lastScannedString)) {
                lastScannedChar = lastScannedString.charAt(0);
                charWidth.setText(Integer.toString(StringHandler.getStringWidth(lastScannedString)));
                charWidth.setEnabled(true);
                syncSingleButton.enabled = true;
            } else {
                lastScannedChar = Character.UNASSIGNED;
            }
        }

        syncSingleButton.displayString = Constants.TRANSLATOR.translate("gui.config.buttonMessage.sync.single", charInput.getText());
        saveButton.enabled = syncSingleButton.enabled;
        syncSingleButton.visible = syncSingleButton.enabled;
        saveButton.visible = syncSingleButton.visible;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        int[] originalCharArray = StringHandler.MC_CHAR_WIDTH.clone();
        byte[] originalGlyphArray = StringHandler.MC_GLYPH_WIDTH.clone();

        if (button.id == backButton.id) {
            mc.displayGuiScreen(parentScreen);
        } else if (button.id == saveButton.id && StringHandler.isValidInteger(charWidth.getText())) {
            // Save Single Value
            int characterWidth = Integer.parseInt(charWidth.getText());
            if (lastScannedChar > 0 && lastScannedChar <= StringHandler.MC_CHAR_WIDTH.length && !Constants.TRANSLATOR.isUnicode) {
                StringHandler.MC_CHAR_WIDTH[lastScannedChar] = characterWidth;
            } else if (StringHandler.MC_GLYPH_WIDTH[lastScannedChar] != 0) {
                StringHandler.MC_GLYPH_WIDTH[lastScannedChar & 255] = (byte) characterWidth;
            }
        } else if (button.id == syncAllButton.id) {
            // Sync ALL Values to FontRender Defaults
            for (int currentCharIndex = 0; currentCharIndex < StringHandler.MC_CHAR_WIDTH.length - 1; currentCharIndex++) {
                char characterObj = (char) (currentCharIndex);
                StringHandler.MC_CHAR_WIDTH[currentCharIndex] = mc.fontRenderer.getCharWidth(characterObj);
            }

            for (int currentGlyphIndex = 0; currentGlyphIndex < StringHandler.MC_GLYPH_WIDTH.length - 1; currentGlyphIndex++) {
                char glyphObj = (char) (currentGlyphIndex & 255);
                StringHandler.MC_GLYPH_WIDTH[currentGlyphIndex] = (byte) mc.fontRenderer.getCharWidth(glyphObj);
            }
        } else if (button.id == syncSingleButton.id) {
            // Sync Single Value to FontRender Defaults
            if (lastScannedChar > 0 && lastScannedChar < StringHandler.MC_CHAR_WIDTH.length && !Constants.TRANSLATOR.isUnicode) {
                StringHandler.MC_CHAR_WIDTH[lastScannedChar] = mc.fontRenderer.getCharWidth(lastScannedChar);
            } else if (StringHandler.MC_GLYPH_WIDTH[lastScannedChar] != 0) {
                StringHandler.MC_GLYPH_WIDTH[lastScannedChar & 255] = (byte) mc.fontRenderer.getCharWidth(lastScannedChar);
            }
        } else if (button.id == resetCharsButton.id) {
            Constants.loadCharData(true);
        }

        if (!Arrays.equals(originalCharArray, StringHandler.MC_CHAR_WIDTH) || !Arrays.equals(originalGlyphArray, StringHandler.MC_GLYPH_WIDTH)) {
            // Write to Char Data
            Constants.writeToCharData();

            // Clear Data for Next Entry
            lastScannedString = null;
            lastScannedChar = Character.UNASSIGNED;
            charInput.setText("");
            charWidth.setText("");
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
