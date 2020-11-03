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

package com.gitlab.cdagaming.craftpresence.utils.gui.controls;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

/**
 * Extended Gui Widget for a Text Field
 *
 * @author CDAGaming
 */
public class ExtendedTextControl extends GuiTextField {
    /**
     * The event to occur when a key event occurs
     */
    private Runnable onKeyEvent;

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param componentId     The ID for the control to Identify as
     * @param fontRendererObj The Font Renderer Instance
     * @param x               The Starting X Position for this Control
     * @param y               The Starting Y Position for this Control
     * @param widthIn         The Width for this Control
     * @param heightIn        The Height for this Control
     */
    public ExtendedTextControl(int componentId, FontRenderer fontRendererObj, int x, int y, int widthIn, int heightIn) {
        super(componentId, fontRendererObj, x, y, widthIn, heightIn);
    }

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param fontRendererObj The Font Renderer Instance
     * @param x               The Starting X Position for this Control
     * @param y               The Starting Y Position for this Control
     * @param widthIn         The Width for this Control
     * @param heightIn        The Height for this Control
     */
    public ExtendedTextControl(FontRenderer fontRendererObj, int x, int y, int widthIn, int heightIn) {
        this(CraftPresence.GUIS.getNextIndex(), fontRendererObj, x, y, widthIn, heightIn);
    }

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param fontRendererObj The Font Renderer Instance
     * @param x               The Starting X Position for this Control
     * @param y               The Starting Y Position for this Control
     * @param widthIn         The Width for this Control
     * @param heightIn        The Height for this Control
     * @param keyEvent        The event to run when characters are typed in this control
     */
    public ExtendedTextControl(FontRenderer fontRendererObj, int x, int y, int widthIn, int heightIn, Runnable keyEvent) {
        this(fontRendererObj, x, y, widthIn, heightIn);
        setOnKeyTyped(keyEvent);
    }

    /**
     * Retrieves the Current Width of this Control
     *
     * @return The Current Width of this Control
     */
    public int getControlWidth() {
        return width;
    }

    /**
     * Retrieves the Current Height of this Control
     *
     * @return The Current Height of this Control
     */
    public int getControlHeight() {
        return height;
    }

    /**
     * Retrieves the Current X Position of this Control
     *
     * @return the Current X Position of this Control
     */
    public int getControlPosX() {
        return x;
    }

    /**
     * Retrieves the Current Y Position of this Control
     *
     * @return the Current Y Position of this Control
     */
    public int getControlPosY() {
        return y;
    }

    /**
     * Sets the Event to occur upon typing keys
     *
     * @param event The event to occur
     */
    public void setOnKeyTyped(Runnable event) {
        onKeyEvent = event;
    }

    /**
     * Triggers the onKey event to occur
     */
    public void onKeyTyped() {
        if (onKeyEvent != null) {
            onKeyEvent.run();
        }
    }

    /**
     * The event to occur when a character is typed within this control
     *
     * @param typedChar The typed character, if any
     * @param keyCode   The keycode, if any
     * @return Whether the event completed successfully
     */
    @Override
    public boolean textboxKeyTyped(char typedChar, int keyCode) {
        final boolean returnValue = super.textboxKeyTyped(typedChar, keyCode);
        if (returnValue) {
            onKeyTyped();
        }

        return returnValue;
    }
}
