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
local type = type

-- Addon APIs
local LibStub = LibStub

local L = CraftPresence.locale

function CraftPresence:CanUseAnalytics()
    return self:AreAnalyticsAllowed()-- and (self:IsClassicBuild() or self:IsRetailBuild())
end

function CraftPresence:InitializeAnalytics()
    if not self:CanUseAnalytics() then return end
    self.WagoAnalytics = LibStub("WagoAnalytics"):Register(GetAddOnMetadata(L["ADDON_NAME"], "X-Wago-ID"))
    self:Print("Added")
end

--- Logs changed data, if possible and allowed, using the specified parameters
---
--- @param fieldName string The config name the change belongs to
--- @param oldValue any The old value of the config variable
--- @param value any The new value of the config variable
function CraftPresence:LogChangedValue(fieldName, oldValue, value)
    if not self:CanUseAnalytics() then return end
    if type(value) == "boolean" then
        if self.WagoAnalytics then self.WagoAnalytics:Switch(fieldName, value) end
    end
    if type(value) == "number" then
        if self.WagoAnalytics then self.WagoAnalytics:SetCounter(fieldName, value) end
    end
end