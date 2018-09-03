package com.gitlab.cdagaming.craftpresence.handler.curse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Manifest {
    @SerializedName("minecraft")
    public Minecraft minecraft;

    @SerializedName("manifestType")
    public String manifestType;

    @SerializedName("manifestVersion")
    public Integer manifestVersion;

    @SerializedName("name")
    public String name;

    @SerializedName("version")
    public String version;

    @SerializedName("author")
    public String author;

    @SerializedName("projectID")
    public Integer projectID;

    @SerializedName("files")
    public List<File> files;

    @SerializedName("overrides")
    public String overrides;

    private class Minecraft {
        @SerializedName("version")
        public String version;

        @SerializedName("modLoaders")
        public List<ModLoader> modLoaders;
    }

    private class File {
        @SerializedName("projectID")
        public Integer projectID;

        @SerializedName("fileID")
        public Integer fileID;

        @SerializedName("required")
        public Boolean required;
    }

    private class ModLoader {
        @SerializedName("id")
        public String id;

        @SerializedName("primary")
        public Boolean primary;
    }
}
