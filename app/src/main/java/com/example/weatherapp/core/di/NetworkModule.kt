package com.example.weatherapp.core.di

import com.example.weatherapp.data.remote.WeatherAPIService
import com.jakewharton.espresso.OkHttp3IdlingResource
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



val client = OkHttpClient()
val idlingResource = OkHttp3IdlingResource.create("OK",client)


@Module
class NetworkModule {

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()

        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    fun provideRetrofit(logging: HttpLoggingInterceptor): Retrofit {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        val BASE_URL_PLAYLIST = "https://api.openweathermap.org/data/2.5/"

        return Retrofit.Builder()
            .baseUrl(BASE_URL_PLAYLIST)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideWeatherAPIService(retrofit: Retrofit):WeatherAPIService{
        return retrofit.create(WeatherAPIService::class.java)
    }
}