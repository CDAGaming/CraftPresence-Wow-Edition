package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAssetUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.CheckBoxControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class GeneralSettingsGui extends ExtendedScreen {
    private ExtendedButtonControl proceedButton;
    private CheckBoxControl detectCurseManifestButton, detectMultiMCManifestButton,
            detectMCUpdaterInstanceButton, detectTechnicPackButton, showTimeButton,
            showBiomeButton, showDimensionButton, showStateButton, enableJoinRequestButton;
    private ExtendedTextControl clientID;

    GeneralSettingsGui(GuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        clientID = addControl(
                new ExtendedTextControl(
                        mc.fontRenderer,
                        (width / 2) + 3, CraftPresence.GUIS.getButtonY(1),
                        180, 20
                )
        );
        clientID.setText(CraftPresence.CONFIG.clientID);
        clientID.setMaxStringLength(18);

        final int calc1 = (width / 2) - 145;
        final int calc2 = (width / 2) + 18;

        // Adding Default Icon Button
        addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, CraftPresence.GUIS.getButtonY(2),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.defaulticon"),
                        () -> CraftPresence.GUIS.openScreen(new SelectorGui(currentScreen, CraftPresence.CONFIG.NAME_defaultIcon, ModUtils.TRANSLATOR.translate("gui.config.title.selector.icon"), DiscordAssetUtils.ICON_LIST, CraftPresence.CONFIG.defaultIcon, null, true)),
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.defaulticon")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );
        detectCurseManifestButton = addControl(
                new CheckBoxControl(
                        calc1, CraftPresence.GUIS.getButtonY(3),
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.detectcursemanifest"),
                        CraftPresence.CONFIG.detectCurseManifest,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.detectcursemanifest")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );
        detectMultiMCManifestButton = addControl(
                new CheckBoxControl(
                        calc2, CraftPresence.GUIS.getButtonY(3),
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.detectmultimcmanifest"),
                        CraftPresence.CONFIG.detectMultiMCManifest,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.detectmultimcmanifest")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );
        detectMCUpdaterInstanceButton = addControl(
                new CheckBoxControl(
                        calc1, CraftPresence.GUIS.getButtonY(4) - 10,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.detectmcupdaterinstance"),
                        CraftPresence.CONFIG.detectMCUpdaterInstance,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.detectmcupdaterinstance")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );
        detectTechnicPackButton = addControl(
                new CheckBoxControl(
                        calc2, CraftPresence.GUIS.getButtonY(4) - 10,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.detecttechnicpack"),
                        CraftPresence.CONFIG.detectTechnicPack,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.detecttechnicpack")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );
        showTimeButton = addControl(
                new CheckBoxControl(
                        calc1, CraftPresence.GUIS.getButtonY(5) - 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.showtime"),
                        CraftPresence.CONFIG.showTime,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.showtime")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );
        showBiomeButton = addControl(
                new CheckBoxControl(
                        calc2, CraftPresence.GUIS.getButtonY(5) - 20,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.showbiome"),
                        CraftPresence.CONFIG.showCurrentBiome,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.showbiome")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );
        showDimensionButton = addControl(
                new CheckBoxControl(
                        calc1, CraftPresence.GUIS.getButtonY(6) - 30,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.showdimension"),
                        CraftPresence.CONFIG.showCurrentDimension,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.showdimension")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );
        showStateButton = addControl(
                new CheckBoxControl(
                        calc2, CraftPresence.GUIS.getButtonY(6) - 30,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.showstate"),
                        CraftPresence.CONFIG.showGameState,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.showstate")
                                ),
                                getMouseX(), getMouseY(),
                                width, height,
                                -1,
                                mc.fontRenderer,
                                true
                        )
                )
        );
        enableJoinRequestButton = addControl(
                new CheckBoxControl(
                        calc1, CraftPresence.GUIS.getButtonY(7) - 40,
                        ModUtils.TRANSLATOR.translate("gui.config.name.general.enablejoinrequest"),
                        CraftPresence.CONFIG.enableJoinRequest,
                        null,
                        () -> CraftPresence.GUIS.drawMultiLineString(
                                StringUtils.splitTextByNewLine(
                                        ModUtils.TRANSLATOR.translate("gui.config.comment.general.enablejoinrequest")
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
                        ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"),
                        () -> {
                            if (!clientID.getText().equals(CraftPresence.CONFIG.clientID)) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.clientID = clientID.getText();
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
                            if (showBiomeButton.isChecked() != CraftPresence.CONFIG.showCurrentBiome) {
                                CraftPresence.CONFIG.hasChanged = true;
                                if (CraftPresence.BIOMES.BIOME_NAMES.isEmpty()) {
                                    CraftPresence.BIOMES.getBiomes();
                                    CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                }
                                CraftPresence.CONFIG.showCurrentBiome = showBiomeButton.isChecked();
                            }
                            if (showDimensionButton.isChecked() != CraftPresence.CONFIG.showCurrentDimension) {
                                CraftPresence.CONFIG.hasChanged = true;
                                if (CraftPresence.DIMENSIONS.DIMENSION_NAMES.isEmpty()) {
                                    CraftPresence.DIMENSIONS.getDimensions();
                                    CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                }
                                CraftPresence.CONFIG.showCurrentDimension = showDimensionButton.isChecked();
                            }
                            if (showStateButton.isChecked() != CraftPresence.CONFIG.showGameState) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.showGameState = showStateButton.isChecked();
                            }
                            if (enableJoinRequestButton.isChecked() != CraftPresence.CONFIG.enableJoinRequest) {
                                CraftPresence.CONFIG.hasChanged = true;
                                CraftPresence.CONFIG.hasClientPropertiesChanged = true;
                                CraftPresence.CONFIG.enableJoinRequest = enableJoinRequestButton.isChecked();
                            }
                            CraftPresence.GUIS.openScreen(parentScreen);
                        },
                        () -> {
                            if (!proceedButton.enabled) {
                                CraftPresence.GUIS.drawMultiLineString(
                                        StringUtils.splitTextByNewLine(
                                                ModUtils.TRANSLATOR.translate("gui.config.hoverMessage.defaultempty")
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

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        preDraw();

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.general");
        final String clientIDText = ModUtils.TRANSLATOR.translate("gui.config.name.general.clientid");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringUtils.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);
        drawString(mc.fontRenderer, clientIDText, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);

        proceedButton.enabled = !StringUtils.isNullOrEmpty(clientID.getText()) && clientID.getText().length() == 18 && StringUtils.getValidLong(clientID.getText()).getFirst();

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Client ID Label
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(clientIDText), mc.fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.comment.general.clientid")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
    }
}
