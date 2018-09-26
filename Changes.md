# CraftPresence Changes

## v1.4.5 (09/28/2018)

### Changes:

* Config & Config GUI Changes:

  * Removed Icon Selection from New Values [(Issue Reference)](https://gitlab.com/CDAGaming/CraftPresence/issues/3)

  * No longer Allow Client IDs with Letters in Config GUI

  * Removed "Add New" Option when Selecting an Icon

* Added `&ign&` Placeholder for Server and SinglePlayer Messages

### Fixes:

* Fixes Mod GUI Class Detection for Per-GUI System (Now Universal)

* Fixes "Unrecognized Command" with `/cp view assets large`

* Fixes an NPE when Editing a Previously Missing Value in Config GUI

* Fixes setImage() for defaultIcon & Else Cases

* Fixes an NPE in Some Cases when Setting an Icon [(Issue Reference)](https://gitlab.com/CDAGaming/CraftPresence/issues/3)

* Fixes Issues with Getting Biome Information [(Issue Reference)](https://gitlab.com/CDAGaming/CraftPresence/issues/4)

* Fixes Case-Sensitive Issues with Search Functions

* Fixes Retrieving Sub Items and Blocks

* Fixes Possible NPE Scenarios with Dimension and Entity Getters

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
