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
import com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities.DiscordStatus;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * System and General Use Utilities
 *
 * @author CDAGaming
 */
public class SystemUtils {
    /**
     * The Current Time Remaining on the Timer
     */
    public int TIMER = 0;

    /**
     * The Name of the User's Operating System
     */
    public String OS_NAME;

    /**
     * The Architecture of the User's System
     */
    public String OS_ARCH;

    /**
     * The Directory the Application is running in
     */
    public String USER_DIR;

    /**
     * If the {@link SystemUtils#OS_NAME} can be classified as LINUX
     */
    public boolean IS_LINUX = false;

    /**
     * If the {@link SystemUtils#OS_NAME} can be classified as MAC
     */
    public boolean IS_MAC = false;

    /**
     * If the {@link SystemUtils#OS_NAME} can be classified as WINDOWS
     */
    public boolean IS_WINDOWS = false;

    /**
     * If the {@link SystemUtils#OS_ARCH} is 64-bit or x64
     */
    public boolean IS_64_BIT = false;

    /**
     * If Loading of critical data has been completed<p>
     * Becomes true after callbacks synchronize once if previously false
     */
    public boolean HAS_LOADED = false;

    /**
     * The Current Epoch Unix Timestamp in Milliseconds
     */
    public long CURRENT_TIMESTAMP;

    /**
     * Whether the Timer is Currently Active
     */
    private boolean isTiming = false;

    /**
     * Whether the Callbacks related to the Mod have been refreshed
     * <p>
     * In this case, the RPC Updates every 2 Seconds with this check ensuring such
     */
    private boolean refreshedCallbacks = false;

    /**
     * The Beginning Unix Timestamp to count down from
     */
    private long BEGINNING_TIMESTAMP;

    /**
     * The Elapsed Time since the application started (In Seconds)
     */
    private long ELAPSED_TIME;

    /**
     * Initialize OS and Timer Information
     */
    public SystemUtils() {
        try {
            OS_NAME = System.getProperty("os.name");
            OS_ARCH = System.getProperty("os.arch");
            USER_DIR = System.getProperty("user.dir");

            IS_LINUX = OS_NAME.startsWith("Linux") || OS_NAME.startsWith("LINUX");
            IS_MAC = OS_NAME.startsWith("Mac");
            IS_WINDOWS = OS_NAME.startsWith("Windows");
            CURRENT_TIMESTAMP = System.currentTimeMillis();
            ELAPSED_TIME = 0;

            // Calculate if 64-Bit Architecture
            final List<String> x64 = Lists.newArrayList("amd64", "x86_64");
            IS_64_BIT = x64.contains(OS_ARCH);
        } catch (Exception ex) {
            ModUtils.LOG.error(ModUtils.TRANSLATOR.translate("craftpresence.logger.error.system"));
            ex.printStackTrace();
        }
    }

    /**
     * The Event to Run on each Client Tick, if passed initialization events
     * <p>
     * Comprises of Synchronizing Data, and Updating Timer-Related Data as needed
     */
    void onTick() {
        ELAPSED_TIME = (System.currentTimeMillis() - CURRENT_TIMESTAMP) / 1000L;

        if (TIMER > 0) {
            if (!isTiming) {
                startTimer();
            } else {
                checkTimer();
            }
        }

        // Every <passTime> Seconds, refresh Callbacks and load state status
        if (ELAPSED_TIME % CraftPresence.CONFIG.refreshRate == 0) {
            if (!refreshedCallbacks) {
                if (!HAS_LOADED && CraftPresence.CLIENT.STATUS == DiscordStatus.Ready) {
                    HAS_LOADED = true;
                }
                CraftPresence.CLIENT.updatePresence(CraftPresence.CLIENT.buildRichPresence());
                refreshedCallbacks = true;
            }
        } else {
            refreshedCallbacks = false;
        }
    }

    /**
     * Begins the Timer, counting down from {@link SystemUtils#BEGINNING_TIMESTAMP}
     */
    private void startTimer() {
        BEGINNING_TIMESTAMP = System.currentTimeMillis() + (TIMER * 1000L);
        isTiming = true;
    }

    /**
     * Determines the Remaining Time until 0, and Stops the Timer @ 0 remaining
     */
    private void checkTimer() {
        if (TIMER > 0) {
            long remainingTime = (BEGINNING_TIMESTAMP - System.currentTimeMillis()) / 1000L;
            TIMER = (int) remainingTime;
        } else if (isTiming) {
            isTiming = false;
        }
    }
}
