/*
 * MIT License
 *
 * Copyright (c) 2018 - 2019 CDAGaming (cstack2011@yahoo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.gitlab.cdagaming.craftpresence.utils;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Translation and Localization Utilities based on Language Code
 *
 * @author CDAGaming
 */
public class TranslationUtils {
    /**
     * Whether the Translations are utilizing Unicode Characters
     */
    public boolean isUnicode = false;

    /**
     * The Language ID to Locate and Retrieve Translations
     */
    private String languageID = "en_US";

    /**
     * The Target ID to locate the Language File
     */
    private String modID;

    /**
     * The Stored Mapping of Valid Translations
     * <p>
     * Format: unlocalizedKey:localizedString
     */
    private Map<String, String> translationMap = Maps.newHashMap();

    /**
     * The Stored Mapping of Language Request History
     * <p>
     * Format: languageID:doesExist
     */
    private Map<String, Boolean> requestMap = Maps.newHashMap();

    /**
     * If using a .Json or .Lang Language File
     */
    private boolean usingJSON = false;

    /**
     * Sets initial Data and Retrieves Valid Translations
     */
    public TranslationUtils() {
        setLanguage(CraftPresence.CONFIG != null ? CraftPresence.CONFIG.languageID : languageID);
        setUsingJSON(false);
        getTranslationMap();
        checkUnicode();
    }

    /**
     * Sets initial Data and Retrieves Valid Translations
     *
     * @param useJSON Toggles whether to use .Json or .Lang, if present
     */
    public TranslationUtils(final boolean useJSON) {
        setLanguage(CraftPresence.CONFIG != null ? CraftPresence.CONFIG.languageID : languageID);
        setUsingJSON(useJSON);
        getTranslationMap();
        checkUnicode();
    }

    /**
     * Sets initial Data and Retrieves Valid Translations
     *
     * @param modID Sets the Target Mod ID to locate Language Files
     */
    public TranslationUtils(final String modID) {
        setLanguage(CraftPresence.CONFIG != null ? CraftPresence.CONFIG.languageID : languageID);
        setModID(modID);
        setUsingJSON(false);
        getTranslationMap();
        checkUnicode();
    }

    /**
     * Sets initial Data and Retrieves Valid Translations
     *
     * @param modID Sets the Target Mod ID to locate Language Files
     * @param useJSON Toggles whether to use .Json or .Lang, if present
     */
    public TranslationUtils(final String modID, final boolean useJSON) {
        setLanguage(CraftPresence.CONFIG != null ? CraftPresence.CONFIG.languageID : languageID);
        setModID(modID);
        setUsingJSON(useJSON);
        getTranslationMap();
        checkUnicode();
    }

    /**
     * The Event to Run on each Client Tick, if passed initialization events
     * <p>
     * Comprises of Synchronizing Data, and Updating Translation Data as needed
     */
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

    /**
     * Determines whether the translations contain Unicode Characters
     */
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

    /**
     * Toggles whether to use .Lang or .Json Language Files
     *
     * @param usingJSON Toggles whether to use .Json or .Lang, if present
     */
    private void setUsingJSON(final boolean usingJSON) {
        this.usingJSON = usingJSON;
    }

    /**
     * Sets the Language ID to Retrieve Translations for, if present
     *
     * @param languageID The Language ID (Default: en_US)
     */
    private void setLanguage(final String languageID) {
        if (!StringUtils.isNullOrEmpty(languageID)) {
            this.languageID = languageID;
        } else {
            this.languageID = "en_US";
        }
    }

    /**
     * Sets the Mod ID to target when locating Language Files
     *
     * @param modID The Mod ID to target
     */
    private void setModID(final String modID) {
        if (!StringUtils.isNullOrEmpty(modID)) {
            this.modID = modID;
        } else {
            this.modID = null;
        }
    }

    /**
     * Retrieves and Synchronizes a List of Translations from a Language File
     */
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

    /**
     * Translates an Unlocalized String, based on the Translations retrieved
     *
     * @param stripColors Whether to Remove Color and Formatting Codes
     * @param translationKey The unLocalized String to translate
     * @param parameters Extra Formatting Arguments, if needed
     * @return The Localized Translated String
     */
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

    /**
     * Translates an Unlocalized String, based on the Translations retrieved
     *
     * @param translationKey The unLocalized String to translate
     * @param parameters Extra Formatting Arguments, if needed
     * @return The Localized Translated String
     */
    public String translate(String translationKey, Object... parameters) {
        return translate(CraftPresence.CONFIG != null && CraftPresence.CONFIG.stripTranslationColors, translationKey, parameters);
    }
}
