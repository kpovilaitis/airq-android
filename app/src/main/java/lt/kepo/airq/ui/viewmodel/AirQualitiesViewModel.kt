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
import lt.kepo.airq.utility.AIR_QUALITY_HERE_STATION_ID

class AirQualitiesViewModel(
    application: Application,
    airQualityRepository: AirQualityRepository,
    locationClient: FusedLocationProviderClient,
    private val updateAirQualitiesUseCase: UpdateAirQualitiesUseCase
) : BaseAirQualityViewModel(application, locationClient, airQualityRepository) {
    val airQualities: LiveData<List<AirQuality>> = airQualityRepository.getCachedAirQualitiesLive()

    fun updateCachedAirQualities(force: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true

            val cachedQualities = if (airQualities.value == null) {
                airQualityRepository.getCachedAirQualities()
            } else {
                airQualities.value!!
            }

            updateCachedAirQualityHere(
                force = force,
                airQualityHere = cachedQualities.singleOrNull { it.stationId == AIR_QUALITY_HERE_STATION_ID }
            )

            cachedQualities
                .filter { it.stationId != AIR_QUALITY_HERE_STATION_ID }
                .let {
                    when (val result = updateAirQualitiesUseCase(force, it)) {
                        is ApiSuccessResponse -> _errorMessage.value = null
                        is ApiErrorResponse<*> -> _errorMessage.value = result.error
                    }
                }

            _isLoading.value = false
        }
    }
}