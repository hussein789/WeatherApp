package com.example.weatherapp.presentation.home

data class WeatherUIModel(
    val temp:Int,
    val todayDate:String,
    val todayTime:String,
    val condition:String,
    val minTemp:Int,
    val maxTemp:Int,
    val wind:Double,
    val humidity:Int,
    val clouds:Int,
    val pressure:Int,
    val sunrise:String,
    val sunset:String,
    val conditionIcon:String,
    val lat:String,
    val lng:String
)