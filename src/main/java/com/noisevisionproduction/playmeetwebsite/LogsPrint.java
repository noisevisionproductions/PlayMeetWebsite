package com.noisevisionproduction.playmeetwebsite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LogsPrint {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    protected void logDebug(String message) {
        logger.debug(message);
    }
}
