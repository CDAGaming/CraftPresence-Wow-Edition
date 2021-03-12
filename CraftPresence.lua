local CraftPresence = LibStub("AceAddon-3.0"):NewAddon("CraftPresence", "AceConsole-3.0", "AceEvent-3.0")

local L = LibStub("AceLocale-3.0"):GetLocale("CraftPresence")
local icon = LibStub("LibDBIcon-1.0")

local minimapState = { hide = false }
local addonVersion = ""

local CraftPresenceLDB = LibStub:GetLibrary("LibDataBroker-1.1"):NewDataObject(L["ADDON_NAME"], {
    type = "launcher",
    text = L["ADDON_NAME"],
    icon = string.format("Interface\\Addons\\%s\\images\\icon.blp", L["ADDON_NAME"]),
    OnClick = function(clickedframe, button)
        CraftPresence.ShowConfig()
    end,
    OnTooltipShow = function(tt)
        tt:AddLine(L["ADDON_NAME"])
        tt:AddLine(" ")
        tt:AddLine(L["ADDON_TOOLTIP_THREE"])
        tt:AddLine(" ")
        tt:AddLine(L["ADDON_TOOLTIP_FIVE"])
    end
})

-- ==================
-- Utilities
-- ==================

--- Determines whether the specified string contains digit characters
---
--- @param str string The input string to evaluate
---
--- @return boolean @ containsDigit
function CraftPresence:ContainsDigit(str)
    return string.find(str, "%d") and true or false
end

--- Determines whether the specified string starts with the specified pattern
---
--- @param String string The input string to evaluate
--- @param Start string The target string to locate at the first index
---
--- @return boolean @ startsWith
function CraftPresence:StartsWith(String, Start)
    return string.sub(String, 1, string.len(Start)) == Start
end

--- Formats the following word to proper casing (Xxxx)
---
--- @param str string The input string to evaluate
---
--- @return string @ formattedString
function CraftPresence:FormatWord(str)
    if (str == nil or str == "") then
        return str
    end
    str = string.lower(str)
    return (str:sub(1, 1):upper() .. str:sub(2))
end

--- Determines whether the specified value is within the specified range
---
--- @param value number The specified value to interpret
--- @param min number The minimum the value is allowed to be
--- @param max number The maximum the value is allowed to be
--- @param contains boolean Whether the range should include min and max
--- @param check_sanity boolean Whether to sanity check the min and max values
---
--- @return boolean @ isWithinValue
function CraftPresence:IsWithinValue(value, min, max, contains, check_sanity)
    -- Sanity checks
    local will_verify = (check_sanity == nil or check_sanity == true)
    if will_verify then
        if min > max then
            min = max
        end
        if max < min then
            max = min
        end
        if min < 0 then
            min = 0
        end
        if max < 0 then
            max = 0
        end
    end
    -- Contains Checks
    local will_contain = (contains ~= nil and contains == true)
    if not will_contain then
        min = min + 1
        max = max - 1
    end
    return (value >= min and value <= max)
end

-- ==================
-- RPC Data
-- ==================

-- Compatibility Data
local version, build, date, toc_version
local retail_toc = 90005
local classic_toc = 11306
-- Storage Data
local realmData = { "US", "KR", "EU", "TW", "CH" }
local global_placeholders = {}
local inner_placeholders = {}
local time_conditions = {}
-- Update State Data
local lastInstanceState
local lastLocalPlane
local hasInstanceChanged = false
local playerAlliance = "None"
local playerCovenant = "None"

--- Parses Game information to form placeholder information
--- @return table, table, table @ global_placeholders, inner_placeholders, time_conditions
function CraftPresence:ParseGameData(queued_global_placeholders)
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
    hasInstanceChanged = (
            ((lastInstanceState == nil) or (instanceType ~= lastInstanceState)) or
                    ((lastLocalPlane == nil) or (name ~= lastLocalPlane))
    )
    -- Covenant and Faction Setup
    -- Retail: If not in a covenant, or cannot identify that this instance belongs to Shadowlands
    -- Then use the Faction as the Alliance; otherwise setup Covenant Data
    local _, localizedFaction = UnitFactionGroup("player")
    local playerCovenantId = 0
    local playerCovenantData
    local playerCovenantRenown = 0
    if toc_version >= retail_toc then
        playerCovenantId = C_Covenants.GetActiveCovenantID()
        playerCovenantData = C_Covenants.GetCovenantData(playerCovenantId)
        playerCovenantRenown = C_CovenantSanctumUI.GetRenownLevel()
    end

    if hasInstanceChanged then
        if (playerCovenantId == 0 or not (
                (string.find(name, "Shadowlands")) or
                        (string.find(name, "Torghast")) or
                        (string.find(self:GetCurrentInstanceTier(), "Shadowlands"))
        )) then
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
    local queued_inner_placeholders = {
        ["@player_info@"] = "", -- Version-Dependent
        ["@player_name@"] = playerName,
        ["@title_name@"] = playerName, -- Version-Dependent
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
    local user_info_preset = playerPrefix .. playerName .. " - " .. (string.format(L["LEVEL_TAG_FORMAT"], playerLevel))
    if toc_version >= retail_toc then
        -- Extra Character Data
        local titleName = UnitPVPName("player")
        local avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = GetAverageItemLevel()
        local specId, specName, _, _, _, _ = GetSpecializationInfo(GetSpecialization())
        local roleName = self:FormatWord(GetSpecializationRoleByID(specId))
        -- Keystone Data
        local ownedKeystoneData = self:GetOwnedKeystone()
        local activeKeystoneData = self:GetActiveKeystone()
        difficultyInfo = difficultyName
        if (activeKeystoneData ~= nil and
                activeKeystoneData.formattedLevel ~= nil and activeKeystoneData.formattedLevel ~= ""
        ) then
            difficultyInfo = (difficultyInfo .. " (" .. activeKeystoneData.formattedLevel .. ")")
        end
        -- Inner Placeholder Adjustments
        queued_inner_placeholders["@title_name@"] = titleName
        queued_inner_placeholders["@player_info@"] = (user_info_preset .. " " .. specName .. " " .. playerClass)
        queued_inner_placeholders["@player_covenant_renown@"] = tostring(playerCovenantRenown)
        queued_inner_placeholders["@player_spec_name@"] = specName
        queued_inner_placeholders["@player_spec_role@"] = roleName
        queued_inner_placeholders["@item_level@"] = string.format("%.2f", avgItemLevel)
        queued_inner_placeholders["@item_level_equipped@"] = string.format("%.2f", avgItemLevelEquipped)
        queued_inner_placeholders["@item_level_pvp@"] = string.format("%.2f", avgItemLevelPvp)
        queued_inner_placeholders["@difficulty_info@"] = difficultyInfo
        queued_inner_placeholders["@active_keystone_level@"] = activeKeystoneData.formattedLevel
        queued_inner_placeholders["@active_keystone_affixes@"] = activeKeystoneData.formattedAffixes
        queued_inner_placeholders["@owned_keystone_level@"] = ownedKeystoneData.formattedLevel
        queued_inner_placeholders["@lockout_encounters@"] = lockoutData.formattedEncounterData
        queued_inner_placeholders["@lockout_current_encounters@"] = lockoutData.currentEncounters
        queued_inner_placeholders["@lockout_total_encounters@"] = lockoutData.totalEncounters
    elseif toc_version <= classic_toc then
        -- Inner Placeholder Adjustments
        queued_inner_placeholders["@player_info@"] = (user_info_preset .. " " .. playerClass)
    end
    -- Calculate limiting RPC conditions
    local queued_global_conditions = {
        ["#dungeon#"] = (
                queued_inner_placeholders["@instance_type@"] == "party" and not (string.find(name, "Garrison"))
        ),
        ["#raid#"] = (queued_inner_placeholders["@instance_type@"] == "raid"),
        ["#battleground#"] = (queued_inner_placeholders["@instance_type@"] == "pvp"),
        ["#arena#"] = (queued_inner_placeholders["@instance_type@"] == "arena")
    }
    local queued_inner_conditions = {
        ["@dead_state@"] = (UnitIsDeadOrGhost("player") and not UnitIsDead("player")),
        ["@lockout_encounters@"] = (lockoutData ~= nil and
                lockoutData.currentEncounters > 0 and lockoutData.totalEncounters > 0)
    }
    local queued_extra_conditions = {
        ["torghast"] = (string.find(name, "Torghast") and (queued_inner_placeholders["@instance_type@"] == "scenario"))
    }
    local queued_time_conditions = {
        ["start"] = (queued_global_conditions["#dungeon#"] or
                queued_global_conditions["#raid#"] or
                queued_global_conditions["#battleground#"] or
                queued_global_conditions["#arena#"]),
        ["start:extra"] = (queued_extra_conditions["torghast"])
    }
    local queued_override_placeholders = {
        "@dead_state@"
    }
    -- Extra Conditionals
    queued_global_conditions["#default#"] = (not queued_time_conditions["start"])

    local outputTable = {}

    for inKey, inValue in pairs(queued_global_placeholders) do
        local output = inValue:trim()
        -- If the Placeholders contained in the messages is an overrider,
        -- we replace the rest of the output with just that placeholder.
        --
        -- Note: Only applies towards inner-placeholders with subject to change data
        for _, overrideValue in pairs(queued_override_placeholders) do
            if string.find(output, overrideValue) then
                if (self:StartsWith(overrideValue, "@") and
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
    return outputTable, queued_inner_placeholders, queued_time_conditions
end

--- Synchronize Conditional Data with Game Info
--- @return table, table, table @ global_placeholders, inner_placeholders, time_conditions
function CraftPresence:SyncConditions()
    global_placeholders = {
        ["#dungeon#"] = self:GetFromDb("dungeonPlaceholderMessage"),
        ["#raid#"] = self:GetFromDb("raidPlaceholderMessage"),
        ["#battleground#"] = self:GetFromDb("battlegroundPlaceholderMessage"),
        ["#arena#"] = self:GetFromDb("arenaPlaceholderMessage"),
        ["#default#"] = self:GetFromDb("defaultPlaceholderMessage")
    }
    inner_placeholders = {}
    time_conditions = {}
    return self:ParseGameData(global_placeholders)
end

--- Creates and encodes a new RPC event from placeholder and conditional data
--- @return string @ newEncodedString
function CraftPresence:EncodeConfigData()
    -- Sync Variable Data
    global_placeholders, inner_placeholders, time_conditions = self:SyncConditions()
    -- RPC Data syncing
    local queued_details = self:GetFromDb("detailsMessage")
    local queued_state = self:GetFromDb("gameStateMessage")
    local queued_large_image_key = self:GetFromDb("largeImageKey")
    local queued_large_image_text = self:GetFromDb("largeImageMessage")
    local queued_small_image_key = self:GetFromDb("smallImageKey")
    local queued_small_image_text = self:GetFromDb("smallImageMessage")
    local queued_time_start = L["UNKNOWN_KEY"]
    local queued_time_end = L["UNKNOWN_KEY"]
    for key, value in pairs(global_placeholders) do
        queued_details = queued_details:gsub(key, value)
        queued_state = queued_state:gsub(key, value)
        queued_large_image_key = queued_large_image_key:gsub(key, value)
        queued_large_image_text = queued_large_image_text:gsub(key, value)
        queued_small_image_key = queued_small_image_key:gsub(key, value)
        queued_small_image_text = queued_small_image_text:gsub(key, value)
    end
    for innerKey, innerValue in pairs(inner_placeholders) do
        queued_details = queued_details:gsub(innerKey, innerValue)
        queued_state = queued_state:gsub(innerKey, innerValue)
        queued_large_image_key = queued_large_image_key:gsub(innerKey, innerValue)
        queued_large_image_text = queued_large_image_text:gsub(innerKey, innerValue)
        queued_small_image_key = queued_small_image_key:gsub(innerKey, innerValue)
        queued_small_image_text = queued_small_image_text:gsub(innerKey, innerValue)
    end
    for timeKey, timeValue in pairs(time_conditions) do
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
    return self:EncodeData(
            self:GetFromDb("clientId"),
            string.lower(queued_large_image_key:gsub("%s+", "_")),
            queued_large_image_text,
            string.lower(queued_small_image_key:gsub("%s+", "_")),
            queued_small_image_text,
            queued_details,
            queued_state,
            queued_time_start,
            queued_time_end
    )
end

--- Instructions to be called when the addon is loaded
function CraftPresence:OnInitialize()
    -- Main Initialization
    self.db = LibStub("AceDB-3.0"):New(L["ADDON_NAME"] .. "DB", self:GetSchemaDefaults())
    LibStub("AceConfig-3.0"):RegisterOptionsTable(L["ADDON_NAME"], self.getOptionsTable, {
        L["ADDON_ID"], L["ADDON_AFFIX"]
    })
    self.optionsFrame = LibStub("AceConfigDialog-3.0"):AddToBlizOptions(L["ADDON_NAME"])
    self.optionsFrame.default = self.ResetDB
    -- Command Registration
    self:RegisterChatCommand(L["ADDON_ID"], "ChatCommand")
    self:RegisterChatCommand(L["ADDON_AFFIX"], "ChatCommand")
    -- Icon Registration
    self:UpdateMinimapState(false)
    icon:Register(L["ADDON_NAME"], CraftPresenceLDB, minimapState)
end

--- Instructions to be called when the addon is enabled
function CraftPresence:OnEnable()
    -- Print initial data and register events
    -- depending on platform and config data
    addonVersion = GetAddOnMetadata(L["ADDON_NAME"], "Version")
    self:Print(string.format(L["ADDON_INTRO"], addonVersion))
    version, build, date, toc_version = GetBuildInfo()
    if self:GetFromDb("verboseMode") then
        self:Print(string.format(L["ADDON_BUILD_INFO"], version, build, date, toc_version))
    end
    -- Register Universal Events
    CraftPresence:AddTriggers("DispatchUpdate",
            "PLAYER_LOGIN", "PLAYER_LEVEL_CHANGED", "PLAYER_UNGHOST", "PLAYER_FLAGS_CHANGED",
            "ZONE_CHANGED", "ZONE_CHANGED_NEW_AREA", "ZONE_CHANGED_INDOORS",
            "BOSS_KILL", "ENCOUNTER_START", "ENCOUNTER_END"
    )
    -- Register Retail-Only Events
    if toc_version >= retail_toc then
        CraftPresence:AddTriggers("DispatchUpdate",
                "PLAYER_SPECIALIZATION_CHANGED",
                "CHALLENGE_MODE_START", "CHALLENGE_MODE_COMPLETED", "CHALLENGE_MODE_RESET",
                "SCENARIO_COMPLETED", "CRITERIA_UPDATE"
        )
    end
    -- Create initial frames and initial rpc update
    self:CreateFrames(self:GetFromDb("frameSize"))
    self:PaintMessageWait()
end

--- Registers the specified event for the subsequent argument values
---
--- @param event any The event tag to register with argument values
function CraftPresence:AddTriggers(event, ...)
    local args = { ... }
    for _, v in ipairs(args) do
        self:RegisterEvent(tostring(v), tostring(event))
    end
end

--- Retrieves Config Data based on the specified parameters
---
--- @param grp string The config group to retrieve
--- @param key string The config key to retrieve
---
--- @return any configValue
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

--- Dispatches and prepares a new frame update
function CraftPresence:DispatchUpdate()
    local delay = self:GetFromDb("callbackDelay")
    if self:IsWithinValue(delay, math.max(L["MINIMUM_CALLBACK_DELAY"], 1), L["MAXIMUM_CALLBACK_DELAY"], true) then
        C_Timer.After(delay, function()
            self:PaintMessageWait()
        end)
    else
        self:PaintMessageWait()
    end
end

--- Instructions to be called when the addon is disabled
function CraftPresence:OnDisable()
    -- Clean up Data before disabling
    self:Print(L["ADDON_CLOSE"])
    self:PaintMessageWait(true, false, true, self:EncodeData(self:GetFromDb("clientId")))
    icon:Hide(L["ADDON_NAME"])
    self:UnregisterAllEvents()
end

--- Updates the minimap status with config data
--- @param update_state boolean Whether or not to update the icon state
function CraftPresence:UpdateMinimapState(update_state)
    minimapState = { hide = not CraftPresence:GetFromDb("showMinimapIcon") }
    if update_state then
        if minimapState["hide"] then
            icon:Hide(L["ADDON_NAME"])
        else
            icon:Show(L["ADDON_NAME"])
        end
    end
end

--- Display the addon's config frame
function CraftPresence:ShowConfig()
    -- a bug can occur in blizzard's implementation of this call
    InterfaceOptionsFrame_OpenToCategory(CraftPresence.optionsFrame)
    InterfaceOptionsFrame_OpenToCategory(CraftPresence.optionsFrame)
end

--- Interprets the specified input to perform specific commands
--- @param input string The specified input to evaluate
function CraftPresence:ChatCommand(input)
    if not input or input:trim() == "" or input == "help" or input == "?" then
        self:Print(string.format(
                L["HELP_COMMANDS"], L["ADDON_NAME"],
                L["ADDON_AFFIX"], L["ADDON_ID"]
        ))
    else
        if input == "test" then
            if self:GetFromDb("debugMode") then
                self:PaintMessageWait(true, false, false)
            else
                self:Print(string.format(
                        L["ERROR_LOG"], string.format(
                                L["ERROR_COMMAND_CONFIG"], L["TITLE_DEBUG_MODE"]
                        )
                ))
            end
        elseif input == "clean" or input == "clear" then
            self:CleanFrames()
        elseif input == "update" then
            self:PaintMessageWait(true)
        elseif input == "config" then
            self:ShowConfig()
        elseif input == "minimap" then
            CraftPresence:UpdateMinimapSetting(not self.db.profile.showMinimapIcon)
        elseif input == "status" then
            if self:GetFromDb("verboseMode") then
                self:Print(self:GetLastEncoded())
            else
                self:Print(string.format(
                        L["ERROR_LOG"], string.format(
                                L["ERROR_COMMAND_CONFIG"], L["TITLE_VERBOSE_MODE"]
                        )
                ))
            end
        elseif input == "placeholders" then
            local placeholderString = L["VERBOSE_PLACEHOLDER_INTRO"]
            global_placeholders, inner_placeholders, time_conditions = self:SyncConditions()
            for key, value in pairs(global_placeholders) do
                placeholderString = placeholderString .. "\n " .. (string.format(
                        L["VERBOSE_PLACEHOLDER_DATA"], key, value
                ))
            end
            for innerKey, innerValue in pairs(inner_placeholders) do
                placeholderString = placeholderString .. "\n " .. (string.format(
                        L["VERBOSE_PLACEHOLDER_DATA"], innerKey, innerValue
                ))
            end
            placeholderString = placeholderString .. "\n" .. L["VERBOSE_PLACEHOLDER_NOTE"]
            self:Print(placeholderString)
        else
            LibStub("AceConfigCmd-3.0"):HandleCommand(L["ADDON_AFFIX"], L["ADDON_NAME"], input)
        end
    end
end
