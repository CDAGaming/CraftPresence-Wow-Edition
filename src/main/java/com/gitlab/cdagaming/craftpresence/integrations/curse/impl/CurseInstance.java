/*
 * MIT License
 *
 * Copyright (c) 2018 - 2020 CDAGaming (cstack2011@yahoo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gitlab.cdagaming.craftpresence.integrations.curse.impl;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    public InstalledModPack installedModpack;
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
    public List<String> modpackOverrides = Lists.newArrayList();
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
    public List<CachedScan> cachedScans = Lists.newArrayList();
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
    public List<InstalledAddon> installedAddons = Lists.newArrayList();
    @SerializedName("isMigrated")
    @Expose
    public Boolean isMigrated;
    @SerializedName("preferenceUploadProfile")
    @Expose
    public Boolean preferenceUploadProfile;

}
