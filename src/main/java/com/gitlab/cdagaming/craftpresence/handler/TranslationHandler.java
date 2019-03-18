package com.gitlab.cdagaming.craftpresence.handler;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class TranslationHandler {
    public static TranslationHandler instance;
    private String languageID = "en_us", modID;
    private Map<String, String> translationMap = Maps.newHashMap();
    private Map<String, Boolean> requestMap = Maps.newHashMap();
    private Minecraft mc = Minecraft.getMinecraft();

    public TranslationHandler() {
        setLanguage(mc.gameSettings.language.toLowerCase());
        getTranslationMap();
    }

    public TranslationHandler(final String modID) {
        setLanguage(mc.gameSettings.language.toLowerCase());
        setModID(modID);
        getTranslationMap();
    }

    void tick() {
        if (!languageID.equals(mc.gameSettings.language.toLowerCase()) &&
                (!requestMap.containsKey(mc.gameSettings.language.toLowerCase()) || requestMap.get(mc.gameSettings.language.toLowerCase()))) {
            setLanguage(mc.gameSettings.language.toLowerCase());
            getTranslationMap();
        }
    }

    private void setLanguage(final String languageID) {
        if (!StringHandler.isNullOrEmpty(languageID)) {
            this.languageID = languageID;
        } else {
            this.languageID = "en_us";
        }
    }

    private void setModID(final String modID) {
        if (!StringHandler.isNullOrEmpty(modID)) {
            this.modID = modID;
        } else {
            this.modID = null;
        }
    }

    private void getTranslationMap() {
        translationMap = Maps.newHashMap();

        InputStream in = getClass().getResourceAsStream("/assets/"
                + (!StringHandler.isNullOrEmpty(modID) ? modID + "/" : "") +
                "lang/" + languageID + ".lang");
        if (in != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            try {
                for (String currentString = reader.readLine(); !StringHandler.isNullOrEmpty(currentString); currentString = reader.readLine()) {
                    if (!currentString.startsWith("#") && currentString.contains("=")) {
                        String[] splitTranslation = currentString.split("=", 2);
                        translationMap.put(splitTranslation[0], splitTranslation[1]);
                    }
                }
                in.close();
            } catch (Exception ex) {
                Constants.LOG.error("An Exception has Occurred while Loading Translation Mappings, Things may not Work well...");
                ex.printStackTrace();
            }
        } else {
            Constants.LOG.error("Translations for " + modID + " do not exist at " + languageID);
            requestMap.put(languageID, false);
            setLanguage("en_us");
        }
    }

    public String translate(String translationKey, Object... parameters) {
        String i18n_Translation = I18n.format(translationKey, parameters);

        if (!i18n_Translation.equals(translationKey)) {
            return i18n_Translation;
        }

        if (translationMap.containsKey(translationKey)) {
            return String.format(translationMap.get(translationKey), parameters);
        } else {
            Constants.LOG.error("Unable to retrieve a Translation for " + translationKey);
            return translationKey;
        }
    }
}
