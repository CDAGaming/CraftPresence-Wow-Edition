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
import com.gitlab.cdagaming.craftpresence.impl.PairConsumer;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import net.minecraft.client.gui.GuiScreen;

public class DynamicEditorGui extends ExtendedScreen {
    private final boolean isNewValue, isDefaultValue;
    private final PairConsumer<String, String> onAdjustEntry, onRemoveEntry;
    private final PairConsumer<String, DynamicEditorGui> onAdjustInit, onNewInit, onSpecificCallback, onHoverCallback;
    public String specificMessage, defaultMessage, mainTitle;
    private ExtendedButtonControl proceedButton;
    private ExtendedTextControl specificMessageInput, newValueName;
    private String attributeName, removeMessage;

    public DynamicEditorGui(GuiScreen parentScreen, String attributeName, PairConsumer<String, DynamicEditorGui> onNewInit, PairConsumer<String, DynamicEditorGui> onAdjustInit, PairConsumer<String, String> onAdjustEntry, PairConsumer<String, String> onRemoveEntry, PairConsumer<String, DynamicEditorGui> onSpecificCallback, PairConsumer<String, DynamicEditorGui> onHoverCallback) {
        super(parentScreen);
        this.attributeName = attributeName;
        isNewValue = StringUtils.isNullOrEmpty(attributeName);
        isDefaultValue = !StringUtils.isNullOrEmpty(attributeName) && "default".equals(attributeName);

        this.onNewInit = onNewInit;
        this.onAdjustInit = onAdjustInit;
        this.onAdjustEntry = onAdjustEntry;
        this.onRemoveEntry = onRemoveEntry;
        this.onSpecificCallback = onSpecificCallback;
        this.onHoverCallback = onHoverCallback;
    }

    @Override
    public void initializeUi() {
        if (isNewValue) {
            mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.editor.add.new");
            if (onNewInit != null) {
                onNewInit.accept(attributeName, this);
            }
        } else {
            if (onAdjustInit != null) {
                onAdjustInit.accept(attributeName, this);
            }
        }

        removeMessage = ModUtils.TRANSLATOR.translate("gui.config.message.remove");

        specificMessageInput = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                )
        );
        specificMessageInput.setText(specificMessage);

        if (onSpecificCallback != null && !isNewValue) {
            // Adding Specific Icon Button
            addControl(
                    new ExtendedButtonControl(
                            (width / 2) - 90, CraftPresence.GUIS.getButtonY(2),
                            180, 20,
                            ModUtils.TRANSLATOR.translate("gui.config.message.button.icon.change"),
                            () -> onSpecificCallback.accept(attributeName, this)
                    )
            );
        }
        if (isNewValue) {
            newValueName = addControl(
                    new ExtendedTextControl(
                            getFontRenderer(),
                            (width / 2) + 3, CraftPresence.GUIS.getButtonY(3),
                            180, 20
                    )
            );
        }

        proceedButton = addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, (height - 30),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.back"),
                        () -> {
                            if (!specificMessageInput.getText().equals(specificMessage) || (isNewValue && !StringUtils.isNullOrEmpty(newValueName.getText()) && !specificMessageInput.getText().equals(defaultMessage)) || (isDefaultValue && !StringUtils.isNullOrEmpty(specificMessageInput.getText()) && !specificMessageInput.getText().equals(specificMessage))) {
                                if (isNewValue && !StringUtils.isNullOrEmpty(newValueName.getText())) {
                                    attributeName = newValueName.getText();
                                }
                                if (onAdjustEntry != null) {
                                    onAdjustEntry.accept(attributeName, specificMessageInput.getText());
                                }
                            }
                            if (StringUtils.isNullOrEmpty(specificMessageInput.getText()) || (specificMessageInput.getText().equalsIgnoreCase(defaultMessage) && !specificMessage.equals(defaultMessage) && !isDefaultValue)) {
                                if (isNewValue && !StringUtils.isNullOrEmpty(newValueName.getText())) {
                                    attributeName = newValueName.getText();
                                }
                                if (onRemoveEntry != null) {
                                    onRemoveEntry.accept(attributeName, specificMessageInput.getText());
                                }
                            }
                            CraftPresence.GUIS.openScreen(parentScreen);
                        },
                        () -> {
                            if (!proceedButton.isControlEnabled()) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.empty.default")
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        getFontRenderer(),
                                        true
                                );
                            }
                        }
                )
        );

        super.initializeUi();
    }

    @Override
    public void preRender() {
        final String messageText = ModUtils.TRANSLATOR.translate("gui.config.message.editor.message");
        final String valueNameText = ModUtils.TRANSLATOR.translate("gui.config.message.editor.value.name");

        renderString(mainTitle, (width / 2f) - (StringUtils.getStringWidth(mainTitle) / 2f), 15, 0xFFFFFF);
        renderString(messageText, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(1, 5), 0xFFFFFF);
        if (isNewValue) {
            renderString(valueNameText, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(3, 5), 0xFFFFFF);
        } else {
            renderString(removeMessage, (width / 2f) - (StringUtils.getStringWidth(removeMessage) / 2f), (height - 45), 0xFFFFFF);
        }

        proceedButton.setControlMessage(
                !specificMessageInput.getText().equals(specificMessage) ||
                        (isNewValue && !StringUtils.isNullOrEmpty(newValueName.getText()) && !specificMessageInput.getText().equals(defaultMessage)) ||
                        (isDefaultValue && !StringUtils.isNullOrEmpty(specificMessageInput.getText()) && !specificMessageInput.getText().equals(specificMessage)) ?
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.continue") : ModUtils.TRANSLATOR.translate("gui.config.message.button.back")
        );

        proceedButton.setControlEnabled(!(StringUtils.isNullOrEmpty(specificMessageInput.getText()) && isDefaultValue));
    }

    @Override
    public void postRender() {
        final String messageText = ModUtils.TRANSLATOR.translate("gui.config.message.editor.message");
        final String valueNameText = ModUtils.TRANSLATOR.translate("gui.config.message.editor.value.name");
        final boolean isHovering = CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 130, CraftPresence.GUIS.getButtonY(1, 5), StringUtils.getStringWidth(messageText), getFontHeight());
        // Hovering over Message Label
        if (isHovering && onHoverCallback != null) {
            onHoverCallback.accept(attributeName, this);
        }

        // Hovering over Value Name Label
        if (isNewValue && CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 130, CraftPresence.GUIS.getButtonY(3, 5), StringUtils.getStringWidth(valueNameText), getFontHeight())) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.hover.value.name")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
        }
    }
}
