# CraftPresence
Completely customize the way others see you play Minecraft via Discord's Rich Presence API & the DiscordIPC API by [jagrosh](https://github.com/jagrosh)!

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/da684ebb8ce947d7ac8935b5de5dd732)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=CDAGaming/CraftPresence-Mirror&amp;utm_campaign=Badge_Grade)
[![Pipeline Status](https://gitlab.com/CDAGaming/CraftPresence/badges/master/pipeline.svg)](https://gitlab.com/CDAGaming/CraftPresence/commits/master)

[![CurseForge-Downloads](http://cf.way2muchnoise.eu/full_craftpresence_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/craftpresence)
[![CurseForge-Availability](http://cf.way2muchnoise.eu/versions/craftpresence.svg)](https://www.curseforge.com/minecraft/mc-mods/craftpresence)

## Port Notes
*   Some versions of CraftPresence for Minecraft 1.14.x, 1.15.x, and 1.16.x require the [FabricMC ModLoader](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
*   Some versions of CraftPresence for Minecraft 1.13.x require the [Rift ModLoader](https://www.curseforge.com/minecraft/mc-mods/rift)

## Features
In addition to having the Ability to Change your Discord Status from "Playing Minecraft",
This Mod offers plenty of Customization Options to specify entirely how others see you play.
From Having Your Current Biome Show Up, To which Dimension you in, as well as which server you're in,
The Customization Possibilities are limitless, with the only real limit being how creative you customize your display.

### Launcher and Pack Integration Support
CraftPresence will detect whether your Launch Directory contains:

*   A valid Twitch/Curse/GDLauncher Manifest (manifest.json, minecraftinstance.json)
*   A MultiMC Instance (instance.cfg)
*   A MCUpdater Instance (instance.json)
*   A Technic installedPacks File (installedPacks)

If using any of these launchers, It'll put the Packs Name in your Display as well as Show its Icon (when not in a server).

Note: In v1.6.0 and above, it'll instead parse the Pack's name into the `&PACK&` placeholder, that you can configure for usage in the RPC

As an example, this is how the mod will convert the pack name to an iconKey:

Example: `All the Mods 3` would parse as `allthemods3`

Note: MultiMC natively has an Icon Key Property that is used instead of converting from the Pack's Display Name

## Commands
CraftPresence currently offers the following Commands:

Keep in mind the following:

*   Commands must be prefixed by either `/craftpresence` or `/cp`
*   In v1.5.0 and above, these commands are only usable via the Commands Gui, found within the Config Gui)

___

*   `/cp view` - Help Command to display Commands available to view a variety of Display Data
*   `/cp reload` - Reloads Mod Data (In v1.4.8 and Above, this forces a Tick Event)
*   `/cp reboot` - Reboots RPC
*   `/cp shutdown` - Shutdown RPC (Can be turned on from `/cp reboot`)
*   `/cp request` - View Join Request Info
*   `/cp view currentData` - Displays in Text Form, your Current RPC Data
*   `/cp view assets (large | small | all)` - Displays all Asset IconKeys available to you
*   `/cp view dimensions` - Displays all Dimension Names available for use, requires `Show Current Dimension` to be enabled
*   `/cp view biomes` - Displays all Biome Names available for use, requires `Show Current Biome` to be enabled
*   `/cp view servers` - Displays all Server Addresses available for use, requires `Show Game State` to be enabled
*   `/cp view screens` - Displays all Gui Names available for use, if Per-Gui is enabled
*   `/cp view items` - Displays all Item Names available for use, if Per-Item is enabled
*   `/cp view entities` - Displays all Entity Names available for use, if Per-Entity is enabled
*   `/cp (help | ?)` - Help Command to display the above commands and these explanations

## KeyBinds
CraftPresence currently contains the following KeyBinds:

Note: In v1.5.5 and Above, KeyBinds are now customized in the Accessibility Settings in the Config Gui, and not the normal controls menu

*   `Open Config Gui` - KeyBind to Open the CraftPresence ConfigGui (Default: GRAVE/TILDE Key)

## Placeholders
In some Configuration Areas, CraftPresence provides some placeholders to make things easier:

Keep in mind the following:

*   In v1.6.0 and Above, You can now define where in the Rich Presence the Messages should go
*   Placeholders are not Case-Sensitive, but should be entered lowercase to prevent issues with recognizing them on v1.5.x and below
*   As of v1.6.8, you can now also use minified versions of placeholders, which are trimmed down to a length of 4; `&DIM&` and `&DIMENSION&` are the same in this case

___

### Presence Display / Global Placeholders
These placeholders can be added within the `Presence Settings` menu within the Config Gui.

As these placeholders are global, they can be set in any of the RPC fields within `Presence Settings` and customized at a deeper level via their sub-placeholders.

*   `&MAINMENU&` - The message to display whilst in the main menu. (See `Status Messages::Main Menu Message` for sub-placeholders)
*   `&BRAND&` - The minecraft branding label, displayed as interpreted by minecraft.
*   `&MCVERSION&` - The minecraft version, displayed as interpreted by minecraft.
*   `&IGN&` - The non-world player info message. (See `Status Messages::Player Outer Info` for sub-placeholders)
*   `&MODS&` - The message to display with your mod count. (See `Status Messages::Mods` for sub-placeholders)
*   `&PACK&` - The message to display whilst using a valid modpack. (See `Status Messages::Modpack Message` for sub-placeholders)
*   `&DIMENSION&` - The Dimension Message, if in use. (See `Dimension Messages` for sub-placeholders)
*   `&BIOME&` - The Biome Message, if in use. (See `Biome Messages` for sub-placeholders)
*   `&SERVER&` - The Server/SinglePlayer Message, if in use. (See `Server Messages` for sub-placeholders)
*   `&SCREEN&` - The Gui Screen Message, if in use. (See `Advanced Settings::Gui Messages` for sub-placeholders)
*   `&TILEENTITY&` - The TileEntity (Block/Item) Message, if in use. (See `Advanced Settings::Item Messages` for sub-placeholders)
*   `&TARGETENTITY&` - The Targeted Entity Message, if in use. (See `Advanced Settings :: Entity Target Messages` for sub-placeholders)
*   `&ATTACKINGENTITY&` - The Attacking Entity Message, if in use. (See `Advanced Settings :: Entity Attacking Messages` for sub-placeholders)
*   `&RIDINGENTITY&` - The Riding Entity Message, if in use. (See `Advanced Settings :: Entity Riding Messages` for sub-placeholders)

___

### Main Menu Message Placeholders
These placeholders translate to the `&MAINMENU&` Global Placeholder in the `Presence Settings` menu within the Config Gui.

You can configure using these Sub-Placeholders within the `Status Messages::Main Menu Message` setting.

*   `&IGN&` - Your Minecraft username
*   `&MODS&` - The number of mods currently in your mods folder

___

### Biome Placeholders
These placeholders translate to the `&BIOME&` Global Placeholder in the `Presence Settings` menu within the Config Gui.

You can configure these Sub-Placeholders throughout the `Biome Messages` area of the Config Gui.

*   `&BIOME&` - The Current Biome Name

___

### Dimension Placeholders
These placeholders translate to the `&DIMENSION&` Global Placeholder in the `Presence Settings` menu within the Config Gui.

You can configure these Sub-Placeholders throughout the `Dimension Messages` area of the Config Gui.

*   `&DIMENSION&` - The Current Dimension Name
*   `&ICON&` - The Default Dimension Icon Name

___

### Server/LAN Message Placeholders
These placeholders translate to the `&SERVER&` Global Placeholder in the `Presence Settings` menu within the Config Gui.

You can configure these Sub-Placeholders throughout the `Server Messages` area of the Config Gui.

*   `&IP&` - The Current Server IP Address
*   `&NAME&` - The Current Server Name
*   `&MOTD&` - The Current Server MOTD (Message of The Day)
*   `&ICON&` - The Default Server Icon Name
*   `&PLAYERS&` - The Current Player Count `(10 / 100 Players)`
*   `&IGN&` - Your Minecraft Username
*   `&TIME&` - The Current World Time
*   `&MODS&` - The Number of Mods currently in your Mods Folder

___

### SinglePlayer Placeholders
These placeholders translate to the `&SERVER&` Global Placeholder in the `Presence Settings` menu within the Config Gui.

You can configure these Sub-Placeholders within the `Status Messages::SinglePlayer Message` setting.

*   `&IGN&` - Your Minecraft Username
*   `&TIME&` - The Current World Time
*   `&MODS&` - The Number of Mods currently in your Mods Folder

___

### Gui Placeholders
These placeholders translate to the `&SCREEN&` Global Placeholder in the `Presence Settings` menu within the Config Gui.

You can configure these Sub-Placeholders throughout the `Advanced Settings::Gui Messages` area of the Config Gui.

*   `&SCREEN&` - The Current Gui Screen Name (Supports `Container` and `Screen` type interfaces)
*   `&CLASS&` - The Current Gui Class (Ex: `net.minecraft.xxx`)

___

### Item Placeholders
These placeholders translate to the `&TILEENTITY&` Global Placeholder in the `Presence Settings` menu within the Config Gui.

You can configure these Sub-Placeholders throughout the `Advanced Settings::Item Messages` area of the Config Gui.

*   `&MAIN&` - The Current Item your Main Hand is Holding
*   `&OFFHAND&` - The Current Item your Off Hand is Holding
*   `&HELMET&` - The Current Helmet Armor Piece you have Equipped
*   `&CHEST&` - The Current Chest Armor Piece you have Equipped
*   `&LEGS&` - The Current Leggings Armor Piece you have Equipped
*   `&BOOTS&` - The Current Boots Armor Piece you have Equipped

___

## Entity Placeholders
These placeholders translate to the `&TARGETENTITY&`, `&ATTACKINGENTITY&`, and `&RIDINGENTITY&` Global Placeholder in the `Presence Settings` menu within the Config Gui.

You can configure these Sub-Placeholders throughout the `Advanced Settings::Entity [Target,Attacking,Riding] Messages` area of the Config Gui.

*   `&ENTITY&` - The Entity Name

Additionally, these sub placeholders support nbt data, where in this case the Entity's nbt data is parsed into sub-placeholders (Outlined in Tooltips)

## Versions of CraftPresence
Beginning in v1.5.2, CraftPresence is now split into different editions, based on the Minecraft Version you use it in:

*   Legacy Version (Minecraft 1.2.5 and Below):
    *   Server Support is unavailable in 1.2.5 and Below (Only SinglePlayer will work with showGameStatus Enabled)
    *   Minecraft 1.1.0 and Below may not work on Forge, and may require a Modified Minecraft Jar with Risugami's ModLoader + ModLoaderMP

**Support for issues related to Vanilla code, Forge, or Risugami's ModLoader is extremely limited**

## Disclaimers & Additional Info

### Minecraft Object Obfuscation
Due to Obfuscation in Minecraft, some of Minecraft Objects such as Screens, Dimensions, or Servers must be opened once in the session to be separately customized.

### Icon Requesting
Not seeing an Icon you like or have a suggestion for an Icon to add/modify on the Default Client ID?

If so, you can make a request on my [Issue Tracker](https://gitlab.com/CDAGaming/CraftPresence/issues/), with the following requirements:

*   If adding an Icon from a dimension, Specify the Mod's Link that the dimension derives from
    *   This is because specific Icon IDs must be used, which can be found by the mod or from checking your Logs/Chat after entering the dimension as CraftPresence will tell you the ID expected

*   A `512x512` or `1024x1024` Icon to be used
    *   Icons near these sizes can be used, but may not give a great final quality

*   If requesting an icon to be modified or removed from the Default Client ID, please specify a reason why
    *   Mostly just so it can be logged why it was changed for future reference

Additionally, you can also create your own Set of Icons by following [this guide](https://gitlab.com/CDAGaming/CraftPresence/-/wikis/Making-your-Own-Client-ID---Using-your-own-Images)

### Support
Need some assistance with one of my mods or wish to provide feedback?

I can be contacted via the following methods:

*   [Email](mailto:cstack2011@yahoo.com)
*   [CurseForge](https://www.curseforge.com/minecraft/mc-mods/craftpresence)
*   [Discord :: ![Discord Chat](https://img.shields.io/discord/455206084907368457.svg)](https://discord.com/invite/BdKkbpP)

Additionally, codebase documentation for this mod is available [here](https://cdagaming.gitlab.io/craftpresence-documentation/) with further guides available on [the wiki](https://gitlab.com/CDAGaming/CraftPresence/-/wikis/Home)

#### Licensing
This Mod is under the MIT License as well as the Apache 2.0 License

This project makes usage of the following dependencies internally:

*   [DiscordIPC API](https://github.com/jagrosh/DiscordIPC) by [jagrosh](https://github.com/jagrosh)
    *   [JUnixSocket](https://github.com/kohlschutter/junixsocket) by [kohlschutter](https://github.com/kohlschutter)

*   [Java Native Access (JNA) API](https://github.com/java-native-access/jna) on v1.5.x and Below

#### Discord Terms of Service
As with other RPC Mods, this Mod uses your in-Game Data to send Display Information to a 3rd Party
Service (In this Case, Discord).

The Terms of Service relating to Creating a Discord ID for icons can be found [here](https://discord.com/developers/docs/legal)

The Terms of Service for using Discord as a service can additionally be located [here](https://discord.com/new/terms)
