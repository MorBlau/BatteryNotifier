package com.mblau.batterynotifier.task

import com.mblau.batterynotifier.dao.SharedPreferencesRepository
import com.mblau.batterynotifier.model.ChargingState
import com.mblau.batterynotifier.model.EventType

class CheckBatteryTaskConfig(val chargingState: ChargingState) {

    var didNotify: Boolean = false

    val eventType = when (chargingState) {
        ChargingState.NOT_CHARGING -> EventType.LOW_BATTERY
        ChargingState.CHARGING -> EventType.HIGH_BATTERY
    }

    fun getBatteryThresholdFromRepository(): Int {
        return when (eventType) {
            EventType.LOW_BATTERY -> SharedPreferencesRepository.getLowBatteryThreshold()
            EventType.HIGH_BATTERY -> SharedPreferencesRepository.getHighBatteryThreshold()
        }
    }

    fun getColorId(): Int {
        return eventType.notificationColorId
    }

    fun getSmallTextId(): Int {
        return eventType.smallTextId
    }

    fun getBigTextId(): Int {
        return eventType.bigTextId
    }

    fun verifyChargingState(chargingState: ChargingState): Boolean {
        return this.chargingState.equals(chargingState)
    }

    fun hasValuePassedThreshold(value: Int, threshold: Int): Boolean {
        return eventType.hasValuePassedThreshold(value, threshold)
    }
}