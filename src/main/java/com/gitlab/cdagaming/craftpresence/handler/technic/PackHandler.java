package com.gitlab.cdagaming.craftpresence.handler.technic;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.handler.FileHandler;
import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import net.minecraft.client.resources.I18n;

import java.io.File;
import java.io.FileNotFoundException;

public class PackHandler {
    public static String PACK_NAME, ICON_NAME;

    public static void loadPack() {
        Constants.LOG.info(I18n.format("craftpresence.logger.info.technic.init"));

        try {
            final File installedPacks = new File(CraftPresence.SYSTEM.USER_DIR + File.separator + ".." + File.separator + ".." + File.separator + "installedPacks");
            final Pack pack = FileHandler.getJSONFromFile(installedPacks, Pack.class);

            if (pack != null) {
                if (CraftPresence.SYSTEM.USER_DIR.contains(pack.selected)) {
                    PACK_NAME = StringHandler.formatWord(pack.selected);
                    ICON_NAME = StringHandler.formatPackIcon(pack.selected);
                    CraftPresence.packFound = true;

                    Constants.LOG.info(I18n.format("craftpresence.logger.info.technic.loaded", PACK_NAME, ICON_NAME));
                } else {
                    Constants.LOG.error(I18n.format("craftpresence.logger.error.technic.limitation"));
                }
            }
        } catch (Exception ex) {
            Constants.LOG.error(I18n.format("craftpresence.logger.error.file.technic"));

            if (ex.getClass() != FileNotFoundException.class) {
                ex.printStackTrace();
            }
        }
    }
}
