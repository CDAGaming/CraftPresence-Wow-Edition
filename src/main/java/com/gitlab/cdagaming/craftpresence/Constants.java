package com.gitlab.cdagaming.craftpresence;

import com.gitlab.cdagaming.craftpresence.handler.FileHandler;
import com.google.common.collect.Lists;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.realms.RealmsSharedConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public class Constants {
    public static final String NAME = "GRADLE:mod_name";
    public static final String majorVersion = "GRADLE:majorVersion";
    public static final String minorVersion = "GRADLE:minorVersion";
    public static final String revisionVersion = "GRADLE:revisionVersion";
    public static final String VERSION_ID = "v" + majorVersion + "." + minorVersion + "." + revisionVersion;
    public static final String MODID = "craftpresence";
    public static final String GUI_FACTORY = "com.gitlab.cdagaming.craftpresence.config.ConfigGUIFactoryDS";
    public static final String MCVersion = RealmsSharedConstants.VERSION_STRING;
    public static final String BRAND = ClientBrandRetriever.getClientModName();
    public static final String configDir = Minecraft.getMinecraft().gameDir + File.separator + "config";
    public static final String modsDir = Minecraft.getMinecraft().gameDir + File.separator + "mods";
    public static final String USERNAME = Minecraft.getMinecraft().getSession().getUsername();
    public static final String UPDATE_JSON = "https://gitlab.com/CDAGaming/VersionLibrary/raw/master/CraftPresence/update.json";
    public static final String FINGERPRINT = "GRADLE:certFingerprint";
    public static final Logger LOG = LogManager.getLogger(MODID);
    public static final boolean IS_DEV = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    static void loadDLL(final boolean Update) {
        boolean UpdateStatus = Update;

        final List<String> x64 = Lists.newArrayList("amd64", "x86_64");
        final boolean is64Bit = x64.contains(CraftPresence.SYSTEM.OS_ARCH);

        final String fileName = (CraftPresence.SYSTEM.IS_WINDOWS ? "discord-rpc.dll"
                : CraftPresence.SYSTEM.IS_LINUX ? "libdiscord-rpc.so"
                : CraftPresence.SYSTEM.IS_MAC ? "libdiscord-rpc.dylib" : "");
        final String url = "https://gitlab.com/CDAGaming/VersionLibrary/raw/master/CraftPresence/resources/DLL/" + (CraftPresence.SYSTEM.IS_WINDOWS ? (is64Bit ? "win32-x86-64" : "win32-x86")
                : CraftPresence.SYSTEM.IS_LINUX ? "linux-x86-64"
                : CraftPresence.SYSTEM.IS_MAC ? "darwin" : "") + "/" + fileName;
        final File file = new File(MODID + File.separator + fileName);
        UpdateStatus = UpdateStatus || !file.exists();

        if (UpdateStatus) {
            FileHandler.downloadFile(url, file);
        }
        FileHandler.loadFileAsDLL(file);
    }
}
