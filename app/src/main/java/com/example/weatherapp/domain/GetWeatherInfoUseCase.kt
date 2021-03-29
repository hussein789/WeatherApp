package com.example.weatherapp.domain

import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.data.repo.WeatherRepository
import com.example.weatherapp.presentation.home.WeatherUIModel
import com.example.weatherapp.presentation.utils.TimeZoneMapUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.lang.RuntimeException
import javax.inject.Inject

class GetWeatherInfoUseCase @Inject constructor(private val repository: WeatherRepository,
private val timeZoneMapUtils: TimeZoneMapUtils) {
    suspend fun execute(cityName:String):Flow<Result<WeatherUIModel>>{
        val result = repository.getWeatherByCityName(cityName)
        return result.map {
            if(it.isSuccess){
                val weather = it.getOrNull()
                weather?.let {
                    val zoneId = timeZoneMapUtils.getTimeZoneId(weather.coord.lat,weather.coord.lon)
                    val weatherUiModel =  WeatherUIModel(
                        Utils.convertKelvinToCel(weather.main.temp),
                        Utils.getTodayDateString(zoneId),
                        Utils.getTodayTimeString(zoneId),
                        weather.weather[0].description,
                        Utils.convertKelvinToCel(weather.main.tempMin),
                        Utils.convertKelvinToCel(weather.main.tempMax),
                        weather.wind.speed,
                        weather.main.humidity,

                        weather.clouds.all,
                        weather.main.pressure,
                        Utils.getTimeStringByMilliSec(weather.sys.sunrise,zoneId),
                        Utils.getTimeStringByMilliSec(weather.sys.sunset,zoneId),
                        weather.weather[0].icon,
                        weather.coord.lat.toString(),
                        weather.coord.lon.toString()
                    )
                    Result.success(weatherUiModel)
                } ?: Result.failure(it.exceptionOrNull()!!)
            }
            else{
                Result.failure(it.exceptionOrNull()!!)
            }
        }
    }
}