package com.gitlab.cdagaming.craftpresence.utils;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TranslationUtils {
    public static TranslationUtils instance;
    public boolean isUnicode = false;
    private String languageID = "en_US", modID;
    private Map<String, String> translationMap = Maps.newHashMap();
    private Map<String, Boolean> requestMap = Maps.newHashMap();
    private boolean usingJSON = false;

    public TranslationUtils() {
        setLanguage(CraftPresence.CONFIG != null ? CraftPresence.CONFIG.languageID : languageID);
        setUsingJSON(false);
        getTranslationMap();
        checkUnicode();
    }

    public TranslationUtils(final boolean useJSON) {
        setLanguage(CraftPresence.CONFIG != null ? CraftPresence.CONFIG.languageID : languageID);
        setUsingJSON(useJSON);
        getTranslationMap();
        checkUnicode();
    }

    public TranslationUtils(final String modID) {
        setLanguage(CraftPresence.CONFIG != null ? CraftPresence.CONFIG.languageID : languageID);
        setModID(modID);
        setUsingJSON(false);
        getTranslationMap();
        checkUnicode();
    }

    public TranslationUtils(final String modID, final boolean useJSON) {
        setLanguage(CraftPresence.CONFIG != null ? CraftPresence.CONFIG.languageID : languageID);
        setModID(modID);
        setUsingJSON(useJSON);
        getTranslationMap();
        checkUnicode();
    }

    void onTick() {
        if (CraftPresence.CONFIG != null && !languageID.equals(CraftPresence.CONFIG.languageID) &&
                (!requestMap.containsKey(CraftPresence.CONFIG.languageID) || requestMap.get(CraftPresence.CONFIG.languageID))) {
            setLanguage(CraftPresence.CONFIG.languageID);
            getTranslationMap();
            checkUnicode();
        }

        if (CraftPresence.instance.gameSettings != null && isUnicode != CraftPresence.instance.gameSettings.forceUnicodeFont) {
            checkUnicode();
        }
    }

    private void checkUnicode() {
        isUnicode = false;
        int i = 0;
        int totalLength = 0;

        for (String currentString : translationMap.values()) {
            int currentLength = currentString.length();
            totalLength += currentLength;

            for (int index = 0; index < currentLength; ++index) {
                if (currentString.charAt(index) >= 256) {
                    ++i;
                }
            }
        }

        float f = (float) i / (float) totalLength;
        isUnicode = (double) f > 0.1D || (CraftPresence.instance.gameSettings != null && CraftPresence.instance.gameSettings.forceUnicodeFont);
    }

    private void setUsingJSON(final boolean usingJSON) {
        this.usingJSON = usingJSON;
    }

    private void setLanguage(final String languageID) {
        if (!StringUtils.isNullOrEmpty(languageID)) {
            this.languageID = languageID;
        } else {
            this.languageID = "en_US";
        }
    }

    private void setModID(final String modID) {
        if (!StringUtils.isNullOrEmpty(modID)) {
            this.modID = modID;
        } else {
            this.modID = null;
        }
    }

    private void getTranslationMap() {
        translationMap = Maps.newHashMap();

        InputStream in = StringUtils.getResourceAsStream(TranslationUtils.class, "/assets/"
                + (!StringUtils.isNullOrEmpty(modID) ? modID + "/" : "") +
                "lang/" + languageID + (usingJSON ? ".json" : ".lang"));
        InputStream fallbackIn = StringUtils.getResourceAsStream(TranslationUtils.class, "/assets/"
                + (!StringUtils.isNullOrEmpty(modID) ? modID + "/" : "") +
                "lang/" + languageID.toLowerCase() + (usingJSON ? ".json" : ".lang"));

        if (in != null || fallbackIn != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in != null ? in : fallbackIn, StandardCharsets.UTF_8));
            try {
                String currentString;
                while ((currentString = reader.readLine()) != null) {
                    currentString = currentString.trim();
                    if (!currentString.startsWith("#") && !currentString.startsWith("[{}]") && (usingJSON ? currentString.contains(":") : currentString.contains("="))) {
                        String[] splitTranslation = usingJSON ? currentString.split(":", 2) : currentString.split("=", 2);
                        if (usingJSON) {
                            String str1 = splitTranslation[0].substring(1, splitTranslation[0].length() - 1).replace("\\n", "\n").replace("\\", "").trim();
                            String str2 = splitTranslation[1].substring(2, splitTranslation[1].length() - 2).replace("\\n", "\n").replace("\\", "").trim();
                            translationMap.put(str1, str2);
                        } else {
                            translationMap.put(splitTranslation[0].trim(), splitTranslation[1].trim());
                        }
                    }
                }

                if (in != null) {
                    in.close();
                }
                if (fallbackIn != null) {
                    fallbackIn.close();
                }
            } catch (Exception ex) {
                ModUtils.LOG.error("An Exception has Occurred while Loading Translation Mappings, Things may not work well...");
                ex.printStackTrace();
            }
        } else {
            ModUtils.LOG.error("Translations for " + modID + " do not exist for " + languageID);
            requestMap.put(languageID, false);
            setLanguage("en_US");
        }
    }

    public String translate(boolean stripColors, String translationKey, Object... parameters) {
        boolean hasError = false;
        String translatedString = translationKey;
        try {
            if (translationMap.containsKey(translationKey)) {
                translatedString = String.format(translationMap.get(translationKey), parameters);
            } else {
                hasError = true;
            }
        } catch (Exception ex) {
            ModUtils.LOG.error("Exception Parsing " + translationKey);
            ex.printStackTrace();
            hasError = true;
        }

        if (hasError) {
            ModUtils.LOG.error("Unable to retrieve a Translation for " + translationKey);
        }
        return stripColors ? StringUtils.stripColors(translatedString) : translatedString;
    }

    public String translate(String translationKey, Object... parameters) {
        return translate(CraftPresence.CONFIG != null && CraftPresence.CONFIG.stripTranslationColors, translationKey, parameters);
    }
}
