package com.example.weatherapp.domain

import android.content.Context
import android.location.Geocoder
import io.nlopez.smartlocation.SmartLocation
import java.util.*
import javax.inject.Inject

class MyLocationUseCase @Inject constructor(
        private val context: Context
) {
    fun invoke(callback: LocationCallback) {
        if (SmartLocation.with(context).location().state().locationServicesEnabled()) {
            SmartLocation.with(context).location()
                    .oneFix()
                    .start {
                        try {
                            val geocode = Geocoder(context, Locale.getDefault())
                            val addressList = geocode.getFromLocation(it.latitude, it.longitude, 1)
                            val fullAddress = addressList?.get(0)?.adminArea?.split(" ")?.first() ?: ""
                            callback.onLocationDetected(fullAddress)
                        } catch (ex: Exception) {
                            callback.onLocationNoDetected()
                        }

                    }
        } else
            callback.locationServiceNotEnabled()
    }
}

interface LocationCallback {
    fun onLocationDetected(cityName: String)
    fun locationServiceNotEnabled()
    fun onLocationNoDetected()
}