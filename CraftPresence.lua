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
local strformat, strlower, strupper = string.format, string.lower, string.upper
local tinsert, tremove, tconcat = table.insert, table.remove, table.concat
local pairs, type, max, unpack = pairs, type, math.max, unpack
local tostring = tostring

local tsetn = function(t, n)
    setmetatable(t, { __len = function()
        return n
    end })
end

local wipe = (table.wipe or function(table)
    for k, _ in pairs(table) do
        table[k] = nil
    end
    tsetn(table, 0)
    return table
end)

local function copyTable(src, dest)
    if type(dest) ~= "table" then
        dest = {}
    else
        wipe(dest)
    end
    if type(src) == "table" then
        for k, v in pairs(src) do
            if type(v) == "table" then
                -- try to index the key first so that the metatable creates the defaults, if set, and use that table
                v = copyTable(v, dest[k])
            end
            dest[k] = v
        end
    end
    return dest
end

-- Addon APIs
local LibStub = LibStub

CraftPresence = LibStub("AceAddon-3.0"):NewAddon("CraftPresence", "AceConsole-3.0", "AceEvent-3.0")
CraftPresence.locale = LibStub("AceLocale-3.0"):GetLocale("CraftPresence")
CraftPresence.registeredEvents = {}
CraftPresence.defaultEventCallback = ""
CraftPresence.placeholders = {}
CraftPresence.buttons = {}
CraftPresence.labels = {}

CraftPresence.time_start = ""
CraftPresence.time_end = ""

CraftPresence.canUseExternals = false
CraftPresence.externalCache = {}

-- Addon Dependencies
CraftPresence.config = LibStub("AceConfigDialog-3.0")

-- Analytical Dependencies
CraftPresence.WagoAnalytics = nil

local L = CraftPresence.locale
local CP_GlobalUtils = CP_GlobalUtils

-- Critical Data (DNT)
local CraftPresenceLDB, icon
local lastEventName
local minimapState = { hide = false }
-- Build and Integration Data
local addOnData = {}
local buildData = {}
local compatData = {}
local activeIntegrations = {}
local isRebasedApi = false

--- Creates and encodes a new RPC event from placeholder and conditional data
---
--- @param log_output boolean Whether to allow logging for this function (Default: false)
---
--- @return string, table @ newEncodedString, args
function CraftPresence:EncodeConfigData(log_output)
    -- Primary Variable Data
    log_output = self:GetOrDefault(log_output, false)
    -- Secondary Variable Data
    local rpcData = {
        self:GetFromDb("clientId"),
        { self:GetFromDb("largeImageKey"), "icon" },
        { self:GetFromDb("largeImageMessage"), "no-dupes" },
        { self:GetFromDb("smallImageKey"), "icon" },
        { self:GetFromDb("smallImageMessage"), "no-dupes" },
        { self:GetFromDb("detailsMessage"), "no-dupes" },
        { self:GetFromDb("gameStateMessage"), "no-dupes" }
    }

    -- Placeholder Syncing
    rpcData = self:SyncDynamicData(log_output, rpcData)

    -- Update Instance Status before exiting method
    if self:HasInstanceChanged() then
        self:SetInstanceChanged(false)
    end
    return self:ConcatTable(L["EVENT_RPC_TAG"], L["ARRAY_SEPARATOR_KEY"], unpack(rpcData))
end

--- Instructions to be called when the addon is loaded
function CraftPresence:OnInitialize()
    -- Main Initialization
    addOnData = self:GetAddOnInfo()
    buildData = self:GetBuildInfo()
    compatData = self:GetCompatibilityInfo()
    isRebasedApi = self:IsRebasedApi()
    -- Options Initialization
    self.db = LibStub("AceDB-3.0"):New(L["ADDON_NAME"] .. "DB", self:GetDefaults())
    LibStub("AceConfig-3.0"):RegisterOptionsTable(L["ADDON_NAME"], self.getOptionsTable, {
        (L["COMMAND_CONFIG"]), (L["COMMAND_CONFIG_ALT"])
    })
    self.config:SetDefaultSize(L["ADDON_NAME"], 858, 660)
    self:EnsureCompatibility(
            self:GetFromDb("schema"), addOnData["schema"], false,
            self:GetFromDb("optionalMigrations")
    )
    -- Analytics Initialization, if allowed
    self:InitializeAnalytics()
    -- Version-Specific Registration
    if buildData["toc_version"] >= compatData["1.12.1"] then
        -- UI Registration
        if buildData["toc_version"] >= compatData["2.0.0"] or isRebasedApi then
            self.optionsFrame = self.config:AddToBlizOptions(L["ADDON_NAME"])
            self.optionsFrame.default = function()
                self:UpdateProfile(true, true, "all")
            end
        end
        -- Icon Registration
        icon = LibStub("LibDBIcon-1.0")
        CraftPresenceLDB = LibStub:GetLibrary("LibDataBroker-1.1"):NewDataObject(L["ADDON_NAME"], {
            type = "launcher",
            text = L["ADDON_NAME"],
            icon = strformat("Interface\\Addons\\%s\\Images\\icon.blp", L["ADDON_NAME"]),
            OnClick = function(_, _)
                self:ShowConfig()
            end,
            OnTooltipShow = function(tt)
                tt:AddLine(addOnData["versionString"])
                tt:AddLine(" ")
                tt:AddLine(L["ADDON_TOOLTIP_THREE"])
                tt:AddLine(" ")
                tt:AddLine(L["ADDON_TOOLTIP_FIVE"])
            end
        })
        self:UpdateMinimapState(false, false)
        icon:Register(L["ADDON_NAME"], CraftPresenceLDB, minimapState)
    end
end

--- Instructions to be called when the addon is enabled
function CraftPresence:OnEnable()
    -- Print and set any Initial Data, if allowed
    if self:GetFromDb("showWelcomeMessage") then
        self:PrintAddonInfo()
    end
    -- Command Registration
    self:RegisterChatCommand(L["ADDON_ID"], "ChatCommand")
    self:RegisterChatCommand(L["ADDON_AFFIX"], "ChatCommand")
    -- Create initial frames and initial rpc update
    self:CreateFrames(self:GetFromDb("frameSize"))
    self:PaintMessageWait()
    self:UpdateMinimapState(true, false)
    -- Register Universal Events
    if buildData["toc_version"] >= compatData["2.0.0"] or isRebasedApi then
        self.defaultEventCallback = "DispatchUpdate"
    else
        self.defaultEventCallback = "DispatchLegacyUpdate"
    end
    self:SyncEvents(self:GetFromDb("events"), self:GetFromDb("verboseMode"))
end

--- Instructions to be called when the addon is disabled
function CraftPresence:OnDisable()
    -- Print Closing Message
    self:Print(L["ADDON_CLOSE"])
    -- Command Un-registration
    self:UnregisterChatCommand(L["ADDON_ID"])
    self:UnregisterChatCommand(L["ADDON_AFFIX"])
    -- Reset RPC Data to Discord
    local resetData = self:ConcatTable(L["EVENT_RPC_TAG"], L["ARRAY_SEPARATOR_KEY"], self:GetFromDb("clientId"))
    self:PaintMessageWait(true, false, true, resetData)
    -- Hide Minimap Icon
    if icon then
        icon:Hide(L["ADDON_NAME"])
    end
    -- Un-register all active events
    -- Note: SyncEvents is not used here so that manually added events are also properly cleared
    self:ModifyTriggers(self.registeredEvents, nil, "remove", self:GetFromDb("verboseMode"))
end

--- Sync the contents of registeredEvents with the specified event table
--- (This is primarily a helper function towards ModifyTriggers, retrofitted to use an event table)
---
--- @param grp table The table of events to interpret (Should match that of self.db.profile.events)
--- @param log_output boolean Whether to allow logging for this function (Default: false)
function CraftPresence:SyncEvents(grp, log_output)
    grp = self:GetOrDefault(grp, {})
    log_output = self:GetOrDefault(log_output, false)
    for eventName, eventData in pairs(grp) do
        local shouldEnable = self:ShouldProcessData(eventData)
        self:ModifyTriggers(eventName, eventData, (shouldEnable and "" or "remove"), log_output)
    end
end

--- Interprets the contents of a dynamic data table to ensure validity and obtain enable level
---
--- @param data table The data table to interpret (Required)
---
--- @return boolean, boolean @ shouldEnable, shouldRegister
function CraftPresence:ShouldProcessData(data)
    local shouldEnable, shouldRegister = false, false

    if type(data) == "table" then
        local currentTOC = buildData["toc_version"]
        local fallbackTOC = buildData["fallback_toc_version"]

        local minTOC = self:GetOrDefault(data.minimumTOC, fallbackTOC)
        if type(minTOC) ~= "number" then
            minTOC = self:VersionToBuild(minTOC)
        end
        local maxTOC = self:GetOrDefault(data.maximumTOC, currentTOC)
        if type(maxTOC) ~= "number" then
            maxTOC = self:VersionToBuild(maxTOC)
        end

        shouldRegister = (
                self:IsNullOrEmpty(data.registerCallback) or
                        tostring(self:GetDynamicReturnValue(data.registerCallback, "function", self)) == "true"
        )
        local canAccept = shouldRegister and (
                self:IsWithinValue(
                        currentTOC, minTOC, maxTOC, true, true, false
                ) or (data.allowRebasedApi and self:IsRebasedApi())
        )
        shouldEnable = data.enabled and canAccept
    end
    return shouldEnable, shouldRegister
end

--- Sync the contents of dynamic data for readable usage
---
--- @param log_output boolean Whether to allow logging for this function (Default: false)
--- @param data table If supplied, do a sequential replace of applicable data
---
--- @return table @ data_table
function CraftPresence:SyncDynamicData(log_output, data)
    local isVerbose, isDebug = self:GetFromDb("verboseMode"), self:GetFromDb("debugMode")
    copyTable(self:GetFromDb("placeholders"), self.placeholders)
    copyTable(self:GetFromDb("buttons"), self.buttons)
    copyTable(self:GetFromDb("labels"), self.labels)
    for key, value in pairs(self.placeholders) do
        if type(value) == "table" then
            -- Sanity Checks
            local keyPrefix = self:GetOrDefault(value.prefix)
            local keySuffix = self:GetOrDefault(value.suffix)
            local newKey = keyPrefix .. key .. keySuffix
            local newValue, tagValue = "", ""

            if self:ShouldProcessData(value) then
                -- Print logging info if needed, before processing
                local logPrefix = L["INFO_PLACEHOLDER_PROCESSING"]
                -- Logging is different depending on verbose/debug states
                local logTemplate = (isVerbose and L["LOG_VERBOSE"]) or
                        (isDebug and L["LOG_DEBUG"]) or
                        (log_output and L["LOG_INFO"]) or nil
                local logData = not self:IsNullOrEmpty(value.processCallback) and (
                        (isVerbose and value.processCallback) or "<...>"
                ) or L["TYPE_NONE"]
                if not self:IsNullOrEmpty(logTemplate) and log_output then
                    self:Print(strformat(
                            logTemplate, strformat(
                                    logPrefix, newKey, self:GetOrDefault(logData, L["TYPE_UNKNOWN"])
                            )
                    ))
                end
                newValue = self:GetDynamicReturnValue(value.processCallback, value.processType, self)
                tagValue = self:GetDynamicReturnValue(value.tagCallback, value.tagType, self)
            end

            -- Main Parsing into valid RPC data
            -- (A secondary placeholder scan is done here to ensure proper replacement, due to unpredictable ordering)
            for subKey, subValue in pairs(self.placeholders) do
                local replacement = self:Replace(subValue.processCallback, newKey, self:GetOrDefault(newValue), true)
                if subValue.processCallback ~= replacement then
                    self.placeholders[subKey].processCallback = replacement
                    if subKey == key then
                        newValue = self:GetDynamicReturnValue(replacement, value.processType, self)
                    end
                end
            end

            -- Sync Button Info
            for buttonKey, buttonValue in pairs(self.buttons) do
                if self:ShouldProcessData(buttonValue) then
                    buttonValue.labelCallback = self:Replace(buttonValue.labelCallback, newKey, self:GetOrDefault(newValue), true)
                    buttonValue.label = self:GetDynamicReturnValue(buttonValue.labelCallback, buttonValue.labelType, self)
                    buttonValue.urlCallback = self:Replace(buttonValue.urlCallback, newKey, self:GetOrDefault(newValue), true)
                    buttonValue.url = self:GetDynamicReturnValue(buttonValue.urlCallback, buttonValue.urlType, self)
                    buttonValue.result = self:ConcatTable(
                            nil, L["ARRAY_SPLIT_KEY"],
                            self:GetOrDefault(buttonValue.label), self:GetOrDefault(buttonValue.url)
                    )
                end
                buttonValue.result = self:GetOrDefault(buttonValue.result)
                self.buttons[buttonKey] = buttonValue
            end

            -- Sync Label Info
            for labelKey, labelValue in pairs(self.labels) do
                if self:ShouldProcessData(labelValue) then
                    labelValue.activeCallback = self:Replace(labelValue.activeCallback, newKey, self:GetOrDefault(newValue), true)
                    labelValue.active = self:GetDynamicReturnValue(labelValue.activeCallback, labelValue.activeType, self)
                    labelValue.inactiveCallback = self:Replace(labelValue.inactiveCallback, newKey, self:GetOrDefault(newValue), true)
                    labelValue.inactive = self:GetDynamicReturnValue(labelValue.inactiveCallback, labelValue.inactiveType, self)
                    labelValue.stateCallback = self:Replace(labelValue.stateCallback, newKey, self:GetOrDefault(newValue), true)
                    labelValue.state = tostring(self:GetDynamicReturnValue(labelValue.stateCallback, "function", self)) == "true"
                end
                self.labels[labelKey] = labelValue
            end

            if data ~= nil then
                data = self:SetFormats({ newValue, nil, newKey, nil },
                        data, true, false
                )

                -- Time Condition Data Setup
                if not self:IsNullOrEmpty(tagValue) then
                    if self:FindMatches(tagValue, "time", false) then
                        if self:FindMatches(tagValue, "time:start", false) then
                            if self:HasInstanceChanged() then
                                self.time_start = "generated"
                            else
                                self.time_start = "last"
                            end
                        elseif self:FindMatches(tagValue, "time:end", false) then
                            if self:HasInstanceChanged() then
                                self.time_end = "generated"
                            else
                                self.time_end = "last"
                            end
                        end
                    end
                end
            end
        end
    end

    if data ~= nil then
        -- Sync then reset time condition data
        tinsert(data, self:GetOrDefault(self.time_start))
        tinsert(data, self:GetOrDefault(self.time_end))
        self.time_start, self.time_end = "", ""

        -- Additional Sanity Checks for Buttons
        for _, value in pairs(self.buttons) do
            if type(value) == "table" and value.result then
                tinsert(data, self:GetCaseData({ value.result, "no-dupes" }))
            end
        end
    end
    return data
end

--- Modifies the specified event using the subsequent argument values
---
--- By default, this function will append and remove from registeredEvents as a toggle function.
--- The toggle behavior can be replaced via the forced_mode argument as well as what params are available.
---
--- Example: An append operation cannot occur without a trigger or if the key already exists in the table.
--- A remove operation cannot occur unless registeredEvents contains the key.
--- Both operations cannot occur unless args are specified, as either a string or a table
---
--- @param args table The event names to be interpreted with the eventTag (if any)
--- @param data table The event data to be attached to args (If any)
--- @param mode string The modifier key to force the behavior of this function (Optional, can be <add|remove|refresh>)
--- @param log_output boolean Whether to allow logging for this function (Default: false)
function CraftPresence:ModifyTriggers(args, data, mode, log_output)
    if type(args) ~= "table" then
        args = { args }
    end
    log_output = self:GetOrDefault(log_output, false)
    mode = strlower(self:GetOrDefault(mode))
    data = self:GetOrDefault(data, {})

    local trigger = self:GetDynamicReturnValue(data.eventCallback, "function", self)

    if args ~= nil then
        for eventKey, eventName in pairs(args) do
            if type(eventKey) == "string" then
                eventName = eventKey
            end

            if mode ~= "remove" and not self:IsNullOrEmpty(trigger) then
                if not self:IsNullOrEmpty(trigger) then
                    mode = (
                            self.registeredEvents[eventName] and
                                    not self:AreTablesEqual(self.registeredEvents[eventName], data)
                    ) and "refresh" or "add"

                    if mode == "refresh" then
                        self:UnregisterEvent(eventName)
                    end
                    if (not self.registeredEvents[eventName] or
                            not self:AreTablesEqual(self.registeredEvents[eventName], data)
                    ) then
                        self.registeredEvents[eventName] = data
                        self:RegisterEvent(eventName, trigger)
                        if log_output then
                            self:Print(strformat(L["COMMAND_EVENT_SUCCESS"], mode, eventName, trigger))
                        end
                    end
                elseif log_output then
                    self:Print(strformat(L["COMMAND_EVENT_NO_TRIGGER"], mode, eventName))
                end
            elseif mode ~= "add" and self.registeredEvents[eventName] then
                mode = "remove"

                self.registeredEvents[eventName] = nil
                self:UnregisterEvent(eventName)
                if log_output then
                    self:Print(strformat(
                            L["COMMAND_EVENT_SUCCESS"], mode, eventName, self:GetOrDefault(trigger, L["TYPE_NONE"])
                    ))
                end
            end
        end
    end
end

--- Prepares and Dispatches a new frame update, using prefilled global args
function CraftPresence:DispatchLegacyUpdate()
    self:DispatchUpdate(event, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9)
end

--- Prepares and Dispatches a new frame update, given the specified arguments
---
--- @param eventName string The name of the event being executed
--- @param args table The arguments associated with the event execution
CraftPresence.DispatchUpdate = CP_GlobalUtils:vararg(2, function(self, eventName, args)
    eventName = self:GetOrDefault(eventName, L["TYPE_UNKNOWN"])
    if args ~= nil then
        -- Process Callback Event Data
        -- Format: ignore_event, log_output
        local isVerbose, isDebug = self:GetFromDb("verboseMode"), self:GetFromDb("debugMode")
        local ignore_event, log_output = false, (isVerbose or isDebug)

        for key, value in pairs(self.registeredEvents) do
            if eventName == key then
                if not self:IsNullOrEmpty(value.processCallback) then
                    ignore_event, log_output = self:GetDynamicReturnValue(
                            value.processCallback, "function", self, lastEventName, eventName, args
                    )
                    ignore_event = self:GetOrDefault(tostring(ignore_event) == "true", false)
                    log_output = self:GetOrDefault(tostring(log_output) == "true", true)
                end
                break
            end
        end

        -- Skip Execution of method if ignore condition hit
        -- Otherwise, proceed on to reading event data
        if ignore_event then
            return
        else
            -- Store event name, and print logging info if needed
            lastEventName = eventName
            local logPrefix = L["INFO_EVENT_PROCESSING"]
            if self:IsTimerLocked() then
                logPrefix = L["INFO_EVENT_SKIPPED"]
            end
            -- Logging is different depending on verbose/debug states
            local logTemplate = (isVerbose and L["LOG_VERBOSE"]) or
                    (isDebug and L["LOG_DEBUG"]) or
                    (log_output and L["LOG_INFO"]) or nil
            local logData = self:GetLength(args) > 0 and (
                    (isVerbose and self:SerializeTable(args)) or "<...>"
            ) or L["TYPE_NONE"]
            if not self:IsNullOrEmpty(logTemplate) and log_output then
                self:Print(strformat(
                        logTemplate, strformat(
                                logPrefix, eventName, self:GetOrDefault(logData, L["TYPE_UNKNOWN"])
                        )
                ))
            end

            -- If the timer is locked, exit the method at this stage
            -- Otherwise, proceed to frame processing
            if self:IsTimerLocked() then
                return
            else
                -- If we are using a callback delay,
                -- the timer is locked if in a skip-style pipeline.
                -- Otherwise, execute the next method normally.
                local delay = self:GetFromDb("callbackDelay")
                if (self:IsWithinValue(
                        delay,
                        max(L["MINIMUM_CALLBACK_DELAY"], 1), L["MAXIMUM_CALLBACK_DELAY"],
                        true, true)) then
                    self:SetTimerLocked(true)
                    self:After(delay, function()
                        self:PaintMessageWait()
                    end)
                else
                    self:PaintMessageWait()
                end
            end
        end
    end
end)

--- Updates the minimap status with config data
---
--- @param update_state boolean Whether or not to update the icon state
--- @param log_output boolean Whether to allow logging for this function (Default: true)
function CraftPresence:UpdateMinimapState(update_state, log_output)
    log_output = self:GetOrDefault(log_output, true)
    minimapState = { hide = not self:GetFromDb("showMinimapIcon") }
    if update_state then
        if icon then
            if minimapState["hide"] then
                icon:Hide(L["ADDON_NAME"])
            else
                icon:Show(L["ADDON_NAME"])
            end
        elseif log_output then
            self:PrintErrorMessage(strformat(L["ERROR_FUNCTION_DISABLED"], "UpdateMinimapState"))
        end
    end
end

--- Interprets the specified input to perform specific commands
--- @param input string The specified input to evaluate
function CraftPresence:ChatCommand(input)
    local command_query = self:Split(input, " ", true, true)
    if self:IsNullOrEmpty(input) or (strlower(input) == "help" or strlower(input) == "?") then
        self:PrintUsageCommand(
                L["USAGE_CMD_HELP"] .. "\n" ..
                        L["USAGE_CMD_CONFIG"] .. "\n" ..
                        L["USAGE_CMD_CLEAN"] .. "\n" ..
                        L["USAGE_CMD_UPDATE"] .. "\n" ..
                        L["USAGE_CMD_MINIMAP"] .. "\n" ..
                        L["USAGE_CMD_STATUS"] .. "\n" ..
                        L["USAGE_CMD_RESET"] .. "\n" ..
                        L["USAGE_CMD_SET"] .. "\n" ..
                        L["USAGE_CMD_INTEGRATION"] .. "\n" ..
                        L["USAGE_CMD_PLACEHOLDERS"] .. "\n" ..
                        L["USAGE_CMD_EVENTS"] .. "\n" ..
                        L["USAGE_CMD_LABELS"]
        )
    else
        local command = strlower(command_query[1])

        if command == "clean" or command == "clear" then
            self:Print(L["COMMAND_CLEAR_SUCCESS"])
            self:CleanFrames()
            self:SetTimerLocked(false)
        elseif command == "update" then
            local testerMode = false
            if command_query[2] ~= nil then
                local query = strlower(command_query[2])
                testerMode = self:GetFromDb("debugMode") and query == "debug"
            end
            self:PaintMessageWait(true, not testerMode, not testerMode, nil)
        elseif command == "config" then
            if command_query[2] ~= nil and strlower(command_query[2]) == "migrate" then
                self:EnsureCompatibility(
                        self:GetFromDb("schema"), addOnData["schema"], true,
                        self:GetFromDb("optionalMigrations")
                )
            else
                self:ShowConfig()
            end
        elseif command == "reset" then
            local reset_single = (command_query[2] ~= nil)
            if reset_single then
                self:GetFromDb(command_query[2], command_query[3], true)
            end
            self:UpdateProfile(true, not reset_single, "all")
        elseif command == "minimap" then
            self:UpdateMinimapSetting(not self.db.profile.showMinimapIcon)
        elseif command == "about" then
            self:PrintAddonInfo()
        elseif command == "status" then
            if self:GetFromDb("debugMode") then
                local last_args, last_encoded = self:GetCachedEncodedData()
                self:GetEncodedMessage(last_args, last_encoded, L["VERBOSE_LAST_ENCODED"], L["LOG_VERBOSE"], true)
            else
                self:PrintErrorMessage(strformat(L["ERROR_COMMAND_CONFIG"], L["TITLE_DEBUG_MODE"]))
            end
        elseif command == "integration" then
            if command_query[2] ~= nil then
                if not activeIntegrations[command_query[2]] then
                    self:Print(strformat(L["INTEGRATION_QUERY"], command_query[2]))
                    local lower_query = strlower(command_query[2])

                    -- Integration Parsing
                    if (lower_query == "reload" or lower_query == "rl") and ReloadUI then
                        ReloadUI()
                    else
                        self:Print(L["INTEGRATION_NOT_FOUND"])
                    end
                else
                    self:Print(L["INTEGRATION_ALREADY_USED"])
                end
            else
                self:PrintUsageCommand(L["USAGE_CMD_INTEGRATION"])
            end
        elseif (
                (command == "placeholders" or command == "placeholder") or
                        (command == "events" or command == "event") or
                        (command == "labels" or command == "label")
        ) then
            -- Parse tag name and table target from command input
            local tag_name, tag_table = "", ""
            if command == "placeholders" or command == "placeholder" then
                tag_name, tag_table = "placeholders", "placeholders"
            elseif command == "events" or command == "event" then
                tag_name, tag_table = "events", "events"
            elseif command == "labels" or command == "label" then
                tag_name, tag_table = "labels", "labels"
            end
            local resultStr = strformat(L["DATA_FOUND_INTRO"], tag_name)

            if command_query[2] ~= nil then
                local flag_query = self:Split(command_query[2], ":", false, true)
                if flag_query[1] == "create" or flag_query[1] == "add" then
                    -- Sub-Query Parsing
                    local modify_mode = flag_query[2] == "modify"

                    -- Main Parsing
                    if command_query[3] ~= nil then
                        local tag_data = self:GetFromDb(tag_table)
                        if tag_data[command_query[3]] and not modify_mode then
                            self:PrintErrorMessage(L["COMMAND_CREATE_MODIFY"])
                        else
                            -- Some Pre-Filled Data is supplied for these areas
                            -- Primarily as some fields are way to long for any command
                            if tag_name == "placeholders" then
                                tag_data[command_query[3]] = {
                                    minimumTOC = tostring(self:GetOrDefault(command_query[4], buildData["toc_version"])),
                                    maximumTOC = tostring(self:GetOrDefault(command_query[5], buildData["toc_version"])),
                                    allowRebasedApi = (self:GetOrDefault(command_query[6], "true") == "true"),
                                    processCallback = "", processType = "string", registerCallback = "",
                                    tagCallback = "", tagType = "string",
                                    prefix = L["DEFAULT_INNER_KEY"], suffix = L["DEFAULT_INNER_KEY"],
                                    enabled = true
                                }
                            elseif tag_name == "events" then
                                tag_data[command_query[3]] = {
                                    minimumTOC = tostring(self:GetOrDefault(command_query[4], buildData["toc_version"])),
                                    maximumTOC = tostring(self:GetOrDefault(command_query[5], buildData["toc_version"])),
                                    allowRebasedApi = (self:GetOrDefault(command_query[6], "true") == "true"),
                                    processCallback = "", registerCallback = "",
                                    eventCallback = "function(self) return self.defaultEventCallback end",
                                    enabled = true
                                }
                            elseif tag_name == "labels" then
                                tag_data[command_query[3]] = {
                                    minimumTOC = tostring(self:GetOrDefault(command_query[4], buildData["toc_version"])),
                                    maximumTOC = tostring(self:GetOrDefault(command_query[5], buildData["toc_version"])),
                                    allowRebasedApi = (self:GetOrDefault(command_query[6], "true") == "true"),
                                    activeCallback = "", inactiveCallback = "",
                                    activeType = "string", inactiveType = "string",
                                    stateCallback = "",
                                    enabled = true
                                }
                            end
                            local eventState = (modify_mode and L["TYPE_MODIFY"]) or L["TYPE_ADDED"]
                            self:SetToDb(tag_table, nil, tag_data)
                            self:Print(strformat(
                                    L["COMMAND_CREATE_SUCCESS"], eventState, command_query[3], tag_name,
                                    self:SerializeTable(tag_data[command_query[3]])
                            ))
                            self:UpdateProfile(true, false, tag_name)
                        end
                    else
                        self:PrintQueryCommand(tag_name, flag_query[1])
                    end
                elseif flag_query[1] == "remove" then
                    if command_query[3] ~= nil then
                        local tag_data = self:GetFromDb(tag_table)
                        if tag_data[command_query[3]] then
                            local includeTag = true
                            if tag_name == "events" then
                                tag_data[command_query[3]].enabled = false
                                self:SyncEvents(tag_data, self:GetFromDb("verboseMode"))
                                includeTag = false
                            end
                            tag_data[command_query[3]] = nil
                            self:SetToDb(tag_table, nil, tag_data)
                            self:Print(strformat(L["COMMAND_REMOVE_SUCCESS"], tag_name, command_query[3]))
                            self:UpdateProfile(true, false, (includeTag and tag_name or nil))
                        else
                            self:PrintErrorMessage(L["COMMAND_REMOVE_NO_MATCH"])
                        end
                    else
                        self:PrintQueryCommand(tag_name, flag_query[1])
                    end
                elseif flag_query[1] == "list" then
                    local foundAny = false
                    if command_query[3] ~= nil then
                        self:Print(strformat(L["DATA_QUERY"], tag_name, command_query[3]))
                    end

                    local tag_data, visible_data, multi_table, enable_callback, notes = {}, {}, false, nil, ""
                    if tag_name == "placeholders" then
                        -- Ensure Placeholders are synced
                        self:SyncDynamicData(self:GetFromDb("verboseMode"))
                        tag_data = self.placeholders
                        visible_data = {
                            "processCallback", "processType"
                        }
                        enable_callback = function(_, value)
                            return self:ShouldProcessData(value)
                        end
                        notes = L["PLACEHOLDERS_NOTE_ONE"] .. "\n" .. L["PLACEHOLDERS_NOTE_TWO"]
                    elseif tag_name == "events" then
                        tag_data = self:GetFromDb(tag_table)
                        visible_data = {
                            "enabled"
                        }
                        enable_callback = function(_, value)
                            return self:ShouldProcessData(value)
                        end
                    elseif tag_name == "labels" then
                        -- Ensure Labels are synced
                        self:SyncDynamicData(self:GetFromDb("verboseMode"))
                        tag_data = self.labels
                        visible_data = {
                            "activeCallback", "activeType"
                        }
                        enable_callback = function(_, value)
                            return self:ShouldProcessData(value)
                        end
                    end
                    -- Iterate through dataTable to form resultString
                    foundAny, resultStr = self:ParseDynamicTable(
                            tag_name, command_query[3], tag_data, foundAny,
                            resultStr, multi_table, visible_data, enable_callback
                    )

                    -- Final parsing of resultString before printing
                    if not foundAny then
                        resultStr = resultStr .. "\n " .. strformat(L["DATA_FOUND_NONE"], tag_name)
                    end
                    resultStr = resultStr .. "\n" .. notes
                    self:Print(resultStr)
                else
                    self:PrintUsageCommand(L["USAGE_CMD_" .. strupper(tag_name)])
                end
            else
                self:PrintUsageCommand(L["USAGE_CMD_" .. strupper(tag_name)])
            end
        elseif command == "set" then
            local query = command_query
            tremove(query, 1)
            LibStub("AceConfigCmd-3.0"):HandleCommand(
                    L["COMMAND_CONFIG"], L["ADDON_NAME"], self:GetOrDefault(tconcat(query, " "))
            )
            self:UpdateProfile(true, false, "all")
        else
            self:PrintErrorMessage(strformat(L["ERROR_COMMAND_UNKNOWN"], input))
        end
    end
    return command_query
end
