package br.com.vinicius.forecast.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import br.com.vinicius.forecast.data.db.CurrentWeatherDao
import br.com.vinicius.forecast.data.db.FutureWeatherDao
import br.com.vinicius.forecast.data.db.WeatherLocationDao
import br.com.vinicius.forecast.data.db.entity.WeatherLocation
import br.com.vinicius.forecast.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import br.com.vinicius.forecast.data.db.unitlocalized.future.UnitSpecificSimpleFutureWeatherEntry
import br.com.vinicius.forecast.data.network.FORECAST_DAYS_COUNT
import br.com.vinicius.forecast.data.network.WeatherNetworkDataSource
import br.com.vinicius.forecast.data.network.response.CurrentWeatherResponse
import br.com.vinicius.forecast.data.network.response.FutureWeatherResponse
import br.com.vinicius.forecast.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
): ForecastRepository {

    init {
        /*weatherNetworkDataSource.downloadCurrentWeather.observeForever{newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }*/
        weatherNetworkDataSource.apply {
            downloadCurrentWeather.observeForever{
                newCurrentWeather -> persistFetchedCurrentWeather(newCurrentWeather)
            }
            downloadFutureWeather.observeForever{
                newFutureWeather -> persistFetchedFutureWeather(newFutureWeather)
            }

        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
       return withContext(Dispatchers.IO){
           initWeatherData()
           return@withContext if(metric) currentWeatherDao.getWeatherMetric()
           else currentWeatherDao.getWeatherImperial()
       }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO){
            return@withContext weatherLocationDao.getLocation()
        }

    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getLocationNonLive()

        if(lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }
        if(isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()

        if(isFetchFutureNeeded())
            fetchFutureWeather()
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private fun persistFetchedFutureWeather(fetchedWeather: FutureWeatherResponse) {
        fun deleteOldForecastData() {
            val today = LocalDate.now()
            futureWeatherDao.deleteOldEntries(today)
        }

        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            val futureWeatherList = fetchedWeather.futureWeatherEntries.entries
            futureWeatherDao.insert(futureWeatherList)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }

    private suspend fun fetchFutureWeather() {
        weatherNetworkDataSource.fetchFutureWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    private fun isFetchFutureNeeded() : Boolean {
        val today = LocalDate.now()
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount < FORECAST_DAYS_COUNT
    }

    override suspend fun getFutureWeather(startDate: LocalDate, metric: Boolean
    ): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext if(metric) futureWeatherDao.getSimpleWeatherForecastsMetric(startDate)
            else futureWeatherDao.getSimpleWeatherForecastsImperial(startDate)
        }

    }

}