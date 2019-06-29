package com.mblau.batterynotifier.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mblau.batterynotifier.dao.SharedPreferencesRepository
import com.mblau.batterynotifier.service.CheckBatteryService


class SystemEventReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive called.")
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED, Intent.ACTION_MY_PACKAGE_REPLACED -> {
                Log.d(TAG, "Action is ${intent.action}, starting service.")
                if (SharedPreferencesRepository.isServiceActive())
                CheckBatteryService.start(context)
            }
            else -> Log.d(TAG, "Wrong intent, aborting.")
        }
    }

    companion object {
        const val TAG = "SystemEventReceiver"
    }

}
