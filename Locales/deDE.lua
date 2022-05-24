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
local L = LibStub("AceLocale-3.0"):NewLocale("CraftPresence", "deDE")
if not L then return end

-- Addon APIs
local CP_GlobalUtils = CP_GlobalUtils
local inkey, outkey = "@", "#"
local setfmt = function(str, replacer_one, replacer_two, pattern_one, pattern_two, plain)
    return CP_GlobalUtils:SetFormat(str, replacer_one, replacer_two, pattern_one, pattern_two, plain)
end

-- Color Codes
local GREEN = '|cFF00FF7F'
local GREY = '|cfd9b9b9b'
local RED = '|cFFFF6060'
local GOLD = '|cFFFFD700'
local PALE_CYAN = '|c33c9fcff'

-- Data Default Values (DNT)
L["DEFAULT_LABEL_AWAY"] = "AFK"
L["DEFAULT_LABEL_BUSY"] = "Beschäftigt"
L["DEFAULT_LABEL_DEAD"] = "Tot"
L["DEFAULT_LABEL_GHOST"] = "Geist"
L["DEFAULT_LABEL_COMBAT"] = "Im Kampf"
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
L["FORMAT_SETTING"] = "%s (Sollte %s sein)"
L["FORMAT_COMMENT"] = setfmt("%s|r\n\n*Default:|r %s", GREEN)
L["FORMAT_USER_PREFIX"] = "(%s) "
L["EVENT_RPC_TAG"] = "$RPCEvent$"
L["EVENT_RPC_LENGTH"] = 11
L["TYPE_MULTILINE_LENGTH"] = 12
L["TYPE_UNKNOWN"] = "Unknown"
L["TYPE_NONE"] = "None"
L["TYPE_ADDED"] = "Hinzugefügt"
L["TYPE_MODIFY"] = "Bearbeitet"
L["STATUS_TRUE"] = "aktiv"
L["STATUS_FALSE"] = "inaktiv"

-- Primary Logging Data
L["LOG_DEBUG"] = setfmt("*[Debug]|r %s", GREY)
L["LOG_VERBOSE"] = setfmt("*[Verbose]|r %s", GREY)
L["LOG_ERROR"] = setfmt("*[Fehler]|r %s", RED)
L["LOG_WARNING"] = setfmt("*[Warnung]|r %s", GOLD)
L["LOG_INFO"] = "[Info] %s"

-- Config Category Data
L["CATEGORY_TITLE_GENERAL"] = "Allgemein"
L["CATEGORY_COMMENT_GENERAL"] = "Einstellungen für die Anzeige der Rich Presence."

L["CATEGORY_TITLE_BUTTONS"] = "Buttons"
L["CATEGORY_TITLE_BUTTONS_EXTENDED"] = "Benutzerdefinierte Buttons in Discord"
L["CATEGORY_COMMENT_BUTTONS"] = "Einstellungen für zusätzliche Buttons in Discord."
L["CATEGORY_COMMENT_BUTTONS_INFO"] = "%s benutzerdefinierte Button%s gefunden!"

L["CATEGORY_TITLE_LABELS"] = "Labels"
L["CATEGORY_TITLE_LABELS_EXTENDED"] = "Benutzerdefinierte Label"
L["CATEGORY_COMMENT_LABELS"] = "Einstellungen für benutzerdefinierte Status (bspw. Im Kampf, AFK, o.ä.)."
L["CATEGORY_COMMENT_LABELS_INFO"] = "%s Label gefunden! (/cp labels im Chat für mehr Informationen)."

L["CATEGORY_TITLE_PLACEHOLDERS"] = "Platzhalter"
L["CATEGORY_TITLE_PLACEHOLDERS_EXTENDED"] = "Benutzerdefinierte Platzhalter"
L["CATEGORY_COMMENT_PLACEHOLDERS"] = "Einstellungen für benutzerdefinierte Platzhalter"
L["CATEGORY_COMMENT_PLACEHOLDERS_INFO"] = "%s Platzhalter gefunden! (/cp placeholders im Chat für mehr Informationen)."

L["CATEGORY_TITLE_EVENTS"] = "Ereignisse"
L["CATEGORY_TITLE_EVENTS_EXTENDED"] = "Gefunden Ereignisse"
L["CATEGORY_COMMENT_EVENTS"] = "Einstellungen für benutzerdefinierte Ereignisse, auf dessen Auslösen die Rich Presence aktualisiert wird."
L["CATEGORY_COMMENT_EVENTS_INFO"] = "%s Ereigniss(e) gefunden! (/cp events im Chat für mehr Informationen)."

L["CATEGORY_TITLE_METRICS"] = "Metrics"
L["CATEGORY_TITLE_METRICS_EXTENDED"] = "Available Metric Services"
L["CATEGORY_COMMENT_METRICS"] = "Settings for customizing 3rd party metric data collection."
L["CATEGORY_COMMENT_METRICS_INFO"] = "%s metric service%s found! (Reload Required to apply changes)."

L["CATEGORY_TITLE_EXTRA"] = "Extra"
L["CATEGORY_COMMENT_EXTRA"] = "Zusätzliche Einstellungen"

L["CATEGORY_TITLE_ABOUT"] = "Über"
L["CATEGORY_COMMENT_ABOUT"] = "Informationen zum Addon."
-- Config Variable Data
L["TITLE_CLIENT_ID"] = "Client ID"
L["COMMENT_CLIENT_ID"] = "Discord Client ID um die Rich Presence zu aktivieren."
L["USAGE_CLIENT_ID"] = "<18-digit numerical id here>"
L["DEFAULT_CLIENT_ID"] = "805124430774272000"
L["ERROR_CLIENT_ID"] = setfmt("Sanity Checks failed for *Client ID|r. Please enter an ^18-digit|r numerical value.",
        GREEN, GREY
)

L["TITLE_GAME_STATE_MESSAGE"] = "Status"
L["COMMENT_GAME_STATE_MESSAGE"] = "Der Statustext, der im Statusbereich angezeigt wird."
L["USAGE_GAME_STATE_MESSAGE"] = "<Dein Statustext hier>"
L["DEFAULT_GAME_STATE_MESSAGE"] = setfmt("*scenario**dungeon**raid**battleground**arena**default*", outkey)

L["TITLE_DETAILS_MESSAGE"] = "Details"
L["COMMENT_DETAILS_MESSAGE"] = "Der Statustext, der im Detailbereich angezeigt wird."
L["USAGE_DETAILS_MESSAGE"] = "<Dein Statustext hier>"
L["DEFAULT_DETAILS_MESSAGE"] = setfmt("*player_info*", inkey)

L["TITLE_LARGE_IMAGE_KEY"] = "Dateiname eines großen Anzeigebildes"
L["COMMENT_LARGE_IMAGE_KEY"] = "Dateiname des Anzeigebildes, der als großes Anzeigebild im Status angezeigt werden soll."
L["USAGE_LARGE_IMAGE_KEY"] = "<Dateiname hier>"
L["DEFAULT_LARGE_IMAGE_KEY"] = "wow_icon"

L["TITLE_SMALL_IMAGE_KEY"] = "Dateiname eines kleinen Anzeigebildes"
L["COMMENT_SMALL_IMAGE_KEY"] = "Dateiname des Anzeigebildes, der als kleines Anzeigebild im Status angezeigt werden soll."
L["USAGE_SMALL_IMAGE_KEY"] = "<Dateiname hier>"
L["DEFAULT_SMALL_IMAGE_KEY"] = setfmt("*player_alliance*", inkey)

L["ERROR_IMAGE_KEY"] = setfmt("Sanity Checks failed for *Image Key|r. Please enter a string ^<= 32|r letters long.",
        GREEN, GREY
)

L["TITLE_LARGE_IMAGE_MESSAGE"] = "Nachricht über dem großen Anzeigebild"
L["COMMENT_LARGE_IMAGE_MESSAGE"] = "Nachricht, die beim Mouseover über das große Anzeigebild erscheinen soll."
L["USAGE_LARGE_IMAGE_MESSAGE"] = "<Deine Nachricht hier>"
L["DEFAULT_LARGE_IMAGE_MESSAGE"] = setfmt("*realm_info*", inkey)

L["TITLE_SMALL_IMAGE_MESSAGE"] = "Nachricht über dem kleinen Anzeigebild"
L["COMMENT_SMALL_IMAGE_MESSAGE"] = "Nachricht, die beim Mouseover über das kleine Anzeigebild erscheinen soll."
L["USAGE_SMALL_IMAGE_MESSAGE"] = "<Deine Nachricht hier>"
L["DEFAULT_SMALL_IMAGE_MESSAGE"] = setfmt("*player_alliance*", inkey)

L["TITLE_DEBUG_MODE"] = "Debug Modus"
L["COMMENT_DEBUG_MODE"] = "Schaltet die Anzeige von erweiterten (Debug-)Nachrichten im Chat ein/aus."

L["TITLE_VERBOSE_MODE"] = "Verbose Modus"
L["COMMENT_VERBOSE_MODE"] = "Schaltet die Anzeige von erweiterten (Verbose-)Nachrichten im Chat ein/aus."

L["TITLE_SHOW_MINIMAP_ICON"] = "Zeige Minimap Symbol"
L["COMMENT_SHOW_MINIMAP_ICON"] = "Schaltet das Minimap Symbol ein/aus, über das diese Konfiguration aufgerufen werden kann."

L["TITLE_QUEUED_PIPELINE"] = "Queued Pipeline"
L["COMMENT_QUEUED_PIPELINE"] = "Schaltet die Ereignis-Pipeline zwischen \"skip\" und \"queue\" um."

L["TITLE_SHOW_WELCOME_MESSAGE"] = "Zeige Willkommensnachricht"
L["COMMENT_SHOW_WELCOME_MESSAGE"] = "Schaltet die initiale Nachricht beim Anmelden ein/aus."

L["TITLE_OPTIONAL_MIGRATIONS"] = "Optional Migrations"
L["COMMENT_OPTIONAL_MIGRATIONS"] = setfmt([[Toggles whether to allow optional config migrations.

*Warning:|r These types of migrations may reset certain config values.]],
        GOLD
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

L["TITLE_FRAME_SIZE"] = "Größe der Frames"
L["COMMENT_FRAME_SIZE"] = setfmt([[The size that each event frame pixel should be rendered at.

*Note:|r This value should be equal to the python script's ^pixel_size|r property.]],
        GOLD, GREEN
)
L["MINIMUM_FRAME_SIZE"] = 5
L["MAXIMUM_FRAME_SIZE"] = 15
L["DEFAULT_FRAME_SIZE"] = 6

L["TITLE_PRIMARYBUTTON"] = "Primärer Button"
L["COMMENT_PRIMARYBUTTON"] = "Einstellungen für den ersten Button in Discord."

L["TITLE_SECONDARYBUTTON"] = "Sekundärer Button"
L["COMMENT_SECONDARYBUTTON"] = "Einstellungen für den zweiten Button in Discord"

L["TITLE_BUTTON_LABEL"] = "Label"
L["COMMENT_BUTTON_LABEL"] = "Der Text, der auf dem Button erscheinen soll."
L["USAGE_BUTTON_LABEL"] = "<Dein Text hier>"

L["TITLE_BUTTON_URL"] = "Url"
L["COMMENT_BUTTON_URL"] = "Die URL, die über den Button geöffnet werden soll."
L["USAGE_BUTTON_URL"] = "<Deine URL hier>"

L["TITLE_BUTTON_ACTIVE"] = "Statustext (wenn aktiv)"
L["COMMENT_BUTTON_ACTIVE"] = "Der anzuzeigende Text, wenn der Status aktiv ist."
L["USAGE_BUTTON_ACTIVE"] = "<Dein Text hier>"

L["TITLE_BUTTON_INACTIVE"] = "Statustext (wenn inaktiv)"
L["COMMENT_BUTTON_INACTIVE"] = "Der anzuzeigende Text, wenn der Status inaktiv ist."
L["USAGE_BUTTON_INACTIVE"] = "<Dein Text hier>"

L["TITLE_BUTTON_ENABLED"] = "Aktiv"
L["COMMENT_BUTTON_ENABLED"] = "Ein-/ausschalten dieses Objektes."

L["TITLE_BUTTON_MINIMUMTOC"] = "Minimale TOC-Version"
L["COMMENT_BUTTON_MINIMUMTOC"] = "Die minimale Version der TOC, die dieses Objekt benötigt."
L["USAGE_BUTTON_MINIMUMTOC"] = "<A 5-digit TOC number or Game Version (x.x.x) here>"
L["DEFAULT_BUTTON_MINIMUMTOC"] = "CurrentTOC (From GetBuildInfo)"

L["TITLE_BUTTON_MAXIMUMTOC"] = "Maximale TOC-Version"
L["COMMENT_BUTTON_MAXIMUMTOC"] = "Die maximale Version der TOC, die dieses Objekt erlaubt."
L["USAGE_BUTTON_MAXIMUMTOC"] = "<A 5-digit TOC number or Game Version (x.x.x) here>"
L["DEFAULT_BUTTON_MAXIMUMTOC"] = "CurrentTOC (From GetBuildInfo)"

L["TITLE_BUTTON_ALLOWREBASEDAPI"] = "Erlaube Rebased APIs"
L["COMMENT_BUTTON_ALLOWREBASEDAPI"] = "Aktivieren bzw. Deaktivieren der Möglichkeit, diese Option mit Rebased APIs zu verwenden."
L["DEFAULT_BUTTON_ALLOWREBASEDAPI"] = "false"

L["TITLE_BUTTON_PROCESSCALLBACK"] = "Process Callback"
L["COMMENT_BUTTON_PROCESSCALLBACK"] = "Eine Funktion, die vor einem im Addon ausgelösten Ereignis ausgeführt wird."
L["USAGE_BUTTON_PROCESSCALLBACK"] = "<Eine Funktion oder String-Refernz auf eine Funktion hier>"

L["TITLE_BUTTON_PROCESSTYPE"] = "Process Type"
L["COMMENT_BUTTON_PROCESSTYPE"] = setfmt("The variable type the *Process Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_BUTTON_PROCESSTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_BUTTON_ACTIVECALLBACK"] = "Active Callback"
L["COMMENT_BUTTON_ACTIVECALLBACK"] = "The function that, if any, is what will be returned when this state is active."
L["USAGE_BUTTON_ACTIVECALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_BUTTON_ACTIVETYPE"] = "Active Type"
L["COMMENT_BUTTON_ACTIVETYPE"] = setfmt("The variable type the *Active Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_BUTTON_ACTIVETYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_BUTTON_INACTIVECALLBACK"] = "Inactive Callback"
L["COMMENT_BUTTON_INACTIVECALLBACK"] = "The function that, if any, is what will be returned when this state is inactive."
L["USAGE_BUTTON_INACTIVECALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_BUTTON_INACTIVETYPE"] = "Inactive Type"
L["COMMENT_BUTTON_INACTIVETYPE"] = setfmt("The variable type the *Inactive Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_BUTTON_INACTIVETYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_BUTTON_LABELCALLBACK"] = "Label Callback"
L["COMMENT_BUTTON_LABELCALLBACK"] = "The function that, if any, is what will be attached as the label for the state."
L["USAGE_BUTTON_LABELCALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_BUTTON_LABELTYPE"] = "Label Type"
L["COMMENT_BUTTON_LABELTYPE"] = setfmt("The variable type the *Label Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_BUTTON_LABELTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_BUTTON_URLCALLBACK"] = "URL Callback"
L["COMMENT_BUTTON_URLCALLBACK"] = "The function that, if any, is what will be attached as the url for the state."
L["USAGE_BUTTON_URLCALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_BUTTON_URLTYPE"] = "URL Type"
L["COMMENT_BUTTON_URLTYPE"] = setfmt("The variable type the *URL Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_BUTTON_URLTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_BUTTON_EVENTCALLBACK"] = "Ereignis Callback"
L["COMMENT_BUTTON_EVENTCALLBACK"] = "Eine Funktion, die bei einem vom Spiel ausgelösten Ereignis ausgeführt wird."
L["USAGE_BUTTON_EVENTCALLBACK"] = "<Eine Funktion oder String-Refernz auf eine Funktion hier>"

L["TITLE_BUTTON_REGISTERCALLBACK"] = "Register Callback"
L["COMMENT_BUTTON_REGISTERCALLBACK"] = "Eine Funktion, die bei den Rückgabewerten \"any\" oder \"true\" die Registrierung der Daten erlaubt."
L["USAGE_BUTTON_REGISTERCALLBACK"] = "<Eine Boolean-Funktion hier>"

L["TITLE_BUTTON_STATECALLBACK"] = "State Callback"
L["COMMENT_BUTTON_STATECALLBACK"] = "The function that, if any, defines if the data should be considered active or not."
L["USAGE_BUTTON_STATECALLBACK"] = "<A boolean function here>"

L["TITLE_BUTTON_TAGCALLBACK"] = "Tag Callback"
L["COMMENT_BUTTON_TAGCALLBACK"] = "Eine Funktion, die es den Daten erlaubt, bestimmte Voraussetzungen zu haben."
L["USAGE_BUTTON_TAGCALLBACK"] = "<Eine Funktion oder String-Refernz auf eine Funktion hier>"

L["TITLE_BUTTON_TAGTYPE"] = "Tag Type"
L["COMMENT_BUTTON_TAGTYPE"] = setfmt("The variable type the *Tag Callback|r should be interpreted as.",
        GREEN
)
L["USAGE_BUTTON_TAGTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_BUTTON_PREFIX"] = "Prefix"
L["COMMENT_BUTTON_PREFIX"] = "Prefix für diese Daten."
L["USAGE_BUTTON_PREFIX"] = "<Dein Text hier>"

L["TITLE_BUTTON_SUFFIX"] = "Suffix"
L["COMMENT_BUTTON_SUFFIX"] = "Suffix für diese Daten."
L["USAGE_BUTTON_SUFFIX"] = "<Dein Text hier>"

-- Global Placeholder Defaults
L["DEFAULT_DUNGEON_MESSAGE"] = setfmt("*zone_name* - In *difficulty_info* Dungeon *lockout_encounters*", inkey)
L["DEFAULT_RAID_MESSAGE"] = setfmt("*zone_name* - In *difficulty_info* Raid *lockout_encounters*", inkey)
L["DEFAULT_SCENARIO_MESSAGE"] = setfmt("*zone_name* - In *difficulty_info* Szenario *lockout_encounters*", inkey)
L["DEFAULT_BATTLEGROUND_MESSAGE"] = setfmt("*zone_name* - Auf Schlachtfeld", inkey)
L["DEFAULT_ARENA_MESSAGE"] = setfmt("*zone_name* - In Arena", inkey)
L["DEFAULT_FALLBACK_MESSAGE"] = setfmt("*zone_info*", inkey)

-- Logging Data
L["VERBOSE_LAST_ENCODED"] = "Letzte gesendete Aktivität => %s"
L["DEBUG_SEND_ACTIVITY"] = "Sende die Aktivität => %s"
L["DEBUG_MAX_BYTES"] = "Maximale Bytes, die gespeichert werden können: %s"
L["DEBUG_VALUE_CHANGED"] = setfmt("*%s|r änderte sich von ^%s|r auf ^%s|r", GREEN, GREY)
L["INFO_EVENT_SKIPPED"] = setfmt("Ereignis übersprungen:\n Name: *%s|r\n Daten: ^%s|r", GREEN, GREY)
L["INFO_EVENT_PROCESSING"] = setfmt("Verarbeite Ereignis:\n Name: *%s|r\n Daten: ^%s|r", GREEN, GREY)
L["INFO_PLACEHOLDER_PROCESSING"] = setfmt("Verarbeite Platzhalter:\n Name: *%s|r\n Daten: ^%s|r", GREEN, GREY)
L["INFO_RESET_CONFIG"] = "Setze Konfiguration zurück..."
L["INFO_OUTDATED_CONFIG"] = setfmt("Veraltete Konfiguration erkannt!\n Migriere von Schema *v%s|r zu *v%s|r...", GREEN)
L["INFO_OPTIONAL_MIGRATION_DATA_ONE"] = setfmt("Optional Migrations are applicable for Schema *v%s|r to *v%s|r!",
        GREEN
)
L["INFO_OPTIONAL_MIGRATION_DATA_TWO"] = setfmt("Please enable *%s|r and run ^/cp config migrate|r to apply them.",
        GREEN, GREY
)
L["ERROR_MESSAGE_OVERFLOW"] = "An RPC message cannot be processed due to exceeding the maximum bytes allowed (%s/%s)"
L["ERROR_COMMAND_CONFIG"] = "Dir fehlt eine bestimmte Konfiguration um diesen Befehl ausführen zu können. (Aktiviere %s)"
L["ERROR_COMMAND_UNKNOWN"] = "Unbekannter Befehl! (Input: %s)"
L["WARNING_BUILD_UNSUPPORTED"] = "Du nutzt eine nicht unterstützte Version von CraftPresence (%s)! (Ignoriere dies, wenn Du die Variante von GitLab verwendest)"
L["WARNING_EVENT_RENDERING_ONE"] = "Einige Deiner Einstellungen können unter Umständen Probleme bei der Generierung der Rich-Presence-Daten verursachen. Bitte überprüfe Deine Einstellungen."
L["WARNING_EVENT_RENDERING_TWO"] = "Please check and adjust the following options: %s"
L["ADDON_LOAD_INFO"] = setfmt("%s geladen.\n Nutze */cp|r oder */craftpresence|r für Chatbefehle.", GREEN, GREY)
L["ADDON_CLOSE"] = "Beende die Discord Rich Presence..."
L["ADDON_BUILD_INFO"] = "Build Info: %s"

-- Command: /cp placeholders
L["PLACEHOLDERS_NOTE_ONE"] = setfmt("NOTE: Keys enclosed by # are global (Can have inner keys)",
        GREEN, outkey
)
L["PLACEHOLDERS_NOTE_TWO"] = setfmt("while ones enclosed by *^|r are inner (Cannot have any other keys)",
        GREY, inkey
)

-- Dynamic Data - Access
L["DATA_QUERY"] = setfmt("Suche in %s nach *%s|r...", GREY)
L["DATA_FOUND_INTRO"] = setfmt("Gefunden: %s (*<key>|r => ^<value>|r):", GREEN, GREY)
L["DATA_FOUND_NONE"] = setfmt("*Kein %s innerhalb der Parameter gefunden|r", RED)
L["DATA_FOUND_DATA"] = setfmt("*%s|r => ^%s|r", GREEN, GREY)

-- Dynamic Data - Creation
L["COMMAND_CREATE_SUCCESS"] = setfmt("%s custom tag *%s|r for ^%s|r with the following data: ^%s|r",
        GREEN, GREY
)
L["COMMAND_CREATE_MODIFY"] = "Die angegebenen Parameter überschreiben bestehende Daten, verwende bitte den create:modify Befehl."

-- Dynamic Data - Removal
L["COMMAND_REMOVE_SUCCESS"] = setfmt("Entfernt: ^%s|r => *%s|r",
        GREEN, GREY
)
L["COMMAND_REMOVE_NO_MATCH"] = "Keine Übereinstimmung mit diesen Parametern gefunden"

-- Command: /cp integration
L["INTEGRATION_QUERY"] = setfmt("Aktiviere die Integration *%s|r...", GREY)
L["INTEGRATION_NOT_FOUND"] = setfmt("*Es konnten keine zu aktivierenden Integrationen mit diesen Parametern gefunden werden.|r", RED)
L["INTEGRATION_ALREADY_USED"] = setfmt("*Die angegebene Integration wird bereits verwendet.|r", RED)

-- Command: /cp clear|clean
L["COMMAND_CLEAR_SUCCESS"] = "Bereinige den aktuellen Frame..."

-- Integration: Event Modification
L["COMMAND_EVENT_SUCCESS"] = setfmt("Aktion ^%s|r erfolgreich auf *%s|r mit der Bindung *%s|r durchgeführt.",
        GREEN, GREY
)
L["COMMAND_EVENT_NO_TRIGGER"] = setfmt("Diese Aktion konnte nicht ausgeführt werden: ^%s|r auf *%s|r (Falscher Trigger))",
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
L["ERROR_FUNCTION_DISABLED"] = "Diese Funktion (%s) ist in dieser Clientversion deaktiviert. Bitte probiere andere Methoden..."
L["ERROR_FUNCTION_DEPRECATED"] = setfmt("Die markierte Funktion ist als obsolet markiert worden: *%s|r",
        GREY
)
L["ERROR_FUNCTION"] = setfmt("Die verwendete Funktion wies einen Fehler auf: *%s|r",
        GREY
)
L["TITLE_ATTEMPTED_FUNCTION"] = "Versuchte Funktion"
L["TITLE_REPLACEMENT_FUNCTION"] = "Ersatz Funktion"
L["TITLE_REMOVAL_VERSION"] = "Wird entfernt in Version"
L["TITLE_FUNCTION_MESSAGE"] = "Nachricht"
L["ERROR_FUNCTION_REPLACE"] = setfmt("Um dies zu beheben, nutze bitte eine neuere Version oder probiere */cp reset|r.",
        GREY
)

-- General Command Data
L["USAGE_CMD_INTRO"] = "Benutzung =>"
L["USAGE_CMD_HELP"] = setfmt(" */cp|r ^help|r or */cp|r ^?|r  -  Zeigt dieses hilfreiche Menü an.",
        GREEN, GREY
)
L["USAGE_CMD_CONFIG"] = setfmt(" */cp|r ^config [migrate]|r  -  Displays/Migrates the *ConfigUI|r.",
        GREEN, GREY
)
L["USAGE_CMD_CLEAN"] = setfmt(" */cp|r ^clean|r or */cp|r ^clear|r  -  Setzt die Addon-Frames zurück.",
        GREEN, GREY
)
L["USAGE_CMD_UPDATE"] = setfmt(" */cp|r ^update [debug]|r  -  Forciere oder debugge Rich-Presence-Updates.",
        GREEN, GREY
)
L["USAGE_CMD_MINIMAP"] = setfmt(" */cp|r ^minimap|r  -  Schaltet das Minimap Symbol um.",
        GREEN, GREY
)
L["USAGE_CMD_STATUS"] = setfmt(" */cp|r ^status|r  -  Zeigt das letzte gesendete Rich-Presence-Update an.",
        GREEN, GREY
)
L["USAGE_CMD_RESET"] = setfmt(" */cp|r ^reset [grp,key]|r  -  Setze die Einstellungen im *ConfigUI|r zurück.",
        GREEN, GREY
)
L["USAGE_CMD_SET"] = setfmt(" */cp|r ^set [grp,key]|r  -  Setze die angegebene Option im *ConfigUI|r.",
        GREEN, GREY
)
L["USAGE_CMD_INTEGRATION"] = setfmt(" */cp|r ^integration [query]|r  -  Aktiviere Integrationen.",
        GREEN, GREY
)
L["USAGE_CMD_PLACEHOLDERS"] = setfmt(" */cp|r ^placeholders [create,remove,list][query]|r  -  Verwalte die Platzhalter.",
        GREEN, GREY
)
L["USAGE_CMD_EVENTS"] = setfmt(" */cp|r ^events [create,remove,list] [query]|r  -  Verwalte die Ereignisse..",
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

L["USAGE_CMD_NOTE_ONE"] = setfmt("NOTE: Alle Befehle müssen mit  */%s|r oder */%s|r beginnen.",
        GREEN, GREY
)
L["USAGE_CMD_NOTE_TWO"] = setfmt("Optionale Parameter werden mit der Syntax *[syntax]|r dargestellt.",
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

L["ADDON_TOOLTIP_THREE"] = "Klicke hier um die Addon-Konfiguration zuzugreifen."
L["ADDON_TOOLTIP_FIVE"] = setfmt("Schalte das Minimap-Symbol mit */cp minimap|r um", PALE_CYAN)
