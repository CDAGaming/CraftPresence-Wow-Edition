# CraftPresence Changes

## v1.6.0 (12/15/2019)

### Changes

*   Added Specific Presence Display Settings to ConfigGUI

    *   Now allows users to set where in the RPC they'd like each message

*   `/cp view currentData` will now show the Current User the RPC is attached to

*   Extended KeyBinding System to expand to future KeyBinds

*   Added a New Config Message in Logs for Initial Setups

*   Pressing the `Reset to Default` Button will no longer save automatically, to prevent accidental misuse

    *   The `Back` Button will turn to `Save` if it detects a queued reset or change

*   Added Placeholders to `&IGN&` within Server Settings:

    *   `&coords&` has been added to display your X and Z Axis Coordinates

*   The Mod will now use the Dimension ID, if any, if the Current Dimension Name returns null (Primarily a fallback)

*   Removed and Cleaned Unused Localization Strings

*   (Code-Behind) Change `isValidInteger` and `isValidLong` to Getters powered by Tuple's for both getting a boolean and the number itself

*   Compatibility Changes:

    *   Now Compiled under Gradle 5.x on 1.8.9 and Above

        *   1.7.10 and 1.6.4 utilize Gradle 2.14.1

    *   Now Targets Java 8 and uses Java 8 Functions on 1.12.2 and Above

### Fixes

*   Changed PG Left and Right KeyBind Triggers to `KEY_UP` and `KEY_DOWN` to resolve a conflict of changing pages while editing something
*   Fixed Config Conflicts with a more dynamic system
*   Fixes a Bug where the ConfigGUI KeyBind could still reset to Left Control (IE the old KeyBind) if a Keycode Error occurs
*   Fixed Icon Support for MCUpdater (Will now use the Pack Name like Curse/Twitch)
*   Code Compliance Adjustments as well as multiple refactors
*   Corrected Credits Message for ItsDizzy
*   Code Warning Fixes and Cleanups
*   Fixes an NPE that may occur when on the Texture Input GUI
*   Fixes a Race Case Issue preventing Inputting RGBA Values for color customization UIs
*   Fixed an Unintentional Warning from a false positive Fingerprint Violation
*   Minor Performance Improvements

### More Information

#### v1.6.0 Mod Rewrite Info

In v1.6.0, CraftPresence has undergone numerous performance and under-the-hood improvements, to both acknowledge user feedback over the last several months and pursue the goal of further modularity.
As Elements such as the Config System and General RPC Elements have undergone various changes, please report any bugs found to [the Issue Tracker](https://gitlab.com/CDAGaming/CraftPresence/issues)

v1.5.x is Generally Recommended at this time for new users, has the systems used in v1.6.0 may have a variety of issues, and considered bleeding-edge

Migration Notes:

*   `&mods&` is now a Generalized Placeholder and can only be called within the Presence Formatting GUI

*   `overwriteServerIcon` has been deprecated, instead use the Presence Formatting GUI to define the priority for icons

    *   Example: `overwriteServerIcon` = Setting `smallImageKey` to `&PACK&&SERVER&` (Having `&PACK&` be before anything else)

#### 1.13.x Build Info

The Rift Edition of this Mod Requires the [Rift ModLoader](https://www.curseforge.com/minecraft/mc-mods/rift) and contains the following differences to take Note of:

*   KeyCodes have changed from an LWJGL Upgrade! Be Sure to Check and Edit your KeyBinds if Migrating from 1.12.2 and Below

Starting In v1.5.0, The 1.13 Rift Port of CraftPresence was deprecated in favor of the 1.13.2 Rift Port

#### 1.14.x Build Info

The 1.14.x Port of this Mod Requires the [FabricMC ModLoader](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and contains the same differences as the 1.13.x Port

#### Snapshot Build Info

Some Versions of this Mod are based on Minecraft Snapshots, and as such, caution is advised.

Any Snapshot Build Released will be marked as BETA to match it's Snapshot Status

#### Legacy Build Info (Minecraft Versions 1.5.2 and Below)

Ports of this Mod for Minecraft Versions 1.5.2 and Lower are on very limited support.

Please Keep in Mind the Following:

*   There is NO Support for Server RPC Displays from MC 1.2.5 and Below, due to Modding Limitations.
*   Bugs that Relate with or are caused by Issues in the Vanilla CodeBase, are unlikely able to be fixed due to MC's Limitations

Please See the Mod Description // README for More Info
