package com.mblau.batterynotifier

import android.app.*
import android.content.Intent
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.os.Build
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
        val notification = myNotificationManager.stickyNotify(this, R.color.colorStickyNotification, smallText, bigText)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
            val builder = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_skylight_notification)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.sticky_text_collapsed))
                .setStyle(Notification.BigTextStyle().bigText(getString(R.string.sticky_text_expanded)))
                .setAutoCancel(true)

            val notification = builder.build()

            startForeground(1, notification)
        } else {

            val builder = NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_skylight_notification)
                .setContentText(getString(R.string.sticky_text_collapsed))
                .setStyle(NotificationCompat.BigTextStyle().bigText(getString(R.string.sticky_text_expanded)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            val notification = builder.build()

            startForeground(1, notification)
        }
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
