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

package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAssetUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ScrollableListControl;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public class SelectorGui extends ExtendedScreen {
    private final String mainTitle, configOption, attributeName, originalValue;
    private final List<String> originalList;
    private final boolean allowContinuing;
    private ExtendedButtonControl proceedButton;
    private ScrollableListControl scrollList;
    private ExtendedTextControl searchBox;
    private String searchTerm;
    private List<String> itemList;

    public SelectorGui(GuiScreen parentScreen, String configOption, String mainTitle, List<String> list, String currentValue, String attributeName, boolean allowContinuing) {
        super(parentScreen);
        itemList = originalList = list;
        originalValue = currentValue;
        this.mainTitle = mainTitle;
        this.attributeName = attributeName;
        this.configOption = configOption;
        this.allowContinuing = allowContinuing;
    }

    @Override
    public void initializeUi() {
        if (itemList != null && !itemList.isEmpty()) {
            proceedButton = addControl(
                    new ExtendedButtonControl(
                            (width - 100), (height - 30),
                            90, 20,
                            ModUtils.TRANSLATOR.translate("gui.config.message.button.back"),
                            () -> {
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
                    )
            );
            scrollList = addList(
                    new ScrollableListControl(
                            mc,
                            width, height,
                            32, height - 45, 18,
                            itemList, originalValue
                    )
            );
            searchBox = addControl(
                    new ExtendedTextControl(
                            mc.fontRenderer,
                            60, (height - 30),
                            120, 20
                    )
            );

            if (allowContinuing && !originalList.equals(DiscordAssetUtils.ICON_LIST)) {
                // Adding Add New Button
                addControl(
                        new ExtendedButtonControl(
                                (width - 195), (height - 30),
                                90, 20,
                                ModUtils.TRANSLATOR.translate("gui.config.message.button.add.new"),
                                () -> CraftPresence.GUIS.openScreen(new DynamicEditorGui(parentScreen, null, configOption))
                        )
                );
            }

            super.initializeUi();
        } else {
            CraftPresence.GUIS.openScreen(new MessageGui(parentScreen, StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.empty.list"))));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        preDraw();

        final String searchText = ModUtils.TRANSLATOR.translate("gui.config.message.editor.search");
        final List<String> modifiedList = Lists.newArrayList();

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

        proceedButton.displayString = allowContinuing && scrollList.currentValue != null && ((originalValue != null && !scrollList.currentValue.equals(originalValue)) || (StringUtils.isNullOrEmpty(originalValue))) ? ModUtils.TRANSLATOR.translate("gui.config.message.button.continue") : ModUtils.TRANSLATOR.translate("gui.config.message.button.back");

        super.drawScreen(mouseX, mouseY, partialTicks);

        drawString(mc.fontRenderer, searchText, (30 - (StringUtils.getStringWidth(searchText) / 2)), (height - 25), 0xFFFFFF);
        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);
    }
}
