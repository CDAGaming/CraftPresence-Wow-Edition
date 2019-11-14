package com.gitlab.cdagaming.craftpresence.utils;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class KeyUtils {
    // Allowed KeyCode Start Limit and Individual Filters
    // After ESC and Including any KeyCodes under 0x00
    // Notes:
    // LWJGL 2: ESC = 0x01
    // LWJGL 3: ESC = 256
    private int keyStartLimit = 0x00;
    private List<Integer> invalidKeys = new ArrayList<Integer>() {{
        add(0x01);
        add(256);
    }};

    public boolean isValidKeyCode(int sourceKeyCode) {
        return sourceKeyCode > keyStartLimit && !invalidKeys.contains(sourceKeyCode);
    }

    public void onTick() {
        if (Keyboard.isCreated() && CraftPresence.CONFIG != null) {
            try {
                if (isValidKeyCode(Integer.parseInt(CraftPresence.CONFIG.configKeyCode)) && Keyboard.isKeyDown(Integer.parseInt(CraftPresence.CONFIG.configKeyCode)) && !CraftPresence.GUIS.isFocused && !CraftPresence.GUIS.openConfigGUI && !CraftPresence.GUIS.configGUIOpened) {
                    CraftPresence.GUIS.openConfigGUI = true;
                }
            } catch (Exception ex) {
                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.keybind", CraftPresence.CONFIG.NAME_configKeycode.replaceAll("_", " ")));
                CraftPresence.CONFIG.configKeyCode = Integer.toString(Keyboard.KEY_LCONTROL);
                CraftPresence.CONFIG.updateConfig();
            }
        }
    }
}
