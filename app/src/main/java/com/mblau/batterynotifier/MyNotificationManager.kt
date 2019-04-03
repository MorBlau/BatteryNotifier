package com.mblau.batterynotifier

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat

const val CHANNEL_ID = "mblau.batterynotifier"
const val TITLE = "Battery Notifier"

private const val ALERT_NOTIFICATION_ID = 23487
private const val ALERT_NOTIFICATION_STRING = "23487"
private const val ALERT_NOTIFICATION_NAME = "Alert Notifications"
private const val STICKY_NOTIFICATION_ID = 30987
private const val STICKY_NOTIFICATION_STRING = "30987"
private const val STICKY_NOTIFICATION_NAME = "Service Notification"

class MyNotificationManager {

    fun notify(context: Context, colorId: Int, smallText: String, bigText: String = smallText) {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val channelDescription = context.getString(R.string.channel_description_alert)
        val color = context.resources.getColor(colorId, context.theme)
        val builder = NotificationCompat.Builder(context, ALERT_NOTIFICATION_STRING)
            .setSmallIcon(R.drawable.ic_skylight_notification)
            .setContentTitle(TITLE)
            .setColor(color)
            .setColorized(true)
            .setContentText(smallText)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (bigText != smallText) builder.setStyle(NotificationCompat.BigTextStyle().bigText(bigText))

        createNotificationChannel(context, ALERT_NOTIFICATION_STRING, ALERT_NOTIFICATION_NAME,
            channelDescription, NotificationManager.IMPORTANCE_DEFAULT, true)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(ALERT_NOTIFICATION_ID, builder.build())
        }

    }

    fun getStickyNotification(context: Context, colorId: Int, smallText: String, bigText: String = smallText): Notification {

        // clicking on notification itself will do nothing
        val emptyPendingIntent = PendingIntent.getActivity(context, 0, Intent(), 0)

        // clicking on button will open app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val buttonPendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val buttonText = context.getString(R.string.sticky_button)
        val action: NotificationCompat.Action = NotificationCompat.Action(R.drawable.navigation_empty_icon, buttonText, buttonPendingIntent)
        val channelDescription = context.getString(R.string.channel_description_sticky)
        val color = context.resources.getColor(colorId, context.theme)
        val builder = NotificationCompat.Builder(context, STICKY_NOTIFICATION_STRING)
            .setSmallIcon(R.drawable.ic_skylight_notification)
            .setContentTitle(TITLE)
            .setColor(color)
            .setColorized(true)
            .setContentText(smallText)
            .setContentIntent(emptyPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .addAction(action)

        if (bigText != smallText) builder.setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
        createNotificationChannel(context, STICKY_NOTIFICATION_STRING, STICKY_NOTIFICATION_NAME,
            channelDescription, NotificationManager.IMPORTANCE_MIN, false)

        return builder.build()

    }

    private fun createNotificationChannel(context: Context, channelId: String, channelName: String, channelDescription: String, importance: Int, withSound: Boolean) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
                if (!withSound) setSound(null, null)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}