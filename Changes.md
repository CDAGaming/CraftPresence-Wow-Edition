# CraftPresence Changes

## v1.4.0 (09/03/2018)

### Changes:

* Build Changes (Code-Behind):

  * Forge Version: `14.23.4.2747` >> `14.23.4.2759`

  * Mappings Version: `snapshot_20180808` >> `snapshot_20180814`

* Config Overhaul for better Simplicity & Usability (v2 >> v3)

* New Commands:

  * `/cp config` - Opens v3 Config GUI

* Edited Commands:

  * `/cp assets <...>` >> `/cp view assets <...>`

  * `/cp dimensions` >> `/cp view dimensions`

  * `/cp currentData` >> `/cp view currentData`

### Fixes:

* Fixes an Issue where Enabling Per-Item would cause an immense Performance Drop and RPC Issues when in Multiplayer

* Fixes Config Saving Issues (New System)

* Minor Typo Fixes

### More Information:

#### Config Overhaul (Breaking Change with v2 Configs):

The Config GUI has now been overhauled to provide a more User-Friendly Experience

* Migrating from `1.7.10 - 1.12.2` (v1.3.x and Lower) - Manual Migrate

* Migrating from `1.13.x` (v1.3.x and Lower) - Rename `craftpresence.cfg` to `craftpresence.properties`

Over time, this GUI will be edited and add new Options, but in v1.4.0, Server and Advanced Settings cannot be customized in the GUI and will be added in a future update.

#### Upcoming Removal of v1.3.5 and Below (09/07/2018)

Due to recent events, CraftPresence v1.3.5 and below will be removed permanently from CurseForge on September 7th, 2018

This is also to ensure that Users do not run into Issues relating to migrating config systems in the future