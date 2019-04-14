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

public class ConfigGUI_AccessibilitySettings extends GuiScreen {
    private final GuiScreen parentScreen;
    private String currentHexValue;
    private int currentRed, currentGreen, currentBlue, currentAlpha;
    private GUIExtendedButton proceedButton;
    private GuiTextField hexText, redText, greenText, blueText, alphaText;

    ConfigGUI_AccessibilitySettings(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        proceedButton = new GUIExtendedButton(700, (width / 2) - 90, (height - 30), 180, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.back"));

        hexText = new GuiTextField(100, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        hexText.setText("-267386864"); // DEBUG: Set to Config Value

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
        drawDefaultBackground();

        final String mainTitle = Constants.TRANSLATOR.translate("gui.config.title");
        final String subTitle = Constants.TRANSLATOR.translate("gui.config.title.accessibility");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringHandler.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringHandler.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);

        proceedButton.enabled = true; // TODO

        hexText.drawTextBox();

        redText.drawTextBox();
        greenText.drawTextBox();
        blueText.drawTextBox();
        alphaText.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == proceedButton.id) {
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

    public void syncValues() {
        int localValue;

        if (!StringHandler.isNullOrEmpty(hexText.getText()) && !hexText.getText().equals(currentHexValue)) {
            if (hexText.getText().startsWith("#") || hexText.getText().length() == 6) {
                localValue = StringHandler.getColorFromHex(hexText.getText()).getRGB();
            } else if (hexText.getText().startsWith("0x")) {
                Color localObj = new Color(Long.decode(hexText.getText()).intValue(), true);
                localValue = localObj.getRGB();
            } else {
                localValue = Integer.decode(hexText.getText());
            }

            currentAlpha = (localValue >> 24 & 255);
            currentRed = (localValue >> 16 & 255);
            currentGreen = (localValue >> 8 & 255);
            currentBlue = (localValue & 255);

            alphaText.setText(Integer.toString(currentAlpha));
            redText.setText(Integer.toString(currentRed));
            greenText.setText(Integer.toString(currentGreen));
            blueText.setText(Integer.toString(currentBlue));

            currentHexValue = hexText.getText();
        } else if (!StringHandler.isNullOrEmpty(redText.getText()) && !StringHandler.isNullOrEmpty(greenText.getText()) && !StringHandler.isNullOrEmpty(blueText.getText()) && !StringHandler.isNullOrEmpty(alphaText.getText())) {
            if (!redText.getText().equals(Integer.toString(currentRed)) || !blueText.getText().equals(Integer.toString(currentBlue)) || !greenText.getText().equals(Integer.toString(currentGreen)) || !alphaText.getText().equals(Integer.toString(currentAlpha))) {
                if (StringHandler.isValidInteger(redText.getText()) && StringHandler.isValidInteger(greenText.getText()) && StringHandler.isValidInteger(blueText.getText()) && StringHandler.isValidInteger(alphaText.getText())) {
                    Color colorObj = new Color(Integer.parseInt(redText.getText()), Integer.parseInt(greenText.getText()), Integer.parseInt(blueText.getText()), Integer.parseInt(alphaText.getText()));

                    currentHexValue = StringHandler.getHexFromColor(colorObj);
                    hexText.setText(currentHexValue);
                }
            }
        }
    }
}
