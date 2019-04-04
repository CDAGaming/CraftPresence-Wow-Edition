package com.gitlab.cdagaming.craftpresence.handler.commands;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.CommandHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.assets.DiscordAssetHandler;
import com.gitlab.cdagaming.craftpresence.handler.discord.rpc.DiscordRPC;
import com.gitlab.cdagaming.craftpresence.handler.gui.controls.GUIExtendedButton;
import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CommandsGUI extends GuiScreen {
    private static String[] executionCommandArgs;
    public final GuiScreen parentScreen, currentScreen;
    public GUIExtendedButton proceedButton;
    private GuiTextField commandInput;
    private String executionString;
    private String[] commandArgs, filteredCommandArgs;
    private List<String> tabCompletions = Lists.newArrayList();

    public CommandsGUI(GuiScreen parentScreen) {
        mc = CraftPresence.instance;
        currentScreen = this;
        this.parentScreen = parentScreen;
    }

    public static void executeCommand(String... args) {
        executionCommandArgs = args;
    }

    private static List<String> getListOfStringsMatchingLastWord(String[] inputArgs, Collection<?> possibleCompletions) {
        String s = inputArgs[inputArgs.length - 1];
        List<String> list = Lists.newArrayList();

        if (!possibleCompletions.isEmpty()) {
            for (String s1 : Iterables.transform(possibleCompletions, Functions.toStringFunction())) {
                if (doesStringStartWith(s, s1)) {
                    list.add(s1);
                }
            }

            if (list.isEmpty()) {
                for (Object object : possibleCompletions) {
                    if (!StringHandler.isNullOrEmpty(String.valueOf(object)) && doesStringStartWith(s, String.valueOf(object))) {
                        list.add(String.valueOf(object));
                    }
                }
            }
        }

        return list;
    }

    private static boolean doesStringStartWith(String original, String region) {
        return region.regionMatches(true, 0, original, 0, original.length());
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        commandInput = new GuiTextField(110, fontRenderer, 115, (height - 30), (width - 120), 20);
        commandInput.setMaxStringLength(128);

        proceedButton = new GUIExtendedButton(700, 10, (height - 30), 100, 20, Constants.TRANSLATOR.translate("gui.config.buttonMessage.back"));

        buttonList.add(proceedButton);

        executionString = Constants.TRANSLATOR.translate("craftpresence.command.usage.main");

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        final String mainTitle = Constants.TRANSLATOR.translate("gui.config.title");
        final String subTitle = Constants.TRANSLATOR.translate("gui.config.title.commands");

        drawString(fontRenderer, mainTitle, (width / 2) - (fontRenderer.getStringWidth(mainTitle) / 2), 10, 0xFFFFFF);
        drawString(fontRenderer, subTitle, (width / 2) - (fontRenderer.getStringWidth(subTitle) / 2), 20, 0xFFFFFF);

        if (!StringHandler.isNullOrEmpty(commandInput.getText()) && commandInput.getText().startsWith("/")) {
            commandArgs = commandInput.getText().replace("/", "").split(" ");
            filteredCommandArgs = commandInput.getText().replace("/", "").replace("cp", "").replace(Constants.MODID, "").trim().split(" ");
            tabCompletions = getTabCompletions(filteredCommandArgs);
        }
        commandInput.drawTextBox();

        // COMMANDS START
        if (executionCommandArgs != null) {
            if (executionCommandArgs.length == 0 || (executionCommandArgs[0].equalsIgnoreCase("help") || executionCommandArgs[0].equalsIgnoreCase("?") || executionCommandArgs[0].equalsIgnoreCase(""))) {
                executionString = Constants.TRANSLATOR.translate("craftpresence.command.usage.main");
            } else if (!StringHandler.isNullOrEmpty(executionCommandArgs[0])) {
                if (executionCommandArgs[0].equalsIgnoreCase("request")) {
                    if (executionCommandArgs.length == 1) {
                        if (!StringHandler.isNullOrEmpty(CraftPresence.CLIENT.STATUS) && (CraftPresence.CLIENT.STATUS.equalsIgnoreCase("joinRequest") && CraftPresence.CLIENT.REQUESTER_USER != null)) {
                            if (CraftPresence.CONFIG.enableJoinRequest) {
                                executionString = Constants.TRANSLATOR.translate("craftpresence.command.request.info", CraftPresence.CLIENT.REQUESTER_USER.username, CraftPresence.TIMER);
                                CraftPresence.awaitingReply = true;
                            } else {
                                DiscordRPC.INSTANCE.Discord_Respond(CraftPresence.CLIENT.REQUESTER_USER.userId, DiscordRPC.DISCORD_REPLY_NO);
                                CraftPresence.CLIENT.STATUS = "ready";
                                CraftPresence.TIMER = 0;
                                CraftPresence.awaitingReply = false;
                            }
                        } else {
                            executionString = Constants.TRANSLATOR.translate("craftpresence.command.request.none");
                            CraftPresence.awaitingReply = false;
                        }
                    } else if (!StringHandler.isNullOrEmpty(executionCommandArgs[1])) {
                        if (CraftPresence.awaitingReply && CraftPresence.CONFIG.enableJoinRequest) {
                            if (executionCommandArgs[1].equalsIgnoreCase("accept")) {
                                executionString = Constants.TRANSLATOR.translate("craftpresence.command.request.accept", CraftPresence.CLIENT.REQUESTER_USER.username);
                                DiscordRPC.INSTANCE.Discord_Respond(CraftPresence.CLIENT.REQUESTER_USER.userId, DiscordRPC.DISCORD_REPLY_YES);
                                CraftPresence.CLIENT.STATUS = "ready";
                                CraftPresence.TIMER = 0;
                                CraftPresence.awaitingReply = false;
                            } else if (executionCommandArgs[1].equalsIgnoreCase("deny")) {
                                executionString = Constants.TRANSLATOR.translate("craftpresence.command.request.denied", CraftPresence.CLIENT.REQUESTER_USER.username);
                                DiscordRPC.INSTANCE.Discord_Respond(CraftPresence.CLIENT.REQUESTER_USER.userId, DiscordRPC.DISCORD_REPLY_NO);
                                CraftPresence.CLIENT.STATUS = "ready";
                                CraftPresence.TIMER = 0;
                                CraftPresence.awaitingReply = false;
                            } else {
                                executionString = Constants.TRANSLATOR.translate("craftpresence.command.unrecognized");
                            }
                        } else {
                            executionString = Constants.TRANSLATOR.translate("craftpresence.command.request.none");
                        }
                    }
                } else if (executionCommandArgs[0].equalsIgnoreCase("reload")) {
                    executionString = Constants.TRANSLATOR.translate("craftpresence.command.reload");
                    CommandHandler.reloadData(true);
                    executionString = Constants.TRANSLATOR.translate("craftpresence.command.reload.complete");
                } else if (executionCommandArgs[0].equalsIgnoreCase("shutdown")) {
                    executionString = Constants.TRANSLATOR.translate("craftpresence.command.shutdown.pre");
                    CraftPresence.CLIENT.shutDown();
                    executionString = Constants.TRANSLATOR.translate("craftpresence.command.shutdown.post");
                } else if (executionCommandArgs[0].equalsIgnoreCase("reboot")) {
                    executionString = Constants.TRANSLATOR.translate("craftpresence.command.reboot.pre");
                    CommandHandler.rebootRPC();
                    executionString = Constants.TRANSLATOR.translate("craftpresence.command.reboot.post");
                } else if (executionCommandArgs[0].equalsIgnoreCase("view")) {
                    if (executionCommandArgs.length == 1) {
                        executionString = Constants.TRANSLATOR.translate("craftpresence.command.usage.view");
                    } else if (!StringHandler.isNullOrEmpty(executionCommandArgs[1])) {
                        if (executionCommandArgs[1].equalsIgnoreCase("items")) {
                            if (CraftPresence.ENTITIES.enabled) {
                                executionString = Constants.TRANSLATOR.translate("craftpresence.command.items.header", CraftPresence.ENTITIES.ENTITY_NAMES);
                            } else {
                                executionString = Constants.TRANSLATOR.translate("gui.config.hoverMessage.access", Constants.TRANSLATOR.translate("gui.config.name.advanced.itemmessages"));
                            }
                        } else if (executionCommandArgs[1].equalsIgnoreCase("servers")) {
                            if (CraftPresence.SERVER.enabled) {
                                executionString = Constants.TRANSLATOR.translate("craftpresence.command.servers.header", CraftPresence.SERVER.knownAddresses);
                            } else {
                                executionString = Constants.TRANSLATOR.translate("gui.config.hoverMessage.access", Constants.TRANSLATOR.translate("gui.config.name.general.showstate"));
                            }
                        } else if (executionCommandArgs[1].equalsIgnoreCase("guis")) {
                            if (CraftPresence.GUIS.enabled) {
                                executionString = Constants.TRANSLATOR.translate("craftpresence.command.guis.header", CraftPresence.GUIS.GUI_NAMES);
                            } else {
                                executionString = Constants.TRANSLATOR.translate("gui.config.hoverMessage.access", Constants.TRANSLATOR.translate("gui.config.name.advanced.guimessages"));
                            }
                        } else if (executionCommandArgs[1].equalsIgnoreCase("biomes")) {
                            if (CraftPresence.BIOMES.enabled) {
                                executionString = Constants.TRANSLATOR.translate("craftpresence.command.biomes.header", CraftPresence.BIOMES.BIOME_NAMES);
                            } else {
                                executionString = Constants.TRANSLATOR.translate("gui.config.hoverMessage.access", Constants.TRANSLATOR.translate("gui.config.name.general.showbiome"));
                            }
                        } else if (executionCommandArgs[1].equalsIgnoreCase("dimensions")) {
                            if (CraftPresence.DIMENSIONS.enabled) {
                                executionString = Constants.TRANSLATOR.translate("craftpresence.command.dimensions.header", CraftPresence.DIMENSIONS.DIMENSION_NAMES);
                            } else {
                                executionString = Constants.TRANSLATOR.translate("gui.config.hoverMessage.access", Constants.TRANSLATOR.translate("gui.config.name.general.showdimension"));
                            }
                        } else if (executionCommandArgs[1].equalsIgnoreCase("currentData")) {
                            executionString = Constants.TRANSLATOR.translate("craftpresence.command.currentdata", CraftPresence.CLIENT.DETAILS, CraftPresence.CLIENT.GAME_STATE, CraftPresence.CLIENT.START_TIMESTAMP, CraftPresence.CLIENT.CLIENT_ID, CraftPresence.CLIENT.LARGEIMAGEKEY, CraftPresence.CLIENT.LARGEIMAGETEXT, CraftPresence.CLIENT.SMALLIMAGEKEY, CraftPresence.CLIENT.SMALLIMAGETEXT, CraftPresence.CLIENT.PARTY_ID, CraftPresence.CLIENT.PARTY_SIZE, CraftPresence.CLIENT.PARTY_MAX, CraftPresence.CLIENT.JOIN_SECRET, CraftPresence.CLIENT.END_TIMESTAMP, CraftPresence.CLIENT.MATCH_SECRET, CraftPresence.CLIENT.SPECTATE_SECRET, CraftPresence.CLIENT.INSTANCE);
                        } else if (executionCommandArgs[1].equalsIgnoreCase("assets")) {
                            if (executionCommandArgs.length == 2) {
                                executionString = Constants.TRANSLATOR.translate("craftpresence.command.usage.assets");
                            } else if (!StringHandler.isNullOrEmpty(executionCommandArgs[2])) {
                                if (executionCommandArgs[2].equalsIgnoreCase("large")) {
                                    executionString = Constants.TRANSLATOR.translate("craftpresence.command.assets.large.header", DiscordAssetHandler.LARGE_ICONS);
                                }
                                if (executionCommandArgs[2].equalsIgnoreCase("small")) {
                                    executionString = Constants.TRANSLATOR.translate("craftpresence.command.assets.small.header", DiscordAssetHandler.SMALL_ICONS);
                                } else if (executionCommandArgs[2].equalsIgnoreCase("all")) {
                                    executionString = Constants.TRANSLATOR.translate("craftpresence.command.assets.all.header", DiscordAssetHandler.ICON_LIST);
                                }
                            }
                        } else {
                            executionString = Constants.TRANSLATOR.translate("craftpresence.command.unrecognized");
                        }
                    }
                } else {
                    executionString = Constants.TRANSLATOR.translate("craftpresence.command.unrecognized");
                }
            } else {
                executionString = Constants.TRANSLATOR.translate("craftpresence.logger.error.command");
            }
        }

        executionCommandArgs = null;
        // COMMANDS END

        CraftPresence.GUIS.drawMultiLineString(StringHandler.splitTextByNewLine(executionString), 25, 45, width, height, -1, fontRenderer, false);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == proceedButton.id) {
            mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parentScreen);
        }
        if (commandInput.isFocused() && commandInput.getText().startsWith("/") && commandArgs != null &&
                (commandArgs[0].equalsIgnoreCase("cp") || commandArgs[0].equalsIgnoreCase(Constants.MODID))) {
            if (keyCode == Keyboard.KEY_TAB && !tabCompletions.isEmpty()) {
                if (commandArgs.length > 1 && (filteredCommandArgs[filteredCommandArgs.length - 1].length() > 1 || filteredCommandArgs[filteredCommandArgs.length - 1].equalsIgnoreCase("?"))) {
                    commandInput.setText(commandInput.getText().replace(filteredCommandArgs[filteredCommandArgs.length - 1], tabCompletions.get(0)));
                }
            } else if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER) {
                executeCommand(filteredCommandArgs);
            }
        }
        commandInput.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        commandInput.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        commandInput.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    private List<String> getTabCompletions(String[] args) {
        List<String> baseCompletions = Lists.newArrayList();
        List<String> assetsCompletions = Lists.newArrayList();
        List<String> viewCompletions = Lists.newArrayList();
        List<String> requestCompletions = Lists.newArrayList();

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
}
