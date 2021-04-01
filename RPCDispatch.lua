local CraftPresence = LibStub("AceAddon-3.0"):GetAddon("CraftPresence")

local L = LibStub("AceLocale-3.0"):GetLocale("CraftPresence")

local frame_count = 0
local frames = {}
local last_encoded = ""

--- Retrieves the Last Sent Encoded Event Message
--- @return string @ lastEncodedMessage
function CraftPresence:GetLastEncoded()
    return string.format(L["VERBOSE_LOG"], string.format(L["VERBOSE_LAST_ENCODED"], last_encoded:gsub("|", "||")))
end

--- Creates an array of frames with the specified size at the TOPLEFT of screen
---
--- @param size number The width and height of the frames
---
--- @return table @ frames
function CraftPresence:CreateFrames(size)
    frame_count = math.floor(GetScreenWidth() / size)
    if self:GetFromDb("debugMode") then
        self:Print(string.format(L["DEBUG_LOG"], string.format(L["DEBUG_MAX_BYTES"], tostring((frame_count * 3) - 1))))
    end

    for i = 1, frame_count do
        frames[i] = CreateFrame("Frame", nil, UIParent)
        frames[i]:SetFrameStrata("TOOLTIP")
        frames[i]:SetWidth(size)
        frames[i]:SetHeight(size)

        -- initialise pixels as black to represent initial null data
        local t = frames[i]:CreateTexture(nil, "TOOLTIP")
        if t.SetColorTexture then
            t:SetColorTexture(0, 0, 0, 1)
        else
            t:SetTexture(0, 0, 0, 1)
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
    if text:len() >= max_bytes then
        if self:GetFromDb("debugMode") then
            self:Print(string.format(
                    L["ERROR_LOG"], string.format(
                            L["ERROR_BYTE_OVERFLOW"], tostring(#text), tostring(max_bytes)
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

    for trio in text:gmatch ".?.?.?" do
        r = string.byte(trio:sub(1, 1))
        if #trio > 1 then
            g = string.byte(trio:sub(2, 2))
        else
            g = 0
        end
        if #trio > 2 then
            b = string.byte(trio:sub(3, 3))
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
--- @param clientId string The client id for Rich Presence
--- @param largeImageKey string The large image key for Rich Presence
--- @param largeImageText string The text ascociated with the largeImageKey, if present
--- @param smallImageKey string The small image key for Rich Presence
--- @param smallImageText string The text ascociated with the smallImageKey, if present
--- @param details string The details message for Rich Presence
--- @param gameState string The game state message for Rich Presence
--- @param startTime string The starting timestamp for Rich Presence
--- @param endTime string The ending timestamp for Rich Presence (Requires startTime to display)
--- @param primaryButton string The primary button to attach to the Rich Presence
--- @param secondaryButton string The secondary button to attach to the Rich Presence
function CraftPresence:EncodeData(clientId, largeImageKey, largeImageText, smallImageKey, smallImageText,
                                  details, gameState, startTime, endTime, primaryButton, secondaryButton)
    if clientId == nil or clientId == "" then
        clientId = L["DEFAULT_CLIENT_ID"]
    end
    if largeImageKey == nil or largeImageKey == "" then
        largeImageKey = L["UNKNOWN_KEY"]
    end
    if largeImageText == nil or largeImageText == "" then
        largeImageText = L["UNKNOWN_KEY"]
    end
    if smallImageKey == nil or smallImageKey == "" then
        smallImageKey = L["UNKNOWN_KEY"]
    end
    if smallImageText == nil or smallImageText == "" then
        smallImageText = L["UNKNOWN_KEY"]
    end
    if details == nil or details == "" then
        details = L["UNKNOWN_KEY"]
    end
    if gameState == nil or gameState == "" then
        gameState = L["UNKNOWN_KEY"]
    end
    if startTime == nil or startTime == "" then
        startTime = L["UNKNOWN_KEY"]
    end
    if endTime == nil or endTime == "" then
        endTime = L["UNKNOWN_KEY"]
    end
    -- Additional Sanity Checks for Primary Button
    if primaryButton == nil or primaryButton == "" then
        primaryButton = L["UNKNOWN_KEY"]
    else
        local button_data = self:Split(primaryButton, L["ARRAY_SPLIT_KEY"])
        primaryButton = ""
        for i = 1, #button_data do
            if button_data[i] == nil or button_data[i] == "" then
                button_data[i] = button_data[i]:gsub(button_data[i], L["UNKNOWN_KEY"])
            end
            primaryButton = primaryButton .. button_data[i]
            if i ~= #button_data then
                primaryButton = primaryButton .. L["ARRAY_SPLIT_KEY"]
            end
        end
    end
    -- Additional Sanity Checks for Secondary Button
    if secondaryButton == nil or secondaryButton == "" then
        secondaryButton = L["UNKNOWN_KEY"]
    else
        local button_data = self:Split(secondaryButton, L["ARRAY_SPLIT_KEY"])
        secondaryButton = ""
        for i = 1, #button_data do
            if button_data[i] == nil or button_data[i] == "" then
                button_data[i] = button_data[i]:gsub(button_data[i], L["UNKNOWN_KEY"])
            end
            secondaryButton = secondaryButton .. button_data[i]
            if i ~= #button_data then
                secondaryButton = secondaryButton .. L["ARRAY_SPLIT_KEY"]
            end
        end
    end
    return string.format(
            L["RPC_EVENT_FORMAT"],
            clientId, largeImageKey, largeImageText, smallImageKey, smallImageText,
            details, gameState, startTime, endTime, primaryButton, secondaryButton
    )
end

--- Sets all allocated frames to null for future allocation
function CraftPresence:CleanFrames()
    for i = 1, frame_count do
        self:PaintFrame(frames[i], 0, 0, 0, 0)
    end
end

--- Displays the currently encoded string as Frames, if changed or forced
---
--- @param force boolean Whether or not to force an update regardless of changes
--- @param update boolean Whether to update last_encoded if proceeding
--- @param clean boolean Whether or not to clear frames after a period of time
--- @param msg string Exact message to parse; Defaults to EncodeConfigData if non-present
function CraftPresence:PaintMessageWait(force, update, clean, msg, instance_update)
    local proceed = (force ~= nil and force == true) -- Default: False
    local will_update = (update == nil or update == true) -- Default: True
    local will_clean = (clean == nil or clean == true) -- Default: True
    local will_instance_update = (instance_update ~= nil and instance_update == true) -- Default: False
    local encoded
    if msg ~= nil then
        encoded = tostring(msg)
    else
        encoded = self:EncodeConfigData(will_instance_update)
    end
    local changed = last_encoded ~= encoded or proceed
    if (changed and encoded ~= nil) then
        if will_update then
            last_encoded = encoded
        end
        if self:GetFromDb("debugMode") then
            self:Print(string.format(L["DEBUG_LOG"], string.format(L["DEBUG_SEND_ACTIVITY"], encoded:gsub("|", "||"))))
        end
        self:PaintSomething(encoded)
        if will_clean then
            local delay = self:GetFromDb("frameClearDelay")
            if (self:IsWithinValue(
                    delay, math.max(L["MINIMUM_FRAME_CLEAR_DELAY"], 1), L["MAXIMUM_FRAME_CLEAR_DELAY"], true
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
