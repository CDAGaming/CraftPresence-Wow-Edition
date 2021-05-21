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
local strformat, tostring, type = string.format, tostring, type
local strbyte, strsub, pairs = string.byte, string.sub, pairs
local max, floor = math.max, math.floor
local CreateFrame, UIParent, GetScreenWidth = CreateFrame, UIParent, GetScreenWidth

-- Addon APIs
local L = CraftPresence.locale

-- Critical Data (DNT)
local frame_count = 0
local frames = {}
local last_encoded = ""
local last_args = {}

--- Convert an encoded RPCEvent message into a displayable format
---
--- @param obj table The encoded object to evaluate
--- @param alt string The alternate form of obj, if needed
--- @param format string The format to log the string as (Default: %s)
--- @param level string The log level to log the string as (Default: %s)
--- @param display boolean Whether or not to print the return value (Default: false)
---
--- @return string @ encoded_message
function CraftPresence:GetEncodedMessage(obj, alt, format, level, display)
    local defaultData = last_encoded
    alt = self:GetOrDefault(alt, defaultData)
    if type(obj) == "table" then
        defaultData = last_args
    end
    obj = self:GetOrDefault(obj, defaultData)
    format = self:GetOrDefault(format, "%s")
    level = self:GetOrDefault(level, "%s")
    display = self:GetOrDefault(display, false)
    local output = self:Replace((type(obj) == "string" and obj) or alt, L["ARRAY_SEPARATOR_KEY"], L["ARRAY_SEPARATOR_KEY_ALT"])
    if self:GetFromDb("verboseMode") and not self:IsNullOrEmpty(obj) then
        output = self:SerializeTable(obj)
    end
    local returnValue = strformat(level, strformat(format, output))
    if display then
        self:Print(returnValue)
    end
    return returnValue
end

--- Helper function for getting last_args and last_encoded
--- @return table, string @ last_args, last_encoded
function CraftPresence:GetCachedEncodedData()
    return last_args, last_encoded
end

--- Creates an array of frames with the specified size at the TOPLEFT of screen
---
--- @param size number The width and height of the frames
---
--- @return table @ frames
function CraftPresence:CreateFrames(size)
    frame_count = floor(GetScreenWidth() / size)
    if self:GetFromDb("debugMode") then
        self:Print(strformat(L["LOG_DEBUG"], strformat(L["DEBUG_MAX_BYTES"], tostring((frame_count * 3) - 1))))
    end

    for i = 1, frame_count do
        frames[i] = CreateFrame("Frame", nil, UIParent)
        frames[i]:SetFrameStrata("TOOLTIP")
        frames[i]:SetWidth(size)
        frames[i]:SetHeight(size)

        -- initialise pixels as null data (RBGA all 0'd)
        local t = frames[i]:CreateTexture(nil, "TOOLTIP")
        if t.SetColorTexture then
            t:SetColorTexture(0, 0, 0, 0)
        else
            t:SetTexture(0, 0, 0, 0)
        end
        t:SetAllPoints(frames[i])
        frames[i].texture = t

        frames[i]:SetPoint("TOPLEFT", (i - 1) * size, 0)
        frames[i]:Show()
    end
    return frames
end

--- Colors and Paints the specified frame to the specified RGB color
---
--- @param frame Frame The specified frame to adjust
--- @param r number The red color value to apply to frame
--- @param g number The green color value to apply to frame
--- @param b number The blue color value to apply to frame
--- @param force number Force index (Sets alpha to 1 if rgb ~= 0 and force ~= 0 and force ~= nil))
function CraftPresence:PaintFrame(frame, r, g, b, force)
    -- set pixel to black if they are null
    if r == nil then
        r = 0
    end
    if g == nil then
        g = 0
    end
    if b == nil then
        b = 0
    end

    -- from 0-255 to 0.0-1.0
    r = r / 255
    g = g / 255
    b = b / 255

    -- set alpha to 1 if this pixel is black and force is 0 or null
    local a
    if r == 0 and g == 0 and b == 0 and (force == 0 or force == nil) then
        a = 0
    else
        a = 1
    end

    -- and now paint it
    if frame.texture.SetColorTexture then
        frame.texture:SetColorTexture(r, g, b, a)
    else
        frame.texture:SetTexture(r, g, b, a)
    end
    frame.texture:SetAllPoints(frame)
end

--- Converts an array of text into bytes represented in RGB for Frame rendering
--- @param text string The text to be interpreted and converted
function CraftPresence:PaintSomething(text)
    local max_bytes = (frame_count - 1) * 3
    if self:GetLength(text) >= max_bytes then
        if self:GetFromDb("debugMode") then
            self:Print(strformat(
                    L["LOG_ERROR"], strformat(
                            L["ERROR_BYTE_INSUFFICIENT"], tostring(self:GetLength(text)), tostring(max_bytes)
                    )
            ))
        end
        return
    end

    -- clean all frames before repainting
    self:CleanFrames()

    local squares_painted = 0
    local r
    local g
    local b

    -- Locate and parse matches for pattern
    -- (Pattern will split string into pairs of up to 3 characters)

    -- Convert each matching pair into an RGB value
    for trio in self:FindMatches(text, ".?.?.?") do
        r = strbyte(strsub(trio, 1, 1))
        if self:GetLength(trio) > 1 then
            g = strbyte(strsub(trio, 2, 2))
        else
            g = 0
        end
        if self:GetLength(trio) > 2 then
            b = strbyte(strsub(trio, 3, 3))
        else
            b = 0
        end
        squares_painted = squares_painted + 1
        self:PaintFrame(frames[squares_painted], r, g, b)
        -- print the next frame black to signal a separator
        -- if the pixel before it is allocated
        if r == nil then
            r = 0
        end
        if g == nil then
            g = 0
        end
        if b == nil then
            b = 0
        end
        if not (r == 0 and b == 0 and g == 0) then
            squares_painted = squares_painted + 1
            self:PaintFrame(frames[squares_painted], 0, 0, 0, 1)
        end
    end
end

--- Encodes varying pieces of RPC Tags into a valid RPC_EVENT
---
--- @param length number The length the args must meet
--- @param args table The arguments to interpret
---
--- @return string, table @ eventString, args
function CraftPresence:EncodeData(length, args)
    local eventString, separator = L["EVENT_RPC_TAG"]
    for key = 1, length do
        if self:IsWithinValue(key, 1, length, true) then
            separator = L["ARRAY_SEPARATOR_KEY"]
        else
            separator = ""
        end
        args[key] = self:TrimString(self:GetCaseData(args[key]))
        eventString = eventString .. args[key] .. separator
    end
    eventString = eventString .. L["EVENT_RPC_TAG"]
    return eventString, args
end

--- Sets all allocated frames to null for future allocation
function CraftPresence:CleanFrames()
    for i = 1, frame_count do
        self:PaintFrame(frames[i], 0, 0, 0, 0)
    end
end

--- Displays the currently encoded string as Frames, depending on arguments
---
--- @param force boolean Whether or not to force an update regardless of changes (Default: false)
--- @param update boolean Whether to update last_encoded if proceeding (Default: true)
--- @param clean boolean Whether or not to clear frames after a period of time (Default: true)
--- @param msg string Exact message to parse; Defaults to EncodeConfigData if non-present
--- @param instance_update boolean Whether to force an instance change (Default: false)
function CraftPresence:PaintMessageWait(force, update, clean, msg, instance_update)
    force = self:GetOrDefault(force, false)
    update = self:GetOrDefault(update, true)
    clean = self:GetOrDefault(clean, true)
    instance_update = self:GetOrDefault(instance_update, false)

    local defaultEncoded, encodedArgs = self:EncodeConfigData(instance_update)
    local encoded = self:GetOrDefault(msg, defaultEncoded)
    local changed = last_encoded ~= encoded or force
    local useTable = self:IsNullOrEmpty(msg)
    if (changed and not self:IsNullOrEmpty(encoded)) then
        if update then
            last_encoded = encoded
            if useTable then
                last_args = encodedArgs
            else
                last_args = {}
            end
        end
        self:GetEncodedMessage((useTable and encodedArgs), encoded, L["DEBUG_SEND_ACTIVITY"], L["LOG_DEBUG"], self:GetFromDb("debugMode"))
        self:PaintSomething(encoded)
        if clean then
            local delay = self:GetFromDb("frameClearDelay")
            if (self:IsWithinValue(
                    delay,
                    max(L["MINIMUM_FRAME_CLEAR_DELAY"], 1), L["MAXIMUM_FRAME_CLEAR_DELAY"],
                    true, true
            )) then
                self:After(delay, function()
                    self:CleanFrames()
                    self:SetTimerLocked(false)
                end)
            else
                self:CleanFrames()
                self:SetTimerLocked(false)
            end
        end
    else
        -- If encoded data has not changed, we will release the timer immediatly
        -- if we are running within a non-queued pipeline
        self:SetTimerLocked(false)
    end
end
