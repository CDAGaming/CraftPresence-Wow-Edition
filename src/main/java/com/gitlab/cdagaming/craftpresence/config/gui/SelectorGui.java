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
import com.gitlab.cdagaming.craftpresence.impl.TupleConsumer;
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
    private final String mainTitle, configOption, attributeName, originalValue;
    private final List<String> originalList;
    private final boolean allowContinuing, allowDynamicEditing;
    private ExtendedButtonControl proceedButton;
    private ScrollableListControl scrollList;
    private ExtendedTextControl searchBox;
    private String searchTerm;
    private List<String> itemList;
    private final TupleConsumer<String, String, String> proceedCallback;
    private final RenderType renderType;

    public SelectorGui(GuiScreen parentScreen, String configOption, String mainTitle, List<String> list, String currentValue, String attributeName, boolean allowContinuing, boolean allowDynamicEditing, RenderType renderType, TupleConsumer<String, String, String> proceedCallback) {
        super(parentScreen);
        itemList = originalList = list;
        originalValue = currentValue;
        this.mainTitle = mainTitle;
        this.attributeName = attributeName;
        this.configOption = configOption;
        this.allowContinuing = allowContinuing;
        this.allowDynamicEditing = allowDynamicEditing;
        this.renderType = renderType;
        this.proceedCallback = proceedCallback;
    }

    public SelectorGui(GuiScreen parentScreen, String configOption, String mainTitle, List<String> list, String currentValue, String attributeName, boolean allowContinuing, boolean allowDynamicEditing, RenderType renderType) {
        this(parentScreen, configOption, mainTitle, list, currentValue, attributeName, allowContinuing, allowDynamicEditing, renderType, null);
    }

    public SelectorGui(GuiScreen parentScreen, String configOption, String mainTitle, List<String> list, String currentValue, String attributeName, boolean allowContinuing, boolean allowDynamicEditing) {
        this(parentScreen, configOption, mainTitle, list, currentValue, attributeName, allowContinuing, allowDynamicEditing, RenderType.None);
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
                                            if (proceedCallback != null) {
                                                proceedCallback.accept(configOption, attributeName, scrollList.currentValue);
                                            } else {
                                                CraftPresence.GUIS.openScreen(new MessageGui(parentScreen, StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.null"))));
                                            }
                                        } else {
                                            CraftPresence.GUIS.openScreen(parentScreen);
                                        }
                                    } else {
                                        if (allowDynamicEditing) {
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
                            32, height - 45, renderType != RenderType.None ? 45 : 18,
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

            if (allowDynamicEditing) {
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
        renderString(searchText, (30 - (StringUtils.getStringWidth(searchText) / 2)), (height - 25), 0xFFFFFF);
        renderString(mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);
    }
}
