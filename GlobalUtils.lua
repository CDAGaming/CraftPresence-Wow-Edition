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
local type, tgetn, assert, tconcat = type, table.getn, assert, table.concat
local tinsert, strchar, random, loadstring, pairs = table.insert, string.char, math.random, loadstring, pairs
local strlen, strsub, strfind = string.len, string.sub, string.find

local tsetn = function(t, n)
    setmetatable(t, { __len = function()
        return n
    end })
end

local wipe = (table.wipe or function(table)
    for k, _ in pairs(table) do
        table[k] = nil
    end
    tsetn(table, 0)
    return table
end)

local charset = {}
do
    -- [0-9a-zA-Z]
    for c = 48, 57 do
        tinsert(charset, strchar(c))
    end
    for c = 65, 90 do
        tinsert(charset, strchar(c))
    end
    for c = 97, 122 do
        tinsert(charset, strchar(c))
    end
end

-- Addon APIs
CraftPresence = LibStub("AceAddon-3.0"):NewAddon("CraftPresence", "AceConsole-3.0", "AceEvent-3.0")

CraftPresence.libraries = {
    AceLocale = LibStub("AceLocale-3.0"),
    AceConfig = LibStub("AceConfig-3.0"),
    AceConfigCmd = LibStub("AceConfigCmd-3.0"),
    AceConfigDialog = LibStub("AceConfigDialog-3.0"),
    AceConfigRegistry = LibStub("AceConfigRegistry-3.0"),
    AceDB = LibStub("AceDB-3.0"),
    AceDBOptions = LibStub("AceDBOptions-3.0"),
    LDB = nil, -- Only used in 1.12.x or above
    LDBIcon = nil -- Only used in 1.12.x or above
}

CraftPresence.internals = {
    name = "CraftPresence",
    identifier = "craftpresence",
    affix = "cp",
    defaultColorPrefix = "|cFF",
    lengths = {
        rpc = 11,
        multiline = 12
    },
    rpc = {
        buttonsSplitKey = "==",
        eventTag = "$RPCEvent$",
        eventSeperator = "|",
        chatSeperator = "||"
    },
    defaults = {
        innerKey = "@",
        globalKey = "#",
        version = "0.0.0",
        build = "0000",
        date = "Jan 1 1969",
        toc = 00000
    }
}

CraftPresence.cache = {
    features = nil,
    flavors = nil,
    addon_info = nil,
    build_info = nil,
    build_tag = nil,
    compatibility_info = nil,
    compatibility_attachments = nil
}

CraftPresence.colors = {
    GREEN = CraftPresence.internals.defaultColorPrefix .. "00FF7F",
    GREY = CraftPresence.internals.defaultColorPrefix .. "9B9B9B",
    RED = CraftPresence.internals.defaultColorPrefix .. "FF6060",
    GOLD = CraftPresence.internals.defaultColorPrefix .. "FFD700",
    LIGHT_BLUE = CraftPresence.internals.defaultColorPrefix .. "69CCF0"
}

-- LUA UTILITIES

--- Set the width and height of the specified frame
--- (INTERNAL USAGE ONLY)
---
--- @param frame any The specified frame to interpret (Required)
--- @param expectedWidth number The specified width to be set
--- @param expectedHeight number The specified height to be set
function CraftPresence:AssertFrameSize(frame, expectedWidth, expectedHeight)
    if not frame then return end
    if expectedWidth then
        frame:SetWidth(expectedWidth)
    end
    if expectedHeight then
        frame:SetHeight(expectedHeight)
    end
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

--- Copy the source table contents to the destination table
---
--- @param src table The source table to interpret
--- @param dest table The destination table to be returned
---
--- @return table @ result
function CraftPresence:CopyTable(src, dest)
    if type(dest) ~= "table" then
        dest = {}
    else
        wipe(dest)
    end
    if type(src) == "table" then
        for k, v in pairs(src) do
            if type(v) == "table" then
                -- try to index the key first so that the metatable creates the defaults, if set, and use that table
                v = self:CopyTable(v, dest[k])
            end
            dest[k] = v
        end
    end
    return dest
end

--- Generates a random string of numbers
---
--- @param length number The length of the resulting number
---
--- @return number @ random_string
function CraftPresence:RandomString(length)
    if not length or length <= 0 then
        return ''
    end
    if math.randomseed and os then
        math.randomseed(os.clock() ^ 5)
    end
    return self:RandomString(length - 1) .. charset[random(1, tgetn(charset))]
end

--- Determines if the specified object is null or empty
---
--- @param obj any The object to interpret
---
--- @return boolean @ is_object_empty
function CraftPresence:IsNullOrEmpty(obj)
    return obj == nil or
        (type(obj) == "string" and obj == "") or
        (type(obj) == "table" and obj == {})
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
    if self:IsNullOrEmpty(str) then
        return str
    end

    local b, e = strfind(str, old, 1, plain)
    if b == nil then
        return str
    else
        return strsub(str, 1, b - 1) .. new .. self:Replace(strsub(str, e + 1), old, new, plain)
    end
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
    if self:IsNullOrEmpty(str) then
        return str
    end
    str = self:Replace(str, pattern_one or "*", replacer_one or "", plain)
    str = self:Replace(str, pattern_two or "%^", replacer_two or "", plain)
    return str
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

local supports_ellipsis = loadstring("return ...") ~= nil
local template_args = supports_ellipsis and "{...}" or "arg"

--- Variable Argument Operation
---
--- @param n number The number of static args
--- @param f function The function to trigger with args
---
--- @return any @ result
function CraftPresence:vararg(n, f)
    local t = {}
    local params = ""
    if n > 0 then
        for i = 1, n do t[i] = "_" .. i end
        params = tconcat(t, ", ", 1, n)
        params = params .. ", "
    end
    local code = [[
        return function( f )
        return function( ]] .. params .. [[... )
            return f( ]] .. params .. template_args .. [[ )
        end
        end
    ]]
    return assert(loadstring(code, "=(vararg)"))()(f)
end
