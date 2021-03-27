package com.example.weatherapp.domain

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

object Utils {

    fun convertKelvinToCel(degree: Double): Int {
        val cel = degree - 273.15
        return ceil(cel).toInt()
    }

    fun getTodayDateString(timezone: Int, cityName: String, countryName: String): String {
        val cityTimeZone = TimeZone.getAvailableIDs().find { it.contains(cityName.replace(" ", "_"), true) }
        if (cityTimeZone != null && cityTimeZone.isNotEmpty()) {
            val timeZone = TimeZone.getTimeZone(cityTimeZone)
            val c = Calendar.getInstance(timeZone)
            val format = SimpleDateFormat("EEE, dd MMM yyyy")
            format.timeZone = timeZone
            return format.format(c.time)
        }
        return ""
    }

    fun getTodayTimeString(timezone: Int, cityName: String, countryName: String): String {
        val cityTimeZone = TimeZone.getAvailableIDs().find { it.contains(cityName.replace(" ", "_"), true) }
        if (cityTimeZone != null && cityTimeZone.isNotEmpty()) {
            val timeZone = TimeZone.getTimeZone(cityTimeZone)
            val c = Calendar.getInstance(timeZone)
            val format = SimpleDateFormat("hh:mm a")
            format.timeZone = timeZone
            return format.format(c.time)
        }
        return ""
    }

    fun getTimeStringByMilliSec(time: Long, timezone: Int,cityName:String): String {
        val cityTimeZone = TimeZone.getAvailableIDs().find { it.contains(cityName.replace(" ", "_"), true) }
        if (cityTimeZone != null && cityTimeZone.isNotEmpty()) {
            val timeZone = TimeZone.getTimeZone(cityTimeZone)
            val c = Calendar.getInstance(timeZone)
            c.timeInMillis = time
            val format = SimpleDateFormat("hh:mm a")
            format.timeZone = timeZone
            return format.format(c.time)
        }
        return ""
    }

    fun setStatusBarColor(view: View, activity: Activity, color: Int?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
            activity.window.statusBarColor = color!!
        }
    }

    fun setLightStatusBar(view: View, activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
            activity.window.statusBarColor = Color.WHITE
        }
    }

}