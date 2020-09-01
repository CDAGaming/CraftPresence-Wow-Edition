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
import com.gitlab.cdagaming.craftpresence.impl.Pair;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

/**
 * Gui Widget for a Movable Slider between a beginning and maximum value
 *
 * @author CDAGaming
 */
public class SliderControl extends ExtendedButtonControl {
    /**
     * The Minimum Value the Slider can reach
     */
    private final float minValue;

    /**
     * The Maximum Value the Slider can Reach
     */
    private final float maxValue;

    /**
     * The rate at which the Slider is able to move at on each move
     */
    private final float valueStep;
    /**
     * The Starting Slider Name to display as
     */
    private final String windowTitle;
    /**
     * The Normalized Slider Value between 0.0f and 1.0f
     */
    private float sliderValue;
    /**
     * The denormalized Slider value between the minimum and maximum values
     */
    private float denormalizedSlideValue;
    /**
     * Whether the Slider is currently being dragged
     */
    private boolean dragging;

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param buttonId      The ID for the control to Identify as
     * @param positionData  The Starting X and Y Positions to place the control in a Gui
     * @param dimensions    The Width and Height dimensions for the control
     * @param startValue    The Starting Value between the minimum and maximum value to set the slider at
     * @param minValue      The Minimum Value the Slider is allowed to be -- denormalized
     * @param maxValue      The Maximum Value the Slider is allowed to be -- denormalized
     * @param valueStep     The rate at which each move to the slider adjusts it's value
     * @param displayString The title to display in the center of the slider
     */
    public SliderControl(int buttonId, Pair<Integer, Integer> positionData, Pair<Integer, Integer> dimensions, float startValue, float minValue, float maxValue, float valueStep, String displayString) {
        super(buttonId, positionData.getFirst(), positionData.getSecond(), dimensions.getFirst(), dimensions.getSecond(), "");

        setSliderValue(startValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.valueStep = valueStep;
        this.displayString = displayString + ": " + denormalizedSlideValue;
        this.windowTitle = displayString;
    }

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param buttonId      The ID for the control to Identify as
     * @param positionData  The Starting X and Y Positions to place the control in a Gui
     * @param dimensions    The Width and Height dimensions for the control
     * @param startValue    The Starting Value between the minimum and maximum value to set the slider at
     * @param minValue      The Minimum Value the Slider is allowed to be -- denormalized
     * @param maxValue      The Maximum Value the Slider is allowed to be -- denormalized
     * @param valueStep     The rate at which each move to the slider adjusts it's value
     * @param displayString The title to display in the center of the slider
     * @param onPushEvent   The Click Event to Occur when this control is clicked
     */
    public SliderControl(int buttonId, Pair<Integer, Integer> positionData, Pair<Integer, Integer> dimensions, float startValue, float minValue, float maxValue, float valueStep, String displayString, Runnable onPushEvent) {
        this(buttonId, positionData, dimensions, startValue, minValue, maxValue, valueStep, displayString);
        setOnClick(onPushEvent);
    }

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param buttonId      The ID for the control to Identify as
     * @param positionData  The Starting X and Y Positions to place the control in a Gui
     * @param dimensions    The Width and Height dimensions for the control
     * @param startValue    The Starting Value between the minimum and maximum value to set the slider at
     * @param minValue      The Minimum Value the Slider is allowed to be -- denormalized
     * @param maxValue      The Maximum Value the Slider is allowed to be -- denormalized
     * @param valueStep     The rate at which each move to the slider adjusts it's value
     * @param displayString The title to display in the center of the slider
     * @param onPushEvent   The Click Event to Occur when this control is clicked
     * @param onHoverEvent  The Hover Event to Occur when this control is clicked
     */
    public SliderControl(int buttonId, Pair<Integer, Integer> positionData, Pair<Integer, Integer> dimensions, float startValue, float minValue, float maxValue, float valueStep, String displayString, Runnable onPushEvent, Runnable onHoverEvent) {
        this(buttonId, positionData, dimensions, startValue, minValue, maxValue, valueStep, displayString, onPushEvent);
        setOnHover(onHoverEvent);
    }

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param positionData  The Starting X and Y Positions to place the control in a Gui
     * @param dimensions    The Width and Height dimensions for the control
     * @param startValue    The Starting Value between the minimum and maximum value to set the slider at
     * @param minValue      The Minimum Value the Slider is allowed to be -- denormalized
     * @param maxValue      The Maximum Value the Slider is allowed to be -- denormalized
     * @param valueStep     The rate at which each move to the slider adjusts it's value
     * @param displayString The title to display in the center of the slider
     */
    public SliderControl(Pair<Integer, Integer> positionData, Pair<Integer, Integer> dimensions, float startValue, float minValue, float maxValue, float valueStep, String displayString) {
        this(CraftPresence.GUIS.getNextIndex(), positionData, dimensions, startValue, minValue, maxValue, valueStep, displayString);
    }

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param positionData  The Starting X and Y Positions to place the control in a Gui
     * @param dimensions    The Width and Height dimensions for the control
     * @param startValue    The Starting Value between the minimum and maximum value to set the slider at
     * @param minValue      The Minimum Value the Slider is allowed to be -- denormalized
     * @param maxValue      The Maximum Value the Slider is allowed to be -- denormalized
     * @param valueStep     The rate at which each move to the slider adjusts it's value
     * @param displayString The title to display in the center of the slider
     * @param onPushEvent   The Click Event to Occur when this control is clicked
     */
    public SliderControl(Pair<Integer, Integer> positionData, Pair<Integer, Integer> dimensions, float startValue, float minValue, float maxValue, float valueStep, String displayString, Runnable onPushEvent) {
        this(positionData, dimensions, startValue, minValue, maxValue, valueStep, displayString);
        setOnClick(onPushEvent);
    }

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param positionData  The Starting X and Y Positions to place the control in a Gui
     * @param dimensions    The Width and Height dimensions for the control
     * @param startValue    The Starting Value between the minimum and maximum value to set the slider at
     * @param minValue      The Minimum Value the Slider is allowed to be -- denormalized
     * @param maxValue      The Maximum Value the Slider is allowed to be -- denormalized
     * @param valueStep     The rate at which each move to the slider adjusts it's value
     * @param displayString The title to display in the center of the slider
     * @param onPushEvent   The Click Event to Occur when this control is clicked
     * @param onHoverEvent  The Hover Event to Occur when this control is clicked
     */
    public SliderControl(Pair<Integer, Integer> positionData, Pair<Integer, Integer> dimensions, float startValue, float minValue, float maxValue, float valueStep, String displayString, Runnable onPushEvent, Runnable onHoverEvent) {
        this(positionData, dimensions, startValue, minValue, maxValue, valueStep, displayString, onPushEvent);
        setOnHover(onHoverEvent);
    }

    /**
     * Returns the current Hover state of this control
     * <p>
     * 0 if the button is disabled<p>
     * 1 if the mouse is NOT hovering over this button<p>
     * 2 if it IS hovering over this button.
     */
    @Override
    protected int getHoverState(boolean mouseOver) {
        return 0;
    }

    /**
     * Fired when the mouse button is dragged.<p>
     * Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    @Override
    protected void mouseDragged(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            if (dragging) {
                sliderValue = (float) (mouseX - (x + 4)) / (float) (width - 8);
                sliderValue = clamp(sliderValue, 0.0F, 1.0F);
                denormalizedSlideValue = denormalizeValue(sliderValue);

                displayString = windowTitle + ": " + denormalizedSlideValue;
            }

            int hoverValue = (hovered ? 2 : 1) * 20;
            CraftPresence.GUIS.renderSlider(x + (int) (sliderValue * (float) (width - 8)), y, 0, 46 + hoverValue, 4, 20, zLevel, BUTTON_TEXTURES);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control.<p>
     * Equivalent of MouseListener.mousePressed(MouseEvent e).
     */
    @Override
    public boolean mousePressed(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            sliderValue = (float) (mouseX - (x + 4)) / (float) (width - 8);
            sliderValue = clamp(sliderValue, 0.0F, 1.0F);
            denormalizedSlideValue = denormalizeValue(sliderValue);

            displayString = windowTitle + ": " + denormalizedSlideValue;
            dragging = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Updates the Current Slider Value<p>
     * Note: Both Normalized and denormalized values are supported
     *
     * @param newValue The New Slider Value
     */
    public void setSliderValue(float newValue) {
        if (newValue >= 0.0f && newValue <= 1.0f) {
            sliderValue = newValue;
            denormalizedSlideValue = denormalizeValue(newValue);
        } else {
            sliderValue = normalizeValue(newValue);
            denormalizedSlideValue = newValue;
        }
        displayString = windowTitle + ": " + denormalizedSlideValue;
    }

    /**
     * Retrieves the Current Normalized / denormalized Slider Value
     *
     * @param useNormal Whether to get the normalized value
     * @return The Current Normalized / denormalized Slider Value
     */
    public float getSliderValue(boolean useNormal) {
        return useNormal ? sliderValue : denormalizedSlideValue;
    }

    /**
     * Clamps the Specified Number between a minimum and maximum limit
     *
     * @param num The number to clamp upon
     * @param min The Minimum Limit for the number
     * @param max The Maximum Limit for the number
     * @return The adjusted and clamped number
     */
    private float clamp(float num, float min, float max) {
        if (num < min) {
            return min;
        } else {
            return Math.min(num, max);
        }
    }

    /**
     * Normalize and Clamp the specified value to a number between 0.0f and 1.0f
     *
     * @param value The denormalized value to normalize
     * @return The converted normalized value
     */
    private float normalizeValue(float value) {
        return clamp((snapToStepClamp(value) - minValue) / (maxValue - minValue), 0.0F, 1.0F);
    }

    /**
     * Denormalize and Expand the specified value to a number between the minimum and maximum slider value
     *
     * @param value The normalized value to denormalize
     * @return The converted denormalized value
     */
    private float denormalizeValue(float value) {
        return snapToStepClamp(minValue + (maxValue - minValue) * clamp(value, 0.0F, 1.0F));
    }

    /**
     * Snaps the Specified Value to the nearest Step Rate Value, then clamps said value within bounds
     *
     * @param originalValue The original value to adjust, if needed
     * @return The Snapped and Clamped proper Slider Value
     */
    private float snapToStepClamp(float originalValue) {
        float value = snapToStep(originalValue);
        return clamp(value, minValue, maxValue);
    }

    /**
     * Rounds the Specified Value to the nearest value, using the Step Rate Value
     *
     * @param originalValue The non-rounded value to interpret
     * @return The Step-Rounded Value at a valid step
     */
    private float snapToStep(float originalValue) {
        float value = originalValue;
        if (valueStep > 0.0F) {
            value = valueStep * (float) Math.round(value / valueStep);
        }

        return value;
    }

    /**
     * Fired when the mouse button is released.<p>
     * Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        dragging = false;
    }
}