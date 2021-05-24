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

-- Lua APIs
local strformat, strlower, strupper = string.format, string.lower, string.upper
local tostring, pairs, unpack = tostring, pairs, unpack
local type, max, tinsert = type, math.max, table.insert

-- Addon APIs
local L = CraftPresence.locale
local config_registry = LibStub("AceConfigRegistry-3.0")
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
-- Storage Data
local global_placeholders = {}
local inner_placeholders = {}
local custom_placeholders = {}
local time_conditions = {}

--- Synchronize Conditional Data with Game Info
---
--- @param force_instance_change boolean Whether to force an instance change (Default: false)
---
--- @return table, table, table @ global_placeholders, inner_placeholders, time_conditions
function CraftPresence:SyncConditions(force_instance_change)
    global_placeholders = {}
    inner_placeholders = {}
    custom_placeholders = self:GetFromDb("customPlaceholders")
    time_conditions = {}
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
    global_placeholders, inner_placeholders, time_conditions = self:SyncConditions(force_instance_change)
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
    for timeKey, timeValue in pairs(time_conditions) do
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
    for key, value in pairs(buttons) do
        local dataValue, dataSeparator = "", ""
        for buttonKey, buttonValue in pairs(value) do
            dataValue = dataValue .. dataSeparator .. buttonValue
            if not self:IsNullOrEmpty(buttonValue) then
                dataSeparator = split_key
            end
        end
        tinsert(rpcData, dataValue)
    end

    -- Global Placeholder Syncing
    for key, value in pairs(global_placeholders) do
        rpcData = self:SetFormats({ value, nil, key, nil }, rpcData, true, false)
    end
    -- Inner Placeholder Syncing
    for key, value in pairs(inner_placeholders) do
        rpcData = self:SetFormats({ value, nil, key, nil }, rpcData, true, false)
    end
    -- Custom Placeholder Syncing
    for key, value in pairs(custom_placeholders) do
        -- Sanity Checks
        value = self:GetDynamicReturnValue(value["data"], value["type"], self)
        -- Main Parsing
        rpcData = self:SetFormats({ value, nil, key, nil }, rpcData, true, false)
    end

    return self:EncodeData(L["EVENT_RPC_LENGTH"], rpcData)
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
            self.optionsFrame.default = self.ResetProfile
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
    -- Print any Initial Data
    self:PrintAddonInfo()
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
    local resetData = self:EncodeData(L["EVENT_RPC_LENGTH"], { self:GetFromDb("clientId") })
    self:PaintMessageWait(true, false, true, resetData)
    -- Hide Minimap Icon
    if icon then
        icon:Hide(L["ADDON_NAME"])
    end
    -- Un-register Events
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
--- @param trigger string The function name to register with argument values (Required for append operations, can be func)
--- @param log_output boolean Whether to allow logging for this function (Default: False)
--- @param mode string The modifier key to force a certain behavior of this function (Optional, can be <add|remove|refresh>)
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
    if args ~= nil then
        -- Ignore Event Conditional Setup
        -- Format: [EVENT_NAME] = ignore_event_condition
        local ignore_event = false

        for key, value in pairs(self.registeredEvents) do
            if eventName == key then
                ignore_event = (
                        self:IsNullOrEmpty(value.ignore) or
                                self:GetDynamicReturnValue(value.ignore, "function", self, lastEventName, eventName, args) == "true"
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
    if self:IsNullOrEmpty(input) or input == "help" or input == "?" then
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
        if input == "clean" or input == "clear" then
            self:Print(L["COMMAND_CLEAR_SUCCESS"])
            self:CleanFrames()
            self:SetTimerLocked(false)
        elseif self:StartsWith(input, "update") then
            -- Query Parsing
            local _, _, query = self:FindMatches(input, " (.*)", false)
            local forceMode, testerMode = false, false
            if query ~= nil then
                query = strlower(query)
                forceMode = query == "force"
                testerMode = self:GetFromDb("debugMode") and query == "test"
            end
            self:PaintMessageWait(true, not testerMode, not testerMode, nil, forceMode)
        elseif input == "config" then
            self:ShowConfig()
        elseif self:StartsWith(input, "reset") then
            local _, _, query = self:FindMatches(input, " (.*)", false)
            if query ~= nil then
                local _, _, sub_query = self:FindMatches(query, ",(.*)", false)
                self:GetFromDb(query, sub_query, true)
            else
                self:ResetProfile(false)
            end

            if buildData["toc_version"] >= compatData["2.0.0"] or isRebasedApi then
                config_registry:NotifyChange(L["ADDON_NAME"])
            end
        elseif input == "minimap" then
            self:UpdateMinimapSetting(not self.db.profile.showMinimapIcon)
        elseif input == "about" then
            self:PrintAddonInfo()
        elseif input == "status" then
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
        elseif self:StartsWith(input, "integration") then
            -- Query Parsing
            local _, _, query = self:FindMatches(input, " (.*)", false)
            if query ~= nil then
                if not activeIntegrations[query] then
                    self:Print(strformat(L["INTEGRATION_QUERY"], query))
                    local lower_query = strlower(query)

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
        elseif self:StartsWith(input, "placeholders") or self:StartsWith(input, "events") then
            -- Parse tag name and table target
            local tag_name, tag_table = "", ""
            if self:StartsWith(input, "placeholders") then
                tag_name, tag_table = "placeholders", "customPlaceholders"
            elseif self:StartsWith(input, "events") then
                tag_name, tag_table = "events", "events"
            end
            local resultStr = strformat(L["DATA_FOUND_INTRO"], tag_name)
            global_placeholders, inner_placeholders, time_conditions = self:SyncConditions()
            -- Query Parsing
            local _, _, query = self:FindMatches(input, " (.*)", false)
            if query ~= nil then
                if self:StartsWith(query, "create") or self:StartsWith(query, "add") then
                    -- Sub-Query Parsing
                    local modifiable = self:StartsWith(query, "create:modify")
                    local _, _, sub_query = self:FindMatches(query, " (.*)", false)

                    -- Type Query Parsing (Dependent on tag name)
                    local _, _, typeQuery
                    if tag_name == "placeholders" then
                        _, _, typeQuery = self:FindMatches(query, "::(.*)::", false)
                        if typeQuery ~= nil then
                            _, _, sub_query = self:FindMatches(query, ":: (.*)", false)
                        end
                    end
                    typeQuery = self:GetOrDefault(typeQuery, "string")

                    -- Main Parsing
                    if sub_query ~= nil then
                        local tag_data = self:GetFromDb(tag_table)
                        local splitQuery = self:Split(sub_query, " ")
                        if tag_data[splitQuery[1]] and not modifiable then
                            self:Print(strformat(L["LOG_ERROR"], L["COMMAND_CREATE_MODIFY"]))
                        elseif (
                                tag_name ~= "placeholders" or not (
                                        global_placeholders[splitQuery[1]] or inner_placeholders[splitQuery[1]]
                                )
                        ) then
                            if tag_name == "placeholders" then
                                tag_data[splitQuery[1]] = {
                                    ["type"] = typeQuery,
                                    ["data"] = self:GetOrDefault(splitQuery[2])
                                }
                            elseif tag_name == "events" then
                                -- Pre-Filled Data is supplied for events
                                -- Primarily as 6 fields is way to long for any command
                                tag_data[splitQuery[1]] = {
                                    minimumTOC = "", maximumTOC = "",
                                    ignoreCallback = "",
                                    registerCallback = "",
                                    eventCallback = "function(self) return self.defaultEventCallback end",
                                    enabled = true
                                }
                            end
                            local eventState = (modifiable and L["TYPE_MODIFY"]) or L["TYPE_ADDED"]
                            self:SetToDb(tag_table, nil, tag_data)
                            self:Print(strformat(
                                    L["COMMAND_CREATE_SUCCESS"], eventState, splitQuery[1], tag_name,
                                    self:SerializeTable(tag_data[splitQuery[1]])
                            ))

                            if buildData["toc_version"] >= compatData["2.0.0"] or isRebasedApi then
                                config_registry:NotifyChange(L["ADDON_NAME"])
                            end

                            -- Additional events, dependent on tag name
                            if tag_name == "events" then
                                self:SyncEvents(tag_data, self:GetFromDb("debugMode"))
                            end
                        else
                            self:Print(strformat(L["LOG_ERROR"], L["COMMAND_CREATE_CONFLICT"]))
                        end
                    else
                        self:PrintUsageCommand(L["USAGE_CMD_" .. strupper(tag_name)])
                    end
                elseif self:StartsWith(query, "remove") then
                    -- Sub-Query Parsing
                    local _, _, sub_query = self:FindMatches(query, " (.*)", false)
                    if sub_query ~= nil then
                        local tag_data = self:GetFromDb(tag_table)
                        if tag_data[sub_query] then
                            tag_data[sub_query] = nil
                            self:SetToDb(tag_table, nil, tag_data)
                            self:Print(strformat(L["COMMAND_REMOVE_SUCCESS"], tag_name, sub_query))

                            if buildData["toc_version"] >= compatData["2.0.0"] or isRebasedApi then
                                config_registry:NotifyChange(L["ADDON_NAME"])
                            end

                            -- Additional events, dependent on tag name
                            if tag_name == "events" then
                                self:SyncEvents(tag_data, self:GetFromDb("debugMode"))
                            end
                        else
                            self:Print(strformat(L["LOG_ERROR"], L["COMMAND_REMOVE_NO_MATCH"]))
                        end
                    else
                        self:PrintUsageCommand(L["USAGE_CMD_" .. strupper(tag_name)])
                    end
                elseif self:StartsWith(query, "list") then
                    -- Sub-Query Parsing
                    local _, _, sub_query = self:FindMatches(query, " (.*)", false)
                    local foundAny = false
                    if sub_query ~= nil then
                        self:Print(strformat(L["DATA_QUERY"], tag_name, sub_query))
                        sub_query = strlower(sub_query)
                    end

                    local tag_data = {}
                    if tag_name == "placeholders" then
                        tag_data = self:CombineTables(global_placeholders, inner_placeholders, custom_placeholders)
                    elseif tag_name == "events" then
                        tag_data = self:GetFromDb(tag_table)
                    end
                    -- Iterate through dataTable to form resultString
                    foundAny, resultStr = self:ParseDynamicTable(tag_name, sub_query, tag_data, foundAny, resultStr)

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
        elseif self:StartsWith(input, "set") then
            local _, _, query = self:FindMatches(input, " (.*)", false)
            LibStub("AceConfigCmd-3.0"):HandleCommand(L["COMMAND_CONFIG"], L["ADDON_NAME"], self:GetOrDefault(query))
        else
            self:Print(strformat(
                    L["LOG_ERROR"], strformat(
                            L["ERROR_COMMAND_UNKNOWN"], input
                    )
            ))
        end
    end
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
