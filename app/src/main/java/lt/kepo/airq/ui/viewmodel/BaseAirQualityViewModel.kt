package lt.kepo.airq.ui.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import lt.kepo.airq.data.repository.airquality.AirQualityRepository
import lt.kepo.airq.utility.isLocationEnabled

abstract class BaseAirQualityViewModel(
    context: Application,
    val airQualityRepository: AirQualityRepository,
    private val fusedLocationClient : FusedLocationProviderClient
) : AndroidViewModel(context) {

    abstract suspend fun updateLocalAirQualityHere(location: Location?)

    fun fetchLocationAirQuality() {
        if (isLocationEnabled(getApplication())) {
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
}