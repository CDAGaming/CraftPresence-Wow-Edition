--[[
MIT License

Copyright (c) 2018 - 2022 CDAGaming (cstack2011@yahoo.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
--]]

-- Lua APIs
local LibStub = LibStub
local L = LibStub("AceLocale-3.0"):NewLocale("CraftPresence", "enUS", true, "raw")
if not L then return end

-- Addon APIs
local inkey, outkey = "@", "#"
local setfmt = function(str, replacer_one, replacer_two, pattern_one, pattern_two, plain)
    return CraftPresence:SetFormat(str, replacer_one, replacer_two, pattern_one, pattern_two, plain)
end

-- Color Codes
local GREEN = '|cFF00FF7F'
local GREY = '|cfd9b9b9b'
local RED = '|cFFFF6060'
local GOLD = '|cFFFFD700'
local PALE_CYAN = '|c33c9fcff'

-- Data Default Values (DNT)
L["DEFAULT_LABEL_AWAY"] = "Away"
L["DEFAULT_LABEL_BUSY"] = "Busy"
L["DEFAULT_LABEL_DEAD"] = "Dead"
L["DEFAULT_LABEL_GHOST"] = "Ghost"
L["DEFAULT_LABEL_COMBAT"] = "In Combat"
-- Internal Values (DNT)
L["ARRAY_SPLIT_KEY"] = "=="
L["ARRAY_SEPARATOR_KEY"] = "|"
L["ARRAY_SEPARATOR_KEY_ALT"] = "||"
L["DEFAULT_INNER_KEY"] = inkey
L["DEFAULT_GLOBAL_KEY"] = outkey
L["ADDON_NAME"] = "CraftPresence"
L["ADDON_ID"] = "craftpresence"
L["ADDON_AFFIX"] = "cp"
L["COMMAND_CONFIG"] = "cp set"
L["COMMAND_CONFIG_ALT"] = "craftpresence set"
L["FORMAT_LEVEL"] = "Level %s"
L["FORMAT_SETTING"] = "%s (Should be %s)"
L["FORMAT_COMMENT"] = setfmt("%s|r\n\n*Default:|r %s", GREEN)
L["FORMAT_USER_PREFIX"] = "(%s) "
L["EVENT_RPC_TAG"] = "$RPCEvent$"
L["EVENT_RPC_LENGTH"] = 11
L["TYPE_MULTILINE_LENGTH"] = 12
L["TYPE_UNKNOWN"] = "Unknown"
L["TYPE_NONE"] = "None"
L["TYPE_ADDED"] = "Added"
L["TYPE_MODIFY"] = "Modified"
L["STATUS_TRUE"] = "active"
L["STATUS_FALSE"] = "inactive"

-- Primary Logging Data
L["LOG_DEBUG"] = setfmt("*[Debug]|r %s", GREY)
L["LOG_VERBOSE"] = setfmt("*[Verbose]|r %s", GREY)
L["LOG_ERROR"] = setfmt("*[Error]|r %s", RED)
L["LOG_WARNING"] = setfmt("*[Warning]|r %s", GOLD)
L["LOG_INFO"] = "[Info] %s"

-- Config Category Data
L["CATEGORY_TITLE_GENERAL"] = "General"
L["CATEGORY_COMMENT_GENERAL"] = "General settings for display info."

L["CATEGORY_TITLE_PRESENCE"] = "Presence"
L["CATEGORY_TITLE_PRESENCE_EXTENDED"] = "Rich Presence Fields"
L["CATEGORY_COMMENT_PRESENCE"] = "Settings for customizing the general display fields of the rich presence."
L["CATEGORY_COMMENT_PRESENCE_INFO"] = setfmt([[%s rich presence field%s found!

*Note:|r See the ^Buttons|r tab for more settings.]],
        GOLD, GREEN
)

L["CATEGORY_TITLE_BUTTONS"] = "Buttons"
L["CATEGORY_TITLE_BUTTONS_EXTENDED"] = "Custom Buttons"
L["CATEGORY_COMMENT_BUTTONS"] = "Settings for customizing additional button data."
L["CATEGORY_COMMENT_BUTTONS_INFO"] = "%s custom button%s found!"

L["CATEGORY_TITLE_LABELS"] = "Labels"
L["CATEGORY_TITLE_LABELS_EXTENDED"] = "Custom Labels"
L["CATEGORY_COMMENT_LABELS"] = "Settings for customizing unit states (Such as In Combat, Away, etc)."
L["CATEGORY_COMMENT_LABELS_INFO"] = setfmt([[%s label%s found!

*Note:|r Use ^/cp labels|r for more info.]],
        GOLD, GREEN
)

L["CATEGORY_TITLE_PLACEHOLDERS"] = "Placeholders"
L["CATEGORY_TITLE_PLACEHOLDERS_EXTENDED"] = "Custom Placeholders"
L["CATEGORY_COMMENT_PLACEHOLDERS"] = "Settings for customizing placeholder data."
L["CATEGORY_COMMENT_PLACEHOLDERS_INFO"] = setfmt([[%s placeholder%s found!

*Note:|r Use ^/cp placeholders|r for more info.]],
        GOLD, GREEN
)

L["CATEGORY_TITLE_EVENTS"] = "Events"
L["CATEGORY_TITLE_EVENTS_EXTENDED"] = "Available Events"
L["CATEGORY_COMMENT_EVENTS"] = "Settings for customizing events to trigger Rich Presence updates on."
L["CATEGORY_COMMENT_EVENTS_INFO"] = setfmt([[%s event%s found!

*Note:|r Use ^/cp events|r for more info.]],
        GOLD, GREEN
)

L["CATEGORY_TITLE_METRICS"] = "Metrics"
L["CATEGORY_TITLE_METRICS_EXTENDED"] = "Available Metric Services"
L["CATEGORY_COMMENT_METRICS"] = "Settings for customizing 3rd party metric data collection."
L["CATEGORY_COMMENT_METRICS_INFO"] = setfmt([[%s metric service%s found!

*Note:|r A ^reload|r may be required for certain settings to take full effect.]],
        GOLD, GREEN
)

L["CATEGORY_TITLE_EXTRA"] = "Extra"
L["CATEGORY_COMMENT_EXTRA"] = "Extra customization options for addon display info."

L["CATEGORY_TITLE_ABOUT"] = "About"
L["CATEGORY_COMMENT_ABOUT"] = "View information about this addon."
-- Config Variable Data
L["TITLE_CLIENT_ID"] = "Client ID"
L["COMMENT_CLIENT_ID"] = "Client ID used for retrieving assets, icon keys, and titles."
L["USAGE_CLIENT_ID"] = "<18-digit numerical id here>"
L["DEFAULT_CLIENT_ID"] = "805124430774272000"
L["ERROR_CLIENT_ID"] = setfmt("Sanity Checks failed for *Client ID|r. Please enter an ^18-digit|r numerical value.",
        GREEN, GREY
)

L["TITLE_DEBUG_MODE"] = "Debug Mode"
L["COMMENT_DEBUG_MODE"] = "Toggles the display of verbose and more descriptive logging."

L["TITLE_VERBOSE_MODE"] = "Verbose Mode"
L["COMMENT_VERBOSE_MODE"] = "Toggles the display of detailed debugger logging."

L["TITLE_SHOW_MINIMAP_ICON"] = "Show Minimap Icon"
L["COMMENT_SHOW_MINIMAP_ICON"] = "Toggles the display of the minimap icon, used to access the config."

L["TITLE_QUEUED_PIPELINE"] = "Queued Pipeline"
L["COMMENT_QUEUED_PIPELINE"] = "Toggles whether the callback delay will operate in a skip or queue style."

L["TITLE_SHOW_WELCOME_MESSAGE"] = "Show Welcome Message"
L["COMMENT_SHOW_WELCOME_MESSAGE"] = "Toggles the display of the addon's initialization logging."

L["TITLE_OPTIONAL_MIGRATIONS"] = "Optional Migrations"
L["COMMENT_OPTIONAL_MIGRATIONS"] = setfmt([[Toggles whether to allow optional config migrations.

*Warning:|r These types of migrations may ^reset|r certain config values.]],
        GOLD, RED
)

L["TITLE_CALLBACK_DELAY"] = "Callback Delay"
L["COMMENT_CALLBACK_DELAY"] = "The delay (in seconds) after event firing before non-forced RPC updates trigger."
L["MINIMUM_CALLBACK_DELAY"] = 0
L["MAXIMUM_CALLBACK_DELAY"] = 5
L["DEFAULT_CALLBACK_DELAY"] = 1

L["TITLE_FRAME_CLEAR_DELAY"] = "Frame Clear Delay"
L["COMMENT_FRAME_CLEAR_DELAY"] = "The delay (in seconds) after non-debug events before drawn frames are cleared."
L["MINIMUM_FRAME_CLEAR_DELAY"] = 3
L["MAXIMUM_FRAME_CLEAR_DELAY"] = 15
L["DEFAULT_FRAME_CLEAR_DELAY"] = 5

L["TITLE_FRAME_SIZE"] = "Frame Render Size"
L["COMMENT_FRAME_SIZE"] = setfmt([[The size that each event frame pixel should be rendered at.

*Note:|r This value should be equal to the python script's ^pixel_size|r property.]],
        GOLD, GREEN
)
L["MINIMUM_FRAME_SIZE"] = 5
L["MAXIMUM_FRAME_SIZE"] = 15
L["DEFAULT_FRAME_SIZE"] = 6

L["TITLE_STATE"] = "Game State"
L["COMMENT_STATE"] = "The data to be interpreted for the Game State area of the Rich Presence."
L["DEFAULT_STATE_MESSAGE"] = setfmt("*scenario**dungeon**raid**battleground**arena**default*", outkey)

L["TITLE_DETAILS"] = "Details"
L["COMMENT_DETAILS"] = "The data to be interpeted for the Details area of the Rich Presence."
L["DEFAULT_DETAILS_MESSAGE"] = setfmt("*player_info*", inkey)

L["TITLE_LARGEIMAGE"] = "Large Image"
L["COMMENT_LARGEIMAGE"] = "The data to be interpeted for the Large Image area of the Rich Presence."
L["DEFAULT_LARGE_IMAGE_KEY"] = "wow_icon"
L["DEFAULT_LARGE_IMAGE_MESSAGE"] = setfmt("*realm_info*", inkey)

L["TITLE_SMALLIMAGE"] = "Small Image"
L["COMMENT_SMALLIMAGE"] = "The data to be interpeted for the Small Image area of the Rich Presence."
L["DEFAULT_SMALL_IMAGE_KEY"] = setfmt("*player_alliance*", inkey)
L["DEFAULT_SMALL_IMAGE_MESSAGE"] = setfmt("*player_alliance*", inkey)

L["TITLE_PRIMARYBUTTON"] = "Primary Button"
L["COMMENT_PRIMARYBUTTON"] = "The data to be interpreted for the Primary Button area of the Rich Presence."

L["TITLE_SECONDARYBUTTON"] = "Secondary Button"
L["COMMENT_SECONDARYBUTTON"] = "The data to be interpreted for the Secondary Button area of the Rich Presence."

L["TITLE_TOGGLE_ENABLED"] = "Enabled"
L["COMMENT_TOGGLE_ENABLED"] = "Whether this data should be used."

L["TITLE_INPUT_MINIMUMTOC"] = "Minimum TOC"
L["COMMENT_INPUT_MINIMUMTOC"] = "The minimum TOC version to register and use this data with."
L["USAGE_INPUT_MINIMUMTOC"] = "<A 5-digit TOC number or Game Version (x.x.x) here>"
L["DEFAULT_INPUT_MINIMUMTOC"] = "CurrentTOC (From GetBuildInfo)"

L["TITLE_INPUT_MAXIMUMTOC"] = "Maximum TOC"
L["COMMENT_INPUT_MAXIMUMTOC"] = "The maximum TOC version to register and use this data with."
L["USAGE_INPUT_MAXIMUMTOC"] = "<A 5-digit TOC number or Game Version (x.x.x) here>"
L["DEFAULT_INPUT_MAXIMUMTOC"] = "CurrentTOC (From GetBuildInfo)"

L["TITLE_TOGGLE_ALLOWREBASEDAPI"] = "Allow Rebased API"
L["COMMENT_TOGGLE_ALLOWREBASEDAPI"] = "Whether to use this data with rebased api client versions."
L["DEFAULT_TOGGLE_ALLOWREBASEDAPI"] = "false"

L["TITLE_INPUT_PROCESSCALLBACK"] = "Process Callback"
L["COMMENT_INPUT_PROCESSCALLBACK"] = "The function that, if any, is called before the event is triggered in the addon."
L["USAGE_INPUT_PROCESSCALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_INPUT_PROCESSTYPE"] = "Process Type"
L["COMMENT_INPUT_PROCESSTYPE"] = setfmt("The variable type the *Process Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_INPUT_PROCESSTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_ACTIVECALLBACK"] = "Active Callback"
L["COMMENT_INPUT_ACTIVECALLBACK"] = "The function that, if any, is what will be returned when this state is active."
L["USAGE_INPUT_ACTIVECALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_INPUT_ACTIVETYPE"] = "Active Type"
L["COMMENT_INPUT_ACTIVETYPE"] = setfmt("The variable type the *Active Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_INPUT_ACTIVETYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_INACTIVECALLBACK"] = "Inactive Callback"
L["COMMENT_INPUT_INACTIVECALLBACK"] = "The function that, if any, is what will be returned when this state is inactive."
L["USAGE_INPUT_INACTIVECALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_INPUT_INACTIVETYPE"] = "Inactive Type"
L["COMMENT_INPUT_INACTIVETYPE"] = setfmt("The variable type the *Inactive Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_INPUT_INACTIVETYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_KEYCALLBACK"] = "Key Callback"
L["COMMENT_INPUT_KEYCALLBACK"] = "The function that, if any, is what will be attached as the key for this field."
L["USAGE_INPUT_KEYCALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_INPUT_KEYTYPE"] = "Key Type"
L["COMMENT_INPUT_KEYTYPE"] = setfmt("The variable type the *Key Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_INPUT_KEYTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_KEYFORMATCALLBACK"] = "Key Format Callback"
L["COMMENT_INPUT_KEYFORMATCALLBACK"] = setfmt("The function that, if any, will be used for formatting the *Key Callback|r.",
        GREEN
)
L["USAGE_INPUT_KEYFORMATCALLBACK"] = "<A function or valid string type for GetCaseData here>"

L["TITLE_INPUT_KEYFORMATTYPE"] = "Key Format Type"
L["COMMENT_INPUT_KEYFORMATTYPE"] = setfmt("The variable type the *Key Format Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_INPUT_KEYFORMATTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_MESSAGECALLBACK"] = "Message Callback"
L["COMMENT_INPUT_MESSAGECALLBACK"] = "The function that, if any, is what will be attached as the message for this field."
L["USAGE_INPUT_MESSAGECALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_INPUT_MESSAGETYPE"] = "Message Type"
L["COMMENT_INPUT_MESSAGETYPE"] = setfmt("The variable type the *Message Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_INPUT_MESSAGETYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_MESSAGEFORMATCALLBACK"] = "Message Format Callback"
L["COMMENT_INPUT_MESSAGEFORMATCALLBACK"] = setfmt("The function that, if any, will be used for formatting the *Message Callback|r.",
        GREEN
)
L["USAGE_INPUT_MESSAGEFORMATCALLBACK"] = "<A function or valid string type for GetCaseData here>"

L["TITLE_INPUT_MESSAGEFORMATTYPE"] = "Message Format Type"
L["COMMENT_INPUT_MESSAGEFORMATTYPE"] = setfmt("The variable type the *Message Format Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_INPUT_MESSAGEFORMATTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_LABELCALLBACK"] = "Label Callback"
L["COMMENT_INPUT_LABELCALLBACK"] = "The function that, if any, is what will be attached as the label for the state."
L["USAGE_INPUT_LABELCALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_INPUT_LABELTYPE"] = "Label Type"
L["COMMENT_INPUT_LABELTYPE"] = setfmt("The variable type the *Label Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_INPUT_LABELTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_URLCALLBACK"] = "URL Callback"
L["COMMENT_INPUT_URLCALLBACK"] = "The function that, if any, is what will be attached as the url for the state."
L["USAGE_INPUT_URLCALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_INPUT_URLTYPE"] = "URL Type"
L["COMMENT_INPUT_URLTYPE"] = setfmt("The variable type the *URL Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_INPUT_URLTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_EVENTCALLBACK"] = "Event Callback"
L["COMMENT_INPUT_EVENTCALLBACK"] = "The function that, if any, will trigger when the game event is called."
L["USAGE_INPUT_EVENTCALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_INPUT_REGISTERCALLBACK"] = "Register Callback"
L["COMMENT_INPUT_REGISTERCALLBACK"] = "The function that, if any and true, will allow the data to be registered."
L["USAGE_INPUT_REGISTERCALLBACK"] = "<A boolean function here>"

L["TITLE_INPUT_UNREGISTERCALLBACK"] = "Unregister Callback"
L["COMMENT_INPUT_UNREGISTERCALLBACK"] = "The function that, if any, will be executed when the data should be unloaded."
L["USAGE_INPUT_UNREGISTERCALLBACK"] = "<A valid function here>"

L["TITLE_INPUT_STATECALLBACK"] = "State Callback"
L["COMMENT_INPUT_STATECALLBACK"] = "The function that, if any, defines if the data should be considered active or not."
L["USAGE_INPUT_STATECALLBACK"] = "<A valid function here>"

L["TITLE_INPUT_TAGCALLBACK"] = "Tag Callback"
L["COMMENT_INPUT_TAGCALLBACK"] = "The function that, if any, will allow the data to have conditionals."
L["USAGE_INPUT_TAGCALLBACK"] = "<A string or string function here>"

L["TITLE_INPUT_TAGTYPE"] = "Tag Type"
L["COMMENT_INPUT_TAGTYPE"] = setfmt("The variable type the *Tag Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_INPUT_TAGTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_PREFIX"] = "Prefix"
L["COMMENT_INPUT_PREFIX"] = "The prefix to be used for this data."
L["USAGE_INPUT_PREFIX"] = "<Your message here>"

L["TITLE_INPUT_SUFFIX"] = "Suffix"
L["COMMENT_INPUT_SUFFIX"] = "The suffix to be used for this data."
L["USAGE_INPUT_SUFFIX"] = "<Your message here>"

-- Global Placeholder Defaults
L["DEFAULT_DUNGEON_MESSAGE"] = setfmt("*zone_name* - In *difficulty_info* Dungeon *lockout_encounters*", inkey)
L["DEFAULT_RAID_MESSAGE"] = setfmt("*zone_name* - In *difficulty_info* Raid *lockout_encounters*", inkey)
L["DEFAULT_SCENARIO_MESSAGE"] = setfmt("*zone_name* - In *difficulty_info* Scenario *lockout_encounters*", inkey)
L["DEFAULT_BATTLEGROUND_MESSAGE"] = setfmt("*zone_name* - In Battleground", inkey)
L["DEFAULT_ARENA_MESSAGE"] = setfmt("*zone_name* - In Arena", inkey)
L["DEFAULT_FALLBACK_MESSAGE"] = setfmt("*zone_info*", inkey)

-- Logging Data
L["VERBOSE_LAST_ENCODED"] = "Last sent activity => %s"
L["DEBUG_SEND_ACTIVITY"] = "Sending activity => %s"
L["DEBUG_MAX_BYTES"] = "Max bytes that can be stored: %s"
L["DEBUG_VALUE_CHANGED"] = setfmt("*%s|r changed from ^%s|r to ^%s|r", GREEN, GREY)
L["INFO_EVENT_SKIPPED"] = setfmt("Event Skipped:\n Name: *%s|r\n Data: ^%s|r", GREEN, GREY)
L["INFO_EVENT_PROCESSING"] = setfmt("Event Processing:\n Name: *%s|r\n Data: ^%s|r", GREEN, GREY)
L["INFO_PLACEHOLDER_PROCESSING"] = setfmt("Placeholder Processing:\n Name: *%s|r\n Data: ^%s|r", GREEN, GREY)
L["INFO_RESET_CONFIG"] = "Resetting Config Data..."
L["INFO_RESET_CONFIG_SINGLE"] = "Resetting Config Data with query => %s"
L["INFO_OUTDATED_CONFIG"] = setfmt("Outdated Config Detected!\n Migrating from Schema *v%s|r to *v%s|r...", GREEN)
L["INFO_OPTIONAL_MIGRATION_DATA_ONE"] = setfmt("Optional Migrations are applicable for Schema *v%s|r to *v%s|r!",
        GREEN
)
L["INFO_OPTIONAL_MIGRATION_DATA_TWO"] = setfmt("Please enable *%s|r and run ^/cp config migrate|r to apply them.",
        GREEN, GREY
)
L["ERROR_MESSAGE_OVERFLOW"] = "An RPC message cannot be processed due to exceeding the maximum bytes allowed (%s/%s)"
L["ERROR_COMMAND_CONFIG"] = "You are missing the required config option to access this command (Enable %s)"
L["ERROR_COMMAND_UNKNOWN"] = "Unknown Command! (Input: %s)"
L["WARNING_BUILD_UNSUPPORTED"] = "You are running an unsupported build of CraftPresence (%s)! (Ignore if Source Build)"
L["WARNING_EVENT_RENDERING_ONE"] = "Some of your Game Settings may interfere with Rich Presence Event Generation"
L["WARNING_EVENT_RENDERING_TWO"] = "Please check and adjust the following options: %s"
L["ADDON_LOAD_INFO"] = setfmt("%s Loaded.\n Use */cp|r or */craftpresence|r for commands.", GREEN, GREY)
L["ADDON_CLOSE"] = "Shutting down Discord Rich Presence..."
L["ADDON_BUILD_INFO"] = "Build Info: %s"

-- Command: /cp placeholders
L["PLACEHOLDERS_NOTE_ONE"] = setfmt("NOTE: Keys enclosed by *^|r are global (Can have inner keys),",
        GREEN, outkey
)
L["PLACEHOLDERS_NOTE_TWO"] = setfmt("while ones enclosed by *^|r are inner (Cannot have any other keys)",
        GREY, inkey
)

-- Dynamic Data - Access
L["DATA_QUERY"] = setfmt("Searching for %s containing *%s|r...", GREY)
L["DATA_FOUND_INTRO"] = setfmt("Available %s (*<key>|r => ^<value>|r):", GREEN, GREY)
L["DATA_FOUND_NONE"] = setfmt("*No %s found within specified parameters|r", RED)
L["DATA_FOUND_DATA"] = setfmt("*%s|r => ^%s|r", GREEN, GREY)

-- Dynamic Data - Creation
L["COMMAND_CREATE_SUCCESS"] = setfmt("%s custom tag *%s|r for ^%s|r with the following data: ^%s|r",
        GREEN, GREY
)
L["COMMAND_CREATE_MODIFY"] = "Specified arguments will replace other data, please use the create:modify cmd"

-- Dynamic Data - Removal
L["COMMAND_REMOVE_SUCCESS"] = setfmt("Removed key within ^%s|r => *%s|r",
        GREEN, GREY
)
L["COMMAND_REMOVE_NO_MATCH"] = "No matches found for specified arguments"

-- Command: /cp integration
L["INTEGRATION_QUERY"] = setfmt("Enabling integrations for *%s|r...", GREY)
L["INTEGRATION_NOT_FOUND"] = setfmt("*No integrations to enable within specified parameters|r", RED)
L["INTEGRATION_ALREADY_USED"] = setfmt("*Specified integration has already been used|r", RED)

-- Command: /cp clear|clean
L["COMMAND_CLEAR_SUCCESS"] = "Clearing active frame data..."

-- Command: /cp reset
L["COMMAND_RESET_NOT_FOUND"] = "Config Data matching the following query was not found => %s"

-- Integration: Event Modification
L["COMMAND_EVENT_SUCCESS"] = setfmt("Successfully performed operation ^%s|r on *%s|r with binding *%s|r",
        GREEN, GREY
)
L["COMMAND_EVENT_NO_TRIGGER"] = setfmt("Unable to perform operation ^%s|r on *%s|r (Invalid trigger)",
        GREEN, GREY
)

-- Config Error Standards
L["ERROR_RANGE_DEFAULT"] = setfmt("Sanity Checks failed for *%s|r. Please enter a number between ^%s|r and ^%s|r.",
        GREEN, GREY
)
L["ERROR_STRING_DEFAULT"] = setfmt("Sanity Checks failed for *%s|r. Please enter a valid string.",
        GREEN, GREY
)

-- Config Warning Standards
L["WARNING_VALUE_UNSAFE"] = setfmt("The value selected for *%s|r can cause malformed behavior in some cases.",
        GREEN
)

-- Function Error Standards
L["ERROR_FUNCTION_DISABLED"] = "This function (%s) is disabled in this Client Version, please try other methods..."
L["ERROR_FUNCTION_DEPRECATED"] = setfmt("A function your using is marked as deprecated, with the following info: *%s|r",
        GREY
)
L["ERROR_FUNCTION"] = setfmt("A function your using has encountered an error, with the following info: *%s|r",
        GREY
)
L["TITLE_ATTEMPTED_FUNCTION"] = "Attempted Function"
L["TITLE_REPLACEMENT_FUNCTION"] = "Replacement Function"
L["TITLE_REMOVAL_VERSION"] = "Removal Version"
L["TITLE_FUNCTION_MESSAGE"] = "Message"
L["ERROR_FUNCTION_REPLACE"] = setfmt("To fix this issue, please use the newer function or do */cp reset|r if unsure",
        GREY
)

-- General Command Data
L["USAGE_CMD_INTRO"] = "Command Usage =>"
L["USAGE_CMD_HELP"] = setfmt(" */cp|r ^help|r or */cp|r ^?|r  -  Displays this helpful menu.",
        GREEN, GREY
)
L["USAGE_CMD_CONFIG"] = setfmt(" */cp|r ^config [migrate]|r  -  Displays/Migrates the *ConfigUI|r.",
        GREEN, GREY
)
L["USAGE_CMD_CLEAN"] = setfmt(" */cp|r ^clean|r or */cp|r ^clear|r  -  Reset addon frames.",
        GREEN, GREY
)
L["USAGE_CMD_UPDATE"] = setfmt(" */cp|r ^update [debug]|r  -  Force or Debug a Rich Presence update.",
        GREEN, GREY
)
L["USAGE_CMD_MINIMAP"] = setfmt(" */cp|r ^minimap|r  -  Toggles the minimap button.",
        GREEN, GREY
)
L["USAGE_CMD_STATUS"] = setfmt(" */cp|r ^status|r  -  Views the last sent Rich Presence event.",
        GREEN, GREY
)
L["USAGE_CMD_RESET"] = setfmt(" */cp|r ^reset [grp,key]|r  -  Reset options in the *ConfigUI|r.",
        GREEN, GREY
)
L["USAGE_CMD_SET"] = setfmt(" */cp|r ^set [grp,key]|r  -  Set options in the *ConfigUI|r.",
        GREEN, GREY
)
L["USAGE_CMD_INTEGRATION"] = setfmt(" */cp|r ^integration [query]|r  -  Enable integrations.",
        GREEN, GREY
)
L["USAGE_CMD_PLACEHOLDERS"] = setfmt(" */cp|r ^placeholders [create,remove,list][query]|r  -  Access placeholders.",
        GREEN, GREY
)
L["USAGE_CMD_EVENTS"] = setfmt(" */cp|r ^events [create,remove,list] [query]|r  -  Access events.",
        GREEN, GREY
)
L["USAGE_CMD_LABELS"] = setfmt(" */cp|r ^labels [create,remove,list][query]|r  -  Access player labels.",
        GREEN, GREY
)

L["USAGE_CMD_CREATE_PLACEHOLDERS"] = setfmt(" *Query:|r %s ^[name,minVersion,maxVersion,allowRebasedApi]|r.",
        GREEN, GREY
)
L["USAGE_CMD_CREATE_EVENTS"] = setfmt(" *Query:|r %s ^[name,minVersion,maxVersion,allowRebasedApi]|r.",
        GREEN, GREY
)
L["USAGE_CMD_CREATE_LABELS"] = setfmt(" *Query:|r %s ^[name,minVersion,maxVersion,allowRebasedApi]|r.",
        GREEN, GREY
)

L["USAGE_CMD_REMOVE_PLACEHOLDERS"] = setfmt(" *Query:|r %s ^<name>|r.",
        GREEN, GREY
)
L["USAGE_CMD_REMOVE_EVENTS"] = setfmt(" *Query:|r %s ^<name>|r.",
        GREEN, GREY
)
L["USAGE_CMD_REMOVE_LABELS"] = setfmt(" *Query:|r %s ^<name>|r.",
        GREEN, GREY
)

L["USAGE_CMD_NOTE_ONE"] = setfmt("NOTE: All commands must be prefixed with either */%s|r or */%s|r.",
        GREEN, GREY
)
L["USAGE_CMD_NOTE_TWO"] = setfmt("Optional arguments in commands are represented by *[syntax]|r.",
        GREEN, GREY
)

-- Frame Text Data
L["ADDON_HEADER_VERSION"] = setfmt("%s *%s|r", PALE_CYAN)
L["ADDON_HEADER_CREDITS"] = "Credits"

L["ADDON_SUMMARY"] = "CraftPresence allows you to customize the way others see you play with Discord Rich Presence."
L["ADDON_DESCRIPTION"] = setfmt([[Created by *CDAGaming|r (https://gitlab.com/CDAGaming)

Thanks to *Attka|r and *wowdim|r on Github for the original base project, that makes this possible.

Special thanks to *the-emerald/python-discord-rpc|r and *Attk4/wow-discord-rich-presence|r]],
        GREEN
)

L["ADDON_TOOLTIP_THREE"] = "Click to access config data."
L["ADDON_TOOLTIP_FIVE"] = setfmt("Toggle minimap button by typing */cp minimap|r", PALE_CYAN)
