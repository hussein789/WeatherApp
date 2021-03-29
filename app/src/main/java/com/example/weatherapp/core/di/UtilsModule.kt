package com.example.weatherapp.core.di

import com.example.weatherapp.presentation.utils.TimeZoneMapImpl
import com.example.weatherapp.presentation.utils.TimeZoneMapUtils
import dagger.Module
import dagger.Provides

@Module
class UtilsModule {

    @Provides
    fun provideTimeMapUtils():TimeZoneMapUtils{
        return TimeZoneMapImpl()
    }
}