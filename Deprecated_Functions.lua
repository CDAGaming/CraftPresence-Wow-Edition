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

--- Retrieves the Player Status for the specified unit
--- DEPRECATED: WILL BE REMOVED IN v1.5.0 (Use GetUnitData)
---
--- @param unit string The unit to interpret (Default: player)
--- @param refresh boolean Whether to sync the resulting status to unitData.last_status (Default: false)
--- @param sync boolean Whether to sync the resulting status to cachedUnitData[unit] (Default: true)
--- @param prefixFormat string Optional argument to determine the prefix formatting (Default: L["FORMAT_USER_PREFIX"])
--- @param unitData table Optional argument for forcing certain unit status data
---
--- @return string, string, table @ playerStatus, playerPrefix, cachedPlayerData
function CraftPresence:GetPlayerStatus(unit, refresh, sync, prefixFormat, unitData)
    local unitData = self:GetUnitStatus(unit, refresh, sync, prefixFormat, unitData)
    return unitData.status, unitData.prefix, unitData
end

--- Retrieves the Last Status of the specified unit, if any
--- DEPRECATED: WILL BE REMOVED IN v1.5.0 (Use GetUnitData)
---
--- @param unit string The unit to interpret (Default: player)
---
--- @return string @ lastPlayerStatus
function CraftPresence:GetLastPlayerStatus(unit)
    unit = self:GetOrDefault(unit, "player")
    return self:GetUnitData(unit).last_status
end

--- Sets a key,value pair within cachedPlayerData, for later usage
--- DEPRECATED: WILL BE REMOVED IN v1.5.0 (Use SetCachedUnitData)
---
--- @param key string The key to insert to the table
--- @param value any The value to insert to the table
---
--- @return table @ cachedPlayerData
function CraftPresence:SetCachedPlayerData(key, value)
    return self:SetCachedUnitData("player", key, value)
end