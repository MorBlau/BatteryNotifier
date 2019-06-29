package com.mblau.batterynotifier.listener

import android.content.SharedPreferences
import android.util.Log
import com.mblau.batterynotifier.*
import com.mblau.batterynotifier.dao.SharedPreferencesRepository
import com.mblau.batterynotifier.service.CheckBatteryService

private const val TAG = "SharedPrefsChangeListen"

class SharedPreferencesChangeListener(private val context: MainActivity) :
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onSharedPreferenceChanged(prefs: SharedPreferences?, key: String?) {
        when (key) {
            SharedPreferencesRepository.SERVICE_ACTIVE -> onServiceEnabledChanged()
            SharedPreferencesRepository.HIGH_BATTERY_THRESHOLD,
            SharedPreferencesRepository.LOW_BATTERY_THRESHOLD -> onBatteryThresholdChanged()
            SharedPreferencesRepository.NOTIFICATION_SOUND -> onNotificationSoundChanged()
        }
    }

    private fun onServiceEnabledChanged() {
        Log.d(TAG, "onServiceEnabledChanged called.")

        val serviceEnabled = SharedPreferencesRepository.isServiceActive()
        Log.d(TAG, "onServiceEnabledChanged service enabled: $serviceEnabled.")
        when {
            serviceEnabled -> handleServiceEnabled()
            else -> stopBatteryService()
        }
    }

    private fun onBatteryThresholdChanged() {
        when {
            !SharedPreferencesRepository.isAnyServiceEnabled() -> stopBatteryService()
            SharedPreferencesRepository.isServiceActive() -> updateBatteryService()
        }
    }

    private fun onNotificationSoundChanged() {
        // TODO("add notification sound")
    }

    private fun handleServiceEnabled() {
        if (SharedPreferencesRepository.isAnyServiceEnabled())
            startBatteryService()
        else
            return // TODO: add toast - no threshold defined
    }

    private fun startBatteryService() {
        Log.d(TAG, "startBatteryService called.")
        context.setFloatingActionButtonColor(R.color.colorOn)
        CheckBatteryService.start(context)
    }

    private fun stopBatteryService() {
        Log.d(TAG, "stopBatteryService called.")
        context.setFloatingActionButtonColor(R.color.colorOff)
        CheckBatteryService.stop(context)
    }

    private fun updateBatteryService() {
        Log.d(TAG, "updateBatteryService called.")
        CheckBatteryService.restart(context)
        // TODO: update service with new values
    }

}
