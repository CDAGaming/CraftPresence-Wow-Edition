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
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gitlab.cdagaming.craftpresence.integrations.curse.impl;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstalledFile {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("displayName")
    @Expose
    public String displayName;
    @SerializedName("fileName")
    @Expose
    public String fileName;
    @SerializedName("fileDate")
    @Expose
    public String fileDate;
    @SerializedName("fileLength")
    @Expose
    public Integer fileLength;
    @SerializedName("releaseType")
    @Expose
    public Integer releaseType;
    @SerializedName("fileStatus")
    @Expose
    public Integer fileStatus;
    @SerializedName("downloadUrl")
    @Expose
    public String downloadUrl;
    @SerializedName("isAlternate")
    @Expose
    public Boolean isAlternate;
    @SerializedName("alternateFileId")
    @Expose
    public Integer alternateFileId;
    @SerializedName("dependencies")
    @Expose
    public List<Object> dependencies = Lists.newArrayList();
    @SerializedName("isAvailable")
    @Expose
    public Boolean isAvailable;
    @SerializedName("modules")
    @Expose
    public List<Module> modules = Lists.newArrayList();
    @SerializedName("packageFingerprint")
    @Expose
    public Integer packageFingerprint;
    @SerializedName("gameVersion")
    @Expose
    public List<String> gameVersion = Lists.newArrayList();
    @SerializedName("hasInstallScript")
    @Expose
    public Boolean hasInstallScript;
    @SerializedName("isCompatibleWithClient")
    @Expose
    public Boolean isCompatibleWithClient;
    @SerializedName("categorySectionPackageType")
    @Expose
    public Integer categorySectionPackageType;
    @SerializedName("restrictProjectFileAccess")
    @Expose
    public Integer restrictProjectFileAccess;
    @SerializedName("projectStatus")
    @Expose
    public Integer projectStatus;
    @SerializedName("projectId")
    @Expose
    public Integer projectId;
    @SerializedName("gameVersionDateReleased")
    @Expose
    public String gameVersionDateReleased;
    @SerializedName("gameId")
    @Expose
    public Integer gameId;
    @SerializedName("isServerPack")
    @Expose
    public Boolean isServerPack;
    @SerializedName("serverPackFileId")
    @Expose
    public Integer serverPackFileId;
    @SerializedName("FileNameOnDisk")
    @Expose
    public String fileNameOnDisk;

}
