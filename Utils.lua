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
local type, pairs, tonumber, loadstring = type, pairs, tonumber, loadstring;
local strgmatch, strgfind, strformat, strgsub = string.gmatch, string.gfind, string.format, string.gsub
local strsub, strfind, strlower, strupper, tostring = string.sub, string.find, string.lower, string.upper, tostring
local strlen, strrep, tgetn, tinsert = string.len, string.rep, table.getn, table.insert
local CreateFrame, UIParent, GetTime = CreateFrame, UIParent, GetTime

-- Addon APIs
local L = CraftPresence.locale
local CP_GlobalUtils = CP_GlobalUtils

--[[ LUA UTILITIES ]]--

local lastIndex = 0

--- Retrieves the Next Available Button Id for use in the currently open Screen
---
--- @return number @ lastIndex
function CraftPresence:GetNextIndex()
    lastIndex = lastIndex + 1
    return lastIndex;
end

--- Resets the Button Index to 0
--- Normally used when closing a screen and no longer using the allocated Id's
function CraftPresence:ResetIndex()
    lastIndex = 0;
end

--- Generates a random string of numbers
---
--- @param length number The length of the resulting number
---
--- @return number @ random_string
function CraftPresence:RandomString(length)
    return CP_GlobalUtils:RandomString(length)
end

--- Determines if the specified object is null or empty
---
--- @param obj any The object to interpret
---
--- @return boolean @ is_object_empty
function CraftPresence:IsNullOrEmpty(obj)
    return CP_GlobalUtils:IsNullOrEmpty(obj)
end

--- Return the specified object or a fallback value if nil
---
--- @param obj any The object to interpret
--- @param default any The fallback value to interpret (Returns an empty string if default is nil)
---
--- @return any @ adjusted_object
function CraftPresence:GetOrDefault(obj, default)
    if self:IsNullOrEmpty(obj) then
        if not self:IsNullOrEmpty(default) then
            obj = default
        else
            obj = ""
        end
    end
    return obj
end

--- Replaces the specified area of a string
---
--- @param str string The input string to evaluate
--- @param old string The portion of the string to replace
--- @param new string The string to replace the old portion with
--- @param plain boolean Whether or not to forbid pattern matching filters
---
--- @return string @ formatted_string
function CraftPresence:Replace(str, old, new, plain)
    return CP_GlobalUtils:Replace(str, old, new, plain)
end

--- Trims a String of leading and duplicate spaces
---
--- @param str string The input string to evaluate
---
--- @return string @ trimmed_string
function CraftPresence:TrimString(str)
    if self:IsNullOrEmpty(str) then
        return str
    end
    str = strgsub(str, "^%s+", "")
    str = strgsub(str, "%s+$", "")
    return str
end

--- Retrieves the length of the specified object
---
--- @param obj any The object to evaluate
---
--- @return number @ object_length
function CraftPresence:GetLength(obj)
    local index = 0
    if tgetn and type(obj) == "table" then
        index = tgetn(obj)
    elseif type(obj) == "string" then
        index = strlen(obj)
    end
    return index
end

--- Locate one or multiple matches for the specified pattern
---
--- @param str string The input string to evaluate
--- @param pattern string The pattern to parse against
--- @param multiple boolean Whether or not to find all/the first match (Default: true)
--- @param index number The index to begin evaluation at (Default: 1)
--- @param plain boolean Whether or not to forbid pattern matching filters (Defalt: false)
---
--- @return any @ match_data
function CraftPresence:FindMatches(str, pattern, multiple, index, plain)
    multiple = self:GetOrDefault(multiple, true)
    plain = self:GetOrDefault(plain, false)
    index = self:GetOrDefault(index, 1)
    if not self:IsNullOrEmpty(str) then
        if multiple then
            if strgmatch then
                return strgmatch(str, pattern)
            elseif strgfind then
                return strgfind(str, pattern)
            end
        elseif strfind then
            return strfind(str, pattern, index, plain)
        end
    end
    return nil
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
--- @param str string The input string to evaluate
--- @param start string The target string to locate at the first index
---
--- @return boolean @ startsWith
function CraftPresence:StartsWith(str, start)
    if self:IsNullOrEmpty(str) then
        return false
    end
    return strsub(str, 1, self:GetLength(start)) == start
end

--- Formats the following word to proper casing (Xxxx)
---
--- @param str string The input string to evaluate
---
--- @return string @ formattedString
function CraftPresence:FormatWord(str)
    if self:IsNullOrEmpty(str) then
        return str
    end
    str = strlower(str)
    return (strupper(strsub(str, 1, 1)) .. strsub(str, 2))
end

--- Replaces a String with the specified formatting
---
--- @param str string The input string to evaluate
--- @param replacer_one string The first replacer
--- @param replacer_two string The second replacer
--- @param pattern_one string The first pattern
--- @param pattern_two string The second pattern
--- @param plain boolean Whether or not to forbid pattern matching filters
---
--- @return string @ formatted_string
function CraftPresence:SetFormat(str, replacer_one, replacer_two, pattern_one, pattern_two, plain)
    return CP_GlobalUtils:SetFormat(str, replacer_one, replacer_two, pattern_one, pattern_two, plain)
end

--- Parses Multiple arguments through the SetFormat method
--- (INTERNAL USAGE ONLY)
---
--- @param format_args table The format arguments to pass to each SetFormat call
--- @param string_args table The strings to parse
--- @param plain boolean Whether or not to forbid pattern matching filters
--- @param set_config boolean Whether to parse and save the resulting values as config data
---
--- @return table, table @ string_args, adjusted_data
function CraftPresence:SetFormats(format_args, string_args, plain, set_config)
    local adjusted_data = {}
    if type(format_args) == "table" and tgetn(format_args) <= 4 and type(string_args) == "table" then
        for key, value in pairs(string_args) do
            local strValue
            if type(value) == "table" then
                if set_config and tgetn(value) <= 2 then
                    strValue = self:GetFromDb(value[1], value[2])
                elseif not set_config then
                    strValue = self:SerializeTable(value)
                end
            else
                if set_config then
                    strValue = self:GetFromDb(value)
                else
                    strValue = value
                end
            end

            if strValue ~= nil then
                local newValue = self:SetFormat(
                        strValue, format_args[1], format_args[2], format_args[3], format_args[4], plain
                )
                if strValue ~= newValue then
                    if type(value) == "table" then
                        if set_config and tgetn(value) <= 2 then
                            self:SetToDb(value[1], value[2], newValue)
                        else
                            self:Print(strformat(
                                    L["LOG_ERROR"], strformat(
                                            L["ERROR_COMMAND_UNKNOWN"], "SetFormats"
                                    )
                            ))
                        end
                    else
                        if set_config then
                            self:SetToDb(value, nil, newValue)
                        else
                            string_args[key] = newValue
                        end
                    end
                    adjusted_data[key] = {
                        ["old"] = strValue,
                        ["new"] = newValue
                    }
                end
            end
        end
    end
    return string_args, adjusted_data
end

--- Determines whether the specified value is within the specified range
---
--- @param value number The specified value to interpret (Default: 0)
--- @param min number The minimum the value is allowed to be (Default: value)
--- @param max number The maximum the value is allowed to be (Default: min)
--- @param contains_min boolean Whether the range should include the min value (Default: false)
--- @param contains_max boolean Whether the range should include the max value (Default: false)
--- @param check_sanity boolean Whether to sanity check the min and max values (Default: true)
---
--- @return boolean @ isWithinValue
function CraftPresence:IsWithinValue(value, min, max, contains_min, contains_max, check_sanity)
    -- Data checks
    value = self:GetOrDefault(value, 0)
    min = self:GetOrDefault(min, value)
    max = self:GetOrDefault(max, min)
    contains_min = self:GetOrDefault(contains_min, false)
    contains_max = self:GetOrDefault(contains_max, false)
    check_sanity = self:GetOrDefault(check_sanity, true)
    -- Sanity checks
    if check_sanity then
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
    if not contains_min then
        min = min + 1
    end
    if not contains_max then
        max = max - 1
    end
    return (value >= min and value <= max)
end

--- Parse and Serialize a table into a printable string
---
--- @param val any The table or object within the table
--- @param name string The name of the object
--- @param skipnewlines boolean Whether or not new lines will be skipped (Default: false)
--- @param depth number Object depth to interpret within (Default: 0)
---
--- @return string @ tableString
function CraftPresence:SerializeTable(val, name, skipnewlines, depth)
    skipnewlines = self:GetOrDefault(skipnewlines, false)
    depth = self:GetOrDefault(depth, 0)

    local tmp = strrep(" ", depth)

    if name then
        tmp = tmp .. name .. " = "
    end

    if type(val) == "table" then
        tmp = tmp .. "{" .. (not skipnewlines and "\n" or "")

        for k, v in pairs(val) do
            tmp = tmp .. self:SerializeTable(
                    v, k, skipnewlines, depth + 1
            ) .. "," .. (not skipnewlines and "\n" or "")
        end

        tmp = tmp .. strrep(" ", depth) .. "}"
    elseif type(val) == "number" then
        tmp = tmp .. tostring(val)
    elseif type(val) == "string" then
        tmp = tmp .. strformat("%q", val)
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
--- @param multiple boolean Whether to find multiple matches
--- @param plain boolean Whether or not to forbid pattern matching filters
--- @param outResults table The output result data (Can be predefined)
---
--- @return table @ outResults
function CraftPresence:Split(str, inSplitPattern, multiple, plain, outResults)
    outResults = self:GetOrDefault(outResults, {})
    if not self:IsNullOrEmpty(str) then
        local doContinue = true
        local theStart = 1
        local theSplitStart, theSplitEnd = self:FindMatches(str, inSplitPattern, false, theStart, plain)
        while theSplitStart and doContinue do
            tinsert(outResults, strsub(str, theStart, theSplitStart - 1))
            theStart = theSplitEnd + 1
            doContinue = multiple
            if doContinue then
                theSplitStart, theSplitEnd = self:FindMatches(str, inSplitPattern, false, theStart, plain)
            end
        end
        tinsert(outResults, strsub(str, theStart))
    end
    return outResults
end

--- Retrieves the Return Value from a Dynamic Function
---
--- @param value any The input (and output) for this event
--- @param valueType string The expected type of this event (Default: type(value))
--- @param limit number How many times to iterate until a different type (Default: 1)
---
--- @return string @ returnValue
function CraftPresence:GetDynamicReturnValue(value, valueType, a1, a2, a3, a4, a5, a6, a7, a8, a9, limit)
    value = self:GetOrDefault(value)
    valueType = self:GetOrDefault(valueType, type(value))
    limit = self:GetOrDefault(limit, 1)
    if not self:IsNullOrEmpty(value) and valueType == "function" and loadstring then
        if type(value) == "string" then
            -- Edge-Case to allow non-return functions
            local lowVal = strlower(value)
            if not self:StartsWith(lowVal, "return ") and self:StartsWith(lowVal, "function") then
                value = "return " .. value
            end
            value = loadstring(value)()
        end
        while type(value) == "function" and limit > 0 do
            value = value(a1, a2, a3, a4, a5, a6, a7, a8, a9)
            limit = limit - 1
        end
    end
    -- Sanity checks
    if type(value) == "table" then
        value = self:SerializeTable(value)
    else
        value = tostring(value)
    end
    return value
end

--[[ API UTILITIES ]]--

local fallbackVersion, fallbackTOC, addonVersion = "0.0.0", 00000
local timer_locked = false
-- Compatibility Data
local build_info, compatibility_info, flavor_info, extra_build_info

--- Retrieve and/or Synchronize App Build Info
--- @return table @ build_info
function CraftPresence:GetBuildInfo()
    if not build_info then
        local version, build, date, tocversion = fallbackVersion, "0000", "Jan 1 1969", fallbackTOC
        if GetBuildInfo then
            version, build, date, tocversion = GetBuildInfo()
            tocversion = tocversion or tonumber(self:VersionToBuild(version))
        end

        build_info = {
            ["version"] = version,
            ["build"] = build,
            ["date"] = date,
            ["toc_version"] = tocversion
        }
    end
    return build_info
end

--- Retrieve and/or Synchronize Build Flavor Info
---
--- @return table @ flavor_info
function CraftPresence:GetFlavorInfo()
    if not flavor_info then
        flavor_info = {
            ["retail"] = 90005, -- Latest Retail
            ["classic"] = 11307, -- Latest Classic
            ["ptr"] = 90100, -- Latest Retail PTR
            ["classic_ptr"] = 11307 -- Latest Classic PTR
        }
    end
    return flavor_info
end

--- Retrieve and/or Synchronize App Compatibility Info
--- (Note that some TOC versions require extra filtering)
---
--- @return table @ compatibility_info
function CraftPresence:GetCompatibilityInfo()
    if not compatibility_info then
        compatibility_info = {
            ["9.0.0"] = 90000, -- Shadowlands 9.0.0
            ["8.0.0"] = 80000, -- BFA 8.0.0
            ["7.0.0"] = 70000, -- Legion 7.0.0
            ["6.0.0"] = 60000, -- WOD 6.0.0
            ["5.0.0"] = 50000, -- MOP 5.0.0
            ["4.0.0"] = 40000, -- CATA 4.0.0
            ["3.0.0"] = 30000, -- WOTLK 3.0.0
            ["2.5.0"] = 20500, -- TBC Classic 2.5.0
            ["2.0.0"] = 20000, -- TBC 2.0.0
            ["1.13.0"] = 11300, -- Vanilla Classic 1.13.0
            ["1.12.1"] = 11201 -- Vanilla 1.12.1
        }
    end
    return compatibility_info
end

--- Retrieve and/or Synchronize Extra Build Info
--- (Note that some TOC versions require extra filtering)
---
--- @return table @ extra_build_info
function CraftPresence:GetExtraBuildInfo()
    if not extra_build_info then
        extra_build_info = {
            ["1.15.1"] = 11501 -- TurtleWoW 1.15.1 (Vanilla)
        }
    end
    return extra_build_info
end

--- Determine if this build identifies as Vanilla Classic
--- @return boolean @ is_classic_rebased
function CraftPresence:IsClassicRebased()
    return self:IsWithinValue(
            self:GetBuildInfo()["toc_version"],
            self:GetCompatibilityInfo()["1.13.0"], self:GetCompatibilityInfo()["2.0.0"],
            true, false
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as TBC Classic
--- @return boolean @ is_tbc_rebased
function CraftPresence:IsTBCRebased()
    return self:IsWithinValue(
            self:GetBuildInfo()["toc_version"],
            self:GetCompatibilityInfo()["2.5.0"], self:GetCompatibilityInfo()["3.0.0"],
            true, false
    ) and not self:IsSpecialVersion()
end

--- Determine if this build is using a rebased api
--- @return boolean @ is_rebased_api
function CraftPresence:IsRebasedApi()
    return self:IsClassicRebased() or self:IsTBCRebased()
end

--- Determine if this build is using a special/modified api
--- @return any @ special_info
function CraftPresence:IsSpecialVersion()
    return self:GetExtraBuildInfo()[self:GetBuildInfo()["version"]]
end

--- Getter for CraftPresence:addonVersion
--- @return string @ addonVersion
function CraftPresence:GetVersion()
    if self:IsNullOrEmpty(addonVersion) then
        addonVersion = GetAddOnMetadata(L["ADDON_NAME"], "Version")
        if not self:ContainsDigit(addonVersion) then
            self:Print(strformat(L["LOG_WARNING"], strformat(L["WARNING_BUILD_UNSUPPORTED"], addonVersion)))
            addonVersion = "v" .. fallbackVersion
        end
    end
    return self:GetOrDefault(addonVersion, fallbackVersion)
end

--- Getter for Addon Locale Data
--- @return table @ localeData
function CraftPresence:GetLocale()
    return L
end

--- Convert a Version String into a build number
---
--- @param versionStr string The version string to evaluate (Default: fallbackVersion)
---
--- @return string @ buildVersion
function CraftPresence:VersionToBuild(versionStr)
    versionStr = self:GetOrDefault(versionStr, fallbackVersion)
    local buildStr
    local splitData = self:Split(versionStr, ".", true, true)
    if splitData[1] and splitData[1] == versionStr then
        buildStr = splitData[1]
    else
        for key, value in pairs(splitData) do
            if key > 1 then
                buildStr = buildStr .. strformat("%02d", value)
            else
                buildStr = buildStr .. value
            end
        end
    end
    return self:GetOrDefault(buildStr, fallbackTOC)
end

--- Display the addon's config frame
function CraftPresence:ShowConfig()
    -- a bug can occur in blizzard's implementation of this call
    if (self:GetBuildInfo()["toc_version"] >= self:GetCompatibilityInfo()["2.0.0"] or
            self:IsRebasedApi()) and InterfaceOptionsFrame_OpenToCategory then
        InterfaceOptionsFrame_OpenToCategory(self.optionsFrame)
        InterfaceOptionsFrame_OpenToCategory(self.optionsFrame)
    else
        self:Print(strformat(
                L["LOG_ERROR"], strformat(
                        L["ERROR_FUNCTION_DISABLED"], "ShowConfig"
                )
        ))
    end
end

--[[ API GETTERS AND SETTERS ]]--

--- Print initial data and register events, depending on platform and config data
function CraftPresence:PrintInitialData()
    self:Print(strformat(L["ADDON_INTRO"], self:GetVersion()))
    if self:GetFromDb("verboseMode") then
        self:Print(strformat(L["ADDON_BUILD_INFO"], self:SerializeTable(self:GetBuildInfo())))
    end
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

--- Parse a Placeholder Table with the specified filter for any matches
--- (INTERNAL USAGE ONLY)
---
--- @param query string The query to filter the placeholderTable by
--- @param placeholderTable table The table to iterate through
--- @param found_placeholders boolean Output Value -- If any placeholders were found before or after
--- @param placeholderStr string Output Value -- The current placeholder result string
---
--- @return boolean, string @ found_placeholders, placeholderStr
function CraftPresence:ParsePlaceholderTable(query, placeholderTable, found_placeholders, placeholderStr)
    placeholderTable = self:GetOrDefault(placeholderTable, {})
    found_placeholders = self:GetOrDefault(found_placeholders, false)
    placeholderStr = self:GetOrDefault(placeholderStr)
    for key, value in pairs(placeholderTable) do
        local strKey = tostring(key)
        local strValue = tostring(value)
        if type(value) == "table" then
            strValue = self:GetDynamicReturnValue(value["data"] or value, value["type"], self)
        end
        if (query == nil or (
                self:FindMatches(strlower(strKey), query, false, 1, true) or
                        self:FindMatches(strlower(strValue), query, false, 1, true))
        ) then
            found_placeholders = true
            placeholderStr = placeholderStr .. "\n " .. (strformat(
                    L["PLACEHOLDERS_FOUND_DATA"], strKey, strValue
            ))
        end
    end
    return found_placeholders, placeholderStr
end

--[[ CONFIG GETTERS AND SETTERS ]]--

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

--- Generates Config Table for a set of custom arguments
---
--- @param rootKey string The config key to retrieve placeholder data (Required)
--- @param titleKey string The title key to display (Can be a function returning a string)
--- @param commentKey string The comment key to display (Can be a function returning a string)
---
--- @return table @ generatedData
function CraftPresence:GetPlaceholderArgs(rootKey, titleKey, commentKey)
    if CraftPresence:IsNullOrEmpty(rootKey) then
        return {}
    end
    titleKey = titleKey or rootKey
    commentKey = commentKey or ""
    local found_count = 0

    titleKey = CraftPresence:GetDynamicReturnValue(titleKey, type(titleKey), CraftPresence)
    local table_args = {
        titleHeader = {
            order = CraftPresence:GetNextIndex(), type = "header", name = titleKey
        }
    }
    local custom_placeholders = CraftPresence:GetFromDb(rootKey)
    if type(custom_placeholders) == "table" then
        for key, value in pairs(custom_placeholders) do
            local value_args = {}
            if type(value) == "table" then
                for innerKey, _ in pairs(value) do
                    value_args[innerKey] = {
                        type = "input", order = CraftPresence:GetNextIndex(), width = 3.0,
                        multiline = (strlower(innerKey) == "data"),
                        name = (L["TITLE_BUTTON_" .. strupper(innerKey)] or CraftPresence:FormatWord(innerKey)),
                        desc = (L["COMMENT_BUTTON_" .. strupper(innerKey)] or ""),
                        usage = (L["USAGE_BUTTON_" .. strupper(innerKey)] or ""),
                        get = function(_)
                            return CraftPresence.db.profile[rootKey][key][innerKey]
                        end,
                        set = function(_, newValue)
                            local oldValue = CraftPresence.db.profile[rootKey][key][innerKey]
                            local isValid = (type(newValue) == "string")
                            if isValid then
                                CraftPresence.db.profile[rootKey][key][innerKey] = newValue
                                CraftPresence:PrintChangedValue(
                                        (rootKey .. " (" .. key .. ", " .. innerKey .. ")"),
                                        oldValue, newValue
                                )
                            end
                        end,
                    }
                end
            end
            table_args[key] = {
                name = (L["TITLE_" .. strupper(key)] or key),
                desc = (L["COMMENT_" .. strupper(key)] or ""),
                type = "group", order = CraftPresence:GetNextIndex(),
                args = value_args
            }
            found_count = found_count + 1
        end
    end

    commentKey = CraftPresence:GetDynamicReturnValue(commentKey, type(commentKey), found_count)
    table_args[CraftPresence:RandomString(8)] = {
        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "medium",
        name = commentKey
    }
    return table_args
end

--[[ QUEUE SYSTEM UTILITIES ]]--

local queued_data = { }
local queue_frame

--- Schedules a function to occur after the specified delay
---
--- @param seconds number The delay in seconds until task execution
--- @param func function The function to perform when delay expires
--- @param args any The arguments, if any, to use with the function
function CraftPresence:After(seconds, func, args)
    if type(args) ~= "table" then
        args = { args }
    end
    local f = function()
        func(args)
    end

    self:SetupQueueSystem()
    queued_data[f] = GetTime() + seconds
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
