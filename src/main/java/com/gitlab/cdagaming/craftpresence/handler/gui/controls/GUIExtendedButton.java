package com.gitlab.cdagaming.craftpresence.handler.gui.controls;

import net.minecraft.client.gui.GuiButton;

public class GUIExtendedButton extends GuiButton {
    public GUIExtendedButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public GUIExtendedButton(int id, int xPos, int yPos, String displayString) {
        super(id, xPos, yPos, displayString);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
