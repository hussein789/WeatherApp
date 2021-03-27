package com.example.weatherapp.core.di

import com.example.weatherapp.presentation.home.WeatherHomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        RemoteDataSourceModule::class, ]
)
interface AppComponent {

    fun inject(target: WeatherHomeFragment)
}