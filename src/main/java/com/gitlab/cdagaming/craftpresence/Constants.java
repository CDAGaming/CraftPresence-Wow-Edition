package com.gitlab.cdagaming.craftpresence;

import com.google.common.collect.Lists;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.realms.RealmsSharedConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
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
    public static final String UPDATE_JSON = "https://gitlab.com/CDAGaming/VersionLibrary/raw/master/CraftPresence/update.json";
    public static final String FINGERPRINT = "GRADLE:certFingerprint";
    public static final Logger LOG = LogManager.getLogger(MODID);
    public static final boolean IS_DEV = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    protected static void loadDLL(final boolean Update) {
        boolean UpdateStatus = Update;
        if (SystemUtils.IS_OS_WINDOWS) {
            final List<String> x64 = Lists.newArrayList("amd64", "x86_64");
            final List<String> x86 = Lists.newArrayList("x86", "i386", "i486", "i586", "i686");
            if (x64.contains(SystemUtils.OS_ARCH)) {
                try {
                    final URL WIN_x86_64 = new URL("https://gitlab.com/CDAGaming/VersionLibrary/raw/master/CraftPresence/resources/DLL/win32-x86-64/discord-rpc.dll");
                    final File file = new File(Constants.MODID + File.separator + "discord-rpc.dll");
                    if (file.exists() && UpdateStatus) {
                        final boolean fileDeleted = file.delete();
                        if (!fileDeleted) {
                            Constants.LOG.error("Failed to Delete " + file.getName());
                        }
                    } else if (!file.exists()) {
                        UpdateStatus = true;
                    }

                    if (UpdateStatus) {
                        FileUtils.copyURLToFile(WIN_x86_64, file);
                    }

                    try {
                        System.load(file.getAbsolutePath());
                    } catch (Exception ex) {
                        boolean isPermsSet = file.setReadable(true) && file.setWritable(true);
                        if (isPermsSet) {
                            System.load(file.getAbsolutePath());
                        }
                    }
                } catch (Exception ex) {
                    Constants.LOG.error("Unable to retrieve DiscordRPC DLL for " + SystemUtils.OS_NAME + " ( Arch: " + SystemUtils.OS_ARCH + " )");
                    ex.printStackTrace();
                }
            }
            if (x86.contains(SystemUtils.OS_ARCH)) {
                try {
                    final URL WIN_x86 = new URL("https://gitlab.com/CDAGaming/VersionLibrary/raw/master/CraftPresence/resources/DLL/win32-x86/discord-rpc.dll");
                    final File file = new File(Constants.MODID + File.separator + "discord-rpc.dll");
                    if (file.exists() && UpdateStatus) {
                        final boolean fileDeleted = file.delete();
                        if (!fileDeleted) {
                            Constants.LOG.error("Failed to Delete " + file.getName());
                        }
                    } else if (!file.exists()) {
                        UpdateStatus = true;
                    }

                    if (UpdateStatus) {
                        FileUtils.copyURLToFile(WIN_x86, file);
                    }

                    try {
                        System.load(file.getAbsolutePath());
                    } catch (Exception ex) {
                        boolean isPermsSet = file.setReadable(true) && file.setWritable(true);
                        if (isPermsSet) {
                            System.load(file.getAbsolutePath());
                        }
                    }
                } catch (Exception ex) {
                    Constants.LOG.error("Unable to retrieve DiscordRPC DLL for " + SystemUtils.OS_NAME + " ( Arch: " + SystemUtils.OS_ARCH + " )");
                    ex.printStackTrace();
                }
            }
        }
        if (SystemUtils.IS_OS_LINUX) {
            try {
                final URL LINUX = new URL("https://gitlab.com/CDAGaming/VersionLibrary/raw/master/CraftPresence/resources/DLL/linux-x86-64/libdiscord-rpc.so");
                final File file = new File(Constants.MODID + File.separator + "libdiscord-rpc.so");
                if (file.exists() && UpdateStatus) {
                    final boolean fileDeleted = file.delete();
                    if (!fileDeleted) {
                        Constants.LOG.error("Failed to Delete " + file.getName());
                    }
                } else if (!file.exists()) {
                    UpdateStatus = true;
                }

                if (UpdateStatus) {
                    FileUtils.copyURLToFile(LINUX, file);
                }

                try {
                    System.load(file.getAbsolutePath());
                } catch (Exception ex) {
                    boolean isPermsSet = file.setReadable(true) && file.setWritable(true);
                    if (isPermsSet) {
                        System.load(file.getAbsolutePath());
                    }
                }
            } catch (Exception ex) {
                Constants.LOG.error("Unable to retrieve DiscordRPC DLL for " + SystemUtils.OS_NAME + " ( Arch: " + SystemUtils.OS_ARCH + " )");
                ex.printStackTrace();
            }
        }
        if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
            try {
                final URL MAC_OS = new URL("https://gitlab.com/CDAGaming/VersionLibrary/raw/master/CraftPresence/resources/DLL/darwin/libdiscord-rpc.dylib");
                final File file = new File(Constants.MODID + File.separator + "libdiscord-rpc.dylib");
                if (file.exists() && UpdateStatus) {
                    final boolean fileDeleted = file.delete();
                    if (!fileDeleted) {
                        Constants.LOG.error("Failed to Delete " + file.getName());
                    }
                } else if (!file.exists()) {
                    UpdateStatus = true;
                }

                if (UpdateStatus) {
                    FileUtils.copyURLToFile(MAC_OS, file);
                }

                try {
                    System.load(file.getAbsolutePath());
                } catch (Exception ex) {
                    boolean isPermsSet = file.setReadable(true) && file.setWritable(true);
                    if (isPermsSet) {
                        System.load(file.getAbsolutePath());
                    }
                }
            } catch (Exception ex) {
                Constants.LOG.error("Unable to retrieve DiscordRPC DLL for " + SystemUtils.OS_NAME + " ( Arch: " + SystemUtils.OS_ARCH + " )");
                ex.printStackTrace();
            }
        }
    }
}
