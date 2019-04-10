package com.mblau.batterynotifier.service

import android.app.*
import android.content.Intent
import android.content.Context
import android.content.Intent.ACTION_POWER_CONNECTED
import android.content.Intent.ACTION_POWER_DISCONNECTED
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.IBinder
import android.text.format.DateUtils
import android.util.Log
import com.mblau.batterynotifier.manager.MyNotificationManager
import com.mblau.batterynotifier.R
import com.mblau.batterynotifier.task.CheckBatteryTaskConfig
import com.mblau.batterynotifier.model.ChargingState
import com.mblau.batterynotifier.receiver.BatteryChargeStateReceiver
import com.mblau.batterynotifier.receiver.NotificationButtonReceiver
import com.mblau.batterynotifier.task.CheckBatteryTask
import java.util.*

private var instance: CheckBatteryService? = null

class CheckBatteryService : Service() {

    private val myNotificationManager = MyNotificationManager
    private var batteryChargeStateReceiver: BatteryChargeStateReceiver? = null
    var notificationButtonReceiver = NotificationButtonReceiver()
    private var checkBatteryTask: CheckBatteryTask? = null
    private var timer: Timer? = null

    val lowCheckBatteryTaskData = CheckBatteryTaskConfig(ChargingState.NOT_CHARGING)
    val highCheckBatteryTaskData = CheckBatteryTaskConfig(ChargingState.CHARGING)
    val checkBatteryTaskDataMap = mutableMapOf(
        ChargingState.CHARGING to highCheckBatteryTaskData,
        ChargingState.NOT_CHARGING to lowCheckBatteryTaskData)

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand called.")
        super.onStartCommand(intent, flags, startId)
        registerReceivers()
        val chargingState = checkChargingState()
        createNewBatteryTask(chargingState)
        val smallText = getString(R.string.sticky_text_collapsed)
        val bigText = getString(R.string.sticky_text_expanded)
        val notification = myNotificationManager.getStickyNotification(this,
            R.color.colorStickyNotification, smallText, bigText)
        Log.d(TAG, "Calling startForeground.")
            startForeground(1, notification)
        instance = this
        startTimer(chargingState)
        return Service.START_NOT_STICKY
    }

    private fun startTimer(state: ChargingState) {
        Log.d(TAG, "Starting task timer.")
        timer?.cancel()
        timer = Timer()
        val startTime = Date(Date().time + DateUtils.SECOND_IN_MILLIS) // one second in the future
        timer!!.scheduleAtFixedRate(checkBatteryTask, startTime, state.period)
    }

    fun handleChangedChargeState() {
        val chargingState = checkChargingState()
        restartTimer(chargingState)
    }

    fun restartTimer(state: ChargingState) {
        Log.d(TAG, "Restarting task timer.")
        myNotificationManager.removeNotification(this)
        createNewBatteryTask(state)
        startTimer(state)
    }

    private fun createNewBatteryTask(state: ChargingState) {
        val config = checkBatteryTaskDataMap[state]!!
        config.didNotify = false
        checkBatteryTask?.cancel()
        checkBatteryTask = CheckBatteryTask(this, config)
    }

    private fun registerReceivers() {
        Log.d(TAG, "registerReceivers called.")
        batteryChargeStateReceiver = BatteryChargeStateReceiver().initialize(this)
        val connectedIntentFilter = IntentFilter(ACTION_POWER_CONNECTED)
        this.registerReceiver(batteryChargeStateReceiver, connectedIntentFilter)

        val disconnectedIntentFilter = IntentFilter(ACTION_POWER_DISCONNECTED)
        this.registerReceiver(batteryChargeStateReceiver, disconnectedIntentFilter)
    }

    private fun unregisterReceivers() {
        Log.d(TAG, "unregisterReceivers called.")
        this.unregisterReceiver(batteryChargeStateReceiver)
        batteryChargeStateReceiver = null
    }

    fun checkChargingStateAndBatteryPercent(): Pair<ChargingState, Int> {
        // check charge status
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let {
                intentFilter -> registerReceiver(null, intentFilter)
        }

        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING) || (status == BatteryManager.BATTERY_STATUS_FULL)

        // check battery percent
        val batteryPercent: Int = batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            (level / scale.toFloat() * 100).toInt()
        } ?: -1

        return (if (isCharging) ChargingState.CHARGING else ChargingState.NOT_CHARGING) to batteryPercent
    }

    private fun checkChargingState(): ChargingState {
        return checkChargingStateAndBatteryPercent().component1()
    }
        override fun onDestroy() {
        Log.d(TAG, "onDestroy called.")
        timer?.cancel()
        unregisterReceivers()
        instance = null
        super.onDestroy()
    }

    companion object {

        private const val TAG = "CheckBatteryService"

        fun start(context: Context) {
            Log.d(TAG, "start called.")
            val intent = Intent(context, CheckBatteryService::class.java)
            context.startService(intent)
        }

        fun stop(context: Context) {
            Log.d(TAG, "stop called.")
            MyNotificationManager.removeNotification(context)
            val intent = Intent(context, CheckBatteryService::class.java)
            context.stopService(intent)
            instance = null
        }

        fun restart(context: Context) {
            Log.d(TAG, "restart called.")
            Log.d(TAG, "Stopping existing service.")
            val intent = Intent(context, CheckBatteryService::class.java)
            context.stopService(intent)
            Log.d(TAG, "Starting new service.")
            context.startService(intent)
        }

        fun isRunning(): Boolean {
            return (instance != null)
        }
    }

}
