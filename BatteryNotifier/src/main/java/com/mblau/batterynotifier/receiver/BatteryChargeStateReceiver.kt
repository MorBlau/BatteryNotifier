package com.mblau.batterynotifier.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mblau.batterynotifier.model.ChargingState
import com.mblau.batterynotifier.service.CheckBatteryService
import com.mblau.batterynotifier.service.SnoozeService


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
            Intent.ACTION_POWER_CONNECTED -> handleChangedChargeState(ChargingState.CHARGING)
            Intent.ACTION_POWER_DISCONNECTED -> handleChangedChargeState(ChargingState.NOT_CHARGING)
            else -> {
                Log.d(TAG, "Wrong intent, aborting.")
            }
        }
    }

    fun handleChangedChargeState(chargingState: ChargingState) {
        checkBatteryService?.handleChangedChargeState(chargingState)
        checkBatteryService?.let {
            SnoozeService.stop(it)
        }
    }

    companion object {
        val TAG = "BattChargeStateReceiver"
    }

}
