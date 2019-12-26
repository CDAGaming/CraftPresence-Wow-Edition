# CraftPresence Changes

## v1.6.1 (1/??/2020)

### Changes

*   All Builds, except for those with META-INF Errors, are now Signed

    *   Before, this only applied to 1.8.9 and above

*   Added a Parser for Update Data, so any Updates will display in the Gui, regardless if using Forge

### Fixes

*   Fixed Issues pertaining to Non-Functional RPC Displays on Unix Systems
*   Fixed Default Tooltip Color Values so just visiting the Gui doesn't trigger a hasChanged State
*   Fixed Some Versions of Minecraft having incorrect Dimension Names (Below 1.5.2)
*   Fixed Slider and Escape Button Functionality on MC 1.14+ Ports
*   Fixed an Incorrect GUI Background Texture Name on MC 1.5.2- Ports

### More Information

#### v1.6.0 Mod Rewrite Info

In v1.6.0, CraftPresence has undergone numerous performance and under-the-hood improvements, to both acknowledge user feedback over the last several months and pursue the goal of further modularity.

As Elements such as the Config System and General RPC Elements have undergone various changes, please report any bugs found to [the Issue Tracker](https://gitlab.com/CDAGaming/CraftPresence/issues)

v1.5.x is Generally Recommended at this time for new users, as the systems used in v1.6.0 may have a variety of issues, and are considered bleeding-edge for the time being

Migration Notes:

*   The RPC System has changed to an internal IPC System hooking into Discord's official endpoints

    *   As a result, you can remove any discord-rpc DLL files present in CraftPresence's folders as they are no longer being used

*   `gameTimePlaceholder` is now combined into the `&worldinfo&` Placeholder

*   Some of the other placeholders within Server and Status Messages have been changed or renamed, please refer to the in-game tooltips for these new names

*   Entity Messages now use the Default Message as the Format, while specific Item Messages are considered placeholders for the formatter

    *   This means, only `&item&` (The Specific Item's Name) will work for specific Items
    *   Otherwise, the more familiar placeholders such as `&main&` and `&offhand&` will retrieve these message when part of the default format setting

*   `&mods&` and `&ign&` are now Generalized Placeholders and can only be called within the Presence Formatting GUI

    *   `&ign&` in Server Messages should be changed to using `&playerinfo&` for in-world Player Data and `&ign&` for out-of-world Player Data

*   `overwriteServerIcon` has been deprecated, instead use the Presence Formatting GUI to define the priority for icons

    *   Example: `overwriteServerIcon` = Setting `smallImageKey` to `&PACK&&SERVER&` (Having `&PACK&` be before anything else)

#### 1.13.x Build Info

The Rift Edition of this Mod Requires the [Rift ModLoader](https://www.curseforge.com/minecraft/mc-mods/rift) and contains the following differences to take Note of:

*   KeyCodes have changed from an LWJGL Upgrade! Be Sure to Check and Edit your KeyBinds if Migrating from 1.12.2 and Below

Starting In v1.5.0, The 1.13 Rift Port of CraftPresence was deprecated in favor of the 1.13.2 Rift Port

#### 1.14.x - 1.15.x Build Info

The 1.14.x and 1.15.x Ports of this Mod Require the [FabricMC ModLoader](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and contains the same differences as the 1.13.x Port

#### Snapshot Build Info

Some Versions of this Mod are based on Minecraft Snapshots, and as such, caution is advised.

Any Snapshot Build Released will be marked as BETA to match it's Snapshot Status

#### Legacy Build Info (Minecraft Versions 1.5.2 and Below)

Ports of this Mod for Minecraft Versions 1.5.2 and Lower are on very limited support.

Please Keep in Mind the Following:

*   There is NO Support for Server RPC Displays from MC 1.2.5 and Below, due to Modding Limitations.
*   Bugs that Relate with or are caused by Issues in the Vanilla CodeBase, are unlikely able to be fixed due to MC's Limitations

Please See the Mod Description // README for More Info
