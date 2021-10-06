package lt.kepo.airquality.details

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lt.kepo.airqualitydata.AirQualitiesRepository
import lt.kepo.airqualitydata.AirQualityDetails
import javax.inject.Inject

@HiltViewModel
class AirQualityDetailsViewModel @Inject constructor(
    private val airQualitiesRepository: AirQualitiesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val airQualityStationId: Int = requireNotNull(savedStateHandle.get("stationId"))
    private val airQuality: Flow<AirQualityDetails?> = airQualitiesRepository
        .getAirQuality(airQualityStationId)

    val isProgressVisible: StateFlow<Boolean> = airQualitiesRepository.loadState
        .map { loadState ->
            loadState is AirQualitiesRepository.LoadState.Loading
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )
    val isErrorVisible: StateFlow<Boolean> = airQualitiesRepository.loadState
        .map { loadState ->
            loadState is AirQualitiesRepository.LoadState.Error
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )
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

    fun refresh() {
        viewModelScope.launch {
            airQualitiesRepository.refresh()
        }
    }

    fun remove() {
        viewModelScope.launch {
            airQualitiesRepository.remove(airQualityStationId)
        }
    }
}