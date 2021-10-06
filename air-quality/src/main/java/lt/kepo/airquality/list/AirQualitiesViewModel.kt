package lt.kepo.airquality.list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lt.kepo.airqualitydata.AirQualitiesRepository
import lt.kepo.airqualitydata.AirQualityDetails
import lt.kepo.airqualitydata.AirQualityListItem
import javax.inject.Inject

@HiltViewModel
class AirQualitiesViewModel @Inject constructor(
    private val airQualitiesRepository: AirQualitiesRepository,
) : ViewModel() {

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
    val airQualities: StateFlow<List<AirQualitiesListItem>> = airQualitiesRepository
        .airQualities
        .map { airQualities ->
            airQualities.sortedByDescending { airQuality ->
                airQuality.isCurrentLocationQuality
            }.map { airQuality ->
                airQuality.toListItem()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList(),
        )

    fun refresh() {
        viewModelScope.launch {
            airQualitiesRepository.refresh()
        }
    }
}

private fun AirQualityListItem.toListItem(): AirQualitiesListItem =
    AirQualitiesListItem(
        stationId = stationId,
        primaryAddress = address,
        secondaryAddress = "",
        airQualityIndex = airQualityIndex,
    )
