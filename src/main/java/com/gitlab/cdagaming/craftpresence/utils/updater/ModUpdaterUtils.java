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
package com.gitlab.cdagaming.craftpresence.utils.updater;

import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.FileUtils;
import com.gitlab.cdagaming.craftpresence.utils.UrlUtils;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Set of Utilities used for Retrieving and Alerting on Any Mod Updates
 *
 * @author CDAGaming
 */
public class ModUpdaterUtils {
    /**
     * Mapping for Storing Strings to be interpreted as unstable/bleeding-edge versions
     */
    private final List<String> latestVersionTags = Lists.newArrayList("latest", "beta", "alpha", "bleeding-edge", "unstable", "rc", "release-candidate");
    /**
     * Mapping for Storing String to be interpreted as stable/recommended versions
     */
    private final List<String> recommendedVersionTags = Lists.newArrayList("recommended", "stable", "release");
    /**
     * The Current Update State for this Mod Updater Instance
     */
    public UpdateState currentState = UpdateState.PENDING;
    /**
     * The MOD ID attached to this Mod Updater Instance
     */
    public String modID;
    /**
     * The Update URL to retrieve Updates from in this Instance
     */
    public String updateUrl;
    /**
     * The Download Url, references as the "homepage" element in the Json
     */
    public String downloadUrl;
    /**
     * The Target Latest/Unstable Version for this Instance, based on retrieved data
     */
    public String targetLatestVersion;
    /**
     * The Target Recommended/Stable Version for this Instance, based on retrieved data
     */
    public String targetRecommendedVersion;
    /**
     * The Target Main Version for this Instance, dependent on Update State
     */
    public String targetVersion;
    /**
     * The Changelog Data attached to the Target Version, if any
     */
    public String targetChangelogData;
    /**
     * The Current Version attached to this Instance
     */
    public String currentVersion;

    /**
     * Initializes this Module from the Specified Arguments
     *
     * @param modID          The Mod Id for this Instance
     * @param updateUrl      The Update Url to attach to this Instance
     * @param currentVersion The Current Version to attach to this Instance
     */
    public ModUpdaterUtils(final String modID, final String updateUrl, final String currentVersion) {
        this.modID = modID;
        this.updateUrl = updateUrl;
        this.currentVersion = currentVersion.replaceAll("[a-zA-Z]", "").replace("\"", "").trim();
    }

    /**
     * Checks for Updates, updating the Current Update Check State
     */
    public void checkForUpdates() {
        // Reset Last Check Data before Starting
        currentState = UpdateState.PENDING;
        targetRecommendedVersion = "0.0.0";
        targetLatestVersion = "0.0.0";
        targetVersion = "0.0.0";
        targetChangelogData = "";
        downloadUrl = "";

        try {
            final JsonObject rootUpdateData = FileUtils.parseJson(UrlUtils.getURLText(updateUrl, "UTF-8"));

            if (rootUpdateData != null) {
                ModUtils.LOG.debugInfo(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.updater.receive.data", rootUpdateData.toString()));

                if (rootUpdateData.has("homepage")) {
                    downloadUrl = rootUpdateData.get("homepage").toString().replace("\"", "").trim();
                } else {
                    ModUtils.LOG.warn(ModUtils.TRANSLATOR.translate("craftpresence.logger.warning.updater.homepage"));
                }

                if (rootUpdateData.has("promos")) {
                    final JsonObject promoData = rootUpdateData.get("promos").getAsJsonObject();

                    // Check through Promo Data for Available Current Versions
                    for (Map.Entry<String, JsonElement> jsonSegment : promoData.entrySet()) {
                        // Case 1: Ensure the Element matches the format: mcVersion-dataTag
                        if (jsonSegment.getKey().contains("-")) {
                            final String[] splitPromo = jsonSegment.getKey().split("-");
                            final String mcVersion = splitPromo[0];

                            // Only Parse the Arguments attached to the target Minecraft Version
                            if (mcVersion.equalsIgnoreCase(ModUtils.MCVersion)) {
                                final String dataTag = splitPromo[1];

                                if (latestVersionTags.contains(dataTag.toLowerCase())) {
                                    targetLatestVersion = jsonSegment.getValue().toString().replaceAll("[a-zA-Z]", "").replace("\"", "").trim();
                                } else if (recommendedVersionTags.contains(dataTag.toLowerCase())) {
                                    targetRecommendedVersion = jsonSegment.getValue().toString().replaceAll("[a-zA-Z]", "").replace("\"", "").trim();
                                }

                                // Break of Loop if Found all needed Data
                                if (!targetLatestVersion.equalsIgnoreCase("0.0.0") && !targetRecommendedVersion.equalsIgnoreCase("0.0.0")) {
                                    break;
                                }
                            }
                        } else if (jsonSegment.getKey().equalsIgnoreCase(ModUtils.MCVersion)) {
                            // Case 2: Find only the Minecraft Version, if present, but do not break the loop
                            targetRecommendedVersion = jsonSegment.getValue().toString().replaceAll("[a-zA-Z]", "").replace("\"", "").trim();
                        } else {
                            ModUtils.LOG.debugWarn(ModUtils.TRANSLATOR.translate("craftpresence.logger.warning.updater.incompatiblejson", jsonSegment.getKey()));
                        }
                    }

                    ModUtils.LOG.debugInfo(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.updater.status", "Latest", ModUtils.MCVersion, targetLatestVersion));
                    ModUtils.LOG.debugInfo(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.updater.status", "Recommended", ModUtils.MCVersion, targetRecommendedVersion));

                    // Update Current Update State
                    final int recommendedState = compareVersions(currentVersion, targetRecommendedVersion);
                    final int latestState = compareVersions(currentVersion, targetLatestVersion);

                    if (recommendedState == 0) {
                        currentState = UpdateState.UP_TO_DATE;
                        targetVersion = targetRecommendedVersion;
                    } else {
                        if (recommendedState == -1) {
                            currentState = UpdateState.OUTDATED;
                            targetVersion = targetRecommendedVersion;
                        } else {
                            if (latestState == 0 || latestState == 1) {
                                currentState = UpdateState.BETA;
                            } else {
                                currentState = UpdateState.BETA_OUTDATED;
                            }
                            targetVersion = targetLatestVersion;
                        }
                    }

                    ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.updater.receive.status", modID, currentState.name(), targetVersion));

                    // Retrieve Changelog Data, if present
                    if (rootUpdateData.has(ModUtils.MCVersion)) {
                        final JsonObject mcVersionData = rootUpdateData.get(ModUtils.MCVersion).getAsJsonObject();

                        if (mcVersionData != null) {
                            final JsonElement semanticVersionData = mcVersionData.has(targetVersion) ? mcVersionData.get(targetVersion) : null;
                            final JsonElement annotatedVersionData = mcVersionData.has("v" + targetVersion) ? mcVersionData.get("v" + targetVersion) : null;

                            if (semanticVersionData != null || annotatedVersionData != null) {
                                final JsonElement changelogData = semanticVersionData != null ? semanticVersionData : annotatedVersionData;

                                targetChangelogData = changelogData.toString().replace("\"", "").trim();
                                ModUtils.LOG.debugInfo(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.updater.receive.changelog", targetChangelogData));
                            } else {
                                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.updater.changelog", modID, targetVersion));
                            }
                        }
                    }
                }
            }
        } catch (Exception | Error ex) {
            // Log Failure and Set Update State to FAILED
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.updater.failed"));

            if (ModUtils.IS_DEV) {
                ex.printStackTrace();
            }
            currentState = UpdateState.FAILED;
        }
    }

    /**
     * Compare a Set of Version Numbers to see if one is higher then another
     *
     * @param str1 The First Version Number to Iterate
     * @param str2 The Second Version Number to Iterate
     * @return 0 if Identical; -1 if Second is Higher; 1 if First is higher
     */
    private int compareVersions(String str1, String str2) {
        if (str1.equals(str2)) return 0;

        final String[] vals1 = str1.split("\\.");
        final String[] vals2 = str2.split("\\.");

        int i = 0;

        // Most efficient way to skip past equal version sub-parts
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) i++;

        // If we didn't reach the end,

        if (i < vals1.length && i < vals2.length)
            // have to use integer comparison to avoid the "10"<"1" problem
            return Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));

        if (i < vals1.length) { // end of str2, check if str1 is all 0's
            boolean allZeros = true;
            for (int j = i; allZeros & (j < vals1.length); j++)
                allZeros = (Integer.parseInt(vals1[j]) == 0);
            return allZeros ? 0 : -1;
        }

        if (i < vals2.length) { // end of str1, check if str2 is all 0's
            boolean allZeros = true;
            for (int j = i; allZeros & (j < vals2.length); j++)
                allZeros = (Integer.parseInt(vals2[j]) == 0);
            return allZeros ? 0 : 1;
        }

        return 0; // Should never happen (identical strings.)
    }

    /**
     * Mapping for CFU State (Based on https://mcforge.readthedocs.io/en/latest/gettingstarted/autoupdate/)
     *
     * <p>FAILED: The version checker could not connect to the URL provided or was unable to retrieve/parse data
     * <p>UP_TO_DATE: The current version is equal to or newer than the latest stable version
     * <p>OUTDATED: There is a new stable version available to download
     * <p>BETA_OUTDATED: There is a new unstable version available to download
     * <p>BETA: The current version is equal to or newer than the latest unstable version
     * <p>PENDING: The version checker has not completed at this time
     */
    public enum UpdateState {
        FAILED,
        UP_TO_DATE,
        OUTDATED,
        BETA_OUTDATED,
        BETA,
        PENDING
    }
}
