package com.mblau.batterynotifier.listener

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.mblau.batterynotifier.util.Constants
import com.mblau.batterynotifier.dao.SharedPreferencesRepository
import com.mblau.batterynotifier.model.MySpinner
import com.mblau.batterynotifier.model.EventType

class UserSelectionListener(private val context: Context, private val spinner: MySpinner) : AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.d(TAG, "onNothingSelected called")
    }

    override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
        Log.d(TAG, "onItemSelected called")
        fun getBatteryValue(position: Int, spinnerMap: HashMap<String, Int>, spinnerLabels: MutableList<String>): Int {
            val label = spinnerLabels[position]
            if (spinnerMap[label] == null) return Constants.VALUE_OFF
            return spinnerMap[label]!!
        }

        if ((parentView == null) or spinner.spinnerLabels.isEmpty()) return
        val value = getBatteryValue(position, spinner.spinnerMap, spinner.spinnerLabels)
        when (spinner.type) {
            EventType.LOW_BATTERY -> {
                SharedPreferencesRepository.updateLowBatteryThreshold(value)
            }
            EventType.HIGH_BATTERY -> {
                SharedPreferencesRepository.updateHighBatteryThreshold(value)
            }
        }
    }

    companion object {
        val TAG = "UserSelectionListener"
    }

}