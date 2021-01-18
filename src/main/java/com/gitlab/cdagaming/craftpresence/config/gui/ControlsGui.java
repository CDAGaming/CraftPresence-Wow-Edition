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

package com.gitlab.cdagaming.craftpresence.config.gui;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.config.ConfigUtils;
import com.gitlab.cdagaming.craftpresence.impl.DataConsumer;
import com.gitlab.cdagaming.craftpresence.impl.Pair;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.KeyUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedScreen;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

// TODO - See below list
// - Add Paginated Support when PaginatedScreen becomes available
// - Filter the keyMappings based on Categories to display properly
public class ControlsGui extends ExtendedScreen {

    // Pair Format: buttonToModify, Config Field to Edit
    // (Store a Backup of Prior Text just in case)
    private String backupKeyString;
    private Pair<ExtendedButtonControl, String> entryData = null;
    private String[] filterData = new String[]{};
    private KeyUtils.FilterMode filterMode;
    // Format: See KeyUtils#KEY_MAPPINGS
    private Map<String, Tuple<KeyBinding, Runnable, DataConsumer<Throwable>>> keyMappings;
    // Format: categoryName:keyNames
    private Map<String, List<String>> categorizedNames = Maps.newHashMap();

    // TODO: Refactor all fields below into paginated screen
    private int nextCategoryRow = 1;

    ControlsGui(GuiScreen parentScreen) {
        super(parentScreen);
        this.filterMode = KeyUtils.FilterMode.None;
        this.keyMappings = CraftPresence.KEYBINDINGS.getKeyMappings();

        sortMappings();
    }

    ControlsGui(GuiScreen parentScreen, KeyUtils.FilterMode filterMode, String... filterData) {
        super(parentScreen);
        this.filterData = filterData;
        this.filterMode = filterMode;
        this.keyMappings = CraftPresence.KEYBINDINGS.getKeyMappings(filterMode, Arrays.asList(filterData));

        sortMappings();
    }

    @Override
    public void initializeUi() {
        // Adding Back Button
        addControl(
                new ExtendedButtonControl(
                        10, (height - 30),
                        95, 20,
                        ModUtils.TRANSLATOR.translate("gui.config.message.button.back"),
                        () -> CraftPresence.GUIS.openScreen(parentScreen)
                )
        );

        super.initializeUi();
    }

    @Override
    public void preRender() {
        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.message.button.controls");
        renderString(mainTitle, (width / 2f) - (StringUtils.getStringWidth(mainTitle) / 2f), 10, 0xFFFFFF);
        renderString(subTitle, (width / 2f) - (StringUtils.getStringWidth(subTitle) / 2f), 20, 0xFFFFFF);

        // TODO: Refactor for Paginated Support
        // (Reallocate to initializeUi first
        for (String categoryName : categorizedNames.keySet()) {
            renderString(ModUtils.TRANSLATOR.translate(categoryName), (width / 2f) - (StringUtils.getStringWidth(categoryName) / 2f), CraftPresence.GUIS.getButtonY(nextCategoryRow, 5), 0xFFFFFF);

            final List<String> keyNames = categorizedNames.get(categoryName);
            int nextRow = nextCategoryRow + 1;
            //nextCategoryRow += keyNames.size();

            for (String keyName : keyNames) {
                renderString(ModUtils.TRANSLATOR.translate(keyMappings.get(keyName).getFirst().getKeyDescription()), (width / 2f) - 130, CraftPresence.GUIS.getButtonY(nextRow, 5), 0xFFFFFF);
                nextRow++;
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (entryData != null) {
            setKeyData(keyCode);
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    /**
     * Sort Key Mappings via their categories, used for placement into gui
     */
    private void sortMappings() {
        for (String keyName : keyMappings.keySet()) {
            final Tuple<KeyBinding, Runnable, DataConsumer<Throwable>> keyData = keyMappings.get(keyName);
            if (!categorizedNames.containsKey(keyData.getFirst().getKeyCategory())) {
                categorizedNames.put(keyData.getFirst().getKeyCategory(), Lists.newArrayList(keyName));
            } else if (!categorizedNames.get(keyData.getFirst().getKeyCategory()).contains(keyName)) {
                categorizedNames.get(keyData.getFirst().getKeyCategory()).add(keyName);
            }
        }
    }

    /**
     * Setup for Key Entry and Save Backup of Prior Setting, if a valid Key Button
     *
     * @param button The Pressed upon KeyCode Button
     */
    private void setupEntryData(final ExtendedButtonControl button) {
        if (entryData == null && button.getOptionalArgs() != null) {
            entryData = new Pair<>(button, button.getOptionalArgs()[0]);

            backupKeyString = button.getControlMessage();
            button.setControlMessage(ModUtils.TRANSLATOR.translate("gui.config.message.editor.enter_key"));
        }
    }

    /**
     * Sets a New KeyCode for the currently queued entry data
     * Entry Data format -> buttonToModify, Config Field to Edit
     *
     * @param keyCode The New KeyCode for modifying
     */
    private void setKeyData(final int keyCode) {
        int keyToSubmit = keyCode;

        // Ensure a Valid KeyCode is entered
        if (!CraftPresence.KEYBINDINGS.isValidKeyCode(keyToSubmit)) {
            keyToSubmit = Keyboard.KEY_NONE;
        }

        final String formattedKey = CraftPresence.KEYBINDINGS.getKeyName(keyToSubmit);

        // If KeyCode Field to modify is not null or empty, attempt to queue change
        try {
            StringUtils.updateField(ConfigUtils.class, CraftPresence.CONFIG, new Tuple<>(entryData.getSecond(), keyToSubmit, null));
            CraftPresence.CONFIG.keySyncQueue.put(entryData.getSecond(), keyToSubmit);
            CraftPresence.CONFIG.hasChanged = true;

            entryData.getFirst().setControlMessage(formattedKey);
        } catch (Exception | Error ex) {
            entryData.getFirst().setControlMessage(backupKeyString);
            ex.printStackTrace();
        }

        // Clear Data
        backupKeyString = null;
        entryData = null;
    }
}

