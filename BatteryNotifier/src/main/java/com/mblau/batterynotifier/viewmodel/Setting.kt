package com.mblau.batterynotifier.viewmodel

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.mblau.batterynotifier.util.Constants
import com.mblau.batterynotifier.R
import com.mblau.batterynotifier.dao.SharedPreferencesRepository
import com.mblau.batterynotifier.listener.UserSelectionListener
import com.mblau.batterynotifier.model.ChargingState
import com.mblau.batterynotifier.model.EventType
import com.mblau.batterynotifier.util.testToast

class Setting(val eventType: EventType): AndroidViewModel() {

    private var adapter: ArrayAdapter<String>
    private var spinner: Spinner = context.findViewById(spinnerId)
    private val spinnerSelectionListener: UserSelectionListener
    private val spinnerValues: MutableList<Int> = mutableListOf(0)
    val spinnerLabels: MutableList<String> = mutableListOf()
    val spinnerMap: HashMap<String, Int> = HashMap()
    var currentSelection: Int = eventType.threshold()

    init {
        setData(minValue, maxValue)
        adapter = ArrayAdapter(context,android.R.layout.simple_spinner_item,spinnerLabels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        setSelection()
        spinnerSelectionListener = UserSelectionListener(context, this)
        spinner.onItemSelectedListener = spinnerSelectionListener
    }

    private fun setData(minValue: Int, maxValue: Int) {
        // Populate spinner values
        for (value in minValue..maxValue step Constants.STEP) spinnerValues.add(value)

        // Populate spinner labels and map
        for (value in spinnerValues) {
            val label =
                if (value > Constants.VALUE_OFF)
                    "$value%"
                else
                    context.resources.getString(R.string.label_off)
            spinnerLabels.add(label)
            spinnerMap[label] = value
        }
    }

    private fun setSelection() {
        val savedValue = when (type) {
            EventType.HIGH_BATTERY -> SharedPreferencesRepository.getHighBatteryThreshold()
                EventType.LOW_BATTERY -> SharedPreferencesRepository.getLowBatteryThreshold()
        }
        val selection =
            if (spinnerValues.contains(savedValue))
                spinnerValues.indexOf(savedValue)
            else 0
        spinner.setSelection(selection)
    }

    private fun transform() {

    }

    public fun onValueChange() {
        testToast("onValueChange")
    }