package com.mblau.batterynotifier.model

import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.mblau.batterynotifier.listener.UserSelectionListener

data class MySpinner(val context: AppCompatActivity, val type: SpinnerType, val spinnerMap: HashMap<String, Int>, val spinnerLabels: MutableList<String>,
                     val spinnerId: Int) {

    var adapter: ArrayAdapter<String>
    var spinner: Spinner = context.findViewById(spinnerId)
    val spinnerSelectionListener: UserSelectionListener

    init {
        adapter = ArrayAdapter(context,android.R.layout.simple_spinner_item,spinnerLabels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinnerSelectionListener = UserSelectionListener(context, this)
        spinner.onItemSelectedListener = spinnerSelectionListener
    }

}