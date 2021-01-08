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

package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAssetUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ScrollableListControl.RenderType;
import com.gitlab.cdagaming.craftpresence.utils.gui.impl.DynamicEditorGui;
import com.gitlab.cdagaming.craftpresence.utils.gui.impl.SelectorGui;
import net.minecraft.client.gui.GuiScreen;

@SuppressWarnings("DuplicatedCode")
public class ServerSettingsGui extends ExtendedScreen {
    private ExtendedButtonControl proceedButton, serverMessagesButton;
    private ExtendedTextControl defaultMOTD, defaultName, defaultMessage;

    ServerSettingsGui(GuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initializeUi() {
        final String defaultServerMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);

        defaultName = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                )
        );
        defaultName.setText(CraftPresence.CONFIG.defaultServerName);
        defaultMOTD = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(2),
                        180, 20
                )
        );
        defaultMOTD.setText(CraftPresence.CONFIG.defaultServerMotd);
        defaultMessage = addControl(
                new ExtendedTextControl(
                        getFontRenderer(),
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(3),
                        180, 20
                )
        );
        defaultMessage.setText(defaultServerMessage);

        serverMessagesButton = addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, CraftPresence.GUIS.getButtonY(4),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.server_messages.server_messages"),
                        () -> CraftPresence.GUIS.openScreen(
                                new SelectorGui(
                                        currentScreen,
                                        ModUtils.TRANSLATOR.translate("gui.config.title.selector.server"), CraftPresence.SERVER.knownAddresses,
                                        null, null,
                                        true, true, RenderType.ServerData,
                                        (attributeName, currentValue) -> {
                                            final String defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                                            final String currentMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, null);

                                            CraftPresence.CONFIG.hasChanged = true;
                                            if (StringUtils.isNullOrEmpty(currentMessage) || currentMessage.equals(defaultMessage)) {
                                                CraftPresence.CONFIG.serverMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMessage);
                                            }
                                            CraftPresence.CONFIG.serverMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 2, CraftPresence.CONFIG.splitCharacter, currentValue);
                                        },
                                        (currentValue, parentScreen) -> {
                                            // Event to occur when Setting Dynamic/Specific Data
                                            CraftPresence.GUIS.openScreen(
                                                    new DynamicEditorGui(
                                                            parentScreen, currentValue, null,
                                                            (attributeName, screenInstance) -> {
                                                                // Event to occur when initializing existing data
                                                                screenInstance.mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.server.edit_specific_server", attributeName);
                                                                screenInstance.defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                                                                screenInstance.specificMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, screenInstance.defaultMessage);
                                                            },
                                                            (attributeName, inputText) -> {
                                                                // Event to occur when adjusting set data
                                                                CraftPresence.CONFIG.hasChanged = true;
                                                                CraftPresence.CONFIG.serverMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, inputText);
                                                            },
                                                            (attributeName, inputText) -> {
                                                                // Event to occur when removing set data
                                                                CraftPresence.CONFIG.serverMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.serverMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                                                CraftPresence.SERVER.knownAddresses.remove(attributeName);
                                                                CraftPresence.SERVER.getServerAddresses();
                                                            },
                                                            (attributeName, screenInstance) -> {
                                                                // Event to occur when adding an attachment icon to set data
                                                                final String defaultIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 2, CraftPresence.CONFIG.splitCharacter, CraftPresence.CONFIG.defaultServerIcon);
                                                                final String specificIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 2, CraftPresence.CONFIG.splitCharacter, defaultIcon);
                                                                CraftPresence.GUIS.openScreen(
                                                                        new SelectorGui(
                                                                                screenInstance,
                                                                                ModUtils.TRANSLATOR.translate("gui.config.title.selector.icon"), DiscordAssetUtils.ICON_LIST,
                                                                                specificIcon, attributeName,
                                                                                true, false, RenderType.DiscordAsset,
                                                                                (innerAttributeName, innerCurrentValue) -> {
                                                                                    // Inner-Event to occur when proceeding with adjusted data
                                                                                    final String defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                                                                                    final String currentMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, innerAttributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, null);

                                                                                    CraftPresence.CONFIG.hasChanged = true;
                                                                                    if (StringUtils.isNullOrEmpty(currentMessage) || currentMessage.equals(defaultMessage)) {
                                                                                        CraftPresence.CONFIG.serverMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.serverMessages, innerAttributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMessage);
                                                                                    }
                                                                                    CraftPresence.CONFIG.serverMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.serverMessages, innerAttributeName, 0, 2, CraftPresence.CONFIG.splitCharacter, innerCurrentValue);
                                                                                }, null, null
                                                                        )
                                                                );
                                                            },
                                                            (attributeName, screenInstance) -> {
                                                                // Event to occur when Hovering over Message Label
                                                                CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.server_messages.server_messages")), screenInstance.getMouseX(), screenInstance.getMouseY(), screenInstance.width, screenInstance.height, screenInstance.getWrapWidth(), screenInstance.getFontRenderer(), true);
                                                            }
                                                    )
                                            );
                                        },
                                        (parentScreen) ->
                                                CraftPresence.GUIS.openScreen(
                                                        new DynamicEditorGui(
                                                                parentScreen, null,
                                                                (attributeName, screenInstance) -> {
                                                                    // Event to occur when initializing new data
                                                                    screenInstance.specificMessage = screenInstance.defaultMessage = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                                                                }, null,
                                                                (attributeName, inputText) -> {
                                                                    // Event to occur when adjusting set data
                                                                    CraftPresence.CONFIG.hasChanged = true;
                                                                    CraftPresence.CONFIG.serverMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, inputText);
                                                                },
                                                                (attributeName, inputText) -> {
                                                                    // Event to occur when removing set data
                                                                    CraftPresence.CONFIG.serverMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.serverMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                                                                    CraftPresence.SERVER.knownAddresses.remove(attributeName);
                                                                    CraftPresence.SERVER.getServerAddresses();
                                                                }, null,
                                                                (attributeName, screenInstance) -> {
                                                                    // Event to occur when Hovering over Message Label
                                                                    CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.server_messages.server_messages")), screenInstance.getMouseX(), screenInstance.getMouseY(), screenInstance.width, screenInstance.height, screenInstance.getWrapWidth(), screenInstance.getFontRenderer(), true);
                                                                }
                                                        )
                                                )
                                )
                        ),
                        () -> {
                            if (!serverMessagesButton.isControlEnabled()) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.server_messages.server_messages"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        getFontRenderer(),
                                        true
                                );
                            } else {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.comment.server_messages.server_messages")
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
        // Adding Default Icon Button
        addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, CraftPresence.GUIS.getButtonY(5),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.server_messages.server_icon"),
                        () -> CraftPresence.GUIS.openScreen(
                                new SelectorGui(
                                        currentScreen,
                                        ModUtils.TRANSLATOR.translate("gui.config.title.selector.icon"), DiscordAssetUtils.ICON_LIST,
                                        CraftPresence.CONFIG.defaultServerIcon, null,
                                        true, false, RenderType.DiscordAsset,
                                        (attributeName, currentValue) -> {
                                            CraftPresence.CONFIG.hasChanged = true;
                                            CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                            CraftPresence.CONFIG.defaultServerIcon = currentValue;
                                        }, null, null
                                )
                        ),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.server_messages.server_icon")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                getFontRenderer(),
                                true
                        )
                )
        );
        proceedButton = addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, (height - 30),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.back"),
                        () -> {
                            if (!defaultName.getText().equals(CraftPresence.CONFIG.defaultServerName)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.defaultServerName = defaultName.getText();
                            }
                            if (!defaultMOTD.getText().equals(CraftPresence.CONFIG.defaultServerMotd)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.defaultServerMotd = defaultMOTD.getText();
                            }
                            if (!defaultMessage.getText().equals(defaultServerMessage)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                StringUtils.setConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMessage.getText());
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
        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.server_messages");
        final String serverNameText = ModUtils.TRANSLATOR.translate("gui.config.name.server_messages.server_name");
        final String serverMOTDText = ModUtils.TRANSLATOR.translate("gui.config.name.server_messages.server_motd");
        final String defaultMessageText = ModUtils.TRANSLATOR.translate("gui.config.message.default.server");

        renderString(mainTitle, (width / 2f) - (StringUtils.getStringWidth(mainTitle) / 2f), 10, 0xFFFFFF);
        renderString(subTitle, (width / 2f) - (StringUtils.getStringWidth(subTitle) / 2f), 20, 0xFFFFFF);
        renderString(serverNameText, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        renderString(serverMOTDText, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(2) + 5, 0xFFFFFF);
        renderString(defaultMessageText, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);

        proceedButton.setControlEnabled(!StringUtils.isNullOrEmpty(defaultMessage.getText()) || !StringUtils.isNullOrEmpty(defaultName.getText()) || !StringUtils.isNullOrEmpty(defaultMOTD.getText()));
        serverMessagesButton.setControlEnabled(CraftPresence.SERVER.enabled);
    }

    @Override
    public void postRender() {
        final String serverNameText = ModUtils.TRANSLATOR.translate("gui.config.name.server_messages.server_name");
        final String serverMOTDText = ModUtils.TRANSLATOR.translate("gui.config.name.server_messages.server_motd");
        final String defaultMessageText = ModUtils.TRANSLATOR.translate("gui.config.message.default.server");
        // Hovering over Default Server Name Label
        if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 130, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(serverNameText), getFontHeight())) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.server_messages.server_name")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
        }
        // Hovering over Default Server MOTD Label
        if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 130, CraftPresence.GUIS.getButtonY(2) + 5, StringUtils.getStringWidth(serverMOTDText), getFontHeight())) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.server_messages.server_motd")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
        }
        // Hovering over Default Server Message Label
        if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 130, CraftPresence.GUIS.getButtonY(3) + 5, StringUtils.getStringWidth(defaultMessageText), getFontHeight())) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.title.server_messages")), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
        }
    }
}
