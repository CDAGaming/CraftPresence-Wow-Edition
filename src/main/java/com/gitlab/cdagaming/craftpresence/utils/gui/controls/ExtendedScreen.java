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

package com.gitlab.cdagaming.craftpresence.utils.gui.controls;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.GuiUtils;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

/**
 * An Extended and Globalized Gui Screen
 *
 * @author CDAGaming
 */
public class ExtendedScreen extends GuiScreen {
    /**
     * The Parent or Past Screen
     */
    public final GuiScreen parentScreen;
    /**
     * The Current Screen Instance
     */
    public final GuiScreen currentScreen;
    /**
     * Similar to buttonList, a list of compatible controls in this Screen
     */
    private final List<Gui> extendedControls = Lists.newArrayList();
    /**
     * Similar to buttonList, a list of compatible ScrollLists in this Screen
     */
    private final List<ScrollableListControl> extendedLists = Lists.newArrayList();
    /**
     * Variable needed to ensure all buttons are initialized before rendering to prevent an NPE
     */
    private boolean initialized = false;
    /**
     * Whether to enable debug mode screen data, specified from screen developers
     */
    private boolean debugMode = false;
    /**
     * Whether to enable verbose mode screen data, specified from screen developers
     */
    private boolean verboseMode = false;

    /**
     * The Last Ticked Mouse X Coordinate
     */
    private int lastMouseX = 0;
    /**
     * The Last Ticked Mouse Y Coordinate
     */
    private int lastMouseY = 0;

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param parentScreen The Parent Screen for this Instance
     */
    public ExtendedScreen(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        currentScreen = this;
        this.parentScreen = parentScreen;
        setDebugMode(ModUtils.IS_DEV);
        setVerboseMode(ModUtils.IS_VERBOSE);
    }

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param parentScreen The Parent Screen for this Instance
     */
    public ExtendedScreen(GuiScreen parentScreen, boolean debugMode) {
        this(parentScreen);
        setDebugMode(debugMode);
    }

    /**
     * Initialization Event for this Control, assigning defined arguments
     *
     * @param parentScreen The Parent Screen for this Instance
     */
    public ExtendedScreen(GuiScreen parentScreen, boolean debugMode, boolean verboseMode) {
        this(parentScreen, debugMode);
        setVerboseMode(verboseMode);
    }

    /**
     * Pre-Initializes this Screen
     * <p>
     * Responsible for Setting preliminary data
     */
    @Override
    public void initGui() {
        // Clear Data before Initialization
        buttonList.clear();
        extendedControls.clear();
        extendedLists.clear();

        Keyboard.enableRepeatEvents(true);
        initializeUi();
        super.initGui();
        initialized = true;
    }

    /**
     * Initializes this Screen
     * <p>
     * Responsible for setting initial Data and creating controls
     */
    public void initializeUi() {
        // N/A
    }

    /**
     * Event to trigger upon Window Resize
     *
     * @param mcIn The Minecraft Instance
     * @param w    The New Screen Width
     * @param h    The New Screen Height
     */
    @Override
    public void onResize(@Nonnull Minecraft mcIn, int w, int h) {
        initialized = false;
        super.onResize(mcIn, w, h);
    }

    /**
     * Adds a Compatible Button to this Screen with specified type
     *
     * @param buttonIn The Button to add to this Screen
     * @param <T>      The Button's Class Type
     * @return The added button with attached class type
     */
    @Nonnull
    @Override
    protected <T extends GuiButton> T addButton(@Nonnull T buttonIn) {
        return addControl(buttonIn);
    }

    /**
     * Adds a Compatible Control to this Screen with specified type
     *
     * @param buttonIn The Control to add to this Screen
     * @param <T>      The Control's Class Type
     * @return The added control with attached class type
     */
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

    /**
     * Adds a Compatible Scroll List to this Screen with specified type
     *
     * @param buttonIn The Scroll List to add to this Screen
     * @param <T>      The Scroll List's Class Type
     * @return The added scroll list with attached class type
     */
    @Nonnull
    protected <T extends ScrollableListControl> T addList(@Nonnull T buttonIn) {
        if (!extendedLists.contains(buttonIn)) {
            extendedLists.add(buttonIn);
        }

        return buttonIn;
    }

    /**
     * Pre-Preliminary Render Event, executes before preRender
     * <p>
     * Primarily used for rendering critical elements before other elements
     */
    public void renderCriticalData() {
        CraftPresence.GUIS.drawBackground(width, height);
    }

    /**
     * Preliminary Render Event, executes after renderCriticalData and before postRender
     * <p>
     * Primarily used for rendering title data and preliminary elements
     */
    public void preRender() {
        // N/A
    }

    /**
     * Post-Render event, executes after super event and preRender
     * <p>
     * Primarily used for rendering hover data
     */
    public void postRender() {
        // N/A
    }

    /**
     * Renders this Screen, including controls and post-Hover Events
     *
     * @param mouseX       The Event Mouse X Coordinate
     * @param mouseY       The Event Mouse Y Coordinate
     * @param partialTicks The Rendering Tick Rate
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Ensures initialization events have run first, preventing an NPE
        if (initialized) {
            renderCriticalData();
            preRender();

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

            postRender();
        }
    }

    /**
     * Event to trigger upon Mouse Input
     *
     * @throws IOException if error occurs in event trigger
     */
    @Override
    public void handleMouseInput() throws IOException {
        for (ScrollableListControl listControl : extendedLists) {
            listControl.handleMouseInput();
        }
        super.handleMouseInput();
    }

    /**
     * Event to trigger upon Button Action, including onClick Events
     *
     * @param button The Button to trigger upon
     * @throws IOException if error occurs in event trigger
     */
    @Override
    protected void actionPerformed(@Nonnull GuiButton button) throws IOException {
        if (button instanceof ExtendedButtonControl) {
            ((ExtendedButtonControl) button).onClick();
        }
        super.actionPerformed(button);
    }

    /**
     * Event to trigger upon Typing a Key
     *
     * @param typedChar The typed Character, if any
     * @param keyCode   The KeyCode entered, if any
     */
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

    /**
     * Event to trigger upon the mouse being clicked
     *
     * @param mouseX      The Event Mouse X Coordinate
     * @param mouseY      The Event Mouse Y Coordinate
     * @param mouseButton The Event Mouse Button Clicked
     * @throws IOException if error occurs in event trigger
     */
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

    /**
     * Event to trigger on each tick
     */
    @Override
    public void updateScreen() {
        for (Gui extendedControl : extendedControls) {
            if (extendedControl instanceof ExtendedTextControl) {
                final ExtendedTextControl textField = (ExtendedTextControl) extendedControl;
                textField.updateCursorCounter();
            }
        }
    }

    /**
     * Event to trigger upon exiting the Gui
     */
    @Override
    public void onGuiClosed() {
        initialized = false;
        CraftPresence.GUIS.resetIndex();
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Renders a String in the Screen, in the style of a notice
     *
     * @param notice The List of Strings to render
     */
    public void renderNotice(final List<String> notice) {
        renderNotice(notice, 2, 3, false, false);
    }

    /**
     * Renders a String in the Screen, in the style of a notice
     *
     * @param notice      The List of Strings to render
     * @param widthScale  The Scale/Value away from the center X to render at
     * @param heightScale The Scale/Value away from the center Y to render at
     */
    public void renderNotice(final List<String> notice, float widthScale, float heightScale) {
        renderNotice(notice, widthScale, heightScale, false, false);
    }

    /**
     * Renders a String in the Screen, in the style of a notice
     *
     * @param notice       The List of Strings to render
     * @param widthScale   The Scale/Value away from the center X to render at
     * @param heightScale  The Scale/Value away from the center Y to render at
     * @param useXAsActual Whether or not to use the widthScale as the actual X value
     * @param useYAsActual Whether or not to use the heightScale as the actual Y value
     */
    public void renderNotice(final List<String> notice, final float widthScale, final float heightScale, final boolean useXAsActual, final boolean useYAsActual) {
        if (notice != null && !notice.isEmpty()) {
            for (int i = 0; i < notice.size(); i++) {
                final String string = notice.get(i);
                renderString(string, (useXAsActual ? widthScale : (width / widthScale)) - (StringUtils.getStringWidth(string) / widthScale), (useYAsActual ? heightScale : (height / heightScale)) + (i * 10), 0xFFFFFF);
            }
        }
    }

    /**
     * Renders a String in the Screen, in the style of normal text
     *
     * @param text  The text to render to the screen
     * @param xPos  The X position to render the text at
     * @param yPos  The Y position to render the text at
     * @param color The color to render the text in
     */
    public void renderString(String text, float xPos, float yPos, int color) {
        getFontRenderer().drawStringWithShadow(text, xPos, yPos, color);
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
     * Get the wrap width for elements to be wrapped by
     * <p>Mostly used as a helper method for wrapping String elements
     *
     * @return the wrap width for elements to be wrapped by
     */
    public int getWrapWidth() {
        return -1;
    }

    /**
     * Get the Current Mouse's Y Coordinate Position
     *
     * @return The Mouse's Y Coordinate Position
     */
    public int getMouseY() {
        return lastMouseY;
    }

    /**
     * Get the Current Font Renderer for this Screen
     *
     * @return The Current Font Renderer for this Screen
     */
    public FontRenderer getFontRenderer() {
        return mc.fontRenderer != null ? mc.fontRenderer : GuiUtils.getDefaultFontRenderer();
    }

    /**
     * Get the Current Font Height for this Screen
     *
     * @return The Current Font Height for this Screen
     */
    public int getFontHeight() {
        return getFontRenderer().FONT_HEIGHT;
    }

    /**
     * Gets whether to display any Debug display data for this screen
     * 
     * @return Whether to display any Debug display data for this screen
     */
    public boolean isDebugMode() {
        return debugMode;
    }

    /**
     * Sets whether to display any Debug display data for this screen
     * 
     * @param isDebugMode Whether to display any Debug display data for this screen
     */
    public void setDebugMode(boolean isDebugMode) {
        this.debugMode = isDebugMode;
    }

    /**
     * Gets whether to display any Verbose display data for this screen
     * 
     * @return Whether to display any Verbose display data for this screen
     */
    public boolean isVerboseMode() {
        return verboseMode;
    }

    /**
     * Sets whether to display any Verbose display data for this screen
     * 
     * @param isVerboseMode Whether to display any Verbose display data for this screen
     */
    public void setVerboseMode(boolean isVerboseMode) {
        this.verboseMode = isVerboseMode;
    }
}
