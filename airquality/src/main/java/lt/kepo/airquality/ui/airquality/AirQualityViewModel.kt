package lt.kepo.airquality.ui.airquality

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import lt.kepo.airquality.domain.UpdateAirQualitiesUseCase
import lt.kepo.core.network.ApiErrorResponse
import lt.kepo.core.network.ApiSuccessResponse
import lt.kepo.core.model.AirQuality
import lt.kepo.airquality.repository.AirQualityRepository
import lt.kepo.core.constants.AIR_QUALITY_HERE_STATION_ID

class AirQualityViewModel(
    initAirQuality: AirQuality,
    application: Application,
    airQualityRepository: AirQualityRepository,
    locationClient: FusedLocationProviderClient,
    private val updateAirQualitiesUseCase: UpdateAirQualitiesUseCase
) : lt.kepo.airquality.ui.BaseAirQualityViewModel(application, locationClient, airQualityRepository) {
    val airQuality: LiveData<AirQuality> = airQualityRepository.getCachedAirQuality(initAirQuality.stationId)

    fun removeAirQuality() {
        viewModelScope.launch {
            airQuality.value?.let { airQualityRepository.deleteCachedAirQuality(it.stationId) }
        }
    }

    fun updateCachedAirQuality(force: Boolean = false) {
        if (airQuality.value?.stationId == AIR_QUALITY_HERE_STATION_ID)
            updateCachedAirQualityHere(force, airQuality.value)
        else {
            viewModelScope.launch {
                _isLoading.value = true

                airQuality.value?.let {
                    when (val result = updateAirQualitiesUseCase(force, listOf(it))) {
                        is ApiSuccessResponse -> _errorMessage.value = null
                        is ApiErrorResponse<*> -> _errorMessage.value = result.error
                    }
                }

                _isLoading.value = false
            }
        }
    }
}