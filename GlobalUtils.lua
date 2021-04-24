-- Global Namespace for function definitions before loading
CP_GlobalUtils = {}

-- Lua APIs
local strgsub = string.gsub

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

--- Replaces a String with the specified formatting
---
--- @param str string The input string to evaluate
--- @param replacer_one string The first replacer
--- @param replacer_two string The second replacer
--- @param pattern_one string The first pattern
--- @param pattern_two string The second pattern
---
--- @return string @ formatted_string
function CP_GlobalUtils.SetFormat(str, replacer_one, replacer_two, pattern_one, pattern_two)
    if CP_GlobalUtils.IsNullOrEmpty(str) then
        return str
    end
    str = strgsub(str, pattern_one or "*", replacer_one or "")
    str = strgsub(str, pattern_two or "%^", replacer_two or "")
    return str
end

return CP_GlobalUtils