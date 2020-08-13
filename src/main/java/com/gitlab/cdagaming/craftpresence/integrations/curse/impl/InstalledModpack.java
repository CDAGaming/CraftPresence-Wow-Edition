package com.gitlab.cdagaming.craftpresence.integrations.curse.impl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InstalledModpack {

    @SerializedName("addonID")
    @Expose
    public Integer addonID;
    @SerializedName("gameInstanceID")
    @Expose
    public String gameInstanceID;
    @SerializedName("installedFile")
    @Expose
    public InstalledFile installedFile;
    @SerializedName("dateInstalled")
    @Expose
    public String dateInstalled;
    @SerializedName("dateUpdated")
    @Expose
    public String dateUpdated;
    @SerializedName("dateLastUpdateAttempted")
    @Expose
    public String dateLastUpdateAttempted;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("preferenceAutoInstallUpdates")
    @Expose
    public Boolean preferenceAutoInstallUpdates;
    @SerializedName("preferenceAlternateFile")
    @Expose
    public Boolean preferenceAlternateFile;
    @SerializedName("preferenceIsIgnored")
    @Expose
    public Boolean preferenceIsIgnored;
    @SerializedName("isModified")
    @Expose
    public Boolean isModified;
    @SerializedName("isWorkingCopy")
    @Expose
    public Boolean isWorkingCopy;
    @SerializedName("isFuzzyMatch")
    @Expose
    public Boolean isFuzzyMatch;
    @SerializedName("preferenceReleaseType")
    @Expose
    public Object preferenceReleaseType;
    @SerializedName("manifestName")
    @Expose
    public Object manifestName;
    @SerializedName("installedTargets")
    @Expose
    public Object installedTargets;

}
