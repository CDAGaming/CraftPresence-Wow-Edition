# CraftPresence
Completely customize the way others see you play World of Warcraft via Discord's Rich Presence API & the pypresence API by [qwertyquerty](https://github.com/qwertyquerty/pypresence)!

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/9f6a38f53cd148aebb307112c7ccd947)](https://www.codacy.com/gl/CDAGaming/CraftPresence-Wow-Edition/dashboard?utm_source=gitlab.com&amp;utm_medium=referral&amp;utm_content=CDAGaming/CraftPresence-Wow-Edition&amp;utm_campaign=Badge_Grade)
[![CraftPresence Workflow](https://github.com/CDAGaming/CraftPresence-Wow-Edition/actions/workflows/main.yml/badge.svg)](https://gitlab.com/CDAGaming/CraftPresence-Wow-Edition/-/commits/master)

[![CurseForge-Downloads](http://cf.way2muchnoise.eu/full_craftpresence_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/craftpresence)
[![CurseForge-Availability](http://cf.way2muchnoise.eu/versions/craftpresence.svg)](https://www.curseforge.com/minecraft/mc-mods/craftpresence)

## Author's Note
*   At this time, the pixel-generation/reading that makes this possible has not been verified by Blizzard. An email and ticket have been put in to gain approval, though until this happens, use with caution.
*   While a video, is aditionally planned for how to set this up, more info on how to install can be found [here](https://gitlab.com/CDAGaming/CraftPresence/-/wikis/Install-Guide-for-World-of-Warcraft-%7C-Minified)

## Port Notes
*   Please check [here](https://www.curseforge.com/minecraft/mc-mods/craftpresence) for the Minecraft Version of this addon

## Features
In addition to having the ability to change your discord status from "Playing <x>",
This mod offers plenty of customization options to specify entirely how others see you play.
From having your current zone show up, to which instance your in, as well as which realm you're in, and far more.
The customization possibilities are limitless, with the only real limit being how creative you customize your display.

### Client Support
CraftPresence will detect and adjust its abilities towards the following versions:

| Version                  | Status | Download | Notes           |
|--------------------------|--------|----------|-----------------|
| Shadowlands (9.x)        | OK     | Any      | N/A             |
| BFA (8.x)                | OK     | Any      | N/A             |
| Legion (7.x)             | OK     | Any      | v0.5.4 or above |
| WOD (6.x)                | OK     | Any      | v0.5.4 or above |
| MOP (5.x)                | OK     | Any      | v0.5.4 or above |
| Cataclysm (4.x)          | OK     | Any      | v0.5.4 or above |
| WOTLK (3.x)              | OK     | Any      | v0.5.4 or above |
| TBC Classic (2.5.x)      | OK     | Any      | N/A             |
| TBC (2.0.x - 2.4.x)      | OK     | Any      | v0.5.4 or above |
| Vanilla Classic (1.13.x) | OK     | Any      | N/A             |
| Vanilla (Under 1.12.x)   | OK     | Any      | v0.7.0 or above |

As this addon uses a singular codebase, Addon code is loaded depending upon which version you are loading it within.

For example, this will mean that while the placeholders will remain the same, their contents may change depending on the version.

Aditionally, while PTR and pre-release versions of the game should work fine, they are often untested until closer to release and may contain issues.

## Commands
CraftPresence currently offers the following Commands:

Keep in mind the following:

*   Commands must be prefixed by either `/craftpresence` or `/cp`

___

*   `/cp config` - Displays the Config menu to customize options and placeholder data
*   `/cp update` - Reloads mod data (Ie forces a Tick Event)
*   `/cp test` - Toggles debugging of Rich Presence Frames (Requires `Debug Mode`)
*   `/cp (clear | clean)` - Reset all frames to their original positions and colors.
*   `/cp status` - Displays your last sent RPC Data, in text form (Requires `Verbose Mode`)
*   `/cp set (grp,key)` - Sets specific setting(s) within the config menu manually.
*   `/cp reset (:grp,key)` - Resets all (or a specific) setting(s) within the config menu.
*   `/cp placeholders (:query)` - Views the currently available placeholders (With specified query, if any)
*   `/cp minimap` - Toggles the display of the minimap button (Requires Reload)
*   `/cp (help | ?)` - Help Command to display the above commands and these explanations

## Placeholders
In some configuration areas, CraftPresence provides some placeholders to make things easier:

Keep in mind the following:

*   Placeholders are not case-sensitive, but should be entered lowercase to prevent issues with recognizing them on v1.5.x and below
*   Global placeholders (Represented by text surrounded by `#` symbols) cannot include other global's but can include Inner Placeholders
*   Inner placeholders (Represented by text surrounded by `@` symbols) cannot include other placeholders within them

___

### Presence Display / Global Placeholders
These placeholders can be added within the `General Settings` menu within the Config Gui.

As these placeholders are global, they can be set in any of the RPC fields within `Presence Settings` and customized at a deeper level via usage of the inner-placeholders.

*   `#dungeon#` - The message to generate whilst in a scenario instance. (See `Placeholders::Dungeon Placeholder Message` for more info)
*   `#raid#` - The message to generate whilst in a raid instance. (See `Placeholders::Raid Placeholder Message` for more info)
*   `#battleground#` - The message to generate whilst in a battleground instance. (See `Placeholders::Battleground Placeholder Message` for sub-placeholders)
*   `#arena#` - The message to generate whilst in an arena instance. (See `Placeholders::Arena Placeholder Message` for sub-placeholders)
*   `#default#` - The message to generate whilst not in a detected instance. (See `Placeholders::Default Placeholder Message` for sub-placeholders)

___

### Presence Display / Inner Placeholders
These placeholders are able to work alongside of/within the aforementioned Global Placeholders.

You can configure some of these placeholders throughout different areas of the game.

*   `@player_info@` - (Preset) Shows basic character info such as status, name, and level (Version-dependent)
*   `@player_name@` - Your player's in-game name
*   `@title_name@` - The currently active title for your character, or `@player_name@` if not applicable
*   `@player_level@` - The player's current in-game level
*   `@player_class@` - The player's current in-game class name
*   `@player_status@` - The player's current in-game status (Support AFK, DND, and alive states)
*   `@player_alliance@` - (Preset) The player's current faction/covenant (Dependent on location and applicability)
*   `@player_covenant@` - The player's current covenant name, or `None` if not applicable
*   `@player_covenant_renown@` - The player's current covenant renown level, if applicable
*   `@player_faction@` - The player's current faction name
*   `@player_spec_name@` - The player's current specialization name
*   `@player_spec_role@` - The player's current instance role, tied to current spec if applicable
*   `@item_level@` - The player's current item level
*   `@item_level_equipped@` - The player's currently equipped item level
*   `@item_level_pvp@` - The player's currently scaled item level, for pvp scenarios
*   `@realm_info@` - (Preset) The player's current region and realm in use
*   `@player_region@` - The player's current region in use
*   `@player_realm@` - The player's current realm in use
*   `@zone_info@` - (Preset) The player's current location, formatted with zone and sub-zone info as applicable
*   `@zone_name@` - The player's current in-game zone name, or `Unknown` if not applicable
*   `@sub_zone_name@` - The player's current in-game sub-zone name, or `Unknown` if not applicable
*   `@difficulty_name@` - The player's current difficulty level (Applicable while in a valid instance)
*   `@difficulty_info@` - (Preset) The player's current difficulty info, formatted with keystone and difficulty data as applicable
*   `@active_keystone_level@` - The player's currently active keystone level, if applicable
*   `@active_keystone_affixes@` - The player's currently active keystone affix names, if applicable
*   `@owned_keystone_level@` - The player's currently owned keystone level, if applicable
*   `@instance_type@` - The player's current in-game instance type, or `none` if not applicable
*   `@localized_name@` - The player's current in-game map instance name (Can be otherwise known as the continent name)
*   `@instance_difficulty@` - The player's current instance difficulty id, if applicable
*   `@max_players@` - The current amount of players allowed in the given instance, if applicable
*   `@dynamic_difficulty@` - The dynamic difficulty level for an instance, if applicable
*   `@is_dynamic@` - Whether the instance difficulty can be changed while zoned in, if applicable
*   `@instance_id@` - The player's current in-game instance map id, if applicable
*   `@instance_group_size@` - The number of players currently within your instance group, if applicable
*   `@lfg_dungeon_id@` - The dungeon id for the player's current instance, if applicable
*   `@lockout_encounters@` - (Preset) The player's current lockout info within an instance, if applicable, containing total and current data
*   `@lockout_current_encounters@` - The player's current encounters/stages completed in an instance, if applicable
*   `@lockout_total_encounters@` - The player's total encounters/stages within an instance, if applicable

___

## Disclaimers & Additional Info

### Icon Requesting
Not seeing an Icon you like or have a suggestion for an Icon to add/modify on the default Client ID?

If so, you can make a request on my [Issue Tracker](https://gitlab.com/CDAGaming/CraftPresence/issues/), with the following requirements:

*   If adding an Icon from a dimension, specify the Mod's link that the dimension derives from
    *   This is because specific Icon IDs must be used, which can be found by the mod or from checking your Logs/Chat after entering the dimension as CraftPresence will tell you the ID expected

*   An Icon of size between `512x512` and `1024x1024` to be used (Either minimum or recommended size for best quality)
    *   Icons between these sizes can be used, but may not give a great final quality

*   If requesting an icon to be modified or removed from the Default Client ID, please specify a reason why
    *   Mostly just so it can be logged why it was changed for future reference

Additionally, you can also create your own Set of Icons by following [this guide](https://gitlab.com/CDAGaming/CraftPresence/-/wikis/Making-your-Own-Client-ID---Using-your-own-Images)

### Support
Need some assistance with one of my mods or wish to provide feedback?

I can be contacted via the following methods:

*   [Email](mailto:cstack2011@yahoo.com)
*   [CurseForge - Minecraft Edition](https://www.curseforge.com/minecraft/mc-mods/craftpresence)
*   [Discord :: ![Discord Chat](https://img.shields.io/discord/455206084907368457.svg)](https://discord.com/invite/BdKkbpP)

Additionally, codebase documentation for this mod is available [here](https://gitlab.com/CDAGaming/CraftPresence-Wow-Edition) with further guides available on [the wiki](https://gitlab.com/CDAGaming/CraftPresence/-/wikis/Home)

#### Licensing
This Mod is under the MIT License as well as the Apache 2.0 License

This project makes usage of the following dependencies internally:

*   [Ace3](https://www.wowace.com/projects/ace3) by [Kaelten](https://www.wowace.com/members/kaelten)
    *   [LibDBIcon-1.0](https://www.curseforge.com/wow/addons/libdbicon-1-0) by [funkehdude](https://www.curseforge.com/members/funkehdude)
    *   [LibDataBroker-1.1](https://www.curseforge.com/wow/addons/libdatabroker-1-1) by [tekkub](https://www.curseforge.com/members/tekkub)
    *   [CallbackHandler-1.0](https://www.curseforge.com/wow/addons/callbackhandler) by [nevcairiel](https://www.curseforge.com/members/nevcairiel)
    *   [LibStub](https://www.wowace.com/projects/libstub) by [Kaelten](https://www.wowace.com/members/kaelten)

#### Discord Terms of Service
As with other RPC Mods, this Mod uses your in-game data to send display information to a 3rd party service (In this Case, Discord).

The terms of service relating to Creating a Discord ID for icons can be found [here](https://discord.com/developers/docs/legal)

The terms of service for using Discord as a service can additionally be located [here](https://discord.com/new/terms)
