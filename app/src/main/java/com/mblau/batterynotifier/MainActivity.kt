package com.mblau.batterynotifier

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.view.ActionMode
import android.util.Log
import com.mblau.batterynotifier.listener.FabClickListener
import com.mblau.batterynotifier.listener.SharedPreferencesChangeListener
import com.mblau.batterynotifier.model.MySpinner
import com.mblau.batterynotifier.model.SpinnerType

private const val TAG = "Main"

class MainActivity : AppCompatActivity() {

    private val sharedPreferencesRepository = SharedPreferencesRepository
    private val sharedPreferencesChangeListener = SharedPreferencesChangeListener(this)
    private lateinit var fabClickedListener: FabClickListener
    private lateinit var lowBatterySpinner: MySpinner
    private lateinit var highBatterySpinner: MySpinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesRepository.initialize(this)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setFab()
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

    private fun setFab() {
        val fabColorId = if (SharedPreferencesRepository.isServiceActive() and SharedPreferencesRepository.isAnyServiceEnabled())
            R.color.colorOn else R.color.colorOff
        getColor(R.color.colorOff)
        setFabColor(fabColorId)
        fabClickedListener = FabClickListener()
        fab.setOnClickListener(fabClickedListener)
    }

    fun setFabColor(colorId: Int) {
        fab.supportBackgroundTintList = ContextCompat.getColorStateList(this, colorId)
    }

    private fun openAboutScreen() {
        Log.d(TAG, "Opening about screen")
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun populateSpinners() {
        Log.d(TAG, "Populating spinners")

        lowBatterySpinner = MySpinner(this, SpinnerType.LOW_BATTERY, R.id.lowBatterySpinner, Constants.MIN_LOW_BATTERY, Constants.MAX_LOW_BATTERY)
        highBatterySpinner = MySpinner(this, SpinnerType.HIGH_BATTERY, R.id.highBatterySpinner, Constants.MIN_HIGH_BATTERY, Constants.MAX_HIGH_BATTERY)
    }

}
