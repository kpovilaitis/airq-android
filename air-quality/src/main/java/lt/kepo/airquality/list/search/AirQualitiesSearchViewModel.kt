package lt.kepo.airquality.list.search

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import lt.kepo.airquality.list.AirQualitiesListItem
import lt.kepo.airqualitydata.AirQualitiesRepository
import lt.kepo.airqualitydata.AirQualityListItem
import lt.kepo.airqualitydata.search.SearchAirQualitiesUseCase
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SearchStationsViewModel @Inject constructor(
    private val searchAirQualitiesUseCase: SearchAirQualitiesUseCase,
    private val airQualitiesRepository: AirQualitiesRepository,
    @Named("application") private val applicationScope: CoroutineScope,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _error = MutableStateFlow<Error?>(null)
    private val searchResults = _query
        .filterNot { query ->
            query.isBlank() || query.length < 2
        }.debounce(400L)
        .mapLatest { query ->
            searchAirQualitiesUseCase(query)
        }.map { result ->
            when (result) {
                is SearchAirQualitiesUseCase.Result.Success -> {
                    result.airQualities
                }
                is SearchAirQualitiesUseCase.Result.Error -> {
                    _error.value = Error.GetStations
                    emptyList()
                }
            }
        }

    val stations: StateFlow<List<AirQualitiesListItem>> = searchResults
        .combine(airQualitiesRepository.airQualities) { results, qualities ->
            results.filterNot { airQuality ->
                qualities
                    .map { it.stationId }
                    .contains(airQuality.stationId)
            }
        }.map { stations ->
            stations.map { station ->
                station.toListItem()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList(),
        )
    val query: StateFlow<String> = _query
    val isProgressVisible: StateFlow<Boolean> = airQualitiesRepository.loadState
        .map { loadState ->
            loadState is AirQualitiesRepository.LoadState.Loading
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )
    val error: StateFlow<Error?> = _error
    val isNothingFoundVisible: StateFlow<Boolean> = _query
        .combine(searchResults) { query, results ->
            query.isNotEmpty() && results.isEmpty()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )
    val isClearActionVisible: StateFlow<Boolean> = _query
        .map { text ->
            text.isNotEmpty()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )

    fun onQueryEntered(query: String) {
        _query.value = query
    }

    fun clearSearch() {
        _query.value = ""
    }

    fun add(stationId: Int) {
        applicationScope.launch {
            airQualitiesRepository.add(stationId)
            _error.value = null

//            saveStationUseCase(stationId)
//                .let { result ->
//                    if (result is AddAirQualityUseCase.Result.Error) {
//                        _error.value = Error.AddStation
//                    } else {
//
//                    }
//                }
        }
    }

    sealed class Error {

        object GetStations : Error()

        object AddStation : Error()
    }
}

private fun AirQualityListItem.toListItem(): AirQualitiesListItem =
    AirQualitiesListItem(
        stationId = stationId,
        primaryAddress = address,
        secondaryAddress = "",
        airQualityIndex = airQualityIndex,
    )
