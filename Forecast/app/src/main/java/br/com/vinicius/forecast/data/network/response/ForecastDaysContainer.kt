package br.com.vinicius.forecast.data.network.response

import br.com.vinicius.forecast.data.db.entity.FutureWeatherEntry
import com.google.gson.annotations.SerializedName


data class ForecastDaysContainer(
    @SerializedName("forecastday")
    val entries: List<FutureWeatherEntry>
)