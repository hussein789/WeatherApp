package com.example.weatherapp.core.di

import com.example.weatherapp.presentation.home.WeatherHomeFragment
import com.example.weatherapp.presentation.splash.SplashFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        RemoteDataSourceModule::class,
    UtilsModule::class
    ]
)
interface AppComponent {

    fun inject(target: WeatherHomeFragment)
    fun inject(target: SplashFragment)
}