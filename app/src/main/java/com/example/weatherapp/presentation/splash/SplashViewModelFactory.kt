package com.example.weatherapp.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.presentation.utils.TimeZoneMapUtils
import java.lang.IllegalArgumentException
import javax.inject.Inject

class SplashViewModelFactory @Inject constructor(
    private val timeZoneMapUtils: TimeZoneMapUtils
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SplashViewModel::class.java))
            return SplashViewModel(timeZoneMapUtils) as T
        throw IllegalArgumentException("Unknown view model found")
    }
}