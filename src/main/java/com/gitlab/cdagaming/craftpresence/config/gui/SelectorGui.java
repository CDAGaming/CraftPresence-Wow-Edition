package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAssetUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ScrollableListControl;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

public class SelectorGui extends GuiScreen {
    public final GuiScreen parentScreen;
    private final String mainTitle, configOption, attributeName, originalValue;
    private final List<String> originalList;
    private final boolean allowContinuing;
    private ExtendedButtonControl proceedButton, addNewButton;
    private ScrollableListControl scrollList;
    private GuiTextField searchBox;
    private String searchTerm;
    private List<String> itemList;

    public SelectorGui(GuiScreen parentScreen, String configOption, String mainTitle, List<String> list, String currentValue, String attributeName, boolean allowContinuing) {
        mc = CraftPresence.instance;
        itemList = originalList = list;
        originalValue = currentValue;
        this.mainTitle = mainTitle;
        this.parentScreen = parentScreen;
        this.attributeName = attributeName;
        this.configOption = configOption;
        this.allowContinuing = allowContinuing;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        if (itemList != null && !itemList.isEmpty()) {
            proceedButton = new ExtendedButtonControl(700, (width - 100), (height - 30), 90, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"));
            scrollList = new ScrollableListControl(mc, width, height, 32, height - 45, 18, itemList, originalValue);
            searchBox = new GuiTextField(110, mc.fontRenderer, 60, (height - 30), 120, 20);

            if (allowContinuing && !originalList.equals(DiscordAssetUtils.ICON_LIST)) {
                addNewButton = new ExtendedButtonControl(600, (width - 195), (height - 30), 90, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.addnew"));
                buttonList.add(addNewButton);
            }

            buttonList.add(proceedButton);

            super.initGui();
        } else {
            CraftPresence.GUIS.openScreen(new MessageGui(parentScreen, StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.emptylist"))));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

        final String searchText = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.search");
        List<String> modifiedList = Lists.newArrayList();

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
            if (originalValue != null && itemList.contains(originalValue)) {
                scrollList.currentValue = originalValue;
            } else {
                scrollList.currentValue = null;
            }
        } else if (scrollList.currentValue == null && originalValue != null) {
            scrollList.currentValue = originalValue;
        }

        scrollList.itemList = itemList;
        scrollList.drawScreen(mouseX, mouseY, partialTicks);
        drawString(mc.fontRenderer, searchText, (30 - (StringUtils.getStringWidth(searchText) / 2)), (height - 25), 0xFFFFFF);
        searchBox.drawTextBox();
        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);

        proceedButton.displayString = allowContinuing && scrollList.currentValue != null && ((originalValue != null && !scrollList.currentValue.equals(originalValue)) || (StringUtils.isNullOrEmpty(originalValue))) ? ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.continue") : ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back");

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(@Nonnull GuiButton button) {
        if (buttonList.contains(addNewButton) && button.id == addNewButton.id) {
            CraftPresence.GUIS.openScreen(new DynamicEditorGui(parentScreen, null, configOption));
        } else if (button.id == proceedButton.id) {
            if (allowContinuing && scrollList.currentValue != null) {
                if (originalValue != null) {
                    if (!scrollList.currentValue.equals(originalValue)) {
                        if (configOption.equals(CraftPresence.CONFIG.NAME_defaultIcon)) {
                            CraftPresence.CONFIG.hasChanged = true;
                            CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                            CraftPresence.CONFIG.defaultIcon = scrollList.currentValue;
                            CraftPresence.GUIS.openScreen(parentScreen);
                        } else if (configOption.equals(CraftPresence.CONFIG.NAME_defaultServerIcon)) {
                            CraftPresence.CONFIG.hasChanged = true;
                            CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                            CraftPresence.CONFIG.defaultServerIcon = scrollList.currentValue;
                            CraftPresence.GUIS.openScreen(parentScreen);
                        } else if (configOption.equals(CraftPresence.CONFIG.NAME_defaultDimensionIcon)) {
                            CraftPresence.CONFIG.hasChanged = true;
                            CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                            CraftPresence.CONFIG.defaultDimensionIcon = scrollList.currentValue;
                            CraftPresence.GUIS.openScreen(parentScreen);
                        } else if (configOption.equals(CraftPresence.CONFIG.NAME_dimensionMessages)) {
                            final String defaultDimensionMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                            final String currentDimensionMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, null);

                            CraftPresence.CONFIG.hasChanged = true;
                            if (StringUtils.isNullOrEmpty(currentDimensionMSG) || currentDimensionMSG.equals(defaultDimensionMSG)) {
                                CraftPresence.CONFIG.dimensionMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.dimensionMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultDimensionMSG);
                            }
                            CraftPresence.CONFIG.dimensionMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.dimensionMessages, attributeName, 0, 2, CraftPresence.CONFIG.splitCharacter, scrollList.currentValue);
                            CraftPresence.GUIS.openScreen(parentScreen);
                        } else if (configOption.equals(CraftPresence.CONFIG.NAME_serverMessages)) {
                            final String defaultServerMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                            final String currentServerMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, null);

                            CraftPresence.CONFIG.hasChanged = true;
                            if (StringUtils.isNullOrEmpty(currentServerMSG) || currentServerMSG.equals(defaultServerMSG)) {
                                CraftPresence.CONFIG.serverMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultServerMSG);
                            }
                            CraftPresence.CONFIG.serverMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 2, CraftPresence.CONFIG.splitCharacter, scrollList.currentValue);
                            CraftPresence.GUIS.openScreen(parentScreen);
                        } else {
                            CraftPresence.GUIS.openScreen(new MessageGui(parentScreen, StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.null"))));
                        }
                    } else {
                        CraftPresence.GUIS.openScreen(parentScreen);
                    }
                } else {
                    if (configOption.equals(CraftPresence.CONFIG.NAME_biomeMessages) || configOption.equals(CraftPresence.CONFIG.NAME_dimensionMessages) || configOption.equals(CraftPresence.CONFIG.NAME_serverMessages) || configOption.equals(CraftPresence.CONFIG.NAME_guiMessages) || configOption.equals(CraftPresence.CONFIG.NAME_itemMessages)) {
                        CraftPresence.GUIS.openScreen(new DynamicEditorGui(parentScreen, scrollList.currentValue, configOption));
                    } else {
                        CraftPresence.GUIS.openScreen(new MessageGui(parentScreen, StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.null"))));
                    }
                }
            } else {
                CraftPresence.GUIS.openScreen(parentScreen);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            CraftPresence.GUIS.openScreen(parentScreen);
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
