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
local type, pairs, tonumber, loadstring, unpack = type, pairs, tonumber, loadstring, unpack
local strgmatch, strgfind, strformat, strgsub = string.gmatch, string.gfind, string.format, string.gsub
local strsub, strfind, strlower, strupper, tostring = string.sub, string.find, string.lower, string.upper, tostring
local strlen, strrep, tgetn, tinsert, tconcat = string.len, string.rep, table.getn, table.insert, table.concat
local CreateFrame, UIParent, GetTime = CreateFrame, UIParent, GetTime

--[[ LUA UTILITIES ]]--

local lastIndex = 0

--- Retrieves the Next Available Button Id for use in the currently open Screen
---
--- @return number @ lastIndex
function CraftPresence:GetNextIndex()
    lastIndex = lastIndex + 1
    return lastIndex
end

--- Resets the Button Index to 0
--- Normally used before closing/opening a screen and/or no longer using the allocated Id's
---
--- @return number @ lastIndex
function CraftPresence:ResetIndex()
    lastIndex = 0
    return lastIndex
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

--- Trims a String of leading and duplicate spaces
---
--- @param str string The input string to evaluate (Required)
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
--- @param str string The input string to evaluate (Required)
--- @param expected string The expected string to locate
---
--- @return boolean @ startsWith
function CraftPresence:StartsWith(str, expected)
    if self:IsNullOrEmpty(str) then return false end
    return self:ContainsAt(str, expected, 1, self:GetLength(expected))
end

--- Determines whether the specified string ends with the specified pattern
---
--- @param str string The input string to evaluate (Required)
--- @param expected string The expected string to locate
---
--- @return boolean @ endsWith
function CraftPresence:EndsWith(str, expected)
    if self:IsNullOrEmpty(str) then return false end
    local start_len = (self:GetLength(str) - self:GetLength(expected)) + 1
    return self:ContainsAt(str, expected, start_len, self:GetLength(str))
end

--- Determines whether the specified string includes the specified pattern at the given position
---
--- @param str string The input string to evaluate (Required)
--- @param expected string The expected string to locate
--- @param start_len number The beginning length to iterate at (Default: 1)
--- @param end_len number The ending length to iterate at (Default: input string length)
---
--- @return boolean @ containsAt
function CraftPresence:ContainsAt(str, expected, start_len, end_len)
    if self:IsNullOrEmpty(str) then return false end
    start_len = self:GetOrDefault(start_len, 1)
    end_len = self:GetOrDefault(end_len, self:GetLength(str))
    return strsub(str, start_len, end_len) == expected
end

--- Formats the following word to proper casing (Xxxx)
---
--- @param str string The input string to evaluate (Required)
---
--- @return string @ formattedString
function CraftPresence:FormatWord(str)
    if self:IsNullOrEmpty(str) then return str end
    str = strlower(str)
    return (strupper(strsub(str, 1, 1)) .. strsub(str, 2))
end

--- Formats the following word to proper icon casing
--- (Helper Function for GetCaseData for ease-of-access)
---
--- @param str string The input string to evaluate (Required)
---
--- @return string @ formattedString
function CraftPresence:FormatAsIcon(str)
    if self:IsNullOrEmpty(str) then return str end
    return self:FormatWithCasing(str, "icon")
end

--- Formats the following word to the specified casing
--- (Helper Function for GetCaseData for ease-of-access)
---
--- @param str string The input string to evaluate (Required)
--- @param casing string The specified casing to use (lower/upper/icon) (Required)
---
--- @return string @ formattedString
function CraftPresence:FormatWithCasing(str, casing)
    if self:IsNullOrEmpty(str) or self:IsNullOrEmpty(casing) then
        return str
    end
    return self:GetCaseData({ str, casing })
end

--- Initializes the identifier and tag from a config setting for further usage
--- (INTERNAL USAGE ONLY)
---
--- @param identifier string The input string to evaluate (Required)
--- @param tag string If specified, append this string to the lookup for evaluation (Optional)
---
--- @return string, string @ formattedIdentifier, concatenatedIdentifier
function CraftPresence:SetupConfigIdentifier(identifier, tag)
    if self:IsNullOrEmpty(identifier) then return end
    tag = self:GetOrDefault(tag)
    local str = strupper(identifier)
    local full_str = str
    if not self:IsNullOrEmpty(tag) then
        full_str = full_str .. "_" .. tag
    end

    return str, full_str
end

--- Retrieve the translated config setting title, depending on arguments
--- (Helper Function for GetConfigMetadata, but can be used seperatly)
---
--- If localized text cannot be found, '<identifier>' will be returned.
---
--- @param identifier string The input string to evaluate (Required)
--- @param tag string If specified, append this string to the lookup for evaluation (Optional)
--- @param str string If specified alongside full_str, Skip SetupConfigIdentifier (Optional)
--- @param full_str string See str param for more info. (Optional)
---
--- @return string @ formattedTitle
function CraftPresence:GetConfigTitle(identifier, tag, str, full_str)
    if self:IsNullOrEmpty(identifier) then return identifier end
    tag = self:GetOrDefault(tag)
    if self:IsNullOrEmpty(str) and self:IsNullOrEmpty(full_str) then
        str, full_str = self:SetupConfigIdentifier(identifier, tag)
    end

    local fallback_title = self:GetOrDefault(self.locale["TITLE_" .. str], identifier)
    local value_title = self:GetOrDefault(self.locale["TITLE_" .. full_str], fallback_title)
    return value_title
end

--- Retrieve the translated config setting comment, depending on arguments
--- (Helper Function for GetConfigMetadata, but can be used seperatly)
---
--- If localized text for a 'DEFAULT_' entry is found,
--- it will also be attached to the returned comment, if a 'COMMENT_' entry exists.
---
--- @param identifier string The input string to evaluate (Required)
--- @param tag string If specified, append this string to the lookup for evaluation (Optional)
--- @param str string If specified alongside full_str, Skip SetupConfigIdentifier (Optional)
--- @param full_str string See str param for more info. (Optional)
---
--- @return string @ formattedComment
function CraftPresence:GetConfigComment(identifier, tag, str, full_str)
    if self:IsNullOrEmpty(identifier) then return identifier end
    tag = self:GetOrDefault(tag)
    if self:IsNullOrEmpty(str) and self:IsNullOrEmpty(full_str) then
        str, full_str = self:SetupConfigIdentifier(identifier, tag)
    end

    local value_comment = self:GetOrDefault(self.locale["COMMENT_" .. full_str], self.locale["COMMENT_" .. str])
    if not self:IsNullOrEmpty(value_comment) then
        local value_default = self:GetOrDefault(self.locale["DEFAULT_" .. full_str], self.locale["DEFAULT_" .. str])
        if not self:IsNullOrEmpty(value_default) then
            value_comment = strformat(self.locale["FORMAT_COMMENT"], value_comment, value_default)
        end
    end
    return value_comment
end

--- Retrieve the translated config setting usage text, depending on arguments
--- (Helper Function for GetConfigMetadata, but can be used seperatly)
---
--- For Ace3 Tables, a null usage field is still displayable,
--- so a nil check may be needed afterwords for correctness.
---
--- @param identifier string The input string to evaluate (Required)
--- @param tag string If specified, append this string to the lookup for evaluation (Optional)
--- @param str string If specified alongside full_str, Skip SetupConfigIdentifier (Optional)
--- @param full_str string See str param for more info. (Optional)
---
--- @return string @ formattedUsage
function CraftPresence:GetConfigUsage(identifier, tag, str, full_str)
    if self:IsNullOrEmpty(identifier) then return identifier end
    tag = self:GetOrDefault(tag)
    if self:IsNullOrEmpty(str) and self:IsNullOrEmpty(full_str) then
        str, full_str = self:SetupConfigIdentifier(identifier, tag)
    end

    local value_usage = self:GetOrDefault(self.locale["USAGE_" .. full_str], self.locale["USAGE_" .. str])
    return value_usage
end

--- Retrieve the setting title, comment, and usage locale info (if possible)
--- using the identifier, and if specified, a concatenated tag identifier.
---
--- @param identifier string The input string to evaluate (Required)
--- @param tag string If specified, append this string to the lookup for evaluation (Optional)
--- @param str string If specified alongside full_str, Skip SetupConfigIdentifier (Optional)
--- @param full_str string See str param for more info. (Optional)
---
--- @return string, string, string @ formattedTitle, formattedComment, formattedUsage
function CraftPresence:GetConfigMetadata(identifier, tag, str, full_str)
    if self:IsNullOrEmpty(identifier) then return identifier end
    tag = self:GetOrDefault(tag)
    if self:IsNullOrEmpty(str) and self:IsNullOrEmpty(full_str) then
        str, full_str = self:SetupConfigIdentifier(identifier, tag)
    end

    local value_title = self:GetConfigTitle(identifier, tag, str, full_str)
    local value_comment = self:GetConfigComment(identifier, tag, str, full_str)
    local value_usage = self:GetConfigUsage(identifier, tag, str, full_str)

    return value_title, value_comment, value_usage
end

--- Return a modified version of the specified string, depending on arguments
--- Valid Case Types: lower|upper|icon|no-dupes
---
--- @param obj any A table (Format: string, casing) or string to evaluate (Required)
---
--- @return string @ adjusted_data
function CraftPresence:GetCaseData(obj)
    if self:IsNullOrEmpty(obj) then return obj end
    local value
    if type(obj) == "table" then
        local innerValue = obj
        if self:GetLength(innerValue) >= 1 then
            value = self:GetOrDefault(innerValue[1])
            if self:GetLength(innerValue) >= 2 then
                local command_query = self:Split(innerValue[2], ",", true, true)
                for k,v in pairs(command_query) do
                    local caseType = strlower(self:GetOrDefault(v))
                    if caseType == "lower" or caseType == "icon" then
                        value = strlower(value)
                        if caseType == "icon" then
                            value = self:Replace(value, "%s+", "_")
                        end
                    elseif caseType == "upper" then
                        value = strupper(value)
                    elseif caseType == "no-dupes" then
                        value = self:RemoveDuplicateWords(value)
                    end
                end
            end
        end
    else
        value = self:GetOrDefault(obj)
    end
    return value
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

--- Removes duplicate words from a string
---
--- @param data any A table of string-elements or a string to interpret
--- @param seperator string The separator to split and concat the resulting string by
---
--- @return string @ output
function CraftPresence:RemoveDuplicateWords(data, seperator)
    data = self:GetOrDefault(data, {})
    seperator = self:GetOrDefault(seperator, " ")
    -- Ensure the type is initially a table consisting of strings
    if type(data) ~= "table" then
        if type(data) == "string" then
            data = self:Split(data, seperator, true, true)
        else
            return data
        end
    elseif self:GetLength(data) > 0 then
        return data
    end

    local output = {}
    for i,v in pairs(data) do
        if data[i-1] ~= v then
            tinsert(output,v)
        end
    end

    return self:GetOrDefault(tconcat(output, seperator))
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
                        [self.locale["TITLE_ATTEMPTED_FUNCTION"]] = value[1],
                        [self.locale["TITLE_FUNCTION_MESSAGE"]] = err
                    }
                    self:PrintErrorMessage(strformat(self.locale["ERROR_FUNCTION"], self:SerializeTable(dataTable)))
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
            ["fallback_version"] = fallbackVersion,
            ["build"] = build,
            ["date"] = date,
            ["toc_version"] = tocversion,
            ["fallback_toc_version"] = fallbackTOC
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
            version = GetAddOnMetadata(self.locale["ADDON_NAME"], "Version")
            schema = GetAddOnMetadata(self.locale["ADDON_NAME"], "X-Schema-ID")
        end

        if not self:ContainsDigit(version) then
            self:PrintWarningMessage(strformat(self.locale["WARNING_BUILD_UNSUPPORTED"], version))
            version = "v" .. fallbackVersion
        end
        versionString = strformat(self.locale["ADDON_HEADER_VERSION"], self.locale["ADDON_NAME"], version)

        addon_info = {
            ["version"] = version,
            ["versionString"] = versionString,
            ["schema"] = tonumber(schema)
        }
    end
    return addon_info
end

--- Retrieve the specified Game Variable
---
--- @param str string The specified variable name (Required)
--- @param result_type string The expected type of the CVar (Default: string)
--- @param fallback string If defined, use this value if the CVar does not exist
---
--- @return any @ variable_info
function CraftPresence:GetGameVariable(str, result_type, fallback)
    if self:IsNullOrEmpty(str) then return str end
    result_type = strlower(self:GetOrDefault(result_type, "string"))
    fallback = self:GetOrDefault(fallback)

    local result
    if C_CVar then
        result = C_CVar.GetCVar(str)
    elseif GetCVar then
        result = GetCVar(str)
    end
    result = self:GetOrDefault(result, fallback)

    -- Type Conversion
    if result_type == "number" then
        return tonumber(result)
    else
        return result
    end
end

--- Retrieve and/or Synchronize Build Flavor Info
---
--- @return table @ flavor_info
function CraftPresence:GetFlavorInfo()
    if not flavor_info then
        flavor_info = {
            ["retail"] = 90205, -- Latest Retail
            ["classic"] = 20504, -- Latest Classic
            ["classic_era"] = 11403, -- Latest Classic Era
            ["ptr"] = 90205, -- Latest Retail PTR
            ["beta"] = 100000, -- Latest Retail Beta
            ["classic_ptr"] = 20504, -- Latest Classic PTR
            ["classic_beta"] = 30400, -- Latest Classic Beta
            ["classic_era_ptr"] = 11403, -- Latest Classic Era PTR
            ["classic_era_beta"] = 11403 -- Latest Classic Era Beta
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
            ["10.0.0"] = 100000, -- Dragonflight 10.0.0
            ["9.0.0"] = 90000, -- Shadowlands 9.0.0
            ["8.0.0"] = 80000, -- BFA 8.0.0
            ["7.0.0"] = 70000, -- Legion 7.0.0
            ["6.0.0"] = 60000, -- WOD 6.0.0
            ["5.0.0"] = 50000, -- MOP 5.0.0
            ["4.0.0"] = 40000, -- CATA 4.0.0
            ["3.4.0"] = 30400, -- WOTLK Classic 3.4.0
            ["3.0.0"] = 30000, -- WOTLK 3.0.0
            ["2.5.0"] = 20500, -- TBC Classic 2.5.0
            ["2.0.0"] = 20000, -- TBC 2.0.0
            ["1.14.0"] = 11400, -- Vanilla Classic (SOM) 1.14.0
            ["1.13.0"] = 11300, -- Vanilla Classic (Original) 1.13.0
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

--- Determine if this build identifies as Wrath Classic
--- @return boolean @ is_wrath_rebased
function CraftPresence:IsWrathRebased()
    return self:IsWithinValue(
            self:GetBuildInfo()["toc_version"],
            self:GetCompatibilityInfo()["3.4.0"], self:GetCompatibilityInfo()["4.0.0"],
            true, false
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as the Retail Live Build of the Game
--- @return boolean @ is_retail_live_build
function CraftPresence:IsRetailLiveBuild()
    return self:GetBuildInfo()["toc_version"] == self:GetFlavorInfo()["retail"]
end

--- Determine if this build identifies as the Retail PTR Build of the Game
--- @return boolean @ is_retail_ptr_build
function CraftPresence:IsRetailPTRBuild()
    return self:IsWithinValue(
            self:GetBuildInfo()["toc_version"],
            self:GetFlavorInfo()["retail"], self:GetFlavorInfo()["ptr"],
            false, true
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as the Retail Beta Build of the Game
--- @return boolean @ is_retail_beta_build
function CraftPresence:IsRetailBetaBuild()
    return self:IsWithinValue(
            self:GetBuildInfo()["toc_version"],
            self:GetFlavorInfo()["ptr"], self:GetFlavorInfo()["beta"],
            false, true
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as a Retail Build of the Game
--- @return boolean @ is_retail_build
function CraftPresence:IsRetailBuild()
    return self:IsRetailLiveBuild() or self:IsRetailPTRBuild() or self:IsRetailBetaBuild()
end

--- Determine if this build identifies as the Classic Live Build of the Game
--- @return boolean @ is_classic_live_build
function CraftPresence:IsClassicLiveBuild()
    return self:GetBuildInfo()["toc_version"] == self:GetFlavorInfo()["classic"]
end

--- Determine if this build identifies as the Classic PTR Build of the Game
--- @return boolean @ is_classic_ptr_build
function CraftPresence:IsClassicPTRBuild()
    return self:IsWithinValue(
            self:GetBuildInfo()["toc_version"],
            self:GetFlavorInfo()["classic"], self:GetFlavorInfo()["classic_ptr"],
            false, true
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as the Classic Beta Build of the Game
--- @return boolean @ is_classic_beta_build
function CraftPresence:IsClassicBetaBuild()
    return self:IsWithinValue(
            self:GetBuildInfo()["toc_version"],
            self:GetFlavorInfo()["classic_ptr"], self:GetFlavorInfo()["classic_beta"],
            false, true
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as a Classic Build of the Game
--- @return boolean @ is_classic_build
function CraftPresence:IsClassicBuild()
    return self:IsClassicLiveBuild() or self:IsClassicPTRBuild() or self:IsClassicBetaBuild()
end

--- Determine if this build identifies as the Classic Era Live Build of the Game
--- @return boolean @ is_classic_era_live_build
function CraftPresence:IsClassicEraLiveBuild()
    return self:GetBuildInfo()["toc_version"] == self:GetFlavorInfo()["classic_era"]
end

--- Determine if this build identifies as the Classic Era PTR Build of the Game
--- @return boolean @ is_classic_era_ptr_build
function CraftPresence:IsClassicEraPTRBuild()
    return self:IsWithinValue(
            self:GetBuildInfo()["toc_version"],
            self:GetFlavorInfo()["classic_era"], self:GetFlavorInfo()["classic_era_ptr"],
            false, true
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as the Classic Era Beta Build of the Game
--- @return boolean @ is_classic_era_beta_build
function CraftPresence:IsClassicEraBetaBuild()
    return self:IsWithinValue(
            self:GetBuildInfo()["toc_version"],
            self:GetFlavorInfo()["classic_era_ptr"], self:GetFlavorInfo()["classic_era_beta"],
            false, true
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as a Classic Era Build of the Game
--- @return boolean @ is_classic_era_build
function CraftPresence:IsClassicEraBuild()
    return self:IsClassicEraLiveBuild() or self:IsClassicEraPTRBuild() or self:IsClassicEraBetaBuild()
end

--- Determine if this build is using a rebased api
--- @return boolean @ is_rebased_api
function CraftPresence:IsRebasedApi()
    return self:IsClassicRebased() or self:IsTBCRebased() or self:IsWrathRebased()
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
--- @param index number If specified, returns a specific index of the split (Optional)
---
--- @return number @ buildVersion
function CraftPresence:VersionToBuild(versionStr, index)
    index = self:GetOrDefault(index, -1)
    versionStr = self:GetOrDefault(versionStr, fallbackVersion)
    local buildStr = ""
    local splitData = self:Split(versionStr, ".", true, true)
    if index >= 1 and splitData[index] then
        buildStr = splitData[index]
    elseif splitData[1] and splitData[1] == versionStr then
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
--- @return any @ frame_object
function CraftPresence:ShowConfig()
    if self.config then
        if InterfaceOptionsFrame_OpenToCategory and self.optionsFrame then
            -- a bug can occur in blizzard's implementation of this call
            -- so it is called twice to workaround it
            InterfaceOptionsFrame_OpenToCategory(self.optionsFrame)
            InterfaceOptionsFrame_OpenToCategory(self.optionsFrame)
            return self.optionsFrame
        else
            self.config:Open(self.locale["ADDON_NAME"])
        end
    else
        self:PrintErrorMessage(strformat(self.locale["ERROR_FUNCTION_DISABLED"], "ShowConfig"))
    end
end

--[[ LOGGING DATA ]]--

--- Prints a formatted message, meant to symbolize an error message
--- @param logStyle string The log format to follow
function CraftPresence:PrintErrorMessage(logStyle)
    if not self:IsNullOrEmpty(logStyle) then
        self:Print(strformat(self.locale["LOG_ERROR"], logStyle))
    end
end

--- Prints a formatted message, meant to symbolize a warning message
--- @param logStyle string The log format to follow
function CraftPresence:PrintWarningMessage(logStyle)
    if not self:IsNullOrEmpty(logStyle) then
        self:Print(strformat(self.locale["LOG_WARNING"], logStyle))
    end
end

--- Prints a formatted message, meant to symbolize a migration message
--- @param current number The current/former schema number (Default: 0)
--- @param target number The new schema number to convert this config against (Default: addOnInfo["schema"])
function CraftPresence:PrintMigrationMessage(current, target)
    current = self:GetOrDefault(current, 0)
    target = self:GetOrDefault(target, self:GetAddOnInfo()["schema"])
    self:Print(strformat(self.locale["INFO_OPTIONAL_MIGRATION_DATA_ONE"], current, target))
    self:Print(strformat(self.locale["INFO_OPTIONAL_MIGRATION_DATA_TWO"], self.locale["TITLE_OPTIONAL_MIGRATIONS"]))
end

--- Prints a formatted message, meant to symbolize an deprecated value
--- @param oldFunc string The old/deprecated function
--- @param newFunc string The new function to migrate to
--- @param version string The version the deprecated function will be removed in
function CraftPresence:PrintDeprecationWarning(oldFunc, newFunc, version)
    if self:GetProperty("verboseMode") then
        local dataTable = {
            [self.locale["TITLE_ATTEMPTED_FUNCTION"]] = self:GetOrDefault(oldFunc, self.locale["TYPE_NONE"]),
            [self.locale["TITLE_REPLACEMENT_FUNCTION"]] = self:GetOrDefault(newFunc, self.locale["TYPE_NONE"]),
            [self.locale["TITLE_REMOVAL_VERSION"]] = self:GetOrDefault(version, self.locale["TYPE_UNKNOWN"])
        }
        self:PrintWarningMessage(
                strformat(self.locale["ERROR_FUNCTION_DEPRECATED"], self:SerializeTable(dataTable))
        )
        self:PrintWarningMessage(self.locale["ERROR_FUNCTION_REPLACE"])
    end
end

--- Print initial addon info, depending on platform and config data
function CraftPresence:PrintAddonInfo()
    self:Print(strformat(self.locale["ADDON_LOAD_INFO"], self:GetAddOnInfo()["versionString"]))
    if self:GetProperty("verboseMode") then
        self:Print(strformat(self.locale["ADDON_BUILD_INFO"], self:SerializeTable(self:GetBuildInfo())))
    end
end

--- Displays the specified usage command in a help text format
--- (INTERNAL USAGE ONLY)
---
--- @param usage string The usage command text (Required)
function CraftPresence:PrintUsageCommand(usage)
    if self:IsNullOrEmpty(usage) then return end
    self:Print(
            self.locale["USAGE_CMD_INTRO"] .. "\n" ..
                    usage .. "\n" ..
                    strformat(self.locale["USAGE_CMD_NOTE_ONE"], self.locale["ADDON_AFFIX"], self.locale["ADDON_ID"]) .. "\n" ..
                    self.locale["USAGE_CMD_NOTE_TWO"] .. "\n"
    )
end

--- Displays the specified usage command in a help text format
--- (INTERNAL USAGE ONLY)
---
--- @param root_name string The root command name (Required)
--- @param query_tag string The sub-argument to the root name, representing what we want a query from
function CraftPresence:PrintQueryCommand(root_name, query_tag)
    if self:IsNullOrEmpty(root_name) then return end
    local usageText = self.locale["USAGE_CMD_" .. strupper(root_name)]
    if not self:IsNullOrEmpty(query_tag) then
        local localeQuery = "USAGE_CMD_" .. strupper(query_tag) .. "_" .. strupper(root_name)
        if not self:IsNullOrEmpty(self.locale[localeQuery]) then
            usageText = usageText .. "\n" .. strformat(self.locale[localeQuery], strlower(query_tag))
        end
    end
    self:PrintUsageCommand(usageText)
end

--[[ API GETTERS AND SETTERS ]]--

--- Sets whether or not the timer is locked
--- @param newValue boolean New variable value (Required)
function CraftPresence:SetTimerLocked(newValue)
    if self:IsNullOrEmpty(newValue) then return end
    -- This method only executes if we are operating in a non-queue pipeline
    if not self:GetProperty("queuedPipeline") then
        timer_locked = newValue
    end
end

--- Returns whether or not the timer is locked
--- @return boolean @ timer_locked
function CraftPresence:IsTimerLocked()
    return timer_locked
end

--- Parse the specified string or table dynamically, using GetCaseData or GetDynamicReturnValue
---
--- @param obj table The table to interpret (Format: string,format_func,format_type)
---
--- @return string @ result
function CraftPresence:ParseDynamicFormatting(obj)
    if self:IsNullOrEmpty(obj) then return obj end
    local value
    if type(obj) == "table" then
        local innerValue = obj
        if self:GetLength(innerValue) >= 1 then
            value = self:GetOrDefault(innerValue[1])
            if not self:IsNullOrEmpty(value) and self:GetLength(innerValue) >= 2 then
                local format_func, format_type = innerValue[2], "string"
                if self:GetLength(innerValue) >= 3 then
                    format_type = self:GetOrDefault(innerValue[3])
                end

                if format_type == "function" then
                    value = self:GetDynamicReturnValue(format_func, format_type, self, value)
                else
                    value = self:GetCaseData({ value, format_func })
                end
            end
        end
    else
        value = self:GetOrDefault(obj)
    end
    return value
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
                local suffix = self:GetOrDefault(type(value) == "table" and value.suffix)
                local newKey = prefix .. tostring(key) .. suffix
                local newValue = self:GetDynamicReturnValue(
                        (
                                type(value) == "table" and self:GetLength(visibleData) >= 1 and value[visibleData[1]]
                        ) or value,
                        (
                                type(value) == "table" and self:GetLength(visibleData) >= 2 and value[visibleData[2]]
                        ), self)
                -- Assert value_type as string
                newValue = tostring(newValue)
                if (self:IsNullOrEmpty(query) or (
                        self:FindMatches(strlower(newKey), query, false, 1, true) or
                                self:FindMatches(strlower(newValue), query, false, 1, true))
                ) then
                    foundData = true
                    resultString = resultString .. "\n " .. (strformat(
                            self.locale["DATA_FOUND_DATA"], newKey, newValue
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
--- @param grp string The config group to retrieve (Required)
--- @param key string The config key to retrieve (Optional)
--- @param reset boolean Whether to reset this property value
---
--- @return any configValue
function CraftPresence:GetProperty(grp, key, reset)
    if self:IsNullOrEmpty(grp) then return nil end
    local defaults = self:GetDefaults()
    if self.db.profile[grp] == nil or (reset and not key) then
        self.db.profile[grp] = defaults.profile[grp]
    end
    if not key then
        return self.db.profile[grp]
    end
    if self.db.profile[grp][key] == nil or reset then
        self.db.profile[grp][key] = defaults.profile[grp][key]
    end
    return self.db.profile[grp][key]
end

--- Sets Config Data based on the specified parameters
---
--- @param grp string The config group to retrieve (Required)
--- @param key string The config key to retrieve (Optional)
--- @param newValue any The new config value to set (Leave Empty to reset value)
--- @param reset boolean Whether to reset this property value
function CraftPresence:SetProperty(grp, key, newValue, reset)
    if self:IsNullOrEmpty(grp) then return end
    local defaults = self:GetDefaults()
    if self.db.profile[grp] == nil or (reset and not key) then
        self.db.profile[grp] = defaults.profile[grp]
    end
    if not key then
        self.db.profile[grp] = newValue
    else
        if self.db.profile[grp][key] == nil or reset then
            self.db.profile[grp][key] = defaults.profile[grp][key]
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
function CraftPresence:GenerateDynamicTable(rootKey, titleKey, commentKey, changedCallback, validCallback, errorCallback)
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
    local rootData = self:GetProperty(rootKey)
    if type(rootData) == "table" then
        for k, v in pairs(rootData) do
            local key, value = k, v
            local value_args = {}
            local keyName, keyComment = self:GetConfigMetadata(key)
            if type(value) == "table" then
                for ik, _ in pairs(value) do
                    local innerKey = ik
                    local valueType = (self:IsToggleTag(innerKey) and "toggle") or "input"
                    local valueIdentifier = strupper(valueType) .. "_" .. strupper(innerKey)
                    local valueName, valueComment, valueUsage = self:GetConfigMetadata(valueIdentifier, strupper(rootKey))
                    value_args[innerKey] = {
                        type = valueType, order = self:GetNextIndex(), width = 3.0,
                        name = valueName, desc = valueComment,
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
                        ) and self.locale["TYPE_MULTILINE_LENGTH"] or false
                        if not self:IsNullOrEmpty(valueUsage) then
                            value_args[innerKey].usage = valueUsage
                        end
                    end
                end
            end
            table_args[key] = {
                name = keyName, desc = keyComment,
                type = "group", order = self:GetNextIndex(),
                args = value_args
            }
            found_count = found_count + 1
        end
    end

    commentKey = self:GetDynamicReturnValue(commentKey, type(commentKey), found_count)
    local commentId = self:RandomString(8)
    table_args[commentId] = {
        type = "description", order = self:GetNextIndex(), fontSize = "medium",
        name = commentKey
    }
    table_args[commentId.."_spacer"] = {
        type = "description", order = self:GetNextIndex(), name = ""
    }
    return table_args
end

--- Retrieve whether or not the specified key is a toggleable tag
--- (INTERNAL USAGE ONLY)
---
--- @param key string The key to interpret
---
--- @return boolean @ isToggleTag
function CraftPresence:IsToggleTag(key)
    key = strlower(self:GetOrDefault(key))
    return self:EndsWith(key, "enabled") or (
            self:FindMatches(key, "allow", false, 1, true) and true or false
    )
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
--- @return any @ frame_object
function CraftPresence:SetupQueueSystem()
    if not queue_frame then
        queue_frame = CreateFrame("Frame", nil, UIParent)
        queue_frame:SetScript("OnUpdate", function()
            local elapsed_time = GetTime()
            if queued_data ~= nil then
                for key, value in pairs(queued_data) do
                    if elapsed_time > value then
                        key()
                        queued_data[key] = nil
                    end
                end
            end
        end)
    end
    return queue_frame
end
