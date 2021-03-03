local CraftPresence = LibStub("AceAddon-3.0"):GetAddon("CraftPresence")

local L = LibStub("AceLocale-3.0"):GetLocale("CraftPresence")

-- DB_DEFAULTS
local DB_DEFAULTS = {
    profile = {
        clientId = L["DEFAULT_CLIENT_ID"],
        gameStateMessage = L["DEFAULT_GAME_STATE_MESSAGE"],
        detailsMessage = L["DEFAULT_DETAILS_MESSAGE"],
        largeImageKey = L["DEFAULT_LARGE_IMAGE_KEY"],
        largeImageMessage = L["DEFAULT_LARGE_IMAGE_MESSAGE"],
        smallImageKey = L["DEFAULT_SMALL_IMAGE_KEY"],
        smallImageMessage = L["DEFAULT_SMALL_IMAGE_MESSAGE"],
        dungeonPlaceholderMessage = L["DEFAULT_DUNGEON_MESSAGE"],
        raidPlaceholderMessage = L["DEFAULT_RAID_MESSAGE"],
        battlegroundPlaceholderMessage = L["DEFAULT_BATTLEGROUND_MESSAGE"],
        arenaPlaceholderMessage = L["DEFAULT_ARENA_MESSAGE"],
        defaultPlaceholderMessage = L["DEFAULT_FALLBACK_MESSAGE"],
        deadStateInnerMessage = L["DEFAULT_DEAD_MESSAGE"],
        showLoggingInChat = true,
        debugMode = false,
        verboseMode = false,
        showMinimapIcon = true,
        callbackDelay = 2
    },
}

-- Config Tabs
-----------------------------------------
--TAB:  GENERAL
-----------------------------------------
local generalOptionsGroup = {
    type = "group", order = 10, name = "General",
    get = function(info)
        return CraftPresence.db.profile[info[#info]]
    end,
    set = function(info, value)
        CraftPresence.db.profile[info[#info]] = value;
    end,
    args = {
        clientId = {
            type = "input", order = 10, width = 1.25,
            name = L["TITLE_CLIENT_ID"],
            desc = L["COMMENT_CLIENT_ID"],
            usage = L["USAGE_CLIENT_ID"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "clientId")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "clientId")
                local isValid = (value ~= nil and CraftPresence:ContainsDigit(value) and string.len(value) == 18)
                if isValid then
                    CraftPresence.db.profile.clientId = value;
                    CraftPresence:PrintChangedValue(L["TITLE_CLIENT_ID"], oldValue, value)
                end
            end,
        },
        blank1 = { type = "description", order = 11, fontSize = "small", name = " " },
        gameStateMessage = {
            type = "input", order = 12, width = 3.0,
            name = L["TITLE_GAME_STATE_MESSAGE"],
            desc = L["COMMENT_GAME_STATE_MESSAGE"],
            usage = L["USAGE_GAME_STATE_MESSAGE"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "gameStateMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "gameStateMessage")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.gameStateMessage = value;
                    CraftPresence:PrintChangedValue(L["TITLE_GAME_STATE_MESSAGE"], oldValue, value)
                end
            end,
        },
        blank2 = { type = "description", order = 13, fontSize = "small", name = " " },
        detailsMessage = {
            type = "input", order = 14, width = 3.0,
            name = L["TITLE_DETAILS_MESSAGE"],
            desc = L["COMMENT_DETAILS_MESSAGE"],
            usage = L["USAGE_DETAILS_MESSAGE"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "detailsMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "detailsMessage")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.detailsMessage = value;
                    CraftPresence:PrintChangedValue(L["TITLE_DETAILS_MESSAGE"], oldValue, value)
                end
            end,
        },
        blank3 = { type = "description", order = 15, fontSize = "small", name = " " },
        largeImageKey = {
            type = "input", order = 16, width = 3.0,
            name = L["TITLE_LARGE_IMAGE_KEY"],
            desc = L["COMMENT_LARGE_IMAGE_KEY"],
            usage = L["USAGE_LARGE_IMAGE_KEY"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "largeImageKey")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "largeImageKey")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.largeImageKey = value;
                    CraftPresence:PrintChangedValue(L["TITLE_LARGE_IMAGE_KEY"], oldValue, value)
                end
            end,
        },
        blank4 = { type = "description", order = 17, fontSize = "small", name = " " },
        largeImageMessage = {
            type = "input", order = 18, width = 3.0,
            name = L["TITLE_LARGE_IMAGE_MESSAGE"],
            desc = L["COMMENT_LARGE_IMAGE_MESSAGE"],
            usage = L["USAGE_LARGE_IMAGE_MESSAGE"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "largeImageMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "largeImageMessage")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.largeImageMessage = value;
                    CraftPresence:PrintChangedValue(L["TITLE_LARGE_IMAGE_MESSAGE"], oldValue, value)
                end
            end,
        },
        blank5 = { type = "description", order = 19, fontSize = "small", name = " " },
        smallImageKey = {
            type = "input", order = 20, width = 3.0,
            name = L["TITLE_SMALL_IMAGE_KEY"],
            desc = L["COMMENT_SMALL_IMAGE_KEY"],
            usage = L["USAGE_SMALL_IMAGE_KEY"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "smallImageKey")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "smallImageKey")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.smallImageKey = value;
                    CraftPresence:PrintChangedValue(L["TITLE_SMALL_IMAGE_KEY"], oldValue, value)
                end
            end,
        },
        blank6 = { type = "description", order = 21, fontSize = "small", name = " " },
        smallImageMessage = {
            type = "input", order = 22, width = 3.0,
            name = L["TITLE_SMALL_IMAGE_MESSAGE"],
            desc = L["COMMENT_SMALL_IMAGE_MESSAGE"],
            usage = L["USAGE_SMALL_IMAGE_MESSAGE"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "smallImageMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "smallImageMessage")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.smallImageMessage = value;
                    CraftPresence:PrintChangedValue(L["TITLE_SMALL_IMAGE_MESSAGE"], oldValue, value)
                end
            end,
        },
        blank7 = { type = "description", order = 23, fontSize = "small", name = " " },
    }
}

-----------------------------------------
--TAB:  PLACEHOLDERS
-----------------------------------------
local placeholderOptionsGroup = {
    type = "group", order = 15, name = "Placeholders",
    get = function(info)
        return CraftPresence.db.profile[info[#info]]
    end,
    set = function(info, value)
        CraftPresence.db.profile[info[#info]] = value;
    end,
    args = {
        dungeonPlaceholderMessage = {
            type = "input", order = 10, width = 3.0,
            name = L["TITLE_DUNGEON_MESSAGE"],
            desc = L["COMMENT_DUNGEON_MESSAGE"],
            usage = L["USAGE_DUNGEON_MESSAGE"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "dungeonPlaceholderMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "dungeonPlaceholderMessage")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.dungeonPlaceholderMessage = value;
                    CraftPresence:PrintChangedValue(L["TITLE_DUNGEON_MESSAGE"], oldValue, value)
                end
            end,
        },
        blank1 = { type = "description", order = 11, fontSize = "small", name = " " },
        raidPlaceholderMessage = {
            type = "input", order = 12, width = 3.0,
            name = L["TITLE_RAID_MESSAGE"], desc = L["COMMENT_RAID_MESSAGE"], usage = L["USAGE_RAID_MESSAGE"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "raidPlaceholderMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "raidPlaceholderMessage")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.raidPlaceholderMessage = value;
                    CraftPresence:PrintChangedValue(L["TITLE_RAID_MESSAGE"], oldValue, value)
                end
            end,
        },
        blank2 = { type = "description", order = 13, fontSize = "small", name = " " },
        battlegroundPlaceholderMessage = {
            type = "input", order = 14, width = 3.0,
            name = L["TITLE_BATTLEGROUND_MESSAGE"],
            desc = L["COMMENT_BATTLEGROUND_MESSAGE"],
            usage = L["USAGE_BATTLEGROUND_MESSAGE"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "battlegroundPlaceholderMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "battlegroundPlaceholderMessage")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.battlegroundPlaceholderMessage = value;
                    CraftPresence:PrintChangedValue(L["TITLE_BATTLEGROUND_MESSAGE"], oldValue, value)
                end
            end,
        },
        blank3 = { type = "description", order = 15, fontSize = "small", name = " " },
        arenaPlaceholderMessage = {
            type = "input", order = 16, width = 3.0,
            name = L["TITLE_ARENA_MESSAGE"],
            desc = L["COMMENT_ARENA_MESSAGE"],
            usage = L["USAGE_ARENA_MESSAGE"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "arenaPlaceholderMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "arenaPlaceholderMessage")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.arenaPlaceholderMessage = value;
                    CraftPresence:PrintChangedValue(L["TITLE_ARENA_MESSAGE"], oldValue, value)
                end
            end,
        },
        blank4 = { type = "description", order = 17, fontSize = "small", name = " " },
        defaultPlaceholderMessage = {
            type = "input", order = 18, width = 3.0,
            name = L["TITLE_FALLBACK_MESSAGE"],
            desc = L["COMMENT_FALLBACK_MESSAGE"],
            usage = L["USAGE_FALLBACK_MESSAGE"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "defaultPlaceholderMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "defaultPlaceholderMessage")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.defaultPlaceholderMessage = value;
                    CraftPresence:PrintChangedValue(L["TITLE_FALLBACK_MESSAGE"], oldValue, value)
                end
            end,
        },
        blank5 = { type = "description", order = 19, fontSize = "small", name = " " },
        deadStateInnerMessage = {
            type = "input", order = 20, width = 3.0,
            name = L["TITLE_DEAD_MESSAGE"], desc = L["COMMENT_DEAD_MESSAGE"], usage = L["USAGE_DEAD_MESSAGE"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "deadStateInnerMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "deadStateInnerMessage")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.deadStateInnerMessage = value;
                    CraftPresence:PrintChangedValue(L["TITLE_DEAD_MESSAGE"], oldValue, value)
                end
            end,
        },
        blank6 = { type = "description", order = 21, fontSize = "small", name = " " },
    }
}

-----------------------------------------
--TAB:  EXTRA
-----------------------------------------
local extraOptionsGroup = {
    type = "group", order = 20, name = "Extra",
    get = function(info)
        return CraftPresence.db.profile[info[#info]]
    end,
    set = function(info, value)
        CraftPresence.db.profile[info[#info]] = value;
    end,
    args = {
        showLoggingInChat = {
            type = "toggle", order = 10,
            name = L["TITLE_SHOW_LOGGING_IN_CHAT"],
            desc = L["COMMENT_SHOW_LOGGING_IN_CHAT"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "showLoggingInChat")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "showLoggingInChat")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.showLoggingInChat = value;
                    CraftPresence:PrintChangedValue(L["TITLE_SHOW_LOGGING_IN_CHAT"], oldValue, value)
                end
            end,
        },
        blank1 = { type = "description", order = 11, fontSize = "small", name = " " },
        debugMode = {
            type = "toggle", order = 12,
            name = L["TITLE_DEBUG_MODE"],
            desc = L["COMMENT_DEBUG_MODE"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "debugMode")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "debugMode")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.debugMode = value;
                    CraftPresence:PrintChangedValue(L["TITLE_DEBUG_MODE"], oldValue, value)
                end
            end,
        },
        blank2 = { type = "description", order = 13, fontSize = "small", name = " " },
        verboseMode = {
            type = "toggle", order = 14,
            name = L["TITLE_VERBOSE_MODE"],
            desc = L["COMMENT_VERBOSE_MODE"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "verboseMode")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "verboseMode")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.verboseMode = value;
                    CraftPresence:PrintChangedValue(L["TITLE_VERBOSE_MODE"], oldValue, value)
                end
            end,
        },
        blank3 = { type = "description", order = 15, fontSize = "small", name = " " },
        showMinimapIcon = {
            type = "toggle", order = 16,
            name = L["TITLE_SHOW_MINIMAP_ICON"],
            desc = L["COMMENT_SHOW_MINIMAP_ICON"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "showMinimapIcon")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "showMinimapIcon")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.showMinimapIcon = value;
                    CraftPresence:PrintChangedValue(L["TITLE_SHOW_MINIMAP_ICON"], oldValue, value)
                end
            end,
        },
        blank4 = { type = "description", order = 17, fontSize = "small", name = " " },
        callbackDelay = {
            type = "range", order = 18, width = 1.50,
            min = 0, max = 60, step = 1,
            name = L["TITLE_CALLBACK_DELAY"],
            desc = L["COMMENT_CALLBACK_DELAY"],
            get = function(_)
                return CraftPresence.GetFromDb(nil, "callbackDelay")
            end,
            set = function(_, value)
                local oldValue = CraftPresence.GetFromDb(nil, "callbackDelay")
                local isValid = true
                if isValid then
                    CraftPresence.db.profile.callbackDelay = value;
                    CraftPresence:PrintChangedValue(L["TITLE_CALLBACK_DELAY"], oldValue, value)
                end
            end,
        },
        blank5 = { type = "description", order = 19, fontSize = "small", name = " " },
    }
}

-----------------------------------------
--TAB:  ABOUT
-----------------------------------------
local aboutGroup = {
    type = "group", order = 25, name = "About",
    args = {
        generalText1 = {
            type = "description", order = 10, width = "full", fontSize = "medium",
            name = L["ADDON_INFO_ONE"],
        },
        thanksHeader = { order = 20, type = "header", name = "Credits", },
        generalText2 = {
            type = "description", order = 40, fontSize = "medium",
            name = L["ADDON_INFO_TWO"]
        },
        blank1 = { type = "description", order = 50, fontSize = "small", name = "", width = "full", },
        generalText3 = {
            type = "description", order = 53, fontSize = "medium",
            name = L["ADDON_INFO_THREE"]
        },
        blank2 = { type = "description", order = 56, fontSize = "small", name = "", width = "full", },
        generalText4 = {
            type = "description", order = 60, fontSize = "medium",
            name = L["ADDON_INFO_FOUR"]
        }
    }
}

function CraftPresence:getOptionsTable()
    local profilesGroup = LibStub("AceDBOptions-3.0"):GetOptionsTable(CraftPresence.db)

    local opts = {
        type = "group", childGroups = "tab",
        name = string.format("%s %s", L["ADDON_NAME"], L["ADDON_VERSION"]),
        get = function(info)
            return CraftPresence.db.profile[info[#info]]
        end,
        set = function(info, value)
            CraftPresence.db.profile[info[#info]] = value;
        end,
        args = {
            generalOptionsGrp = generalOptionsGroup,
            placeholderOptionsGrp = placeholderOptionsGroup,
            extraOptionsGrp = extraOptionsGroup,
            profilesGrp = profilesGroup,
            aboutGrp = aboutGroup,
        },
    };
    return opts;
end

function CraftPresence:CanLogChanges()
    return (CraftPresence.GetFromDb(nil, "verboseMode") and CraftPresence.GetFromDb(nil, "showLoggingInChat"))
end

function CraftPresence:PrintChangedValue(fieldName, oldValue, value)
    if oldValue ~= value and CraftPresence.CanLogChanges() then
        CraftPresence:Print(
                string.format(
                        L["VERBOSE_LOG"], string.format(
                                L["DEBUG_VALUE_CHANGED"], fieldName, tostring(oldValue), tostring(value)
                        )
                )
        )
    end
end

function CraftPresence:GetDefaults()
    return DB_DEFAULTS;
end

function CraftPresence:ResetDB()
    CraftPresence.db:ResetProfile(false, true)
end
