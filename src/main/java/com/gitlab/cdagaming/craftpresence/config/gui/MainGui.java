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
import com.gitlab.cdagaming.craftpresence.impl.Pair;
import com.gitlab.cdagaming.craftpresence.utils.CommandUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.commands.CommandsGui;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class MainGui extends ExtendedScreen {
    private ExtendedButtonControl biomeSet, dimensionSet, serverSet, statusSet, proceedButton, commandGUIButton;

    public MainGui(GuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initializeUi() {
        CraftPresence.GUIS.configGUIOpened = true;

        final int calc1 = (width / 2) - 183;
        final int calc2 = (width / 2) + 3;

        // Added General Settings Button
        addControl(
                new ExtendedButtonControl(
                        calc1, CraftPresence.GUIS.getButtonY(1),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.general"),
                        () -> CraftPresence.GUIS.openScreen(new GeneralSettingsGui(currentScreen)),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.title.general")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );
        biomeSet = addControl(
                new ExtendedButtonControl(
                        calc2, CraftPresence.GUIS.getButtonY(1),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.biome_messages"),
                        () -> CraftPresence.GUIS.openScreen(new BiomeSettingsGui(currentScreen)),
                        () -> {
                            if (!biomeSet.enabled) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.general.show_biome"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        mc.fontRenderer,
                                        true
                                );
                            } else {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.comment.title.biome_messages")
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        mc.fontRenderer,
                                        true
                                );
                            }
                        }
                )
        );
        dimensionSet = addControl(
                new ExtendedButtonControl(
                        calc1, CraftPresence.GUIS.getButtonY(2),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.dimension_messages"),
                        () -> CraftPresence.GUIS.openScreen(new DimensionSettingsGui(currentScreen)),
                        () -> {
                            if (!dimensionSet.enabled) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.general.show_dimension"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        mc.fontRenderer,
                                        true
                                );
                            } else {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.comment.title.dimension_messages")
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        mc.fontRenderer,
                                        true
                                );
                            }
                        }
                )
        );
        serverSet = addControl(
                new ExtendedButtonControl(
                        calc2, CraftPresence.GUIS.getButtonY(2),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.server_messages"),
                        () -> CraftPresence.GUIS.openScreen(new ServerSettingsGui(currentScreen)),
                        () -> {
                            if (!serverSet.enabled) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.general.show_state"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        mc.fontRenderer,
                                        true
                                );
                            } else {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.comment.title.server_messages")
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        mc.fontRenderer,
                                        true
                                );
                            }
                        }
                )
        );
        statusSet = addControl(
                new ExtendedButtonControl(
                        calc1, CraftPresence.GUIS.getButtonY(3),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.status_messages"),
                        () -> CraftPresence.GUIS.openScreen(new StatusMessagesGui(currentScreen)),
                        () -> {
                            if (!statusSet.enabled) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.access",
                                                        ModUtils.TRANSLATOR.translate("gui.config.name.general.show_state"))
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        mc.fontRenderer,
                                        true
                                );
                            } else {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.comment.title.status_messages")
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        -1,
                                        mc.fontRenderer,
                                        true
                                );
                            }
                        }
                )
        );
        // Added Advanced Settings Button
        addControl(
                new ExtendedButtonControl(
                        calc2, CraftPresence.GUIS.getButtonY(3),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.advanced"),
                        () -> CraftPresence.GUIS.openScreen(new AdvancedSettingsGui(currentScreen)),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.title.advanced")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );
        // Added Accessibility Settings Button
        addControl(
                new ExtendedButtonControl(
                        calc1, CraftPresence.GUIS.getButtonY(4),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.accessibility"),
                        () -> CraftPresence.GUIS.openScreen(new AccessibilitySettingsGui(currentScreen)),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.title.accessibility")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );
        // Added Presence Settings Button
        addControl(
                new ExtendedButtonControl(
                        calc2, CraftPresence.GUIS.getButtonY(4),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.presence_settings"),
                        () -> CraftPresence.GUIS.openScreen(new PresenceSettingsGui(currentScreen)),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.presence_settings")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
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
                            if (CraftPresence.CONFIG.hasChanged) {
                                CraftPresence.CONFIG.updateConfig(false);
                                CraftPresence.CONFIG.read(false, "UTF-8");
                                if (CraftPresence.CONFIG.hasClientPropertiesChanged) {
                                    CommandUtils.rebootRPC();
                                    CraftPresence.CONFIG.hasClientPropertiesChanged = false;
                                }
                                CommandUtils.reloadData(true);
                                CraftPresence.CONFIG.hasChanged = false;
                            }

                            CraftPresence.GUIS.configGUIOpened = false;
                            if (mc.player != null) {
                                mc.player.closeScreen();
                            } else {
                                CraftPresence.GUIS.openScreen(parentScreen);
                            }
                        }
                )
        );
        // Added About Button
        addControl(
                new ExtendedButtonControl(
                        (width - 105), (height - 55),
                        95, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.about"),
                        () -> CraftPresence.GUIS.openScreen(new AboutGui(currentScreen))
                )
        );
        commandGUIButton = addControl(
                new ExtendedButtonControl(
                        (width - 105), (height - 30),
                        95, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.title.commands"),
                        () -> CraftPresence.GUIS.openScreen(new CommandsGui(currentScreen))
                )
        );
        // Added Reset Config Button
        addControl(
                new ExtendedButtonControl(
                        10, (height - 30),
                        95, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.reset"),
                        () -> {
                            CraftPresence.CONFIG.setupInitialValues();
                            CraftPresence.CONFIG.hasChanged = true;
                            CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                            syncRenderStates();
                        }
                )
        );
        // Added Sync Config Button
        addControl(
                new ExtendedButtonControl(
                        10, (height - 55),
                        95, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.sync.config"),
                        () -> {
                            final List<Pair<String, Object>> currentConfigDataMappings = CraftPresence.CONFIG.configDataMappings;
                            CraftPresence.CONFIG.read(false, "UTF-8");

                            // Only Mark to Save if there have been Changes in the File
                            if (!CraftPresence.CONFIG.configDataMappings.equals(currentConfigDataMappings)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                syncRenderStates();
                            }
                        },
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.button.sync.config")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );

        super.initializeUi();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        preDraw();

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String releaseNotice = ModUtils.TRANSLATOR.translate("gui.config.message.tentative", ModUtils.VERSION_ID + " - " + StringUtils.formatWord(ModUtils.VERSION_LABEL));

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);

        // noinspection RedundantSuppression,ConstantConditions,MismatchedStringCase
        if (!ModUtils.VERSION_TYPE.equals("release")) {
            drawString(mc.fontRenderer, releaseNotice, (width / 2) - 30 - (StringUtils.getStringWidth(releaseNotice) / 2), height - 85, 0xFFFFFF);
        }

        syncRenderStates();

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Title Label
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - (StringUtils.getStringWidth(mainTitle) / 2f), 15, StringUtils.getStringWidth(mainTitle), mc.fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.title", ModUtils.VERSION_ID, ModUtils.MOD_SCHEMA_VERSION)), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            if (CraftPresence.CONFIG.hasChanged || CraftPresence.CONFIG.hasClientPropertiesChanged) {
                CraftPresence.CONFIG.setupInitialValues();
                CraftPresence.CONFIG.read(false, "UTF-8");
                CraftPresence.CONFIG.hasChanged = false;
                CraftPresence.CONFIG.hasClientPropertiesChanged = false;
            }
            CraftPresence.GUIS.configGUIOpened = false;
        }
        super.keyTyped(typedChar, keyCode);
    }

    private void syncRenderStates() {
        biomeSet.enabled = CraftPresence.CONFIG.showCurrentBiome;
        dimensionSet.enabled = CraftPresence.CONFIG.showCurrentDimension;
        serverSet.enabled = CraftPresence.CONFIG.showGameState;
        statusSet.enabled = CraftPresence.CONFIG.showGameState;
        commandGUIButton.enabled = CraftPresence.CONFIG.enableCommands;

        proceedButton.displayString = CraftPresence.CONFIG.hasChanged ? ModUtils.TRANSLATOR.translate("gui.config.message.button.save") : ModUtils.TRANSLATOR.translate("gui.config.message.button.back");
    }
}
