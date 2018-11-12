package com.gitlab.cdagaming.craftpresence.handler.commands;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.CommandHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.rpc.DiscordRPC;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class CPCommands extends CommandBase {
    @Override
    public String getName() {
        return Constants.MODID;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
        List<String> baseCompletions = new ArrayList<>();
        List<String> assetsCompletions = new ArrayList<>();
        List<String> viewCompletions = new ArrayList<>();
        List<String> requestCompletions = new ArrayList<>();

        baseCompletions.add("?");
        baseCompletions.add("help");
        baseCompletions.add("config");
        baseCompletions.add("reload");
        baseCompletions.add("request");
        baseCompletions.add("view");
        baseCompletions.add("reboot");
        baseCompletions.add("shutdown");

        viewCompletions.add("currentData");
        viewCompletions.add("assets");
        viewCompletions.add("dimensions");
        viewCompletions.add("biomes");
        viewCompletions.add("guis");
        viewCompletions.add("items");
        viewCompletions.add("servers");

        assetsCompletions.add("all");
        assetsCompletions.add("large");
        assetsCompletions.add("small");

        requestCompletions.add("accept");
        requestCompletions.add("deny");

        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, baseCompletions);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("view")) {
                return getListOfStringsMatchingLastWord(args, viewCompletions);
            } else if (args[0].equalsIgnoreCase("request")) {
                return getListOfStringsMatchingLastWord(args, requestCompletions);
            } else {
                return Collections.emptyList();
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("view") && args[1].equalsIgnoreCase("assets")) {
                return getListOfStringsMatchingLastWord(args, assetsCompletions);
            } else {
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("cp");

        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) {
        executeCommand((Entity) commandSender, args);
    }

    public void executeCommand(Entity sender, String... args) {
        if (sender != null) {
            if (args.length == 0 || (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))) {
                StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.usage.main"));
            } else if (!StringHandler.isNullOrEmpty(args[0])) {
                if (args[0].equalsIgnoreCase("request")) {
                    if (args.length == 1) {
                        if (!StringHandler.isNullOrEmpty(CraftPresence.CLIENT.STATUS) && (CraftPresence.CLIENT.STATUS.equalsIgnoreCase("joinRequest") && CraftPresence.CLIENT.REQUESTER_USER != null)) {
                            if (CraftPresence.CONFIG.enableJoinRequest) {
                                StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.request.info", CraftPresence.CLIENT.REQUESTER_USER.username, CraftPresence.TIMER));
                                CraftPresence.awaitingReply = true;
                            } else {
                                DiscordRPC.INSTANCE.Discord_Respond(CraftPresence.CLIENT.REQUESTER_USER.userId, DiscordRPC.DISCORD_REPLY_NO);
                                CraftPresence.CLIENT.STATUS = "ready";
                                CraftPresence.TIMER = 0;
                                CraftPresence.awaitingReply = false;
                            }
                        } else {
                            StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.request.none"));
                            CraftPresence.awaitingReply = false;
                        }
                    } else if (!StringHandler.isNullOrEmpty(args[1])) {
                        if (CraftPresence.awaitingReply && CraftPresence.CONFIG.enableJoinRequest) {
                            if (args[1].equalsIgnoreCase("accept")) {
                                StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.request.accept", CraftPresence.CLIENT.REQUESTER_USER.username));
                                DiscordRPC.INSTANCE.Discord_Respond(CraftPresence.CLIENT.REQUESTER_USER.userId, DiscordRPC.DISCORD_REPLY_YES);
                                CraftPresence.CLIENT.STATUS = "ready";
                                CraftPresence.TIMER = 0;
                                CraftPresence.awaitingReply = false;
                            } else if (args[1].equalsIgnoreCase("deny")) {
                                StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.request.denied", CraftPresence.CLIENT.REQUESTER_USER.username));
                                DiscordRPC.INSTANCE.Discord_Respond(CraftPresence.CLIENT.REQUESTER_USER.userId, DiscordRPC.DISCORD_REPLY_NO);
                                CraftPresence.CLIENT.STATUS = "ready";
                                CraftPresence.TIMER = 0;
                                CraftPresence.awaitingReply = false;
                            } else {
                                StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.unrecognized"));
                            }
                        } else {
                            StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.request.none"));
                        }
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.reload"));
                    CommandHandler.reloadData();
                    StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.reload.complete"));
                } else if (args[0].equalsIgnoreCase("shutdown")) {
                    StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.shutdown.pre"));
                    CraftPresence.CLIENT.shutDown();
                    StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.shutdown.post"));
                } else if (args[0].equalsIgnoreCase("reboot")) {
                    StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.reboot.pre"));
                    CommandHandler.rebootRPC();
                    StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.reboot.post"));
                } else if (args[0].equalsIgnoreCase("config")) {
                    CraftPresence.GUIS.openConfigGUI = true;
                } else if (args[0].equalsIgnoreCase("view")) {
                    if (args.length == 1) {
                        StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.usage.view"));
                    } else if (!StringHandler.isNullOrEmpty(args[1])) {
                        if (args[1].equalsIgnoreCase("items")) {
                            if (CraftPresence.ENTITIES.enabled) {
                                StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.items.header", CraftPresence.ENTITIES.ENTITY_NAMES));
                            } else {
                                StringHandler.sendMessageToPlayer(sender, I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.advanced.itemmessages")));
                            }
                        } else if (args[1].equalsIgnoreCase("servers")) {
                            if (CraftPresence.SERVER.enabled) {
                                StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.servers.header", CraftPresence.SERVER.knownAddresses));
                            } else {
                                StringHandler.sendMessageToPlayer(sender, I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.general.showstate")));
                            }
                        } else if (args[1].equalsIgnoreCase("guis")) {
                            if (CraftPresence.GUIS.enabled) {
                                StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.guis.header", CraftPresence.GUIS.GUI_NAMES));
                            } else {
                                StringHandler.sendMessageToPlayer(sender, I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.advanced.guimessages")));
                            }
                        } else if (args[1].equalsIgnoreCase("biomes")) {
                            if (CraftPresence.BIOMES.enabled) {
                                StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.biomes.header", CraftPresence.BIOMES.BIOME_NAMES));
                            } else {
                                StringHandler.sendMessageToPlayer(sender, I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.general.showbiome")));
                            }
                        } else if (args[1].equalsIgnoreCase("dimensions")) {
                            if (CraftPresence.DIMENSIONS.enabled) {
                                StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.dimensions.header", CraftPresence.DIMENSIONS.DIMENSION_NAMES));
                            } else {
                                StringHandler.sendMessageToPlayer(sender, I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.general.showdimension")));
                            }
                        } else if (args[1].equalsIgnoreCase("currentData")) {
                            StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.currentdata", CraftPresence.CLIENT.DETAILS, CraftPresence.CLIENT.GAME_STATE, CraftPresence.CLIENT.START_TIMESTAMP, CraftPresence.CLIENT.CLIENT_ID, CraftPresence.CLIENT.LARGEIMAGEKEY, CraftPresence.CLIENT.LARGEIMAGETEXT, CraftPresence.CLIENT.SMALLIMAGEKEY, CraftPresence.CLIENT.SMALLIMAGETEXT, CraftPresence.CLIENT.PARTY_ID, CraftPresence.CLIENT.PARTY_SIZE, CraftPresence.CLIENT.PARTY_MAX, CraftPresence.CLIENT.JOIN_SECRET, CraftPresence.CLIENT.END_TIMESTAMP, CraftPresence.CLIENT.MATCH_SECRET, CraftPresence.CLIENT.SPECTATE_SECRET, CraftPresence.CLIENT.INSTANCE));
                        } else if (args[1].equalsIgnoreCase("assets")) {
                            if (args.length == 2) {
                                StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.usage.assets"));
                            } else if (!StringHandler.isNullOrEmpty(args[2])) {
                                if (args[2].equalsIgnoreCase("large") || args[2].equalsIgnoreCase("all")) {
                                    StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.assets.large.header", DiscordAssetHandler.LARGE_ICONS));
                                }
                                if (args[2].equalsIgnoreCase("small") || args[2].equalsIgnoreCase("all")) {
                                    StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.assets.small.header", DiscordAssetHandler.SMALL_ICONS));
                                }
                            }
                        } else {
                            StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.unrecognized"));
                        }
                    }
                } else {
                    StringHandler.sendMessageToPlayer(sender, I18n.format("craftpresence.command.unrecognized"));
                }
            }
        } else {
            Constants.LOG.error(I18n.format("craftpresence.logger.error.command"));
        }
    }
}
