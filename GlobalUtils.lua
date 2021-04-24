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

--- Escapes Regex Characters within the specified string
---
--- @param str string The input string to evaluate
---
--- @return string @ escaped_string
function CP_GlobalUtils.RegexEscape(str)
    if CP_GlobalUtils.IsNullOrEmpty(str) then
        return str
    end
    return strgsub(str, "[%(%)%.%%%+%-%*%?%[%]%^%$]", "%%%0")
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
    if plain then
        old = CP_GlobalUtils.RegexEscape(old)
        new = CP_GlobalUtils.RegexEscape(new)
    end
    return strgsub(str, old, new)
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