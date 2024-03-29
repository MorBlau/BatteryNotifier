package com.mblau.batterynotifier.dao

import android.content.Context

import android.content.SharedPreferences

object SharedPreferencesRepository {

    private const val BATTERY_NOTIFIER_PREFS = "batteryNotifierPrefs"
    const val SERVICE_ACTIVE = "serviceActive"
    const val LOW_BATTERY_THRESHOLD = "lowBatteryThreshold"
    const val HIGH_BATTERY_THRESHOLD = "highBatteryThreshold"
    const val NOTIFICATION_SOUND = "notificationSound"
    const val DELAY_OPTION_1 = "delayOption1"
    const val DELAY_OPTION_2 = "delayOption2"

    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(BATTERY_NOTIFIER_PREFS, Context.MODE_PRIVATE)
    }

    fun getLowBatteryThreshold(): Int {
        return sharedPreferences.getInt(LOW_BATTERY_THRESHOLD, 0)
    }

    fun registerChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun isLowBatteryServiceEnabled(): Boolean {
        return getLowBatteryThreshold() > 0
    }

    fun updateLowBatteryThreshold(threshold: Int) {
        sharedPreferences.edit()
            .putInt(LOW_BATTERY_THRESHOLD, threshold)
            .apply()
    }

    fun getHighBatteryThreshold(): Int {
        return sharedPreferences.getInt(HIGH_BATTERY_THRESHOLD, 0)
    }

    fun getDelayOption1(): Long {
        return sharedPreferences.getLong(DELAY_OPTION_1, 5)
    }

    fun getDelayOption2(): Long {
        return sharedPreferences.getLong(DELAY_OPTION_2, 15)
    }

    fun getDelayOptions(): List<Long> {
        return listOf(getDelayOption1(), getDelayOption2())
    }

    fun isHighBatteryServiceEnabled(): Boolean {
        return getHighBatteryThreshold() > 0
    }

    fun updateHighBatteryThreshold(threshold: Int) {
        sharedPreferences.edit()
            .putInt(HIGH_BATTERY_THRESHOLD, threshold)
            .apply()
    }

    fun isAnyServiceEnabled(): Boolean {
        return isLowBatteryServiceEnabled() or isHighBatteryServiceEnabled()
    }

    fun getNotificationSound(): String {
        return sharedPreferences.getString(NOTIFICATION_SOUND, "DEFAULT")
    }

    fun updateServiceActive(active: Boolean) {
        sharedPreferences.edit()
            .putBoolean(SERVICE_ACTIVE, active)
            .apply()
    }

    fun isServiceActive(): Boolean {
        return sharedPreferences.getBoolean(SERVICE_ACTIVE, false)
    }

}