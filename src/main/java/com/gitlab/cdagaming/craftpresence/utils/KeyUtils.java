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
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gitlab.cdagaming.craftpresence.utils;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.google.common.collect.Lists;
import org.lwjgl.input.Keyboard;

import java.util.List;

/**
 * Keyboard Utilities to Parse KeyCodes and handle KeyCode Events
 *
 * @author CDAGaming
 */
public class KeyUtils {
    /**
     * Allowed KeyCode Start Limit and Individual Filters
     * After ESC and Including any KeyCodes under 0x00
     * <p>
     * Notes:
     * LWJGL 2: ESC = 0x01
     * LWJGL 3: ESC = 256
     */
    private final List<Integer> invalidKeys = Lists.newArrayList(1, 256);

    /**
     * Determine if the Source KeyCode fulfills the following conditions
     * <p>
     * 1) Is Not Contained or Listed within {@link KeyUtils#invalidKeys}
     *
     * @param sourceKeyCode The Source KeyCode to Check
     * @return {@code true} if and only if a Valid KeyCode
     */
    public boolean isValidKeyCode(int sourceKeyCode) {
        return !invalidKeys.contains(sourceKeyCode);
    }

    /**
     * Determine the LWJGL KeyCode Name for the inputted KeyCode
     * TODO: Add Overload to allow an Integer
     *
     * @param original A KeyCode, converted to String
     * @return Either an LWJGL KeyCode Name (LCTRL) or the KeyCode if none can be found
     */
    public String getKeyName(String original) {
        if (!StringUtils.isNullOrEmpty(original)) {
            final Tuple<Boolean, Integer> integerData = StringUtils.getValidInteger(original);

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

    /**
     * Tick Method for KeyUtils, that runs on each tick
     * <p>
     * Implemented @ {@link CommandUtils#reloadData}
     */
    void onTick() {
        if (Keyboard.isCreated() && CraftPresence.CONFIG != null) {
            try {
                if (isValidKeyCode(CraftPresence.CONFIG.configKeyCode) && Keyboard.isKeyDown(CraftPresence.CONFIG.configKeyCode) && !CraftPresence.GUIS.isFocused && !CraftPresence.GUIS.openConfigGUI && !CraftPresence.GUIS.configGUIOpened) {
                    CraftPresence.GUIS.openConfigGUI = true;
                }
            } catch (Exception | Error ex) {
                // If an Error Occurs, Reset the Key to it's default value in accordance to the protocol version
                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.keybind", CraftPresence.CONFIG.NAME_configKeyCode.replaceAll("_", " ")));
                CraftPresence.CONFIG.configKeyCode = ModUtils.MCProtocolID > 340 ? 96 : 41;
                CraftPresence.CONFIG.updateConfig();
            }
        }
    }
}
