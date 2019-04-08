package com.mblau.batterynotifier.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
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
        val isCharging = when (intent.action) {
            Intent.ACTION_POWER_CONNECTED -> true
            Intent.ACTION_POWER_DISCONNECTED -> false
            else -> {
                Log.d(TAG, "Wrong intent, aborting.")
                return
            }
        }
        checkBatteryService?.handleChangedChargeState(isCharging)
    }

    private fun handleChargeState(isCharging: Boolean) {
        when {
            checkBatteryService == null -> Log.e(TAG, "checkBatteryService is null!!!")
            isCharging -> checkBatteryService!!.startedCharging()
            !isCharging -> checkBatteryService!!.stoppedCharging()
        }
    }

    companion object {
        val TAG = "BattChargeStateReceiver"
    }

}
