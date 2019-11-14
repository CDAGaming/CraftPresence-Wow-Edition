package com.gitlab.cdagaming.craftpresence.utils.mcupdater;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class MCUpdaterInstance {
    private String mcversion;
    private String packName;
    private String packId;
    private String revision;
    private String hash = "";
    private List<Object> instanceFiles = Lists.newArrayList();
    private List<Object> jarMods = Lists.newArrayList();
    private Map<String, Boolean> optionalMods = Maps.newHashMap();

    public List<Object> getInstanceFiles() {
        return instanceFiles;
    }

    public void setInstanceFiles(List<Object> instanceFiles) {
        this.instanceFiles = instanceFiles;
    }

    public Map<String, Boolean> getOptionalMods() {
        return this.optionalMods;
    }

    public void setOptionalMods(Map<String, Boolean> optionalMods) {
        this.optionalMods = optionalMods;
    }

    public Boolean getModStatus(String key) {
        return this.optionalMods.get(key);
    }

    public void setModStatus(String key, Boolean value) {
        this.optionalMods.put(key, value);
    }

    public String getMCVersion() {
        return mcversion;
    }

    public void setMCVersion(String mcversion) {
        this.mcversion = mcversion;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public List<Object> getJarMods() {
        return jarMods;
    }

    public void setJarMods(List<Object> jarMods) {
        this.jarMods = jarMods;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "MCUpdaterInstance{" +
                "mcversion:" + this.mcversion + ";" +
                "revision:" + this.revision + ";" +
                "hash:" + this.hash + ";" +
                "}";
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }
}
