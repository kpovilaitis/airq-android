package lt.kepo.airquality.list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lt.kepo.airqualitydata.AirQualitiesRepository
import lt.kepo.airqualitydata.AirQualityListItem
import lt.kepo.core.SimpleEvent
import javax.inject.Inject

@HiltViewModel
class AirQualitiesViewModel @Inject constructor(
    private val airQualitiesRepository: AirQualitiesRepository,
) : ViewModel() {

    val isProgressVisible: Flow<Boolean> = airQualitiesRepository.isLoading
    val showError: Flow<SimpleEvent> = airQualitiesRepository.error
        .filter { it is AirQualitiesRepository.Error.Refresh }
        .map { SimpleEvent() }

    val airQualities: Flow<List<AirQualitiesListItem>> = airQualitiesRepository.airQualities
        .map { airQualities ->
            airQualities.sortedByDescending { airQuality ->
                airQuality.isCurrentLocation
            }.map { airQuality ->
                airQuality.toListItem()
            }
        }

    fun refresh() {
        viewModelScope.launch {
            airQualitiesRepository.refresh()
        }
    }
}

private fun AirQualityListItem.toListItem(): AirQualitiesListItem =
    AirQualitiesListItem(
        stationId = stationId,
        address = address,
        index = index,
        isCurrentLocation = isCurrentLocation,
    )
