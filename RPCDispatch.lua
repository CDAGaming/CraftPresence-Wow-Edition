--[[
MIT License

Copyright (c) 2018 - 2025 CDAGaming (cstack2011@yahoo.com)

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
local strformat, tostring, type, pairs = string.format, tostring, type, pairs
local tinsert, tconcat = table.insert, table.concat
local strbyte, strsub = string.byte, string.sub
local max, floor, abs, unpack, mod = math.max, math.floor, math.abs, unpack, (math.mod or math.fmod)
local CreateFrame, UIParent, GetScreenWidth, GetScreenHeight = CreateFrame, UIParent, GetScreenWidth, GetScreenHeight

-- Critical Data (DNT)
local frame_count = 0
local frames = {}
local last_encoded = ""
local last_args = {}
local render_warnings, last_render_warnings = "", ""
local render_settings = {
    ["contrast"] = {
        value = 50,
        minimumTOC = "80000", maximumTOC = "",
        allowRebasedApi = true,
        enabled = true
    },
    ["brightness"] = {
        value = 50,
        minimumTOC = "80000", maximumTOC = "",
        allowRebasedApi = true,
        enabled = true
    },
    ["gamma"] = {
        value = 1.0,
        minimumTOC = "", maximumTOC = "",
        allowRebasedApi = true,
        enabled = true
    }
}
local valid_anchors = {
    "TOPLEFT", "TOPRIGHT",
    "BOTTOMLEFT", "BOTTOMRIGHT",
    "TOP", "BOTTOM", "LEFT", "RIGHT"
}

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
    local output = self:Replace(
        (type(obj) == "string" and obj) or alt,
        self.internals.rpc.eventSeperator, self.internals.rpc.chatSeperator
    )
    if self:GetProperty("verboseMode") and not self:IsNullOrEmpty(obj) then
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

--- Determine if there are any conflicting render settings for RPC Event Generation
--- @return boolean @ are_settings_valid
function CraftPresence:AssertRenderSettings()
    -- Clear Prior Data
    last_render_warnings = render_warnings
    render_warnings = ""

    local error_info = {}
    for key, data in pairs(render_settings) do
        if type(data) == "table" then
            local is_correct = false

            if self:ShouldProcessData(data) then
                if type(data.value) == "table" then
                    for _, innerValue in pairs(data.value) do
                        if not is_correct then
                            is_correct = self:GetGameVariable(key, type(innerValue), innerValue) == innerValue
                        end
                    end
                else
                    is_correct = self:GetGameVariable(key, type(data.value), data.value) == data.value
                end

                if not is_correct then
                    tinsert(error_info, strformat(self.locale["FORMAT_SETTING"], key, data.value))
                end
            end
        elseif self:GetGameVariable(key, type(data), data) ~= data then
            tinsert(error_info, strformat(self.locale["FORMAT_SETTING"], key, data))
        end
    end

    if self:GetLength(error_info) > 0 then
        render_warnings = self:SerializeTable(error_info)
        if self:GetProperty("verboseMode") or last_render_warnings ~= render_warnings then
            self:PrintWarningMessage(self.locale["WARNING_EVENT_RENDERING_ONE"])
            self:PrintWarningMessage(strformat(self.locale["WARNING_EVENT_RENDERING_TWO"], render_warnings))
        end
    end
    return self:IsNullOrEmpty(render_warnings)
end

--- Helper function for getting valid_anchors
--- @return table @ valid_anchors
function CraftPresence:GetValidAnchors()
    return valid_anchors
end

--- Helper function for getting the scaled screen width
--- @return number @ screen_width
function CraftPresence:GetScaledWidth()
    return floor((GetScreenWidth() * UIParent:GetEffectiveScale()) + 0.5)
end

--- Helper function for getting the scaled screen height
--- @return number @ screen_height
function CraftPresence:GetScaledHeight()
    return floor((GetScreenHeight() * UIParent:GetEffectiveScale()) + 0.5)
end

--- Creates an array of frames with the specified size at the specified anchor of screen
---
--- @param width number The width of the frames (Required)
--- @param height number The height of the frames (Required)
--- @param anchor string The relative anchor point for the frame (Default: 'TOPLEFT')
--- @param is_vertical boolean Whether frames should generate in a vertical orientation (Default: false)
--- @param start_x number The starting x-axis position to begin rendering frames (Default: 0)
--- @param start_y number The starting y-axis position to begin rendering frames (Default: 0)
---
--- @return table @ frames
function CraftPresence:CreateFrames(width, height, anchor, is_vertical, start_x, start_y)
    if not width then return end
    if not height then return end
    anchor = self:GetOrDefault(anchor, "TOPLEFT")
    is_vertical = self:GetOrDefault(is_vertical, false)
    start_x = self:GetOrDefault(start_x, 0)
    start_y = self:GetOrDefault(start_y, 0)
    frames = {}
    frame_count = 0
    -- Set Baseline Positioning based on anchor
    -- TOP -> y=0
    --   TOPLEFT -> x=0
    --   TOPRIGHT -> x=GetScaledWidth()-frameWidth
    --   CENTER -> x=GetScaledWidth()/2
    -- BOTTOM -> y=GetScaledHeight()-frameHeight
    --   BOTTOMLEFT -> x=0
    --   BOTTOMRIGHT -> x=GetScaledWidth()-frameWidth
    --   CENTER -> x=GetScaledWidth()/2
    -- LEFT -> x=0, y=GetScaledHeight()/2
    -- RIGHT -> x=GetScaledWidth()-frameWidth, y=GetScaledHeight()/2
    -- CENTER -> x=GetScaledWidth()/2, y=GetScaledHeight()/2
    local base_width, base_height = self:GetScaledWidth(), self:GetScaledHeight()
    local base_x, base_y = 0,0
    if self:EndsWith(anchor, "RIGHT") then
        base_x = base_width - width
    end
    if self:StartsWith(anchor, "BOTTOM") then
        base_y = base_height - height
    end
    if anchor == "TOP" or anchor == "BOTTOM" or anchor == "CENTER" then
        base_x = base_width / 2
    end
    if anchor == "LEFT" or anchor == "RIGHT" or anchor == "CENTER" then
        base_y = base_height / 2
    end
    local real_x = base_x + start_x
    local real_y = base_y + start_y
    -- Calculate Maximum Frames based on *real* position data
    if is_vertical then
        frame_count = (base_height - real_y) / height
    else
        frame_count = (base_width - real_x) / width
    end
    frame_count = floor(frame_count)
    if self:GetProperty("debugMode") then
        self:Print(strformat(self.locale["LOG_DEBUG"],
            strformat(self.locale["DEBUG_MAX_BYTES"], tostring(frame_count * 3))
        ))
    end

    for i = 1, frame_count do
        frames[i] = CreateFrame("Frame", nil, UIParent)
        -- Make it independent of UI Scale
        if abs(frames[i]:GetEffectiveScale() - 1) > 0.01 then -- Frame is not full size
            frames[i]:SetScale(1 / frames[i]:GetParent():GetEffectiveScale()) -- Rescale the frame to be full size
        end
        -- Alternation of frame strata helps to reduce amount of blur between frames
        if mod(i, 2) == 0 then
            frames[i]:SetFrameStrata("TOOLTIP")
        else
            frames[i]:SetFrameStrata("FULLSCREEN_DIALOG")
        end
        frames[i]:SetWidth(width)
        frames[i]:SetHeight(height)

        -- Initialise the frame as null data (RBGA all 0s)
        self:PaintFrame(frames[i], 0, 0, 0, 0)

        -- Set Frame Position (x,y)
        local pos_x, pos_y = start_x, start_y
        if not is_vertical then
            pos_x = pos_x + ((i - 1) * width)
        else
            pos_y = -(pos_y + ((i - 1) * height))
        end

        frames[i]:SetPoint(anchor, pos_x, pos_y)
        frames[i]:Show()
    end
    return frames
end

--- Colors and Paints the specified frame to the specified RGB color
--- (INTERNAL USAGE ONLY -- Use PaintMessageWait)
---
--- @param frame Frame The specified frame to adjust (Required)
--- @param r number The red color value to apply to frame
--- @param g number The green color value to apply to frame
--- @param b number The blue color value to apply to frame
--- @param force number Force index (Sets alpha to 1 if rgb ~= 0 and force ~= 0 and force ~= nil))
function CraftPresence:PaintFrame(frame, r, g, b, force)
    if frame == nil then return end
    if not frame.texture and frame.CreateTexture then
        frame.texture = frame:CreateTexture(nil, "OVERLAY")
    end
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

--- Converts an array of text into bytes represented in RGB for frame rendering
--- (INTERNAL USAGE ONLY -- Use PaintMessageWait)
---
--- @param text string The text to be interpreted and converted (Required)
function CraftPresence:PaintSomething(text)
    if self:IsNullOrEmpty(text) then return end

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
    end
    self:PaintFrame(frames[squares_painted], 255, 255, 255, 1)

    local current_bytes = (squares_painted * 3)
    local max_bytes = (frame_count * 3)
    if current_bytes > max_bytes then
        self:PrintErrorMessage(strformat(self.locale["ERROR_MESSAGE_OVERFLOW"],
            tostring(current_bytes), tostring(max_bytes)
        ))
        self:CleanFrames()
    end
end

--- Concatenates the specified arguments with the specified parameters
---
--- @param appendTag string The string to append to the front and back of the result string
--- @param splitTag string The string to split each element in args by (Default: ',')
--- @param args any The arguments to interpret
---
--- @return string, table @ resultString, args
CraftPresence.ConcatTable = CraftPresence:vararg(3, function(self, appendTag, splitTag, args)
    appendTag = self:GetOrDefault(appendTag)
    splitTag = self:GetOrDefault(splitTag, ",")
    for key, value in pairs(args) do
        args[key] = self:TrimString(self:GetCaseData(value))
    end
    local combinedString, resultString = tconcat(args, splitTag), ""
    if combinedString ~= splitTag then
        resultString = (appendTag .. combinedString .. appendTag)
    end
    return resultString, args
end)

--- Sets all allocated frames to null for future allocation
function CraftPresence:CleanFrames()
    for i = 1, frame_count do
        self:PaintFrame(frames[i], 0, 0, 0, 0)
    end
    self.canUseExternals = true
end

--- Creates and encodes a new RPC event from placeholder and conditional data
---
--- @param log_output boolean Whether to allow logging for this function (Default: false)
---
--- @return string, table @ newEncodedString, args
function CraftPresence:EncodeConfigData(log_output)
    log_output = self:GetOrDefault(log_output, false)

    -- Placeholder Syncing
    local rpcData = self:SyncDynamicData(log_output, true)

    -- Update Instance Status before exiting method
    if self:HasInstanceChanged() then
        self:SetInstanceChanged(false)
    end
    return self:ConcatTable(self.internals.rpc.eventTag, self.internals.rpc.eventSeperator, unpack(rpcData))
end

--- Displays the currently encoded string as Frames, depending on arguments
---
--- @param force boolean Whether or not to force an update regardless of changes (Default: false)
--- @param update boolean Whether to update last_encoded if proceeding (Default: true)
--- @param clean boolean Whether or not to clear frames after a period of time (Default: true)
--- @param msg string Exact message to parse; Defaults to EncodeConfigData if non-present
function CraftPresence:PaintMessageWait(force, update, clean, msg)
    force = self:GetOrDefault(force, false)
    update = self:GetOrDefault(update, true)
    clean = self:GetOrDefault(clean, true)

    local defaultEncoded, encodedArgs = self:EncodeConfigData(self:GetProperty("verboseMode"))
    local encoded = self:GetOrDefault(msg, defaultEncoded)
    local changed = (last_encoded ~= encoded and self:AssertRenderSettings()) or force
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
        self:GetEncodedMessage(
            (useTable and encodedArgs), encoded,
            self.locale["DEBUG_SEND_ACTIVITY"], self.locale["LOG_DEBUG"],
            self:GetProperty("debugMode")
        )
        self:PaintSomething(encoded)
        if clean then
            local delay = self:GetProperty("frameClearDelay")
            if (self:IsWithinValue(
                delay,
                max(self.locale["MINIMUM_FRAME_CLEAR_DELAY"], 1), self.locale["MAXIMUM_FRAME_CLEAR_DELAY"],
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
        -- If encoded data has not changed, we will release the timer immediately
        -- if we are running within a non-queued pipeline
        self:SetTimerLocked(false)
    end
end
