package com.noisevisionproduction.playmeetwebsite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LogsPrint {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    protected void logInfo(String message) {
        logger.info(message);
    }

    protected void logDebug(String message) {
        logger.debug(message);
    }
}
