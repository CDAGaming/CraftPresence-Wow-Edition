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
local strformat, pairs, tostring = string.format, pairs, tostring
local tinsert, tconcat = table.insert, table.concat

-- Addon APIs
local L = CraftPresence.locale
local setfmt = function(str, replacer_one, replacer_two, pattern_one, pattern_two, plain)
    return CraftPresence:SetFormat(str, replacer_one, replacer_two, pattern_one, pattern_two, plain)
end

--[[ GAME GETTERS AND SETTERS ]]--

local hasInstanceChanged
local cachedUnitData = {}

--- Retrieves the Player Status for the specified unit
---
--- @param unit string The unit to interpret (Default: player)
--- @param refresh boolean Whether to sync the resulting status to unitData.last_status (Default: false)
--- @param sync boolean Whether to sync the resulting status to cachedUnitData[unit] (Default: true)
--- @param prefixFormat string Optional argument to determine the prefix formatting (Default: L["FORMAT_USER_PREFIX"])
--- @param unitData table Optional argument for forcing certain unit status data
---
--- @return table @ unitData
function CraftPresence:GetUnitStatus(unit, refresh, sync, prefixFormat, unitData)
    unit = self:GetOrDefault(unit, "player")
    refresh = self:GetOrDefault(refresh, false)
    sync = self:GetOrDefault(sync, true)
    prefixFormat = self:GetOrDefault(prefixFormat, L["FORMAT_USER_PREFIX"])
    unitData = self:GetOrDefault(unitData, {})

    unitData.away = self:GetOrDefault(
            unitData.away or (UnitIsAFK and UnitIsAFK(unit)),
            (cachedUnitData[unit] and cachedUnitData[unit].away)
    )
    unitData.busy = self:GetOrDefault(
            unitData.busy or (UnitIsDND and UnitIsDND(unit)),
            (cachedUnitData[unit] and cachedUnitData[unit].busy)
    )
    unitData.dead = self:GetOrDefault(
            unitData.dead or (UnitIsDead and UnitIsDead(unit)),
            (cachedUnitData[unit] and cachedUnitData[unit].dead)
    )
    unitData.ghost = self:GetOrDefault(
            unitData.ghost or (UnitIsGhost and UnitIsGhost(unit)),
            (cachedUnitData[unit] and cachedUnitData[unit].ghost)
    )

    -- Sync Player Name Tweaks
    local unitInfo = {}
    if unitData.away then
        tinsert(unitInfo, L["LABEL_AWAY"])
        if cachedUnitData[unit] and self:IsNullOrEmpty(cachedUnitData[unit].reason) then
            cachedUnitData[unit].reason = DEFAULT_AFK_MESSAGE or ""
        end
    elseif unitData.busy then
        tinsert(unitInfo, L["LABEL_BUSY"])
        if cachedUnitData[unit] and self:IsNullOrEmpty(cachedUnitData[unit].reason) then
            cachedUnitData[unit].reason = DEFAULT_DND_MESSAGE or ""
        end
    end
    if unitData.ghost then
        tinsert(unitInfo, L["LABEL_GHOST"])
    elseif unitData.dead then
        tinsert(unitInfo, L["LABEL_DEAD"])
    end
    unitData.status = tconcat(unitInfo, ",")
    unitData.reason = self:GetOrDefault(unitData.reason or
            (cachedUnitData[unit] and cachedUnitData[unit].reason)
    )

    -- Parse Player Status
    if not self:IsNullOrEmpty(unitData.status) then
        unitData.prefix = strformat(prefixFormat, unitData.status)
    else
        unitData.status = L["LABEL_ONLINE"]
        unitData.prefix = ""
        unitData.reason = ""
    end

    -- Return Data (and sync if needed)
    if refresh then
        unitData.last_status = unitData.status
    end
    if sync then
        cachedUnitData[unit] = unitData
    end
    return unitData
end

--- Retrieves whether the instance has recently changed
--- @return boolean @ hasInstanceChanged
function CraftPresence:HasInstanceChanged()
    return self:GetOrDefault(hasInstanceChanged, false)
end

--- Retrieves the Cached Unit Data of the specified unit, if any
---
--- @param unit string The unit to interpret
---
--- @return table @ unitData
function CraftPresence:GetUnitData(unit)
    unit = self:GetOrDefault(unit, "player")
    return cachedUnitData[unit]
end

--- Sets a key,value pair within cachedPlayerData, for later usage
---
--- @param unit string The unit to interpret
--- @param key string The key to insert to the table
--- @param value any The value to insert to the table
---
--- @return table @ cachedPlayerData
function CraftPresence:SetCachedUnitData(unit, key, value)
    unit = self:GetOrDefault(unit, "player")
    if not (self:IsNullOrEmpty(key) or self:IsNullOrEmpty(value)) then
        key = self:TrimString(key)
        value = self:TrimString(value)
        cachedUnitData[unit][key] = value
    end
    return cachedUnitData[unit]
end

--[[ GAME UTILITIES ]]--

local realmData = { "US", "KR", "EU", "TW", "CH" }
local buildData = CraftPresence:GetBuildInfo()
local compatData = CraftPresence:GetCompatibilityInfo()
local isRebasedApi = CraftPresence:IsRebasedApi()

local lastInstanceState, lastAreaName
local playerAlliance, playerCovenant = L["TYPE_NONE"], L["TYPE_NONE"]

--- Parses Game information to form placeholder information
---
--- @param force_instance_change boolean Whether to force an instance change (Default: false)
---
--- @return table, table @ placeholders, conditionals
function CraftPresence:ParseGameData(force_instance_change)
    force_instance_change = self:GetOrDefault(force_instance_change, false)
    local inkey = self:GetFromDb("innerPlaceholderKey")
    local outkey = self:GetFromDb("globalPlaceholderKey")
    -- Variable Initialization
    local name, instanceType, difficultyID, difficultyName, maxPlayers,
    dynamicDifficulty, isDynamic, instanceID, instanceGroupSize, LfgDungeonID
    if buildData["toc_version"] >= compatData["2.5.0"] or isRebasedApi then
        name, instanceType, difficultyID, difficultyName, maxPlayers,
        dynamicDifficulty, isDynamic, instanceID, instanceGroupSize, LfgDungeonID = GetInstanceInfo()
    end
    local difficultyInfo = difficultyName
    local lockoutData = self:GetCachedLockout()
    if buildData["toc_version"] >= compatData["6.0.0"] then
        lockoutData = self:GetCurrentLockoutData(true)
    end
    -- Player Data
    local playerName = UnitName("player")
    local playerData = self:GetUnitStatus("player", true)
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
                (self:FindMatches(name, "Shadowlands", false, 1, false)) or
                        (self:FindMatches(name, "Torghast", false, 1, false)) or
                        (self:FindMatches(self:GetCurrentInstanceTier(), "Shadowlands", false, 1, false))
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

    -- Placeholder Calculations
    local newPlaceholders = {}

    -- Calculate Global Placeholders
    newPlaceholders.global = {
        [setfmt("*dungeon*", outkey)] = self:GetFromDb("dungeonPlaceholderMessage"),
        [setfmt("*raid*", outkey)] = self:GetFromDb("raidPlaceholderMessage"),
        [setfmt("*battleground*", outkey)] = self:GetFromDb("battlegroundPlaceholderMessage"),
        [setfmt("*arena*", outkey)] = self:GetFromDb("arenaPlaceholderMessage"),
        [setfmt("*default*", outkey)] = self:GetFromDb("defaultPlaceholderMessage")
    }
    -- Calculate Inner Placeholders
    newPlaceholders.inner = {
        [setfmt("*player_info*", inkey)] = "", -- Version-Dependent
        [setfmt("*player_name*", inkey)] = playerName,
        [setfmt("*title_name*", inkey)] = playerName, -- Version-Dependent
        [setfmt("*player_level*", inkey)] = playerLevel,
        [setfmt("*player_class*", inkey)] = playerClass,
        [setfmt("*player_status*", inkey)] = playerData.status,
        [setfmt("*player_reason*", inkey)] = playerData.reason,
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
    -- Calculate Custom Placeholders, as needed
    newPlaceholders.custom = self.placeholders.custom
    -- Version Dependent Data
    local userData = playerData.prefix .. playerName .. " - " .. (strformat(L["FORMAT_LEVEL"], playerLevel))
    if buildData["toc_version"] >= compatData["5.0.0"] or isRebasedApi then
        -- Extra Character Data
        local titleName = UnitPVPName("player")
        local avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = 0, 0, 0
        local specInfo, specId, specName, roleName
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
        -- Trim and Adjust User Data
        local userInfo = userData
        if not self:IsNullOrEmpty(specName) then
            userInfo = (userInfo .. " " .. specName)
        end
        if not self:IsNullOrEmpty(playerClass) then
            userInfo = (userInfo .. " " .. playerClass)
        end
        -- Inner Placeholder Adjustments
        newPlaceholders.inner[setfmt("*title_name*", inkey)] = titleName
        newPlaceholders.inner[setfmt("*player_info*", inkey)] = userInfo
        newPlaceholders.inner[setfmt("*player_covenant_renown*", inkey)] = tostring(playerCovenantRenown)
        newPlaceholders.inner[setfmt("*player_spec_name*", inkey)] = specName
        newPlaceholders.inner[setfmt("*player_spec_role*", inkey)] = roleName
        newPlaceholders.inner[setfmt("*item_level*", inkey)] = strformat("%.2f", avgItemLevel or 0)
        newPlaceholders.inner[setfmt("*item_level_equipped*", inkey)] = strformat("%.2f", avgItemLevelEquipped or 0)
        newPlaceholders.inner[setfmt("*item_level_pvp*", inkey)] = strformat("%.2f", avgItemLevelPvp or 0)
        newPlaceholders.inner[setfmt("*difficulty_info*", inkey)] = difficultyInfo
        newPlaceholders.inner[setfmt("*active_keystone_level*", inkey)] = activeKeystoneData.formattedLevel
        newPlaceholders.inner[setfmt("*active_keystone_affixes*", inkey)] = activeKeystoneData.formattedAffixes
        newPlaceholders.inner[setfmt("*owned_keystone_level*", inkey)] = ownedKeystoneData.formattedLevel
        newPlaceholders.inner[setfmt("*lockout_encounters*", inkey)] = lockoutData.formattedEncounterData
        newPlaceholders.inner[setfmt("*lockout_current_encounters*", inkey)] = lockoutData.currentEncounters
        newPlaceholders.inner[setfmt("*lockout_total_encounters*", inkey)] = lockoutData.totalEncounters
    else
        -- Inner Placeholder Adjustments
        newPlaceholders.inner[setfmt("*player_info*", inkey)] = (userData .. " " .. playerClass)
    end

    -- Calculate limiting RPC conditions
    -- If the condition is true, the placeholder will be active
    local newConditions = {}

    newConditions.global = {
        [setfmt("*dungeon*", outkey)] = (
                newPlaceholders.inner[setfmt("*instance_type*", inkey)] == "party" and
                        not self:FindMatches(name, "Garrison", false)
        ),
        [setfmt("*raid*", outkey)] = (newPlaceholders.inner[setfmt("*instance_type*", inkey)] == "raid"),
        [setfmt("*battleground*", outkey)] = (newPlaceholders.inner[setfmt("*instance_type*", inkey)] == "pvp"),
        [setfmt("*arena*", outkey)] = (newPlaceholders.inner[setfmt("*instance_type*", inkey)] == "arena")
    }
    newConditions.inner = {
        [setfmt("*lockout_encounters*", inkey)] = (lockoutData ~= nil and
                (lockoutData.currentEncounters or 0) > 0 and (lockoutData.totalEncounters or 0) > 0)
    }
    newConditions.extra = {
        ["torghast"] = (self:FindMatches(name, "Torghast", false) and
                newPlaceholders.inner[setfmt("*instance_type*", inkey)] == "scenario")
    }
    newConditions.time = {
        ["start"] = (newConditions.global[setfmt("*dungeon*", outkey)] or
                newConditions.global[setfmt("*raid*", outkey)] or
                newConditions.global[setfmt("*battleground*", outkey)] or
                newConditions.global[setfmt("*arena*", outkey)]),
        ["start:extra"] = (newConditions.extra["torghast"])
    }
    -- If these placeholders are active, they will override the field they are in
    newPlaceholders.override = {
        -- N/A
    }
    -- Synchronize any Extra Conditionals
    newConditions.global[setfmt("*default*", outkey)] = (not newConditions.time["start"])
    for key, value in pairs(buildData) do
        local formattedKey = ("*" .. key .. "*")
        newPlaceholders.inner[setfmt(formattedKey, inkey)] = value
    end

    -- Prepare the final list of global placeholders
    for inKey, inValue in pairs(newPlaceholders.global) do
        local output = self:TrimString(inValue)
        -- If the Placeholders contained in the messages is an overrider,
        -- we replace the rest of the output with just that placeholder.
        --
        -- Note: Only applies towards inner-placeholders with subject to change data
        for _, overrideValue in pairs(newPlaceholders.override) do
            if self:FindMatches(output, overrideValue, false) then
                if (self:StartsWith(overrideValue, inkey) and
                        (newConditions.inner[overrideValue] == nil or newConditions.inner[overrideValue])
                ) then
                    output = overrideValue
                    break
                end
            end
        end

        if (newConditions.global[inKey] == nil or newConditions.global[inKey]) then
            for key, value in pairs(newPlaceholders.inner) do
                if (newConditions.inner[key] == nil or newConditions.inner[key]) then
                    output = self:Replace(output, key, value, true)
                else
                    output = self:Replace(output, key, "", true)
                end
            end
            newPlaceholders.global[inKey] = self:TrimString(output)
        else
            newPlaceholders.global[inKey] = ""
        end
    end
    -- Update Instance Status before exiting method
    if hasInstanceChanged then
        lastInstanceState = instanceType
        lastAreaName = name
    end
    return newPlaceholders, newConditions
end
