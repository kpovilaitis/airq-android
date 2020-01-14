package lt.kepo.airq.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.*
import lt.kepo.airq.data.api.ApiErrorResponse
import lt.kepo.airq.data.api.ApiSuccessResponse
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.repository.airquality.AirQualityRepository
import lt.kepo.airq.domain.UpdateAirQualitiesUseCase
import lt.kepo.airq.domain.UpdateAirQualityHereUseCase

class AirQualityViewModel(
    initAirQuality: AirQuality,
    application: Application,
    private val airQualityRepository: AirQualityRepository,
    locationClient: FusedLocationProviderClient,
    private val updateAirQualitiesUseCase: UpdateAirQualitiesUseCase,
    updateAirQualityHereUseCase: UpdateAirQualityHereUseCase
) : BaseAirQualityViewModel(application, locationClient, updateAirQualityHereUseCase) {
    val airQuality: LiveData<AirQuality> = airQualityRepository.getLocalAirQuality(initAirQuality.stationId)

    fun removeAirQuality() {
        viewModelScope.launch {
            airQuality.value?.let { airQualityRepository.deleteLocalAirQuality(it.stationId) }
        }
    }

    fun updateAirQuality(force: Boolean = false) {
        if (airQuality.value?.isCurrentLocationQuality == true)
            updateLocalAirQualityHere(force, airQuality.value)
        else {
            _isLoading.value = true

            viewModelScope.launch {
                airQuality.value?.let {
                    when (val result = updateAirQualitiesUseCase(force, listOf(it))) {
                        is ApiSuccessResponse -> _isLoading.value = false
                        is ApiErrorResponse<*> -> {
                            _isLoading.value = false
                            _errorMessage.value = result.error
                        }
                    }
                }
            }
        }
    }
}