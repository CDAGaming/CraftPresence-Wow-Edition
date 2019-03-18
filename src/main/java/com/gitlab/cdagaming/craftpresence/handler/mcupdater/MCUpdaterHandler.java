package com.gitlab.cdagaming.craftpresence.handler.mcupdater;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.FileHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;

import java.io.File;
import java.io.FileNotFoundException;

public class MCUpdaterHandler {
    public static Instance instance;

    public static void loadInstance() {
        Constants.LOG.info(Constants.TRANSLATOR.translate("craftpresence.logger.info.mcupdater.init"));

        try {
            instance = FileHandler.getJSONFromFile(new File("instance.json"), Instance.class);

            if (instance != null && !StringHandler.isNullOrEmpty(instance.getPackName())) {
                CraftPresence.packFound = true;
                Constants.LOG.info(Constants.TRANSLATOR.translate("craftpresence.logger.info.mcupdater.loaded", instance.getPackName()));
            }
        } catch (Exception ex) {
            Constants.LOG.error(Constants.TRANSLATOR.translate("craftpresence.logger.error.file.mcupdater"));

            if (ex.getClass() != FileNotFoundException.class) {
                ex.printStackTrace();
            }
        }
    }
}
