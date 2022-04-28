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

-- Addon APIs
local L = CraftPresence.locale

--- Ensure Config Compatibility with the specified schema
--- (Only forwards-compatibility is supported here)
---
--- NOTE: current should be equal to target by end of method.
---
--- @param current number The current/former schema number (Default: 0)
--- @param target number The new schema number to convert this config against (Default: addOnInfo["schema"]
--- @param log_output boolean Whether to allow logging for this function (Default: true)
function CraftPresence:EnsureCompatibility(current, target, log_output)
    current = self:GetOrDefault(current, 0)
    target = self:GetOrDefault(target, self:GetAddOnInfo()["schema"])
    log_output = self:GetOrDefault(log_output, true)

    if current < target then
        if log_output then
            self:Print(strformat(L["INFO_OUTDATED_CONFIG"], current, target))
        end

        if self:IsWithinValue(current, 0, 1, true, true) then
            -- Schema Changes (v0 -> v1):
            --  events[k].ignoreCallback has been renamed to events[k].processCallback
            --  We want to rename this accordingly to prevent losing data
            local events = self:GetFromDb("events")
            for k, v in pairs(events) do
                if type(v) == "table" and v.ignoreCallback ~= nil then
                    v.processCallback = v.ignoreCallback
                    v.ignoreCallback = nil
                end
                events[k] = v
            end
            self:SetToDb("events", nil, events)
            current = 1
        end

        if self:IsWithinValue(current, 1, 2, true, true) then
            -- Schema Changes (v1 -> v2):
            --   Table renamed from customPlaceholders to placeholders
            --   The data field is renamed to processCallback
            --   The type field is renamed to processType
            --   Added multiple new fields for placeholders and events to allow better configurability
            local placeholders = self:GetFromDb("customPlaceholders")
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
                        self:GetDefaults().profile.placeholders, placeholders
                )

                local old_global_prefix = self:GetOrDefault(self:GetFromDb("globalPlaceholderKey"))
                local old_inner_prefix = self:GetOrDefault(self:GetFromDb("innerPlaceholderKey"))
                if self:GetFromDb("defaultPlaceholderMessage") ~= nil then
                    placeholders["default"].prefix = old_global_prefix
                    placeholders["default"].processType = "string"
                    placeholders["default"].processCallback = self:GetFromDb("defaultPlaceholderMessage")
                    self:SetToDb("defaultPlaceholderMessage", nil, nil)
                end
                if self:GetFromDb("arenaPlaceholderMessage") ~= nil then
                    placeholders["arena"].prefix = old_global_prefix
                    placeholders["arena"].processType = "string"
                    placeholders["arena"].processCallback = self:GetFromDb("arenaPlaceholderMessage")
                    self:SetToDb("arenaPlaceholderMessage", nil, nil)
                end
                if self:GetFromDb("battlegroundPlaceholderMessage") ~= nil then
                    placeholders["battleground"].prefix = old_global_prefix
                    placeholders["battleground"].processType = "string"
                    placeholders["battleground"].processCallback = self:GetFromDb("battlegroundPlaceholderMessage")
                    self:SetToDb("battlegroundPlaceholderMessage", nil, nil)
                end
                if self:GetFromDb("raidPlaceholderMessage") ~= nil then
                    placeholders["raid"].prefix = old_global_prefix
                    placeholders["raid"].processType = "string"
                    placeholders["raid"].processCallback = self:GetFromDb("raidPlaceholderMessage")
                    self:SetToDb("raidPlaceholderMessage", nil, nil)
                end
                if self:GetFromDb("dungeonPlaceholderMessage") ~= nil then
                    placeholders["dungeon"].prefix = old_global_prefix
                    placeholders["dungeon"].processType = "string"
                    placeholders["dungeon"].processCallback = self:GetFromDb("dungeonPlaceholderMessage")
                    self:SetToDb("dungeonPlaceholderMessage", nil, nil)
                end

                for k, v in pairs(placeholders) do
                    if v.prefix == L["DEFAULT_INNER_KEY"] then
                        placeholders[k].prefix = old_inner_prefix
                    end
                end

                self:SetToDb("placeholders", nil, placeholders)
                self:SetToDb("customPlaceholders", nil, nil)
            end
            current = 2
        end

        if self:IsWithinValue(current, 2, 3, true, true) then
            -- Schema Changes (v2 -> v3):
            --   Adds allowRebasedApi flag for events and placeholders
            local data = self:GetFromDb("placeholders")
            local default = self:GetDefaults().profile.placeholders
            for k, _ in pairs(data) do
                data[k].allowRebasedApi = self:GetOrDefault(
                        data[k].allowRebasedApi,
                        self:GetOrDefault((default[k] and default[k].allowRebasedApi), false)
                )
            end
            self:SetToDb("placeholders", nil, data)

            data = self:GetFromDb("events")
            default = self:GetDefaults().profile.events
            for k, _ in pairs(data) do
                data[k].allowRebasedApi = self:GetOrDefault(
                        data[k].allowRebasedApi,
                        self:GetOrDefault((default[k] and default[k].allowRebasedApi), false)
                )
            end
            self:SetToDb("events", nil, data)
            current = 3
        end

        if self:IsWithinValue(current, 3, 4, true, true) then
            -- Schema Changes (v3 -> v4):
            --   buttons:label,url renamed to buttons:labelCallback,urlCallback
            --   labels:active,inactive renamed to labels:activeCallback,inactiveCallback
            local buttonData = self:GetFromDb("buttons")
            if buttonData ~= nil then
                for k,v in pairs(buttonData) do
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
                self:SetToDb("buttons", nil, buttonData)
            end
            local labelData = self:GetFromDb("labels")
            if labelData ~= nil then
                for k,v in pairs(labelData) do
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
                        v.enabled = self:GetOrDefault(v.enabled, true)
                        v.minimumTOC = self:GetOrDefault(v.minimumTOC)
                        v.maximumTOC = self:GetOrDefault(v.maximumTOC)
                        v.allowRebasedApi = self:GetOrDefault(v.allowRebasedApi, true)
                    end
                    labelData[k] = v
                end
                self:SetToDb("labels", nil, labelData)
            end
            current = 4
        end

        self:SetToDb("schema", nil, min(current, target))
    end
end