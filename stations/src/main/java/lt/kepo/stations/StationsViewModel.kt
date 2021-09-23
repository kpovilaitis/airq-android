package lt.kepo.stations

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class StationsViewModel @Inject constructor(
    private val searchStationsUseCase: SearchStationsUseCase,
    private val saveStationUseCase: SaveStationUseCase,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _isProgressVisible = MutableStateFlow(false)
    private val _error = MutableStateFlow<Error?>(null)
    private val searchResults = _query
        .filterNot { query ->
            query.isBlank() || query.length < 2
        }.debounce(400L)
        .mapLatest { query ->
            searchStationsUseCase(query)
        }.map { result ->
            when (result) {
                is SearchStationsUseCase.Result.Success -> {
                    result.stations
                }
                is SearchStationsUseCase.Result.Error -> {
                    _error.value = Error.GetStations
                    emptyList()
                }
            }
        }

    val stations: StateFlow<List<StationsListItem>> = searchResults
        .map { stations ->
            stations.map { station ->
                station.toListItem()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList(),
        )
    val query: StateFlow<String> = _query
    val isProgressVisible: StateFlow<Boolean> = _isProgressVisible
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

    fun addStation(stationId: Int) {
        viewModelScope.launch {
            _isProgressVisible.value = true
            _error.value = null

            saveStationUseCase(stationId)
                .let { result ->
                    if (result is SaveStationUseCase.Result.Error) {
                        _error.value = Error.AddStation
                    }
                }

            _isProgressVisible.value = false
        }
    }

    sealed class Error {

        object GetStations : Error()

        object AddStation : Error()
    }
}

private fun Station.toListItem(): StationsListItem =
    StationsListItem(
        id = id,
        name = name,
        airQualityIndex = airQualityIndex,
    )
