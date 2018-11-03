# CraftPresence Changes

## v1.4.9 (11/09/2018)

### Changes:

* Added New Config Option:

  * `lanMSG` - Available in Status Messages to Set a Message to display while in a LAN Game

  * `enableJoinRequest` - Available in General Config GUI and allows Enabling or Disabling Discord Join Requests

* Added Ask To Join and Join Request Support

* Added LAN Game Support

* Added Some Additional Translations, mostly those missed in v1.4.8

### Fixes:

* Fixes some Minor NPE Scenarios

* Fixes a Crash when Joining a LAN Server [Issue Reference](https://gitlab.com/CDAGaming/CraftPresence/issues/8)

* Fixes Issues with Fallback Icon Setting

* Fixes Rapid RPC Update Scenarios

* Fixes RPC Tick Issues

* Fixes Some CommandSender Reference Issues

### More Information:

#### Config Overhaul (Breaking Change with v2 Configs):

The Config GUI has now been overhauled to provide a more User-Friendly Experience

* Migrating on `1.7.10 - 1.12.2` (From v1.3.x and Below) - Remove `craftpresence.cfg` and either Regenerate or Manually Migrate

* Migrating on `1.13.x` (From v1.3.x and Below) - Rename `craftpresence.cfg` to `craftpresence.properties` or Regenerate Config

Over time, this GUI will be edited and change and improve to further customize your experience.

#### Ask to Join // Join Request Info:

In v1.4.9, Join Request and "Ask to Join" are now supported!

These Features are both in-development, so although most Issues have been fixed, you may/may not run into major issues.

If You happen to have a major Issue occur relating to this, Do not hesitate to make an Issue on our Issue Tracker!

#### 1.13 Build Info:

The 1.13 Port of this Mod Requires the [Rift ModLoader](https://minecraft.curseforge.com/projects/rift) and contains the following differences to take Note of:

* KeyCodes have changed! Be Sure to Check and Edit your KeyBinds if Migrating from 1.12.2 and Below

* Commands have been changed slightly to be compliant with [Brigadier's Command Parser](https://github.com/Mojang/brigadier) in 1.13