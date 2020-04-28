package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import net.minecraft.client.gui.GuiScreen;

import java.util.Arrays;
import java.util.List;

public class CharacterEditorGui extends ExtendedScreen {
    private ExtendedButtonControl saveButton, syncSingleButton;
    private ExtendedTextControl charInput, charWidth;
    private String lastScannedString;
    private char lastScannedChar;

    private int[] originalCharArray = StringUtils.MC_CHAR_WIDTH.clone();
    private byte[] originalGlyphArray = StringUtils.MC_GLYPH_WIDTH.clone();

    CharacterEditorGui(GuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initGui() {
        charInput = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                )
        );
        charWidth = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(2),
                        180, 20
                )
        );
        charInput.setMaxStringLength(1);
        charWidth.setMaxStringLength(2);

        final ExtendedButtonControl resetCharsButton = addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, (height - 30),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.reset"),
                        () -> ModUtils.loadCharData(true, "UTF-8")
                )
        );
        final ExtendedButtonControl syncAllButton = addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90,
                        resetCharsButton.y - 25,
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.sync.all"),
                        () -> {
                            // Sync ALL Values to FontRender Defaults
                            for (int currentCharIndex = 0; currentCharIndex < StringUtils.MC_CHAR_WIDTH.length - 1; currentCharIndex++) {
                                final char characterObj = (char) (currentCharIndex);
                                StringUtils.MC_CHAR_WIDTH[currentCharIndex] = mc.fontRenderer.getStringWidth(Character.toString(characterObj));
                            }

                            for (int currentGlyphIndex = 0; currentGlyphIndex < StringUtils.MC_GLYPH_WIDTH.length - 1; currentGlyphIndex++) {
                                final char glyphObj = (char) (currentGlyphIndex & 255);
                                StringUtils.MC_GLYPH_WIDTH[currentGlyphIndex] = (byte) mc.fontRenderer.getStringWidth(Character.toString(glyphObj));
                            }
                        }
                )
        );
        syncSingleButton = addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, syncAllButton.y - 25,
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.sync.single", charInput.getText()),
                        () -> {
                            // Sync Single Value to FontRender Defaults
                            if (lastScannedChar > 0 && lastScannedChar < StringUtils.MC_CHAR_WIDTH.length && !ModUtils.TRANSLATOR.isUnicode) {
                                StringUtils.MC_CHAR_WIDTH[lastScannedChar] = mc.fontRenderer.getStringWidth(Character.toString(lastScannedChar));
                            } else if (StringUtils.MC_GLYPH_WIDTH[lastScannedChar] != 0) {
                                StringUtils.MC_GLYPH_WIDTH[lastScannedChar & 255] = (byte) mc.fontRenderer.getStringWidth(Character.toString(lastScannedChar));
                            }
                        }
                )
        );

        // Adding Back Button
        addControl(
                new ExtendedButtonControl(
                        5, (height - 30),
                        100, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"),
                        () -> CraftPresence.GUIS.openScreen(parentScreen)
                )
        );
        saveButton = addControl(
                new ExtendedButtonControl(
                        width - 105, (height - 30),
                        100, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.save"),
                        () -> {
                            final Tuple<Boolean, Integer> charData = StringUtils.getValidInteger(charWidth.getText());

                            if (charData.getFirst()) {
                                // Save Single Value if Char Data is a Valid Number
                                final int characterWidth = charData.getSecond();
                                if (lastScannedChar > 0 && lastScannedChar <= StringUtils.MC_CHAR_WIDTH.length && !ModUtils.TRANSLATOR.isUnicode) {
                                    StringUtils.MC_CHAR_WIDTH[lastScannedChar] = characterWidth;
                                } else if (StringUtils.MC_GLYPH_WIDTH[lastScannedChar] != 0) {
                                    StringUtils.MC_GLYPH_WIDTH[lastScannedChar & 255] = (byte) characterWidth;
                                }
                            }
                        }
                )
        );

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        preDraw();
        checkValues();

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.editor.character");
        final String charInputTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.charinput");
        final String charWidthTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.charwidth");
        final List<String> notice = StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.character.notice"));

        drawNotice(notice, 2, 2);

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);
        drawString(mc.fontRenderer, charInputTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);

        if (StringUtils.isNullOrEmpty(charInput.getText())) {
            charWidth.setText("");
            charWidth.setVisible(false);
            charWidth.setEnabled(charWidth.getVisible());

            syncSingleButton.enabled = false;
        }

        if (!charInput.getText().equals(lastScannedString)) {
            lastScannedString = charInput.getText();

            if (!StringUtils.isNullOrEmpty(lastScannedString)) {
                lastScannedChar = lastScannedString.charAt(0);
                charWidth.setText(Integer.toString(StringUtils.getStringWidth(lastScannedString)));
                charWidth.setVisible(true);
                charWidth.setEnabled(charWidth.getVisible());

                syncSingleButton.enabled = true;
            } else {
                lastScannedChar = Character.UNASSIGNED;
            }
        }

        if (charWidth.getVisible()) {
            // Only Draw string for Character Width when it's enabled
            drawString(mc.fontRenderer, charWidthTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(2) + 10, 0xFFFFFF);
        }

        syncSingleButton.displayString = ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.sync.single", charInput.getText());
        saveButton.enabled = syncSingleButton.enabled;
        syncSingleButton.visible = syncSingleButton.enabled;
        saveButton.visible = syncSingleButton.visible;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Checks and Modifies Character and Glyph Arrays, depending on conditions
     */
    private void checkValues() {
        if (!Arrays.equals(originalCharArray, StringUtils.MC_CHAR_WIDTH) || !Arrays.equals(originalGlyphArray, StringUtils.MC_GLYPH_WIDTH)) {
            // Write to Char Data and Re-Set originalCharArray and originalGlyphArray
            ModUtils.writeToCharData("UTF-8");
            originalCharArray = StringUtils.MC_CHAR_WIDTH.clone();
            originalGlyphArray = StringUtils.MC_GLYPH_WIDTH.clone();

            // Clear Data for Next Entry
            lastScannedString = null;
            lastScannedChar = Character.UNASSIGNED;
            charInput.setText("");
            charWidth.setText("");
        }
    }
}
