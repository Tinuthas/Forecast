package br.com.vinicius.forecast.ui.base

import androidx.lifecycle.ViewModel
import br.com.vinicius.forecast.data.provider.UnitProvider
import br.com.vinicius.forecast.data.repository.ForecastRepository
import br.com.vinicius.forecast.internal.UnitSystem
import br.com.vinicius.forecast.internal.lazyDeferred

abstract class WeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel(){

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetricUnit: Boolean
            get() = unitSystem == UnitSystem.METRIC

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}