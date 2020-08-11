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
package com.gitlab.cdagaming.craftpresence;

import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.TranslationUtils;
import com.gitlab.cdagaming.craftpresence.utils.updater.ModUpdaterUtils;
import com.google.common.collect.Lists;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.realms.RealmsSharedConstants;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Constant Variables and Methods used throughout the Application
 *
 * @author CDAGaming
 */
@SuppressWarnings("DuplicatedCode")
public class ModUtils {
    /**
     * The Application's Name
     */
    public static final String NAME = "@NAME@";

    /**
     * The Application's Major Revision Number (Ex: 1 in 1.0.2)
     */
    public static final String majorVersion = "@MAJOR_VERSION@";

    /**
     * The Application's Minor Revision Number (Ex: 0 in 1.0.2)
     */
    public static final String minorVersion = "@MINOR_VERSION@";

    /**
     * The Application's Revision Version Number (Ex: 2 in 1.0.2)
     */
    public static final String revisionVersion = "@REVISION_VERSION@";

    /**
     * The Application's Formatted Version ID
     */
    public static final String VERSION_ID = "v" + majorVersion + "." + minorVersion + "." + revisionVersion;

    /**
     * The Application's Identifier
     */
    public static final String MODID = "craftpresence";

    /**
     * The Application's Configuration Schema Version ID
     */
    public static final int MOD_SCHEMA_VERSION = 1;

    /**
     * The Application's GUI Factory, if any
     */
    public static final String GUI_FACTORY = "com.gitlab.cdagaming.craftpresence.config.ConfigGuiDataFactory";

    /**
     * The Detected Minecraft Version
     */
    public static final String MCVersion = RealmsSharedConstants.VERSION_STRING;

    /**
     * The Detected Minecraft Protocol Version
     */
    public static final int MCProtocolID = RealmsSharedConstants.NETWORK_PROTOCOL_VERSION;

    /**
     * The Detected Brand Information within Minecraft
     */
    public static final String BRAND = ClientBrandRetriever.getClientModName();

    /**
     * The Application's Configuration Directory
     */
    public static final String configDir = CraftPresence.SYSTEM.USER_DIR + File.separator + "config";

    /**
     * The Application's "mods" Directory
     */
    public static final String modsDir = CraftPresence.SYSTEM.USER_DIR + File.separator + "mods";

    /**
     * The Detected Username within Minecraft
     */
    public static final String USERNAME = Minecraft.getMinecraft().getSession().getUsername();

    /**
     * The URL to receive Update Information from
     */
    public static final String UPDATE_JSON = "https://raw.githubusercontent.com/CDAGaming/VersionLibrary/master/CraftPresence/update.json";

    /**
     * The Certificate Fingerprint, assigned in CI, to check against for violations
     */
    public static final String FINGERPRINT = "@FINGERPRINT@";

    /**
     * The Application's Instance of {@link ModLogger} for Logging Information
     */
    public static final ModLogger LOG = new ModLogger(MODID);

    /**
     * The Current Thread's Class Loader, used to dynamically receive data as needed
     */
    public static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();

    /**
     * The Application's Instance of {@link TranslationUtils} for Localization and Translating Data Strings
     */
    public static final TranslationUtils TRANSLATOR = new TranslationUtils(MODID, false);

    /**
     * The Application's Instance of {@link ModUpdaterUtils} for Retrieving if the Application has an update
     */
    public static final ModUpdaterUtils UPDATER = new ModUpdaterUtils(MODID, UPDATE_JSON, VERSION_ID);

    /**
     * If this Application is running/needs Legacy Data
     */
    public static final boolean IS_LEGACY = false;

    /**
     * Whether to forcibly block any tooltips related to this Application from rendering
     */
    public static boolean forceBlockTooltipRendering = false;

    /**
     * If this Application should be run in a Developer or Debug State
     */
    public static boolean IS_DEV = false;

    /**
     * If this Application is running in a de-obfuscated or Developer environment
     */
    public static boolean IS_VERBOSE = (Launch.blackboard != null && !Launch.blackboard.isEmpty() && Launch.blackboard.containsKey("fml.deobfuscatedEnvironment")) && (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    /**
     * Retrieves and Initializes Character Data<p>
     * Primarily used for ensuring proper Tooltip Rendering
     *
     * @param Update   Whether to Force Update and Initialization of Character Data
     * @param encoding The Charset Encoding to parse character data in
     */
    public static void loadCharData(final boolean Update, final String encoding) {
        LOG.info(TRANSLATOR.translate(true, "craftpresence.logger.info.chardata.init"));
        final String fileName = "chardata.properties", charDataPath = "/assets/" + MODID + "/" + fileName;
        final File charDataDir = new File(MODID + File.separator + fileName);
        boolean UpdateStatus = Update || !charDataDir.exists(), errored = false;
        InputStream inputData = null;
        InputStreamReader inputStream = null;
        OutputStream outputData = null;
        BufferedReader reader = null;

        if (UpdateStatus) {
            // Create Data Directory if non-existent
            if (!charDataDir.getParentFile().exists() && !charDataDir.getParentFile().mkdirs()) {
                errored = true;
            }

            LOG.info(TRANSLATOR.translate(true, "craftpresence.logger.info.download.init", fileName, charDataDir.getAbsolutePath(), charDataPath));
            inputData = StringUtils.getResourceAsStream(ModUtils.class, charDataPath);

            // Write Data from Local charData to Directory if Update is needed
            if (inputData != null) {
                try {
                    outputData = new FileOutputStream(charDataDir);

                    byte[] transferBuffer = new byte[inputData.available()];
                    for (int readBuffer = inputData.read(transferBuffer); readBuffer != -1; readBuffer = inputData.read(transferBuffer)) {
                        outputData.write(transferBuffer, 0, readBuffer);
                    }

                    LOG.info(TRANSLATOR.translate(true, "craftpresence.logger.info.download.loaded", fileName, charDataDir.getAbsolutePath(), charDataPath));
                } catch (Exception ex) {
                    errored = true;
                }
            } else {
                errored = true;
            }
        }

        if (!errored) {
            try {
                inputData = new FileInputStream(charDataDir);
                inputStream = new InputStreamReader(inputData, Charset.forName(encoding));
                reader = new BufferedReader(inputStream);

                String currentString;
                while ((currentString = reader.readLine()) != null) {
                    String[] localWidths;
                    currentString = currentString.trim();

                    if (!currentString.startsWith("=") && currentString.contains("=")) {
                        String[] splitString = currentString.split("=", 2);

                        if (splitString[0].equalsIgnoreCase("charWidth")) {
                            localWidths = splitString[1].replaceAll("\\[", "").replaceAll("]", "").split(", ");

                            for (int i = 0; i < localWidths.length && i <= 256; i++) {
                                StringUtils.MC_CHAR_WIDTH[i] = Integer.parseInt(localWidths[i].trim());
                            }
                        } else if (splitString[0].equalsIgnoreCase("glyphWidth")) {
                            localWidths = splitString[1].replaceAll("\\[", "").replaceAll("]", "").split(", ");

                            for (int i = 0; i < localWidths.length && i <= 65536; i++) {
                                StringUtils.MC_GLYPH_WIDTH[i] = Byte.parseByte(localWidths[i].trim());
                            }
                        }
                    }
                }

                if (Arrays.equals(StringUtils.MC_CHAR_WIDTH, new int[256]) || Arrays.equals(StringUtils.MC_GLYPH_WIDTH, new byte[65536])) {
                    errored = true;
                }
            } catch (Exception ex) {
                loadCharData(true, "UTF-8");
            }
        }

        try {
            if (reader != null) {
                reader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (inputData != null) {
                inputData.close();
            }
            if (outputData != null) {
                outputData.close();
            }
        } catch (Exception ex) {
            LOG.error(TRANSLATOR.translate(true, "craftpresence.logger.error.dataclose"));
            ex.printStackTrace();
        } finally {
            if (errored) {
                LOG.error(TRANSLATOR.translate(true, "craftpresence.logger.error.chardata"));
                forceBlockTooltipRendering = true;
            } else {
                LOG.info(TRANSLATOR.translate(true, "craftpresence.logger.info.chardata.loaded"));
                forceBlockTooltipRendering = false;
            }
        }
    }

    /**
     * Saves and Synchronizes Current Character Data
     *
     * @param encoding The Charset Encoding to write character data in
     */
    public static void writeToCharData(final String encoding) {
        List<String> textData = Lists.newArrayList();
        InputStream inputData = null;
        InputStreamReader inputStream = null;
        OutputStream outputData = null;
        OutputStreamWriter outputStream = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        final File charDataDir = new File(MODID + File.separator + "chardata.properties");

        if (charDataDir.exists()) {
            try {
                // Read and Queue Character Data
                inputData = new FileInputStream(charDataDir);
                inputStream = new InputStreamReader(inputData, Charset.forName(encoding));
                br = new BufferedReader(inputStream);

                String currentString;
                while (!StringUtils.isNullOrEmpty((currentString = br.readLine()))) {
                    if (currentString.contains("=")) {
                        if (currentString.toLowerCase().startsWith("charwidth")) {
                            textData.add("charWidth=" + Arrays.toString(StringUtils.MC_CHAR_WIDTH));
                        } else if (currentString.toLowerCase().startsWith("glyphwidth")) {
                            textData.add("glyphWidth=" + Arrays.toString(StringUtils.MC_GLYPH_WIDTH));
                        }
                    }
                }

                // Write Queued Character Data
                outputData = new FileOutputStream(charDataDir);
                outputStream = new OutputStreamWriter(outputData, Charset.forName(encoding));
                bw = new BufferedWriter(outputStream);

                if (!textData.isEmpty()) {
                    for (String lineInput : textData) {
                        bw.write(lineInput);
                        bw.newLine();
                    }
                } else {
                    // If charWidth and glyphWidth don't exist, Reset Character Data
                    loadCharData(true, "UTF-8");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                    if (bw != null) {
                        bw.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (inputData != null) {
                        inputData.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (outputData != null) {
                        outputData.close();
                    }
                } catch (Exception ex) {
                    LOG.error(TRANSLATOR.translate(true, "craftpresence.logger.error.dataclose"));
                    ex.printStackTrace();
                }
            }
        } else {
            loadCharData(true, "UTF-8");
        }
    }
}
