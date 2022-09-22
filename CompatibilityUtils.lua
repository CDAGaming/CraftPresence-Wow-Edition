--[[
MIT License

Copyright (c) 2018 - 2022 CDAGaming (cstack2011@yahoo.com)

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
local pairs, type = pairs, type

--- Retrieve and/or Synchronize Build Flavor Info
---
--- @param key string If specified, attempt to retrieve and return flavor_info[key]
--- @param value string If specified, attempt to retrieve and return flavor_info[key][value]
---
--- @return table @ flavor_info
function CraftPresence:GetFlavorInfo(key, value)
    if not self.cache.flavors then
        self.cache.flavors = {
            ["retail"] = {
                ["toc"] = 90207,
                ["name"] = "World of Warcraft",
                ["product_id"] = "retail"
            },
            ["classic"] = {
                ["toc"] = 30400,
                ["name"] = "Wrath of the Lich King Classic",
                ["product_id"] = "classic"
            },
            ["classic_era"] = {
                ["toc"] = 11403,
                ["name"] = "World of Warcraft Classic",
                ["product_id"] = "classic_era"
            },
            ["ptr"] = {
                ["toc"] = 90207,
                ["name"] = "Public Test Realm",
                ["product_id"] = "ptr"
            },
            ["beta"] = {
                ["toc"] = 100002,
                ["name"] = "Dragonflight Beta",
                ["product_id"] = "beta"
            },
            ["classic_ptr"] = {
                ["toc"] = 30400,
                ["name"] = "Public Test Realm (Wrath Classic)",
                ["product_id"] = "classic_ptr"
            },
            ["classic_beta"] = {
                ["toc"] = 30400,
                ["name"] = "Wrath of the Lich King Classic Beta",
                ["product_id"] = "classic_beta"
            },
            ["classic_era_ptr"] = {
                ["toc"] = 11403,
                ["name"] = "Classic Season of Mastery PTR",
                ["product_id"] = "classic_era_ptr"
            }
        }
    end
    if not self:IsNullOrEmpty(key) and self.cache.flavors[key] ~= nil then
        if not self:IsNullOrEmpty(value) and self.cache.flavors[key][value] ~= nil then
            return self.cache.flavors[key][value]
        end
        return self.cache.flavors[key]
    end
    return self.cache.flavors
end

--- Attempt to retrieve Build Flavor TOC Info for the specified version
--- (Helper Function for GetFlavorInfo)
---
--- @param key any The version or toc number to attempt to locate within GetFlavorInfo.
---
--- @return number @ result
function CraftPresence:GetFlavorTOC(key)
    return self:GetFlavorInfo(key, "toc")
end

--- Retrieve and/or Synchronize App Compatibility Info
--- (Note: Some TOC versions may require extra filtering)
---
--- Note: This function should only be used internally; Use Helper Functions for more consumer-style usage
---
--- @param key string If specified, attempt to retrieve and return compatibility_info[key]
--- @param value string If specified, attempt to retrieve and return compatibility_info[key][value]
---
--- @return table @ compatibility_info
function CraftPresence:GetCompatibilityInfo(key, value)
    if not self.cache.compatibility_info then
        self.cache.compatibility_info = {
            ["10.x"] = {
                ["minimumTOC"] = 100000,
                ["baseTOC"] = 90000,
                ["name"] = "Dragonflight"
            },
            ["9.x"] = {
                ["minimumTOC"] = 90000,
                ["maximumTOC"] = 90207,
                ["baseTOC"] = 80000,
                ["name"] = "Shadowlands"
            },
            ["8.x"] = {
                ["minimumTOC"] = 80000,
                ["maximumTOC"] = 80307,
                ["baseTOC"] = 70000,
                ["name"] = "Battle for Azeroth"
            },
            ["7.x"] = {
                ["minimumTOC"] = 70000,
                ["maximumTOC"] = 70305,
                ["baseTOC"] = 60000,
                ["name"] = "Legion"
            },
            ["6.x"] = {
                ["minimumTOC"] = 60000,
                ["maximumTOC"] = 60204,
                ["baseTOC"] = 50000,
                ["name"] = "Warlords of Draenor"
            },
            ["5.x"] = {
                ["minimumTOC"] = 50000,
                ["maximumTOC"] = 50408,
                ["baseTOC"] = 40000,
                ["name"] = "Mists of Pandaria"
            },
            ["4.x"] = {
                ["minimumTOC"] = 40000,
                ["maximumTOC"] = 40304,
                ["baseTOC"] = 30000,
                ["name"] = "Cataclysm"
            },
            ["3.4.x"] = {
                ["minimumTOC"] = 30400,
                ["maximumTOC"] = 30400,
                ["build_tag"] = "rebased",
                ["baseTOC"] = 90205,
                ["name"] = "Wrath of the Lich King Classic"
            },
            ["3.x"] = {
                ["minimumTOC"] = 30000,
                ["maximumTOC"] = 30305,
                ["baseTOC"] = 20000,
                ["name"] = "Wrath of the Lich King"
            },
            ["2.5.x"] = {
                ["minimumTOC"] = 20500,
                ["maximumTOC"] = 20504,
                ["build_tag"] = "rebased",
                ["baseTOC"] = 90005,
                ["name"] = "Burning Crusade Classic"
            },
            ["2.x"] = {
                ["minimumTOC"] = 20000,
                ["maximumTOC"] = 20403,
                ["baseTOC"] = 11200,
                ["name"] = "Burning Crusade",
                ["features"] = {
                    ["enforceInterface"] = {
                        ["minimumTOC"] = 20000,
                        ["maximumTOC"] = 40000,
                        ["allowRebasedApi"] = false,
                        ["enabled"] = true
                    }
                }
            },
            ["1.14.x"] = {
                ["minimumTOC"] = 11400,
                ["maximumTOC"] = 11403,
                ["build_tag"] = "rebased",
                ["baseTOC"] = 20502,
                ["name"] = "Classic Season of Mastery"
            },
            ["1.13.x"] = {
                ["minimumTOC"] = 11300,
                ["maximumTOC"] = 11307,
                ["build_tag"] = "rebased",
                ["baseTOC"] = 80100,
                ["name"] = "Classic"
            },
            ["1.12.x"] = {
                ["minimumTOC"] = 11200,
                ["maximumTOC"] = 11203,
                ["name"] = "Vanilla",
                ["features"] = {
                    ["modernInstanceState"] = {
                        ["minimumTOC"] = 30000,
                        ["allowRebasedApi"] = true,
                        ["enabled"] = true
                    },
                    ["notifyOnChange"] = {
                        ["minimumTOC"] = 20000,
                        ["allowRebasedApi"] = true,
                        ["enabled"] = true
                    },
                    ["modernDispatcher"] = {
                        ["minimumTOC"] = 20000,
                        ["allowRebasedApi"] = true,
                        ["enabled"] = true
                    },
                    ["modernConfigUI"] = {
                        ["minimumTOC"] = 20000,
                        ["allowRebasedApi"] = true,
                        ["enabled"] = true
                    },
                    ["registerUI"] = {
                        ["minimumTOC"] = 11200,
                        ["allowRebasedApi"] = true,
                        ["enabled"] = true
                    }
                }
            }
        }
        -- Special Builds
        self.cache.compatibility_info["1.16.x"] = {
            ["minimumTOC"] = 11600,
            ["maximumTOC"] = 11600,
            ["name"] = "TurtleWow 1.16.x",
            ["build_tag"] = "special",
            ["baseTOC"] = 11201
        }
        self.cache.compatibility_info["1.15.x"] = {
            ["minimumTOC"] = 11500,
            ["maximumTOC"] = 11501,
            ["name"] = "TurtleWow 1.15.x",
            ["build_tag"] = "special",
            ["baseTOC"] = 11201
        }
    end
    if not self:IsNullOrEmpty(key) and self.cache.compatibility_info[key] ~= nil then
        if not self:IsNullOrEmpty(value) and self.cache.compatibility_info[key][value] ~= nil then
            return self.cache.compatibility_info[key][value]
        end
        return self.cache.compatibility_info[key]
    end
    return self.cache.compatibility_info
end

--- Retrieve and/or Synchronize App Compatibility Info
--- (Note: Some TOC versions may require extra filtering)
---
--- Note: This is a consumer-friendly helper function for GetCompatibilityInfo
---
--- @param version any The version or toc number to attempt to locate within GetCompatibilityInfo (Required)
--- @param field any If specified, the field value to locate within GetCompatibilityInfo[version], if found. (Optional)
---
--- @return table @ compatibility_info
function CraftPresence:FindCompatibilityInfo(version, field)
    if not self.cache.compatibility_attachments then self.cache.compatibility_attachments = {} end
    if self:IsNullOrEmpty(version) then
        return self:GetCompatibilityInfo()
    else
        if self:GetCompatibilityInfo()[version] ~= nil then
            return self:GetCompatibilityInfo(version, field)
        else
            version = self:VersionToBuild(version)
        end
    end

    if not self.cache.compatibility_attachments[version] then
        for key, value in pairs(self:GetCompatibilityInfo()) do
            local _, _, shouldAccept = self:ShouldProcessData(value, version, true)
            if shouldAccept then
                self.cache.compatibility_attachments[version] = key
                if not self:IsNullOrEmpty(field) and value[field] ~= nil then
                    return value[field]
                end
                return value
            end
        end
    end
    return self:GetCompatibilityInfo(self.cache.compatibility_attachments[version], field)
end

--- Attempt to retrieve App Compatibility TOC Info for the specified version
--- (Helper Function for FindCompatibilityInfo)
---
--- @param key any The version or toc number to attempt to locate within FindCompatibilityInfo.
---
--- @return number, number @ minimumTOC, maximumTOC
function CraftPresence:FindCompatibilityTOC(key)
    return self:FindCompatibilityInfo(key, "minimumTOC"), self:FindCompatibilityInfo(key, "maximumTOC")
end

--- Determine if a function or feature is marked as supported in the specified version (Or it's based version)
---
--- @param feature string The feature name to attempt to lookup (Required)
--- @param version any The version or toc number to attempt to locate within FindCompatibilityInfo. (Default: currentTOC)
---
--- @return boolean @ is_feature_supported
function CraftPresence:IsFeatureSupported(feature, version, checkOverride)
    if not self.cache.features then self.cache.features = {} end
    if self:IsNullOrEmpty(feature) then return false end
    if self:IsNullOrEmpty(version) then
        version = self:GetBuildInfo("toc_version")
    end
    checkOverride = self:GetOrDefault(checkOverride, version)

    if self.cache.features[version] ~= nil then
        if self.cache.features[version][feature] ~= nil then
            return self.cache.features[version][feature]
        end
    else
        self.cache.features[version] = {}
    end

    local foundData = self:FindCompatibilityInfo(checkOverride)
    local featureAbsent = (foundData.features == nil) or (foundData.features[feature] == nil)
    local featureSupported = false
    if not featureAbsent then
        featureSupported = self:ShouldProcessData(foundData.features[feature], version)
        self.cache.features[version][feature] = featureSupported
    end

    if foundData.baseTOC == nil then
        self.cache.features[version][feature] = featureSupported
        return featureSupported
    else
        return self:IsFeatureSupported(feature, version, foundData.baseTOC)
    end
end

--- Determine if this build identifies as Vanilla Classic
--- @return boolean @ is_classic_rebased
function CraftPresence:IsClassicRebased()
    return self:IsWithinValue(
        self:GetBuildInfo("toc_version"),
        self:FindCompatibilityTOC("1.13.x"), self:FindCompatibilityTOC("2.x"),
        true, false
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as TBC Classic
--- @return boolean @ is_tbc_rebased
function CraftPresence:IsTBCRebased()
    return self:IsWithinValue(
        self:GetBuildInfo("toc_version"),
        self:FindCompatibilityTOC("2.5.x"), self:FindCompatibilityTOC("3.x"),
        true, false
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as Wrath Classic
--- @return boolean @ is_wrath_rebased
function CraftPresence:IsWrathRebased()
    return self:IsWithinValue(
        self:GetBuildInfo("toc_version"),
        self:FindCompatibilityTOC("3.4.x"), self:FindCompatibilityTOC("4.x"),
        true, false
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as the Retail Live Build of the Game
--- @return boolean @ is_retail_live_build
function CraftPresence:IsRetailLiveBuild()
    return self:GetBuildInfo("toc_version") == self:GetFlavorTOC("retail")
end

--- Determine if this build identifies as the Retail PTR Build of the Game
--- @return boolean @ is_retail_ptr_build
function CraftPresence:IsRetailPTRBuild()
    return self:IsWithinValue(
        self:GetBuildInfo("toc_version"),
        self:GetFlavorTOC("retail"), self:GetFlavorTOC("ptr"),
        false, true
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as the Retail Beta Build of the Game
--- @return boolean @ is_retail_beta_build
function CraftPresence:IsRetailBetaBuild()
    return self:IsWithinValue(
        self:GetBuildInfo("toc_version"),
        self:GetFlavorTOC("ptr"), self:GetFlavorTOC("beta"),
        false, true
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as a Retail Build of the Game
--- @return boolean @ is_retail_build
function CraftPresence:IsRetailBuild()
    return self:IsRetailLiveBuild() or self:IsRetailPTRBuild() or self:IsRetailBetaBuild()
end

--- Determine if this build identifies as the Classic Live Build of the Game
--- @return boolean @ is_classic_live_build
function CraftPresence:IsClassicLiveBuild()
    return self:GetBuildInfo("toc_version") == self:GetFlavorTOC("classic")
end

--- Determine if this build identifies as the Classic PTR Build of the Game
--- @return boolean @ is_classic_ptr_build
function CraftPresence:IsClassicPTRBuild()
    return self:IsWithinValue(
        self:GetBuildInfo("toc_version"),
        self:GetFlavorTOC("classic"), self:GetFlavorTOC("classic_ptr"),
        false, true
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as the Classic Beta Build of the Game
--- @return boolean @ is_classic_beta_build
function CraftPresence:IsClassicBetaBuild()
    return self:IsWithinValue(
        self:GetBuildInfo("toc_version"),
        self:GetFlavorTOC("classic_ptr"), self:GetFlavorTOC("classic_beta"),
        false, true
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as a Classic Build of the Game
--- @return boolean @ is_classic_build
function CraftPresence:IsClassicBuild()
    return self:IsClassicLiveBuild() or self:IsClassicPTRBuild() or self:IsClassicBetaBuild()
end

--- Determine if this build identifies as the Classic Era Live Build of the Game
--- @return boolean @ is_classic_era_live_build
function CraftPresence:IsClassicEraLiveBuild()
    return self:GetBuildInfo("toc_version") == self:GetFlavorTOC("classic_era")
end

--- Determine if this build identifies as the Classic Era PTR Build of the Game
--- @return boolean @ is_classic_era_ptr_build
function CraftPresence:IsClassicEraPTRBuild()
    return self:IsWithinValue(
        self:GetBuildInfo("toc_version"),
        self:GetFlavorTOC("classic_era"), self:GetFlavorTOC("classic_era_ptr"),
        false, true
    ) and not self:IsSpecialVersion()
end

--- Determine if this build identifies as a Classic Era Build of the Game
--- @return boolean @ is_classic_era_build
function CraftPresence:IsClassicEraBuild()
    return self:IsClassicEraLiveBuild() or self:IsClassicEraPTRBuild()
end

--- Retrieve the Build Tag, if specified within compatibility info
--- @return string build_tag
function CraftPresence:GetBuildTag()
    if self.cache.build_tag == nil then
        local data = self:FindCompatibilityInfo(self:GetBuildInfo("version"), "build_tag", true)
        self.cache.build_tag = (type(data) == "string" and data)
    end
    return self.cache.build_tag
end

--- Determine if this build is using a rebased api
--- @return boolean @ is_rebased_api
function CraftPresence:IsRebasedApi()
    return self:GetBuildTag() == "rebased"
end

--- Determine if this build is using a special or modified api
--- @return boolean @ is_special_version
function CraftPresence:IsSpecialVersion()
    return self:GetBuildTag() == "special"
end