--[[
MIT License

Copyright (c) 2018 - 2024 CDAGaming (cstack2011@yahoo.com)

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

-- DB_DEFAULTS
local DB_DEFAULTS

-- Generates the default database settings, to ensure availability
function CraftPresence:GenerateDefaults()
    DB_DEFAULTS = {
        profile = {
            schema = 0,
            clientId = self.locale["DEFAULT_CLIENT_ID"],
            showMinimapIcon = true,
            showCompartmentEntry = true,
            showWelcomeMessage = true,
            enforceInterface = false,
            presence = {
                ["state"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    messageCallback = self.locale["DEFAULT_STATE_MESSAGE"],
                    messageFormatCallback = "no-dupes",
                    messageType = "string", messageFormatType = "string",
                    enabled = true
                },
                ["details"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    messageCallback = self.locale["DEFAULT_DETAILS_MESSAGE"],
                    messageFormatCallback = "no-dupes",
                    messageType = "string", messageFormatType = "string",
                    enabled = true
                },
                ["largeImage"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    keyCallback = self.locale["DEFAULT_LARGE_IMAGE_KEY"],
                    keyFormatCallback = "icon",
                    messageCallback = self.locale["DEFAULT_LARGE_IMAGE_MESSAGE"],
                    messageFormatCallback = "no-dupes",
                    keyType = "string", messageType = "string",
                    keyFormatType = "string", messageFormatType = "string",
                    enabled = true
                },
                ["smallImage"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    keyCallback = self.locale["DEFAULT_SMALL_IMAGE_KEY"],
                    keyFormatCallback = "icon",
                    messageCallback = self.locale["DEFAULT_SMALL_IMAGE_MESSAGE"],
                    messageFormatCallback = "no-dupes",
                    keyType = "string", messageType = "string",
                    keyFormatType = "string", messageFormatType = "string",
                    enabled = true
                }
            },
            debugMode = false,
            verboseMode = false,
            queuedPipeline = false,
            optionalMigrations = false,
            callbackDelay = self.locale["DEFAULT_CALLBACK_DELAY"],
            frameWidth = self.locale["DEFAULT_FRAME_WIDTH"],
            frameHeight = self.locale["DEFAULT_FRAME_HEIGHT"],
            frameAnchor = self.locale["DEFAULT_FRAME_ANCHOR"], -- self:GetValidAnchors(); "TOPLEFT"
            verticalFrames = false,
            frameStartX = self.locale["DEFAULT_FRAME_START_X"],
            frameStartY = self.locale["DEFAULT_FRAME_START_Y"],
            frameClearDelay = self.locale["DEFAULT_FRAME_CLEAR_DELAY"],
            buttons = {
                ["primaryButton"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    labelCallback = "", urlCallback = "",
                    messageFormatCallback = "no-dupes",
                    labelType = "string", urlType = "string",
                    messageFormatType = "string",
                    enabled = true
                },
                ["secondaryButton"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    labelCallback = "", urlCallback = "",
                    messageFormatCallback = "no-dupes",
                    labelType = "string", urlType = "string",
                    messageFormatType = "string",
                    enabled = true
                }
            },
            metrics = {
                ["WagoAnalytics"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self, fieldName, oldValue, value)
    if not self.WagoAnalytics then return end
    if type(value) == 'boolean' then
        self.WagoAnalytics:Switch(fieldName, value)
    end
    if type(value) == 'number' then
        self.WagoAnalytics:SetCounter(fieldName, value)
    end
end]]                ,
                    stateCallback = [[function (self)
    self.WagoAnalytics = LibStub('WagoAnalytics'):Register(GetAddOnMetadata(self.internals.name, 'X-Wago-ID'))
end]]                ,
                    unregisterCallback = [[function (self)
    if not self.WagoAnalytics then return end
    self.WagoAnalytics = nil
end]]                ,
                    enabled = false
                }
            },
            labels = {
                ["away"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    activeCallback = self.locale["DEFAULT_LABEL_AWAY"], inactiveCallback = "",
                    activeType = "string", inactiveType = "string",
                    stateCallback = [[function (self)
    return self:GetOrDefault(
        (UnitIsAFK and (UnitIsAFK('player') or false)),
        (self:GetUnitData('player').away) or false
    )
end]]                ,
                    enabled = true
                },
                ["busy"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    activeCallback = self.locale["DEFAULT_LABEL_BUSY"], inactiveCallback = "",
                    activeType = "string", inactiveType = "string",
                    stateCallback = [[function (self)
    return self:GetOrDefault(
        (UnitIsDND and (UnitIsDND('player') or false)),
        (self:GetUnitData('player').busy) or false
    )
end]]                ,
                    enabled = true
                },
                ["dead"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    activeCallback = self.locale["DEFAULT_LABEL_DEAD"], inactiveCallback = "",
                    activeType = "string", inactiveType = "string",
                    stateCallback = [[function (self)
    return self:GetOrDefault(
        (UnitIsDead and (UnitIsDead('player') or false)),
        (self:GetUnitData('player').dead) or false
    )
end]]                ,
                    enabled = true
                },
                ["ghost"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    activeCallback = self.locale["DEFAULT_LABEL_GHOST"], inactiveCallback = "",
                    activeType = "string", inactiveType = "string",
                    stateCallback = [[function (self)
    return self:GetOrDefault(
        (UnitIsGhost and (UnitIsGhost('player') or false)),
        (self:GetUnitData('player').ghost) or false
    )
end]]                ,
                    enabled = true
                },
                ["in_combat"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    activeCallback = self.locale["DEFAULT_LABEL_COMBAT"], inactiveCallback = "",
                    activeType = "string", inactiveType = "string",
                    stateCallback = [[function (self)
    return self:GetOrDefault(
        (UnitAffectingCombat and (UnitAffectingCombat('player') or false)),
        (self:GetUnitData('player').in_combat) or false
    )
end]]                ,
                    enabled = true
                },
                ["online"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    activeCallback = "", inactiveCallback = "",
                    activeType = "string", inactiveType = "string",
                    stateCallback = [[function (self)
    return self:GetOrDefault(
        (UnitIsConnected and (UnitIsConnected('player') or false)),
        (self:GetUnitData('player').online) or false
    )
end]]                ,
                    enabled = true
                }
            },
            placeholders = {
                ["default"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = self.locale["DEFAULT_FALLBACK_MESSAGE"],
                    processType = "string",
                    registerCallback = [[function (self)
    return GetInstanceInfo == nil or select(2, GetInstanceInfo()) == 'none' or (
        self:IsNullOrEmpty(select(2, GetInstanceInfo()))
    )
end]]                ,
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.globalKey, suffix = self.internals.defaults.globalKey
                },
                ["arena"] = {
                    minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                    processCallback = self.locale["DEFAULT_ARENA_MESSAGE"],
                    processType = "string",
                    registerCallback = [[function (self)
    return select(2, GetInstanceInfo()) == 'arena'
end]]                ,
                    tagCallback = "time_start",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.globalKey, suffix = self.internals.defaults.globalKey
                },
                ["battleground"] = {
                    minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                    processCallback = self.locale["DEFAULT_BATTLEGROUND_MESSAGE"],
                    processType = "string",
                    registerCallback = [[function (self)
    return select(2, GetInstanceInfo()) == 'pvp'
end]]                ,
                    tagCallback = "time_start",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.globalKey, suffix = self.internals.defaults.globalKey
                },
                ["raid"] = {
                    minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                    processCallback = self.locale["DEFAULT_RAID_MESSAGE"],
                    processType = "string",
                    registerCallback = [[function (self)
    return select(2, GetInstanceInfo()) == 'raid'
end]]                ,
                    tagCallback = "time_start",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.globalKey, suffix = self.internals.defaults.globalKey
                },
                ["dungeon"] = {
                    minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                    processCallback = self.locale["DEFAULT_DUNGEON_MESSAGE"],
                    processType = "string",
                    registerCallback = [[function (self)
    return select(2, GetInstanceInfo()) == 'party' and (
        not self:FindMatches(self:GetCurrentInstanceName(true), 'Garrison', false)
    )
end]]                ,
                    tagCallback = "time_start",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.globalKey, suffix = self.internals.defaults.globalKey
                },
                ["scenario"] = {
                    minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                    processCallback = self.locale["DEFAULT_SCENARIO_MESSAGE"],
                    processType = "string",
                    registerCallback = [[function (self)
    return select(2, GetInstanceInfo()) == 'scenario'
end]]                ,
                    tagCallback = "time_start",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.globalKey, suffix = self.internals.defaults.globalKey
                },
                ["player_name"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return UnitName('player')
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["title_name"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return UnitPVPName('player')
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["player_level"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return UnitLevel('player')
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["player_realm"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    local playerRealm = self.locale['TYPE_UNKNOWN']
    if GetRealmName then
        playerRealm = GetRealmName()
    end
    return playerRealm
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["player_region"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    local regionTable = { "US", "KR", "EU", "TW", "CH" }
    local playerRegion = (_G["GetCurrentRegion"] and regionTable[GetCurrentRegion()]) or (_G["GetCurrentRegionName"] and GetCurrentRegionName()) or strupper(strsub(GetCVar("realmList"), 1, 2)) or "TR"
    return playerRegion
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["player_class"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return UnitClass('player')
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["player_race"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return UnitRace('player')
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["player_gender"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    local genderData = { "Unknown", "Male", "Female" }
    local playerGenderId = 1
    if UnitSex then
        playerGenderId = UnitSex('player')
    end
    return genderData[playerGenderId]
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["player_icon"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    local genderData = { "Unknown", "Male", "Female" }
    local playerGenderId = 1
    local fallback = genderData[playerGenderId]
    local playerRace, playerGender = fallback, ''
    local result = playerRace
    if UnitRace then
        playerRace = UnitRace('player')
        result = self:Replace(playerRace, '%s+', '')
    end
    if UnitSex then
        playerGenderId = UnitSex('player')
        playerGender = genderData[playerGenderId]
        result = result .. '_' .. playerGender
    end
    return self:FormatAsIcon(result)
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["player_status"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return self:GetUnitStatus("player").status
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["player_reason"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return self:GetUnitStatus("player").reason
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
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
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["player_faction"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    local englishFaction, localizedFaction = UnitFactionGroup('player')
    englishFaction = self:GetOrDefault(englishFaction, self.locale['TYPE_NONE'])
    localizedFaction = self:GetOrDefault(localizedFaction, englishFaction)
    return localizedFaction
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["player_alliance"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    -- Covenant and Faction Setup
    -- Retail: If not in a covenant, or cannot identify that this instance belongs to Shadowlands
    -- Then use the Faction as the Alliance; otherwise use Covenant Data
    local name = self:GetOrDefault(self:GetCurrentInstanceName(true))
    local englishFaction, localizedFaction = UnitFactionGroup('player')
    englishFaction = self:GetOrDefault(englishFaction, self.locale['TYPE_NONE'])
    localizedFaction = self:GetOrDefault(localizedFaction, englishFaction)
    local playerCovenantId, playerCovenantData = 0
    if C_Covenants then
        playerCovenantId = C_Covenants.GetActiveCovenantID()
        playerCovenantData = C_Covenants.GetCovenantData(playerCovenantId)
    end
    -- Covenant and/or Faction data is only updated if the instance is changed
    if (playerCovenantId == 0 or self:IsNullOrEmpty(name) or not (
            (self:FindMatches(name, 'Shadowlands', false, 1, false)) or
                    (self:FindMatches(name, 'Zereth Mortis', false, 1, false)) or
                    (self:FindMatches(name, 'Torghast', false, 1, false)) or
                    (self:FindMatches(self:GetCurrentInstanceTier(), 'Shadowlands', false, 1, false))
    )) then
        return localizedFaction
    else
        return playerCovenantData.name
    end
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
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
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["player_info"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    -- Player Data
    local playerName = UnitName('player')
    local playerData = self:GetUnitStatus('player')
    -- Extra Player Data
    local unitLevel = UnitLevel('player')
    local unitClass = UnitClass('player')
    local userInfo = playerData.prefix .. playerName .. ' - ' .. (string.format(self.locale['FORMAT_LEVEL'], unitLevel))
    -- Specialization Info (5.0.4 and above)
    if GetSpecialization and GetSpecializationInfo then
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
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["player_spec_name"] = {
                    minimumTOC = "50004", maximumTOC = "", allowRebasedApi = false,
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
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["player_spec_role"] = {
                    minimumTOC = "50004", maximumTOC = "", allowRebasedApi = false,
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
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["realm_info"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    -- Get Player Info
    local regionTable = { "US", "KR", "EU", "TW", "CH" }
    local playerRealm = self.locale['TYPE_UNKNOWN']
    if GetRealmName then
        playerRealm = GetRealmName()
    end
    local playerRegion = (_G["GetCurrentRegion"] and regionTable[GetCurrentRegion()]) or (_G["GetCurrentRegionName"] and GetCurrentRegionName()) or strupper(strsub(GetCVar("realmList"), 1, 2)) or "TR"
    return (playerRegion .. ' - ' .. playerRealm)
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["player_covenant_renown"] = {
                    minimumTOC = "90000", maximumTOC = "", allowRebasedApi = false,
                    processCallback = [[function (self)
    return tostring(C_CovenantSanctumUI.GetRenownLevel())
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["localized_name"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return self:GetOrDefault(self:GetCurrentInstanceName(true), self.locale['TYPE_UNKNOWN'])
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["instance_type"] = {
                    minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return select(2, GetInstanceInfo())
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["instance_difficulty"] = {
                    minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return tostring(select(3, GetInstanceInfo()))
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["difficulty_name"] = {
                    minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return select(4, GetInstanceInfo())
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["current_players"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    local current = 0
    if GetNumGroupMembers then
        current = GetNumGroupMembers()
    else
        if UnitInParty("player") and GetNumPartyMembers then
            current = GetNumPartyMembers()
        elseif UnitInRaid("player") and GetNumRaidMembers then
            current = GetNumRaidMembers()
        end
    end
    return tostring(current)
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["max_players"] = {
                    minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return tostring(select(5, GetInstanceInfo()))
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["dynamic_difficulty"] = {
                    minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return tostring(select(6, GetInstanceInfo()))
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["is_dynamic"] = {
                    minimumTOC = "30200", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return tostring(select(7, GetInstanceInfo()))
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["instance_id"] = {
                    minimumTOC = "50004", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return tostring(select(8, GetInstanceInfo()))
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["instance_group_size"] = {
                    minimumTOC = "50400", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return tostring(select(9, GetInstanceInfo()))
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["lfg_dungeon_id"] = {
                    minimumTOC = "80000", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return tostring(select(10, GetInstanceInfo()))
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["zone_name"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return self:GetOrDefault(GetRealZoneText(), self.locale['TYPE_UNKNOWN'])
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["sub_zone_name"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return self:GetOrDefault(GetSubZoneText(), self.locale["TYPE_UNKNOWN"])
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
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
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["item_level"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    local avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = 0, 0, 0
    if GetAverageItemLevel then
        avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = GetAverageItemLevel()
    elseif GearScore_GetScore then
        local target = "player"
        _, avgItemLevel = GearScore_GetScore(UnitName(target), target)
    end
    return string.format("%.2f", avgItemLevel or 0)
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["item_level_equipped"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    local avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = 0, 0, 0
    if GetAverageItemLevel then
        avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = GetAverageItemLevel()
    elseif GearScore_GetScore then
        local target = "player"
        _, avgItemLevel = GearScore_GetScore(UnitName(target), target)
    end
    return string.format("%.2f", avgItemLevelEquipped or avgItemLevel)
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["item_level_pvp"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    local avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = 0, 0, 0
    if GetAverageItemLevel then
        avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = GetAverageItemLevel()
    elseif GearScore_GetScore then
        local target = "player"
        _, avgItemLevel = GearScore_GetScore(UnitName(target), target)
    end
    return string.format("%.2f", avgItemLevelPvp or avgItemLevel)
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["active_keystone_level"] = {
                    minimumTOC = "50000", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return self:GetActiveKeystone().formattedLevel
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["active_keystone_affixes"] = {
                    minimumTOC = "50000", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return self:GetActiveKeystone().formattedAffixes
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["active_keystone_rating"] = {
                    minimumTOC = "50000", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return tostring(self:GetActiveKeystone().rating)
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["internal_keystone_rating"] = {
                    minimumTOC = "50000", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return tostring(self:GetActiveKeystone().internal_rating)
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["external_keystone_rating"] = {
                    minimumTOC = "50000", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return tostring(self:GetActiveKeystone().external_rating)
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["owned_keystone_level"] = {
                    minimumTOC = "50000", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self)
    return self:GetOwnedKeystone().formattedLevel
end]]                ,
                    processType = "function",
                    registerCallback = "",
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["lockout_encounters"] = {
                    minimumTOC = "60000", maximumTOC = "", allowRebasedApi = false,
                    processCallback = [[function (self)
    return self:GetCurrentLockoutData(true).formattedEncounterData
end]]                ,
                    processType = "function",
                    registerCallback = [[function (self)
    local lockoutData = self:GetCurrentLockoutData(true)
    return lockoutData ~= nil and lockoutData.currentEncounters > 0 and lockoutData.totalEncounters > 0
end]]                ,
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["lockout_current_encounters"] = {
                    minimumTOC = "60000", maximumTOC = "", allowRebasedApi = false,
                    processCallback = [[function (self)
    return self:GetCurrentLockoutData(true).currentEncounters
end]]                ,
                    processType = "function",
                    registerCallback = [[function (self)
    local lockoutData = self:GetCurrentLockoutData(true)
    return lockoutData ~= nil and lockoutData.currentEncounters > 0 and lockoutData.totalEncounters > 0
end]]                ,
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
                },
                ["lockout_total_encounters"] = {
                    minimumTOC = "60000", maximumTOC = "", allowRebasedApi = false,
                    processCallback = [[function (self)
    return self:GetCurrentLockoutData(true).totalEncounters
end]]                ,
                    processType = "function",
                    registerCallback = [[function (self)
    local lockoutData = self:GetCurrentLockoutData(true)
    return lockoutData ~= nil and lockoutData.currentEncounters > 0 and lockoutData.totalEncounters > 0
end]]                ,
                    tagCallback = "",
                    tagType = "string",
                    enabled = true,
                    prefix = self.internals.defaults.innerKey, suffix = self.internals.defaults.innerKey
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
        self:SetCachedUnitData('player', 'reason', self:GetOrDefault(splitMessage[2], DEFAULT_AFK_MESSAGE))
    elseif isBusyStatus then
        self:SetCachedUnitData('player', 'busy', args[1] ~= CLEARED_DND and isBusyStatus)
        self:SetCachedUnitData('player', 'reason', self:GetOrDefault(splitMessage[2], DEFAULT_DND_MESSAGE))
    else
        self:SetCachedUnitData('player', 'reason', '')
    end
    return not (isAfkStatus or isBusyStatus)
end]]                ,
                    registerCallback = "",
                    eventCallback = "function(self) return self.defaultEventCallback end",
                    enabled = true
                },
                ["PLAYER_LOGIN"] = {
                    minimumTOC = "10800", maximumTOC = "", allowRebasedApi = true,
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
                    minimumTOC = "80001", maximumTOC = "", allowRebasedApi = false,
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
                ["PLAYER_REGEN_ENABLED"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = "",
                    registerCallback = "",
                    eventCallback = "function(self) return self.defaultEventCallback end",
                    enabled = true
                },
                ["PLAYER_REGEN_DISABLED"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = "",
                    registerCallback = "",
                    eventCallback = "function(self) return self.defaultEventCallback end",
                    enabled = true
                },
                ["PLAYER_FLAGS_CHANGED"] = {
                    minimumTOC = "", maximumTOC = "", allowRebasedApi = true,
                    processCallback = [[function (self, lastName, _, args)
    return lastName == 'CHAT_MSG_SYSTEM' or args[1] ~= 'player'
end]]                ,
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
                    minimumTOC = "50004", maximumTOC = "", allowRebasedApi = false,
                    processCallback = "function (_, _, _, args) return args[1] ~= 'player' end",
                    registerCallback = "",
                    eventCallback = "function(self) return self.defaultEventCallback end",
                    enabled = true
                },
                ["ACTIVE_TALENT_GROUP_CHANGED"] = {
                    minimumTOC = "30200", maximumTOC = "", allowRebasedApi = false,
                    processCallback = "function (_, _, _, args) return args[1] == args[2] end",
                    registerCallback = "",
                    eventCallback = "function(self) return self.defaultEventCallback end",
                    enabled = true
                },
                ["GROUP_JOINED"] = {
                    minimumTOC = "50100", maximumTOC = "", allowRebasedApi = true,
                    processCallback = "",
                    registerCallback = "",
                    eventCallback = "function(self) return self.defaultEventCallback end",
                    enabled = true
                },
                ["GROUP_LEFT"] = {
                    minimumTOC = "50100", maximumTOC = "", allowRebasedApi = true,
                    processCallback = "",
                    registerCallback = "",
                    eventCallback = "function(self) return self.defaultEventCallback end",
                    enabled = true
                },
                ["ENCOUNTER_START"] = {
                    minimumTOC = "50402", maximumTOC = "", allowRebasedApi = true,
                    processCallback = "",
                    registerCallback = "",
                    eventCallback = "function(self) return self.defaultEventCallback end",
                    enabled = true
                },
                ["ENCOUNTER_END"] = {
                    minimumTOC = "50402", maximumTOC = "", allowRebasedApi = true,
                    processCallback = "",
                    registerCallback = "",
                    eventCallback = "function(self) return self.defaultEventCallback end",
                    enabled = true
                },
                ["CHALLENGE_MODE_START"] = {
                    minimumTOC = "60002", maximumTOC = "", allowRebasedApi = false,
                    processCallback = "",
                    registerCallback = "",
                    eventCallback = "function(self) return self.defaultEventCallback end",
                    enabled = true
                },
                ["CHALLENGE_MODE_COMPLETED"] = {
                    minimumTOC = "50004", maximumTOC = "", allowRebasedApi = false,
                    processCallback = "",
                    registerCallback = "",
                    eventCallback = "function(self) return self.defaultEventCallback end",
                    enabled = true
                },
                ["CHALLENGE_MODE_RESET"] = {
                    minimumTOC = "60002", maximumTOC = "", allowRebasedApi = false,
                    processCallback = "",
                    registerCallback = "",
                    eventCallback = "function(self) return self.defaultEventCallback end",
                    enabled = true
                },
                ["SCENARIO_COMPLETED"] = {
                    minimumTOC = "60002", maximumTOC = "", allowRebasedApi = false,
                    processCallback = "",
                    registerCallback = "",
                    eventCallback = "function(self) return self.defaultEventCallback end",
                    enabled = true
                },
                ["CRITERIA_COMPLETE"] = {
                    minimumTOC = "50004", maximumTOC = "", allowRebasedApi = false,
                    processCallback = "",
                    registerCallback = "",
                    eventCallback = "function(self) return self.defaultEventCallback end",
                    enabled = true
                }
            }
        }
    }
end

--- Retrieves the default settings for new profiles
--- @return table @ DB_DEFAULTS
function CraftPresence:GetDefaults()
    if not DB_DEFAULTS then
        self:GenerateDefaults()
    end
    return DB_DEFAULTS
end
