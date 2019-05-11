package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUIExtendedButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ConfigGUI_NullEntry extends GuiScreen {
    private final GuiScreen parentScreen;
    private GUIExtendedButton backButton;

    ConfigGUI_NullEntry(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        backButton = new GUIExtendedButton(700, (width / 2) - 90, (height - 30), 180, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.back"));

        buttonList.add(backButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

        final String mainTitle = Constants.TRANSLATOR.translate("gui.config.title.error");
        final List<String> notice = StringHandler.splitTextByNewLine(Constants.TRANSLATOR.translate("gui.config.errorMessage.null"));

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringHandler.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);
        if (notice != null && !notice.isEmpty()) {
            for (int i = 0; i < notice.size(); i++) {
                final String string = notice.get(i);
                drawString(mc.fontRenderer, string, (width / 2) - (StringHandler.getStringWidth(string) / 2), (height / 3) + (i * 10), 0xFFFFFF);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == backButton.id) {
            mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
