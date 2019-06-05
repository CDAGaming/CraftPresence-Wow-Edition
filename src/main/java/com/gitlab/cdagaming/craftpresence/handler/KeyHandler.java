package com.gitlab.cdagaming.craftpresence.handler;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import org.lwjgl.input.Keyboard;

public class KeyHandler {
    public void onTick() {
        if (Keyboard.isCreated() && CraftPresence.CONFIG != null) {
            try {
                if (Keyboard.isKeyDown(Integer.parseInt(CraftPresence.CONFIG.configKeyCode)) && !CraftPresence.GUIS.isFocused && !CraftPresence.GUIS.openConfigGUI && !CraftPresence.GUIS.configGUIOpened) {
                    CraftPresence.GUIS.openConfigGUI = true;
                }
            } catch (Exception ex) {
                Constants.LOG.error(Constants.TRANSLATOR.translate("craftpresence.logger.error.keybind", CraftPresence.CONFIG.NAME_configKeycode.replaceAll("_", " ")));
                CraftPresence.CONFIG.configKeyCode = Integer.toString(Keyboard.KEY_LCONTROL);
                CraftPresence.CONFIG.updateConfig();
            }
        }
    }
}
