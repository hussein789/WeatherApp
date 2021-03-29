package com.example.weatherapp.domain

import BaseTestClass
import com.example.weatherapp.data.model.*
import com.example.weatherapp.data.repo.WeatherRepository
import com.example.weatherapp.presentation.utils.TimeZoneMapUtils
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class GetWeatherInfoUseCaseTest : BaseTestClass(){

    lateinit var getWeatherInfoUseCase: GetWeatherInfoUseCase
    private val repository = mock(WeatherRepository::class.java)
    private val timeZoneMapUtils = mock(TimeZoneMapUtils::class.java)

    private val cityName = "London"

    private val weatherModel = WeatherModel("stations",
    Clouds(100),200, Coord(-0.1257,51.5085),1616880038,2643743,
        Main(276.26,71,1024,281.63,282.59,280.93),
        "London", Sys("GB",1414,1616824014,1616869474,1),0,10000,
        listOf(Weather("overcast clouds","04n",804,"Clouds")),
        Wind(230,5.66)
    )
    private val exception = RuntimeException("network error")


    @Test
    fun getWeatherUseCase_invokeRepositoryCall() = runBlockingTest{
        mockSuccess()
        getWeatherInfoUseCase.execute(cityName)

        verify(repository,times(1)).getWeatherByCityName(cityName)
    }

    @Test
    fun getWeatherUseCase_setSameDescription() = runBlockingTest{
        mockSuccess()
        getWeatherInfoUseCase.execute(cityName).map {
            val result = it.getOrNull()
            assertEquals(result?.condition,weatherModel.weather[0].description)
        }

    }

    @Test
    fun getWeatherUseCase_setWindSpeed() = runBlockingTest{
        mockSuccess()
        getWeatherInfoUseCase.execute(cityName).map {
            val result = it.getOrNull()
            assertEquals(result?.wind.toString(),weatherModel.wind.speed.toString())
        }
    }

    @Test
    fun getWeatherUseCase_setSameHumidity() = runBlockingTest{
        mockSuccess()
        getWeatherInfoUseCase.execute(cityName).map {
            val result = it.getOrNull()
            assertEquals(result?.humidity,weatherModel.main.humidity)
        }
    }

    @Test
    fun getWeatherUseCase_setSameClouds() = runBlockingTest{
        mockSuccess()
        getWeatherInfoUseCase.execute(cityName).map {
            val result = it.getOrNull()
            assertEquals(result?.clouds,weatherModel.clouds.all)
        }
    }

    @Test
    fun getWeatherUseCase_setSamePressure() = runBlockingTest{
        mockSuccess()
        getWeatherInfoUseCase.execute(cityName).map {
            val result = it.getOrNull()
            assertEquals(result?.pressure,weatherModel.main.pressure)
        }
    }

    @Test
    fun getWeatherUseCase_setSameLatLng() = runBlockingTest{
        mockSuccess()

        getWeatherInfoUseCase.execute(cityName).map {
            val result = it.getOrNull()
            assertEquals(result?.lat,weatherModel.coord.lat.toString())
            assertEquals(result?.lng,weatherModel.coord.lon.toString())
        }

    }

    @Test
    fun getWeatherUseCase_setSameIcon() = runBlockingTest{
        mockSuccess()
        getWeatherInfoUseCase.execute(cityName).map {
            val result = it.getOrNull()
            assertEquals(result?.conditionIcon,weatherModel.weather[0].icon)
        }
    }

    @Test
    fun getWeatherUseCase_returnError_emitError() = runBlockingTest{
        mockFailCase()

        getWeatherInfoUseCase.execute(cityName).map {
            val result = it.exceptionOrNull()
            assertEquals(result,exception)
        }
    }


    private fun mockSuccess() = runBlockingTest{
        whenever(repository.getWeatherByCityName(cityName)).thenReturn(
            flow{
               emit(Result.success(weatherModel))
            }
        )
        getWeatherInfoUseCase = GetWeatherInfoUseCase(repository,timeZoneMapUtils)
    }

    private fun mockFailCase() = runBlockingTest{
        whenever(repository.getWeatherByCityName(cityName)).thenReturn(
            flow{
                emit(Result.failure<WeatherModel>(exception))
            }
        )
        getWeatherInfoUseCase = GetWeatherInfoUseCase(repository,timeZoneMapUtils)
    }
}