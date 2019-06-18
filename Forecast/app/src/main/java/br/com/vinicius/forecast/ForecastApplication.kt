package br.com.vinicius.forecast

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import br.com.vinicius.forecast.data.db.ForecastDatabase
import br.com.vinicius.forecast.data.network.*
import br.com.vinicius.forecast.data.provider.LocationProvider
import br.com.vinicius.forecast.data.provider.LocationProviderImpl
import br.com.vinicius.forecast.data.provider.UnitProvider
import br.com.vinicius.forecast.data.provider.UnitProviderImpl
import br.com.vinicius.forecast.data.repository.ForecastRepository
import br.com.vinicius.forecast.data.repository.ForecastRepositoryImpl
import br.com.vinicius.forecast.ui.weather.current.CurrentWeatherViewModelFactory
import br.com.vinicius.forecast.ui.weather.future.list.FutureListWeatherViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForecastApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().futureWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().weatherLocationDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApixuWeatherApiService(instance())}
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<ForecastRepository>() with singleton {ForecastRepositoryImpl(instance(), instance(), instance(),
            instance(), instance())}
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance() ) }
        bind() from provider { FutureListWeatherViewModelFactory(instance(), instance() ) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}