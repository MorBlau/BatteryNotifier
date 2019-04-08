package com.mblau.batterynotifier.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mblau.batterynotifier.manager.MyNotificationManager


class NotificationButtonReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive called")
        MyNotificationManager.removeNotification(context)
    }

    companion object {
        const val TAG = "NotifButtonReceiver"
    }
}