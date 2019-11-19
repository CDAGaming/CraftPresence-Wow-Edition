package com.gitlab.cdagaming.craftpresence.utils;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.google.common.collect.Lists;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class KeyUtils {
    // Allowed KeyCode Start Limit and Individual Filters
    // After ESC and Including any KeyCodes under 0x00
    // Notes:
    // LWJGL 2: ESC = 0x01
    // LWJGL 3: ESC = 256
    private int keyStartLimit = 0x00;
    private List<Integer> invalidKeys = Lists.newArrayList(0x01, 256);

    public boolean isValidKeyCode(int sourceKeyCode) {
        return sourceKeyCode > keyStartLimit && !invalidKeys.contains(sourceKeyCode);
    }

    void onTick() {
        if (CraftPresence.CONFIG != null) {
            try {
                if (isValidKeyCode(Integer.parseInt(CraftPresence.CONFIG.configKeyCode)) && Keyboard.isKeyDown(Integer.parseInt(CraftPresence.CONFIG.configKeyCode)) && !CraftPresence.GUIS.isFocused && !CraftPresence.GUIS.openConfigGUI && !CraftPresence.GUIS.configGUIOpened) {
                    CraftPresence.GUIS.openConfigGUI = true;
                }
            } catch (Exception | Error ex) {
                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.keybind", CraftPresence.CONFIG.NAME_configKeyCode.replaceAll("_", " ")));
                CraftPresence.CONFIG.configKeyCode = Integer.toString(Keyboard.KEY_LCONTROL);
                CraftPresence.CONFIG.updateConfig();
            }
        }
    }
}
