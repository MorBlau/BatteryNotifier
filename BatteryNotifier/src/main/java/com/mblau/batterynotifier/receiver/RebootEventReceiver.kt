package com.mblau.batterynotifier.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.mblau.batterynotifier.dao.SharedPreferencesRepository
import com.mblau.batterynotifier.service.CheckBatteryService


class RebootEventReceiver: BroadcastReceiver() {

    private val sharedPreferencesRepository = SharedPreferencesRepository

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive called.")
        Toast.makeText(context, "Reboot event received.", Toast.LENGTH_LONG).show()

        // after reboot, we need to re-initialize the repository
        sharedPreferencesRepository.initialize(context)

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                Log.d(TAG, "Action is ${intent.action}, starting service.")
                Toast.makeText(context, "Reboot event received.", Toast.LENGTH_LONG).show()
                CheckBatteryService.startIfNeeded(context)
            }
            else -> Log.d(TAG, "Wrong intent, aborting.")
        }
    }

    companion object {
        const val TAG = "RebootEventReceiver"
    }

}
