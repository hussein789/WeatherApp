package com.example.weatherapp.presentation.home

import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.GetWeatherInfoUseCase
import com.example.weatherapp.domain.IsAnyProviderAvailableUseCase
import com.example.weatherapp.domain.IsLocationServiceEnabledUseCase
import com.example.weatherapp.domain.MyLocationUseCase
import com.example.weatherapp.presentation.SingleLiveEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

class WeatherHomeViewModel(
    private val getWeatherInfoUseCase: GetWeatherInfoUseCase,
    private val locationUseCase: MyLocationUseCase,
    private val context: Context,
    private val isLocationServiceEnabledUseCase: IsLocationServiceEnabledUseCase,
    private val isAnyProviderAvailableUseCase: IsAnyProviderAvailableUseCase
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

    private val showLocationPermission = SingleLiveEvent<Boolean>()
    val showLocationPermissionLD: LiveData<Boolean> get() = showLocationPermission

    private val showEmptyState = SingleLiveEvent<Boolean>()
    val showEmptyStateLD: LiveData<Boolean> get() = showEmptyState


    fun getWeatherByCityName(cityName: String) {
        viewModelScope.launch {
            if (cityName.isEmpty()) {
                showEmptyState.value = true
            } else {
                showLoading.value = true
                try {
                    val result = getWeatherInfoUseCase.execute(cityName)
                    result.collect {
                        if (it.isSuccess) {
                            weatherInfo.value = it.getOrNull()
                            showContent.value = true
                        } else {
                            showError.value = true
                        }
                        showLoading.value = false
                    }
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
                if (isLocationServiceEnabledUseCase.execute()) {
                    if (isAnyProviderAvailableUseCase.execute()) {
                        showLoading.value = true
                        val location = locationUseCase.execute()
                        val fullAddress = getCityNameFromLocation(location)
                        searchedCityName.value = fullAddress
                        showLoading.value = false
                    }
                } else {
                    showLocationPermission.value = true
                }

            } catch (ex: Exception) {
                showError.value = true
                showLoading.value = false
            }
        }
    }

    private fun getCityNameFromLocation(location: Location): String {
        val geocode = Geocoder(context, Locale.getDefault())
        val addressList =
            geocode.getFromLocation(location.latitude, location.longitude, 1)
        return addressList?.get(0)?.adminArea ?: ""
    }


    fun onSearchTextChanged(text: CharSequence?) {
        getWeatherByCityName(text?.toString() ?: "")
    }

    fun setOnClearTextIconClicked() {
        searchedCityName.value = ""
    }

}