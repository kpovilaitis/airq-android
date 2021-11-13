package lt.kepo.airquality.details

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lt.kepo.airqualitydata.AirQualitiesRepository
import lt.kepo.airqualitydata.AirQualityDetails
import lt.kepo.core.SimpleEvent
import javax.inject.Inject

@HiltViewModel
class AirQualityDetailsViewModel @Inject constructor(
    private val airQualitiesRepository: AirQualitiesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val airQualityStationId: Int = requireNotNull(savedStateHandle.get("stationId"))
    private val airQuality: Flow<AirQualityDetails?> = airQualitiesRepository
        .getAirQuality(airQualityStationId)

    val isProgressVisible: Flow<Boolean> = airQualitiesRepository.isLoading
    val showError: Flow<SimpleEvent> = airQualitiesRepository.error.transform { error ->
        if (error is AirQualitiesRepository.Error.Refresh) {
            emit(SimpleEvent())
        }
    }
    val stationName: Flow<String> = airQuality.map { airQuality ->
        airQuality?.address ?: ""
    }
    val index: Flow<String> = airQuality.map { airQuality ->
        airQuality?.airQualityIndex ?: ""
    }
    val sulfurOxide: Flow<Double?> = airQuality.map { airQuality ->
        airQuality?.sulfurOxide
    }
    val ozone: Flow<Double?> = airQuality.map {  airQuality ->
        airQuality?.ozone
    }
    val particle10: Flow<Double?> = airQuality.map { airQuality ->
        airQuality?.particle10
    }
    val particle25: Flow<Double?> = airQuality.map { airQuality ->
        airQuality?.particle25
    }
    val isCurrentLocationLabelVisible: Flow<Boolean> = airQuality.map { airQuality ->
        airQuality?.isCurrentLocationQuality == true
    }
    val isRemoveAirQualityVisible: Flow<Boolean> = airQuality.map {  airQuality ->
        airQuality?.isCurrentLocationQuality?.not() == true
    }

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