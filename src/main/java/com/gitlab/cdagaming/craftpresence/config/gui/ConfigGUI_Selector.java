package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUIScrollList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigGUI_Selector extends GuiScreen {
    private final GuiScreen parentscreen;
    private GuiButton proceedButton, addNewButton;
    private GUIScrollList scrollList;
    private GuiTextField searchBox;
    private String configoption;
    private String title, attributeName, originalValue, searchTerm;
    private List<String> itemList, originalList;

    ConfigGUI_Selector(GuiScreen parentScreen, String configOption, String TITLE, List<String> list, @Nullable String currentValue, @Nullable String attributeName) {
        mc = Minecraft.getMinecraft();
        parentscreen = parentScreen;
        configoption = configOption;
        title = TITLE;
        itemList = originalList = list;
        originalValue = currentValue;
        this.attributeName = attributeName;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);
        proceedButton = new GuiButton(700, (sr.getScaledWidth() - 100), (sr.getScaledHeight() - 30), 90, 20, "Back");
        scrollList = new GUIScrollList(mc, sr.getScaledWidth(), sr.getScaledHeight(), 32, sr.getScaledHeight() - 45, 18, itemList, originalValue);
        searchBox = new GuiTextField(110, fontRenderer, 60, (sr.getScaledHeight() - 30), 120, 20);

        if (!originalList.equals(DiscordAssetHandler.ICON_LIST)) {
            addNewButton = new GuiButton(600, (sr.getScaledWidth() - 195), (sr.getScaledHeight() - 30), 90, 20, "Add New");
            buttonList.add(addNewButton);
        }

        buttonList.add(proceedButton);
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        List<String> modifiedList = new ArrayList<>();
        drawDefaultBackground();

        if (!searchBox.getText().isEmpty()) {
            if (!searchBox.getText().equals(searchTerm)) {
                searchTerm = searchBox.getText();
                for (String item : originalList) {
                    if (item.toLowerCase().contains(searchTerm.toLowerCase()) && !modifiedList.contains(item.toLowerCase())) {
                        modifiedList.add(item);
                    }
                }
                itemList = modifiedList;
            }
        } else {
            itemList = originalList;
        }

        if (!itemList.equals(originalList) && !itemList.contains(scrollList.currentValue)) {
            scrollList.currentValue = null;
        }

        scrollList.itemList = itemList;
        scrollList.drawScreen(mouseX, mouseY, partialTicks);
        drawString(fontRenderer, "Search:", (30 - (fontRenderer.getStringWidth("Search:") / 2)), (sr.getScaledHeight() - 25), 0xFFFFFF);
        searchBox.drawTextBox();
        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);

        if (scrollList.currentValue != null && ((originalValue != null && !scrollList.currentValue.equals(originalValue)) || (StringHandler.isNullOrEmpty(originalValue)))) {
            proceedButton.displayString = "Continue";
        } else {
            proceedButton.displayString = "Back";
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (buttonList.contains(addNewButton) && button.id == addNewButton.id) {
            mc.displayGuiScreen(new ConfigGUI_Editor(parentscreen, null, configoption));
        } else if (button.id == proceedButton.id) {
            if (scrollList.currentValue != null) {
                if (originalValue != null) {
                    if (!scrollList.currentValue.equals(originalValue)) {
                        if (configoption.equals(CraftPresence.CONFIG.NAME_defaultIcon)) {
                            CraftPresence.CONFIG.hasChanged = true;
                            CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                            CraftPresence.CONFIG.defaultIcon = scrollList.currentValue;
                            mc.displayGuiScreen(parentscreen);
                        } else if (configoption.equals(CraftPresence.CONFIG.NAME_defaultServerIcon)) {
                            CraftPresence.CONFIG.hasChanged = true;
                            CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                            CraftPresence.CONFIG.defaultServerIcon = scrollList.currentValue;
                            mc.displayGuiScreen(parentscreen);
                        } else if (configoption.equals(CraftPresence.CONFIG.NAME_defaultDimensionIcon)) {
                            CraftPresence.CONFIG.hasChanged = true;
                            CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                            CraftPresence.CONFIG.defaultDimensionIcon = scrollList.currentValue;
                            mc.displayGuiScreen(parentscreen);
                        } else if (configoption.equals(CraftPresence.CONFIG.NAME_dimensionMessages)) {
                            final String defaultDimensionMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                            final String currentDimensionMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, null);

                            CraftPresence.CONFIG.hasChanged = true;
                            CraftPresence.CONFIG.rebootOnWorldLoad = true;
                            if (StringHandler.isNullOrEmpty(currentDimensionMSG) || currentDimensionMSG.equals(defaultDimensionMSG)) {
                                CraftPresence.CONFIG.dimensionMessages = StringHandler.setConfigPart(CraftPresence.CONFIG.dimensionMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultDimensionMSG);
                            }
                            CraftPresence.CONFIG.dimensionMessages = StringHandler.setConfigPart(CraftPresence.CONFIG.dimensionMessages, attributeName, 0, 2, CraftPresence.CONFIG.splitCharacter, scrollList.currentValue);
                            mc.displayGuiScreen(parentscreen);
                        } else if (configoption.equals(CraftPresence.CONFIG.NAME_serverMessages)) {
                            final String defaultServerMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                            final String currentServerMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, null);

                            CraftPresence.CONFIG.hasChanged = true;
                            CraftPresence.CONFIG.rebootOnWorldLoad = true;
                            if (StringHandler.isNullOrEmpty(currentServerMSG) || currentServerMSG.equals(defaultServerMSG)) {
                                CraftPresence.CONFIG.serverMessages = StringHandler.setConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultServerMSG);
                            }
                            CraftPresence.CONFIG.serverMessages = StringHandler.setConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 2, CraftPresence.CONFIG.splitCharacter, scrollList.currentValue);
                            mc.displayGuiScreen(parentscreen);
                        } else {
                            mc.displayGuiScreen(new ConfigGUI_NullEntry(parentscreen));
                        }
                    } else {
                        mc.displayGuiScreen(parentscreen);
                    }
                } else {
                    if (configoption.equals(CraftPresence.CONFIG.NAME_biomeMessages) || configoption.equals(CraftPresence.CONFIG.NAME_dimensionMessages) || configoption.equals(CraftPresence.CONFIG.NAME_serverMessages) || configoption.equals(CraftPresence.CONFIG.NAME_guiMessages) || configoption.equals(CraftPresence.CONFIG.NAME_itemMessages)) {
                        mc.displayGuiScreen(new ConfigGUI_Editor(parentscreen, scrollList.currentValue, configoption));
                    } else {
                        mc.displayGuiScreen(new ConfigGUI_NullEntry(parentscreen));
                    }
                }
            } else {
                mc.displayGuiScreen(parentscreen);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            mc.displayGuiScreen(parentscreen);
        }
        searchBox.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        searchBox.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        searchBox.updateCursorCounter();
    }

    @Override
    public void handleMouseInput() throws IOException {
        scrollList.handleMouseInput();
        super.handleMouseInput();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
