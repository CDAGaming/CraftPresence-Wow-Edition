package com.gitlab.cdagaming.craftpresence.integrations.curse.impl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Minecraft {

    @SerializedName("version")
    @Expose
    public String version;
    @SerializedName("additionalJavaArgs")
    @Expose
    public Object additionalJavaArgs;
    @SerializedName("modLoaders")
    @Expose
    public List<ModLoader> modLoaders = new ArrayList<ModLoader>();
    @SerializedName("libraries")
    @Expose
    public Object libraries;

}
