package com.gitlab.cdagaming.craftpresence.handler.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.config.gui.ConfigGUI_Main;
import com.gitlab.cdagaming.craftpresence.handler.FileHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUICheckBox;
import com.google.common.reflect.ClassPath;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GUIHandler {
    public boolean openConfigGUI = false, isInUse = false, enabled = false;

    public List<String> GUI_NAMES = new ArrayList<>();
    private List<String> EXCLUSIONS = new ArrayList<>();
    private List<Class> allowedClasses = new ArrayList<>();
    private String CURRENT_GUI_NAME;
    private Class CURRENT_GUI_CLASS;
    private GuiScreen CURRENT_SCREEN;
    private List<Class> GUI_CLASSES = new ArrayList<>();

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

    public boolean isMouseOver(final int mouseX, final int mouseY, final int elementX, final int elementY, final int elementWidth, final int elementHeight) {
        return mouseX >= elementX && mouseX <= elementX + elementWidth && mouseY >= elementY && mouseY <= elementY + elementHeight;
    }

    public boolean isMouseOver(final int mouseX, final int mouseY, final GuiButton button) {
        return isMouseOver(mouseX, mouseY, button.x, button.y, button.width, button.height);
    }

    public boolean isMouseOver(final int mouseX, final int mouseY, final GUICheckBox checkBox) {
        return isMouseOver(mouseX, mouseY, checkBox.x, checkBox.y, checkBox.boxWidth, checkBox.height);
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
        isInUse = enabled && CraftPresence.instance.currentScreen != null;
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
        if (CraftPresence.instance.currentScreen != null) {
            final Class newScreenClass = CraftPresence.instance.currentScreen.getClass();
            final String newScreenName = newScreenClass.getSimpleName();

            if (!CraftPresence.instance.currentScreen.equals(CURRENT_SCREEN) || !newScreenClass.equals(CURRENT_GUI_CLASS) || !newScreenName.equals(CURRENT_GUI_NAME)) {
                CURRENT_SCREEN = CraftPresence.instance.currentScreen;
                CURRENT_GUI_CLASS = newScreenClass;
                CURRENT_GUI_NAME = newScreenName;
                queuedForUpdate = true;
            }
        }

        if (queuedForUpdate) {
            updateGUIPresence();
        }
    }

    // TODO: Find a Better and More Stable Implementation for ClassPath, that doesn't need Shading
    @SuppressWarnings("UnstableApiUsage")
    public void getGUIs() {
        final ClassLoader LOADER = Thread.currentThread().getContextClassLoader();
        final String[] startingExclusions = new String[]{
                "net.minecraft.network", "net.minecraft.server"
        };
        final Class[] startingAllowedClasses = new Class[]{
                GuiScreen.class, GuiContainer.class
        };

        for (String exclusion : startingExclusions) {
            if (!EXCLUSIONS.contains(exclusion)) {
                EXCLUSIONS.add(exclusion);
            }
        }

        for (Class allowedClass : startingAllowedClasses) {
            if (!allowedClasses.contains(allowedClass)) {
                allowedClasses.add(allowedClass);
            }
        }

        final List<ClassPath.ClassInfo> classes = new ArrayList<>();
        try {
            classes.addAll(ClassPath.from(LOADER).getTopLevelClasses());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (ClassPath.ClassInfo info : classes) {
            boolean hasExclusions = false;
            for (String exclusion : EXCLUSIONS) {
                if (info.getName().startsWith(exclusion) || info.getName().contains(exclusion) || info.getName().equals(exclusion)) {
                    hasExclusions = true;
                    break;
                }
            }

            if (!hasExclusions && info.getName().startsWith("net.minecraft.")) {
                Class guiClass;
                try {
                    guiClass = Class.forName(info.getName());
                } catch (NoClassDefFoundError | ExceptionInInitializerError | ClassNotFoundException ex) {
                    EXCLUSIONS.add(info.getName());
                    continue;
                }

                if (guiClass != null && guiClass.getSuperclass() != null && (allowedClasses.contains(guiClass.getSuperclass()) || GUI_CLASSES.contains(guiClass.getSuperclass()))) {
                    if (!GUI_NAMES.contains(guiClass.getSimpleName())) {
                        GUI_NAMES.add(guiClass.getSimpleName());
                    }
                    if (!GUI_CLASSES.contains(guiClass)) {
                        GUI_CLASSES.add(guiClass);
                    }
                }
            }
        }

        for (String modClass : FileHandler.getModClassNames()) {
            boolean hasExclusions = false;

            for (String exclusion : EXCLUSIONS) {
                if (modClass.contains(exclusion) || modClass.equals(exclusion)) {
                    hasExclusions = true;
                    break;
                }
            }

            if (!hasExclusions) {
                Class modGUIClass;
                try {
                    modGUIClass = Class.forName(modClass);
                } catch (NoClassDefFoundError | ExceptionInInitializerError | ClassNotFoundException ex) {
                    EXCLUSIONS.add(modClass);
                    continue;
                }

                if (modGUIClass != null && modGUIClass.getSuperclass() != null && (allowedClasses.contains(modGUIClass.getSuperclass()) || GUI_CLASSES.contains(modGUIClass.getSuperclass()))) {
                    if (!GUI_NAMES.contains(modGUIClass.getSimpleName())) {
                        GUI_NAMES.add(modGUIClass.getSimpleName());
                    }
                    if (!GUI_CLASSES.contains(modGUIClass)) {
                        GUI_CLASSES.add(modGUIClass);
                    }
                }
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

    private void updateGUIPresence() {
        final String defaultGUIMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.guiMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
        final String currentGUIMSG = StringHandler.getConfigPart(CraftPresence.CONFIG.guiMessages, CURRENT_GUI_NAME, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultGUIMSG);

        // NOTE: Overrides Biomes
        CraftPresence.CLIENT.GAME_STATE = currentGUIMSG.replace("&gui&", CURRENT_GUI_NAME).replace("&class&", CURRENT_GUI_CLASS.getSimpleName()).replace("&screen&", CURRENT_SCREEN.toString());
        CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
        queuedForUpdate = false;
    }

    public void drawHoveringText(List<String> textLines, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, FontRenderer font) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
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
                List<String> wrappedTextLines = new ArrayList<>();
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

            for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber) {
                String line = textLines.get(lineNumber);
                font.drawStringWithShadow(line, (float) tooltipX, (float) tooltipY, -1);

                if (lineNumber + 1 == titleLinesCount) {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    private void drawGradientRect(int zLevel, int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float startAlpha = (float)(startColor >> 24 & 255) / 255.0F;
        float startRed   = (float)(startColor >> 16 & 255) / 255.0F;
        float startGreen = (float)(startColor >>  8 & 255) / 255.0F;
        float startBlue  = (float)(startColor       & 255) / 255.0F;
        float endAlpha   = (float)(endColor   >> 24 & 255) / 255.0F;
        float endRed     = (float)(endColor   >> 16 & 255) / 255.0F;
        float endGreen   = (float)(endColor   >>  8 & 255) / 255.0F;
        float endBlue    = (float)(endColor         & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(right,    top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        buffer.pos( left,    top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        buffer.pos( left, bottom, zLevel).color(  endRed,   endGreen,   endBlue,   endAlpha).endVertex();
        buffer.pos(right, bottom, zLevel).color(  endRed,   endGreen,   endBlue,   endAlpha).endVertex();
        tessellator.draw();

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public void drawContinuousTexturedBox(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
                                          int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel, @Nullable ResourceLocation res) {
        if (res != null) {
            CraftPresence.instance.getTextureManager().bindTexture(res);
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

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
