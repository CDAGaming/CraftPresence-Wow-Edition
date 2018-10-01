package com.gitlab.cdagaming.craftpresence.config;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import net.minecraft.client.resources.I18n;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigHandler {
    // Config Names
    // GENERAL
    public String NAME_detectCurseManifest, NAME_detectMultiMCManifest, NAME_detectTechnicPack,
            NAME_showTime, NAME_showCurrentBiome, NAME_showCurrentDimension,
            NAME_showGameState, NAME_clientID, NAME_defaultIcon;
    // BIOME MESSAGES
    public String NAME_biomeMessages;
    // DIMENSION MESSAGES
    public String NAME_defaultDimensionIcon, NAME_dimensionMessages;
    // SERVER MESSAGES
    public String NAME_defaultServerIcon, NAME_defaultServerName,
            NAME_defaultServerMOTD, NAME_serverMessages;
    // STATUS MESSAGES
    public String NAME_mainmenuMSG, NAME_singleplayerMSG, NAME_loadingMSG,
            NAME_packPlaceholderMSG, NAME_playerPlaceholderMSG, NAME_playerAmountPlaceholderMSG,
            NAME_gameTimePlaceholderMSG, NAME_vivecraftMessage;
    // ADVANCED
    public String NAME_enableCommands, NAME_enablePERGUI, NAME_enablePERItem,
            NAME_splitCharacter, NAME_guiMessages, NAME_itemMessages;

    // Config Variables
    // GENERAL
    public Boolean detectCurseManifest, detectMultiMCManifest, detectTechnicPack, showTime,
            showCurrentBiome, showCurrentDimension, showGameState;
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
    public String mainmenuMSG, singleplayerMSG, loadingMSG, packPlaceholderMSG,
            playerPlaceholderMSG, playerAmountPlaceholderMSG,
            gameTimePlaceholderMSG, vivecraftMessage;
    // ADVANCED
    public Boolean enableCommands, enablePERGUI, enablePERItem;
    public String splitCharacter;
    public String[] guiMessages, itemMessages;
    // CLASS-SPECIFIC - PUBLIC
    public Boolean hasChanged = false, hasClientPropertiesChanged = false, rebootOnWorldLoad = false;
    // CLASS-SPECIFIC - PUBLIC
    public Properties properties = new Properties();
    // CLASS-SPECIFIC - PRIVATE
    private String fileName;
    private Boolean verified = false;

    public ConfigHandler(String fileName) {
        this.fileName = fileName;
    }

    private void setupNames() {
        // GENERAL
        NAME_detectCurseManifest = I18n.format("gui.config.name.general.detectcursemanifest").replaceAll(" ", "_");
        NAME_detectMultiMCManifest = I18n.format("gui.config.name.general.detectmultimcmanifest").replaceAll(" ", "_");
        NAME_detectTechnicPack = I18n.format("gui.config.name.general.detecttechnicpack").replaceAll(" ", "_");
        NAME_showTime = I18n.format("gui.config.name.general.showtime").replaceAll(" ", "_");
        NAME_showCurrentBiome = I18n.format("gui.config.name.general.showbiome").replaceAll(" ", "_");
        NAME_showCurrentDimension = I18n.format("gui.config.name.general.showdimension").replaceAll(" ", "_");
        NAME_showGameState = I18n.format("gui.config.name.general.showstate").replaceAll(" ", "_");
        NAME_clientID = I18n.format("gui.config.name.general.clientid").replaceAll(" ", "_");
        NAME_defaultIcon = I18n.format("gui.config.name.general.defaulticon").replaceAll(" ", "_");
        // BIOME MESSAGES
        NAME_biomeMessages = I18n.format("gui.config.name.biomemessages.biomemessages").replaceAll(" ", "_");
        // DIMENSION MESSAGES
        NAME_defaultDimensionIcon = I18n.format("gui.config.name.dimensionmessages.dimensionicon").replaceAll(" ", "_");
        NAME_dimensionMessages = I18n.format("gui.config.name.dimensionmessages.dimensionmessages").replaceAll(" ", "_");
        // SERVER MESSAGES
        NAME_defaultServerIcon = I18n.format("gui.config.name.servermessages.servericon").replaceAll(" ", "_");
        NAME_defaultServerName = I18n.format("gui.config.name.servermessages.servername").replaceAll(" ", "_");
        NAME_defaultServerMOTD = I18n.format("gui.config.name.servermessages.servermotd").replaceAll(" ", "_");
        NAME_serverMessages = I18n.format("gui.config.name.servermessages.servermessages").replaceAll(" ", "_");
        // STATUS MESSAGES
        NAME_mainmenuMSG = I18n.format("gui.config.name.statusmessages.mainmenumsg").replaceAll(" ", "_");
        NAME_singleplayerMSG = I18n.format("gui.config.name.statusmessages.singleplayermsg").replaceAll(" ", "_");
        NAME_loadingMSG = I18n.format("gui.config.name.statusmessages.loadingmsg").replaceAll(" ", "_");
        NAME_packPlaceholderMSG = I18n.format("gui.config.name.statusmessages.placeholder.packmsg").replaceAll(" ", "_");
        NAME_playerPlaceholderMSG = I18n.format("gui.config.name.statusmessages.placeholder.playermsg").replaceAll(" ", "_");
        NAME_playerAmountPlaceholderMSG = I18n.format("gui.config.name.statusmessages.placeholder.playeramountmsg").replaceAll(" ", "_");
        NAME_gameTimePlaceholderMSG = I18n.format("gui.config.name.statusmessages.placeholder.gametimemsg").replaceAll(" ", "_");
        NAME_vivecraftMessage = I18n.format("gui.config.name.statusmessages.special.vivecraftmsg").replaceAll(" ", "_");
        // ADVANCED
        NAME_enableCommands = I18n.format("gui.config.name.advanced.enablecommands").replaceAll(" ", "_");
        NAME_enablePERGUI = I18n.format("gui.config.name.advanced.enablepergui").replaceAll(" ", "_");
        NAME_enablePERItem = I18n.format("gui.config.name.advanced.enableperitem").replaceAll(" ", "_");
        NAME_splitCharacter = I18n.format("gui.config.name.advanced.splitcharacter").replaceAll(" ", "_");
        NAME_guiMessages = I18n.format("gui.config.name.advanced.guimessages").replaceAll(" ", "_");
        NAME_itemMessages = I18n.format("gui.config.name.advanced.itemmessages").replaceAll(" ", "_");
    }

    public void initialize() {
        try {
            File configFile = new File(fileName);
            File parentDir = configFile.getParentFile();
            boolean isConfigNew = (!parentDir.exists() && parentDir.mkdirs()) || (!configFile.exists() && configFile.createNewFile());

            setupNames();

            if (isConfigNew) {
                updateConfig();
            }
            read();
        } catch (Exception ex) {
            Constants.LOG.error(I18n.format("craftpresence.logger.error.config.save"));
            ex.printStackTrace();
        }
    }

    public void read() {
        try {
            Reader configReader = new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
            properties.load(configReader);
            configReader.close();
        } catch (Exception ex) {
            Constants.LOG.error(I18n.format("craftpresence.logger.error.config.save"));
            ex.printStackTrace();
        }

        try {
            // GENERAL
            detectCurseManifest = Boolean.parseBoolean(properties.getProperty(NAME_detectCurseManifest));
            detectMultiMCManifest = Boolean.parseBoolean(properties.getProperty(NAME_detectMultiMCManifest));
            detectTechnicPack = Boolean.parseBoolean(properties.getProperty(NAME_detectTechnicPack));
            showTime = Boolean.parseBoolean(properties.getProperty(NAME_showTime));
            showCurrentBiome = Boolean.parseBoolean(properties.getProperty(NAME_showCurrentBiome));
            showCurrentDimension = Boolean.parseBoolean(properties.getProperty(NAME_showCurrentDimension));
            showGameState = Boolean.parseBoolean(properties.getProperty(NAME_showGameState));
            clientID = properties.getProperty(NAME_clientID);
            defaultIcon = properties.getProperty(NAME_defaultIcon);
            // BIOME MESSAGES
            biomeMessages = properties.getProperty(NAME_biomeMessages).replaceAll("\\[", "").replaceAll("]", "").split(", ");
            // DIMENSION MESSAGES
            defaultDimensionIcon = properties.getProperty(NAME_defaultDimensionIcon);
            dimensionMessages = properties.getProperty(NAME_dimensionMessages).replaceAll("\\[", "").replaceAll("]", "").split(", ");
            // SERVER MESSAGES
            defaultServerIcon = properties.getProperty(NAME_defaultServerIcon);
            defaultServerName = properties.getProperty(NAME_defaultServerName);
            defaultServerMOTD = properties.getProperty(NAME_defaultServerMOTD);
            serverMessages = properties.getProperty(NAME_serverMessages).replaceAll("\\[", "").replaceAll("]", "").split(", ");
            // STATUS MESSAGES
            mainmenuMSG = properties.getProperty(NAME_mainmenuMSG);
            singleplayerMSG = properties.getProperty(NAME_singleplayerMSG);
            loadingMSG = properties.getProperty(NAME_loadingMSG);
            packPlaceholderMSG = properties.getProperty(NAME_packPlaceholderMSG);
            playerPlaceholderMSG = properties.getProperty(NAME_playerPlaceholderMSG);
            playerAmountPlaceholderMSG = properties.getProperty(NAME_playerAmountPlaceholderMSG);
            gameTimePlaceholderMSG = properties.getProperty(NAME_gameTimePlaceholderMSG);
            vivecraftMessage = properties.getProperty(NAME_vivecraftMessage);
            // ADVANCED
            enableCommands = Boolean.parseBoolean(properties.getProperty(NAME_enableCommands));
            enablePERGUI = Boolean.parseBoolean(properties.getProperty(NAME_enablePERGUI));
            enablePERItem = Boolean.parseBoolean(properties.getProperty(NAME_enablePERItem));
            splitCharacter = properties.getProperty(NAME_splitCharacter);
            guiMessages = properties.getProperty(NAME_guiMessages).replaceAll("\\[", "").replaceAll("]", "").split(", ");
            itemMessages = properties.getProperty(NAME_itemMessages).replaceAll("\\[", "").replaceAll("]", "").split(", ");
        } catch (NullPointerException ex) {
            verifyConfig(properties);
        } finally {
            if (!verified) {
                verifyConfig(properties);
            } else {
                Constants.LOG.info(I18n.format("craftpresence.logger.info.config.save"));
            }
        }
    }

    public void updateConfig() {
        Properties properties = new Properties();

        // GENERAL
        properties.setProperty(NAME_detectCurseManifest, !StringHandler.isNullOrEmpty(Boolean.toString(detectCurseManifest)) ? Boolean.toString(detectCurseManifest) : "true");
        properties.setProperty(NAME_detectMultiMCManifest, !StringHandler.isNullOrEmpty(Boolean.toString(detectMultiMCManifest)) ? Boolean.toString(detectMultiMCManifest) : "true");
        properties.setProperty(NAME_detectTechnicPack, !StringHandler.isNullOrEmpty(Boolean.toString(detectTechnicPack)) ? Boolean.toString(detectTechnicPack) : "true");
        properties.setProperty(NAME_showTime, !StringHandler.isNullOrEmpty(Boolean.toString(showTime)) ? Boolean.toString(showTime) : "true");
        properties.setProperty(NAME_showCurrentBiome, !StringHandler.isNullOrEmpty(Boolean.toString(showCurrentBiome)) ? Boolean.toString(showCurrentBiome) : "false");
        properties.setProperty(NAME_showCurrentDimension, !StringHandler.isNullOrEmpty(Boolean.toString(showCurrentDimension)) ? Boolean.toString(showCurrentDimension) : "true");
        properties.setProperty(NAME_showGameState, !StringHandler.isNullOrEmpty(Boolean.toString(showGameState)) ? Boolean.toString(showGameState) : "true");
        properties.setProperty(NAME_clientID, !StringHandler.isNullOrEmpty(clientID) ? clientID : "450485984333660181");
        properties.setProperty(NAME_defaultIcon, !StringHandler.isNullOrEmpty(defaultIcon) ? defaultIcon : "grass");
        // BIOME MESSAGES
        properties.setProperty(NAME_biomeMessages, !StringHandler.isNullOrEmpty(Arrays.toString(biomeMessages)) ? Arrays.toString(biomeMessages) : Arrays.toString(new String[]{"default" + (!StringHandler.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + "Playing in &biome&"}));
        // DIMENSION MESSAGES
        properties.setProperty(NAME_defaultDimensionIcon, !StringHandler.isNullOrEmpty(defaultDimensionIcon) ? defaultDimensionIcon : "unknown");
        properties.setProperty(NAME_dimensionMessages, !StringHandler.isNullOrEmpty(Arrays.toString(dimensionMessages)) ? Arrays.toString(dimensionMessages) : Arrays.toString(new String[]{"default" + (!StringHandler.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + "In The &dimension&"}));
        // SERVER MESSAGES
        properties.setProperty(NAME_defaultServerIcon, !StringHandler.isNullOrEmpty(defaultServerIcon) ? defaultServerIcon : "default");
        properties.setProperty(NAME_defaultServerName, !StringHandler.isNullOrEmpty(defaultServerName) ? defaultServerName : I18n.format("selectServer.defaultName"));
        properties.setProperty(NAME_defaultServerMOTD, !StringHandler.isNullOrEmpty(defaultServerMOTD) ? defaultServerMOTD : I18n.format("craftpresence.defaults.servermessages.servermotd"));
        properties.setProperty(NAME_serverMessages, !StringHandler.isNullOrEmpty(Arrays.toString(serverMessages)) ? Arrays.toString(serverMessages) : Arrays.toString(new String[]{"default" + (!StringHandler.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + "Playing on &motd&"}));
        // STATUS MESSAGES
        properties.setProperty(NAME_mainmenuMSG, !StringHandler.isNullOrEmpty(mainmenuMSG) ? mainmenuMSG : I18n.format("craftpresence.defaults.state.mainmenu"));
        properties.setProperty(NAME_singleplayerMSG, !StringHandler.isNullOrEmpty(singleplayerMSG) ? singleplayerMSG : I18n.format("craftpresence.defaults.state.singleplayer"));
        properties.setProperty(NAME_loadingMSG, !StringHandler.isNullOrEmpty(loadingMSG) ? loadingMSG : I18n.format("craftpresence.defaults.state.loading"));
        properties.setProperty(NAME_packPlaceholderMSG, !StringHandler.isNullOrEmpty(packPlaceholderMSG) ? packPlaceholderMSG : I18n.format("craftpresence.defaults.placeholder.pack"));
        properties.setProperty(NAME_playerPlaceholderMSG, !StringHandler.isNullOrEmpty(playerPlaceholderMSG) ? playerPlaceholderMSG : I18n.format("craftpresence.defaults.placeholder.ign"));
        properties.setProperty(NAME_playerAmountPlaceholderMSG, !StringHandler.isNullOrEmpty(playerAmountPlaceholderMSG) ? playerAmountPlaceholderMSG : I18n.format("craftpresence.defaults.placeholder.players"));
        properties.setProperty(NAME_gameTimePlaceholderMSG, !StringHandler.isNullOrEmpty(gameTimePlaceholderMSG) ? gameTimePlaceholderMSG : I18n.format("craftpresence.defaults.placeholder.time"));
        properties.setProperty(NAME_vivecraftMessage, !StringHandler.isNullOrEmpty(vivecraftMessage) ? vivecraftMessage : I18n.format("craftpresence.defaults.special.vivecraft"));
        // ADVANCED
        properties.setProperty(NAME_enableCommands, !StringHandler.isNullOrEmpty(Boolean.toString(enableCommands)) ? Boolean.toString(enableCommands) : "true");
        properties.setProperty(NAME_enablePERGUI, !StringHandler.isNullOrEmpty(Boolean.toString(enablePERGUI)) ? Boolean.toString(enablePERGUI) : "false");
        properties.setProperty(NAME_enablePERItem, !StringHandler.isNullOrEmpty(Boolean.toString(enablePERItem)) ? Boolean.toString(enablePERItem) : "false");
        properties.setProperty(NAME_splitCharacter, !StringHandler.isNullOrEmpty(splitCharacter) ? splitCharacter : ";");
        properties.setProperty(NAME_guiMessages, !StringHandler.isNullOrEmpty(Arrays.toString(guiMessages)) ? Arrays.toString(guiMessages) : Arrays.toString(new String[]{"default" + (!StringHandler.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + "In &gui&"}));
        properties.setProperty(NAME_itemMessages, !StringHandler.isNullOrEmpty(Arrays.toString(itemMessages)) ? Arrays.toString(itemMessages) : Arrays.toString(new String[]{"default" + (!StringHandler.isNullOrEmpty(splitCharacter) ? splitCharacter : ";") + "Holding &main&"}));

        // Check for Conflicts before Saving
        if (showCurrentBiome && showGameState) {
            Constants.LOG.warn(I18n.format("craftpresence.logger.warning.config.conflict.biomestate"));
            showCurrentBiome = false;
            properties.setProperty(NAME_showCurrentBiome, "false");
        }
        if (enablePERGUI && showGameState) {
            Constants.LOG.warn(I18n.format("craftpresence.logger.warning.config.conflict.pergui"));
            enablePERGUI = false;
            properties.setProperty(NAME_enablePERGUI, "false");
        }

        save(properties);
    }

    private void verifyConfig(final Properties properties) {
        List<String> validProperties = new ArrayList<>();
        List<String> removedProperties = new ArrayList<>();
        boolean needsFullUpdate = false;

        for (Field field : getClass().getFields()) {
            if (field.getName().contains("NAME_")) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(this);
                    validProperties.add(value.toString());
                    if (!properties.stringPropertyNames().contains(value.toString()) && validProperties.contains(value.toString())) {
                        Constants.LOG.error(I18n.format("craftpresence.logger.error.config.emptyprop", value.toString()));
                        needsFullUpdate = true;
                    }
                } catch (Exception ignored) {
                }
            }
        }

        for (String property : properties.stringPropertyNames()) {
            if (!validProperties.contains(property)) {
                Constants.LOG.error(I18n.format("craftpresence.logger.error.config.invalidprop", property));
                removedProperties.add(property);
                properties.remove(property);
                save(properties);
            }
            if (!removedProperties.contains(property)) {
                if (StringHandler.isNullOrEmpty(properties.getProperty(property))) {
                    Constants.LOG.error(I18n.format("craftpresence.logger.error.config.emptyprop", property));
                    needsFullUpdate = true;
                } else {
                    if (property.equals(NAME_clientID) && (properties.getProperty(property).length() != 18 || properties.getProperty(property).matches(".*[a-z].*") || properties.getProperty(property).matches(".*[A-Z].*"))) {
                        Constants.LOG.error(I18n.format("craftpresence.logger.error.config.invalidprop", property));
                        clientID = "450485984333660181";
                        properties.setProperty(property, clientID);
                        save(properties);
                    }
                    if (property.equals(NAME_splitCharacter) && (properties.getProperty(property).length() != 1 || properties.getProperty(property).matches(".*[a-z].*") || properties.getProperty(property).matches(".*[A-Z].*"))) {
                        Constants.LOG.error(I18n.format("craftpresence.logger.error.config.invalidprop", property));
                        splitCharacter = ";";
                        properties.setProperty(property, splitCharacter);
                        save(properties);
                    }

                    if (property.equals(NAME_biomeMessages)) {
                        boolean defaultFound = !StringHandler.isNullOrEmpty(StringHandler.getConfigPart(biomeMessages, "default", 0, 1, splitCharacter, null));
                        if (!defaultFound) {
                            Constants.LOG.error(I18n.format("craftpresence.logger.error.config.defaultmissing", property));
                            biomeMessages = StringHandler.addToArray(biomeMessages, biomeMessages.length, "default" + splitCharacter + "Playing in &biome&");
                            properties.setProperty(property, Arrays.toString(biomeMessages));
                            save(properties);
                        }
                    }
                    if (property.equals(NAME_dimensionMessages)) {
                        boolean defaultFound = !StringHandler.isNullOrEmpty(StringHandler.getConfigPart(dimensionMessages, "default", 0, 1, splitCharacter, null));
                        if (!defaultFound) {
                            Constants.LOG.error(I18n.format("craftpresence.logger.error.config.defaultmissing", property));
                            dimensionMessages = StringHandler.addToArray(dimensionMessages, dimensionMessages.length, "default" + splitCharacter + "In The &dimension&");
                            properties.setProperty(property, Arrays.toString(dimensionMessages));
                            save(properties);
                        }
                    }
                    if (property.equals(NAME_serverMessages)) {
                        boolean defaultFound = !StringHandler.isNullOrEmpty(StringHandler.getConfigPart(serverMessages, "default", 0, 1, splitCharacter, null));
                        if (!defaultFound) {
                            Constants.LOG.error(I18n.format("craftpresence.logger.error.config.defaultmissing", property));
                            serverMessages = StringHandler.addToArray(serverMessages, serverMessages.length, "default" + splitCharacter + "Playing on &motd&");
                            properties.setProperty(property, Arrays.toString(serverMessages));
                            save(properties);
                        }
                    }
                    if (property.equals(NAME_guiMessages)) {
                        boolean defaultFound = !StringHandler.isNullOrEmpty(StringHandler.getConfigPart(guiMessages, "default", 0, 1, splitCharacter, null));
                        if (!defaultFound) {
                            Constants.LOG.error(I18n.format("craftpresence.logger.error.config.defaultmissing", property));
                            guiMessages = StringHandler.addToArray(guiMessages, guiMessages.length, "default" + splitCharacter + "In &gui&");
                            properties.setProperty(property, Arrays.toString(guiMessages));
                            save(properties);
                        }
                    }
                    if (property.equals(NAME_itemMessages)) {
                        boolean defaultFound = !StringHandler.isNullOrEmpty(StringHandler.getConfigPart(itemMessages, "default", 0, 1, splitCharacter, null));
                        if (!defaultFound) {
                            Constants.LOG.error(I18n.format("craftpresence.logger.error.config.defaultmissing", property));
                            itemMessages = StringHandler.addToArray(itemMessages, itemMessages.length, "default" + splitCharacter + "Holding &main&");
                            properties.setProperty(property, Arrays.toString(itemMessages));
                            save(properties);
                        }
                    }
                }
            }
        }

        if (needsFullUpdate) {
            updateConfig();
            verified = true;
            read();
        } else {
            verified = true;
        }
    }

    public void save(final Properties properties) {
        try {
            Writer configWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), StandardCharsets.UTF_8);
            properties.store(configWriter, null);
            configWriter.close();
        } catch (Exception ex) {
            Constants.LOG.error(I18n.format("craftpresence.logger.error.config.save"));
            ex.printStackTrace();
        }
    }
}