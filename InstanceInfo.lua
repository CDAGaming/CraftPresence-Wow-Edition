local CraftPresence = LibStub("AceAddon-3.0"):GetAddon("CraftPresence")

-- Programmatically generate list of all dungeons and raids, to be used in determining what expansion
-- the instance your are currently inside, actually belongs to.

-- Generate table of dungeon and raid instances by expansion
local InstanceTable
function CraftPresence:GenerateInstanceTable()
    local instTable = {}

    local numTiers = EJ_GetNumTiers()
    local raid = false

    -- Run this twice, once for dungeons, once for raids
    for b = 1, 2 do
        local instanceType = ""

        for t = 1, numTiers do
            EJ_SelectTier(t)
            local tierName = EJ_GetTierInfo(t)
            local instanceIndex = 1
            local id, name = EJ_GetInstanceByIndex(instanceIndex, raid)

            while id do
                if not (id == nil) then
                    instTable[id] = tierName
                end
                instanceIndex = instanceIndex + 1
                id, name = EJ_GetInstanceByIndex(instanceIndex, raid)
            end
        end

        -- Flip state, do raids next round
        raid = not raid
    end

    return instTable
end


--[[
Figure out which expansion our current instance belongs to.

Problem: Remade dungeons, such as Deadmines, belong to more than 1 expansion,
but retain only one name and instanceID. Making a simple list of ID's insufficient.

At the time of writing (Legion), remade Vanilla dungeons are all Heroic variants.
Meaning, if we enter a "vanilla" instance, on Heroic difficulty, it must be a remake.
We can then simply skip scanning Vanilla, the next hit will accurately tell us our real tier.

This can break if something like a TBC instance is remade, for example, so this is a fragile approach.
--]]
function CraftPresence:GetCurrentInstanceTier()
    -- Check that the InstanceTable even exists, if not, create it
    InstanceTable = InstanceTable or CraftPresence:GenerateInstanceTable()

    -- Bail out if we're not even in an instance!
    if not IsInInstance() then
        return "NotAnInstance"
    end

    -- Generate a chronologically ordered list of expansion names
    local tierList = {}

    for i = 1, EJ_GetNumTiers() do
        tierList[i] = EJ_GetTierInfo(i)
    end

    local name, instanceType, difficulty, difficultyName = GetInstanceInfo()
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

    -- Second, is it Heroic? If so, skip all of Vanilla. Else, search the entire instance table
    local startIndex = (difficulty == 2) and 2 or 1

    -- Perform the actual search, scanning by instanceID, skipping Classic if we're in Heroic
    return InstanceTable[instanceID] or "UnknownTier"
end

function CraftPresence:GetCurrentLockoutData()
    local lockoutData = { name = "", difficulty = "", difficultyId = 0, currentEncounters = 0, totalEncounters = 0, formattedEncounterData = "" }
    if IsInInstance() then
        local name, instanceType, difficulty, difficultyName = GetInstanceInfo()
        local unitMapID = C_Map.GetBestMapForUnit("player")
        if not unitMapID then
            unitMapID = C_Map.GetFallbackWorldMapID()
        end
        local instanceID = EJ_GetInstanceForMap(unitMapID)
        for index = 1, GetNumSavedInstances() do
            local instanceName, id, reset, instanceDifficulty, locked, extended, instanceIDMostSig, isRaid, maxPlayers, instanceDifficultyName, numEncounters, encounterProgress, extendDisabled = GetSavedInstanceInfo(index)
            if locked then
                if (name == instanceName and difficulty == instanceDifficulty) then
                    lockoutData = { name = instanceName, difficulty = difficultyName, difficultyId = difficulty, currentEncounters = encounterProgress, totalEncounters = numEncounters, formattedEncounterData = ("(" .. encounterProgress .. "/" .. numEncounters .. ")") }
                    break
                end
            end
        end
    end
    return lockoutData
end

function CraftPresence:GetOwnedKeystone()
    local keystoneInfo
    local mapID = C_MythicPlus.GetOwnedKeystoneChallengeMapID()

    if mapID then
        local keystoneLevel = C_MythicPlus.GetOwnedKeystoneLevel()
        local keystoneDungeon = C_ChallengeMode.GetMapUIInfo(mapID)

        keystoneInfo = { dungeon = keystoneDungeon, level = keystoneLevel, formattedLevel = ("+" .. keystoneLevel) }
    else
        keystoneInfo = { dungeon = nil, level = 0, formattedLevel = "" }
    end

    return keystoneInfo
end

function CraftPresence:GetActiveKeystone()
    local keystoneInfo
    local mapID = C_ChallengeMode.GetActiveChallengeMapID()
    local formattedKeyAffixes = ""

    if mapID then
        local activeKeystoneLevel, activeAffixIDs, wasActiveKeystoneCharged = C_ChallengeMode.GetActiveKeystoneInfo()
        local keystoneDungeon = C_ChallengeMode.GetMapUIInfo(mapID)
        if activeAffixIDs ~= nil then
            for key, affixId in pairs(activeAffixIDs) do
                local name, description, fileDataId = C_ChallengeMode.GetAffixInfo(affixId)
                formattedKeyAffixes = formattedKeyAffixes .. ", " .. name
            end
        end

        keystoneInfo = { dungeon = keystoneDungeon, activeAffixes = activeAffixIDs, wasCharged = wasActiveKeystoneCharged, level = activeKeystoneLevel, formattedLevel = ("+" .. activeKeystoneLevel), formattedAffixes = formattedKeyAffixes }
    else
        keystoneInfo = { dungeon = nil, activeAffixes = nil, wasCharged = false, level = 0, formattedLevel = "", formattedAffixes = formattedKeyAffixes }
    end

    return keystoneInfo
end
