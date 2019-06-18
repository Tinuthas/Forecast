package br.com.vinicius.forecast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.vinicius.forecast.data.network.response.CurrentWeatherResponse
import br.com.vinicius.forecast.data.network.response.FutureWeatherResponse
import br.com.vinicius.forecast.internal.NoConnectivityInterceptor

const val FORECAST_DAYS_COUNT = 7

class WeatherNetworkDataSourceImpl(
  private val apixuWeatherApiService: ApixuWeatherApiService
) : WeatherNetworkDataSource {

    private val _downloadCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try {
            val fetchedCurrentWeather = apixuWeatherApiService
                .getCurrentWeather(location, languageCode)
                .await()
            _downloadCurrentWeather.postValue(fetchedCurrentWeather)
        }catch (e: NoConnectivityInterceptor) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }

    private val _downloadFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val downloadFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadFutureWeather

    override suspend fun fetchFutureWeather(location: String, languageCode: String) {
        try {
            val fetchedFutureWeather = apixuWeatherApiService
                .getFutureWeather(location, FORECAST_DAYS_COUNT, languageCode)
                .await()
            _downloadFutureWeather.postValue(fetchedFutureWeather)
        }catch (e: NoConnectivityInterceptor) {
            Log.e("Connectivity", "No internet connection.")
        }
    }
}