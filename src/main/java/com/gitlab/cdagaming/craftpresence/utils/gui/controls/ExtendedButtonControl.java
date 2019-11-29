/*
 * MIT License
 *
 * Copyright (c) 2018 - 2019 CDAGaming (cstack2011@yahoo.com)
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
package com.gitlab.cdagaming.craftpresence.utils.gui.controls;

import net.minecraft.client.gui.GuiButton;

/**
 * Extended Gui Widget for a Clickable Button
 *
 * @author CDAGaming
 */
public class ExtendedButtonControl extends GuiButton {
    /**
     * Optional Arguments used for functions within the Mod, if any
     */
    private String[] optionalArgs;

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param buttonId The ID for the control to Identify as
     * @param x The Starting X Position for this Control
     * @param y The Starting Y Position for this Control
     * @param widthIn The Width for this Control
     * @param heightIn The Height for this Control
     * @param buttonText The display text, to display within this control
     * @param optionalArgs The optional Arguments, if any, to associate with this control
     */
    public ExtendedButtonControl(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, String... optionalArgs) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);

        this.optionalArgs = optionalArgs;
    }

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param id The ID for the Control to Identify as
     * @param xPos The Starting X Position for this Control
     * @param yPos The Starting Y Position for this Control
     * @param displayString The display text, to display within this Control
     */
    public ExtendedButtonControl(int id, int xPos, int yPos, String displayString) {
        super(id, xPos, yPos, displayString);
    }

    /**
     * Retrieves the Current Width of this Control
     *
     * @return The Current Width of this Control
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retrieves the Current Height of this Control
     *
     * @return The Current Height of this Control
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retrieves, if any, the Optional Arguments assigned within this Control
     *
     * @return The Optional Arguments assigned within this Control, if any
     */
    public String[] getOptionalArgs() {
        return optionalArgs;
    }
}
