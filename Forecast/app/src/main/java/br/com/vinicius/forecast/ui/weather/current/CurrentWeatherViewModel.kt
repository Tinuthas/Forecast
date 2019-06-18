package br.com.vinicius.forecast.ui.weather.current

import androidx.lifecycle.ViewModel
import br.com.vinicius.forecast.data.provider.UnitProvider
import br.com.vinicius.forecast.data.repository.ForecastRepository
import br.com.vinicius.forecast.internal.UnitSystem
import br.com.vinicius.forecast.internal.lazyDeferred
import br.com.vinicius.forecast.ui.base.WeatherViewModel

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider){


    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(super.isMetricUnit)
    }

}