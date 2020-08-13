package com.gitlab.cdagaming.craftpresence.integrations.curse.impl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SyncProfile {

    @SerializedName("PreferenceEnabled")
    @Expose
    public Boolean preferenceEnabled;
    @SerializedName("PreferenceAutoSync")
    @Expose
    public Boolean preferenceAutoSync;
    @SerializedName("PreferenceAutoDelete")
    @Expose
    public Boolean preferenceAutoDelete;
    @SerializedName("PreferenceBackupSavedVariables")
    @Expose
    public Boolean preferenceBackupSavedVariables;
    @SerializedName("GameInstanceGuid")
    @Expose
    public String gameInstanceGuid;
    @SerializedName("SyncProfileID")
    @Expose
    public Integer syncProfileID;
    @SerializedName("SavedVariablesProfile")
    @Expose
    public Object savedVariablesProfile;
    @SerializedName("LastSyncDate")
    @Expose
    public String lastSyncDate;

}
