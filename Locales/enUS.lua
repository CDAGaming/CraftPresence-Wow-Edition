local L = LibStub("AceLocale-3.0"):NewLocale("CraftPresence", "enUS", true)

local COLOR_GREEN = '|cFF00FF7F'
local COLOR_GREY = '|cfd9b9b9b'
local COLOR_RED = '|cFFFF6060'
local COLOR_GOLD = '|cFFFFD700'

-- Config Category Data
L["CATEGORY_TITLE_GENERAL"] = "General"
L["CATEGORY_COMMENT_GENERAL"] = "General settings for display info"

L["CATEGORY_TITLE_PLACEHOLDERS"] = "Placeholders"
L["CATEGORY_COMMENT_PLACEHOLDERS"] = "Settings for customizing placeholder data"

L["CATEGORY_TITLE_BUTTONS"] = "Buttons"
L["CATEGORY_COMMENT_BUTTONS"] = "Settings for customizing additional button data"

L["CATEGORY_TITLE_EXTRA"] = "Extra"
L["CATEGORY_COMMENT_EXTRA"] = "Extra customizability options for addon display info"

L["CATEGORY_TITLE_ABOUT"] = "About"
L["CATEGORY_COMMENT_ABOUT"] = "About this addon"
-- Config Variable Data
L["TITLE_CLIENT_ID"] = "Client ID"
L["COMMENT_CLIENT_ID"] = "Client ID used for retrieving assets, icon keys, and titles"
L["USAGE_CLIENT_ID"] = "<18-digit numerical id here>"
L["DEFAULT_CLIENT_ID"] = "805124430774272000"
L["ERROR_CLIENT_ID"] = "Sanity Checks failed for Client ID. Please enter a 18-digit numerical value."

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
L["DEFAULT_FALLBACK_MESSAGE"] = "@zone_info@"

L["TITLE_DEBUG_MODE"] = "Debug Mode"
L["COMMENT_DEBUG_MODE"] = "Toggles the display of verbose and more descriptive logging."

L["TITLE_VERBOSE_MODE"] = "Verbose Mode"
L["COMMENT_VERBOSE_MODE"] = "Toggles the display of detailed debugger logging."

L["TITLE_SHOW_MINIMAP_ICON"] = "Show Minimap Icon"
L["COMMENT_SHOW_MINIMAP_ICON"] = "Toggles the display of the minimap icon, used to access the config. (Requires reload)"

L["TITLE_QUEUED_PIPELINE"] = "Queued Pipeline"
L["COMMENT_QUEUED_PIPELINE"] = ([=[Toggles whether the callback delay will operate in a skip or queue style]=])

L["TITLE_CALLBACK_DELAY"] = "Callback Delay"
L["COMMENT_CALLBACK_DELAY"] = "The delay after events before RPC updates trigger (Doesn't effect /cp update)"
L["MINIMUM_CALLBACK_DELAY"] = 0
L["MAXIMUM_CALLBACK_DELAY"] = 30

L["TITLE_FRAME_CLEAR_DELAY"] = "Frame Clear Delay"
L["COMMENT_FRAME_CLEAR_DELAY"] = "The delay after events before drawn frames are cleared (Doesn't effect debug states)"
L["MINIMUM_FRAME_CLEAR_DELAY"] = 5
L["MAXIMUM_FRAME_CLEAR_DELAY"] = 15

L["TITLE_FRAME_SIZE"] = "Frame Render Size"
L["COMMENT_FRAME_SIZE"] = ([=[The size that each frame pixel should render at
(This value needs to be the same as the script's config_size property)]=])
L["MINIMUM_FRAME_SIZE"] = 5
L["MAXIMUM_FRAME_SIZE"] = 15

L["TITLE_PRIMARY_BUTTON"] = "Primary Button"
L["COMMENT_PRIMARY_BUTTON"] = "The data to be interpreted for the Primary Button area of the RPC"

L["TITLE_SECONDARY_BUTTON"] = "Secondary Button"
L["COMMENT_SECONDARY_BUTTON"] = "The data to be interpreted for the Secondary Button area of the RPC"

L["TITLE_BUTTON_LABEL"] = "Label"
L["COMMENT_BUTTON_LABEL"] = "The message(s) to be interpreted as the label."
L["USAGE_BUTTON_LABEL"] = "<Your message here>"

L["TITLE_BUTTON_URL"] = "Url"
L["COMMENT_BUTTON_URL"] = "The message(s) to be interpreted as the url."
L["USAGE_BUTTON_URL"] = "<Your message here>"

-- Logging Data
L["VERBOSE_LAST_ENCODED"] = "Last sent activity => %s"
L["DEBUG_SEND_ACTIVITY"] = "Sending activity => %s"
L["DEBUG_MAX_BYTES"] = "Max bytes that can be stored: %s"
L["DEBUG_VALUE_CHANGED"] = "%s changed from %s to %s"
L["INFO_EVENT_SKIPPED"] = "Event Skipped => %s"
L["INFO_EVENT_PROCESSING"] = "Event Processing => %s"
L["INFO_RESET_CONFIG"] = "Resetting Config Data..."
L["ERROR_BYTE_OVERFLOW"] = "You're painting too many bytes (%s vs %s)"
L["ERROR_COMMAND_CONFIG"] = "You are missing the required config option to access this command (Enable %s)"
L["ADDON_INTRO"] = "CraftPresence %s Loaded. Use /cp or /craftpresence to access config."
L["ADDON_CLOSE"] = "Shutting down Discord Rich Presence..."
L["ADDON_BUILD_INFO"] = "Build Info: %s"

-- Command: /cp placeholders
L["INFO_PLACEHOLDERS_QUERY"] = ([=[Searching for placeholders containing ^%s|r...]=]
):gsub('*', COLOR_GREEN):gsub('%^', COLOR_GREY)
L["INFO_PLACEHOLDER_INTRO"] = ([=[Available Placeholders (*<key>|r => ^<value>|r):]=]
):gsub('*', COLOR_GREEN):gsub('%^', COLOR_GREY)
L["INFO_PLACEHOLDER_NONE"] = ([=[*No placeholders found within specified parameters|r]=]
):gsub('*', COLOR_RED):gsub('%^', COLOR_GREY)
L["INFO_PLACEHOLDER_DATA"] = ([=[*%s|r => ^%s|r]=]
):gsub('*', COLOR_GREEN):gsub('%^', COLOR_GREY)
L["INFO_PLACEHOLDER_NOTE"] = ([=[NOTE: Placeholder keys surrounded by *#|r are global (Can include inner placeholders),
while ones surrounded by *@|r are inner (Cannot include other placeholders)]=]
):gsub('*', COLOR_GREEN):gsub('%^', COLOR_GREY)

-- Command: /cp clear|clean
L["INFO_COMMAND_CLEAR"] = "Clearing active frame data..."

-- Config Error Standards
L["ERROR_RANGE_DEFAULT"] = "Sanity Checks failed for %s. Please enter a numerical value between %s and %s."
-- General Command Data
L["HELP_COMMANDS"] = ([=[Here is a list of all important *%s|r commands:
 */cp|r ^help|r or */cp|r ^?|r  -  Displays this helpful menu.
 */cp|r ^config|r  -  Displays the *OptionsUI|r.
 */cp|r ^test|r  -  Toggles debugging of Rich Presence Frames.
 */cp|r ^clean|r or */cp|r ^clear|r  -  Reset all frames to their original positions and colors.
 */cp|r ^update[:force]|r  -  Forcibly update your Rich Presence display (Also forces an instance change if specified).
 */cp|r ^minimap|r  -  Toggles the display of the minimap button.
 */cp|r ^status|r  -  Views the last sent Rich Presence event.
 */cp|r ^reset[:grp,key]|r  -  Resets all (or a specific) setting(s) within the *OptionsUI|r.
 */cp|r ^placeholders[:query]|r  -  Views the currently available placeholders.
 NOTE: All commands must be prefixed with either */%s|r or */%s|r.
 Optional arguments in commands are represented by *[syntax]|r.
]=]):gsub('*', COLOR_GREEN):gsub('%^', COLOR_GREY)
-- Frame Text Data
L["ADDON_HEADER_CREDITS"] = "Credits"

L["ADDON_INFO_ONE"] = ([=[CraftPresence allows you to customize the way others see you play with Discord Rich Presence.
]=])
L["ADDON_INFO_TWO"] = "Created by CDAGaming (https://gitlab.com/CDAGaming)"
L["ADDON_INFO_THREE"] = "Thanks to Attka and wowdim on Github for the original base project, that makes this possible."
L["ADDON_INFO_FOUR"] = "Special thanks to the-emerald/python-discord-rpc and Attk4/wow-discord-rich-presence"

L["ADDON_TOOLTIP_THREE"] = "Click to access config data."
L["ADDON_TOOLTIP_FIVE"] = "Toggle minimap button by typing |c33c9fcff/cp minimap|r"
-- Data Default Values
L["ONLINE_LABEL"] = "Online"
L["AFK_LABEL"] = "AFK"
L["DND_LABEL"] = "DND"
L["GHOST_LABEL"] = "Ghost"
L["DEAD_LABEL"] = "Dead"
L["TYPE_UNKNOWN"] = "Unknown"
L["TYPE_NONE"] = "None"
L["LEVEL_TAG_FORMAT"] = "Level %s"

-- NOTE: The values below should NEVER be changed
L["UNKNOWN_KEY"] = "Skip"
L["ARRAY_SPLIT_KEY"] = "=="
L["DEBUG_LOG"] = "[Debug] %s"
L["VERBOSE_LOG"] = "[Verbose] %s"
L["ERROR_LOG"] = ([=[*[Error]|r %s]=]):gsub('*', COLOR_RED)
L["WARNING_LOG"] = ([=[*[Warning]|r %s]=]):gsub('*', COLOR_GOLD)
L["ADDON_NAME"] = "CraftPresence"
L["ADDON_ID"] = "craftpresence"
L["ADDON_AFFIX"] = "cp"
L["RPC_EVENT_FORMAT"] = "$RPCEvent$%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s$RPCEvent$"
