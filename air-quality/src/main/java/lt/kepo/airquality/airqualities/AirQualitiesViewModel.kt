package lt.kepo.airquality.airqualities

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lt.kepo.airquality.AirQualitiesRepository
import lt.kepo.airquality.AirQuality
import lt.kepo.airquality.IsAirQualitiesExpired
import javax.inject.Inject

@HiltViewModel
class AirQualitiesViewModel @Inject constructor(
    private val airQualitiesRepository: AirQualitiesRepository,
    private val isAirQualitiesExpired: IsAirQualitiesExpired
) : ViewModel() {

    private val _isProgressVisible = MutableStateFlow(false)
    private val _error = MutableStateFlow<Error?>(null)

    val isProgressVisible: StateFlow<Boolean> = _isProgressVisible
    val error: StateFlow<Error?> = _error
    val airQualities: StateFlow<List<AirQualitiesListItem>> = airQualitiesRepository
        .getAll()
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

    fun refreshAirQualities(isForced: Boolean) {
        viewModelScope.launch {
            _isProgressVisible.value = true
            _error.value = null

            if (isForced || isAirQualitiesExpired()) {
                airQualitiesRepository
                    .refresh()
                    .let { refreshResult ->
                        when (refreshResult) {
                            is AirQualitiesRepository.RefreshResult.Error -> {
                                _error.value = Error.RefreshAirQualities
                            }
                        }
                    }
            }

            _isProgressVisible.value = false
        }
    }

    sealed class Error {

        object RefreshAirQualities : Error()
    }
}

private fun AirQuality.toListItem(): AirQualitiesListItem =
    AirQualitiesListItem(
        stationId = stationId,
        primaryAddress = primaryAddress,
        secondaryAddress = secondaryAddress,
        airQualityIndex = airQualityIndex,
    )
