# CraftPresence
Completely customize the way others see you play World of Warcraft via Discord's Rich Presence API & the pypresence API by [qwertyquerty](https://github.com/qwertyquerty/pypresence)!

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/9f6a38f53cd148aebb307112c7ccd947)](https://www.codacy.com/gl/CDAGaming/CraftPresence-Wow-Edition/dashboard?utm_source=gitlab.com&amp;utm_medium=referral&amp;utm_content=CDAGaming/CraftPresence-Wow-Edition&amp;utm_campaign=Badge_Grade)
[![CraftPresence Workflow](https://github.com/CDAGaming/CraftPresence-Wow-Edition/actions/workflows/main.yml/badge.svg)](https://gitlab.com/CDAGaming/CraftPresence-Wow-Edition/-/commits/master)

[![CurseForge-Downloads](https://cf.way2muchnoise.eu/full_457334_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/craftpresence)
[![CurseForge-Availability](https://cf.way2muchnoise.eu/versions/457334.svg)](https://www.curseforge.com/minecraft/mc-mods/craftpresence)

## How To Install
*   This addon requires addition steps to setup, more info can be found [here](https://gitlab.com/CDAGaming/CraftPresence/-/wikis/Install-Guide-for-World-of-Warcraft-%7C-Minified)

## Port Notes
*   Please check [here](https://www.curseforge.com/minecraft/mc-mods/craftpresence) for the Minecraft Version of this addon

## Features
In addition to having the ability to change your discord status from "Playing *gameNameHere*",
This mod offers plenty of customization options to specify entirely how others see you play.
From having your current zone show up, to which instance your in, as well as which realm you're in, and far more.
The customization possibilities are limitless, with the only real limit being how creative you customize your display.

### Client Support
CraftPresence will detect and adjust its abilities towards the following versions:

| Version                           | Status | Download | Notes           |
|-----------------------------------|--------|----------|-----------------|
| The War Within (11.x)             | PENDING| Any      | N/A             |
| Dragonflight (10.x)               | OK     | Any      | N/A             |
| Shadowlands (9.x)                 | OK     | Any      | N/A             |
| BFA (8.x)                         | OK     | Any      | N/A             |
| Legion (7.x)                      | OK     | Any      | v0.5.4 or above |
| WOD (6.x)                         | OK     | Any      | v0.5.4 or above |
| MOP (5.x)                         | OK     | Any      | v0.5.4 or above |
| Cataclysm Classic (4.4.x)         | OK     | Any      | N/A             |
| Cataclysm (4.0.x - 4.3.x)         | OK     | Any      | v0.5.4 or above |
| WOTLK Classic (3.4.x)             | OK     | Any      | N/A             |
| WOTLK (3.0.x - 3.3.x)             | OK     | Any      | v0.5.4 or above |
| TBC Classic (2.5.x)               | OK     | Any      | N/A             |
| TBC (2.0.x - 2.4.x)               | OK     | Any      | v0.5.4 or above |
| Vanilla Classic (1.13.x - 1.15.x) | OK     | Any      | N/A             |
| Vanilla (1.12.x and below)        | OK     | Any      | v0.7.8 or above |

As this addon uses a singular codebase, different segments of code trigger depending on which version you are loading it within.

For example, this will mean that while the placeholders will remain the same, their contents may change depending on the version.

Additionally, while PTR and pre-release versions of the game should work fine, they are often untested until closer to release and may contain issues.

## Commands
CraftPresence currently offers the following Commands:

Keep in mind the following:

*   Commands must be prefixed by either `/craftpresence` or `/cp`

___

*   `/cp (help | ?)` - Help Command to display the below commands and these explanations
*   `/cp config (migrate,standalone)` - Displays the Config menu to customize options and placeholder data
*   `/cp (clear,clean)` - Reset all frames to their original positions and colors.
*   `/cp update (debug)` - Force or Debug an RPC update
*   `/cp minimap` - Toggles the display of the minimap button
*   `/cp compartment` - Toggles the display of the addon compartment entry
*   `/cp status` - Displays your last sent RPC Data, in text form (Requires `Verbose Mode`)
*   `/cp reset (grp,key)` - Resets all (or a specific) setting(s) within the config menu.
*   `/cp set (grp,key)` - Sets specific setting(s) within the config menu manually.
*   `/cp integration (query)`  -  Enable optional integrations, using specified query
*   `/cp placeholders (create,remove,list) (query)` - Views/modifies the currently available placeholders.
*   `/cp events (create,remove,list) (query)` - Views/modifies the currently available events.
*   `/cp labels (create,remove,list) (query)` - Views/modifies the currently available labels.

## Placeholders
In some configuration areas, CraftPresence provides some placeholders to make things easier:

Keep in mind the following:

*   Placeholder can either be presets or use direct info. Presets use a combination of direct info placeholders to return a formatted layout, while direct info placeholders simply use 1-2 API calls to return it's output with very minimal formatting.
*   Some placeholders may not be available in some clients, either due to API instability or simply because the API call(s) it uses do not exist.
*   While this list is quite expansive, users can create their own placeholders with the `/cp placeholders` command.
*   Creating more advanced layouts *might* require some Lua experience, as well as experience with the World of Warcraft APIs. Usage of the `Events` section may also be needed if your placeholder requires the usage of specific in-game events that this addon does not already have registered.

___

### Presence Display / Global Placeholders
These placeholders can be added within the `Presence` and `Buttons` menu within the Config Gui.

These types of placeholders are designed for more generalized information, such as what to display in certain instances. They often combine quite a few inner placeholders representing the instance.

*   `#default#` - The message to generate whilst not in a detected instance.
*   `#arena#` - The message to generate whilst in an arena instance.
    *   Availability: 3.2.0 clients and above (Or rebased clients)
*   `#battleground#` - The message to generate whilst in a battleground instance.
    *   Availability: 3.2.0 clients and above (Or rebased clients)
*   `#raid#` - The message to generate whilst in a raid instance.
    *   Availability: 3.2.0 clients and above (Or rebased clients)
*   `#dungeon#` - The message to generate whilst in a dungeon instance.
    *   Availability: 3.2.0 clients and above (Or rebased clients)
*   `#scenario#` - The message to generate whilst in a scenario instance.
    *   Availability: 3.2.0 clients and above (Or rebased clients)

___

### Presence Display / Inner Placeholders
These placeholders can be added within the `Presence` and `Buttons` menu within the Config Gui.

These types of placeholders are designed for specialized pieces of information, or rather a subset of information.

#### __Presets__

*   `@player_info@` - Shows basic character info such as status, name, and level
*   `@player_icon@` - The player's current in-game personality icon, using both `@player_race@` and `@player_gender@` (Format: `race_gender`)
*   `@realm_info@` - The player's current region and realm in use
*   `@zone_info@` - The player's current location, formatted with zone and sub-zone info as applicable
*   `@difficulty_info@` - The player's current difficulty info, formatted with keystone and difficulty data as applicable
    *   Availability: 3.2.0 clients and above (Or rebased clients)
*   `@lockout_encounters@` - The player's current lockout info within an instance, if applicable, containing total and current data
    *   Availability: 6.0.0 clients and above

#### __Direct Info__

*   `@player_name@` - [Your player's in-game name][UnitName]
*   `@title_name@` - [The currently active title for your character][UnitPVPName], or `@player_name@` if not applicable
*   `@player_level@` - [The player's current in-game level][UnitLevel]
*   `@player_realm@` - [The player's current realm in use][GetRealmName]
    *   Will return `Unknown` if function is not available
*   `@player_region@` - [The player's current region in use][GetCurrentRegion]
    *   Returns `regionTable[playerRegionId]` or `TR` as the output
    *   `regionTable` can be tweaked to customize the abbreviations, if desired
*   `@player_class@` - [The player's current in-game class name][UnitClass]
*   `@player_race@` - [The player's current in-game race name][UnitRace]
*   `@player_gender@` - [The player's current in-game gender name][UnitSex]
    *   Returns `genderData[playerGenderId or 1]` as the output
    *   `genderData` can be tweaked to customize the result, if desired
*   `@player_status@` - The player's current in-game status
    *   Refer to the `Labels` section of the Config UI
    *   By default, this function supports Away, Busy, and Dead/Alive player states
    *   Will return `Unknown` if there is no applicable data
*   `@player_reason@` - The player's current in-game reason message
    *   While normally empty, certain player states (Such as being AFK or Busy) can supply the reason string to be used here.
*   `@player_faction@` - [The player's current faction name][UnitFactionGroup]
    *   Returns the `localizedFaction`, with a fallback to `englishFaction`, then `None` if neither exist.
*   `@player_alliance@` - The player's current dynamic faction name
    *   This is a highly dynamic placeholder that can change depending on location, expansion content, and applicability.
    *   For 9.x Zones, this will return the [name of your current covenant][C_Covenants.GetCovenantData]
    *   If not in any of the aforementioned conditions, this will return the same as `@player_faction@`.
*   `@player_covenant@` - [The player's current covenant name][C_Covenants.GetCovenantData]
    *   Availability: 9.0.0 clients and above
    *   Returns `None` if the player does not have a covenant
*   `@player_spec_name@` - The player's current specialization name
    *   References: [GetSpecialization], [GetSpecializationInfo], [GetSpecializationRoleByID]
    *   Availability: 5.0.4 clients and above
    *   Returns `None` if the player does not have a specialization
*   `@player_spec_role@` - The player's current instance role, tied to current spec if applicable
    *   References: [GetSpecialization], [GetSpecializationInfo], [GetSpecializationRoleByID]
    *   Availability: 5.0.4 clients and above
    *   Returns `None` if the player does not have a specialization
*   `@player_covenant_renown@` - [The player's current covenant renown level][C_CovenantSanctumUI.GetRenownLevel]
    *   Availability: 9.0.0 clients and above
*   `@localized_name@` - The player's current in-game map instance name
    *   This can be otherwise known as the continent name
    *   Returns `Unknown` if there is no applicable data
    *   For Clients at 3.2.0 or above (Or clients that are rebased), this function uses the 1st return value from [GetInstanceInfo]
    *   For Clients below 3.2.0 that are not rebased, this function uses the World Map's continent name
*   `@instance_type@` - The player's current in-game instance type
    *   Uses the 2nd return value from [GetInstanceInfo]
    *   Availability: 3.2.0 clients and above (Or rebased clients)
*   `@instance_difficulty@` - The player's current instance difficulty id
    *   Uses the 3rd return value from [GetInstanceInfo]
    *   Availability: 3.2.0 clients and above (Or rebased clients)
*   `@difficulty_name@` - The player's current difficulty level name
    *   Uses the 4th return value from [GetInstanceInfo]
    *   Availability: 3.2.0 clients and above (Or rebased clients)
*   `@current_players@` - The current amount of players allowed in the given instance
    *   References: [GetNumGroupMembers], [GetNumPartyMembers], [GetNumRaidMembers]
*   `@max_players@` - The current amount of players allowed in the given instance
    *   Uses the 5th return value from [GetInstanceInfo]
    *   Availability: 3.2.0 clients and above (Or rebased clients)
*   `@dynamic_difficulty@` - The dynamic difficulty level for an instance
    *   Uses the 6th return value from [GetInstanceInfo]
    *   Availability: 3.2.0 clients and above (Or rebased clients)
*   `@is_dynamic@` - Whether the instance difficulty can be changed while zoned in
    *   Uses the 7th return value from [GetInstanceInfo]
    *   Availability: 3.2.0 clients and above (Or rebased clients)
*   `@instance_id@` - The player's current in-game instance map id
    *   Uses the 8th return value from [GetInstanceInfo]
    *   Availability: 5.0.4 clients and above
*   `@instance_group_size@` - The number of players currently within your instance group
    *   Uses the 9th return value from [GetInstanceInfo]
    *   Availability: 5.4.0 clients and above
*   `@lfg_dungeon_id@` - The dungeon id for the player's current instance
    *   Uses the 10th return value from [GetInstanceInfo]
    *   Availability: 8.0.0 clients and above
*   `@zone_name@` - [The player's current in-game zone name][GetRealZoneText]
    *   Returns `Unknown` if in an indeterminable zone
*   `@sub_zone_name@` - [The player's current in-game sub-zone name][GetSubZoneText]
    *   Returns `Unknown` if in an indeterminable sub-zone
*   `@item_level@` - The player's current item level
    *   Uses the 1st return value from [GetAverageItemLevel]
    *   Returns `0` if the function is not available or result is null
*   `@item_level_equipped@` - The player's currently equipped item level
    *   Uses the 2nd return value from [GetAverageItemLevel]
    *   Returns `@item_level@` if the function is not available or result is null
*   `@item_level_pvp@` - The player's currently scaled item level, for pvp scenarios
    *   Uses the 3rd return value from [GetAverageItemLevel]
    *   Returns `@item_level@` if the function is not available or result is null
*   `@active_keystone_level@` - The player's currently active keystone level
    *   See [CraftPresence/InstanceInfo] for more info on retrieving this output
    *   Availability: 5.0.0 clients and above
*   `@active_keystone_affixes@` - The player's currently active keystone affix names
    *   See [CraftPresence/InstanceInfo] for more info on retrieving this output
    *   Availability: 5.0.0 clients and above
*   `@active_keystone_rating@` - The player's current Mythic Plus Rating
    *   See [CraftPresence/InstanceInfo] for more info on retrieving this output
    *   Availability: 5.0.0 clients and above
*   `@internal_keystone_rating@` - The primary placeholder for `@active_keystone_rating@`
    *   See [CraftPresence/InstanceInfo] for more info on retrieving this output
    *   The internal rating is from [C_ChallengeMode.GetOverallDungeonScore]
    *   If this function is not present, it will fallback to `0`
    *   Availability: 5.0.0 clients and above
*   `@external_keystone_rating@` - The fallback placeholder for `@active_keystone_rating@`
    *   See [CraftPresence/InstanceInfo] for more info on retrieving this output
    *   The external rating involves the usage of 3rd party addon api's, such as [RaiderIO]
    *   If this function is not present, it will fallback to `@internal_keystone_rating@`
    *   Availability: 5.0.0 clients and above
*   `@owned_keystone_name@` - The player's currently owned keystone name
    *   See [CraftPresence/InstanceInfo] for more info on retrieving this output
    *   Availability: 5.0.0 clients and above
*   `@owned_keystone_level@` - The player's currently owned keystone level
    *   See [CraftPresence/InstanceInfo] for more info on retrieving this output
    *   Availability: 5.0.0 clients and above
*   `@lockout_current_encounters@` - The player's current encounters/stages completed in an instance
    *   See [CraftPresence/InstanceInfo] for more info on retrieving this output
    *   Availability: 6.0.0 clients and above
*   `@lockout_total_encounters@` - The player's total encounters/stages within an instance
    *   See [CraftPresence/InstanceInfo] for more info on retrieving this output
    *   Availability: 6.0.0 clients and above

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

Additionally, you can also create your own Set of Icons by following [this guide](https://gitlab.com/CDAGaming/CraftPresence/-/wikis/Making-your-Own-Client-ID-+-Using-your-own-Images)

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

*   [Ace3](https://www.curseforge.com/wow/addons/ace3) by [Kaelten](https://www.curseforge.com/members/kaelten)
    *   [LibDBIcon-1.0](https://www.curseforge.com/wow/addons/libdbicon-1-0) by [funkehdude](https://www.curseforge.com/members/funkehdude)
    *   [LibDataBroker-1.1](https://www.curseforge.com/wow/addons/libdatabroker-1-1) by [tekkub](https://www.curseforge.com/members/tekkub)
    *   [CallbackHandler-1.0](https://www.curseforge.com/wow/addons/callbackhandler) by [nevcairiel](https://www.curseforge.com/members/nevcairiel)
    *   [LibStub](https://www.curseforge.com/wow/addons/libstub) by [Kaelten](https://www.curseforge.com/members/kaelten)

#### Discord Terms of Service
As with other RPC Mods, this Mod uses your in-game data to send display information to a 3rd party service (In this Case, Discord).

The terms of service relating to Creating a Discord ID for icons can be found [here](https://discord.com/developers/docs/legal)

The terms of service for using Discord as a service can additionally be located [here](https://discord.com/terms)

[UnitName]: https://warcraft.wiki.gg/wiki/API_UnitName
[UnitPVPName]: https://warcraft.wiki.gg/wiki/API_UnitPVPName
[UnitLevel]: https://warcraft.wiki.gg/wiki/API_UnitLevel
[GetRealmName]: https://warcraft.wiki.gg/wiki/API_GetRealmName
[GetCurrentRegion]: https://warcraft.wiki.gg/wiki/API_GetCurrentRegion
[UnitClass]: https://warcraft.wiki.gg/wiki/API_UnitClass
[UnitRace]: https://warcraft.wiki.gg/wiki/API_UnitRace
[UnitSex]: https://warcraft.wiki.gg/wiki/API_UnitSex
[UnitFactionGroup]: https://warcraft.wiki.gg/wiki/API_UnitFactionGroup
[C_Covenants.GetCovenantData]: https://warcraft.wiki.gg/wiki/API_C_Covenants.GetCovenantData
[GetSpecialization]: https://warcraft.wiki.gg/wiki/API_GetSpecialization
[GetSpecializationInfo]: https://warcraft.wiki.gg/wiki/API_GetSpecializationInfo
[GetSpecializationRoleByID]: https://warcraft.wiki.gg/wiki/API_GetSpecializationRoleByID
[C_CovenantSanctumUI.GetRenownLevel]: https://warcraft.wiki.gg/wiki/API_C_CovenantSanctumUI.GetRenownLevel
[GetInstanceInfo]: https://warcraft.wiki.gg/wiki/API_GetInstanceInfo
[GetNumGroupMembers]: https://warcraft.wiki.gg/wiki/API_GetNumGroupMembers
[GetNumPartyMembers]: https://warcraft.wiki.gg/wiki/API_GetNumPartyMembers
[GetNumRaidMembers]: https://warcraft.wiki.gg/wiki/API_GetNumRaidMembers
[GetRealZoneText]: https://warcraft.wiki.gg/wiki/API_GetRealZoneText
[GetSubZoneText]: https://warcraft.wiki.gg/wiki/API_GetSubZoneText
[GetAverageItemLevel]: https://warcraft.wiki.gg/wiki/API_GetAverageItemLevel
[C_ChallengeMode.GetOverallDungeonScore]: https://warcraft.wiki.gg/wiki/API_C_ChallengeMode.GetOverallDungeonScore

[CraftPresence/InstanceInfo]: https://gitlab.com/CDAGaming/CraftPresence-Wow-Edition/-/raw/master/InstanceInfo.lua

[RaiderIO]: https://raider.io/
