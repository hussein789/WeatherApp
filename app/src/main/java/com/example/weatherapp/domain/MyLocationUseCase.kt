package com.example.weatherapp.domain

import android.content.Context
import android.location.Location
import io.nlopez.smartlocation.SmartLocation
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MyLocationUseCase @Inject constructor(
        private val context: Context
) {
    suspend fun execute():Location{
        return suspendCoroutine { continuation ->
            SmartLocation.with(context).location()
                .oneFix()
                .start {
                    try {
                        continuation.resume(it)
                    } catch (e: Exception) {
                    }
                }
        }
    }
}
