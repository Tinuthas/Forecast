package br.com.vinicius.forecast.data.repository

import androidx.lifecycle.LiveData
import br.com.vinicius.forecast.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean) : LiveData<out UnitSpecificCurrentWeatherEntry>
}