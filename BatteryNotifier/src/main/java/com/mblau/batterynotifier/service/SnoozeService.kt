package com.mblau.batterynotifier.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.text.format.DateUtils
import android.util.Log
import com.mblau.batterynotifier.manager.EXTRA_SNOOZE_BATTERY
import com.mblau.batterynotifier.manager.EXTRA_SNOOZE_DELAY
import com.mblau.batterynotifier.manager.EXTRA_SNOOZE_STATE
import com.mblau.batterynotifier.manager.MyNotificationManager
import com.mblau.batterynotifier.model.ChargingState
import com.mblau.batterynotifier.task.SnoozedNotifyTask
import java.util.*

private var instance: SnoozeService? = null

class SnoozeService: Service() {

    private var timer: Timer? = null
    private var snoozedNotifyTask: SnoozedNotifyTask? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "SnoozeService started")
        instance = this
        val delay = intent.getLongExtra(EXTRA_SNOOZE_DELAY, 5 * DateUtils.MINUTE_IN_MILLIS)
        val chargingState = intent.getSerializableExtra(EXTRA_SNOOZE_STATE) as ChargingState
        val batteryPercent = intent.getIntExtra(EXTRA_SNOOZE_BATTERY, 0)
        MyNotificationManager.removeNotification(this)
        createNewSnoozeTask(chargingState, batteryPercent)
        startTimer(delay)
        return START_NOT_STICKY
    }

    private fun startTimer(delay: Long) {
        Log.d(TAG, "Starting task timer.")
        timer?.cancel()
        timer = Timer()
        val startTime = Date(Date().time + delay)
        timer!!.schedule(snoozedNotifyTask, startTime)
    }

    private fun createNewSnoozeTask(state: ChargingState, batteryPercent: Int) {
        snoozedNotifyTask = SnoozedNotifyTask(this, state, batteryPercent)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy called.")
        timer?.cancel()
        instance = null
        super.onDestroy()
    }

    companion object {
        const val TAG = "SnoozeService"

        fun stop(context: Context) {
            Log.d(TAG, "stop called.")
            val intent = Intent(context, SnoozeService::class.java)
            context.stopService(intent)
            instance = null
        }
    }
}