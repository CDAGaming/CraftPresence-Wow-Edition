package com.gitlab.cdagaming.craftpresence.handler.curse;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.FileHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.I18n;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

public class ManifestHandler {
    public static Manifest manifest;
    private static Map<String, Manifest> manifests = Maps.newHashMap();

    public static boolean contains(String key) {
        return manifest != null && manifests.containsKey(key);
    }

    public static void loadManifest() {
        Constants.LOG.info(I18n.format("craftpresence.logger.info.manifest.init"));
        manifests = Maps.newHashMap();

        try {
            manifest = FileHandler.getJSONFromFile(new File("manifest.json"), Manifest.class);

            if (manifest != null && !StringHandler.isNullOrEmpty(manifest.name)) {
                manifests.put(manifest.name, manifest);
                CraftPresence.packFound = true;
                Constants.LOG.info(I18n.format("craftpresence.logger.info.manifest.loaded", manifest.name));
            }
        } catch (Exception ex) {
            Constants.LOG.error(I18n.format("craftpresence.logger.error.file.manifest"));

            if (ex.getClass() != FileNotFoundException.class) {
                ex.printStackTrace();
            }
        }
    }
}
