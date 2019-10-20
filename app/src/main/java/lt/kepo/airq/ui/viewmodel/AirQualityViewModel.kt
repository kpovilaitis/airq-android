package lt.kepo.airq.ui.viewmodel

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import lt.kepo.airq.data.api.ApiErrorResponse
import lt.kepo.airq.data.api.ApiSuccessResponse
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.repository.airquality.AirQualityRepository
import lt.kepo.airq.utility.isLocationEnabled

class AirQualityViewModel(
    private val airQualityRepository: AirQualityRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _errorMessage = MutableLiveData<String>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    val airQuality: LiveData<AirQuality> = Transformations.switchMap( airQualityRepository.getLocalAirQualityHere() ) { liveData { emit(it) } }
    val errorMessage: LiveData<String> get() = _errorMessage

    init {
        if (shouldUseLocation(application)) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
        }
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }

    fun getRemoteAirQualityHere(context: Context) {
        if (shouldUseLocation(context)) {
            fusedLocationClient.locationAvailability.addOnSuccessListener { locationAvailability ->
                if (locationAvailability.isLocationAvailable)
                    fusedLocationClient.lastLocation.addOnSuccessListener { loc -> updateRemoteAirQualityHere(loc) }
                else
                    updateRemoteAirQualityHere(null)
            }
        } else {
            updateRemoteAirQualityHere(null)
        }
    }

    private fun updateRemoteAirQualityHere(location: Location?) {
        _errorMessage.value = ""

        viewModelScope.launch {
            when (val response = airQualityRepository.getRemoteAirQualityHere(location)) {
                is ApiSuccessResponse -> {
                    val airQualityResponse = response.data

                    airQualityResponse.isCurrentLocationQuality = true

                    airQualityRepository.upsertLocalAirQualityHere(airQualityResponse)
                }
                is ApiErrorResponse -> _errorMessage.value = response.errorMessage
            }
        }
    }

    private fun shouldUseLocation(context: Context): Boolean {
        return if (isLocationEnabled(context)) {
            if (!::fusedLocationClient.isInitialized) fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            true
        } else false
    }
}