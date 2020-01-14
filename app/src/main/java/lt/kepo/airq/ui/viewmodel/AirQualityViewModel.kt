package lt.kepo.airq.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.*
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.repository.airquality.AirQualityRepository

class AirQualityViewModel(
    initAirQuality: AirQuality,
    application: Application,
    airQualityRepository: AirQualityRepository,
    locationClient: FusedLocationProviderClient
) : BaseAirQualityViewModel(application, airQualityRepository, locationClient) {
    val airQuality: LiveData<AirQuality> = airQualityRepository.getLocalAirQuality(initAirQuality.stationId)

    fun removeAirQuality() {
        viewModelScope.launch { airQualityRepository.deleteLocalAirQuality(airQuality.value!!.stationId) }
    }

    fun updateAirQuality() {
        if (airQuality.value?.isCurrentLocationQuality == true)
            fetchLocationAirQuality()
        else
            viewModelScope.launch { updateLocalAirQuality(airQuality.value!!) }
    }
}