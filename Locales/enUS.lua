local L = LibStub("AceLocale-3.0"):NewLocale("CraftPresence", "enUS", true)

----------------------
--LUA UTILITIES (From Utils.lua)
----------------------

-- Lua APIs
local type, strgsub = type, string.gsub

--- Determines if the specified object is null or empty
---
--- @param obj any The object to interpret
---
--- @return boolean @ is_object_empty
local function IsNullOrEmpty(obj)
    return obj == nil or
            (type(obj) == "string" and obj == "") or
            (type(obj) == "table" and obj == {})
end

--- Replaces a String with the specified formatting
---
--- @param str string The input string to evaluate
--- @param replacer_one string The first replacer
--- @param replacer_two string The second replacer
--- @param pattern_one string The first pattern
--- @param pattern_two string The second pattern
---
--- @return string @ formatted_string
local function SetFormat(str, replacer_one, replacer_two, pattern_one, pattern_two)
    if IsNullOrEmpty(str) then
        return str
    end
    str = strgsub(str, pattern_one or "*", replacer_one or "")
    str = strgsub(str, pattern_two or "%^", replacer_two or "")
    return str
end

-- Color Codes
local GREEN = '|cFF00FF7F'
local GREY = '|cfd9b9b9b'
local RED = '|cFFFF6060'
local GOLD = '|cFFFFD700'

-- Data Default Values
L["ONLINE_LABEL"] = "Online"
L["AFK_LABEL"] = "AFK"
L["DND_LABEL"] = "DND"
L["GHOST_LABEL"] = "Ghost"
L["DEAD_LABEL"] = "Dead"
L["TYPE_UNKNOWN"] = "Unknown"
L["TYPE_NONE"] = "None"
L["LEVEL_TAG_FORMAT"] = "Level %s"
-- Internal Values (DNT)
L["UNKNOWN_KEY"] = "Skip"
L["ARRAY_SPLIT_KEY"] = "=="
L["INNER_KEY"] = "@"
L["GLOBAL_KEY"] = "#"
L["ADDON_NAME"] = "CraftPresence"
L["ADDON_ID"] = "craftpresence"
L["ADDON_AFFIX"] = "cp"
L["CONFIG_COMMAND"] = "cp set"
L["CONFIG_COMMAND_ALT"] = "craftpresence set"
L["RPC_EVENT_FORMAT"] = "$RPCEvent$%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s$RPCEvent$"

-- Addon APIs
local inkey, outkey = "@", "#"
local setfmt = function(str, replacer_one, replacer_two, pattern_one, pattern_two)
    return SetFormat(str, replacer_one, replacer_two, pattern_one, pattern_two)
end

-- Primary Logging Data
L["DEBUG_LOG"] = setfmt("*[Debug]|r %s", GREY)
L["VERBOSE_LOG"] = setfmt("*[Verbose]|r %s", GREY)
L["ERROR_LOG"] = setfmt("*[Error]|r %s", RED)
L["WARNING_LOG"] = setfmt("*[Warning]|r %s", GOLD)

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
L["COMMENT_GAME_STATE_MESSAGE"] = "The message to be displayed in the Game State area of the RPC."
L["USAGE_GAME_STATE_MESSAGE"] = "<Your message here>"
L["DEFAULT_GAME_STATE_MESSAGE"] = setfmt("*dungeon**raid**battleground**arena**default*", outkey)

L["TITLE_DETAILS_MESSAGE"] = "Details Message"
L["COMMENT_DETAILS_MESSAGE"] = "The message to be displayed in the Details area of the RPC."
L["USAGE_DETAILS_MESSAGE"] = "<Your message here>"
L["DEFAULT_DETAILS_MESSAGE"] = setfmt("*player_info*", inkey)

L["TITLE_LARGE_IMAGE_KEY"] = "Large Image Key"
L["COMMENT_LARGE_IMAGE_KEY"] = "The image key to be displayed as the Large Image of the RPC."
L["USAGE_LARGE_IMAGE_KEY"] = "<Your message here>"
L["DEFAULT_LARGE_IMAGE_KEY"] = "wow_icon"

L["TITLE_LARGE_IMAGE_MESSAGE"] = "Large Image Message"
L["COMMENT_LARGE_IMAGE_MESSAGE"] = "The message to be displayed when hovering over the Large Image area of the RPC."
L["USAGE_LARGE_IMAGE_MESSAGE"] = "<Your message here>"
L["DEFAULT_LARGE_IMAGE_MESSAGE"] = setfmt("*realm_info*", inkey)

L["TITLE_SMALL_IMAGE_KEY"] = "Small Image Key"
L["COMMENT_SMALL_IMAGE_KEY"] = "The image key to be displayed as the Small Image of the RPC."
L["USAGE_SMALL_IMAGE_KEY"] = "<Your message here>"
L["DEFAULT_SMALL_IMAGE_KEY"] = setfmt("*player_alliance*", inkey)

L["TITLE_SMALL_IMAGE_MESSAGE"] = "Small Image Message"
L["COMMENT_SMALL_IMAGE_MESSAGE"] = "The message to be displayed when hovering over the Small Image area of the RPC."
L["USAGE_SMALL_IMAGE_MESSAGE"] = "<Your message here>"
L["DEFAULT_SMALL_IMAGE_MESSAGE"] = setfmt("*player_alliance*", inkey)

L["TITLE_DUNGEON_MESSAGE"] = "Dungeon Placeholder Message"
L["COMMENT_DUNGEON_MESSAGE"] = setfmt("The message to show as the *dungeon* placeholder.", outkey)
L["USAGE_DUNGEON_MESSAGE"] = "<Your message here>"
L["DEFAULT_DUNGEON_MESSAGE"] = setfmt("*zone_name* - In *difficulty_info* Dungeon *lockout_encounters*", inkey)

L["TITLE_RAID_MESSAGE"] = "Raid Placeholder Message"
L["COMMENT_RAID_MESSAGE"] = setfmt("The message to show as the *raid* placeholder.", outkey)
L["USAGE_RAID_MESSAGE"] = "<Your message here>"
L["DEFAULT_RAID_MESSAGE"] = setfmt("*zone_name* - In *difficulty_info* Raid *lockout_encounters*", inkey)

L["TITLE_BATTLEGROUND_MESSAGE"] = "Battleground Placeholder Message"
L["COMMENT_BATTLEGROUND_MESSAGE"] = setfmt("The message to show as the *battleground* placeholder.", outkey)
L["USAGE_BATTLEGROUND_MESSAGE"] = "<Your message here>"
L["DEFAULT_BATTLEGROUND_MESSAGE"] = setfmt("*zone_name* - In Battleground", inkey)

L["TITLE_ARENA_MESSAGE"] = "Arena Placeholder Message"
L["COMMENT_ARENA_MESSAGE"] = setfmt("The message to show as the *arena* placeholder.", outkey)
L["USAGE_ARENA_MESSAGE"] = "<Your message here>"
L["DEFAULT_ARENA_MESSAGE"] = setfmt("*zone_name* - In Arena", inkey)

L["TITLE_FALLBACK_MESSAGE"] = "Default Placeholder Message"
L["COMMENT_FALLBACK_MESSAGE"] = setfmt("The message to show as the *default* placeholder.", outkey)
L["USAGE_FALLBACK_MESSAGE"] = "<Your message here>"
L["DEFAULT_FALLBACK_MESSAGE"] = setfmt("*zone_info*", inkey)

L["TITLE_DEBUG_MODE"] = "Debug Mode"
L["COMMENT_DEBUG_MODE"] = "Toggles the display of verbose and more descriptive logging."

L["TITLE_VERBOSE_MODE"] = "Verbose Mode"
L["COMMENT_VERBOSE_MODE"] = "Toggles the display of detailed debugger logging."

L["TITLE_SHOW_MINIMAP_ICON"] = "Show Minimap Icon"
L["COMMENT_SHOW_MINIMAP_ICON"] = "Toggles the display of the minimap icon, used to access the config. (Requires reload)"

L["TITLE_QUEUED_PIPELINE"] = "Queued Pipeline"
L["COMMENT_QUEUED_PIPELINE"] = "Toggles whether the callback delay will operate in a skip or queue style"

L["TITLE_CALLBACK_DELAY"] = "Callback Delay"
L["COMMENT_CALLBACK_DELAY"] = "The delay after events before RPC updates trigger (Doesn't effect /cp update)"
L["MINIMUM_CALLBACK_DELAY"] = 0
L["MAXIMUM_CALLBACK_DELAY"] = 30

L["TITLE_FRAME_CLEAR_DELAY"] = "Frame Clear Delay"
L["COMMENT_FRAME_CLEAR_DELAY"] = "The delay after events before drawn frames are cleared (Doesn't effect debug states)"
L["MINIMUM_FRAME_CLEAR_DELAY"] = 5
L["MAXIMUM_FRAME_CLEAR_DELAY"] = 15

L["TITLE_FRAME_SIZE"] = "Frame Render Size"
L["COMMENT_FRAME_SIZE"] = "The size that each frame should render at (Should equal the script's config_size property)"
L["MINIMUM_FRAME_SIZE"] = 5
L["MAXIMUM_FRAME_SIZE"] = 15

L["TITLE_PRIMARY_BUTTON"] = "Primary Button"
L["COMMENT_PRIMARY_BUTTON"] = "The data to show for the Primary Button area of the RPC"

L["TITLE_SECONDARY_BUTTON"] = "Secondary Button"
L["COMMENT_SECONDARY_BUTTON"] = "The data to show for the Secondary Button area of the RPC"

L["TITLE_BUTTON_LABEL"] = "Label"
L["COMMENT_BUTTON_LABEL"] = "The message to show as the label."
L["USAGE_BUTTON_LABEL"] = "<Your message here>"

L["TITLE_BUTTON_URL"] = "Url"
L["COMMENT_BUTTON_URL"] = "The message to show as the url."
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
L["ERROR_COMMAND_UNKNOWN"] = "Unknown Command! (Input: %s)"
L["ADDON_INTRO"] = setfmt("CraftPresence ^%s|r Loaded.\n Use */cp|r or */craftpresence|r for commands.", GREEN, GREY)
L["ADDON_CLOSE"] = "Shutting down Discord Rich Presence..."
L["ADDON_BUILD_INFO"] = "Build Info: %s"

-- Command: /cp placeholders
L["PLACEHOLDERS_QUERY"] = setfmt("Searching for placeholders containing *%s|r...", GREY)
L["PLACEHOLDERS_INTRO"] = setfmt("Available Placeholders (*<key>|r => ^<value>|r):", GREEN, GREY)
L["PLACEHOLDERS_FOUND_NONE"] = setfmt("*No placeholders found within specified parameters|r", RED)
L["PLACEHOLDERS_FOUND_DATA"] = setfmt("*%s|r => ^%s|r", GREEN, GREY)
L["PLACEHOLDERS_NOTE"] = setfmt("NOTE: Keys enclosed by *^|r are global (Can have inner keys),", GREEN, outkey)
L["PLACEHOLDERS_NOTE_TWO"] = setfmt("while ones enclosed by *^|r are inner (Cannot have any other keys)", GREY, inkey)

-- Command: /cp integration
L["INTEGRATION_QUERY"] = setfmt("Enabling integrations for *%s|r...", GREY)
L["INTEGRATION_NOT_FOUND"] = setfmt("*No integrations to enable within specified parameters|r", RED)
L["INTEGRATION_ALREADY_USED"] = setfmt("*Specified integration has already been used|r", RED)

-- Command: /cp clear|clean
L["INFO_COMMAND_CLEAR"] = "Clearing active frame data..."

-- Command: /cp create
L["COMMAND_CREATE_ADDED"] = setfmt("Added custom placeholder *%s|r with the following data: %s", GREEN, GREY)
L["COMMAND_CREATE_OVERRIDE"] = "Specified arguments will replace other placeholders, please use the create:override cmd"
L["COMMAND_CREATE_OVERWRITE"] = "Unable to use specified arguments (Would overwrite an inner/global placeholder)"

-- Command: /cp remove
L["COMMAND_REMOVE_REMOVED"] = setfmt("Removed custom placeholder *%s|r", GREEN)
L["COMMAND_REMOVE_NO_MATCH"] = "No matches found for specified arguments"

-- Config Error Standards
L["ERROR_RANGE_DEFAULT"] = "Sanity Checks failed for %s. Please enter a numerical value between %s and %s."
L["ERROR_FUNCTION_DISABLED"] = "This function (%s) is disabled in this Client Version, please try other methods..."
-- General Command Data
L["USAGE_CMD_INTRO"] = setfmt("*%s|r Command Usage:", GREEN)
L["USAGE_CMD_HELP"] = setfmt(" */cp|r ^help|r or */cp|r ^?|r  -  Displays this helpful menu.", GREEN, GREY)
L["USAGE_CMD_CONFIG"] = setfmt(" */cp|r ^config|r  -  Displays the *ConfigUI|r.", GREEN, GREY)
L["USAGE_CMD_TEST"] = setfmt(" */cp|r ^test|r  -  Toggles debugging of RPC frames.", GREEN, GREY)
L["USAGE_CMD_CLEAN"] = setfmt(" */cp|r ^clean|r or */cp|r ^clear|r  -  Reset addon frames.", GREEN, GREY)
L["USAGE_CMD_UPDATE"] = setfmt(" */cp|r ^update[:force]|r  -  Force an RPC update.", GREEN, GREY)
L["USAGE_CMD_MINIMAP"] = setfmt(" */cp|r ^minimap|r  -  Toggles the minimap button.", GREEN, GREY)
L["USAGE_CMD_STATUS"] = setfmt(" */cp|r ^status|r  -  Views the last sent RPC event.", GREEN, GREY)
L["USAGE_CMD_RESET"] = setfmt(" */cp|r ^reset[:grp,key]|r  -  Reset options in the *ConfigUI|r.", GREEN, GREY)
L["USAGE_CMD_SET"] = setfmt(" */cp|r ^set[:grp,key]|r  -  Set options in the *ConfigUI|r.", GREEN, GREY)
L["USAGE_CMD_INTEGRATION"] = setfmt(" */cp|r ^integration[:query]|r  -  Enable integrations.", GREEN, GREY)
L["USAGE_CMD_CREATE"] = setfmt(" */cp|r ^create [::value_type::][query]|r  -  Create custom data.", GREEN, GREY)
L["USAGE_CMD_REMOVE"] = setfmt(" */cp|r ^remove [query]|r  -  Remove custom placeholder data.", GREEN, GREY)
L["USAGE_CMD_PLACEHOLDERS"] = setfmt(" */cp|r ^placeholders[:query]|r  -  View RPC placeholders.", GREEN, GREY)

L["USAGE_CMD_NOTE"] = setfmt("NOTE: All commands must be prefixed with either */%s|r or */%s|r.", GREEN, GREY)
L["USAGE_CMD_NOTE_TWO"] = setfmt("Optional arguments in commands are represented by *[syntax]|r.", GREEN, GREY)

-- Frame Text Data
L["ADDON_HEADER_CREDITS"] = "Credits"

L["ADDON_INFO_ONE"] = "CraftPresence allows you to customize the way others see you play with Discord Rich Presence."
L["ADDON_INFO_TWO"] = "Created by CDAGaming (https://gitlab.com/CDAGaming)"
L["ADDON_INFO_THREE"] = "Thanks to Attka and wowdim on Github for the original base project, that makes this possible."
L["ADDON_INFO_FOUR"] = "Special thanks to the-emerald/python-discord-rpc and Attk4/wow-discord-rich-presence"

L["ADDON_TOOLTIP_THREE"] = "Click to access config data."
L["ADDON_TOOLTIP_FIVE"] = "Toggle minimap button by typing |c33c9fcff/cp minimap|r"
