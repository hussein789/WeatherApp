package com.example.weatherapp.domain

import android.content.Context
import io.nlopez.smartlocation.SmartLocation
import javax.inject.Inject

class IsLocationServiceEnabledUseCase @Inject constructor(private val context:Context)  {
    fun execute() = SmartLocation.with(context).location().state().locationServicesEnabled()
}