# CraftPresence Changes

## v1.4.2 (09/16/2018)

### Changes:

* Build Changes (Code-Behind):

  * Forge Build Version (1.12.2): `14.23.4.2759` >> `14.23.4.2760`

  * MCP Snapshot Version (1.13): `snapshot_20180903` >> `snapshot_20180914`

* Config & Config GUI Changes:

  * Add Search & Add New Functionality to Scroll Lists

  * Implemented Server Settings to GUI

  * Added Mod Support to Per-GUI System

  * Implement Config Verification System to prevent Invalid/Empty Settings

* Added Join Request Support (Joining from Invite Only, not Ask to Join)

* Added Temporary ViveCraft Compatibility (Will be Customizable in v1.4.5)

### Fixes:

* Fixes & Re-Adds some Missing Options in Config

* Fixes Cases of Null/Empty/Invalid Default Messages being allowed in GUI

* Fixes Linux & MacOS Compatibility (Again)

* Fixes Multi-Lingual RPC Support (Logging is Bugged)

* Fixes Cases where CraftPresence's Auto Server Icon Finder overwrote your Config Selection

* Fixes Removing Values from Selector Settings

* Fixes False-Positive Saving Scenarios

* Fixes `&icon&` Placeholder Support in Server IconKeys

* Fixes Issues involving the Config GUI replacing Settings after multiple edits in one instance (Ex: Changing a specific Dimension Icon Multiple Times)

* Minor Typo and Translation Fixes

### More Information:

#### Config Overhaul (Breaking Change with v2 Configs):

The Config GUI has now been overhauled to provide a more User-Friendly Experience

* Migrating from `1.7.10 - 1.12.2` (v1.3.x and Lower) - Manual Migrate

* Migrating from `1.13.x` (v1.3.x and Lower) - Rename `craftpresence.cfg` to `craftpresence.properties`

Over time, this GUI will be edited and add new Options to customize your experience.

#### Inviting Others to Join Info:

This Feature is currently in-dev, and somethings may not work relating to Joining your friends such as:

* Minecraft may not launch if you click Join on an Invite

    * This is due to an Issue with how Minecraft is detected in Discord where it only detects certain javaw.exe's, Not all of them

    * Possible Workaround: Set your Client's Java Version to Java 8 Update 172 (Discord only Detects this version of javaw.exe)

    * If things still do not work after this workaround, Please submit an Issue
