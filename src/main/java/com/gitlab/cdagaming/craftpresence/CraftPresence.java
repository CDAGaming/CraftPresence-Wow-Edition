package com.gitlab.cdagaming.craftpresence;

import com.gitlab.cdagaming.craftpresence.config.ConfigHandler;
import com.gitlab.cdagaming.craftpresence.handler.CommandHandler;
import com.gitlab.cdagaming.craftpresence.handler.URLHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.DiscordHandler;
import com.gitlab.cdagaming.craftpresence.handler.entity.EntityHandler;
import com.gitlab.cdagaming.craftpresence.handler.gui.GUIHandler;
import com.gitlab.cdagaming.craftpresence.handler.server.ServerHandler;
import com.gitlab.cdagaming.craftpresence.handler.world.BiomeHandler;
import com.gitlab.cdagaming.craftpresence.handler.world.DimensionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION_ID, clientSideOnly = true, guiFactory = Constants.GUI_FACTORY, canBeDeactivated = true, updateJSON = Constants.UPDATE_JSON, certificateFingerprint = Constants.FINGERPRINT, acceptedMinecraftVersions = "*")
public class CraftPresence {
    public static boolean packFound = false;
    public static Minecraft instance;
    public static EntityPlayer player;

    public static ConfigHandler CONFIG;
    public static DiscordHandler CLIENT = new DiscordHandler();
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

        CONFIG = new ConfigHandler(Constants.configDir + File.separator + Constants.MODID + ".properties");
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        if (Constants.IS_DEV) {
            Constants.LOG.warn(I18n.format("craftpresence.logger.warning.debugmode"));
        }
        CONFIG.initialize();

        CommandHandler.init();
        CommandHandler.reloadData();

        final File CP_DIR = new File(Constants.MODID);
        URLHandler.acceptCertificates();
        Constants.loadDLL(!CP_DIR.exists() || CP_DIR.listFiles() == null);

        try {
            CLIENT.CLIENT_ID = CONFIG.clientID;
            CLIENT.setup();
            CLIENT.init();
            CLIENT.updateTimestamp();
            CommandHandler.setLoadingPresence(event.getModState());
        } catch (Exception ex) {
            Constants.LOG.error(I18n.format("craftpresence.logger.error.load"));
            ex.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        CommandHandler.setLoadingPresence(event.getModState());
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        CommandHandler.reloadData();

        if ((!CommandHandler.isOnMainMenuPresence() && player == null) && (!DIMENSIONS.isInUse && !BIOMES.isInUse && !GUIS.isInUse && !ENTITIES.isInUse && !SERVER.isInUse)) {
            CommandHandler.setMainMenuPresence();
        }

        if (CraftPresence.CONFIG.rebootOnWorldLoad && player != null) {
            CommandHandler.rebootRPC();
            CraftPresence.CONFIG.rebootOnWorldLoad = false;
        }
    }
}
