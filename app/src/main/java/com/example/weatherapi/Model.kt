package com.example.weatherapi

object Model {
    data class Result(
        val id : String,
        val coord : Coord,
        val wind : Wind,
        val clouds : Clouds,
        val weather: List<Weather>,
        val main: Main,
        val sys:Sys,
        val visibility : String,
        val dt:String,
        val name: String,
        val cod: String,
        val timezone: String
    )

    data class Clouds (
        val all : String
    )

    data class Coord (
        val lon : String,
        val lat : String
    )

    data class Weather(
        val id : String,
        val main: String,
        val description: String,
        val icon: String
    )

    data class Main(
        val temp: String,
        val feels_like : String,
        val pressure: String,
        val humidity: String,
        val temp_min: String,
        val temp_max: String
    )
    data class Sys(
        val type : String,
        val id : String,
        val sunrise: String,
        val sunset: String,
        val country: String
    )

    data class Wind (
        val speed : String,
        val deg : String
    )
}