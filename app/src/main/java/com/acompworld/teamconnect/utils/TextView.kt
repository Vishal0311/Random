package com.acompworld.teamconnect.utils

import android.annotation.SuppressLint
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ConstantLocale")
val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

@SuppressLint("ConstantLocale")
val appDateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

var TextView.timeStamp: String
    set(value) {
        val date = isoDateFormat.parse(value)
        text = appDateFormat.format(date)
    }
    get() {
        val date = appDateFormat.parse(text.toString())
        return isoDateFormat.format(date)
    }



@SuppressLint("ConstantLocale")
val appDateFormat2 = SimpleDateFormat("dd-mm-yyy", Locale.getDefault())
var TextView.timeStamp2: String
    set(value) {
        val date2 = isoDateFormat.parse(value)
        text = appDateFormat2.format(date2)
    }
    get() {
        val date2 = appDateFormat2.parse(text.toString())
        return isoDateFormat.format(date2)
    }