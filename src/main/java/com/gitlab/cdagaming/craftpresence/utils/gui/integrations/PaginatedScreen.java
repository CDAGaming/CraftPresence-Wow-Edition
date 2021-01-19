/*
 * MIT License
 *
 * Copyright (c) 2018 - 2021 CDAGaming (cstack2011@yahoo.com)
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

package com.gitlab.cdagaming.craftpresence.utils.gui.integrations;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedTextControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ScrollableListControl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public class PaginatedScreen extends ExtendedScreen {
    private final Map<Integer, List<Gui>> paginatedControls = Maps.newHashMap();
    private final Map<Integer, List<ScrollableListControl>> paginatedLists = Maps.newHashMap();
    protected int startPage = 1, currentPage = startPage, maxPages = startPage;
    protected ExtendedButtonControl nextPageButton, previousPageButton, backButton;
    private Runnable onPageChange;

    public PaginatedScreen(GuiScreen parentScreen) {
        super(parentScreen);
    }

    public PaginatedScreen(GuiScreen parentScreen, boolean debugMode) {
        super(parentScreen, debugMode);
    }

    public PaginatedScreen(GuiScreen parentScreen, boolean debugMode, boolean verboseMode) {
        super(parentScreen, debugMode, verboseMode);
    }

    /**
     * Initializes this Screen
     * <p>
     * Responsible for setting initial Data and creating controls
     */
    @Override
    public void initializeUi() {
        backButton = addControl(
                new ExtendedButtonControl(
                        (width / 2) - 90, (height - 30),
                        180, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.back"),
                        () -> CraftPresence.GUIS.openScreen(parentScreen)
                )
        );
        previousPageButton = addControl(
                new ExtendedButtonControl(
                        backButton.getControlPosX() - 23, (height - 30),
                        20, 20,
                        "<",
                        () -> {
                            if (currentPage > startPage) {
                                currentPage--;
                                if (onPageChange != null) {
                                    onPageChange.run();
                                }
                            }
                        }
                )
        );
        nextPageButton = addControl(
                new ExtendedButtonControl(
                        (backButton.getControlPosX() + backButton.getControlWidth()) + 3, (height - 30),
                        20, 20,
                        ">",
                        () -> {
                            if (currentPage < maxPages) {
                                currentPage++;
                                if (onPageChange != null) {
                                    onPageChange.run();
                                }
                            }
                        }
                )
        );

        previousPageButton.setControlEnabled(currentPage > startPage);
        nextPageButton.setControlEnabled(currentPage < maxPages);
        super.initializeUi();
    }

    /**
     * Adds a Compatible Control to this Screen with specified type
     *
     * @param buttonIn The Control to add to this Screen
     * @param <T>      The Control's Class Type
     * @return The added control with attached class type
     */
    @Nonnull
    protected <T extends Gui> T addControl(@Nonnull T buttonIn, final int renderTarget) {
        if (!paginatedControls.containsKey(renderTarget)) {
            paginatedControls.put(renderTarget, Lists.newArrayList(buttonIn));
            if (renderTarget > maxPages) {
                maxPages = renderTarget;
            }
        } else {
            paginatedControls.get(renderTarget).add(buttonIn);
        }
        return super.addControl(buttonIn);
    }

    /**
     * Adds a Compatible Scroll List to this Screen with specified type
     *
     * @param buttonIn The Scroll List to add to this Screen
     * @param <T>      The Scroll List's Class Type
     * @return The added scroll list with attached class type
     */
    @Nonnull
    protected <T extends ScrollableListControl> T addList(@Nonnull T buttonIn, final int renderTarget) {
        if (!paginatedLists.containsKey(renderTarget)) {
            paginatedLists.put(renderTarget, Lists.newArrayList(buttonIn));
            if (renderTarget > maxPages) {
                maxPages = renderTarget;
            }
        } else {
            paginatedLists.get(renderTarget).add(buttonIn);
        }
        return super.addList(buttonIn);
    }

    /**
     * Preliminary Render Event, executes after renderCriticalData and before postRender
     * <p>
     * Primarily used for rendering title data and preliminary elements
     */
    @Override
    public void preRender() {
        final List<Gui> defaultButtons = Lists.newArrayList(previousPageButton, nextPageButton, backButton);
        final List<Gui> elementsToRender = paginatedControls.getOrDefault(currentPage, defaultButtons);
        final List<ScrollableListControl> listsToRender = paginatedLists.getOrDefault(currentPage, Lists.newArrayList());

        for (Gui extendedControl : extendedControls) {
            // Toggle visibility/disable element is not on page
            if (extendedControl instanceof ExtendedButtonControl) {
                ((ExtendedButtonControl) extendedControl).setControlVisible(elementsToRender.contains(extendedControl) || defaultButtons.contains(extendedControl));
                ((ExtendedButtonControl) extendedControl).setControlEnabled(elementsToRender.contains(extendedControl) || defaultButtons.contains(extendedControl));
            }
            if (extendedControl instanceof ExtendedTextControl) {
                ((ExtendedTextControl) extendedControl).setVisible(elementsToRender.contains(extendedControl) || defaultButtons.contains(extendedControl));
                ((ExtendedTextControl) extendedControl).setEnabled(elementsToRender.contains(extendedControl) || defaultButtons.contains(extendedControl));
            }
        }
        for (ScrollableListControl listControl : extendedLists) {
            listControl.setEnabled(listsToRender.contains(listControl));
        }

        previousPageButton.setControlEnabled(currentPage > startPage);
        nextPageButton.setControlEnabled(currentPage < maxPages);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_UP && currentPage > startPage) {
            currentPage--;
        }

        if (keyCode == Keyboard.KEY_DOWN && currentPage < maxPages) {
            currentPage++;
        }

        super.keyTyped(typedChar, keyCode);
    }

    /**
     * Renders a String in the Screen, in the style of normal text
     *
     * @param text  The text to render to the screen
     * @param xPos  The X position to render the text at
     * @param yPos  The Y position to render the text at
     * @param color The color to render the text in
     */
    public void renderString(String text, float xPos, float yPos, int color, int renderTarget) {
        if (renderTarget == currentPage) {
            getFontRenderer().drawStringWithShadow(text, xPos, yPos, color);
        }
    }

    /**
     * Set the Event to trigger upon page change
     *
     * @param onPageChange The new event to be triggered
     */
    public void setOnPageChange(Runnable onPageChange) {
        this.onPageChange = onPageChange;
    }
}
