package com.mblau.batterynotifier

import android.app.*
import android.content.Intent
import android.content.Context
import android.os.IBinder


private const val NOTIFICATION_CHANNEL_NAME = "BATTERY_NOTIFIER_STICKY"
private const val NOTIFICATION_CHANNEL_ID = "131216"

private var instance: BatteryCheckupService? = null

class BatteryCheckupService : Service() {

    val myNotificationManager = MyNotificationManager()

    override fun onBind(p0: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val smallText = getString(R.string.sticky_text_collapsed)
        val bigText = getString(R.string.sticky_text_expanded)
        val notification = myNotificationManager.getStickyNotification(this, R.color.colorStickyNotification, smallText, bigText)
            startForeground(1, notification)
        instance = this
        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, BatteryCheckupService::class.java)
            context.startService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, BatteryCheckupService::class.java)
            context.stopService(intent)
            instance = null
        }

        fun isRunning(): Boolean {
            return (instance != null)
        }
    }

}
