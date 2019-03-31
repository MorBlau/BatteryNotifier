package com.mblau.batterynotifier.logging

import java.util.logging.Level
import java.util.logging.Logger

class Logger(name: String) {

    private var logger: Logger = Logger.getLogger(name)

    fun config(msg: String) {
        logger.log(Level.CONFIG, msg)
    }

    fun info(msg: String) {
        logger.log(Level.INFO, msg)
    }

    fun warning(msg: String) {
        logger.log(Level.WARNING, msg)
    }

    fun severe(msg: String) {
        logger.log(Level.SEVERE, msg)
    }
}