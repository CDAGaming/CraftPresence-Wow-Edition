package com.gitlab.cdagaming.craftpresence;

import com.gitlab.cdagaming.craftpresence.utils.FileUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.TranslationUtils;
import com.google.common.collect.Lists;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.realms.RealmsSharedConstants;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ModUtils {
    public static final String NAME = "GRADLE:mod_name";
    public static final String majorVersion = "GRADLE:majorVersion";
    public static final String minorVersion = "GRADLE:minorVersion";
    public static final String revisionVersion = "GRADLE:revisionVersion";
    public static final String VERSION_ID = "v" + majorVersion + "." + minorVersion + "." + revisionVersion;
    public static final String MODID = "craftpresence";
    public static final String GUI_FACTORY = "com.gitlab.cdagaming.craftpresence.config.ConfigGuiDataFactory";
    public static final String MCVersion = RealmsSharedConstants.VERSION_STRING;
    public static final String BRAND = ClientBrandRetriever.getClientModName();
    public static final String configDir = CraftPresence.SYSTEM.USER_DIR + File.separator + "config";
    public static final String modsDir = CraftPresence.SYSTEM.USER_DIR + File.separator + "mods";
    public static final String USERNAME = Minecraft.getMinecraft().getSession().getUsername();
    public static final String UPDATE_JSON = "https://gitlab.com/CDAGaming/VersionLibrary/raw/master/CraftPresence/update.json";
    public static final String FINGERPRINT = "GRADLE:certFingerprint";
    public static final ModLogger LOG = new ModLogger(MODID);
    public static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();
    public static final TranslationUtils TRANSLATOR = new TranslationUtils(MODID, false);
    public static final boolean IS_DEV = (Launch.blackboard != null && !Launch.blackboard.isEmpty() && Launch.blackboard.containsKey("fml.deobfuscatedEnvironment")) && (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    public static boolean forceBlockTooltipRendering = false;

    static void loadDLL(final boolean Update) {
        boolean UpdateStatus = Update;

        final String fileName = (CraftPresence.SYSTEM.IS_WINDOWS ? "discord-rpc.dll"
                : CraftPresence.SYSTEM.IS_LINUX ? "libdiscord-rpc.so"
                : CraftPresence.SYSTEM.IS_MAC ? "libdiscord-rpc.dylib" : "");
        final String url = "https://gitlab.com/CDAGaming/VersionLibrary/raw/master/CraftPresence/resources/DLL/" + (CraftPresence.SYSTEM.IS_WINDOWS ? (CraftPresence.SYSTEM.IS_64_BIT ? "win32-x86-64" : "win32-x86")
                : CraftPresence.SYSTEM.IS_LINUX ? "linux-x86-64"
                : CraftPresence.SYSTEM.IS_MAC ? "darwin" : "") + "/" + fileName;
        final File file = new File(MODID + File.separator + fileName);
        UpdateStatus = UpdateStatus || !file.exists();

        if (UpdateStatus) {
            FileUtils.downloadFile(url, file);
        }
        FileUtils.loadFileAsDLL(file);
    }

    public static void loadCharData(final boolean Update) {
        LOG.info(TRANSLATOR.translate(true, "craftpresence.logger.info.chardata.init"));
        final String fileName = "chardata.properties", charDataPath = "/assets/" + MODID + "/" + fileName;
        final File charDataDir = new File(MODID + File.separator + fileName);
        boolean UpdateStatus = Update || !charDataDir.exists(), errored = false;
        InputStream inputData = null;
        InputStreamReader inputStream = null;
        OutputStream outputData = null;
        BufferedReader reader = null;

        if (UpdateStatus) {
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
                inputStream = new InputStreamReader(inputData, StandardCharsets.UTF_8);
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
                loadCharData(true);
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

    public static void writeToCharData() {
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
                inputStream = new InputStreamReader(inputData, StandardCharsets.UTF_8);
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
                outputStream = new OutputStreamWriter(outputData, StandardCharsets.UTF_8);
                bw = new BufferedWriter(outputStream);

                if (!textData.isEmpty()) {
                    for (String lineInput : textData) {
                        bw.write(lineInput);
                        bw.newLine();
                    }
                } else {
                    // If charWidth and glyphWidth don't exist, Reset Character Data
                    loadCharData(true);
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
            loadCharData(true);
        }
    }
}
