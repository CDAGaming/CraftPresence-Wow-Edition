local CraftPresence = LibStub("AceAddon-3.0"):NewAddon("CraftPresence", "AceConsole-3.0", "AceEvent-3.0")

local L = LibStub("AceLocale-3.0"):GetLocale("CraftPresence")

-- ==================
-- Utilities
-- ==================

function CraftPresence:ContainsDigit(str)
    return string.find(str, "%d") and true or false
end

function CraftPresence:StartsWith(String, Start)
    return string.sub(String, 1, string.len(Start)) == Start
end

function CraftPresence:FormatWord(str)
    if (str == nil or str == "") then
        return str
    end
    str = string.lower(str)
    return (str:sub(1, 1):upper() .. str:sub(2))
end

-- ==================
-- RPC Data
-- ==================

-- Compatibility Data
local version, build, date, toc_version
local retail_toc = 90002
local classic_toc = 11306
-- Storage Data
local realmData = { "US", "KR", "EU", "TW", "CH" }
local stored_global_placeholders = {}
local stored_inner_placeholders = {}
local stored_time_conditions = {}
-- Update State Data
local lastInstanceState
local lastLocalPlane
local hasInstanceChanged = false
local playerAlliance = "None"
local playerCovenant = "None"

function CraftPresence:ParsePlaceholderData(global_placeholders)
    local name, instanceType, difficultyID, difficultyName, maxPlayers,
    dynamicDifficulty, isDynamic, instanceID, instanceGroupSize, LfgDungeonID = GetInstanceInfo()
    local difficultyInfo = difficultyName
    -- Retail: Lockout Data is only available for Retail Wow
    local lockoutData
    if toc_version >= retail_toc then
        lockoutData = self:GetCurrentLockoutData()
    end
    -- Player Name Tweaks (DND/AFK Data)
    local playerName = UnitName("player")
    local isAfk = UnitIsAFK("player")
    local isOnDnd = UnitIsDND("player")
    local playerStatus
    local playerPrefix = ""
    if isAfk then
        playerStatus = L["AFK_LABEL"]
    elseif isOnDnd then
        playerStatus = L["DND_LABEL"]
    end
    -- Parse Player Status
    if not (playerStatus == nil) then
        playerPrefix = ("(" .. playerStatus .. ")") .. " "
    else
        playerStatus = L["ONLINE_LABEL"]
    end
    -- Extra Player Data
    local playerLevel = UnitLevel("player")
    local playerRealm = GetRealmName()
    local playerRegion = realmData[GetCurrentRegion()]
    local playerClass = UnitClass("player")
    hasInstanceChanged = ((lastInstanceState == nil) or (instanceType ~= lastInstanceState)) or ((lastLocalPlane == nil) or (name ~= lastLocalPlane))
    -- Covenant and Faction Setup
    -- Retail: If not in a covenant, or cannot identify that this instance belongs to Shadowlands
    -- Then use the Faction as the Alliance; otherwise setup Covenant Data
    local englishFaction, localizedFaction = UnitFactionGroup("player")
    local playerCovenantId = 0
    local playerCovenantData
    local playerCovenantRenown = 0
    if hasInstanceChanged then
        if toc_version >= retail_toc then
            playerCovenantId = C_Covenants.GetActiveCovenantID()
            playerCovenantData = C_Covenants.GetCovenantData(playerCovenantId)
            playerCovenantRenown = C_CovenantSanctumUI.GetRenownLevel()
        end

        if playerCovenantId == 0 or not ((string.find(name, "Shadowlands")) or (string.find(name, "Torghast")) or (string.find(self:GetCurrentInstanceTier(), "Shadowlands"))) then
            playerAlliance = localizedFaction
            playerCovenant = "None"
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
    if (zone_name == nil or zone_name == "") then
        zone_name = L["ZONE_NAME_UNKNOWN"]
    end
    -- Format the zone info based on zone data
    if (sub_name == nil or sub_name == "") then
        formatted_zone_info = zone_name
        sub_name = L["ZONE_NAME_UNKNOWN"]
    else
        formatted_zone_info = (sub_name .. " - " .. zone_name)
    end
    -- Calculate Inner Placeholders
    local inner_placeholders = {
        ["@player_info@"] = "", -- Version-Dependent
        ["@player_name@"] = playerName,
        ["@player_level@"] = playerLevel,
        ["@player_class@"] = playerClass,
        ["@player_status@"] = playerStatus,
        ["@player_alliance@"] = playerAlliance,
        ["@player_covenant@"] = playerCovenant,
        ["@player_covenant_renown@"] = "0", -- Retail-Only
        ["@player_faction@"] = localizedFaction,
        ["@player_spec_name@"] = "", -- Retail-Only
        ["@player_spec_role@"] = "", -- Retail-Only
        ["@item_level@"] = "0", -- Retail-Only
        ["@item_level_equipped@"] = "0", -- Retail-Only
        ["@item_level_pvp@"] = "0", -- Retail-Only
        ["@realm_info@"] = (playerRegion .. " - " .. playerRealm),
        ["@player_region@"] = playerRegion,
        ["@player_realm@"] = playerRealm,
        ["@zone_info@"] = formatted_zone_info,
        ["@zone_name@"] = zone_name,
        ["@sub_zone_name@"] = sub_name,
        ["@dead_state@"] = self:GetFromDb("deadStateInnerMessage"),
        ["@difficulty_name@"] = difficultyName,
        ["@difficulty_info@"] = difficultyInfo, -- Retail-Effected
        ["@active_keystone_level@"] = "", -- Retail-Only
        ["@active_keystone_affixes@"] = "", -- Retail-Only
        ["@owned_keystone_level@"] = "", -- Retail-Only
        ["@instance_type@"] = instanceType,
        ["@localized_name@"] = name,
        ["@instance_difficulty@"] = tostring(difficultyID),
        ["@max_players@"] = tostring(maxPlayers),
        ["@dynamic_difficulty@"] = tostring(dynamicDifficulty),
        ["@is_dynamic@"] = tostring(isDynamic),
        ["@instance_id@"] = tostring(instanceID),
        ["@instance_group_size@"] = tostring(instanceGroupSize),
        ["@lfg_dungeon_id@"] = tostring(LfgDungeonID),
        ["@lockout_encounters@"] = "", -- Retail-Only
        ["@lockout_current_encounters@"] = 0, -- Retail-Only
        ["@lockout_total_encounters@"] = 0 -- Retail-Only
    }
    -- Version Dependent Data
    if toc_version >= retail_toc then
        -- Extra Character Data
        local avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = GetAverageItemLevel()
        local specId, specName, specDescription, specIcon, specBackground, specRoleId = GetSpecializationInfo(GetSpecialization())
        local roleName = self:FormatWord(GetSpecializationRoleByID(specId))
        -- Keystone Data
        local ownedKeystoneData = self:GetOwnedKeystone()
        local activeKeystoneData = self:GetActiveKeystone()
        difficultyInfo = difficultyName
        if activeKeystoneData ~= nil and activeKeystoneData.formattedLevel ~= nil and activeKeystoneData.formattedLevel ~= "" then
            difficultyInfo = (difficultyInfo .. " (" .. activeKeystoneData.formattedLevel .. ")")
        end
        -- Inner Placeholder Adjustments
        inner_placeholders["@player_info@"] = (playerPrefix .. playerName .. " - " .. (string.format(L["LEVEL_TAG_FORMAT"], playerLevel)) .. " " .. specName .. " " .. playerClass)
        inner_placeholders["@player_covenant_renown@"] = tostring(playerCovenantRenown)
        inner_placeholders["@player_spec_name@"] = specName
        inner_placeholders["@player_spec_role@"] = roleName
        inner_placeholders["@item_level@"] = string.format("%.2f", avgItemLevel)
        inner_placeholders["@item_level_equipped@"] = string.format("%.2f", avgItemLevelEquipped)
        inner_placeholders["@item_level_pvp@"] = string.format("%.2f", avgItemLevelPvp)
        inner_placeholders["@difficulty_info@"] = difficultyInfo
        inner_placeholders["@active_keystone_level@"] = activeKeystoneData.formattedLevel
        inner_placeholders["@active_keystone_affixes@"] = activeKeystoneData.formattedAffixes
        inner_placeholders["@owned_keystone_level@"] = ownedKeystoneData.formattedLevel
        inner_placeholders["@lockout_encounters@"] = lockoutData.formattedEncounterData
        inner_placeholders["@lockout_current_encounters@"] = lockoutData.currentEncounters
        inner_placeholders["@lockout_total_encounters@"] = lockoutData.totalEncounters
    elseif toc_version <= classic_toc then
        -- Inner Placeholder Adjustments
        inner_placeholders["@player_info@"] = (playerPrefix .. playerName .. " - " .. (string.format(L["LEVEL_TAG_FORMAT"], playerLevel)) .. " " .. playerClass)
    end
    -- Calculate limiting RPC conditions
    local global_conditions = {
        ["#dungeon#"] = (inner_placeholders["@instance_type@"] == "party" and not (string.find(name, "Garrison"))),
        ["#raid#"] = (inner_placeholders["@instance_type@"] == "raid"),
        ["#battleground#"] = (inner_placeholders["@instance_type@"] == "pvp"),
        ["#arena#"] = (inner_placeholders["@instance_type@"] == "arena")
    }
    local inner_conditions = {
        ["@dead_state@"] = (UnitIsDeadOrGhost("player") and not UnitIsDead("player")),
        ["@lockout_encounters@"] = (lockoutData ~= nil and lockoutData.currentEncounters > 0 and lockoutData.totalEncounters > 0)
    }
    local extra_conditions = {
        ["torghast"] = (string.find(name, "Torghast") and (inner_placeholders["@instance_type@"] == "scenario"))
    }
    local time_conditions = {
        ["start"] = (global_conditions["#dungeon#"] or global_conditions["#raid#"] or global_conditions["#battleground#"] or global_conditions["#arena#"]),
        ["start:extra"] = (extra_conditions["torghast"])
    }
    local override_placeholders = {
        "@dead_state@"
    }
    -- Extra Conditionals
    global_conditions["#default#"] = (not time_conditions["start"])

    local outputTable = {}

    for inKey, inValue in pairs(global_placeholders) do
        local output = inValue:trim()
        -- If the Placeholders contained in the messages is an overrider, we replace the rest of the output with just that placeholder
        -- Only applies towards inner-placeholders with subject to change data
        for overrideKey, overrideValue in pairs(override_placeholders) do
            if string.find(output, overrideValue) then
                if (self:StartsWith(overrideValue, "@") and (inner_conditions[overrideValue] == nil or inner_conditions[overrideValue])) then
                    output = overrideValue
                    break
                end
            end
        end

        if (global_conditions[inKey] == nil or global_conditions[inKey]) then
            for key, value in pairs(inner_placeholders) do
                if (inner_conditions[key] == nil or inner_conditions[key]) then
                    output = output:gsub(key, value)
                else
                    output = output:gsub(key, "")
                end
            end
            outputTable[inKey] = output:trim()
        else
            outputTable[inKey] = ""
        end
    end
    -- Update Instance Status before exiting method
    if hasInstanceChanged then
        lastInstanceState = instanceType
        lastLocalPlane = name
    end
    return outputTable, inner_placeholders, time_conditions
end

function CraftPresence:EncodeConfigData()
    -- Re-Initialize and Sync Placeholder and Conditional Data
    stored_global_placeholders = {
        ["#dungeon#"] = self:GetFromDb("dungeonPlaceholderMessage"),
        ["#raid#"] = self:GetFromDb("raidPlaceholderMessage"),
        ["#battleground#"] = self:GetFromDb("battlegroundPlaceholderMessage"),
        ["#arena#"] = self:GetFromDb("arenaPlaceholderMessage"),
        ["#default#"] = self:GetFromDb("defaultPlaceholderMessage")
    }
    stored_inner_placeholders = {}
    stored_time_conditions = {}
    stored_global_placeholders, stored_inner_placeholders, stored_time_conditions = self:ParsePlaceholderData(stored_global_placeholders)
    -- RPC Data syncing
    local queued_details = self:GetFromDb("detailsMessage")
    local queued_state = self:GetFromDb("gameStateMessage")
    local queued_large_image_key = self:GetFromDb("largeImageKey")
    local queued_large_image_text = self:GetFromDb("largeImageMessage")
    local queued_small_image_key = self:GetFromDb("smallImageKey")
    local queued_small_image_text = self:GetFromDb("smallImageMessage")
    local queued_time_start = L["UNKNOWN_KEY"]
    local queued_time_end = L["UNKNOWN_KEY"]
    for key, value in pairs(stored_global_placeholders) do
        queued_details = queued_details:gsub(key, value)
        queued_state = queued_state:gsub(key, value)
        queued_large_image_key = queued_large_image_key:gsub(key, value)
        queued_large_image_text = queued_large_image_text:gsub(key, value)
        queued_small_image_key = queued_small_image_key:gsub(key, value)
        queued_small_image_text = queued_small_image_text:gsub(key, value)
    end
    for innerKey, innerValue in pairs(stored_inner_placeholders) do
        queued_details = queued_details:gsub(innerKey, innerValue)
        queued_state = queued_state:gsub(innerKey, innerValue)
        queued_large_image_key = queued_large_image_key:gsub(innerKey, innerValue)
        queued_large_image_text = queued_large_image_text:gsub(innerKey, innerValue)
        queued_small_image_key = queued_small_image_key:gsub(innerKey, innerValue)
        queued_small_image_text = queued_small_image_text:gsub(innerKey, innerValue)
    end
    for timeKey, timeValue in pairs(stored_time_conditions) do
        if timeValue then
            if (string.find(timeKey, "start")) then
                if hasInstanceChanged then
                    queued_time_start = "generated"
                else
                    queued_time_start = "last"
                end
            elseif (string.find(timeKey, "end")) then
                if hasInstanceChanged then
                    queued_time_end = "generated"
                else
                    queued_time_end = "last"
                end
            end
        end
    end
    return self:EncodeData(self:GetFromDb("clientId"), string.lower(queued_large_image_key:gsub("%s+", "_")), queued_large_image_text, string.lower(queued_small_image_key:gsub("%s+", "_")), queued_small_image_text, queued_details, queued_state, queued_time_start, queued_time_end)
end

local AddonDB_Defaults = {
    global = {
        Characters = {
            ['*'] = {
            },
        },
    },
}

function CraftPresence:OnInitialize()
    -- Called when the addon is loaded
    self.db = LibStub("AceDB-3.0"):New(L["ADDON_NAME"] .. "DB", AddonDB_Defaults)
    LibStub("AceConfig-3.0"):RegisterOptionsTable(L["ADDON_NAME"], self.getOptionsTable)
    self.optionsFrame = LibStub("AceConfigDialog-3.0"):AddToBlizOptions(L["ADDON_NAME"])
    self.optionsFrame.default = self.ResetDB
    -- Command Registration
    self:RegisterChatCommand("craftpresence", "ChatCommand")
    self:RegisterChatCommand("cp", "ChatCommand")
end

function CraftPresence:OnEnable()
    -- Called when the addon is enabled
    self:Print(L["ADDON_INTRO"])
    version, build, date, toc_version = GetBuildInfo()
    self:RegisterEvent("PLAYER_LOGIN", "DispatchUpdate")
    self:RegisterEvent("ZONE_CHANGED", "DispatchUpdate")
    self:RegisterEvent("ZONE_CHANGED_NEW_AREA", "DispatchUpdate")
    self:RegisterEvent("ZONE_CHANGED_INDOORS", "DispatchUpdate")
    self:RegisterEvent("PLAYER_UNGHOST", "DispatchUpdate")
    self:RegisterEvent("PLAYER_FLAGS_CHANGED", "DispatchUpdate")
    if toc_version >= retail_toc then
        self:RegisterEvent("PLAYER_SPECIALIZATION_CHANGED", "DispatchUpdate")
    end

    self:CreateFrames()
    self:PaintMessageWait()
end

function CraftPresence:GetFromDb(grp, key, ...)
    local DB_DEFAULTS = CraftPresence:GetDefaults()
    if CraftPresence.db.profile[grp] == nil then
        CraftPresence.db.profile[grp] = DB_DEFAULTS.profile[grp]
    end
    if not key then
        return CraftPresence.db.profile[grp]
    end
    if CraftPresence.db.profile[grp][key] == nil then
        CraftPresence.db.profile[grp][key] = DB_DEFAULTS.profile[grp][key]
    end
    return CraftPresence.db.profile[grp][key]
end

function CraftPresence:DispatchUpdate()
    local delay = self:GetFromDb("callbackDelay")
    if delay >= 1 and delay <= 60 then
        C_Timer.After(delay, function()
            self:PaintMessageWait()
        end)
    else
        self:PaintMessageWait()
    end
end

function CraftPresence:OnDisable()
    -- Called when the addon is disabled
end

function CraftPresence:ChatCommand(input)
    if not input or input:trim() == "" then
        -- a bug can occur in blizzard's implementation of this call
        InterfaceOptionsFrame_OpenToCategory(self.optionsFrame)
        InterfaceOptionsFrame_OpenToCategory(self.optionsFrame)
    else
        if input == "test" then
            self:TestFrames()
        elseif input == "clean" or input == "clear" then
            self:CleanFrames()
        elseif input == "update" then
            self:PaintMessageWait(true)
        elseif input == "status" then
            if self:GetFromDb("verboseMode") and self:GetFromDb("showLoggingInChat") then
                self:Print(self:PrintLastEncoded())
            else
                self:Print(string.format(L["ERROR_LOG"], string.format(L["ERROR_COMMAND_CONFIG"], (L["TITLE_VERBOSE_MODE"] .. ", " .. L["TITLE_SHOW_LOGGING_IN_CHAT"]))))
            end
        elseif input == "placeholders" then
            if self:GetFromDb("verboseMode") and self:GetFromDb("showLoggingInChat") then
                self:Print(string.format(L["VERBOSE_LOG"], L["VERBOSE_PLACEHOLDER_INTRO"]))
                for key, value in pairs(stored_global_placeholders) do
                    self:Print(string.format(L["VERBOSE_LOG"], string.format(L["VERBOSE_PLACEHOLDER_DATA"], "Global", key, value)))
                end
                for innerKey, innerValue in pairs(stored_inner_placeholders) do
                    self:Print(string.format(L["VERBOSE_LOG"], string.format(L["VERBOSE_PLACEHOLDER_DATA"], "Inner", innerKey, innerValue)))
                end
            else
                self:Print(string.format(L["ERROR_LOG"], string.format(L["ERROR_COMMAND_CONFIG"], (L["TITLE_VERBOSE_MODE"] .. ", " .. L["TITLE_SHOW_LOGGING_IN_CHAT"]))))
            end
        else
            LibStub("AceConfigCmd-3.0"):HandleCommand("cp", "CraftPresence", input)
        end
    end
end
