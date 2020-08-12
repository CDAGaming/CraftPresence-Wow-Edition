package com.gitlab.cdagaming.craftpresence.utils.curse.impl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModLoader {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("primary")
    @Expose
    public Boolean primary;

}
