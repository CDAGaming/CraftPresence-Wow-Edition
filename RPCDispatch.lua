local CraftPresence = LibStub("AceAddon-3.0"):GetAddon("CraftPresence")

local L = LibStub("AceLocale-3.0"):GetLocale("CraftPresence")

local frame_count = 0
local frames = {}
local last_encoded = ""

function CraftPresence:PrintLastEncoded()
    return string.format(L["VERBOSE_LOG"], string.format(L["VERBOSE_LAST_ENCODED"], last_encoded:gsub("|", "||")))
end

function CraftPresence:CreateFrames()
    local size = 6
    frame_count = math.floor(GetScreenWidth() / size)
    if self:GetFromDb("debugMode") and self:GetFromDb("showLoggingInChat") then
        self:Print(string.format(L["DEBUG_LOG"], string.format(L["DEBUG_MAX_BYTES"], tostring((frame_count * 3) - 1))))
    end

    for i=1, frame_count do
        frames[i] = CreateFrame("Frame", nil, UIParent)
        frames[i]:SetFrameStrata("TOOLTIP")
        frames[i]:SetWidth(size)
        frames[i]:SetHeight(size)

        -- initialise pixels as black to represent initial null data
        local t = frames[i]:CreateTexture(nil, "TOOLTIP")
        t:SetColorTexture(0, 0, 0, 1)
        t:SetAllPoints(frames[i])
        frames[i].texture = t

        frames[i]:SetPoint("TOPLEFT", (i - 1) * size, 0)
        frames[i]:Show()
    end
    return frames
end

function CraftPresence:PaintFrame(frame, r, g, b, force)
    -- set pixel to black if they are null
    if r == nil then r = 0 end
    if g == nil then g = 0 end
    if b == nil then b = 0 end

    -- from 0-255 to 0.0-1.0
    r = r / 255
    g = g / 255
    b = b / 255

    -- set alpha to 1 if this pixel is black and force is 0 or null
    local a = 0
    if r == 0 and g == 0 and b == 0 and (force == 0 or force == nil) then a = 0 else a = 1 end

    -- and now paint it
    frame.texture:SetColorTexture(r, g, b, a)
    frame.texture:SetAllPoints(frame)
end

function CraftPresence:PaintSomething(text)
    local max_bytes = (frame_count - 1) * 3
    if text:len() >= max_bytes then
        if self:GetFromDb("debugMode") and self:GetFromDb("showLoggingInChat") then
            self:Print(string.format(L["ERROR_LOG"], string.format(L["ERROR_BYTE_OVERFLOW"], tostring(#text), tostring(max_bytes))))
        end
        return
    end

    -- clean all frames before repainting
    self:CleanFrames()

    local squares_painted = 0
    local r = 0
    local g = 0
    local b = 0

    for trio in text:gmatch".?.?.?" do
        r = 0; g = 0; b = 0
        r = string.byte(trio:sub(1,1))
        if #trio > 1 then g = string.byte(trio:sub(2,2)) end
        if #trio > 2 then b = string.byte(trio:sub(3,3)) end
        squares_painted = squares_painted + 1
        self:PaintFrame(frames[squares_painted], r, g, b)
        -- print the next frame black to signal a separator
        -- if the pixel before it is allocated
        if r == nil then r = 0 end
        if g == nil then g = 0 end
        if b == nil then b = 0 end
        if not(r == 0 and b == 0 and g == 0) then
            squares_painted = squares_painted + 1
            self:PaintFrame(frames[squares_painted], 0, 0, 0, 1)
        end
    end
end

function CraftPresence:EncodeData(clientId, largeImageKey, largeImageText, smallImageKey, smallImageText, details, gameState, startTime, endTime)
    if(clientId == nil or clientId == "") then clientId = L["DEFAULT_CLIENT_ID"] end
    if largeImageKey == "" then largeImageKey = nil end
    if largeImageText == "" then largeImageText = nil end
    if smallImageKey == "" then smallImageKey = nil end
    if smallImageText == "" then smallImageText = nil end
    if details == "" then details = nil end
    if gameState == "" then gameState = nil end
    return ("$RPCEvent$" .. (clientId) .. "|" .. (largeImageKey or L["UNKNOWN_KEY"]) .. "|" .. (largeImageText or L["UNKNOWN_KEY"]) .. "|" .. (smallImageKey or L["UNKNOWN_KEY"]) .. "|" .. (smallImageText or L["UNKNOWN_KEY"]) .. "|" .. (details or L["UNKNOWN_KEY"]) .. "|" .. (gameState or L["UNKNOWN_KEY"]) .. "|" .. (startTime or L["UNKNOWN_KEY"]) .. "|" .. (endTime or L["UNKNOWN_KEY"]) .. "$RPCEvent$")
end

function CraftPresence:CleanFrames()
    for i=1, frame_count do
        self:PaintFrame(frames[i], 0, 0, 0, 0)
    end
end

function CraftPresence:TestFrames()
    local encoded = self:EncodeConfigData()
    if encoded ~= nil then self:PaintSomething(encoded) end
end

function CraftPresence:PaintMessageWait(force)
    local proceed = (force ~= nil and force == true)
    local encoded = self:EncodeConfigData()
    local changed = last_encoded ~= encoded or proceed
    if(changed and encoded ~= nil) then
        last_encoded = encoded
        if self:GetFromDb("debugMode") and self:GetFromDb("showLoggingInChat") then
            self:Print(string.format(L["DEBUG_LOG"], string.format(L["DEBUG_SEND_ACTIVITY"], encoded:gsub("|", "||"))))
        end
        self:PaintSomething(encoded)
        C_Timer.After(10, function() self:CleanFrames() end)
    end
end