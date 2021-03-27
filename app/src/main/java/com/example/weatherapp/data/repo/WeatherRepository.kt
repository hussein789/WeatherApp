package com.example.weatherapp.data.repo

import com.example.weatherapp.data.datasource.RemoteDataSource
import com.example.weatherapp.data.model.WeatherModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface WeatherRepository {
    suspend fun getWeatherByCityName(cityName: String): WeatherModel
}

class WeatherRepositoryImpl
    (private val remoteDataSource: RemoteDataSource) : WeatherRepository {
    override suspend fun getWeatherByCityName(cityName: String): WeatherModel {
        return remoteDataSource.getWeatherData(cityName)
    }

}