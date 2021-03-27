package com.example.weatherapp.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.domain.GetWeatherInfoUseCase
import com.example.weatherapp.domain.MyLocationUseCase
import java.lang.IllegalArgumentException
import javax.inject.Inject

class WeatherHomeViewModelFactory @Inject constructor(
    private val useCase: GetWeatherInfoUseCase,
    private val locationUseCase: MyLocationUseCase,
    private val context:Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WeatherHomeViewModel::class.java))
            return WeatherHomeViewModel(useCase,locationUseCase,context) as T
        throw IllegalArgumentException("Unknown view model found")
    }
}