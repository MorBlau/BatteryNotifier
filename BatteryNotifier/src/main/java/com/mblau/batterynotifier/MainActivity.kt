package com.mblau.batterynotifier

import android.content.ComponentName
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.util.Log
import com.mblau.batterynotifier.dao.SharedPreferencesRepository
import com.mblau.batterynotifier.listener.FloatingActionButtonClickListener
import com.mblau.batterynotifier.listener.SharedPreferencesChangeListener
import com.mblau.batterynotifier.model.MySpinner
import com.mblau.batterynotifier.model.EventType
import com.mblau.batterynotifier.receiver.AppUpdateReceiver
import com.mblau.batterynotifier.receiver.RebootEventReceiver
import com.mblau.batterynotifier.service.CheckBatteryService
import com.mblau.batterynotifier.util.Constants

private const val TAG = "Main"

class MainActivity : AppCompatActivity() {

    private val sharedPreferencesRepository = SharedPreferencesRepository
    private val sharedPreferencesChangeListener = SharedPreferencesChangeListener(this)

    private lateinit var floatingActionButtonClickListener: FloatingActionButtonClickListener
    private lateinit var lowBatterySpinner: MySpinner
    private lateinit var highBatterySpinner: MySpinner
    private lateinit var rebootReceiver: ComponentName
    private lateinit var updateReceiver: ComponentName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesRepository.initialize(this)
        initRebootReceivers()
        syncServiceState()
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setFloatingActionButton()
        populateSpinners()
        sharedPreferencesRepository.registerChangeListener(sharedPreferencesChangeListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_about -> {
                openAboutScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun syncServiceState() {
        // if service is not running, but in preferences is saved as running, we will turn it off
        if (!CheckBatteryService.isRunning() and SharedPreferencesRepository.isServiceActive())
            SharedPreferencesRepository.updateServiceActive(false)
    }

    private fun setFloatingActionButton() {
        val floatingActionButtonColorId =
            if (SharedPreferencesRepository.isServiceActive() and SharedPreferencesRepository.isAnyServiceEnabled())
                R.color.colorOn
            else
                R.color.colorOff
        setFloatingActionButtonColor(floatingActionButtonColorId)
        floatingActionButtonClickListener = FloatingActionButtonClickListener()
        floatingActionButton.setOnClickListener(floatingActionButtonClickListener)
    }

    fun setFloatingActionButtonColor(colorId: Int) {
        floatingActionButton.supportBackgroundTintList = ContextCompat.getColorStateList(this, colorId)
    }

    private fun openAboutScreen() {
        Log.d(TAG, "Opening about screen")
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun populateSpinners() {
        Log.d(TAG, "Populating spinners")

        lowBatterySpinner = MySpinner(this, EventType.LOW_BATTERY, R.id.lowBatterySpinner, Constants.MIN_LOW_BATTERY, Constants.MAX_LOW_BATTERY)
        highBatterySpinner = MySpinner(this, EventType.HIGH_BATTERY, R.id.highBatterySpinner, Constants.MIN_HIGH_BATTERY, Constants.MAX_HIGH_BATTERY)
    }

    private fun initRebootReceivers() {
        rebootReceiver = ComponentName(this, RebootEventReceiver::class.java)
        updateReceiver = ComponentName(this, AppUpdateReceiver::class.java)

        packageManager.setComponentEnabledSetting(
            rebootReceiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}
