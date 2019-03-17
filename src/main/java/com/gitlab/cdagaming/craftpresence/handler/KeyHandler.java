package com.gitlab.cdagaming.craftpresence.handler;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

public class KeyHandler {
    private List<KeyBinding> keyBindings = Lists.newArrayList();
    private KeyBinding configKeybinding;

    public void register() {
        keyBindings.addAll(Arrays.asList(CraftPresence.instance.gameSettings.keyBindings));
        configKeybinding = new KeyBinding("key.craftpresence.config_keybind", Keyboard.KEY_RCONTROL, "key.craftpresence.category");

        keyBindings.add(configKeybinding);

        CraftPresence.instance.gameSettings.keyBindings = keyBindings.toArray(new KeyBinding[]{});
    }

    public void onTick() {
        if (configKeybinding != null) {
            if (configKeybinding.getKeyCode() < 0) {
                Constants.LOG.error(I18n.format("craftpresence.logger.error.keybind"));
                configKeybinding.setKeyCode(Keyboard.KEY_RCONTROL);
            } else if (Keyboard.isKeyDown(configKeybinding.getKeyCode()) && !(CraftPresence.instance.currentScreen instanceof GuiControls) && !CraftPresence.GUIS.openConfigGUI && !CraftPresence.GUIS.configGUIOpened) {
                CraftPresence.GUIS.openConfigGUI = true;
            }
        }
    }
}
