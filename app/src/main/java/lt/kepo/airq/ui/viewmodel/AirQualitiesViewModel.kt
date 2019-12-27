package lt.kepo.airq.ui.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.*
import kotlinx.coroutines.*
import lt.kepo.airq.data.api.ApiErrorResponse
import lt.kepo.airq.data.api.ApiSuccessResponse
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.repository.airquality.AirQualityRepository

class AirQualitiesViewModel(
    airQualityRepository: AirQualityRepository,
    context: Application
) : BaseAirQualityViewModel(airQualityRepository, context) {
    private val _airQualities = MutableLiveData<MutableList<AirQuality>>()
    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String> get() = _errorMessage
    val airQualities: LiveData<MutableList<AirQuality>> get() = _airQualities

    init {
        updateLocalAirQualities()
    }

    fun getLocalAirQualities() {
        viewModelScope.launch { _airQualities.value = airQualityRepository.getLocalAirQualities().toMutableList() }
    }

    fun updateLocalAirQualities() {
        viewModelScope.launch {
            _airQualities.value = airQualityRepository.getLocalAirQualities().toMutableList()

            when {
                _airQualities.value == null -> return@launch
                _airQualities.value!!.isEmpty() -> fetchLocationAirQuality()
                else -> {
                    _airQualities.value!!.forEach { airQuality ->
                        when (airQuality.isCurrentLocationQuality) {
                            true -> fetchLocationAirQuality()
                            false -> viewModelScope.launch { updateLocalAirQuality(airQuality) }
                        }
                    }

                    if (_airQualities.value!!.none { it.isCurrentLocationQuality } ) {
                        fetchLocationAirQuality()
                    }
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
            is ApiErrorResponse -> _errorMessage.value = response.error
        }
    }

    override suspend fun updateLocalAirQualityHere(location: Location?) {
        when (val response = airQualityRepository.getRemoteAirQualityHere(location)) {
            is ApiSuccessResponse -> {
                val airQualityResponse = response.data

                airQualityResponse.isCurrentLocationQuality = true

                _airQualities.value?.find { it.isCurrentLocationQuality } .also {
                    if (it == null)
                        _airQualities.value!!.add(airQualityResponse)
                    else
                        it.update(airQualityResponse)
                }
                _airQualities.value = _airQualities.value

                airQualityRepository.deleteLocalAirQualityHere()
                airQualityRepository.insertLocalAirQuality(airQualityResponse)
            }
            is ApiErrorResponse -> _errorMessage.value = response.error
        }
    }
}