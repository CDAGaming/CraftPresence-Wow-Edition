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

    public String getKeyName(String original) {
        if (!StringUtils.isNullOrEmpty(original)) {
            Tuple<Boolean, Integer> integerData = StringUtils.getValidInteger(original);

            if (integerData.getFirst() && isValidKeyCode(integerData.getSecond())) {
                // Input is a valid Integer and Valid KeyCode
                final String keyName = Keyboard.getKeyName(integerData.getSecond());

                // If Key Name is not Empty or Null, use that, otherwise use original
                if (!StringUtils.isNullOrEmpty(keyName)) {
                    return keyName;
                } else {
                    return original;
                }
            } else {
                // If Not a Valid Integer or Valid KeyCode, return NONE
                return Keyboard.getKeyName(Keyboard.KEY_NONE);
            }
        } else {
            // If input is a Null Value, return NONE
            return Keyboard.getKeyName(Keyboard.KEY_NONE);
        }
    }

    void onTick() {
        if (Keyboard.isCreated() && CraftPresence.CONFIG != null) {
            try {
                if (isValidKeyCode(Integer.parseInt(CraftPresence.CONFIG.configKeyCode)) && Keyboard.isKeyDown(Integer.parseInt(CraftPresence.CONFIG.configKeyCode)) && !CraftPresence.GUIS.isFocused && !CraftPresence.GUIS.openConfigGUI && !CraftPresence.GUIS.configGUIOpened) {
                    CraftPresence.GUIS.openConfigGUI = true;
                }
            } catch (Exception | Error ex) {
                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.keybind", CraftPresence.CONFIG.NAME_configKeyCode.replaceAll("_", " ")));
                CraftPresence.CONFIG.configKeyCode = Integer.toString(Keyboard.KEY_GRAVE);
                CraftPresence.CONFIG.updateConfig();
            }
        }
    }
}
