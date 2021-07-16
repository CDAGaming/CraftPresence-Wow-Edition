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
local strformat = string.format
local tinsert, tconcat = table.insert, table.concat

-- Addon APIs
local L = CraftPresence.locale

--[[ GAME GETTERS AND SETTERS ]]--

local hasInstanceChanged, lastInstanceState, lastAreaName
local cachedUnitData = {}

--- Retrieves the Player Status for the specified unit
---
--- @param unit string The unit to interpret (Default: player)
--- @param refresh boolean Whether to sync the resulting status to unitData.last_status (Default: false)
--- @param sync boolean Whether to sync the resulting status to cachedUnitData[unit] (Default: true)
--- @param prefixFormat string Optional argument to determine the prefix formatting (Default: L["FORMAT_USER_PREFIX"])
--- @param unitData table Optional argument for forcing certain unit status data
---
--- @return table @ unitData
function CraftPresence:GetUnitStatus(unit, refresh, sync, prefixFormat, unitData)
    unit = self:GetOrDefault(unit, "player")
    refresh = self:GetOrDefault(refresh, false)
    sync = self:GetOrDefault(sync, true)
    prefixFormat = self:GetOrDefault(prefixFormat, L["FORMAT_USER_PREFIX"])
    unitData = self:GetOrDefault(unitData, {})

    unitData.away = self:GetOrDefault(
            unitData.away or (UnitIsAFK and UnitIsAFK(unit)),
            (cachedUnitData[unit] and cachedUnitData[unit].away) or false
    )
    unitData.busy = self:GetOrDefault(
            unitData.busy or (UnitIsDND and UnitIsDND(unit)),
            (cachedUnitData[unit] and cachedUnitData[unit].busy) or false
    )
    unitData.dead = self:GetOrDefault(
            unitData.dead or (UnitIsDead and UnitIsDead(unit)),
            (cachedUnitData[unit] and cachedUnitData[unit].dead) or false
    )
    unitData.ghost = self:GetOrDefault(
            unitData.ghost or (UnitIsGhost and UnitIsGhost(unit)),
            (cachedUnitData[unit] and cachedUnitData[unit].ghost) or false
    )

    -- Sync Player Name Tweaks
    local unitInfo = {}
    if unitData.away then
        tinsert(unitInfo, L["LABEL_AWAY"])
        if cachedUnitData[unit] and self:IsNullOrEmpty(cachedUnitData[unit].reason) then
            cachedUnitData[unit].reason = self:GetOrDefault(DEFAULT_AFK_MESSAGE)
        end
    elseif unitData.busy then
        tinsert(unitInfo, L["LABEL_BUSY"])
        if cachedUnitData[unit] and self:IsNullOrEmpty(cachedUnitData[unit].reason) then
            cachedUnitData[unit].reason = self:GetOrDefault(DEFAULT_DND_MESSAGE)
        end
    end
    if unitData.ghost then
        tinsert(unitInfo, L["LABEL_GHOST"])
    elseif unitData.dead then
        tinsert(unitInfo, L["LABEL_DEAD"])
    end
    unitData.status = tconcat(unitInfo, ",")
    unitData.reason = self:GetOrDefault(unitData.reason or
            (cachedUnitData[unit] and cachedUnitData[unit].reason)
    )

    -- Parse Player Status
    if not self:IsNullOrEmpty(unitData.status) then
        unitData.prefix = strformat(prefixFormat, unitData.status)
    else
        unitData.status = L["LABEL_ONLINE"]
        unitData.prefix = ""
        unitData.reason = ""
    end

    -- Return Data (Syncing if needed)
    if refresh then
        unitData.last_status = unitData.status
    end
    if sync then
        cachedUnitData[unit] = unitData
    end
    return unitData
end

--- Retrieves whether the instance has (or should be marked as) recently changed
--- @return boolean @ hasInstanceChanged
function CraftPresence:HasInstanceChanged()
    local has_changed = false
    if GetInstanceInfo then
        local name, instanceType = GetInstanceInfo()
        has_changed = (
                ((lastInstanceState == nil) or (instanceType ~= lastInstanceState)) or
                        ((lastAreaName == nil) or (name ~= lastAreaName))
        )

        if has_changed then
            lastInstanceState = instanceType
            lastAreaName = name
        end
    end
    return self:GetOrDefault(hasInstanceChanged or has_changed, false)
end

--- Sets whether the instance has (or should be marked as) recently changed
---
--- @param value boolean Whether the instance should be marked as changed
function CraftPresence:SetInstanceChanged(value)
    hasInstanceChanged = value
end

--- Retrieves the Cached Unit Data of the specified unit, if any
---
--- @param unit string The unit to interpret
---
--- @return table @ unitData
function CraftPresence:GetUnitData(unit)
    unit = self:GetOrDefault(unit, "player")
    return cachedUnitData[unit]
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
