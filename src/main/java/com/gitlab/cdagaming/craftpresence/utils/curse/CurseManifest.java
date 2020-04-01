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

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.gitlab.cdagaming.craftpresence.utils.curse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * The Json Parsing Syntax for a Curse/Twitch Manifest
 *
 * @author CDAGaming
 */
public class CurseManifest {
    /**
     * The {@link Minecraft} data attached to this Manifest
     */
    @SerializedName("minecraft")
    public Minecraft minecraft;

    /**
     * The Type of this Manifest
     */
    @SerializedName("manifestType")
    public String manifestType;

    /**
     * The Version ID Number for this Manifest
     */
    @SerializedName("manifestVersion")
    public Integer manifestVersion;

    /**
     * The Main Name of this Manifest
     */
    @SerializedName("name")
    public String name;

    /**
     * The Version attached to this Manifest
     */
    @SerializedName("version")
    public String version;

    /**
     * The Author of this Manifest
     */
    @SerializedName("author")
    public String author;

    /**
     * The Main ID for this Manifest
     */
    @SerializedName("projectID")
    public Integer projectID;

    /**
     * The List of {@link File} data attached to this Manifest
     */
    @SerializedName("files")
    public List<File> files;

    @SerializedName("overrides")
    public String overrides;

    /**
     * Mapping for Storing MC Related Data
     */
    private class Minecraft {
        /**
         * The Minecraft Version for this Manifest
         */
        @SerializedName("version")
        public String version;

        /**
         * The ModLoaders attached to this Manifest
         */
        @SerializedName("modLoaders")
        public List<ModLoader> modLoaders;
    }

    /**
     * Mapping for Storing Data for Files related to this Manifest
     */
    private static class File {
        /**
         * The Project ID for this File
         */
        @SerializedName("projectID")
        public Integer projectID;

        /**
         * The Main ID for this File
         */
        @SerializedName("fileID")
        public Integer fileID;

        /**
         * Whether this file is a required or optional mod
         */
        @SerializedName("required")
        public Boolean required;
    }

    /**
     * Mapping for storing ModLoader Data
     */
    private static class ModLoader {
        /**
         * The ModLoader's ID
         */
        @SerializedName("id")
        public String id;

        /**
         * Whether this ModLoader is primary or not
         */
        @SerializedName("primary")
        public Boolean primary;
    }
}
