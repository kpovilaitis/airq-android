package lt.kepo.airquality.airqualitydetails

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import lt.kepo.airquality.AirQualitiesRepository
import lt.kepo.airquality.AirQuality
import lt.kepo.airquality.IsAirQualitiesExpired
import javax.inject.Inject

@HiltViewModel
class AirQualityDetailsViewModel @Inject constructor(
    private val airQualitiesRepository: AirQualitiesRepository,
    private val isAirQualitiesExpired: IsAirQualitiesExpired,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _isProgressVisible = MutableLiveData(false)
    private val _error = MutableLiveData<Error>(null)
    private val airQualityStationId: Int = requireNotNull(savedStateHandle.get("station_id"))

    val isProgressVisible: LiveData<Boolean> = _isProgressVisible
    val errorMessage: LiveData<Error> = _error
    val airQuality: LiveData<AirQuality> = airQualitiesRepository
        .getAirQuality(airQualityStationId)
        .mapNotNull { it }
        .asLiveData(viewModelScope.coroutineContext)
    val isRemoveAirQualityVisible: LiveData<Boolean> = airQuality.map { airQuality ->
        airQuality.isCurrentLocationQuality.not()
    }

    fun refreshAirQuality(isForced: Boolean) {
        viewModelScope.launch {
            _isProgressVisible.value = true
            _error.value = null

            if (isForced || isAirQualitiesExpired()) {
                airQualitiesRepository
                    .refresh()
                    .let { refreshResult ->
                        when (refreshResult) {
                            is AirQualitiesRepository.RefreshResult.Error -> {
                                _error.value = Error.RefreshAirQuality
                            }
                        }
                    }
            }

            _isProgressVisible.value = false
        }
    }

    fun removeAirQuality() {
        viewModelScope.launch {
            airQualitiesRepository.remove(airQualityStationId)
        }
    }

    sealed class Error {

        object RefreshAirQuality : Error()
    }
}