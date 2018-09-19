package com.gitlab.cdagaming.craftpresence.handler.commands;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.CommandHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CPCommands extends CommandBase implements IClientCommand {
    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }

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
        return new TextComponentTranslation("craftpresence.command.usage.main").getFormattedText();
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
    public void execute(@Nullable MinecraftServer server, @Nullable ICommandSender sender, @Nonnull String[] args) {
        if (sender instanceof EntityPlayer) {
            if (args.length == 0 || (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))) {
                sender.sendMessage(new TextComponentString(getUsage(sender)));
            } else if (!StringHandler.isNullOrEmpty(args[0])) {
                if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(new TextComponentTranslation("craftpresence.command.reload"));
                    CommandHandler.reloadData();
                    sender.sendMessage(new TextComponentTranslation("craftpresence.command.reload.complete"));
                } else if (args[0].equalsIgnoreCase("shutdown")) {
                    sender.sendMessage(new TextComponentTranslation("craftpresence.command.shutdown.pre"));
                    CraftPresence.CLIENT.shutDown();
                    sender.sendMessage(new TextComponentTranslation("craftpresence.command.shutdown.post"));
                } else if (args[0].equalsIgnoreCase("reboot")) {
                    sender.sendMessage(new TextComponentTranslation("craftpresence.command.reboot.pre"));
                    CommandHandler.rebootRPC();
                    sender.sendMessage(new TextComponentTranslation("craftpresence.command.reboot.post"));
                } else if (args[0].equalsIgnoreCase("config")) {
                    CraftPresence.GUIS.openConfigGUI = true;
                } else if (args[0].equalsIgnoreCase("view")) {
                    if (args.length == 1) {
                        sender.sendMessage(new TextComponentTranslation("craftpresence.command.usage.view"));
                    } else if (!StringHandler.isNullOrEmpty(args[1])) {
                        if (args[1].equalsIgnoreCase("dimensions")) {
                            sender.sendMessage(new TextComponentTranslation("craftpresence.command.dimensions.header", CraftPresence.DIMENSIONS.DIMENSION_NAMES));
                        } else if (args[1].equalsIgnoreCase("currentData")) {
                            sender.sendMessage(new TextComponentTranslation("craftpresence.command.currentdata", CraftPresence.CLIENT.DETAILS, CraftPresence.CLIENT.GAME_STATE, CraftPresence.CLIENT.START_TIMESTAMP, CraftPresence.CLIENT.CLIENT_ID, CraftPresence.CLIENT.LARGEIMAGEKEY, CraftPresence.CLIENT.LARGEIMAGETEXT, CraftPresence.CLIENT.SMALLIMAGEKEY, CraftPresence.CLIENT.SMALLIMAGETEXT));
                        } else if (args[1].equalsIgnoreCase("assets")) {
                            if (args.length == 2) {
                                sender.sendMessage(new TextComponentTranslation("craftpresence.command.usage.assets"));
                            } else if (!StringHandler.isNullOrEmpty(args[2])) {
                                if (args[2].equalsIgnoreCase("large") || args[2].equalsIgnoreCase("all")) {
                                    sender.sendMessage(new TextComponentTranslation("craftpresence.command.assets.large.header", DiscordAssetHandler.LARGE_ICONS));
                                }
                                if (args[2].equalsIgnoreCase("small") || args[2].equalsIgnoreCase("all")) {
                                    sender.sendMessage(new TextComponentTranslation("craftpresence.command.assets.small.header", DiscordAssetHandler.SMALL_ICONS));
                                }
                            }
                        } else {
                            sender.sendMessage(new TextComponentTranslation("craftpresence.command.unrecognized"));
                        }
                    }
                } else {
                    sender.sendMessage(new TextComponentTranslation("craftpresence.command.unrecognized"));
                }
            }
        }
    }
}
