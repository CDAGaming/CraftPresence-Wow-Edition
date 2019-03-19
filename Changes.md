# CraftPresence Changes

## v1.5.2 (03/29/2019)

### Changes:

* Added Translations for "Pinging...", "Polling..." and "Cannot connect" that are missing in some versions of MC

* Added Translation for if a File Fails to be deleted in downloading or updating Discord DLLs

* Refactored Translation System (Added a Fallback for if I18n Fails, and for Older Versions of Minecraft)

* Java/JRE 1.6 Compatibility

* Deprecated Support of Loading Messages due to Changes in 1.13 and above (Mod now Initializes once on Main Menu and after the Loading Splash Screen)

### Fixes:

* Fixes a Major Crash that occurred if All Config Options are not Present (Read Error on Empty Config File)

* Fixes Improper "/cp view assets all" Command Output

* Removed Extra Debug Logging when Using Tab Completion in CommandsGUI

* Fixed Tab Completion not Functioning in 1.13.2 and 1.14

* Fixed missing Null-Checks for the Notices in the About and NullEntry GUI, to prevent issues

* Fix Default Server Name listing as selectServer.defaultName

* Fix onJoinRequest Callback not opening the Commands GUI

* Disabling Commands no longer makes the Config GUI Inaccessible

* Fix Missing Scroll List Methods in 1.13.2 Rift and 1.14 Fabric

* Fix Possible NPE from RPC Updates before DLL was Loaded

* Fix Control Menu and KeyBinding Issues in 1.13.2 Rift and 1.14 Fabric Ports (See More Info)

### More Information:

#### Keybinding / Control Menu Fix Notes:

The Fixes for the Control Menu Crashes in Rift 1.13.2 required the merging of [this PR on Chocohead's Rift Repository](https://github.com/Chocohead/Rift/pull/11)

If you prefer to have this fix, Please use [this Rift Version](https://www.jitpack.io/#CDAGaming/Rift/jitpack-0a2217b941-1)

Jar: [Click Here](https://www.jitpack.io/com/github/CDAGaming/Rift/jitpack-0a2217b941-1/Rift-jitpack-0a2217b941-1.jar)

MultiMC Example Patch: [Click Here](https://gist.github.com/CDAGaming/ba84849826e96b69b829b7453e459edf)

#### Command Rewrite Info:

In v1.5.0, Commands for CraftPresence are now within a new menu of the config gui.

Along with this rewrite, take note of the following:

* Tab Completion will only work for the most likely suggestion, and will only suggest for more then 2 characters in a word.

* Only CraftPresence Commands, started with /cp or /craftpresence are Supported!

* At this time, in 1.13.2 and 1.14, The Config GUI can only open ingame due to an unknown issue.

This GUI is subject to improve as future updates are released.

#### Ask to Join // Join Request Info:

In v1.4.9, Join Request and "Ask to Join" are now supported!

These Features are both extremely new, so although most Issues have been fixed, you may/may not run into major issues.

If You happen to have any issues relating to this, do not hesitate to make an Issue on our Issue Tracker!

#### 1.13.x Build Info:

The Rift Edition of this Mod Requires the [Rift ModLoader](https://minecraft.curseforge.com/projects/rift) and contains the following differences to take Note of:

* KeyCodes have changed! Be Sure to Check and Edit your KeyBinds if Migrating from 1.12.2 and Below

In v1.5.0, The 1.13 Rift Port of CraftPresence was deprecated in favor of the 1.13.2 Rift Port

#### 1.14.x Build Info:

The 1.14.x Port of this Mod Requires the [FabricMC ModLoader](https://minecraft.curseforge.com/projects/fabric) and contains the same differences as the 1.13.x Port

This Version of the Build is based on Minecraft Snapshots, since 1.14 is not fully released, caution is advised.

A 1.14.x Snapshot Build will expire upon the release of a succeeding build. (Ex: If a 19w04b build is Released, then the 19w04a build is removed)

#### Legacy Build Info (Minecraft Versions 1.5.2 and Below):

Ports of this Mod for Minecraft Versions 1.5.2 and Lower are on very limited support.

Please Keep in Mind the Following:

* Ensure that you also Download and Have the Required Libraries (Posted in Additional Files Section on CurseForge)

* Store the Required Libraries in your .minecraft/bin or .minecraft/libraries Folder, depending on how your .minecraft folder is setup

* In the Legacy Builds, a different Translation Engine is being used instead of I18n. Please Report any Issues that come up as a result of this Change
