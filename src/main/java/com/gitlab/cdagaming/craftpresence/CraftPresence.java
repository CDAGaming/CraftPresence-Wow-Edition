package com.gitlab.cdagaming.craftpresence;

import com.gitlab.cdagaming.craftpresence.config.ConfigHandler;
import com.gitlab.cdagaming.craftpresence.handler.CommandHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.URLHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.DiscordHandler;
import com.gitlab.cdagaming.craftpresence.handler.entity.EntityHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.GUIHandler;
import com.gitlab.cdagaming.craftpresence.handler.server.ServerHandler;
import com.gitlab.cdagaming.craftpresence.handler.world.BiomeHandler;
import com.gitlab.cdagaming.craftpresence.handler.world.DimensionHandler;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

import java.io.File;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION_ID, clientSideOnly = true, guiFactory = Constants.GUI_FACTORY, canBeDeactivated = true, updateJSON = Constants.UPDATE_JSON, certificateFingerprint = Constants.FINGERPRINT, acceptedMinecraftVersions = "*")
public class CraftPresence {
    public static DiscordHandler CLIENT;

    public static ConfigHandler CONFIG;
    public static ServerHandler SERVER = new ServerHandler();
    public static BiomeHandler BIOMES = new BiomeHandler();
    public static DimensionHandler DIMENSIONS = new DimensionHandler();
    public static EntityHandler ENTITIES = new EntityHandler();
    public static GUIHandler GUIS = new GUIHandler();

    @Mod.EventHandler
    public void disableMod(final FMLModDisabledEvent event) {
        CLIENT.shutDown();
    }

    @Mod.EventHandler
    public void onFingerprintViolation(final FMLFingerprintViolationEvent event) {
        if (!Constants.IS_DEV)
            Constants.LOG.warn(I18n.format("craftpresence.logger.warning.fingerprintviolation"));
    }

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(BIOMES);
        MinecraftForge.EVENT_BUS.register(DIMENSIONS);
        MinecraftForge.EVENT_BUS.register(ENTITIES);
        MinecraftForge.EVENT_BUS.register(GUIS);
        MinecraftForge.EVENT_BUS.register(SERVER);

        CONFIG = new ConfigHandler(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + Constants.MODID + ".properties");
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        CONFIG.initialize();

        CommandHandler.reloadData();
        CommandHandler.init();

        final ForgeVersion.Status UPDATE_STATUS = ForgeVersion.getResult(Loader.instance().activeModContainer()).status;
        final File CP_DIR = new File(Constants.MODID);
        URLHandler.acceptCertificates();
        if (UPDATE_STATUS == ForgeVersion.Status.OUTDATED || UPDATE_STATUS == ForgeVersion.Status.BETA_OUTDATED || (!CP_DIR.exists() || CP_DIR.listFiles() == null)) {
            Constants.loadDLL(true);
        } else {
            Constants.loadDLL(false);
        }

        try {
            CLIENT = new DiscordHandler(!StringHandler.isNullOrEmpty(CONFIG.clientID) ? CONFIG.clientID : "450485984333660181");
            CLIENT.init();
            CLIENT.updateTimestamp();
            CommandHandler.setLoadingPresence(event.getModState());
        } catch (Exception e) {
            Constants.LOG.error(I18n.format("craftpresence.logger.error.load"));
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        CommandHandler.setMainMenuPresence();
    }
}
