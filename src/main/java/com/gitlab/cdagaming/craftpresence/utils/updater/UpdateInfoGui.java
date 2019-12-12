package com.gitlab.cdagaming.craftpresence.utils.updater;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.net.URI;
import java.util.List;

public class UpdateInfoGui extends GuiScreen {
    private final GuiScreen parentScreen;
    private final ModUpdaterUtils modUpdater;
    private ExtendedButtonControl downloadButton, checkButton, backButton;

    public UpdateInfoGui(GuiScreen parentScreen, ModUpdaterUtils modUpdater) {
        mc = CraftPresence.instance;
        this.parentScreen = parentScreen;
        this.modUpdater = modUpdater;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        checkButton = new ExtendedButtonControl(600, (width / 2) - 90, (height - 30), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.checkForUpdates"));
        backButton = new ExtendedButtonControl(700, 10, (height - 30), 95, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"));
        downloadButton = new ExtendedButtonControl(810, (width - 105), (height - 30), 95, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.download"));

        buttonList.add(checkButton);
        buttonList.add(backButton);
        buttonList.add(downloadButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.changes");
        final List<String> notice = StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.changelog", modUpdater.targetVersion, modUpdater.targetChangelogData));

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);

        CraftPresence.GUIS.drawMultiLineString(notice, 25, 45, width, height, -1, mc.fontRenderer, false);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == checkButton.id) {
            modUpdater.checkForUpdates();
        } else if (button.id == backButton.id) {
            CraftPresence.GUIS.openScreen(parentScreen);
        } else if (button.id == downloadButton.id) {
            try {
                Desktop.getDesktop().browse(new URI(modUpdater.downloadUrl));
            } catch (Exception ex) {
                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.web", modUpdater.downloadUrl));
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            CraftPresence.GUIS.openScreen(parentScreen);
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
