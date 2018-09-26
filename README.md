# CraftPresence
Completely customize the way others see you play Minecraft via Discord's Rich Presence API & JNA (Java Native Access) API!

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/af71fdc17825460abdfd562777ac5133)](https://www.codacy.com/app/CDAGaming/CraftPresence?utm_source=gitlab.com&amp;utm_medium=referral&amp;utm_content=CDAGaming/CraftPresence&amp;utm_campaign=Badge_Grade)
[![Pipeline Status](https://gitlab.com/CDAGaming/CraftPresence/badges/master/pipeline.svg)](https://gitlab.com/CDAGaming/CraftPresence/commits/master)
[![CurseForge-Downloads](http://cf.way2muchnoise.eu/full_297038_downloads.svg)](https://minecraft.curseforge.com/projects/craftpresence)
[![CurseForge-Availability](http://cf.way2muchnoise.eu/versions/For%20MC_297038_all.svg)](https://minecraft.curseforge.com/projects/craftpresence)

## Features
Besides having the Ability to Change your Discord Status from "Playing Minecraft",
This Mod offers plenty of Customization options to specify entirely how others see you play.
From Having Your Current Biome Show Up, To which Dimension you in, as well as which server your in,
The Customization Possibilities are limitless, with the only real limit being how creative you customize your display

#### Twitch // CursePack // MultiMC // Technic Support
At this time, CraftPresence will detect whether your Launch Directory contains a Valid Twitch Manifest (manifest.json), a MultiMC Instance (instance.cfg), or a Technic installedPacks File (installedPacks).
If so, It'll put the Packs Name in your Display as well as Show it's Icon (when not in a server).

As an example, this is how the mod will convert the pack name to an iconKey:

`All the Mods 3 >> allthemods3`

(Note the above format only applies to Twitch & Technic Packs ; MultiMC natively has an Icon Key Property)

#### Advanced Settings
This section of the Config holds more technical settings such as Debug Logging,
Split Characters for other config sections, as well as a Per-Event System to change
your display depending on which GUI/Action your doing.

## Commands
Beginning in v1.3.0, CraftPresence now offers the following Commands:

(Commands are prefixed by `/craftpresence` or `/cp`)
(Commands Listed are the Syntax as of v1.4.0*)

- `/cp view` - View a Variety of Display Data
- `/cp config` - Opens CraftPresence's Config GUI
- `/cp reload` - Reloads Mod Data based on which Settings you have enabled
- `/cp reboot` - Reboots RPC
- `/cp shutdown` - Shutdown RPC (Can be turned on from `/cp reboot`)
- `/cp view currentData` - Displays in Text Form, your Current RPC Data
- `/cp view assets <large|small|all>` - Displays all Asset IconKeys available to you
- `/cp view dimensions` - Displays all Dimension Names available for use
- `/cp help // /cp ?` - Help Command to display the Above Commands and These Explanations

## Placeholders
In some Configuration Areas, CraftPresence Provides some Placeholders to make things easier:

(Placeholders are also not Case-Sensitive, but should be entered lowercase*)

(If Connecting to a Server via Direct Connect, CraftPresence will use the Default MOTD and Name, If you use those placeholders in your config*)

#### Biome Placeholders
- &BIOME& - The Current Biome Name
- &ID& - The Current Biome ID

#### Dimension Placeholders
- &DIMENSION& - The Current Dimension Name
- &ICON& - The Default Dimension Icon Name
- &ID& - The Current Dimension ID

#### Server Placeholders
- &IP& - The Current Server IP
- &NAME& - The Current Server Name
- &MOTD& - The Current Server MOTD (Message of The Day)
- &ICON& - The Default Server Icon Name
- &PLAYERS& - The Current Player Count `(10 / 100 Players)`
- &IGN& - Your Minecraft Username

#### SinglePlayer Placeholders
- &IGN& - Your Minecraft Username

#### GUI Placeholders
- &GUI& - The Current GUI Name (Supports GUIContainers and GUIScreens)
- &CLASS& - The Current GUI Class (Ex: net.minecraft.xxx)
- &SCREEN& - The Current GUI Screen Instance

#### Item/Entity Placeholders
- &MAIN& - The Current Item your Main Hand is Holding
- &OFFHAND& - The Current Item your Off Hand is Holding*

(Starting in v1.4.0, this is your OffHand Item's Message*)

## Disclaimer
As with other RPC Mods, this Mod uses your in-Game Data to send Display Information to a 3rd Party
Service (In this Case, Discord).

The Terms of Service relating to Creating your own Discord ID can be found [here](https://discordapp.com/developers/docs/legal)