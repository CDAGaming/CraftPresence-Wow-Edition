-- Global Namespace for function definitions before loading
CP_GlobalUtils = {}

-- Lua APIs
local _G = getfenv() or _G or {}
local type, strsub, strfind, tgetn, assert = type, string.sub, string.find, table.getn, assert
local tinsert, strchar, random, next = table.insert, string.char, math.random, next

----------------------------------
--COMPATIBILITY UTILITIES
----------------------------------

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

----------------------------------
--LUA UTILITIES
----------------------------------

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
function CP_GlobalUtils.RandomString(length)
    if not length or length <= 0 then
        return ''
    end
    if math.randomseed and os then
        math.randomseed(os.clock() ^ 5)
    end
    return CP_GlobalUtils.RandomString(length - 1) .. charset[random(1, tgetn(charset))]
end

--- Determines if the specified object is null or empty
---
--- @param obj any The object to interpret
---
--- @return boolean @ is_object_empty
function CP_GlobalUtils.IsNullOrEmpty(obj)
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
function CP_GlobalUtils.Replace(str, old, new, plain)
    if CP_GlobalUtils.IsNullOrEmpty(str) then
        return str
    end

    local b,e = strfind(str, old, 1, plain)
    if b==nil then
        return str
    else
        return strsub(str, 1, b-1) .. new .. CP_GlobalUtils.Replace(strsub(str, e+1), old, new, plain)
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
function CP_GlobalUtils.SetFormat(str, replacer_one, replacer_two, pattern_one, pattern_two, plain)
    if CP_GlobalUtils.IsNullOrEmpty(str) then
        return str
    end
    str = CP_GlobalUtils.Replace(str, pattern_one or "*", replacer_one or "", plain)
    str = CP_GlobalUtils.Replace(str, pattern_two or "%^", replacer_two or "", plain)
    return str
end

return CP_GlobalUtils