--[[
MIT License

Copyright (c) 2018 - 2026 CDAGaming (cstack2011@yahoo.com)

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
local pairs, max, select = pairs, math.max, select
local strlower = string.lower

-- Lockout Data Storage
local InstanceTable
local defaultLockoutData = {
    name = "",
    difficulty = "",
    difficultyId = 0,
    currentEncounters = 0,
    totalEncounters = 0,
    formattedEncounterData = ""
}
local cachedLockoutState = defaultLockoutData

--- Generate table of dungeon and raid instances by expansion
---
--- Programmatically generates a list of all dungeons and raids, to be used in determining what expansion
--- the instance your are currently inside, actually belongs to.
---
--- @return table @ instTable
function CraftPresence:GenerateInstanceTable()
    local instTable = {}

    local numTiers = ((EJ_GetNumTiers and EJ_GetNumTiers()) or 0)
    local raid = false

    -- Run this twice, once for dungeons, once for raids
    if numTiers > 0 then
        for _ = 1, 2 do
            for t = 1, numTiers do
                EJ_SelectTier(t)
                local tierName = EJ_GetTierInfo(t)
                local instanceIndex = 1
                local id, _ = EJ_GetInstanceByIndex(instanceIndex, raid)

                while id do
                    if not (id == nil) then
                        instTable[id] = tierName
                    end
                    instanceIndex = instanceIndex + 1
                    id, _ = EJ_GetInstanceByIndex(instanceIndex, raid)
                end
            end

            -- Flip state, do raids next round
            raid = not raid
        end
    end

    return instTable
end

--- Figure out which expansion our current instance belongs to.
---
--- Problem: Remade dungeons, such as Deadmines, belong to more than 1 expansion,
--- but retain only one name and instanceID. Making a simple list of ID's insufficient.
---
--- At the time of writing (Legion), remade Vanilla dungeons are all Heroic variants.
--- Meaning, if we enter a "vanilla" instance, on Heroic difficulty, it must be a remake.
--- We can then simply skip scanning Vanilla, the next hit will accurately tell us our real tier.
---
--- This can break if something like a TBC instance is remade, for example, so this is a fragile approach.
---
--- @return string @ instanceTier
function CraftPresence:GetCurrentInstanceTier()
    -- Check that the InstanceTable even exists, if not, create it
    InstanceTable = InstanceTable or self:GenerateInstanceTable()

    -- Bail out if we're not even in an instance!
    if not IsInInstance() then
        return "NotAnInstance"
    end

    local instanceType = select(2, GetInstanceInfo())
    local unitMapID = C_Map.GetBestMapForUnit("player")
    if not unitMapID then
        unitMapID = C_Map.GetFallbackWorldMapID()
    end
    local instanceID = EJ_GetInstanceForMap(unitMapID)

    -- First, check if we even track this type of instance, if not, bail out
    local trackedTypes = {
        party = true,
        raid = true,
    }

    if not trackedTypes[instanceType] then
        return "UnknownInstanceType"
    end

    -- Perform the actual search, scanning by instanceID, returning UnknownTier
    -- if said instance is unable to be located in the table
    return InstanceTable[instanceID] or "UnknownTier"
end

--- Determine the Current Instance Name (Or continent name if not applicable)
---
--- @param ensure_accuracy boolean Whether extra API functions should be used to ensure correctness
---
--- @return string result
function CraftPresence:GetCurrentInstanceName(ensure_accuracy)
    local result
    if not self:IsFeatureSupported("modernInstanceState", self:GetBuildInfo("toc_version")) then
        if ensure_accuracy then
            SetMapToCurrentZone()
        end
        local continents = { GetMapContinents() }
        result = continents[GetCurrentMapContinent()]
    elseif GetInstanceInfo then
        result = select(1, GetInstanceInfo())
    end
    return result
end

--- Retrieve current Instance/Scenario Lockout Data
---
--- Format:
--- lockoutData {
---     string @ name,
---     string @ difficulty,
---     number @ difficultyId,
---     number @ currentEncounters,
---     number @ totalEncounters,
---     string @ formattedEncounterData
--- }
---
--- @param sync boolean Whether the lockoutData should be cached
---
--- @return table @ lockoutData
function CraftPresence:GetCurrentLockoutData(sync)
    -- Assign default data before execution
    local lockoutData = defaultLockoutData

    -- If we are in an instance, do the following:
    -- * Scan through Scenario step info, if applicable
    -- * Otherwise, Detect lockout data, if present
    if IsInInstance() then
        local instanceName, _, difficulty, difficultyName = GetInstanceInfo()
        if C_Scenario and C_Scenario.IsInScenario() then
            local _, _, steps = C_Scenario.GetStepInfo()
            if steps > 0 then
                local completedSteps = 0
                for i = 1, steps do
                    local completed = false
                    if C_ScenarioInfo and C_ScenarioInfo.GetCriteriaInfo then
                        completed = C_ScenarioInfo.GetCriteriaInfo(i).completed
                    elseif C_Scenario.GetCriteriaInfo then
                        completed = select(3, C_Scenario.GetCriteriaInfo(i))
                    end

                    if completed then
                        completedSteps = completedSteps + 1
                    end
                end

                lockoutData = {
                    name = instanceName,
                    difficulty = difficultyName,
                    difficultyId = difficulty,
                    currentEncounters = completedSteps,
                    totalEncounters = steps,
                    formattedEncounterData = ("(" .. completedSteps .. "/" .. steps .. ")")
                }
            end
        else
            RequestRaidInfo()
            for index = 1, GetNumSavedInstances() do
                local areaName, _, _, areaSc, locked, _, _, _, _, _, numStages, curProg, _ = GetSavedInstanceInfo(index)
                if locked then
                    if (instanceName == areaName and difficulty == areaSc) then
                        lockoutData = {
                            name = areaName,
                            difficulty = difficultyName,
                            difficultyId = difficulty,
                            currentEncounters = curProg,
                            totalEncounters = numStages,
                            formattedEncounterData = ("(" .. curProg .. "/" .. numStages .. ")")
                        }
                        break
                    end
                end
            end
        end
    end
    if sync then
        cachedLockoutState = lockoutData
    end
    return lockoutData
end

--- Retrieves your currently owned Keystone Information
---
--- Format:
--- keystoneInfo {
---     table @ dungeon,
---     number @ level,
---     string @ formattedLevel
--- }
---
--- @return table @ keystoneInfo
function CraftPresence:GetOwnedKeystone()
    local keystoneInfo = {
        dungeon = nil,
        level = 0,
        formattedLevel = ""
    }

    if C_MythicPlus ~= nil then
        local mapID = C_MythicPlus.GetOwnedKeystoneChallengeMapID()

        if mapID then
            local keystoneLevel = C_MythicPlus.GetOwnedKeystoneLevel()
            local keystoneDungeon = C_ChallengeMode.GetMapUIInfo(mapID)

            keystoneInfo = {
                dungeon = keystoneDungeon,
                level = keystoneLevel,
                formattedLevel = ("+" .. keystoneLevel)
            }
        end
    end

    return keystoneInfo
end

--- Retrieves your currently active Keystone Information
---
--- Format:
--- keystoneInfo {
---     string @ dungeon,
---     table @ activeAffixes,
---     boolean @ wasCharged,
---     number @ level,
---     number @ rating,
---     number @ external_rating,
---     string @ formattedLevel,
---     string @ formattedAffixes
--- }
---
--- @return table @ keystoneInfo
function CraftPresence:GetActiveKeystone()
    local formattedKeyAffixes = ""
    local keystoneInfo = {
        dungeon = nil,
        activeAffixes = nil,
        wasCharged = false,
        level = 0, rating = nil, internal_rating = nil, external_rating = nil,
        formattedLevel = "",
        formattedAffixes = formattedKeyAffixes
    }

    -- External Rating Calculations (Done in pre-processing)
    local io_info = self:GetExternalInfo("rio")
    if io_info ~= nil and io_info.mythicKeystoneProfile ~= nil then
        keystoneInfo.external_rating = io_info.mythicKeystoneProfile.currentScore
    end

    if C_ChallengeMode ~= nil then
        local mapID = C_ChallengeMode.GetActiveChallengeMapID()

        -- Primary Rating Calculation
        if C_ChallengeMode.GetOverallDungeonScore then
            keystoneInfo.internal_rating = C_ChallengeMode.GetOverallDungeonScore()
        end

        if mapID then
            local activeKeyLevel, activeAffixIDs, wasActiveKeyCharged = C_ChallengeMode.GetActiveKeystoneInfo()
            local keystoneDungeon = C_ChallengeMode.GetMapUIInfo(mapID)
            if activeAffixIDs ~= nil then
                for _, affixId in pairs(activeAffixIDs) do
                    local name, _, _ = C_ChallengeMode.GetAffixInfo(affixId)
                    formattedKeyAffixes = formattedKeyAffixes .. ", " .. name
                end
            end

            keystoneInfo.dungeon = keystoneDungeon
            keystoneInfo.activeAffixes = activeAffixIDs
            keystoneInfo.wasCharged = wasActiveKeyCharged
            keystoneInfo.level = activeKeyLevel
            keystoneInfo.formattedLevel = ("+" .. activeKeyLevel)
            keystoneInfo.formattedAffixes = formattedKeyAffixes
        end
    end

    -- Post-Function Sanity Checks
    keystoneInfo.internal_rating = self:GetOrDefault(keystoneInfo.internal_rating, 0)
    keystoneInfo.external_rating = self:GetOrDefault(keystoneInfo.external_rating, keystoneInfo.internal_rating)
    keystoneInfo.rating = max(keystoneInfo.external_rating, keystoneInfo.internal_rating)

    return keystoneInfo
end

----------------------------------
--API GETTERS AND SETTERS
----------------------------------

--- Retrieves the Cached Lockout Data, if any
--- @return table @ cachedLockoutState
function CraftPresence:GetCachedLockout()
    return cachedLockoutState
end

--- Retrieves Information from a supported external api
---
--- @param key string The key identifier to retrieve from externalCache
--- @param can_modify boolean Whether the value can be appended into the externalCache (Optional)
--- @param can_use_externals boolean Whether externals can be safely accessed at this time (Optional)
---
--- @return any @ externalValue
function CraftPresence:GetExternalInfo(key, can_modify, can_use_externals)
    can_modify = self:GetOrDefault(can_modify, self.externalCache[key] == nil)
    can_use_externals = self:GetOrDefault(can_use_externals, self.canUseExternals)
    key = strlower(self:GetOrDefault(key))

    if can_modify then
        if can_use_externals then
            if (key == "raiderio" or key == "rio") and RaiderIO then
                self.externalCache[key] = RaiderIO.GetProfile("player")
            end
        end
    end

    return self.externalCache[key]
end
