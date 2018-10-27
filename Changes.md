# CraftPresence Changes

## v1.4.9 (??/??/2018)

### Changes:

* ???

### Fixes:

* Fixes Rapid RPC Updates for if Show Game State and Per-Item are both Enabled

* Fixes Some CommandSender Reference Issues

### More Information:

#### Config Overhaul (Breaking Change with v2 Configs):

The Config GUI has now been overhauled to provide a more User-Friendly Experience

* Migrating on `1.7.10 - 1.12.2` (From v1.3.x and Below) - Remove `craftpresence.cfg` and either Regenerate or Manually Migrate

* Migrating on `1.13.x` (From v1.3.x and Below) - Rename `craftpresence.cfg` to `craftpresence.properties` or Regenerate Config

Over time, this GUI will be edited and change and improve to further customize your experience.

#### Inviting Others to Join Info:

This Feature is currently in-dev, and some things may not work relating to Joining your friends such as:

* Minecraft may not launch if you click Join on an Invite

    * This is due to an Issue with how Minecraft is detected in Discord where it only detects certain javaw.exe's, Not all of them

    * Possible Workaround: Set your Client's Java Version to Java 8 Update 172 (Discord only Detects this version of javaw.exe)

    * If things still do not work after this workaround, Please submit an Issue
