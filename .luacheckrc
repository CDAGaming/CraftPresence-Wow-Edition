std = "lua51"
max_line_length = false
codes = true
exclude_files = {
	"**/Libraries",
}
only = {
	"011", -- syntax
	"1", -- globals
}
globals = {
	-- wow std api
	"abs",
	"acos",
	"asin",
	"atan",
	"atan2",
	"bit",
	"ceil",
	"cos",
	"date",
	"debuglocals",
	"debugprofilestart",
	"debugprofilestop",
	"debugstack",
	"deg",
	"difftime",
	"exp",
	"fastrandom",
	"floor",
	"forceinsecure",
	"foreach",
	"foreachi",
	"format",
	"frexp",
	"geterrorhandler",
	"getn",
	"gmatch",
	"gsub",
	"hooksecurefunc",
	"issecure",
	"issecurevariable",
	"ldexp",
	"log",
	"log10",
	"math.mod",
	"max",
	"min",
	"mod",
	"rad",
	"random",
	"scrub",
	"securecall",
	"seterrorhandler",
	"sin",
	"sort",
	"sqrt",
	"strbyte",
	"strchar",
	"strcmputf8i",
	"strconcat",
	"strfind",
	"string.gfind",
	"string.join",
	"string.split",
	"string.trim",
	"strjoin",
	"strlen",
	"strlenutf8",
	"strlower",
	"strmatch",
	"strrep",
	"strrev",
	"strsplit",
	"strsub",
	"strtrim",
	"strupper",
	"table.getn",
	"table.setn",
	"table.wipe",
	"tan",
	"time",
	"tinsert",
	"tremove",

	-- framexml
	"getprinthandler",
	"hash_SlashCmdList",
	"setprinthandler",
	"tContains",
	"tDeleteItem",
	"tInvert",
	"tostringall",

	-- namespaces
	"C_ChallengeMode",
	"C_CVar",
	"C_Map",
	"C_MythicPlus",
	"C_Scenario",
	"EJ_GetInstanceByIndex",
	"EJ_GetInstanceForMap",
	"EJ_GetNumTiers",
	"EJ_GetTierInfo",
	"EJ_SelectTier",
	"LibStub",
	"CraftPresence",
	"RaiderIO",

	-- events
	"arg1",
	"arg2",
	"arg3",
	"arg4",
	"arg5",
	"arg6",
	"arg7",
	"arg8",
	"arg9",
	"event",

	-- functions
	"CreateFrame",
	"UIParent",
	"GetAddOnMetadata",
	"GetBuildInfo",
	"GetCurrentMapContinent",
	"GetCVar",
	"GetInstanceInfo",
	"GetMapContinents",
	"GetNumSavedInstances",
	"GetSavedInstanceInfo",
	"GetScreenWidth",
	"GetTime",
	"InterfaceOptionsFrame",
	"InterfaceOptionsFrameAddOns",
	"InterfaceOptionsFrameCategories",
	"InterfaceOptions_AddCategory",
	"InterfaceOptionsFrame_OpenToCategory",
	"InterfaceOptionsFrame_OpenToFrame",
	"IsInInstance",
	"ReloadUI",
	"RequestRaidInfo",
	"SetMapToCurrentZone"
}
