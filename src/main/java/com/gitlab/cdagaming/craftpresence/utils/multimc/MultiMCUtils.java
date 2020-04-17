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
package com.gitlab.cdagaming.craftpresence.utils.multimc;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Set of Utilities used to Parse MultiMC Instance Information
 *
 * @author CDAGaming
 */
public class MultiMCUtils {
    /**
     * Mapping of Configuration Data from received Instance Data, if any
     */
    private static final Properties configFile = new Properties();
    /**
     * The location of the MultiMC Instance properties, if any
     */
    private static final String instanceFile = new File(CraftPresence.SYSTEM.USER_DIR).getParent() + File.separator + "instance.cfg";
    /**
     * The MultiMC Instance Name
     */
    public static String INSTANCE_NAME;
    /**
     * The Icon Key to use for this Pack
     */
    public static String ICON_KEY;

    /**
     * Determines if the Configuration Data has the specified property
     *
     * @param key The property to search for
     * @return {@code true} if the property exists in the Configuration Data
     */
    public static boolean contains(String key) {
        return configFile.containsKey(key);
    }

    /**
     * Gets the specified property's value from the Configuration Data, if present
     *
     * @param key The property to search for
     * @return The property's value in the Configuration Data, if it is present
     */
    public static String get(String key) {
        return contains(key) ? configFile.getProperty(key) : null;
    }

    /**
     * Attempts to retrieve and load Pack Information, if any
     */
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
