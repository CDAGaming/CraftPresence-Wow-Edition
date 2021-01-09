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

package com.gitlab.cdagaming.craftpresence.config;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.KeyConverter;
import com.gitlab.cdagaming.craftpresence.impl.Pair;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.TranslationUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.PartyPrivacy;
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
    // Config Property Mappings = Pair<propertyFieldName, valueFieldName>
    public final List<Pair<String, Object>> configDataMappings = Lists.newArrayList();
    // CONSTANTS
    private final String[] blackListedCharacters = new String[]{",", "[", "]"},
            keyCodeTriggers = new String[]{"keycode", "keybinding"},
            languageTriggers = new String[]{"language", "lang", "langId", "languageId"},
            globalTriggers = new String[]{"global", "last", "schema"};
    // Config Data Mappings = Pair<propertyValue, value>
    private final List<Pair<String, String>> configPropertyMappings = Lists.newArrayList();
    private final String fileName;
    // Config Names
    // GLOBAL (NON-USER-ADJUSTABLE)
    public String NAME_schemaVersion, NAME_lastMcVersionId;
    // GENERAL
    public String NAME_detectCurseManifest, NAME_detectMultiMCManifest, NAME_detectMCUpdaterInstance, NAME_detectTechnicPack,
            NAME_showTime, NAME_detectBiomeData, NAME_detectDimensionData,
            NAME_detectWorldData, NAME_clientId, NAME_defaultIcon, NAME_enableJoinRequest,
            NAME_partyPrivacyLevel, NAME_resetTimeOnInit, NAME_autoRegister;
    // BIOME MESSAGES
    public String NAME_defaultBiomeIcon, NAME_biomeMessages;
    // DIMENSION MESSAGES
    public String NAME_defaultDimensionIcon, NAME_dimensionMessages;
    // SERVER MESSAGES
    public String NAME_defaultServerIcon, NAME_defaultServerName,
            NAME_defaultServerMotd, NAME_serverMessages;
    // STATUS MESSAGES
    public String NAME_mainMenuMessage, NAME_loadingMessage, NAME_lanMessage, NAME_singlePlayerMessage, NAME_packPlaceholderMessage,
            NAME_outerPlayerPlaceholderMessage, NAME_innerPlayerPlaceholderMessage, NAME_playerCoordinatePlaceholderMessage, NAME_playerHealthPlaceholderMessage,
            NAME_playerAmountPlaceholderMessage, NAME_playerItemsPlaceholderMessage, NAME_worldPlaceholderMessage, NAME_modsPlaceholderMessage, NAME_vivecraftMessage, NAME_fallbackPackPlaceholderMessage;
    // ADVANCED
    public String NAME_enableCommands, NAME_enablePerGui, NAME_enablePerItem, NAME_enablePerEntity, NAME_renderTooltips, NAME_formatWords, NAME_debugMode, NAME_verboseMode,
            NAME_splitCharacter, NAME_refreshRate, NAME_roundSize, NAME_includeExtraGuiClasses, NAME_guiMessages, NAME_itemMessages, NAME_entityTargetMessages, NAME_entityAttackingMessages, NAME_entityRidingMessages;
    // ACCESSIBILITY
    public String NAME_tooltipBackgroundColor, NAME_tooltipBorderColor, NAME_guiBackgroundColor, NAME_buttonBackgroundColor, NAME_showBackgroundAsDark, NAME_languageId, NAME_stripTranslationColors, NAME_showLoggingInChat, NAME_stripExtraGuiElements, NAME_configKeyCode;
    // DISPLAY MESSAGES
    public String NAME_gameStateMessage, NAME_detailsMessage, NAME_largeImageMessage, NAME_smallImageMessage, NAME_largeImageKey, NAME_smallImageKey;
    // Config Variables
    // GLOBAL (NON-USER-ADJUSTABLE)
    public String schemaVersion, lastMcVersionId;
    // GENERAL
    public boolean detectCurseManifest, detectMultiMCManifest, detectMCUpdaterInstance, detectTechnicPack, showTime,
            detectBiomeData, detectDimensionData, detectWorldData, enableJoinRequest, resetTimeOnInit, autoRegister;
    public String clientId, defaultIcon;
    public int partyPrivacyLevel;
    // BIOME MESSAGES
    public String defaultBiomeIcon;
    public String[] biomeMessages;
    // DIMENSION MESSAGES
    public String defaultDimensionIcon;
    public String[] dimensionMessages;
    // SERVER MESSAGES
    public String defaultServerIcon, defaultServerName, defaultServerMotd;
    public String[] serverMessages;
    // STATUS MESSAGES
    public String mainMenuMessage, loadingMessage, lanMessage, singlePlayerMessage, packPlaceholderMessage,
            outerPlayerPlaceholderMessage, innerPlayerPlaceholderMessage, playerCoordinatePlaceholderMessage, playerHealthPlaceholderMessage,
            playerAmountPlaceholderMessage, playerItemsPlaceholderMessage, worldPlaceholderMessage, modsPlaceholderMessage, vivecraftMessage, fallbackPackPlaceholderMessage;
    // ADVANCED
    public boolean enableCommands, enablePerGui, enablePerItem, enablePerEntity, renderTooltips, formatWords, debugMode, verboseMode, includeExtraGuiClasses;
    public String splitCharacter;
    public int refreshRate, roundSize;
    public String[] guiMessages, itemMessages, entityTargetMessages, entityAttackingMessages, entityRidingMessages;
    // ACCESSIBILITY
    public String tooltipBackgroundColor, tooltipBorderColor, guiBackgroundColor, buttonBackgroundColor, languageId;
    public int configKeyCode;
    public boolean showBackgroundAsDark, stripTranslationColors, showLoggingInChat, stripExtraGuiElements;
    // DISPLAY MESSAGES
    public String gameStateMessage, detailsMessage, largeImageMessage, smallImageMessage, largeImageKey, smallImageKey;
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
        NAME_schemaVersion = ModUtils.TRANSLATOR.translate(true, "gui.config.name.global.schema_version").replaceAll(" ", "_");
        schemaVersion = Integer.toString(ModUtils.MOD_SCHEMA_VERSION);
        NAME_lastMcVersionId = ModUtils.TRANSLATOR.translate(true, "gui.config.name.global.last_mc_version_id").replaceAll(" ", "_");
        lastMcVersionId = Integer.toString(ModUtils.MCProtocolID);
        // GENERAL
        NAME_detectCurseManifest = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.detect_curse_manifest").replaceAll(" ", "_");
        NAME_detectMultiMCManifest = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.detect_multimc_manifest").replaceAll(" ", "_");
        NAME_detectMCUpdaterInstance = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.detect_mcupdater_instance").replaceAll(" ", "_");
        NAME_detectTechnicPack = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.detect_technic_pack").replaceAll(" ", "_");
        NAME_showTime = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.show_time").replaceAll(" ", "_");
        NAME_detectBiomeData = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.detect_biome_data").replaceAll(" ", "_");
        NAME_detectDimensionData = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.detect_dimension_data").replaceAll(" ", "_");
        NAME_detectWorldData = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.detect_world_data").replaceAll(" ", "_");
        NAME_clientId = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.client_id").replaceAll(" ", "_");
        NAME_defaultIcon = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.default_icon").replaceAll(" ", "_");
        NAME_enableJoinRequest = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.enable_join_request").replaceAll(" ", "_");
        NAME_partyPrivacyLevel = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.party_privacy").replaceAll(" ", "_");
        NAME_resetTimeOnInit = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.reset_time_on_init").replaceAll(" ", "_");
        NAME_autoRegister = ModUtils.TRANSLATOR.translate(true, "gui.config.name.general.auto_register").replaceAll(" ", "_");
        detectCurseManifest = true;
        detectMultiMCManifest = true;
        detectMCUpdaterInstance = true;
        detectTechnicPack = true;
        showTime = true;
        detectBiomeData = false;
        detectDimensionData = true;
        detectWorldData = true;
        clientId = "450485984333660181";
        defaultIcon = "grass";
        enableJoinRequest = false;
        partyPrivacyLevel = PartyPrivacy.Public.getPartyIndex();
        resetTimeOnInit = false;
        autoRegister = false;
        // BIOME MESSAGES
        NAME_defaultBiomeIcon = ModUtils.TRANSLATOR.translate(true, "gui.config.name.biome_messages.biome_icon".replaceAll(" ", "_"));
        NAME_biomeMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.biome_messages.biome_messages").replaceAll(" ", "_");
        defaultBiomeIcon = "unknown";
        biomeMessages = new String[]{"default" + (!StringUtils.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + "Playing in &biome&"};
        // DIMENSION MESSAGES
        NAME_defaultDimensionIcon = ModUtils.TRANSLATOR.translate(true, "gui.config.name.dimension_messages.dimension_icon").replaceAll(" ", "_");
        NAME_dimensionMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.dimension_messages.dimension_messages").replaceAll(" ", "_");
        defaultDimensionIcon = "unknown";
        dimensionMessages = new String[]{"default" + (!StringUtils.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + "In the &dimension&"};
        // SERVER MESSAGES
        NAME_defaultServerIcon = ModUtils.TRANSLATOR.translate(true, "gui.config.name.server_messages.server_icon").replaceAll(" ", "_");
        NAME_defaultServerName = ModUtils.TRANSLATOR.translate(true, "gui.config.name.server_messages.server_name").replaceAll(" ", "_");
        NAME_defaultServerMotd = ModUtils.TRANSLATOR.translate(true, "gui.config.name.server_messages.server_motd").replaceAll(" ", "_");
        NAME_serverMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.server_messages.server_messages").replaceAll(" ", "_");
        defaultServerIcon = "default";
        defaultServerName = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.server_messages.server_name");
        defaultServerMotd = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.server_messages.server_motd");
        serverMessages = new String[]{"default" + (!StringUtils.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + "Playing on &motd&"};
        // STATUS MESSAGES
        NAME_mainMenuMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.status_messages.main_menu_message").replaceAll(" ", "_");
        NAME_loadingMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.status_messages.loading_message").replaceAll(" ", "_");
        NAME_lanMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.status_messages.lan_message").replaceAll(" ", "_");
        NAME_singlePlayerMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.status_messages.single_player_message").replaceAll(" ", "_");
        NAME_packPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.status_messages.placeholder.pack_message").replaceAll(" ", "_");
        NAME_outerPlayerPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.status_messages.placeholder.player_message.out").replaceAll(" ", "_");
        NAME_innerPlayerPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.status_messages.placeholder.player_message.in").replaceAll(" ", "_");
        NAME_playerCoordinatePlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.status_messages.placeholder.player_coordinate_message").replaceAll(" ", "_");
        NAME_playerHealthPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.status_messages.placeholder.player_health_message").replaceAll(" ", "_");
        NAME_playerAmountPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.status_messages.placeholder.player_amount_message").replaceAll(" ", "_");
        NAME_playerItemsPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.status_messages.placeholder.player_item_message").replaceAll(" ", "_");
        NAME_worldPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.status_messages.placeholder.world_message").replaceAll(" ", "_");
        NAME_modsPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.status_messages.placeholder.mods_message").replaceAll(" ", "_");
        NAME_vivecraftMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.status_messages.special.vivecraft_message").replaceAll(" ", "_");
        NAME_fallbackPackPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.status_messages.fallback.pack_placeholder_message").replaceAll(" ", "_");
        mainMenuMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.state.main_menu");
        loadingMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.state.loading");
        lanMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.state.lan");
        singlePlayerMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.state.single_player");
        packPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.pack");
        outerPlayerPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.player_info.out");
        innerPlayerPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.player_info.in");
        playerCoordinatePlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.player_info.coordinate");
        playerHealthPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.player_info.health");
        playerAmountPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.players");
        playerItemsPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.player_info.items");
        worldPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.world_info");
        modsPlaceholderMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.placeholder.mods");
        vivecraftMessage = ModUtils.TRANSLATOR.translate(true, "craftpresence.defaults.special.vivecraft");
        fallbackPackPlaceholderMessage = "";
        // ADVANCED
        NAME_enableCommands = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.enable_commands").replaceAll(" ", "_");
        NAME_enablePerGui = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.enable_per_gui").replaceAll(" ", "_");
        NAME_enablePerItem = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.enable_per_item").replaceAll(" ", "_");
        NAME_enablePerEntity = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.enable_per_entity").replaceAll(" ", "_");
        NAME_renderTooltips = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.render_tooltips").replaceAll(" ", "_");
        NAME_formatWords = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.format_words").replaceAll(" ", "_");
        NAME_debugMode = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.debug_mode").replaceAll(" ", "_");
        NAME_verboseMode = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.verbose_mode").replaceAll(" ", "_");
        NAME_splitCharacter = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.split_character").replaceAll(" ", "_");
        NAME_refreshRate = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.refresh_rate").replaceAll(" ", "_");
        NAME_roundSize = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.round_size").replaceAll(" ", "_");
        NAME_includeExtraGuiClasses = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.include_extra_gui_classes").replaceAll(" ", "_");
        NAME_guiMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.gui_messages").replaceAll(" ", "_");
        NAME_itemMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.item_messages").replaceAll(" ", "_");
        NAME_entityTargetMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.entity_target_messages").replaceAll(" ", "_");
        NAME_entityAttackingMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.entity_attacking_messages").replaceAll(" ", "_");
        NAME_entityRidingMessages = ModUtils.TRANSLATOR.translate(true, "gui.config.name.advanced.entity_riding_messages").replaceAll(" ", "_");
        enableCommands = true;
        enablePerGui = false;
        enablePerItem = false;
        enablePerEntity = false;
        renderTooltips = true;
        formatWords = true;
        debugMode = false;
        verboseMode = false;
        splitCharacter = ";";
        refreshRate = 2;
        roundSize = 3;
        includeExtraGuiClasses = false;
        guiMessages = new String[]{"default" + (!StringUtils.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + "In &screen&"};
        itemMessages = new String[]{"default" + (!StringUtils.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + "Holding &item&"};
        entityTargetMessages = new String[]{"default" + (!StringUtils.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + "Targeting &entity&"};
        entityAttackingMessages = new String[]{"default" + (!StringUtils.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + "Attacking &entity&"};
        entityRidingMessages = new String[]{"default" + (!StringUtils.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + "Riding &entity&"};
        // ACCESSIBILITY
        NAME_tooltipBackgroundColor = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.tooltip_background_color").replaceAll(" ", "_");
        NAME_tooltipBorderColor = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.tooltip_border_color").replaceAll(" ", "_");
        NAME_guiBackgroundColor = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.gui_background_color").replaceAll(" ", "_");
        NAME_buttonBackgroundColor = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.button_background_color").replaceAll(" ", "_");
        NAME_languageId = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.language_id").replaceAll(" ", "_");
        NAME_showBackgroundAsDark = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.show_background_as_dark").replaceAll(" ", "_");
        NAME_stripTranslationColors = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.strip_translation_colors").replaceAll(" ", "_");
        NAME_showLoggingInChat = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.show_logging_in_chat").replaceAll(" ", "_");
        NAME_stripExtraGuiElements = ModUtils.TRANSLATOR.translate(true, "gui.config.name.accessibility.strip_extra_gui_elements").replaceAll(" ", "_");
        NAME_configKeyCode = ModUtils.TRANSLATOR.translate(true, "key.craftpresence.config_keycode.name").replaceAll(" ", "_");
        tooltipBackgroundColor = "0xF0100010";
        tooltipBorderColor = "0x505000FF";
        guiBackgroundColor = "minecraft" + (!StringUtils.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + (ModUtils.MCProtocolID < 61 && ModUtils.IS_LEGACY ? "/gui/background.png" : "textures/gui/options_background.png");
        buttonBackgroundColor = "minecraft" + (!StringUtils.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + (ModUtils.MCProtocolID < 61 && ModUtils.IS_LEGACY ? "/gui/gui.png" : "textures/gui/widgets.png");
        languageId = ModUtils.MCProtocolID >= 315 ? "en_us" : "en_US";
        showBackgroundAsDark = true;
        stripTranslationColors = false;
        showLoggingInChat = false;
        stripExtraGuiElements = false;
        configKeyCode = ModUtils.MCProtocolID > 340 ? 96 : 41;
        // DISPLAY MESSAGES
        NAME_gameStateMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.display.game_state_message").replaceAll(" ", "_");
        NAME_detailsMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.display.details_message").replaceAll(" ", "_");
        NAME_largeImageMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.display.large_image_message").replaceAll(" ", "_");
        NAME_smallImageMessage = ModUtils.TRANSLATOR.translate(true, "gui.config.name.display.small_image_message").replaceAll(" ", "_");
        NAME_largeImageKey = ModUtils.TRANSLATOR.translate(true, "gui.config.name.display.large_image_key").replaceAll(" ", "_");
        NAME_smallImageKey = ModUtils.TRANSLATOR.translate(true, "gui.config.name.display.small_image_key").replaceAll(" ", "_");
        gameStateMessage = "&SERVER& &PACK&";
        detailsMessage = "&MAINMENU&&DIMENSION&";
        largeImageMessage = "&MAINMENU&&DIMENSION&";
        smallImageMessage = "&SERVER& &PACK&";
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

                    configDataMappings.add(new Pair<>(field.get(this).toString(), valueField.get(this)));
                    configPropertyMappings.add(new Pair<>(field.getName(), valueField.getName()));
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
                    updateConfig(false);
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
            boolean shouldRefreshLater = false;
            final List<String> propertyList = Lists.newArrayList(properties.stringPropertyNames());

            // Format: ListOfMigrationTargets:MigrationId
            final List<Pair<List<String>, String>> migrationData = Lists.newArrayList();

            for (Pair<String, String> configProperty : configPropertyMappings) {
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
                            throw new IllegalArgumentException(ModUtils.TRANSLATOR.translate(true, "craftpresence.exception.config.prop.null", propertyName));
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
                            final Pair<Boolean, Integer> boolData = StringUtils.getValidInteger(foundProperty);

                            if (boolData.getFirst()) {
                                // This check will trigger if the Field Name contains KeyCode Triggers
                                // If the Property Name contains these values, move onwards
                                for (String keyTrigger : keyCodeTriggers) {
                                    if (configProperty.getSecond().toLowerCase().contains(keyTrigger.toLowerCase())) {
                                        if (!CraftPresence.KEYBINDINGS.isValidKeyCode(boolData.getSecond())) {
                                            // If not a valid KeyCode, Revert Value to prior Data
                                            if (!skipLogging) {
                                                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.prop.empty", propertyName));
                                            }
                                            fieldObject = defaultValue;
                                        } else {
                                            // If so, iterate through the migration data allocated earlier
                                            // to see if the property needs any data migrations
                                            for (Pair<List<String>, String> migrationChunk : migrationData) {
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
                                    ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.prop.empty", propertyName));
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
                                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.prop.empty", propertyName));
                            }
                            fieldObject = defaultValue;
                        }
                    } finally {
                        if (fieldObject != null) {
                            fieldObject = syncMigrationData(skipLogging, migrationData, configProperty, fieldObject, foundProperty, propertyName, defaultValue);
                            StringUtils.updateField(getClass(), CraftPresence.CONFIG, new Tuple<>(configProperty.getSecond(), fieldObject, null));
                        }
                    }
                } else {
                    // If a Config Variable is not present in the Properties File, queue a Config Update
                    if (!skipLogging) {
                        ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.config.prop.empty", propertyName));
                        for (String globalTrigger : globalTriggers) {
                            if (propertyName.toLowerCase().contains(globalTrigger.toLowerCase())) {
                                // If a Global Variable is empty, we'll need to re-trigger a read event after updateConfig
                                // in order to ensure migration data and validity is performed
                                shouldRefreshLater = true;
                                break;
                            }
                        }
                    }
                }
                currentIndex++;
            }

            for (String remainingProp : propertyList) {
                // Removes any Invalid Properties, that were not checked off during read
                if (!skipLogging) {
                    ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.prop.invalid", remainingProp));
                }
                properties.remove(remainingProp);
                save("UTF-8");
            }

            // Execute a Config Update, to ensure validity
            updateConfig(shouldRefreshLater);
        }

        try {
            if (configReader != null) {
                configReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.data.close"));
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
    private Object syncMigrationData(final boolean skipLogging, List<Pair<List<String>, String>> migrationData, final Pair<String, String> configProperty, final Object fieldObject, final Object foundProperty, final String propertyName, final Object defaultValue) {
        Object finalFieldObject = fieldObject;
        // Move through any triggers or Migration Data, if needed
        // Before proceeding to final parsing
        for (String globalTrigger : globalTriggers) {
            if (configProperty.getSecond().toLowerCase().contains(globalTrigger.toLowerCase())) {
                // If the variable if Global, check and see if it is different from it's default value
                // In some cases, additional migrations may also be needed, in which case data is added to the list
                if (!skipLogging && !finalFieldObject.toString().equals(defaultValue.toString())) {
                    ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.adjust.global", propertyName));
                }

                if (propertyName.equals(NAME_lastMcVersionId)) {
                    int currentParseValue = -1, defaultParseValue = Integer.parseInt(defaultValue.toString());
                    try {
                        currentParseValue = Integer.parseInt(finalFieldObject.toString());
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
                        ModUtils.LOG.debugInfo(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.info.migration.add", Arrays.asList(keyCodeTriggers).toString(), keyCodeMigrationId, keyCodeMigrationId.equals(KeyConverter.ConversionMode.None.name()) ? "Verification" : "Setting Change"));
                        ModUtils.LOG.debugInfo(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.info.migration.add", Arrays.asList(languageTriggers).toString(), languageMigrationId, languageMigrationId.equals(TranslationUtils.ConversionMode.None.name()) ? "Verification" : "Setting Change"));
                    }

                    // Global Case 1 Notes (KeyCode):
                    // In this situation, if the currently parsed protocol version differs and
                    // is a newer version then 1.12.2 (340), then
                    // we need to ensure any keycode assignments are in an LWJGL 3 format
                    // Otherwise, if using a config from above 1.12.2 (340) on it or anything lower,
                    // we need to ensure any keycode assignments are in an LWJGL 2 format.
                    // If neither is true, then we mark the migration data as None, and it will be verified
                    migrationData.add(new Pair<>(Arrays.asList(keyCodeTriggers), keyCodeMigrationId));
                    // Normal Case 1 Notes (Language ID):
                    // In this situation, if the currently parsed protocol version differs and
                    // is a newer version then or exactly 1.11 (315), then
                    // we need to ensure any Language Locale's are complying with Pack Format 3 and above
                    // Otherwise, if using a config from anything less then 1.11 (315),
                    // we need to ensure any Language Locale's are complying with Pack Format 2 and below
                    // If neither is true, then we mark the migration data as None, and it will be verified
                    migrationData.add(new Pair<>(Arrays.asList(languageTriggers), languageMigrationId));
                }
                finalFieldObject = defaultValue;
                break;
            }
        }

        // This check will trigger if the Field Name contains Language Identifier Triggers
        // If the Property Name contains these values, move onwards
        for (String langTrigger : languageTriggers) {
            if (configProperty.getSecond().toLowerCase().contains(langTrigger.toLowerCase())) {
                // If so, iterate through the migration data allocated earlier
                // to see if the property needs any data migrations
                for (Pair<List<String>, String> migrationChunk : migrationData) {
                    if (migrationChunk.getFirst().contains(langTrigger.toLowerCase()) && !migrationChunk.getSecond().equalsIgnoreCase(TranslationUtils.ConversionMode.Unknown.name())) {
                        // If so, retrieve the second part of the migration data,
                        // and adjust the property accordingly with the mode it should use
                        final String migratedLanguageId = TranslationUtils.convertId(foundProperty.toString(), TranslationUtils.ConversionMode.valueOf(migrationChunk.getSecond()));
                        if (!skipLogging && !migratedLanguageId.equals(foundProperty.toString())) {
                            ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.info.migration.apply", migrationChunk.getFirst().toString(), migrationChunk.getSecond(), propertyName, foundProperty.toString(), migratedLanguageId));
                        }
                        finalFieldObject = migratedLanguageId;
                        break;
                    }
                }
                break;
            }
        }
        return finalFieldObject;
    }

    /**
     * Aligns and Updates the {@link ConfigUtils#properties} with the data from {@link ConfigUtils#configDataMappings}
     *
     * @param shouldDataSync Whether a Config Read should be triggered again
     */
    public void updateConfig(final boolean shouldDataSync) {
        // Track if in need of a data re-sync
        boolean needsDataSync = shouldDataSync;

        // Sync Edits from Read Events that may have occurred
        syncMappings();

        for (Pair<String, Object> configDataSet : configDataMappings) {
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
                        ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.config.missing.default", configDataSet.getFirst()));
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
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.error.data.close"));
            ex.printStackTrace();
        }
    }
}