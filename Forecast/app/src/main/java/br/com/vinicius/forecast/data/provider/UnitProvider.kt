package br.com.vinicius.forecast.data.provider

import br.com.vinicius.forecast.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}