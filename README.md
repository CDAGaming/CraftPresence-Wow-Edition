# CraftPresence
Completely customize the way others see you play Minecraft via Discord's Rich Presence API & the DiscordIPC API by [jagrosh](https://github.com/jagrosh)!

[![Pipeline Status](https://gitlab.com/CDAGaming/CraftPresence/badges/master/pipeline.svg)](https://gitlab.com/CDAGaming/CraftPresence/commits/master)
[![CurseForge-Downloads](http://cf.way2muchnoise.eu/full_craftpresence_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/craftpresence)
[![CurseForge-Availability](http://cf.way2muchnoise.eu/versions/craftpresence.svg)](https://www.curseforge.com/minecraft/mc-mods/craftpresence)

## Port Notes
*   CraftPresence for Minecraft 1.14.x and 1.15.x requires the [FabricMC ModLoader](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
*   CraftPresence for Minecraft 1.13.x requires the [Rift ModLoader](https://www.curseforge.com/minecraft/mc-mods/rift)

## Features
Besides having the Ability to Change your Discord Status from "Playing Minecraft",
This Mod offers plenty of Customization Options to specify entirely how others see you play.
From Having Your Current Biome Show Up, To which Dimension you in, as well as which server you're in,
The Customization Possibilities are limitless, with the only real limit being how creative you customize your display.

### Twitch // CursePack // MultiMC // MCUpdater // Technic Support
At this time, CraftPresence will detect whether your Launch Directory contains a valid Twitch Manifest (manifest.json), a MultiMC Instance (instance.cfg), a MCUpdater Instance (instance.json), or a Technic installedPacks File (installedPacks).
If so, It'll put the Packs Name in your Display as well as Show its Icon (when not in a server).

As an example, this is how the mod will convert the pack name to an iconKey:

`All the Mods 3 == allthemods3`

(Note the above format only applies to Twitch & Technic Packs ; MultiMC natively has an Icon Key Property)

## Commands
CraftPresence currently offers the following Commands:

(Commands must be prefixed by either `/craftpresence` or `/cp`)

(In v1.5.0 and Above, Commands are now inputted via a new menu called "Commands" in the Config GUI)

*   `/cp view` - Help Command to display Commands available to view a variety of Display Data
*   `/cp reload` - Reloads Mod Data (In v1.4.8 and Above, this forces a Tick Event)
*   `/cp reboot` - Reboots RPC
*   `/cp shutdown` - Shutdown RPC (Can be turned on from `/cp reboot`)
*   `/cp request` - View Join Request Info
*   `/cp view currentData` - Displays in Text Form, your Current RPC Data
*   `/cp view assets (large | small | all)` - Displays all Asset IconKeys available to you
*   `/cp view dimensions` - Displays all Dimension Names available for use, if Showing Current Dimension is enabled
*   `/cp view biomes` - Displays all Biome Names available for use, if Showing Current Biome is enabled
*   `/cp view servers` - Displays all Server Addresses available for use, if Show Game State is enabled
*   `/cp view guis` - Displays all GUI Names available for use, if Per-GUI is Enabled
*   `/cp view items` - Displays all Item Names available for use, if Per-Item is Enabled
*   `/cp (help | ?)` - Help Command to display the Above Commands and These Explanations

## KeyBinds
CraftPresence currently contains the following KeyBinds:

(In v1.5.5 and Above, KeyBinds are now customized in the Accessibility Settings in the Config GUI)

*   `Open Config GUI` - KeyBind to Open the CraftPresence ConfigGUI (Default: GRAVE Key)

## Placeholders
In some Configuration Areas, CraftPresence provides some Placeholders to make things easier:

(In v1.6.0 and Above, You can now define where in the Rich Presence the Messages should go)

(Placeholders are not Case-Sensitive, but should be entered lowercase to prevent issues recognizing them on v1.5.x and below)

### Presence Display Placeholders
*   &DIMENSION& - The Dimension Message from your Dimension Settings, if enabled and in use
*   &BIOME& - The Biome Message from your Biome Settings, if enabled and in use
*   &SERVER& - The Server/SinglePlayer Message from your Server Settings, if enabled and in use
*   &GUI& - The GUI Message from your GUI Settings, if enabled and in use
*   &TILEENTITY& - The TileEntity (Block/Item) Message from your Advanced -> Item Messages Settings, if enabled and in use
*   &TARGETENTITY& - The Targeted Entity Message from your Advanced -> Entity Target Messages Setting, if Per-Entity is enabled and in use
*   &ATTACKINGENTITY& - The Attacking Entity Message from your Advanced -> Entity Attacking Messages Setting, if Per-Entity is enabled and in use
*   &RIDINGENTITY& - The Riding Entity Message from your Advanced -> Entity Riding Messages Setting, if Per-Entity is enabled and in use

(For Image Keys, only the first placeholder and an additional suffix if any will be used)

### Main Menu Message Placeholders
*   &IGN& - Your Minecraft Username
*   &MODS& - The Number of Mods currently in your Mods Folder

### Biome Placeholders
*   &BIOME& - The Current Biome Name
*   &ID& - The Current Biome ID

### Dimension Placeholders
*   &DIMENSION& - The Current Dimension Name
*   &ICON& - The Default Dimension Icon Name
*   &ID& - The Current Dimension ID

### Server/LAN Message Placeholders
*   &IP& - The Current Server IP Address
*   &NAME& - The Current Server Name
*   &MOTD& - The Current Server MOTD (Message of The Day)
*   &ICON& - The Default Server Icon Name
*   &PLAYERS& - The Current Player Count `(10 / 100 Players)`
*   &IGN& - Your Minecraft Username
*   &TIME& - The Current World Time
*   &MODS& - The Number of Mods currently in your Mods Folder

### SinglePlayer Placeholders
*   &IGN& - Your Minecraft Username
*   &TIME& - The Current World Time
*   &MODS& - The Number of Mods currently in your Mods Folder

### GUI Placeholders
*   &GUI& - The Current GUI Name (Supports GUIContainers and GUIScreens)
*   &CLASS& - The Current GUI Class (Ex: net.minecraft.xxx)
*   &SCREEN& - The Current GUI Screen Instance

### Item/Entity Placeholders
*   &MAIN& - The Current Item your Main Hand is Holding
*   &OFFHAND& - The Current Item your Off Hand is Holding*
*   &HELMET& - The Current Helmet Armor Piece you have Equipped
*   &CHEST& - The Current Chest Armor Piece you have Equipped
*   &LEGS& - The Current Leggings Armor Piece you have Equipped
*   &BOOTS& - The Current Boots Armor Piece you have Equipped

## Versions of CraftPresence
Beginning in v1.5.2, CraftPresence is now split into different editions, based on the Minecraft Version you use it in:

*   Legacy Version (Minecraft 1.2.5 and Below):
    *   Server Support is unavailable in 1.2.5 and Below (Only SinglePlayer will work with showGameStatus Enabled)
    *   Minecraft 1.1.0 and Below may not work on Forge, and may require a Modified Minecraft Jar with ModLoader + ModLoaderMP

(Support for Issues related to Vanilla Code, Forge, or ModLoader is extremely Limited)

## Disclaimers & Additional Info

### Minecraft Object Obfuscation
Due to Obfuscation in Minecraft, some of Minecraft Objects such as GUIs, Dimensions, or Servers must be opened once in the session to be separately customized.

### Icon Request
Not seeing an Icon you like or have a suggestion for an Icon to add/modify on the Default Client ID?

If so, you can make a request on my [Issue Tracker](https://gitlab.com/CDAGaming/CraftPresence/issues/), so long as the following requirements are met:

*   If adding an Icon from a dimension, Specify the Mod's Link that the dimension derives from
    *   This is because specific Icon IDs must be used, which can be found by the mod or from checking your Logs/Chat after entering the dimension as CraftPresence will tell you the ID expected

*   A 512x512 or 1024x1024 Icon to be used
    *   Icons near these sizes are also allowed, but may not give a great final quality

*   If requesting an icon be modified or removed from the Default Client ID, please specify a reason why
    *   Mostly just so it can be logged why it was changed for future reference

#### Licensing
This Mod is licensed under the MIT License as well as the Apache 2.0 License

This Project makes usage of the following dependencies internally:

*   [DiscordIPC API](https://github.com/jagrosh/DiscordIPC) by [jagrosh](https://github.com/jagrosh)
    *   [JUnixSocket](https://github.com/kohlschutter/junixsocket) by [kohlschutter](https://github.com/kohlschutter)

*   [Java Native Access (JNA) API](https://github.com/java-native-access/jna) on v1.5.x and Below

#### Discord TOS
As with other RPC Mods, this Mod uses your in-Game Data to send Display Information to a 3rd Party
Service (In this Case, Discord).

The Terms of Service relating to Creating a Discord ID for icons can be found [here](https://discordapp.com/developers/docs/legal)
