# CraftPresence Changes

## v1.5.3.1 (04/15/2019)

### Changes:

* New Accessibility GUI with Options for:
  
  * Options to Customize Tooltip Background and Border Colors (Supports Most Color Formats)
  
  * Soon to have Options for Fallback Language and KeyBindings in a Future Update

### Fixes:

* Fix CPU Issues related to Timer and RPC Callbacks

* Fix Compatibility with Customizing Servers with Ports

* Fix Cases where ServerHandler Events would not run if a Connection was Null

* Fix The End displaying as sky in some Versions of CraftPresence

* Miscellaneous Compatibility Fixes

### More Information:

#### KeyBinding / Control Menu Fix Notes:

The Fixes for the Control Menu Crashes in Rift 1.13.2 required the merging of [this PR on Chocohead's Rift Repository](https://github.com/Chocohead/Rift/pull/11)

If you prefer to have this fix, Please use [this Rift Version](https://www.jitpack.io/#CDAGaming/Rift/jitpack-0a2217b941-1)

Download Jar: [Click Here](https://www.jitpack.io/com/github/CDAGaming/Rift/jitpack-0a2217b941-1/Rift-jitpack-0a2217b941-1.jar)

MultiMC Example Patch: [Click Here](https://gist.github.com/CDAGaming/ba84849826e96b69b829b7453e459edf)

#### Command Rewrite Info:

Starting In v1.5.0, Commands for CraftPresence are now within a new menu of the Config Gui.

Along with this rewrite, take note of the following:

* Tab Completion will only work for the most likely suggestion, and will only suggest for more then 2 characters in a word.

* Only CraftPresence Commands, started with /cp or /craftpresence are Supported!

* At this time, in 1.13.2 and 1.14, The Config GUI can only open in-game due to an unknown issue.

This GUI is subject to improve as future updates are released.

#### Ask to Join // Join Request Info:

Starting In v1.4.9, Join Request and "Ask to Join" are now supported!

These Features are both extremely new, so although most Issues have been fixed, you may/may not run into major issues.

If You happen to have any issues relating to this, do not hesitate to make an Issue on our Issue Tracker!

#### 1.13.x Build Info:

The Rift Edition of this Mod Requires the [Rift ModLoader](https://minecraft.curseforge.com/projects/rift) and contains the following differences to take Note of:

* KeyCodes have changed! Be Sure to Check and Edit your KeyBinds if Migrating from 1.12.2 and Below

Starting In v1.5.0, The 1.13 Rift Port of CraftPresence was deprecated in favor of the 1.13.2 Rift Port

#### 1.14.x Build Info:

The 1.14.x Port of this Mod Requires the [FabricMC ModLoader](https://minecraft.curseforge.com/projects/fabric) and contains the same differences as the 1.13.x Port

This Version of the Build is based on Minecraft Snapshots, since 1.14 is not fully released, caution is advised.

A 1.14.x Snapshot Build will expire upon the release of a succeeding build. (Ex: If a 19w04b build is Released, then the 19w04a build is removed)

#### Legacy Build Info (Minecraft Versions 1.5.2 and Below):

Ports of this Mod for Minecraft Versions 1.5.2 and Lower are on very limited support.

Please Keep in Mind the Following:

* There is NO Support for Server RPC Displays from MC 1.2.5 and Below, due to Modding Limitations.

* Bugs that Relate with Issues in Vanilla Code, are unlikely able to be fixed due to MC's Limitations

* In the Legacy Builds, a different Translation Engine is being used instead of I18n. Please Report any Issues that come up as a result of this Change.

Please See Mod Description // README for More Info
