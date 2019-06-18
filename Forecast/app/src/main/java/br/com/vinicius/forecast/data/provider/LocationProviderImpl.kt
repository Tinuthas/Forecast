package br.com.vinicius.forecast.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import br.com.vinicius.forecast.data.db.entity.WeatherLocation
import br.com.vinicius.forecast.internal.LocationPermissionNotGrantedException
import br.com.vinicius.forecast.internal.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context) : PreferenceProvider(context), LocationProvider{

    private val appContext = context.applicationContext


    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        val deviceLocationChange = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        }catch (e: LocationPermissionNotGrantedException) {
            false
        }
        return deviceLocationChange || hasCustomLocationChanged(lastWeatherLocation)
    }

    private fun hasCustomLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        if(!isUsingDeviceLocation()){
            val customLocationName = getCustomLocationName()
            return customLocationName != lastWeatherLocation.name
        }
        return false

    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        if(!isUsingDeviceLocation()) return false
        val deviceLocation = getLastDeviceLocation().await() ?: return false

        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - lastWeatherLocation.lat) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - lastWeatherLocation.lon) > comparisonThreshold

    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if(hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun isUsingDeviceLocation(): Boolean{
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    override suspend fun getPreferredLocationString(): String {
        if(isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationName()}"
                return "${deviceLocation.latitude}, ${deviceLocation.altitude}"
            }catch (e: LocationPermissionNotGrantedException){
                return "${getCustomLocationName()}"
            }
        }else
            return "${getCustomLocationName()}"
    }
}