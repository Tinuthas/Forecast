package br.com.vinicius.forecast.ui.weather.future.list

import androidx.lifecycle.ViewModel
import br.com.vinicius.forecast.data.provider.UnitProvider
import br.com.vinicius.forecast.data.repository.ForecastRepository
import br.com.vinicius.forecast.internal.lazyDeferred
import br.com.vinicius.forecast.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureListWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weatherEntries by lazyDeferred {  forecastRepository.getFutureWeather(LocalDate.now(), super.isMetricUnit)}
}
