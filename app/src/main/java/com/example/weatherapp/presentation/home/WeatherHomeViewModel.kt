package com.example.weatherapp.presentation.home

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.GetWeatherInfoUseCase
import com.example.weatherapp.domain.LocationCallback
import com.example.weatherapp.domain.MyLocationUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WeatherHomeViewModel(
        private val getWeatherInfoUseCase: GetWeatherInfoUseCase,
        private val locationUseCase: MyLocationUseCase,
        private val applicationContext: Context
) : ViewModel() {

    private val showError = MutableLiveData<Boolean>()
    val errorLD: LiveData<Boolean> get() = showError

    private val showLoading = MutableLiveData<Boolean>()
    val loadingLD: LiveData<Boolean> get() = showLoading

    private val weatherInfo = MutableLiveData<WeatherUIModel>()
    val weatherLD: LiveData<WeatherUIModel> get() = weatherInfo

    private val showContent = MutableLiveData<Boolean>()
    val showContentLD: LiveData<Boolean> get() = showContent

    private val searchedCityName = MutableLiveData<String>()
    val searchedCityNameLD: LiveData<String> get() = searchedCityName

    private val showLocationPermission = MutableLiveData<Boolean>()
    val showLocationPermissionLD: LiveData<Boolean> get() = showLocationPermission

    private val showEmptyState = MutableLiveData<Boolean>()
    val showEmptyStateLD: LiveData<Boolean> get() = showEmptyState

    fun getWeatherByCityName(cityName: String) {
        viewModelScope.launch {
            if (cityName.isEmpty()) {
                showEmptyState.value = true
            } else {
                showLoading.value = true
                try {
                    val result = getWeatherInfoUseCase.invoke(cityName)
                    weatherInfo.value = result
                    showContent.value = true
                } catch (ex: Exception) {
                    showError.value = true
                }
                showLoading.value = false
            }

        }
    }

    fun getWeatherForCurrentLocation() {
        viewModelScope.launch {
            try {
                showLoading.value = true
                locationUseCase.invoke(object : LocationCallback {
                    override fun onLocationDetected(cityName: String) {
                        searchedCityName.value = cityName
                    }

                    override fun locationServiceNotEnabled() {
                        showLocationPermission.value = true
                        showLoading.value = false
                    }

                    override fun onLocationNoDetected() {
                        showLoading.value = false
                    }
                })
            } catch (ex: Exception) {
                showError.value = true
                showLoading.value = false
            }
        }
    }

    fun onSearchTextChanged(text: CharSequence?) {
        getWeatherByCityName(text?.toString() ?: "")
    }

    fun setOnClearTextIconClicked() {
        searchedCityName.value = ""
    }

}