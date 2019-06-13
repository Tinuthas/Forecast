package br.com.vinicius.forecast.ui.weather.current

import androidx.lifecycle.ViewModel
import br.com.vinicius.forecast.data.repository.ForecastRepository
import br.com.vinicius.forecast.internal.UnitSystem
import br.com.vinicius.forecast.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel(){

    private val unitSystem = UnitSystem.METRIC// get from settings later

    val isMetric : Boolean
        get() = unitSystem == UnitSystem.METRIC
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }
}