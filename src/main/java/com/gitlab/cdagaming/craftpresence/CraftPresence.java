package com.gitlab.cdagaming.craftpresence;

import com.gitlab.cdagaming.craftpresence.config.ConfigUtils;
import com.gitlab.cdagaming.craftpresence.utils.CommandUtils;
import com.gitlab.cdagaming.craftpresence.utils.KeyUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.SystemUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.DiscordUtils;
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.IPCClient;
import com.gitlab.cdagaming.craftpresence.utils.entity.EntityUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.GuiUtils;
import com.gitlab.cdagaming.craftpresence.utils.server.ServerUtils;
import com.gitlab.cdagaming.craftpresence.utils.world.BiomeUtils;
import com.gitlab.cdagaming.craftpresence.utils.world.DimensionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

@Mod(modid = ModUtils.MODID, name = ModUtils.NAME, version = ModUtils.VERSION_ID, clientSideOnly = true, guiFactory = ModUtils.GUI_FACTORY, canBeDeactivated = true, updateJSON = ModUtils.UPDATE_JSON, certificateFingerprint = ModUtils.FINGERPRINT, acceptedMinecraftVersions = "*")
public class CraftPresence {
    public static boolean packFound = false, awaitingReply = false, closing = false;
    public static Minecraft instance = Minecraft.getMinecraft();
    public static EntityPlayer player = instance.player;

    public static ConfigUtils CONFIG;
    public static SystemUtils SYSTEM = new SystemUtils();
    public static KeyUtils KEYBINDINGS = new KeyUtils();
    public static DiscordUtils CLIENT = new DiscordUtils();
    public static ServerUtils SERVER = new ServerUtils();
    public static BiomeUtils BIOMES = new BiomeUtils();
    public static DimensionUtils DIMENSIONS = new DimensionUtils();
    public static EntityUtils ENTITIES = new EntityUtils();
    public static GuiUtils GUIS = new GuiUtils();
    public static Timer timerObj = new Timer(CraftPresence.class.getSimpleName());

    private boolean initialized = false;

    public CraftPresence() {
        scheduleTick();
    }

    private void init() {
        // If running in Developer Mode, Warn of Possible Issues and Log OS Info
        if (ModUtils.IS_DEV) {
            ModUtils.LOG.warn(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.warning.debugmode"));
            ModUtils.LOG.info(ModUtils.TRANSLATOR.translate(true, "craftpresence.logger.info.os", SYSTEM.OS_NAME, SYSTEM.OS_ARCH, SYSTEM.IS_64_BIT));
        }

        SYSTEM = new SystemUtils();
        CONFIG = new ConfigUtils(ModUtils.configDir + File.separator + ModUtils.MODID + ".properties");
        CONFIG.initialize();

        final File CP_DIR = new File(ModUtils.MODID);
        ModUtils.loadCharData(!CP_DIR.exists() || CP_DIR.listFiles() == null);

        CommandUtils.init();

        try {
            CLIENT.CLIENT_ID = CONFIG.clientID;
            CLIENT.setup();
            CLIENT.init();
            CLIENT.updateTimestamp();
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.load"));
            ex.printStackTrace();
        } finally {
            initialized = true;
        }
    }

    private void scheduleTick() {
        if (!closing) {
            timerObj.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            clientTick();
                        }
                    },
                    50
            );
        }
    }

    private void clientTick() {
        if (initialized) {
            instance = Minecraft.getMinecraft();
            player = instance.player;

            CommandUtils.reloadData(false);

            if (CONFIG.showCurrentDimension && DIMENSIONS.DIMENSION_NAMES.isEmpty()) {
                DIMENSIONS.getDimensions();
            }
            if (CONFIG.showCurrentBiome && BIOMES.BIOME_NAMES.isEmpty()) {
                BIOMES.getBiomes();
            }
            if (CONFIG.enablePERGUI && GUIS.GUI_NAMES.isEmpty()) {
                GUIS.getGUIs();
            }
            if (CONFIG.enablePERItem && ENTITIES.ENTITY_NAMES.isEmpty()) {
                ENTITIES.getEntities();
            }
            if (CONFIG.showGameState && SERVER.knownAddresses.isEmpty()) {
                SERVER.getServerAddresses();
            }

            if (!CONFIG.hasChanged) {
                if (!CommandUtils.isInMainMenu && (!DIMENSIONS.isInUse && !BIOMES.isInUse && !ENTITIES.isInUse && !SERVER.isInUse)) {
                    CommandUtils.setMainMenuPresence();
                } else if (CommandUtils.isInMainMenu && player != null) {
                    CommandUtils.isInMainMenu = false;

                    CLIENT.initArgumentData("&MAINMENU&");
                    CLIENT.initIconData("&MAINMENU&");
                }

                if (awaitingReply && SYSTEM.TIMER == 0) {
                    StringUtils.sendMessageToPlayer(player, ModUtils.TRANSLATOR.translate("craftpresence.command.request.ignored", CLIENT.REQUESTER_USER.getName()));
                    CLIENT.ipcInstance.respondToJoinRequest(CLIENT.REQUESTER_USER, IPCClient.ApprovalMode.DENY, null);
                    awaitingReply = false;
                    CLIENT.STATUS = "ready";
                } else if (!awaitingReply && CLIENT.REQUESTER_USER != null) {
                    CLIENT.REQUESTER_USER = null;
                    CLIENT.STATUS = "ready";
                }
            }
        } else if (!closing) {
            init();
        }

        scheduleTick();
    }
}
