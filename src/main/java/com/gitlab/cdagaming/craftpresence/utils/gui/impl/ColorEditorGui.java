/*
 * MIT License
 *
 * Copyright (c) 2018 - 2021 CDAGaming (cstack2011@yahoo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gitlab.cdagaming.craftpresence.utils.gui.impl;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.DataConsumer;
import com.gitlab.cdagaming.craftpresence.impl.Pair;
import com.gitlab.cdagaming.craftpresence.impl.PairConsumer;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.ImageUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.SliderControl;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.File;

@SuppressWarnings("DuplicatedCode")
public class ColorEditorGui extends ExtendedScreen {
    private final String configValueName;
    // Event Data
    private final PairConsumer<Integer, ColorEditorGui> onAdjustEntry;
    private final DataConsumer<ColorEditorGui> onInit;
    public String currentNormalHexValue, startingHexValue, currentNormalTexturePath, startingTexturePath;
    public boolean usingExternalTexture = false;
    // Global Data
    private int pageNumber;
    private ExtendedButtonControl proceedButton, nextPageButton, previousPageButton;
    // Page 1 Variables
    private String currentConvertedHexValue;
    private int currentRed, currentGreen, currentBlue, currentAlpha;
    private ExtendedTextControl hexText;
    private SliderControl redText, greenText, blueText, alphaText;
    // Page 2 Variables
    private String currentConvertedTexturePath;
    private ExtendedTextControl textureText;
    private boolean isModified = false;
    private ResourceLocation currentTexture;

    public ColorEditorGui(GuiScreen parentScreen, String configValueName, PairConsumer<Integer, ColorEditorGui> onAdjustEntry, DataConsumer<ColorEditorGui> onInit) {
        super(parentScreen);
        this.configValueName = configValueName;
        this.pageNumber = 0;
        this.onAdjustEntry = onAdjustEntry;
        this.onInit = onInit;
    }

    @Override
    public void initializeUi() {
        final int calc1 = (width / 2) - 183;
        final int calc2 = (width / 2) + 3;

        // Page 1 Items
        final String redTitle = ModUtils.TRANSLATOR.translate("gui.config.message.editor.color.value.red");
        final String greenTitle = ModUtils.TRANSLATOR.translate("gui.config.message.editor.color.value.green");
        final String blueTitle = ModUtils.TRANSLATOR.translate("gui.config.message.editor.color.value.blue");
        final String alphaTitle = ModUtils.TRANSLATOR.translate("gui.config.message.editor.color.value.alpha");

        hexText = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        calc2, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                )
        );
        hexText.setMaxStringLength(10);

        redText = addControl(
                new SliderControl(
                        new Pair<>(calc1, CraftPresence.GUIS.getButtonY(2)),
                        new Pair<>(180, 20),
                        1.0f, 0.0f,
                        255.0f, 1.0f,
                        redTitle,
                        new Tuple<>(
                                this::syncValues,
                                () -> {
                                },
                                this::syncValues
                        )
                )
        );
        greenText = addControl(
                new SliderControl(
                        new Pair<>(calc2, CraftPresence.GUIS.getButtonY(2)),
                        new Pair<>(180, 20),
                        1.0f, 0.0f,
                        255.0f, 1.0f,
                        greenTitle,
                        new Tuple<>(
                                this::syncValues,
                                () -> {
                                },
                                this::syncValues
                        )
                )
        );
        blueText = addControl(
                new SliderControl(
                        new Pair<>(calc1, CraftPresence.GUIS.getButtonY(3)),
                        new Pair<>(180, 20),
                        1.0f, 0.0f,
                        255.0f, 1.0f,
                        blueTitle,
                        new Tuple<>(
                                this::syncValues,
                                () -> {
                                },
                                this::syncValues
                        )
                )
        );
        alphaText = addControl(
                new SliderControl(
                        new Pair<>(calc2, CraftPresence.GUIS.getButtonY(3)),
                        new Pair<>(180, 20),
                        1.0f, 0.0f,
                        255.0f, 1.0f,
                        alphaTitle,
                        new Tuple<>(
                                this::syncValues,
                                () -> {
                                },
                                this::syncValues
                        )
                )
        );

        // Page 2 Items
        textureText = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        calc2, CraftPresence.GUIS.getButtonY(1),
                        180, 20,
                        this::syncValues
                )
        );
        textureText.setMaxStringLength(32767);

        proceedButton = addControl(
                new ExtendedButtonControl(
                        10, (height - 30),
                        80, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.back"),
                        () -> {
                            syncValues();
                            if (isModified && onAdjustEntry != null) {
                                onAdjustEntry.accept(pageNumber, this);
                            }
                            CraftPresence.GUIS.openScreen(parentScreen);
                        }
                )
        );
        previousPageButton = addControl(
                new ExtendedButtonControl(
                        (proceedButton.getControlPosX() + proceedButton.getControlWidth()) + 3, (height - 30),
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
                        (previousPageButton.getControlPosX() + previousPageButton.getControlWidth()) + 3, (height - 30),
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
        super.initializeUi();
    }

    @Override
    public void preRender() {
        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.editor.color", configValueName.replaceAll("_", " "));
        final String previewTitle = ModUtils.TRANSLATOR.translate("gui.config.message.editor.preview");

        renderString(mainTitle, (width / 2f) - (StringUtils.getStringWidth(mainTitle) / 2f), 10, 0xFFFFFF);
        renderString(previewTitle, width - 90, height - 29.5f, 0xFFFFFF);

        // Ensure Button Activity on Page 1
        hexText.setVisible(pageNumber == 0);
        hexText.setEnabled(hexText.getVisible());

        redText.setControlEnabled(pageNumber == 0);
        redText.setControlVisible(redText.isControlEnabled());
        greenText.setControlEnabled(pageNumber == 0);
        greenText.setControlVisible(greenText.isControlEnabled());
        blueText.setControlEnabled(pageNumber == 0);
        blueText.setControlVisible(blueText.isControlEnabled());
        alphaText.setControlEnabled(pageNumber == 0);
        alphaText.setControlVisible(alphaText.isControlEnabled());

        // Ensure Button Activity on Page 2
        textureText.setVisible(pageNumber == 1);
        textureText.setEnabled(textureText.getVisible());

        // Setup Data for Drawing
        double tooltipX = width - 45;
        double tooltipY = height - 45;
        double tooltipHeight = 40;
        double tooltipTextWidth = 40;

        String borderColor = "#000000";
        String borderColorEnd = "#000000";

        // Page 1 Items
        if (pageNumber == 0) {
            final String hexCodeTitle = ModUtils.TRANSLATOR.translate("gui.config.message.editor.hex_code");

            renderString(hexCodeTitle, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(1, 5), 0xFFFFFF);

            proceedButton.setControlEnabled(!StringUtils.isNullOrEmpty(hexText.getText()));

            // Draw Preview Box
            CraftPresence.GUIS.drawGradientRect(300, tooltipX - 3, tooltipY - 3, width - 2, height - 2, currentConvertedHexValue, currentConvertedHexValue);
        }

        // Page 2 Items
        if (pageNumber == 1) {
            final String textureTitle = ModUtils.TRANSLATOR.translate("gui.config.message.editor.texture_path");

            renderString(textureTitle, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(1, 5), 0xFFFFFF);

            proceedButton.setControlEnabled(!StringUtils.isNullOrEmpty(textureText.getText()));

            if (currentTexture == null) {
                currentTexture = new ResourceLocation("");
            }

            // Ensure the Texture is refreshed consistently, if an external texture
            double widthDivider = 32.0D, heightDivider = 32.0D;

            if (usingExternalTexture) {
                final String formattedConvertedName = currentConvertedTexturePath.replaceFirst("file://", "");
                final String[] urlBits = formattedConvertedName.split("/");
                final String textureName = urlBits[urlBits.length - 1].trim();
                currentTexture = ImageUtils.getTextureFromUrl(textureName, currentConvertedTexturePath.toLowerCase().startsWith("file://") ? new File(formattedConvertedName) : formattedConvertedName);

                widthDivider = 44;
                heightDivider = 43;
            }

            // Draw Preview Box
            CraftPresence.GUIS.drawTextureRect(0.0D, width - 47, height - 47, 44, 44, 0, widthDivider, heightDivider, false, currentTexture);
        }

        // Draw Border around Preview Box
        CraftPresence.GUIS.drawGradientRect(300, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColor, borderColorEnd);
        CraftPresence.GUIS.drawGradientRect(300, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColor, borderColorEnd);
        CraftPresence.GUIS.drawGradientRect(300, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColor, borderColor);
        CraftPresence.GUIS.drawGradientRect(300, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

        previousPageButton.setControlEnabled(pageNumber != 0);
        nextPageButton.setControlEnabled(pageNumber != 1);

        proceedButton.setControlMessage(isModified ? ModUtils.TRANSLATOR.translate("gui.config.message.button.save") : ModUtils.TRANSLATOR.translate("gui.config.message.button.back"));
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
        super.keyTyped(typedChar, keyCode);
    }

    /**
     * Initialize Texture and Color Values for Initial Preview and Page
     */
    private void initValues() {
        if (onInit != null) {
            onInit.accept(this);

            if (StringUtils.isNullOrEmpty(hexText.getText()) && !StringUtils.isNullOrEmpty(startingHexValue)) {
                hexText.setText(startingHexValue);
                currentNormalHexValue = null;
                currentConvertedHexValue = null;
                currentConvertedTexturePath = null;
                currentTexture = new ResourceLocation("");
                pageNumber = 0;
            } else if (StringUtils.isNullOrEmpty(textureText.getText()) && !StringUtils.isNullOrEmpty(startingTexturePath)) {
                textureText.setText(startingTexturePath);
                currentNormalHexValue = null;
                currentConvertedHexValue = null;
                currentConvertedTexturePath = null;
                currentTexture = new ResourceLocation("");
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
                    try {
                        localColor = new Color(Long.decode(hexText.getText()).intValue(), true);
                        localValue = localColor.getRGB();
                    } catch (Exception ignored) {
                    }
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
                final boolean isRedDifferent = redText.isDragging() && redText.getSliderValue(false) != currentRed,
                        isGreenDifferent = greenText.isDragging() && greenText.getSliderValue(false) != currentGreen,
                        isBlueDifferent = blueText.isDragging() && blueText.getSliderValue(false) != currentBlue,
                        isAlphaDifferent = alphaText.isDragging() && alphaText.getSliderValue(false) != currentAlpha;

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
            isModified = !hexText.getText().equals(startingHexValue);
        }

        // Page 2 - Texture Syncing
        if (pageNumber == 1) {
            if (!StringUtils.isNullOrEmpty(textureText.getText())) {
                usingExternalTexture = ImageUtils.isExternalImage(textureText.getText());

                // Only Perform Texture Conversion Steps if not an external Url
                // As an external Url should be parsed as-is in most use cases
                if (!usingExternalTexture) {
                    if (textureText.getText().contains(CraftPresence.CONFIG.splitCharacter)) {
                        textureText.setText(textureText.getText().replace(CraftPresence.CONFIG.splitCharacter, ":"));
                    }

                    if (textureText.getText().contains(":") && !textureText.getText().startsWith(":")) {
                        currentNormalTexturePath = textureText.getText();
                    } else if (textureText.getText().startsWith(":")) {
                        currentNormalTexturePath = textureText.getText().substring(1);
                    } else {
                        currentNormalTexturePath = "minecraft:" + textureText.getText();
                    }
                } else {
                    currentNormalTexturePath = textureText.getText();
                }

                currentConvertedTexturePath = currentNormalTexturePath.trim();

                // Only when we are not using an external texture, would we then need
                // to convert the path to Minecraft's normal format.
                //
                // If we are using an external texture however, then we'd just make
                // a texture name from the last part of the url and retrieve the external texture
                if (!usingExternalTexture) {
                    if (currentConvertedTexturePath.contains(":")) {
                        final String[] splitInput = currentConvertedTexturePath.split(":", 2);
                        currentTexture = new ResourceLocation(splitInput[0], splitInput[1]);
                    } else {
                        currentTexture = new ResourceLocation(currentConvertedTexturePath);
                    }
                } else {
                    final String formattedConvertedName = currentConvertedTexturePath.replaceFirst("file://", "");
                    final String[] urlBits = formattedConvertedName.trim().split("/");
                    final String textureName = urlBits[urlBits.length - 1].trim();
                    currentTexture = ImageUtils.getTextureFromUrl(textureName, currentConvertedTexturePath.toLowerCase().startsWith("file://") ? new File(formattedConvertedName) : formattedConvertedName);
                }
            } else {
                currentTexture = new ResourceLocation("");
            }
            isModified = !textureText.getText().equals(startingTexturePath.replace(CraftPresence.CONFIG.splitCharacter, ":"));
        }
    }
}
