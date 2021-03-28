package com.example.weatherapp.presentation.home

import androidx.test.espresso.IdlingRegistry
import com.example.weatherapp.BaseUITest
import com.example.weatherapp.R
import com.example.weatherapp.core.di.idlingResource
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import org.junit.Assert.*
import org.junit.Test

class WeatherHomeFragmentTest : BaseUITest(){

    @Test
    fun weatherTitleDisplayed(){
        assertDisplayed("Weather App")
    }

    @Test
    fun showLoadingWhileFetchingData(){
        assertDisplayed(R.id.progress)
    }

    @Test
    fun hideLoadingAfterFetchingData(){
        Thread.sleep(4000)
        assertNotDisplayed(R.id.progress)
    }

    @Test
    fun weatherInfoDisplayed(){
        IdlingRegistry.getInstance().unregister(idlingResource)
        Thread.sleep(4000)
        assertContains("Wind")
        assertContains("Humidity")
        assertContains("Pressure")
        assertContains("Cloud Cover")
        assertContains("Geo coords")

        assertDisplayed(R.id.temp)
        assertDisplayed(R.id.temp_max_text)
        assertDisplayed(R.id.temp_min_text)
    }

    @Test
    fun locateMeButtonDisplayed(){
        assertDisplayed("Locate me")
    }
}