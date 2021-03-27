package com.example.weatherapp.domain

import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.data.repo.WeatherRepository
import com.example.weatherapp.presentation.home.WeatherUIModel
import javax.inject.Inject

class GetWeatherInfoUseCase @Inject constructor(private val repository: WeatherRepository) {
    suspend fun invoke(cityName:String):WeatherUIModel{
        val weather = repository.getWeatherByCityName(cityName)
        return WeatherUIModel(
            Utils.convertKelvinToCel(weather.main.temp),
            Utils.getTodayDateString(weather.timezone,weather.name,weather.sys.country),
            Utils.getTodayTimeString(weather.timezone,weather.name,weather.sys.country),
            weather.weather[0].description,
            Utils.convertKelvinToCel(weather.main.tempMin),
            Utils.convertKelvinToCel(weather.main.tempMax),
            weather.wind.speed,
            weather.main.humidity,
            weather.clouds.all,
            weather.main.pressure,
            Utils.getTimeStringByMilliSec(weather.sys.sunrise,weather.timezone,weather.name),
            Utils.getTimeStringByMilliSec(weather.sys.sunset,weather.timezone,weather.name),
            weather.weather[0].icon,
            weather.coord.lat.toString(),
            weather.coord.lon.toString()
        )
    }
}