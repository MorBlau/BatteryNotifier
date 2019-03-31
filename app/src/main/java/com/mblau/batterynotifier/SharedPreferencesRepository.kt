package com.mblau.batterynotifier

import android.content.Context

import android.content.SharedPreferences

object SharedPreferencesRepository {

    private const val BATTERY_NOTIFIER_PREFS = "batteryNotifierPrefs"
    const val SERVICE_ENABLED = "serviceEnabled"
    const val LOW_BATTERY_THRESHOLD = "lowBatteryThreshold"
    const val HIGH_BATTERY_THRESHOLD = "highBatteryThreshold"
    const val NOTIFICATION_SOUND = "notificationSound"

    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(BATTERY_NOTIFIER_PREFS,Context.MODE_PRIVATE)
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

    fun isHighBatteryServiceEnabled(): Boolean {
        return getHighBatteryThreshold() > 0
    }

    fun updateHighBatteryThreshold(threshold: Int) {
        sharedPreferences.edit()
            .putInt(HIGH_BATTERY_THRESHOLD, threshold)
            .apply()
    }

    fun getNotificationSound(): String {
        return sharedPreferences.getString(NOTIFICATION_SOUND,"DEFAULT")
    }

    fun updateServiceEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(SERVICE_ENABLED, enabled)
            .apply()
    }

    fun isServiceEnabled(): Boolean {
        return sharedPreferences.getBoolean(SERVICE_ENABLED, false)
    }

}