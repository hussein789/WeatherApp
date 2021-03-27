package com.example.weatherapp.core.di

import com.example.weatherapp.data.datasource.RemoteDataSource
import com.example.weatherapp.data.repo.WeatherRepository
import com.example.weatherapp.data.repo.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideWeatherRepository(remoteDataSource: RemoteDataSource):WeatherRepository{
        return WeatherRepositoryImpl(remoteDataSource)
    }
}