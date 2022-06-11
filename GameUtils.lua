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
local strformat, strupper, tostring = string.format, string.upper, tostring
local tinsert, tconcat = table.insert, table.concat
local pairs, type, select = pairs, type, select

-- GAME GETTERS AND SETTERS

local hasInstanceChanged, lastInstanceState, lastAreaName
local cachedUnitData = {}

--- Retrieves the Player Status for the specified unit
---
--- @param unit string The unit to interpret (Default: player)
--- @param sync boolean Whether to sync the resulting status to cachedUnitData[unit] (Default: true)
--- @param refresh boolean Whether to sync dynamic data before method execution (Default: false)
--- @param prefixFormat string Optional argument to determine the prefix formatting (Default: self.locale["FORMAT_USER_PREFIX"])
--- @param unitData table Optional argument for forcing certain unit status data
--- @param separator boolean Optional argument for specifying how unitInfo is segmented into unitData.status (Default: ",")
--- @param index number Optional argument that, if specified, will only use this argument from detected unitInfo
---
--- @return table @ unitData
function CraftPresence:GetUnitStatus(unit, sync, refresh, prefixFormat, unitData, separator, index)
    unit = self:GetOrDefault(unit, "player")
    sync = self:GetOrDefault(sync, true)
    refresh = self:GetOrDefault(refresh, false)
    prefixFormat = self:GetOrDefault(prefixFormat, self.locale["FORMAT_USER_PREFIX"])
    unitData = self:GetOrDefault(unitData, cachedUnitData[unit] or {})
    separator = self:GetOrDefault(separator, ",")

    local unitInfo = {}
    local unitString = ""

    -- Ensure labels are synced, if allowed
    if refresh then
        self:SyncDynamicData(self:GetProperty("verboseMode"))
    end

    -- Prepare (Or reset if present) an info list for unitData to list available messages
    unitData.info = {}

    for key, value in pairs(self.labels) do
        if self:ShouldProcessData(value) then
            unitData[key] = value.state
            unitString = value[self.locale["STATUS_" .. strupper(tostring(value.state))]]
            if not self:IsNullOrEmpty(unitString) then
                tinsert(unitInfo, unitString)
                unitData.info[key] = unitString
            end
        end
    end

    -- Sync Player Status with unitInfo data
    unitData.full_status = tconcat(unitInfo, separator)
    if type(index) == "number" and index >= 1 and self:GetLength(unitInfo) >= index then
        unitData.status = self:GetOrDefault(unitInfo[index])
    else
        unitData.status = unitData.full_status
    end
    unitData.reason = self:GetOrDefault(unitData.reason or
        (cachedUnitData[unit] and cachedUnitData[unit].reason)
    )

    -- Parse Player Status
    if not self:IsNullOrEmpty(unitData.status) then
        unitData.prefix = strformat(prefixFormat, unitData.status)
    else
        unitData.status = self.locale["TYPE_UNKNOWN"]
        unitData.prefix = ""
        unitData.reason = ""
    end

    -- Return Data (Will also cache data if allowed)
    if sync then
        if unitData.last_status ~= unitData.full_status then
            unitData.last_status = unitData.full_status
        end
        cachedUnitData[unit] = unitData
    end
    return unitData
end

--- Retrieves whether the instance has (or should be marked as) recently changed
--- @return boolean @ hasInstanceChanged
function CraftPresence:HasInstanceChanged()
    local has_changed, areaName, instanceState = false, self:GetCurrentInstanceName(true), nil
    if self:IsRebasedApi() or self:GetBuildInfo("toc_version") >= self:GetCompatibilityInfo("3.0.0") then
        instanceState = select(2, GetInstanceInfo())
    end

    has_changed = (((lastInstanceState == nil) or (instanceState ~= lastInstanceState)) or
        ((lastAreaName == nil) or (areaName ~= lastAreaName)))
    if has_changed then
        lastInstanceState = instanceState
        lastAreaName = areaName
    end
    return self:GetOrDefault(hasInstanceChanged or has_changed, false)
end

--- Sets whether the instance has (or should be marked as) recently changed
---
--- @param value boolean Whether the instance should be marked as changed (Required)
function CraftPresence:SetInstanceChanged(value)
    if self:IsNullOrEmpty(value) then return end
    hasInstanceChanged = value
end

--- Retrieves the Cached Unit Data of the specified unit, if any
---
--- @param unit string The unit to interpret
---
--- @return table @ unitData
function CraftPresence:GetUnitData(unit)
    unit = self:GetOrDefault(unit, "player")
    return cachedUnitData[unit] or {}
end

--- Sets a key,value pair within cachedPlayerData, for later usage
---
--- @param unit string The unit to interpret
--- @param key string The key to insert to the table
--- @param value any The value to insert to the table
---
--- @return table @ cachedPlayerData
function CraftPresence:SetCachedUnitData(unit, key, value)
    unit = self:GetOrDefault(unit, "player")
    if not (self:IsNullOrEmpty(key) or self:IsNullOrEmpty(value)) then
        key = self:TrimString(key)
        value = self:TrimString(value)
        cachedUnitData[unit][key] = value
    end
    return cachedUnitData[unit]
end
