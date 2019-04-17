package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUIExtendedButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class ConfigGUI_ColorEditor extends GuiScreen {
    private final GuiScreen parentScreen;
    private String currentNormalHexValue, currentConvertedHexValue, configValueName, startingHexValue;
    private int currentRed, currentGreen, currentBlue, currentAlpha;
    private GUIExtendedButton proceedButton;
    private GuiTextField hexText, redText, greenText, blueText, alphaText;

    ConfigGUI_ColorEditor(GuiScreen parentScreen, String configValueName) {
        mc = CraftPresence.instance;
        this.parentScreen = parentScreen;
        this.configValueName = configValueName;

        if (!StringHandler.isNullOrEmpty(configValueName)) {
            if (configValueName.equals(CraftPresence.CONFIG.NAME_tooltipBGColor)) {
                startingHexValue = CraftPresence.CONFIG.tooltipBGColor;
            } else if (configValueName.equals(CraftPresence.CONFIG.NAME_tooltipBorderColor)) {
                startingHexValue = CraftPresence.CONFIG.tooltipBorderColor;
            }
        }
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        proceedButton = new GUIExtendedButton(700, 10, (height - 30), 80, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.back"));

        hexText = new GuiTextField(100, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        hexText.setText(startingHexValue);

        redText = new GuiTextField(110, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(3), 180, 20);
        greenText = new GuiTextField(120, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(4), 180, 20);
        blueText = new GuiTextField(130, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(5), 180, 20);
        alphaText = new GuiTextField(140, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(6), 180, 20);

        buttonList.add(proceedButton);

        syncValues();
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

        final String mainTitle = Constants.TRANSLATOR.translate("gui.config.title");
        final String subTitle = Constants.TRANSLATOR.translate("gui.config.title.editor.color", configValueName.replaceAll("_", " "));

        final String hexCodeTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.hexcode");
        final String redTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.redcolorvalue");
        final String greenTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.greencolorvalue");
        final String blueTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.bluecolorvalue");
        final String alphaTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.alphacolorvalue");

        final String previewTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.preview");
        final String noticeTitle = Constants.TRANSLATOR.translate("gui.config.editorMessage.refresh");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringHandler.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringHandler.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);

        drawString(mc.fontRenderer, hexCodeTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        drawString(mc.fontRenderer, redTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);
        drawString(mc.fontRenderer, greenTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(4) + 5, 0xFFFFFF);
        drawString(mc.fontRenderer, blueTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(5) + 5, 0xFFFFFF);
        drawString(mc.fontRenderer, alphaTitle, (width / 2) - 130, CraftPresence.GUIS.getButtonY(6) + 5, 0xFFFFFF);

        drawString(mc.fontRenderer, previewTitle, width - 90, height - 30, 0xFFFFFF);
        drawString(mc.fontRenderer, noticeTitle, (width / 2) - 90, height - 30, 0xFFFFFF);

        proceedButton.enabled = !StringHandler.isNullOrEmpty(hexText.getText());

        hexText.drawTextBox();

        redText.drawTextBox();
        greenText.drawTextBox();
        blueText.drawTextBox();
        alphaText.drawTextBox();

        //Draw Preview Rectangle
        CraftPresence.GUIS.drawGradientRect(300, width - 45, height - 45, width - 5, height - 5, currentConvertedHexValue, currentConvertedHexValue);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == proceedButton.id) {
            syncValues();

            if (!StringHandler.isNullOrEmpty(configValueName)) {
                if (configValueName.equals(CraftPresence.CONFIG.NAME_tooltipBGColor) && !currentNormalHexValue.equals(CraftPresence.CONFIG.tooltipBGColor)) {
                    CraftPresence.CONFIG.hasChanged = true;
                    CraftPresence.CONFIG.tooltipBGColor = currentNormalHexValue;
                } else if (configValueName.equals(CraftPresence.CONFIG.NAME_tooltipBorderColor) && !currentNormalHexValue.equals(CraftPresence.CONFIG.tooltipBorderColor)) {
                    CraftPresence.CONFIG.hasChanged = true;
                    CraftPresence.CONFIG.tooltipBorderColor = currentNormalHexValue;
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

        hexText.textboxKeyTyped(typedChar, keyCode);
        redText.textboxKeyTyped(typedChar, keyCode);
        greenText.textboxKeyTyped(typedChar, keyCode);
        blueText.textboxKeyTyped(typedChar, keyCode);
        alphaText.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_NUMPADENTER || keyCode == Keyboard.KEY_RETURN) {
            syncValues();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        hexText.mouseClicked(mouseX, mouseY, mouseButton);
        redText.mouseClicked(mouseX, mouseY, mouseButton);
        greenText.mouseClicked(mouseX, mouseY, mouseButton);
        blueText.mouseClicked(mouseX, mouseY, mouseButton);
        alphaText.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        hexText.updateCursorCounter();
        redText.updateCursorCounter();
        greenText.updateCursorCounter();
        blueText.updateCursorCounter();
        alphaText.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    private void syncValues() {
        Integer localValue = null;
        Color localColor;

        if (!StringHandler.isNullOrEmpty(hexText.getText()) && !hexText.getText().equals(currentNormalHexValue)) {
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
            if (!redText.getText().equals(Integer.toString(currentRed)) || !blueText.getText().equals(Integer.toString(currentBlue)) || !greenText.getText().equals(Integer.toString(currentGreen)) || !alphaText.getText().equals(Integer.toString(currentAlpha))) {
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
    }
}
