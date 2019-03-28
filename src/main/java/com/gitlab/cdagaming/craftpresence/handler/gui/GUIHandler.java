package com.gitlab.cdagaming.craftpresence.handler.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.config.gui.ConfigGUI_Main;
import com.gitlab.cdagaming.craftpresence.handler.CommandHandler;
import com.gitlab.cdagaming.craftpresence.handler.FileHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUICheckBox;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUIExtendedButton;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("TryWithIdenticalCatches")
public class GUIHandler {
    public boolean openConfigGUI = false, configGUIOpened = false, isInUse = false, enabled = false;

    public List<String> GUI_NAMES = Lists.newArrayList();
    private String CURRENT_GUI_NAME;
    private Class CURRENT_GUI_CLASS;
    private GuiScreen CURRENT_SCREEN;
    private List<Class> GUI_CLASSES = Lists.newArrayList();

    private boolean queuedForUpdate = false;

    private static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float zLevel) {
        final float uScale = 1f / 0x100;
        final float vScale = 1f / 0x100;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder wr = tessellator.getBuffer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        wr.pos(x, y + height, zLevel).tex(u * uScale, ((v + height) * vScale)).endVertex();
        wr.pos(x + width, y + height, zLevel).tex((u + width) * uScale, ((v + height) * vScale)).endVertex();
        wr.pos(x + width, y, zLevel).tex((u + width) * uScale, (v * vScale)).endVertex();
        wr.pos(x, y, zLevel).tex(u * uScale, (v * vScale)).endVertex();
        tessellator.draw();
    }

    public boolean isMouseOver(final double mouseX, final double mouseY, final double elementX, final double elementY, final double elementWidth, final double elementHeight) {
        return mouseX >= elementX && mouseX <= elementX + elementWidth && mouseY >= elementY && mouseY <= elementY + elementHeight;
    }

    public boolean isMouseOver(final double mouseX, final double mouseY, final GUIExtendedButton button) {
        return isMouseOver(mouseX, mouseY, button.x, button.y, button.getWidth(), button.getHeight());
    }

    public boolean isMouseOver(final double mouseX, final double mouseY, final GUICheckBox checkBox) {
        return isMouseOver(mouseX, mouseY, checkBox.x, checkBox.y, checkBox.boxWidth, checkBox.getHeight());
    }

    public void emptyData() {
        GUI_NAMES.clear();
        GUI_CLASSES.clear();
        clearClientData();
    }

    public void clearClientData() {
        CURRENT_GUI_NAME = null;
        CURRENT_SCREEN = null;
        CURRENT_GUI_CLASS = null;

        queuedForUpdate = false;
        isInUse = false;
    }

    public void onTick() {
        enabled = !CraftPresence.CONFIG.hasChanged ? CraftPresence.CONFIG.enablePERGUI && !CraftPresence.CONFIG.showGameState : enabled;
        isInUse = enabled && (CraftPresence.instance.currentScreen != null || CURRENT_SCREEN != null);
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
            CraftPresence.instance.displayGuiScreen(new ConfigGUI_Main(CraftPresence.instance.currentScreen));
            openConfigGUI = false;
        }
    }

    private void updateGUIData() {
        if ((CURRENT_SCREEN != null && CraftPresence.instance.currentScreen == null) || (CURRENT_SCREEN == null && !StringHandler.isNullOrEmpty(CraftPresence.CLIENT.GAME_STATE)) || (isInUse && CommandHandler.isOnMainMenuPresence())) {
            clearClientData();
            CraftPresence.CLIENT.GAME_STATE = "";
            CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
        } else {
            if (CraftPresence.instance.currentScreen != null) {
                final Class newScreenClass = CraftPresence.instance.currentScreen.getClass();
                final String newScreenName = newScreenClass.getSimpleName();

                if (!CraftPresence.instance.currentScreen.equals(CURRENT_SCREEN) || !newScreenClass.equals(CURRENT_GUI_CLASS) || !newScreenName.equals(CURRENT_GUI_NAME)) {
                    CURRENT_SCREEN = CraftPresence.instance.currentScreen;
                    CURRENT_GUI_CLASS = newScreenClass;
                    CURRENT_GUI_NAME = newScreenName;
                    queuedForUpdate = true;
                }

                // TODO: VERIFY
                if (!GUI_NAMES.contains(newScreenName) || !GUI_CLASSES.contains(newScreenClass)) {
                    getGUIs();
                }
            }
        }

        if (queuedForUpdate) {
            updateGUIPresence();
        }
    }

    public void getGUIs() {
        final Class[] searchClasses = new Class[]{
                GuiScreen.class, GuiContainer.class
        };

        for (Class classObj : FileHandler.getClassNamesMatchingSuperType(Arrays.asList(searchClasses), "net.minecraft", "com.gitlab.cdagaming.craftpresence")) {
            if (!GUI_NAMES.contains(classObj.getSimpleName())) {
                GUI_NAMES.add(classObj.getSimpleName());
            }
            if (!GUI_CLASSES.contains(classObj)) {
                GUI_CLASSES.add(classObj);
            }
        }

        for (String guiMessage : CraftPresence.CONFIG.guiMessages) {
            if (!StringHandler.isNullOrEmpty(guiMessage)) {
                final String[] part = guiMessage.split(CraftPresence.CONFIG.splitCharacter);
                if (!StringHandler.isNullOrEmpty(part[0]) && !GUI_NAMES.contains(part[0])) {
                    GUI_NAMES.add(part[0]);
                }
            }
        }
    }

    public void updateGUIPresence() {
        final String defaultGUIMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.guiMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String currentGUIMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.guiMessages, CURRENT_GUI_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultGUIMSG);

        // NOTE: Overrides Biomes
        CraftPresence.CLIENT.GAME_STATE = currentGUIMSG.replace("&gui&", CURRENT_GUI_NAME).replace("&class&", CURRENT_GUI_CLASS.getSimpleName()).replace("&screen&", CURRENT_SCREEN.toString());
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
        queuedForUpdate = false;
    }

    public void drawMultiLineString(List<String> textLines, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, FontRenderer font, boolean withBackground) {
        if (!textLines.isEmpty() && font != null) {
            int tooltipTextWidth = 0;

            for (String textLine : textLines) {
                int textLineWidth = font.getStringWidth(textLine);

                if (textLineWidth > tooltipTextWidth) {
                    tooltipTextWidth = textLineWidth;
                }
            }

            boolean needsWrap = false;

            int titleLinesCount = 1;
            int tooltipX = mouseX + 12;
            if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
                tooltipX = mouseX - 16 - tooltipTextWidth;
                if (tooltipX < 4) // if the tooltip doesn't fit on the screen
                {
                    if (mouseX > screenWidth / 2) {
                        tooltipTextWidth = mouseX - 12 - 8;
                    } else {
                        tooltipTextWidth = screenWidth - 16 - mouseX;
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
                    List<String> wrappedLine = font.listFormattedStringToWidth(textLine, tooltipTextWidth);
                    if (i == 0) {
                        titleLinesCount = wrappedLine.size();
                    }

                    for (String line : wrappedLine) {
                        int lineWidth = font.getStringWidth(line);
                        if (lineWidth > wrappedTooltipWidth) {
                            wrappedTooltipWidth = lineWidth;
                        }
                        wrappedTextLines.add(line);
                    }
                }
                tooltipTextWidth = wrappedTooltipWidth;
                textLines = wrappedTextLines;

                if (mouseX > screenWidth / 2) {
                    tooltipX = mouseX - 16 - tooltipTextWidth;
                } else {
                    tooltipX = mouseX + 12;
                }
            }

            int tooltipY = mouseY - 12;
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
                RenderHelper.disableStandardItemLighting();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);

                final int zLevel = 300;
                int backgroundColor = 0xF0100010;
                int borderColorStart = 0x505000FF;
                int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;

                drawGradientRect(zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
                drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
                drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
                drawGradientRect(zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
                drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
                drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
                drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
                drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
                drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);
            }

            for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber) {
                String line = textLines.get(lineNumber);
                font.drawStringWithShadow(line, (float) tooltipX, (float) tooltipY, -1);

                if (lineNumber + 1 == titleLinesCount) {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            if (withBackground) {
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                RenderHelper.enableStandardItemLighting();
            }
        }
    }

    private void drawGradientRect(int zLevel, int left, int top, int right, int bottom, int startColor, int endColor) {
        float startAlpha = (startColor >> 24 & 255) / 255.0F;
        float startRed = (startColor >> 16 & 255) / 255.0F;
        float startGreen = (startColor >> 8 & 255) / 255.0F;
        float startBlue = (startColor & 255) / 255.0F;
        float endAlpha = (endColor >> 24 & 255) / 255.0F;
        float endRed = (endColor >> 16 & 255) / 255.0F;
        float endGreen = (endColor >> 8 & 255) / 255.0F;
        float endBlue = (endColor & 255) / 255.0F;

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
    }

    public void drawContinuousTexturedBox(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
                                          int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel, ResourceLocation res) {
        if (res != null) {
            CraftPresence.instance.getTextureManager().bindTexture(res);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

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
            for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++)
                drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y + topBorder + (j * fillerHeight), u + leftBorder, v + topBorder, (i == xPasses ? remainderWidth : fillerWidth), (j == yPasses ? remainderHeight : fillerHeight), zLevel);
        }

        // Side Borders
        for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
            // Left Border
            drawTexturedModalRect(x, y + topBorder + (j * fillerHeight), u, v + topBorder, leftBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);
            // Right Border
            drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + (j * fillerHeight), u + leftBorder + fillerWidth, v + topBorder, rightBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);
        }
    }

    public int getButtonY(int order) {
        return (40 + (25 * (order - 1)));
    }
}
