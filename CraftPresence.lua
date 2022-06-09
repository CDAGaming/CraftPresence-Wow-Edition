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
local pairs, type, max, tostring = pairs, type, math.max, tostring

-- Addon APIs

CraftPresence.locale = {}
CraftPresence.registeredEvents = {}
CraftPresence.registeredMetrics = {}
CraftPresence.defaultEventCallback = ""
CraftPresence.placeholders = {}
CraftPresence.presenceData = {}
CraftPresence.buttons = {}
CraftPresence.labels = {}

CraftPresence.time_start = ""
CraftPresence.time_end = ""

CraftPresence.canUseExternals = false
CraftPresence.externalCache = {}

-- Critical Data (DNT)
local CraftPresenceLDB
local lastEventName
local minimapState = { hide = false }
-- Build and Integration Data
local addOnData = {}
local buildData = {}
local compatData = {}
local activeIntegrations = {}
local isRebasedApi = false

--- Instructions to be called when the addon is loaded
function CraftPresence:OnInitialize()
    -- Main Initialization
    self.locale = self.libraries.AceLocale:GetLocale(self.internals.name)
    addOnData = self:GetAddOnInfo()
    buildData = self:GetBuildInfo()
    compatData = self:GetCompatibilityInfo()
    isRebasedApi = self:IsRebasedApi()
    -- Options Initialization
    self:GenerateDefaults()
    self.db = self.libraries.AceDB:New(self.internals.name .. "DB", self:GetDefaults())
    self.libraries.AceConfig:RegisterOptionsTable(self.internals.name, self.GetOptions, {
        (self.internals.name .. " set"), (self.internals.affix .. " set")
    })
    self.libraries.AceConfigDialog:SetDefaultSize(self.internals.name, 858, 660)
    self:EnsureCompatibility(
        self:GetProperty("schema"), addOnData["schema"], false,
        self:GetProperty("optionalMigrations")
    )
    -- Analytics Initialization
    self:SyncAnalytics(self:GetProperty("metrics"))
    self:LogChangedValue("currentTOC", nil, buildData["toc_version"])
    -- Version-Specific Registration
    if buildData["toc_version"] >= compatData["1.12.1"] then
        -- UI Registration
        if InterfaceOptions_AddCategory then
            local can_register = true
            local minTOC, maxTOC = compatData["2.0.0"], compatData["4.0.0"]
            local currentTOC = buildData["toc_version"]
            if not isRebasedApi and (currentTOC >= minTOC and currentTOC < maxTOC) then
                -- On TBC and Wrath Clients, the interface options frame size is far too small
                -- to show any meaningful data for addon settings displayed in there.
                -- If enforceInterface is enabled, we'll adjust the size to retail's counterpart,
                -- as well as register it to Blizzard's options panel.
                -- Otherwise, we'll use the legacy UI to prevent sizing/accessibility issues.
                if self:GetProperty("enforceInterface") then
                    self:AssertFrameSize(InterfaceOptionsFrame, 858, 660)
                    self:AssertFrameSize(InterfaceOptionsFrameCategories, 175, 569)
                    self:AssertFrameSize(InterfaceOptionsFrameAddOns, 175, 569)
                else
                    can_register = false
                end
            end
            if can_register then
                self.optionsFrame = self.libraries.AceConfigDialog:AddToBlizOptions(self.internals.name)
                self.optionsFrame.default = function()
                    self:UpdateProfile(true, true, "all")
                end
            end
        end
        -- Icon Registration
        self.libraries.LibDBIcon = LibStub("LibDBIcon-1.0")
        CraftPresenceLDB = LibStub:GetLibrary("LibDataBroker-1.1"):NewDataObject(self.internals.name, {
            type = "launcher",
            text = self.internals.name,
            icon = strformat("Interface\\Addons\\%s\\Images\\icon.blp", self.internals.name),
            OnClick = function(_, _)
                self:ShowConfig()
            end,
            OnTooltipShow = function(tt)
                tt:AddLine(addOnData["versionString"])
                tt:AddLine(" ")
                tt:AddLine(self.locale["ADDON_TOOLTIP_THREE"])
                tt:AddLine(" ")
                tt:AddLine(self.locale["ADDON_TOOLTIP_FIVE"])
            end
        })
        self:UpdateMinimapState(false, false)
        self.libraries.LibDBIcon:Register(self.internals.name, CraftPresenceLDB, minimapState)
    end
end

--- Instructions to be called when the addon is enabled
function CraftPresence:OnEnable()
    -- Print and set any Initial Data, if allowed
    if self:GetProperty("showWelcomeMessage") then
        self:PrintAddonInfo()
    end
    -- Command Registration
    self:RegisterChatCommand(self.internals.identifier, "ChatCommand")
    self:RegisterChatCommand(self.internals.affix, "ChatCommand")
    -- Create initial frames and initial rpc update
    self:CreateFrames(self:GetProperty("frameSize"))
    self:PaintMessageWait()
    self:UpdateMinimapState(true, false)
    -- Register Universal Events
    if buildData["toc_version"] >= compatData["2.0.0"] or isRebasedApi then
        self.defaultEventCallback = "DispatchUpdate"
    else
        self.defaultEventCallback = "DispatchLegacyUpdate"
    end
    self:SyncEvents(self:GetProperty("events"), self:GetProperty("verboseMode"))
end

--- Instructions to be called when the addon is disabled
function CraftPresence:OnDisable()
    -- Print Closing Message
    self:Print(self.locale["ADDON_CLOSE"])
    -- Command Un-registration
    self:UnregisterChatCommand(self.internals.identifier)
    self:UnregisterChatCommand(self.internals.affix)
    -- Reset RPC Data to Discord
    local resetData = self:ConcatTable(self.internals.rpc.eventTag, self.internals.rpc.eventSeperator, self:GetProperty("clientId"))
    self:PaintMessageWait(true, false, true, resetData)
    -- Hide Minimap Icon
    if self.libraries.LibDBIcon then
        self.libraries.LibDBIcon:Hide(self.internals.name)
    end
    -- Un-register all active events
    -- Note: SyncEvents is not used here so that manually added events are also properly cleared
    self:ModifyTriggers(self.registeredEvents, nil, "remove", self:GetProperty("verboseMode"))
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

        shouldRegister = (self:IsNullOrEmpty(data.registerCallback) or tostring(
            self:GetDynamicReturnValue(data.registerCallback, "function", self)
        ) == "true")
        local canAccept = shouldRegister and (self:IsWithinValue(
            currentTOC, minTOC, maxTOC, true, true, false
        ) or (data.allowRebasedApi and self:IsRebasedApi()))
        shouldEnable = data.enabled and canAccept
    end
    return shouldEnable, shouldRegister
end

--- Sync the contents of dynamic data for readable usage
---
--- @param log_output boolean Whether to allow logging for this function (Default: false)
--- @param supply_data boolean If true, format applicable data into a displayable format for returning (Default: False)
---
--- @return table @ data_table
function CraftPresence:SyncDynamicData(log_output, supply_data)
    log_output = self:GetOrDefault(log_output, false)
    supply_data = self:GetOrDefault(supply_data, false)

    local isVerbose, isDebug = self:GetProperty("verboseMode"), self:GetProperty("debugMode")

    self:CopyTable(self:GetProperty("placeholders"), self.placeholders)
    self:CopyTable(self:GetProperty("presence"), self.presenceData)
    self:CopyTable(self:GetProperty("buttons"), self.buttons)
    self:CopyTable(self:GetProperty("labels"), self.labels)

    for key, value in pairs(self.placeholders) do
        if type(value) == "table" then
            -- Sanity Checks
            local keyPrefix = self:GetOrDefault(value.prefix)
            local keySuffix = self:GetOrDefault(value.suffix)
            local newKey = keyPrefix .. key .. keySuffix
            local newValue, tagValue = "", ""

            if self:ShouldProcessData(value) then
                -- Print logging info if needed, before processing
                local logPrefix = self.locale["INFO_PLACEHOLDER_PROCESSING"]
                -- Logging is different depending on verbose/debug states
                local logTemplate = (isVerbose and self.locale["LOG_VERBOSE"]) or
                    (isDebug and self.locale["LOG_DEBUG"]) or
                    (log_output and self.locale["LOG_INFO"]) or nil
                local logData = not self:IsNullOrEmpty(value.processCallback) and (
                    (isVerbose and value.processCallback) or "<...>") or self.locale["TYPE_NONE"]
                if not self:IsNullOrEmpty(logTemplate) and log_output then
                    self:Print(strformat(
                        logTemplate, strformat(
                            logPrefix, newKey, self:GetOrDefault(logData, self.locale["TYPE_UNKNOWN"])
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

            -- Sync Rich Presence Info
            for presenceKey, presenceValue in pairs(self.presenceData) do
                if self:ShouldProcessData(presenceValue) then
                    if presenceValue.keyCallback then
                        presenceValue.keyCallback = self:Replace(presenceValue.keyCallback, newKey, self:GetOrDefault(newValue), true)
                        presenceValue.keyResult = self:GetDynamicReturnValue(presenceValue.keyCallback, presenceValue.keyType, self)
                    end
                    if presenceValue.messageCallback then
                        presenceValue.messageCallback = self:Replace(presenceValue.messageCallback, newKey, self:GetOrDefault(newValue), true)
                        presenceValue.messageResult = self:GetDynamicReturnValue(presenceValue.messageCallback, presenceValue.messageType, self)
                    end
                end

                if presenceValue.keyCallback then
                    presenceValue.keyResult = self:GetOrDefault(presenceValue.keyResult)
                end
                if presenceValue.messageCallback then
                    presenceValue.messageResult = self:GetOrDefault(presenceValue.messageResult)
                end
                self.presenceData[presenceKey] = presenceValue
            end

            -- Sync Button Info
            for buttonKey, buttonValue in pairs(self.buttons) do
                if self:ShouldProcessData(buttonValue) then
                    buttonValue.labelCallback = self:Replace(buttonValue.labelCallback, newKey, self:GetOrDefault(newValue), true)
                    buttonValue.label = self:GetDynamicReturnValue(buttonValue.labelCallback, buttonValue.labelType, self)
                    buttonValue.urlCallback = self:Replace(buttonValue.urlCallback, newKey, self:GetOrDefault(newValue), true)
                    buttonValue.url = self:GetDynamicReturnValue(buttonValue.urlCallback, buttonValue.urlType, self)
                end
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

            if supply_data then
                -- Sync Tag Conditional Data
                if not self:IsNullOrEmpty(tagValue) then
                    if self:FindMatches(tagValue, "time", false) then
                        if self[tagValue] ~= nil then
                            if self:HasInstanceChanged() then
                                self[tagValue] = "generated"
                            else
                                self[tagValue] = "last"
                            end
                        end
                    end
                end
            end
        end
    end

    if supply_data then
        -- Apply Main Presence Fields to data, if allowed
        local largeImage = self.presenceData["largeImage"]
        local smallImage = self.presenceData["smallImage"]
        local details = self.presenceData["details"]
        local state = self.presenceData["state"]
        local data = {
            self:GetProperty("clientId"),
            self:ParseDynamicFormatting({ largeImage.keyResult, largeImage.keyFormatCallback, largeImage.keyFormatType }),
            self:ParseDynamicFormatting({ largeImage.messageResult, largeImage.messageFormatCallback, largeImage.messageFormatType }),
            self:ParseDynamicFormatting({ smallImage.keyResult, smallImage.keyFormatCallback, smallImage.keyFormatType }),
            self:ParseDynamicFormatting({ smallImage.messageResult, smallImage.messageFormatCallback, smallImage.messageFormatType }),
            self:ParseDynamicFormatting({ details.messageResult, details.messageFormatCallback, details.messageFormatType }),
            self:ParseDynamicFormatting({ state.messageResult, state.messageFormatCallback, state.messageFormatType })
        }
        -- Sync then reset time condition data
        tinsert(data, self:GetOrDefault(self.time_start))
        tinsert(data, self:GetOrDefault(self.time_end))
        self.time_start, self.time_end = "", ""

        -- Apply Combined Button Fields to data, if allowed
        for _, value in pairs(self.buttons) do
            if type(value) == "table" then
                value.result = self:ConcatTable(
                    nil, self.internals.rpc.buttonsSplitKey,
                    self:ParseDynamicFormatting({ value.label, value.messageFormatCallback, value.messageFormatType }),
                    self:GetOrDefault(value.url)
                )
                tinsert(data, self:GetOrDefault(value.result))
            end
        end
        return data
    end
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
                    mode = (self.registeredEvents[eventName] and not self:AreTablesEqual(
                        self.registeredEvents[eventName], data
                    )) and "refresh" or "add"

                    if mode == "refresh" then
                        self:UnregisterEvent(eventName)
                    end
                    if (not self.registeredEvents[eventName] or
                        not self:AreTablesEqual(self.registeredEvents[eventName], data)
                        ) then
                        self.registeredEvents[eventName] = data
                        self:RegisterEvent(eventName, trigger)
                        if log_output then
                            self:Print(strformat(self.locale["COMMAND_EVENT_SUCCESS"], mode, eventName, trigger))
                        end
                    end
                elseif log_output then
                    self:Print(strformat(self.locale["COMMAND_EVENT_NO_TRIGGER"], mode, eventName))
                end
            elseif mode ~= "add" and self.registeredEvents[eventName] then
                mode = "remove"

                self.registeredEvents[eventName] = nil
                self:UnregisterEvent(eventName)
                if log_output then
                    self:Print(strformat(
                        self.locale["COMMAND_EVENT_SUCCESS"], mode, eventName, self:GetOrDefault(trigger, self.locale["TYPE_NONE"])
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
CraftPresence.DispatchUpdate = CraftPresence:vararg(2, function(self, eventName, args)
    eventName = self:GetOrDefault(eventName, self.locale["TYPE_UNKNOWN"])
    if args ~= nil then
        -- Process Callback Event Data
        -- Format: ignore_event, log_output
        local isVerbose, isDebug = self:GetProperty("verboseMode"), self:GetProperty("debugMode")
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
            local logPrefix = self.locale["INFO_EVENT_PROCESSING"]
            if self:IsTimerLocked() then
                logPrefix = self.locale["INFO_EVENT_SKIPPED"]
            end
            -- Logging is different depending on verbose/debug states
            local logTemplate = (isVerbose and self.locale["LOG_VERBOSE"]) or
                (isDebug and self.locale["LOG_DEBUG"]) or
                (log_output and self.locale["LOG_INFO"]) or nil
            local logData = self:GetLength(args) > 0 and (
                (isVerbose and self:SerializeTable(args)) or "<...>") or self.locale["TYPE_NONE"]
            if not self:IsNullOrEmpty(logTemplate) and log_output then
                self:Print(strformat(
                    logTemplate, strformat(
                        logPrefix, eventName, self:GetOrDefault(logData, self.locale["TYPE_UNKNOWN"])
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
                local delay = self:GetProperty("callbackDelay")
                if (self:IsWithinValue(
                    delay,
                    max(self.locale["MINIMUM_CALLBACK_DELAY"], 1), self.locale["MAXIMUM_CALLBACK_DELAY"],
                    true, true
                )) then
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
    minimapState = { hide = not self:GetProperty("showMinimapIcon") }
    if update_state then
        if self.libraries.LibDBIcon then
            if minimapState["hide"] then
                self.libraries.LibDBIcon:Hide(self.internals.name)
            else
                self.libraries.LibDBIcon:Show(self.internals.name)
            end
        elseif log_output then
            self:PrintErrorMessage(strformat(self.locale["ERROR_FUNCTION_DISABLED"], "UpdateMinimapState"))
        end
    end
end

--- Interprets the specified input to perform specific commands
--- @param input string The specified input to evaluate
function CraftPresence:ChatCommand(input)
    local command_query = self:Split(input, " ", true, true)
    if self:IsNullOrEmpty(input) or (strlower(input) == "help" or strlower(input) == "?") then
        self:PrintUsageCommand(
            self.locale["USAGE_CMD_HELP"] .. "\n" ..
            self.locale["USAGE_CMD_CONFIG"] .. "\n" ..
            self.locale["USAGE_CMD_CLEAN"] .. "\n" ..
            self.locale["USAGE_CMD_UPDATE"] .. "\n" ..
            self.locale["USAGE_CMD_MINIMAP"] .. "\n" ..
            self.locale["USAGE_CMD_STATUS"] .. "\n" ..
            self.locale["USAGE_CMD_RESET"] .. "\n" ..
            self.locale["USAGE_CMD_SET"] .. "\n" ..
            self.locale["USAGE_CMD_INTEGRATION"] .. "\n" ..
            self.locale["USAGE_CMD_PLACEHOLDERS"] .. "\n" ..
            self.locale["USAGE_CMD_EVENTS"] .. "\n" ..
            self.locale["USAGE_CMD_LABELS"]
        )
    else
        local command = strlower(command_query[1])

        if command == "clean" or command == "clear" then
            self:Print(self.locale["COMMAND_CLEAR_SUCCESS"])
            self:CleanFrames()
            self:SetTimerLocked(false)
        elseif command == "update" then
            local testerMode = false
            if command_query[2] ~= nil then
                local query = strlower(command_query[2])
                testerMode = self:GetProperty("debugMode") and query == "debug"
            end
            self:PaintMessageWait(true, not testerMode, not testerMode, nil)
        elseif command == "config" then
            local has_argument = not self:IsNullOrEmpty(command_query[2])
            if has_argument and strlower(command_query[2]) == "migrate" then
                self:EnsureCompatibility(
                    self:GetProperty("schema"), addOnData["schema"], true,
                    self:GetProperty("optionalMigrations")
                )
            else
                self:ShowConfig(has_argument and strlower(command_query[2]) == "standalone")
            end
        elseif command == "reset" then
            local reset_single = (command_query[2] ~= nil)
            if reset_single then
                local data = {
                    group = command_query[2],
                    key = command_query[3]
                }

                if (not data.key and self.db.profile[data.group]) or
                    (data.key ~= nil and self.db.profile[data.group] and self.db.profile[data.group][data.key]) then
                    self:GetProperty(command_query[2], command_query[3], true)
                    self:Print(strformat(self.locale["INFO_RESET_CONFIG_SINGLE"], self:SerializeTable(data)))
                else
                    self:PrintErrorMessage(strformat(self.locale["COMMAND_RESET_NOT_FOUND"], self:SerializeTable(data)))
                end
            end
            self:UpdateProfile(true, not reset_single, "all")
        elseif command == "minimap" then
            self:UpdateMinimapSetting(not self:GetProperty("showMinimapIcon"))
        elseif command == "about" then
            self:PrintAddonInfo()
        elseif command == "status" then
            if self:GetProperty("debugMode") then
                local last_args, last_encoded = self:GetCachedEncodedData()
                self:GetEncodedMessage(last_args, last_encoded, self.locale["VERBOSE_LAST_ENCODED"], self.locale["LOG_VERBOSE"], true)
            else
                self:PrintErrorMessage(strformat(self.locale["ERROR_COMMAND_CONFIG"], self.locale["TITLE_DEBUG_MODE"]))
            end
        elseif command == "integration" then
            if command_query[2] ~= nil then
                if not activeIntegrations[command_query[2]] then
                    self:Print(strformat(self.locale["INTEGRATION_QUERY"], command_query[2]))
                    local lower_query = strlower(command_query[2])

                    -- Integration Parsing
                    if (lower_query == "reload" or lower_query == "rl") and ReloadUI then
                        ReloadUI()
                    else
                        self:Print(self.locale["INTEGRATION_NOT_FOUND"])
                    end
                else
                    self:Print(self.locale["INTEGRATION_ALREADY_USED"])
                end
            else
                self:PrintUsageCommand(self.locale["USAGE_CMD_INTEGRATION"])
            end
        elseif (not self:IsNullOrEmpty(command) and
            (command == "placeholders" or command == "placeholder") or
            (command == "events" or command == "event") or
            (command == "labels" or command == "label")) then
            -- Parse tag name and table target from command input
            local tag_name, tag_table = "", ""
            if command == "placeholders" or command == "placeholder" then
                tag_name, tag_table = "placeholders", "placeholders"
            elseif command == "events" or command == "event" then
                tag_name, tag_table = "events", "events"
            elseif command == "labels" or command == "label" then
                tag_name, tag_table = "labels", "labels"
            end
            local resultStr = strformat(self.locale["DATA_FOUND_INTRO"], tag_name)

            if command_query[2] ~= nil then
                local flag_query = self:Split(command_query[2], ":", false, true)
                if flag_query[1] == "create" or flag_query[1] == "add" then
                    -- Sub-Query Parsing
                    local modify_mode = flag_query[2] == "modify"

                    -- Main Parsing
                    if command_query[3] ~= nil then
                        local tag_data = self:GetProperty(tag_table)
                        local default_data = self:GetOrDefault(self:GetDefaults().profile[tag_table][command_query[3]], {})
                        if tag_data[command_query[3]] and not modify_mode then
                            self:PrintErrorMessage(self.locale["COMMAND_CREATE_MODIFY"])
                        else
                            -- Some Pre-Filled Data is supplied for these areas
                            -- Primarily as some fields are way to long for any command
                            if tag_name == "placeholders" then
                                local default_rebased = tostring(self:GetOrDefault(default_data.allowRebasedApi, "true"))
                                tag_data[command_query[3]] = {
                                    minimumTOC = tostring(self:GetOrDefault(command_query[4], default_data.minimumTOC or buildData["toc_version"])),
                                    maximumTOC = tostring(self:GetOrDefault(command_query[5], default_data.maximumTOC or buildData["toc_version"])),
                                    allowRebasedApi = tostring(self:GetOrDefault(command_query[6], default_rebased)) == "true",
                                    processCallback = self:GetOrDefault(default_data.processCallback),
                                    processType = self:GetOrDefault(default_data.processType, "string"),
                                    registerCallback = self:GetOrDefault(default_data.registerCallback),
                                    tagCallback = self:GetOrDefault(default_data.tagCallback),
                                    tagType = self:GetOrDefault(default_data.tagType, "string"),
                                    prefix = self:GetOrDefault(default_data.prefix, self.internals.defaultInnerKey),
                                    suffix = self:GetOrDefault(default_data.suffix, self.internals.defaultInnerKey),
                                    enabled = self:GetOrDefault(default_data.enabled, true)
                                }
                            elseif tag_name == "events" then
                                local default_rebased = tostring(self:GetOrDefault(default_data.allowRebasedApi, "true"))
                                tag_data[command_query[3]] = {
                                    minimumTOC = tostring(self:GetOrDefault(command_query[4], default_data.minimumTOC or buildData["toc_version"])),
                                    maximumTOC = tostring(self:GetOrDefault(command_query[5], default_data.maximumTOC or buildData["toc_version"])),
                                    allowRebasedApi = tostring(self:GetOrDefault(command_query[6], default_rebased)) == "true",
                                    processCallback = self:GetOrDefault(default_data.processCallback),
                                    registerCallback = self:GetOrDefault(default_data.registerCallback),
                                    eventCallback = self:GetOrDefault(default_data.eventCallback, "function(self) return self.defaultEventCallback end"),
                                    enabled = self:GetOrDefault(default_data.enabled, true)
                                }
                            elseif tag_name == "labels" then
                                local default_rebased = tostring(self:GetOrDefault(default_data.allowRebasedApi, "true"))
                                tag_data[command_query[3]] = {
                                    minimumTOC = tostring(self:GetOrDefault(command_query[4], default_data.minimumTOC or buildData["toc_version"])),
                                    maximumTOC = tostring(self:GetOrDefault(command_query[5], default_data.maximumTOC or buildData["toc_version"])),
                                    allowRebasedApi = tostring(self:GetOrDefault(command_query[6], default_rebased)) == "true",
                                    activeCallback = self:GetOrDefault(default_data.activeCallback),
                                    inactiveCallback = self:GetOrDefault(default_data.inactiveCallback),
                                    activeType = self:GetOrDefault(default_data.activeType, "string"),
                                    inactiveType = self:GetOrDefault(default_data.inactiveType, "string"),
                                    stateCallback = self:GetOrDefault(default_data.stateCallback),
                                    enabled = self:GetOrDefault(default_data.enabled, true)
                                }
                            end
                            local eventState = (modify_mode and self.locale["TYPE_MODIFY"]) or self.locale["TYPE_ADDED"]
                            self:SetProperty(tag_table, nil, tag_data)
                            self:Print(strformat(
                                self.locale["COMMAND_CREATE_SUCCESS"], eventState, command_query[3], tag_name,
                                self:SerializeTable(tag_data[command_query[3]])
                            ))
                            self:UpdateProfile(true, false, tag_name)
                        end
                    else
                        self:PrintQueryCommand(tag_name, flag_query[1])
                    end
                elseif flag_query[1] == "remove" then
                    if command_query[3] ~= nil then
                        local tag_data = self:GetProperty(tag_table)
                        if tag_data[command_query[3]] then
                            local includeTag = true
                            if tag_name == "events" then
                                tag_data[command_query[3]].enabled = false
                                self:SyncEvents(tag_data, self:GetProperty("verboseMode"))
                                includeTag = false
                            end
                            tag_data[command_query[3]] = nil
                            self:SetProperty(tag_table, nil, tag_data)
                            self:Print(strformat(self.locale["COMMAND_REMOVE_SUCCESS"], tag_name, command_query[3]))
                            self:UpdateProfile(true, false, (includeTag and tag_name or nil))
                        else
                            self:PrintErrorMessage(self.locale["COMMAND_REMOVE_NO_MATCH"])
                        end
                    else
                        self:PrintQueryCommand(tag_name, flag_query[1])
                    end
                elseif flag_query[1] == "list" then
                    local foundAny = false
                    if command_query[3] ~= nil then
                        self:Print(strformat(self.locale["DATA_QUERY"], tag_name, command_query[3]))
                    end

                    local tag_data, visible_data, multi_table, enable_callback, notes = {}, {}, false, nil, ""
                    if tag_name == "placeholders" then
                        -- Ensure Placeholders are synced
                        self:SyncDynamicData(self:GetProperty("verboseMode"))
                        tag_data = self.placeholders
                        visible_data = {
                            "processCallback", "processType"
                        }
                        enable_callback = function(_, value)
                            return self:ShouldProcessData(value)
                        end
                        notes = self.locale["PLACEHOLDERS_NOTE_ONE"] .. "\n" .. self.locale["PLACEHOLDERS_NOTE_TWO"]
                    elseif tag_name == "events" then
                        tag_data = self:GetProperty(tag_table)
                        visible_data = {
                            "enabled"
                        }
                        enable_callback = function(_, value)
                            return self:ShouldProcessData(value)
                        end
                    elseif tag_name == "labels" then
                        -- Ensure Labels are synced
                        self:SyncDynamicData(self:GetProperty("verboseMode"))
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
                        resultStr = resultStr .. "\n " .. strformat(self.locale["DATA_FOUND_NONE"], tag_name)
                    end
                    resultStr = resultStr .. "\n" .. notes
                    self:Print(resultStr)
                else
                    self:PrintUsageCommand(self.locale["USAGE_CMD_" .. strupper(tag_name)])
                end
            else
                self:PrintUsageCommand(self.locale["USAGE_CMD_" .. strupper(tag_name)])
            end
        elseif command == "set" then
            local query = command_query
            tremove(query, 1)
            self.libraries.AceConfigCmd:HandleCommand(
                (self.internals.name .. " " .. command), self.internals.name, self:GetOrDefault(tconcat(query, " "))
            )
            self:UpdateProfile(true, false, "all")
        else
            self:PrintErrorMessage(strformat(self.locale["ERROR_COMMAND_UNKNOWN"], input))
        end
    end
    return command_query
end
