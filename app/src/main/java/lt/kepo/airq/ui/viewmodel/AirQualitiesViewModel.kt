package lt.kepo.airq.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.*
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.repository.airquality.AirQualityRepository

class AirQualitiesViewModel(
    application: Application,
    airQualityRepository: AirQualityRepository,
    locationClient: FusedLocationProviderClient
) : BaseAirQualityViewModel(application, airQualityRepository, locationClient) {
    val airQualities: LiveData<List<AirQuality>> = airQualityRepository.getLocalAirQualities()
    val airQualityHere: LiveData<AirQuality> = airQualityRepository.getLocalAirQualityHere()

    fun updateLocalAirQualities() {
        viewModelScope.launch {airQualities.value?.forEach { updateLocalAirQuality(it) } }
    }

    fun updateLocalAirQualityHere() {
        viewModelScope.launch { fetchLocationAirQuality() }
    }
}