/*
 * MIT License
 *
 * Copyright (c) 2018 - 2019 CDAGaming (cstack2011@yahoo.com)
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
package com.gitlab.cdagaming.craftpresence.utils.mcupdater;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * The Json Parsing Syntax for an MCUpdater Instance
 *
 * @author CDAGaming
 */
public class MCUpdaterInstance {
    /**
     * The Minecraft Version targeted by this Instance
     */
    private String mcversion;

    /**
     * The Pack Name for this Instance
     */
    private String packName;

    /**
     * The Pack ID for this Instance
     */
    private String packId;

    /**
     * The Pack Revision for this Instance
     */
    private String revision;

    /**
     * The Pack's HashCode for this Instance
     */
    private String hash = "";

    /**
     * A List of Related Files for this Instance
     */
    private List<Object> instanceFiles = Lists.newArrayList();

    /**
     * A List of Jar Mods related to this Instance
     */
    private List<Object> jarMods = Lists.newArrayList();

    /**
     * A List of Optional Mods related to this Instance
     */
    private Map<String, Boolean> optionalMods = Maps.newHashMap();

    /**
     * Gets the list of related files for this Instance
     *
     * @return A List of relative files for the Instance
     */
    public List<Object> getInstanceFiles() {
        return instanceFiles;
    }

    /**
     * Sets the list of related files for the Instance
     *
     * @param instanceFiles The new List of related files for this Instance
     */
    public void setInstanceFiles(List<Object> instanceFiles) {
        this.instanceFiles = instanceFiles;
    }

    /**
     * Gets the List of Optional Mods related to this Instance
     *
     * @return The List of Optional Mods related to this Instance
     */
    public Map<String, Boolean> getOptionalMods() {
        return this.optionalMods;
    }

    /**
     * Sets the List of Optional Mods related to this Instance
     *
     * @param optionalMods The new List of Optional Mods related to this Instance
     */
    public void setOptionalMods(Map<String, Boolean> optionalMods) {
        this.optionalMods = optionalMods;
    }

    /**
     * Gets the status of the Specified Optional Mod, if any
     *
     * @param key The optional mod name to search for
     * @return The Mod's Status, if existent as an optional mod
     */
    public Boolean getModStatus(String key) {
        return this.optionalMods.get(key);
    }

    /**
     * Sets the status of the Specified Optional Mod, if any
     *
     * @param key   The optional mod name to set
     * @param value The optional Mod's Status to set
     */
    public void setModStatus(String key, Boolean value) {
        this.optionalMods.put(key, value);
    }

    /**
     * Gets the Minecraft Version targeted by this Instance
     *
     * @return The Minecraft Version targeted by this Instance
     */
    public String getMCVersion() {
        return mcversion;
    }

    /**
     * Sets the Minecraft Version targeted by this Instance
     *
     * @param mcversion The new Minecraft Version to be targeted by this Instance
     */
    public void setMCVersion(String mcversion) {
        this.mcversion = mcversion;
    }

    /**
     * Gets the pack's revision for this Instance
     *
     * @return The pack's revision for this Instance
     */
    public String getRevision() {
        return revision;
    }

    /**
     * Sets the pack's revision for this Instance
     *
     * @param revision The new pack revision for this Instance
     */
    public void setRevision(String revision) {
        this.revision = revision;
    }

    /**
     * Gets the List of Jar Mods related to this Instance
     *
     * @return The list of Jar Mods related to this Instance
     */
    public List<Object> getJarMods() {
        return jarMods;
    }

    /**
     * Sets the List of Jar Mods related to this Instance
     *
     * @param jarMods The new list of Jar Mods related to this Instance
     */
    public void setJarMods(List<Object> jarMods) {
        this.jarMods = jarMods;
    }

    /**
     * Gets the HashCode for this Instance
     *
     * @return The HashCode for this Instance
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the HashCode for this Instance
     *
     * @param hash The new HashCode for this Instance
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Prints Instance Data as a Readable String
     *
     * @return A readable version of this Instance
     */
    @Override
    public String toString() {
        return "MCUpdaterInstance{" +
                "mcversion:" + this.mcversion + ";" +
                "revision:" + this.revision + ";" +
                "hash:" + this.hash + ";" +
                "}";
    }

    /**
     * Gets the pack name for this Instance
     *
     * @return The pack name for the Instance
     */
    public String getPackName() {
        return packName;
    }

    /**
     * Sets the pack name for this Instance
     *
     * @param packName The new pack name for the Instance
     */
    public void setPackName(String packName) {
        this.packName = packName;
    }

    /**
     * Gets the pack ID for this Instance
     *
     * @return The pack ID for the Instance
     */
    public String getPackId() {
        return packId;
    }

    /**
     * Sets the pack ID for this Instance
     *
     * @param packId The new pack ID for this Instance
     */
    public void setPackId(String packId) {
        this.packId = packId;
    }
}
