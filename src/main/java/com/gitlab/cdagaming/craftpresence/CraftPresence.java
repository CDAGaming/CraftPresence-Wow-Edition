package com.gitlab.cdagaming.craftpresence;

import com.gitlab.cdagaming.craftpresence.config.ConfigHandler;
import com.gitlab.cdagaming.craftpresence.handler.CommandHandler;
import com.gitlab.cdagaming.craftpresence.handler.KeyHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.DiscordHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.rpc.DiscordRPC;
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
    public static boolean packFound = false, awaitingReply = false;
    public static Minecraft instance = Minecraft.getMinecraft();
    public static EntityPlayer player = instance.player;
    public static int TIMER = 0;

    public static ConfigHandler CONFIG;
    public static KeyHandler KEYBINDINGS = new KeyHandler();
    public static DiscordHandler CLIENT = new DiscordHandler();
    public static ServerHandler SERVER = new ServerHandler();
    public static BiomeHandler BIOMES = new BiomeHandler();
    public static DimensionHandler DIMENSIONS = new DimensionHandler();
    public static EntityHandler ENTITIES = new EntityHandler();
    public static GUIHandler GUIS = new GUIHandler();

    private static Thread timerThread = new Thread("CraftPresence-Timer") {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (TIMER > 0) {
                    TIMER--;
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    };

    static {
        timerThread.start();
    }

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
        Constants.loadDLL(!CP_DIR.exists() || CP_DIR.listFiles() == null);

        try {
            CLIENT.CLIENT_ID = CONFIG.clientID;
            CLIENT.setup();
            CLIENT.init();
            CLIENT.updateTimestamp();
            CommandHandler.setLoadingPresence(event.getClass().getSimpleName());
        } catch (Exception ex) {
            Constants.LOG.error(I18n.format("craftpresence.logger.error.load"));
            ex.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        CommandHandler.setLoadingPresence(event.getClass().getSimpleName());
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        CommandHandler.reloadData();

        if (CraftPresence.CONFIG.showCurrentDimension && CraftPresence.DIMENSIONS.DIMENSION_NAMES.isEmpty()) {
            CraftPresence.DIMENSIONS.getDimensions();
        }
        if (CraftPresence.CONFIG.showCurrentBiome && CraftPresence.BIOMES.BIOME_NAMES.isEmpty()) {
            CraftPresence.BIOMES.getBiomes();
        }
        if (CraftPresence.CONFIG.enablePERGUI && CraftPresence.GUIS.GUI_NAMES.isEmpty()) {
            CraftPresence.GUIS.getGUIs();
        }
        if (CraftPresence.CONFIG.enablePERItem && CraftPresence.ENTITIES.ENTITY_NAMES.isEmpty()) {
            CraftPresence.ENTITIES.getEntities();
        }
        if (CraftPresence.CONFIG.showGameState && CraftPresence.SERVER.knownAddresses.isEmpty()) {
            CraftPresence.SERVER.getServerAddresses();
        }

        if (!CONFIG.hasChanged) {
            if ((!CommandHandler.isOnMainMenuPresence() && player == null) && (!DIMENSIONS.isInUse && !BIOMES.isInUse && !GUIS.isInUse && !ENTITIES.isInUse && !SERVER.isInUse)) {
                CommandHandler.setMainMenuPresence();
            }

            if (CONFIG.rebootOnWorldLoad && player != null) {
                CommandHandler.rebootRPC();
                CONFIG.rebootOnWorldLoad = false;
            }

            if (awaitingReply && TIMER == 0) {
                StringHandler.sendMessageToPlayer(player, I18n.format("craftpresence.command.request.ignored", CLIENT.REQUESTER_USER.username));
                DiscordRPC.INSTANCE.Discord_Respond(CLIENT.REQUESTER_USER.userId, DiscordRPC.DISCORD_REPLY_IGNORE);
                awaitingReply = false;
                CLIENT.STATUS = "ready";
            } else if (!awaitingReply && CLIENT.REQUESTER_USER != null) {
                CLIENT.REQUESTER_USER = null;
                CLIENT.STATUS = "ready";
            }
        }
    }
}
