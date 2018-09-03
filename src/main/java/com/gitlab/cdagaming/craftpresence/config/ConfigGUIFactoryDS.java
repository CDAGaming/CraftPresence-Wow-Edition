package com.gitlab.cdagaming.craftpresence.config;

import com.gitlab.cdagaming.craftpresence.config.gui.ConfigGUI_Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

public class ConfigGUIFactoryDS implements IModGuiFactory {
    @Override
    public void initialize(Minecraft mc) {
        // N/A
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new ConfigGUI_Main(parentScreen);
    }
}