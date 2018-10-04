# CraftPresence Changes

## v1.4.8 (10/??/2018)

### Changes:

* Added `&helmet&`, `&chest&`, `&legs&`, and `&boots&` in Item Messages for Equipped Armor

* Added an `overwriteServerIcon` Config Option to allow a Pack Icon to take Priority over a Server Icon

### Fixes:

* Fixes NPE Scenarios in Item Getters

* Revert Auto-Setting Options in the Config GUI

* Fixes `\n` Escape Parsing

* Fixes Back Button Enable Conditions in Status Messages GUI

### More Information:

#### Config Overhaul (Breaking Change with v2 Configs):

The Config GUI has now been overhauled to provide a more User-Friendly Experience

* Migrating on `1.7.10 - 1.12.2` (From v1.3.x and Below) - Remove `craftpresence.cfg` and either Regenerate or Manually Migrate

* Migrating on `1.13.x` (From v1.3.x and Below) - Rename `craftpresence.cfg` to `craftpresence.properties` or Regenerate Config

Over time, this GUI will be edited and add new Options to further customize your experience.

#### Inviting Others to Join Info:

This Feature is currently in-dev, and some things may not work relating to Joining your friends such as:

* Minecraft may not launch if you click Join on an Invite

    * This is due to an Issue with how Minecraft is detected in Discord where it only detects certain javaw.exe's, Not all of them

    * Possible Workaround: Set your Client's Java Version to Java 8 Update 172 (Discord only Detects this version of javaw.exe)

    * If things still do not work after this workaround, Please submit an Issue
