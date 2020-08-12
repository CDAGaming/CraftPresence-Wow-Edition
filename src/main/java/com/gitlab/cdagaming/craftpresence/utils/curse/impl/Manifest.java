package com.gitlab.cdagaming.craftpresence.utils.curse.impl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Manifest {

    @SerializedName("minecraft")
    @Expose
    public Minecraft minecraft;
    @SerializedName("manifestType")
    @Expose
    public String manifestType;
    @SerializedName("manifestVersion")
    @Expose
    public Integer manifestVersion;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("version")
    @Expose
    public String version;
    @SerializedName("author")
    @Expose
    public String author;
    @SerializedName("description")
    @Expose
    public Object description;
    @SerializedName("projectID")
    @Expose
    public Integer projectID;
    @SerializedName("files")
    @Expose
    public List<File> files = new ArrayList<>();
    @SerializedName("overrides")
    @Expose
    public String overrides;

}
