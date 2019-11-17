package lt.kepo.airq.ui.viewmodel

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.coroutines.*
import lt.kepo.airq.data.api.ApiSuccessResponse
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.repository.airquality.AirQualityRepository
import lt.kepo.airq.utility.isLocationEnabled

class AirQualitiesViewModel(
    private val airQualityRepository: AirQualityRepository,
    private val context: Application
) : AndroidViewModel(context) {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val _airQualities = MutableLiveData<MutableList<AirQuality>>()
    val airQualities: LiveData<MutableList<AirQuality>> get() = _airQualities

    init {
        if (canUseLocation(context))
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        getLocalAirQualities()
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }

    fun getLocalAirQualities() {
        viewModelScope.launch { updateLocalAirQualities(airQualityRepository.getLocalAirQualities().toMutableList()) }
    }

    private suspend fun updateLocalAirQualities(qualities: MutableList<AirQuality>) {
        _airQualities.value = qualities

        when {
            _airQualities.value == null -> return
            _airQualities.value!!.isEmpty() -> getLocation()
            else -> _airQualities.value?.forEach { airQuality ->
                when (airQuality.isCurrentLocationQuality) {
                    true -> getLocation()
                    false -> viewModelScope.launch { updateLocalAirQuality(airQuality) }
                }
            }
        }
    }

    private suspend fun updateLocalAirQuality(quality: AirQuality) {
        when (val response = airQualityRepository.getRemoteAirQuality(quality.stationId)) {
            is ApiSuccessResponse -> {
                val fetchedAirQuality = response.data

                _airQualities.value?.find { it.stationId == quality.stationId } .also { it?.update(fetchedAirQuality) }
                _airQualities.value = _airQualities.value

                airQualityRepository.upsertLocalAirQuality(fetchedAirQuality)
            }
        }
    }

    private fun getLocation() {
        if (canUseLocation(context)) {
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

    private suspend fun updateLocalAirQualityHere(location: Location?) {
        when (val response = airQualityRepository.getRemoteAirQualityHere(location)) {
            is ApiSuccessResponse -> {
                val airQualityResponse = response.data

                airQualityResponse.isCurrentLocationQuality = true

                _airQualities.value?.find { it.isCurrentLocationQuality } .also { it?.update(airQualityResponse) }
                _airQualities.value = _airQualities.value

                airQualityRepository.deleteLocalAirQualityHere()
                airQualityRepository.insertLocalAirQuality(airQualityResponse)
            }
        }
    }

    private fun canUseLocation(context: Context): Boolean {
        return if (isLocationEnabled(context)) {
            if (!::fusedLocationClient.isInitialized)
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            true
        } else
            false
    }
}