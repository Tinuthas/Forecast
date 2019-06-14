package br.com.vinicius.forecast.ui.weather.current

import androidx.lifecycle.ViewModel
import br.com.vinicius.forecast.data.provider.UnitProvider
import br.com.vinicius.forecast.data.repository.ForecastRepository
import br.com.vinicius.forecast.internal.UnitSystem
import br.com.vinicius.forecast.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider
) : ViewModel(){

    private val unitSystem = unitProvider.getUnitSystem()
        //UnitSystem.METRIC// get from settings later

    val isMetric : Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }

    val weatherLocation by lazyDeferred{
        forecastRepository.getWeatherLocation()
    }
}