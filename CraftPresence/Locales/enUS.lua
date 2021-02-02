local L = LibStub("AceLocale-3.0"):NewLocale("CraftPresence", "enUS", true)

L["TITLE_CLIENT_ID"] = "Client ID"
L["COMMENT_CLIENT_ID"] = "Client ID used for retrieving assets, icon keys, and titles"
L["USAGE_CLIENT_ID"] = "<16-digit numerical id here>"
L["DEFAULT_CLIENT_ID"] = "805124430774272000"
L["ERROR_CLIENT_ID"] = "Error: Sanity Checks failed for Client ID. Please enter a 16-digit numerical value."

L["TITLE_GAME_STATE_MESSAGE"] = "Game State Message"
L["COMMENT_GAME_STATE_MESSAGE"] = "The message(s) to be displayed in the Game State area of the RPC."
L["USAGE_GAME_STATE_MESSAGE"] = "<Your message here, using $eventType:message$ format with @xxx@ being placeholders>"
L["DEFAULT_GAME_STATE_MESSAGE"] = "$party:@zone@ - @dungeon@$$raid:@zone@ - @raid@$$pvp:@zone@ - @battleground@$$default:@sub_zone@ - @zone||dead@$"

L["TITLE_DETAILS_MESSAGE"] = "Details Message"
L["COMMENT_DETAILS_MESSAGE"] = "The message(s) to be displayed in the Details area of the RPC."
L["USAGE_DETAILS_MESSAGE"] = "<Your message here, using $eventType:message$ format with @xxx@ being placeholders>"
L["DEFAULT_DETAILS_MESSAGE"] = "@player_info@ - @realm_info@"

L["TITLE_SHOW_LOGGING_IN_CHAT"] = "Show Logging in Chat"
L["COMMENT_SHOW_LOGGING_IN_CHAT"] = "Toggles the display of logging within chat."

L["TITLE_DEBUG_MODE"] = "Debug Mode"
L["COMMENT_DEBUG_MODE"] = "Toggles the display of verbose and more descriptive logging."
