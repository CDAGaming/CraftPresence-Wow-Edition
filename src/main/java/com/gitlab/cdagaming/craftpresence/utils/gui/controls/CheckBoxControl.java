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
package com.gitlab.cdagaming.craftpresence.utils.gui.controls;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import net.minecraft.client.Minecraft;

/**
 * Gui Widget for a Clickable Checkbox-Style Button
 *
 * @author CDAGaming
 */
@SuppressWarnings("NullableProblems")
public class CheckBoxControl extends ExtendedButtonControl {
    /**
     * The width of the inner box of this control
     */
    public int boxWidth;

    /**
     * The Check state of this control
     */
    private boolean is_Checked;

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param id            The ID for the control to Identify as
     * @param xPos          The Starting X Position for this Control
     * @param yPos          The Starting Y Position for this Control
     * @param displayString The display text, to display within this control
     * @param isChecked     The beginning check state for this Control
     */
    public CheckBoxControl(int id, int xPos, int yPos, String displayString, boolean isChecked) {
        super(id, xPos, yPos, displayString);
        is_Checked = isChecked;
        boxWidth = 11;
        height = 11;
        width = boxWidth + 2 + StringUtils.getStringWidth(displayString);
    }

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param id            The ID for the control to Identify as
     * @param xPos          The Starting X Position for this Control
     * @param yPos          The Starting Y Position for this Control
     * @param displayString The display text, to display within this control
     * @param isChecked     The beginning check state for this Control
     * @param onPushEvent   The Click Event to Occur when this control is clicked
     */
    public CheckBoxControl(int id, int xPos, int yPos, String displayString, boolean isChecked, Runnable onPushEvent) {
        this(id, xPos, yPos, displayString, isChecked);
        setOnClick(onPushEvent);
    }

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param id            The ID for the control to Identify as
     * @param xPos          The Starting X Position for this Control
     * @param yPos          The Starting Y Position for this Control
     * @param displayString The display text, to display within this control
     * @param isChecked     The beginning check state for this Control
     * @param onPushEvent   The Click Event to Occur when this control is clicked
     * @param onHoverEvent  The Hover Event to Occur when this control is clicked
     */
    public CheckBoxControl(int id, int xPos, int yPos, String displayString, boolean isChecked, Runnable onPushEvent, Runnable onHoverEvent) {
        this(id, xPos, yPos, displayString, isChecked, onPushEvent);
        setOnHover(onHoverEvent);
    }

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param xPos          The Starting X Position for this Control
     * @param yPos          The Starting Y Position for this Control
     * @param displayString The display text, to display within this control
     * @param isChecked     The beginning check state for this Control
     */
    public CheckBoxControl(int xPos, int yPos, String displayString, boolean isChecked) {
        this(CraftPresence.GUIS.getNextIndex(), xPos, yPos, displayString, isChecked);
    }

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param xPos          The Starting X Position for this Control
     * @param yPos          The Starting Y Position for this Control
     * @param displayString The display text, to display within this control
     * @param isChecked     The beginning check state for this Control
     * @param onPushEvent   The Click Event to Occur when this control is clicked
     */
    public CheckBoxControl(int xPos, int yPos, String displayString, boolean isChecked, Runnable onPushEvent) {
        this(xPos, yPos, displayString, isChecked);
        setOnClick(onPushEvent);
    }

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param xPos          The Starting X Position for this Control
     * @param yPos          The Starting Y Position for this Control
     * @param displayString The display text, to display within this control
     * @param isChecked     The beginning check state for this Control
     * @param onPushEvent   The Click Event to Occur when this control is clicked
     * @param onHoverEvent  The Hover Event to Occur when this control is clicked
     */
    public CheckBoxControl(int xPos, int yPos, String displayString, boolean isChecked, Runnable onPushEvent, Runnable onHoverEvent) {
        this(xPos, yPos, displayString, isChecked, onPushEvent);
        setOnHover(onHoverEvent);
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
        if (visible) {
            hovered = CraftPresence.GUIS.isMouseOver(mouseX, mouseY, this);
            CraftPresence.GUIS.drawContinuousTexturedBox(new Tuple<>(x, y), new Tuple<>(0, 46), new Tuple<>(boxWidth, height), new Tuple<>(200, 20), new Tuple<>(2, 3), new Tuple<>(2, 2), zLevel, BUTTON_TEXTURES);
            mouseDragged(mc, mouseX, mouseY);
            int color = !enabled ? 10526880 : 14737632;

            if (is_Checked)
                drawCenteredString(mc.fontRenderer, "x", x + boxWidth / 2 + 1, y + 1, 14737632);

            drawString(mc.fontRenderer, displayString, x + boxWidth + 2, y + 2, color);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control.<p>
     * Equivalent of MouseListener.mousePressed(MouseEvent e).
     */
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (enabled && visible && hovered) {
            is_Checked = !is_Checked;
            return true;
        }
        return false;
    }

    /**
     * Returns this Control's Check State
     *
     * @return The Current Check State of this control
     */
    public boolean isChecked() {
        return is_Checked;
    }
}
