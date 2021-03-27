package com.example.weatherapp.data.datasource

import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.data.remote.WeatherAPIService
import javax.inject.Inject

interface RemoteDataSource{
    suspend fun getWeatherData(cityName:String):WeatherModel
}

open class RemoteDataSourceImpl(
    private val weatherAPIService: WeatherAPIService
): RemoteDataSource {

    override suspend fun getWeatherData(cityName: String): WeatherModel {
        return weatherAPIService.getWeather(cityName)
    }

}