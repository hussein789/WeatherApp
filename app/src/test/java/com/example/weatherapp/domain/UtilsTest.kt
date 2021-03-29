package com.example.weatherapp.domain

import org.junit.Assert.*
import org.junit.Test

class UtilsTest{


    @Test
    fun convertKelvinToCel(){
        val result = Utils.convertKelvinToCel(281.63)
        assertEquals(result,9)
    }

    @Test
    fun getTodayDateString_invalidCityName_returnEmptyString(){
        val result = Utils.getTodayDateString(null)

        assertEquals(result,"")
    }

    @Test
    fun getTodayDateString_validCityName_returnEmptyString(){
        val result = Utils.getTodayDateString("London")

        assertNotEquals(result,"")
    }

    @Test
    fun getTodayTimeString_validCityName_returnEmptyString(){
        val result = Utils.getTodayTimeString("California")

        assertNotEquals(result,"")
    }

    @Test
    fun getTodayTimeString_inValidCityName_returnEmptyString(){
        val result = Utils.getTodayDateString(null)

        assertEquals(result,"")
    }
}