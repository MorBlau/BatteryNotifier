package com.mblau.batterynotifier.manager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.text.format.DateUtils
import android.util.Log
import com.mblau.batterynotifier.MainActivity
import com.mblau.batterynotifier.R
import com.mblau.batterynotifier.dao.SharedPreferencesRepository
import com.mblau.batterynotifier.model.ChargingState
import com.mblau.batterynotifier.service.SnoozeService
import com.mblau.batterynotifier.task.CheckBatteryTaskConfig

const val CHANNEL_ID = "mblau.batterynotifier"
const val TITLE = "Battery Notifier"

private const val ALERT_NOTIFICATION_ID = 23487
private const val ALERT_NOTIFICATION_STRING = "23487"
private const val ALERT_NOTIFICATION_NAME = "Alert Notifications"
private const val STICKY_NOTIFICATION_ID = 30987
private const val STICKY_NOTIFICATION_STRING = "30987"
private const val STICKY_NOTIFICATION_NAME = "Service Notification"

const val EXTRA_SNOOZE_DELAY = "com.mblau.batterynotifier.intent.EXTRA_SNOOZE_DELAY"
const val EXTRA_SNOOZE_STATE = "com.mblau.batterynotifier.intent.EXTRA_SNOOZE_STATE"
const val EXTRA_SNOOZE_BATTERY = "com.mblau.batterynotifier.intent.EXTRA_SNOOZE_BATTERY"

object MyNotificationManager {

    const val TAG = "MyNotificationManager"

    fun notify(context: Context, data: CheckBatteryTaskConfig, batteryPercent: Int) {
        Log.d(TAG, "notify called.")

        Log.d(TAG, "Battery event will be notified.")
        val colorId = data.getColorId()
        val smallText = context.getString(data.getSmallTextId(), batteryPercent)
        val bigText = context.getString(data.getBigTextId(), batteryPercent)

        val channelDescription = context.getString(R.string.channel_description_alert)
        val color = context.resources.getColor(colorId, context.theme)
        val builder = NotificationCompat.Builder(context, ALERT_NOTIFICATION_STRING)
            .setSmallIcon(R.drawable.ic_skylight_notification)
            .setContentTitle(TITLE)
            .setColor(color)
            .setColorized(true)
            .setContentText(smallText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        addButtons(context, builder, data, batteryPercent)

        if (bigText != smallText) builder.setStyle(NotificationCompat.BigTextStyle().bigText(bigText))

        createNotificationChannel(context,
            ALERT_NOTIFICATION_STRING,
            ALERT_NOTIFICATION_NAME,
            channelDescription, NotificationManager.IMPORTANCE_DEFAULT, true)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(ALERT_NOTIFICATION_ID, builder.build())
        }
        data.didNotify = true
    }

    fun removeNotification(context: Context) {
        NotificationManagerCompat.from(context).cancel(ALERT_NOTIFICATION_ID)
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
            .setSmallIcon(R.drawable.ic_battery_service_icon)
            .setContentTitle(TITLE)
            .setColor(color)
            .setColorized(true)
            .setContentText(smallText)
            .setContentIntent(emptyPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setAutoCancel(true)
            .addAction(action)

        if (bigText != smallText) builder.setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
        createNotificationChannel(context,
            STICKY_NOTIFICATION_STRING,
            STICKY_NOTIFICATION_NAME,
            channelDescription, NotificationManager.IMPORTANCE_MIN, false)

        return builder.build()

    }

    private fun createNotificationChannel(context: Context, channelId: String, channelName: String, channelDescription: String, importance: Int, withSound: Boolean) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
                if (withSound) {
                    val attributes = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                    setSound(getSoundUri(context), attributes)
                    enableVibration(true)
                    enableLights(true)
                }
                else
                    setSound(null, null)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun addButtons(context: Context, builder: NotificationCompat.Builder, data: CheckBatteryTaskConfig, batteryPercent: Int) {
        for (snoozeTime in data.getSnoozeButtonValues()) {
            val snoozeAction = createSnoozePendingIntent(context, data.chargingState, batteryPercent, snoozeTime)
            builder.addAction(snoozeAction)
        }
    }

    private fun createSnoozePendingIntent(context: Context, chargingState: ChargingState, batteryPercent: Int, snoozeTime: Long): NotificationCompat.Action {
        val snoozeActionIntent = Intent(context, SnoozeService::class.java)
            .putExtra(EXTRA_SNOOZE_DELAY, (snoozeTime * DateUtils.MINUTE_IN_MILLIS))
            .putExtra(EXTRA_SNOOZE_STATE, chargingState)
            .putExtra(EXTRA_SNOOZE_BATTERY, batteryPercent)
        val snoozeActionPendingIntent: PendingIntent = PendingIntent.getService(
            context, 0, snoozeActionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val snoozeActionText = "Snooze $snoozeTime"
        return NotificationCompat.Action(R.drawable.ic_skylight_notification, snoozeActionText, snoozeActionPendingIntent)
    }

    private fun getSoundUri(context: Context): Uri {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.tweet)
    }
}