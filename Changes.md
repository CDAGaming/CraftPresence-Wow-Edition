# CraftPresence Changes

## v1.7.5 (11/09/2020)

_A Detailed Changelog from the last Release is available [here](https://gitlab.com/CDAGaming/CraftPresence/-/compare/release%2Fv1.7.2...release%2Fv1.7.5)_

### Changes

*   Added `/cp export assets` command (Has two optional arguments for usage specification)
*   (Breaking) Changed `Show Game State` to `Detect World Data` to be more representative
*   (Breaking) Changed `Show Biome` and `Show Dimension` to `Detect Biome Data` and `Detect Dimension Data` to be more representative
*   List Data will no longer update before the config saves (Related to a v1.7.2 change)
*   `StringUtils#getStringWidth` now properly supports bold characters (Also fixes the release notice position)
*   All Data in the Color Editor Guis will now synchronize values without needing to push the Enter Key (As such using the Enter Key for syncing has been removed)
*   (Adjustment) Some Textures in the mod will now only display is the texture itself is not null
*   The `&health&` sub-placeholder in `&playerinfo&` will now be rounded, to the nearest whole number
*   Added Support for Party Privacy Levels in RPC (See [this commit](https://github.com/discord/discord-rpc/pull/306/))
*   Added Server list icon support for the Selector Guis
*   Added `Strip Extra Gui Elements` setting to `Accessibility Settings`
*   Added `Reset Time on Init` setting to `General Settings`
*   Added Icon Support for Biomes (You'll need to supply your own icons for them, not enough room in default client id for all of them)
*   (QOL) The Preview box in the `Color Editor` Guis now have a border around it for better clarity
*   (Backend) Added support for Flatpak and Snap distributions of Discord
*   (Backend) Base64 and direct byte array Image Formats now supported in `ImageUtils` and related areas
*   (Backend) Added Uuid Detection, will be used within applicable settings that have player uuid values
*   (Backend-Breaking) Multiple Config Variable changes to have better clarity in codebase -- `Default Server MOTD` changed to `Default Server Motd`
*   (Backend-Regression) Added back support for autoRegister into IPC backend
*   An assortment of backend optimizations and improvements

### Fixes

*   Fixes an oversight with `StringUtils#roundDouble` not being able to round up to a full whole number
*   Fixes some Guis not properly displaying the Image-Preview styled scroll list
*   Fixes HTTPS access for `FileUtils#downloadFile`
*   Fixes Color Editor Gui saving issues when only editing the red color value
*   Fixes Color Editor Gui having false positive `hasChanged` states
*   Fixes improper sizing in some cases with external text controls
*   Fixes most if not all possible circumstances of GuiUtils causing JVM errors
*   Fixes `keyTyped` event in `ExtendedTextControl` to only occur if the super event would execute
*   Fixes an oversight causing `/cp shutdown` and `/cp reboot` combined usage fail
*   Fixes a GL flag clearing Issue causing unintended Gui rendering behaviors
*   Fixes a regression causing Servers with Port Numbers to not properly be detected for overrides

___

### More Information

#### v1.7.0 Upgrade Info

v1.7.0 of CraftPresence is the next major feature and technical update after v1.6.0.
It is celebrating the recent milestones of over **five million downloads** as well as hitting the number one spot in Twitch Integration.

This release holds numerous improvements over the existing configuration system, as well as adding numerous long-standing user requests and planned features that have been queued over time since v1.4.0s release,
that are outlined in the changelog above.

While no config migrations are necessary at this time for updating to v1.7.0, this can change as time goes on in the v1.7.x Pipeline, and will be noted here as such when and if these types of changes occur.

More features will additionally be planned and added for later in the v1.7.x Pipeline as further releases arrive.

#### 1.13.x Build Info

The Rift Edition of this Mod Requires the [Rift ModLoader](https://www.curseforge.com/minecraft/mc-mods/rift) and contains the following differences to take Note of:

*   KeyCodes have changed from an LWJGL Upgrade! Be Sure to check and edit your KeyBinds if migrating from 1.12.2 and below.

Starting in v1.5.0, The 1.13 Rift Port of CraftPresence was deprecated in favor of the 1.13.2 Rift Port.

Starting in v1.7.0, The aforementioned KeyCode warning is now void, due to new systems introduced to convert keybindings between LWJGL versions, and this message will be removed in v1.7.1.

Note: Due to Maven Troubles on behalf of the Rift Team, Rift Versions are no longer supported as of v1.6.1, though the Differences do still take effect for Forge.

#### 1.14.x - 1.16.x Build Info

Some 1.14.x, 1.15.x, and 1.16.x Ports of this Mod Require the [FabricMC ModLoader](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and contains the same differences as the 1.13.x Port.

#### Snapshot Build Info

Some Versions of this Mod are for Minecraft Snapshots or Experimental Versions, and as such, caution should be noted.

Any Snapshot Build released will be marked as BETA to match its Snapshot Status depending on tests done before release and issues found.

Snapshot Builds, depending on circumstances, may also contain changes for a future version of the mod, and will be noted as so if this is the case.

#### Legacy Build Info (Minecraft Versions 1.5.2 and Below)

Ports of this Mod for Minecraft Versions 1.5.2 and Lower are on very limited support.

Please keep in mind the following:

*   There is NO Support for Server RPC Displays from MC 1.2.5 and Below, due to modding limitations.
*   Bugs that relate with or are caused by issues in the Vanilla Codebase, are unlikely able to be fixed due to Minecraft's limitations

See the Mod Description // README for More Info
