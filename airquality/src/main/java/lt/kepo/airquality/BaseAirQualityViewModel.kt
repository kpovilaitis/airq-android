package lt.kepo.airquality

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import lt.kepo.airquality.domain.shouldUpdate
import lt.kepo.core.network.ApiErrorResponse
import lt.kepo.core.network.ApiSuccessResponse
import lt.kepo.core.model.AirQuality
import lt.kepo.airquality.domain.AirQualityRepository

abstract class BaseAirQualityViewModel(
    context: Application,
    private val fusedLocationClient : FusedLocationProviderClient,
    internal val airQualityRepository: AirQualityRepository
) : AndroidViewModel(context) {
    val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun updateCachedAirQualityHere(force: Boolean, airQualityHere: AirQuality?) {
        if (force || airQualityHere == null || airQualityHere.shouldUpdate()) {
            if (isLocationEnabled(getApplication())) {
                fusedLocationClient.locationAvailability.addOnSuccessListener { locationAvailability ->
                    if (locationAvailability.isLocationAvailable) {
                        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                            viewModelScope.launch { updateAirQualityHere(loc) }
                        }
                    } else {
                        viewModelScope.launch { updateAirQualityHere(null) }
                    }
                }
            } else {
                viewModelScope.launch { updateAirQualityHere(null) }
            }
        }
    }

    private suspend fun updateAirQualityHere(location: Location?) {
        when (val result = airQualityRepository.updateAirQualityHere(location)) {
            is ApiErrorResponse<*> -> _errorMessage.value = result.error
        }
    }

    private fun isLocationEnabled(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // This is new method provided in API 28
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            locationManager.isLocationEnabled
                    && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        } else { // This is Deprecated in API 28
            Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            ) != Settings.Secure.LOCATION_MODE_OFF
        }
    }
}