package br.com.vinicius.forecast.internal

import java.io.IOException
import java.lang.Exception

class NoConnectivityInterceptor: IOException()
class LocationPermissionNotGrantedException: Exception()