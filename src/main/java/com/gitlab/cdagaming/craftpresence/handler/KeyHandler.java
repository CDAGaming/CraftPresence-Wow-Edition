package com.gitlab.cdagaming.craftpresence.handler;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class KeyHandler {
    private List<KeyBinding> keyBindings = new ArrayList<>();
    private KeyBinding configKeybinding;

    public void register() {
        configKeybinding = new KeyBinding("craftpresence.keybind.config.desc", Keyboard.KEY_RCONTROL, "craftpresence.keybind.category");

        keyBindings.add(configKeybinding);

        for (KeyBinding keyBinding : keyBindings) {
            CraftPresence.instance.gameSettings.keyBindings = ArrayUtils.add(CraftPresence.instance.gameSettings.keyBindings, keyBinding);
        }
    }

    public void onTick() {
        if (CraftPresence.player != null) {
            if (configKeybinding.isPressed() && !CraftPresence.GUIS.openConfigGUI) {
                CraftPresence.GUIS.openConfigGUI = true;
            }
        }
    }
}
