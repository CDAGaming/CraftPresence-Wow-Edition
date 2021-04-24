local CraftPresence = LibStub("AceAddon-3.0"):GetAddon("CraftPresence")

local L = LibStub("AceLocale-3.0"):GetLocale("CraftPresence")

-- Lua APIs
local strformat, pairs, tostring = string.format, pairs, tostring

-- Addon APIs
local inkey = function()
    return CraftPresence:GetFromDb("innerPlaceholderKey")
end
local outkey = function()
    return CraftPresence:GetFromDb("globalPlaceholderKey")
end
local setfmt = function(str, replacer_one, replacer_two, pattern_one, pattern_two, plain)
    return CraftPresence:SetFormat(str, replacer_one, replacer_two, pattern_one, pattern_two, plain)
end

----------------------------------
--GAME GETTERS AND SETTERS
----------------------------------

local lastPlayerStatus = ""
local hasInstanceChanged = false

--- Retrieves the Player Status for the specified unit
---
--- @param unit string The unit name (Default: player)
--- @param sync boolean Whether to sync the resulting status to lastPlayerStatus
--- @param isRebasedApi boolean Whether the client is on a rebased api
---
--- @return string, string @ playerStatus, playerPrefix
function CraftPresence:GetPlayerStatus(unit, sync, isRebasedApi)
    unit = unit or "player"
    local playerStatus = ""
    local playerPrefix = ""
    local isAfk, isOnDnd, isDead, isGhost
    -- Ensure Version Compatibility
    if self:GetBuildInfo()["toc_version"] >= self:GetCompatibilityInfo()["2.0.0"] or isRebasedApi then
        isAfk = UnitIsAFK(unit)
        isOnDnd = UnitIsDND(unit)
    end
    -- Sync Player Name Tweaks (DND/AFK Data)
    isDead = UnitIsDead(unit)
    isGhost = UnitIsGhost(unit)
    if isAfk then
        playerStatus = L["AFK_LABEL"]
    elseif isOnDnd then
        playerStatus = L["DND_LABEL"]
    elseif isGhost then
        playerStatus = L["GHOST_LABEL"]
    elseif isDead then
        playerStatus = L["DEAD_LABEL"]
    end
    -- Parse Player Status
    if not self:IsNullOrEmpty(playerStatus) then
        playerPrefix = ("(" .. playerStatus .. ")") .. " "
    else
        playerStatus = L["ONLINE_LABEL"]
    end

    -- Return Data (and sync if needed)
    if sync then
        lastPlayerStatus = playerStatus
    end
    return playerStatus, playerPrefix
end

--- Retrieves whether the instance has recently changed
--- @return boolean @ hasInstanceChanged
function CraftPresence:HasInstanceChanged()
    return hasInstanceChanged
end

--- Retrieves the Last Player Status, if any
--- @return string @ lastPlayerStatus
function CraftPresence:GetLastPlayerStatus()
    return lastPlayerStatus
end

----------------------------------
--GAME UTILITIES
----------------------------------

local realmData = { "US", "KR", "EU", "TW", "CH" }
local buildData = CraftPresence:GetBuildInfo()
local compatData = CraftPresence:GetCompatibilityInfo()
local isRebasedApi = CraftPresence:IsRebasedApi()

local lastInstanceState, lastAreaName
local playerAlliance = L["TYPE_NONE"]
local playerCovenant = L["TYPE_NONE"]

--- Parses Game information to form placeholder information
---
--- @param force_instance_change boolean Whether to force an instance change (Default: false)
---
--- @return table, table, table @ global_placeholders, inner_placeholders, time_conditions
function CraftPresence:ParseGameData(force_instance_change)
    force_instance_change = force_instance_change ~= nil and force_instance_change == true
    -- Variable Initialization
    local name, instanceType, difficultyID, difficultyName, maxPlayers,
    dynamicDifficulty, isDynamic, instanceID, instanceGroupSize, LfgDungeonID
    if buildData["toc_version"] >= compatData["2.5.0"] or isRebasedApi then
        name, instanceType, difficultyID, difficultyName, maxPlayers,
        dynamicDifficulty, isDynamic, instanceID, instanceGroupSize, LfgDungeonID = GetInstanceInfo()
    else
        -- Fallback Data, as needed
        name = ""
    end
    local difficultyInfo = difficultyName
    local lockoutData = {}
    if buildData["toc_version"] >= compatData["6.0.0"] then
        lockoutData = self:GetCurrentLockoutData(true)
    end
    -- Player Data
    local playerName = UnitName("player")
    local playerStatus, playerPrefix = self:GetPlayerStatus("player", true, isRebasedApi)
    -- Extra Player Data
    local playerLevel = UnitLevel("player")
    local playerRealm = GetRealmName()
    local playerRegionId = 1
    if buildData["toc_version"] >= compatData["6.0.0"] or isRebasedApi then
        playerRegionId = GetCurrentRegion()
    end
    local playerRegion = realmData[playerRegionId]
    local playerClass = UnitClass("player")
    hasInstanceChanged = force_instance_change or (
            ((lastInstanceState == nil) or (instanceType ~= lastInstanceState)) or
                    ((lastAreaName == nil) or (name ~= lastAreaName))
    )
    -- Covenant and Faction Setup
    -- Retail: If not in a covenant, or cannot identify that this instance belongs to Shadowlands
    -- Then use the Faction as the Alliance; otherwise setup Covenant Data
    local _, localizedFaction = UnitFactionGroup("player")
    local playerCovenantId = 0
    local playerCovenantData
    local playerCovenantRenown = 0
    if buildData["toc_version"] >= compatData["9.0.0"] then
        playerCovenantId = C_Covenants.GetActiveCovenantID()
        playerCovenantData = C_Covenants.GetCovenantData(playerCovenantId)
        playerCovenantRenown = C_CovenantSanctumUI.GetRenownLevel()
    end
    -- Covenant and/or Faction data is only updated if the instance is changed
    if hasInstanceChanged then
        if (playerCovenantId == 0 or not (
                (self:FindMatches(name, "Shadowlands", false)) or
                        (self:FindMatches(name, "Torghast", false)) or
                        (self:FindMatches(self:GetCurrentInstanceTier(), "Shadowlands", false))
        )) then
            playerAlliance = localizedFaction
            playerCovenant = L["TYPE_NONE"]
        else
            playerCovenant = playerCovenantData.name
            playerAlliance = playerCovenant
        end
    end
    -- Zone Data
    local formatted_zone_info
    local zone_name = GetRealZoneText()
    local sub_name = GetSubZoneText()
    -- Null-Case to ensure Zone Name always equals something
    if self:IsNullOrEmpty(zone_name) then
        zone_name = L["TYPE_UNKNOWN"]
    end
    -- Format the zone info based on zone data
    if self:IsNullOrEmpty(sub_name) then
        formatted_zone_info = zone_name
        sub_name = L["TYPE_UNKNOWN"]
    else
        formatted_zone_info = (sub_name .. " - " .. zone_name)
    end
    -- Calculate Global Placeholders
    local queued_global_placeholders = {
        [setfmt("*dungeon*", outkey)] = self:GetFromDb("dungeonPlaceholderMessage"),
        [setfmt("*raid*", outkey)] = self:GetFromDb("raidPlaceholderMessage"),
        [setfmt("*battleground*", outkey)] = self:GetFromDb("battlegroundPlaceholderMessage"),
        [setfmt("*arena*", outkey)] = self:GetFromDb("arenaPlaceholderMessage"),
        [setfmt("*default*", outkey)] = self:GetFromDb("defaultPlaceholderMessage")
    }
    -- Calculate Inner Placeholders
    local queued_inner_placeholders = {
        [setfmt("*player_info*", inkey)] = "", -- Version-Dependent
        [setfmt("*player_name*", inkey)] = playerName,
        [setfmt("*title_name*", inkey)] = playerName, -- Version-Dependent
        [setfmt("*player_level*", inkey)] = playerLevel,
        [setfmt("*player_class*", inkey)] = playerClass,
        [setfmt("*player_status*", inkey)] = playerStatus,
        [setfmt("*player_alliance*", inkey)] = playerAlliance,
        [setfmt("*player_covenant*", inkey)] = playerCovenant,
        [setfmt("*player_covenant_renown*", inkey)] = "0", -- Retail-Only
        [setfmt("*player_faction*", inkey)] = localizedFaction,
        [setfmt("*player_spec_name*", inkey)] = "", -- Retail-Only
        [setfmt("*player_spec_role*", inkey)] = "", -- Retail-Only
        [setfmt("*item_level*", inkey)] = "0", -- Retail-Only
        [setfmt("*item_level_equipped*", inkey)] = "0", -- Retail-Only
        [setfmt("*item_level_pvp*", inkey)] = "0", -- Retail-Only
        [setfmt("*realm_info*", inkey)] = (playerRegion .. " - " .. playerRealm),
        [setfmt("*player_region*", inkey)] = playerRegion,
        [setfmt("*player_realm*", inkey)] = playerRealm,
        [setfmt("*zone_info*", inkey)] = formatted_zone_info,
        [setfmt("*zone_name*", inkey)] = zone_name,
        [setfmt("*sub_zone_name*", inkey)] = sub_name,
        [setfmt("*difficulty_name*", inkey)] = difficultyName,
        [setfmt("*difficulty_info*", inkey)] = difficultyInfo, -- Retail-Effected
        [setfmt("*active_keystone_level*", inkey)] = "", -- Retail-Only
        [setfmt("*active_keystone_affixes*", inkey)] = "", -- Retail-Only
        [setfmt("*owned_keystone_level*", inkey)] = "", -- Retail-Only
        [setfmt("*instance_type*", inkey)] = instanceType,
        [setfmt("*localized_name*", inkey)] = name,
        [setfmt("*instance_difficulty*", inkey)] = tostring(difficultyID),
        [setfmt("*max_players*", inkey)] = tostring(maxPlayers),
        [setfmt("*dynamic_difficulty*", inkey)] = tostring(dynamicDifficulty),
        [setfmt("*is_dynamic*", inkey)] = tostring(isDynamic),
        [setfmt("*instance_id*", inkey)] = tostring(instanceID),
        [setfmt("*instance_group_size*", inkey)] = tostring(instanceGroupSize),
        [setfmt("*lfg_dungeon_id*", inkey)] = tostring(LfgDungeonID),
        [setfmt("*lockout_encounters*", inkey)] = "", -- Retail-Only
        [setfmt("*lockout_current_encounters*", inkey)] = 0, -- Retail-Only
        [setfmt("*lockout_total_encounters*", inkey)] = 0 -- Retail-Only
    }
    -- Version Dependent Data
    local userData = playerPrefix .. playerName .. " - " .. (strformat(L["LEVEL_TAG_FORMAT"], playerLevel))
    if buildData["toc_version"] >= compatData["5.0.0"] or isRebasedApi then
        -- Extra Character Data
        local titleName = UnitPVPName("player")
        local avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = 0, 0, 0
        local specId
        local specName = ""
        local roleName = ""
        local specInfo
        if buildData["toc_version"] >= compatData["5.0.0"] then
            avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = GetAverageItemLevel()
            specInfo = GetSpecialization()
        end
        -- Hotfix: Prevent a null-case with Spec Info
        -- (Only happens if events fire too quickly for into to populate, or if you don't have a spec learned/available)
        if specInfo ~= nil then
            specId, specName = GetSpecializationInfo(GetSpecialization())
            if specId ~= nil then
                roleName = self:FormatWord(GetSpecializationRoleByID(specId))
            else
                specName = L["TYPE_NONE"]
            end
        end
        -- Keystone Data
        local ownedKeystoneData = self:GetOwnedKeystone()
        local activeKeystoneData = self:GetActiveKeystone()
        difficultyInfo = difficultyName
        if (activeKeystoneData ~= nil and not self:IsNullOrEmpty(activeKeystoneData.formattedLevel)) then
            difficultyInfo = (difficultyInfo .. " (" .. activeKeystoneData.formattedLevel .. ")")
        end
        -- Inner Placeholder Adjustments
        queued_inner_placeholders[setfmt("*title_name*", inkey)] = titleName
        queued_inner_placeholders[setfmt("*player_info*", inkey)] = (userData .. " " .. specName .. " " .. playerClass)
        queued_inner_placeholders[setfmt("*player_covenant_renown*", inkey)] = tostring(playerCovenantRenown)
        queued_inner_placeholders[setfmt("*player_spec_name*", inkey)] = specName
        queued_inner_placeholders[setfmt("*player_spec_role*", inkey)] = roleName
        queued_inner_placeholders[setfmt("*item_level*", inkey)] = strformat("%.2f", avgItemLevel or 0)
        queued_inner_placeholders[setfmt("*item_level_equipped*", inkey)] = strformat("%.2f", avgItemLevelEquipped or 0)
        queued_inner_placeholders[setfmt("*item_level_pvp*", inkey)] = strformat("%.2f", avgItemLevelPvp or 0)
        queued_inner_placeholders[setfmt("*difficulty_info*", inkey)] = difficultyInfo
        queued_inner_placeholders[setfmt("*active_keystone_level*", inkey)] = activeKeystoneData.formattedLevel
        queued_inner_placeholders[setfmt("*active_keystone_affixes*", inkey)] = activeKeystoneData.formattedAffixes
        queued_inner_placeholders[setfmt("*owned_keystone_level*", inkey)] = ownedKeystoneData.formattedLevel
        queued_inner_placeholders[setfmt("*lockout_encounters*", inkey)] = lockoutData.formattedEncounterData
        queued_inner_placeholders[setfmt("*lockout_current_encounters*", inkey)] = lockoutData.currentEncounters
        queued_inner_placeholders[setfmt("*lockout_total_encounters*", inkey)] = lockoutData.totalEncounters
    else
        -- Inner Placeholder Adjustments
        queued_inner_placeholders[setfmt("*player_info*", inkey)] = (userData .. " " .. playerClass)
    end
    -- Calculate limiting RPC conditions
    -- If the condition is true, the placeholder will be active
    local queued_global_conditions = {
        [setfmt("*dungeon*", outkey)] = (
                queued_inner_placeholders[setfmt("*instance_type*", inkey)] == "party" and
                        not self:FindMatches(name, "Garrison", false)
        ),
        [setfmt("*raid*", outkey)] = (queued_inner_placeholders[setfmt("*instance_type*", inkey)] == "raid"),
        [setfmt("*battleground*", outkey)] = (queued_inner_placeholders[setfmt("*instance_type*", inkey)] == "pvp"),
        [setfmt("*arena*", outkey)] = (queued_inner_placeholders[setfmt("*instance_type*", inkey)] == "arena")
    }
    local queued_inner_conditions = {
        [setfmt("*lockout_encounters*", inkey)] = (lockoutData ~= nil and
                (lockoutData.currentEncounters or 0) > 0 and (lockoutData.totalEncounters or 0) > 0)
    }
    local queued_extra_conditions = {
        ["torghast"] = (self:FindMatches(name, "Torghast", false) and
                queued_inner_placeholders[setfmt("*instance_type*", inkey)] == "scenario")
    }
    local queued_time_conditions = {
        ["start"] = (queued_global_conditions[setfmt("*dungeon*", outkey)] or
                queued_global_conditions[setfmt("*raid*", outkey)] or
                queued_global_conditions[setfmt("*battleground*", outkey)] or
                queued_global_conditions[setfmt("*arena*", outkey)]),
        ["start:extra"] = (queued_extra_conditions["torghast"])
    }
    -- If these placeholders are active, they will override the field they are in
    local queued_override_placeholders = {
        -- N/A
    }
    -- Synchronize any Extra Conditionals
    queued_global_conditions[setfmt("*default*", outkey)] = (not queued_time_conditions["start"])
    for key, value in pairs(buildData) do
        local formattedKey = ("*" .. key .. "*")
        queued_inner_placeholders[setfmt(formattedKey, inkey)] = value
    end

    -- Prepare the final list of global placeholders
    for inKey, inValue in pairs(queued_global_placeholders) do
        local output = self:TrimString(inValue)
        -- If the Placeholders contained in the messages is an overrider,
        -- we replace the rest of the output with just that placeholder.
        --
        -- Note: Only applies towards inner-placeholders with subject to change data
        for _, overrideValue in pairs(queued_override_placeholders) do
            if self:FindMatches(output, overrideValue, false) then
                if (self:StartsWith(overrideValue, inkey) and
                        (queued_inner_conditions[overrideValue] == nil or queued_inner_conditions[overrideValue])
                ) then
                    output = overrideValue
                    break
                end
            end
        end

        if (queued_global_conditions[inKey] == nil or queued_global_conditions[inKey]) then
            for key, value in pairs(queued_inner_placeholders) do
                if (queued_inner_conditions[key] == nil or queued_inner_conditions[key]) then
                    output = self:Replace(output, key, value, true)
                else
                    output = self:Replace(output, key, "", true)
                end
            end
            queued_global_placeholders[inKey] = self:TrimString(output)
        else
            queued_global_placeholders[inKey] = ""
        end
    end
    -- Update Instance Status before exiting method
    if hasInstanceChanged then
        lastInstanceState = instanceType
        lastAreaName = name
    end
    return queued_global_placeholders, queued_inner_placeholders, queued_time_conditions
end
