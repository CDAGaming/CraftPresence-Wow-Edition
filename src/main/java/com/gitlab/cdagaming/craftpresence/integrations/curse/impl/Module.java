package com.gitlab.cdagaming.craftpresence.integrations.curse.impl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Module {

    @SerializedName("foldername")
    @Expose
    public String foldername;
    @SerializedName("fingerprint")
    @Expose
    public Integer fingerprint;
    @SerializedName("type")
    @Expose
    public Integer type;

}
