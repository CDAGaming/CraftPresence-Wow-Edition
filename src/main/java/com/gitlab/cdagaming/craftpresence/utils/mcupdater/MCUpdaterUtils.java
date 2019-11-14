package com.gitlab.cdagaming.craftpresence.utils.mcupdater;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.FileUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class MCUpdaterUtils {
    public static MCUpdaterInstance instance;

    public static void loadInstance() {
        ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.mcupdater.init"));

        try {
            instance = FileUtils.getJSONFromFile(new File("instance.json"), MCUpdaterInstance.class);

            if (instance != null && !StringUtils.isNullOrEmpty(instance.getPackName())) {
                CraftPresence.packFound = true;
                ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.mcupdater.loaded", instance.getPackName()));
            }
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.file.mcupdater"));

            if (ex.getClass() != FileNotFoundException.class) {
                ex.printStackTrace();
            }
        }
    }
}
