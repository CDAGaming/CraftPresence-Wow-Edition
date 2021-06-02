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
        [self.metaValue .. "prefix"] = self:GetFromDb("globalPlaceholderKey"),
        ["dungeon"] = self:GetFromDb("dungeonPlaceholderMessage"),
        ["raid"] = self:GetFromDb("raidPlaceholderMessage"),
        ["battleground"] = self:GetFromDb("battlegroundPlaceholderMessage"),
        ["arena"] = self:GetFromDb("arenaPlaceholderMessage"),
        ["default"] = self:GetFromDb("defaultPlaceholderMessage")
    }
    -- Calculate Inner Placeholders
    newPlaceholders.inner = {
        [self.metaValue .. "prefix"] = self:GetFromDb("innerPlaceholderKey"),
        ["player_info"] = "", -- Version-Dependent
        ["player_name"] = playerName,
        ["title_name"] = playerName, -- Version-Dependent
        ["player_level"] = playerLevel,
        ["player_class"] = playerClass,
        ["player_status"] = playerData.status,
        ["player_reason"] = playerData.reason,
        ["player_alliance"] = playerAlliance,
        ["player_covenant"] = playerCovenant,
        ["player_covenant_renown"] = "0", -- Retail-Only
        ["player_faction"] = localizedFaction,
        ["player_spec_name"] = "", -- Retail-Only
        ["player_spec_role"] = "", -- Retail-Only
        ["item_level"] = "0", -- Retail-Only
        ["item_level_equipped"] = "0", -- Retail-Only
        ["item_level_pvp"] = "0", -- Retail-Only
        ["realm_info"] = (playerRegion .. " - " .. playerRealm),
        ["player_region"] = playerRegion,
        ["player_realm"] = playerRealm,
        ["zone_info"] = formatted_zone_info,
        ["zone_name"] = zone_name,
        ["sub_zone_name"] = sub_name,
        ["difficulty_name"] = difficultyName,
        ["difficulty_info"] = difficultyInfo, -- Retail-Effected
        ["active_keystone_level"] = "", -- Retail-Only
        ["active_keystone_affixes"] = "", -- Retail-Only
        ["owned_keystone_level"] = "", -- Retail-Only
        ["instance_type"] = instanceType,
        ["localized_name"] = name,
        ["instance_difficulty"] = tostring(difficultyID),
        ["max_players"] = tostring(maxPlayers),
        ["dynamic_difficulty"] = tostring(dynamicDifficulty),
        ["is_dynamic"] = tostring(isDynamic),
        ["instance_id"] = tostring(instanceID),
        ["instance_group_size"] = tostring(instanceGroupSize),
        ["lfg_dungeon_id"] = tostring(LfgDungeonID),
        ["lockout_encounters"] = "", -- Retail-Only
        ["lockout_current_encounters"] = 0, -- Retail-Only
        ["lockout_total_encounters"] = 0 -- Retail-Only
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
        newPlaceholders.inner["title_name"] = titleName
        newPlaceholders.inner["player_info"] = userInfo
        newPlaceholders.inner["player_covenant_renown"] = tostring(playerCovenantRenown)
        newPlaceholders.inner["player_spec_name"] = specName
        newPlaceholders.inner["player_spec_role"] = roleName
        newPlaceholders.inner["item_level"] = strformat("%.2f", avgItemLevel or 0)
        newPlaceholders.inner["item_level_equipped"] = strformat("%.2f", avgItemLevelEquipped or 0)
        newPlaceholders.inner["item_level_pvp"] = strformat("%.2f", avgItemLevelPvp or 0)
        newPlaceholders.inner["difficulty_info"] = difficultyInfo
        newPlaceholders.inner["active_keystone_level"] = activeKeystoneData.formattedLevel
        newPlaceholders.inner["active_keystone_affixes"] = activeKeystoneData.formattedAffixes
        newPlaceholders.inner["owned_keystone_level"] = ownedKeystoneData.formattedLevel
        newPlaceholders.inner["lockout_encounters"] = lockoutData.formattedEncounterData
        newPlaceholders.inner["lockout_current_encounters"] = lockoutData.currentEncounters
        newPlaceholders.inner["lockout_total_encounters"] = lockoutData.totalEncounters
    else
        -- Inner Placeholder Adjustments
        newPlaceholders.inner["player_info"] = (userData .. " " .. playerClass)
    end

    -- Calculate limiting RPC conditions
    -- If the condition is true, the placeholder will be active
    local newConditions = {}

    newConditions.global = {
        ["dungeon"] = (
                newPlaceholders.inner["instance_type"] == "party" and
                        not self:FindMatches(name, "Garrison", false)
        ),
        ["raid"] = (newPlaceholders.inner["instance_type"] == "raid"),
        ["battleground"] = (newPlaceholders.inner["instance_type"] == "pvp"),
        ["arena"] = (newPlaceholders.inner["instance_type"] == "arena")
    }
    newConditions.inner = {
        ["lockout_encounters"] = (lockoutData ~= nil and
                (lockoutData.currentEncounters or 0) > 0 and (lockoutData.totalEncounters or 0) > 0)
    }
    newConditions.extra = {
        ["torghast"] = (self:FindMatches(name, "Torghast", false) and
                newPlaceholders.inner["instance_type"] == "scenario")
    }
    newConditions.time = {
        ["start"] = (newConditions.global["dungeon"] or
                newConditions.global["raid"] or
                newConditions.global["battleground"] or
                newConditions.global["arena"]),
        ["start:extra"] = (newConditions.extra["torghast"])
    }
    -- If these placeholders are active, they will override the field they are in
    newPlaceholders.override = {
        -- N/A
    }
    -- Synchronize any Extra Conditionals
    newConditions.global["default"] = (not newConditions.time["start"])
    for key, value in pairs(buildData) do
        newPlaceholders.inner[key] = value
    end

    -- Prepare the final list of global placeholders
    for inKey, inValue in pairs(newPlaceholders.global) do
        if not self:StartsWith(inKey, self.metaValue) then
            local output = self:TrimString(inValue)
            local innerKeyPrefix = self:GetOrDefault(newPlaceholders.inner[self.metaValue .. "prefix"])
            -- If the Placeholders contained in the messages is an overrider,
            -- we replace the rest of the output with just that placeholder.
            --
            -- Note: Only applies towards inner-placeholders with subject to change data
            for _, overrideValue in pairs(newPlaceholders.override) do
                if self:FindMatches(output, overrideValue, false) then
                    if (self:StartsWith(overrideValue, innerKeyPrefix) and
                            (newConditions.inner[overrideValue] == nil or newConditions.inner[overrideValue])
                    ) then
                        output = overrideValue
                        break
                    end
                end
            end

            -- If the conditions for the global placeholder are nil or true,
            -- and the inner placeholder conditions are also satisfied,
            -- we replace the placeholder key with the inner placeholder result.
            --
            -- If the global placeholder condition is false, it's result will be nulled instead.
            if (newConditions.global[inKey] == nil or newConditions.global[inKey]) then
                for key, value in pairs(newPlaceholders.inner) do
                    if (not self:StartsWith(key, self.metaValue) and
                            (newConditions.inner[key] == nil or newConditions.inner[key])
                    ) then
                        output = self:Replace(output, (innerKeyPrefix .. key .. innerKeyPrefix), value, true)
                    else
                        output = self:Replace(output, (innerKeyPrefix .. key .. innerKeyPrefix), "", true)
                    end
                end
                newPlaceholders.global[inKey] = self:TrimString(output)
            else
                newPlaceholders.global[inKey] = ""
            end
        end
    end
    -- Update Instance Status before exiting method
    if hasInstanceChanged then
        lastInstanceState = instanceType
        lastAreaName = name
    end
    return newPlaceholders, newConditions
end
