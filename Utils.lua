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
local strlen, strrep, tgetn, tinsert, unpack = string.len, string.rep, table.getn, table.insert, unpack
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

--- Generates a random string of numbers with the specified length
---
--- @param length number The length of the resulting number (Default: 9)
---
--- @return number @ random_string
function CraftPresence:RandomString(length)
    return CP_GlobalUtils:RandomString(self:GetOrDefault(length, 9))
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

--- Returns arguments from the specified table between the specified range
---
--- @param data table The table to interpret
--- @param from number The index at which to begin storing for return (Required)
--- @param to number The index at which to stop storing for return (Default: data:length)
---
--- @return table @ resultData
function CraftPresence:GetArgsInRange(data, from, to)
    local resultData = {}
    from = self:GetOrDefault(from, 0)
    if type(data) == "table" then
        to = self:GetOrDefault(to, self:GetLength(data))
        for k, v in pairs(data) do
            if self:IsWithinValue(k, from, to, true, true) then
                tinsert(resultData, v)
            end
        end
    end
    return resultData
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
    if self:IsNullOrEmpty(str) or type(str) ~= "string" then
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
--- @param expected string The expected string to locate
---
--- @return boolean @ startsWith
function CraftPresence:StartsWith(str, expected)
    if self:IsNullOrEmpty(str) then
        return false
    end
    return self:ContainsAt(str, expected, 1, self:GetLength(expected))
end

--- Determines whether the specified string ends with the specified pattern
---
--- @param str string The input string to evaluate
--- @param expected string The expected string to locate
---
--- @return boolean @ endsWith
function CraftPresence:EndsWith(str, expected)
    if self:IsNullOrEmpty(str) then
        return false
    end
    local start_len = (self:GetLength(str) - self:GetLength(expected)) + 1
    return self:ContainsAt(str, expected, start_len, self:GetLength(str))
end

--- Determines whether the specified string includes the specified pattern at the given position
---
--- @param str string The input string to evaluate
--- @param expected string The expected string to locate
--- @param start_len number The beginning length to iterate at (Default: 1)
--- @param end_len number The ending length to iterate at (Default: input string length)
---
--- @return boolean @ containsAt
function CraftPresence:ContainsAt(str, expected, start_len, end_len)
    if self:IsNullOrEmpty(str) then
        return false
    end
    start_len = self:GetOrDefault(start_len, 1)
    end_len = self:GetOrDefault(end_len, self:GetLength(str))
    return strsub(str, start_len, end_len) == expected
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

--- Return a lower/upper cased version of the specified string, depending on arguments
---
--- @param obj any A table (Format: string, casing) or string to evaluate
---
--- @return string @ adjusted_data
function CraftPresence:GetCaseData(obj)
    local value
    if type(obj) == "table" then
        local innerValue = obj
        if self:GetLength(innerValue) == 2 then
            value = self:GetOrDefault(innerValue[1])
            local caseType = strlower(self:GetOrDefault(innerValue[2]))
            if caseType == "lower" or caseType == "icon" then
                value = strlower(value)
                if caseType == "icon" then
                    value = self:Replace(value, "%s+", "_")
                end
            elseif caseType == "upper" then
                value = strupper(value)
            end
        end
    else
        value = self:GetOrDefault(obj)
    end
    return value
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

--- Variable Argument Operation
---
--- @param n number The number of static args
--- @param f function The function to trigger with args
---
--- @return any @ result
function CraftPresence:vararg(n, f)
    return CP_GlobalUtils:vararg(n, f)
end

--- Parses Multiple arguments through the SetFormat method
---
--- @param format_args table The format arguments to pass to each SetFormat call
--- @param string_args table The strings to parse
--- @param plain boolean Whether or not to forbid pattern matching filters
--- @param set_config boolean Whether to parse and save the resulting values as config data
---
--- @return table, table @ string_args, adjusted_data
function CraftPresence:SetFormats(format_args, string_args, plain, set_config)
    local adjusted_data = {}
    if type(format_args) == "table" and self:GetLength(format_args) <= 4 and type(string_args) == "table" then
        for key, value in pairs(string_args) do
            local strValue
            if type(value) == "table" then
                if set_config and self:GetLength(value) <= 2 then
                    strValue = self:GetFromDb(value[1], value[2])
                elseif not set_config then
                    if self:GetLength(value) == 2 then
                        strValue = self:GetCaseData(value)
                    else
                        strValue = self:SerializeTable(value)
                    end
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
                        if set_config and self:GetLength(value) <= 2 then
                            self:SetToDb(value[1], value[2], newValue)
                        else
                            if self:GetLength(value) == 2 then
                                string_args[key] = self:GetCaseData({ newValue, value[2] })
                            else
                                self:PrintErrorMessage(strformat(L["ERROR_COMMAND_UNKNOWN"], "SetFormats"))
                            end
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
--- @param multiple boolean Whether to find multiple matches (Default: false)
--- @param plain boolean Whether or not to forbid pattern matching filters (Default: false)
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

--- Retrieves the Return Value(s) from a Dynamic Function
---
--- @param value any The input (and output) for this event
--- @param valueType string The expected type of this event (Default: type(value))
---
--- @return any @ returnData
CraftPresence.GetDynamicReturnValue = CraftPresence:vararg(3, function(self, value, valueType, args)
    value = self:GetOrDefault(value)
    valueType = self:GetOrDefault(valueType, type(value))

    if not self:IsNullOrEmpty(value) then
        if valueType == "function" and loadstring then
            -- The value is converted to a table from here onwards
            -- in order to support multiple return values
            if type(value) ~= "table" then
                value = { value }
            end

            if type(value[1]) == "string" then
                -- Edge-Case to allow non-return functions
                local lowVal = strlower(value[1])
                if not self:StartsWith(lowVal, "return ") and self:StartsWith(lowVal, "function") then
                    value[1] = "return " .. value[1]
                end
                local func, err = loadstring(value[1])
                if not func then
                    local dataTable = {
                        [L["TITLE_ATTEMPTED_FUNCTION"]] = value[1],
                        [L["TITLE_FUNCTION_MESSAGE"]] = err
                    }
                    self:PrintErrorMessage(strformat(L["ERROR_FUNCTION"], self:SerializeTable(dataTable)))
                else
                    value[1] = func
                end
            end
            while type(value[1]) == "function" do
                value = { value[1](unpack(args)) }
            end

            return unpack(value)
        else
            -- Sanity checks
            if type(value) == "table" then
                value = self:SerializeTable(value)
            else
                value = tostring(value)
            end
        end
    end
    return value
end)

--- Return whether or not two tables are equivalent in elements and keys
---
--- @param t1 table The first table to iterate against
--- @param t2 table The second table to iterate against
---
--- @return boolean @ tables_equal
function CraftPresence:AreTablesEqual(t1, t2)
    t1 = self:GetOrDefault(t1, {})
    t2 = self:GetOrDefault(t2, {})
    return self:SerializeTable(t1) == self:SerializeTable(t2)
end

--- Return a combination of tables, depending on arguments
---
--- @param rootTable table The root table to combine data into
---
--- @return table @ combined_table
CraftPresence.CombineTables = CraftPresence:vararg(2, function(self, rootTable, args)
    for _, dynTable in pairs(args) do
        if type(dynTable) == "table" then
            for k, v in pairs(dynTable) do
                rootTable[k] = v
            end
        end
    end
    return rootTable
end)

--[[ API UTILITIES ]]--

local fallbackVersion, fallbackTOC = "0.0.0", 00000
local timer_locked = false
-- Compatibility Data
local addon_info, build_info, compatibility_info, flavor_info, extra_build_info

--- Retrieve and/or Synchronize App Build Info
--- @return table @ build_info
function CraftPresence:GetBuildInfo()
    if not build_info then
        local version, build, date, tocversion = fallbackVersion, "0000", "Jan 1 1969", fallbackTOC
        if GetBuildInfo then
            version, build, date, tocversion = GetBuildInfo()
            tocversion = tocversion or self:VersionToBuild(version)
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

--- Retrieve and/or Synchronize Addon Metadata
--- @return table @ addon_info
function CraftPresence:GetAddOnInfo()
    if not addon_info then
        local version, versionString, schema
        if GetAddOnMetadata then
            version = GetAddOnMetadata(L["ADDON_NAME"], "Version")
            schema = GetAddOnMetadata(L["ADDON_NAME"], "X-Schema-ID")
        end

        if not self:ContainsDigit(version) then
            self:Print(strformat(L["LOG_WARNING"], strformat(L["WARNING_BUILD_UNSUPPORTED"], version)))
            version = "v" .. fallbackVersion
        end
        versionString = strformat(L["ADDON_HEADER_VERSION"], L["ADDON_NAME"], version)

        addon_info = {
            ["version"] = version,
            ["versionString"] = versionString,
            ["schema"] = tonumber(schema)
        }
    end
    return addon_info
end

--- Retrieve and/or Synchronize Build Flavor Info
---
--- @return table @ flavor_info
function CraftPresence:GetFlavorInfo()
    if not flavor_info then
        flavor_info = {
            ["retail"] = 90105, -- Latest Retail
            ["classic"] = 20502, -- Latest Classic
            ["classic_era"] = 11401, -- Latest Classic Era
            ["ptr"] = 90200, -- Latest Retail PTR
            ["classic_ptr"] = 20502, -- Latest Classic PTR
            ["classic_era_ptr"] = 11401 -- Latest Classic Era PTR
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
            ["1.14.0"] = 11400, -- Vanilla Classic 1.14.0
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

--- Getter for Addon Locale Data
--- @return table @ localeData
function CraftPresence:GetLocale()
    return L
end

--- Convert a Version String into a build number
---
--- @param versionStr string The version string to evaluate (Default: fallbackVersion)
---
--- @return number @ buildVersion
function CraftPresence:VersionToBuild(versionStr)
    versionStr = self:GetOrDefault(versionStr, fallbackVersion)
    local buildStr = ""
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
    return self:GetOrDefault(tonumber(buildStr), fallbackTOC)
end

--- Display the addon's config frame
function CraftPresence:ShowConfig()
    if (self:GetBuildInfo()["toc_version"] >= self:GetCompatibilityInfo()["2.0.0"] or
            self:IsRebasedApi()) and InterfaceOptionsFrame_OpenToCategory then
        -- a bug can occur in blizzard's implementation of this call
        -- so it is called twice to workaround it
        InterfaceOptionsFrame_OpenToCategory(self.optionsFrame)
        InterfaceOptionsFrame_OpenToCategory(self.optionsFrame)
    else
        self:PrintErrorMessage(strformat(L["ERROR_FUNCTION_DISABLED"], "ShowConfig"))
    end
end

--[[ LOGGING DATA ]]--

--- Prints a formatted message, meant to symbolize an error message
--- @param logStyle string The log format to follow
function CraftPresence:PrintErrorMessage(logStyle)
    if not self:IsNullOrEmpty(logStyle) then
        self:Print(strformat(L["LOG_ERROR"], logStyle))
    end
end

--- Prints a formatted message, meant to symbolize an deprecated value
--- @param oldFunc string The old/deprecated function
--- @param newFunc string The new function to migrate to
--- @param version string The version the deprecated function will be removed in
function CraftPresence:PrintDeprecationWarning(oldFunc, newFunc, version)
    if self:GetFromDb("verboseMode") then
        local dataTable = {
            [L["TITLE_ATTEMPTED_FUNCTION"]] = self:GetOrDefault(oldFunc, L["TYPE_NONE"]),
            [L["TITLE_REPLACEMENT_FUNCTION"]] = self:GetOrDefault(newFunc, L["TYPE_NONE"]),
            [L["TITLE_REMOVAL_VERSION"]] = self:GetOrDefault(version, L["TYPE_UNKNOWN"])
        }
        self:Print(
                strformat(L["LOG_WARNING"], strformat(L["ERROR_FUNCTION_DEPRECATED"],
                        self:SerializeTable(dataTable)
                ))
        )
        self:Print(strformat(L["LOG_WARNING"], L["ERROR_FUNCTION_REPLACE"]))
    end
end

--[[ API GETTERS AND SETTERS ]]--

--- Print initial addon info, depending on platform and config data
function CraftPresence:PrintAddonInfo()
    self:Print(strformat(L["ADDON_LOAD_INFO"], self:GetAddOnInfo()["versionString"]))
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

--- Parse a Dynamic Table with the specified filter(s) for any matches
--- (INTERNAL USAGE ONLY)
---
--- @param tagName string The tag name that determines how data is processed in this method
--- @param query string The query to filter the placeholderTable by
--- @param dataTable table The table to iterate through
--- @param foundData boolean Output Value -- If any placeholders were found before or after
--- @param resultString string Output Value -- The current placeholder result string
--- @param isMultiTable boolean Optional Value -- If dataTable is comprised of smaller tables (Depth of 1)
--- @param visibleData table Optional Value -- Determines whether some keys can be dynamically parsed
--- @param enableCallback function Optional Value -- Used in value tables to determine if dynamic returns are allowed
---
--- @return boolean, string @ found_data, resultString
function CraftPresence:ParseDynamicTable(tagName, query, dataTable, foundData,
                                         resultString, isMultiTable, visibleData, enableCallback)
    tagName = self:GetOrDefault(tagName)
    dataTable = self:GetOrDefault(dataTable, {})
    visibleData = self:GetOrDefault(visibleData, {})
    foundData = self:GetOrDefault(foundData, false)
    resultString = self:GetOrDefault(resultString)
    isMultiTable = self:GetOrDefault(isMultiTable, false)

    for key, value in pairs(dataTable) do
        if isMultiTable and type(value) == "table" then
            foundData, resultString = self:ParseDynamicTable(
                    tagName, query, value, foundData,
                    resultString, not isMultiTable, visibleData, enableCallback
            )
        else
            local can_process = self:GetOrDefault(enableCallback ~= nil and enableCallback(key, value), true)
            if can_process then
                local prefix = self:GetOrDefault(type(value) == "table" and value.prefix)
                local newKey = prefix .. tostring(key) .. prefix
                local newValue = self:GetDynamicReturnValue(
                        (
                                type(value) == "table" and self:GetLength(visibleData) >= 2 and value[visibleData[1]]
                        ) or value,
                        (
                                type(value) == "table" and self:GetLength(visibleData) >= 2 and value[visibleData[2]]
                        ), self)
                if (self:IsNullOrEmpty(query) or (
                        self:FindMatches(strlower(newKey), query, false, 1, true) or
                                self:FindMatches(strlower(newValue), query, false, 1, true))
                ) then
                    foundData = true
                    resultString = resultString .. "\n " .. (strformat(
                            L["DATA_FOUND_DATA"], newKey, newValue
                    ))
                end
            end
        end
    end
    return foundData, resultString
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
    local DB_DEFAULTS = self:GetDefaults()
    if self.db.profile[grp] == nil or (reset and not key) then
        self.db.profile[grp] = DB_DEFAULTS.profile[grp]
    end
    if not key then
        return self.db.profile[grp]
    end
    if self.db.profile[grp][key] == nil or reset then
        self.db.profile[grp][key] = DB_DEFAULTS.profile[grp][key]
    end
    return self.db.profile[grp][key]
end

--- Sets Config Data based on the specified parameters
---
--- @param grp string The config group to retrieve
--- @param key string The config key to retrieve
--- @param newValue any The new config value to set
--- @param reset boolean Whether to reset this property value
function CraftPresence:SetToDb(grp, key, newValue, reset)
    local DB_DEFAULTS = self:GetDefaults()
    if self.db.profile[grp] == nil or (reset and not key) then
        self.db.profile[grp] = DB_DEFAULTS.profile[grp]
    end
    if not key then
        self.db.profile[grp] = newValue
    else
        if self.db.profile[grp][key] == nil or reset then
            self.db.profile[grp][key] = DB_DEFAULTS.profile[grp][key]
        end
        self.db.profile[grp][key] = newValue
    end
end

--- Generates Config Table for a set of custom arguments
---
--- @param rootKey string The config key to retrieve placeholder data (Required)
--- @param titleKey string The title key to display (Can be a function returning a string)
--- @param commentKey string The comment key to display (Can be a function returning a string)
--- @param changedCallback function The callback to trigger when a value is changed (Optional)
--- @param validCallback function The callback to trigger to determine if the new value is valid (Optional)
--- @param errorCallback function The callback to trigger if an error was encountered in changing the value (Optional)
---
--- @return table @ generatedData
function CraftPresence:GetPlaceholderArgs(rootKey, titleKey, commentKey, changedCallback, validCallback, errorCallback)
    if self:IsNullOrEmpty(rootKey) then
        return {}
    end
    titleKey = self:GetOrDefault(titleKey, rootKey)
    commentKey = self:GetOrDefault(commentKey)
    changedCallback = self:GetOrDefault(changedCallback)
    validCallback = self:GetOrDefault(validCallback, true)
    errorCallback = self:GetOrDefault(errorCallback)
    local found_count = 0

    titleKey = self:GetDynamicReturnValue(titleKey, type(titleKey), self)
    local table_args = {
        titleHeader = {
            order = self:GetNextIndex(), type = "header", name = titleKey
        }
    }
    local rootData = self:GetFromDb(rootKey)
    if type(rootData) == "table" then
        for key, value in pairs(rootData) do
            local value_args = {}
            if type(value) == "table" then
                for innerKey, _ in pairs(value) do
                    local valueType = (self:IsToggleTag(innerKey) and "toggle") or "input"
                    value_args[innerKey] = {
                        type = valueType, order = self:GetNextIndex(), width = 3.0,
                        name = self:GetOrDefault(L["TITLE_BUTTON_" .. strupper(innerKey)], self:FormatWord(innerKey)),
                        desc = self:GetOrDefault(L["COMMENT_BUTTON_" .. strupper(innerKey)]),
                        get = function(_)
                            return self.db.profile[rootKey][key][innerKey]
                        end,
                        set = function(_, newValue)
                            local oldValue = self.db.profile[rootKey][key][innerKey]
                            local isValid = (
                                    (valueType == "toggle" and type(newValue) == "boolean") or
                                            (type(newValue) == "string")
                            ) and (type(validCallback) ~= "function" or validCallback(self, newValue) == true)
                            local fieldName = (rootKey .. " (" .. key .. ", " .. innerKey .. ")")
                            if isValid then
                                self.db.profile[rootKey][key][innerKey] = newValue
                                self:PrintChangedValue(fieldName, oldValue, newValue)
                                if not self:IsNullOrEmpty(changedCallback) and type(changedCallback) == "function" then
                                    changedCallback(self, fieldName, oldValue, newValue)
                                end
                            elseif not self:IsNullOrEmpty(errorCallback) and type(errorCallback) == "function" then
                                errorCallback(self, fieldName, oldValue, newValue)
                            end
                        end,
                    }
                    -- Apply additional params
                    if valueType == "input" then
                        value_args[innerKey].multiline = (
                                self:EndsWith(strlower(innerKey), "data") or
                                        self:EndsWith(strlower(innerKey), "callback")
                        )
                        local value_usage = self:GetOrDefault(L["USAGE_BUTTON_" .. strupper(innerKey)])
                        if not self:IsNullOrEmpty(value_usage) then
                            value_args[innerKey].usage = value_usage
                        end
                    end
                end
            end
            table_args[key] = {
                name = self:GetOrDefault(L["TITLE_" .. strupper(key)], key),
                desc = self:GetOrDefault(L["COMMENT_" .. strupper(key)]),
                type = "group", order = self:GetNextIndex(),
                args = value_args
            }
            found_count = found_count + 1
        end
    end

    commentKey = self:GetDynamicReturnValue(commentKey, type(commentKey), found_count)
    table_args[self:RandomString(8)] = {
        type = "description", order = self:GetNextIndex(), fontSize = "medium",
        name = commentKey
    }
    return table_args
end

--- Retrieve whether or not the specified key is a toggleable tag
---
--- @param key string The key to interpret
---
--- @return boolean @ isToggleTag
function CraftPresence:IsToggleTag(key)
    key = strlower(self:GetOrDefault(key))
    return self:EndsWith(key, "enabled") or self:FindMatches(key, "allow", false, 1, true)
end

--[[ QUEUE SYSTEM UTILITIES ]]--

local queued_data = { }
local queue_frame

--- Schedules a function to occur after the specified delay
---
--- @param seconds number The delay in seconds until task execution (Minimum: 1)
--- @param func function The function to perform when delay expires
--- @param args any The arguments, if any, to use with the function
CraftPresence.After = CraftPresence:vararg(3, function(self, seconds, func, args)
    if type(seconds) == "number" and seconds >= 1 and type(func) == "function" then
        local f = function()
            func(unpack(args))
        end

        self:SetupQueueSystem()
        queued_data[f] = GetTime() + seconds
    end
end)

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
