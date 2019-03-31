package com.mblau.batterynotifier.listener

import android.content.Context
import android.view.View
import com.mblau.batterynotifier.NotificationFactory
import com.mblau.batterynotifier.R
import com.mblau.batterynotifier.SharedPreferencesRepository
import com.mblau.batterynotifier.logging.LoggerFactory

class FabClickListener(private val context: Context) : View.OnClickListener {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val notificationFactory = NotificationFactory()

    override fun onClick(view: View?) {
        // action to perform when clicking on floating action button
        logger.config("Fab was clicked!")
        val color = context.resources.getColor(R.color.colorOff, context.theme)
        val smallText = "Hello world!!!"
        val bigText =
            "$smallText\n" +
                    "Active: ${if (SharedPreferencesRepository.isServiceEnabled()) "yes" else "no"}.\n" +
                    "High battery threshold: ${SharedPreferencesRepository.getHighBatteryThreshold()}.\n" +
                    "Low battery threshold: ${SharedPreferencesRepository.getLowBatteryThreshold()}."

        notificationFactory.notify(context, color, smallText, bigText)
    }

}