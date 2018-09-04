package com.gitlab.cdagaming.craftpresence.handler.gui.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;

import java.util.List;

public class GUIScrollList extends GuiSlot {
    public String currentValue;
    public List<String> list;

    public GUIScrollList(Minecraft mc, int width, int height, int topIn, int bottomIn, int slotHeightIn, List<String> itemList) {
        super(mc, width, height, topIn, bottomIn, slotHeightIn);
        list = itemList;
    }

    @Override
    protected int getSize() {
        return list.size();
    }

    @Override
    public void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        currentValue = list.get(slotIndex);
    }

    @Override
    public boolean isSelected(int slotIndex) {
        return list.get(slotIndex).equals(currentValue);
    }

    @Override
    protected void drawBackground() {
        drawContainerBackground(Tessellator.getInstance());
    }

    @Override
    protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
        if ((yPos + 10) < height) {
            mc.fontRenderer.drawStringWithShadow(list.get(slotIndex), xPos, yPos + 1, 16777215);
        }
    }
}
