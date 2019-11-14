package com.gitlab.cdagaming.craftpresence.config;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.google.common.collect.Maps;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class ConfigUtils {
    // Config Names
    // GENERAL
    public String NAME_detectCurseManifest, NAME_detectMultiMCManifest, NAME_detectMCUpdaterInstance, NAME_detectTechnicPack,
            NAME_showTime, NAME_showCurrentBiome, NAME_showCurrentDimension,
            NAME_showGameState, NAME_clientID, NAME_defaultIcon, NAME_enableJoinRequest;
    // BIOME MESSAGES
    public String NAME_biomeMessages;
    // DIMENSION MESSAGES
    public String NAME_defaultDimensionIcon, NAME_dimensionMessages;
    // SERVER MESSAGES
    public String NAME_defaultServerIcon, NAME_defaultServerName,
            NAME_defaultServerMOTD, NAME_serverMessages;
    // STATUS MESSAGES
    public String NAME_mainmenuMSG, NAME_lanMSG, NAME_singleplayerMSG,
            NAME_packPlaceholderMSG, NAME_playerPlaceholderMSG, NAME_playerAmountPlaceholderMSG,
            NAME_gameTimePlaceholderMSG, NAME_modsPlaceholderMSG, NAME_vivecraftMessage;
    // ADVANCED
    public String NAME_enableCommands, NAME_enablePERGUI, NAME_enablePERItem, NAME_overwriteServerIcon, NAME_renderTooltips,
            NAME_splitCharacter, NAME_guiMessages, NAME_itemMessages;
    // ACCESSIBILITY
    public String NAME_tooltipBGColor, NAME_tooltipBorderColor, NAME_guiBGColor, NAME_languageID, NAME_stripTranslationColors, NAME_showLoggingInChat, NAME_configKeycode;

    // Config Variables
    // GENERAL
    public boolean detectCurseManifest, detectMultiMCManifest, detectMCUpdaterInstance, detectTechnicPack, showTime,
            showCurrentBiome, showCurrentDimension, showGameState, enableJoinRequest;
    public String clientID, defaultIcon;
    // BIOME MESSAGES
    public String[] biomeMessages;
    // DIMENSION MESSAGES
    public String defaultDimensionIcon;
    public String[] dimensionMessages;
    // SERVER MESSAGES
    public String defaultServerIcon, defaultServerName, defaultServerMOTD;
    public String[] serverMessages;
    // STATUS MESSAGES
    public String mainmenuMSG, lanMSG, singleplayerMSG, packPlaceholderMSG,
            playerPlaceholderMSG, playerAmountPlaceholderMSG,
            gameTimePlaceholderMSG, modsPlaceholderMSG, vivecraftMessage;
    // ADVANCED
    public boolean enableCommands, enablePERGUI, enablePERItem, overwriteServerIcon, renderTooltips;
    public String splitCharacter;
    public String[] guiMessages, itemMessages;
    // ACCESSIBILITY
    public String tooltipBGColor, tooltipBorderColor, guiBGColor, languageID, configKeyCode;
    public boolean stripTranslationColors, showLoggingInChat;

    // CLASS-SPECIFIC - PUBLIC
    public boolean hasChanged = false, hasClientPropertiesChanged = false;
    public String queuedSplitCharacter;
    public File configFile, parentDir;
    public Properties properties = new Properties();
    // CLASS-SPECIFIC - PRIVATE
    private Map<String, String> configPropertyMappings = Maps.newHashMap();
    private String fileName;
    private boolean verified = false, initialized = false, isConfigNew = false;

    public ConfigUtils(String fileName) {
        this.fileName = fileName;
    }

    public void setupInitialValues() {
        // GENERAL
        NAME_detectCurseManifest = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.detectcursemanifest").replaceAll(" ", "_");
        NAME_detectMultiMCManifest = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.detectmultimcmanifest").replaceAll(" ", "_");
        NAME_detectMCUpdaterInstance = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.detectmcupdaterinstance").replaceAll(" ", "_");
        NAME_detectTechnicPack = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.detecttechnicpack").replaceAll(" ", "_");
        NAME_showTime = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.showtime").replaceAll(" ", "_");
        NAME_showCurrentBiome = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.showbiome").replaceAll(" ", "_");
        NAME_showCurrentDimension = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.showdimension").replaceAll(" ", "_");
        NAME_showGameState = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.showstate").replaceAll(" ", "_");
        NAME_clientID = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.clientid").replaceAll(" ", "_");
        NAME_defaultIcon = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.defaulticon").replaceAll(" ", "_");
        NAME_enableJoinRequest = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.enablejoinrequest").replaceAll(" ", "_");
        detectCurseManifest = true;
        detectMultiMCManifest = true;
        detectMCUpdaterInstance = true;
        detectTechnicPack = true;
        showTime = true;
        showCurrentBiome = false;
        showCurrentDimension = true;
        showGameState = true;
        clientID = "450485984333660181";
        defaultIcon = "grass";
        enableJoinRequest = false;
        // BIOME MESSAGES
        NAME_biomeMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.biomemessages.biomemessages").replaceAll(" ", "_");
        biomeMessages = new String[]{"default;Playing in &biome&"};
        // DIMENSION MESSAGES
        NAME_defaultDimensionIcon = ModUtils.TRANSLATOR.translate(true, "gui.config.name.dimensionmessages.dimensionicon").replaceAll(" ", "_");
        NAME_dimensionMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.dimensionmessages.dimensionmessages").replaceAll(" ", "_");
        defaultDimensionIcon = "unknown";
        dimensionMessages = new String[]{"default" + (!StringUtils.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + "In The &dimension&"};
        // SERVER MESSAGES
        NAME_defaultServerIcon = ModUtils.TRANSLATOR.translate(true, "gui.config.name.servermessages.servericon").replaceAll(" ", "_");
        NAME_defaultServerName = ModUtils.TRANSLATOR.translate(true, "gui.config.name.servermessages.servername").replaceAll(" ", "_");
        NAME_defaultServerMOTD = ModUtils.TRANSLATOR.translate(true, "gui.config.name.servermessages.servermotd").replaceAll(" ", "_");
        NAME_serverMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.servermessages.servermessages").replaceAll(" ", "_");
        defaultServerIcon = "default";
        defaultServerName = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.servermessages.servername");
        defaultServerMOTD = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.servermessages.servermotd");
        serverMessages = new String[]{"default;Playing on &motd&"};
        // STATUS MESSAGES
        NAME_mainmenuMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.mainmenumsg").replaceAll(" ", "_");
        NAME_lanMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.lanmsg").replaceAll(" ", "_");
        NAME_singleplayerMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.singleplayermsg").replaceAll(" ", "_");
        NAME_packPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.placeholder.packmsg").replaceAll(" ", "_");
        NAME_playerPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.placeholder.playermsg").replaceAll(" ", "_");
        NAME_playerAmountPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.placeholder.playeramountmsg").replaceAll(" ", "_");
        NAME_gameTimePlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.placeholder.gametimemsg").replaceAll(" ", "_");
        NAME_modsPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.placeholder.modsmsg").replaceAll(" ", "_");
        NAME_vivecraftMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.special.vivecraftmsg").replaceAll(" ", "_");
        mainmenuMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.state.mainmenu");
        lanMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.state.lan");
        singleplayerMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.state.singleplayer");
        packPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.pack");
        playerPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.ign");
        playerAmountPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.players");
        gameTimePlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.time");
        modsPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.mods");
        vivecraftMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.special.vivecraft");
        // ADVANCED
        NAME_enableCommands = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.enablecommands").replaceAll(" ", "_");
        NAME_enablePERGUI = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.enablepergui").replaceAll(" ", "_");
        NAME_enablePERItem = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.enableperitem").replaceAll(" ", "_");
        NAME_overwriteServerIcon = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.overwriteservericon").replaceAll(" ", "_");
        NAME_renderTooltips = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.rendertooltips").replaceAll(" ", "_");
        NAME_splitCharacter = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.splitcharacter").replaceAll(" ", "_");
        NAME_guiMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.guimessages").replaceAll(" ", "_");
        NAME_itemMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.itemmessages").replaceAll(" ", "_");
        enableCommands = true;
        enablePERGUI = false;
        enablePERItem = false;
        overwriteServerIcon = false;
        renderTooltips = true;
        splitCharacter = ";";
        guiMessages = new String[]{"default;In &gui&"};
        itemMessages = new String[]{"default;Holding &main&"};
        // ACCESSIBILITY
        NAME_tooltipBGColor = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.tooltipbgcolor").replaceAll(" ", "_");
        NAME_tooltipBorderColor = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.tooltipbordercolor").replaceAll(" ", "_");
        NAME_guiBGColor = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.guibgcolor").replaceAll(" ", "_");
        NAME_languageID = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.languageid").replaceAll(" ", "_");
        NAME_stripTranslationColors = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.striptranslationcolors").replaceAll(" ", "_");
        NAME_showLoggingInChat = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.showlogginginchat").replaceAll(" ", "_");
        NAME_configKeycode = ModUtils.TRANSLATOR.translate(true, "key.craftpresence.config_keybind").replaceAll(" ", "_");
        tooltipBGColor = "-267386864";
        tooltipBorderColor = "1347420415";
        guiBGColor = "minecraft" + splitCharacter + "textures/gui/options_background.png";
        languageID = "en_US";
        stripTranslationColors = false;
        showLoggingInChat = false;
        configKeyCode = Integer.toString(Keyboard.KEY_GRAVE);

        for (Field field : getClass().getDeclaredFields()) {
            if (field.getName().contains("NAME_")) {
                try {
                    field.setAccessible(true);
                    configPropertyMappings.put(field.getName(), field.get(this).toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        initialized = true;
    }

    public void initialize() {
        try {
            configFile = new File(fileName);
            parentDir = configFile.getParentFile();
            isConfigNew = (!parentDir.exists() && parentDir.mkdirs()) || (!configFile.exists() && configFile.createNewFile());
            setupInitialValues();
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.save"));
            ex.printStackTrace();
        } finally {
            if (initialized) {
                if (isConfigNew) {
                    updateConfig();
                }
                read(false);
            }
        }
    }

    public void read(final boolean skipLogging) {
        Reader configReader = null;
        FileInputStream inputStream = null;
        verified = false;

        try {
            inputStream = new FileInputStream(configFile);
            configReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            properties.load(configReader);
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.save"));
            ex.printStackTrace();
        } finally {
            try {
                // GENERAL
                detectCurseManifest = StringUtils.isValidBoolean(properties.getProperty(NAME_detectCurseManifest)) ? Boolean.parseBoolean(properties.getProperty(NAME_detectCurseManifest)) : detectCurseManifest;
                detectMultiMCManifest = StringUtils.isValidBoolean(properties.getProperty(NAME_detectMultiMCManifest)) ? Boolean.parseBoolean(properties.getProperty(NAME_detectMultiMCManifest)) : detectMultiMCManifest;
                detectMCUpdaterInstance = StringUtils.isValidBoolean(properties.getProperty(NAME_detectMCUpdaterInstance)) ? Boolean.parseBoolean(properties.getProperty(NAME_detectMCUpdaterInstance)) : detectMCUpdaterInstance;
                detectTechnicPack = StringUtils.isValidBoolean(properties.getProperty(NAME_detectTechnicPack)) ? Boolean.parseBoolean(properties.getProperty(NAME_detectTechnicPack)) : detectTechnicPack;
                showTime = StringUtils.isValidBoolean(properties.getProperty(NAME_showTime)) ? Boolean.parseBoolean(properties.getProperty(NAME_showTime)) : showTime;
                showCurrentBiome = StringUtils.isValidBoolean(properties.getProperty(NAME_showCurrentBiome)) ? Boolean.parseBoolean(properties.getProperty(NAME_showCurrentBiome)) : showCurrentBiome;
                showCurrentDimension = StringUtils.isValidBoolean(properties.getProperty(NAME_showCurrentDimension)) ? Boolean.parseBoolean(properties.getProperty(NAME_showCurrentDimension)) : showCurrentDimension;
                showGameState = StringUtils.isValidBoolean(properties.getProperty(NAME_showGameState)) ? Boolean.parseBoolean(properties.getProperty(NAME_showGameState)) : showGameState;
                clientID = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_clientID)) ? properties.getProperty(NAME_clientID) : clientID;
                defaultIcon = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_defaultIcon)) ? properties.getProperty(NAME_defaultIcon) : defaultIcon;
                enableJoinRequest = StringUtils.isValidBoolean(properties.getProperty(NAME_enableJoinRequest)) ? Boolean.parseBoolean(properties.getProperty(NAME_enableJoinRequest)) : enableJoinRequest;
                // BIOME MESSAGES
                biomeMessages = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_biomeMessages).replaceAll("\\[", "").replaceAll("]", "")) ? properties.getProperty(NAME_biomeMessages).replaceAll("\\[", "").replaceAll("]", "").split(", ") : biomeMessages;
                // DIMENSION MESSAGES
                defaultDimensionIcon = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_defaultDimensionIcon)) ? properties.getProperty(NAME_defaultDimensionIcon) : defaultDimensionIcon;
                dimensionMessages = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_dimensionMessages).replaceAll("\\[", "").replaceAll("]", "")) ? properties.getProperty(NAME_dimensionMessages).replaceAll("\\[", "").replaceAll("]", "").split(", ") : dimensionMessages;
                // SERVER MESSAGES
                defaultServerIcon = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_defaultServerIcon)) ? properties.getProperty(NAME_defaultServerIcon) : defaultServerIcon;
                defaultServerName = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_defaultServerName)) ? properties.getProperty(NAME_defaultServerName) : defaultServerName;
                defaultServerMOTD = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_defaultServerMOTD)) ? properties.getProperty(NAME_defaultServerMOTD) : defaultServerMOTD;
                serverMessages = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_serverMessages).replaceAll("\\[", "").replaceAll("]", "")) ? properties.getProperty(NAME_serverMessages).replaceAll("\\[", "").replaceAll("]", "").split(", ") : serverMessages;
                // STATUS MESSAGES
                mainmenuMSG = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_mainmenuMSG)) ? properties.getProperty(NAME_mainmenuMSG) : mainmenuMSG;
                lanMSG = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_lanMSG)) ? properties.getProperty(NAME_lanMSG) : lanMSG;
                singleplayerMSG = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_singleplayerMSG)) ? properties.getProperty(NAME_singleplayerMSG) : singleplayerMSG;
                packPlaceholderMSG = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_packPlaceholderMSG)) ? properties.getProperty(NAME_packPlaceholderMSG) : packPlaceholderMSG;
                playerPlaceholderMSG = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_playerPlaceholderMSG)) ? properties.getProperty(NAME_playerPlaceholderMSG) : playerPlaceholderMSG;
                playerAmountPlaceholderMSG = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_playerAmountPlaceholderMSG)) ? properties.getProperty(NAME_playerAmountPlaceholderMSG) : playerAmountPlaceholderMSG;
                gameTimePlaceholderMSG = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_gameTimePlaceholderMSG)) ? properties.getProperty(NAME_gameTimePlaceholderMSG) : gameTimePlaceholderMSG;
                modsPlaceholderMSG = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_modsPlaceholderMSG)) ? properties.getProperty(NAME_modsPlaceholderMSG) : modsPlaceholderMSG;
                vivecraftMessage = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_vivecraftMessage)) ? properties.getProperty(NAME_vivecraftMessage) : vivecraftMessage;
                // ADVANCED
                enableCommands = StringUtils.isValidBoolean(properties.getProperty(NAME_enableCommands)) ? Boolean.parseBoolean(properties.getProperty(NAME_enableCommands)) : enableCommands;
                enablePERGUI = StringUtils.isValidBoolean(properties.getProperty(NAME_enablePERGUI)) ? Boolean.parseBoolean(properties.getProperty(NAME_enablePERGUI)) : enablePERGUI;
                enablePERItem = StringUtils.isValidBoolean(properties.getProperty(NAME_enablePERItem)) ? Boolean.parseBoolean(properties.getProperty(NAME_enablePERItem)) : enablePERItem;
                overwriteServerIcon = StringUtils.isValidBoolean(properties.getProperty(NAME_overwriteServerIcon)) ? Boolean.parseBoolean(properties.getProperty(NAME_overwriteServerIcon)) : overwriteServerIcon;
                renderTooltips = StringUtils.isValidBoolean(properties.getProperty(NAME_renderTooltips)) ? Boolean.parseBoolean(properties.getProperty(NAME_renderTooltips)) : renderTooltips;
                splitCharacter = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_splitCharacter)) ? properties.getProperty(NAME_splitCharacter) : splitCharacter;
                guiMessages = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_guiMessages).replaceAll("\\[", "").replaceAll("]", "")) ? properties.getProperty(NAME_guiMessages).replaceAll("\\[", "").replaceAll("]", "").split(", ") : guiMessages;
                itemMessages = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_itemMessages).replaceAll("\\[", "").replaceAll("]", "")) ? properties.getProperty(NAME_itemMessages).replaceAll("\\[", "").replaceAll("]", "").split(", ") : itemMessages;
                // ACCESSIBILITY
                tooltipBGColor = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_tooltipBGColor)) ? properties.getProperty(NAME_tooltipBGColor) : tooltipBGColor;
                tooltipBorderColor = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_tooltipBorderColor)) ? properties.getProperty(NAME_tooltipBorderColor) : tooltipBorderColor;
                guiBGColor = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_guiBGColor)) ? properties.getProperty(NAME_guiBGColor) : guiBGColor;
                languageID = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_languageID)) ? properties.getProperty(NAME_languageID) : languageID;
                stripTranslationColors = StringUtils.isValidBoolean(properties.getProperty(NAME_stripTranslationColors)) ? Boolean.parseBoolean(properties.getProperty(NAME_stripTranslationColors)) : stripTranslationColors;
                showLoggingInChat = StringUtils.isValidBoolean(properties.getProperty(NAME_showLoggingInChat)) ? Boolean.parseBoolean(properties.getProperty(NAME_showLoggingInChat)) : showLoggingInChat;
                configKeyCode = !StringUtils.isNullOrEmpty(properties.getProperty(NAME_configKeycode)) ? properties.getProperty(NAME_configKeycode) : configKeyCode;
            } catch (NullPointerException ex) {
                verifyConfig();
            } finally {
                if (!verified) {
                    verifyConfig();
                }
                if (!skipLogging) {
                    if (isConfigNew) {
                        ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.info.config.new"));
                    } else {
                        ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.info.config.save"));
                    }
                }
            }
        }

        try {
            if (configReader != null) {
                configReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.dataclose"));
            ex.printStackTrace();
        }
    }

    public void updateConfig() {
        // GENERAL
        properties.setProperty(NAME_detectCurseManifest, Boolean.toString(detectCurseManifest));
        properties.setProperty(NAME_detectMultiMCManifest, Boolean.toString(detectMultiMCManifest));
        properties.setProperty(NAME_detectMCUpdaterInstance, Boolean.toString(detectMCUpdaterInstance));
        properties.setProperty(NAME_detectTechnicPack, Boolean.toString(detectTechnicPack));
        properties.setProperty(NAME_showTime, Boolean.toString(showTime));
        properties.setProperty(NAME_showCurrentBiome, Boolean.toString(showCurrentBiome));
        properties.setProperty(NAME_showCurrentDimension, Boolean.toString(showCurrentDimension));
        properties.setProperty(NAME_showGameState, Boolean.toString(showGameState));
        properties.setProperty(NAME_clientID, clientID);
        properties.setProperty(NAME_defaultIcon, defaultIcon);
        properties.setProperty(NAME_enableJoinRequest, Boolean.toString(enableJoinRequest));
        // BIOME MESSAGES
        properties.setProperty(NAME_biomeMessages, Arrays.toString(biomeMessages));
        // DIMENSION MESSAGES
        properties.setProperty(NAME_defaultDimensionIcon, defaultDimensionIcon);
        properties.setProperty(NAME_dimensionMessages, Arrays.toString(dimensionMessages));
        // SERVER MESSAGES
        properties.setProperty(NAME_defaultServerIcon, defaultServerIcon);
        properties.setProperty(NAME_defaultServerName, defaultServerName);
        properties.setProperty(NAME_defaultServerMOTD, defaultServerMOTD);
        properties.setProperty(NAME_serverMessages, Arrays.toString(serverMessages));
        // STATUS MESSAGES
        properties.setProperty(NAME_mainmenuMSG, mainmenuMSG);
        properties.setProperty(NAME_lanMSG, lanMSG);
        properties.setProperty(NAME_singleplayerMSG, singleplayerMSG);
        properties.setProperty(NAME_packPlaceholderMSG, packPlaceholderMSG);
        properties.setProperty(NAME_playerPlaceholderMSG, playerPlaceholderMSG);
        properties.setProperty(NAME_playerAmountPlaceholderMSG, playerAmountPlaceholderMSG);
        properties.setProperty(NAME_gameTimePlaceholderMSG, gameTimePlaceholderMSG);
        properties.setProperty(NAME_modsPlaceholderMSG, modsPlaceholderMSG);
        properties.setProperty(NAME_vivecraftMessage, vivecraftMessage);
        // ADVANCED
        properties.setProperty(NAME_enableCommands, Boolean.toString(enableCommands));
        properties.setProperty(NAME_enablePERGUI, Boolean.toString(enablePERGUI));
        properties.setProperty(NAME_enablePERItem, Boolean.toString(enablePERItem));
        properties.setProperty(NAME_overwriteServerIcon, Boolean.toString(overwriteServerIcon));
        properties.setProperty(NAME_renderTooltips, Boolean.toString(renderTooltips));
        properties.setProperty(NAME_splitCharacter, splitCharacter);
        properties.setProperty(NAME_guiMessages, Arrays.toString(guiMessages));
        properties.setProperty(NAME_itemMessages, Arrays.toString(itemMessages));
        // ACCESSIBILITY
        properties.setProperty(NAME_tooltipBGColor, tooltipBGColor);
        properties.setProperty(NAME_tooltipBorderColor, tooltipBorderColor);
        properties.setProperty(NAME_guiBGColor, guiBGColor);
        properties.setProperty(NAME_languageID, languageID);
        properties.setProperty(NAME_stripTranslationColors, Boolean.toString(stripTranslationColors));
        properties.setProperty(NAME_showLoggingInChat, Boolean.toString(showLoggingInChat));
        properties.setProperty(NAME_configKeycode, configKeyCode);

        // Check for Conflicts before Saving
        if (showCurrentBiome && showGameState) {
            ModUtils.LOG.warn(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.warning.config.conflict.biomestate"));
            showCurrentBiome = false;
            properties.setProperty(NAME_showCurrentBiome, "false");
        }
        if (enablePERGUI && showGameState) {
            ModUtils.LOG.warn(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.warning.config.conflict.pergui"));
            enablePERGUI = false;
            properties.setProperty(NAME_enablePERGUI, "false");
        }

        save();
    }

    private void verifyConfig() {
        boolean needsFullUpdate = false;

        for (Map.Entry<String, String> configEntrySet : configPropertyMappings.entrySet()) {
            if (!properties.stringPropertyNames().contains(configEntrySet.getValue())) {
                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.emptyprop", configEntrySet.getValue()));
                Field configPropertyValue = null;
                try {
                    // Case 1: Try to Locate Property Value by Exact Case
                    // Ex: NAME_exampleValue tries to get from exampleValue Assignment
                    configPropertyValue = getClass().getDeclaredField(configEntrySet.getKey().replace("NAME_", ""));
                } catch (Exception ex) {
                    // Case 2: Look through All Declared Fields to see If it matches in lower case
                    // Ex: NAME_exampleValue >> Tries to find exampleValue in any case
                    for (Field declaredField : getClass().getDeclaredFields()) {
                        if (declaredField != null && declaredField.getName().equalsIgnoreCase(configEntrySet.getKey().replace("NAME_", ""))) {
                            configPropertyValue = declaredField;
                            break;
                        }
                    }
                }

                try {
                    // Attempt to Save Value if Found a Matching Value
                    if (configPropertyValue != null) {
                        configPropertyValue.setAccessible(true);
                        properties.setProperty(configEntrySet.getValue(), configPropertyValue.get(this).toString());
                        save();
                        needsFullUpdate = true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        for (String property : properties.stringPropertyNames()) {
            if (!configPropertyMappings.containsValue(property)) {
                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.invalidprop", property));
                properties.remove(property);
                save();
            } else {
                if (property.equals(NAME_clientID) && (properties.getProperty(property).length() != 18 || !StringUtils.getValidLong(properties.getProperty(property)).getFirst())) {
                    ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.invalidprop", property));
                    clientID = "450485984333660181";
                    properties.setProperty(property, clientID);
                    save();
                }
                if (property.equals(NAME_configKeycode) && (!StringUtils.getValidInteger(properties.getProperty(property)).getFirst())) {
                    ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.invalidprop", property));
                    configKeyCode = Integer.toString(Keyboard.KEY_LCONTROL);
                    properties.setProperty(property, configKeyCode);
                    save();
                }
                if (property.equals(NAME_splitCharacter) && (properties.getProperty(property).length() != 1 || properties.getProperty(property).matches(".*[a-z].*") || properties.getProperty(property).matches(".*[A-Z].*"))) {
                    ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.invalidprop", property));
                    queuedSplitCharacter = ";";
                }
                if ((property.equals(NAME_enableJoinRequest) && properties.getProperty(property).equals("false")) && (!StringUtils.isNullOrEmpty(CraftPresence.CLIENT.PARTY_ID) || !StringUtils.isNullOrEmpty(CraftPresence.CLIENT.JOIN_SECRET) || CraftPresence.SYSTEM.TIMER != 0 || CraftPresence.awaitingReply || CraftPresence.CLIENT.PARTY_SIZE != 0 || CraftPresence.CLIENT.PARTY_MAX != 0 || CraftPresence.CLIENT.REQUESTER_USER != null)) {
                    CraftPresence.awaitingReply = false;
                    CraftPresence.CLIENT.REQUESTER_USER = null;
                    CraftPresence.CLIENT.JOIN_SECRET = null;
                    CraftPresence.CLIENT.PARTY_ID = null;
                    CraftPresence.CLIENT.PARTY_SIZE = 0;
                    CraftPresence.CLIENT.PARTY_MAX = 0;
                    CraftPresence.SYSTEM.TIMER = 0;
                    CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
                }

                if (property.equals(NAME_biomeMessages) && biomeMessages != null) {
                    boolean defaultFound = !StringUtils.isNullOrEmpty(StringUtils.getConfigPart(biomeMessages, "default", 0, 1, splitCharacter, null));
                    if (!defaultFound) {
                        ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.defaultmissing", property));
                        biomeMessages = StringUtils.addToArray(biomeMessages, biomeMessages.length, "default" + splitCharacter + "Playing in &biome&");
                        properties.setProperty(property, Arrays.toString(biomeMessages));
                        save();
                    }
                }
                if (property.equals(NAME_dimensionMessages) && dimensionMessages != null) {
                    boolean defaultFound = !StringUtils.isNullOrEmpty(StringUtils.getConfigPart(dimensionMessages, "default", 0, 1, splitCharacter, null));
                    if (!defaultFound) {
                        ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.defaultmissing", property));
                        dimensionMessages = StringUtils.addToArray(dimensionMessages, dimensionMessages.length, "default" + splitCharacter + "In The &dimension&");
                        properties.setProperty(property, Arrays.toString(dimensionMessages));
                        save();
                    }
                }
                if (property.equals(NAME_serverMessages) && serverMessages != null) {
                    boolean defaultFound = !StringUtils.isNullOrEmpty(StringUtils.getConfigPart(serverMessages, "default", 0, 1, splitCharacter, null));
                    if (!defaultFound) {
                        ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.defaultmissing", property));
                        serverMessages = StringUtils.addToArray(serverMessages, serverMessages.length, "default" + splitCharacter + "Playing on &motd&");
                        properties.setProperty(property, Arrays.toString(serverMessages));
                        save();
                    }
                }
                if (property.equals(NAME_guiMessages) && guiMessages != null) {
                    boolean defaultFound = !StringUtils.isNullOrEmpty(StringUtils.getConfigPart(guiMessages, "default", 0, 1, splitCharacter, null));
                    if (!defaultFound) {
                        ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.defaultmissing", property));
                        guiMessages = StringUtils.addToArray(guiMessages, guiMessages.length, "default" + splitCharacter + "In &gui&");
                        properties.setProperty(property, Arrays.toString(guiMessages));
                        save();
                    }
                }
                if (property.equals(NAME_itemMessages) && itemMessages != null) {
                    boolean defaultFound = !StringUtils.isNullOrEmpty(StringUtils.getConfigPart(itemMessages, "default", 0, 1, splitCharacter, null));
                    if (!defaultFound) {
                        ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.defaultmissing", property));
                        itemMessages = StringUtils.addToArray(itemMessages, itemMessages.length, "default" + splitCharacter + "Holding &main&");
                        properties.setProperty(property, Arrays.toString(itemMessages));
                        save();
                    }
                }
            }
        }

        if (!properties.stringPropertyNames().isEmpty() && !StringUtils.isNullOrEmpty(queuedSplitCharacter)) {
            // Transfer Split Character, if needed and able to
            for (String propertyName : configPropertyMappings.values()) {
                if (properties.stringPropertyNames().contains(propertyName) && properties.getProperty(propertyName).contains(splitCharacter)) {
                    properties.setProperty(propertyName, properties.getProperty(propertyName).replace(splitCharacter, queuedSplitCharacter));
                    save();
                }
            }

            needsFullUpdate = true;
            queuedSplitCharacter = null;
        }

        if (needsFullUpdate) {
            setupInitialValues();
            verified = false;
            if (!properties.stringPropertyNames().isEmpty()) {
                read(true);
            }
            updateConfig();
        } else {
            verified = true;
        }
    }

    public void save() {
        Writer configWriter = null;
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(configFile);
            configWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            properties.store(configWriter, null);
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.save"));
            ex.printStackTrace();
        }

        try {
            if (configWriter != null) {
                configWriter.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.dataclose"));
            ex.printStackTrace();
        }
    }
}