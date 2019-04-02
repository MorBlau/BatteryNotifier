package com.mblau.batterynotifier.model

import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.mblau.batterynotifier.Constants
import com.mblau.batterynotifier.R
import com.mblau.batterynotifier.SharedPreferencesRepository
import com.mblau.batterynotifier.listener.UserSelectionListener

data class MySpinner(private val context: AppCompatActivity, val type: SpinnerType, private val spinnerId: Int, val minValue: Int, val maxValue: Int) {

    private var adapter: ArrayAdapter<String>
    private var spinner: Spinner = context.findViewById(spinnerId)
    private val spinnerSelectionListener: UserSelectionListener
    private val spinnerValues: MutableList<Int> = mutableListOf(0)
    val spinnerLabels: MutableList<String> = mutableListOf()
    val spinnerMap: HashMap<String, Int> = HashMap()

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
            val label = if (value > Constants.VALUE_OFF) ("$value%") else context.resources.getString(R.string.label_off)
            spinnerLabels.add(label)
            spinnerMap[label] = value
        }
    }

    private fun setSelection() {
        val savedValue = when (type) {
            SpinnerType.HIGH_BATTERY -> SharedPreferencesRepository.getHighBatteryThreshold()
                SpinnerType.LOW_BATTERY -> SharedPreferencesRepository.getLowBatteryThreshold()
        }
        val selection = if (spinnerValues.contains(savedValue)) spinnerValues.indexOf(savedValue) else 0
        spinner.setSelection(selection)
    }
}