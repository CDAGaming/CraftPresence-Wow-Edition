# CraftPresence Changes

## v1.5.0 (03/08/2019)

### Changes:

* MCUpdater Instance Detection & Support [(PR Reference)](https://gitlab.com/CDAGaming/CraftPresence/merge_requests/1)

* Added Forge 1.13.2 Support

* Added New Commands GUI with Rewritten Command Interface

* Allow Dashes to be used for Pack Icons (No Longer Replaced by Underscores) [(Issue Reference)](https://gitlab.com/CDAGaming/CraftPresence/issues/15)

* Config GUI can now be opened from anywhere in Minecraft, not just within a world! (Default: Right CTRL)

* Enable Join Request is now Disabled by Default (Won't effect Current Configs)

### Fixes:

* [1.13.x - Forge] Fixes for KeyBindings // Control Menu Crashes

* Fixes OnHover Double Spaces in Messages sent to Discord RPC

* Fixes a possible IndexOutOfBounds Crash by blocking Mouse Buttons from being used as Keybinds to open the Config GUI

### More Information:

#### Command Rewrite Info:

In v1.5.0, Commands for CraftPresence are now within a new menu of the config gui.

Along with this rewrite, take note of the following:

* Tab Completion will only work for the most likely suggestion, and will only suggest for more then 2 characters in a word.

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
