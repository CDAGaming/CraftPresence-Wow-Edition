package com.gitlab.cdagaming.craftpresence.utils.gui.controls;

import net.minecraft.client.gui.GuiButton;

public class ExtendedButtonControl extends GuiButton {
    private String[] optionalArgs;

    public ExtendedButtonControl(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, String... optionalArgs) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);

        this.optionalArgs = optionalArgs;
    }

    public ExtendedButtonControl(int id, int xPos, int yPos, String displayString) {
        super(id, xPos, yPos, displayString);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String[] getOptionalArgs() {
        return optionalArgs;
    }
}
