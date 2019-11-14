package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.assets.DiscordAssetUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class DynamicEditorGui extends GuiScreen {
    private final GuiScreen parentScreen, currentScreen;
    private ExtendedButtonControl proceedButton, specificIconButton;
    private GuiTextField specificMessage, newValueName;
    private String attributeName, configOption, specificMSG, defaultMSG, mainTitle, removeMSG;
    private boolean isNewValue, isDefaultValue;

    DynamicEditorGui(GuiScreen parentScreen, String attributeName, String configOption) {
        mc = CraftPresence.instance;
        currentScreen = this;
        this.parentScreen = parentScreen;
        this.configOption = configOption;
        this.attributeName = attributeName;
        isNewValue = StringUtils.isNullOrEmpty(attributeName);
        isDefaultValue = !StringUtils.isNullOrEmpty(attributeName) && "default".equals(attributeName);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        if (isNewValue) {
            mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.editor.addnew");
            if (parentScreen instanceof BiomeSettingsGui) {
                specificMSG = defaultMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.biomeMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
            } else if (parentScreen instanceof DimensionSettingsGui) {
                specificMSG = defaultMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
            } else if (parentScreen instanceof ServerSettingsGui) {
                specificMSG = defaultMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
            } else if (parentScreen instanceof AdvancedSettingsGui) {
                if (configOption.equals(CraftPresence.CONFIG.NAME_guiMessages)) {
                    specificMSG = defaultMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.guiMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                } else if (configOption.equals(CraftPresence.CONFIG.NAME_itemMessages)) {
                    specificMSG = defaultMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                }
            }
        } else {
            if (parentScreen instanceof BiomeSettingsGui) {
                mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.biome.editspecificbiome", attributeName);
                defaultMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.biomeMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                specificMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.biomeMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMSG);
            } else if (parentScreen instanceof DimensionSettingsGui) {
                mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.dimension.editspecificdimension", attributeName);
                defaultMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                specificMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMSG);
            } else if (parentScreen instanceof ServerSettingsGui) {
                mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.server.editspecificserver", attributeName);
                defaultMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                specificMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMSG);
            } else if (parentScreen instanceof AdvancedSettingsGui) {
                if (configOption.equals(CraftPresence.CONFIG.NAME_guiMessages)) {
                    mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.gui.editspecificgui", attributeName);
                    defaultMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.guiMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                    specificMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.guiMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMSG);
                } else if (configOption.equals(CraftPresence.CONFIG.NAME_itemMessages)) {
                    mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title.gui.editspecificitem", attributeName);
                    defaultMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages, "default", 0, 1, CraftPresence.CONFIG.splitCharacter, null);
                    specificMSG = StringUtils.getConfigPart(CraftPresence.CONFIG.itemMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, defaultMSG);
                }
            }
        }

        removeMSG = ModUtils.TRANSLATOR.translate("gui.config.message.remove");

        specificMessage = new GuiTextField(110, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(1), 180, 20);
        specificMessage.setText(specificMSG);

        if ((parentScreen instanceof DimensionSettingsGui || parentScreen instanceof ServerSettingsGui) && !isNewValue) {
            specificIconButton = new ExtendedButtonControl(100, (width / 2) - 90, CraftPresence.GUIS.getButtonY(2), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.iconchange"));
            buttonList.add(specificIconButton);
        }
        if (isNewValue) {
            newValueName = new GuiTextField(120, mc.fontRenderer, (width / 2) + 3, CraftPresence.GUIS.getButtonY(3), 180, 20);
        }

        proceedButton = new ExtendedButtonControl(900, (width / 2) - 90, (height - 30), 180, 20, ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back"));

        buttonList.add(proceedButton);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CraftPresence.GUIS.drawBackground(width, height);

        final String messageText = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.message");
        final String valueNameText = ModUtils.TRANSLATOR.translate("gui.config.editorMessage.valuename");

        drawString(mc.fontRenderer, mainTitle, (width / 2) - (StringUtils.getStringWidth(mainTitle) / 2), 15, 0xFFFFFF);
        drawString(mc.fontRenderer, messageText, (width / 2) - 130, CraftPresence.GUIS.getButtonY(1) + 5, 0xFFFFFF);
        if (isNewValue) {
            drawString(mc.fontRenderer, valueNameText, (width / 2) - 130, CraftPresence.GUIS.getButtonY(3) + 5, 0xFFFFFF);
            newValueName.drawTextBox();
        } else {
            drawString(mc.fontRenderer, removeMSG, (width / 2) - (StringUtils.getStringWidth(removeMSG) / 2), (height - 45), 0xFFFFFF);
        }
        specificMessage.drawTextBox();

        proceedButton.displayString = !specificMessage.getText().equals(specificMSG) || (isNewValue && !StringUtils.isNullOrEmpty(newValueName.getText()) && !specificMessage.getText().equals(defaultMSG)) || (isDefaultValue && !StringUtils.isNullOrEmpty(specificMessage.getText()) && !specificMessage.getText().equals(specificMSG)) ? ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.continue") : ModUtils.TRANSLATOR.translate("gui.config.buttonMessage.back");

        proceedButton.enabled = !(StringUtils.isNullOrEmpty(specificMessage.getText()) && isDefaultValue);

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Hovering over Message Label
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(1) + 5, StringUtils.getStringWidth(messageText), mc.fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.message.remove")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        // Hovering over Value Name Label
        if (isNewValue && CraftPresence.GUIS.isMouseOver(mouseX, mouseY, (width / 2f) - 130, CraftPresence.GUIS.getButtonY(3) + 5, StringUtils.getStringWidth(valueNameText), mc.fontRenderer.FONT_HEIGHT)) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.hoverMessage.valuename")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
        if (CraftPresence.GUIS.isMouseOver(mouseX, mouseY, proceedButton) && !proceedButton.enabled) {
            CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate("gui.config.hoverMessage.defaultempty")), mouseX, mouseY, width, height, -1, mc.fontRenderer, true);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == proceedButton.id) {
            if (!specificMessage.getText().equals(specificMSG) || (isNewValue && !StringUtils.isNullOrEmpty(newValueName.getText()) && !specificMessage.getText().equals(defaultMSG)) || (isDefaultValue && !StringUtils.isNullOrEmpty(specificMessage.getText()) && !specificMessage.getText().equals(specificMSG))) {
                if (isNewValue && !StringUtils.isNullOrEmpty(newValueName.getText())) {
                    attributeName = newValueName.getText();
                }
                CraftPresence.CONFIG.hasChanged = true;
                if (parentScreen instanceof BiomeSettingsGui) {
                    CraftPresence.CONFIG.biomeMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.biomeMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, specificMessage.getText());
                } else if (parentScreen instanceof DimensionSettingsGui) {
                    CraftPresence.CONFIG.dimensionMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.dimensionMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, specificMessage.getText());
                } else if (parentScreen instanceof ServerSettingsGui) {
                    CraftPresence.CONFIG.serverMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, specificMessage.getText());
                } else if (parentScreen instanceof AdvancedSettingsGui) {
                    if (configOption.equals(CraftPresence.CONFIG.NAME_guiMessages)) {
                        CraftPresence.CONFIG.guiMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.guiMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, specificMessage.getText());
                    } else if (configOption.equals(CraftPresence.CONFIG.NAME_itemMessages)) {
                        CraftPresence.CONFIG.itemMessages = StringUtils.setConfigPart(CraftPresence.CONFIG.itemMessages, attributeName, 0, 1, CraftPresence.CONFIG.splitCharacter, specificMessage.getText());
                    }
                }
            }
            if (StringUtils.isNullOrEmpty(specificMessage.getText()) || (specificMessage.getText().equalsIgnoreCase(defaultMSG) && !specificMSG.equals(defaultMSG) && !isDefaultValue)) {
                if (isNewValue && !StringUtils.isNullOrEmpty(newValueName.getText())) {
                    attributeName = newValueName.getText();
                }
                CraftPresence.CONFIG.hasChanged = true;
                if (parentScreen instanceof BiomeSettingsGui) {
                    CraftPresence.CONFIG.biomeMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.biomeMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                    CraftPresence.BIOMES.BIOME_NAMES.remove(attributeName);
                    CraftPresence.BIOMES.getBiomes();
                } else if (parentScreen instanceof DimensionSettingsGui) {
                    CraftPresence.CONFIG.dimensionMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.dimensionMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                    CraftPresence.DIMENSIONS.DIMENSION_NAMES.remove(attributeName);
                    CraftPresence.DIMENSIONS.getDimensions();
                } else if (parentScreen instanceof ServerSettingsGui) {
                    CraftPresence.CONFIG.serverMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.serverMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                    CraftPresence.SERVER.knownAddresses.remove(attributeName);
                    CraftPresence.SERVER.getServerAddresses();
                } else if (parentScreen instanceof AdvancedSettingsGui) {
                    if (configOption.equals(CraftPresence.CONFIG.NAME_guiMessages)) {
                        CraftPresence.CONFIG.guiMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.guiMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                        CraftPresence.GUIS.GUI_NAMES.remove(attributeName);
                        CraftPresence.GUIS.getGUIs();
                    } else if (configOption.equals(CraftPresence.CONFIG.NAME_itemMessages)) {
                        CraftPresence.CONFIG.itemMessages = StringUtils.removeFromArray(CraftPresence.CONFIG.itemMessages, attributeName, 0, CraftPresence.CONFIG.splitCharacter);
                        CraftPresence.ENTITIES.ENTITY_NAMES.remove(attributeName);
                        CraftPresence.ENTITIES.getEntities();
                    }
                }
            }
            mc.displayGuiScreen(parentScreen);
        } else if (buttonList.contains(specificIconButton) && button.id == specificIconButton.id) {
            if (parentScreen instanceof DimensionSettingsGui) {
                final String defaultIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, "default", 0, 2, CraftPresence.CONFIG.splitCharacter, CraftPresence.CONFIG.defaultDimensionIcon);
                final String specificIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.dimensionMessages, attributeName, 0, 2, CraftPresence.CONFIG.splitCharacter, defaultIcon);
                mc.displayGuiScreen(new SelectorGui(currentScreen, CraftPresence.CONFIG.NAME_dimensionMessages, ModUtils.TRANSLATOR.translate("gui.config.title.selector.icon"), DiscordAssetUtils.ICON_LIST, specificIcon, attributeName, true));
            } else if (parentScreen instanceof ServerSettingsGui) {
                final String defaultIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, "default", 0, 2, CraftPresence.CONFIG.splitCharacter, CraftPresence.CONFIG.defaultServerIcon);
                final String specificIcon = StringUtils.getConfigPart(CraftPresence.CONFIG.serverMessages, attributeName, 0, 2, CraftPresence.CONFIG.splitCharacter, defaultIcon);
                mc.displayGuiScreen(new SelectorGui(currentScreen, CraftPresence.CONFIG.NAME_serverMessages, ModUtils.TRANSLATOR.translate("gui.config.title.selector.icon"), DiscordAssetUtils.ICON_LIST, specificIcon, attributeName, true));
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parentScreen);
        }

        if (isNewValue) {
            newValueName.textboxKeyTyped(typedChar, keyCode);
        }
        specificMessage.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (isNewValue) {
            newValueName.mouseClicked(mouseX, mouseY, mouseButton);
        }
        specificMessage.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        if (isNewValue) {
            newValueName.updateCursorCounter();
        }
        specificMessage.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
