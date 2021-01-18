/*
 * MIT License
 *
 * Copyright (c) 2018 - 2021 CDAGaming (cstack2011@yahoo.com)
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
import com.gitlab.cdagaming.craftpresence.impl.DataConsumer;
import com.gitlab.cdagaming.craftpresence.impl.Pair;
import com.google.common.collect.Maps;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import java.util.Map;

/**
 * Keyboard Utilities to Parse KeyCodes and handle KeyCode Events
 *
 * @author CDAGaming
 */
public class KeyUtils {
    private final Map<KeyBinding, Pair<Runnable, DataConsumer<Throwable>>> KEY_MAPPING = Maps.newHashMap();

    void register() {
        KEY_MAPPING.put(
                new KeyBinding("key.craftpresence.config_keycode.name", Keyboard.KEY_GRAVE, "key.craftpresence.category"),
                new Pair<>(
                        () -> {
                            if (!CraftPresence.GUIS.isFocused && !CraftPresence.GUIS.openConfigGUI && !CraftPresence.GUIS.configGUIOpened) {
                                CraftPresence.GUIS.openConfigGUI = true;
                            }
                        }, null
                )
        );

        for (KeyBinding keyBind : KEY_MAPPING.keySet()) {
            CraftPresence.instance.gameSettings.keyBindings = ArrayUtils.add(CraftPresence.instance.gameSettings.keyBindings, keyBind);
        }
    }

    /**
     * Tick Method for KeyUtils, that runs on each tick
     * <p>
     * Implemented @ {@link CommandUtils#reloadData}
     */
    void onTick() {
        if (CraftPresence.instance != null) {
            try {
                for (KeyBinding keyBind : KEY_MAPPING.keySet()) {
                    if (keyBind.isPressed()) {
                        final Pair<Runnable, DataConsumer<Throwable>> keyData = KEY_MAPPING.get(keyBind);
                        try {
                            keyData.getFirst().run();
                        } catch (Exception | Error ex) {
                            if (keyData.getSecond() != null) {
                                keyData.getSecond().accept(ex);
                            } else {
                                ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.keycode", keyBind.getDisplayName()));
                            }
                        }
                    }
                }
            } catch (Exception | Error ex) {
                if (ModUtils.IS_VERBOSE) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
