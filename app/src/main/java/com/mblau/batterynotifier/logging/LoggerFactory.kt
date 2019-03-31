package com.mblau.batterynotifier.logging

object LoggerFactory {

    var loggerMap: HashMap<String, Logger> = hashMapOf()

    fun getLogger(clazz: Class<*>): Logger {
        val name = clazz.javaClass.simpleName
        if (loggerMap.contains(name)) return loggerMap[name]!!
        val logger = Logger(name)
        loggerMap.put(name, logger)
        return logger
    }
}