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
package com.gitlab.cdagaming.craftpresence.integrations.curse.impl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseModLoader {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("gameVersionId")
    @Expose
    public Integer gameVersionId;
    @SerializedName("minecraftGameVersionId")
    @Expose
    public Integer minecraftGameVersionId;
    @SerializedName("forgeVersion")
    @Expose
    public String forgeVersion;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("type")
    @Expose
    public Integer type;
    @SerializedName("downloadUrl")
    @Expose
    public String downloadUrl;
    @SerializedName("filename")
    @Expose
    public String filename;
    @SerializedName("installMethod")
    @Expose
    public Integer installMethod;
    @SerializedName("latest")
    @Expose
    public Boolean latest;
    @SerializedName("recommended")
    @Expose
    public Boolean recommended;
    @SerializedName("approved")
    @Expose
    public Boolean approved;
    @SerializedName("dateModified")
    @Expose
    public String dateModified;
    @SerializedName("mavenVersionString")
    @Expose
    public String mavenVersionString;
    @SerializedName("versionJson")
    @Expose
    public String versionJson;
    @SerializedName("librariesInstallLocation")
    @Expose
    public String librariesInstallLocation;
    @SerializedName("minecraftVersion")
    @Expose
    public String minecraftVersion;
    @SerializedName("modLoaderGameVersionId")
    @Expose
    public Integer modLoaderGameVersionId;
    @SerializedName("modLoaderGameVersionTypeId")
    @Expose
    public Integer modLoaderGameVersionTypeId;
    @SerializedName("modLoaderGameVersionStatus")
    @Expose
    public Integer modLoaderGameVersionStatus;
    @SerializedName("modLoaderGameVersionTypeStatus")
    @Expose
    public Integer modLoaderGameVersionTypeStatus;
    @SerializedName("mcGameVersionId")
    @Expose
    public Integer mcGameVersionId;
    @SerializedName("mcGameVersionTypeId")
    @Expose
    public Integer mcGameVersionTypeId;
    @SerializedName("mcGameVersionStatus")
    @Expose
    public Integer mcGameVersionStatus;
    @SerializedName("mcGameVersionTypeStatus")
    @Expose
    public Integer mcGameVersionTypeStatus;
    @SerializedName("installProfileJson")
    @Expose
    public String installProfileJson;

}
