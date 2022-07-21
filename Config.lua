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
local pairs, type, tostring = pairs, type, tostring
local strformat, strlower = string.format, string.lower

--- Retrieves the option table to be used in the Config Menu
--- @return table @ opts
function CraftPresence:GetOptions()
    -- It is necesary to have this value, as calling it with self implied
    -- Causes NotifyChange to fail from time to time
    local self = CraftPresence
    local profilesGroup = self.libraries.AceDBOptions:GetOptionsTable(self.db)
    local defaults = self:GetDefaults().profile

    -- Ensure Ordering is Correct, by resetting the index before generating the options
    self:ResetIndex()

    local opts = {
        type = "group", childGroups = "tab",
        name = self:GetAddOnInfo("versionString"),
        get = function(info)
            return self.db.profile[info[self:GetLength(info)]]
        end,
        set = function(info, value)
            self.db.profile[info[self:GetLength(info)]] = value
        end,
        args = {
            generalOptions = {
                type = "group", order = self:GetNextIndex(),
                name = self.locale["CATEGORY_TITLE_GENERAL"], desc = self.locale["CATEGORY_COMMENT_GENERAL"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = {
                    clientId = {
                        type = "input", order = self:GetNextIndex(), width = 1.25,
                        name = self:GetConfigTitle("CLIENT_ID"),
                        desc = self:GetConfigComment("CLIENT_ID", nil, nil, nil, defaults.clientId),
                        usage = self:GetConfigUsage("CLIENT_ID"),
                        get = function(_)
                            return self:GetProperty("clientId")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetProperty("clientId")
                            local isValid = (value ~= nil and
                                self:ContainsDigit(value) and
                                self:GetLength(value) == 18)
                            if isValid then
                                self:SetProperty("clientId", nil, value)
                                self:PrintChangedValue(self.locale["TITLE_CLIENT_ID"], oldValue, value)
                            else
                                self:PrintErrorMessage(self.locale["ERROR_CLIENT_ID"])
                            end
                        end
                    },
                    blank1 = {
                        type = "description", order = self:GetNextIndex(), name = ""
                    },
                    showMinimapIcon = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = self:GetConfigTitle("SHOW_MINIMAP_ICON"),
                        desc = self:GetConfigComment("SHOW_MINIMAP_ICON", nil, nil, nil, defaults.showMinimapIcon),
                        get = function(_)
                            return self:GetProperty("showMinimapIcon")
                        end,
                        set = function(_, value)
                            self:UpdateMinimapSetting(value)
                        end
                    },
                    showWelcomeMessage = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = self:GetConfigTitle("SHOW_WELCOME_MESSAGE"),
                        desc = self:GetConfigComment("SHOW_WELCOME_MESSAGE", nil, nil, nil, defaults.showWelcomeMessage),
                        get = function(_)
                            return self:GetProperty("showWelcomeMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetProperty("showWelcomeMessage")
                            local isValid = (type(value) == "boolean")
                            if isValid then
                                self:SetProperty("showWelcomeMessage", nil, value)
                                self:PrintChangedValue(self.locale["TITLE_SHOW_WELCOME_MESSAGE"], oldValue, value)
                            end
                        end
                    },
                    blank2 = {
                        type = "description", order = self:GetNextIndex(), name = ""
                    },
                    enforceInterface = {
                        type = "toggle", order = self:GetNextIndex(),
                        disabled = function()
                            local currentTOC = self:GetBuildInfo("toc_version")
                            return not self:IsFeatureSupported("enforceInterface", currentTOC)
                        end,
                        name = self:GetConfigTitle("ENFORCE_INTERFACE"),
                        desc = self:GetConfigComment("ENFORCE_INTERFACE", nil, nil, nil, defaults.enforceInterface),
                        get = function(_)
                            return self:GetProperty("enforceInterface")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetProperty("enforceInterface")
                            local isValid = (type(value) == "boolean")
                            if isValid then
                                self:SetProperty("enforceInterface", nil, value)
                                self:PrintChangedValue(self.locale["TITLE_ENFORCE_INTERFACE"], oldValue, value)
                            end
                        end
                    }
                }
            },
            presenceOptions = {
                type = "group", order = self:GetNextIndex(),
                name = self.locale["CATEGORY_TITLE_PRESENCE"], desc = self.locale["CATEGORY_COMMENT_PRESENCE"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = self:GenerateDynamicTable("presence", self.locale["CATEGORY_TITLE_PRESENCE_EXTENDED"],
                    function(count)
                        return strformat(self.locale["CATEGORY_COMMENT_PRESENCE_INFO"], count, (count == 1 and "") or "s")
                    end
                )
            },
            buttonOptions = {
                type = "group", order = self:GetNextIndex(),
                name = self.locale["CATEGORY_TITLE_BUTTONS"], desc = self.locale["CATEGORY_COMMENT_BUTTONS"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = self:GenerateDynamicTable("buttons", self.locale["CATEGORY_TITLE_BUTTONS_EXTENDED"],
                    function(count)
                        return strformat(self.locale["CATEGORY_COMMENT_BUTTONS_INFO"], count, (count == 1 and "") or "s")
                    end,
                    nil,
                    function(self, newValue)
                        return not self:FindMatches(tostring(newValue), self.internals.rpc.buttonsSplitKey, false)
                    end,
                    function(self, fieldName, _, _)
                        self:PrintErrorMessage(
                            strformat(self.locale["ERROR_STRING_DEFAULT"], fieldName)
                        )
                    end
                )
            },
            labelOptions = {
                type = "group", order = self:GetNextIndex(),
                name = self.locale["CATEGORY_TITLE_LABELS"], desc = self.locale["CATEGORY_COMMENT_LABELS"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = self:GenerateDynamicTable("labels", self.locale["CATEGORY_TITLE_LABELS_EXTENDED"],
                    function(count)
                        return strformat(self.locale["CATEGORY_COMMENT_LABELS_INFO"], count, (count == 1 and "") or "s")
                    end
                )
            },
            placeholderOptions = {
                type = "group", order = self:GetNextIndex(),
                name = self.locale["CATEGORY_TITLE_PLACEHOLDERS"], desc = self.locale["CATEGORY_COMMENT_PLACEHOLDERS"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = self:GenerateDynamicTable("placeholders", self.locale["CATEGORY_TITLE_PLACEHOLDERS_EXTENDED"],
                    function(count)
                        return strformat(self.locale["CATEGORY_COMMENT_PLACEHOLDERS_INFO"], count,
                            (count == 1 and "") or "s")
                    end
                )
            },
            eventOptions = {
                type = "group", order = self:GetNextIndex(),
                name = self.locale["CATEGORY_TITLE_EVENTS"], desc = self.locale["CATEGORY_COMMENT_EVENTS"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = self:GenerateDynamicTable("events", self.locale["CATEGORY_TITLE_EVENTS_EXTENDED"],
                    function(count)
                        return strformat(self.locale["CATEGORY_COMMENT_EVENTS_INFO"], count, (count == 1 and "") or "s")
                    end,
                    function(root)
                        root:SyncEvents(root:GetProperty("events"), root:GetProperty("debugMode"))
                    end
                )
            },
            metricsOptions = {
                type = "group", order = self:GetNextIndex(),
                name = self.locale["CATEGORY_TITLE_METRICS"], desc = self.locale["CATEGORY_COMMENT_METRICS"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = self:GenerateDynamicTable("metrics", self.locale["CATEGORY_TITLE_METRICS_EXTENDED"],
                    function(count)
                        return strformat(self.locale["CATEGORY_COMMENT_METRICS_INFO"], count, (count == 1 and "") or "s")
                    end,
                    function(root)
                        root:SyncAnalytics(root:GetProperty("metrics"))
                    end
                )
            },
            extraOptions = {
                type = "group", order = self:GetNextIndex(),
                name = self.locale["CATEGORY_TITLE_EXTRA"], desc = self.locale["CATEGORY_COMMENT_EXTRA"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = {
                    debugMode = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = self:GetConfigTitle("DEBUG_MODE"),
                        desc = self:GetConfigComment("DEBUG_MODE", nil, nil, nil, defaults.debugMode),
                        get = function(_)
                            return self:GetProperty("debugMode")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetProperty("debugMode")
                            local isValid = (type(value) == "boolean")
                            if isValid then
                                self:SetProperty("debugMode", nil, value)
                                self:PrintChangedValue(self.locale["TITLE_DEBUG_MODE"], oldValue, value)
                            end
                        end
                    },
                    verboseMode = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = self:GetConfigTitle("VERBOSE_MODE"),
                        desc = self:GetConfigComment("VERBOSE_MODE", nil, nil, nil, defaults.verboseMode),
                        get = function(_)
                            return self:GetProperty("verboseMode")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetProperty("verboseMode")
                            local isValid = (type(value) == "boolean")
                            if isValid then
                                self:SetProperty("verboseMode", nil, value)
                                self:PrintChangedValue(self.locale["TITLE_VERBOSE_MODE"], oldValue, value)
                            end
                        end
                    },
                    blank1 = {
                        type = "description", order = self:GetNextIndex(), name = ""
                    },
                    queuedPipeline = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = self:GetConfigTitle("QUEUED_PIPELINE"),
                        desc = self:GetConfigComment("QUEUED_PIPELINE", nil, nil, nil, defaults.queuedPipeline),
                        get = function(_)
                            return self:GetProperty("queuedPipeline")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetProperty("queuedPipeline")
                            local isValid = (type(value) == "boolean")
                            if isValid then
                                self:SetProperty("queuedPipeline", nil, value)
                                self:PrintChangedValue(self.locale["TITLE_QUEUED_PIPELINE"], oldValue, value)
                            end
                        end
                    },
                    optionalMigrations = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = self:GetConfigTitle("OPTIONAL_MIGRATIONS"),
                        desc = self:GetConfigComment("OPTIONAL_MIGRATIONS", nil, nil, nil, defaults.optionalMigrations),
                        get = function(_)
                            return self:GetProperty("optionalMigrations")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetProperty("optionalMigrations")
                            local isValid = (type(value) == "boolean")
                            if isValid then
                                self:SetProperty("optionalMigrations", nil, value)
                                self:PrintChangedValue(self.locale["TITLE_OPTIONAL_MIGRATIONS"], oldValue, value)
                            end
                        end
                    },
                    blank2 = {
                        type = "description", order = self:GetNextIndex(), name = ""
                    },
                    callbackDelay = {
                        type = "range", order = self:GetNextIndex(), width = 1.50, step = 1,
                        min = self.locale["MINIMUM_CALLBACK_DELAY"], max = self.locale["MAXIMUM_CALLBACK_DELAY"],
                        name = self:GetConfigTitle("CALLBACK_DELAY"),
                        desc = self:GetConfigComment("CALLBACK_DELAY", nil, nil, nil, defaults.callbackDelay),
                        get = function(_)
                            return self:GetProperty("callbackDelay")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetProperty("callbackDelay")
                            local isValid = (self:IsWithinValue(
                                value,
                                self.locale["MINIMUM_CALLBACK_DELAY"], self.locale["MAXIMUM_CALLBACK_DELAY"],
                                true, true
                            ))
                            if isValid then
                                self:SetProperty("callbackDelay", nil, value)
                                self:PrintChangedValue(self.locale["TITLE_CALLBACK_DELAY"], oldValue, value)
                                if value <= 0 then
                                    self:PrintWarningMessage(
                                        strformat(self.locale["WARNING_VALUE_UNSAFE"],
                                            self.locale["TITLE_CALLBACK_DELAY"])
                                    )
                                end
                            else
                                self:PrintErrorMessage(
                                    strformat(self.locale["ERROR_RANGE_DEFAULT"], self.locale["TITLE_CALLBACK_DELAY"],
                                        self.locale["MINIMUM_CALLBACK_DELAY"], self.locale["MAXIMUM_CALLBACK_DELAY"])
                                )
                            end
                        end
                    },
                    frameClearDelay = {
                        type = "range", order = self:GetNextIndex(), width = 1.50, step = 1,
                        min = self.locale["MINIMUM_FRAME_CLEAR_DELAY"], max = self.locale["MAXIMUM_FRAME_CLEAR_DELAY"],
                        name = self:GetConfigTitle("FRAME_CLEAR_DELAY"),
                        desc = self:GetConfigComment("FRAME_CLEAR_DELAY", nil, nil, nil, defaults.frameClearDelay),
                        get = function(_)
                            return self:GetProperty("frameClearDelay")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetProperty("frameClearDelay")
                            local isValid = (self:IsWithinValue(
                                value,
                                self.locale["MINIMUM_FRAME_CLEAR_DELAY"], self.locale["MAXIMUM_FRAME_CLEAR_DELAY"],
                                true, true
                            ))
                            if isValid then
                                self:SetProperty("frameClearDelay", nil, value)
                                self:PrintChangedValue(self.locale["TITLE_FRAME_CLEAR_DELAY"], oldValue, value)
                                if value <= 0 then
                                    self:PrintWarningMessage(
                                        strformat(self.locale["WARNING_VALUE_UNSAFE"],
                                            self.locale["TITLE_FRAME_CLEAR_DELAY"])
                                    )
                                end
                            else
                                self:PrintErrorMessage(
                                    strformat(self.locale["ERROR_RANGE_DEFAULT"],
                                        self.locale["TITLE_FRAME_CLEAR_DELAY"],
                                        self.locale["MINIMUM_FRAME_CLEAR_DELAY"],
                                        self.locale["MAXIMUM_FRAME_CLEAR_DELAY"]
                                    )
                                )
                            end
                        end
                    },
                    blank3 = {
                        type = "description", order = self:GetNextIndex(), name = ""
                    },
                    frameSize = {
                        type = "range", order = self:GetNextIndex(), width = 1.50, step = 1,
                        min = self.locale["MINIMUM_FRAME_SIZE"], max = self.locale["MAXIMUM_FRAME_SIZE"],
                        name = self:GetConfigTitle("FRAME_SIZE"),
                        desc = self:GetConfigComment("FRAME_SIZE", nil, nil, nil, defaults.frameSize),
                        get = function(_)
                            return self:GetProperty("frameSize")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetProperty("frameSize")
                            local isValid = (self:IsWithinValue(
                                value,
                                self.locale["MINIMUM_FRAME_SIZE"], self.locale["MAXIMUM_FRAME_SIZE"],
                                true, true
                            ))
                            if isValid then
                                self:SetProperty("frameSize", nil, value)
                                self:PrintChangedValue(self.locale["TITLE_FRAME_SIZE"], oldValue, value)
                                if value <= 0 then
                                    self:PrintWarningMessage(
                                        strformat(self.locale["WARNING_VALUE_UNSAFE"], self.locale["TITLE_FRAME_SIZE"])
                                    )
                                end
                            else
                                self:PrintErrorMessage(
                                    strformat(self.locale["ERROR_RANGE_DEFAULT"], self.locale["TITLE_FRAME_SIZE"],
                                        self.locale["MINIMUM_FRAME_SIZE"], self.locale["MAXIMUM_FRAME_SIZE"])
                                )
                            end
                        end
                    }
                }
            },
            profiles = profilesGroup,
            about = {
                type = "group", order = self:GetNextIndex(),
                name = self.locale["CATEGORY_TITLE_ABOUT"], desc = self.locale["CATEGORY_COMMENT_ABOUT"],
                args = {
                    summary = {
                        type = "description", order = self:GetNextIndex(), width = "full", fontSize = "medium",
                        name = self.locale["ADDON_SUMMARY"],
                    },
                    creditsHeader = {
                        order = self:GetNextIndex(), type = "header", name = self.locale["ADDON_HEADER_CREDITS"]
                    },
                    description = {
                        type = "description", order = self:GetNextIndex(), fontSize = "medium",
                        name = self.locale["ADDON_DESCRIPTION"]
                    }
                }
            },
        },
    }
    -- We also reset the index after the options table is generated
    -- to ensure that everything is cleaned up nice and tidy
    self:ResetIndex()
    return opts
end

--- Retrieves whether or not logging changed data is allowed
--- @return boolean @ canLogChanges
function CraftPresence:CanLogChanges()
    return self:GetProperty("verboseMode")
end

--- Prints change data, if possible, using the specified parameters
---
--- @param fieldName string The config name the change belongs to
--- @param oldValue any The old value of the config variable
--- @param value any The new value of the config variable
--- @param ignoreMetrics boolean Whether or not to ignore metric submission, even if allowed to otherwise
function CraftPresence:PrintChangedValue(fieldName, oldValue, value, ignoreMetrics)
    oldValue = self:GetOrDefault(oldValue, self.locale["TYPE_NONE"])
    value = self:GetOrDefault(value, self.locale["TYPE_NONE"])
    ignoreMetrics = self:GetOrDefault(ignoreMetrics, false)
    if oldValue ~= value then
        if self:CanLogChanges() then
            self:Print(
                strformat(
                    self.locale["LOG_VERBOSE"], strformat(
                        self.locale["DEBUG_VALUE_CHANGED"], fieldName, tostring(oldValue), tostring(value)
                    )
                )
            )
        end
        if not ignoreMetrics then
            self:LogChangedValue(fieldName, oldValue, value)
        end
    end
end

--- Updates showMinimapIcon with the specified value
--- @param newValue boolean The new value to change showMinimapIcon to
function CraftPresence:UpdateMinimapSetting(newValue)
    local oldValue = self:GetProperty("showMinimapIcon")
    local isValid = (type(newValue) == "boolean")
    if isValid then
        self:SetProperty("showMinimapIcon", nil, newValue)
        self:UpdateMinimapState(true)
        self:PrintChangedValue(self.locale["TITLE_SHOW_MINIMAP_ICON"], oldValue, newValue)
    end
end

--- Modify the settings in the currently active config profile with the specified arguments
---
--- @param notify boolean Whether or not to fire NotifyChange after operation (Default: true)
--- @param reset boolean Whether or not to Reset this profile to it's defaults (Default: false)
--- @param tags table Optional Value depicting data sets to refresh, if supported
---
--- @return table @ profile
CraftPresence.UpdateProfile = CraftPresence:vararg(3, function(self, notify, reset, tags)
    local currentTOC = self:GetBuildInfo("toc_version")
    local canNotify = self:IsFeatureSupported("notifyOnChange", currentTOC)
    notify = self:GetOrDefault(notify, true)
    reset = self:GetOrDefault(reset, false)
    tags = self:GetOrDefault(tags, {})

    if reset then
        self:Print(self.locale["INFO_RESET_CONFIG"])
        self.db:ResetProfile(false, true)
        -- Assign Mandatory Values
        self:SetProperty("schema", nil, self:GetAddOnInfo("schema"))
    end

    if type(tags) ~= "table" then
        tags = { tags }
    end

    -- Additional dynamic sync events
    for _, tagName in pairs(tags) do
        tagName = strlower(tagName)

        if tagName == "all" or tagName == "events" then
            self:SyncEvents(self:GetProperty("events"), self:GetProperty("verboseMode"))
        end
        if tagName == "all" or tagName == "placeholders" or tagName == "buttons" or tagName == "labels" then
            self:SyncDynamicData(self:GetProperty("verboseMode"))
        end
    end

    if notify and canNotify then
        self.libraries.AceConfigRegistry:NotifyChange(self.internals.name)
    end

    if reset then
        return self:GetDefaults()
    else
        return self.db.profile
    end
end)
