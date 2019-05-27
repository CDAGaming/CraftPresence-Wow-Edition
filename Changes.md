# CraftPresence Changes

## v1.5.5 (06/07/2019)

### Changes:

* Added 1.14.2 Fabric Support (1.14 and 1.14.1 Versions are now Deprecated)

* Commands for viewing Assets, Servers, Dimensions, GUIs, and Biomes have been changed to open their Lists in a Selector GUI

* Added Options to Accessibility GUI:
  
  * Option to Customize GUI Background to a Color or Texture
  
  * Extended Previous Tooltip Color Options to also be able to use a Texture
  
  * Added Options for Language ID and Setting KeyBindings for Universal Compatibility (Custom Rift Jar no longer Required for KeyBind Fix)

* Added Options to Advanced GUI:

  * Option to Disable Rendering Tooltips

  * Options to Edit or Re-Sync Character and Glyph Widths
  
* Added an Option to Reset your Config to Default Settings

* Fully Deprecated I18n, to put Newer Translation System in Full Control

* Added Boolean to TranslationHandler to control whether Color Codes are Included in Translated Text

* Added Additional Formatting Rules to Dimension Name Formatting

* Removed some Sections of Live Editing due to Issues caused with some Config Options requiring Saving

* Changing your Split Character will now transfer your other settings to the New Character on Verification

### Fixes:

* Fixes Game Crashing from Null List in Selector (Will now bring you immediately back to original Screen)

* Fixes Issues with Config Verification failing to Re-Run if it needs a full update

* Fixed Double Logging in ConfigHandler (Now has a parameter to Skip Logging at the End)

* Initialization now takes place much earlier and no longer requires Main Menu Stuff

* Fixes Improper Centering of Buttons in Advanced Settings GUI

* Fixes Several Possible Cases of NPEs

* Fixes an NPE within GUIs if Config is null

* Fixes KeyBindings not functioning when not in a Game (Now work properly once on Main Menu)

* Fixes a Bug with Translation Resource Loading (Now tries to use Main Thread Loader, then local ClassLoader)

* Fixes an Exception with Missing Format Arguments in Translation Handler

* Fixes Some Selector Titles Not being Localized Properly

* Fixes an Extremely Rare Bug with Small Icon List coming up for `/cp view assets large`

* Fixes some Encoding Bugs causing Translations or JSON Retrievals to have garbled data

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

A 1.14.x Snapshot Build will be marked as BETA to match it's Snapshot Status

#### Legacy Build Info (Minecraft Versions 1.5.2 and Below):

Ports of this Mod for Minecraft Versions 1.5.2 and Lower are on very limited support.

Please Keep in Mind the Following:

* There is NO Support for Server RPC Displays from MC 1.2.5 and Below, due to Modding Limitations.

* Bugs that Relate with or are caused by Issues in the Vanilla CodeBase, are unlikely able to be fixed due to MC's Limitations

Please See the Mod Description // README for More Info
