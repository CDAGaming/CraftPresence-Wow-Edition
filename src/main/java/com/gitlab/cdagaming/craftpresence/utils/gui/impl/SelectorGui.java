/*
 * MIT License
 *
 * Copyright (c) 2018 - 2021 CDAGaming (cstack2011@yahoo.com)
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

package com.gitlab.cdagaming.craftpresence.utils.gui.impl;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.DataConsumer;
import com.gitlab.cdagaming.craftpresence.impl.PairConsumer;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ScrollableListControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ScrollableListControl.RenderType;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public class SelectorGui extends ExtendedScreen {
    private final String mainTitle, attributeName, originalValue;
    private final List<String> originalList;
    private final boolean allowContinuing, allowDynamicEditing;
    private final PairConsumer<String, String> onUpdatedCallback;
    private final RenderType renderType;
    private final PairConsumer<String, GuiScreen> onAdjustDynamicEntry;
    private final DataConsumer<GuiScreen> onAddDynamicEntry;
    private ExtendedButtonControl proceedButton;
    private ScrollableListControl scrollList;
    private ExtendedTextControl searchBox;
    private String searchTerm;
    private List<String> itemList;

    public SelectorGui(GuiScreen parentScreen, String mainTitle, List<String> list, String currentValue, String attributeName, boolean allowContinuing, boolean allowDynamicEditing, RenderType renderType, PairConsumer<String, String> onUpdatedCallback, PairConsumer<String, GuiScreen> onAdjustDynamicEntry, DataConsumer<GuiScreen> onAddDynamicEntry) {
        super(parentScreen);
        itemList = originalList = list;
        originalValue = currentValue;
        this.mainTitle = mainTitle;
        this.attributeName = attributeName;
        this.allowContinuing = allowContinuing;
        this.allowDynamicEditing = allowDynamicEditing;
        this.renderType = renderType;
        this.onUpdatedCallback = onUpdatedCallback;
        this.onAdjustDynamicEntry = onAdjustDynamicEntry;
        this.onAddDynamicEntry = onAddDynamicEntry;
    }

    public SelectorGui(GuiScreen parentScreen, String mainTitle, List<String> list, String currentValue, String attributeName, boolean allowContinuing, boolean allowDynamicEditing, RenderType renderType, PairConsumer<String, GuiScreen> onAdjustDynamicEntry, DataConsumer<GuiScreen> onAddDynamicEntry) {
        this(parentScreen, mainTitle, list, currentValue, attributeName, allowContinuing, allowDynamicEditing, renderType, null, onAdjustDynamicEntry, onAddDynamicEntry);
    }

    public SelectorGui(GuiScreen parentScreen, String mainTitle, List<String> list, String currentValue, String attributeName, boolean allowContinuing, boolean allowDynamicEditing, PairConsumer<String, GuiScreen> onAdjustDynamicEntry, DataConsumer<GuiScreen> onAddDynamicEntry) {
        this(parentScreen, mainTitle, list, currentValue, attributeName, allowContinuing, allowDynamicEditing, RenderType.None, onAdjustDynamicEntry, onAddDynamicEntry);
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
                                            if (onUpdatedCallback != null) {
                                                onUpdatedCallback.accept(attributeName, scrollList.currentValue);
                                                CraftPresence.GUIS.openScreen(parentScreen);
                                            } else {
                                                CraftPresence.GUIS.openScreen(new MessageGui(parentScreen, StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.null"))));
                                            }
                                        } else {
                                            CraftPresence.GUIS.openScreen(parentScreen);
                                        }
                                    } else {
                                        if (allowDynamicEditing && onAdjustDynamicEntry != null) {
                                            onAdjustDynamicEntry.accept(scrollList.currentValue, parentScreen);
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
                            32, height - 45, renderType != RenderType.None && !CraftPresence.CONFIG.stripExtraGuiElements ? 45 : 18,
                            itemList, originalValue,
                            renderType
                    )
            );
            searchBox = addControl(
                    new ExtendedTextControl(
                            getFontRenderer(),
                            60, (height - 30),
                            120, 20
                    )
            );

            if (allowDynamicEditing && onAddDynamicEntry != null) {
                // Adding Add New Button
                addControl(
                        new ExtendedButtonControl(
                                (width - 195), (height - 30),
                                90, 20,
                                ModUtils.TRANSLATOR.translate("gui.config.message.button.add.new"),
                                () -> onAddDynamicEntry.accept(parentScreen)
                        )
                );
            }

            super.initializeUi();
        } else {
            CraftPresence.GUIS.openScreen(new MessageGui(parentScreen, StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.empty.list"))));
        }
    }

    @Override
    public void preRender() {
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

        proceedButton.setControlMessage(
                allowContinuing && scrollList.currentValue != null &&
                        ((originalValue != null && !scrollList.currentValue.equals(originalValue)) || (StringUtils.isNullOrEmpty(originalValue))) ?
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.continue") : ModUtils.TRANSLATOR.translate("gui.config.message.button.back")
        );
    }

    @Override
    public void postRender() {
        final String searchText = ModUtils.TRANSLATOR.translate("gui.config.message.editor.search");
        final String extraText = isVerboseMode() ? ModUtils.TRANSLATOR.translate("gui.config.title.selector.extra", itemList.size(), originalList.size()) : "";
        final String displayText = mainTitle + " " + extraText;

        renderString(searchText, (30 - (StringUtils.getStringWidth(searchText) / 2f)), (height - 25), 0xFFFFFF);
        renderString(displayText, (width / 2f) - (StringUtils.getStringWidth(displayText) / 2f), 15, 0xFFFFFF);
    }
}
