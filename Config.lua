local CraftPresence = LibStub("AceAddon-3.0"):GetAddon("CraftPresence")

local L = LibStub("AceLocale-3.0"):GetLocale("CraftPresence")

-- Lua APIs
local strformat, type, tostring = string.format, type, tostring

-- SCHEMA_DEFAULTS
local SCHEMA_DEFAULTS = {
    global = {
        Characters = {
            ['*'] = {
            },
        },
    },
}

-- PLACEHOLDER_AREAS
local PLACEHOLDER_AREAS = {
    "gameStateMessage", "detailsMessage",
    "largeImageKey", "largeImageMessage",
    "smallImageKey", "smallImageMessage",
    "dungeonPlaceholderMessage", "raidPlaceholderMessage",
    "battlegroundPlaceholderMessage", "arenaPlaceholderMessage",
    "defaultPlaceholderMessage"
}

-- BLOCKED_CHARACTERS
local BLOCKED_CHARACTERS = {}

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
        buttons = {
            primaryButton = {
                label = "",
                url = ""
            },
            secondaryButton = {
                label = "",
                url = ""
            }
        },
        customPlaceholders = {},
        innerPlaceholderKey = L["DEFAULT_INNER_KEY"],
        globalPlaceholderKey = L["DEFAULT_GLOBAL_KEY"]
    },
}

--- Retrieves the option table to be used in the Config Menu
--- @return table @ opts
function CraftPresence:getOptionsTable()
    local profilesGroup = LibStub("AceDBOptions-3.0"):GetOptionsTable(CraftPresence.db)

    local opts = {
        type = "group", childGroups = "tab",
        name = strformat("%s %s", L["ADDON_NAME"], CraftPresence:GetVersion()),
        get = function(info)
            return CraftPresence.db.profile[info[CraftPresence:GetLength(info)]]
        end,
        set = function(info, value)
            CraftPresence.db.profile[info[CraftPresence:GetLength(info)]] = value
        end,
        args = {
            generalOptions = {
                type = "group", order = CraftPresence:GetNextIndex(),
                name = L["CATEGORY_TITLE_GENERAL"], desc = L["CATEGORY_COMMENT_GENERAL"],
                get = function(info)
                    return CraftPresence.db.profile[info[CraftPresence:GetLength(info)]]
                end,
                set = function(info, value)
                    CraftPresence.db.profile[info[CraftPresence:GetLength(info)]] = value
                end,
                args = {
                    clientId = {
                        type = "input", order = CraftPresence:GetNextIndex(), width = 1.25,
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
                    blank1 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                    gameStateMessage = {
                        type = "input", order = CraftPresence:GetNextIndex(), width = 3.0,
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
                    blank2 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                    detailsMessage = {
                        type = "input", order = CraftPresence:GetNextIndex(), width = 3.0,
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
                    blank3 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                    largeImageKey = {
                        type = "input", order = CraftPresence:GetNextIndex(), width = 3.0,
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
                    blank4 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                    largeImageMessage = {
                        type = "input", order = CraftPresence:GetNextIndex(), width = 3.0,
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
                    blank5 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                    smallImageKey = {
                        type = "input", order = CraftPresence:GetNextIndex(), width = 3.0,
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
                    blank6 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                    smallImageMessage = {
                        type = "input", order = CraftPresence:GetNextIndex(), width = 3.0,
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
                    blank7 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                }
            },
            placeholderOptions = {
                type = "group", order = CraftPresence:GetNextIndex(),
                name = L["CATEGORY_TITLE_PLACEHOLDERS"], desc = L["CATEGORY_COMMENT_PLACEHOLDERS"],
                get = function(info)
                    return CraftPresence.db.profile[info[CraftPresence:GetLength(info)]]
                end,
                set = function(info, value)
                    CraftPresence.db.profile[info[CraftPresence:GetLength(info)]] = value
                end,
                args = {
                    dungeonPlaceholderMessage = {
                        type = "input", order = CraftPresence:GetNextIndex(), width = 3.0,
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
                    blank1 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                    raidPlaceholderMessage = {
                        type = "input", order = CraftPresence:GetNextIndex(), width = 3.0,
                        name = L["TITLE_RAID_MESSAGE"],
                        desc = L["COMMENT_RAID_MESSAGE"],
                        usage = L["USAGE_RAID_MESSAGE"],
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
                    blank2 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                    battlegroundPlaceholderMessage = {
                        type = "input", order = CraftPresence:GetNextIndex(), width = 3.0,
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
                    blank3 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                    arenaPlaceholderMessage = {
                        type = "input", order = CraftPresence:GetNextIndex(), width = 3.0,
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
                    blank4 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                    defaultPlaceholderMessage = {
                        type = "input", order = CraftPresence:GetNextIndex(), width = 3.0,
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
                    blank5 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                }
            },
            buttonOptions = {
                type = "group", order = CraftPresence:GetNextIndex(),
                name = L["CATEGORY_TITLE_BUTTONS"], desc = L["CATEGORY_COMMENT_BUTTONS"],
                get = function(info)
                    return CraftPresence.db.profile[info[CraftPresence:GetLength(info)]]
                end,
                set = function(info, value)
                    CraftPresence.db.profile[info[CraftPresence:GetLength(info)]] = value
                end,
                args = CraftPresence:GetPlaceholderArgs("buttons", strformat(
                        L["CATEGORY_TITLE_BUTTONS_EXTENDED"], L["CATEGORY_TITLE_CUSTOM"]),
                        function(a) return strformat(L["CATEGORY_COMMENT_BUTTONS_INFO"], a, (a == 1 and "") or "s") end
                )
            },
            customOptions = {
                type = "group", order = CraftPresence:GetNextIndex(),
                name = L["CATEGORY_TITLE_CUSTOM"], desc = L["CATEGORY_COMMENT_CUSTOM"],
                get = function(info)
                    return CraftPresence.db.profile[info[CraftPresence:GetLength(info)]]
                end,
                set = function(info, value)
                    CraftPresence.db.profile[info[CraftPresence:GetLength(info)]] = value
                end,
                args = CraftPresence:GetPlaceholderArgs("customPlaceholders", strformat(
                        L["CATEGORY_TITLE_CUSTOM_EXTENDED"], L["CATEGORY_TITLE_CUSTOM"]),
                        function(a) return strformat(L["CATEGORY_COMMENT_CUSTOM_INFO"], a, (a == 1 and "") or "s") end
                )
            },
            extraOptions = {
                type = "group", order = CraftPresence:GetNextIndex(),
                name = L["CATEGORY_TITLE_EXTRA"], desc = L["CATEGORY_COMMENT_EXTRA"],
                get = function(info)
                    return CraftPresence.db.profile[info[CraftPresence:GetLength(info)]]
                end,
                set = function(info, value)
                    CraftPresence.db.profile[info[CraftPresence:GetLength(info)]] = value
                end,
                args = {
                    debugMode = {
                        type = "toggle", order = CraftPresence:GetNextIndex(),
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
                    verboseMode = {
                        type = "toggle", order = CraftPresence:GetNextIndex(),
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
                    blank2 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                    showMinimapIcon = {
                        type = "toggle", order = CraftPresence:GetNextIndex(),
                        name = L["TITLE_SHOW_MINIMAP_ICON"],
                        desc = L["COMMENT_SHOW_MINIMAP_ICON"],
                        get = function(_)
                            return CraftPresence:GetFromDb("showMinimapIcon")
                        end,
                        set = function(_, value)
                            CraftPresence:UpdateMinimapSetting(value)
                        end,
                    },
                    queuedPipeline = {
                        type = "toggle", order = CraftPresence:GetNextIndex(),
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
                    blank4 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                    callbackDelay = {
                        type = "range", order = CraftPresence:GetNextIndex(), width = 1.50,
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
                                        strformat(L["ERROR_RANGE_DEFAULT"], L["TITLE_CALLBACK_DELAY"],
                                                L["MINIMUM_CALLBACK_DELAY"], L["MAXIMUM_CALLBACK_DELAY"])
                                )
                            end
                        end,
                    },
                    frameClearDelay = {
                        type = "range", order = CraftPresence:GetNextIndex(), width = 1.50,
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
                                        strformat(L["ERROR_RANGE_DEFAULT"], L["TITLE_FRAME_CLEAR_DELAY"],
                                                L["MINIMUM_FRAME_CLEAR_DELAY"], L["MAXIMUM_FRAME_CLEAR_DELAY"])
                                )
                            end
                        end,
                    },
                    blank6 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                    frameSize = {
                        type = "range", order = CraftPresence:GetNextIndex(), width = 1.50,
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
                                        strformat(L["ERROR_RANGE_DEFAULT"], L["TITLE_FRAME_SIZE"],
                                                L["MINIMUM_FRAME_SIZE"], L["MAXIMUM_FRAME_SIZE"])
                                )
                            end
                        end,
                    },
                    blank7 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                    globalPlaceholderKey = {
                        type = "input", order = CraftPresence:GetNextIndex(), width = 1.0,
                        name = L["TITLE_GLOBAL_PLACEHOLDER_KEY"],
                        desc = L["COMMENT_GLOBAL_PLACEHOLDER_KEY"],
                        usage = L["USAGE_GLOBAL_PLACEHOLDER_KEY"],
                        get = function(_)
                            return CraftPresence:GetFromDb("globalPlaceholderKey")
                        end,
                        set = function(_, value)
                            local oldValue = CraftPresence:GetFromDb("globalPlaceholderKey")
                            local isValid = (
                                    type(value) == "string" and
                                            CraftPresence:GetLength(value) == 1 and
                                            not BLOCKED_CHARACTERS[value]
                            )
                            if isValid and (value ~= CraftPresence.db.profile.innerPlaceholderKey) then
                                CraftPresence.db.profile.globalPlaceholderKey = value
                                CraftPresence:PrintChangedValue(L["TITLE_GLOBAL_PLACEHOLDER_KEY"], oldValue, value)
                                CraftPresence:SetFormats({ value, nil, oldValue, nil }, PLACEHOLDER_AREAS, true, true)
                            else
                                CraftPresence:PrintInvalidValue(L["ERROR_GLOBAL_PLACEHOLDER_KEY"])
                            end
                        end,
                    },
                    innerPlaceholderKey = {
                        type = "input", order = CraftPresence:GetNextIndex(), width = 1.0,
                        name = L["TITLE_INNER_PLACEHOLDER_KEY"],
                        desc = L["COMMENT_INNER_PLACEHOLDER_KEY"],
                        usage = L["USAGE_INNER_PLACEHOLDER_KEY"],
                        get = function(_)
                            return CraftPresence:GetFromDb("innerPlaceholderKey")
                        end,
                        set = function(_, value)
                            local oldValue = CraftPresence:GetFromDb("innerPlaceholderKey")
                            local isValid = (
                                    type(value) == "string" and
                                            CraftPresence:GetLength(value) == 1 and
                                            not BLOCKED_CHARACTERS[value]
                            )
                            if isValid and (value ~= CraftPresence.db.profile.globalPlaceholderKey) then
                                CraftPresence.db.profile.innerPlaceholderKey = value
                                CraftPresence:PrintChangedValue(L["TITLE_INNER_PLACEHOLDER_KEY"], oldValue, value)
                                CraftPresence:SetFormats({ value, nil, oldValue, nil }, PLACEHOLDER_AREAS, true, true)
                            else
                                CraftPresence:PrintInvalidValue(L["ERROR_INNER_PLACEHOLDER_KEY"])
                            end
                        end,
                    },
                    blank8 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "small", name = " "
                    },
                }
            },
            profiles = profilesGroup,
            about = {
                type = "group", order = CraftPresence:GetNextIndex(),
                name = L["CATEGORY_TITLE_ABOUT"], desc = L["CATEGORY_COMMENT_ABOUT"],
                args = {
                    generalText1 = {
                        type = "description", order = CraftPresence:GetNextIndex(), width = "full", fontSize = "medium",
                        name = L["ADDON_INFO_ONE"],
                    },
                    thanksHeader = {
                        order = CraftPresence:GetNextIndex(), type = "header", name = L["ADDON_HEADER_CREDITS"]
                    },
                    generalText2 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "medium",
                        name = L["ADDON_INFO_TWO"]
                    },
                    blank1 = {
                        type = "description", order = CraftPresence:GetNextIndex(),
                        fontSize = "small", name = "", width = "full"
                    },
                    generalText3 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "medium",
                        name = L["ADDON_INFO_THREE"]
                    },
                    blank2 = {
                        type = "description", order = CraftPresence:GetNextIndex(),
                        fontSize = "small", name = "", width = "full"
                    },
                    generalText4 = {
                        type = "description", order = CraftPresence:GetNextIndex(), fontSize = "medium",
                        name = L["ADDON_INFO_FOUR"]
                    }
                }
            },
        },
    }
    CraftPresence:ResetIndex()
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
        if CraftPresence:IsNullOrEmpty(oldValue) then
            oldValue = L["TYPE_NONE"]
        end
        if CraftPresence:IsNullOrEmpty(value) then
            value = L["TYPE_NONE"]
        end
        CraftPresence:Print(
                strformat(
                        L["VERBOSE_LOG"], strformat(
                                L["DEBUG_VALUE_CHANGED"], fieldName, tostring(oldValue), tostring(value)
                        )
                )
        )
    end
end

--- Prints a formatted message, meant to symbolize an invalid value
--- @param logStyle string The log format to follow
function CraftPresence:PrintInvalidValue(logStyle)
    CraftPresence:Print(strformat(L["ERROR_LOG"], logStyle))
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
