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
local pairs, type = pairs, type
local strformat, min = string.format, math.min

--- Ensure Config Compatibility with the specified schema
--- (Only forwards-compatibility is supported here)
---
--- NOTE: current should be equal to target by end of method.
---
--- NOTE: A non-destructive and destructive passthrough is available with this method.
---       while non-destructive will avoid resetting/modifying current user data,
---       a destructive pass through ensures that new default settings are also passed through,
---       even if it means resetting that config field.
---
--- @param current number The current/former schema number (Default: 0)
--- @param target number The new schema number to convert this config against (Default: addOnInfo["schema"])
--- @param force boolean Whether to ignore the initial check between current and target (Default: false)
--- @param can_modify boolean Whether to allow modifying/resetting config data (Default: false)
--- @param log_output boolean Whether to allow logging for this function (Default: true)
function CraftPresence:EnsureCompatibility(current, target, force, can_modify, log_output)
    current = self:GetOrDefault(current, 0)
    target = self:GetOrDefault(target, self:GetAddOnInfo()["schema"])
    force = self:GetOrDefault(force, false)
    can_modify = self:GetOrDefault(can_modify, false)
    log_output = self:GetOrDefault(log_output, true)

    if force or current < target then
        if log_output then
            self:Print(strformat(self.locale["INFO_OUTDATED_CONFIG"], current, target))
        end
        local defaults = self:GetDefaults().profile

        if self:IsWithinValue(current, 0, 1, true, true) then
            -- Schema Changes (v0 -> v1):
            --  events[k].ignoreCallback has been renamed to events[k].processCallback
            --  We want to rename this accordingly to prevent losing data
            local events = self:GetProperty("events")
            for k, v in pairs(events) do
                if type(v) == "table" and v.ignoreCallback ~= nil then
                    v.processCallback = v.ignoreCallback
                    v.ignoreCallback = nil
                end
                events[k] = v
            end
            self:SetProperty("events", nil, events)
            current = 1
        end

        if self:IsWithinValue(current, 1, 2, true, true) then
            -- Schema Changes (v1 -> v2):
            --   Table renamed from customPlaceholders to placeholders
            --   The data field is renamed to processCallback
            --   The type field is renamed to processType
            --   Added multiple new fields for placeholders and events to allow better configurability
            local placeholders = self:GetProperty("customPlaceholders")
            if placeholders ~= nil then
                for k, v in pairs(placeholders) do
                    if type(v) == "table" then
                        v.processCallback = self:GetOrDefault(v.processCallback)
                        v.processType = self:GetOrDefault(v.processType, "string")
                        if v.data ~= nil then
                            v.processCallback = v.data
                            v.data = nil
                        end
                        if v.type ~= nil then
                            v.processType = v.type
                            v.type = nil
                        end
                        v.enabled = self:GetOrDefault(v.enabled, true)
                        v.prefix = self:GetOrDefault(v.prefix)
                        v.tagCallback = self:GetOrDefault(v.tagCallback)
                        v.tagType = self:GetOrDefault(v.tagType, "string")
                        v.minimumTOC = self:GetOrDefault(v.minimumTOC)
                        v.maximumTOC = self:GetOrDefault(v.maximumTOC)
                        v.registerCallback = self:GetOrDefault(v.registerCallback)
                    end
                    placeholders[k] = v
                end

                placeholders = self:CombineTables(
                        defaults.placeholders, placeholders
                )

                local old_global_prefix = self:GetOrDefault(self:GetProperty("globalPlaceholderKey"))
                local old_inner_prefix = self:GetOrDefault(self:GetProperty("innerPlaceholderKey"))
                if self:GetProperty("defaultPlaceholderMessage") ~= nil then
                    placeholders["default"].prefix = old_global_prefix
                    placeholders["default"].processType = "string"
                    placeholders["default"].processCallback = self:GetProperty("defaultPlaceholderMessage")
                    self:SetProperty("defaultPlaceholderMessage", nil, nil)
                end
                if self:GetProperty("arenaPlaceholderMessage") ~= nil then
                    placeholders["arena"].prefix = old_global_prefix
                    placeholders["arena"].processType = "string"
                    placeholders["arena"].processCallback = self:GetProperty("arenaPlaceholderMessage")
                    self:SetProperty("arenaPlaceholderMessage", nil, nil)
                end
                if self:GetProperty("battlegroundPlaceholderMessage") ~= nil then
                    placeholders["battleground"].prefix = old_global_prefix
                    placeholders["battleground"].processType = "string"
                    placeholders["battleground"].processCallback = self:GetProperty("battlegroundPlaceholderMessage")
                    self:SetProperty("battlegroundPlaceholderMessage", nil, nil)
                end
                if self:GetProperty("raidPlaceholderMessage") ~= nil then
                    placeholders["raid"].prefix = old_global_prefix
                    placeholders["raid"].processType = "string"
                    placeholders["raid"].processCallback = self:GetProperty("raidPlaceholderMessage")
                    self:SetProperty("raidPlaceholderMessage", nil, nil)
                end
                if self:GetProperty("dungeonPlaceholderMessage") ~= nil then
                    placeholders["dungeon"].prefix = old_global_prefix
                    placeholders["dungeon"].processType = "string"
                    placeholders["dungeon"].processCallback = self:GetProperty("dungeonPlaceholderMessage")
                    self:SetProperty("dungeonPlaceholderMessage", nil, nil)
                end

                for k, v in pairs(placeholders) do
                    if v.prefix == self.locale["DEFAULT_INNER_KEY"] then
                        placeholders[k].prefix = old_inner_prefix
                    end
                end

                self:SetProperty("placeholders", nil, placeholders)
                self:SetProperty("customPlaceholders", nil, nil)
            end
            current = 2
        end

        if self:IsWithinValue(current, 2, 3, true, true) then
            -- Schema Changes (v2 -> v3):
            --   Adds allowRebasedApi flag for events and placeholders
            local data = self:GetProperty("placeholders")
            local default = defaults.placeholders
            for k, _ in pairs(data) do
                data[k].allowRebasedApi = self:GetOrDefault(
                        data[k].allowRebasedApi,
                        self:GetOrDefault((default[k] and default[k].allowRebasedApi), false)
                )
            end
            self:SetProperty("placeholders", nil, data)

            data = self:GetProperty("events")
            default = defaults.events
            for k, _ in pairs(data) do
                data[k].allowRebasedApi = self:GetOrDefault(
                        data[k].allowRebasedApi,
                        self:GetOrDefault((default[k] and default[k].allowRebasedApi), false)
                )
            end
            self:SetProperty("events", nil, data)
            current = 3
        end

        if self:IsWithinValue(current, 3, 4.1, true, true) then
            -- Schema Changes (v3 -> v4):
            --   buttons:label,url renamed to buttons:labelCallback,urlCallback
            --   labels:active,inactive renamed to labels:activeCallback,inactiveCallback
            -- Schema Changes (v4 -> v4.1):
            --   Added `stateCallback` field for `labels` (Ported from `GetUnitStatus`)
            --   Added `suffix` field for `placeholders` (Defaults to what your `prefix` is to preserve data)
            --
            -- Destructive Changes (v3 -> v4.1):
            --   `events.CHAT_MSG_SYSTEM`:
            --      - Added a GetOrDefault call to `processCallback` to ensure fallback to `DEFAULT_XXX_MESSAGE`
            --   `placeholders.player_alliance`
            --      - `minimumTOC` flag removed, was previously marked as supporting rebased/3.2.0 or above
            --      - Added a nil check for the name in `processCallback` to allow TBC and Vanilla Wow client support
            local buttonData = self:GetProperty("buttons")
            if buttonData ~= nil then
                for k, v in pairs(buttonData) do
                    if type(v) == "table" then
                        v.labelCallback = self:GetOrDefault(v.labelCallback)
                        v.labelType = self:GetOrDefault(v.labelType, "string")
                        v.urlCallback = self:GetOrDefault(v.urlCallback)
                        v.urlType = self:GetOrDefault(v.urlType, "string")
                        if v.label ~= nil then
                            v.labelCallback = v.label
                            v.label = nil
                        end
                        if v.url ~= nil then
                            v.urlCallback = v.url
                            v.url = nil
                        end
                        v.enabled = self:GetOrDefault(v.enabled, true)
                        v.minimumTOC = self:GetOrDefault(v.minimumTOC)
                        v.maximumTOC = self:GetOrDefault(v.maximumTOC)
                        v.allowRebasedApi = self:GetOrDefault(v.allowRebasedApi, true)
                    end
                    buttonData[k] = v
                end
                self:SetProperty("buttons", nil, buttonData)
            end
            local labelData = self:GetProperty("labels")
            local defaultLabels = defaults.labels
            if labelData ~= nil then
                for k, v in pairs(labelData) do
                    if type(v) == "table" then
                        v.activeCallback = self:GetOrDefault(v.activeCallback)
                        v.activeType = self:GetOrDefault(v.activeType, "string")
                        v.inactiveCallback = self:GetOrDefault(v.inactiveCallback)
                        v.inactiveType = self:GetOrDefault(v.inactiveType, "string")
                        if v.active ~= nil then
                            v.activeCallback = v.active
                            v.active = nil
                        end
                        if v.inactive ~= nil then
                            v.inactiveCallback = v.inactive
                            v.inactive = nil
                        end
                        v.stateCallback = self:GetOrDefault(
                                v.stateCallback,
                                self:GetOrDefault(defaultLabels[k] and defaultLabels[k].stateCallback)
                        )
                        v.enabled = self:GetOrDefault(v.enabled, true)
                        v.minimumTOC = self:GetOrDefault(v.minimumTOC)
                        v.maximumTOC = self:GetOrDefault(v.maximumTOC)
                        v.allowRebasedApi = self:GetOrDefault(v.allowRebasedApi, true)
                    end
                    labelData[k] = v
                end
                self:SetProperty("labels", nil, labelData)
            end
            local placeholderData = self:GetProperty("placeholders")
            if placeholderData ~= nil then
                for k, v in pairs(placeholderData) do
                    if type(v) == "table" then
                        v.suffix = self:GetOrDefault(
                                v.suffix,
                                self:GetOrDefault(v.prefix)
                        )
                    end
                    placeholderData[k] = v
                end
                self:SetProperty("placeholders", nil, placeholderData)
            end

            if current ~= 4.1 then
                if can_modify then
                    self:SetProperty("events", "CHAT_MSG_SYSTEM", nil, true)
                    self:SetProperty("placeholders", "player_alliance", nil, true)
                elseif log_output then
                    self:PrintMigrationMessage(current, target)
                end
                current = 4.1
            end
        end

        if self:IsWithinValue(current, 4.1, 4.5, true, true) then
            -- Schema Changes (v4.1 -> v4.5):
            --   Renamed `time:start` and `time:end` to match their actual variable names in order to consolodate logic
            --   Rich Presence Fields have been moved into their own tab and made dynamic
            local placeholderData = self:GetProperty("placeholders")
            if placeholderData ~= nil then
                for k, v in pairs(placeholderData) do
                    if type(v) == "table" then
                        v.tagCallback = self:Replace(v.tagCallback, "time:start", "time_start", true)
                        v.tagCallback = self:Replace(v.tagCallback, "time:end", "time_end", true)
                    end
                    placeholderData[k] = v
                end
                self:SetProperty("placeholders", nil, placeholderData)
            end

            local presenceData = self:GetProperty("presence")
            local defaultPresence = defaults.presence
            if presenceData ~= nil then
                local newLargeImage = presenceData["largeImage"]
                local defaultLargeImage = defaultPresence["largeImage"]
                newLargeImage.keyCallback = self:GetOrDefault(
                        self:GetProperty("largeImageKey"), defaultLargeImage.keyCallback
                )
                self:SetProperty("largeImageKey", nil, nil)
                newLargeImage.messageCallback = self:GetOrDefault(
                        self:GetProperty("largeImageMessage"), defaultLargeImage.messageCallback
                )
                self:SetProperty("largeImageMessage", nil, nil)
                presenceData["largeImage"] = newLargeImage

                local newSmallImage = presenceData["smallImage"]
                local defaultSmallImage = defaultPresence["smallImage"]
                newSmallImage.keyCallback = self:GetOrDefault(
                        self:GetProperty("smallImageKey"), defaultSmallImage.keyCallback
                )
                self:SetProperty("smallImageKey", nil, nil)
                newSmallImage.messageCallback = self:GetOrDefault(
                        self:GetProperty("smallImageMessage"), defaultSmallImage.messageCallback
                )
                self:SetProperty("smallImageMessage", nil, nil)
                presenceData["smallImage"] = newSmallImage

                presenceData["state"].messageCallback = self:GetOrDefault(
                        self:GetProperty("gameStateMessage"), defaultPresence["state"].messageCallback
                )
                self:SetProperty("gameStateMessage", nil, nil)
                presenceData["details"].messageCallback = self:GetOrDefault(
                        self:GetProperty("detailsMessage"), defaultPresence["details"].messageCallback
                )
                self:SetProperty("detailsMessage", nil, nil)

                self:SetProperty("presence", nil, presenceData)
            end
            current = 4.5
        end

        self:SetProperty("schema", nil, min(current, target))
        self:UpdateProfile(true, false, "all")
    end
end