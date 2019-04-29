package com.gitlab.cdagaming.craftpresence.handler;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import org.lwjgl.input.Keyboard;

public class KeyHandler {
    public void onTick() {
        if (Keyboard.isKeyDown(Integer.parseInt(CraftPresence.CONFIG.configKeyCode)) && !CraftPresence.GUIS.openConfigGUI && !CraftPresence.GUIS.configGUIOpened) {
            CraftPresence.GUIS.openConfigGUI = true;
        }
    }
}
