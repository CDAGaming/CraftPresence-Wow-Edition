package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CharacterEditorGui extends GuiScreen {
    private final GuiScreen parentScreen;
    private ExtendedButtonControl backButton, saveButton, syncAllButton, syncSingleButton, resetCharsButton;
    private GuiTextField charInput, charWidth;
    private String lastScannedString;
    private char lastScannedChar;

    private int[] originalCharArray = StringUtils.MC_CHAR_WIDTH.clone();
    private byte[] originalGlyphArray = StringUtils.MC_GLYPH_WIDTH.clone();

    CharacterEditorGui(GuiScreen parentScreen) {
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

        resetCharsButton = new ExtendedButtonControl(200, (width / 2) - 90, (height - 30), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.reset"));
        syncAllButton = new ExtendedButtonControl(300, (width / 2) - 90, resetCharsButton.y - 25, 180, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.sync.all"));
        syncSingleButton = new ExtendedButtonControl(400, (width / 2) - 90, syncAllButton.y - 25, 180, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.sync.single", charInput.getText()));

        backButton = new ExtendedButtonControl(700, 5, (height - 30), 100, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"));
        saveButton = new ExtendedButtonControl(800, width - 105, (height - 30), 100, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.save"));

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
        checkValues();

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.editor.character");
        final String charInputTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.charinput");
        final String charWidthTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.charwidth");
        final List<String> notice = StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.character.notice"));

        if (notice != null && !notice.isEmpty()) {
            for (int i = 0; i < notice.size(); i++) {
                final String string = notice.get(i);
                drawString(mc.fontRenderer, string, (width / 2) - (StringUtils.getStringWidth(string) / 2), (height / 2) + (i * 10), 0xFFFFFF);
            }
        }

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);
        drawString(mc.fontRenderer, charInputTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        drawString(mc.fontRenderer, charWidthTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(2) + 10, 0xFFFFFF);

        charInput.drawTextBox();
        charWidth.drawTextBox();

        if (StringUtils.isNullOrEmpty(charInput.getText())) {
            charWidth.setText("");
            charWidth.setEnabled(false);

            syncSingleButton.enabled = false;
        }

        if (!charInput.getText().equals(lastScannedString)) {
            lastScannedString = charInput.getText();

            if (!StringUtils.isNullOrEmpty(lastScannedString)) {
                lastScannedChar = lastScannedString.charAt(0);
                charWidth.setText(Integer.toString(StringUtils.getStringWidth(lastScannedString)));
                charWidth.setEnabled(true);
                syncSingleButton.enabled = true;
            } else {
                lastScannedChar = Character.UNASSIGNED;
            }
        }

        syncSingleButton.displayString = ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.sync.single", charInput.getText());
        saveButton.enabled = syncSingleButton.enabled;
        syncSingleButton.visible = syncSingleButton.enabled;
        saveButton.visible = syncSingleButton.visible;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == backButton.id) {
            CraftPresence.GUIS.openScreen(parentScreen);
        } else if (button.id == saveButton.id) {
            Tuple<Boolean, Integer> charData = StringUtils.getValidInteger(charWidth.getText());

            if (charData.getFirst()) {
                // Save Single Value if Char Data is a Valid Number
                int characterWidth = charData.getSecond();
                if (lastScannedChar > 0 && lastScannedChar <= StringUtils.MC_CHAR_WIDTH.length && !ModUtils.TRANSLATOR.isUnicode) {
                    StringUtils.MC_CHAR_WIDTH[lastScannedChar] = characterWidth;
                } else if (StringUtils.MC_GLYPH_WIDTH[lastScannedChar] != 0) {
                    StringUtils.MC_GLYPH_WIDTH[lastScannedChar & 255] = (byte) characterWidth;
                }
            }
        } else if (button.id == syncAllButton.id) {
            // Sync ALL Values to FontRender Defaults
            for (int currentCharIndex = 0; currentCharIndex < StringUtils.MC_CHAR_WIDTH.length - 1; currentCharIndex++) {
                char characterObj = (char) (currentCharIndex);
                StringUtils.MC_CHAR_WIDTH[currentCharIndex] = mc.fontRenderer.getStringWidth(Character.toString(characterObj));
            }

            for (int currentGlyphIndex = 0; currentGlyphIndex < StringUtils.MC_GLYPH_WIDTH.length - 1; currentGlyphIndex++) {
                char glyphObj = (char) (currentGlyphIndex & 255);
                StringUtils.MC_GLYPH_WIDTH[currentGlyphIndex] = (byte) mc.fontRenderer.getStringWidth(Character.toString(glyphObj));
            }
        } else if (button.id == syncSingleButton.id) {
            // Sync Single Value to FontRender Defaults
            if (lastScannedChar > 0 && lastScannedChar < StringUtils.MC_CHAR_WIDTH.length && !ModUtils.TRANSLATOR.isUnicode) {
                StringUtils.MC_CHAR_WIDTH[lastScannedChar] = mc.fontRenderer.getStringWidth(Character.toString(lastScannedChar));
            } else if (StringUtils.MC_GLYPH_WIDTH[lastScannedChar] != 0) {
                StringUtils.MC_GLYPH_WIDTH[lastScannedChar & 255] = (byte) mc.fontRenderer.getStringWidth(Character.toString(lastScannedChar));
            }
        } else if (button.id == resetCharsButton.id) {
            ModUtils.loadCharData(true);
        }
    }

    private void checkValues() {
        if (!Arrays.equals(originalCharArray, StringUtils.MC_CHAR_WIDTH) || !Arrays.equals(originalGlyphArray, StringUtils.MC_GLYPH_WIDTH)) {
            // Write to Char Data and Re-Set originalCharArray and originalGlyphArray
            ModUtils.writeToCharData();
            originalCharArray = StringUtils.MC_CHAR_WIDTH.clone();
            originalGlyphArray = StringUtils.MC_GLYPH_WIDTH.clone();

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
            CraftPresence.GUIS.openScreen(parentScreen);
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
