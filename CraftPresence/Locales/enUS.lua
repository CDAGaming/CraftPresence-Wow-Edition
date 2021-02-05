local L = LibStub("AceLocale-3.0"):NewLocale("CraftPresence", "enUS", true)

L["TITLE_CLIENT_ID"] = "Client ID"
L["COMMENT_CLIENT_ID"] = "Client ID used for retrieving assets, icon keys, and titles"
L["USAGE_CLIENT_ID"] = "<16-digit numerical id here>"
L["DEFAULT_CLIENT_ID"] = "805124430774272000"
L["ERROR_CLIENT_ID"] = "Error: Sanity Checks failed for Client ID. Please enter a 16-digit numerical value."

L["TITLE_GAME_STATE_MESSAGE"] = "Game State Message"
L["COMMENT_GAME_STATE_MESSAGE"] = "The message(s) to be displayed in the Game State area of the RPC."
L["USAGE_GAME_STATE_MESSAGE"] = "<Your message here, using #eventType# format with @xxx@ being inner placeholders>"
L["DEFAULT_GAME_STATE_MESSAGE"] = "#dungeon##raid##battleground##arena##default#"

L["TITLE_DETAILS_MESSAGE"] = "Details Message"
L["COMMENT_DETAILS_MESSAGE"] = "The message(s) to be displayed in the Details area of the RPC."
L["USAGE_DETAILS_MESSAGE"] = "<Your message here, using #eventType# format with @xxx@ being inner placeholders>"
L["DEFAULT_DETAILS_MESSAGE"] = "@player_info@"

L["TITLE_LARGE_IMAGE_KEY"] = "Large Image Key"
L["COMMENT_LARGE_IMAGE_KEY"] = "The image key to be displayed as the Large Image of the RPC."
L["USAGE_LARGE_IMAGE_KEY"] = "<Your message here, using #eventType# format with @xxx@ being inner placeholders>"
L["DEFAULT_LARGE_IMAGE_KEY"] = "wow_icon"

L["TITLE_LARGE_IMAGE_MESSAGE"] = "Large Image Message"
L["COMMENT_LARGE_IMAGE_MESSAGE"] = "The message(s) to be displayed when hovering over the Large Image area of the RPC."
L["USAGE_LARGE_IMAGE_MESSAGE"] = "<Your message here, using #eventType# format with @xxx@ being inner placeholders>"
L["DEFAULT_LARGE_IMAGE_MESSAGE"] = "@realm_info@"

L["TITLE_SMALL_IMAGE_KEY"] = "Small Image Key"
L["COMMENT_SMALL_IMAGE_KEY"] = "The image key to be displayed as the Small Image of the RPC."
L["USAGE_SMALL_IMAGE_KEY"] = "<Your message here, using #eventType# format with @xxx@ being inner placeholders>"
L["DEFAULT_SMALL_IMAGE_KEY"] = "@player_alliance@"

L["TITLE_SMALL_IMAGE_MESSAGE"] = "Small Image Message"
L["COMMENT_SMALL_IMAGE_MESSAGE"] = "The message(s) to be displayed when hovering over the Small Image area of the RPC."
L["USAGE_SMALL_IMAGE_MESSAGE"] = "<Your message here, using #eventType# format with @xxx@ being inner placeholders>"
L["DEFAULT_SMALL_IMAGE_MESSAGE"] = "@player_alliance@"

L["TITLE_DUNGEON_MESSAGE"] = "Dungeon Placeholder Message"
L["COMMENT_DUNGEON_MESSAGE"] = "The message(s) to be interpreted as the #dungeon# placeholder."
L["USAGE_DUNGEON_MESSAGE"] = "<Your message here>"
L["DEFAULT_DUNGEON_MESSAGE"] = "@zone_name@ - In @difficulty_name@ Dungeon"

L["TITLE_RAID_MESSAGE"] = "Raid Placeholder Message"
L["COMMENT_RAID_MESSAGE"] = "The message(s) to be interpreted as the #raid# placeholder."
L["USAGE_RAID_MESSAGE"] = "<Your message here>"
L["DEFAULT_RAID_MESSAGE"] = "@zone_name@ - In @difficulty_name@ Raid"

L["TITLE_BATTLEGROUND_MESSAGE"] = "Battleground Placeholder Message"
L["COMMENT_BATTLEGROUND_MESSAGE"] = "The message(s) to be interpreted as the #battleground# placeholder."
L["USAGE_BATTLEGROUND_MESSAGE"] = "<Your message here>"
L["DEFAULT_BATTLEGROUND_MESSAGE"] = "@zone_name@ - In Battleground"

L["TITLE_ARENA_MESSAGE"] = "Arena Placeholder Message"
L["COMMENT_ARENA_MESSAGE"] = "The message(s) to be interpreted as the #arena# placeholder."
L["USAGE_ARENA_MESSAGE"] = "<Your message here>"
L["DEFAULT_ARENA_MESSAGE"] = "@zone_name@ - In Arena"

L["TITLE_FALLBACK_MESSAGE"] = "Default Placeholder Message"
L["COMMENT_FALLBACK_MESSAGE"] = "The message(s) to be interpreted as the #default# placeholder (TODO: Can be annotated with :optional where used if you wish it to show standalone)."
L["USAGE_FALLBACK_MESSAGE"] = "<Your message here>"
L["DEFAULT_FALLBACK_MESSAGE"] = "@zone_info@@dead_state@"

L["TITLE_DEAD_MESSAGE"] = "Dead State Inner Placeholder Message"
L["COMMENT_DEAD_MESSAGE"] = "The message(s) to be interpreted as the @dead_state@ placeholder."
L["USAGE_DEAD_MESSAGE"] = "<Your message here>"
L["DEFAULT_DEAD_MESSAGE"] = "Corpse Running"

L["TITLE_SHOW_LOGGING_IN_CHAT"] = "Show Logging in Chat"
L["COMMENT_SHOW_LOGGING_IN_CHAT"] = "Toggles the display of logging within chat."

L["TITLE_DEBUG_MODE"] = "Debug Mode"
L["COMMENT_DEBUG_MODE"] = "Toggles the display of verbose and more descriptive logging."

L["TITLE_VERBOSE_MODE"] = "Verbose Mode"
L["COMMENT_VERBOSE_MODE"] = "Toggles the display of debugger logging (Should be kept off if you wish to avoid spam)."

L["ZONE_NAME_UNKNOWN"] = "Unknown"
