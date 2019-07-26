package com.mblau.batterynotifier.util

import android.content.Context
import android.widget.Toast

fun testToast(context: Context, text: String) {
    val testText = "Test: $text"
    Toast.makeText(context, testText, Toast.LENGTH_LONG).show()
}