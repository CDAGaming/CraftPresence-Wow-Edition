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

-- Lua APIs
local pairs, type = pairs, type
local strformat = string.format

-- Addon APIs
local L = CraftPresence.locale

--- Ensure Config Compatibility with the specified schema
--- (Only forwards-compatibility is supported here)
---
--- @param from number The current/former schema number (Default: 0)
--- @param to number The new schema number to convert this config against (Default: addOnInfo["schema"]
--- @param log_output boolean Whether to allow logging for this function (Default: true)
function CraftPresence:EnsureCompatibility(from, to, log_output)
    from = self:GetOrDefault(from, 0)
    to = self:GetOrDefault(to, self:GetAddOnInfo()["schema"])
    log_output = self:GetOrDefault(log_output, true)

    if from < to then
        if log_output then
            self:Print(strformat(L["INFO_OUTDATED_CONFIG"], from, to))
        end

        if self:IsWithinValue(from, 0, 1, true, true) then
            -- Schema Changes (v0 -> v1):
            --  events[k].ignoreCallback has been renamed to events[k].processCallback
            --  We want to rename this accordingly to prevent losing data
            local events = self:GetFromDb("events")
            for k, v in pairs(events) do
                if type(v) == "table" and v.ignoreCallback ~= nil then
                    events[k].processCallback = v.ignoreCallback
                    events[k].ignoreCallback = nil
                end
            end
            self:SetToDb("events", nil, events)
        end
        self:SetToDb("schema", nil, to)
    end
end