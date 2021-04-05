local CraftPresence = LibStub("AceAddon-3.0"):NewAddon("CraftPresence", "AceConsole-3.0", "AceEvent-3.0")

local L = LibStub("AceLocale-3.0"):GetLocale("CraftPresence")

-- Critical Data (Do not remove)
local CraftPresenceLDB
local icon
local minimapState = { hide = false }
local addonVersion = ""
-- Build and Integration Data
local realmData = { "US", "KR", "EU", "TW", "CH" }
local buildData = {}
local compatData = {}
local integrationData = {}
local isRebasedApi = false

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
    dynamicDifficulty, isDynamic, instanceID, instanceGroupSize, LfgDungeonID
    if buildData["toc_version"] >= compatData["2.5.x"] or isRebasedApi then
        name, instanceType, difficultyID, difficultyName, maxPlayers,
        dynamicDifficulty, isDynamic, instanceID, instanceGroupSize, LfgDungeonID = GetInstanceInfo()
    else
        -- Fallback Data, as needed
        name = ""
    end
    local difficultyInfo = difficultyName
    local lockoutData = {}
    if buildData["toc_version"] >= compatData["6.0.x"] then
        lockoutData = self:GetCurrentLockoutData(true)
    end
    -- Player Data
    local playerName = UnitName("player")
    local playerStatus, playerPrefix = self:GetPlayerStatus("player", true, isRebasedApi)
    -- Extra Player Data
    local playerLevel = UnitLevel("player")
    local playerRealm = GetRealmName()
    local playerRegionId = 1
    if buildData["toc_version"] >= compatData["6.0.x"] or isRebasedApi then
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
    if buildData["toc_version"] >= compatData["5.0.x"] or isRebasedApi then
        -- Extra Character Data
        local titleName = UnitPVPName("player")
        local avgItemLevel, avgItemLevelEquipped, avgItemLevelPvp = 0, 0, 0
        local specId
        local specName = ""
        local roleName = ""
        local specInfo
        if buildData["toc_version"] >= compatData["5.0.x"] then
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
                queued_inner_placeholders["@instance_type@"] == "party" and
                        not self:FindMatches(name, "Garrison", false)
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
        ["torghast"] = (self:FindMatches(name, "Torghast", false) and
                queued_inner_placeholders["@instance_type@"] == "scenario")
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
        local output = self:TrimString(inValue)
        -- If the Placeholders contained in the messages is an overrider,
        -- we replace the rest of the output with just that placeholder.
        --
        -- Note: Only applies towards inner-placeholders with subject to change data
        for _, overrideValue in pairs(queued_override_placeholders) do
            if self:FindMatches(output, overrideValue, false) then
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
                    output = string.gsub(output, key, value)
                else
                    output = string.gsub(output, key, "")
                end
            end
            outputTable[inKey] = self:TrimString(output)
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
        queued_details = string.gsub(queued_details, key, value)
        queued_state = string.gsub(queued_state, key, value)
        queued_large_image_key = string.gsub(queued_large_image_key, key, value)
        queued_large_image_text = string.gsub(queued_large_image_text, key, value)
        queued_small_image_key = string.gsub(queued_small_image_key, key, value)
        queued_small_image_text = string.gsub(queued_small_image_text, key, value)
        queued_primary_button = string.gsub(queued_primary_button, key, value)
        queued_secondary_button = string.gsub(queued_secondary_button, key, value)
    end
    -- Inner Placeholder Syncing
    for key, value in pairs(inner_placeholders) do
        queued_details = string.gsub(queued_details, key, value)
        queued_state = string.gsub(queued_state, key, value)
        queued_large_image_key = string.gsub(queued_large_image_key, key, value)
        queued_large_image_text = string.gsub(queued_large_image_text, key, value)
        queued_small_image_key = string.gsub(queued_small_image_key, key, value)
        queued_small_image_text = string.gsub(queued_small_image_text, key, value)
        queued_primary_button = string.gsub(queued_primary_button, key, value)
        queued_secondary_button = string.gsub(queued_secondary_button, key, value)
    end
    -- Time Condition Syncing
    for timeKey, timeValue in pairs(time_conditions) do
        if timeValue then
            if (self:FindMatches(timeKey, "start", false)) then
                if hasInstanceChanged then
                    queued_time_start = "generated"
                else
                    queued_time_start = "last"
                end
            elseif (self:FindMatches(timeKey, "end", false)) then
                if hasInstanceChanged then
                    queued_time_end = "generated"
                else
                    queued_time_end = "last"
                end
            end
        end
    end
    return self:EncodeData(
            string.gsub(self:GetFromDb("clientId"), "%s+", " "),
            string.gsub(string.lower(queued_large_image_key), "%s+", "_"),
            string.gsub(queued_large_image_text, "%s+", " "),
            string.gsub(string.lower(queued_small_image_key), "%s+", "_"),
            string.gsub(queued_small_image_text, "%s+", " "),
            string.gsub(queued_details, "%s+", " "),
            string.gsub(queued_state, "%s+", " "),
            string.gsub(queued_time_start, "%s+", " "),
            string.gsub(queued_time_end, "%s+", " "),
            string.gsub(queued_primary_button, "%s+", " "),
            string.gsub(queued_secondary_button, "%s+", " ")
    )
end

--- Instructions to be called when the addon is loaded
function CraftPresence:OnInitialize()
    -- Main Initialization
    buildData = self:GetBuildInfo()
    compatData = self:GetCompatibilityInfo()
    isRebasedApi = (self:IsClassicRebased() or self:IsTBCRebased())
    -- Options Initialization
    self.db = LibStub("AceDB-3.0"):New(L["ADDON_NAME"] .. "DB", self:GetSchemaDefaults())
    LibStub("AceConfig-3.0"):RegisterOptionsTable(L["ADDON_NAME"], self.getOptionsTable, {
        (L["CONFIG_COMMAND"]), (L["CONFIG_COMMAND_ALT"])
    })
    -- Command Registration
    self:RegisterChatCommand(L["ADDON_ID"], "ChatCommand")
    self:RegisterChatCommand(L["ADDON_AFFIX"], "ChatCommand")
    -- Version-Specific Registration
    if buildData["toc_version"] >= compatData["1.12.1"] then
        -- UI Registration
        if buildData["toc_version"] >= compatData["2.5.x"] or isRebasedApi then
            self.optionsFrame = LibStub("AceConfigDialog-3.0"):AddToBlizOptions(L["ADDON_NAME"])
            self.optionsFrame.default = self.ResetDB
        end
        -- Icon Registration
        icon = LibStub("LibDBIcon-1.0")
        CraftPresenceLDB = LibStub:GetLibrary("LibDataBroker-1.1"):NewDataObject(L["ADDON_NAME"], {
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
        self:UpdateMinimapState(false)
        icon:Register(L["ADDON_NAME"], CraftPresenceLDB, minimapState)
    end
end

--- Instructions to be called when the addon is enabled
function CraftPresence:OnEnable()
    -- Print initial data and register events
    -- depending on platform and config data
    self:Print(string.format(L["ADDON_INTRO"], self:GetVersion()))
    if self:GetFromDb("verboseMode") then
        self:Print(string.format(L["ADDON_BUILD_INFO"], self:SerializeTable(buildData)))
    end
    -- Register Universal Events
    local eventName
    if buildData["toc_version"] >= compatData["2.0.x"] or isRebasedApi then
        eventName = "DispatchModernUpdate"
    else
        eventName = "DispatchLegacyUpdate"
    end
    CraftPresence:AddTriggers(eventName,
            { "PLAYER_LOGIN", "PLAYER_LEVEL_UP",
              "PLAYER_ALIVE", "PLAYER_DEAD", "PLAYER_FLAGS_CHANGED",
              "ZONE_CHANGED", "ZONE_CHANGED_NEW_AREA", "ZONE_CHANGED_INDOORS",
              "UPDATE_INSTANCE_INFO" }
    )
    -- Register Version-Specific Events
    if buildData["toc_version"] >= compatData["5.0.x"] then
        CraftPresence:AddTriggers(eventName,
                { "PLAYER_SPECIALIZATION_CHANGED" }
        )
    end
    if buildData["toc_version"] >= compatData["6.0.x"] then
        CraftPresence:AddTriggers(eventName,
                { "ACTIVE_TALENT_GROUP_CHANGED",
                  "CHALLENGE_MODE_START", "CHALLENGE_MODE_COMPLETED", "CHALLENGE_MODE_RESET",
                  "SCENARIO_COMPLETED", "CRITERIA_COMPLETE" }
        )
    end
    if buildData["toc_version"] >= compatData["8.0.x"] then
        CraftPresence:AddTriggers(eventName,
                { "PLAYER_LEVEL_CHANGED" }
        )
    end
    -- Create initial frames and initial rpc update
    self:CreateFrames(self:GetFromDb("frameSize"))
    self:PaintMessageWait()
end

--- Registers the specified event for the subsequent argument values
---
--- @param event string The event tag to register with argument values
--- @param args table The events to bind to the event tag
function CraftPresence:AddTriggers(event, args)
    if type(args) ~= "table" then
        args = { args }
    end

    if event ~= nil and args ~= nil then
        for _, v in ipairs(args) do
            self:RegisterEvent(tostring(v), tostring(event))
        end
    end
end

--- Prepares and Dispatches a new frame update, given the specified arguments
function CraftPresence:DispatchLegacyUpdate()
    self:DispatchUpdate({
        event, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9
    })
end

--- Prepares and Dispatches a new frame update, given the specified arguments
function CraftPresence:DispatchModernUpdate(event, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9)
    self:DispatchUpdate({
        event, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9
    })
end

--- Prepares and Dispatches a new frame update, given the specified arguments
function CraftPresence:DispatchUpdate(args)
    if type(args) ~= "table" then
        args = { args }
    end

    if args ~= nil then
        -- Ignore Event Conditional Setup
        -- Format: [EVENT_NAME] = ignore_event_condition
        local eventName = args[1]
        local ignore_event = false
        local ignore_event_conditions = {
            ["PLAYER_FLAGS_CHANGED"] = (
                    self:GetLastPlayerStatus() == self:GetPlayerStatus("player", false, isRebasedApi)
            ),
            ["UPDATE_INSTANCE_INFO"] = (not IsInInstance() or
                    lastEventName == eventName or
                    self:GetCachedLockout() == self:GetCurrentLockoutData(false)
            ),
            ["PLAYER_ALIVE"] = (lastEventName ~= "PLAYER_DEAD"),
            ["PLAYER_LEVEL_UP"] = (lastEventName == "PLAYER_LEVEL_CHANGED"),
            ["PLAYER_LEVEL_CHANGED"] = (lastEventName == "PLAYER_LEVEL_UP"),
            ["ACTIVE_TALENT_GROUP_CHANGED"] = (args[2] == args[3]),
            ["PLAYER_SPECIALIZATION_CHANGED"] = (args[2] ~= "player")
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
    if buildData["toc_version"] >= compatData["1.12.1"] then
        icon:Hide(L["ADDON_NAME"])
    end
    self:UnregisterAllEvents()
end

--- Updates the minimap status with config data
--- @param update_state boolean Whether or not to update the icon state
function CraftPresence:UpdateMinimapState(update_state)
    minimapState = { hide = not CraftPresence:GetFromDb("showMinimapIcon") }
    if update_state then
        if buildData["toc_version"] >= compatData["1.12.1"] then
            if minimapState["hide"] then
                icon:Hide(L["ADDON_NAME"])
            else
                icon:Show(L["ADDON_NAME"])
            end
        else
            self:Print(string.format(
                    L["ERROR_LOG"], string.format(
                            L["ERROR_FUNCTION_DISABLED"], "UpdateMinimapState"
                    )
            ))
        end
    end
end

--- Display the addon's config frame
function CraftPresence:ShowConfig()
    -- a bug can occur in blizzard's implementation of this call
    if buildData["toc_version"] >= compatData["2.5.x"] or isRebasedApi then
        InterfaceOptionsFrame_OpenToCategory(CraftPresence.optionsFrame)
        InterfaceOptionsFrame_OpenToCategory(CraftPresence.optionsFrame)
    else
        CraftPresence:Print(string.format(
                L["ERROR_LOG"], string.format(
                        L["ERROR_FUNCTION_DISABLED"], "ShowConfig"
                )
        ))
    end
end

--- Getter for CraftPresence:addonVersion
--- @return string addonVersion
function CraftPresence:GetVersion()
    if self:IsNullOrEmpty(addonVersion) then
        addonVersion = GetAddOnMetadata(L["ADDON_NAME"], "Version")
    end
    return addonVersion
end

--- Interprets the specified input to perform specific commands
--- @param input string The specified input to evaluate
function CraftPresence:ChatCommand(input)
    if self:IsNullOrEmpty(input) or input == "help" or input == "?" then
        self:Print(
                string.format(L["USAGE_CMD_INTRO"], L["ADDON_NAME"]) .. "\n" ..
                        L["USAGE_CMD_HELP"] .. "\n" ..
                        L["USAGE_CMD_CONFIG"] .. "\n" ..
                        L["USAGE_CMD_TEST"] .. "\n" ..
                        L["USAGE_CMD_CLEAN"] .. "\n" ..
                        L["USAGE_CMD_UPDATE"] .. "\n" ..
                        L["USAGE_CMD_MINIMAP"] .. "\n" ..
                        L["USAGE_CMD_STATUS"] .. "\n" ..
                        L["USAGE_CMD_RESET"] .. "\n" ..
                        L["USAGE_CMD_SET"] .. "\n" ..
                        L["USAGE_CMD_INTEGRATION"] .. "\n" ..
                        L["USAGE_CMD_PLACEHOLDERS"] .. "\n" ..
                        string.format(L["USAGE_CMD_NOTE"], L["ADDON_AFFIX"], L["ADDON_ID"]) .. "\n" ..
                        L["USAGE_CMD_NOTE_TWO"] .. "\n"
        )
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
            self:Print(L["INFO_COMMAND_CLEAR"])
            self:CleanFrames()
            self:SetTimerLocked(false)
        elseif self:StartsWith(input, "update") then
            -- Query Parsing
            local _, _, query = self:FindMatches(input, ":(.*)", false)
            if query ~= nil then
                query = string.lower(query)
            end
            self:PaintMessageWait(true, true, true, nil, (query == "force"))
        elseif input == "config" then
            self:ShowConfig()
        elseif self:StartsWith(input, "reset") then
            local _, _, query = self:FindMatches(input, ":(.*)", false)
            if query ~= nil then
                local sub_query = self:FindMatches(query, ",(.*)", false)
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
        elseif self:StartsWith(input, "integration") then
            -- Query Parsing
            local _, _, query = self:FindMatches(input, ":(.*)", false)
            if query ~= nil and not integrationData[query] then
                self:Print(string.format(L["INTEGRATION_QUERY"], query))
                query = string.lower(query)

                -- Integration Parsing
                if (query == "viragdevtool" or query == "vdt") and ViragDevTool_AddData then
                    ViragDevTool_AddData(CraftPresence, "CraftPresence")
                    -- Uncomment for single-use integration
                    -- integrationData["viragdevtool"] = true
                    -- integrationData["vdt"] = true
                else
                    self:Print(L["INTEGRATION_NOT_FOUND"])
                end
            end
        elseif self:StartsWith(input, "placeholders") then
            local placeholderStr = L["PLACEHOLDERS_INTRO"]
            global_placeholders, inner_placeholders, time_conditions = self:SyncConditions()
            -- Query Parsing
            local _, _, query = self:FindMatches(input, ":(.*)", false)
            local found_placeholders = false
            if query ~= nil then
                self:Print(string.format(L["PLACEHOLDERS_QUERY"], query))
                query = string.lower(query)
            end
            -- Global placeholder iteration to form placeholderString
            for key, value in pairs(global_placeholders) do
                local strKey = tostring(key)
                local strValue = tostring(value)
                if (query == nil or (
                        self:FindMatches(string.lower(strKey), query, false, 1, true) or
                                self:FindMatches(string.lower(strValue), query, false, 1, true))
                ) then
                    found_placeholders = true
                    placeholderStr = placeholderStr .. "\n " .. (string.format(
                            L["PLACEHOLDERS_FOUND_DATA"], strKey, strValue
                    ))
                end
            end
            -- Inner placeholder iteration to form placeholderString
            for key, value in pairs(inner_placeholders) do
                local strKey = tostring(key)
                local strValue = tostring(value)
                if (query == nil or (
                        self:FindMatches(string.lower(strKey), query, false, 1, true) or
                                self:FindMatches(string.lower(strValue), query, false, 1, true))
                ) then
                    found_placeholders = true
                    placeholderStr = placeholderStr .. "\n " .. (string.format(
                            L["PLACEHOLDERS_FOUND_DATA"], strKey, strValue
                    ))
                end
            end
            -- Final parsing of placeholderString before printing
            if not found_placeholders then
                placeholderStr = placeholderStr .. "\n " .. L["PLACEHOLDERS_FOUND_NONE"]
            end
            placeholderStr = placeholderStr .. "\n" .. L["PLACEHOLDERS_NOTE"] .. "\n" .. L["PLACEHOLDERS_NOTE_TWO"]
            self:Print(placeholderStr)
        elseif self:StartsWith(input, "set") then
            local _, _, query = self:FindMatches(input, " (.*)", false)
            LibStub("AceConfigCmd-3.0"):HandleCommand(L["CONFIG_COMMAND"], L["ADDON_NAME"], query or "")
        else
            self:Print(string.format(
                    L["ERROR_LOG"], string.format(
                            L["ERROR_COMMAND_UNKNOWN"], input
                    )
            ))
        end
    end
end
