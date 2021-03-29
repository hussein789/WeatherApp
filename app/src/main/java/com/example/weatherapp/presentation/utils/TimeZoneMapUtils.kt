package com.example.weatherapp.presentation.utils

import us.dustinj.timezonemap.TimeZone
import us.dustinj.timezonemap.TimeZoneMap

interface TimeZoneMapUtils{
    fun iniTimeZone()
    fun getTimeZoneForRegion(lat:Double,lng:Double):TimeZone?
    fun getTimeZoneId(lat:Double,lng:Double):String?
}

class TimeZoneMapImpl:TimeZoneMapUtils{
    companion object{
        lateinit var map:TimeZoneMap
    }
    override fun iniTimeZone() {
        map = TimeZoneMap.forEverywhere()
    }

    override fun getTimeZoneForRegion(lat: Double, lng: Double):TimeZone? {
        return map.getOverlappingTimeZone(lat,lng)
    }

    override fun getTimeZoneId(lat:Double,lng:Double): String? {
        return getTimeZoneForRegion(lat,lng)?.zoneId
    }
}