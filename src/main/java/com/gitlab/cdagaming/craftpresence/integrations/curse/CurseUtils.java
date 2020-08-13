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
package com.gitlab.cdagaming.craftpresence.integrations.curse;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.FileUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.integrations.curse.impl.CurseInstance;
import com.gitlab.cdagaming.craftpresence.integrations.curse.impl.Manifest;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Set of Utilities used to Parse Curse/Twitch Manifest Information
 *
 * @author CDAGaming
 */
public class CurseUtils {
    /**
     * The Curse Instance Name
     */
    public static String INSTANCE_NAME;

    /**
     * Attempts to retrieve and load Manifest Information, if any
     */
    public static void loadManifest() {
        ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.manifest.init"));

        Manifest manifest = null;
        CurseInstance instance = null;

        try {
            manifest = FileUtils.getJSONFromFile(new File("manifest.json"), Manifest.class);
        } catch (Exception ex) {
            try {
                ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.manifest.fallback"));
                if (ex.getClass() != FileNotFoundException.class || ModUtils.IS_VERBOSE) {
                    ex.printStackTrace();
                }

                instance = FileUtils.getJSONFromFile(new File("minecraftinstance.json"), CurseInstance.class);
            } catch (Exception ex2) {
                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.file.manifest"));

                if (ex2.getClass() != FileNotFoundException.class || ModUtils.IS_VERBOSE) {
                    ex2.printStackTrace();
                }
            }
        } finally {
            if (manifest != null || instance != null) {
                INSTANCE_NAME = manifest != null ? manifest.name : instance.name;

                if (!StringUtils.isNullOrEmpty(INSTANCE_NAME)) {
                    CraftPresence.packFound = true;
                    ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.manifest.loaded", INSTANCE_NAME));
                }
            }
        }
    }
}
