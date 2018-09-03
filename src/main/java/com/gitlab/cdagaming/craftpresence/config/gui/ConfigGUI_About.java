package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.net.URI;

public class ConfigGUI_About extends GuiScreen {
    private static final String SOURCE_URL = "https://gitlab.com/CDAGaming/CraftPresence";
    private final GuiScreen parentscreen;
    private GuiButton viewSource, backButton;

    ConfigGUI_About(GuiScreen parentScreen) {
        mc = Minecraft.getMinecraft();
        parentscreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);
        backButton = new GuiButton(700, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 30), 180, 20, "Back");
        viewSource = new GuiButton(810, (sr.getScaledWidth() / 2) - 90, (sr.getScaledHeight() - 55), 180, 20, "View Source");

        buttonList.add(backButton);
        buttonList.add(viewSource);
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String title = "About this Configuration GUI";
        String[] notice = ("This Configuration GUI was made from scratch by\n" +
                "Jonathing, and will continue to be maintained by\n" +
                "CDAGaming. A lot of effort went into making this\n" +
                "custom GUI, so show him some support! Thanks.\n\n" +
                "Feel free to learn from this GUI's code on\n" +
                "the CraftPresence GitLab repository.").split("\n");
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();
        drawString(fontRenderer, title, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(title) / 2), 20, 0xFFFFFF);
        for (int i = 0; i < notice.length; i++) {
            String string = notice[i];
            drawString(fontRenderer, string, (sr.getScaledWidth() / 2) - (fontRenderer.getStringWidth(string) / 2), 80 + (i * 10), 0xFFFFFF);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        // Done button
        if (button.id == backButton.id) {
            mc.displayGuiScreen(parentscreen);
        }
        // View Source button
        if (button.id == viewSource.id) {
            try {
                Desktop.getDesktop().browse(new URI(SOURCE_URL));
            } catch (Exception ex) {
                Constants.LOG.error("Couldn't open link");
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
