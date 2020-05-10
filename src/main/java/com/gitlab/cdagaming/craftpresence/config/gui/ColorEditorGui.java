/*
 * MIT License
 *
 * Copyright (c) 2018 - 2020 CDAGaming (cstack2011@yahoo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.SliderControl;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ColorEditorGui extends ExtendedScreen {
    private final String configValueName;
    private int pageNumber;
    private ExtendedButtonControl proceedButton, nextPageButton, previousPageButton;
    // Page 1 Variables
    private String currentNormalHexValue, currentConvertedHexValue, startingHexValue;
    private int currentRed, currentGreen, currentBlue, currentAlpha;
    private ExtendedTextControl hexText;
    private SliderControl redText, greenText, blueText, alphaText;
    // Page 2 Variables
    private String currentNormalMCTexturePath, currentConvertedMCTexturePath, startingMCTexturePath;
    private ExtendedTextControl mcTextureText;
    private ResourceLocation currentMCTexture;

    ColorEditorGui(GuiScreen parentScreen, String configValueName) {
        super(parentScreen);
        this.pageNumber = 0;
        this.configValueName = configValueName;
    }

    @Override
    public void initGui() {
        final int calc1 = (width / 2) - 183;
        final int calc2 = (width / 2) + 3;

        // Page 1 Items
        final String redTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.redcolorvalue");
        final String greenTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.greencolorvalue");
        final String blueTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.bluecolorvalue");
        final String alphaTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.alphacolorvalue");

        hexText = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1) + 15,
                        180, 20
                )
        );

        redText = addControl(
                new SliderControl(
                        new Tuple<>(calc1, CraftPresence.GUIS.getButtonY(3)),
                        new Tuple<>(180, 20),
                        1.0f, 0.0f,
                        255.0f, 1.0f,
                        redTitle
                )
        );
        greenText = addControl(
                new SliderControl(
                        new Tuple<>(calc2, CraftPresence.GUIS.getButtonY(3)),
                        new Tuple<>(180, 20),
                        1.0f, 0.0f,
                        255.0f, 1.0f,
                        greenTitle
                )
        );
        blueText = addControl(
                new SliderControl(
                        new Tuple<>(calc1, CraftPresence.GUIS.getButtonY(4)),
                        new Tuple<>(180, 20),
                        1.0f, 0.0f,
                        255.0f, 1.0f,
                        blueTitle
                )
        );
        alphaText = addControl(
                new SliderControl(
                        new Tuple<>(calc2, CraftPresence.GUIS.getButtonY(4)),
                        new Tuple<>(180, 20),
                        1.0f, 0.0f,
                        255.0f, 1.0f,
                        alphaTitle
                )
        );

        // Page 2 Items
        mcTextureText = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1) + 15,
                        180, 20
                )
        );
        mcTextureText.setMaxStringLength(512);

        proceedButton = addControl(
                new ExtendedButtonControl(
                        10, (height - 30),
                        80, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"),
                        () -> {
                            syncValues();

                            if (!StringUtils.isNullOrEmpty(configValueName)) {
                                if (pageNumber == 0) {
                                    if (configValueName.equals(CraftPresence.CONFIG.NAME_tooltipBGColor) && !currentNormalHexValue.equals(CraftPresence.CONFIG.tooltipBGColor)) {
                                        CraftPresence.CONFIG.hasChanged = true;
                                        CraftPresence.CONFIG.tooltipBGColor = currentNormalHexValue;
                                    } else if (configValueName.equals(CraftPresence.CONFIG.NAME_tooltipBorderColor) && !currentNormalHexValue.equals(CraftPresence.CONFIG.tooltipBorderColor)) {
                                        CraftPresence.CONFIG.hasChanged = true;
                                        CraftPresence.CONFIG.tooltipBorderColor = currentNormalHexValue;
                                    } else if (configValueName.equals(CraftPresence.CONFIG.NAME_guiBGColor) && !currentNormalHexValue.equals(CraftPresence.CONFIG.guiBGColor)) {
                                        CraftPresence.CONFIG.hasChanged = true;
                                        CraftPresence.CONFIG.guiBGColor = currentNormalHexValue;
                                    }
                                }

                                if (pageNumber == 1) {
                                    if (configValueName.equals(CraftPresence.CONFIG.NAME_tooltipBGColor) && !currentNormalMCTexturePath.equals(CraftPresence.CONFIG.tooltipBGColor.replace(CraftPresence.CONFIG.splitCharacter, ":"))) {
                                        CraftPresence.CONFIG.hasChanged = true;
                                        CraftPresence.CONFIG.tooltipBGColor = currentNormalMCTexturePath.replace(":", CraftPresence.CONFIG.splitCharacter);
                                    } else if (configValueName.equals(CraftPresence.CONFIG.NAME_tooltipBorderColor) && !currentNormalMCTexturePath.equals(CraftPresence.CONFIG.tooltipBorderColor.replace(CraftPresence.CONFIG.splitCharacter, ":"))) {
                                        CraftPresence.CONFIG.hasChanged = true;
                                        CraftPresence.CONFIG.tooltipBorderColor = currentNormalMCTexturePath.replace(":", CraftPresence.CONFIG.splitCharacter);
                                    } else if (configValueName.equals(CraftPresence.CONFIG.NAME_guiBGColor) && !currentNormalMCTexturePath.equals(CraftPresence.CONFIG.guiBGColor.replace(CraftPresence.CONFIG.splitCharacter, ":"))) {
                                        CraftPresence.CONFIG.hasChanged = true;
                                        CraftPresence.CONFIG.guiBGColor = currentNormalMCTexturePath.replace(":", CraftPresence.CONFIG.splitCharacter);
                                    }
                                }
                            }

                            CraftPresence.GUIS.openScreen(parentScreen);
                        }
                )
        );
        previousPageButton = addControl(
                new ExtendedButtonControl(
                        (proceedButton.x + proceedButton.getWidth()) + 3, (height - 30),
                        20, 20,
                        "<",
                        () -> {
                            if (pageNumber != 0) {
                                pageNumber--;
                                initValues();
                                syncValues();
                            }
                        }
                )
        );
        nextPageButton = addControl(
                new ExtendedButtonControl(
                        (previousPageButton.x + previousPageButton.getWidth()) + 3, (height - 30),
                        20, 20,
                        ">",
                        () -> {
                            if (pageNumber != 1) {
                                pageNumber++;
                                initValues();
                                syncValues();
                            }
                        }
                )
        );

        initValues();
        syncValues();
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        preDraw();

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.editor.color", configValueName.replaceAll("_", " "));
        final String previewTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.preview");
        final String noticeTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.refresh");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringUtils.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);
        drawString(mc.fontRenderer, previewTitle, width - 90, height - 25, 0xFFFFFF);
        drawString(mc.fontRenderer, noticeTitle, (width / 2) - 90, CraftPresence.GUIS.getButtonY(1) - 5, 0xFFFFFF);

        // Ensure Button Activity on Page 1
        hexText.setVisible(pageNumber == 0);
        hexText.setEnabled(hexText.getVisible());

        redText.enabled = pageNumber == 0;
        redText.visible = redText.enabled;
        greenText.enabled = pageNumber == 0;
        greenText.visible = greenText.enabled;
        blueText.enabled = pageNumber == 0;
        blueText.visible = blueText.enabled;
        alphaText.enabled = pageNumber == 0;
        alphaText.visible = alphaText.enabled;

        // Ensure Button Activity on Page 2
        mcTextureText.setVisible(pageNumber == 1);
        mcTextureText.setEnabled(mcTextureText.getVisible());

        // Page 1 Items
        if (pageNumber == 0) {
            final String hexCodeTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.hexcode");

            drawString(mc.fontRenderer, hexCodeTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 20, 0xFFFFFF);

            proceedButton.enabled = !StringUtils.isNullOrEmpty(hexText.getText());

            CraftPresence.GUIS.drawGradientRect(300, width - 45, height - 45, width - 1, height - 2, currentConvertedHexValue, currentConvertedHexValue);
        }

        // Page 2 Items
        if (pageNumber == 1) {
            final String mcTextureTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.texturepath");

            drawString(mc.fontRenderer, mcTextureTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 20, 0xFFFFFF);

            proceedButton.enabled = !StringUtils.isNullOrEmpty(mcTextureText.getText());

            if (currentMCTexture == null) {
                currentMCTexture = new ResourceLocation("");
            }
            CraftPresence.GUIS.drawTextureRect(0.0D, width - 45, height - 45, 44, 43, 0, currentMCTexture);
        }

        previousPageButton.enabled = pageNumber != 0;
        nextPageButton.enabled = pageNumber != 1;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_UP && pageNumber != 0) {
            pageNumber--;
            initValues();
            syncValues();
        }

        if (keyCode == Keyboard.KEY_DOWN && pageNumber != 1) {
            pageNumber++;
            initValues();
            syncValues();
        }

        if (keyCode == Keyboard.KEY_NUMPADENTER || keyCode == Keyboard.KEY_RETURN) {
            syncValues();
        }
        super.keyTyped(typedChar, keyCode);
    }

    /**
     * Initialize Texture and Color Values for Initial Preview and Page
     */
    private void initValues() {
        if (!StringUtils.isNullOrEmpty(configValueName)) {
            if (configValueName.equals(CraftPresence.CONFIG.NAME_tooltipBGColor)) {
                if (StringUtils.isValidColorCode(CraftPresence.CONFIG.tooltipBGColor)) {
                    startingHexValue = CraftPresence.CONFIG.tooltipBGColor;
                } else if (!StringUtils.isNullOrEmpty(CraftPresence.CONFIG.tooltipBGColor)) {
                    startingMCTexturePath = CraftPresence.CONFIG.tooltipBGColor;
                }
            } else if (configValueName.equals(CraftPresence.CONFIG.NAME_tooltipBorderColor)) {
                if (StringUtils.isValidColorCode(CraftPresence.CONFIG.tooltipBorderColor)) {
                    startingHexValue = CraftPresence.CONFIG.tooltipBorderColor;
                } else if (!StringUtils.isNullOrEmpty(CraftPresence.CONFIG.tooltipBGColor)) {
                    startingMCTexturePath = CraftPresence.CONFIG.tooltipBorderColor;
                }
            } else if (configValueName.equals(CraftPresence.CONFIG.NAME_guiBGColor)) {
                if (StringUtils.isValidColorCode(CraftPresence.CONFIG.guiBGColor)) {
                    startingHexValue = CraftPresence.CONFIG.guiBGColor;
                } else if (!StringUtils.isNullOrEmpty(CraftPresence.CONFIG.guiBGColor)) {
                    startingMCTexturePath = CraftPresence.CONFIG.guiBGColor;
                }
            }

            if (StringUtils.isNullOrEmpty(hexText.getText()) && !StringUtils.isNullOrEmpty(startingHexValue)) {
                hexText.setText(startingHexValue);
                currentNormalHexValue = null;
                currentConvertedHexValue = null;
                currentConvertedMCTexturePath = null;
                currentMCTexture = new ResourceLocation("");
                pageNumber = 0;
            } else if (StringUtils.isNullOrEmpty(mcTextureText.getText()) && !StringUtils.isNullOrEmpty(startingMCTexturePath)) {
                mcTextureText.setText(startingMCTexturePath);
                currentNormalHexValue = null;
                currentConvertedHexValue = null;
                currentConvertedMCTexturePath = null;
                currentMCTexture = new ResourceLocation("");
                pageNumber = 1;
            }
        }
    }

    /**
     * Synchronize RGBA, Hex, and Texture Settings for Preview
     */
    private void syncValues() {
        // Page 1 - RGBA / Hex Syncing
        if (pageNumber == 0) {
            Integer localValue = null;
            Color localColor;

            if (!StringUtils.isNullOrEmpty(hexText.getText())) {
                if (hexText.getText().startsWith("#") || hexText.getText().length() == 6) {
                    localValue = StringUtils.getColorFromHex(hexText.getText()).getRGB();
                } else if (hexText.getText().startsWith("0x")) {
                    localColor = new Color(Long.decode(hexText.getText()).intValue(), true);
                    localValue = localColor.getRGB();
                } else if (StringUtils.getValidInteger(hexText.getText()).getFirst()) {
                    localValue = Integer.decode(hexText.getText());
                }
            }

            if (localValue != null && !Integer.toString(localValue).equals(currentConvertedHexValue)) {
                currentAlpha = (localValue >> 24 & 255);
                currentRed = (localValue >> 16 & 255);
                currentGreen = (localValue >> 8 & 255);
                currentBlue = (localValue & 255);

                alphaText.setSliderValue(currentAlpha);
                redText.setSliderValue(currentRed);
                greenText.setSliderValue(currentGreen);
                blueText.setSliderValue(currentBlue);

                currentNormalHexValue = hexText.getText();
                currentConvertedHexValue = Integer.toString(localValue);
            } else {
                boolean isRedDifferent = alphaText.getSliderValue(false) != currentRed,
                        isGreenDifferent = greenText.getSliderValue(false) != currentGreen,
                        isBlueDifferent = blueText.getSliderValue(false) != currentBlue,
                        isAlphaDifferent = alphaText.getSliderValue(false) != currentAlpha;

                // Determine if any Values DO need updates
                if (isRedDifferent || isGreenDifferent || isBlueDifferent || isAlphaDifferent) {
                    currentRed = (int) redText.getSliderValue(false) & 255;
                    currentGreen = (int) greenText.getSliderValue(false) & 255;
                    currentBlue = (int) blueText.getSliderValue(false) & 255;
                    currentAlpha = (int) alphaText.getSliderValue(false) & 255;

                    localColor = new Color(currentRed, currentGreen, currentBlue, currentAlpha);

                    currentNormalHexValue = StringUtils.getHexFromColor(localColor);
                    hexText.setText(currentNormalHexValue);

                    currentConvertedHexValue = Long.toString(Long.decode(currentNormalHexValue).intValue());
                }
            }
        }

        // Page 2 - MC Texture Syncing
        if (pageNumber == 1) {
            if (!StringUtils.isNullOrEmpty(mcTextureText.getText())) {
                if (mcTextureText.getText().contains(CraftPresence.CONFIG.splitCharacter)) {
                    mcTextureText.setText(mcTextureText.getText().replace(CraftPresence.CONFIG.splitCharacter, ":"));
                }

                if (mcTextureText.getText().contains(":") && !mcTextureText.getText().startsWith(":")) {
                    currentNormalMCTexturePath = mcTextureText.getText();
                } else if (mcTextureText.getText().startsWith(":")) {
                    currentNormalMCTexturePath = mcTextureText.getText().substring(1);
                } else {
                    currentNormalMCTexturePath = "minecraft:" + mcTextureText.getText();
                }
                currentConvertedMCTexturePath = currentNormalMCTexturePath;

                if (currentConvertedMCTexturePath.contains(":")) {
                    String[] splitInput = currentConvertedMCTexturePath.split(":", 2);
                    currentMCTexture = new ResourceLocation(splitInput[0], splitInput[1]);
                } else {
                    currentMCTexture = new ResourceLocation(currentConvertedMCTexturePath);
                }
            } else {
                currentMCTexture = new ResourceLocation("");
            }
        }
    }
}