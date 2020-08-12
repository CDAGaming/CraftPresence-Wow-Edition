package com.gitlab.cdagaming.craftpresence.utils.curse.impl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class File {

    @SerializedName("projectID")
    @Expose
    public Integer projectID;
    @SerializedName("fileID")
    @Expose
    public Integer fileID;
    @SerializedName("required")
    @Expose
    public Boolean required;

}
