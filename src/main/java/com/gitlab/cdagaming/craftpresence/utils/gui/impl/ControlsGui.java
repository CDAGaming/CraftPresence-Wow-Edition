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

package com.gitlab.cdagaming.craftpresence.utils.gui.impl;

import com.gitlab.cdagaming.craftpresence.CraftPresence;
import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.gitlab.cdagaming.craftpresence.config.ConfigUtils;
import com.gitlab.cdagaming.craftpresence.impl.DataConsumer;
import com.gitlab.cdagaming.craftpresence.impl.Pair;
import com.gitlab.cdagaming.craftpresence.impl.Tuple;
import com.gitlab.cdagaming.craftpresence.utils.KeyUtils;
import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.gitlab.cdagaming.craftpresence.utils.gui.controls.ExtendedButtonControl;
import com.gitlab.cdagaming.craftpresence.utils.gui.integrations.PaginatedScreen;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ControlsGui extends PaginatedScreen {

    // Format: See KeyUtils#KEY_MAPPINGS
    private final Map<String, Tuple<KeyBinding, Runnable, DataConsumer<Throwable>>> keyMappings;
    // Format: categoryName:keyNames
    private final Map<String, List<String>> categorizedNames = Maps.newHashMap();
    // Format: pageNumber:[elementText:[xPos:yPos]:color]
    private final Map<Integer, List<Tuple<String, Pair<Float, Float>, Integer>>> preRenderQueue = Maps.newHashMap(), postRenderQueue = Maps.newHashMap();
    // Pair Format: buttonToModify, Config Field to Edit
    // (Store a Backup of Prior Text just in case)
    private String backupKeyString;
    private Pair<ExtendedButtonControl, String> entryData = null;
    private final int maxElementsPerPage = 7, startRow = 1;
    private int currentAllocatedRow = startRow, currentAllocatedPage = startPage;

    public ControlsGui(GuiScreen parentScreen) {
        super(parentScreen);
        this.keyMappings = CraftPresence.KEYBINDINGS.getKeyMappings();

        sortMappings();
    }

    public ControlsGui(GuiScreen parentScreen, KeyUtils.FilterMode filterMode, String... filterData) {
        super(parentScreen);
        this.keyMappings = CraftPresence.KEYBINDINGS.getKeyMappings(filterMode, Arrays.asList(filterData));

        sortMappings();
    }

    @Override
    public void initializeUi() {
        setupScreenData();

        super.initializeUi();

        backButton.setOnClick(
                () -> {
                    if (entryData == null) {
                        CraftPresence.GUIS.openScreen(parentScreen);
                    }
                }
        );
    }

    @Override
    public void preRender() {
        final String mainTitle = ModUtils.TRANSLATOR.translate("gui.config.title");
        final String subTitle = ModUtils.TRANSLATOR.translate("gui.config.message.button.controls");
        renderString(mainTitle, (width / 2f) - (StringUtils.getStringWidth(mainTitle) / 2f), 10, 0xFFFFFF);
        renderString(subTitle, (width / 2f) - (StringUtils.getStringWidth(subTitle) / 2f), 20, 0xFFFFFF);

        super.preRender();

        for (Integer pageNumber : preRenderQueue.keySet()) {
            final List<Tuple<String, Pair<Float, Float>, Integer>> elementList = preRenderQueue.get(pageNumber);
            for (Tuple<String, Pair<Float, Float>, Integer> elementData : elementList) {
                renderString(ModUtils.TRANSLATOR.translate(elementData.getFirst()), elementData.getSecond().getFirst(), elementData.getSecond().getSecond(), elementData.getThird(), pageNumber);
            }
        }
    }

    @Override
    public void postRender() {
        for (Integer pageNumber : postRenderQueue.keySet()) {
            final List<Tuple<String, Pair<Float, Float>, Integer>> elementList = postRenderQueue.get(pageNumber);
            for (Tuple<String, Pair<Float, Float>, Integer> elementData : elementList) {
                if (currentPage == pageNumber && CraftPresence.GUIS.isMouseOver(getMouseX(), getMouseY(), elementData.getSecond().getFirst(), elementData.getSecond().getSecond(), StringUtils.getStringWidth(ModUtils.TRANSLATOR.translate(elementData.getFirst())), getFontHeight())) {
                    CraftPresence.GUIS.drawMultiLineString(StringUtils.splitTextByNewLine(ModUtils.TRANSLATOR.translate(elementData.getFirst().replace(".name", ".description"))), getMouseX(), getMouseY(), width, height, getWrapWidth(), getFontRenderer(), true);
                }
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
     * Setup Rendering Queues for different parts of the Screen
     */
    private void setupScreenData() {
        // Clear any Prior Data before hand'
        preRenderQueue.clear();
        postRenderQueue.clear();

        final int renderPosition = (width / 2) + 3;
        for (String categoryName : categorizedNames.keySet()) {
            syncPageData();
            final Tuple<String, Pair<Float, Float>, Integer> categoryData = new Tuple<>(categoryName, new Pair<>((width / 2f) - (StringUtils.getStringWidth(categoryName) / 2f), (float) CraftPresence.GUIS.getButtonY(currentAllocatedRow, 5)), 0xFFFFFF);
            if (!preRenderQueue.containsKey(currentAllocatedPage)) {
                preRenderQueue.put(currentAllocatedPage, Lists.newArrayList(categoryData));
            } else {
                preRenderQueue.get(currentAllocatedPage).add(categoryData);
            }

            final List<String> keyNames = categorizedNames.get(categoryName);
            currentAllocatedRow++;

            for (String keyName : keyNames) {
                final Tuple<KeyBinding, Runnable, DataConsumer<Throwable>> keyData = keyMappings.get(keyName);
                final Tuple<String, Pair<Float, Float>, Integer> positionData = new Tuple<>(keyData.getFirst().getKeyDescription(), new Pair<>((width / 2f) - 130, (float) CraftPresence.GUIS.getButtonY(currentAllocatedRow, 5)), 0xFFFFFF);
                if (!preRenderQueue.containsKey(currentAllocatedPage)) {
                    preRenderQueue.put(currentAllocatedPage, Lists.newArrayList(positionData));
                } else {
                    preRenderQueue.get(currentAllocatedPage).add(positionData);
                }

                if (!postRenderQueue.containsKey(currentAllocatedPage)) {
                    postRenderQueue.put(currentAllocatedPage, Lists.newArrayList(positionData));
                } else {
                    postRenderQueue.get(currentAllocatedPage).add(positionData);
                }

                final ExtendedButtonControl keyCodeButton = new ExtendedButtonControl(
                        renderPosition + 20, CraftPresence.GUIS.getButtonY(currentAllocatedRow),
                        120, 20,
                        CraftPresence.KEYBINDINGS.getKeyName(keyData.getFirst().getKeyCode()),
                        keyName
                );
                keyCodeButton.setOnClick(() -> setupEntryData(keyCodeButton));

                addControl(keyCodeButton, currentAllocatedPage);
                currentAllocatedRow++;
                syncPageData();
            }
        }
    }

    /**
     * Synchronize Page Data based on placed elements
     */
    private void syncPageData() {
        if (currentAllocatedRow >= maxElementsPerPage) {
            currentAllocatedPage++;
            currentAllocatedRow = startRow;
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

