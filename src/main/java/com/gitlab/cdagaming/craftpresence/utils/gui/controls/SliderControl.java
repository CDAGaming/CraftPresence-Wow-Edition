package com.gitlab.cdagaming.craftpresence.utils.gui.controls;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import net.minecraft.client.Minecraft;

public class SliderControl extends ExtendedButtonControl
{
    private float sliderValue, denormalizedSlideValue;
    private String windowTitle;
    private boolean dragging;

    private final float minValue;
    private final float maxValue;
    private final float valueStep;

    public SliderControl(int buttonId, Tuple<Integer, Integer> positionData, Tuple<Integer, Integer> dimensions, float startValue, float minValue, float maxValue, float valueStep, String displayString)
    {
        super(buttonId, positionData.getFirst(), positionData.getSecond(), dimensions.getFirst(), dimensions.getSecond(), "");

        setSliderValue(startValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.valueStep = valueStep;
        this.displayString = displayString + ": " + denormalizedSlideValue;
        this.windowTitle = displayString;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    @Override
    protected int getHoverState(boolean mouseOver)
    {
        return 0;
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
        if (visible)
        {
            if (dragging)
            {
                sliderValue = (float)(mouseX - (x + 4)) / (float)(width - 8);
                sliderValue = clamp(sliderValue, 0.0F, 1.0F);
                denormalizedSlideValue = denormalizeValue(sliderValue);
                //sliderValue = normalizeValue(denormalizedSlideValue);

                displayString = windowTitle + ": " + denormalizedSlideValue;
            }

            CraftPresence.GUIS.renderSlider(x + (int)(sliderValue * (float)(width - 8)), y, 0, 66, 4, 20, zLevel, BUTTON_TEXTURES);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (super.mousePressed(mc, mouseX, mouseY))
        {
            sliderValue = (float)(mouseX - (x + 4)) / (float)(width - 8);
            sliderValue = clamp(sliderValue, 0.0F, 1.0F);
            denormalizedSlideValue = denormalizeValue(sliderValue);

            displayString = windowTitle + ": " + denormalizedSlideValue;
            dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

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

    public float getSliderValue(boolean useNormal) {
        return useNormal ? sliderValue : denormalizedSlideValue;
    }

    private float clamp(float num, float min, float max) {
        if (num < min)
        {
            return min;
        }
        else
        {
            return Math.min(num, max);
        }
    }

    private float normalizeValue(float value)
    {
        return clamp((snapToStepClamp(value) - minValue) / (maxValue - minValue), 0.0F, 1.0F);
    }

    private float denormalizeValue(float value)
    {
        return snapToStepClamp(minValue + (maxValue - minValue) * clamp(value, 0.0F, 1.0F));
    }

    private float snapToStepClamp(float originalValue)
    {
        float value = snapToStep(originalValue);
        return clamp(value, minValue, maxValue);
    }

    private float snapToStep(float originalValue)
    {
        float value = originalValue;
        if (valueStep > 0.0F)
        {
            value = valueStep * (float)Math.round(value / valueStep);
        }

        return value;
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    @Override
    public void mouseReleased(int mouseX, int mouseY)
    {
        dragging = false;
    }
}