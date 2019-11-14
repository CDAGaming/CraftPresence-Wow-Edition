package com.gitlab.cdagaming.craftpresence.utils.curse;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.FileUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.google.common.collect.Maps;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

public class CurseUtils {
    public static CurseManifest manifest;
    private static Map<String, CurseManifest> manifests = Maps.newHashMap();

    public static boolean contains(String key) {
        return manifest != null && manifests.containsKey(key);
    }

    public static void loadManifest() {
        ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.manifest.init"));
        manifests = Maps.newHashMap();

        try {
            manifest = FileUtils.getJSONFromFile(new File("manifest.json"), CurseManifest.class);

            if (manifest != null && !StringUtils.isNullOrEmpty(manifest.name)) {
                manifests.put(manifest.name, manifest);
                CraftPresence.packFound = true;
                ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.manifest.loaded", manifest.name));
            }
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.file.manifest"));

            if (ex.getClass() != FileNotFoundException.class) {
                ex.printStackTrace();
            }
        }
    }
}
