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

    local unitInfo = {}
    local unitString = ""
    local labelData = self:GetFromDb("labels")

    unitData.away = self:GetOrDefault(
            unitData.away or (UnitIsAFK and UnitIsAFK(unit)),
            (cachedUnitData[unit] and cachedUnitData[unit].away) or false
    )
    unitString = self:GetOrDefault(labelData["away"][L["STATUS_"..strupper(tostring(unitData.away))]])
    if not self:IsNullOrEmpty(unitString) then
        tinsert(unitInfo, unitString)
    end

    unitData.busy = self:GetOrDefault(
            unitData.busy or (UnitIsDND and UnitIsDND(unit)),
            (cachedUnitData[unit] and cachedUnitData[unit].busy) or false
    )
    unitString = self:GetOrDefault(labelData["busy"][L["STATUS_"..strupper(tostring(unitData.busy))]])
    if not self:IsNullOrEmpty(unitString) then
        tinsert(unitInfo, unitString)
    end

    unitData.dead = self:GetOrDefault(
            unitData.dead or (UnitIsDead and UnitIsDead(unit)),
            (cachedUnitData[unit] and cachedUnitData[unit].dead) or false
    )
    unitString = self:GetOrDefault(labelData["dead"][L["STATUS_"..strupper(tostring(unitData.dead))]])
    if not self:IsNullOrEmpty(unitString) then
        tinsert(unitInfo, unitString)
    end

    unitData.ghost = self:GetOrDefault(
            unitData.ghost or (UnitIsGhost and UnitIsGhost(unit)),
            (cachedUnitData[unit] and cachedUnitData[unit].ghost) or false
    )
    unitString = self:GetOrDefault(labelData["ghost"][L["STATUS_"..strupper(tostring(unitData.ghost))]])
    if not self:IsNullOrEmpty(unitString) then
        tinsert(unitInfo, unitString)
    end

    unitData.in_combat = self:GetOrDefault(
            unitData.in_combat or (UnitAffectingCombat and UnitAffectingCombat(unit)),
            (cachedUnitData[unit] and cachedUnitData[unit].in_combat) or false
    )
    unitString = self:GetOrDefault(labelData["in_combat"][L["STATUS_"..strupper(tostring(unitData.in_combat))]])
    if not self:IsNullOrEmpty(unitString) then
        tinsert(unitInfo, unitString)
    end

    -- Sync Player Name Tweaks
    if unitData.away then
        if cachedUnitData[unit] and self:IsNullOrEmpty(cachedUnitData[unit].reason) then
            cachedUnitData[unit].reason = self:GetOrDefault(DEFAULT_AFK_MESSAGE)
        end
    elseif unitData.busy then
        if cachedUnitData[unit] and self:IsNullOrEmpty(cachedUnitData[unit].reason) then
            cachedUnitData[unit].reason = self:GetOrDefault(DEFAULT_DND_MESSAGE)
        end
    end
    unitData.status = tconcat(unitInfo, ",")
    unitData.reason = self:GetOrDefault(unitData.reason or
            (cachedUnitData[unit] and cachedUnitData[unit].reason)
    )

    -- Parse Player Status
    if not self:IsNullOrEmpty(unitData.status) then
        unitData.prefix = strformat(prefixFormat, unitData.status)
    else
        unitData.status = L["STATUS_ONLINE"]
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
