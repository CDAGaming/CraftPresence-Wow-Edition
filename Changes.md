# CraftPresence Changes

## v1.7.0 (??/??/2020)

### Changes

*   Added the `&yPosition&` Sub-Placeholder to the `&coords&` Placeholder
*   Hardcore Mode now supported in `&difficulty&` Sub-Placeholder
*   Added Comments to the Config File, showing a title and more clearly showing version and modified metadata
*   Added a Conversion Layer for converting KeyCodes between LWJGL2 (1.12.2 and below) and LWJGL3 (1.13 and above)
*   Added a Conversion Layer for converting Language IDs between Pack Format 1-2 (1.10.2 and below) and Pack Format 3 and Up (1.11 and above)
*   Hovering over the Config Gui Main Screen Title, now shows a tooltip with the current mod version
*   Separated `IS_DEV` into two options, to allow for toggling excessive logging (Verbose Logging)

### Fixes

*   Fixed an Issue causing KeyCode Properties to be able to skip verification
*   Fixed an Issue where the `Config Gui` Keybind was not properly identified
*   Fixed an Issue causing some LWJGL 3 KeyCodes to not be recognized

### More Information

#### v1.7.0 Upgrade Info

TBD

#### 1.13.x Build Info

The Rift Edition of this Mod Requires the [Rift ModLoader](https://www.curseforge.com/minecraft/mc-mods/rift) and contains the following differences to take Note of:

*   KeyCodes have changed from an LWJGL Upgrade! Be Sure to check and edit your KeyBinds if migrating from 1.12.2 and below.

Starting in v1.5.0, The 1.13 Rift Port of CraftPresence was deprecated in favor of the 1.13.2 Rift Port

Starting in v1.7.0, The aforementioned KeyCode warning is now void, due to new systems introduced to convert keybindings between LWJGL versions

Note: Due to Maven Troubles on behalf of the Rift Team, Rift Versions are no longer supported as of v1.6.1, though the Differences do still take effect for Forge

#### 1.14.x - 1.16.x Build Info

Some 1.14.x, 1.15.x, and 1.16.x Ports of this Mod Require the [FabricMC ModLoader](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and contains the same differences as the 1.13.x Port

#### Snapshot Build Info

Some Versions of this Mod are for Minecraft Snapshots or Experimental Versions, and as such, caution should be noted.

Any Snapshot Build Released will be marked as BETA to match its Snapshot Status depending on Tests done before release and issues found.

Snapshot Builds, depending on circumstances, may also contain changes for a future version of the mod, and will be noted as so if this is the case.

#### Legacy Build Info (Minecraft Versions 1.5.2 and Below)

Ports of this Mod for Minecraft Versions 1.5.2 and Lower are on very limited support.

Please Keep in Mind the Following:

*   There is NO Support for Server RPC Displays from MC 1.2.5 and Below, due to modding limitations.
*   Bugs that relate with or are caused by issues in the Vanilla Codebase, are unlikely able to be fixed due to MC's limitations

See the Mod Description // README for More Info
