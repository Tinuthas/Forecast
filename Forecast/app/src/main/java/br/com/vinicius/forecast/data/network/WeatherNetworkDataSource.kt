package br.com.vinicius.forecast.data.network

import androidx.lifecycle.LiveData
import br.com.vinicius.forecast.data.network.response.CurrentWeatherResponse
import br.com.vinicius.forecast.data.network.response.FutureWeatherResponse

interface WeatherNetworkDataSource {
    val downloadCurrentWeather: LiveData<CurrentWeatherResponse>
    val downloadFutureWeather: LiveData<FutureWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )

    suspend fun fetchFutureWeather(
        location: String,
        languageCode: String
    )


}