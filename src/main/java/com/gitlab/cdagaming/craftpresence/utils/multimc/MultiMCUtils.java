package com.gitlab.cdagaming.craftpresence.utils.multimc;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class MultiMCUtils {
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
        ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.instance.init"));
        try {
            final InputStream STREAM = new FileInputStream(instanceFile);
            configFile.load(STREAM);

            INSTANCE_NAME = get("name");

            final String tempIconKey = get("iconKey"), defaultIconName = "default", defaultIconKey = "infinity";
            ICON_KEY = StringUtils.formatPackIcon(!StringUtils.isNullOrEmpty(tempIconKey) && !tempIconKey.equals(defaultIconName) ? tempIconKey : defaultIconKey);

            if (!StringUtils.isNullOrEmpty(INSTANCE_NAME) && !StringUtils.isNullOrEmpty(ICON_KEY)) {
                ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.instance.loaded", INSTANCE_NAME, ICON_KEY));
                CraftPresence.packFound = true;
            }
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.file.instance"));

            if (ex.getClass() != FileNotFoundException.class) {
                ex.printStackTrace();
            }
        }
    }
}
