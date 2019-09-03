package com.mblau.batterynotifier.model

import com.mblau.batterynotifier.R
import com.mblau.batterynotifier.dao.SharedPreferencesRepository

enum class EventType(

    val chargingState: ChargingState,
    val notificationColorId: Int,
    val smallTextId: Int,
    val bigTextId: Int,
    val hasValuePassedThreshold: (Int, Int) -> Boolean,
    val threshold: () -> Int
) {

    HIGH_BATTERY(
        ChargingState.CHARGING,
        R.color.colorHigh,
        R.string.notify_high_collapsed,
        R.string.notify_high_expanded,
        fun(value: Int, threshold: Int): Boolean {
            return (value >= threshold)
        },
        fun (): Int {
            return SharedPreferencesRepository.getHighBatteryThreshold()
        }
    ),
    LOW_BATTERY(
        ChargingState.NOT_CHARGING,
        R.color.colorLow,
        R.string.notify_low_collapsed,
        R.string.notify_low_expanded,
        fun(value: Int, threshold: Int): Boolean {
            return (value <= threshold)
        },
        fun (): Int {
            return SharedPreferencesRepository.getLowBatteryThreshold()
        }
    );


//    override fun toString(): String {
//        return super.toString()
//    }
}