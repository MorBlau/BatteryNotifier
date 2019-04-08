package com.mblau.batterynotifier.util

import android.text.format.DateUtils

object Constants {

    const val MIN_LOW_BATTERY = 10
    const val MAX_LOW_BATTERY = 50
    const val MIN_HIGH_BATTERY = 55
    const val MAX_HIGH_BATTERY = 90
    const val STEP = 5

    const val PERIOD_CHARGING = DateUtils.MINUTE_IN_MILLIS * 15
    const val PERIOD_NOT_CHARGING = DateUtils.SECOND_IN_MILLIS * 30

    const val VALUE_OFF = 0
}