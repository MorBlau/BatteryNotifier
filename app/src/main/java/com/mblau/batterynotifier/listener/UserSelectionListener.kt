package com.mblau.batterynotifier.listener

import android.content.Context
import android.view.View
import android.widget.AdapterView
import com.mblau.batterynotifier.Constants
import com.mblau.batterynotifier.NotificationFactory
import com.mblau.batterynotifier.SharedPreferencesRepository
import com.mblau.batterynotifier.logging.LoggerFactory
import com.mblau.batterynotifier.model.MySpinner
import com.mblau.batterynotifier.model.SpinnerType

class UserSelectionListener(private val context: Context, private val spinner: MySpinner) : AdapterView.OnItemSelectedListener {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun onNothingSelected(p0: AdapterView<*>?) {
        logger.config("onNothingSelected called")
    }

    override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
        logger.config("onItemSelected called")
        fun getBatteryValue(position: Int, spinnerMap: HashMap<String, Int>, spinnerLabels: MutableList<String>): Int {
            val label = spinnerLabels[position]
            if (spinnerMap[label] == null) return Constants.VALUE_OFF
            return spinnerMap[label]!!
        }

        if ((parentView == null) or spinner.spinnerLabels.isEmpty()) return
        val value = getBatteryValue(position, spinner.spinnerMap, spinner.spinnerLabels)
        when (spinner.type) {
            SpinnerType.LOW_BATTERY -> {
                SharedPreferencesRepository.updateLowBatteryThreshold(value)
            }
            SpinnerType.HIGH_BATTERY -> {
                SharedPreferencesRepository.updateHighBatteryThreshold(value)
            }
        }
    }

}