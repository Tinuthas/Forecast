package br.com.vinicius.forecast.data.network.response

import br.com.vinicius.forecast.data.db.entity.WeatherLocation
import com.google.gson.annotations.SerializedName


data class FutureWeatherResponse(
    @SerializedName("forecast")
    val futureWeatherEntries: ForecastDaysContainer,
    val location: WeatherLocation
)