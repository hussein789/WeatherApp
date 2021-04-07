package com.example.weatherapp.domain

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import com.example.weatherapp.WeatherApp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

object Utils {

    fun convertKelvinToCel(degree: Double): Int {
        val cel = degree - 273.15
        return ceil(cel).toInt()
    }

    fun getTodayDateString(timeZoneId:String?): String {
        return try {
            timeZoneId?.let {
                val timeZone = TimeZone.getTimeZone(timeZoneId)
                val c = Calendar.getInstance(timeZone)
                val format = SimpleDateFormat("EEE, dd MMM yyyy")
                format.timeZone = timeZone
                format.format(c.time)
            } ?: ""
        } catch (ex: Exception) {
            ""
        }

    }

    fun getTodayTimeString(timeZoneId: String?): String {
        return try {
            timeZoneId?.let {
                val timeZone = TimeZone.getTimeZone(timeZoneId)
                val c = Calendar.getInstance(timeZone)
                val format = SimpleDateFormat("hh:mm a")
                format.timeZone = timeZone
                format.format(c.time)
            } ?: ""
        } catch (ex: Exception) {
            ""
        }

    }

    fun getTimeStringByMilliSec(time: Long,timeZoneId:String?): String {
        return timeZoneId?.let {
            val timeZone = TimeZone.getTimeZone(timeZoneId)
            val unix_seconds: Long = time
            val date = Date(unix_seconds * 1000L)
            val df = SimpleDateFormat("hh:mm a")
            df.timeZone = timeZone
            return df.format(date)
        } ?: ""

    }

}