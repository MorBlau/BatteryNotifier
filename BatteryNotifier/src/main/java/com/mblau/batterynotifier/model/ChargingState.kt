package com.mblau.batterynotifier.model

import com.mblau.batterynotifier.util.Constants

enum class ChargingState(val isCharging: Boolean, val period: Long) {

    CHARGING(true, Constants.PERIOD_CHARGING),
    NOT_CHARGING(false, Constants.PERIOD_NOT_CHARGING);

    fun getOther(): ChargingState {
        return when (this) {
            CHARGING -> NOT_CHARGING
            NOT_CHARGING -> CHARGING
        }
    }
}