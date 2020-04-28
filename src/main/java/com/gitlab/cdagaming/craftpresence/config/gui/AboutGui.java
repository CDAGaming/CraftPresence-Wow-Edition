package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.UrlUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.gitlab.cdagaming.craftpresence.utils.updater.UpdateInfoGui;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public class AboutGui extends ExtendedScreen {
    private static final String SOURCE_URL = "https://gitlab.com/CDAGaming/CraftPresence";

    AboutGui(GuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initGui() {
        // Adding Version Check Button
        addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, (height - 30),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.versionInfo"),
                        () -> CraftPresence.GUIS.openScreen(new UpdateInfoGui(currentScreen, ModUtils.UPDATER))
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

        // Adding View Source Button
        addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, (height - 55),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.viewsource"),
                        () -> {
                            try {
                                UrlUtils.openUrl(SOURCE_URL);
                            } catch (Exception ex) {
                                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.web", SOURCE_URL));
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

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.about.config");
        final List<String> notice = StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.credits"));

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);
        drawNotice(notice);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
