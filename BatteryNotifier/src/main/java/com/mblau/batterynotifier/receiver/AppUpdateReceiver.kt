package com.mblau.batterynotifier.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mblau.batterynotifier.service.CheckBatteryService


class AppUpdateReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive called.")
        when (intent.action) {
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                Log.d(TAG, "Action is ${intent.action}, starting service.")
                CheckBatteryService.startIfNeeded(context)
            }
            else -> Log.d(TAG, "Wrong intent, aborting.")
        }
    }

    companion object {
        const val TAG = "AppUpdateReceiver"
    }

}
