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
    private boolean usingJSON = false;

    public TranslationHandler() {
        setLanguage(mc.gameSettings.language.toLowerCase());
        setUsingJSON(false);
        getTranslationMap();
    }

    public TranslationHandler(final boolean useJSON) {
        setLanguage(mc.gameSettings.language.toLowerCase());
        setUsingJSON(useJSON);
        getTranslationMap();
    }

    public TranslationHandler(final String modID) {
        setLanguage(mc.gameSettings.language.toLowerCase());
        setModID(modID);
        setUsingJSON(false);
        getTranslationMap();
    }

    public TranslationHandler(final String modID, final boolean useJSON) {
        setLanguage(mc.gameSettings.language.toLowerCase());
        setModID(modID);
        setUsingJSON(useJSON);
        getTranslationMap();
    }

    void tick() {
        if (!languageID.equals(mc.gameSettings.language.toLowerCase()) &&
                (!requestMap.containsKey(mc.gameSettings.language.toLowerCase()) || requestMap.get(mc.gameSettings.language.toLowerCase()))) {
            setLanguage(mc.gameSettings.language.toLowerCase());
            getTranslationMap();
        }
    }

    private void setUsingJSON(final boolean usingJSON) {
        this.usingJSON = usingJSON;
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
                "lang/" + languageID + (usingJSON ? ".json" : ".lang"));

        if (in != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            try {
                for (String currentString = reader.readLine().trim(); !StringHandler.isNullOrEmpty(currentString); currentString = reader.readLine().trim()) {
                    if (!currentString.startsWith("#") && !currentString.startsWith("[{}]") && (usingJSON ? currentString.contains(":") : currentString.contains("="))) {
                        String[] splitTranslation = usingJSON ? currentString.split(":", 2) : currentString.split("=", 2);
                        if (usingJSON) {
                            String str1 = splitTranslation[0].substring(1, splitTranslation[0].length() - 1).replace("\\n", "\n").replace("\\", "").trim();
                            String str2 = splitTranslation[1].substring(2, splitTranslation[1].length() - 2).replace("\\n", "\n").replace("\\", "").trim();
                            translationMap.put(str1.replace("Â", ""), str2.replace("Â", ""));
                        } else {
                            translationMap.put(splitTranslation[0].trim().replace("Â", ""), splitTranslation[1].trim().replace("Â", ""));
                        }
                    }
                }
                in.close();
            } catch (Exception ex) {
                Constants.LOG.error("An Exception has Occurred while Loading Translation Mappings, Things may not work well...");
                ex.printStackTrace();
            }
        } else {
            Constants.LOG.error("Translations for " + modID + " do not exist for " + languageID);
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
