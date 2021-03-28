package com.example.weatherapp.data.datasource

import BaseTestClass
import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.data.remote.WeatherAPIService
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.mock
import java.lang.RuntimeException

class RemoteDataSourceTest : BaseTestClass() {

    private val weatherAPIService = mock(WeatherAPIService::class.java)
    private val weatherModel = mock(WeatherModel::class.java)

    lateinit var remoteDataSource:RemoteDataSource

    private val cityName = "London"
    private val exception = RuntimeException("something went wrong")

    @Test
    fun getWeatherData_successCase_returnSuccessModel() = runBlockingTest{
        mockSuccessCase()

        remoteDataSource.getWeatherData(cityName).map {
            val result = it.getOrNull()

            assertEquals(result,weatherModel)
        }
    }

    @Test
    fun getWeatherData_failureCase_returnException() = runBlockingTest{
        mockFailureCase()

        remoteDataSource.getWeatherData(cityName).map {
            val result = it.exceptionOrNull()
            assertEquals(result,exception)
        }
    }

    private fun mockSuccessCase() = runBlockingTest{
        whenever(weatherAPIService.getWeather(cityName)).thenReturn(weatherModel)
        remoteDataSource = RemoteDataSourceImpl(weatherAPIService)
    }

    private fun mockFailureCase() = runBlockingTest {
        whenever(weatherAPIService.getWeather(cityName)).thenThrow(exception)
        remoteDataSource = RemoteDataSourceImpl(weatherAPIService)
    }

}