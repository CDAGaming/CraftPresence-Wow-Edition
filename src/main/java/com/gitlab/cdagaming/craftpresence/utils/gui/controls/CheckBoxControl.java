package com.gitlab.cdagaming.craftpresence.utils.gui.controls;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import net.minecraft.client.Minecraft;

@SuppressWarnings("NullableProblems")
public class CheckBoxControl extends ExtendedButtonControl {
    public int boxWidth;
    private boolean is_Checked;

    public CheckBoxControl(int id, int xPos, int yPos, String displayString, boolean isChecked) {
        super(id, xPos, yPos, displayString);
        is_Checked = isChecked;
        boxWidth = 11;
        height = 11;
        width = boxWidth + 2 + StringUtils.getStringWidth(displayString);
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
     * Returns true if the mouse has been pressed on this control.
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

    public boolean isChecked() {
        return is_Checked;
    }
}
