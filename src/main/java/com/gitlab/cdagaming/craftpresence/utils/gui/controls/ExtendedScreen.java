package com.gitlab.cdagaming.craftpresence.utils.gui.controls;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

public class ExtendedScreen extends GuiScreen {
    public final GuiScreen parentScreen, currentScreen;
    private final List<Gui> extendedControls = Lists.newArrayList();
    private final List<ScrollableListControl> extendedLists = Lists.newArrayList();
    // Variable Needed to ensure all buttons are initialized before rendering to prevent an NPE
    private boolean initialized = false;

    private int lastMouseX = 0, lastMouseY = 0;

    public ExtendedScreen(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        currentScreen = this;
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        super.initGui();
        initialized = true;
    }

    @Nonnull
    @Override
    protected <T extends GuiButton> T addButton(@Nonnull T buttonIn) {
        return addControl(buttonIn);
    }

    @Nonnull
    protected <T extends Gui> T addControl(@Nonnull T buttonIn) {
        if (buttonIn instanceof GuiButton && !buttonList.contains(buttonIn)) {
            buttonList.add((GuiButton) buttonIn);
        }
        if (!extendedControls.contains(buttonIn)) {
            extendedControls.add(buttonIn);
        }

        return buttonIn;
    }

    @Nonnull
    protected <T extends ScrollableListControl> T addList(@Nonnull T buttonIn) {
        if (!extendedLists.contains(buttonIn)) {
            extendedLists.add(buttonIn);
        }

        return buttonIn;
    }

    public void preDraw() {
        if (initialized) {
            CraftPresence.GUIS.drawBackground(width, height);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Ensures initialization events have run first, preventing an NPE
        if (initialized) {
            for (ScrollableListControl listControl : extendedLists) {
                listControl.drawScreen(mouseX, mouseY, partialTicks);
            }

            for (Gui extendedControl : extendedControls) {
                if (extendedControl instanceof ExtendedTextControl) {
                    final ExtendedTextControl textField = (ExtendedTextControl) extendedControl;
                    textField.drawTextBox();
                }
            }

            super.drawScreen(mouseX, mouseY, partialTicks);

            lastMouseX = mouseX;
            lastMouseY = mouseY;

            for (Gui extendedControl : extendedControls) {
                if (extendedControl instanceof ExtendedButtonControl) {
                    final ExtendedButtonControl extendedButton = (ExtendedButtonControl) extendedControl;
                    if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, extendedButton)) {
                        extendedButton.onHover();
                    }
                }
            }
        }
    }

    @Override
    protected void actionPerformed(@Nonnull GuiButton button) throws IOException {
        if (button instanceof ExtendedButtonControl) {
            ((ExtendedButtonControl) button).onClick();
        }
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            CraftPresence.GUIS.openScreen(parentScreen);
        }

        for (Gui extendedControl : extendedControls) {
            if (extendedControl instanceof ExtendedTextControl) {
                final ExtendedTextControl textField = (ExtendedTextControl) extendedControl;
                textField.textboxKeyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (Gui extendedControl : extendedControls) {
            if (extendedControl instanceof ExtendedTextControl) {
                final ExtendedTextControl textField = (ExtendedTextControl) extendedControl;
                textField.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        for (Gui extendedControl : extendedControls) {
            if (extendedControl instanceof ExtendedTextControl) {
                final ExtendedTextControl textField = (ExtendedTextControl) extendedControl;
                textField.updateCursorCounter();
            }
        }
    }

    @Override
    public void onGuiClosed() {
        CraftPresence.GUIS.resetIndex();
        Keyboard.enableRepeatEvents(false);
    }

    public void drawNotice(final List<String> notice) {
        drawNotice(notice, 2, 3);
    }

    public void drawNotice(final List<String> notice, int widthScale, int heightScale) {
        if (notice != null && !notice.isEmpty()) {
            for (int i = 0; i < notice.size(); i++) {
                final String string = notice.get(i);
                drawString(mc.fontRenderer, string, (width / widthScale) - (StringUtils.getStringWidth(string) / 2), (height / heightScale) + (i * 10), 0xFFFFFF);
            }
        }
    }

    /**
     * Get the Current Mouse's X Coordinate Position
     *
     * @return The Mouse's X Coordinate Position
     */
    public int getMouseX() {
        return lastMouseX;
    }

    /**
     * Get the Current Mouse's Y Coordinate Position
     *
     * @return The Mouse's Y Coordinate Position
     */
    public int getMouseY() {
        return lastMouseY;
    }
}
