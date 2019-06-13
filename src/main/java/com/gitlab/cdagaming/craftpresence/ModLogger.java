package com.gitlab.cdagaming.craftpresence;

import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModLogger {
    private String loggerName;
    private Logger logInstance;

    ModLogger(final String loggerName) {
        this.loggerName = loggerName;
        this.logInstance = LogManager.getLogger(loggerName);
    }

    public void error(final String logMessage, Object... logArguments) {
        if (CraftPresence.player != null && !CraftPresence.CONFIG.hasChanged && CraftPresence.CONFIG.showLoggingInChat) {
            StringHandler.sendMessageToPlayer(CraftPresence.player, "§6§l[§f" + loggerName + "§6]§r " + logMessage);
        } else {
            logInstance.error(logMessage, logArguments);
        }
    }

    public void warn(final String logMessage, Object... logArguments) {
        if (CraftPresence.player != null && !CraftPresence.CONFIG.hasChanged && CraftPresence.CONFIG.showLoggingInChat) {
            StringHandler.sendMessageToPlayer(CraftPresence.player, "§6§l[§f" + loggerName + "§6]§r " + logMessage);
        } else {
            logInstance.warn(logMessage, logArguments);
        }
    }

    public void info(final String logMessage, Object... logArguments) {
        if (CraftPresence.player != null && !CraftPresence.CONFIG.hasChanged && CraftPresence.CONFIG.showLoggingInChat) {
            StringHandler.sendMessageToPlayer(CraftPresence.player, "§6§l[§f" + loggerName + "§6]§r " + logMessage);
        } else {
            logInstance.info(logMessage, logArguments);
        }
    }
}
