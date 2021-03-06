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
local _G = getfenv() or _G or {}
local type, strsub, strfind, tgetn, assert, tconcat = type, string.sub, string.find, table.getn, assert, table.concat
local tinsert, strchar, random, next, loadstring = table.insert, string.char, math.random, next, loadstring

-- Addon APIs
CP_GlobalUtils = {}

--[[ COMPATIBILITY UTILITIES ]]--

local function SecureNext(elements, key)
    return _G.securecall(next, elements, key);
end

local function AssertFrameSize(frame, expectedWidth, expectedHeight)
    frame:SetWidth(expectedWidth)
    frame:SetHeight(expectedHeight)
end

InterfaceOptionsFrame_Show = function()
    AssertFrameSize(InterfaceOptionsFrame, 858, 660)
    AssertFrameSize(InterfaceOptionsFrameCategories, 175, 569)
    AssertFrameSize(InterfaceOptionsFrameAddOns, 175, 569)
    if (InterfaceOptionsFrame:IsShown()) then
        InterfaceOptionsFrame:Hide();
    else
        InterfaceOptionsFrame:Show();
    end
end

InterfaceOptionsFrame_OpenToCategory = InterfaceOptionsFrame_OpenToCategory or (function(panel)
    local panelName;
    if (type(panel) == "string") then
        panelName = panel
        panel = nil
    end

    assert(panelName or panel, 'Usage: InterfaceOptionsFrame_OpenToCategory("categoryName" or panel)');

    local elementToDisplay

    for _, element in SecureNext, _G.INTERFACEOPTIONS_ADDONCATEGORIES do
        if (element == panel or (panelName and element.name and element.name == panelName)) then
            elementToDisplay = element
            break
        end
    end

    if (not elementToDisplay) then
        return
    end

    InterfaceOptionsFrameTab2:Click();
    local buttons = InterfaceOptionsFrameAddOns.buttons;
    for i, button in SecureNext, buttons do
        if (button.element == elementToDisplay) then
            InterfaceOptionsFrameAddOns.buttons[i]:Click()
        elseif (elementToDisplay.parent and button.element and (
                button.element.name == elementToDisplay.parent and button.element.collapsed
        )) then
            OptionsListButtonToggle_OnClick(button.toggle)
        end
    end

    if (not InterfaceOptionsFrame:IsShown()) then
        InterfaceOptionsFrame_Show()
    end
end)

--[[ LUA UTILITIES ]]--

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

--- Generates a random string of numbers
---
--- @param length number The length of the resulting number
---
--- @return number @ random_string
function CP_GlobalUtils:RandomString(length)
    if not length or length <= 0 then
        return ''
    end
    if math.randomseed and os then
        math.randomseed(os.clock() ^ 5)
    end
    return CP_GlobalUtils:RandomString(length - 1) .. charset[random(1, tgetn(charset))]
end

--- Determines if the specified object is null or empty
---
--- @param obj any The object to interpret
---
--- @return boolean @ is_object_empty
function CP_GlobalUtils:IsNullOrEmpty(obj)
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
function CP_GlobalUtils:Replace(str, old, new, plain)
    if CP_GlobalUtils:IsNullOrEmpty(str) then
        return str
    end

    local b, e = strfind(str, old, 1, plain)
    if b == nil then
        return str
    else
        return strsub(str, 1, b - 1) .. new .. CP_GlobalUtils:Replace(strsub(str, e + 1), old, new, plain)
    end
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
function CP_GlobalUtils:SetFormat(str, replacer_one, replacer_two, pattern_one, pattern_two, plain)
    if CP_GlobalUtils:IsNullOrEmpty(str) then
        return str
    end
    str = CP_GlobalUtils:Replace(str, pattern_one or "*", replacer_one or "", plain)
    str = CP_GlobalUtils:Replace(str, pattern_two or "%^", replacer_two or "", plain)
    return str
end

local supports_ellipsis = loadstring("return ...") ~= nil
local template_args = supports_ellipsis and "{...}" or "arg"

--- Variable Argument Operation
---
--- @param n number The number of static args
--- @param f function The function to trigger with args
---
--- @return any @ result
function CP_GlobalUtils:vararg(n, f)
    local t = {}
    local params = ""
    if n > 0 then
        for i = 1, n do t[ i ] = "_"..i end
        params = tconcat(t, ", ", 1, n)
        params = params .. ", "
    end
    local code = [[
        return function( f )
        return function( ]]..params..[[... )
            return f( ]]..params..template_args..[[ )
        end
        end
    ]]
    return assert(loadstring(code, "=(vararg)"))()(f)
end

return CP_GlobalUtils