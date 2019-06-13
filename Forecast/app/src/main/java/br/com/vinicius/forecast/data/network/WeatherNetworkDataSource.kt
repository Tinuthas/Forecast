package br.com.vinicius.forecast.data.network

import androidx.lifecycle.LiveData
import br.com.vinicius.forecast.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )
}