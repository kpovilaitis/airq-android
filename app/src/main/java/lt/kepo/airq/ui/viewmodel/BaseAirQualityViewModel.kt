package lt.kepo.airq.ui.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import lt.kepo.airq.data.api.ApiErrorResponse
import lt.kepo.airq.data.api.ApiSuccessResponse
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.repository.airquality.AirQualityRepository
import lt.kepo.airq.utility.isLocationEnabled
import java.util.*

abstract class BaseAirQualityViewModel(
    context: Application,
    val airQualityRepository: AirQualityRepository,
    private val fusedLocationClient : FusedLocationProviderClient
) : AndroidViewModel(context) {
    val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchLocationAirQuality() {
        if (isLocationEnabled(getApplication())) {
            fusedLocationClient.locationAvailability.addOnSuccessListener { locationAvailability ->
                if (locationAvailability.isLocationAvailable)
                    fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                        viewModelScope.launch { updateLocalAirQualityHere(loc) }
                    }
                else
                    viewModelScope.launch { updateLocalAirQualityHere(null) }
            }
        } else {
            viewModelScope.launch { updateLocalAirQualityHere(null) }
        }
    }

    suspend fun updateLocalAirQuality(quality: AirQuality) {
        when (val response = airQualityRepository.getRemoteAirQuality(quality.stationId)) {
            is ApiSuccessResponse -> airQualityRepository.insertLocalAirQuality(response.data)
            is ApiErrorResponse -> _errorMessage.value = response.error
        }
    }

    private suspend fun updateLocalAirQualityHere(location: Location?) {
        when (val response = airQualityRepository.getRemoteAirQualityHere(location)) {
            is ApiSuccessResponse -> {
                val airQualityResponse = response.data

                airQualityResponse.isCurrentLocationQuality = true

                airQualityRepository.deleteLocalAirQualityHere()
                airQualityRepository.insertLocalAirQuality(airQualityResponse)
            }
            is ApiErrorResponse -> _errorMessage.value = response.error
        }
    }
}