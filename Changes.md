# CraftPresence Changes

## v1.7.0 (09/15/2020)

_A Detailed Changelog from the last Release is available [here](https://gitlab.com/CDAGaming/CraftPresence/-/compare/release%2Fv1.6.8...release%2Fv1.7.0)_

### Changes

*   Amended Releases to now always have `-%ModLoaderName%` as the suffix (Previously, Forge releases didn't have this suffix)
*   Added the `&yPosition&` Sub-Placeholder to the `&coords&` Placeholder
*   Added a Fallback `&PACK&` Placeholder Message in `Status Messages` (Will only be used if no pack gets found, and is not empty)
*   Hardcore Mode now supported in `&difficulty&` Sub-Placeholder
*   Added Comments to the Config File, showing a title and more clearly showing version and modified metadata
*   Added a Conversion Layer for converting KeyCodes between LWJGL2 (1.12.2 and below) and LWJGL3 (1.13 and above)
*   Added a Conversion Layer for converting Language IDs between Pack Format 1-2 (1.10.2 and below) and Pack Format 3 and Up (1.11 and above)
*   Hovering over the Config Gui Main Screen Title, now shows a tooltip with the current mod version
*   Separated `IS_DEV` into two options, to allow for toggling excessive logging and stacktrace outputs (Verbose Logging)
*   Added Support for the `minecraftinstance.json` file in Twitch/Curse Packs (IE Custom Twitch/Curse Packs now work)
*   Added Support for Live Editing (Clicking the `Sync Config` button will sync any changes made to `craftpresence.properties` to the game without needing to restart minecraft)
*   Changed `&GUI&` to `&SCREEN&` for clarification; removed old `&SCREEN&` placeholder
*   Removed the placeholder limits in Image Keys in `Presence Settings`
*   Localization, Documentation, and Backend Updates

### Fixes

*   Fixed filename issues that corresponded with increased user-base confusion
*   Fixed instances of the Mod Id not being the same across mod loaders
*   Fixed an issue causing KeyCode Properties to be able to skip verification due to improper identification
*   Fixed an issue where the `Config Gui` KeyBind was not properly identified as an Integer
*   Fixed an issue causing some LWJGL 3 KeyCodes to not be recognized
*   Fixed an issue causing `Format Exceptions` on some translations
*   Fixed an issue causing the `Load State` to be infinitely displayed, if you modified your config whilst in-game
*   Fixed an issue that caused some pack icon keys to be parsed incorrectly
*   Fixed an issue causing properties with an empty default value to become falsely interpreted to be reset if still empty
*   Fixed an issue with some translations being missing
*   Fixed an issue where some disabled options were reporting the wrong options to be enabled

___

### More Information

#### v1.7.0 Upgrade Info

v1.7.0 of CraftPresence is the next major feature and technical update after v1.6.0.
It is celebrating the recent milestones of over **five million downloads** as well as hitting the number one spot in Twitch Integration.

This release holds numerous improvements over the Configuration System, as well as adding long-standing user requests and planned features that I've queued over time since v1.4.0s release,
that are outlined in the changelog above.

While no Config Migrations are necessary at this time for updating to v1.7.0, this can change as time goes on in the v1.7.x Pipeline, and will be noted here as such when and if these types of changes occur.

More features will additionally be planned and added for later in the v1.7.x Pipeline as further releases arrive.

#### 1.13.x Build Info

The Rift Edition of this Mod Requires the [Rift ModLoader](https://www.curseforge.com/minecraft/mc-mods/rift) and contains the following differences to take Note of:

*   KeyCodes have changed from an LWJGL Upgrade! Be Sure to check and edit your KeyBinds if migrating from 1.12.2 and below.

Starting in v1.5.0, The 1.13 Rift Port of CraftPresence was deprecated in favor of the 1.13.2 Rift Port.

Starting in v1.7.0, The aforementioned KeyCode warning is now void, due to new systems introduced to convert keybindings between LWJGL versions, and this message will be removed in v1.7.1.

Note: Due to Maven Troubles on behalf of the Rift Team, Rift Versions are no longer supported as of v1.6.1, though the Differences do still take effect for Forge.

#### 1.14.x - 1.16.x Build Info

Some 1.14.x, 1.15.x, and 1.16.x Ports of this Mod Require the [FabricMC ModLoader](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and contains the same differences as the 1.13.x Port.

#### Snapshot Build Info

Some Versions of this Mod are for Minecraft Snapshots or Experimental Versions, and as such, caution should be noted.

Any Snapshot Build Released will be marked as BETA to match its Snapshot Status depending on Tests done before release and issues found.

Snapshot Builds, depending on circumstances, may also contain changes for a future version of the mod, and will be noted as so if this is the case.

#### Legacy Build Info (Minecraft Versions 1.5.2 and Below)

Ports of this Mod for Minecraft Versions 1.5.2 and Lower are on very limited support.

Please Keep in Mind the Following:

*   There is NO Support for Server RPC Displays from MC 1.2.5 and Below, due to modding limitations.
*   Bugs that relate with or are caused by issues in the Vanilla Codebase, are unlikely able to be fixed due to MC's limitations

See the Mod Description // README for More Info
