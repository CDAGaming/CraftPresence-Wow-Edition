-- Programmatically generate list of all dungeons and raids, to be used in determining what expansion
-- the instance your are currently inside, actually belongs to.

-- Generate table of dungeon and raid instances by expansion
function GenerateInstanceTable()
    local instTable = {}

    instTable.trackedTypes = "party, raid"

    local numTiers = EJ_GetNumTiers()
    local numToScan = 10000
    local raid = false

    -- Run this twice, once for dungeons, once for raids
    for b=1, 2 do
        local instanceType = ""
        if raid then instanceType = "Raids" else instanceType = "Dungeons" end
        instTable[instanceType] = {}

        for t=1, numTiers do
            EJ_SelectTier(t)
            local tierName = EJ_GetTierInfo(t)
            instTable[instanceType][tierName] = {}

            for i=1, numToScan do
                local id, name = EJ_GetInstanceByIndex(i, raid)
                if name then
                    instTable[instanceType][tierName][name] = id
                end
            end
        end

        -- Flip state, do raids next round
        raid = not raid
    end

    return instTable
end


--[[Figure out which expansion our current instance belongs to.

Problem: Remade dungeons, such as Deadmines, belong to more than 1 expansion,
but retain only one name and instanceID. Making a simple list of ID's insufficient.

At the time of writing (Legion), remade Vanilla dungeons are all Heroic variants.
Meaning, if we enter a "vanilla" instance, on Heroic difficulty, it must be a remake.
We can then simply skip scanning Vanilla, the next hit will accurately tell us our real tier.

This can break if something like a TBC instance is remade, for example, so this is a fragile approach.
--]]
function GetCurrentInstanceTier()
    -- Check that the InstanceTable even exists, if not, create it
    InstanceTable = InstanceTable or GenerateInstanceTable()

    -- Bail out if we're not even in an instance!
    if not IsInInstance() then return "NotAnInstance" end


    -- Generate a chronologically ordered list of expansion names
    local tierList = {}

    for i = 1, EJ_GetNumTiers() do
        tierList[i] = EJ_GetTierInfo(i)
    end


    local name, instanceType, difficulty, difficultyName = GetInstanceInfo()
    local instanceID = EJ_GetInstanceForMap(C_Map.GetBestMapForUnit("player"))

    -- Determine the search key to be used. This should be refactored somehow.
    local searchType = (
            function()
                if instanceType == "party" then
                    return "Dungeons"
                elseif instanceType == "raid" then
                    return "Raids"
                end
            end
    )()

    -- First, check if we even track this type of instance, if not, bail out
    if not string.find(InstanceTable.trackedTypes, instanceType) then return "UnknownInstanceType" end

    -- Second, is it Heroic? If so, skip all of Vanilla. Else, search the entire instance table
    local startIndex = (difficulty == 2) and 2 or 1


    -- Perform the actual search, scanning by instanceID, skipping Classic if we're in Heroic
    for i = startIndex, #tierList do
        subTable = InstanceTable[searchType][tierList[i]]

        for k, v in pairs(subTable) do
            if (instanceID == v) then
                return tierList[i]
            end
        end
    end
    return "UnknownTier"
end


-- Celebratory print()
local name, instanceType, difficulty, difficultyName = GetInstanceInfo()
print( format('You are in "%s (%s)", from the "%s" expansion.', name, difficultyName, GetCurrentInstanceTier()))