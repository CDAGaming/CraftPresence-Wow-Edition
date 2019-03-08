# CraftPresence Changes

## v1.5.1 (03/??/2019)

### Changes:

* Added Translations for "Pinging..." and "Cannot connect" that are missing in some versions of MC

* Added Translation for if a File Fails to be deleted in downloading or updating Discord DLLs

### Fixes:

* Fixes Improper "/cp view assets all" Command Output

* Removed Extra Debug Logging when Using Tab Completion in CommandsGUI

* Fixed Tab Completion not Functioning in 1.13.2 and 1.14

* Fixed missing Null-Checks for the Notices in the About and NullEntry GUI, to prevent issues

* Fix onJoinRequest Callback not opening the Commands GUI

### More Information:

#### Command Rewrite Info:

In v1.5.0, Commands for CraftPresence are now within a new menu of the config gui.

Along with this rewrite, take note of the following:

* Tab Completion will only work for the most likely suggestion, and will only suggest for more then 2 characters in a word.

* Only CraftPresence Commands, started with /cp or /craftpresence are Supported!

* At this time, in 1.13.2 and 1.14, The Config GUI can only open ingame due to an unknown issue

This GUI is subject to improve as future updates are released.

#### Ask to Join // Join Request Info:

In v1.4.9, Join Request and "Ask to Join" are now supported!

These Features are both extremely new, so although most Issues have been fixed, you may/may not run into major issues.

If You happen to have any issues relating to this, do not hesitate to make an Issue on our Issue Tracker!

#### 1.13.x Build Info:

The Rift Edition of this Mod Requires the [Rift ModLoader](https://minecraft.curseforge.com/projects/rift) and contains the following differences to take Note of:

* KeyCodes have changed! Be Sure to Check and Edit your KeyBinds if Migrating from 1.12.2 and Below

#### 1.14.x Build Info:

The 1.14.x Port of this Mod Requires the [FabricMC ModLoader](https://minecraft.curseforge.com/projects/fabric) and contains the same differences as the 1.13.x Port

This Version of the Build is based on Minecraft Snapshots, since 1.14 is not fully released, caution is advised.

A 1.14.x Snapshot Build will expire upon the release of a succeeding build. (Ex: If a 19w04b build is Released, then the 19w04a build is removed)
