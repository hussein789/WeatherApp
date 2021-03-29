package com.example.weatherapp.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.presentation.SingleLiveEvent
import com.example.weatherapp.presentation.utils.TimeZoneMapUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SplashViewModel(private val timeZoneMapUtils: TimeZoneMapUtils) : ViewModel() {

    private val showLoading = MutableLiveData<Boolean>()
    val showLoadingLD:LiveData<Boolean> get() = showLoading

    private val navigateToWeatherScreen = SingleLiveEvent<Boolean>()
    val navigateToWeatherScreenLD:LiveData<Boolean> get() = navigateToWeatherScreen

    private val job = Job()
    private val uiScope = CoroutineScope(job + Dispatchers.IO)



    fun init() {
        uiScope.launch {
            try {
                showLoading.postValue(true)
                timeZoneMapUtils.iniTimeZone()
                navigateToWeatherScreen.postValue(true)
                showLoading.postValue(false)
            }
            catch (ex:Exception){
                showLoading.postValue(false)
                navigateToWeatherScreen.postValue(true)
            }

        }

    }
}