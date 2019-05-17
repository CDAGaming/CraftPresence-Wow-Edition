package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUIExtendedButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class ConfigGUI_ColorEditor extends GuiScreen {
    private final GuiScreen parentScreen;
    private int pageNumber;
    private GUIExtendedButton proceedButton, nextPageButton, previousPageButton;
    private String configValueName;

    // Page 1 Variables
    private String currentNormalHexValue, currentConvertedHexValue, startingHexValue;
    private int currentRed, currentGreen, currentBlue, currentAlpha;
    private GuiTextField hexText, redText, greenText, blueText, alphaText;
    // Page 2 Variables
    private String currentNormalMCTexturePath, currentConvertedMCTexturePath, startingMCTexturePath;
    private GuiTextField mcTextureText;
    private ResourceLocation currentMCTexture;

    ConfigGUI_ColorEditor(GuiScreen parentScreen, String configValueName) {
        mc = CraftPresence.instance;
        pageNumber = 0;
        this.parentScreen = parentScreen;
        this.configValueName = configValueName;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        // Page 1 Items
        hexText = new GuiTextField(100, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1) + 15, 180, 20);

        redText = new GuiTextField(110, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(3), 180, 20);
        greenText = new GuiTextField(120, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(4), 180, 20);
        blueText = new GuiTextField(130, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(5), 180, 20);
        alphaText = new GuiTextField(140, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(6), 180, 20);

        // Page 2 Items
        mcTextureText = new GuiTextField(150, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1) + 15, 180, 20);
        mcTextureText.setMaxStringLength(512);

        proceedButton = new GUIExtendedButton(700, 10, (height - 30), 80, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.back"));
        previousPageButton = new GUIExtendedButton(800, (proceedButton.x + proceedButton.getWidth()) + 3, (height - 30), 20, 20, "<");
        nextPageButton = new GUIExtendedButton(900, (previousPageButton.x + previousPageButton.getWidth()) + 3, (height - 30), 20, 20, ">");

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

        final String mainTitle = Constants.TRANSLATOR.translate("gui.config.title");
        final String subTitle = Constants.TRANSLATOR.translate("gui.config.title.editor.color", configValueName.replaceAll("_", " "));
        final String previewTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.preview");
        final String noticeTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.refresh");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringHandler.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringHandler.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);
        drawString(mc.fontRenderer, previewTitle, width - 90, height - 25, 0xFFFFFF);
        drawString(mc.fontRenderer, noticeTitle, (width / 2) - 90, CraftPresence.GUIS.getButtonY(1) - 5, 0xFFFFFF);

        // Page 1 Items
        if (pageNumber == 0) {
            final String hexCodeTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.hexcode");
            final String redTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.redcolorvalue");
            final String greenTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.greencolorvalue");
            final String blueTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.bluecolorvalue");
            final String alphaTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.alphacolorvalue");

            drawString(mc.fontRenderer, hexCodeTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 20, 0xFFFFFF);
            drawString(mc.fontRenderer, redTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, greenTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(4) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, blueTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(5) + 5, 0xFFFFFF);
            drawString(mc.fontRenderer, alphaTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(6) + 5, 0xFFFFFF);

            hexText.drawTextBox();

            redText.drawTextBox();
            greenText.drawTextBox();
            blueText.drawTextBox();
            alphaText.drawTextBox();

            proceedButton.enabled = !StringHandler.isNullOrEmpty(hexText.getText());

            CraftPresence.GUIS.drawGradientRect(300, width - 45, height - 45, width - 1, height - 2, currentConvertedHexValue, currentConvertedHexValue);
        }

        // Page 2 Items
        if (pageNumber == 1) {
            final String mcTextureTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.texturepath");

            drawString(mc.fontRenderer, mcTextureTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 20, 0xFFFFFF);

            mcTextureText.drawTextBox();

            proceedButton.enabled = !StringHandler.isNullOrEmpty(mcTextureText.getText());

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

            if (!StringHandler.isNullOrEmpty(configValueName)) {
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

            mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parentScreen);
        }

        if (keyCode == Keyboard.KEY_LEFT && pageNumber != 0) {
            pageNumber--;
            initValues();
            syncValues();
        }

        if (keyCode == Keyboard.KEY_RIGHT && pageNumber != 1) {
            pageNumber++;
            initValues();
            syncValues();
        }

        // Page 1 Items
        if (pageNumber == 0) {
            hexText.textboxKeyTyped(typedChar, keyCode);
            redText.textboxKeyTyped(typedChar, keyCode);
            greenText.textboxKeyTyped(typedChar, keyCode);
            blueText.textboxKeyTyped(typedChar, keyCode);
            alphaText.textboxKeyTyped(typedChar, keyCode);
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
            redText.mouseClicked(mouseX, mouseY, mouseButton);
            greenText.mouseClicked(mouseX, mouseY, mouseButton);
            blueText.mouseClicked(mouseX, mouseY, mouseButton);
            alphaText.mouseClicked(mouseX, mouseY, mouseButton);
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
            redText.updateCursorCounter();
            greenText.updateCursorCounter();
            blueText.updateCursorCounter();
            alphaText.updateCursorCounter();
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
        if (!StringHandler.isNullOrEmpty(configValueName)) {
            if (configValueName.equals(CraftPresence.CONFIG.NAME_tooltipBGColor)) {
                if (StringHandler.isValidColorCode(CraftPresence.CONFIG.tooltipBGColor)) {
                    startingHexValue = CraftPresence.CONFIG.tooltipBGColor;
                } else if (!StringHandler.isNullOrEmpty(CraftPresence.CONFIG.tooltipBGColor)) {
                    startingMCTexturePath = CraftPresence.CONFIG.tooltipBGColor;
                }
            } else if (configValueName.equals(CraftPresence.CONFIG.NAME_tooltipBorderColor)) {
                if (StringHandler.isValidColorCode(CraftPresence.CONFIG.tooltipBorderColor)) {
                    startingHexValue = CraftPresence.CONFIG.tooltipBorderColor;
                } else if (!StringHandler.isNullOrEmpty(CraftPresence.CONFIG.tooltipBGColor)) {
                    startingMCTexturePath = CraftPresence.CONFIG.tooltipBorderColor;
                }
            } else if (configValueName.equals(CraftPresence.CONFIG.NAME_guiBGColor)) {
                if (StringHandler.isValidColorCode(CraftPresence.CONFIG.guiBGColor)) {
                    startingHexValue = CraftPresence.CONFIG.guiBGColor;
                } else if (!StringHandler.isNullOrEmpty(CraftPresence.CONFIG.guiBGColor)) {
                    startingMCTexturePath = CraftPresence.CONFIG.guiBGColor;
                }
            }

            if (StringHandler.isNullOrEmpty(hexText.getText()) && !StringHandler.isNullOrEmpty(startingHexValue)) {
                hexText.setText(startingHexValue);
                currentNormalHexValue = null;
                currentConvertedHexValue = null;
                currentNormalHexValue = null;
                currentConvertedMCTexturePath = null;
                currentMCTexture = new ResourceLocation("");
                pageNumber = 0;
            } else if (StringHandler.isNullOrEmpty(mcTextureText.getText()) && !StringHandler.isNullOrEmpty(startingMCTexturePath)) {
                mcTextureText.setText(startingMCTexturePath);
                currentNormalHexValue = null;
                currentConvertedHexValue = null;
                currentNormalHexValue = null;
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

            if (!StringHandler.isNullOrEmpty(hexText.getText())) {
                if (hexText.getText().startsWith("#") || hexText.getText().length() == 6) {
                    localValue = StringHandler.getColorFromHex(hexText.getText()).getRGB();
                } else if (hexText.getText().startsWith("0x")) {
                    localColor = new Color(Long.decode(hexText.getText()).intValue(), true);
                    localValue = localColor.getRGB();
                } else if (StringHandler.isValidInteger(hexText.getText())) {
                    localValue = Integer.decode(hexText.getText());
                }

                if (localValue != null) {
                    currentAlpha = (localValue >> 24 & 255);
                    currentRed = (localValue >> 16 & 255);
                    currentGreen = (localValue >> 8 & 255);
                    currentBlue = (localValue & 255);

                    alphaText.setText(Integer.toString(currentAlpha));
                    redText.setText(Integer.toString(currentRed));
                    greenText.setText(Integer.toString(currentGreen));
                    blueText.setText(Integer.toString(currentBlue));

                    currentNormalHexValue = hexText.getText();
                    currentConvertedHexValue = Integer.toString(localValue);
                }
            } else if (!StringHandler.isNullOrEmpty(redText.getText()) && !StringHandler.isNullOrEmpty(greenText.getText()) && !StringHandler.isNullOrEmpty(blueText.getText()) && !StringHandler.isNullOrEmpty(alphaText.getText())) {
                if (StringHandler.isValidInteger(redText.getText()) && StringHandler.isValidInteger(greenText.getText()) && StringHandler.isValidInteger(blueText.getText()) && StringHandler.isValidInteger(alphaText.getText())) {
                    if (!(Integer.parseInt(redText.getText()) > 255 || Integer.parseInt(greenText.getText()) > 255 || Integer.parseInt(blueText.getText()) > 255 || Integer.parseInt(alphaText.getText()) > 255)) {
                        localColor = new Color(Integer.parseInt(redText.getText()), Integer.parseInt(greenText.getText()), Integer.parseInt(blueText.getText()), Integer.parseInt(alphaText.getText()));

                        currentNormalHexValue = StringHandler.getHexFromColor(localColor);
                        hexText.setText(currentNormalHexValue);

                        currentConvertedHexValue = Long.toString(Long.decode(currentNormalHexValue).intValue());
                    }
                }
            }
        }

        // Page 2 - MC Texture Syncing
        if (pageNumber == 1) {
            if (!StringHandler.isNullOrEmpty(mcTextureText.getText())) {
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
