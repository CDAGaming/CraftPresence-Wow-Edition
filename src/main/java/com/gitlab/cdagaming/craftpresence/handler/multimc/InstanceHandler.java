package com.gitlab.cdagaming.craftpresence.handler.multimc;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import net.minecraft.client.resources.I18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class InstanceHandler {
    private static Properties configFile = new Properties();
    private static String instanceFile = new File(System.getProperty("user.dir")).getParent() + "/instance.cfg";

    public static boolean contains(String key) {
        if (configFile != null) {
            return configFile.containsKey(key);
        }
        return false;
    }

    public static String get(String key) {
        if (configFile != null && contains(key)) {
            return configFile.getProperty(key);
        }
        return null;
    }

    public static void loadInstance() {
        Constants.LOG.info(I18n.format("craftpresence.logger.info.instance.init"));
        try {
            final InputStream STREAM = new FileInputStream(instanceFile);
            configFile.load(STREAM);

            if (!StringHandler.isNullOrEmpty(get("name"))) {
                if (!StringHandler.isNullOrEmpty(get("iconKey"))) {
                    Constants.LOG.info(I18n.format("craftpresence.logger.info.instance.loaded", get("name"), get("iconKey")));
                } else {
                    Constants.LOG.info(I18n.format("craftpresence.logger.info.instance.loaded", get("name"), "NONE"));
                }
            }
        } catch (Exception ex) {
            Constants.LOG.error(I18n.format("craftpresence.logger.error.file.instance"));

            if (ex.getClass() != FileNotFoundException.class) {
                ex.printStackTrace();
            }
        }
    }
}
