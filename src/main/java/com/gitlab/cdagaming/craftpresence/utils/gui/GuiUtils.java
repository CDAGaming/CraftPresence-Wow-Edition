/*
 * MIT License
 *
 * Copyright (c) 2018 - 2019 CDAGaming (cstack2011@yahoo.com)
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
package com.gitlab.cdagaming.craftpresence.utils.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.config.gui.MainGui;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.FileUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.CheckBoxControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Gui Utilities used to Parse Gui Data and handle related RPC Events, and rendering tasks
 *
 * @author CDAGaming
 */
public class GuiUtils {
    /**
     * If the Config GUI should open
     */
    public boolean openConfigGUI = false;

    /**
     * If the Config GUI is currently open
     */
    public boolean configGUIOpened = false;

    /**
     * Whether this module is active and currently in use
     */
    public boolean isInUse = false;

    /**
     * If an Element is being focused on in a GUI or if a GUI is currently open
     * <p>Conditions depend on Game Version
     */
    public boolean isFocused = false;

    /**
     * Whether this module is allowed to start and enabled
     */
    public boolean enabled = false;

    /**
     * A List of the detected Gui Screen Names
     */
    public List<String> GUI_NAMES = Lists.newArrayList();

    /**
     * The name of the Current Gui the player is in
     */
    private String CURRENT_GUI_NAME;

    /**
     * The Class Type of the Current Gui the player is in
     */
    private Class<?> CURRENT_GUI_CLASS;

    /**
     * The Current Instance of the Gui the player is in
     */
    private GuiScreen CURRENT_SCREEN;

    /**
     * A List of the detected Gui Screen Classes
     */
    private List<Class<?>> GUI_CLASSES = Lists.newArrayList();

    /**
     * Draws a Textured Rectangle (Modal Version), following the defined arguments
     *
     * @param x      The Starting X Position of the Object
     * @param y      The Starting Y Position of the Object
     * @param u      The U Mapping Value
     * @param v      The V Mapping Value
     * @param width  The Width of the Object
     * @param height The Height of the Object
     * @param zLevel The Z Level Position of the Object
     */
    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, double zLevel) {
        final float uScale = 1f / 0x100;
        final float vScale = 1f / 0x100;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder wr = tessellator.getBuffer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        wr.pos(x, y + height, zLevel).tex(u * uScale, ((v + height) * vScale)).endVertex();
        wr.pos(x + width, y + height, zLevel).tex((u + width) * uScale, ((v + height) * vScale)).endVertex();
        wr.pos(x + width, y, zLevel).tex((u + width) * uScale, (v * vScale)).endVertex();
        wr.pos(x, y, zLevel).tex(u * uScale, (v * vScale)).endVertex();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        tessellator.draw();
    }

    /**
     * Determines if the Mouse is over an element, following the defined Arguments
     *
     * @param mouseX        The Mouse's Current X Position
     * @param mouseY        The Mouse's Current Y Position
     * @param elementX      The Object's starting X Position
     * @param elementY      The Object's starting Y Position
     * @param elementWidth  The total width of the object
     * @param elementHeight The total height of the object
     * @return {@code true} if the Mouse Position is within the bounds of the object, and thus is over it
     */
    public boolean isMouseOver(final double mouseX, final double mouseY, final double elementX, final double elementY, final double elementWidth, final double elementHeight) {
        return mouseX >= elementX && mouseX <= elementX + elementWidth && mouseY >= elementY && mouseY <= elementY + elementHeight;
    }

    /**
     * Determines if the Mouse is over an element, following the defined Arguments
     *
     * @param mouseX The Mouse's Current X Position
     * @param mouseY The Mouse's Current Y Position
     * @param button The Object to check bounds and position
     * @return {@code true} if the Mouse Position is within the bounds of the object, and thus is over it
     */
    public boolean isMouseOver(final double mouseX, final double mouseY, final ExtendedButtonControl button) {
        return isMouseOver(mouseX, mouseY, button.x, button.y, button.getWidth() - 1, button.getHeight() - 1);
    }

    /**
     * Determines if the Mouse is over an element, following the defined Arguments
     *
     * @param mouseX   The Mouse's Current X Position
     * @param mouseY   The Mouse's Current Y Position
     * @param checkBox The Object to check bounds and position
     * @return {@code true} if the Mouse Position is within the bounds of the object, and thus is over it
     */
    public boolean isMouseOver(final double mouseX, final double mouseY, final CheckBoxControl checkBox) {
        return isMouseOver(mouseX, mouseY, checkBox.x, checkBox.y, checkBox.boxWidth - 1, checkBox.getHeight() - 1);
    }

    /**
     * Clears FULL Data from this Module
     */
    private void emptyData() {
        GUI_NAMES.clear();
        GUI_CLASSES.clear();
        clearClientData();
    }

    /**
     * Clears Runtime Client Data from this Module (PARTIAL Clear)
     */
    public void clearClientData() {
        CURRENT_GUI_NAME = null;
        CURRENT_SCREEN = null;
        CURRENT_GUI_CLASS = null;

        isInUse = false;
        CraftPresence.CLIENT.initArgumentData("&GUI&");
        CraftPresence.CLIENT.initIconData("&GUI&");
    }

    /**
     * Module Event to Occur on each tick within the Application
     */
    public void onTick() {
        enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.CONFIG.enablePERGUI : enabled;
        isInUse = enabled && (CraftPresence.instance.currentScreen != null || CURRENT_SCREEN != null);
        isFocused = CraftPresence.instance.currentScreen != null && CraftPresence.instance.currentScreen.isFocused();
        final boolean needsUpdate = enabled && (GUI_NAMES.isEmpty() || GUI_CLASSES.isEmpty());

        if (needsUpdate) {
            getGUIs();
        }

        if (isInUse) {
            updateGUIData();
            if (enabled && CraftPresence.instance.currentScreen == null) {
                clearClientData();
            } else if (!enabled) {
                emptyData();
            }
        }

        if (openConfigGUI) {
            openScreen(new MainGui(CraftPresence.instance.currentScreen));
            openConfigGUI = false;
        }
    }

    /**
     * Adds a Scheduled/Queued Task to Display the Specified Gui Screen
     *
     * @param targetScreen The target Gui Screen to display
     */
    public void openScreen(final GuiScreen targetScreen) {
        CraftPresence.instance.addScheduledTask(() -> CraftPresence.instance.displayGuiScreen(targetScreen));
    }

    /**
     * Synchronizes Data related to this module, if needed
     */
    private void updateGUIData() {
        if (CraftPresence.instance.currentScreen == null) {
            clearClientData();
        } else {
            final GuiScreen newScreen = CraftPresence.instance.currentScreen;
            final Class<?> newScreenClass = newScreen.getClass();
            final String newScreenName = newScreenClass.getSimpleName();

            if (!newScreen.equals(CURRENT_SCREEN) || !newScreenClass.equals(CURRENT_GUI_CLASS) || !newScreenName.equals(CURRENT_GUI_NAME)) {
                CURRENT_SCREEN = newScreen;
                CURRENT_GUI_CLASS = newScreenClass;
                CURRENT_GUI_NAME = newScreenName;

                if (!GUI_NAMES.contains(newScreenName)) {
                    GUI_NAMES.add(newScreenName);
                }

                if (!GUI_CLASSES.contains(newScreenClass)) {
                    GUI_CLASSES.add(newScreenClass);
                }

                updateGUIPresence();
            }
        }
    }

    /**
     * Retrieves and Synchronizes detected Gui Screen Classes
     */
    public void getGUIs() {
        final Class<?>[] searchClasses = new Class[]{
                GuiScreen.class, GuiContainer.class
        };

        for (Class<?> classObj : FileUtils.getClassNamesMatchingSuperType(Arrays.asList(searchClasses), "net.minecraft", "com.gitlab.cdagaming.craftpresence")) {
            if (!GUI_NAMES.contains(classObj.getSimpleName())) {
                GUI_NAMES.add(classObj.getSimpleName());
            }
            if (!GUI_CLASSES.contains(classObj)) {
                GUI_CLASSES.add(classObj);
            }
        }

        for (String guiMessage : CraftPresence.CONFIG.guiMessages) {
            if (!StringUtils.isNullOrEmpty(guiMessage)) {
                final String[] part = guiMessage.split(CraftPresence.CONFIG.splitCharacter);
                if (!StringUtils.isNullOrEmpty(part[0]) && !GUI_NAMES.contains(part[0])) {
                    GUI_NAMES.add(part[0]);
                }
            }
        }
    }

    /**
     * Updates RPC Data related to this Module
     */
    public void updateGUIPresence() {
        // Form GUI Argument List
        List<Tuple<String, String>> guiArgs = Lists.newArrayList();

        guiArgs.add(new Tuple<>("&GUI&", CURRENT_GUI_NAME));
        guiArgs.add(new Tuple<>("&CLASS&", CURRENT_GUI_CLASS.getSimpleName()));
        guiArgs.add(new Tuple<>("&SCREEN&", CURRENT_SCREEN.toString()));

        // Add All Generalized Arguments, if any
        if (!CraftPresence.CLIENT.generalArgs.isEmpty()) {
            guiArgs.addAll(CraftPresence.CLIENT.generalArgs);
        }

        final String defaultGUIMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.guiMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String currentGUIMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.guiMessages, CURRENT_GUI_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultGUIMSG);

        final String CURRENT_GUI_MESSAGE = StringUtils.sequentialReplaceAnyCase(currentGUIMSG, guiArgs);

        CraftPresence.CLIENT.syncArgument("&GUI&", CURRENT_GUI_MESSAGE, false);
        CraftPresence.CLIENT.initIconData("&GUI&");
    }

    /**
     * Renders a Specified Multi-Line String, constrained by position and dimension arguments
     *
     * @param textToInput    The Specified Multi-Line String, split by lines into a list
     * @param posX           The starting X position to render the String
     * @param posY           The starting Y position to render the String
     * @param screenWidth    The maximum width to allow rendering to (Text will wrap if output is greater)
     * @param screenHeight   The maximum height to allow rendering to (Text will wrap if output is greater)
     * @param maxTextWidth   The maximum width the output can be before wrapping
     * @param font           The font renderer to use to render the String
     * @param withBackground Whether a background should display around and under the String, like a tooltip
     */
    public void drawMultiLineString(final List<String> textToInput, int posX, int posY, int screenWidth, int screenHeight, int maxTextWidth, FontRenderer font, boolean withBackground) {
        if (CraftPresence.CONFIG.renderTooltips && !ModUtils.forceBlockTooltipRendering && !textToInput.isEmpty() && font != null) {
            List<String> textLines = textToInput;
            int tooltipTextWidth = 0;

            for (String textLine : textLines) {
                int textLineWidth = StringUtils.getStringWidth(textLine);

                if (textLineWidth > tooltipTextWidth) {
                    tooltipTextWidth = textLineWidth;
                }
            }

            boolean needsWrap = false;

            int titleLinesCount = 1;
            int tooltipX = posX + 12;
            if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
                tooltipX = posX - 16 - tooltipTextWidth;
                if (tooltipX < 4) // if the tooltip doesn't fit on the screen
                {
                    if (posX > screenWidth / 2) {
                        tooltipTextWidth = posX - 12 - 8;
                    } else {
                        tooltipTextWidth = screenWidth - 16 - posX;
                    }
                    needsWrap = true;
                }
            }

            if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
                tooltipTextWidth = maxTextWidth;
                needsWrap = true;
            }

            if (needsWrap) {
                int wrappedTooltipWidth = 0;
                List<String> wrappedTextLines = Lists.newArrayList();
                for (int i = 0; i < textLines.size(); i++) {
                    String textLine = textLines.get(i);
                    List<String> wrappedLine = StringUtils.splitTextByNewLine(StringUtils.wrapFormattedStringToWidth(textLine, tooltipTextWidth));
                    if (i == 0) {
                        titleLinesCount = wrappedLine.size();
                    }

                    for (String line : wrappedLine) {
                        int lineWidth = StringUtils.getStringWidth(line);
                        if (lineWidth > wrappedTooltipWidth) {
                            wrappedTooltipWidth = lineWidth;
                        }
                        wrappedTextLines.add(line);
                    }
                }
                tooltipTextWidth = wrappedTooltipWidth;
                textLines = wrappedTextLines;

                if (posX > screenWidth / 2) {
                    tooltipX = posX - 16 - tooltipTextWidth;
                } else {
                    tooltipX = posX + 12;
                }
            }

            int tooltipY = posY - 12;
            int tooltipHeight = 8;

            if (textLines.size() > 1) {
                tooltipHeight += (textLines.size() - 1) * 10;
                if (textLines.size() > titleLinesCount) {
                    tooltipHeight += 2; // gap between title lines and next lines
                }
            }

            if (tooltipY < 4) {
                tooltipY = 4;
            } else if (tooltipY + tooltipHeight + 4 > screenHeight) {
                tooltipY = screenHeight - tooltipHeight - 4;
            }

            if (withBackground) {
                GL11.glDisable(GL11.GL_DEPTH_TEST);

                final int zLevel = 300;
                String backgroundColor, borderColor;
                ResourceLocation backGroundTexture, borderTexture;

                // Perform Checks for different Color Format Fixes
                // Fix 1 Example: ababab -> #ababab
                // Fix 2 Example: 0xFFFFFF -> -1 or 100010
                //
                // Also Ensure (if using MC Textures) that they annotate with nameHere:textureHere

                if (StringUtils.isValidColorCode(CraftPresence.CONFIG.tooltipBGColor)) {
                    if (CraftPresence.CONFIG.tooltipBGColor.length() == 6) {
                        backgroundColor = "#" + CraftPresence.CONFIG.tooltipBGColor;
                    } else if (CraftPresence.CONFIG.tooltipBGColor.startsWith("0x")) {
                        backgroundColor = Long.toString(Long.decode(CraftPresence.CONFIG.tooltipBGColor).intValue());
                    } else {
                        backgroundColor = CraftPresence.CONFIG.tooltipBGColor;
                    }

                    // Draw with Colors
                    drawGradientRect(zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
                    drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
                    drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
                    drawGradientRect(zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
                    drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
                } else {
                    if (CraftPresence.CONFIG.tooltipBGColor.contains(CraftPresence.CONFIG.splitCharacter)) {
                        backgroundColor = CraftPresence.CONFIG.tooltipBGColor.replace(CraftPresence.CONFIG.splitCharacter, ":");
                    } else if (CraftPresence.CONFIG.tooltipBGColor.contains(":") && !CraftPresence.CONFIG.tooltipBGColor.startsWith(":")) {
                        backgroundColor = CraftPresence.CONFIG.tooltipBGColor;
                    } else if (CraftPresence.CONFIG.tooltipBGColor.startsWith(":")) {
                        backgroundColor = CraftPresence.CONFIG.tooltipBGColor.substring(1);
                    } else {
                        backgroundColor = "minecraft:" + CraftPresence.CONFIG.tooltipBGColor;
                    }

                    if (backgroundColor.contains(":")) {
                        String[] splitInput = backgroundColor.split(":", 2);
                        backGroundTexture = new ResourceLocation(splitInput[0], splitInput[1]);
                    } else {
                        backGroundTexture = new ResourceLocation(backgroundColor);
                    }

                    drawTextureRect(300, tooltipX - 4, tooltipY - 4, tooltipTextWidth + 8, tooltipHeight + 8, 0, backGroundTexture);
                }

                if (StringUtils.isValidColorCode(CraftPresence.CONFIG.tooltipBorderColor)) {
                    if (CraftPresence.CONFIG.tooltipBorderColor.length() == 6) {
                        borderColor = "#" + CraftPresence.CONFIG.tooltipBorderColor;
                    } else if (CraftPresence.CONFIG.tooltipBorderColor.startsWith("0x")) {
                        borderColor = Long.toString(Long.decode(CraftPresence.CONFIG.tooltipBorderColor).intValue());
                    } else {
                        borderColor = CraftPresence.CONFIG.tooltipBorderColor;
                    }

                    // Draw with Colors
                    int borderColorCode = (borderColor.startsWith("#") ? StringUtils.getColorFromHex(borderColor).getRGB() : Integer.parseInt(borderColor));
                    String borderColorEnd = Integer.toString((borderColorCode & 0xFEFEFE) >> 1 | borderColorCode & 0xFF000000);

                    drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColor, borderColorEnd);
                    drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColor, borderColorEnd);
                    drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColor, borderColor);
                    drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);
                } else {
                    if (CraftPresence.CONFIG.tooltipBorderColor.contains(CraftPresence.CONFIG.splitCharacter)) {
                        borderColor = CraftPresence.CONFIG.tooltipBorderColor.replace(CraftPresence.CONFIG.splitCharacter, ":");
                    } else if (CraftPresence.CONFIG.tooltipBorderColor.contains(":") && !CraftPresence.CONFIG.tooltipBorderColor.startsWith(":")) {
                        borderColor = CraftPresence.CONFIG.tooltipBorderColor;
                    } else if (CraftPresence.CONFIG.tooltipBorderColor.startsWith(":")) {
                        borderColor = CraftPresence.CONFIG.tooltipBorderColor.substring(1);
                    } else {
                        borderColor = "minecraft:" + CraftPresence.CONFIG.tooltipBorderColor;
                    }

                    if (borderColor.contains(":")) {
                        String[] splitInput = borderColor.split(":", 2);
                        borderTexture = new ResourceLocation(splitInput[0], splitInput[1]);
                    } else {
                        borderTexture = new ResourceLocation(borderColor);
                    }

                    drawTextureRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipTextWidth + 5, 1, 0, borderTexture); // Top Border
                    drawTextureRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipTextWidth + 5, 1, 0, borderTexture); // Bottom Border
                    drawTextureRect(zLevel, tooltipX - 3, tooltipY - 3, 1, tooltipHeight + 5, 0, borderTexture); // Left Border
                    drawTextureRect(zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3, 1, tooltipHeight + 6, 0, borderTexture); // Right Border
                }
            }

            for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber) {
                String line = textLines.get(lineNumber);
                font.drawStringWithShadow(line, tooltipX, tooltipY, -1);

                if (lineNumber + 1 == titleLinesCount) {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            if (withBackground) {
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
    }

    /**
     * Draws a Background onto a Gui, supporting RGBA Codes, Game Textures and Hexadecimal Colors
     *
     * @param width  The width to render the background to
     * @param height The height to render the background to
     */
    public void drawBackground(final double width, final double height) {
        if (CraftPresence.instance.world != null) {
            drawGradientRect(300, 0, 0, width, height, "-1072689136", "-804253680");
        } else {
            String bgCode = CraftPresence.CONFIG.guiBGColor;
            ResourceLocation loc;

            if (StringUtils.isValidColorCode(bgCode)) {
                drawGradientRect(300, 0, 0, width, height, bgCode, bgCode);
            } else if (!StringUtils.isNullOrEmpty(bgCode)) {
                if (bgCode.contains(CraftPresence.CONFIG.splitCharacter)) {
                    bgCode = bgCode.replace(CraftPresence.CONFIG.splitCharacter, ":");
                }

                if (bgCode.contains(":")) {
                    String[] splitInput = bgCode.split(":", 2);
                    loc = new ResourceLocation(splitInput[0], splitInput[1]);
                } else {
                    loc = new ResourceLocation(bgCode);
                }

                drawTextureRect(0.0D, 0.0D, 0.0D, width, height, 0, loc);
            }
        }
    }

    /**
     * Renders a Slider Object from the defined arguments
     *
     * @param x           The Starting X Position to render the slider
     * @param y           The Starting Y Position to render the slider
     * @param u           The U Mapping Value
     * @param v           The V Mapping Value
     * @param width       The full width for the slider to render to
     * @param height      The full height for the slider to render to
     * @param zLevel      The Z level position for the slider to render at
     * @param texLocation The game texture to render the slider as
     */
    public void renderSlider(int x, int y, int u, int v, int width, int height, double zLevel, ResourceLocation texLocation) {
        if (texLocation != null) {
            CraftPresence.instance.getTextureManager().bindTexture(texLocation);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        drawTexturedModalRect(x, y, u, v, width, height, zLevel);
        drawTexturedModalRect(x + 4, y, u + 196, v, width, height, zLevel);
    }

    /**
     * Draws a Textured Rectangle, following the defined arguments
     *
     * @param zLevel      The Z Level Position of the Object
     * @param xPos        The Starting X Position of the Object
     * @param yPos        The Starting Y Position of the Object
     * @param width       The Width of the Object
     * @param height      The Height of the Object
     * @param tint        The Tinting Level of the Object
     * @param texLocation The game texture to render the object as
     */
    public void drawTextureRect(double zLevel, double xPos, double yPos, double width, double height, double tint, ResourceLocation texLocation) {
        if (texLocation != null) {
            CraftPresence.instance.getTextureManager().bindTexture(texLocation);
        }

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        final float divider = 32.0F;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(xPos, yPos + height, zLevel).tex(0.0D, (height / divider + tint)).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(xPos + width, yPos + height, zLevel).tex((width / divider), (height / divider + tint)).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(xPos + width, yPos, zLevel).tex((width / divider), tint).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(xPos, yPos, zLevel).tex(0.0D, tint).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
    }

    /**
     * Draws a Gradient Rectangle, following the defined arguments
     *
     * @param zLevel         The Z Level Position of the Object
     * @param left           The Left side length of the Object
     * @param top            The top length of the Object
     * @param right          The Right side length of the Object
     * @param bottom         The bottom length of the Object
     * @param startColorCode The Starting Hexadecimal or RGBA Color Code
     * @param endColorCode   The ending Hexadecimal or RGBA Color Code
     */
    public void drawGradientRect(float zLevel, double left, double top, double right, double bottom, String startColorCode, String endColorCode) {
        Color startColorObj = null, endColorObj = null;
        int startColor = 0xFFFFFF, endColor = 0xFFFFFF;
        float startAlpha, startRed, startGreen, startBlue,
                endAlpha, endRed, endGreen, endBlue;

        if (!StringUtils.isNullOrEmpty(startColorCode)) {
            if (startColorCode.startsWith("#")) {
                startColorObj = StringUtils.getColorFromHex(startColorCode);
                endColorObj = (!StringUtils.isNullOrEmpty(endColorCode) && endColorCode.startsWith("#")) ? StringUtils.getColorFromHex(endColorCode) : startColorObj;
            } else {
                // Determine if Start Color Code is a Valid Number
                Tuple<Boolean, Integer> startColorData = StringUtils.getValidInteger(startColorCode),
                        endColorData = StringUtils.getValidInteger(endColorCode);

                // Check and ensure that at least one of the Color Codes are correct
                if (startColorData.getFirst() || endColorData.getFirst()) {
                    startColor = startColorData.getFirst() ? startColorData.getSecond() : endColor;
                    endColor = endColorData.getFirst() ? endColorData.getSecond() : startColor;
                }
            }
        }

        int startColorInstance = startColorObj != null ? startColorObj.getRGB() : startColor;
        int endColorInstance = endColorObj != null ? endColorObj.getRGB() : endColor;

        startAlpha = (startColorInstance >> 24 & 255) / 255.0F;
        startRed = (startColorInstance >> 16 & 255) / 255.0F;
        startGreen = (startColorInstance >> 8 & 255) / 255.0F;
        startBlue = (startColorInstance & 255) / 255.0F;

        endAlpha = (endColorInstance >> 24 & 255) / 255.0F;
        endRed = (endColorInstance >> 16 & 255) / 255.0F;
        endGreen = (endColorInstance >> 8 & 255) / 255.0F;
        endBlue = (endColorInstance & 255) / 255.0F;

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(right, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        buffer.pos(left, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        buffer.pos(left, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).endVertex();
        buffer.pos(right, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).endVertex();
        tessellator.draw();

        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Draws a Continuously Textured Box, following the defined arguments
     *
     * @param positionData       The Starting X and Y Positions to place the Object
     * @param uVLevels           The U and V Value Mappings for the Object
     * @param screenDimensions   The Maximum length and height to render the object as
     * @param textureDimensions  The Width and Height for the Object's Texture
     * @param verticalBorderData The Top and Bottom Border Lengths for the Object
     * @param sideBorderData     The Left and Right Side Lengths for the Object
     * @param zLevel             The Z Level position of the Object
     * @param res                The game texture to render the object as
     */
    public void drawContinuousTexturedBox(Tuple<Integer, Integer> positionData, Tuple<Integer, Integer> uVLevels, Tuple<Integer, Integer> screenDimensions, Tuple<Integer, Integer> textureDimensions,
                                          Tuple<Integer, Integer> verticalBorderData, Tuple<Integer, Integer> sideBorderData, double zLevel, ResourceLocation res) {
        if (res != null) {
            CraftPresence.instance.getTextureManager().bindTexture(res);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int x = positionData.getFirst();
        int y = positionData.getSecond();

        int u = uVLevels.getFirst();
        int v = uVLevels.getSecond();

        int width = screenDimensions.getFirst();
        int height = screenDimensions.getSecond();

        int textureWidth = textureDimensions.getFirst();
        int textureHeight = textureDimensions.getSecond();

        int topBorder = verticalBorderData.getFirst();
        int bottomBorder = verticalBorderData.getSecond();
        int leftBorder = sideBorderData.getFirst();
        int rightBorder = sideBorderData.getSecond();

        int fillerWidth = textureWidth - leftBorder - rightBorder;
        int fillerHeight = textureHeight - topBorder - bottomBorder;
        int canvasWidth = width - leftBorder - rightBorder;
        int canvasHeight = height - topBorder - bottomBorder;
        int xPasses = canvasWidth / fillerWidth;
        int remainderWidth = canvasWidth % fillerWidth;
        int yPasses = canvasHeight / fillerHeight;
        int remainderHeight = canvasHeight % fillerHeight;

        // Draw Borders
        // Top Left
        drawTexturedModalRect(x, y, u, v, leftBorder, topBorder, zLevel);
        // Top Right
        drawTexturedModalRect(x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder, zLevel);
        // Bottom Left
        drawTexturedModalRect(x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder, bottomBorder, zLevel);
        // Bottom Right
        drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, rightBorder, bottomBorder, zLevel);

        for (int i = 0; i < xPasses + (remainderWidth > 0 ? 1 : 0); i++) {
            // Top Border
            drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y, u + leftBorder, v, (i == xPasses ? remainderWidth : fillerWidth), topBorder, zLevel);
            // Bottom Border
            drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, (i == xPasses ? remainderWidth : fillerWidth), bottomBorder, zLevel);

            // Throw in some filler for good measure
            for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
                drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y + topBorder + (j * fillerHeight), u + leftBorder, v + topBorder, (i == xPasses ? remainderWidth : fillerWidth), (j == yPasses ? remainderHeight : fillerHeight), zLevel);
            }
        }

        // Side Borders
        for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
            // Left Border
            drawTexturedModalRect(x, y + topBorder + (j * fillerHeight), u, v + topBorder, leftBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);
            // Right Border
            drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + (j * fillerHeight), u + leftBorder + fillerWidth, v + topBorder, rightBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);
        }
    }

    /**
     * Calculate the Y Value for Buttons in a Standard-Sized Gui
     *
     * @param order Current Order of buttons above it, or 1 if none
     * @return The Calculated Y Value to place the Button at
     */
    public int getButtonY(int order) {
        return (40 + (25 * (order - 1)));
    }
}
