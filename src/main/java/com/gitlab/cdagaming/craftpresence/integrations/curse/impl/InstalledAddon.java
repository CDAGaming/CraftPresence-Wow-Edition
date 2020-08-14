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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InstalledAddon {

    @SerializedName("addonID")
    @Expose
    public Integer addonID;
    @SerializedName("gameInstanceID")
    @Expose
    public String gameInstanceID;
    @SerializedName("installedFile")
    @Expose
    public Object installedFile;
    @SerializedName("dateInstalled")
    @Expose
    public String dateInstalled;
    @SerializedName("dateUpdated")
    @Expose
    public String dateUpdated;
    @SerializedName("dateLastUpdateAttempted")
    @Expose
    public String dateLastUpdateAttempted;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("preferenceAutoInstallUpdates")
    @Expose
    public Boolean preferenceAutoInstallUpdates;
    @SerializedName("preferenceAlternateFile")
    @Expose
    public Boolean preferenceAlternateFile;
    @SerializedName("preferenceIsIgnored")
    @Expose
    public Boolean preferenceIsIgnored;
    @SerializedName("isModified")
    @Expose
    public Boolean isModified;
    @SerializedName("isWorkingCopy")
    @Expose
    public Boolean isWorkingCopy;
    @SerializedName("isFuzzyMatch")
    @Expose
    public Boolean isFuzzyMatch;
    @SerializedName("preferenceReleaseType")
    @Expose
    public Object preferenceReleaseType;
    @SerializedName("manifestName")
    @Expose
    public Object manifestName;
    @SerializedName("installedTargets")
    @Expose
    public Object installedTargets;

}
