local CraftPresence = LibStub("AceAddon-3.0"):NewAddon("CraftPresence", "AceConsole-3.0", "AceEvent-3.0")

local L = LibStub("AceLocale-3.0"):GetLocale("CraftPresence")
local icon = LibStub("LibDBIcon-1.0")

local minimapState = { hide = false }
local addonVersion = ""

local realmData = { "US", "KR", "EU", "TW", "CH" }
local buildData = {}
local compatData = {}

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
-- RPC Data
-- ==================

-- Storage Data
local global_placeholders = {}
local inner_placeholders = {}
local time_conditions = {}
-- Update State Data
local lastInstanceState
local lastAreaName
local lastEventName
local hasInstanceChanged = false
local playerAlliance = L["TYPE_NONE"]
local playerCovenant = L["TYPE_NONE"]

--- Parses Game information to form placeholder information
---
--- @param queued_global_placeholders table Queued Global Plaseholders to interpret
--- @param force_instance_change boolean Whether to force an instance change (Default: false)
---
--- @return table, table, table @ global_placeholders, inner_placeholders, time_conditions
function CraftPresence:ParseGameData(queued_global_placeholders, force_instance_change)
    force_instance_change = force_instance_change ~= nil and force_instance_change == true
    -- Variable Initialization
    local name, instanceType, difficultyID, difficultyName, maxPlayers,
    dynamicDifficulty, isDynamic, instanceID, instanceGroupSize, LfgDungeonID = GetInstanceInfo()
    local difficultyInfo = difficultyName
    local lockoutData = {}
    if buildData["toc_version"] >= compatData["6.0.x"] then
        lockoutData = self:GetCurrentLockoutData(true)
    end
    -- Player Data
    local playerName = UnitName("player")
    local playerStatus, playerPrefix = self:GetPlayerStatus("player", true)
    -- Extra Player Data
    local playerLevel = UnitLevel("player")
    local playerRealm = GetRealmName()
    local playerRegionId = 1
    if buildData["toc_version"] >= compatData["6.0.x"] then
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
    if buildData["toc_version"] >= compatData["9.0.x"] then
        playerCovenantId = C_Covenants.GetActiveCovenantID()
        playerCovenantData = C_Covenants.GetCovenantData(playerCovenantId)
        playerCovenantRenown = C_CovenantSanctumUI.GetRenownLevel()
    end
    -- Covenant and/or Faction data is only updated if the instance is changed
    if hasInstanceChanged then
        if (playerCovenantId == 0 or not (
                (string.find(name, "Shadowlands")) or
                        (string.find(name, "Torghast")) or
                        (string.find(self:GetCurrentInstanceTier(), "Shadowlands"))
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
    if (zone_name == nil or zone_name == "") then
        zone_name = L["TYPE_UNKNOWN"]
    end
    -- Format the zone info based on zone data
    if (sub_name == nil or sub_name == "") then
        formatted_zone_info = zone_name
        sub_name = L["TYPE_UNKNOWN"]
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
    if buildData["toc_version"] >= compatData["5.0.x"] then
        -- Extra Character Data
        local titleName = UnitPVPName("player")
        local avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = GetAverageItemLevel()
        local specId
        local specName = ""
        local roleName = ""
        local specInfo = GetSpecialization()
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
        queued_inner_placeholders["@item_level@"] = string.format("%.2f", avgItemLevel or 0)
        queued_inner_placeholders["@item_level_equipped@"] = string.format("%.2f", avgItemLevelEquipped or 0)
        queued_inner_placeholders["@item_level_pvp@"] = string.format("%.2f", avgItemLevelPvp or 0)
        queued_inner_placeholders["@difficulty_info@"] = difficultyInfo
        queued_inner_placeholders["@active_keystone_level@"] = activeKeystoneData.formattedLevel
        queued_inner_placeholders["@active_keystone_affixes@"] = activeKeystoneData.formattedAffixes
        queued_inner_placeholders["@owned_keystone_level@"] = ownedKeystoneData.formattedLevel
        queued_inner_placeholders["@lockout_encounters@"] = lockoutData.formattedEncounterData
        queued_inner_placeholders["@lockout_current_encounters@"] = lockoutData.currentEncounters
        queued_inner_placeholders["@lockout_total_encounters@"] = lockoutData.totalEncounters
    else
        -- Inner Placeholder Adjustments
        queued_inner_placeholders["@player_info@"] = (user_info_preset .. " " .. playerClass)
    end
    -- Calculate limiting RPC conditions
    -- If the condition is true, the placeholder will be active
    local queued_global_conditions = {
        ["#dungeon#"] = (
                queued_inner_placeholders["@instance_type@"] == "party" and not (string.find(name, "Garrison"))
        ),
        ["#raid#"] = (queued_inner_placeholders["@instance_type@"] == "raid"),
        ["#battleground#"] = (queued_inner_placeholders["@instance_type@"] == "pvp"),
        ["#arena#"] = (queued_inner_placeholders["@instance_type@"] == "arena")
    }
    local queued_inner_conditions = {
        ["@lockout_encounters@"] = (lockoutData ~= nil and
                (lockoutData.currentEncounters or 0) > 0 and (lockoutData.totalEncounters or 0) > 0)
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
    -- If these placeholders are active, they will override the field they are in
    local queued_override_placeholders = {
        -- N/A
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
        lastAreaName = name
    end
    return outputTable, queued_inner_placeholders, queued_time_conditions
end

--- Synchronize Conditional Data with Game Info
---
--- @param force_instance_change boolean Whether to force an instance change (Default: false)
---
--- @return table, table, table @ global_placeholders, inner_placeholders, time_conditions
function CraftPresence:SyncConditions(force_instance_change)
    global_placeholders = {
        ["#dungeon#"] = self:GetFromDb("dungeonPlaceholderMessage"),
        ["#raid#"] = self:GetFromDb("raidPlaceholderMessage"),
        ["#battleground#"] = self:GetFromDb("battlegroundPlaceholderMessage"),
        ["#arena#"] = self:GetFromDb("arenaPlaceholderMessage"),
        ["#default#"] = self:GetFromDb("defaultPlaceholderMessage")
    }
    inner_placeholders = {}
    time_conditions = {}
    return self:ParseGameData(global_placeholders, force_instance_change)
end

--- Creates and encodes a new RPC event from placeholder and conditional data
---
--- @param force_instance_change boolean Whether to force an instance change (Default: false)
---
--- @return string @ newEncodedString
function CraftPresence:EncodeConfigData(force_instance_change)
    -- Sync Variable Data
    global_placeholders, inner_placeholders, time_conditions = self:SyncConditions(force_instance_change)
    -- RPC Data syncing
    local queued_details = self:GetFromDb("detailsMessage")
    local queued_state = self:GetFromDb("gameStateMessage")
    local queued_large_image_key = self:GetFromDb("largeImageKey")
    local queued_large_image_text = self:GetFromDb("largeImageMessage")
    local queued_small_image_key = self:GetFromDb("smallImageKey")
    local queued_small_image_text = self:GetFromDb("smallImageMessage")
    local queued_time_start = L["UNKNOWN_KEY"]
    local queued_time_end = L["UNKNOWN_KEY"]
    local split_key = L["ARRAY_SPLIT_KEY"]
    local queued_primary_button = (
            self:GetFromDb("primaryButton", "label") .. split_key .. self:GetFromDb("primaryButton", "url")
    )
    local queued_secondary_button = (
            self:GetFromDb("secondaryButton", "label") .. split_key .. self:GetFromDb("secondaryButton", "url")
    )
    -- Global Placeholder Syncing
    for key, value in pairs(global_placeholders) do
        queued_details = queued_details:gsub(key, value)
        queued_state = queued_state:gsub(key, value)
        queued_large_image_key = queued_large_image_key:gsub(key, value)
        queued_large_image_text = queued_large_image_text:gsub(key, value)
        queued_small_image_key = queued_small_image_key:gsub(key, value)
        queued_small_image_text = queued_small_image_text:gsub(key, value)
        queued_primary_button = queued_primary_button:gsub(key, value)
        queued_secondary_button = queued_secondary_button:gsub(key, value)
    end
    -- Inner Placeholder Syncing
    for innerKey, innerValue in pairs(inner_placeholders) do
        queued_details = queued_details:gsub(innerKey, innerValue)
        queued_state = queued_state:gsub(innerKey, innerValue)
        queued_large_image_key = queued_large_image_key:gsub(innerKey, innerValue)
        queued_large_image_text = queued_large_image_text:gsub(innerKey, innerValue)
        queued_small_image_key = queued_small_image_key:gsub(innerKey, innerValue)
        queued_small_image_text = queued_small_image_text:gsub(innerKey, innerValue)
        queued_primary_button = queued_primary_button:gsub(innerKey, innerValue)
        queued_secondary_button = queued_secondary_button:gsub(innerKey, innerValue)
    end
    -- Time Condition Syncing
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
            self:GetFromDb("clientId"):gsub("%s+", " "),
            string.lower(queued_large_image_key:gsub("%s+", "_")),
            queued_large_image_text:gsub("%s+", " "),
            string.lower(queued_small_image_key:gsub("%s+", "_")),
            queued_small_image_text:gsub("%s+", " "),
            queued_details:gsub("%s+", " "),
            queued_state:gsub("%s+", " "),
            queued_time_start:gsub("%s+", " "),
            queued_time_end:gsub("%s+", " "),
            queued_primary_button:gsub("%s+", " "),
            queued_secondary_button:gsub("%s+", " ")
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
    self:Print(string.format(L["ADDON_INTRO"], self:GetVersion()))
    buildData = self:GetBuildInfo()
    compatData = self:GetCompatibilityInfo()
    if self:GetFromDb("verboseMode") then
        self:Print(string.format(L["ADDON_BUILD_INFO"], self:SerializeTable(buildData)))
    end
    -- Register Universal Events
    CraftPresence:AddTriggers("DispatchUpdate",
            "PLAYER_LOGIN", "PLAYER_LEVEL_UP",
            "PLAYER_ALIVE", "PLAYER_DEAD", "PLAYER_FLAGS_CHANGED",
            "ZONE_CHANGED", "ZONE_CHANGED_NEW_AREA", "ZONE_CHANGED_INDOORS",
            "UPDATE_INSTANCE_INFO"
    )
    -- Register Version-Specific Events
    if buildData["toc_version"] >= compatData["5.0.x"] then
        CraftPresence:AddTriggers("DispatchUpdate",
                "PLAYER_SPECIALIZATION_CHANGED"
        )
    end
    if buildData["toc_version"] >= compatData["6.0.x"] then
        CraftPresence:AddTriggers("DispatchUpdate",
                "ACTIVE_TALENT_GROUP_CHANGED",
                "CHALLENGE_MODE_START", "CHALLENGE_MODE_COMPLETED", "CHALLENGE_MODE_RESET",
                "SCENARIO_COMPLETED", "CRITERIA_COMPLETE"
        )
    end
    if buildData["toc_version"] >= compatData["8.0.x"] then
        CraftPresence:AddTriggers("DispatchUpdate",
                "PLAYER_LEVEL_CHANGED"
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

--- Prepares and Dispatches a new frame update, given the specified arguments
function CraftPresence:DispatchUpdate(...)
    local args = { ... }
    if args ~= nil then
        -- Ignore Event Conditional Setup
        -- Format: [EVENT_NAME] = ignore_event_condition
        local eventName = args[1]
        local ignore_event = false
        local ignore_event_conditions = {
            ["PLAYER_FLAGS_CHANGED"] = (args[2] ~= "player" or (
                    self:GetLastPlayerStatus() == self:GetPlayerStatus(args[2], false)
            )),
            ["UPDATE_INSTANCE_INFO"] = (not IsInInstance() or
                    lastEventName == eventName or
                    self:GetCachedLockout() == self:GetCurrentLockoutData(false)
            ),
            ["PLAYER_ALIVE"] = (lastEventName ~= "PLAYER_DEAD"),
            ["PLAYER_LEVEL_UP"] = (lastEventName == "PLAYER_LEVEL_CHANGED"),
            ["PLAYER_LEVEL_CHANGED"] = (lastEventName == "PLAYER_LEVEL_UP")
        }
        for key, value in pairs(ignore_event_conditions) do
            if eventName == key and value then
                ignore_event = true
                break
            end
        end

        -- Skip Execution of method if ignore condition hit
        -- Otherwise, proceed on to reading event data
        if ignore_event then
            return
        else
            -- Store event name, and print logging info if needed
            lastEventName = eventName
            local logPrefix = L["INFO_EVENT_PROCESSING"]
            if self:IsTimerLocked() then
                logPrefix = L["INFO_EVENT_SKIPPED"]
            end
            -- Logging is different depending on verbose/debug states
            if self:GetFromDb("debugMode") then
                if self:GetFromDb("verboseMode") then
                    self:Print(string.format(
                            L["VERBOSE_LOG"], string.format(
                                    logPrefix, self:SerializeTable(args)
                            )
                    ))
                else
                    self:Print(string.format(
                            L["DEBUG_LOG"], string.format(
                                    logPrefix, eventName
                            )
                    ))
                end
            end
            -- If the timer is locked, exit the method at this stage
            -- Otherwise, proceed to frame processing
            if self:IsTimerLocked() then
                return
            else
                -- If we are using a callback delay,
                -- the timer is locked if in a skip-style pipeline.
                -- Otherwise, execute the next method normally.
                local delay = self:GetFromDb("callbackDelay")
                local min_delay = math.max(L["MINIMUM_CALLBACK_DELAY"], 1)
                if self:IsWithinValue(delay, min_delay, L["MAXIMUM_CALLBACK_DELAY"], true) then
                    self:SetTimerLocked(true)
                    self:After(delay, function()
                        self:PaintMessageWait()
                    end)
                else
                    self:PaintMessageWait()
                end
            end
        end
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

--- Getter for CraftPresence:addonVersion
--- @return string addonVersion
function CraftPresence:GetVersion()
    if addonVersion == nil or addonVersion == "" then
        addonVersion = GetAddOnMetadata(L["ADDON_NAME"], "Version")
    end
    return addonVersion
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
            self:SetTimerLocked(false)
        elseif self:StartsWith(input, "update") then
            -- Query Parsing
            local query = string.match(input, ":(.*)")
            if query ~= nil then
                query = string.lower(query)
            end
            self:PaintMessageWait(true, true, true, nil, (query == "force"))
        elseif input == "config" then
            self:ShowConfig()
        elseif self:StartsWith(input, "reset") then
            local query = string.match(input, ":(.*)")
            if query ~= nil then
                local sub_query = string.match(query, ",(.*)")
                self:GetFromDb(query, sub_query, true)
            else
                self:ResetDB()
            end
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
        elseif self:StartsWith(input, "placeholders") then
            local placeholderString = L["INFO_PLACEHOLDER_INTRO"]
            global_placeholders, inner_placeholders, time_conditions = self:SyncConditions()
            -- Query Parsing
            local query = string.match(input, ":(.*)")
            local found_placeholders = false
            if query ~= nil then
                self:Print(string.format(L["INFO_PLACEHOLDERS_QUERY"], query))
                query = string.lower(query)
            end
            -- Global placeholder iteration to form placeholderString
            for key, value in pairs(global_placeholders) do
                local strKey = tostring(key)
                local strValue = tostring(value)
                if (query == nil or (
                        (string.lower(strKey)):find(query, 1, true) or
                                (string.lower(strValue)):find(query, 1, true))
                ) then
                    found_placeholders = true
                    placeholderString = placeholderString .. "\n " .. (string.format(
                            L["INFO_PLACEHOLDER_DATA"], strKey, strValue
                    ))
                end
            end
            -- Inner placeholder iteration to form placeholderString
            for key, value in pairs(inner_placeholders) do
                local strKey = tostring(key)
                local strValue = tostring(value)
                if (query == nil or (
                        (string.lower(strKey)):find(query, 1, true) or
                                (string.lower(strValue)):find(query, 1, true))
                ) then
                    found_placeholders = true
                    placeholderString = placeholderString .. "\n " .. (string.format(
                            L["INFO_PLACEHOLDER_DATA"], strKey, strValue
                    ))
                end
            end
            -- Final parsing of placeholderString before printing
            if not found_placeholders then
                placeholderString = placeholderString .. "\n " .. L["INFO_PLACEHOLDER_NONE"]
            end
            placeholderString = placeholderString .. "\n" .. L["INFO_PLACEHOLDER_NOTE"]
            self:Print(placeholderString)
        else
            LibStub("AceConfigCmd-3.0"):HandleCommand(L["ADDON_AFFIX"], L["ADDON_NAME"], input)
        end
    end
end
