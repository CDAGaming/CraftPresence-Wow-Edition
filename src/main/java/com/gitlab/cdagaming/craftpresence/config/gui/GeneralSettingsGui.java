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
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.PartyPrivacy;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.CheckBoxControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import net.minecraft.client.gui.GuiScreen;

@SuppressWarnings("DuplicatedCode")
public class GeneralSettingsGui extends ExtendedScreen {
    private ExtendedButtonControl proceedButton, partyPrivacyLevelButton;
    private CheckBoxControl detectCurseManifestButton, detectMultiMCManifestButton,
            detectMCUpdaterInstanceButton, detectTechnicPackButton, showTimeButton,
            detectBiomeDataButton, detectDimensionDataButton, detectWorldDataButton,
            enableJoinRequestButton, resetTimeOnInitButton;
    private ExtendedTextControl clientId;

    private int currentPartyPrivacy = PartyPrivacy.Public.getPartyIndex();

    GeneralSettingsGui(GuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initializeUi() {
        clientId = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                )
        );
        clientId.setText(CraftPresence.CONFIG.clientId);
        clientId.setMaxStringLength(18);

        final int buttonCalc1 = (width / 2) - 183;
        final int buttonCalc2 = (width / 2) + 3;

        final int checkboxCalc1 = (width / 2) - 145;
        final int checkboxCalc2 = (width / 2) + 18;

        // Adding Default Icon Button
        addControl(
                new ExtendedButtonControl(
                        buttonCalc1, CraftPresence.GUIS.getButtonY(2),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.default_icon"),
                        () -> CraftPresence.GUIS.openScreen(new SelectorGui(currentScreen, CraftPresence.CONFIG.NAME_defaultIcon, ModUtils.TRANSLATOR.translate("gui.config.title.selector.icon"), DiscordAssetUtils.ICON_LIST, CraftPresence.CONFIG.defaultIcon, null, true)),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.default_icon")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                mc.fontRenderer,
                                true
                        )
                )
        );
        currentPartyPrivacy = CraftPresence.CONFIG.partyPrivacyLevel;
        partyPrivacyLevelButton = addControl(
                new ExtendedButtonControl(
                        buttonCalc2, CraftPresence.GUIS.getButtonY(2),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.party_privacy") + " => " + PartyPrivacy.from(currentPartyPrivacy).getDisplayName(),
                        () -> {
                            currentPartyPrivacy = (currentPartyPrivacy + 1) % 2;
                        },
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.party_privacy")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                mc.fontRenderer,
                                true
                        )
                )
        );
        detectCurseManifestButton = addControl(
                new CheckBoxControl(
                        checkboxCalc1, CraftPresence.GUIS.getButtonY(3),
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.detect_curse_manifest"),
                        CraftPresence.CONFIG.detectCurseManifest,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.detect_curse_manifest")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                mc.fontRenderer,
                                true
                        )
                )
        );
        detectMultiMCManifestButton = addControl(
                new CheckBoxControl(
                        checkboxCalc2, CraftPresence.GUIS.getButtonY(3),
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.detect_multimc_manifest"),
                        CraftPresence.CONFIG.detectMultiMCManifest,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.detect_multimc_manifest")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                mc.fontRenderer,
                                true
                        )
                )
        );
        detectMCUpdaterInstanceButton = addControl(
                new CheckBoxControl(
                        checkboxCalc1, CraftPresence.GUIS.getButtonY(4) - 10,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.detect_mcupdater_instance"),
                        CraftPresence.CONFIG.detectMCUpdaterInstance,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.detect_mcupdater_instance")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                mc.fontRenderer,
                                true
                        )
                )
        );
        detectTechnicPackButton = addControl(
                new CheckBoxControl(
                        checkboxCalc2, CraftPresence.GUIS.getButtonY(4) - 10,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.detect_technic_pack"),
                        CraftPresence.CONFIG.detectTechnicPack,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.detect_technic_pack")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                mc.fontRenderer,
                                true
                        )
                )
        );
        showTimeButton = addControl(
                new CheckBoxControl(
                        checkboxCalc1, CraftPresence.GUIS.getButtonY(5) - 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.show_time"),
                        CraftPresence.CONFIG.showTime,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.show_time")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                mc.fontRenderer,
                                true
                        )
                )
        );
        detectBiomeDataButton = addControl(
                new CheckBoxControl(
                        checkboxCalc2, CraftPresence.GUIS.getButtonY(5) - 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.detect_biome_data"),
                        CraftPresence.CONFIG.detectBiomeData,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.detect_biome_data")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                mc.fontRenderer,
                                true
                        )
                )
        );
        detectDimensionDataButton = addControl(
                new CheckBoxControl(
                        checkboxCalc1, CraftPresence.GUIS.getButtonY(6) - 30,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.detect_dimension_data"),
                        CraftPresence.CONFIG.detectDimensionData,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.detect_dimension_data")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                mc.fontRenderer,
                                true
                        )
                )
        );
        detectWorldDataButton = addControl(
                new CheckBoxControl(
                        checkboxCalc2, CraftPresence.GUIS.getButtonY(6) - 30,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.detect_world_data"),
                        CraftPresence.CONFIG.detectWorldData,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.detect_world_data")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                mc.fontRenderer,
                                true
                        )
                )
        );
        enableJoinRequestButton = addControl(
                new CheckBoxControl(
                        checkboxCalc1, CraftPresence.GUIS.getButtonY(7) - 40,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.enable_join_request"),
                        CraftPresence.CONFIG.enableJoinRequest,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.enable_join_request")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
                                mc.fontRenderer,
                                true
                        )
                )
        );
        resetTimeOnInitButton = addControl(
                new CheckBoxControl(
                        checkboxCalc2, CraftPresence.GUIS.getButtonY(7) - 40,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.reset_time_on_init"),
                        CraftPresence.CONFIG.resetTimeOnInit,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.reset_time_on_init")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                getWrapWidth(),
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
                            if (!clientId.getText().equals(CraftPresence.CONFIG.clientId)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.clientId = clientId.getText();
                            }
                            if (currentPartyPrivacy != CraftPresence.CONFIG.partyPrivacyLevel) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.partyPrivacyLevel = currentPartyPrivacy;
                            }
                            if (detectCurseManifestButton.isChecked() != CraftPresence.CONFIG.detectCurseManifest) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.detectCurseManifest = detectCurseManifestButton.isChecked();
                            }
                            if (detectMultiMCManifestButton.isChecked() != CraftPresence.CONFIG.detectMultiMCManifest) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.detectMultiMCManifest = detectMultiMCManifestButton.isChecked();
                            }
                            if (detectMCUpdaterInstanceButton.isChecked() != CraftPresence.CONFIG.detectMCUpdaterInstance) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.detectMCUpdaterInstance = detectMCUpdaterInstanceButton.isChecked();
                            }
                            if (detectTechnicPackButton.isChecked() != CraftPresence.CONFIG.detectTechnicPack) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.detectTechnicPack = detectTechnicPackButton.isChecked();
                            }
                            if (showTimeButton.isChecked() != CraftPresence.CONFIG.showTime) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.showTime = showTimeButton.isChecked();
                            }
                            if (detectBiomeDataButton.isChecked() != CraftPresence.CONFIG.detectBiomeData) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.detectBiomeData = detectBiomeDataButton.isChecked();
                            }
                            if (detectDimensionDataButton.isChecked() != CraftPresence.CONFIG.detectDimensionData) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.detectDimensionData = detectDimensionDataButton.isChecked();
                            }
                            if (detectWorldDataButton.isChecked() != CraftPresence.CONFIG.detectWorldData) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.detectWorldData = detectWorldDataButton.isChecked();
                            }
                            if (enableJoinRequestButton.isChecked() != CraftPresence.CONFIG.enableJoinRequest) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.enableJoinRequest = enableJoinRequestButton.isChecked();
                            }
                            if (resetTimeOnInitButton.isChecked() != CraftPresence.CONFIG.resetTimeOnInit) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.resetTimeOnInit = resetTimeOnInitButton.isChecked();
                            }
                            CraftPresence.GUIS.openScreen(parentScreen);
                        },
                        () -> {
                            if (!proceedButton.enabled) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.message.hover.empty.default")
                                        ),
                                        getMouseX(), getMouseY(),
                                        width, height,
                                        getWrapWidth(),
                                        mc.fontRenderer,
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
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.general");
        final String clientIdText = ModUtils.TRANSLATOR.translate("gui.config.name.general.client_id");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringUtils.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);
        drawString(mc.fontRenderer, clientIdText, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);

        partyPrivacyLevelButton.displayString = ModUtils.TRANSLATOR.translate("gui.config.name.general.party_privacy") + " => " + PartyPrivacy.from(currentPartyPrivacy).getDisplayName();
        proceedButton.enabled = !StringUtils.isNullOrEmpty(clientId.getText()) && clientId.getText().length() == 18 && StringUtils.getValidLong(clientId.getText()).getFirst();
    }

    @Override
    public void postRender() {
        final String clientIdText = ModUtils.TRANSLATOR.translate("gui.config.name.general.client_id");
        // Hovering over Client ID Label
        if (CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), (width / 2f) - 130, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(clientIdText), mc.fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.general.client_id")), getMouseX(), getMouseY(), width, height, getWrapWidth(), mc.fontRenderer, true);
        }
    }
}
