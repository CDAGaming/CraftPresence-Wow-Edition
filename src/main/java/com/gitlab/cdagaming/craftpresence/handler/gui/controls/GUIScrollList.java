package com.gitlab.cdagaming.craftpresence.handler.gui.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;

import java.util.List;

public class GUIScrollList extends GuiSlot {
    public String currentValue;
    public List<String> itemList;

    public GUIScrollList(Minecraft mc, int width, int height, int topIn, int bottomIn, int slotHeightIn, List<String> itemList, String currentValue) {
        super(mc, width, height, topIn, bottomIn, slotHeightIn);
        this.itemList = itemList;
        this.currentValue = currentValue;
    }

    @Override
    protected int getSize() {
        return itemList.size();
    }

    @Override
    public void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        currentValue = getSelectedItem(slotIndex);
    }

    @Override
    public boolean isSelected(int slotIndex) {
        return getSelectedItem(slotIndex).equals(currentValue);
    }

    @Override
    protected void drawBackground() {

    }

    @Override
    protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
        mc.fontRenderer.drawStringWithShadow(getSelectedItem(slotIndex), xPos, yPos, 0xFFFFFF);
    }

    public String getSelectedItem(int slotIndex) {
        try {
            return itemList.get(slotIndex);
        } catch (Exception ex) {
            return null;
        }
    }
}
