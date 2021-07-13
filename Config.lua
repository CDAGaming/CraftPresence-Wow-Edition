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
local pairs, type, tostring = pairs, type, tostring
local strformat, strlower = string.format, string.lower

-- Addon APIs
local LibStub = LibStub

local L = CraftPresence.locale
local config_registry = LibStub("AceConfigRegistry-3.0")

-- DB_DEFAULTS
local DB_DEFAULTS = {
    profile = {
        schema = 0,
        clientId = L["DEFAULT_CLIENT_ID"],
        gameStateMessage = L["DEFAULT_GAME_STATE_MESSAGE"],
        detailsMessage = L["DEFAULT_DETAILS_MESSAGE"],
        largeImageKey = L["DEFAULT_LARGE_IMAGE_KEY"],
        largeImageMessage = L["DEFAULT_LARGE_IMAGE_MESSAGE"],
        smallImageKey = L["DEFAULT_SMALL_IMAGE_KEY"],
        smallImageMessage = L["DEFAULT_SMALL_IMAGE_MESSAGE"],
        debugMode = false,
        verboseMode = false,
        showMinimapIcon = true,
        queuedPipeline = false,
        showWelcomeMessage = true,
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
        placeholders = {
            ["default"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = L["DEFAULT_FALLBACK_MESSAGE"],
                processType = "string",
                registerCallback = [[function (self)
    return GetInstanceInfo == nil or select(2, GetInstanceInfo()) == 'none' or (
        self:IsNullOrEmpty(select(2, GetInstanceInfo()))
    )
end]],
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_GLOBAL_KEY"]
            },
            ["arena"] = {
                minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                processCallback = L["DEFAULT_ARENA_MESSAGE"],
                processType = "string",
                registerCallback = [[function (self)
    return select(2, GetInstanceInfo()) == 'arena'
end]],
                tagCallback = "time:start",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_GLOBAL_KEY"]
            },
            ["battleground"] = {
                minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                processCallback = L["DEFAULT_BATTLEGROUND_MESSAGE"],
                processType = "string",
                registerCallback = [[function (self)
    return select(2, GetInstanceInfo()) == 'pvp'
end]],
                tagCallback = "time:start",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_GLOBAL_KEY"]
            },
            ["raid"] = {
                minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                processCallback = L["DEFAULT_RAID_MESSAGE"],
                processType = "string",
                registerCallback = [[function (self)
    return select(2, GetInstanceInfo()) == 'raid'
end]],
                tagCallback = "time:start",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_GLOBAL_KEY"]
            },
            ["dungeon"] = {
                minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                processCallback = L["DEFAULT_DUNGEON_MESSAGE"],
                processType = "string",
                registerCallback = [[function (self)
    return select(2, GetInstanceInfo()) == 'party' and (
        not self:FindMatches(select(1, GetInstanceInfo()), 'Garrison', false)
    )
end]],
                tagCallback = "time:start",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_GLOBAL_KEY"]
            },
            ["scenario"] = {
                minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                processCallback = L["DEFAULT_SCENARIO_MESSAGE"],
                processType = "string",
                registerCallback = [[function (self)
    return select(2, GetInstanceInfo()) == 'scenario'
end]],
                tagCallback = "time:start",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_GLOBAL_KEY"]
            },
            ["player_name"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return UnitName('player')
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["title_name"] = {
                minimumTOC = "50000", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return UnitPVPName('player')
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["player_level"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return UnitLevel('player')
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["player_realm"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return GetRealmName()
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["player_region"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    local realmData = { "US", "KR", "EU", "TW", "CH" }
    local playerRegionId = 1
    if GetCurrentRegion then
        playerRegionId = GetCurrentRegion()
    end
    return realmData[playerRegionId]
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["player_class"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return UnitClass('player')
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["player_status"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return self:GetUnitStatus("player", true).status
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["player_reason"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return self:GetUnitStatus("player", true).reason
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["difficulty_info"] = {
                minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    local difficultyInfo = select(4, GetInstanceInfo())
    -- Keystone Data
    local activeKeystoneData = self:GetActiveKeystone()
    if (activeKeystoneData ~= nil and not self:IsNullOrEmpty(activeKeystoneData.formattedLevel)) then
        difficultyInfo = (difficultyInfo .. " (" .. activeKeystoneData.formattedLevel .. ")")
    end
    return difficultyInfo
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["player_faction"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    local englishFaction, localizedFaction = UnitFactionGroup('player')
    englishFaction = self:GetOrDefault(englishFaction, self.locale['TYPE_NONE'])
    localizedFaction = self:GetOrDefault(localizedFaction, englishFaction)
    return localizedFaction
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["player_alliance"] = {
                minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    -- Covenant and Faction Setup
    -- Retail: If not in a covenant, or cannot identify that this instance belongs to Shadowlands
    -- Then use the Faction as the Alliance; otherwise use Covenant Data
    local name = select(1, GetInstanceInfo())
    local englishFaction, localizedFaction = UnitFactionGroup('player')
    englishFaction = self:GetOrDefault(englishFaction, self.locale['TYPE_NONE'])
    localizedFaction = self:GetOrDefault(localizedFaction, englishFaction)
    local playerCovenantId, playerCovenantData = 0
    if C_Covenants ~= nil then
        playerCovenantId = C_Covenants.GetActiveCovenantID()
        playerCovenantData = C_Covenants.GetCovenantData(playerCovenantId)
    end
    -- Covenant and/or Faction data is only updated if the instance is changed
    if (playerCovenantId == 0 or not (
            (self:FindMatches(name, 'Shadowlands', false, 1, false)) or
                    (self:FindMatches(name, 'Torghast', false, 1, false)) or
                    (self:FindMatches(self:GetCurrentInstanceTier(), 'Shadowlands', false, 1, false))
    )) then
        return localizedFaction
    else
        return playerCovenantData.name
    end
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["player_covenant"] = {
                minimumTOC = "90000", maximumTOC = "", allowRebasedApi = false,
                processCallback = [[function (self)
    local covenantId = C_Covenants.GetActiveCovenantID()
    if covenantId == 0 then
        return self.locale['TYPE_NONE']
    else
        return C_Covenants.GetCovenantData(covenantId).name
    end
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["player_info"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    -- Player Data
    local playerName = UnitName('player')
    local playerData = self:GetUnitStatus('player', true)
    -- Extra Player Data
    local unitLevel = UnitLevel('player')
    local unitClass = UnitClass('player')
    local userInfo = playerData.prefix .. playerName .. ' - ' .. (string.format(self.locale['FORMAT_LEVEL'], unitLevel))
    -- Specialization Info
    if GetSpecialization then
        local specInfo, specId, specName, roleName = GetSpecialization()
        --
        -- Hotfix: Prevent a null-case with Spec Info
        --
        -- This only happens if events fire too quickly for into to populate,
        -- or if you don't have a spec learned or available.
        if specInfo ~= nil then
            specId, specName = GetSpecializationInfo(specInfo)
            if specId ~= nil then
                roleName = self:FormatWord(GetSpecializationRoleByID(specId))
            else
                specName = self.locale["TYPE_NONE"]
            end
        end
        --
        -- Trim and Adjust User Data
        if not self:IsNullOrEmpty(specName) then
            userInfo = (userInfo .. ' ' .. specName)
        end
    end
    -- Final Parsing
    if not self:IsNullOrEmpty(unitClass) then
            userInfo = (userInfo .. ' ' .. unitClass)
    end
    return userInfo
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["player_spec_name"] = {
                minimumTOC = "50000", maximumTOC = "", allowRebasedApi = false,
                processCallback = [[function (self)
    local specInfo, specId, specName, roleName = GetSpecialization()
    --
    -- Hotfix: Prevent a null-case with Spec Info
    --
    -- This only happens if events fire too quickly for into to populate,
    -- or if you don't have a spec learned or available.
    if specInfo ~= nil then
        specId, specName = GetSpecializationInfo(GetSpecialization())
        if specId ~= nil then
            roleName = self:FormatWord(GetSpecializationRoleByID(specId))
        else
            specName = self.locale["TYPE_NONE"]
        end
    end
    return specName
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["player_spec_role"] = {
                minimumTOC = "50000", maximumTOC = "", allowRebasedApi = false,
                processCallback = [[function (self)
    local specInfo, specId, specName, roleName = GetSpecialization()
    --
    -- Hotfix: Prevent a null-case with Spec Info
    --
    -- This only happens if events fire too quickly for into to populate,
    -- or if you don't have a spec learned or available.
    if specInfo ~= nil then
        specId, specName = GetSpecializationInfo(GetSpecialization())
        if specId ~= nil then
            roleName = self:FormatWord(GetSpecializationRoleByID(specId))
        else
            specName = self.locale["TYPE_NONE"]
        end
    end
    return roleName
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["realm_info"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    -- Get Player Info
    local realmData = { "US", "KR", "EU", "TW", "CH" }
    local playerRealm = GetRealmName()
    local playerRegionId = 1
    if GetCurrentRegion then
        playerRegionId = GetCurrentRegion()
    end
    local playerRegion = realmData[playerRegionId]
    return (playerRegion .. ' - ' .. playerRealm)
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["player_covenant_renown"] = {
                minimumTOC = "90000", maximumTOC = "", allowRebasedApi = false,
                processCallback = [[function (self)
    return tostring(C_CovenantSanctumUI.GetRenownLevel())
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["localized_name"] = {
                minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return select(1, GetInstanceInfo())
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["instance_type"] = {
                minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return select(2, GetInstanceInfo())
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["instance_difficulty"] = {
                minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return tostring(select(3, GetInstanceInfo()))
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["difficulty_name"] = {
                minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return select(4, GetInstanceInfo())
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["max_players"] = {
                minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return tostring(select(5, GetInstanceInfo()))
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["dynamic_difficulty"] = {
                minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return tostring(select(6, GetInstanceInfo()))
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["is_dynamic"] = {
                minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return tostring(select(7, GetInstanceInfo()))
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["instance_id"] = {
                minimumTOC = "50000", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return tostring(select(8, GetInstanceInfo()))
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["instance_group_size"] = {
                minimumTOC = "50400", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return tostring(select(9, GetInstanceInfo()))
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["lfg_dungeon_id"] = {
                minimumTOC = "80000", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return tostring(select(10, GetInstanceInfo()))
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["zone_name"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return self:GetOrDefault(GetRealZoneText(), self.locale['TYPE_UNKNOWN'])
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["sub_zone_name"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return self:GetOrDefault(GetSubZoneText(), self.locale["TYPE_UNKNOWN"])
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["zone_info"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    -- Zone Data
    local formatted_zone_info
    local zone_name = GetRealZoneText()
    local sub_name = GetSubZoneText()
    -- Null-Case to ensure Zone Name always equals something
    if self:IsNullOrEmpty(zone_name) then
        zone_name = self.locale['TYPE_UNKNOWN']
    end
    -- Format the zone info based on zone data
    if self:IsNullOrEmpty(sub_name) then
        formatted_zone_info = zone_name
        sub_name = self.locale['TYPE_UNKNOWN']
    else
        formatted_zone_info = (sub_name .. ' - ' .. zone_name)
    end
    return formatted_zone_info
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["item_level"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    local avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = 0, 0, 0
    if GetAverageItemLevel then
        avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = GetAverageItemLevel()
    end
    return string.format("%.2f", avgItemLevel or 0)
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["item_level_equipped"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    local avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = 0, 0, 0
    if GetAverageItemLevel then
        avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = GetAverageItemLevel()
    end
    return string.format("%.2f", avgItemLevelEquipped or 0)
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["item_level_pvp"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    local avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = 0, 0, 0
    if GetAverageItemLevel then
        avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = GetAverageItemLevel()
    end
    return string.format("%.2f", avgItemLevelPvp or 0)
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["active_keystone_level"] = {
                minimumTOC = "50000", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return self:GetActiveKeystone().formattedLevel
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["active_keystone_rating"] = {
                minimumTOC = "50000", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return tostring(self:GetActiveKeystone().rating)
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["active_keystone_affixes"] = {
                minimumTOC = "50000", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return self:GetActiveKeystone().formattedAffixes
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["external_keystone_rating"] = {
                minimumTOC = "50000", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return tostring(self:GetActiveKeystone().external_rating)
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["owned_keystone_level"] = {
                minimumTOC = "50000", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self)
    return self:GetOwnedKeystone().formattedLevel
end]],
                processType = "function",
                registerCallback = "",
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["lockout_encounters"] = {
                minimumTOC = "60000", maximumTOC = "", allowRebasedApi = false,
                processCallback = [[function (self)
    return self:GetCurrentLockoutData(true).formattedEncounterData
end]],
                processType = "function",
                registerCallback = [[function (self)
    local lockoutData = self:GetCurrentLockoutData(true)
    return lockoutData ~= nil and lockoutData.currentEncounters > 0 and lockoutData.totalEncounters > 0
end]],
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["lockout_current_encounters"] = {
                minimumTOC = "60000", maximumTOC = "", allowRebasedApi = false,
                processCallback = [[function (self)
    return self:GetCurrentLockoutData(true).currentEncounters
end]],
                processType = "function",
                registerCallback = [[function (self)
    local lockoutData = self:GetCurrentLockoutData(true)
    return lockoutData ~= nil and lockoutData.currentEncounters > 0 and lockoutData.totalEncounters > 0
end]],
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            },
            ["lockout_total_encounters"] = {
                minimumTOC = "60000", maximumTOC = "", allowRebasedApi = false,
                processCallback = [[function (self)
    return self:GetCurrentLockoutData(true).totalEncounters
end]],
                processType = "function",
                registerCallback = [[function (self)
    local lockoutData = self:GetCurrentLockoutData(true)
    return lockoutData ~= nil and lockoutData.currentEncounters > 0 and lockoutData.totalEncounters > 0
end]],
                tagCallback = "",
                tagType = "string",
                enabled = true, prefix = L["DEFAULT_INNER_KEY"]
            }
        },
        events = {
            ["CHAT_MSG_SYSTEM"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self, _, _, args)
    local splitMessage = self:Split(args[1], ':', false, true)
    local afkFormat = self:Split(MARKED_AFK_MESSAGE, ':', false, true)
    local isAfkStatus = args[1] == CLEARED_AFK or args[1] == MARKED_AFK or self:StartsWith(args[1], afkFormat[1])
    local busyFormat = self:Split(MARKED_DND, ':', false, true)
    local isBusyStatus = args[1] == CLEARED_DND or self:StartsWith(args[1], busyFormat[1])
    if isAfkStatus then
        self:SetCachedUnitData('player', 'away', args[1] ~= CLEARED_AFK and isAfkStatus)
        self:SetCachedUnitData('player', 'reason', splitMessage[2])
    elseif isBusyStatus then
        self:SetCachedUnitData('player', 'busy', args[1] ~= CLEARED_DND and isBusyStatus)
        self:SetCachedUnitData('player', 'reason', splitMessage[2])
    else
        self:SetCachedUnitData('player', 'reason', '')
    end
    return not (isAfkStatus or isBusyStatus)
end]],
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["PLAYER_LOGIN"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["PLAYER_LEVEL_UP"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = ("function (_, lastName, _, _) return lastName == 'PLAYER_LEVEL_CHANGED' end"),
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["PLAYER_LEVEL_CHANGED"] = {
                minimumTOC = "80000", maximumTOC = "", allowRebasedApi = false,
                processCallback = ("function (_, lastName, _, _) return lastName == 'PLAYER_LEVEL_UP' end"),
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["PLAYER_ALIVE"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = ("function (_, lastName, _, _) return lastName == 'PLAYER_DEAD' end"),
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["PLAYER_DEAD"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["PLAYER_FLAGS_CHANGED"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = [[function (self, lastName, _, args)
    local unitData = self:GetUnitData()
    return lastName == 'CHAT_MSG_SYSTEM' or args[1] ~= 'player' or unitData.last_status == unitData.status
end]],
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["ZONE_CHANGED"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["ZONE_CHANGED_NEW_AREA"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["ZONE_CHANGED_INDOORS"] = {
                minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                processCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["PLAYER_SPECIALIZATION_CHANGED"] = {
                minimumTOC = "50000", maximumTOC = "", allowRebasedApi = false,
                processCallback = "function (_, _, _, args) return args[1] ~= 'player' end",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["ACTIVE_TALENT_GROUP_CHANGED"] = {
                minimumTOC = "60000", maximumTOC = "", allowRebasedApi = false,
                processCallback = "function (_, _, _, args) return args[1] == args[2] end",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["ENCOUNTER_END"] = {
                minimumTOC = "60000", maximumTOC = "", allowRebasedApi = false,
                processCallback = [[function (self, _, _, args)
    return (not IsInInstance() or args[5] ~= 1 or self:GetCachedLockout() == self:GetCurrentLockoutData(false))
end]],
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["CHALLENGE_MODE_START"] = {
                minimumTOC = "60000", maximumTOC = "", allowRebasedApi = false,
                processCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["CHALLENGE_MODE_COMPLETED"] = {
                minimumTOC = "60000", maximumTOC = "", allowRebasedApi = false,
                processCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["CHALLENGE_MODE_RESET"] = {
                minimumTOC = "60000", maximumTOC = "", allowRebasedApi = false,
                processCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["SCENARIO_COMPLETED"] = {
                minimumTOC = "60000", maximumTOC = "", allowRebasedApi = false,
                processCallback = "",
                registerCallback = "",
                eventCallback = "function(self) return self.defaultEventCallback end",
                enabled = true
            },
            ["CRITERIA_COMPLETE"] = {
                minimumTOC = "60000", maximumTOC = "", allowRebasedApi = false,
                processCallback = "",
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
    local profilesGroup = LibStub("AceDBOptions-3.0"):GetOptionsTable(self.db)

    local opts = {
        type = "group", childGroups = "tab",
        name = self:GetAddOnInfo()["versionString"],
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
                                self:PrintErrorMessage(L["ERROR_CLIENT_ID"])
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
                        type = "input", order = self:GetNextIndex(), width = 1.50,
                        name = L["TITLE_LARGE_IMAGE_KEY"],
                        desc = L["COMMENT_LARGE_IMAGE_KEY"],
                        usage = L["USAGE_LARGE_IMAGE_KEY"],
                        get = function(_)
                            return self:GetFromDb("largeImageKey")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("largeImageKey")
                            local isValid = (type(value) == "string") and self:GetLength(value) <= 32
                            if isValid then
                                self.db.profile.largeImageKey = value
                                self:PrintChangedValue(L["TITLE_LARGE_IMAGE_KEY"], oldValue, value)
                            else
                                self:PrintErrorMessage(L["ERROR_IMAGE_KEY"])
                            end
                        end,
                    },
                    smallImageKey = {
                        type = "input", order = self:GetNextIndex(), width = 1.50,
                        name = L["TITLE_SMALL_IMAGE_KEY"],
                        desc = L["COMMENT_SMALL_IMAGE_KEY"],
                        usage = L["USAGE_SMALL_IMAGE_KEY"],
                        get = function(_)
                            return self:GetFromDb("smallImageKey")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("smallImageKey")
                            local isValid = (type(value) == "string") and self:GetLength(value) <= 32
                            if isValid then
                                self.db.profile.smallImageKey = value
                                self:PrintChangedValue(L["TITLE_SMALL_IMAGE_KEY"], oldValue, value)
                            else
                                self:PrintErrorMessage(L["ERROR_IMAGE_KEY"])
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
                    blank6 = {
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
                args = self:GetPlaceholderArgs("buttons", L["CATEGORY_TITLE_BUTTONS_EXTENDED"],
                        function(count)
                            return strformat(L["CATEGORY_COMMENT_BUTTONS_INFO"], count, (count == 1 and "") or "s")
                        end,
                        nil,
                        function(self, newValue)
                            return not self:FindMatches(newValue, L["ARRAY_SPLIT_KEY"], false)
                        end,
                        function(self, fieldName, _, _)
                            self:PrintErrorMessage(
                                    strformat(L["ERROR_STRING_DEFAULT"], fieldName)
                            )
                        end
                )
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
                args = self:GetPlaceholderArgs("placeholders", L["CATEGORY_TITLE_PLACEHOLDERS_EXTENDED"],
                        function(count)
                            return strformat(L["CATEGORY_COMMENT_PLACEHOLDERS_INFO"], count, (count == 1 and "") or "s")
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
                    blank1 = {
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
                    blank2 = {
                        type = "description", order = self:GetNextIndex(), fontSize = "small", name = " "
                    },
                    showWelcomeMessage = {
                        type = "toggle", order = self:GetNextIndex(),
                        name = L["TITLE_SHOW_WELCOME_MESSAGE"],
                        desc = L["COMMENT_SHOW_WELCOME_MESSAGE"],
                        get = function(_)
                            return self:GetFromDb("showWelcomeMessage")
                        end,
                        set = function(_, value)
                            local oldValue = self:GetFromDb("showWelcomeMessage")
                            local isValid = (type(value) == "boolean")
                            if isValid then
                                self.db.profile.showWelcomeMessage = value
                                self:PrintChangedValue(L["TITLE_SHOW_WELCOME_MESSAGE"], oldValue, value)
                            end
                        end,
                    },
                    blank3 = {
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
                                self:PrintErrorMessage(
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
                                self:PrintErrorMessage(
                                        strformat(L["ERROR_RANGE_DEFAULT"], L["TITLE_FRAME_CLEAR_DELAY"],
                                                L["MINIMUM_FRAME_CLEAR_DELAY"], L["MAXIMUM_FRAME_CLEAR_DELAY"])
                                )
                            end
                        end,
                    },
                    blank4 = {
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
                                self:PrintErrorMessage(
                                        strformat(L["ERROR_RANGE_DEFAULT"], L["TITLE_FRAME_SIZE"],
                                                L["MINIMUM_FRAME_SIZE"], L["MAXIMUM_FRAME_SIZE"])
                                )
                            end
                        end,
                    },
                    blank5 = {
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
    DB_DEFAULTS.profile.schema = self:GetAddOnInfo()["schema"]
    return DB_DEFAULTS
end

--- Modify the settings in the currently active config profile with the specified arguments
---
--- @param notify boolean Whether or not to fire NotifyChange after operation (Default: true)
--- @param reset boolean Whether or not to Reset this profile to it's defaults (Default: false)
--- @param tags table Optional Value depicting data sets to refresh, if supported
---
--- @return table @ profile
CraftPresence.UpdateProfile = CraftPresence:vararg(3, function(self, notify, reset, tags)
    local canNotify = self:GetBuildInfo()["toc_version"] >= self:GetCompatibilityInfo()["2.0.0"] or self:IsRebasedApi()
    notify = self:GetOrDefault(notify, true)
    reset = self:GetOrDefault(reset, false)

    if reset then
        self:Print(L["INFO_RESET_CONFIG"])
        self.db:ResetProfile(false, true)
    end

    -- Additional dynamic sync events
    for _, tagName in pairs(tags) do
        tagName = strlower(tagName)

        if tagName == "all" or tagName == "events" then
            self:SyncEvents(self:GetFromDb("events"), self:GetFromDb("debugMode"))
        end
        if tagName == "all" or tagName == "compat" then
            self:EnsureCompatibility(self:GetFromDb("schema"), self:GetAddOnInfo()["schema"])
        end
    end

    if notify and canNotify then
        config_registry:NotifyChange(L["ADDON_NAME"])
    end

    if reset then
        return self:GetDefaults()
    else
        return self.db.profile
    end
end)
