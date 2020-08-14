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

package com.gitlab.cdagaming.craftpresence.config;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.KeyConverter;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.TranslationUtils;
import com.google.common.collect.Lists;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Properties and Configuration Data for CraftPresence
 *
 * @author CDAGaming
 */
public class ConfigUtils {
    // CONSTANTS
    private final String[] blackListedCharacters = new String[]{",", "[", "]"},
            keyCodeTriggers = new String[]{"keycode", "keybind", "keybinding"},
            languageTriggers = new String[]{"language", "lang", "langId", "languageId"},
            globalTriggers = new String[]{"global", "last", "schema"};
    // Mappings:
    // Config Data = Tuple<propertyValue, value>
    // Config Property = Tuple<propertyFieldName, valueFieldName>
    private final List<Tuple<String, String>> configPropertyMappings = Lists.newArrayList();
    public final List<Tuple<String, Object>> configDataMappings = Lists.newArrayList();
    private final String fileName;
    // Config Names
    // GLOBAL (NON-USER-ADJUSTABLE)
    public String NAME_schemaVersion, NAME_lastMcVersionId;
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
    public String NAME_mainmenuMSG, NAME_loadingMSG, NAME_lanMSG, NAME_singleplayerMSG, NAME_packPlaceholderMSG,
            NAME_outerPlayerPlaceholderMSG, NAME_innerPlayerPlaceholderMSG, NAME_playerCoordinatePlaceholderMSG, NAME_playerHealthPlaceholderMSG,
            NAME_playerAmountPlaceholderMSG, NAME_playerItemsPlaceholderMSG, NAME_worldPlaceholderMSG, NAME_modsPlaceholderMSG, NAME_vivecraftMessage, NAME_fallbackPackPlaceholderMSG;
    // ADVANCED
    public String NAME_enableCommands, NAME_enablePERGUI, NAME_enablePERItem, NAME_enablePEREntity, NAME_renderTooltips, NAME_formatWords, NAME_debugMode, NAME_verboseMode,
            NAME_splitCharacter, NAME_refreshRate, NAME_guiMessages, NAME_itemMessages, NAME_entityTargetMessages, NAME_entityAttackingMessages, NAME_entityRidingMessages;
    // ACCESSIBILITY
    public String NAME_tooltipBGColor, NAME_tooltipBorderColor, NAME_guiBGColor, NAME_languageID, NAME_stripTranslationColors, NAME_showLoggingInChat, NAME_configKeyCode;
    // DISPLAY MESSAGES
    public String NAME_gameStateMSG, NAME_detailsMSG, NAME_largeImageMSG, NAME_smallImageMSG, NAME_largeImageKey, NAME_smallImageKey;
    // Config Variables
    // GLOBAL (NON-USER-ADJUSTABLE)
    public String schemaVersion, lastMcVersionId;
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
    public String mainmenuMSG, loadingMSG, lanMSG, singleplayerMSG, packPlaceholderMSG,
            outerPlayerPlaceholderMSG, innerPlayerPlaceholderMSG, playerCoordinatePlaceholderMSG, playerHealthPlaceholderMSG,
            playerAmountPlaceholderMSG, playerItemsPlaceholderMSG, worldPlaceholderMSG, modsPlaceholderMSG, vivecraftMessage, fallbackPackPlaceholderMSG;
    // ADVANCED
    public boolean enableCommands, enablePERGUI, enablePERItem, enablePEREntity, renderTooltips, formatWords, debugMode, verboseMode;
    public String splitCharacter;
    public int refreshRate;
    public String[] guiMessages, itemMessages, entityTargetMessages, entityAttackingMessages, entityRidingMessages;
    // ACCESSIBILITY
    public String tooltipBGColor, tooltipBorderColor, guiBGColor, languageID;
    public int configKeyCode;
    public boolean stripTranslationColors, showLoggingInChat;
    // DISPLAY MESSAGES
    public String gameStateMSG, detailsMSG, largeImageMSG, smallImageMSG, largeImageKey, smallImageKey;
    // CLASS-SPECIFIC - PUBLIC
    public boolean hasChanged = false, hasClientPropertiesChanged = false;

    // CLASS-SPECIFIC - PRIVATE
    public String queuedSplitCharacter;
    public File configFile, parentDir;
    public Properties properties = new Properties();
    private boolean initialized = false, isConfigNew = false;

    /**
     * Initializes a New Instance with the Specified Data
     *
     * @param fileName The filename to write this Configuration as
     */
    public ConfigUtils(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Setup of Initial Values and Names of Config Data
     */
    public void setupInitialValues() {
        // GLOBAL (NON-USER-ADJUSTABLE)
        NAME_schemaVersion = ModUtils.TRANSLATOR.translate(true, "gui.config.name.global.schemaversion").replaceAll(" ", "_");
        schemaVersion = Integer.toString(ModUtils.MOD_SCHEMA_VERSION);
        NAME_lastMcVersionId = ModUtils.TRANSLATOR.translate(true, "gui.config.name.global.lastmcversionid").replaceAll(" ", "_");
        lastMcVersionId = Integer.toString(ModUtils.MCProtocolID);
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
        dimensionMessages = new String[]{"default" + (!StringUtils.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + "In the &dimension&"};
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
        NAME_loadingMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.loadingmsg").replaceAll(" ", "_");
        NAME_lanMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.lanmsg").replaceAll(" ", "_");
        NAME_singleplayerMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.singleplayermsg").replaceAll(" ", "_");
        NAME_packPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.placeholder.packmsg").replaceAll(" ", "_");
        NAME_outerPlayerPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.placeholder.playermsg.out").replaceAll(" ", "_");
        NAME_innerPlayerPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.placeholder.playermsg.in").replaceAll(" ", "_");
        NAME_playerCoordinatePlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.placeholder.playercoordinatemsg").replaceAll(" ", "_");
        NAME_playerHealthPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.placeholder.playerhealthmsg").replaceAll(" ", "_");
        NAME_playerAmountPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.placeholder.playeramountmsg").replaceAll(" ", "_");
        NAME_playerItemsPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.placeholder.playeritemmsg").replaceAll(" ", "_");
        NAME_worldPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.placeholder.worldmsg").replaceAll(" ", "_");
        NAME_modsPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.placeholder.modsmsg").replaceAll(" ", "_");
        NAME_vivecraftMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.special.vivecraftmsg").replaceAll(" ", "_");
        NAME_fallbackPackPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "gui.config.name.statusmessages.fallback.packplaceholdermsg").replaceAll(" ", "_");
        mainmenuMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.state.mainmenu");
        loadingMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.state.loading");
        lanMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.state.lan");
        singleplayerMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.state.singleplayer");
        packPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.pack");
        outerPlayerPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.playerinfo.out");
        innerPlayerPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.playerinfo.in");
        playerCoordinatePlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.playerinfo.coordinate");
        playerHealthPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.playerinfo.health");
        playerAmountPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.players");
        playerItemsPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.playerinfo.items");
        worldPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.worldinfo");
        modsPlaceholderMSG = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.mods");
        vivecraftMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.special.vivecraft");
        fallbackPackPlaceholderMSG = "";
        // ADVANCED
        NAME_enableCommands = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.enablecommands").replaceAll(" ", "_");
        NAME_enablePERGUI = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.enablepergui").replaceAll(" ", "_");
        NAME_enablePERItem = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.enableperitem").replaceAll(" ", "_");
        NAME_enablePEREntity = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.enableperentity").replaceAll(" ", "_");
        NAME_renderTooltips = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.rendertooltips").replaceAll(" ", "_");
        NAME_formatWords = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.formatwords").replaceAll(" ", "_");
        NAME_debugMode = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.debugmode").replaceAll(" ", "_");
        NAME_verboseMode = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.verbosemode").replaceAll(" ", "_");
        NAME_splitCharacter = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.splitcharacter").replaceAll(" ", "_");
        NAME_refreshRate = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.refreshrate").replaceAll(" ", "_");
        NAME_guiMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.guimessages").replaceAll(" ", "_");
        NAME_itemMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.itemmessages").replaceAll(" ", "_");
        NAME_entityTargetMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.entitytargetmessages").replaceAll(" ", "_");
        NAME_entityAttackingMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.entityattackingmessages").replaceAll(" ", "_");
        NAME_entityRidingMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.entityridingmessages").replaceAll(" ", "_");
        enableCommands = true;
        enablePERGUI = false;
        enablePERItem = false;
        enablePEREntity = false;
        renderTooltips = true;
        formatWords = true;
        debugMode = false;
        verboseMode = false;
        splitCharacter = ";";
        refreshRate = 2;
        guiMessages = new String[]{"default;In &gui&"};
        itemMessages = new String[]{"default;Holding &item&"};
        entityTargetMessages = new String[]{"default;Targeting &entity&"};
        entityAttackingMessages = new String[]{"default;Attacking &entity&"};
        entityRidingMessages = new String[]{"default;Riding &entity&"};
        // ACCESSIBILITY
        NAME_tooltipBGColor = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.tooltipbgcolor").replaceAll(" ", "_");
        NAME_tooltipBorderColor = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.tooltipbordercolor").replaceAll(" ", "_");
        NAME_guiBGColor = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.guibgcolor").replaceAll(" ", "_");
        NAME_languageID = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.languageid").replaceAll(" ", "_");
        NAME_stripTranslationColors = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.striptranslationcolors").replaceAll(" ", "_");
        NAME_showLoggingInChat = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.showlogginginchat").replaceAll(" ", "_");
        NAME_configKeyCode = ModUtils.TRANSLATOR.translate(true, "key.craftpresence.config_keybind.name").replaceAll(" ", "_");
        tooltipBGColor = "0xF0100010";
        tooltipBorderColor = "0x505000FF";
        guiBGColor = "minecraft" + splitCharacter + (ModUtils.IS_LEGACY ? "/gui/background.png" : "textures/gui/options_background.png");
        languageID = ModUtils.MCProtocolID >= 315 ? "en_us" : "en_US";
        stripTranslationColors = false;
        showLoggingInChat = false;
        configKeyCode = ModUtils.MCProtocolID > 340 ? 96 : 41;
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
        smallImageMSG = "&SERVER& &PACK&";
        largeImageKey = "&MAINMENU&&DIMENSION&";
        smallImageKey = "&SERVER&&PACK&";

        syncMappings();
        initialized = true;
    }

    /**
     * Synchronizes Config Mappings to the Currently available Field Data
     */
    private void syncMappings() {
        // Ensure Data is Cleared
        configDataMappings.clear();
        configPropertyMappings.clear();

        // Add Data to Mappings (Order-Reliant; Ensure Global Variables are first)
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getName().startsWith("NAME_")) {
                try {
                    final Field valueField = getClass().getField(field.getName().replaceFirst("NAME_", ""));

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

    /**
     * Initializes Config Data, in preparation for sync/read operations
     */
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
                read(false, "UTF-8");
            }
        }
    }

    /**
     * Reads available Config Data from the {@link ConfigUtils#configFile}
     * <p>Also ensures validity for the properties detected
     *
     * @param skipLogging Whether or not Logging should be skipped
     * @param encoding    The Encoding for the {@link ConfigUtils#configFile}
     */
    public void read(final boolean skipLogging, final String encoding) {
        Reader configReader = null;
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(configFile);
            configReader = new InputStreamReader(inputStream, Charset.forName(encoding));
            properties.load(configReader);
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.save"));
            ex.printStackTrace();
        } finally {
            int currentIndex = 0;
            final List<String> propertyList = Lists.newArrayList(properties.stringPropertyNames());

            // Format: ListOfMigrationTargets:MigrationId
            final List<Tuple<List<String>, String>> migrationData = Lists.newArrayList();

            for (Tuple<String, String> configProperty : configPropertyMappings) {
                Object fieldObject = null, foundProperty;
                final String propertyName = configDataMappings.get(currentIndex).getFirst();
                final Object defaultValue = configDataMappings.get(currentIndex).getSecond();
                final Class<?> expectedClass = configDataMappings.get(currentIndex).getSecond().getClass();

                if (propertyList.contains(propertyName)) {
                    propertyList.remove(propertyName);
                    foundProperty = properties.get(propertyName);

                    try {
                        // Case 1: Attempt to Automatically Cast to Expected Variable
                        // Note: This will likely only work for strings or simplistic conversions
                        fieldObject = expectedClass.cast(foundProperty);

                        // If null or toString is null (And the default value of the property is not empty)
                        // Throw an Exception to reset value to Prior/Default Value
                        if (!StringUtils.isNullOrEmpty(defaultValue.toString()) && (fieldObject == null || StringUtils.isNullOrEmpty(fieldObject.toString()))) {
                            throw new IllegalArgumentException(ModUtils.TRANSLATOR.translate(true, "craftpresence.exception.config.nullprop", propertyName));
                        }
                    } catch (Exception ex) {
                        // Case 2: Manually Convert Variable based on Expected Type
                        if ((expectedClass == boolean.class || expectedClass == Boolean.class) &&
                                StringUtils.isValidBoolean(foundProperty)) {
                            // Convert to Boolean if Valid
                            fieldObject = Boolean.parseBoolean(foundProperty.toString());
                        } else if ((expectedClass == int.class || expectedClass == Integer.class) &&
                                StringUtils.getValidInteger(foundProperty).getFirst()) {
                            // Convert to Integer if Valid, and if not reset it
                            final Tuple<Boolean, Integer> boolData = StringUtils.getValidInteger(foundProperty);

                            if (boolData.getFirst()) {
                                // This check will trigger if the Field Name contains KeyCode Triggers
                                // If the Property Name contains these values, move onwards
                                for (String keyTrigger : keyCodeTriggers) {
                                    if (configProperty.getSecond().toLowerCase().contains(keyTrigger.toLowerCase())) {
                                        if (!CraftPresence.KEYBINDINGS.isValidKeyCode(boolData.getSecond())) {
                                            // If not a valid KeyCode, Revert Value to prior Data
                                            if (!skipLogging) {
                                                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.emptyprop", propertyName));
                                            }
                                            fieldObject = defaultValue;
                                        } else {
                                            // If so, iterate through the migration data allocated earlier
                                            // to see if the property needs any data migrations
                                            for (Tuple<List<String>, String> migrationChunk : migrationData) {
                                                if (migrationChunk.getFirst().contains(keyTrigger.toLowerCase()) && !migrationChunk.getSecond().equalsIgnoreCase(KeyConverter.ConversionMode.Unknown.name())) {
                                                    // If so, retrieve the second part of the migration data,
                                                    // and adjust the property accordingly with the mode it should use
                                                    final int migratedKeyCode = KeyConverter.convertKey(boolData.getSecond(), KeyConverter.ConversionMode.valueOf(migrationChunk.getSecond()));
                                                    if (!skipLogging && migratedKeyCode != boolData.getSecond()) {
                                                        ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.info.migration.apply", migrationChunk.getFirst().toString(), migrationChunk.getSecond(), propertyName, boolData.getSecond(), migratedKeyCode));
                                                    }
                                                    fieldObject = migratedKeyCode;
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }

                                if (fieldObject == null) {
                                    fieldObject = boolData.getSecond();
                                }
                            } else {
                                // If not a valid Integer, Revert Value to prior Data
                                if (!skipLogging) {
                                    ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.emptyprop", propertyName));
                                }
                                fieldObject = defaultValue;
                            }
                        } else if (expectedClass == String[].class) {
                            // Convert to String Array (After Verifying it is a single Array)
                            final String convertedString = StringUtils.removeMatches(StringUtils.getMatches("\\[([^\\s]+?)\\]", foundProperty), null, 1);

                            if (!StringUtils.isNullOrEmpty(convertedString) &&
                                    (convertedString.startsWith("[") && convertedString.endsWith("]"))) {
                                // If Valid, interpret into formatted Array
                                final String preArrayString = convertedString.replaceAll("\\[", "").replaceAll("]", "");
                                if (preArrayString.contains(", ")) {
                                    fieldObject = preArrayString.split(", ");
                                } else if (preArrayString.contains(",")) {
                                    fieldObject = preArrayString.split(",");
                                } else {
                                    fieldObject = new String[]{preArrayString};
                                }
                            }
                        } else {
                            // If not a Convertible Type, Revert Value to prior Data
                            if (!skipLogging) {
                                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.emptyprop", propertyName));
                            }
                            fieldObject = defaultValue;
                        }
                    } finally {
                        if (fieldObject != null) {
                            fieldObject = syncMigrationData(skipLogging, migrationData, configProperty, fieldObject, foundProperty, propertyName, defaultValue);
                            StringUtils.updateField(getClass(), CraftPresence.CONFIG, new Tuple<>(configProperty.getSecond(), fieldObject));
                        }
                    }
                } else {
                    // If a Config Variable is not present in the Properties File, queue a Config Update
                    if (!skipLogging) {
                        ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.config.emptyprop", propertyName));
                    }
                }
                currentIndex++;
            }

            for (String remainingProp : propertyList) {
                // Removes any Invalid Properties, that were not checked off during read
                if (!skipLogging) {
                    ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.invalidprop", remainingProp));
                }
                properties.remove(remainingProp);
                save("UTF-8");
            }

            // Execute a Config Update, to ensure validity
            updateConfig();
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

    /**
     * Synchronizes and Checks for any Migration Data, in relation to the fieldObject and other Settings
     *
     * @param skipLogging    Whether or not Logging should be skipped
     * @param migrationData  Mapping for any Existing Migration Data being tracked (Format:
     * @param configProperty The Field Name of the Property to Check for potential changes/migrations
     * @param fieldObject    The Value of the Property to Check for potential changes/migrations
     * @param foundProperty  The original Value of the property to migrate/verify if needed
     * @param propertyName   The Name of the Property to migrate/verify if needed
     * @param defaultValue   The Default Value of the Property to Check against fieldObject
     * @return The Checked/Verified Field Object, depending on method execution
     */
    private Object syncMigrationData(boolean skipLogging, List<Tuple<List<String>, String>> migrationData, Tuple<String, String> configProperty, Object fieldObject, Object foundProperty, String propertyName, Object defaultValue) {
        // Move through any triggers or Migration Data, if needed
        // Before proceeding to final parsing
        for (String globalTrigger : globalTriggers) {
            if (configProperty.getSecond().toLowerCase().contains(globalTrigger.toLowerCase())) {
                // If the variable if Global, check and see if it is different from it's default value
                // In some cases, additional migrations may also be needed, in which case data is added to the list
                if (!skipLogging && !fieldObject.toString().equals(defaultValue.toString())) {
                    ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.globaladjust", propertyName));
                }

                if (propertyName.equals(NAME_lastMcVersionId)) {
                    int currentParseValue = -1, defaultParseValue = Integer.parseInt(defaultValue.toString());
                    try {
                        currentParseValue = Integer.parseInt(fieldObject.toString());
                    } catch (Exception | Error ex) {
                        if (ModUtils.IS_VERBOSE) {
                            ex.printStackTrace();
                        }
                    }

                    String keyCodeMigrationId = KeyConverter.ConversionMode.Unknown.name(), languageMigrationId = TranslationUtils.ConversionMode.Unknown.name();
                    if (currentParseValue <= 340 && defaultParseValue > 340) {
                        keyCodeMigrationId = KeyConverter.ConversionMode.Lwjgl3.name();
                    } else if (currentParseValue > 340 && defaultParseValue <= 340) {
                        keyCodeMigrationId = KeyConverter.ConversionMode.Lwjgl2.name();
                    } else if (currentParseValue >= 0 && defaultParseValue >= 0) {
                        keyCodeMigrationId = KeyConverter.ConversionMode.None.name();
                    }

                    if (currentParseValue < 315 && defaultParseValue >= 315) {
                        languageMigrationId = TranslationUtils.ConversionMode.PackFormat3.name();
                    } else if (currentParseValue >= 315 && defaultParseValue < 315) {
                        languageMigrationId = TranslationUtils.ConversionMode.PackFormat2.name();
                    } else if (currentParseValue >= 0 && defaultParseValue >= 0) {
                        languageMigrationId = TranslationUtils.ConversionMode.None.name();
                    }

                    if (!skipLogging) {
                        ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.info.migration.add", Arrays.asList(keyCodeTriggers).toString(), keyCodeMigrationId, keyCodeMigrationId.equals(KeyConverter.ConversionMode.None.name()) ? "Verification" : "Setting Change"));
                        ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.info.migration.add", Arrays.asList(languageTriggers).toString(), languageMigrationId, languageMigrationId.equals(TranslationUtils.ConversionMode.None.name()) ? "Verification" : "Setting Change"));
                    }

                    // Global Case 1 Notes (KeyCode):
                    // In this situation, if the currently parsed protocol version differs and
                    // is a newer version then 1.12.2 (340), then
                    // we need to ensure any keycodes are in an LWJGL 3 format
                    // Otherwise, if using a config from above 1.12.2 (340) on it or anything lower,
                    // we need to ensure any keycodes are in an LWJGL 2 format.
                    // If neither is true, then we mark the migration data as None, and it will be verified
                    migrationData.add(new Tuple<>(Arrays.asList(keyCodeTriggers), keyCodeMigrationId));
                    // Normal Case 1 Notes (Language ID):
                    // In this situation, if the currently parsed protocol version differs and
                    // is a newer version then or exactly 1.11 (315), then
                    // we need to ensure any Language Locale's are complying with Pack Format 3 and above
                    // Otherwise, if using a config from anything less then 1.11 (315),
                    // we need to ensure any Language Locale's are complying with Pack Format 2 and below
                    // If neither is true, then we mark the migration data as None, and it will be verified
                    migrationData.add(new Tuple<>(Arrays.asList(languageTriggers), languageMigrationId));
                }
                fieldObject = defaultValue;
                break;
            }
        }

        // This check will trigger if the Field Name contains Language Identifier Triggers
        // If the Property Name contains these values, move onwards
        for (String langTrigger : languageTriggers) {
            if (configProperty.getSecond().toLowerCase().contains(langTrigger.toLowerCase())) {
                // If so, iterate through the migration data allocated earlier
                // to see if the property needs any data migrations
                for (Tuple<List<String>, String> migrationChunk : migrationData) {
                    if (migrationChunk.getFirst().contains(langTrigger.toLowerCase()) && !migrationChunk.getSecond().equalsIgnoreCase(TranslationUtils.ConversionMode.Unknown.name())) {
                        // If so, retrieve the second part of the migration data,
                        // and adjust the property accordingly with the mode it should use
                        final String migratedLanguageId = TranslationUtils.convertId(foundProperty.toString(), TranslationUtils.ConversionMode.valueOf(migrationChunk.getSecond()));
                        if (!skipLogging && !migratedLanguageId.equals(foundProperty.toString())) {
                            ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.info.migration.apply", migrationChunk.getFirst().toString(), migrationChunk.getSecond(), propertyName, foundProperty.toString(), migratedLanguageId));
                        }
                        fieldObject = migratedLanguageId;
                        break;
                    }
                }
                break;
            }
        }
        return fieldObject;
    }

    /**
     * Aligns and Updates the {@link ConfigUtils#properties} with the data from {@link ConfigUtils#configDataMappings}
     */
    public void updateConfig() {
        // Track if in need of a data re-sync
        boolean needsDataSync = false;

        // Sync Edits from Read Events that may have occurred
        syncMappings();

        for (Tuple<String, Object> configDataSet : configDataMappings) {
            final Class<?> expectedClass = configDataSet.getSecond().getClass();
            String finalOutput;

            try {
                if (expectedClass == String[].class) {
                    // Save as String Array, after ensuring default argument exists
                    String[] finalArray = (String[]) configDataSet.getSecond();

                    // Ensure default Value for String Array is available
                    // If default value is not present, give it a dummy value
                    boolean defaultFound = !StringUtils.isNullOrEmpty(StringUtils.getConfigPart(finalArray, "default", 0, 1, splitCharacter, null));
                    if (!defaultFound) {
                        ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.defaultmissing", configDataSet.getFirst()));
                        finalArray = StringUtils.addToArray(finalArray, finalArray.length, "default" + splitCharacter + "NaN");
                        needsDataSync = true;
                    }

                    finalOutput = Arrays.toString(finalArray);
                } else {
                    // If not a Convertible Type, Attempt Auto Conversion
                    finalOutput = configDataSet.getSecond().toString();
                }

                // Replace Split Character if needed, and is not a blacklisted character
                if (!StringUtils.isNullOrEmpty(queuedSplitCharacter) && finalOutput.contains(splitCharacter) && !Arrays.asList(blackListedCharacters).contains(queuedSplitCharacter)) {
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
        if (!StringUtils.isNullOrEmpty(queuedSplitCharacter) && !Arrays.asList(blackListedCharacters).contains(queuedSplitCharacter)) {
            splitCharacter = queuedSplitCharacter;
            queuedSplitCharacter = null;
        }

        save("UTF-8");

        // Re-Sync the Config Mappings and Fields if needed
        if (needsDataSync) {
            read(true, "UTF-8");
        }
    }

    /**
     * Saves the Synchronized {@link ConfigUtils#properties} with the {@link ConfigUtils#configFile}
     *
     * @param encoding The encoding to save the {@link ConfigUtils#configFile} as
     */
    public void save(final String encoding) {
        Writer configWriter = null;
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(configFile);
            configWriter = new OutputStreamWriter(outputStream, Charset.forName(encoding));
            properties.store(configWriter,
                    ModUtils.TRANSLATOR.translate(true, "gui.config.title") + "\n" +
                            ModUtils.TRANSLATOR.translate(true, "gui.config.comment.title", ModUtils.VERSION_ID, ModUtils.MOD_SCHEMA_VERSION) + "\n\n" +
                            ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.info.config.notice")
            );
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