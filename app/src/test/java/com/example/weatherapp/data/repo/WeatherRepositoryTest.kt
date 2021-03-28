package com.example.weatherapp.data.repo

import com.example.weatherapp.data.datasource.RemoteDataSource
import com.example.weatherapp.data.model.WeatherModel
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.mock
import java.lang.RuntimeException

class WeatherRepositoryTest{

    lateinit var repository:WeatherRepository
    private val remoteDataSource = mock(RemoteDataSource::class.java)
    private val weatherModel = mock(WeatherModel::class.java)

    private val cityName = "London"
    private val exception = RuntimeException("something went wrong")

    @Test
    fun getWeatherByCityName_remoteSuccess_emitSuccess() = runBlockingTest{
        mockSuccessCase()

        repository.getWeatherByCityName(cityName).map {
            val result = it.getOrNull()
            assertEquals(result,weatherModel)
        }
    }

    @Test
    fun getWeatherByCityName_remoteFailure_emitFailCase() = runBlockingTest{
        mockFailureCase()

        repository.getWeatherByCityName(cityName).map {
            val result = it.exceptionOrNull()
            assertEquals(result,exception)
        }
    }

    private fun mockFailureCase() = runBlockingTest {
        whenever(remoteDataSource.getWeatherData(cityName)).thenReturn(
            flow {
                emit(Result.failure<WeatherModel>(exception))
            }
        )
        repository = WeatherRepositoryImpl(remoteDataSource)
    }

    private fun mockSuccessCase() = runBlockingTest{
        whenever(remoteDataSource.getWeatherData(cityName)).thenReturn(
            flow {
                emit(Result.success(weatherModel))
            }
        )
        repository = WeatherRepositoryImpl(remoteDataSource)
    }
}