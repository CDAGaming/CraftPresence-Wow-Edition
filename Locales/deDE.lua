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
local self = CraftPresence
local L = self.libraries.AceLocale:NewLocale(self.internals.name, "deDE")
if not L then return end

-- Type Identifier Data
L["TYPE_UNKNOWN"] = "Unknown"
L["TYPE_NONE"] = "None"
L["TYPE_ADDED"] = "Hinzugefügt"
L["TYPE_MODIFY"] = "Bearbeitet"
L["STATUS_TRUE"] = "aktiv"
L["STATUS_FALSE"] = "inaktiv"

-- Formatting Data
L["FORMAT_LEVEL"] = "Level %s"
L["FORMAT_SETTING"] = "%s (Sollte %s sein)"
L["FORMAT_COMMENT"] = self:SetFormat("%s|r\n\n*Default:|r %s", self.colors.GREEN)
L["FORMAT_USER_PREFIX"] = "(%s) "

-- Primary Logging Data
L["LOG_DEBUG"] = self:SetFormat("*[Debug]|r %s", self.colors.GREY)
L["LOG_VERBOSE"] = self:SetFormat("*[Verbose]|r %s", self.colors.GREY)
L["LOG_ERROR"] = self:SetFormat("*[Fehler]|r %s", self.colors.RED)
L["LOG_WARNING"] = self:SetFormat("*[Warnung]|r %s", self.colors.GOLD)
L["LOG_INFO"] = "[Info] %s"

-- Config Category Data
L["CATEGORY_TITLE_GENERAL"] = "Allgemein"
L["CATEGORY_COMMENT_GENERAL"] = "Einstellungen für die Anzeige der Rich Presence."

L["CATEGORY_TITLE_PRESENCE"] = "Presence"
L["CATEGORY_TITLE_PRESENCE_EXTENDED"] = "Rich Presence Fields"
L["CATEGORY_COMMENT_PRESENCE"] = "Settings for customizing the general display fields of the rich presence."
L["CATEGORY_COMMENT_PRESENCE_INFO"] = self:SetFormat([[%s rich presence field%s found!

*Note:|r See the ^Buttons|r tab for more settings.]],
        self.colors.GOLD, self.colors.GREEN
)

L["CATEGORY_TITLE_BUTTONS"] = "Buttons"
L["CATEGORY_TITLE_BUTTONS_EXTENDED"] = "Benutzerdefinierte Buttons in Discord"
L["CATEGORY_COMMENT_BUTTONS"] = "Einstellungen für zusätzliche Buttons in Discord."
L["CATEGORY_COMMENT_BUTTONS_INFO"] = "%s benutzerdefinierte Button%s gefunden!"

L["CATEGORY_TITLE_LABELS"] = "Labels"
L["CATEGORY_TITLE_LABELS_EXTENDED"] = "Benutzerdefinierte Label"
L["CATEGORY_COMMENT_LABELS"] = "Einstellungen für benutzerdefinierte Status (bspw. Im Kampf, AFK, o.ä.)."
L["CATEGORY_COMMENT_LABELS_INFO"] = self:SetFormat([[%s Label gefunden!

*Note:|r ^/cp labels|r im Chat für mehr Informationen.]],
        self.colors.GOLD, self.colors.GREEN
)

L["CATEGORY_TITLE_PLACEHOLDERS"] = "Platzhalter"
L["CATEGORY_TITLE_PLACEHOLDERS_EXTENDED"] = "Benutzerdefinierte Platzhalter"
L["CATEGORY_COMMENT_PLACEHOLDERS"] = "Einstellungen für benutzerdefinierte Platzhalter"
L["CATEGORY_COMMENT_PLACEHOLDERS_INFO"] = self:SetFormat([[%s Platzhalter gefunden!

*Note:|r ^/cp placeholders|r im Chat für mehr Informationen.]],
        self.colors.GOLD, self.colors.GREEN
)

L["CATEGORY_TITLE_EVENTS"] = "Ereignisse"
L["CATEGORY_TITLE_EVENTS_EXTENDED"] = "Gefunden Ereignisse"
L["CATEGORY_COMMENT_EVENTS"] = "Einstellungen für benutzerdefinierte Ereignisse, auf dessen Auslösen die Rich Presence aktualisiert wird."
L["CATEGORY_COMMENT_EVENTS_INFO"] = self:SetFormat([[%s Ereigniss(e) gefunden!

*Note:|r ^/cp events|r im Chat für mehr Informationen.]],
        self.colors.GOLD, self.colors.GREEN
)

L["CATEGORY_TITLE_METRICS"] = "Metrics"
L["CATEGORY_TITLE_METRICS_EXTENDED"] = "Available Metric Services"
L["CATEGORY_COMMENT_METRICS"] = "Settings for customizing 3rd party metric data collection."
L["CATEGORY_COMMENT_METRICS_INFO"] = self:SetFormat([[%s metric service%s found!

*Note:|r A ^reload|r may be required for certain settings to take full effect.]],
        self.colors.GOLD, self.colors.GREEN
)

L["CATEGORY_TITLE_EXTRA"] = "Extra"
L["CATEGORY_COMMENT_EXTRA"] = "Zusätzliche Einstellungen"

L["CATEGORY_TITLE_ABOUT"] = "Über"
L["CATEGORY_COMMENT_ABOUT"] = "Informationen zum Addon."
-- Config Variable Data
L["TITLE_CLIENT_ID"] = "Client ID"
L["COMMENT_CLIENT_ID"] = "Discord Client ID um die Rich Presence zu aktivieren."
L["USAGE_CLIENT_ID"] = "<18-digit (or higher) numerical id here>"
L["DEFAULT_CLIENT_ID"] = "805124430774272000"
L["ERROR_CLIENT_ID"] = self:SetFormat("Sanity Checks failed for *Client ID|r. Please enter an ^18-digit (or higher)|r numerical value.",
        self.colors.GREEN, self.colors.GREY
)

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

L["TITLE_ENFORCE_INTERFACE"] = "Enforce Retail Interface"
L["COMMENT_ENFORCE_INTERFACE"] = self:SetFormat([[Toggles whether to allow enforcement of Retail Interface settings.

*Notes:|r
  - This setting is primarily to allow a Retail Interface to display on certain legacy Clients,
such as the original Classic, TBC, and Wrath Clients often now used for private servers.
  - Due to the multitude of legacy addons that are still used in these clients, and as this setting
involves ^resizing|r frames and usage of ^early|r apis, some issues can occur with this option.
  - This option is disabled in non-applicable clients, since it won't do anything in those versions.

*Warning:|r A ^reload|r is required after setting this option to apply it's changes.]],
        self.colors.GOLD, self.colors.RED
)

L["TITLE_OPTIONAL_MIGRATIONS"] = "Optional Migrations"
L["COMMENT_OPTIONAL_MIGRATIONS"] = self:SetFormat([[Toggles whether to allow optional config migrations.

*Warning:|r These types of migrations may ^reset|r certain config values.]],
        self.colors.GOLD, self.colors.RED
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
L["COMMENT_FRAME_SIZE"] = self:SetFormat([[The size that each event frame pixel should be rendered at.

*Note:|r This value should be equal to the python script's ^pixel_size|r property.]],
        self.colors.GOLD, self.colors.GREEN
)
L["MINIMUM_FRAME_SIZE"] = 5
L["MAXIMUM_FRAME_SIZE"] = 15
L["DEFAULT_FRAME_SIZE"] = 6

L["TITLE_STATE"] = "Game State"
L["COMMENT_STATE"] = "The data to be interpreted for the Game State area of the Rich Presence."
L["DEFAULT_STATE_MESSAGE"] = self:SetFormat("*scenario**dungeon**raid**battleground**arena**default*", self.internals.defaultGlobalKey)

L["TITLE_DETAILS"] = "Details"
L["COMMENT_DETAILS"] = "The data to be interpeted for the Details area of the Rich Presence."
L["DEFAULT_DETAILS_MESSAGE"] = self:SetFormat("*player_info*", self.internals.defaultInnerKey)

L["TITLE_LARGEIMAGE"] = "Large Image"
L["COMMENT_LARGEIMAGE"] = "The data to be interpeted for the Large Image area of the Rich Presence."
L["DEFAULT_LARGE_IMAGE_KEY"] = "wow_icon"
L["DEFAULT_LARGE_IMAGE_MESSAGE"] = self:SetFormat("*realm_info*", self.internals.defaultInnerKey)

L["TITLE_SMALLIMAGE"] = "Small Image"
L["COMMENT_SMALLIMAGE"] = "The data to be interpeted for the Small Image area of the Rich Presence."
L["DEFAULT_SMALL_IMAGE_KEY"] = self:SetFormat("*player_alliance*", self.internals.defaultInnerKey)
L["DEFAULT_SMALL_IMAGE_MESSAGE"] = self:SetFormat("*player_alliance*", self.internals.defaultInnerKey)

L["TITLE_PRIMARYBUTTON"] = "Primärer Button"
L["COMMENT_PRIMARYBUTTON"] = "Einstellungen für den ersten Button in Discord."

L["TITLE_SECONDARYBUTTON"] = "Sekundärer Button"
L["COMMENT_SECONDARYBUTTON"] = "Einstellungen für den zweiten Button in Discord"

L["TITLE_TOGGLE_ENABLED"] = "Aktiv"
L["COMMENT_TOGGLE_ENABLED"] = "Ein-/ausschalten dieses Objektes."
L["DEFAULT_TOGGLE_ENABLED"] = "true"

L["TITLE_INPUT_MINIMUMTOC"] = "Minimale TOC-Version"
L["COMMENT_INPUT_MINIMUMTOC"] = "Die minimale Version der TOC, die dieses Objekt benötigt."
L["USAGE_INPUT_MINIMUMTOC"] = "<A 5-digit TOC number or Game Version (x.x.x) here>"
L["DEFAULT_INPUT_MINIMUMTOC"] = "Current Version => " .. self:GetBuildInfo("extended_version")

L["TITLE_INPUT_MAXIMUMTOC"] = "Maximale TOC-Version"
L["COMMENT_INPUT_MAXIMUMTOC"] = "Die maximale Version der TOC, die dieses Objekt erlaubt."
L["USAGE_INPUT_MAXIMUMTOC"] = "<A 5-digit TOC number or Game Version (x.x.x) here>"
L["DEFAULT_INPUT_MAXIMUMTOC"] = "Current Version => " .. self:GetBuildInfo("extended_version")

L["TITLE_TOGGLE_ALLOWREBASEDAPI"] = "Erlaube Rebased APIs"
L["COMMENT_TOGGLE_ALLOWREBASEDAPI"] = "Aktivieren bzw. Deaktivieren der Möglichkeit, diese Option mit Rebased APIs zu verwenden."
L["DEFAULT_TOGGLE_ALLOWREBASEDAPI"] = "false"

L["TITLE_INPUT_PROCESSCALLBACK"] = "Process Callback"
L["COMMENT_INPUT_PROCESSCALLBACK"] = "Eine Funktion, die vor einem im Addon ausgelösten Ereignis ausgeführt wird."
L["USAGE_INPUT_PROCESSCALLBACK"] = "<Eine Funktion oder String-Refernz auf eine Funktion hier>"

L["TITLE_INPUT_PROCESSTYPE"] = "Process Type"
L["COMMENT_INPUT_PROCESSTYPE"] = self:SetFormat("The variable type the *Process Callback|r should be interpreted as.",
        self.colors.GREEN
)
L["USAGE_INPUT_PROCESSTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_ACTIVECALLBACK"] = "Active Callback"
L["COMMENT_INPUT_ACTIVECALLBACK"] = "The function that, if any, is what will be returned when this state is active."
L["USAGE_INPUT_ACTIVECALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_INPUT_ACTIVETYPE"] = "Active Type"
L["COMMENT_INPUT_ACTIVETYPE"] = self:SetFormat("The variable type the *Active Callback|r should be interpreted as.",
        self.colors.GREEN
)
L["USAGE_INPUT_ACTIVETYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_INACTIVECALLBACK"] = "Inactive Callback"
L["COMMENT_INPUT_INACTIVECALLBACK"] = "The function that, if any, is what will be returned when this state is inactive."
L["USAGE_INPUT_INACTIVECALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_INPUT_INACTIVETYPE"] = "Inactive Type"
L["COMMENT_INPUT_INACTIVETYPE"] = self:SetFormat("The variable type the *Inactive Callback|r should be interpreted as.",
        self.colors.GREEN
)
L["USAGE_INPUT_INACTIVETYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_KEYCALLBACK"] = "Key Callback"
L["COMMENT_INPUT_KEYCALLBACK"] = "The function that, if any, is what will be attached as the key for this field."
L["USAGE_INPUT_KEYCALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_INPUT_KEYTYPE"] = "Key Type"
L["COMMENT_INPUT_KEYTYPE"] = self:SetFormat("The variable type the *Key Callback|r should be interpreted as.",
        self.colors.GREEN
)
L["USAGE_INPUT_KEYTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_KEYFORMATCALLBACK"] = "Key Format Callback"
L["COMMENT_INPUT_KEYFORMATCALLBACK"] = self:SetFormat("The function that, if any, will be used for formatting the *Key Callback|r.",
        self.colors.GREEN
)
L["USAGE_INPUT_KEYFORMATCALLBACK"] = "<A function or valid string type for GetCaseData here>"

L["TITLE_INPUT_KEYFORMATTYPE"] = "Key Format Type"
L["COMMENT_INPUT_KEYFORMATTYPE"] = self:SetFormat("The variable type the *Key Format Callback|r should be interpreted as.",
        self.colors.GREEN
)
L["USAGE_INPUT_KEYFORMATTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_MESSAGECALLBACK"] = "Message Callback"
L["COMMENT_INPUT_MESSAGECALLBACK"] = "The function that, if any, is what will be attached as the message for this field."
L["USAGE_INPUT_MESSAGECALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_INPUT_MESSAGETYPE"] = "Message Type"
L["COMMENT_INPUT_MESSAGETYPE"] = self:SetFormat("The variable type the *Message Callback|r should be interpreted as.",
        self.colors.GREEN
)
L["USAGE_INPUT_MESSAGETYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_MESSAGEFORMATCALLBACK"] = "Message Format Callback"
L["COMMENT_INPUT_MESSAGEFORMATCALLBACK"] = self:SetFormat("The function that, if any, will be used for formatting the *Message Callback|r.",
        self.colors.GREEN
)
L["USAGE_INPUT_MESSAGEFORMATCALLBACK"] = "<A function or valid string type for GetCaseData here>"

L["TITLE_INPUT_MESSAGEFORMATTYPE"] = "Message Format Type"
L["COMMENT_INPUT_MESSAGEFORMATTYPE"] = self:SetFormat("The variable type the *Message Format Callback|r should be interpreted as.",
        self.colors.GREEN
)
L["USAGE_INPUT_MESSAGEFORMATTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_LABELCALLBACK"] = "Label Callback"
L["COMMENT_INPUT_LABELCALLBACK"] = "The function that, if any, is what will be attached as the label for the state."
L["USAGE_INPUT_LABELCALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_INPUT_LABELTYPE"] = "Label Type"
L["COMMENT_INPUT_LABELTYPE"] = self:SetFormat("The variable type the *Label Callback|r should be interpreted as.",
        self.colors.GREEN
)
L["USAGE_INPUT_LABELTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_URLCALLBACK"] = "URL Callback"
L["COMMENT_INPUT_URLCALLBACK"] = "The function that, if any, is what will be attached as the url for the state."
L["USAGE_INPUT_URLCALLBACK"] = "<A function or string reference to a function here>"

L["TITLE_INPUT_URLTYPE"] = "URL Type"
L["COMMENT_INPUT_URLTYPE"] = self:SetFormat("The variable type the *URL Callback|r should be interpreted as.",
        self.colors.GREEN
)
L["USAGE_INPUT_URLTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_EVENTCALLBACK"] = "Ereignis Callback"
L["COMMENT_INPUT_EVENTCALLBACK"] = "Eine Funktion, die bei einem vom Spiel ausgelösten Ereignis ausgeführt wird."
L["USAGE_INPUT_EVENTCALLBACK"] = "<Eine Funktion oder String-Refernz auf eine Funktion hier>"

L["TITLE_INPUT_REGISTERCALLBACK"] = "Register Callback"
L["COMMENT_INPUT_REGISTERCALLBACK"] = "Eine Funktion, die bei den Rückgabewerten \"any\" oder \"true\" die Registrierung der Daten erlaubt."
L["USAGE_INPUT_REGISTERCALLBACK"] = "<Eine Boolean-Funktion hier>"

L["TITLE_INPUT_UNREGISTERCALLBACK"] = "Unregister Callback"
L["COMMENT_INPUT_UNREGISTERCALLBACK"] = "The function that, if any, will be executed when the data should be unloaded."
L["USAGE_INPUT_UNREGISTERCALLBACK"] = "<A valid function here>"

L["TITLE_INPUT_STATECALLBACK"] = "State Callback"
L["COMMENT_INPUT_STATECALLBACK"] = "The function that, if any, defines if the data should be considered active or not."
L["USAGE_INPUT_STATECALLBACK"] = "<A valid function here>"

L["TITLE_INPUT_TAGCALLBACK"] = "Tag Callback"
L["COMMENT_INPUT_TAGCALLBACK"] = "Eine Funktion, die es den Daten erlaubt, bestimmte Voraussetzungen zu haben."
L["USAGE_INPUT_TAGCALLBACK"] = "<Eine Funktion oder String-Refernz auf eine Funktion hier>"

L["TITLE_INPUT_TAGTYPE"] = "Tag Type"
L["COMMENT_INPUT_TAGTYPE"] = self:SetFormat("The variable type the *Tag Callback|r should be interpreted as.",
        self.colors.GREEN
)
L["USAGE_INPUT_TAGTYPE"] = "<A variable type name here, can be function|string>"

L["TITLE_INPUT_PREFIX"] = "Prefix"
L["COMMENT_INPUT_PREFIX"] = "Prefix für diese Daten."
L["USAGE_INPUT_PREFIX"] = "<Dein Text hier>"

L["TITLE_INPUT_SUFFIX"] = "Suffix"
L["COMMENT_INPUT_SUFFIX"] = "Suffix für diese Daten."
L["USAGE_INPUT_SUFFIX"] = "<Dein Text hier>"

-- Global Placeholder Defaults
L["DEFAULT_DUNGEON_MESSAGE"] = self:SetFormat("*zone_name* - In *difficulty_info* Dungeon *lockout_encounters*", self.internals.defaultInnerKey)
L["DEFAULT_RAID_MESSAGE"] = self:SetFormat("*zone_name* - In *difficulty_info* Raid *lockout_encounters*", self.internals.defaultInnerKey)
L["DEFAULT_SCENARIO_MESSAGE"] = self:SetFormat("*zone_name* - In *difficulty_info* Szenario *lockout_encounters*", self.internals.defaultInnerKey)
L["DEFAULT_BATTLEGROUND_MESSAGE"] = self:SetFormat("*zone_name* - Auf Schlachtfeld", self.internals.defaultInnerKey)
L["DEFAULT_ARENA_MESSAGE"] = self:SetFormat("*zone_name* - In Arena", self.internals.defaultInnerKey)
L["DEFAULT_FALLBACK_MESSAGE"] = self:SetFormat("*zone_info*", self.internals.defaultInnerKey)

-- Global Label Defaults
L["DEFAULT_LABEL_AWAY"] = "AFK"
L["DEFAULT_LABEL_BUSY"] = "Beschäftigt"
L["DEFAULT_LABEL_DEAD"] = "Tot"
L["DEFAULT_LABEL_GHOST"] = "Geist"
L["DEFAULT_LABEL_COMBAT"] = "Im Kampf"

-- Logging Data
L["VERBOSE_LAST_ENCODED"] = "Letzte gesendete Aktivität => %s"
L["DEBUG_SEND_ACTIVITY"] = "Sende die Aktivität => %s"
L["DEBUG_MAX_BYTES"] = "Maximale Bytes, die gespeichert werden können: %s"
L["DEBUG_VALUE_CHANGED"] = self:SetFormat("*%s|r änderte sich von ^%s|r auf ^%s|r", self.colors.GREEN, self.colors.GREY)
L["INFO_EVENT_SKIPPED"] = self:SetFormat("Ereignis übersprungen:\n Name: *%s|r\n Daten: ^%s|r", self.colors.GREEN, self.colors.GREY)
L["INFO_EVENT_PROCESSING"] = self:SetFormat("Verarbeite Ereignis:\n Name: *%s|r\n Daten: ^%s|r", self.colors.GREEN, self.colors.GREY)
L["INFO_PLACEHOLDER_PROCESSING"] = self:SetFormat("Verarbeite Platzhalter:\n Name: *%s|r\n Daten: ^%s|r", self.colors.GREEN, self.colors.GREY)
L["INFO_RESET_CONFIG"] = "Setze Konfiguration zurück..."
L["INFO_RESET_CONFIG_SINGLE"] = "Resetting Config Data with query => %s"
L["INFO_OUTDATED_CONFIG"] = self:SetFormat("Veraltete Konfiguration erkannt!\n Migriere von Schema *v%s|r zu *v%s|r...", self.colors.GREEN)
L["INFO_OPTIONAL_MIGRATION_DATA_ONE"] = self:SetFormat("Optional Migrations are applicable for Schema *v%s|r to *v%s|r!",
        self.colors.GREEN
)
L["INFO_OPTIONAL_MIGRATION_DATA_TWO"] = self:SetFormat("Please enable *%s|r and run ^/cp config migrate|r to apply them.",
        self.colors.GREEN, self.colors.GREY
)
L["ERROR_MESSAGE_OVERFLOW"] = "An RPC message cannot be processed due to exceeding the maximum bytes allowed (%s/%s)"
L["ERROR_COMMAND_CONFIG"] = "Dir fehlt eine bestimmte Konfiguration um diesen Befehl ausführen zu können. (Aktiviere %s)"
L["ERROR_COMMAND_UNKNOWN"] = "Unbekannter Befehl! (Input: %s)"
L["WARNING_BUILD_UNSUPPORTED"] = self:SetFormat([[You are currently running an *unsupported|r build of CraftPresence!

Detected Version: ^%s|r
Note: This message can be safely ignored if this is a Source Build.]],
        self.colors.RED, self.colors.LIGHT_BLUE
)
L["WARNING_EVENT_RENDERING_ONE"] = "Einige Deiner Einstellungen können unter Umständen Probleme bei der Generierung der Rich-Presence-Daten verursachen. Bitte überprüfe Deine Einstellungen."
L["WARNING_EVENT_RENDERING_TWO"] = "Please check and adjust the following options: %s"
L["ADDON_LOAD_INFO"] = self:SetFormat("^%s|r geladen.\n Nutze */cp|r oder */craftpresence|r für Chatbefehle.", self.colors.GREEN, self.colors.LIGHT_BLUE)
L["ADDON_CLOSE"] = "Beende die Discord Rich Presence..."
L["ADDON_BUILD_INFO"] = "Build Info: %s"

-- Command: /cp placeholders
L["PLACEHOLDERS_NOTE_ONE"] = self:SetFormat("NOTE: Keys enclosed by *^|r are global (Can have inner keys),",
        self.colors.GREEN, self.internals.defaultGlobalKey
)
L["PLACEHOLDERS_NOTE_TWO"] = self:SetFormat("while ones enclosed by *^|r are inner (Cannot have any other keys)",
        self.colors.GREY, self.internals.defaultInnerKey
)

-- Dynamic Data - Access
L["DATA_QUERY"] = self:SetFormat("Suche in %s nach *%s|r...", self.colors.GREY)
L["DATA_FOUND_INTRO"] = self:SetFormat("Gefunden: %s (*<key>|r => ^<value>|r):", self.colors.GREEN, self.colors.GREY)
L["DATA_FOUND_NONE"] = self:SetFormat("*Kein %s innerhalb der Parameter gefunden|r", self.colors.RED)
L["DATA_FOUND_DATA"] = self:SetFormat("*%s|r => ^%s|r", self.colors.GREEN, self.colors.GREY)

-- Dynamic Data - Creation
L["COMMAND_CREATE_SUCCESS"] = self:SetFormat("%s custom tag *%s|r for ^%s|r with the following data: ^%s|r",
        self.colors.GREEN, self.colors.GREY
)
L["COMMAND_CREATE_MODIFY"] = "Die angegebenen Parameter überschreiben bestehende Daten, verwende bitte den create:modify Befehl."

-- Dynamic Data - Removal
L["COMMAND_REMOVE_SUCCESS"] = self:SetFormat("Entfernt: ^%s|r => *%s|r",
        self.colors.GREEN, self.colors.GREY
)
L["COMMAND_REMOVE_NO_MATCH"] = "Keine Übereinstimmung mit diesen Parametern gefunden"

-- Command: /cp integration
L["INTEGRATION_QUERY"] = self:SetFormat("Aktiviere die Integration *%s|r...", self.colors.GREY)
L["INTEGRATION_NOT_FOUND"] = self:SetFormat("*Es konnten keine zu aktivierenden Integrationen mit diesen Parametern gefunden werden.|r", self.colors.RED)
L["INTEGRATION_ALREADY_USED"] = self:SetFormat("*Die angegebene Integration wird bereits verwendet.|r", self.colors.RED)

-- Command: /cp clear|clean
L["COMMAND_CLEAR_SUCCESS"] = "Bereinige den aktuellen Frame..."

-- Command: /cp reset
L["COMMAND_RESET_NOT_FOUND"] = "Config Data matching the following query was not found => %s"

-- Integration: Event Modification
L["COMMAND_EVENT_SUCCESS"] = self:SetFormat("Aktion ^%s|r erfolgreich auf *%s|r mit der Bindung *%s|r durchgeführt.",
        self.colors.GREEN, self.colors.GREY
)
L["COMMAND_EVENT_NO_TRIGGER"] = self:SetFormat("Diese Aktion konnte nicht ausgeführt werden: ^%s|r auf *%s|r (Falscher Trigger))",
        self.colors.GREEN, self.colors.GREY
)

-- Config Error Standards
L["ERROR_RANGE_DEFAULT"] = self:SetFormat("Sanity Checks failed for *%s|r. Please enter a number between ^%s|r and ^%s|r.",
        self.colors.GREEN, self.colors.GREY
)
L["ERROR_STRING_DEFAULT"] = self:SetFormat("Sanity Checks failed for *%s|r. Please enter a valid string.",
        self.colors.GREEN, self.colors.GREY
)

-- Config Warning Standards
L["WARNING_VALUE_UNSAFE"] = self:SetFormat("The value selected for *%s|r can cause malformed behavior in some cases.",
        self.colors.GREEN
)

-- Function Error Standards
L["ERROR_FUNCTION_DISABLED"] = "Diese Funktion (%s) ist in dieser Clientversion deaktiviert. Bitte probiere andere Methoden..."
L["ERROR_FUNCTION_DEPRECATED"] = self:SetFormat("Die markierte Funktion ist als obsolet markiert worden: *%s|r",
        self.colors.GREY
)
L["ERROR_FUNCTION"] = self:SetFormat("Die verwendete Funktion wies einen Fehler auf: *%s|r",
        self.colors.GREY
)
L["TITLE_ATTEMPTED_FUNCTION"] = "Versuchte Funktion"
L["TITLE_REPLACEMENT_FUNCTION"] = "Ersatz Funktion"
L["TITLE_REMOVAL_VERSION"] = "Wird entfernt in Version"
L["TITLE_FUNCTION_MESSAGE"] = "Nachricht"
L["ERROR_FUNCTION_REPLACE"] = self:SetFormat("Um dies zu beheben, nutze bitte eine neuere Version oder probiere */cp reset|r.",
        self.colors.GREY
)

-- General Command Data
L["USAGE_CMD_INTRO"] = "Benutzung =>"
L["USAGE_CMD_HELP"] = self:SetFormat("  */cp|r ^help|r or */cp|r ^?|r  -  Zeigt dieses hilfreiche Menü an.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_CONFIG"] = self:SetFormat("  */cp|r ^config [migrate,standalone]|r  -  Displays/Migrates the *ConfigUI|r.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_CLEAN"] = self:SetFormat("  */cp|r ^clean|r or */cp|r ^clear|r  -  Setzt die Addon-Frames zurück.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_UPDATE"] = self:SetFormat("  */cp|r ^update [debug]|r  -  Forciere oder debugge Rich-Presence-Updates.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_MINIMAP"] = self:SetFormat("  */cp|r ^minimap|r  -  Schaltet das Minimap Symbol um.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_STATUS"] = self:SetFormat("  */cp|r ^status|r  -  Zeigt das letzte gesendete Rich-Presence-Update an.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_RESET"] = self:SetFormat("  */cp|r ^reset [grp,key]|r  -  Setze die Einstellungen im *ConfigUI|r zurück.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_SET"] = self:SetFormat("  */cp|r ^set [grp,key]|r  -  Setze die angegebene Option im *ConfigUI|r.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_INTEGRATION"] = self:SetFormat("  */cp|r ^integration [query]|r  -  Aktiviere Integrationen.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_PLACEHOLDERS"] = self:SetFormat("  */cp|r ^placeholders [create,remove,list][query]|r  -  Verwalte die Platzhalter.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_EVENTS"] = self:SetFormat("  */cp|r ^events [create,remove,list] [query]|r  -  Verwalte die Ereignisse..",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_LABELS"] = self:SetFormat("  */cp|r ^labels [create,remove,list][query]|r  -  Access player labels.",
        self.colors.GREEN, self.colors.GREY
)

L["USAGE_CMD_CREATE_PLACEHOLDERS"] = self:SetFormat("  *Query:|r %s ^[name,minVersion,maxVersion,allowRebasedApi]|r.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_CREATE_EVENTS"] = self:SetFormat("  *Query:|r %s ^[name,minVersion,maxVersion,allowRebasedApi]|r.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_CREATE_LABELS"] = self:SetFormat("  *Query:|r %s ^[name,minVersion,maxVersion,allowRebasedApi]|r.",
        self.colors.GREEN, self.colors.GREY
)

L["USAGE_CMD_REMOVE_PLACEHOLDERS"] = self:SetFormat("  *Query:|r %s ^<name>|r.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_REMOVE_EVENTS"] = self:SetFormat("  *Query:|r %s ^<name>|r.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_REMOVE_LABELS"] = self:SetFormat("  *Query:|r %s ^<name>|r.",
        self.colors.GREEN, self.colors.GREY
)

L["USAGE_CMD_NOTE"] = self:SetFormat([[Notes:
  - Alle Befehle müssen mit  */%s|r oder */%s|r beginnen.
  - Optionale Parameter werden mit der Syntax ^[syntax]|r dargestellt.]],
        self.colors.GREEN, self.colors.GREY
)

-- Frame Text Data
L["ADDON_HEADER_VERSION"] = self:SetFormat("%s *%s|r", self.colors.LIGHT_BLUE)
L["ADDON_HEADER_CREDITS"] = "Credits"

L["ADDON_SUMMARY"] = "CraftPresence allows you to customize the way others see you play with Discord Rich Presence."
L["ADDON_DESCRIPTION"] = self:SetFormat([[Created by *CDAGaming|r (https://gitlab.com/CDAGaming)

Thanks to *Attka|r and *wowdim|r on Github for the original base project, that makes this possible.

Special thanks to *the-emerald/python-discord-rpc|r and *Attk4/wow-discord-rich-presence|r]],
        self.colors.GREEN
)

L["ADDON_TOOLTIP_THREE"] = "Klicke hier um die Addon-Konfiguration zuzugreifen."
L["ADDON_TOOLTIP_FIVE"] = self:SetFormat("Schalte das Minimap-Symbol mit */cp minimap|r um", self.colors.LIGHT_BLUE)
