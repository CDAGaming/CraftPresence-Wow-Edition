package com.gitlab.cdagaming.craftpresence;

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
        logInstance.error(logMessage, logArguments);
    }

    public void warn(final String logMessage, Object... logArguments) {
        logInstance.warn(logMessage, logArguments);
    }

    public void info(final String logMessage, Object... logArguments) {
        logInstance.info(logMessage, logArguments);
    }
}
