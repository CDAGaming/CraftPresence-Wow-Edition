# CraftPresence Changes

## v1.6.5 (6/26/2020)

### Changes

*   Source upgrades for ForgeGradle 3.x and Gradle on 1.12.2 and above
*   Added a `Format Words` option in Advanced Settings, used to toggle automatic capitalization in RPC Strings
*   Added `Entity Target`, `Entity Attacking` and `Entity Riding` Options in Advanced Settings to begin the Per-Entity System
*   Additionally, added NBT Tag Support for Per-Item System
*   Debug Mode now supports Placeholder Value previews in some circumstances
*   Changed `&ENTITY&` placeholder belonging to Items and Blocks to `&TILEENTITY&`
*   Backend Upgrades and Rewrites to Gui Screens and related Utilities
*   Localization Updates to Update Checker
*   Returning from Popular Demand, Added Back the `Loading Message` to Status Messages
*   Added `Refresh Rate` Option to Advanced Settings to control the rate modules and RPC is refreshed
*   1.13.2 Rift Port now Temporarily Deprecated due to lack of support and Maven Downtime
*   Adjusted Translations to be more concise and in-line with namespaced id requirements
*   Minor cleanups and backend improvements

### Fixes

*   Fixed Dynamic Editor Screen to properly display the placeholders when hovering over message label
*   Fixed missing Backend Gradle functions on ForgeGradle 3.x Ports
*   Fixed Improper Version Label on 1.13+ Ports
*   Fixed Focus Gui Issues again on 1.13+ Ports
*   Fixed Rare Instances of Button Actions not functioning on 1.13+ Ports
*   Fixed Possible Gui Rescaling Issues
*   Fixed `&difficulty&` placeholder interpretation
*   Fixed a NPE that occurred without a Discord Avatar
*   Fixed Possible Precision Inconsistencies related to Coordinate Information
*   Fixed a possible 403 HTTP Error in receiving Update Information

### More Information

#### v1.6.0 Mod Rewrite Info

In v1.6.0, CraftPresence has undergone numerous performance and under-the-hood improvements, to both acknowledge user feedback over the last several months and pursue the goal of further modularity.

As Elements such as the Config System and General RPC Elements have undergone various changes, please report any bugs found to [the Issue Tracker](https://gitlab.com/CDAGaming/CraftPresence/issues)

Migration Notes:

*   The RPC System has changed to an internal IPC System hooking into Discord's official endpoints

    *   As a result, you can remove any discord-rpc DLL files present in CraftPresence's folders as they are no longer being used

*   `gameTimePlaceholder` is now combined into the `&worldinfo&` Placeholder

*   Some other placeholders within Server and Status Messages have been changed or renamed, please refer to the in-game tooltips for these new names

*   Entity Messages now use the Default Message as the Format, while specific Item Messages are placeholders for the formatted string

    *   This means, only `&item&` (The Specific Item's Name) will work for specific Items
    *   Otherwise, the more familiar placeholders such as `&main&` and `&offhand&` will retrieve this message when part of the default format setting

*   `&mods&` and `&ign&` are now Generalized Placeholders and can only be called within the Presence Formatting GUI

    *   `&ign&` in Server Messages should be changed to using `&playerinfo&` for in-world Player Data and `&ign&` for out-of-world Player Data

*   `overwriteServerIcon` has been deprecated, instead use the Presence Formatting GUI to define the priority for icons

    *   Example: `overwriteServerIcon` = Setting `smallImageKey` to `&PACK&&SERVER&` (Having `&PACK&` be before anything else)

#### 1.13.x Build Info

The Rift Edition of this Mod Requires the [Rift ModLoader](https://www.curseforge.com/minecraft/mc-mods/rift) and contains the following differences to take Note of:

*   KeyCodes have changed from an LWJGL Upgrade! Be Sure to check and edit your KeyBinds if migrating from 1.12.2 and below.

Starting in v1.5.0, The 1.13 Rift Port of CraftPresence was deprecated in favor of the 1.13.2 Rift Port

#### 1.14.x - 1.15.x Build Info

The 1.14.x and 1.15.x Ports of this Mod Require the [FabricMC ModLoader](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and contains the same differences as the 1.13.x Port

#### Snapshot Build Info

Some Versions of this Mod are based on Minecraft Snapshots, and as such, caution is advised.

Any Snapshot Build Released will be marked as BETA to match its Snapshot Status

#### Legacy Build Info (Minecraft Versions 1.5.2 and Below)

Ports of this Mod for Minecraft Versions 1.5.2 and Lower are on very limited support.

Please Keep in Mind the Following:

*   There is NO Support for Server RPC Displays from MC 1.2.5 and Below, due to modding limitations.
*   Bugs that relate with or are caused by issues in the Vanilla Codebase, are unlikely able to be fixed due to MC's limitations

See the Mod Description // README for More Info
