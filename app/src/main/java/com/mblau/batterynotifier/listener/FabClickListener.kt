package com.mblau.batterynotifier.listener

import android.util.Log
import android.view.View
import com.mblau.batterynotifier.SharedPreferencesRepository

class FabClickListener : View.OnClickListener {

    private val TAG = "FabClickListener"

    override fun onClick(view: View?) {
        // action to perform when clicking on floating action button
        Log.d(TAG, "fab was clicked!")
        val isEnabled = SharedPreferencesRepository.isServiceActive()
        handleChangeServiceState(isEnabled)
    }

    fun handleChangeServiceState(isEnabled: Boolean) {
        when {
            isEnabled -> SharedPreferencesRepository.updateServiceEnabled(false)
            SharedPreferencesRepository.isAnyServiceEnabled() -> SharedPreferencesRepository.updateServiceEnabled(true)
        }
    }

}