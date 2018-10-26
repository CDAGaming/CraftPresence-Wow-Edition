package com.gitlab.cdagaming.craftpresence.handler.commands;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.CommandHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CPCommands extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return Constants.MODID;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Nonnull
    @Override
    public String getUsage(@Nullable ICommandSender sender) {
        return "";
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(@Nullable MinecraftServer server, @Nullable ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        List<String> baseCompletions = new ArrayList<>();
        List<String> assetsCompletions = new ArrayList<>();
        List<String> viewCompletions = new ArrayList<>();

        baseCompletions.add("?");
        baseCompletions.add("help");
        baseCompletions.add("config");
        baseCompletions.add("reload");
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

        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, baseCompletions);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("view")) {
                return getListOfStringsMatchingLastWord(args, viewCompletions);
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

    @Nonnull
    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("cp");

        return aliases;
    }

    @Override
    public void execute(@Nullable MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        Entity commandSender = sender.getCommandSenderEntity();
        if (args.length == 0 || (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))) {
            StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.usage.main"));
        } else if (!StringHandler.isNullOrEmpty(args[0])) {
            if (args[0].equalsIgnoreCase("reload")) {
                StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.reload"));
                CommandHandler.reloadData();
                StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.reload.complete"));
            } else if (args[0].equalsIgnoreCase("shutdown")) {
                StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.shutdown.pre"));
                CraftPresence.CLIENT.shutDown();
                StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.shutdown.post"));
            } else if (args[0].equalsIgnoreCase("reboot")) {
                StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.reboot.pre"));
                CommandHandler.rebootRPC();
                StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.reboot.post"));
            } else if (args[0].equalsIgnoreCase("config")) {
                CraftPresence.GUIS.openConfigGUI = true;
            } else if (args[0].equalsIgnoreCase("view")) {
                if (args.length == 1) {
                    StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.usage.view"));
                } else if (!StringHandler.isNullOrEmpty(args[1])) {
                    if (args[1].equalsIgnoreCase("items")) {
                        if (CraftPresence.ENTITIES.enabled) {
                            StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.items.header", CraftPresence.ENTITIES.ENTITY_NAMES));
                        } else {
                            StringHandler.sendMessageToPlayer(commandSender, I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.advanced.itemmessages")));
                        }
                    } else if (args[1].equalsIgnoreCase("servers")) {
                        if (CraftPresence.SERVER.enabled) {
                            StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.servers.header", CraftPresence.SERVER.knownAddresses));
                        } else {
                            StringHandler.sendMessageToPlayer(commandSender, I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.general.showstate")));
                        }
                    } else if (args[1].equalsIgnoreCase("guis")) {
                        if (CraftPresence.GUIS.enabled) {
                            StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.guis.header", CraftPresence.GUIS.GUI_NAMES));
                        } else {
                            StringHandler.sendMessageToPlayer(commandSender, I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.advanced.guimessages")));
                        }
                    } else if (args[1].equalsIgnoreCase("biomes")) {
                        if (CraftPresence.BIOMES.enabled) {
                            StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.biomes.header", CraftPresence.BIOMES.BIOME_NAMES));
                        } else {
                            StringHandler.sendMessageToPlayer(commandSender, I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.general.showbiome")));
                        }
                    } else if (args[1].equalsIgnoreCase("dimensions")) {
                        if (CraftPresence.DIMENSIONS.enabled) {
                            StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.dimensions.header", CraftPresence.DIMENSIONS.DIMENSION_NAMES));
                        } else {
                            StringHandler.sendMessageToPlayer(commandSender, I18n.format("gui.config.hoverMessage.access", I18n.format("gui.config.name.general.showdimension")));
                        }
                    } else if (args[1].equalsIgnoreCase("currentData")) {
                        StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.currentdata", CraftPresence.CLIENT.DETAILS, CraftPresence.CLIENT.GAME_STATE, CraftPresence.CLIENT.START_TIMESTAMP, CraftPresence.CLIENT.CLIENT_ID, CraftPresence.CLIENT.LARGEIMAGEKEY, CraftPresence.CLIENT.LARGEIMAGETEXT, CraftPresence.CLIENT.SMALLIMAGEKEY, CraftPresence.CLIENT.SMALLIMAGETEXT, CraftPresence.CLIENT.PARTY_ID, CraftPresence.CLIENT.PARTY_SIZE, CraftPresence.CLIENT.PARTY_MAX, CraftPresence.CLIENT.JOIN_SECRET, CraftPresence.CLIENT.END_TIMESTAMP, CraftPresence.CLIENT.MATCH_SECRET, CraftPresence.CLIENT.SPECTATE_SECRET, CraftPresence.CLIENT.INSTANCE));
                    } else if (args[1].equalsIgnoreCase("assets")) {
                        if (args.length == 2) {
                            StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.usage.assets"));
                        } else if (!StringHandler.isNullOrEmpty(args[2])) {
                            if (args[2].equalsIgnoreCase("large") || args[2].equalsIgnoreCase("all")) {
                                StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.assets.large.header", DiscordAssetHandler.LARGE_ICONS));
                            }
                            if (args[2].equalsIgnoreCase("small") || args[2].equalsIgnoreCase("all")) {
                                StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.assets.small.header", DiscordAssetHandler.SMALL_ICONS));
                            }
                        }
                    } else {
                        StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.unrecognized"));
                    }
                }
            } else {
                StringHandler.sendMessageToPlayer(commandSender, I18n.format("craftpresence.command.unrecognized"));
            }
        }
    }
}
