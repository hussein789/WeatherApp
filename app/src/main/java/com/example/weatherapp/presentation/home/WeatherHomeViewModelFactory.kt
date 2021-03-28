package com.example.weatherapp.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.domain.GetWeatherInfoUseCase
import com.example.weatherapp.domain.IsAnyProviderAvailableUseCase
import com.example.weatherapp.domain.IsLocationServiceEnabledUseCase
import com.example.weatherapp.domain.MyLocationUseCase
import java.lang.IllegalArgumentException
import javax.inject.Inject

class WeatherHomeViewModelFactory @Inject constructor(
    private val useCase: GetWeatherInfoUseCase,
    private val locationUseCase: MyLocationUseCase,
    private val context:Context,
    private val isLocationServiceEnabledUseCase: IsLocationServiceEnabledUseCase,
    private val isAnyProviderAvailableUseCase: IsAnyProviderAvailableUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WeatherHomeViewModel::class.java))
            return WeatherHomeViewModel(useCase,locationUseCase,context,isLocationServiceEnabledUseCase,
            isAnyProviderAvailableUseCase) as T
        throw IllegalArgumentException("Unknown view model found")
    }
}