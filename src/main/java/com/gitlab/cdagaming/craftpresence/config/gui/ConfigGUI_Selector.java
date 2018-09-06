package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAsset;
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
    private GuiButton proceedButton;
    private GUIScrollList scrollList;
    private GuiTextField searchBox;
    private String configoption;
    private String title, id, searchTerm;
    private List<String> itemList, originalList;

    ConfigGUI_Selector(GuiScreen parentScreen, String configOption, String TITLE, List<String> list, @Nullable String ID) {
        mc = Minecraft.getMinecraft();
        parentscreen = parentScreen;
        configoption = configOption;
        title = TITLE;
        itemList = originalList = list;
        id = ID;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);
        proceedButton = new GuiButton(700, (sr.getScaledWidth() / 2) + 60, (sr.getScaledHeight() - 30), 90, 20, "Back");
        scrollList = new GUIScrollList(mc, sr.getScaledWidth(), sr.getScaledHeight() - 45, 32, sr.getScaledHeight() - 45, 18, itemList);
        searchBox = new GuiTextField(110, fontRenderer, (sr.getScaledWidth() / 2) - 140, (sr.getScaledHeight() - 30), 120, 20);

        buttonList.add(proceedButton);
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        List<String> modifiedList = new ArrayList<>();
        drawDefaultBackground();
        drawString(fontRenderer, "Search:", ((sr.getScaledWidth() / 2) - 165) - (fontRenderer.getStringWidth("Search:") / 2), (sr.getScaledHeight() - 25), 0xFFFFFF);
        searchBox.drawTextBox();

        if (!searchBox.getText().isEmpty()) {
            if (!searchBox.getText().equals(searchTerm)) {
                searchTerm = searchBox.getText();
                for (String item : originalList) {
                    if (item.contains(searchTerm) && !modifiedList.contains(item)) {
                        modifiedList.add(item);
                    }
                }
                itemList = modifiedList;
            }
        } else {
            itemList = originalList;
        }

        if (itemList != originalList && !itemList.contains(scrollList.currentValue)) {
            scrollList.currentValue = null;
        }

        scrollList.list = itemList;
        scrollList.drawScreen(mouseX, mouseY, partialTicks);
        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);

        if (scrollList.currentValue != null) {
            if (configoption.equals(CraftPresence.CONFIG.NAME_defaultIcon) && !scrollList.currentValue.equals(CraftPresence.CONFIG.defaultIcon)) {
                proceedButton.displayString = "Continue";
            } else if (configoption.equals(CraftPresence.CONFIG.NAME_defaultServerIcon) && !scrollList.currentValue.equals(CraftPresence.CONFIG.defaultServerIcon)) {
                proceedButton.displayString = "Continue";
            } else {
                proceedButton.displayString = "Continue";
            }
        } else {
            proceedButton.displayString = "Back";
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        // Back button
        if (button.id == proceedButton.id) {
            if (scrollList.currentValue != null) {
                if (id != null) {
                    if (configoption.equals(CraftPresence.CONFIG.NAME_dimensionMessages)) {
                        final String currentDimensionMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, id, 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                        final String currentDimensionIcon = StringHandler.getConfigPart(CraftPresence.CONFIG.dimensionMessages, id, 0, 2, CraftPresence.CONFIG.splitCharacter, null);
                        if (!scrollList.currentValue.equals(currentDimensionIcon)) {
                            CraftPresence.CONFIG.hasChanged = true;
                            CraftPresence.CONFIG.rebootOnWorldLoad = true;
                            if (StringHandler.isNullOrEmpty(currentDimensionMSG)) {
                                CraftPresence.CONFIG.dimensionMessages = StringHandler.setConfigPart(CraftPresence.CONFIG.dimensionMessages, id, 0, 1, CraftPresence.CONFIG.splitCharacter, "In The &dimension&");
                            }
                            CraftPresence.CONFIG.dimensionMessages = StringHandler.setConfigPart(CraftPresence.CONFIG.dimensionMessages, id, 0, 2, CraftPresence.CONFIG.splitCharacter, scrollList.currentValue);
                        }
                        mc.displayGuiScreen(parentscreen);
                    } else {
                        mc.displayGuiScreen(new ConfigGUI_NullEntry(parentscreen));
                    }
                } else {
                    if (configoption.equals(CraftPresence.CONFIG.NAME_defaultIcon) && !scrollList.currentValue.equals(CraftPresence.CONFIG.defaultIcon)) {
                        CraftPresence.CONFIG.hasChanged = true;
                        CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                        if (CraftPresence.CLIENT.LARGEIMAGEKEY.equals(CraftPresence.CONFIG.defaultIcon)) {
                            CraftPresence.CLIENT.setImage(scrollList.currentValue, DiscordAsset.AssetType.LARGE);
                        }
                        if (CraftPresence.CLIENT.SMALLIMAGEKEY.equals(CraftPresence.CONFIG.defaultIcon)) {
                            CraftPresence.CLIENT.setImage(scrollList.currentValue, DiscordAsset.AssetType.SMALL);
                        }
                        CraftPresence.CONFIG.defaultIcon = scrollList.currentValue;
                        mc.displayGuiScreen(parentscreen);
                    } else if (configoption.equals(CraftPresence.CONFIG.NAME_defaultServerIcon) && !scrollList.currentValue.equals(CraftPresence.CONFIG.defaultServerIcon)) {
                        CraftPresence.CONFIG.hasChanged = true;
                        CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                        if (CraftPresence.CLIENT.SMALLIMAGEKEY.equals(CraftPresence.CONFIG.defaultServerIcon)) {
                            CraftPresence.CLIENT.setImage(scrollList.currentValue, DiscordAsset.AssetType.SMALL);
                        }
                        CraftPresence.CONFIG.defaultServerIcon = scrollList.currentValue;
                    } else if (configoption.equals(CraftPresence.CONFIG.NAME_biomeMessages) || configoption.equals(CraftPresence.CONFIG.NAME_dimensionMessages)) {
                        mc.displayGuiScreen(new ConfigGUI_Editor(parentscreen, scrollList.currentValue));
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
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        searchBox.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        searchBox.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        searchBox.updateCursorCounter();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        scrollList.handleMouseInput();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
