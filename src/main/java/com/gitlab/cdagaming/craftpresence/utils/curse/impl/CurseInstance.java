package com.gitlab.cdagaming.craftpresence.utils.curse.impl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CurseInstance {

    @SerializedName("baseModLoader")
    @Expose
    public BaseModLoader baseModLoader;
    @SerializedName("isUnlocked")
    @Expose
    public Boolean isUnlocked;
    @SerializedName("javaArgsOverride")
    @Expose
    public Object javaArgsOverride;
    @SerializedName("javaDirOverride")
    @Expose
    public Object javaDirOverride;
    @SerializedName("lastPlayed")
    @Expose
    public String lastPlayed;
    @SerializedName("manifest")
    @Expose
    public Manifest manifest;
    @SerializedName("fileDate")
    @Expose
    public String fileDate;
    @SerializedName("installedModpack")
    @Expose
    public InstalledModpack installedModpack;
    @SerializedName("projectID")
    @Expose
    public Integer projectID;
    @SerializedName("fileID")
    @Expose
    public Integer fileID;
    @SerializedName("customAuthor")
    @Expose
    public Object customAuthor;
    @SerializedName("modpackOverrides")
    @Expose
    public List<String> modpackOverrides = new ArrayList<String>();
    @SerializedName("isMemoryOverride")
    @Expose
    public Boolean isMemoryOverride;
    @SerializedName("allocatedMemory")
    @Expose
    public Integer allocatedMemory;
    @SerializedName("guid")
    @Expose
    public String guid;
    @SerializedName("gameTypeID")
    @Expose
    public Integer gameTypeID;
    @SerializedName("installPath")
    @Expose
    public String installPath;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("cachedScans")
    @Expose
    public List<CachedScan> cachedScans = new ArrayList<CachedScan>();
    @SerializedName("isValid")
    @Expose
    public Boolean isValid;
    @SerializedName("lastPreviousMatchUpdate")
    @Expose
    public String lastPreviousMatchUpdate;
    @SerializedName("isEnabled")
    @Expose
    public Boolean isEnabled;
    @SerializedName("isPinned")
    @Expose
    public Boolean isPinned;
    @SerializedName("gameVersion")
    @Expose
    public String gameVersion;
    @SerializedName("preferenceAlternateFile")
    @Expose
    public Boolean preferenceAlternateFile;
    @SerializedName("preferenceAutoInstallUpdates")
    @Expose
    public Boolean preferenceAutoInstallUpdates;
    @SerializedName("preferenceQuickDeleteLibraries")
    @Expose
    public Boolean preferenceQuickDeleteLibraries;
    @SerializedName("preferenceDeleteSavedVariables")
    @Expose
    public Boolean preferenceDeleteSavedVariables;
    @SerializedName("preferenceProcessFileCommands")
    @Expose
    public Boolean preferenceProcessFileCommands;
    @SerializedName("preferenceReleaseType")
    @Expose
    public Integer preferenceReleaseType;
    @SerializedName("syncProfile")
    @Expose
    public SyncProfile syncProfile;
    @SerializedName("preferenceShowAddOnInfo")
    @Expose
    public Boolean preferenceShowAddOnInfo;
    @SerializedName("installDate")
    @Expose
    public String installDate;
    @SerializedName("installedAddons")
    @Expose
    public List<InstalledAddon> installedAddons = new ArrayList<InstalledAddon>();
    @SerializedName("isMigrated")
    @Expose
    public Boolean isMigrated;
    @SerializedName("preferenceUploadProfile")
    @Expose
    public Boolean preferenceUploadProfile;

}
