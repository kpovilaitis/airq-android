package lt.kepo.airq.ui.viewmodel

import android.app.Application
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
    initAirQuality: AirQuality,
    airQualityRepository: AirQualityRepository,
    context: Application
) : BaseAirQualityViewModel(airQualityRepository, context) {
    private val _airQuality = MutableLiveData<AirQuality>(initAirQuality)
    private val _errorMessage = MutableLiveData<String>()

    val airQuality: LiveData<AirQuality> get() = _airQuality
    val errorMessage: LiveData<String> get() = _errorMessage

    fun removeAirQuality() {
        viewModelScope.launch { airQualityRepository.deleteLocalAirQuality(airQuality.value!!.stationId) }
    }

    fun updateAirQuality() {
        if (airQuality.value != null) {
            if (airQuality.value!!.isCurrentLocationQuality)
                fetchLocationAirQuality()
            else
                viewModelScope.launch { fetchAirQuality(airQuality.value!!) }
        }
    }

    private suspend fun fetchAirQuality(airQuality : AirQuality) {
        when (val response = airQualityRepository.getRemoteAirQuality(airQuality.stationId)) {
            is ApiSuccessResponse -> {
                val fetchedAirQuality = response.data

                _airQuality.value?.update(fetchedAirQuality)
                _airQuality.value = _airQuality.value

                airQualityRepository.upsertLocalAirQuality(fetchedAirQuality)
            }
        }
    }

    override suspend fun updateLocalAirQualityHere(location: Location?) {
        _errorMessage.value = ""

        viewModelScope.launch {
            when (val response = airQualityRepository.getRemoteAirQualityHere(location)) {
                is ApiSuccessResponse -> {
                    val fetchedAirQuality = response.data

                    fetchedAirQuality.isCurrentLocationQuality = true

                    airQualityRepository.insertLocalAirQuality(fetchedAirQuality)
                }
                is ApiErrorResponse -> _errorMessage.value = response.errorMessage
            }
        }
    }
}