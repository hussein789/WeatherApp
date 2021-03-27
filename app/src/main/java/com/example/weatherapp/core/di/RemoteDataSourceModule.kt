package com.example.weatherapp.core.di

import com.example.weatherapp.data.datasource.RemoteDataSource
import com.example.weatherapp.data.datasource.RemoteDataSourceImpl
import com.example.weatherapp.data.remote.WeatherAPIService
import dagger.Module
import dagger.Provides

@Module
class RemoteDataSourceModule {

    @Provides
    fun provideRemoteDataSource(weatherAPIService: WeatherAPIService):RemoteDataSource{
        return RemoteDataSourceImpl(weatherAPIService)
    }
}