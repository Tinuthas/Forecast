package br.com.vinicius.forecast.data.repository

import androidx.lifecycle.LiveData
import br.com.vinicius.forecast.data.db.entity.WeatherLocation
import br.com.vinicius.forecast.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import br.com.vinicius.forecast.data.db.unitlocalized.future.UnitSpecificSimpleFutureWeatherEntry
import org.threeten.bp.LocalDate

interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean) : LiveData<out UnitSpecificCurrentWeatherEntry>

    suspend fun getFutureWeather(startDate: LocalDate, metric: Boolean): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>>

    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}