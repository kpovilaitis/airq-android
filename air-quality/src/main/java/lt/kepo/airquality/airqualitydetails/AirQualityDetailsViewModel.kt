package lt.kepo.airquality.airqualitydetails

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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

    private val _isProgressVisible = MutableStateFlow(false)
    private val _error = MutableStateFlow<Error?>(null)
    private val airQualityStationId: Int = requireNotNull(savedStateHandle.get("stationId"))
    private val airQuality: Flow<AirQuality?> = airQualitiesRepository
        .getAirQuality(airQualityStationId)

    val isProgressVisible: StateFlow<Boolean> = _isProgressVisible
    val errorMessage: StateFlow<Error?> = _error
    val stationName: StateFlow<String> = airQuality
        .map { it?.primaryAddress ?: "" }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = "",
        )
    val index: StateFlow<String> = airQuality
        .map { it?.airQualityIndex ?: "" }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = "",
        )
    val sulfurOxide: StateFlow<Double> = airQuality
        .map { it?.sulfurOxide ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = 0.0,
        )
    val ozone: StateFlow<Double> = airQuality
        .map { it?.ozone ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = 0.0,
        )
    val particle10: StateFlow<Double> = airQuality
        .map { it?.particle10 ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = 0.0,
        )
    val particle25: StateFlow<Double> = airQuality
        .map { it?.particle25 ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = 0.0,
        )
    val isCurrentLocationLabelVisible: StateFlow<Boolean> = airQuality
        .map { airQuality ->
            airQuality?.isCurrentLocationQuality == true
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )
    val isRemoveAirQualityVisible: StateFlow<Boolean> = airQuality
        .map { airQuality ->
            airQuality?.isCurrentLocationQuality?.not() == true
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )

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