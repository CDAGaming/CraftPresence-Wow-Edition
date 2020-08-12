package com.gitlab.cdagaming.craftpresence.utils.curse.impl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class InstalledFile {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("displayName")
    @Expose
    public String displayName;
    @SerializedName("fileName")
    @Expose
    public String fileName;
    @SerializedName("fileDate")
    @Expose
    public String fileDate;
    @SerializedName("fileLength")
    @Expose
    public Integer fileLength;
    @SerializedName("releaseType")
    @Expose
    public Integer releaseType;
    @SerializedName("fileStatus")
    @Expose
    public Integer fileStatus;
    @SerializedName("downloadUrl")
    @Expose
    public String downloadUrl;
    @SerializedName("isAlternate")
    @Expose
    public Boolean isAlternate;
    @SerializedName("alternateFileId")
    @Expose
    public Integer alternateFileId;
    @SerializedName("dependencies")
    @Expose
    public List<Object> dependencies = new ArrayList<Object>();
    @SerializedName("isAvailable")
    @Expose
    public Boolean isAvailable;
    @SerializedName("modules")
    @Expose
    public List<Module> modules = new ArrayList<Module>();
    @SerializedName("packageFingerprint")
    @Expose
    public Integer packageFingerprint;
    @SerializedName("gameVersion")
    @Expose
    public List<String> gameVersion = new ArrayList<String>();
    @SerializedName("hasInstallScript")
    @Expose
    public Boolean hasInstallScript;
    @SerializedName("isCompatibleWithClient")
    @Expose
    public Boolean isCompatibleWithClient;
    @SerializedName("categorySectionPackageType")
    @Expose
    public Integer categorySectionPackageType;
    @SerializedName("restrictProjectFileAccess")
    @Expose
    public Integer restrictProjectFileAccess;
    @SerializedName("projectStatus")
    @Expose
    public Integer projectStatus;
    @SerializedName("projectId")
    @Expose
    public Integer projectId;
    @SerializedName("gameVersionDateReleased")
    @Expose
    public String gameVersionDateReleased;
    @SerializedName("gameId")
    @Expose
    public Integer gameId;
    @SerializedName("isServerPack")
    @Expose
    public Boolean isServerPack;
    @SerializedName("serverPackFileId")
    @Expose
    public Integer serverPackFileId;
    @SerializedName("FileNameOnDisk")
    @Expose
    public String fileNameOnDisk;

}
