package com.gitlab.cdagaming.craftpresence.handler;

import com.gitlab.cdagaming.craftpresence.Constants;
import com.gitlab.cdagaming.craftpresence.CraftPresence;

public class SystemHandler {
    public String OS_NAME, OS_ARCH, USER_DIR;
    public boolean isTiming = false;
    public boolean IS_LINUX = false, IS_MAC = false, IS_WINDOWS = false;
    private long BEGINNING_TIMESTAMP;

    public SystemHandler() {
        try {
            OS_NAME = System.getProperty("os.name");
            OS_ARCH = System.getProperty("os.arch");
            USER_DIR = System.getProperty("user.dir");

            IS_LINUX = OS_NAME.startsWith("Linux") || OS_NAME.startsWith("LINUX");
            IS_MAC = OS_NAME.startsWith("Mac");
            IS_WINDOWS = OS_NAME.startsWith("Windows");
        } catch (Exception ex) {
            Constants.LOG.error(Constants.TRANSLATOR.translate("craftpresence.logger.error.system"));
            ex.printStackTrace();
        }
    }

    public void startTimer() {
        BEGINNING_TIMESTAMP = System.currentTimeMillis() + (CraftPresence.TIMER * 1000L);
        isTiming = true;
    }

    public void checkTimer() {
        if (CraftPresence.TIMER > 0) {
            long remainingTime = (BEGINNING_TIMESTAMP - System.currentTimeMillis()) / 1000L;
            CraftPresence.TIMER = (int) remainingTime;
        } else if (isTiming) {
            isTiming = false;
        }
    }
}
