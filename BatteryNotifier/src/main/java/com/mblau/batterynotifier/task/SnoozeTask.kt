//package com.mblau.batterynotifier.task
//
//import android.util.Log
//import com.mblau.batterynotifier.manager.MyNotificationManager
//import com.mblau.batterynotifier.service.CheckBatteryService
//import java.util.*
//
//class SnoozeTask(private val context: CheckBatteryService, private val notificationManager: MyNotificationManager): TimerTask() {
//
//    override fun run() {
//        Log.d(TAG, "task started for notification.")
//        // check charge state and battery percent
//        val (chargingState, batteryPercent) = context.checkChargingStateAndBatteryPercent()
//        Log.d(TAG, "battery percent is $batteryPercent")
//
//        val batteryThreshold = dataConfig.getBatteryThresholdFromRepository()
//        val eventType = dataConfig.eventType
//        val didNotify = dataConfig.didNotify
//        val hasValuePassedThreshold = dataConfig.hasValuePassedThreshold(batteryPercent, batteryThreshold)
//        val chargingStateVerified = dataConfig.verifyChargingState(chargingState)
//
//        if (hasValuePassedThreshold and chargingStateVerified and !didNotify) {
//            Log.d(TAG, "Threshold passed! Threshold: $batteryThreshold, event: $eventType")
//            notify(dataConfig, batteryThreshold)
//
//            Log.d(TAG, "Cancelling task.")
//            this.cancel()
//        }
//    }
//
//    private fun notify(data: CheckBatteryTaskConfig, batteryPercent: Int) {
//        Log.d(TAG, "notify called.")
//
//        Log.d(TAG, "Battery event will be notified.")
//        val colorId = data.getColorId()
//        val smallText = context.getString(data.getSmallTextId(), batteryPercent)
//        val bigText = context.getString(data.getBigTextId(), batteryPercent)
//
//        notificationManager.notify(context, colorId, smallText, bigText, 5, 10)
//        data.didNotify = true
//    }
//
//    companion object {
//        private const val TAG = "CheckBatteryTask"
//    }
//
//}