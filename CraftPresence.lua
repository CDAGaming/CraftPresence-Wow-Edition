CraftPresence = LibStub("AceAddon-3.0"):NewAddon("CraftPresence", "AceConsole-3.0", "AceEvent-3.0")

local L = LibStub("AceLocale-3.0"):GetLocale("CraftPresence")

local options = {
    name = "CraftPresence",
    handler = CraftPresence,
    type = 'group',
    args = {
		homeMessage = {
            type = "input",
            name = L["Home Message"],
            desc = L["The message text to be displayed when you get home"],
            usage = L["<Your message here>"],
            get = "GetHomeMessage",
            set = "SetHomeMessage",
		},
		zoneMessage = {
            type = "input",
            name = L["Zone Message"],
            desc = L["The message text to be displayed when you enter a new zone"],
            usage = L["<Your message here>"],
            get = "GetZoneMessage",
            set = "SetZoneMessage",
        },
        showInChat = {
            type = "toggle",
            name = L["Show in Chat"],
            desc = L["Toggles the display of the message in the chat window."],
            get = "IsShowInChat",
            set = "ToggleShowInChat",
        },
        showOnScreen = {
            type = "toggle",
            name = L["Show on Screen"],
            desc = L["Toggles the display of the message on the screen."],
            get = "IsShowOnScreen",
            set = "ToggleShowOnScreen"
        },
    },
}

local defaults = {
    profile = {
		homeMessage = L["Welcome Home!"],
		zoneMessage = L["Entering new zone!"],
        showInChat = false,
        showOnScreen = true,
    },
}

function CraftPresence:OnInitialize()
	-- Called when the addon is loaded
	self.db = LibStub("AceDB-3.0"):New("CraftPresenceDB", defaults, true)
	LibStub("AceConfig-3.0"):RegisterOptionsTable("CraftPresence", options)
	self.optionsFrame = LibStub("AceConfigDialog-3.0"):AddToBlizOptions("CraftPresence", "CraftPresence")
	self:RegisterChatCommand("craftpresence", "ChatCommand")
	self:RegisterChatCommand("cp", "ChatCommand")
end

function CraftPresence:OnEnable()
	-- Called when the addon is enabled
	self:Print("Hello World!")
	self:RegisterEvent("ZONE_CHANGED")
	discord.startPlugin()
end

function CraftPresence:OnDisable()
	-- Called when the addon is disabled
end

function CraftPresence:ZONE_CHANGED()
    if GetBindLocation() == GetSubZoneText() then
		if self.db.profile.showInChat then
            self:Print(self.db.profile.homeMessage)
        end

        if self.db.profile.showOnScreen then
            UIErrorsFrame:AddMessage(self.db.profile.homeMessage, 1.0, 1.0, 1.0, 5.0)
        end
	else
		if self.db.profile.showInChat then
            self:Print(self.zoneMessage)
        end

        if self.db.profile.showOnScreen then
            UIErrorsFrame:AddMessage(self.db.profile.zoneMessage, 1.0, 1.0, 1.0, 5.0)
        end
    end
end

function CraftPresence:GetHomeMessage(info)
    return self.db.profile.homeMessage
end

function CraftPresence:SetHomeMessage(info, newValue)
    self.db.profile.homeMessage = newValue
end

function CraftPresence:GetZoneMessage(info)
    return self.db.profile.zoneMessage
end

function CraftPresence:SetZoneMessage(info, newValue)
    self.db.profile.zoneMessage = newValue
end

function CraftPresence:IsShowInChat(info)
    return self.db.profile.showInChat
end

function CraftPresence:ToggleShowInChat(info, value)
    self.db.profile.showInChat = value
end

function CraftPresence:IsShowOnScreen(info)
    return self.db.profile.showOnScreen
end

function CraftPresence:ToggleShowOnScreen(info, value)
    self.db.profile.showOnScreen = value
end

function CraftPresence:ChatCommand(input)
    if not input or input:trim() == "" then
        InterfaceOptionsFrame_OpenToCategory(self.optionsFrame)
    else
        LibStub("AceConfigCmd-3.0"):HandleCommand("cp", "CraftPresence", input)
    end
end
