package com.mblau.batterynotifier

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.content_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar)
        setVersionName()
    }

    private fun setVersionName() {
        val versionName = applicationContext.packageManager.getPackageInfo(packageName, 0).versionName
        val aboutVersionText = this.getString(R.string.about_version) + " " + versionName
        aboutVersion.text = aboutVersionText
    }

}
