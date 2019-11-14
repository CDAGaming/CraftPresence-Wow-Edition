package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class MessageGui extends GuiScreen {
    private final GuiScreen parentScreen;
    private ExtendedButtonControl backButton;

    private List<String> messageData;

    MessageGui(GuiScreen parentScreen, List<String> messageData) {
        mc = CraftPresence.instance;
        this.parentScreen = parentScreen;
        this.messageData = messageData;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        backButton = new ExtendedButtonControl(700, (width / 2) - 90, (height - 30), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"));

        buttonList.add(backButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.message");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);
        if (messageData != null && !messageData.isEmpty()) {
            for (int i = 0; i < messageData.size(); i++) {
                final String string = messageData.get(i);
                drawString(mc.fontRenderer, string, (width / 2) - (StringUtils.getStringWidth(string) / 2), (height / 3) + (i * 10), 0xFFFFFF);
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
