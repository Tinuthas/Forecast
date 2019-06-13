package br.com.vinicius.forecast.data.network.response

import br.com.vinicius.forecast.data.db.entity.CurrentWeatherEntry
import br.com.vinicius.forecast.data.db.entity.Location
import com.google.gson.annotations.SerializedName


data class CurrentWeatherResponse(
    @SerializedName( "current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: Location
)