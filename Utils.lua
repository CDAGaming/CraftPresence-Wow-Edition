local CraftPresence = LibStub("AceAddon-3.0"):GetAddon("CraftPresence")

----------------------------------
--LUA UTILITIES
----------------------------------

--- Determines whether the specified string contains digit characters
---
--- @param str string The input string to evaluate
---
--- @return boolean @ containsDigit
function CraftPresence:ContainsDigit(str)
    return string.find(str, "%d") and true or false
end

--- Determines whether the specified string starts with the specified pattern
---
--- @param String string The input string to evaluate
--- @param Start string The target string to locate at the first index
---
--- @return boolean @ startsWith
function CraftPresence:StartsWith(String, Start)
    return string.sub(String, 1, string.len(Start)) == Start
end

--- Formats the following word to proper casing (Xxxx)
---
--- @param str string The input string to evaluate
---
--- @return string @ formattedString
function CraftPresence:FormatWord(str)
    if (str == nil or str == "") then
        return str
    end
    str = string.lower(str)
    return (str:sub(1, 1):upper() .. str:sub(2))
end

--- Determines whether the specified value is within the specified range
---
--- @param value number The specified value to interpret
--- @param min number The minimum the value is allowed to be
--- @param max number The maximum the value is allowed to be
--- @param contains boolean Whether the range should include min and max
--- @param check_sanity boolean Whether to sanity check the min and max values
---
--- @return boolean @ isWithinValue
function CraftPresence:IsWithinValue(value, min, max, contains, check_sanity)
    -- Sanity checks
    local will_verify = (check_sanity == nil or check_sanity == true)
    if will_verify then
        if min > max then
            min = max
        end
        if max < min then
            max = min
        end
        if min < 0 then
            min = 0
        end
        if max < 0 then
            max = 0
        end
    end
    -- Contains Checks
    local will_contain = (contains ~= nil and contains == true)
    if not will_contain then
        min = min + 1
        max = max - 1
    end
    return (value >= min and value <= max)
end

--- Parse and Serialize a table into a printable string
---
--- @param val any The table or object within the table
--- @param name string The name of the object
--- @param skipnewlines boolean Whether or not new lines will be skipped
--- @param depth number Object depth to interpret within
---
--- @return string @ tableString
function CraftPresence:SerializeTable(val, name, skipnewlines, depth)
    skipnewlines = skipnewlines or false
    depth = depth or 0

    local tmp = string.rep(" ", depth)

    if name then
        tmp = tmp .. name .. " = "
    end

    if type(val) == "table" then
        tmp = tmp .. "{" .. (not skipnewlines and "\n" or "")

        for k, v in pairs(val) do
            tmp = tmp .. CraftPresence:SerializeTable(
                    v, k, skipnewlines, depth + 1
            ) .. "," .. (not skipnewlines and "\n" or "")
        end

        tmp = tmp .. string.rep(" ", depth) .. "}"
    elseif type(val) == "number" then
        tmp = tmp .. tostring(val)
    elseif type(val) == "string" then
        tmp = tmp .. string.format("%q", val)
    elseif type(val) == "boolean" then
        tmp = tmp .. (val and "true" or "false")
    else
        tmp = tmp .. "\"[inserializeable datatype:" .. type(val) .. "]\""
    end

    return tmp
end

----------------------------------
--API UTILITIES
----------------------------------

local lastPlayerStatus

--- Retrieves the Player Status for the specified unit
---
--- @param unit string The unit name (Default: player)
--- @param sync boolean Whether to sync the resulting status to lastPlayerStatus
---
--- @return string, string @ playerStatus, playerPrefix
function CraftPresence:GetPlayerStatus(unit, sync)
    unit = unit or "player"
    -- Player Name Tweaks (DND/AFK Data)
    local isAfk = UnitIsAFK(unit)
    local isOnDnd = UnitIsDND(unit)
    local isDead = UnitIsDead(unit)
    local isGhost = UnitIsGhost(unit)
    local playerStatus
    local playerPrefix = ""
    if isAfk then
        playerStatus = L["AFK_LABEL"]
    elseif isOnDnd then
        playerStatus = L["DND_LABEL"]
    elseif isGhost then
        playerStatus = L["GHOST_LABEL"]
    elseif isDead then
        playerStatus = L["DEAD_LABEL"]
    end
    -- Parse Player Status
    if not (playerStatus == nil) then
        playerPrefix = ("(" .. playerStatus .. ")") .. " "
    else
        playerStatus = L["ONLINE_LABEL"]
    end
    -- Return Data (and sync if needed)
    if sync then
        lastPlayerStatus = playerStatus
    end
    return playerStatus, playerPrefix
end

----------------------------------
--API GETTERS AND SETTERS
----------------------------------

--- Retrieves the Last Player Status, if any
--- @return string @ lastPlayerStatus
function CraftPresence:GetLastPlayerStatus()
    return lastPlayerStatus
end