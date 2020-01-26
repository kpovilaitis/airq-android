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
import lt.kepo.airquality.ui.BaseAirQualityViewModel
import lt.kepo.core.constants.AIR_QUALITY_HERE_STATION_ID

class AirQualityViewModel(
    initAirQuality: AirQuality,
    application: Application,
    airQualityRepository: AirQualityRepository,
    locationClient: FusedLocationProviderClient,
    private val updateAirQualitiesUseCase: UpdateAirQualitiesUseCase
) : BaseAirQualityViewModel(application, locationClient, airQualityRepository) {
    val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading
    val airQuality: LiveData<AirQuality> = airQualityRepository.getCachedAirQuality(initAirQuality.stationId)

    fun removeAirQuality() {
        viewModelScope.launch {
            airQuality.value?.let { airQualityRepository.deleteCachedAirQuality(it.stationId) }
        }
    }

    fun updateCachedAirQuality(force: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true

            if (airQuality.value?.stationId == AIR_QUALITY_HERE_STATION_ID)
                updateCachedAirQualityHere(
                    force = force,
                    airQualityHere = airQuality.value
                )
            else {
                airQuality.value?.let {
                    when (val result = updateAirQualitiesUseCase(force, listOf(it))) {
                        is ApiSuccessResponse -> _errorMessage.value = null
                        is ApiErrorResponse<*> -> _errorMessage.value = result.error
                    }
                }
            }

            _isLoading.value = false
        }
    }
}