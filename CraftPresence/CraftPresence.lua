CraftPresence = LibStub("AceAddon-3.0"):NewAddon("CraftPresence", "AceConsole-3.0", "AceEvent-3.0")

local L = LibStub("AceLocale-3.0"):GetLocale("CraftPresence")

local options = {
    name = "CraftPresence",
    handler = CraftPresence,
    type = 'group',
    args = {
		clientId = {
            type = "input",
            name = L["TITLE_CLIENT_ID"],
            desc = L["COMMENT_CLIENT_ID"],
            usage = L["USAGE_CLIENT_ID"],
            get = "GetClientId",
            set = "SetClientId",
		},
		gameStateMessage = {
            type = "input",
            name = L["TITLE_GAME_STATE_MESSAGE"],
            desc = L["COMMENT_GAME_STATE_MESSAGE"],
            usage = L["USAGE_GAME_STATE_MESSAGE"],
            get = "GetGameStateMessage",
            set = "SetGameStateMessage",
        },
		detailsMessage = {
            type = "input",
            name = L["TITLE_DETAILS_MESSAGE"],
            desc = L["COMMENT_DETAILS_MESSAGE"],
            usage = L["USAGE_DETAILS_MESSAGE"],
            get = "GetDetailsMessage",
            set = "SetDetailsMessage",
        },
        showLoggingInChat = {
            type = "toggle",
            name = L["TITLE_SHOW_LOGGING_IN_CHAT"],
            desc = L["COMMENT_SHOW_LOGGING_IN_CHAT"],
            get = "IsShowLoggingInChat",
            set = "ToggleShowLoggingInChat",
        },
        debugMode = {
            type = "toggle",
            name = L["TITLE_DEBUG_MODE"],
            desc = L["COMMENT_DEBUG_MODE"],
            get = "IsDebugMode",
            set = "ToggleDebugMode"
        },
    },
}

local defaults = {
    profile = {
		clientId = L["DEFAULT_CLIENT_ID"],
		gameStateMessage = L["DEFAULT_GAME_STATE_MESSAGE"],
		detailsMessage = L["DEFAULT_DETAILS_MESSAGE"],
        showLoggingInChat = true,
        debugMode = true,
    },
}

-- ==================
-- Utilities
-- ==================

function CraftPresence:ContainsDigit(str)
	return string.find(str, "%d") and true or false
end


-- ==================
-- RPC Data
-- ==================

local frame_count = 0
local frames = {}
local realmData = {"US", "KR", "EU", "TW", "CH"}
local last_encoded = ""

function CraftPresence:CreateFrames()
	local size = 12
	frame_count = math.floor(GetScreenWidth() / size)
	if self:IsDebugMode() and self:IsShowLoggingInChat() then
		self:Print("Debug => Max bytes that can be stored: " .. (frame_count * 3) - 1)
	end

	for i=1, frame_count do
		frames[i] = CreateFrame("Frame", nil, UIParent)
		frames[i]:SetFrameStrata("TOOLTIP")
		frames[i]:SetWidth(size)
		frames[i]:SetHeight(size)

		-- initialise it as black
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
	-- turn them into black if they are null
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
		if self:IsDebugMode() and self:IsShowLoggingInChat() then
			self:Print("Error => You're painting too many bytes (" .. #text .. " vs " .. max_bytes .. ")")
		end
		return
	end

	-- clean all
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
	end

	-- and then paint the last one black
	self:PaintFrame(frames[squares_painted], 0, 0, 0, 1)
end

function CraftPresence:EncodeZoneType()
	local name, instanceType, difficultyID, difficultyName, maxPlayers,
		dynamicDifficulty, isDynamic, instanceID, instanceGroupSize, LfgDungeonID = GetInstanceInfo()
	local firstLine = nil
	local secondLine = nil

	local startTime = nil
	local endTime = nil

	local playerName = UnitName("player")
	local playerRealm = GetRealmName()
	local playerRegion = realmData[GetCurrentRegion()]
	local playerClass = UnitClass("player")
	local zone_name = GetRealZoneText()

	if(zone_name == nil) then zone_name = "Not available" end
	local sub_name = GetSubZoneText()

	if instanceType == 'party' then
		firstLine = zone_name
		secondLine = string.format("In %s Dungeon", difficultyName)
		startTime = "generated"
	elseif instanceType == 'raid' then
		firstLine = zone_name
		secondLine = string.format("In %s Raid", difficultyName)
		startTime = "generated"
	elseif instanceType == 'pvp' then
		firstLine = zone_name
		secondLine = "In Battleground"
		startTime = "generated"
	else
		if UnitIsDeadOrGhost("player") and not UnitIsDead("player") then
			firstLine = sub_name
			secondLine = "Corpse Running"
		else
			firstLine = sub_name
			secondLine = zone_name
		end
	end

	local playerInfo = playerName .. " - " .. playerClass
	local realmInfo = playerRegion .. " - " .. playerRealm
	if firstLine == "" or firstLine == nil or secondLine == "" or secondLine == nil then return nil end
	local lineInfo = firstLine .. " - " .. secondLine
	return self:EncodeData(self:GetClientId(), "wow_icon", realmInfo, nil, nil, playerInfo, lineInfo, startTime, endTime)
end

function CraftPresence:EncodeData(clientId, largeImageKey, largeImageText, smallImageKey, smallImageText, details, gameState, startTime, endTime)
	if clientId ~= nil then clientId = L["DEFAULT_CLIENT_ID"] end
	local nullKey = "None"
	local endKey = "$RPCEvent$" .. (clientId) .. "|" .. (largeImageKey or nullKey) .. "|" .. (largeImageText or nullKey) .. "|" .. (smallImageKey or nullKey) .. "|" .. (smallImageText or nullKey) .. "|" .. (details or nullKey) .. "|" .. (gameState or nullKey) .. "|" .. (startTime or nullKey) .. "|" .. (endTime or nullKey) .. "$RPCEvent$"
	if self:IsDebugMode() and self:IsShowLoggingInChat() then
		self:Print("[Debug] Sending activity => " .. endKey)
	end
	return endKey
end

function CraftPresence:CleanFrames()
	for i=1, frame_count do
		self:PaintFrame(frames[i], 0, 0, 0, 0)
	end
end

function CraftPresence:TestFrames()
	local encoded = self:EncodeZoneType()
	if encoded ~= nil then self:PaintSomething(encoded) end
end

function CraftPresence:PaintMessageWait(force)
	local proceed = force ~= nil and force == true;
	local encoded = self:EncodeZoneType()
	local changed = last_encoded ~= encoded or proceed
	if(changed and encoded ~= nil) then
		last_encoded = encoded
		self:PaintSomething(encoded)
		C_Timer.After(10, function() self:CleanFrames() end)
	end
end

function CraftPresence:OnInitialize()
	-- Called when the addon is loaded
	self.db = LibStub("AceDB-3.0"):New("CraftPresenceDB", defaults, true)
	LibStub("AceConfig-3.0"):RegisterOptionsTable("CraftPresence", options)
	self.optionsFrame = LibStub("AceConfigDialog-3.0"):AddToBlizOptions("CraftPresence", "CraftPresence")
	-- Command Registration
	self:RegisterChatCommand("craftpresence", "ChatCommand")
	self:RegisterChatCommand("cp", "ChatCommand")
end

function CraftPresence:OnEnable()
	-- Called when the addon is enabled
	self:Print("Discord Rich Presence Loaded")
	self:RegisterEvent("PLAYER_LOGIN", "DispatchUpdate")
	self:RegisterEvent("ZONE_CHANGED", "DispatchUpdate")
	self:RegisterEvent("ZONE_CHANGED_NEW_AREA", "DispatchUpdate")
	self:RegisterEvent("ZONE_CHANGED_INDOORS", "DispatchUpdate")
	self:RegisterEvent("PLAYER_UNGHOST", "DispatchUpdate")

	self:CreateFrames()
	self:PaintMessageWait()
end

function CraftPresence:DispatchUpdate()
	self:PaintMessageWait()
end

function CraftPresence:OnDisable()
	-- Called when the addon is disabled
end

function CraftPresence:ChatCommand(input)
    if not input or input:trim() == "" then
        InterfaceOptionsFrame_OpenToCategory(self.optionsFrame)
	else
		if input == "test" then
			self:TestFrames()
		elseif input == "clean" or input == "clear" then
			self:CleanFrames()
		elseif input:trim() == "update" then
			self:PaintMessageWait(true)
		else
			LibStub("AceConfigCmd-3.0"):HandleCommand("cp", "CraftPresence", input)
		end
    end
end
-- ====================================================
-- Getters and Setters (Config Data)
-- =====================================================

function CraftPresence:GetClientId(info)
    return self.db.profile.clientId
end

function CraftPresence:SetClientId(info, newValue)
	if newValue ~= nil and self:ContainsDigit(newValue) and string.len(newValue) == 16 then
		self.db.profile.clientId = newValue
	else
		self:Print(L["ERROR_CLIENT_ID"])
	end
end

function CraftPresence:GetGameStateMessage(info)
    return self.db.profile.gameStateMessage
end

function CraftPresence:SetGameStateMessage(info, newValue)
    self.db.profile.gameStateMessage = newValue
end

function CraftPresence:GetDetailsMessage(info)
    return self.db.profile.detailsMessage
end

function CraftPresence:SetDetailsMessage(info, newValue)
    self.db.profile.detailsMessage = newValue
end

function CraftPresence:IsShowLoggingInChat(info)
    return self.db.profile.showLoggingInChat
end

function CraftPresence:ToggleShowLoggingInChat(info, value)
    self.db.profile.showLoggingInChat = value
end

function CraftPresence:IsDebugMode(info)
    return self.db.profile.debugMode
end

function CraftPresence:ToggleDebugMode(info, value)
    self.db.profile.debugMode = value
end
