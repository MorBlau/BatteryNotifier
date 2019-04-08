package com.mblau.batterynotifier.task

import android.util.Log
import com.mblau.batterynotifier.manager.MyNotificationManager
import com.mblau.batterynotifier.service.CheckBatteryService
import java.util.*

class CheckBatteryTask(private val context: CheckBatteryService,
                       private val dataConfig: CheckBatteryTaskConfig): TimerTask() {

    override fun run() {
        Log.d(TAG, "task started for event ${dataConfig.eventType}.")
        // check charge state and battery percent
        val (chargingState, batteryPercent) = context.checkChargingStateAndBatteryPercent()
        Log.d(TAG, "battery percent is $batteryPercent")

        val batteryThreshold = dataConfig.getBatteryThresholdFromRepository()
        val eventType = dataConfig.eventType
        val didNotify = dataConfig.didNotify
        val hasValuePassedThreshold = dataConfig.hasValuePassedThreshold(batteryPercent, batteryThreshold)
        val chargingStateVerified = dataConfig.verifyChargingState(chargingState)

        if (hasValuePassedThreshold and chargingStateVerified and !didNotify) {
            Log.d(TAG, "Threshold passed! Threshold: $batteryThreshold, event: $eventType")
            MyNotificationManager.notify(context, dataConfig, batteryThreshold)

            Log.d(TAG, "Cancelling task.")
            this.cancel()
        }
    }

    companion object {
        private const val TAG = "CheckBatteryTask"
    }

}