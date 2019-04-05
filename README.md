# CraftPresence
Completely customize the way others see you play Minecraft via Discord's Rich Presence API & the JNA (Java Native Access) API!

[![Pipeline Status](https://gitlab.com/CDAGaming/CraftPresence/badges/master/pipeline.svg)](https://gitlab.com/CDAGaming/CraftPresence/commits/master)
[![CurseForge-Downloads](http://cf.way2muchnoise.eu/full_297038_downloads.svg)](https://minecraft.curseforge.com/projects/craftpresence)
[![CurseForge-Availability](http://cf.way2muchnoise.eu/versions/For%20MC_297038_all.svg)](https://minecraft.curseforge.com/projects/craftpresence)

## Port Notes
- CraftPresence for Minecraft 1.14.x requires the [FabricMC ModLoader](https://minecraft.curseforge.com/projects/fabric)
- CraftPresence for Minecraft 1.13 requires the [Rift ModLoader](https://minecraft.curseforge.com/projects/rift)
- CraftPresence for Minecraft 1.13.2 (Rift Edition) requires [Chocohead's Version of Rift](https://jitpack.io/com/github/chocohead/rift/2c400465c7e0b5abf4e643372fbd9a7e6d39e3ff/rift-2c400465c7e0b5abf4e643372fbd9a7e6d39e3ff.jar)

## Features
Besides having the Ability to Change your Discord Status from "Playing Minecraft",
This Mod offers plenty of Customization options to specify entirely how others see you play.
From Having Your Current Biome Show Up, To which Dimension you in, as well as which server your in,
The Customization Possibilities are limitless, with the only real limit being how creative you customize your display.

#### Twitch // CursePack // MultiMC // MCUpdater // Technic Support
At this time, CraftPresence will detect whether your Launch Directory contains a valid Twitch Manifest (manifest.json), a MultiMC Instance (instance.cfg), a MCUpdater Instance (instance.json), or a Technic installedPacks File (installedPacks).
If so, It'll put the Packs Name in your Display as well as Show it's Icon (when not in a server).

As an example, this is how the mod will convert the pack name to an iconKey:

`All the Mods 3 >> allthemods3`

(Note the above format only applies to Twitch & Technic Packs ; MultiMC natively has an Icon Key Property)

## Commands
CraftPresence currently offers the following Commands:

(Commands must be prefixed by either `/craftpresence` or `/cp`)

(In v1.5.0 and Above, Commands are now Inputted via a new menu called "Commands" in the Config GUI)

- `/cp view` - Help Command to display Commands available to view a variety of Display Data
- `/cp reload` - Reloads Mod Data (In v1.4.8 and Above, this forces a Tick Event)
- `/cp reboot` - Reboots RPC
- `/cp shutdown` - Shutdown RPC (Can be turned on from `/cp reboot`)
- `/cp request` - View Join Request Info
- `/cp view currentData` - Displays in Text Form, your Current RPC Data
- `/cp view assets <large|small|all>` - Displays all Asset IconKeys available to you
- `/cp view dimensions` - Displays all Dimension Names available for use, if Showing Current Dimension is enabled
- `/cp view biomes` - Displays all Biome Names available for use, if Showing Current Biome is enabled
- `/cp view servers` - Displays all Server Addresses available for use, if Show Game State is enabled
- `/cp view guis` - Displays all GUI Names available for use, if Per-GUI is Enabled
- `/cp view items` - Displays all Item Names available for use, if Per-Item is Enabled
- `/cp <help|?>` - Help Command to display the Above Commands and These Explanations

## Placeholders
In some Configuration Areas, CraftPresence provides some Placeholders to make things easier:

(Placeholders are not Case-Sensitive, but should be entered lowercase to prevent issues recognizing them*)

#### Main Menu Message Placeholders
- &IGN& - Your Minecraft Username
- &MODS& - The Amount of Mods currently in your Mods Folder

#### Biome Placeholders
- &BIOME& - The Current Biome Name
- &ID& - The Current Biome ID

#### Dimension Placeholders
- &DIMENSION& - The Current Dimension Name
- &ICON& - The Default Dimension Icon Name
- &ID& - The Current Dimension ID

#### Server/LAN Message Placeholders
- &IP& - The Current Server IP Address
- &NAME& - The Current Server Name
- &MOTD& - The Current Server MOTD (Message of The Day)
- &ICON& - The Default Server Icon Name
- &PLAYERS& - The Current Player Count `(10 / 100 Players)`
- &IGN& - Your Minecraft Username
- &TIME& - The Current World Time
- &MODS& - The Amount of Mods currently in your Mods Folder

#### SinglePlayer Placeholders
- &IGN& - Your Minecraft Username
- &TIME& - The Current World Time
- &MODS& - The Amount of Mods currently in your Mods Folder

#### GUI Placeholders*
- &GUI& - The Current GUI Name (Supports GUIContainers and GUIScreens)
- &CLASS& - The Current GUI Class (Ex: net.minecraft.xxx)
- &SCREEN& - The Current GUI Screen Instance

#### Item/Entity Placeholders
- &MAIN& - The Current Item your Main Hand is Holding
- &OFFHAND& - The Current Item your Off Hand is Holding*
- &HELMET& - The Current Helmet Armor Piece you have Equipped
- &CHEST& - The Current Chest Armor Piece you have Equipped
- &LEGS& - The Current Leggings Armor Piece you have Equipped
- &BOOTS& - The Current Boots Armor Piece you have Equipped

## Versions of CraftPresence
Beginning in v1.5.2, CraftPresence is now split into two different Editions, based on the MC Version you use it in:

- Full Version (MC 1.3.2 - 1.14.x) includes All Features of CraftPresence at Full Support for Issues
  - In MC 1.5.2 and Below, a Separate Translation Engine is being used, and may cause issues (In Higher Versions, this is only used as a fallback)

- Legacy Version (MC 1.2.5 and Below)
  - Unavailable Server Support in 1.2.5 and Below (Only SinglePlayer will work with showGameStatus Enabled)
  - Support for Issues related to Vanilla Code, Forge, or ModLoader is extremely Limited

## Disclaimers
#### GUI Support for Minecraft GUIs
Due to Obfuscation from Mapping Providers, Minecraft GUIs must be opened once in order to be separately customized.

#### Discord TOS
As with other RPC Mods, this Mod uses your in-Game Data to send Display Information to a 3rd Party
Service (In this Case, Discord).

The Terms of Service relating to Creating your own Discord ID can be found [here](https://discordapp.com/developers/docs/legal)
