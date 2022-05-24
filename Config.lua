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

-- Addon APIs
local LibStub = LibStub

local L = CraftPresence.locale
local config_registry = LibStub("AceConfigRegistry-3.0")

--- Retrieves the option table to be used in the Config Menu
--- @return table @ opts
function CraftPresence:GetOptions()
    -- It is necesary to have this value, as calling it with self implied
    -- Causes NotifyChange to fail from time to time
    local self = CraftPresence
    local profilesGroup = LibStub("AceDBOptions-3.0"):GetOptionsTable(self.db)

    -- Ensure Ordering is Correct, by resetting the index before generating the options
    self:ResetIndex()

    local opts = {
        type = "group", childGroups = "tab",
        name = self:GetAddOnInfo()["versionString"],
        get = function(info)
            return self.db.profile[info[self:GetLength(info)]]
        end,
        set = function(info, value)
            self.db.profile[info[self:GetLength(info)]] = value
        end,
        args = {
            generalOptions = {
                type = "group", order = self:GetNextIndex(),
                name = L["CATEGORY_TITLE_GENERAL"], desc = L["CATEGORY_COMMENT_GENERAL"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = {
                    clientId = {
                        type = "input", order = self:GetNextIndex(), width = 1.25,
                        name = L["TITLE_CLIENT_ID"],
                        desc = self:GetConfigComment("CLIENT_ID"),
                        usage = L["USAGE_CLIENT_ID"],
                        get = function(_)
                            return self:GetFromDb("clientId")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("clientId")
                            local isValid = (
                                    value ~= nil and
                                            self:ContainsDigit(value) and
                                            self:GetLength(value) == 18
                            )
                            if isValid then
                                self.db.profile.clientId = value
                                self:PrintChangedValue(L["TITLE_CLIENT_ID"], oldValue, value)
                            else
                                self:PrintErrorMessage(L["ERROR_CLIENT_ID"])
                            end
                        end
                    },
                    blank1 = {
                        type = "description", order = self:GetNextIndex(), name = ""
                    },
                    gameStateMessage = {
                        type = "input", order = self:GetNextIndex(), width = 3.0,
                        name = L["TITLE_GAME_STATE_MESSAGE"],
                        desc = self:GetConfigComment("GAME_STATE_MESSAGE"),
                        usage = L["USAGE_GAME_STATE_MESSAGE"],
                        get = function(_)
                            return self:GetFromDb("gameStateMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("gameStateMessage")
                            local isValid = (type(value) == "string")
                            if isValid then
                                self.db.profile.gameStateMessage = value
                                self:PrintChangedValue(L["TITLE_GAME_STATE_MESSAGE"], oldValue, value)
                            end
                        end
                    },
                    blank2 = {
                        type = "description", order = self:GetNextIndex(), name = ""
                    },
                    detailsMessage = {
                        type = "input", order = self:GetNextIndex(), width = 3.0,
                        name = L["TITLE_DETAILS_MESSAGE"],
                        desc = self:GetConfigComment("DETAILS_MESSAGE"),
                        usage = L["USAGE_DETAILS_MESSAGE"],
                        get = function(_)
                            return self:GetFromDb("detailsMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("detailsMessage")
                            local isValid = (type(value) == "string")
                            if isValid then
                                self.db.profile.detailsMessage = value
                                self:PrintChangedValue(L["TITLE_DETAILS_MESSAGE"], oldValue, value)
                            end
                        end
                    },
                    blank3 = {
                        type = "description", order = self:GetNextIndex(), name = ""
                    },
                    largeImageKey = {
                        type = "input", order = self:GetNextIndex(), width = 1.50,
                        name = L["TITLE_LARGE_IMAGE_KEY"],
                        desc = self:GetConfigComment("LARGE_IMAGE_KEY"),
                        usage = L["USAGE_LARGE_IMAGE_KEY"],
                        get = function(_)
                            return self:GetFromDb("largeImageKey")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("largeImageKey")
                            local isValid = (type(value) == "string") and self:GetLength(value) <= 32
                            if isValid then
                                self.db.profile.largeImageKey = value
                                self:PrintChangedValue(L["TITLE_LARGE_IMAGE_KEY"], oldValue, value)
                            else
                                self:PrintErrorMessage(L["ERROR_IMAGE_KEY"])
                            end
                        end
                    },
                    smallImageKey = {
                        type = "input", order = self:GetNextIndex(), width = 1.50,
                        name = L["TITLE_SMALL_IMAGE_KEY"],
                        desc = self:GetConfigComment("SMALL_IMAGE_KEY"),
                        usage = L["USAGE_SMALL_IMAGE_KEY"],
                        get = function(_)
                            return self:GetFromDb("smallImageKey")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("smallImageKey")
                            local isValid = (type(value) == "string") and self:GetLength(value) <= 32
                            if isValid then
                                self.db.profile.smallImageKey = value
                                self:PrintChangedValue(L["TITLE_SMALL_IMAGE_KEY"], oldValue, value)
                            else
                                self:PrintErrorMessage(L["ERROR_IMAGE_KEY"])
                            end
                        end
                    },
                    blank4 = {
                        type = "description", order = self:GetNextIndex(), name = ""
                    },
                    largeImageMessage = {
                        type = "input", order = self:GetNextIndex(), width = 3.0,
                        name = L["TITLE_LARGE_IMAGE_MESSAGE"],
                        desc = self:GetConfigComment("LARGE_IMAGE_MESSAGE"),
                        usage = L["USAGE_LARGE_IMAGE_MESSAGE"],
                        get = function(_)
                            return self:GetFromDb("largeImageMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("largeImageMessage")
                            local isValid = (type(value) == "string")
                            if isValid then
                                self.db.profile.largeImageMessage = value
                                self:PrintChangedValue(L["TITLE_LARGE_IMAGE_MESSAGE"], oldValue, value)
                            end
                        end
                    },
                    blank5 = {
                        type = "description", order = self:GetNextIndex(), name = ""
                    },
                    smallImageMessage = {
                        type = "input", order = self:GetNextIndex(), width = 3.0,
                        name = L["TITLE_SMALL_IMAGE_MESSAGE"],
                        desc = self:GetConfigComment("SMALL_IMAGE_MESSAGE"),
                        usage = L["USAGE_SMALL_IMAGE_MESSAGE"],
                        get = function(_)
                            return self:GetFromDb("smallImageMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("smallImageMessage")
                            local isValid = (type(value) == "string")
                            if isValid then
                                self.db.profile.smallImageMessage = value
                                self:PrintChangedValue(L["TITLE_SMALL_IMAGE_MESSAGE"], oldValue, value)
                            end
                        end
                    }
                }
            },
            buttonOptions = {
                type = "group", order = self:GetNextIndex(),
                name = L["CATEGORY_TITLE_BUTTONS"], desc = L["CATEGORY_COMMENT_BUTTONS"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = self:GetPlaceholderArgs("buttons", L["CATEGORY_TITLE_BUTTONS_EXTENDED"],
                        function(count)
                            return strformat(L["CATEGORY_COMMENT_BUTTONS_INFO"], count, (count == 1 and "") or "s")
                        end,
                        nil,
                        function(self, newValue)
                            return not self:FindMatches(tostring(newValue), L["ARRAY_SPLIT_KEY"], false)
                        end,
                        function(self, fieldName, _, _)
                            self:PrintErrorMessage(
                                    strformat(L["ERROR_STRING_DEFAULT"], fieldName)
                            )
                        end
                )
            },
            labelOptions = {
                type = "group", order = self:GetNextIndex(),
                name = L["CATEGORY_TITLE_LABELS"], desc = L["CATEGORY_COMMENT_LABELS"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = self:GetPlaceholderArgs("labels", L["CATEGORY_TITLE_LABELS_EXTENDED"],
                        function(count)
                            return strformat(L["CATEGORY_COMMENT_LABELS_INFO"], count, (count == 1 and "") or "s")
                        end
                )
            },
            placeholderOptions = {
                type = "group", order = self:GetNextIndex(),
                name = L["CATEGORY_TITLE_PLACEHOLDERS"], desc = L["CATEGORY_COMMENT_PLACEHOLDERS"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = self:GetPlaceholderArgs("placeholders", L["CATEGORY_TITLE_PLACEHOLDERS_EXTENDED"],
                        function(count)
                            return strformat(L["CATEGORY_COMMENT_PLACEHOLDERS_INFO"], count, (count == 1 and "") or "s")
                        end
                )
            },
            eventOptions = {
                type = "group", order = self:GetNextIndex(),
                name = L["CATEGORY_TITLE_EVENTS"], desc = L["CATEGORY_COMMENT_EVENTS"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = self:GetPlaceholderArgs("events", L["CATEGORY_TITLE_EVENTS_EXTENDED"],
                        function(count)
                            return strformat(L["CATEGORY_COMMENT_EVENTS_INFO"], count, (count == 1 and "") or "s")
                        end,
                        function(root)
                            root:SyncEvents(root:GetFromDb("events"), root:GetFromDb("debugMode"))
                        end
                )
            },
            metricsOptions = {
                type = "group", order = self:GetNextIndex(),
                name = L["CATEGORY_TITLE_METRICS"], desc = L["CATEGORY_COMMENT_METRICS"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = self:GetPlaceholderArgs("metrics", L["CATEGORY_TITLE_METRICS_EXTENDED"],
                        function(count)
                            return strformat(L["CATEGORY_COMMENT_METRICS_INFO"], count, (count == 1 and "") or "s")
                        end
                )
            },
            extraOptions = {
                type = "group", order = self:GetNextIndex(),
                name = L["CATEGORY_TITLE_EXTRA"], desc = L["CATEGORY_COMMENT_EXTRA"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = {
                    debugMode = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = L["TITLE_DEBUG_MODE"],
                        desc = self:GetConfigComment("DEBUG_MODE"),
                        get = function(_)
                            return self:GetFromDb("debugMode")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("debugMode")
                            local isValid = (type(value) == "boolean")
                            if isValid then
                                self.db.profile.debugMode = value
                                self:PrintChangedValue(L["TITLE_DEBUG_MODE"], oldValue, value)
                            end
                        end
                    },
                    verboseMode = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = L["TITLE_VERBOSE_MODE"],
                        desc = self:GetConfigComment("VERBOSE_MODE"),
                        get = function(_)
                            return self:GetFromDb("verboseMode")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("verboseMode")
                            local isValid = (type(value) == "boolean")
                            if isValid then
                                self.db.profile.verboseMode = value
                                self:PrintChangedValue(L["TITLE_VERBOSE_MODE"], oldValue, value)
                            end
                        end
                    },
                    blank1 = {
                        type = "description", order = self:GetNextIndex(), name = ""
                    },
                    showMinimapIcon = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = L["TITLE_SHOW_MINIMAP_ICON"],
                        desc = self:GetConfigComment("SHOW_MINIMAP_ICON"),
                        get = function(_)
                            return self:GetFromDb("showMinimapIcon")
                        end,
                        set = function(_, value)
                            self:UpdateMinimapSetting(value)
                        end
                    },
                    queuedPipeline = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = L["TITLE_QUEUED_PIPELINE"],
                        desc = self:GetConfigComment("QUEUED_PIPELINE"),
                        get = function(_)
                            return self:GetFromDb("queuedPipeline")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("queuedPipeline")
                            local isValid = (type(value) == "boolean")
                            if isValid then
                                self.db.profile.queuedPipeline = value
                                self:PrintChangedValue(L["TITLE_QUEUED_PIPELINE"], oldValue, value)
                            end
                        end
                    },
                    blank2 = {
                        type = "description", order = self:GetNextIndex(), name = ""
                    },
                    showWelcomeMessage = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = L["TITLE_SHOW_WELCOME_MESSAGE"],
                        desc = self:GetConfigComment("SHOW_WELCOME_MESSAGE"),
                        get = function(_)
                            return self:GetFromDb("showWelcomeMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("showWelcomeMessage")
                            local isValid = (type(value) == "boolean")
                            if isValid then
                                self.db.profile.showWelcomeMessage = value
                                self:PrintChangedValue(L["TITLE_SHOW_WELCOME_MESSAGE"], oldValue, value)
                            end
                        end
                    },
                    optionalMigrations = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = L["TITLE_OPTIONAL_MIGRATIONS"],
                        desc = self:GetConfigComment("OPTIONAL_MIGRATIONS"),
                        get = function(_)
                            return self:GetFromDb("optionalMigrations")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("optionalMigrations")
                            local isValid = (type(value) == "boolean")
                            if isValid then
                                self.db.profile.optionalMigrations = value
                                self:PrintChangedValue(L["TITLE_OPTIONAL_MIGRATIONS"], oldValue, value)
                            end
                        end
                    },
                    blank3 = {
                        type = "description", order = self:GetNextIndex(), name = ""
                    },
                    callbackDelay = {
                        type = "range", order = self:GetNextIndex(), width = 1.50,
                        min = L["MINIMUM_CALLBACK_DELAY"], max = L["MAXIMUM_CALLBACK_DELAY"], step = 1,
                        name = L["TITLE_CALLBACK_DELAY"],
                        desc = self:GetConfigComment("CALLBACK_DELAY"),
                        get = function(_)
                            return self:GetFromDb("callbackDelay")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("callbackDelay")
                            local isValid = (self:IsWithinValue(
                                    value,
                                    L["MINIMUM_CALLBACK_DELAY"], L["MAXIMUM_CALLBACK_DELAY"],
                                    true, true
                            ))
                            if isValid then
                                self.db.profile.callbackDelay = value
                                self:PrintChangedValue(L["TITLE_CALLBACK_DELAY"], oldValue, value)
                                if value <= 0 then
                                    self:PrintWarningMessage(
                                            strformat(L["WARNING_VALUE_UNSAFE"], L["TITLE_CALLBACK_DELAY"])
                                    )
                                end
                            else
                                self:PrintErrorMessage(
                                        strformat(L["ERROR_RANGE_DEFAULT"], L["TITLE_CALLBACK_DELAY"],
                                                L["MINIMUM_CALLBACK_DELAY"], L["MAXIMUM_CALLBACK_DELAY"])
                                )
                            end
                        end
                    },
                    frameClearDelay = {
                        type = "range", order = self:GetNextIndex(), width = 1.50,
                        min = L["MINIMUM_FRAME_CLEAR_DELAY"], max = L["MAXIMUM_FRAME_CLEAR_DELAY"], step = 1,
                        name = L["TITLE_FRAME_CLEAR_DELAY"],
                        desc = self:GetConfigComment("FRAME_CLEAR_DELAY"),
                        get = function(_)
                            return self:GetFromDb("frameClearDelay")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("frameClearDelay")
                            local isValid = (self:IsWithinValue(
                                    value,
                                    L["MINIMUM_FRAME_CLEAR_DELAY"], L["MAXIMUM_FRAME_CLEAR_DELAY"],
                                    true, true
                            ))
                            if isValid then
                                self.db.profile.frameClearDelay = value
                                self:PrintChangedValue(L["TITLE_FRAME_CLEAR_DELAY"], oldValue, value)
                                if value <= 0 then
                                    self:PrintWarningMessage(
                                            strformat(L["WARNING_VALUE_UNSAFE"], L["TITLE_FRAME_CLEAR_DELAY"])
                                    )
                                end
                            else
                                self:PrintErrorMessage(
                                        strformat(L["ERROR_RANGE_DEFAULT"], L["TITLE_FRAME_CLEAR_DELAY"],
                                                L["MINIMUM_FRAME_CLEAR_DELAY"], L["MAXIMUM_FRAME_CLEAR_DELAY"])
                                )
                            end
                        end
                    },
                    blank4 = {
                        type = "description", order = self:GetNextIndex(), name = ""
                    },
                    frameSize = {
                        type = "range", order = self:GetNextIndex(), width = 1.50,
                        min = L["MINIMUM_FRAME_SIZE"], max = L["MAXIMUM_FRAME_SIZE"], step = 1,
                        name = L["TITLE_FRAME_SIZE"],
                        desc = self:GetConfigComment("FRAME_SIZE"),
                        get = function(_)
                            return self:GetFromDb("frameSize")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("frameSize")
                            local isValid = (self:IsWithinValue(
                                    value,
                                    L["MINIMUM_FRAME_SIZE"], L["MAXIMUM_FRAME_SIZE"],
                                    true, true
                            ))
                            if isValid then
                                self.db.profile.frameSize = value
                                self:PrintChangedValue(L["TITLE_FRAME_SIZE"], oldValue, value)
                                if value <= 0 then
                                    self:PrintWarningMessage(
                                            strformat(L["WARNING_VALUE_UNSAFE"], L["TITLE_FRAME_SIZE"])
                                    )
                                end
                            else
                                self:PrintErrorMessage(
                                        strformat(L["ERROR_RANGE_DEFAULT"], L["TITLE_FRAME_SIZE"],
                                                L["MINIMUM_FRAME_SIZE"], L["MAXIMUM_FRAME_SIZE"])
                                )
                            end
                        end
                    }
                }
            },
            profiles = profilesGroup,
            about = {
                type = "group", order = self:GetNextIndex(),
                name = L["CATEGORY_TITLE_ABOUT"], desc = L["CATEGORY_COMMENT_ABOUT"],
                args = {
                    summary = {
                        type = "description", order = self:GetNextIndex(), width = "full", fontSize = "medium",
                        name = L["ADDON_SUMMARY"],
                    },
                    creditsHeader = {
                        order = self:GetNextIndex(), type = "header", name = L["ADDON_HEADER_CREDITS"]
                    },
                    description = {
                        type = "description", order = self:GetNextIndex(), fontSize = "medium",
                        name = L["ADDON_DESCRIPTION"]
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
    return self:GetFromDb("verboseMode")
end

--- Prints change data, if possible, using the specified parameters
---
--- @param fieldName string The config name the change belongs to
--- @param oldValue any The old value of the config variable
--- @param value any The new value of the config variable
--- @param ignoreMetrics boolean Whether or not to ignore metric submission, even if allowed to otherwise
function CraftPresence:PrintChangedValue(fieldName, oldValue, value, ignoreMetrics)
    oldValue = self:GetOrDefault(oldValue, L["TYPE_NONE"])
    value = self:GetOrDefault(value, L["TYPE_NONE"])
    ignoreMetrics = self:GetOrDefault(ignoreMetrics, false)
    if oldValue ~= value then
        if self:CanLogChanges() then
            self:Print(
                    strformat(
                            L["LOG_VERBOSE"], strformat(
                                    L["DEBUG_VALUE_CHANGED"], fieldName, tostring(oldValue), tostring(value)
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
    local oldValue = self.db.profile.showMinimapIcon
    local isValid = (type(newValue) == "boolean")
    if isValid then
        self.db.profile.showMinimapIcon = newValue
        self:UpdateMinimapState(true)
        self:PrintChangedValue(L["TITLE_SHOW_MINIMAP_ICON"], oldValue, newValue)
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
    local canNotify = self:GetBuildInfo()["toc_version"] >= self:GetCompatibilityInfo()["2.0.0"] or self:IsRebasedApi()
    notify = self:GetOrDefault(notify, true)
    reset = self:GetOrDefault(reset, false)
    tags = self:GetOrDefault(tags, {})

    if reset then
        self:Print(L["INFO_RESET_CONFIG"])
        self.db:ResetProfile(false, true)
        -- Assign Mandatory Values
        self.db.profile.schema = self:GetAddOnInfo()["schema"]
    end

    if type(tags) ~= "table" then
        tags = { tags }
    end

    -- Additional dynamic sync events
    for _, tagName in pairs(tags) do
        tagName = strlower(tagName)

        if tagName == "all" or tagName == "events" then
            self:SyncEvents(self:GetFromDb("events"), self:GetFromDb("verboseMode"))
        end
        if tagName == "all" or tagName == "placeholders" or tagName == "buttons" or tagName == "labels" then
            self:SyncDynamicData(self:GetFromDb("verboseMode"))
        end
    end

    if notify and canNotify then
        config_registry:NotifyChange(L["ADDON_NAME"])
    end

    if reset then
        return self:GetDefaults()
    else
        return self.db.profile
    end
end)
