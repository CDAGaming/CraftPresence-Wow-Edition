local L = LibStub("AceLocale-3.0"):NewLocale("CraftPresence", "enUS", true)

local COLOR1 = '|cFF00FF7F'
local COLOR2 = '|cfd9b9b9b'

L["TITLE_CLIENT_ID"] = "Client ID"
L["COMMENT_CLIENT_ID"] = "Client ID used for retrieving assets, icon keys, and titles"
L["USAGE_CLIENT_ID"] = "<18-digit numerical id here>"
L["DEFAULT_CLIENT_ID"] = "805124430774272000"
L["ERROR_CLIENT_ID"] = "Error: Sanity Checks failed for Client ID. Please enter a 18-digit numerical value."

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
L["DEFAULT_DUNGEON_MESSAGE"] = "@zone_name@ - In @difficulty_info@ Dungeon @lockout_encounters@"

L["TITLE_RAID_MESSAGE"] = "Raid Placeholder Message"
L["COMMENT_RAID_MESSAGE"] = "The message(s) to be interpreted as the #raid# placeholder."
L["USAGE_RAID_MESSAGE"] = "<Your message here>"
L["DEFAULT_RAID_MESSAGE"] = "@zone_name@ - In @difficulty_info@ Raid @lockout_encounters@"

L["TITLE_BATTLEGROUND_MESSAGE"] = "Battleground Placeholder Message"
L["COMMENT_BATTLEGROUND_MESSAGE"] = "The message(s) to be interpreted as the #battleground# placeholder."
L["USAGE_BATTLEGROUND_MESSAGE"] = "<Your message here>"
L["DEFAULT_BATTLEGROUND_MESSAGE"] = "@zone_name@ - In Battleground"

L["TITLE_ARENA_MESSAGE"] = "Arena Placeholder Message"
L["COMMENT_ARENA_MESSAGE"] = "The message(s) to be interpreted as the #arena# placeholder."
L["USAGE_ARENA_MESSAGE"] = "<Your message here>"
L["DEFAULT_ARENA_MESSAGE"] = "@zone_name@ - In Arena"

L["TITLE_FALLBACK_MESSAGE"] = "Default Placeholder Message"
L["COMMENT_FALLBACK_MESSAGE"] = "The message(s) to be interpreted as the #default# placeholder."
L["USAGE_FALLBACK_MESSAGE"] = "<Your message here>"
L["DEFAULT_FALLBACK_MESSAGE"] = "@zone_info@@dead_state@"

L["TITLE_DEAD_MESSAGE"] = "Dead State Inner Placeholder Message"
L["COMMENT_DEAD_MESSAGE"] = "The message(s) to be interpreted as the @dead_state@ placeholder."
L["USAGE_DEAD_MESSAGE"] = "<Your message here>"
L["DEFAULT_DEAD_MESSAGE"] = "Corpse Running"

L["TITLE_DEBUG_MODE"] = "Debug Mode"
L["COMMENT_DEBUG_MODE"] = "Toggles the display of verbose and more descriptive logging."

L["TITLE_VERBOSE_MODE"] = "Verbose Mode"
L["COMMENT_VERBOSE_MODE"] = "Toggles the display of debugger logging (Should be kept off if you wish to avoid spam)."

L["TITLE_SHOW_MINIMAP_ICON"] = "Show Minimap Icon"
L["COMMENT_SHOW_MINIMAP_ICON"] = "Toggles the display of the minimap icon, used to access the config. (May require a reload)"

L["TITLE_CALLBACK_DELAY"] = "Callback Delay"
L["COMMENT_CALLBACK_DELAY"] = "The delay after events before RPC updates trigger (Doesn't effect /cp update)"
L["ERROR_CALLBACK_DELAY"] = "Error: Sanity Checks failed for Callback Delay. Please enter a numerical value above 0 and below 30."

L["TITLE_FRAME_CLEAR_DELAY"] = "Frame Clear Delay"
L["COMMENT_FRAME_CLEAR_DELAY"] = "The delay after events before drawn frames are cleared (Doesn't effect debug states)"
L["ERROR_FRAME_CLEAR_DELAY"] = "Error: Sanity Checks failed for Frame Clear Delay. Please enter a numerical value above 5 and below 15."

L["TITLE_FRAME_SIZE"] = "Frame Render Size"
L["COMMENT_FRAME_SIZE"] = "The size that each frame pixel should render at (This value needs to be the same as the script's config_size property)"
L["ERROR_FRAME_SIZE"] = "Error: Sanity Checks failed for Frame Render Size. Please enter a numerical value above 5 and below 15."

-- Logging Data
L["DEBUG_SEND_ACTIVITY"] = "Sending activity => %s"
L["DEBUG_MAX_BYTES"] = "Max bytes that can be stored: %s"
L["DEBUG_VALUE_CHANGED"] = "%s changed from %s to %s"
L["VERBOSE_LAST_ENCODED"] = "Last sent activity => %s"
L["VERBOSE_PLACEHOLDER_INTRO"] = "Available Placeholders (<key> -> <value>):"
L["VERBOSE_PLACEHOLDER_DATA"] = "%s Data: %s -> %s"
L["ERROR_BYTE_OVERFLOW"] = "You're painting too many bytes (%s vs %s)"
L["ERROR_COMMAND_CONFIG"] = "You are missing the required config option to access this command (Enable %s)"
L["ADDON_INTRO"] = "Discord Rich Presence Loaded. Use /cp or /craftpresence to access config."
L["ADDON_CLOSE"] = "Shutting down Discord Rich Presence..."
L["ADDON_BUILD_INFO"] = "Build Info: v%s (%s) dated %s => %s"
-- Command Data
L["HELP_COMMANDS"] = ([=[Here is a list of all important *%s|r commands:
 */cp|r or */craftpresence|r or */cp|r ^help|r  -  Displays this helpful menu.
 */cp|r ^config|r  -  Displays the *OptionsUI|r.
 */cp|r ^test|r  -  Toggles debugging of Rich Presence Frames.
 */cp|r ^clean|r or */cp|r ^clear|r  -  Reset all frames to their original positions and colors.
 */cp|r ^update|r  -  Forcibly update your Rich Presence display.
 */cp|r ^minimap|r  -  Toggles the display of the minimap button.
 */cp|r ^status|r  -  Views the last sent Rich Presence event.
 */cp|r ^placeholders|r  -  Views the currently available placeholders.
 NOTE: All commands must be prefixed with either */%s|r or */%s|r
]=]):gsub('*', COLOR1):gsub('%^', COLOR2)
-- Frame Text Data
L["ADDON_INFO_ONE"] = "CraftPresence allows you to completely customize the way others see you play with Discord Rich Presence."
L["ADDON_INFO_TWO"] = "Created by CDAGaming (https://gitlab.com/CDAGaming)"
L["ADDON_INFO_THREE"] = "Thanks to Attka and wowdim on Github for the original base project, that makes this possible."
L["ADDON_INFO_FOUR"] = "Special thanks to the-emerald/python-discord-rpc and Attk4/wow-discord-rich-presence"

L["ADDON_TOOLTIP_THREE"] = "Click to access config data."
L["ADDON_TOOLTIP_FIVE"] = "Toggle minimap button by typing |c33c9fcff/cp minimap|r"
-- Data Default Values
L["ONLINE_LABEL"] = "Online"
L["AFK_LABEL"] = "AFK"
L["DND_LABEL"] = "DND"
L["ZONE_NAME_UNKNOWN"] = "Unknown"
L["LEVEL_TAG_FORMAT"] = "Level %s"
-- The values below should NEVER be changed
L["UNKNOWN_KEY"] = "Skip"
L["DEBUG_LOG"] = "[Debug] %s"
L["VERBOSE_LOG"] = "[Verbose] %s"
L["ERROR_LOG"] = "[Error] %s"
L["WARNING_LOG"] = "[Warning] %s"
L["ADDON_NAME"] = "CraftPresence"
L["ADDON_ID"] = "craftpresence"
L["ADDON_AFFIX"] = "cp"
L["ADDON_VERSION"] = "v0.1.2"
L["RPC_EVENT_FORMAT"] = "$RPCEvent$%s|%s|%s|%s|%s|%s|%s|%s|%s$RPCEvent$"
