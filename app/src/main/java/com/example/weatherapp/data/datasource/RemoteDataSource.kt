package com.example.weatherapp.data.datasource

import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.data.remote.WeatherAPIService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.RuntimeException
import javax.inject.Inject

interface RemoteDataSource{
    suspend fun getWeatherData(cityName:String):Flow<Result<WeatherModel>>
}

open class RemoteDataSourceImpl(
    private val weatherAPIService: WeatherAPIService
): RemoteDataSource {

    override suspend fun getWeatherData(cityName: String): Flow<Result<WeatherModel>> {
        return flow {
            val result = weatherAPIService.getWeather(cityName)
            emit(Result.success(result))
        }
            .catch {
                emit(Result.failure(RuntimeException("something went wrong")))
            }
    }

}