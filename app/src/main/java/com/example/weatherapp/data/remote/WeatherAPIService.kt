package com.example.weatherapp.data.remote

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIService {

    @GET("weather")
    suspend fun getWeather(
        @Query("q") cityName:String,
        @Query("appid") key:String = BuildConfig.API_KEY
    ):WeatherModel
}