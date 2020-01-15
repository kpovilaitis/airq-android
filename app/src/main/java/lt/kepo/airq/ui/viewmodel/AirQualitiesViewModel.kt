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

class AirQualitiesViewModel(
    application: Application,
    airQualityRepository: AirQualityRepository,
    locationClient: FusedLocationProviderClient,
    private val updateAirQualitiesUseCase: UpdateAirQualitiesUseCase,
    updateAirQualityHereUseCase: UpdateAirQualityHereUseCase
) : BaseAirQualityViewModel(application, locationClient, updateAirQualityHereUseCase) {
    val airQualities: LiveData<List<AirQuality>> = airQualityRepository.getCachedAirQualities()
    val airQualityHere: LiveData<AirQuality> = airQualityRepository.getCachedAirQualityHere()

    fun updateLocalAirQualities(force: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true

            airQualities.value?.let {
                when (val result = updateAirQualitiesUseCase(force, it)) {
                    is ApiSuccessResponse -> _errorMessage.value = null
                    is ApiErrorResponse<*> -> _errorMessage.value = result.error
                }
            }

            _isLoading.value = false
        }
    }

    fun updateLocalAirQualityHere(force: Boolean = false) {
        viewModelScope.launch {
            updateLocalAirQualityHere(force, airQualityHere.value)
        }
    }
}