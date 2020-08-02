# CraftPresence Changes

## v1.6.7 (08/03/2020)

### Changes

*   Each release going forward will have a tag attached to it on the repository, going down to v1.4.0
*   Added a Fallback Switch for the config gui for if it is forced closed (Reference: [#69](https://gitlab.com/CDAGaming/CraftPresence/-/issues/69))
*   RPC Formatting will now only capitalize the first word in the parameter, giving more control to the separate placeholders
*   Minor Backend Updates involving Dependency and Logic Updates

### Fixes

*   Fixed a Bug causing Concurrent Modifications and Hard Crashes

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

*   Both `&mods&` and `&ign&` are now Generalized Placeholders and can only be called within the Presence Formatting GUI

    *   `&ign&` in Server Messages should be changed to using `&playerinfo&` for in-world Player Data and `&ign&` for out-of-world Player Data

*   `overwriteServerIcon` has been deprecated, instead use the Presence Formatting GUI to define the priority for icons

    *   Example: `overwriteServerIcon` = Setting `smallImageKey` to `&PACK&&SERVER&` (Having `&PACK&` be before anything else)

#### 1.13.x Build Info

The Rift Edition of this Mod Requires the [Rift ModLoader](https://www.curseforge.com/minecraft/mc-mods/rift) and contains the following differences to take Note of:

*   KeyCodes have changed from an LWJGL Upgrade! Be Sure to check and edit your KeyBinds if migrating from 1.12.2 and below.

Starting in v1.5.0, The 1.13 Rift Port of CraftPresence was deprecated in favor of the 1.13.2 Rift Port

#### 1.14.x - 1.16.x Build Info

The 1.14.x and 1.16.x Ports of this Mod Require the [FabricMC ModLoader](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and contains the same differences as the 1.13.x Port

#### Snapshot Build Info

Some Versions of this Mod are for Minecraft Snapshots, and as such, caution should be noted.

Any Snapshot Build Released will be marked as BETA to match its Snapshot Status depending on Tests done before release and issues found.

#### Legacy Build Info (Minecraft Versions 1.5.2 and Below)

Ports of this Mod for Minecraft Versions 1.5.2 and Lower are on very limited support.

Please Keep in Mind the Following:

*   There is NO Support for Server RPC Displays from MC 1.2.5 and Below, due to modding limitations.
*   Bugs that relate with or are caused by issues in the Vanilla Codebase, are unlikely able to be fixed due to MC's limitations

See the Mod Description // README for More Info
