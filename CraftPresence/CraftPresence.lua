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
		largeImageMessage = {
            type = "input",
            name = L["TITLE_LARGE_IMAGE_MESSAGE"],
            desc = L["COMMENT_LARGE_IMAGE_MESSAGE"],
            usage = L["USAGE_LARGE_IMAGE_MESSAGE"],
            get = "GetLargeImageMessage",
            set = "SetLargeImageMessage",
		},
		dungeonPlaceholderMessage = {
			type = "input",
			name = L["TITLE_DUNGEON_MESSAGE"],
			desc = L["COMMENT_DUNGEON_MESSAGE"],
			usage = L["USAGE_DUNGEON_MESSAGE"],
			get = "GetDungeonPlaceholderMessage",
			set = "SetDungeonPlaceholderMessage",
		},
		raidPlaceholderMessage = {
			type = "input",
			name = L["TITLE_RAID_MESSAGE"],
			desc = L["COMMENT_RAID_MESSAGE"],
			usage = L["USAGE_RAID_MESSAGE"],
			get = "GetRaidPlaceholderMessage",
			set = "SetRaidPlaceholderMessage",
		},
		battlegroundPlaceholderMessage = {
			type = "input",
			name = L["TITLE_BATTLEGROUND_MESSAGE"],
			desc = L["COMMENT_BATTLEGROUND_MESSAGE"],
			usage = L["USAGE_BATTLEGROUND_MESSAGE"],
			get = "GetBattlegroundPlaceholderMessage",
			set = "SetBattlegroundPlaceholderMessage",
		},
		defaultPlaceholderMessage = {
			type = "input",
			name = L["TITLE_FALLBACK_MESSAGE"],
			desc = L["COMMENT_FALLBACK_MESSAGE"],
			usage = L["USAGE_FALLBACK_MESSAGE"],
			get = "GetDefaultPlaceholderMessage",
			set = "SetDefaultPlaceholderMessage",
		},
		deadStateInnerMessage = {
			type = "input",
			name = L["TITLE_DEAD_MESSAGE"],
			desc = L["COMMENT_DEAD_MESSAGE"],
			usage = L["USAGE_DEAD_MESSAGE"],
			get = "GetDeadInnerMessage",
			set = "SetDeadInnerMessage",
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
		verboseMode = {
            type = "toggle",
            name = L["TITLE_VERBOSE_MODE"],
            desc = L["COMMENT_VERBOSE_MODE"],
            get = "IsVerboseMode",
            set = "ToggleVerboseMode"
        },
    },
}

local defaults = {
    profile = {
		clientId = L["DEFAULT_CLIENT_ID"],
		gameStateMessage = L["DEFAULT_GAME_STATE_MESSAGE"],
		detailsMessage = L["DEFAULT_DETAILS_MESSAGE"],
		largeImageMessage = L["DEFAULT_LARGE_IMAGE_MESSAGE"],
		dungeonPlaceholderMessage = L["DEFAULT_DUNGEON_MESSAGE"],
		raidPlaceholderMessage = L["DEFAULT_RAID_MESSAGE"],
		battlegroundPlaceholderMessage = L["DEFAULT_BATTLEGROUND_MESSAGE"],
		defaultPlaceholderMessage = L["DEFAULT_FALLBACK_MESSAGE"],
		deadStateInnerMessage = L["DEFAULT_DEAD_MESSAGE"],
        showLoggingInChat = true,
		debugMode = true,
		verboseMode = true,
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
local nullKey = "Skip"

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

function CraftPresence:ParsePlaceholderData(global_placeholders)
	local name, instanceType, difficultyID, difficultyName, maxPlayers,
		dynamicDifficulty, isDynamic, instanceID, instanceGroupSize, LfgDungeonID = GetInstanceInfo()
	local playerName = UnitName("player")
	local playerRealm = GetRealmName()
	local playerRegion = realmData[GetCurrentRegion()]
	local playerClass = UnitClass("player")
	local zone_name = GetRealZoneText()
	-- Zone Data
	if(zone_name == nil) then zone_name = "Not available" end
	local sub_name = GetSubZoneText()
	-- Calculate Inner Placeholders
	local inner_placeholders = {
		["@player_info@"] = (playerName .. " - " .. playerClass),
		["@realm_info@"] = (playerRegion .. " - " .. playerRealm),
		["@zone_name@"] = zone_name,
		["@sub_zone_name@"] = sub_name,
		["@dead_state@"] = self:GetDeadInnerMessage(),
		["@difficulty_name@"] = difficultyName,
		["@instance_type@"] = instanceType
	}
	-- Calculate limiting RPC conditions
	local global_conditions = {
		["#dungeon#"] = (inner_placeholders["@instance_type@"] == "party"),
		["#raid#"] = (inner_placeholders["@instance_type@"] == "raid"),
		["#battleground#"] = (inner_placeholders["@instance_type@"] == "pvp")
	}
	local inner_conditions = {
		["@dead_state@"] = (UnitIsDeadOrGhost("player") and not UnitIsDead("player"))
	}
	local time_conditions = {
		["start"] = (global_conditions["#dungeon#"] or global_conditions["#raid#"] or global_conditions["#battleground#"])
	}
	global_conditions["#default#"] = (not time_conditions["start"])

	local outputTable = {}

	for inKey,inValue in pairs(global_placeholders) do
		local output = inValue:trim()
		
		if(global_conditions[inKey] == nil or global_conditions[inKey]) then
			for key,value in pairs(inner_placeholders) do
				if(inner_conditions[key] == nil or inner_conditions[key]) then
					output = output:gsub(key, value)
				else
					output = output:gsub(key, "")
				end
			end
			outputTable[inKey] = output:trim()
		else
			outputTable[inKey] = ""
		end
	end
	return outputTable, inner_placeholders, time_conditions
end

function CraftPresence:EncodeConfigData()
	-- Placeholder data and syncing
	local global_placeholders = {
		["#dungeon#"] = self:GetDungeonPlaceholderMessage(),
		["#raid#"] = self:GetRaidPlaceholderMessage(),
		["#battleground#"] = self:GetBattlegroundPlaceholderMessage(),
		["#default#"] = self:GetDefaultPlaceholderMessage()
	}
	local inner_placeholders = {}
	local time_conditions = {}
	global_placeholders, inner_placeholders, time_conditions = self:ParsePlaceholderData(global_placeholders)
	-- RPC Data syncing
	local queued_details = self:GetDetailsMessage()
	local queued_state = self:GetGameStateMessage()
	local queued_large_image_text = self:GetLargeImageMessage()
	local queued_time_start = nullKey
	local queued_time_end = nullKey
	for key,value in pairs(global_placeholders) do
		if self:IsVerboseMode() and self:IsShowLoggingInChat() then
			self:Print("Global Data:", key, "=", value)
		end
		queued_details = queued_details:gsub(key, value)
		queued_state = queued_state:gsub(key, value)
		queued_large_image_text = queued_large_image_text:gsub(key, value)
	end
	for innerKey,innerValue in pairs(inner_placeholders) do
		if self:IsVerboseMode() and self:IsShowLoggingInChat() then
			self:Print("Inner Data:", innerKey, "=", innerValue)
		end
		queued_details = queued_details:gsub(innerKey, innerValue)
		queued_state = queued_state:gsub(innerKey, innerValue)
		queued_large_image_text = queued_large_image_text:gsub(innerKey, innerValue)
	end
	for timeKey,timeValue in pairs(time_conditions) do
		if timeValue then
			if timeKey == "start" then
				queued_time_start = "generated"
			elseif timeKey == "end" then
				queued_time_end = "generated"
			end
		end
	end
	return self:EncodeData(self:GetClientId(), "wow_icon", queued_large_image_text, nil, nil, queued_details, queued_state, queued_time_start, queued_time_end)
end

function CraftPresence:EncodeData(clientId, largeImageKey, largeImageText, smallImageKey, smallImageText, details, gameState, startTime, endTime)
	if clientId ~= nil then clientId = L["DEFAULT_CLIENT_ID"] end
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
	local encoded = self:EncodeConfigData()
	if encoded ~= nil then self:PaintSomething(encoded) end
end

function CraftPresence:PaintMessageWait(force)
	local proceed = force ~= nil and force == true;
	local encoded = self:EncodeConfigData()
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

function CraftPresence:GetLargeImageMessage(info)
    return self.db.profile.largeImageMessage
end

function CraftPresence:SetLargeImageMessage(info, newValue)
    self.db.profile.largeImageMessage = newValue
end

-- ================================
-- Placeholder Settings
-- ================================

function CraftPresence:GetDungeonPlaceholderMessage(info)
    return self.db.profile.dungeonPlaceholderMessage
end

function CraftPresence:SetDungeonPlaceholderMessage(info, newValue)
    self.db.profile.dungeonPlaceholderMessage = newValue
end

function CraftPresence:GetRaidPlaceholderMessage(info)
    return self.db.profile.raidPlaceholderMessage
end

function CraftPresence:SetRaidPlaceholderMessage(info, newValue)
    self.db.profile.raidPlaceholderMessage = newValue
end

function CraftPresence:GetBattlegroundPlaceholderMessage(info)
    return self.db.profile.battlegroundPlaceholderMessage
end

function CraftPresence:SetBattlegroundPlaceholderMessage(info, newValue)
    self.db.profile.battlegroundPlaceholderMessage = newValue
end

function CraftPresence:GetDefaultPlaceholderMessage(info)
    return self.db.profile.defaultPlaceholderMessage
end

function CraftPresence:SetDefaultPlaceholderMessage(info, newValue)
    self.db.profile.defaultPlaceholderMessage = newValue
end

function CraftPresence:GetDeadInnerMessage(info)
    return self.db.profile.deadStateInnerMessage
end

function CraftPresence:SetDeadInnerMessage(info, newValue)
    self.db.profile.deadStateInnerMessage = newValue
end

-- ================================
-- Core Settings
-- ================================
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

function CraftPresence:IsVerboseMode(info)
    return self.db.profile.verboseMode
end

function CraftPresence:ToggleVerboseMode(info, value)
    self.db.profile.verboseMode = value
end
