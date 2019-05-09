# CraftPresence Changes

## v1.5.5 (06/05/2019)

### Changes:

* Added Options to Accessibility GUI:
  
  * Option to Customize GUI Background to a Color or Texture
  
  * Extended Previous Tooltip Color Options to also be able to use a Texture
  
  * Added Options for Language ID and Setting KeyBindings for Universal Compatibility (Custom Rift Jar no longer Required for KeyBind Fix)

* Added Options to Advanced GUI:

  * Option to Disable Rendering Tooltips

  * Options to Edit or Re-Sync Character and Glyph Widths
  
* Deprecated I18n, to put Newer Translation System in Full Control

* Added Additional Formatting Rules to Dimension Name Formatting

### Fixes:

* Initialization now takes place much earlier and no longer requires Main Menu Stuff

* Fixes Improper Centering of Buttons in Advanced Settings GUI

* Fixes Several Possible Cases of NPEs

* Fixes KeyBindings not functioning when not in a Game (Now work properly once on Main Menu)

* Fixes a Bug with Translation Resource Loading (Now tries to use Main Thread Loader, then local ClassLoader)

### More Information:

#### Command Rewrite Info:

Starting In v1.5.0, Commands for CraftPresence are now within a new menu of the Config Gui.

Along with this rewrite, take note of the following:

* Tab Completion will only work for the most likely suggestion, and will only suggest for more then 2 characters in a word.

* Only CraftPresence Commands, started with /cp or /craftpresence are Supported!

* At this time, in 1.13.2 and 1.14, The Config GUI can only open in-game due to an unknown issue.

This GUI is subject to improve as future updates are released.

#### 1.13.x Build Info:

The Rift Edition of this Mod Requires the [Rift ModLoader](https://minecraft.curseforge.com/projects/rift) and contains the following differences to take Note of:

* KeyCodes have changed from an LWJGL Upgrade! Be Sure to Check and Edit your KeyBinds if Migrating from 1.12.2 and Below

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

Please See Mod Description // README for More Info
