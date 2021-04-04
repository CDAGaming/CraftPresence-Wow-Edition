local CraftPresence = LibStub("AceAddon-3.0"):GetAddon("CraftPresence")

local L = LibStub("AceLocale-3.0"):GetLocale("CraftPresence")

----------------------------------
--LUA UTILITIES
----------------------------------

--- Trims a String of leading and duplicate spaces
---
--- @param str string The input string to evaluate
---
--- @return string @ trimmed_string
function CraftPresence:TrimString(str)
    if str == nil or str == "" then
        return str
    end
    str = string.gsub(str, "^%s*(.-)%s*$", "%1")
    str = string.gsub(str, "%s+", " ")
    return str
end

--- Retrieves the length of the specified object
---
--- @param obj any The object to evaluate
---
--- @return number @ object_length
function CraftPresence:GetLength(obj)
    local index = 0
    if table.getn and type(obj) == "table" then
        index = table.getn(obj)
    elseif type(obj) == "string" then
        index = string.len(obj)
    end
    return index
end

--- Locate one or multiple matches for the specified pattern
---
--- @param str string The input string to evaluate
--- @param pattern string The pattern to parse against
--- @param multiple boolean Whether or not to find all/the first match
---
--- @return any @ match_data
function CraftPresence:FindMatches(str, pattern, multiple, index, plain)
    multiple = multiple == nil or multiple == true
    plain = plain ~= nil and plain == true
    index = index or 1
    if multiple then
        if string.gmatch then
            return string.gmatch(str, pattern)
        elseif string.gfind then
            return string.gfind(str, pattern)
        else
            return nil
        end
    else
        if string.find then
            return string.find(str, pattern, index, plain)
        else
            return nil
        end
    end
end

--- Determines whether the specified string contains digit characters
---
--- @param str string The input string to evaluate
---
--- @return boolean @ containsDigit
function CraftPresence:ContainsDigit(str)
    return self:FindMatches(str, "%d", false) and true or false
end

--- Determines whether the specified string starts with the specified pattern
---
--- @param String string The input string to evaluate
--- @param Start string The target string to locate at the first index
---
--- @return boolean @ startsWith
function CraftPresence:StartsWith(String, Start)
    return string.sub(String, 1, self:GetLength(Start)) == Start
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

--- Splits a string by the specified arguments
---
--- @param str string The input string to interpret
--- @param inSplitPattern string The pattern to split the string by
--- @param outResults table The output result data (Can be predefined)
---
--- @return table @ outResults
function CraftPresence:Split(str, inSplitPattern, outResults)
    if not outResults then
        outResults = { }
    end
    local theStart = 1
    local theSplitStart, theSplitEnd = self:FindMatches(str, inSplitPattern, false, theStart)
    while theSplitStart do
        table.insert(outResults, string.sub(str, theStart, theSplitStart - 1))
        theStart = theSplitEnd + 1
        theSplitStart, theSplitEnd = self:FindMatches(str, inSplitPattern, false, theStart)
    end
    table.insert(outResults, string.sub(str, theStart))
    return outResults
end

----------------------------------
--API UTILITIES
----------------------------------

local lastPlayerStatus
local timer_locked = false
-- Compatibility Data
local build_info, compatibility_info

--- Retrieves and Syncronizes App Build Info
--- @return table @ build_info
function CraftPresence:GetBuildInfo()
    if not build_info then
        local version, build, date, tocversion = GetBuildInfo()
        version = version or "0.0.0"
        build = build or "0000"
        date = date or "Jan 1 1969"
        tocversion = tocversion or tonumber(build)

        build_info = {}
        build_info["version"] = version
        build_info["build"] = build
        build_info["date"] = date
        build_info["toc_version"] = tocversion
    end
    return build_info
end

--- Retrieves and Syncronizes App Compatibility Info
--- (Note some TOC version require extra filtering)
---
--- @return table @ compatibility_info
function CraftPresence:GetCompatibilityInfo()
    if not compatibility_info then
        compatibility_info = {
            ["retail"] = 90005, -- Latest Retail
            ["classic"] = 11306, -- Latest Classic
            ["9.0.x"] = 90000, -- SL 9.0.x
            ["8.0.x"] = 80000, -- BFA 8.0.X
            ["7.0.x"] = 70000, -- LEG 7.0.X
            ["6.0.x"] = 60000, -- WOD 6.0.X
            ["5.0.x"] = 50000, -- MOP 5.0.X
            ["4.0.x"] = 40000, -- CATA 4.0.X
            ["3.0.x"] = 30000, -- WOTLK 3.0.X
            ["2.5.x"] = 20500, -- TBC Classic
            ["2.0.x"] = 20000, -- TBC 2.0.x
            ["1.13.x"] = 11300, -- Vanilla Classic
            ["1.12.1"] = 5875 -- Vanilla 1.12.1
        }
    end
    return compatibility_info
end

--- Compatibility Helper: Determine if Build is 1.x on a Rebased API
--- @return boolean @ is_classic_rebased
function CraftPresence:IsClassicRebased()
    return (
            self:GetBuildInfo()["toc_version"] >= self:GetCompatibilityInfo()["1.13.x"] and
                    self:GetBuildInfo()["toc_version"] < self:GetCompatibilityInfo()["2.0.x"]
    )
end

--- Compatibility Helper: Determine if Build is 2.x on a Rebased API
--- @return boolean @ is_tbc_rebased
function CraftPresence:IsTBCRebased()
    return (
            self:GetBuildInfo()["toc_version"] >= self:GetCompatibilityInfo()["2.5.x"] and
                    self:GetBuildInfo()["toc_version"] < self:GetCompatibilityInfo()["3.0.x"]
    )
end

--- Retrieves the Player Status for the specified unit
---
--- @param unit string The unit name (Default: player)
--- @param sync boolean Whether to sync the resulting status to lastPlayerStatus
---
--- @return string, string @ playerStatus, playerPrefix
function CraftPresence:GetPlayerStatus(unit, sync)
    unit = unit or "player"
    local playerStatus = L["ONLINE_LABEL"]
    local playerPrefix = ""
    -- Ensure Version Compatibility
    if self:GetBuildInfo()["toc_version"] > self:GetCompatibilityInfo()["1.12.1"] then
        -- Player Name Tweaks (DND/AFK Data)
        local isAfk = UnitIsAFK(unit)
        local isOnDnd = UnitIsDND(unit)
        local isDead = UnitIsDead(unit)
        local isGhost = UnitIsGhost(unit)
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
        end
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

--- Sets whether or not the timer is locked
--- @param newValue boolean New variable value
function CraftPresence:SetTimerLocked(newValue)
    -- This method only executes if we are operating in a non-queue pipeline
    if not self:GetFromDb("queuedPipeline") then
        timer_locked = newValue
    end
end

--- Returns whether or not the timer is locked
--- @return boolean @ timer_locked
function CraftPresence:IsTimerLocked()
    return timer_locked
end

----------------------------------
--CONFIG GETTERS AND SETTERS
----------------------------------

--- Retrieves Config Data based on the specified parameters
---
--- @param grp string The config group to retrieve
--- @param key string The config key to retrieve
--- @param reset boolean Whether to reset this property value
---
--- @return any configValue
function CraftPresence:GetFromDb(grp, key, reset)
    local DB_DEFAULTS = CraftPresence:GetDefaults()
    if CraftPresence.db.profile[grp] == nil or (reset and not key) then
        CraftPresence.db.profile[grp] = DB_DEFAULTS.profile[grp]
    end
    if not key then
        return CraftPresence.db.profile[grp]
    end
    if CraftPresence.db.profile[grp][key] == nil or reset then
        CraftPresence.db.profile[grp][key] = DB_DEFAULTS.profile[grp][key]
    end
    return CraftPresence.db.profile[grp][key]
end

--- Sets Config Data based on the specified parameters
---
--- @param grp string The config group to retrieve
--- @param key string The config key to retrieve
--- @param newValue any The new config value to set
--- @param reset boolean Whether to reset this property value
---
--- @return any configValue
function CraftPresence:SetToDb(grp, key, newValue, reset)
    local DB_DEFAULTS = CraftPresence:GetDefaults()
    if CraftPresence.db.profile[grp] == nil or (reset and not key) then
        CraftPresence.db.profile[grp] = DB_DEFAULTS.profile[grp]
    end
    if not key then
        CraftPresence.db.profile[grp] = newValue
    else
        if CraftPresence.db.profile[grp][key] == nil or reset then
            CraftPresence.db.profile[grp][key] = DB_DEFAULTS.profile[grp][key]
        end
        CraftPresence.db.profile[grp][key] = newValue
    end
end

--- Generate Button Arguments for the specified arguments
---
--- @param grp string The group the config variable is located at
--- @param key string The key the config variable is located at
---
--- @return table @ generatedData
function CraftPresence:GetButtonArgs(grp, key)
    return {
        label = {
            type = "input", order = 1, width = 3.0,
            name = L["TITLE_BUTTON_LABEL"], desc = L["COMMENT_BUTTON_LABEL"], usage = L["USAGE_BUTTON_LABEL"],
            get = function(_)
                return CraftPresence:GetFromDb(grp, key or "label")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb(grp, key or "label")
                local isValid = (type(value) == "string")
                if isValid then
                    CraftPresence:SetToDb(grp, key or "label", value)
                    CraftPresence:PrintChangedValue(("(" .. grp .. ", " .. (key or "label") .. ")"), oldValue, value)
                end
            end,
        },
        url = {
            type = "input", order = 2, width = 3.0,
            name = L["TITLE_BUTTON_URL"], desc = L["COMMENT_BUTTON_URL"], usage = L["USAGE_BUTTON_URL"],
            get = function(_)
                return CraftPresence:GetFromDb(grp, key or "url")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb(grp, key or "url")
                local isValid = (type(value) == "string")
                if isValid then
                    CraftPresence:SetToDb(grp, key or "url", value)
                    CraftPresence:PrintChangedValue(("(" .. grp .. ", " .. (key or "url") .. ")"), oldValue, value)
                end
            end,
        },
    }
end

----------------------------------
--QUEUE SYSTEM UTILITIES
----------------------------------

local queued_data = { }
local queue_frame

--- Schedules a function to occur after the specified delay
---
--- @param seconds number The delay in seconds until task execution
--- @param func function The function to perform when delay expires
function CraftPresence:After(seconds, func, args)
    if type(args) ~= "table" then
        args = { args }
    end
    local f = function()
        func(args)
    end

    if C_Timer ~= nil then
        C_Timer.After(seconds, f)
    else
        self:SetupQueueSystem()
        queued_data[f] = GetTime() + seconds
    end
end

--- Initializes Queue Systems
function CraftPresence:SetupQueueSystem()
    if not queue_frame then
        queue_frame = CreateFrame("Frame", nil, UIParent)
        queue_frame:SetScript("OnUpdate", function()
            local elapsed_time = GetTime()
            if queued_data ~= nil then
                for key, value in pairs(queued_data) do
                    if elapsed_time > value then
                        key();
                        queued_data[key] = nil;
                    end
                end
            end
        end)
    end
end
