package com.gitlab.cdagaming.craftpresence.handler.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.config.gui.ConfigGUI_Main;
import com.gitlab.cdagaming.craftpresence.handler.FileHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.google.common.reflect.ClassPath;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
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

    public boolean isMouseOverElement(final int mouseX, final int mouseY, final int elementX, final int elementY, final int elementWidth, final int elementHeight) {
        return mouseX >= elementX && mouseX <= elementX + elementWidth && mouseY >= elementY && mouseY <= elementY + elementHeight;
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
        final boolean needsUpdate = enabled && (GUI_NAMES.isEmpty() || GUI_CLASSES.isEmpty());

        if (needsUpdate) {
            getGUIs();
        }

        if (enabled && CraftPresence.instance.currentScreen != null) {
            isInUse = true;
            updateGUIData();
        }

        if (isInUse) {
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
