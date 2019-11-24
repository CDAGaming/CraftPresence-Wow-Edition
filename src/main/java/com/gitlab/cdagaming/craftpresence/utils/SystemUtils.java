package com.gitlab.cdagaming.craftpresence.utils;

import com.gitlab.cdagaming.craftpresence.ModUtils;
import com.google.common.collect.Lists;

import java.util.List;

public class SystemUtils {
    public int TIMER = 0;
    public String OS_NAME, OS_ARCH, USER_DIR;
    public boolean IS_LINUX = false, IS_MAC = false, IS_WINDOWS = false, IS_64_BIT = false;
    public long CURRENT_TIMESTAMP;
    private boolean isTiming = false, refreshedCallbacks = false;
    private long BEGINNING_TIMESTAMP, ELAPSED_TIME;

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

    void onTick() {
        ELAPSED_TIME = (System.currentTimeMillis() - CURRENT_TIMESTAMP) / 1000L;

        if (TIMER > 0) {
            if (!isTiming) {
                startTimer();
            } else {
                checkTimer();
            }
        }

        // Every Two Seconds, refresh Callbacks
        if (ELAPSED_TIME % 2 == 0) {
            if (!refreshedCallbacks) {
                //.Discord_RunCallbacks();
                refreshedCallbacks = true;
            }
        } else {
            refreshedCallbacks = false;
        }
    }

    private void startTimer() {
        BEGINNING_TIMESTAMP = System.currentTimeMillis() + (TIMER * 1000L);
        isTiming = true;
    }

    private void checkTimer() {
        if (TIMER > 0) {
            long remainingTime = (BEGINNING_TIMESTAMP - System.currentTimeMillis()) / 1000L;
            TIMER = (int) remainingTime;
        } else if (isTiming) {
            isTiming = false;
        }
    }
}
