package lt.kepo.airquality.ui.airqualities

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import lt.kepo.airquality.domain.UpdateAirQualitiesUseCase
import lt.kepo.core.constants.AIR_QUALITY_HERE_STATION_ID

class AirQualitiesViewModel(
    application: Application,
    airQualityRepository: lt.kepo.airquality.repository.AirQualityRepository,
    locationClient: FusedLocationProviderClient,
    private val updateAirQualitiesUseCase: UpdateAirQualitiesUseCase
) : lt.kepo.airquality.ui.BaseAirQualityViewModel(application, locationClient, airQualityRepository) {
    val airQualities: LiveData<List<lt.kepo.core.model.AirQuality>> = airQualityRepository.getCachedAirQualitiesLive()

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
                        is lt.kepo.core.network.ApiSuccessResponse -> _errorMessage.value = null
                        is lt.kepo.core.network.ApiErrorResponse<*> -> _errorMessage.value = result.error
                    }
                }

            _isLoading.value = false
        }
    }
}