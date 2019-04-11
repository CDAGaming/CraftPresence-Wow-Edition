package com.gitlab.cdagaming.craftpresence.handler;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.handler.discord.rpc.DiscordRPC;

public class SystemHandler {
    public int TIMER = 0;
    public String OS_NAME, OS_ARCH, USER_DIR;
    public boolean IS_LINUX = false, IS_MAC = false, IS_WINDOWS = false;
    public long CURRENT_TIMESTAMP;
    private boolean isTiming = false, refreshedCallbacks = false;
    private long BEGINNING_TIMESTAMP, ELAPSED_TIME;

    public SystemHandler() {
        try {
            OS_NAME = System.getProperty("os.name");
            OS_ARCH = System.getProperty("os.arch");
            USER_DIR = System.getProperty("user.dir");

            IS_LINUX = OS_NAME.startsWith("Linux") || OS_NAME.startsWith("LINUX");
            IS_MAC = OS_NAME.startsWith("Mac");
            IS_WINDOWS = OS_NAME.startsWith("Windows");
            CURRENT_TIMESTAMP = System.currentTimeMillis();
        } catch (Exception ex) {
            Constants.LOG.error(Constants.TRANSLATOR.translate("craftpresence.logger.error.system"));
            ex.printStackTrace();
        }
    }

    public void tick() {
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
                DiscordRPC.INSTANCE.Discord_RunCallbacks();
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
