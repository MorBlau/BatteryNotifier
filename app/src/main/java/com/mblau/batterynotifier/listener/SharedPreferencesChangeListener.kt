package com.mblau.batterynotifier.listener

import android.content.SharedPreferences
import android.util.Log
import com.mblau.batterynotifier.Constants.VALUE_OFF
import com.mblau.batterynotifier.*

private const val TAG = "SharedPrefsChangeListen"

class SharedPreferencesChangeListener(private val context: MainActivity) :
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val myNotificationManager: MyNotificationManager = MyNotificationManager()

    override fun onSharedPreferenceChanged(prefs: SharedPreferences?, key: String?) {
        when (key) {
            SharedPreferencesRepository.SERVICE_ENABLED -> onServiceEnabledChanged()
            SharedPreferencesRepository.HIGH_BATTERY_THRESHOLD,
            SharedPreferencesRepository.LOW_BATTERY_THRESHOLD -> onBatteryThresholdChanged()
            SharedPreferencesRepository.NOTIFICATION_SOUND -> onNotificationSoundChanged()
        }
    }

    private fun onServiceEnabledChanged() {
        Log.d(TAG, "onServiceEnabledChanged called.")

        val serviceEnabled = SharedPreferencesRepository.isServiceActive()
        val colorId = if (serviceEnabled) R.color.colorOn else R.color.colorOff
        val smallText = "Hello world!!!"
        val bigText =
            "$smallText\n" +
                    "Active: ${if (serviceEnabled) "yes" else "no"}.\n" +
                    "High battery threshold: ${SharedPreferencesRepository.getHighBatteryThreshold()}.\n" +
                    "Low battery threshold: ${SharedPreferencesRepository.getLowBatteryThreshold()}."

        myNotificationManager.notify(context, colorId, smallText, bigText)

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
        if (SharedPreferencesRepository.isAnyServiceEnabled()) startBatteryService()
        else return // TODO: add toast - no threshold defined
    }

    private fun startBatteryService() {
        context.setFabColor(R.color.colorOn)
        BatteryCheckupService.start(context)
    }

    private fun stopBatteryService() {
        context.setFabColor(R.color.colorOff)
        BatteryCheckupService.stop(context)
    }

    private fun updateBatteryService() {
        // TODO: update service with new values
    }

}
