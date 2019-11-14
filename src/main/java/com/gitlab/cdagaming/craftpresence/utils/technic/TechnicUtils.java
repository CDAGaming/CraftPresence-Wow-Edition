package com.gitlab.cdagaming.craftpresence.utils.technic;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.utils.FileUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class TechnicUtils {
    public static String PACK_NAME, ICON_NAME;

    public static void loadPack() {
        ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.technic.init"));

        try {
            final File installedPacks = new File(CraftPresence.SYSTEM.USER_DIR + File.separator + ".." + File.separator + ".." + File.separator + "installedPacks");
            final TechnicPack technicPack = FileUtils.getJSONFromFile(installedPacks, TechnicPack.class);

            if (technicPack != null) {
                if (CraftPresence.SYSTEM.USER_DIR.contains(technicPack.selected)) {
                    PACK_NAME = StringUtils.formatWord(technicPack.selected);
                    ICON_NAME = StringUtils.formatPackIcon(technicPack.selected);
                    CraftPresence.packFound = true;

                    ModUtils.LOG.info(ModUtils.TRANSLATOR.translate("craftpresence.logger.info.technic.loaded", PACK_NAME, ICON_NAME));
                } else {
                    ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.technic.limitation"));
                }
            }
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.file.technic"));

            if (ex.getClass() != FileNotFoundException.class) {
                ex.printStackTrace();
            }
        }
    }
}
