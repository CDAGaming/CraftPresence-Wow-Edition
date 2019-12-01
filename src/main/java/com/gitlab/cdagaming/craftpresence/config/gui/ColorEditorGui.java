package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.SliderControl;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class ColorEditorGui extends GuiScreen {
    private final GuiScreen parentScreen;
    private int pageNumber;
    private ExtendedButtonControl proceedButton, nextPageButton, previousPageButton;
    private String configValueName;

    // Page 1 Variables
    private String currentNormalHexValue, currentConvertedHexValue, startingHexValue;
    private int currentRed, currentGreen, currentBlue, currentAlpha;
    private GuiTextField hexText;
    private SliderControl redText, greenText, blueText, alphaText;
    // Page 2 Variables
    private String currentNormalMCTexturePath, currentConvertedMCTexturePath, startingMCTexturePath;
    private GuiTextField mcTextureText;
    private ResourceLocation currentMCTexture;

    ColorEditorGui(GuiScreen parentScreen, String configValueName) {
        mc = CraftPresence.instance;
        pageNumber = 0;
        this.parentScreen = parentScreen;
        this.configValueName = configValueName;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        int calc1 = (width / 2) - 183;
        int calc2 = (width / 2) + 3;

        // Page 1 Items
        final String redTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.redcolorvalue");
        final String greenTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.greencolorvalue");
        final String blueTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.bluecolorvalue");
        final String alphaTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.alphacolorvalue");

        hexText = new GuiTextField(100, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1) + 15, 180, 20);

        redText = new SliderControl(110, new Tuple<>(calc1, CraftPresence.GUIS.getButtonY(3)), new Tuple<>(180, 20), 1.0f, 0.0f, 255.0f, 1.0f, redTitle);
        greenText = new SliderControl(120, new Tuple<>(calc2, CraftPresence.GUIS.getButtonY(3)), new Tuple<>(180, 20), 1.0f, 0.0f, 255.0f, 1.0f, greenTitle);
        blueText = new SliderControl(130, new Tuple<>(calc1, CraftPresence.GUIS.getButtonY(4)), new Tuple<>(180, 20), 1.0f, 0.0f, 255.0f, 1.0f, blueTitle);
        alphaText = new SliderControl(140, new Tuple<>(calc2, CraftPresence.GUIS.getButtonY(4)), new Tuple<>(180, 20), 1.0f, 0.0f, 255.0f, 1.0f, alphaTitle);

        buttonList.add(redText);
        buttonList.add(greenText);
        buttonList.add(blueText);
        buttonList.add(alphaText);

        // Page 2 Items
        mcTextureText = new GuiTextField(150, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1) + 15, 180, 20);
        mcTextureText.setMaxStringLength(512);

        proceedButton = new ExtendedButtonControl(700, 10, (height - 30), 80, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"));
        previousPageButton = new ExtendedButtonControl(800, (proceedButton.x + proceedButton.getWidth()) + 3, (height - 30), 20, 20, "<");
        nextPageButton = new ExtendedButtonControl(900, (previousPageButton.x + previousPageButton.getWidth()) + 3, (height - 30), 20, 20, ">");

        buttonList.add(previousPageButton);
        buttonList.add(nextPageButton);
        buttonList.add(proceedButton);

        initValues();
        syncValues();
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.editor.color", configValueName.replaceAll("_", " "));
        final String previewTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.preview");
        final String noticeTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.refresh");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringUtils.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);
        drawString(mc.fontRenderer, previewTitle, width - 90, height - 25, 0xFFFFFF);
        drawString(mc.fontRenderer, noticeTitle, (width / 2) - 90, CraftPresence.GUIS.getButtonY(1) - 5, 0xFFFFFF);

        // Ensure Button Activity on Page 1
        redText.enabled = pageNumber == 0;
        redText.visible = redText.enabled;
        greenText.enabled = pageNumber == 0;
        greenText.visible = greenText.enabled;
        blueText.enabled = pageNumber == 0;
        blueText.visible = blueText.enabled;
        alphaText.enabled = pageNumber == 0;
        alphaText.visible = alphaText.enabled;

        // Page 1 Items
        if (pageNumber == 0) {
            final String hexCodeTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.hexcode");

            drawString(mc.fontRenderer, hexCodeTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 20, 0xFFFFFF);

            hexText.drawTextBox();

            proceedButton.enabled = !StringUtils.isNullOrEmpty(hexText.getText());

            CraftPresence.GUIS.drawGradientRect(300, width - 45, height - 45, width - 1, height - 2, currentConvertedHexValue, currentConvertedHexValue);
        }

        // Page 2 Items
        if (pageNumber == 1) {
            final String mcTextureTitle = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.texturepath");

            drawString(mc.fontRenderer, mcTextureTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 20, 0xFFFFFF);

            mcTextureText.drawTextBox();

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
    protected void actionPerformed(GuiButton button) {
        if (button.id == previousPageButton.id && pageNumber != 0) {
            pageNumber--;
            initValues();
            syncValues();
        }
        if (button.id == nextPageButton.id && pageNumber != 1) {
            pageNumber++;
            initValues();
            syncValues();
        }

        if (button.id == proceedButton.id) {
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
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            CraftPresence.GUIS.openScreen(parentScreen);
        }

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

        // Page 1 Items
        if (pageNumber == 0) {
            hexText.textboxKeyTyped(typedChar, keyCode);
        }

        // Page 2 Items
        if (pageNumber == 1) {
            mcTextureText.textboxKeyTyped(typedChar, keyCode);
        }

        if (keyCode == Keyboard.KEY_NUMPADENTER || keyCode == Keyboard.KEY_RETURN) {
            syncValues();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        // Page 1 Items
        if (pageNumber == 0) {
            hexText.mouseClicked(mouseX, mouseY, mouseButton);
        }

        // Page 2 Items
        if (pageNumber == 1) {
            mcTextureText.mouseClicked(mouseX, mouseY, mouseButton);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        // Page 1 Items
        if (pageNumber == 0) {
            hexText.updateCursorCounter();
        }

        // Page 2 Items
        if (pageNumber == 1) {
            mcTextureText.updateCursorCounter();
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

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
