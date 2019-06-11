package br.com.vinicius.forecast.data.response

import com.google.gson.annotations.SerializedName


data class CurrentWeatherResponse(
    @SerializedName( "current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: Location
)