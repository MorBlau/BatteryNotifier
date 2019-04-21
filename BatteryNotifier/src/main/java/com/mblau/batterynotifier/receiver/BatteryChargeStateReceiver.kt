package com.mblau.batterynotifier.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mblau.batterynotifier.model.ChargingState
import com.mblau.batterynotifier.service.CheckBatteryService


class BatteryChargeStateReceiver: BroadcastReceiver() {

    private var checkBatteryService: CheckBatteryService? = null

    init {
        Log.d(TAG, "init called.")
    }

    fun initialize(checkBatteryService: CheckBatteryService): BatteryChargeStateReceiver {
        this.checkBatteryService = checkBatteryService
        return this
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive called.")
        when (intent.action) {
            Intent.ACTION_POWER_CONNECTED -> checkBatteryService?.handleChangedChargeState(ChargingState.CHARGING)
            Intent.ACTION_POWER_DISCONNECTED -> checkBatteryService?.handleChangedChargeState(ChargingState.NOT_CHARGING)
            else -> {
                Log.d(TAG, "Wrong intent, aborting.")
            }
        }
    }

    companion object {
        val TAG = "BattChargeStateReceiver"
    }

}
