package com.example.weatherapp.domain

import android.content.Context
import io.nlopez.smartlocation.SmartLocation
import javax.inject.Inject

class IsAnyProviderAvailableUseCase @Inject constructor(private val context:Context) {
    fun execute() = SmartLocation.with(context).location().state().isAnyProviderAvailable
}