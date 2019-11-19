package com.gitlab.cdagaming.craftpresence.config;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.Tuple;
import com.google.common.collect.Lists;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigUtils {
    // Config Names
    // GENERAL
    public String NAME_detectCurseManifest, NAME_detectMultiMCManifest, NAME_detectMCUpdaterInstance, NAME_detectTechnicPack,
            NAME_showTime, NAME_showCurrentBiome, NAME_showCurrentDimension,
            NAME_showGameState, NAME_clientID, NAME_defaultIcon, NAME_enableJoinRequest;
    // BIOME MESSAGES
    public String NAME_defaultBiomeIcon, NAME_biomeMessages;
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
    public String NAME_tooltipBGColor, NAME_tooltipBorderColor, NAME_guiBGColor, NAME_languageID, NAME_stripTranslationColors, NAME_showLoggingInChat, NAME_configKeyCode;
    // DISPLAY MESSAGES
    public String NAME_gameStateMSG, NAME_detailsMSG, NAME_largeImageMSG, NAME_smallImageMSG, NAME_largeImageKey, NAME_smallImageKey;

    // Config Variables
    // GENERAL
    public boolean detectCurseManifest, detectMultiMCManifest, detectMCUpdaterInstance, detectTechnicPack, showTime,
            showCurrentBiome, showCurrentDimension, showGameState, enableJoinRequest;
    public String clientID, defaultIcon;
    // BIOME MESSAGES
    public String defaultBiomeIcon;
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
    // DISPLAY MESSAGES
    public String gameStateMSG, detailsMSG, largeImageMSG, smallImageMSG, largeImageKey, smallImageKey;

    // CLASS-SPECIFIC - PUBLIC
    public boolean hasChanged = false, hasClientPropertiesChanged = false;
    public String queuedSplitCharacter;
    public File configFile, parentDir;
    public Properties properties = new Properties();

    // CLASS-SPECIFIC - PRIVATE

    // Mappings:
    // Config Data = Tuple<propertyValue, value>
    // Config Property = Tuple<propertyFieldName, valueFieldName>
    private List<Tuple<String, String>> configPropertyMappings = Lists.newArrayList();
    private List<Tuple<String, Object>> configDataMappings = Lists.newArrayList();

    private String fileName;
    private boolean initialized = false, isConfigNew = false;

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
        NAME_defaultBiomeIcon = ModUtils.TRANSLATOR.translate(true, "gui.config.name.biomemessages.biomeicon").replaceAll(" ", "_");
        NAME_biomeMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.biomemessages.biomemessages").replaceAll(" ", "_");
        defaultBiomeIcon = "plains";
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
        NAME_configKeyCode = ModUtils.TRANSLATOR.translate(true, "key.craftpresence.config_keybind").replaceAll(" ", "_");
        tooltipBGColor = "-267386864";
        tooltipBorderColor = "1347420415";
        guiBGColor = "minecraft" + splitCharacter + "textures/gui/options_background.png";
        languageID = "en_US";
        stripTranslationColors = false;
        showLoggingInChat = false;
        configKeyCode = Integer.toString(Keyboard.KEY_GRAVE);
        // DISPLAY MESSAGES
        NAME_gameStateMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.display.gamestatemsg").replaceAll(" ", "_");
        NAME_detailsMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.display.detailsmsg").replaceAll(" ", "_");
        NAME_largeImageMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.display.largeimagemsg").replaceAll(" ", "_");
        NAME_smallImageMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.display.smallimagemsg").replaceAll(" ", "_");
        NAME_largeImageKey = ModUtils.TRANSLATOR.translate(true, "gui.config.name.display.largeimagekey").replaceAll(" ", "_");
        NAME_smallImageKey = ModUtils.TRANSLATOR.translate(true, "gui.config.name.display.smallimagekey").replaceAll(" ", "_");
        gameStateMSG = "&SERVER& &PACK&";
        detailsMSG = "&MAINMENU&&DIMENSION&";
        largeImageMSG = "&MAINMENU&&DIMENSION&";
        smallImageMSG = "&SERVER&";
        largeImageKey = "&MAINMENU&&DIMENSION&";
        smallImageKey = "&SERVER&&PACK&";

        syncMappings();
        initialized = true;
    }

    private void syncMappings() {
        // Ensure Data is Cleared
        configDataMappings.clear();
        configPropertyMappings.clear();

        // Add Data to Mappings
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getName().startsWith("NAME_")) {
                try {
                    Field valueField = getClass().getField(field.getName().replaceFirst("NAME_", ""));

                    field.setAccessible(true);
                    valueField.setAccessible(true);

                    configDataMappings.add(new Tuple<>(field.get(this).toString(), valueField.get(this)));
                    configPropertyMappings.add(new Tuple<>(field.getName(), valueField.getName()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
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

        try {
            inputStream = new FileInputStream(configFile);
            configReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            properties.load(configReader);
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.save"));
            ex.printStackTrace();
        } finally {
            int currentIndex = 0;
            final List<String> propertyList = Lists.newArrayList(properties.stringPropertyNames());

            for (Tuple<String, String> configProperty : configPropertyMappings) {
                Object fieldObject = null, foundProperty;
                Class<?> expectedClass = configDataMappings.get(currentIndex).getSecond().getClass();

                if (propertyList.contains(configDataMappings.get(currentIndex).getFirst())) {
                    propertyList.remove(configDataMappings.get(currentIndex).getFirst());
                    foundProperty = properties.get(configDataMappings.get(currentIndex).getFirst());

                    try {
                        // Case 1: Attempt to Automatically Cast to Expected Variable
                        // *This will likely only work for strings or simplistic conversions*
                        fieldObject = expectedClass.cast(foundProperty);

                        // If null or toString is null, throw Exception to reset value to Prior/Default Value
                        if (fieldObject == null || StringUtils.isNullOrEmpty(fieldObject.toString())) {
                            throw new IllegalArgumentException(ModUtils.TRANSLATOR.translate(true, "craftpresence.exception.config.nullprop", configDataMappings.get(currentIndex).getFirst()));
                        }
                    } catch (Exception ex) {
                        // Case 2: Manually Convert Variable based on Expected Type
                        if ((expectedClass == boolean.class || expectedClass == Boolean.class) &&
                                StringUtils.isValidBoolean(foundProperty.toString())) {
                            // Convert to Boolean if Valid
                            fieldObject = Boolean.parseBoolean(foundProperty.toString());
                        } else if ((expectedClass == int.class || expectedClass == Integer.class) &&
                                StringUtils.getValidInteger(foundProperty.toString()).getFirst()) {
                            // Convert to Integer if Valid
                            fieldObject = StringUtils.getValidInteger(foundProperty.toString()).getSecond();
                        } else if (expectedClass == String[].class) {
                            // Convert to String Array (After Verifying it is a single Array)
                            final String convertedString = StringUtils.removeMatches(StringUtils.getMatches("^\\[([^\\s]+?)\\]", foundProperty.toString()), 1, true);

                            if (!StringUtils.isNullOrEmpty(convertedString) &&
                                    (convertedString.startsWith("\\[") && convertedString.endsWith("]")) &&
                                    convertedString.contains(", ")) {
                                fieldObject = foundProperty.toString()
                                        .replaceAll("\\[", "").replaceAll("]", "")
                                        .split(", ");
                            }
                        } else {
                            // If not a Convertible Type, Revert Value to prior Data
                            if (!skipLogging) {
                                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.emptyprop", configDataMappings.get(currentIndex).getFirst()));
                            }
                            fieldObject = configDataMappings.get(currentIndex).getSecond();
                        }
                    } finally {
                        if (fieldObject != null) {
                            StringUtils.updateField(getClass(), CraftPresence.CONFIG, new Tuple<>(configProperty.getSecond(), fieldObject));
                        }
                    }
                }
                currentIndex++;
            }

            for (String remainingProp : propertyList) {
                // Removes any Invalid Properties, that were not checked off during read
                if (!skipLogging) {
                    ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.invalidprop", remainingProp));
                }
                properties.remove(remainingProp);
                save();
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
        } finally {
            if (!skipLogging) {
                if (isConfigNew) {
                    ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.info.config.new"));
                } else {
                    ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.info.config.save"));
                }
            }
        }
    }

    public void updateConfig() {
        // Track if in need of a data re-sync
        boolean needsDataSync = false;

        // Sync Edits from Read Events that may have occurred
        syncMappings();

        for (Tuple<String, Object> configDataSet : configDataMappings) {
            Class<?> expectedClass = configDataSet.getSecond().getClass();
            String finalOutput;

            try {
                if (expectedClass == String[].class) {
                    // Save as String Array, after ensuring default argument exists

                    // Ensure default Value for String Array is available
                    // If default value is not present, give it a dummy value
                    String[] finalArray = (String[]) configDataSet.getSecond();
                    boolean defaultFound = !StringUtils.isNullOrEmpty(StringUtils.getConfigPart(finalArray, "default", 0, 1, splitCharacter, null));
                    if (!defaultFound) {
                        ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.defaultmissing", configDataSet.getFirst()));
                        finalArray = StringUtils.addToArray(finalArray, finalArray.length, "default" + splitCharacter + "null");
                        needsDataSync = true;
                    }

                    finalOutput = Arrays.toString(finalArray);
                } else {
                    // If not a Convertible Type, Attempt Auto Conversion
                    finalOutput = configDataSet.getSecond().toString();
                }

                // Replace Split Character if needed
                if (!StringUtils.isNullOrEmpty(queuedSplitCharacter) && finalOutput.contains(splitCharacter)) {
                    finalOutput = finalOutput.replace(splitCharacter, queuedSplitCharacter);
                    needsDataSync = true;
                }

                // Save Final Output Value in Properties
                properties.setProperty(configDataSet.getFirst(), finalOutput);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Save Queued Split Character, if any
        splitCharacter = queuedSplitCharacter;
        queuedSplitCharacter = null;

        if (needsDataSync) {
            read(true);
        } else {
            save();
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