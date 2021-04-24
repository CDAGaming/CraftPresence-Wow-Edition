-- Global Namespace for function definitions before loading
CP_GlobalUtils = {}

-- Lua APIs
local type, strsub, strfind = type, string.sub, string.find

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