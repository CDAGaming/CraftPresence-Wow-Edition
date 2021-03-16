local CraftPresence = LibStub("AceAddon-3.0"):GetAddon("CraftPresence")

local InstanceTable

--- Generate table of dungeon and raid instances by expansion
---
--- Programmatically generates a list of all dungeons and raids, to be used in determining what expansion
--- the instance your are currently inside, actually belongs to.
---
--- @return table @ instTable
function CraftPresence:GenerateInstanceTable()
    local instTable = {}

    local numTiers = EJ_GetNumTiers()
    local raid = false

    -- Run this twice, once for dungeons, once for raids
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
    InstanceTable = InstanceTable or CraftPresence:GenerateInstanceTable()

    -- Bail out if we're not even in an instance!
    if not IsInInstance() then
        return "NotAnInstance"
    end

    local _, instanceType, _, _ = GetInstanceInfo()
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

--- Retrieve current Instance/Scenario Lockout Data
---
--- Format:
--- lockoutData {
---     string @ name,
---     string @ difficulty,
---     number @ difficultyId,
---     number @ currentEncounters,
---     string @ formattedEncounterData
--- }
---
--- @return table @ lockoutData
function CraftPresence:GetCurrentLockoutData()
    -- Assign default data before execution
    local lockoutData = {
        name = "",
        difficulty = "",
        difficultyId = 0,
        currentEncounters = 0,
        totalEncounters = 0,
        formattedEncounterData = ""
    }

    -- If we are in an instance, do the following:
    -- * Scan through Scenario step info, if applicable
    -- * Otherwise, Detect lockout data, if present
    if IsInInstance() then
        local instanceName, _, difficulty, difficultyName = GetInstanceInfo()
        if C_Scenario.IsInScenario() then
            local _, _, steps = C_Scenario.GetStepInfo()
            if steps > 0 then
                local completedSteps = 0
                for i = 1, steps do
                    local _, _, completed, _, _, _, _, _, _, _, _, _, _ = C_Scenario.GetCriteriaInfo(i)
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
            for index = 1, GetNumSavedInstances() do
                local areaName, _, _, areaSc, locked, _, _, _, _, _, numStages, stageStatus, _ = GetSavedInstanceInfo(index)
                if locked then
                    if (instanceName == areaName and difficulty == areaSc) then
                        lockoutData = {
                            name = areaName,
                            difficulty = difficultyName,
                            difficultyId = difficulty,
                            currentEncounters = stageStatus,
                            totalEncounters = numStages,
                            formattedEncounterData = ("(" .. stageStatus .. "/" .. numStages .. ")")
                        }
                        break
                    end
                end
            end
        end
    end
    return lockoutData
end

--- Retrieves your currently owned Keystone Information
---
--- Format:
--- keystoneInfo {
---     string @ dungeon,
---     number @ level,
---     string @ formattedLevel
--- }
---
--- @return table @ keystoneInfo
function CraftPresence:GetOwnedKeystone()
    local keystoneInfo
    local mapID = C_MythicPlus.GetOwnedKeystoneChallengeMapID()

    if mapID then
        local keystoneLevel = C_MythicPlus.GetOwnedKeystoneLevel()
        local keystoneDungeon = C_ChallengeMode.GetMapUIInfo(mapID)

        keystoneInfo = {
            dungeon = keystoneDungeon,
            level = keystoneLevel,
            formattedLevel = ("+" .. keystoneLevel)
        }
    else
        keystoneInfo = {
            dungeon = nil,
            level = 0,
            formattedLevel = ""
        }
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
---     string @ formattedLevel,
---     string @ formattedAffixes
--- }
---
--- @return table @ keystoneInfo
function CraftPresence:GetActiveKeystone()
    local keystoneInfo
    local mapID = C_ChallengeMode.GetActiveChallengeMapID()
    local formattedKeyAffixes = ""

    if mapID then
        local activeKeystoneLevel, activeAffixIDs, wasActiveKeystoneCharged = C_ChallengeMode.GetActiveKeystoneInfo()
        local keystoneDungeon = C_ChallengeMode.GetMapUIInfo(mapID)
        if activeAffixIDs ~= nil then
            for _, affixId in pairs(activeAffixIDs) do
                local name, _, _ = C_ChallengeMode.GetAffixInfo(affixId)
                formattedKeyAffixes = formattedKeyAffixes .. ", " .. name
            end
        end

        keystoneInfo = {
            dungeon = keystoneDungeon,
            activeAffixes = activeAffixIDs,
            wasCharged = wasActiveKeystoneCharged,
            level = activeKeystoneLevel,
            formattedLevel = ("+" .. activeKeystoneLevel),
            formattedAffixes = formattedKeyAffixes
        }
    else
        keystoneInfo = {
            dungeon = nil,
            activeAffixes = nil,
            wasCharged = false,
            level = 0,
            formattedLevel = "",
            formattedAffixes = formattedKeyAffixes
        }
    end

    return keystoneInfo
end
