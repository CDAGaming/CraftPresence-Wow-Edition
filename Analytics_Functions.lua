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
local type, pairs = type, pairs

--- Analyze the list of metric engines specified, interpreting those that should be processed
---
--- @param metric_engines table The table of metric engines to interpret
function CraftPresence:InitializeAnalytics(metric_engines)
    metric_engines = self:GetOrDefault(metric_engines, {})
    for key, data in pairs(metric_engines) do
        if self:ShouldProcessData(data) then
            -- If enabled, execute the engine's stateCallback then add it to the registered list
            self:GetDynamicReturnValue(data.stateCallback, "function", self)
            self.registeredMetrics[key] = data
        end
    end
end

--- Logs changed data, if possible and allowed, using the specified parameters
---
--- @param fieldName string The config name the change belongs to
--- @param oldValue any The old value of the config variable
--- @param value any The new value of the config variable
function CraftPresence:LogChangedValue(fieldName, oldValue, value)
    for key, data in pairs(self.registeredMetrics) do
        -- Execute the processCallback for each registered engine
        self:GetDynamicReturnValue(data.processCallback, "function", self, fieldName, oldValue, value)
    end
end