package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.core.di.AppComponent
import com.example.weatherapp.core.di.AppModule
import com.example.weatherapp.core.di.DaggerAppComponent
import us.dustinj.timezonemap.TimeZoneMap

class WeatherApp : Application() {

    companion object{
        lateinit var appComponent:AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDaggerComponent()
    }

    private fun initDaggerComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this.applicationContext))
            .build()
    }

}