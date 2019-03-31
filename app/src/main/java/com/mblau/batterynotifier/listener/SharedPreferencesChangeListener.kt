package com.mblau.batterynotifier.listener

import android.content.SharedPreferences
import com.mblau.batterynotifier.Constants.VALUE_OFF
import com.mblau.batterynotifier.SharedPreferencesRepository

class SharedPreferencesChangeListener :
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onSharedPreferenceChanged(prefs: SharedPreferences?, key: String?) {
        when (key) {
            SharedPreferencesRepository.SERVICE_ENABLED -> onServiceEnabledChanged()
            SharedPreferencesRepository.HIGH_BATTERY_THRESHOLD -> onHighBatteryThresholdChanged()
            SharedPreferencesRepository.LOW_BATTERY_THRESHOLD -> onLowBatteryThresholdChanged()
            SharedPreferencesRepository.NOTIFICATION_SOUND -> onNotificationSoundChanged()
        }
    }

    private fun onServiceEnabledChanged() {
        val serviceEnabled = SharedPreferencesRepository.isServiceEnabled()
        when {
            serviceEnabled -> true //TODO("create service and start")
            else -> true //TODO("destroy service (all services in case more than one is running?)")
        }
    }

    private fun onHighBatteryThresholdChanged() {
        val highBatteryThreshold = SharedPreferencesRepository.getHighBatteryThreshold()
        when (highBatteryThreshold) {
            VALUE_OFF -> true //TODO("turn off high battery service/listener or destroy service if needed")
            else -> true //TODO("if service is running, update service with new value)")
        }
    }

    private fun onLowBatteryThresholdChanged() {
        val lowBatteryThreshold = SharedPreferencesRepository.getLowBatteryThreshold()
        when (lowBatteryThreshold) {
            VALUE_OFF -> true //TODO("turn off low battery service/listener or destroy service if needed")
            else -> true //TODO("if service is running, update service with new value)")
        }
    }

    private fun onNotificationSoundChanged() {
        // TODO("add notification sound")
    }

}