# CraftPresence Changes

## v1.4.9.8 (12/12/2018)

### Changes:

* Code ReFactors and Optimizations

### Fixes:

* [1.13.x] Fixes Scroll Lists Clicking through background

* Fixes Missing " - " Typo in Biome Hover Text

* Fixes IndexOutOfBounds Exception on getRandomAsset

* [HOTFIX] Fixes Linux and MacOS DLL Downloading from Original Build [(Issue Reference)](https://gitlab.com/CDAGaming/CraftPresence/issues/10)
### More Information:

#### Config Overhaul (Breaking Change with v2 Configs):

The Config GUI has now been overhauled to provide a more User-Friendly Experience

* Migrating from `1.7.10 - 1.12.2` (On v1.3.x and Below) - Remove `craftpresence.cfg` and either Regenerate or Manually Migrate

* Migrating from `1.13.x` (On v1.3.x and Below) - Rename `craftpresence.cfg` to `craftpresence.properties` or Regenerate Config

Over time, this GUI will be edited and change and improve to further customize your experience.

#### Ask to Join // Join Request Info:

In v1.4.9, Join Request and "Ask to Join" are now supported!

These Features are both extremely new, so although most Issues have been fixed, you may/may not run into major issues.

If You happen to have a major Issue occur relating to this, Do not hesitate to make an Issue on our Issue Tracker!

#### 1.13.x Build Info:

The 1.13.x Port of this Mod Requires the [Rift ModLoader](https://minecraft.curseforge.com/projects/rift) and contains the following differences to take Note of:

* KeyCodes have changed! Be Sure to Check and Edit your KeyBinds if Migrating from 1.12.2 and Below

* Commands have been changed slightly to be compliant with [Brigadier's Command Parser](https://github.com/Mojang/brigadier) in 1.13