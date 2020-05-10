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

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.gitlab.cdagaming.craftpresence.utils.updater;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.UrlUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public class UpdateInfoGui extends ExtendedScreen {
    private final ModUpdaterUtils modUpdater;
    private ExtendedButtonControl downloadButton, checkButton;

    public UpdateInfoGui(GuiScreen parentScreen, ModUpdaterUtils modUpdater) {
        super(parentScreen);
        this.modUpdater = modUpdater;
    }

    @Override
    public void initGui() {
        checkButton = addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, (height - 30),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.checkForUpdates"),
                        modUpdater::checkForUpdates
                )
        );
        // Adding Back Button
        addControl(
                new ExtendedButtonControl(
                        10, (height - 30),
                        95, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"),
                        () -> CraftPresence.GUIS.openScreen(parentScreen)
                )
        );
        downloadButton = addControl(
                new ExtendedButtonControl(
                        (width - 105), (height - 30),
                        95, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.download"),
                        () -> {
                            try {
                                UrlUtils.openUrl(modUpdater.downloadUrl);
                            } catch (Exception ex) {
                                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.web", modUpdater.downloadUrl));
                                ex.printStackTrace();
                            }
                        }
                )
        );

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        preDraw();

        downloadButton.enabled = modUpdater.currentState == ModUpdaterUtils.UpdateState.OUTDATED ||
                modUpdater.currentState == ModUpdaterUtils.UpdateState.BETA_OUTDATED;

        checkButton.enabled = modUpdater.currentState != ModUpdaterUtils.UpdateState.PENDING;

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.title.changes", modUpdater.currentState.name());
        final List<String> notice = StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.changelog", modUpdater.targetVersion, modUpdater.targetChangelogData));

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(mc.fontRenderer, subTitle, (width / 2) - (StringUtils.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);

        CraftPresence.GUIS.drawMultiLineString(notice, 25, 45, width, height, -1, mc.fontRenderer, false);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}