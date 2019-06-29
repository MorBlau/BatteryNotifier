package com.mblau.batterynotifier.listener

import android.util.Log
import android.view.View
import com.mblau.batterynotifier.dao.SharedPreferencesRepository

class FloatingActionButtonClickListener : View.OnClickListener {

    private val TAG = "FABClickListener"

    override fun onClick(view: View?) {
        // action to perform when clicking on floating action button
        Log.d(TAG, "Floating action button was clicked!")
        val isEnabled = SharedPreferencesRepository.isServiceActive()
        handleChangeServiceState(isEnabled)
    }

    fun handleChangeServiceState(isEnabled: Boolean) {
        when {
            isEnabled -> SharedPreferencesRepository.updateServiceActive(false)
            SharedPreferencesRepository.isAnyServiceEnabled() -> SharedPreferencesRepository.updateServiceActive(true)
        }
    }

}