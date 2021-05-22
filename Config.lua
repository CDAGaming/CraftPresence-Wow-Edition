-- Lua APIs
local strformat, type, tostring = string.format, type, tostring

-- Addon APIs
local L = CraftPresence.locale
local config_registry = LibStub("AceConfigRegistry-3.0")

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
        globalPlaceholderKey = L["DEFAULT_GLOBAL_KEY"],
        events = {
            ["PLAYER_LOGIN"] = {
                minimumTOC = "", maximumTOC = "",
                ignoreCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["PLAYER_LEVEL_UP"] = {
                minimumTOC = "", maximumTOC = "",
                ignoreCallback = ("function (self, lastName, name, args) return lastName == 'PLAYER_LEVEL_CHANGED' end"),
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["PLAYER_LEVEL_CHANGED"] = {
                minimumTOC = "80000", maximumTOC = "",
                ignoreCallback = ("function (self, lastName, name, args) return lastName == 'PLAYER_LEVEL_UP' end"),
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["PLAYER_ALIVE"] = {
                minimumTOC = "", maximumTOC = "",
                ignoreCallback = ("function (self, lastName, name, args) return lastName == 'PLAYER_DEAD' end"),
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["PLAYER_DEAD"] = {
                minimumTOC = "", maximumTOC = "",
                ignoreCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["PLAYER_FLAGS_CHANGED"] = {
                minimumTOC = "", maximumTOC = "",
                ignoreCallback = [[
function (self, lastName, name, args)
    return args[1] ~= 'player' or self:GetLastPlayerStatus() == self:GetPlayerStatus()
end
                ]],
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["ZONE_CHANGED"] = {
                minimumTOC = "", maximumTOC = "",
                ignoreCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["ZONE_CHANGED_NEW_AREA"] = {
                minimumTOC = "", maximumTOC = "",
                ignoreCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["ZONE_CHANGED_INDOORS"] = {
                minimumTOC = "", maximumTOC = "",
                ignoreCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["PLAYER_SPECIALIZATION_CHANGED"] = {
                minimumTOC = "50000", maximumTOC = "",
                ignoreCallback = "function (self, lastName, name, args) return args[1] ~= 'player' end",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["ACTIVE_TALENT_GROUP_CHANGED"] = {
                minimumTOC = "60000", maximumTOC = "",
                ignoreCallback = "function (self, lastName, name, args) return args[1] == args[2] end",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["ENCOUNTER_END"] = {
                minimumTOC = "60000", maximumTOC = "",
                ignoreCallback = [[
function (self, lastName, name, args)
    return (not IsInInstance() or args[5] ~= 1 or self:GetCachedLockout() == self:GetCurrentLockoutData(false))
end
                ]],
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["CHALLENGE_MODE_START"] = {
                minimumTOC = "60000", maximumTOC = "",
                ignoreCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["CHALLENGE_MODE_COMPLETED"] = {
                minimumTOC = "60000", maximumTOC = "",
                ignoreCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["CHALLENGE_MODE_RESET"] = {
                minimumTOC = "60000", maximumTOC = "",
                ignoreCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["SCENARIO_COMPLETED"] = {
                minimumTOC = "60000", maximumTOC = "",
                ignoreCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["CRITERIA_COMPLETE"] = {
                minimumTOC = "60000", maximumTOC = "",
                ignoreCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
        }
    },
}

--- Retrieves the option table to be used in the Config Menu
--- @return table @ opts
function CraftPresence:getOptionsTable()
    local self = CraftPresence
    local profilesGroup = LibStub("AceDBOptions-3.0"):GetOptionsTable(self.db)

    local opts = {
        type = "group", childGroups = "tab",
        name = self:GetVersionString(),
        get = function(info)
            return self.db.profile[info[self:GetLength(info)]]
        end,
        set = function(info, value)
            self.db.profile[info[self:GetLength(info)]] = value
        end,
        args = {
            generalOptions = {
                type = "group", order = self:GetNextIndex(),
                name = L["CATEGORY_TITLE_GENERAL"], desc = L["CATEGORY_COMMENT_GENERAL"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = {
                    clientId = {
                        type = "input", order = self:GetNextIndex(), width = 1.25,
                        name = L["TITLE_CLIENT_ID"],
                        desc = L["COMMENT_CLIENT_ID"],
                        usage = L["USAGE_CLIENT_ID"],
                        get = function(_)
                            return self:GetFromDb("clientId")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("clientId")
                            local isValid = (
                                    value ~= nil and
                                            self:ContainsDigit(value) and
                                            self:GetLength(value) == 18
                            )
                            if isValid then
                                self.db.profile.clientId = value
                                self:PrintChangedValue(L["TITLE_CLIENT_ID"], oldValue, value)
                            else
                                self:PrintInvalidValue(L["ERROR_CLIENT_ID"])
                            end
                        end,
                    },
                    blank1 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                    gameStateMessage = {
                        type = "input", order = self:GetNextIndex(), width = 3.0,
                        name = L["TITLE_GAME_STATE_MESSAGE"],
                        desc = L["COMMENT_GAME_STATE_MESSAGE"],
                        usage = L["USAGE_GAME_STATE_MESSAGE"],
                        get = function(_)
                            return self:GetFromDb("gameStateMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("gameStateMessage")
                            local isValid = (type(value) == "string")
                            if isValid then
                                self.db.profile.gameStateMessage = value
                                self:PrintChangedValue(L["TITLE_GAME_STATE_MESSAGE"], oldValue, value)
                            end
                        end,
                    },
                    blank2 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                    detailsMessage = {
                        type = "input", order = self:GetNextIndex(), width = 3.0,
                        name = L["TITLE_DETAILS_MESSAGE"],
                        desc = L["COMMENT_DETAILS_MESSAGE"],
                        usage = L["USAGE_DETAILS_MESSAGE"],
                        get = function(_)
                            return self:GetFromDb("detailsMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("detailsMessage")
                            local isValid = (type(value) == "string")
                            if isValid then
                                self.db.profile.detailsMessage = value
                                self:PrintChangedValue(L["TITLE_DETAILS_MESSAGE"], oldValue, value)
                            end
                        end,
                    },
                    blank3 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                    largeImageKey = {
                        type = "input", order = self:GetNextIndex(), width = 3.0,
                        name = L["TITLE_LARGE_IMAGE_KEY"],
                        desc = L["COMMENT_LARGE_IMAGE_KEY"],
                        usage = L["USAGE_LARGE_IMAGE_KEY"],
                        get = function(_)
                            return self:GetFromDb("largeImageKey")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("largeImageKey")
                            local isValid = (type(value) == "string")
                            if isValid then
                                self.db.profile.largeImageKey = value
                                self:PrintChangedValue(L["TITLE_LARGE_IMAGE_KEY"], oldValue, value)
                            end
                        end,
                    },
                    blank4 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                    largeImageMessage = {
                        type = "input", order = self:GetNextIndex(), width = 3.0,
                        name = L["TITLE_LARGE_IMAGE_MESSAGE"],
                        desc = L["COMMENT_LARGE_IMAGE_MESSAGE"],
                        usage = L["USAGE_LARGE_IMAGE_MESSAGE"],
                        get = function(_)
                            return self:GetFromDb("largeImageMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("largeImageMessage")
                            local isValid = (type(value) == "string")
                            if isValid then
                                self.db.profile.largeImageMessage = value
                                self:PrintChangedValue(L["TITLE_LARGE_IMAGE_MESSAGE"], oldValue, value)
                            end
                        end,
                    },
                    blank5 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                    smallImageKey = {
                        type = "input", order = self:GetNextIndex(), width = 3.0,
                        name = L["TITLE_SMALL_IMAGE_KEY"],
                        desc = L["COMMENT_SMALL_IMAGE_KEY"],
                        usage = L["USAGE_SMALL_IMAGE_KEY"],
                        get = function(_)
                            return self:GetFromDb("smallImageKey")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("smallImageKey")
                            local isValid = (type(value) == "string")
                            if isValid then
                                self.db.profile.smallImageKey = value
                                self:PrintChangedValue(L["TITLE_SMALL_IMAGE_KEY"], oldValue, value)
                            end
                        end,
                    },
                    blank6 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                    smallImageMessage = {
                        type = "input", order = self:GetNextIndex(), width = 3.0,
                        name = L["TITLE_SMALL_IMAGE_MESSAGE"],
                        desc = L["COMMENT_SMALL_IMAGE_MESSAGE"],
                        usage = L["USAGE_SMALL_IMAGE_MESSAGE"],
                        get = function(_)
                            return self:GetFromDb("smallImageMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("smallImageMessage")
                            local isValid = (type(value) == "string")
                            if isValid then
                                self.db.profile.smallImageMessage = value
                                self:PrintChangedValue(L["TITLE_SMALL_IMAGE_MESSAGE"], oldValue, value)
                            end
                        end,
                    },
                    blank7 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                }
            },
            placeholderOptions = {
                type = "group", order = self:GetNextIndex(),
                name = L["CATEGORY_TITLE_PLACEHOLDERS"], desc = L["CATEGORY_COMMENT_PLACEHOLDERS"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = {
                    dungeonPlaceholderMessage = {
                        type = "input", order = self:GetNextIndex(), width = 3.0,
                        name = L["TITLE_DUNGEON_MESSAGE"],
                        desc = L["COMMENT_DUNGEON_MESSAGE"],
                        usage = L["USAGE_DUNGEON_MESSAGE"],
                        get = function(_)
                            return self:GetFromDb("dungeonPlaceholderMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("dungeonPlaceholderMessage")
                            local isValid = (type(value) == "string")
                            if isValid then
                                self.db.profile.dungeonPlaceholderMessage = value
                                self:PrintChangedValue(L["TITLE_DUNGEON_MESSAGE"], oldValue, value)
                            end
                        end,
                    },
                    blank1 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                    raidPlaceholderMessage = {
                        type = "input", order = self:GetNextIndex(), width = 3.0,
                        name = L["TITLE_RAID_MESSAGE"],
                        desc = L["COMMENT_RAID_MESSAGE"],
                        usage = L["USAGE_RAID_MESSAGE"],
                        get = function(_)
                            return self:GetFromDb("raidPlaceholderMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("raidPlaceholderMessage")
                            local isValid = (type(value) == "string")
                            if isValid then
                                self.db.profile.raidPlaceholderMessage = value
                                self:PrintChangedValue(L["TITLE_RAID_MESSAGE"], oldValue, value)
                            end
                        end,
                    },
                    blank2 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                    battlegroundPlaceholderMessage = {
                        type = "input", order = self:GetNextIndex(), width = 3.0,
                        name = L["TITLE_BATTLEGROUND_MESSAGE"],
                        desc = L["COMMENT_BATTLEGROUND_MESSAGE"],
                        usage = L["USAGE_BATTLEGROUND_MESSAGE"],
                        get = function(_)
                            return self:GetFromDb("battlegroundPlaceholderMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("battlegroundPlaceholderMessage")
                            local isValid = (type(value) == "string")
                            if isValid then
                                self.db.profile.battlegroundPlaceholderMessage = value
                                self:PrintChangedValue(L["TITLE_BATTLEGROUND_MESSAGE"], oldValue, value)
                            end
                        end,
                    },
                    blank3 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                    arenaPlaceholderMessage = {
                        type = "input", order = self:GetNextIndex(), width = 3.0,
                        name = L["TITLE_ARENA_MESSAGE"],
                        desc = L["COMMENT_ARENA_MESSAGE"],
                        usage = L["USAGE_ARENA_MESSAGE"],
                        get = function(_)
                            return self:GetFromDb("arenaPlaceholderMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("arenaPlaceholderMessage")
                            local isValid = (type(value) == "string")
                            if isValid then
                                self.db.profile.arenaPlaceholderMessage = value
                                self:PrintChangedValue(L["TITLE_ARENA_MESSAGE"], oldValue, value)
                            end
                        end,
                    },
                    blank4 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                    defaultPlaceholderMessage = {
                        type = "input", order = self:GetNextIndex(), width = 3.0,
                        name = L["TITLE_FALLBACK_MESSAGE"],
                        desc = L["COMMENT_FALLBACK_MESSAGE"],
                        usage = L["USAGE_FALLBACK_MESSAGE"],
                        get = function(_)
                            return self:GetFromDb("defaultPlaceholderMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("defaultPlaceholderMessage")
                            local isValid = (type(value) == "string")
                            if isValid then
                                self.db.profile.defaultPlaceholderMessage = value
                                self:PrintChangedValue(L["TITLE_FALLBACK_MESSAGE"], oldValue, value)
                            end
                        end,
                    },
                    blank5 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                }
            },
            buttonOptions = {
                type = "group", order = self:GetNextIndex(),
                name = L["CATEGORY_TITLE_BUTTONS"], desc = L["CATEGORY_COMMENT_BUTTONS"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = self:GetPlaceholderArgs("buttons", strformat(
                        L["CATEGORY_TITLE_BUTTONS_EXTENDED"], L["CATEGORY_TITLE_CUSTOM"]),
                        function(count)
                            return strformat(L["CATEGORY_COMMENT_BUTTONS_INFO"], count, (count == 1 and "") or "s")
                        end
                )
            },
            customOptions = {
                type = "group", order = self:GetNextIndex(),
                name = L["CATEGORY_TITLE_CUSTOM"], desc = L["CATEGORY_COMMENT_CUSTOM"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = self:GetPlaceholderArgs("customPlaceholders", strformat(
                        L["CATEGORY_TITLE_CUSTOM_EXTENDED"], L["CATEGORY_TITLE_CUSTOM"]),
                        function(count)
                            return strformat(L["CATEGORY_COMMENT_CUSTOM_INFO"], count, (count == 1 and "") or "s")
                        end
                )
            },
            eventOptions = {
                type = "group", order = self:GetNextIndex(),
                name = L["CATEGORY_TITLE_EVENTS"], desc = L["CATEGORY_COMMENT_EVENTS"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = self:GetPlaceholderArgs("events", L["CATEGORY_TITLE_EVENTS_EXTENDED"],
                        function(count)
                            return strformat(L["CATEGORY_COMMENT_EVENTS_INFO"], count, (count == 1 and "") or "s")
                        end,
                        function(root)
                            root:SyncEvents(root:GetFromDb("events"), root:GetFromDb("debugMode"))
                        end
                )
            },
            extraOptions = {
                type = "group", order = self:GetNextIndex(),
                name = L["CATEGORY_TITLE_EXTRA"], desc = L["CATEGORY_COMMENT_EXTRA"],
                get = function(info)
                    return self.db.profile[info[self:GetLength(info)]]
                end,
                set = function(info, value)
                    self.db.profile[info[self:GetLength(info)]] = value
                end,
                args = {
                    debugMode = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = L["TITLE_DEBUG_MODE"],
                        desc = L["COMMENT_DEBUG_MODE"],
                        get = function(_)
                            return self:GetFromDb("debugMode")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("debugMode")
                            local isValid = (type(value) == "boolean")
                            if isValid then
                                self.db.profile.debugMode = value
                                self:PrintChangedValue(L["TITLE_DEBUG_MODE"], oldValue, value)
                            end
                        end,
                    },
                    verboseMode = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = L["TITLE_VERBOSE_MODE"],
                        desc = L["COMMENT_VERBOSE_MODE"],
                        get = function(_)
                            return self:GetFromDb("verboseMode")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("verboseMode")
                            local isValid = (type(value) == "boolean")
                            if isValid then
                                self.db.profile.verboseMode = value
                                self:PrintChangedValue(L["TITLE_VERBOSE_MODE"], oldValue, value)
                            end
                        end,
                    },
                    blank2 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                    showMinimapIcon = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = L["TITLE_SHOW_MINIMAP_ICON"],
                        desc = L["COMMENT_SHOW_MINIMAP_ICON"],
                        get = function(_)
                            return self:GetFromDb("showMinimapIcon")
                        end,
                        set = function(_, value)
                            self:UpdateMinimapSetting(value)
                        end,
                    },
                    queuedPipeline = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = L["TITLE_QUEUED_PIPELINE"],
                        desc = L["COMMENT_QUEUED_PIPELINE"],
                        get = function(_)
                            return self:GetFromDb("queuedPipeline")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("queuedPipeline")
                            local isValid = (type(value) == "boolean")
                            if isValid then
                                self.db.profile.queuedPipeline = value
                                self:PrintChangedValue(L["TITLE_QUEUED_PIPELINE"], oldValue, value)
                            end
                        end,
                    },
                    blank4 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                    callbackDelay = {
                        type = "range", order = self:GetNextIndex(), width = 1.50,
                        min = L["MINIMUM_CALLBACK_DELAY"], max = L["MAXIMUM_CALLBACK_DELAY"], step = 1,
                        name = L["TITLE_CALLBACK_DELAY"],
                        desc = L["COMMENT_CALLBACK_DELAY"],
                        get = function(_)
                            return self:GetFromDb("callbackDelay")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("callbackDelay")
                            local isValid = (self:IsWithinValue(
                                    value,
                                    L["MINIMUM_CALLBACK_DELAY"], L["MAXIMUM_CALLBACK_DELAY"],
                                    true, true
                            ))
                            if isValid then
                                self.db.profile.callbackDelay = value
                                self:PrintChangedValue(L["TITLE_CALLBACK_DELAY"], oldValue, value)
                            else
                                self:PrintInvalidValue(
                                        strformat(L["ERROR_RANGE_DEFAULT"], L["TITLE_CALLBACK_DELAY"],
                                                L["MINIMUM_CALLBACK_DELAY"], L["MAXIMUM_CALLBACK_DELAY"])
                                )
                            end
                        end,
                    },
                    frameClearDelay = {
                        type = "range", order = self:GetNextIndex(), width = 1.50,
                        min = L["MINIMUM_FRAME_CLEAR_DELAY"], max = L["MAXIMUM_FRAME_CLEAR_DELAY"], step = 1,
                        name = L["TITLE_FRAME_CLEAR_DELAY"],
                        desc = L["COMMENT_FRAME_CLEAR_DELAY"],
                        get = function(_)
                            return self:GetFromDb("frameClearDelay")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("frameClearDelay")
                            local isValid = (self:IsWithinValue(
                                    value,
                                    L["MINIMUM_FRAME_CLEAR_DELAY"], L["MAXIMUM_FRAME_CLEAR_DELAY"],
                                    true, true
                            ))
                            if isValid then
                                self.db.profile.frameClearDelay = value
                                self:PrintChangedValue(L["TITLE_FRAME_CLEAR_DELAY"], oldValue, value)
                            else
                                self:PrintInvalidValue(
                                        strformat(L["ERROR_RANGE_DEFAULT"], L["TITLE_FRAME_CLEAR_DELAY"],
                                                L["MINIMUM_FRAME_CLEAR_DELAY"], L["MAXIMUM_FRAME_CLEAR_DELAY"])
                                )
                            end
                        end,
                    },
                    blank6 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                    frameSize = {
                        type = "range", order = self:GetNextIndex(), width = 1.50,
                        min = L["MINIMUM_FRAME_SIZE"], max = L["MAXIMUM_FRAME_SIZE"], step = 1,
                        name = L["TITLE_FRAME_SIZE"],
                        desc = L["COMMENT_FRAME_SIZE"],
                        get = function(_)
                            return self:GetFromDb("frameSize")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("frameSize")
                            local isValid = (self:IsWithinValue(
                                    value,
                                    L["MINIMUM_FRAME_SIZE"], L["MAXIMUM_FRAME_SIZE"],
                                    true, true
                            ))
                            if isValid then
                                self.db.profile.frameSize = value
                                self:PrintChangedValue(L["TITLE_FRAME_SIZE"], oldValue, value)
                            else
                                self:PrintInvalidValue(
                                        strformat(L["ERROR_RANGE_DEFAULT"], L["TITLE_FRAME_SIZE"],
                                                L["MINIMUM_FRAME_SIZE"], L["MAXIMUM_FRAME_SIZE"])
                                )
                            end
                        end,
                    },
                    blank7 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                    globalPlaceholderKey = {
                        type = "input", order = self:GetNextIndex(), width = 1.0,
                        name = L["TITLE_GLOBAL_PLACEHOLDER_KEY"],
                        desc = L["COMMENT_GLOBAL_PLACEHOLDER_KEY"],
                        usage = L["USAGE_GLOBAL_PLACEHOLDER_KEY"],
                        get = function(_)
                            return self:GetFromDb("globalPlaceholderKey")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("globalPlaceholderKey")
                            local isValid = (
                                    type(value) == "string" and
                                            self:GetLength(value) == 1 and
                                            not BLOCKED_CHARACTERS[value]
                            )
                            if isValid and (value ~= self.db.profile.innerPlaceholderKey) then
                                self.db.profile.globalPlaceholderKey = value
                                self:PrintChangedValue(L["TITLE_GLOBAL_PLACEHOLDER_KEY"], oldValue, value)
                                self:SetFormats({ value, nil, oldValue, nil }, PLACEHOLDER_AREAS, true, true)
                            else
                                self:PrintInvalidValue(L["ERROR_GLOBAL_PLACEHOLDER_KEY"])
                            end
                        end,
                    },
                    innerPlaceholderKey = {
                        type = "input", order = self:GetNextIndex(), width = 1.0,
                        name = L["TITLE_INNER_PLACEHOLDER_KEY"],
                        desc = L["COMMENT_INNER_PLACEHOLDER_KEY"],
                        usage = L["USAGE_INNER_PLACEHOLDER_KEY"],
                        get = function(_)
                            return self:GetFromDb("innerPlaceholderKey")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("innerPlaceholderKey")
                            local isValid = (
                                    type(value) == "string" and
                                            self:GetLength(value) == 1 and
                                            not BLOCKED_CHARACTERS[value]
                            )
                            if isValid and (value ~= self.db.profile.globalPlaceholderKey) then
                                self.db.profile.innerPlaceholderKey = value
                                self:PrintChangedValue(L["TITLE_INNER_PLACEHOLDER_KEY"], oldValue, value)
                                self:SetFormats({ value, nil, oldValue, nil }, PLACEHOLDER_AREAS, true, true)
                            else
                                self:PrintInvalidValue(L["ERROR_INNER_PLACEHOLDER_KEY"])
                            end
                        end,
                    },
                    blank8 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                }
            },
            profiles = profilesGroup,
            about = {
                type = "group", order = self:GetNextIndex(),
                name = L["CATEGORY_TITLE_ABOUT"], desc = L["CATEGORY_COMMENT_ABOUT"],
                args = {
                    generalText1 = {
                        type = "description", order = self:GetNextIndex(), width = "full", fontSize = "medium",
                        name = L["ADDON_INFO_ONE"],
                    },
                    thanksHeader = {
                        order = self:GetNextIndex(), type = "header", name = L["ADDON_HEADER_CREDITS"]
                    },
                    generalText2 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "medium",
                        name = L["ADDON_INFO_TWO"]
                    },
                    blank1 = {
                        type = "description", order = self:GetNextIndex(),
                        fontSize = "small", name = "", width = "full"
                    },
                    generalText3 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "medium",
                        name = L["ADDON_INFO_THREE"]
                    },
                    blank2 = {
                        type = "description", order = self:GetNextIndex(),
                        fontSize = "small", name = "", width = "full"
                    },
                    generalText4 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "medium",
                        name = L["ADDON_INFO_FOUR"]
                    }
                }
            },
        },
    }
    self:ResetIndex()
    return opts
end

--- Retrieves whether or not logging changed data is allowed
--- @return boolean @ canLogChanges
function CraftPresence:CanLogChanges()
    return self:GetFromDb("verboseMode")
end

--- Prints change data, if possible, using the specified parameters
---
--- @param fieldName string The config name the change belongs to
--- @param oldValue any The old value of the config variable
--- @param value any The new value of the config variable
function CraftPresence:PrintChangedValue(fieldName, oldValue, value)
    oldValue = self:GetOrDefault(oldValue, L["TYPE_NONE"])
    value = self:GetOrDefault(value, L["TYPE_NONE"])
    if oldValue ~= value and self:CanLogChanges() then
        self:Print(
                strformat(
                        L["LOG_VERBOSE"], strformat(
                                L["DEBUG_VALUE_CHANGED"], fieldName, tostring(oldValue), tostring(value)
                        )
                )
        )
    end
end

--- Prints a formatted message, meant to symbolize an invalid value
--- @param logStyle string The log format to follow
function CraftPresence:PrintInvalidValue(logStyle)
    self:Print(strformat(L["LOG_ERROR"], logStyle))
end

--- Updates showMinimapIcon with the specified value
--- @param newValue boolean The new value to change showMinimapIcon to
function CraftPresence:UpdateMinimapSetting(newValue)
    local oldValue = self.db.profile.showMinimapIcon
    local isValid = (type(newValue) == "boolean")
    if isValid then
        self.db.profile.showMinimapIcon = newValue
        self:UpdateMinimapState(true)
        self:PrintChangedValue(L["TITLE_SHOW_MINIMAP_ICON"], oldValue, newValue)
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

--- Resets all settings in the currently active config profile to their defaults
---
--- @param notify boolean Whether or not to fire NotifyChange after operation (Default: false)
function CraftPresence:ResetProfile(notify)
    self:Print(L["INFO_RESET_CONFIG"])
    self.db:ResetProfile(false, true)
    if notify then
        config_registry:NotifyChange(L["ADDON_NAME"])
    end
end
