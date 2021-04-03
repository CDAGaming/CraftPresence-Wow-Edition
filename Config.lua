local CraftPresence = LibStub("AceAddon-3.0"):GetAddon("CraftPresence")

local L = LibStub("AceLocale-3.0"):GetLocale("CraftPresence")

-- SCHEMA_DEFAULTS
local SCHEMA_DEFAULTS = {
    global = {
        Characters = {
            ['*'] = {
            },
        },
    },
}

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
        debugMode = false,
        verboseMode = false,
        showMinimapIcon = true,
        queuedPipeline = false,
        callbackDelay = 2,
        frameSize = 6,
        frameClearDelay = 10,
        primaryButton = {
            label = "",
            url = ""
        },
        secondaryButton = {
            label = "",
            url = ""
        }
    },
}

-- Config Tabs
-----------------------------------------
--TAB:  GENERAL
-----------------------------------------
local generalOptionsGroup = {
    type = "group", order = 10,
    name = L["CATEGORY_TITLE_GENERAL"], desc = L["CATEGORY_COMMENT_GENERAL"],
    get = function(info)
        return CraftPresence.db.profile[info[CraftPresence:GetLength(info)]]
    end,
    set = function(info, value)
        CraftPresence.db.profile[info[CraftPresence:GetLength(info)]] = value
    end,
    args = {
        clientId = {
            type = "input", order = 10, width = 1.25,
            name = L["TITLE_CLIENT_ID"],
            desc = L["COMMENT_CLIENT_ID"],
            usage = L["USAGE_CLIENT_ID"],
            get = function(_)
                return CraftPresence:GetFromDb("clientId")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("clientId")
                local isValid = (
                        value ~= nil and
                                CraftPresence:ContainsDigit(value) and
                                CraftPresence:GetLength(value) == 18
                )
                if isValid then
                    CraftPresence.db.profile.clientId = value
                    CraftPresence:PrintChangedValue(L["TITLE_CLIENT_ID"], oldValue, value)
                else
                    CraftPresence:PrintInvalidValue(L["ERROR_CLIENT_ID"])
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
                return CraftPresence:GetFromDb("gameStateMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("gameStateMessage")
                local isValid = (type(value) == "string")
                if isValid then
                    CraftPresence.db.profile.gameStateMessage = value
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
                return CraftPresence:GetFromDb("detailsMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("detailsMessage")
                local isValid = (type(value) == "string")
                if isValid then
                    CraftPresence.db.profile.detailsMessage = value
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
                return CraftPresence:GetFromDb("largeImageKey")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("largeImageKey")
                local isValid = (type(value) == "string")
                if isValid then
                    CraftPresence.db.profile.largeImageKey = value
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
                return CraftPresence:GetFromDb("largeImageMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("largeImageMessage")
                local isValid = (type(value) == "string")
                if isValid then
                    CraftPresence.db.profile.largeImageMessage = value
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
                return CraftPresence:GetFromDb("smallImageKey")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("smallImageKey")
                local isValid = (type(value) == "string")
                if isValid then
                    CraftPresence.db.profile.smallImageKey = value
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
                return CraftPresence:GetFromDb("smallImageMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("smallImageMessage")
                local isValid = (type(value) == "string")
                if isValid then
                    CraftPresence.db.profile.smallImageMessage = value
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
    type = "group", order = 15,
    name = L["CATEGORY_TITLE_PLACEHOLDERS"], desc = L["CATEGORY_COMMENT_PLACEHOLDERS"],
    get = function(info)
        return CraftPresence.db.profile[info[CraftPresence:GetLength(info)]]
    end,
    set = function(info, value)
        CraftPresence.db.profile[info[CraftPresence:GetLength(info)]] = value
    end,
    args = {
        dungeonPlaceholderMessage = {
            type = "input", order = 10, width = 3.0,
            name = L["TITLE_DUNGEON_MESSAGE"],
            desc = L["COMMENT_DUNGEON_MESSAGE"],
            usage = L["USAGE_DUNGEON_MESSAGE"],
            get = function(_)
                return CraftPresence:GetFromDb("dungeonPlaceholderMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("dungeonPlaceholderMessage")
                local isValid = (type(value) == "string")
                if isValid then
                    CraftPresence.db.profile.dungeonPlaceholderMessage = value
                    CraftPresence:PrintChangedValue(L["TITLE_DUNGEON_MESSAGE"], oldValue, value)
                end
            end,
        },
        blank1 = { type = "description", order = 11, fontSize = "small", name = " " },
        raidPlaceholderMessage = {
            type = "input", order = 12, width = 3.0,
            name = L["TITLE_RAID_MESSAGE"], desc = L["COMMENT_RAID_MESSAGE"], usage = L["USAGE_RAID_MESSAGE"],
            get = function(_)
                return CraftPresence:GetFromDb("raidPlaceholderMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("raidPlaceholderMessage")
                local isValid = (type(value) == "string")
                if isValid then
                    CraftPresence.db.profile.raidPlaceholderMessage = value
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
                return CraftPresence:GetFromDb("battlegroundPlaceholderMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("battlegroundPlaceholderMessage")
                local isValid = (type(value) == "string")
                if isValid then
                    CraftPresence.db.profile.battlegroundPlaceholderMessage = value
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
                return CraftPresence:GetFromDb("arenaPlaceholderMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("arenaPlaceholderMessage")
                local isValid = (type(value) == "string")
                if isValid then
                    CraftPresence.db.profile.arenaPlaceholderMessage = value
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
                return CraftPresence:GetFromDb("defaultPlaceholderMessage")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("defaultPlaceholderMessage")
                local isValid = (type(value) == "string")
                if isValid then
                    CraftPresence.db.profile.defaultPlaceholderMessage = value
                    CraftPresence:PrintChangedValue(L["TITLE_FALLBACK_MESSAGE"], oldValue, value)
                end
            end,
        },
        blank5 = { type = "description", order = 19, fontSize = "small", name = " " },
    }
}

-----------------------------------------
--TAB:  BUTTONS
-----------------------------------------
local buttonOptionsGroup = {
    type = "group", order = 20,
    name = L["CATEGORY_TITLE_BUTTONS"], desc = L["CATEGORY_COMMENT_BUTTONS"],
    get = function(info)
        return CraftPresence.db.profile[info[CraftPresence:GetLength(info)]]
    end,
    set = function(info, value)
        CraftPresence.db.profile[info[CraftPresence:GetLength(info)]] = value
    end,
    args = {
        buttonHeader = { order = 1, type = "header", name = L["CATEGORY_TITLE_BUTTONS"], },
        primaryButton = {
            name = L["TITLE_PRIMARY_BUTTON"],
            desc = L["COMMENT_PRIMARY_BUTTON"],
            type = "group", order = 2,
            args = CraftPresence:GetButtonArgs("primaryButton")
        },
        secondaryButton = {
            name = L["TITLE_SECONDARY_BUTTON"],
            desc = L["COMMENT_SECONDARY_BUTTON"],
            type = "group", order = 3,
            args = CraftPresence:GetButtonArgs("secondaryButton")
        },
        blank1 = { type = "description", order = 4, fontSize = "small", name = " " },
    }
}

-----------------------------------------
--TAB:  EXTRA
-----------------------------------------
local extraOptionsGroup = {
    type = "group", order = 25,
    name = L["CATEGORY_TITLE_EXTRA"], desc = L["CATEGORY_COMMENT_EXTRA"],
    get = function(info)
        return CraftPresence.db.profile[info[CraftPresence:GetLength(info)]]
    end,
    set = function(info, value)
        CraftPresence.db.profile[info[CraftPresence:GetLength(info)]] = value
    end,
    args = {
        debugMode = {
            type = "toggle", order = 10,
            name = L["TITLE_DEBUG_MODE"],
            desc = L["COMMENT_DEBUG_MODE"],
            get = function(_)
                return CraftPresence:GetFromDb("debugMode")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("debugMode")
                local isValid = (type(value) == "boolean")
                if isValid then
                    CraftPresence.db.profile.debugMode = value
                    CraftPresence:PrintChangedValue(L["TITLE_DEBUG_MODE"], oldValue, value)
                end
            end,
        },
        blank1 = { type = "description", order = 11, fontSize = "small", name = " " },
        verboseMode = {
            type = "toggle", order = 12,
            name = L["TITLE_VERBOSE_MODE"],
            desc = L["COMMENT_VERBOSE_MODE"],
            get = function(_)
                return CraftPresence:GetFromDb("verboseMode")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("verboseMode")
                local isValid = (type(value) == "boolean")
                if isValid then
                    CraftPresence.db.profile.verboseMode = value
                    CraftPresence:PrintChangedValue(L["TITLE_VERBOSE_MODE"], oldValue, value)
                end
            end,
        },
        blank2 = { type = "description", order = 13, fontSize = "small", name = " " },
        showMinimapIcon = {
            type = "toggle", order = 14,
            name = L["TITLE_SHOW_MINIMAP_ICON"],
            desc = L["COMMENT_SHOW_MINIMAP_ICON"],
            get = function(_)
                return CraftPresence:GetFromDb("showMinimapIcon")
            end,
            set = function(_, value)
                CraftPresence:UpdateMinimapSetting(value)
            end,
        },
        blank3 = { type = "description", order = 15, fontSize = "small", name = " " },
        queuedPipeline = {
            type = "toggle", order = 16,
            name = L["TITLE_QUEUED_PIPELINE"],
            desc = L["COMMENT_QUEUED_PIPELINE"],
            get = function(_)
                return CraftPresence:GetFromDb("queuedPipeline")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("queuedPipeline")
                local isValid = (type(value) == "boolean")
                if isValid then
                    CraftPresence.db.profile.queuedPipeline = value
                    CraftPresence:PrintChangedValue(L["TITLE_QUEUED_PIPELINE"], oldValue, value)
                end
            end,
        },
        blank4 = { type = "description", order = 17, fontSize = "small", name = " " },
        callbackDelay = {
            type = "range", order = 18, width = 1.50,
            min = L["MINIMUM_CALLBACK_DELAY"], max = L["MAXIMUM_CALLBACK_DELAY"], step = 1,
            name = L["TITLE_CALLBACK_DELAY"],
            desc = L["COMMENT_CALLBACK_DELAY"],
            get = function(_)
                return CraftPresence:GetFromDb("callbackDelay")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("callbackDelay")
                local isValid = (CraftPresence:IsWithinValue(
                        value, L["MINIMUM_CALLBACK_DELAY"], L["MAXIMUM_CALLBACK_DELAY"], true
                ))
                if isValid then
                    CraftPresence.db.profile.callbackDelay = value
                    CraftPresence:PrintChangedValue(L["TITLE_CALLBACK_DELAY"], oldValue, value)
                else
                    CraftPresence:PrintInvalidValue(
                            L["ERROR_RANGE_DEFAULT"], L["TITLE_CALLBACK_DELAY"],
                            L["MINIMUM_CALLBACK_DELAY"], L["MAXIMUM_CALLBACK_DELAY"]
                    )
                end
            end,
        },
        frameClearDelay = {
            type = "range", order = 19, width = 1.50,
            min = L["MINIMUM_FRAME_CLEAR_DELAY"], max = L["MAXIMUM_FRAME_CLEAR_DELAY"], step = 1,
            name = L["TITLE_FRAME_CLEAR_DELAY"],
            desc = L["COMMENT_FRAME_CLEAR_DELAY"],
            get = function(_)
                return CraftPresence:GetFromDb("frameClearDelay")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("frameClearDelay")
                local isValid = (CraftPresence:IsWithinValue(
                        value, L["MINIMUM_FRAME_CLEAR_DELAY"], L["MAXIMUM_FRAME_CLEAR_DELAY"], true
                ))
                if isValid then
                    CraftPresence.db.profile.frameClearDelay = value
                    CraftPresence:PrintChangedValue(L["TITLE_FRAME_CLEAR_DELAY"], oldValue, value)
                else
                    CraftPresence:PrintInvalidValue(
                            L["ERROR_RANGE_DEFAULT"], L["TITLE_FRAME_CLEAR_DELAY"],
                            L["MINIMUM_FRAME_CLEAR_DELAY"], L["MAXIMUM_FRAME_CLEAR_DELAY"]
                    )
                end
            end,
        },
        blank6 = { type = "description", order = 20, fontSize = "small", name = " " },
        frameSize = {
            type = "range", order = 21, width = 1.50,
            min = L["MINIMUM_FRAME_SIZE"], max = L["MAXIMUM_FRAME_SIZE"], step = 1,
            name = L["TITLE_FRAME_SIZE"],
            desc = L["COMMENT_FRAME_SIZE"],
            get = function(_)
                return CraftPresence:GetFromDb("frameSize")
            end,
            set = function(_, value)
                local oldValue = CraftPresence:GetFromDb("frameSize")
                local isValid = (CraftPresence:IsWithinValue(
                        value, L["MINIMUM_FRAME_SIZE"], L["MAXIMUM_FRAME_SIZE"], true
                ))
                if isValid then
                    CraftPresence.db.profile.frameSize = value
                    CraftPresence:PrintChangedValue(L["TITLE_FRAME_SIZE"], oldValue, value)
                else
                    CraftPresence:PrintInvalidValue(
                            L["ERROR_RANGE_DEFAULT"], L["TITLE_FRAME_SIZE"],
                            L["MINIMUM_FRAME_SIZE"], L["MAXIMUM_FRAME_SIZE"]
                    )
                end
            end,
        },
        blank7 = { type = "description", order = 22, fontSize = "small", name = " " },
    }
}

-----------------------------------------
--TAB:  ABOUT
-----------------------------------------
local aboutGroup = {
    type = "group", order = 30,
    name = L["CATEGORY_TITLE_ABOUT"], desc = L["CATEGORY_COMMENT_ABOUT"],
    args = {
        generalText1 = {
            type = "description", order = 10, width = "full", fontSize = "medium",
            name = L["ADDON_INFO_ONE"],
        },
        thanksHeader = { order = 20, type = "header", name = L["ADDON_HEADER_CREDITS"], },
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

--- Retrieves the option table to be used in the Config Menu
--- @return table @ opts
function CraftPresence:getOptionsTable()
    local profilesGroup = LibStub("AceDBOptions-3.0"):GetOptionsTable(CraftPresence.db)

    local opts = {
        type = "group", childGroups = "tab",
        name = string.format("%s %s", L["ADDON_NAME"], CraftPresence:GetVersion()),
        get = function(info)
            return CraftPresence.db.profile[info[CraftPresence:GetLength(info)]]
        end,
        set = function(info, value)
            CraftPresence.db.profile[info[CraftPresence:GetLength(info)]] = value
        end,
        args = {
            generalOptions = generalOptionsGroup,
            placeholderOptions = placeholderOptionsGroup,
            buttonOptions = buttonOptionsGroup,
            extraOptions = extraOptionsGroup,
            profiles = profilesGroup,
            about = aboutGroup,
        },
    }
    return opts
end

--- Retrieves whether or not logging changed data is allowed
--- @return boolean @ canLogChanges
function CraftPresence:CanLogChanges()
    return CraftPresence:GetFromDb("verboseMode")
end

--- Prints change data, if possible, using the specified parameters
---
--- @param fieldName string The config name the change belongs to
--- @param oldValue any The old value of the config variable
--- @param value any The new value of the config variable
function CraftPresence:PrintChangedValue(fieldName, oldValue, value)
    if oldValue ~= value and CraftPresence.CanLogChanges() then
        if oldValue == nil or oldValue == "" then
            oldValue = L["TYPE_NONE"]
        end
        if value == nil or value == "" then
            value = L["TYPE_NONE"]
        end
        CraftPresence:Print(
                string.format(
                        L["VERBOSE_LOG"], string.format(
                                L["DEBUG_VALUE_CHANGED"], fieldName, tostring(oldValue), tostring(value)
                        )
                )
        )
    end
end

--- Prints a formatted message, meant to symbolize an invalid value
--- @param logStyle string The log format to follow
function CraftPresence:PrintInvalidValue(logStyle, ...)
    CraftPresence:Print(string.format(
            L["ERROR_LOG"], string.format(
                    logStyle, ...
            )
    ))
end

--- Updates showMinimapIcon with the specified value
--- @param newValue boolean The new value to change showMinimapIcon to
function CraftPresence:UpdateMinimapSetting(newValue)
    local oldValue = CraftPresence.db.profile.showMinimapIcon
    local isValid = (type(newValue) == "boolean")
    if isValid then
        CraftPresence.db.profile.showMinimapIcon = newValue
        CraftPresence:UpdateMinimapState(true)
        CraftPresence:PrintChangedValue(L["TITLE_SHOW_MINIMAP_ICON"], oldValue, newValue)
    end
end

--- Retrieves the default settings for the config menu
--- @return table @ DB_DEFAULTS
function CraftPresence:GetDefaults()
    return DB_DEFAULTS
end

--- Retrieves the default schema settings for the config menu
--- @return table @ SCHEMA_DEFAULTS
function CraftPresence:GetSchemaDefaults()
    return SCHEMA_DEFAULTS
end

--- Resets the settings in the config to their defaults
function CraftPresence:ResetDB()
    CraftPresence:Print(L["INFO_RESET_CONFIG"])
    CraftPresence.db:ResetProfile(false, true)
end
