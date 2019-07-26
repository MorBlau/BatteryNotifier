package com.mblau.batterynotifier.task

import com.mblau.batterynotifier.service.SnoozeService
import java.util.*

import android.util.Log
import com.mblau.batterynotifier.manager.MyNotificationManager
import com.mblau.batterynotifier.model.ChargingState

class SnoozedNotifyTask(private val context: SnoozeService, private val chargingState: ChargingState, private val batteryPercent: Int): TimerTask() {

    override fun run() {
        Log.d(TAG, "Task started for snoozed notification.")
        val data = CheckBatteryTaskConfig(chargingState)
        MyNotificationManager.notify(context, data, batteryPercent)
    }

    companion object {
        private const val TAG = "SnoozedNotifyTask"
    }

}