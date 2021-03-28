package com.example.weatherapp.data.repo

import com.example.weatherapp.data.datasource.RemoteDataSource
import com.example.weatherapp.data.model.WeatherModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface WeatherRepository {
    suspend fun getWeatherByCityName(cityName: String): Flow<Result<WeatherModel?>>
}

class WeatherRepositoryImpl
    (
    private val remoteDataSource: RemoteDataSource
) : WeatherRepository {
    override suspend fun getWeatherByCityName(cityName: String): Flow<Result<WeatherModel?>> {
        return remoteDataSource.getWeatherData(cityName).map {
            if(it.isSuccess)
                Result.success(it.getOrNull())
            else
                Result.failure(it.exceptionOrNull()!!)
        }
    }
}