--[[
MIT License

Copyright (c) 2018 - 2021 CDAGaming (cstack2011@yahoo.com)

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

-- Initial Addon Initializers (DNT)
CraftPresence = LibStub("AceAddon-3.0"):NewAddon("CraftPresence", "AceConsole-3.0", "AceEvent-3.0")
CraftPresence.locale = LibStub("AceLocale-3.0"):GetLocale("CraftPresence")
CraftPresence.registeredEvents = {}
CraftPresence.defaultEventCallback = ""
CraftPresence.placeholders = {}
CraftPresence.conditions = {}

-- Lua APIs
local strformat, strlower, strupper = string.format, string.lower, string.upper
local tinsert, tremove, tconcat = table.insert, table.remove, table.concat
local pairs, type, max, unpack = pairs, type, math.max, unpack

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

-- Addon APIs
local L = CraftPresence.locale
local CP_GlobalUtils = CP_GlobalUtils

-- Critical Data (DNT)
local CraftPresenceLDB, icon
local lastEventName
local minimapState = { hide = false }
-- Build and Integration Data
local buildData = {}
local compatData = {}
local activeIntegrations = {}
local isRebasedApi = false

--- Synchronize Conditional Data with Game Info
---
--- @param force_instance_change boolean Whether to force an instance change (Default: false)
---
--- @return table, table @ placeholders, conditionals
function CraftPresence:SyncConditions(force_instance_change)
    wipe(self.placeholders)
    wipe(self.conditions)
    return self:ParseGameData(force_instance_change)
end

--- Creates and encodes a new RPC event from placeholder and conditional data
---
--- @param force_instance_change boolean Whether to force an instance change (Default: false)
---
--- @return string, table @ newEncodedString, args
function CraftPresence:EncodeConfigData(force_instance_change)
    -- Primary Variable Data
    local split_key = L["ARRAY_SPLIT_KEY"]
    self.placeholders, self.conditions = self:SyncConditions(force_instance_change)
    -- Secondary Variable Data
    local buttons = self:GetFromDb("buttons")
    local rpcData = {
        self:GetFromDb("clientId"),
        { self:GetFromDb("largeImageKey"), "lower" },
        self:GetFromDb("largeImageMessage"),
        { self:GetFromDb("smallImageKey"), "lower" },
        self:GetFromDb("smallImageMessage"),
        self:GetFromDb("detailsMessage"),
        self:GetFromDb("gameStateMessage")
    }
    -- Time Condition Syncing
    local time_start, time_end
    for timeKey, timeValue in pairs(self.conditions.time) do
        if timeValue then
            if (self:FindMatches(timeKey, "start", false)) then
                if self:HasInstanceChanged() then
                    time_start = "generated"
                else
                    time_start = "last"
                end
            elseif (self:FindMatches(timeKey, "end", false)) then
                if self:HasInstanceChanged() then
                    time_end = "generated"
                else
                    time_end = "last"
                end
            end
        end
    end
    tinsert(rpcData, self:GetOrDefault(time_start))
    tinsert(rpcData, self:GetOrDefault(time_end))

    -- Additional Sanity Checks for Buttons
    for _, value in pairs(buttons) do
        if type(value) == "table" and value.label and value.url then
            local dataValue = self:ConcatTable(nil, split_key, value.label, value.url)
            tinsert(rpcData, dataValue)
        end
    end

    -- Placeholder Syncing
    for key, value in pairs(self.placeholders) do
        -- Notes:
        --  Only categorized placeholders are allowed, to maintain control
        --  Keys beginning with certain characters are considered metadata values
        if type(value) == "table" and not self:StartsWith(key, self.metaValue) then
            local keyPrefix = self:GetOrDefault(self.placeholders[key][self.metaValue .. "prefix"])
            for subKey, subValue in pairs(value) do
                if not self:StartsWith(subKey, self.metaValue) then
                    -- Sanity Checks
                    subKey = keyPrefix .. subKey .. keyPrefix
                    subValue = self:GetDynamicReturnValue(
                            (type(subValue) == "table" and subValue["data"]) or subValue,
                            (type(subValue) == "table" and subValue["type"]), self)
                    -- Main Parsing
                    rpcData = self:SetFormats({ subValue, nil, subKey, nil },
                            rpcData, true, false
                    )
                end
            end
        end
    end

    return self:ConcatTable(L["EVENT_RPC_TAG"], L["ARRAY_SEPARATOR_KEY"], unpack(rpcData))
end

--- Instructions to be called when the addon is loaded
function CraftPresence:OnInitialize()
    -- Main Initialization
    buildData = self:GetBuildInfo()
    compatData = self:GetCompatibilityInfo()
    isRebasedApi = self:IsRebasedApi()
    -- Options Initialization
    self.db = LibStub("AceDB-3.0"):New(L["ADDON_NAME"] .. "DB", self:GetSchemaDefaults())
    LibStub("AceConfig-3.0"):RegisterOptionsTable(L["ADDON_NAME"], self.getOptionsTable, {
        (L["COMMAND_CONFIG"]), (L["COMMAND_CONFIG_ALT"])
    })
    -- Version-Specific Registration
    if buildData["toc_version"] >= compatData["1.12.1"] then
        -- UI Registration
        if buildData["toc_version"] >= compatData["2.0.0"] or isRebasedApi then
            self.optionsFrame = LibStub("AceConfigDialog-3.0"):AddToBlizOptions(L["ADDON_NAME"])
            self.optionsFrame.default = self.UpdateProfile
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
                tt:AddLine(self:GetVersionString())
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
    self.metaValue = self:GetFromDb("tableMetaKey")
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
    self:SyncEvents(self:GetFromDb("events"), self:GetFromDb("debugMode"))
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
    self:ModifyTriggers(self.registeredEvents, nil, self:GetFromDb("debugMode"), "remove")
end

--- Sync the contents of registeredEvents with the specified event table
--- (This is primarily a helper function towards ModifyTriggers, retrofitted to use an event table)
---
--- @param grp table The table of events to interpret (Should match that of self.db.profile.events)
--- @param log_output boolean Whether to allow logging for this function (Default: false)
function CraftPresence:SyncEvents(grp, log_output)
    local currentTOC = buildData["toc_version"]

    grp = self:GetOrDefault(grp, {})
    log_output = self:GetOrDefault(log_output, false)
    for eventName, eventData in pairs(grp) do
        local minTOC = self:GetOrDefault(eventData["minimumTOC"], currentTOC)
        if type(minTOC) ~= "number" then
            minTOC = self:VersionToBuild(minTOC)
        end
        local maxTOC = self:GetOrDefault(eventData["maximumTOC"], currentTOC)
        if type(maxTOC) ~= "number" then
            maxTOC = self:VersionToBuild(maxTOC)
        end

        local canAccept = (
                self:IsNullOrEmpty(eventData["registerCallback"]) or
                        self:GetDynamicReturnValue(eventData["registerCallback"], "function", self) == "true"
        )
        canAccept = canAccept and self:IsWithinValue(currentTOC, minTOC, maxTOC, true, true, false)
        local shouldEnable = eventData["enabled"] and canAccept

        self:ModifyTriggers(
                eventName, self:GetDynamicReturnValue(eventData["eventCallback"], "function", self),
                log_output, (shouldEnable and "" or "remove"), eventData["ignoreCallback"]
        )
    end
end

--- Modifies the specified event using the subsequent argument values
---
--- By default, this function will append and remove from registeredEvents as a toggle function.
--- The toggle behavior can be overriden via the forced_mode argument as well as what params are available.
---
--- Example: An append operation cannot occur without a trigger or if the key already exists in the table.
--- A remove operation cannot occur unless registeredEvents contains the key.
--- Both operations cannot occur unless args are specified, as either a string or a table
---
--- @param args table The event names to be interpreted with the eventTag (if any)
--- @param trigger string The function name to register with arguments (Required for append operations, can be func)
--- @param log_output boolean Whether to allow logging for this function (Default: false)
--- @param mode string The modifier key to force the behavior of this function (Optional, can be <add|remove|refresh>)
--- @param ignore_condition string The function, that if true, will ignore this event if it occurs (Optional)
function CraftPresence:ModifyTriggers(args, trigger, log_output, mode, ignore_condition)
    if type(args) ~= "table" then
        args = { args }
    end
    log_output = self:GetOrDefault(log_output, false)
    mode = strlower(self:GetOrDefault(mode))
    ignore_condition = self:GetOrDefault(ignore_condition, "function(_,_,_,_) return false end")
    local event_data = {
        target = trigger,
        ignore = ignore_condition
    }

    if args ~= nil then
        for eventKey, eventName in pairs(args) do
            if type(eventKey) == "string" then
                eventName = eventKey
            end

            if mode ~= "remove" and not self:IsNullOrEmpty(trigger) then
                if not self:IsNullOrEmpty(trigger) then
                    mode = (
                            self.registeredEvents[eventName] and
                                    not self:AreTablesEqual(self.registeredEvents[eventName], event_data)
                    ) and "refresh" or "add"

                    if mode == "refresh" then
                        self:UnregisterEvent(eventName)
                    end
                    if (not self.registeredEvents[eventName] or
                            not self:AreTablesEqual(self.registeredEvents[eventName], event_data)
                    ) then
                        self.registeredEvents[eventName] = event_data
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
        -- Ignore Event Conditional Setup
        -- Format: [EVENT_NAME] = ignore_event_condition
        local ignore_event = false

        for key, value in pairs(self.registeredEvents) do
            if eventName == key then
                ignore_event = (
                        self:IsNullOrEmpty(value.ignore) or
                                self:GetDynamicReturnValue(
                                        value.ignore, "function", self, lastEventName, eventName, args
                                ) == "true"
                )
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
            local isVerbose, isDebug = self:GetFromDb("verboseMode"), self:GetFromDb("debugMode")
            local logTemplate = (isVerbose and L["LOG_VERBOSE"]) or (isDebug and L["LOG_DEBUG"]) or nil
            local logData = self:GetLength(args) > 0 and (
                    (isVerbose and self:SerializeTable(args)) or
                            (isDebug and "<...>")
            ) or nil
            if not self:IsNullOrEmpty(logTemplate) then
                self:Print(strformat(
                        logTemplate, strformat(
                                logPrefix, eventName, self:GetOrDefault(logData, L["TYPE_NONE"])
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
            self:Print(strformat(
                    L["LOG_ERROR"], strformat(
                            L["ERROR_FUNCTION_DISABLED"], "UpdateMinimapState"
                    )
            ))
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
                        L["USAGE_CMD_EVENTS"]
        )
    else
        local command = strlower(command_query[1])

        if command == "clean" or command == "clear" then
            self:Print(L["COMMAND_CLEAR_SUCCESS"])
            self:CleanFrames()
            self:SetTimerLocked(false)
        elseif command == "update" then
            local forceMode, testerMode = false, false
            if command_query[2] ~= nil then
                local query = strlower(command_query[2])
                forceMode = query == "force"
                testerMode = self:GetFromDb("debugMode") and query == "test"
            end
            self:PaintMessageWait(true, not testerMode, not testerMode, nil, forceMode)
        elseif command == "config" then
            self:ShowConfig()
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
                self:Print(strformat(
                        L["LOG_ERROR"], strformat(
                                L["ERROR_COMMAND_CONFIG"], L["TITLE_DEBUG_MODE"]
                        )
                ))
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
                        (command == "events" or command == "event")
        ) then
            -- Parse tag name and table target from command input
            local tag_name, tag_table = "", ""
            if command == "placeholders" or command == "placeholder" then
                tag_name, tag_table = "placeholders", "customPlaceholders"
                self.placeholders, self.conditions = self:SyncConditions()
            elseif command == "events" or command == "event" then
                tag_name, tag_table = "events", "events"
            end
            local resultStr = strformat(L["DATA_FOUND_INTRO"], tag_name)

            if command_query[2] ~= nil then
                local flag_query = self:Split(command_query[2], ":", false, true)
                if flag_query[1] == "create" or flag_query[1] == "add" then
                    -- Sub-Query Parsing
                    local modifiable = flag_query[2] == "modify"

                    -- Main Parsing
                    if command_query[3] ~= nil then
                        local tag_data = self:GetFromDb(tag_table)
                        if tag_data[command_query[3]] and not modifiable then
                            self:PrintErrorMessage(L["COMMAND_CREATE_MODIFY"])
                        elseif (
                                tag_name ~= "placeholders" or not (
                                        self.placeholders.global[command_query[3]] or
                                                self.placeholders.inner[command_query[3]] or
                                                self:StartsWith(command_query[3], self.metaValue)
                                )
                        ) then
                            -- Some Pre-Filled Data is supplied for these areas
                            -- Primarily as some fields are way to long for any command
                            if tag_name == "placeholders" then
                                local postArgs = self:GetArgsInRange(command_query, 5)
                                tag_data[command_query[3]] = {
                                    ["type"] = self:GetOrDefault(command_query[4], "string"),
                                    ["data"] = self:ConcatTable(nil, " ", unpack(postArgs))
                                }
                            elseif tag_name == "events" then
                                tag_data[command_query[3]] = {
                                    minimumTOC = self:GetOrDefault(command_query[5]),
                                    maximumTOC = self:GetOrDefault(command_query[6]),
                                    ignoreCallback = "", registerCallback = "",
                                    eventCallback = "function(self) return self.defaultEventCallback end",
                                    enabled = (self:GetOrDefault(command_query[4], "true") == "true")
                                }
                            end
                            local eventState = (modifiable and L["TYPE_MODIFY"]) or L["TYPE_ADDED"]
                            self:SetToDb(tag_table, nil, tag_data)
                            self:Print(strformat(
                                    L["COMMAND_CREATE_SUCCESS"], eventState, command_query[3], tag_name,
                                    self:SerializeTable(tag_data[command_query[3]])
                            ))
                            self:UpdateProfile(true, false, tag_name)
                        else
                            self:PrintErrorMessage(L["COMMAND_CREATE_CONFLICT"])
                        end
                    else
                        self:PrintUsageCommand(L["USAGE_CMD_" .. strupper(tag_name)])
                    end
                elseif flag_query[1] == "remove" then
                    if command_query[3] ~= nil then
                        local tag_data = self:GetFromDb(tag_table)
                        if tag_data[command_query[3]] then
                            local includeTag = true
                            if tag_name == "events" then
                                tag_data[command_query[3]].enabled = false
                                self:SyncEvents(tag_data, self:GetFromDb("debugMode"))
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
                        self:PrintUsageCommand(L["USAGE_CMD_" .. strupper(tag_name)])
                    end
                elseif flag_query[1] == "list" then
                    local foundAny = false
                    if command_query[3] ~= nil then
                        self:Print(strformat(L["DATA_QUERY"], tag_name, command_query[3]))
                    end

                    local tag_data, multi_table = {}, false
                    if tag_name == "placeholders" then
                        tag_data = self.placeholders
                        multi_table = true
                    elseif tag_name == "events" then
                        tag_data = self:GetFromDb(tag_table)
                        multi_table = false
                    end
                    -- Iterate through dataTable to form resultString
                    foundAny, resultStr = self:ParseDynamicTable(
                            tag_name, command_query[3], tag_data, foundAny, resultStr, multi_table
                    )

                    -- Final parsing of resultString before printing
                    if not foundAny then
                        resultStr = resultStr .. "\n " .. strformat(L["DATA_FOUND_NONE"], tag_name)
                    end
                    resultStr = resultStr .. "\n" .. L["PLACEHOLDERS_NOTE_ONE"] .. "\n" .. L["PLACEHOLDERS_NOTE_TWO"]
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
            self:Print(strformat(
                    L["LOG_ERROR"], strformat(
                            L["ERROR_COMMAND_UNKNOWN"], input
                    )
            ))
        end
    end
    return command_query
end

--- Displays the specified usage command in a help text format
--- (INTERNAL USAGE ONLY)
---
--- @param usage string The usage command text
function CraftPresence:PrintUsageCommand(usage)
    usage = self:GetOrDefault(usage)
    self:Print(
            strformat(L["USAGE_CMD_INTRO"], L["ADDON_NAME"]) .. "\n" ..
                    usage .. "\n" ..
                    strformat(L["USAGE_CMD_NOTE"], L["ADDON_AFFIX"], L["ADDON_ID"]) .. "\n" ..
                    L["USAGE_CMD_NOTE_TWO"] .. "\n"
    )
end
