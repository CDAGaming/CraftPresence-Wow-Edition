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

-- Lua APIs
local strformat, strlower = string.format, string.lower
local tostring, pairs = tostring, pairs
local type, max, tinsert = type, math.max, table.insert

-- Addon APIs
local L = CraftPresence.locale
local config_registry = LibStub("AceConfigRegistry-3.0")

-- Critical Data (DNT)
local CraftPresenceLDB, icon
local lastEventName, registryEventName
local minimapState = { hide = false }
-- Build and Integration Data
local buildData = {}
local compatData = {}
local integrationData = {}
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
        self:GetFromDb("largeImageKey"),
        self:GetFromDb("largeImageMessage"),
        self:GetFromDb("smallImageKey"),
        self:GetFromDb("smallImageMessage"),
        self:GetFromDb("detailsMessage"),
        self:GetFromDb("gameStateMessage")
    }
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
    -- Command Registration
    self:RegisterChatCommand(L["ADDON_ID"], "ChatCommand")
    self:RegisterChatCommand(L["ADDON_AFFIX"], "ChatCommand")
    -- Version-Specific Registration
    if buildData["toc_version"] >= compatData["1.12.1"] then
        -- UI Registration
        if buildData["toc_version"] >= compatData["2.0.0"] or isRebasedApi then
            self.optionsFrame = LibStub("AceConfigDialog-3.0"):AddToBlizOptions(L["ADDON_NAME"])
            self.optionsFrame.default = self.ResetDB
        end
        -- Icon Registration
        icon = LibStub("LibDBIcon-1.0")
        CraftPresenceLDB = LibStub:GetLibrary("LibDataBroker-1.1"):NewDataObject(L["ADDON_NAME"], {
            type = "launcher",
            text = L["ADDON_NAME"],
            icon = strformat("Interface\\Addons\\%s\\images\\icon.blp", L["ADDON_NAME"]),
            OnClick = function(_, _)
                CraftPresence:ShowConfig()
            end,
            OnTooltipShow = function(tt)
                tt:AddLine(L["ADDON_NAME"])
                tt:AddLine(" ")
                tt:AddLine(L["ADDON_TOOLTIP_THREE"])
                tt:AddLine(" ")
                tt:AddLine(L["ADDON_TOOLTIP_FIVE"])
            end
        })
        self:UpdateMinimapState(false)
        icon:Register(L["ADDON_NAME"], CraftPresenceLDB, minimapState)
    end
end

--- Instructions to be called when the addon is enabled
function CraftPresence:OnEnable()
    -- Print any Initial Data
    self:PrintInitialData()
    -- Register Universal Events
    if buildData["toc_version"] >= compatData["2.0.0"] or isRebasedApi then
        registryEventName = "DispatchModernUpdate"
    else
        registryEventName = "DispatchLegacyUpdate"
    end
    self:AddTriggers(registryEventName,
            { "PLAYER_LOGIN", "PLAYER_LEVEL_UP",
              "PLAYER_ALIVE", "PLAYER_DEAD", "PLAYER_FLAGS_CHANGED",
              "ZONE_CHANGED", "ZONE_CHANGED_NEW_AREA", "ZONE_CHANGED_INDOORS",
              "UPDATE_INSTANCE_INFO" }
    )
    -- Register Version-Specific Events
    if buildData["toc_version"] >= compatData["5.0.0"] then
        self:AddTriggers(registryEventName,
                { "PLAYER_SPECIALIZATION_CHANGED" }
        )
    end
    if buildData["toc_version"] >= compatData["6.0.0"] then
        self:AddTriggers(registryEventName,
                { "ACTIVE_TALENT_GROUP_CHANGED",
                  "CHALLENGE_MODE_START", "CHALLENGE_MODE_COMPLETED", "CHALLENGE_MODE_RESET",
                  "SCENARIO_COMPLETED", "CRITERIA_COMPLETE" }
        )
    end
    if buildData["toc_version"] >= compatData["8.0.0"] then
        self:AddTriggers(registryEventName,
                { "PLAYER_LEVEL_CHANGED" }
        )
    end
    -- Create initial frames and initial rpc update
    self:CreateFrames(self:GetFromDb("frameSize"))
    self:PaintMessageWait()
end

--- Registers the specified event for the subsequent argument values
---
--- @param event string The event tag to register with argument values
--- @param args table The events to bind to the event tag
function CraftPresence:AddTriggers(event, args)
    if type(args) ~= "table" then
        args = { args }
    end

    if event ~= nil and args ~= nil then
        for _, v in pairs(args) do
            local eventTag, eventBinding = tostring(v), tostring(event)
            CraftPresence.registeredEvents[eventTag] = eventBinding
            self:RegisterEvent(eventTag, eventBinding)
        end
    end
end

--- Un-Registers the specified events
---
--- @param args table The events to unregister
function CraftPresence:RemoveTriggers(args)
    if type(args) ~= "table" then
        args = { args }
    end

    if args ~= nil then
        for _, v in pairs(args) do
            local eventTag = tostring(v)
            CraftPresence.registeredEvents[eventTag] = nil
            self:UnregisterEvent(eventTag)
        end
    end
end

--- Prepares and Dispatches a new frame update, given the specified arguments
function CraftPresence:DispatchLegacyUpdate()
    self:DispatchUpdate({
        event, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9
    })
end

--- Prepares and Dispatches a new frame update, given the specified arguments
function CraftPresence:DispatchModernUpdate(event, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9)
    self:DispatchUpdate({
        event, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9
    })
end

--- Prepares and Dispatches a new frame update, given the specified arguments
function CraftPresence:DispatchUpdate(args)
    if type(args) ~= "table" then
        args = { args }
    end

    if args ~= nil then
        -- Ignore Event Conditional Setup
        -- Format: [EVENT_NAME] = ignore_event_condition
        local eventName = args[1]
        local ignore_event = false
        local ignore_event_conditions = {
            ["PLAYER_FLAGS_CHANGED"] = (
                    (args[2] ~= nil and args[2] ~= "player") or
                            self:GetLastPlayerStatus() == self:GetPlayerStatus("player",
                                    false, isRebasedApi, L["FORMAT_USER_PREFIX"]
                            )
            ),
            ["UPDATE_INSTANCE_INFO"] = (not IsInInstance() or
                    lastEventName == eventName or
                    buildData["toc_version"] < compatData["6.0.0"] or
                    self:GetCachedLockout() == self:GetCurrentLockoutData(false)
            ),
            ["PLAYER_ALIVE"] = (lastEventName ~= "PLAYER_DEAD"),
            ["PLAYER_LEVEL_UP"] = (lastEventName == "PLAYER_LEVEL_CHANGED"),
            ["PLAYER_LEVEL_CHANGED"] = (lastEventName == "PLAYER_LEVEL_UP"),
            ["ACTIVE_TALENT_GROUP_CHANGED"] = (args[2] == args[3]),
            ["PLAYER_SPECIALIZATION_CHANGED"] = (args[2] ~= "player")
        }

        for key, value in pairs(ignore_event_conditions) do
            if eventName == key and value then
                ignore_event = true
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
            if self:GetFromDb("debugMode") then
                if self:GetFromDb("verboseMode") then
                    self:Print(strformat(
                            L["LOG_VERBOSE"], strformat(
                                    logPrefix, self:SerializeTable(args)
                            )
                    ))
                else
                    self:Print(strformat(
                            L["LOG_DEBUG"], strformat(
                                    logPrefix, eventName
                            )
                    ))
                end
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
end

--- Instructions to be called when the addon is disabled
function CraftPresence:OnDisable()
    -- Clean up Data before disabling
    self:Print(L["ADDON_CLOSE"])
    local resetData = self:EncodeData(L["EVENT_RPC_LENGTH"], { self:GetFromDb("clientId") })
    self:PaintMessageWait(true, false, true, resetData)
    if icon then
        icon:Hide(L["ADDON_NAME"])
    end
    self:UnregisterAllEvents()
end

--- Updates the minimap status with config data
--- @param update_state boolean Whether or not to update the icon state
function CraftPresence:UpdateMinimapState(update_state)
    minimapState = { hide = not self:GetFromDb("showMinimapIcon") }
    if update_state then
        if icon then
            if minimapState["hide"] then
                icon:Hide(L["ADDON_NAME"])
            else
                icon:Show(L["ADDON_NAME"])
            end
        else
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
        self:Print(
                strformat(L["USAGE_CMD_INTRO"], L["ADDON_NAME"]) .. "\n" ..
                        L["USAGE_CMD_HELP"] .. "\n" ..
                        L["USAGE_CMD_CONFIG"] .. "\n" ..
                        L["USAGE_CMD_TEST"] .. "\n" ..
                        L["USAGE_CMD_CLEAN"] .. "\n" ..
                        L["USAGE_CMD_UPDATE"] .. "\n" ..
                        L["USAGE_CMD_MINIMAP"] .. "\n" ..
                        L["USAGE_CMD_STATUS"] .. "\n" ..
                        L["USAGE_CMD_RESET"] .. "\n" ..
                        L["USAGE_CMD_SET"] .. "\n" ..
                        L["USAGE_CMD_INTEGRATION"] .. "\n" ..
                        L["USAGE_CMD_CREATE"] .. "\n" ..
                        L["USAGE_CMD_PLACEHOLDERS"] .. "\n" ..
                        strformat(L["USAGE_CMD_NOTE"], L["ADDON_AFFIX"], L["ADDON_ID"]) .. "\n" ..
                        L["USAGE_CMD_NOTE_TWO"] .. "\n"
        )
    else
        if input == "test" then
            if self:GetFromDb("debugMode") then
                self:PaintMessageWait(true, false, false)
            else
                self:Print(strformat(
                        L["LOG_ERROR"], strformat(
                                L["ERROR_COMMAND_CONFIG"], L["TITLE_DEBUG_MODE"]
                        )
                ))
            end
        elseif input == "clean" or input == "clear" then
            self:Print(L["COMMAND_CLEAR_SUCCESS"])
            self:CleanFrames()
            self:SetTimerLocked(false)
        elseif self:StartsWith(input, "update") then
            -- Query Parsing
            local _, _, query = self:FindMatches(input, " (.*)", false)
            if query ~= nil then
                query = strlower(query)
            end
            self:PaintMessageWait(true, true, true, nil, (query == "force" or query == "true"))
        elseif input == "config" then
            self:ShowConfig()
        elseif self:StartsWith(input, "reset") then
            local _, _, query = self:FindMatches(input, " (.*)", false)
            if query ~= nil then
                local _, _, sub_query = self:FindMatches(query, ",(.*)", false)
                self:GetFromDb(query, sub_query, true)
            else
                self:ResetDB()
            end

            if buildData["toc_version"] >= compatData["2.0.0"] or isRebasedApi then
                config_registry:NotifyChange(L["ADDON_NAME"])
            end
        elseif input == "minimap" then
            self:UpdateMinimapSetting(not self.db.profile.showMinimapIcon)
        elseif input == "about" then
            self:PrintInitialData()
        elseif input == "status" then
            if self:GetFromDb("debugMode") then
                self:GetEncodedMessage(nil, nil, L["VERBOSE_LAST_ENCODED"], L["LOG_VERBOSE"], true)
            else
                self:Print(strformat(
                        L["LOG_ERROR"], strformat(
                                L["ERROR_COMMAND_CONFIG"], L["TITLE_DEBUG_MODE"]
                        )
                ))
            end
        elseif self:StartsWith(input, "create") then
            -- Query Parsing
            local canModify = self:StartsWith(input, "create:modify")
            local _, _, typeQuery = self:FindMatches(input, "::(.*)::", false)
            local _, _, query = self:FindMatches(input, " (.*)", false)

            -- Sanity Check Type
            if typeQuery ~= nil then
                _, _, query = self:FindMatches(input, ":: (.*)", false)
            else
                typeQuery = "string"
            end
            -- Main Parsing
            if query ~= nil then
                local customPlaceholders = self:GetFromDb("customPlaceholders")
                local splitQuery = self:Split(query, " ")
                splitQuery[2] = splitQuery[2] or ""
                if customPlaceholders[splitQuery[1]] and not canModify then
                    self:Print(strformat(L["LOG_ERROR"], L["COMMAND_CREATE_MODIFY"]))
                elseif not (global_placeholders[splitQuery[1]] or inner_placeholders[splitQuery[1]]) then
                    customPlaceholders[splitQuery[1]] = {
                        ["type"] = typeQuery,
                        ["data"] = splitQuery[2]
                    }
                    local eventState = L["TYPE_ADDED"]
                    if canModify then
                        eventState = L["TYPE_MODIFY"]
                    end
                    self:SetToDb("customPlaceholders", nil, customPlaceholders)
                    self:Print(strformat(
                            L["COMMAND_CREATE_SUCCESS"], eventState, splitQuery[1], self:SerializeTable(
                                    customPlaceholders[splitQuery[1]]
                            )
                    ))

                    if buildData["toc_version"] >= compatData["2.0.0"] or isRebasedApi then
                        config_registry:NotifyChange(L["ADDON_NAME"])
                    end
                else
                    self:Print(strformat(L["LOG_ERROR"], L["COMMAND_CREATE_CONFLICT"]))
                end
            else
                self:Print(
                        strformat(L["USAGE_CMD_INTRO"], L["ADDON_NAME"]) .. "\n" ..
                                L["USAGE_CMD_CREATE"] .. "\n" ..
                                strformat(L["USAGE_CMD_NOTE"], L["ADDON_AFFIX"], L["ADDON_ID"]) .. "\n" ..
                                L["USAGE_CMD_NOTE_TWO"] .. "\n"
                )
            end
        elseif self:StartsWith(input, "remove") then
            -- Query Parsing
            local _, _, query = self:FindMatches(input, " (.*)", false)
            if query ~= nil then
                local customPlaceholders = self:GetFromDb("customPlaceholders")
                if customPlaceholders[query] then
                    customPlaceholders[query] = nil
                    self:SetToDb("customPlaceholders", nil, customPlaceholders)
                    self:Print(strformat(L["COMMAND_REMOVE_SUCCESS"], query))

                    if buildData["toc_version"] >= compatData["2.0.0"] or isRebasedApi then
                        config_registry:NotifyChange(L["ADDON_NAME"])
                    end
                else
                    self:Print(strformat(L["LOG_ERROR"], L["COMMAND_REMOVE_NO_MATCH"]))
                end
            else
                self:Print(
                        strformat(L["USAGE_CMD_INTRO"], L["ADDON_NAME"]) .. "\n" ..
                                L["USAGE_CMD_REMOVE"] .. "\n" ..
                                strformat(L["USAGE_CMD_NOTE"], L["ADDON_AFFIX"], L["ADDON_ID"]) .. "\n" ..
                                L["USAGE_CMD_NOTE_TWO"] .. "\n"
                )
            end
        elseif self:StartsWith(input, "integration") then
            -- Query Parsing
            local _, _, query = self:FindMatches(input, " (.*)", false)
            if query ~= nil then
                if not integrationData[query] then
                    self:Print(strformat(L["INTEGRATION_QUERY"], query))
                    local lower_query = strlower(query)

                    -- Integration Parsing
                    if (lower_query == "reload" or lower_query == "rl") and ReloadUI then
                        ReloadUI()
                    elseif (self:StartsWith(lower_query, "event")) then
                        -- Sub-Query Parsing
                        local _, _, eventQuery = self:FindMatches(query, " (.*)", false)
                        if eventQuery ~= nil then
                            if CraftPresence.registeredEvents[eventQuery] then
                                self:RemoveTriggers(eventQuery)
                            else
                                self:AddTriggers(registryEventName, eventQuery)
                            end
                        else
                            self:Print(strformat(
                                    L["LOG_ERROR"], strformat(
                                            L["ERROR_COMMAND_UNKNOWN"], input
                                    )
                            ))
                        end
                    else
                        self:Print(L["INTEGRATION_NOT_FOUND"])
                    end
                else
                    self:Print(L["INTEGRATION_ALREADY_USED"])
                end
            else
                self:Print(
                        strformat(L["USAGE_CMD_INTRO"], L["ADDON_NAME"]) .. "\n" ..
                                L["USAGE_CMD_INTEGRATION"] .. "\n" ..
                                strformat(L["USAGE_CMD_NOTE"], L["ADDON_AFFIX"], L["ADDON_ID"]) .. "\n" ..
                                L["USAGE_CMD_NOTE_TWO"] .. "\n"
                )
            end
        elseif self:StartsWith(input, "placeholders") then
            local placeholderStr = L["PLACEHOLDERS_FOUND_INTRO"]
            global_placeholders, inner_placeholders, time_conditions = self:SyncConditions()
            -- Query Parsing
            local _, _, query = self:FindMatches(input, " (.*)", false)
            local foundAny = false
            if query ~= nil then
                self:Print(strformat(L["PLACEHOLDERS_QUERY"], query))
                query = strlower(query)
            end
            -- Global placeholder iteration to form placeholderString
            foundAny, placeholderStr = self:ParsePlaceholderTable(query, global_placeholders, foundAny, placeholderStr)
            -- Inner placeholder iteration to form placeholderString
            foundAny, placeholderStr = self:ParsePlaceholderTable(query, inner_placeholders, foundAny, placeholderStr)
            -- Custom placeholder iteration to form placeholderString
            foundAny, placeholderStr = self:ParsePlaceholderTable(query, custom_placeholders, foundAny, placeholderStr)
            -- Final parsing of placeholderString before printing
            if not foundAny then
                placeholderStr = placeholderStr .. "\n " .. L["PLACEHOLDERS_FOUND_NONE"]
            end
            placeholderStr = placeholderStr .. "\n" .. L["PLACEHOLDERS_NOTE_ONE"] .. "\n" .. L["PLACEHOLDERS_NOTE_TWO"]
            self:Print(placeholderStr)
        elseif self:StartsWith(input, "set") then
            local _, _, query = self:FindMatches(input, " (.*)", false)
            LibStub("AceConfigCmd-3.0"):HandleCommand(L["COMMAND_CONFIG"], L["ADDON_NAME"], query or "")
        else
            self:Print(strformat(
                    L["LOG_ERROR"], strformat(
                            L["ERROR_COMMAND_UNKNOWN"], input
                    )
            ))
        end
    end
end
