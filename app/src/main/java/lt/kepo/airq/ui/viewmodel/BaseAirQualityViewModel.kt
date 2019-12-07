package lt.kepo.airq.ui.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import lt.kepo.airq.data.repository.airquality.AirQualityRepository
import lt.kepo.airq.utility.isLocationEnabled

abstract class BaseAirQualityViewModel(
    val airQualityRepository: AirQualityRepository,
    private val context: Application
) : AndroidViewModel(context) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    init {
        if (canUseLocation())
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }

    abstract suspend fun updateLocalAirQualityHere(location: Location?)

    fun fetchLocationAirQuality() {
        if (canUseLocation()) {
            fusedLocationClient.locationAvailability.addOnSuccessListener { locationAvailability ->
                if (locationAvailability.isLocationAvailable)
                    fusedLocationClient.lastLocation.addOnSuccessListener { loc -> viewModelScope.launch { updateLocalAirQualityHere(loc) } }
                else
                    viewModelScope.launch { updateLocalAirQualityHere(null) }
            }
        } else {
            viewModelScope.launch { updateLocalAirQualityHere(null) }
        }
    }

    private fun canUseLocation(): Boolean {
        return if (isLocationEnabled(context)) {
            if (!::fusedLocationClient.isInitialized)
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            true
        } else
            false
    }
}