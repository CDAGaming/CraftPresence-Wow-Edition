package com.gitlab.cdagaming.craftpresence.handler.multimc;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class InstanceHandler {
    public static String INSTANCE_NAME, ICON_KEY;

    private static Properties configFile = new Properties();
    private static String instanceFile = new File(CraftPresence.SYSTEM.USER_DIR).getParent() + File.separator + "instance.cfg";

    public static boolean contains(String key) {
        return configFile != null && configFile.containsKey(key);
    }

    public static String get(String key) {
        return contains(key) ? configFile.getProperty(key) : null;
    }

    public static void loadInstance() {
        Constants.LOG.info(Constants.TRANSLATOR.translate("craftpresence.logger.info.instance.init"));
        try {
            final InputStream STREAM = new FileInputStream(instanceFile);
            configFile.load(STREAM);

            INSTANCE_NAME = get("name");
            ICON_KEY = StringHandler.formatPackIcon(!StringHandler.isNullOrEmpty(get("iconKey")) && !get("iconKey").equals("default") ? get("iconKey") : "infinity");

            if (!StringHandler.isNullOrEmpty(INSTANCE_NAME) && !StringHandler.isNullOrEmpty(ICON_KEY)) {
                Constants.LOG.info(Constants.TRANSLATOR.translate("craftpresence.logger.info.instance.loaded", INSTANCE_NAME, ICON_KEY));
                CraftPresence.packFound = true;
            }
        } catch (Exception ex) {
            Constants.LOG.error(Constants.TRANSLATOR.translate("craftpresence.logger.error.file.instance"));

            if (ex.getClass() != FileNotFoundException.class) {
                ex.printStackTrace();
            }
        }
    }
}
