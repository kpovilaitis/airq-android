package lt.kepo.airquality.ui.airqualities

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import lt.kepo.airquality.domain.UpdateAirQualitiesUseCase
import lt.kepo.airquality.repository.AirQualityRepository
import lt.kepo.airquality.ui.BaseAirQualityViewModel
import lt.kepo.core.constants.AIR_QUALITY_HERE_STATION_ID
import lt.kepo.core.model.AirQuality
import lt.kepo.core.network.ApiErrorResponse
import lt.kepo.core.network.ApiSuccessResponse

class AirQualitiesViewModel(
    application: Application,
    airQualityRepository: AirQualityRepository,
    locationClient: FusedLocationProviderClient,
    private val updateAirQualitiesUseCase: UpdateAirQualitiesUseCase
) : BaseAirQualityViewModel(application, locationClient, airQualityRepository) {
    val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading
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

            if (cachedQualities.any()) {
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
}