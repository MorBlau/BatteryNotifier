package com.mblau.batterynotifier

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import com.mblau.batterynotifier.Constants.MAX_HIGH_BATTERY
import com.mblau.batterynotifier.Constants.MAX_LOW_BATTERY
import com.mblau.batterynotifier.Constants.MIN_HIGH_BATTERY
import com.mblau.batterynotifier.Constants.MIN_LOW_BATTERY
import com.mblau.batterynotifier.Constants.VALUE_OFF
import com.mblau.batterynotifier.Constants.STEP
import com.mblau.batterynotifier.listener.FabClickListener
import com.mblau.batterynotifier.listener.SharedPreferencesChangeListener
import com.mblau.batterynotifier.logging.LoggerFactory.getLogger
import com.mblau.batterynotifier.model.MySpinner
import com.mblau.batterynotifier.model.SpinnerType

class MainActivity : AppCompatActivity() {

    private val logger = getLogger(this.javaClass)

    private val sharedPreferencesRepository = SharedPreferencesRepository
    private val sharedPreferencesChangeListener = SharedPreferencesChangeListener()
    private lateinit var fabClickedListener: FabClickListener
    private lateinit var lowBatterySpinner: MySpinner
    private lateinit var highBatterySpinner: MySpinner

    private val highBatterySpinnerMap: HashMap<String, Int> = HashMap()
    private val lowBatterySpinnerMap: HashMap<String, Int> = HashMap()
    private val lowBatterySpinnerLabels = mutableListOf<String>()
    private val highBatterySpinnerLabels = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        sharedPreferencesRepository.initialize(this)
        fabClickedListener = FabClickListener(this)
        fab.setOnClickListener(fabClickedListener)
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

    private fun openAboutScreen() {
        logger.config("Opening about screen")
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun populateSpinners() {
        logger.config("Populating spinners")
        fun setSpinnerData(spinnerMap: HashMap<String, Int>, spinnerLabels: MutableList<String>, spinnerValues: MutableList<Int>) {
            for (value in spinnerValues) {
                val label = if (value > VALUE_OFF ) ("$value%") else resources.getString(R.string.label_off)
                spinnerLabels.add(label)
                spinnerMap[label] = value
            }
        }

        val lowSpinnerValues = mutableListOf(0)
        for (value in MIN_LOW_BATTERY..MAX_LOW_BATTERY step STEP) lowSpinnerValues.add(value)
        setSpinnerData(lowBatterySpinnerMap, lowBatterySpinnerLabels, lowSpinnerValues)

        val highSpinnerValues = mutableListOf(0)
        for (value in MIN_HIGH_BATTERY..MAX_HIGH_BATTERY step STEP) highSpinnerValues.add(value)
        setSpinnerData(highBatterySpinnerMap, highBatterySpinnerLabels, highSpinnerValues)

        lowBatterySpinner = MySpinner(this, SpinnerType.LOW_BATTERY, lowBatterySpinnerMap, lowBatterySpinnerLabels, R.id.lowBatterySpinner)
        highBatterySpinner = MySpinner(this, SpinnerType.HIGH_BATTERY, highBatterySpinnerMap, highBatterySpinnerLabels, R.id.highBatterySpinner)
    }

}
