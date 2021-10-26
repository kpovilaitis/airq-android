package lt.kepo.airquality.list.search

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import lt.kepo.airquality.list.AirQualitiesListItem
import lt.kepo.airqualitydata.AirQualitiesRepository
import lt.kepo.airqualitydata.AirQualityListItem
import lt.kepo.airqualitydata.search.SearchAirQualitiesUseCase
import lt.kepo.core.Event
import lt.kepo.core.addSource
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SearchStationsViewModel @Inject constructor(
    private val searchAirQualitiesUseCase: SearchAirQualitiesUseCase,
    private val airQualitiesRepository: AirQualitiesRepository,
    @Named("application") private val applicationScope: CoroutineScope,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _showError = MutableStateFlow<Event<Error>?>(null)
        .addSource(viewModelScope, airQualitiesRepository.error) { error ->
            if (error is AirQualitiesRepository.Error.Add) {
                value = Event(Error.Add)
            }
        }
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
                    _showError.value = Event(Error.Search)
                    emptyList()
                }
            }
        }
    val stations: Flow<List<AirQualitiesListItem>> = searchResults
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
        }
    val query: Flow<String> = _query
    val isProgressVisible: Flow<Boolean> = airQualitiesRepository.isLoading
    val isNoResultsVisible: Flow<Boolean> = _query
        .combine(searchResults) { query, results ->
            query.isNotEmpty() && results.isEmpty()
        }
    val isClearActionVisible: Flow<Boolean> = _query
        .map { text ->
            text.isNotEmpty()
        }
    val showError: Flow<Event<Error>?> = _showError

    fun onQueryEntered(query: String) {
        _query.value = query
    }

    fun clearSearch() {
        _query.value = ""
    }

    fun add(stationId: Int) {
        applicationScope.launch {
            airQualitiesRepository.add(stationId)
        }
    }

    sealed class Error {

        object Search : Error()

        object Add : Error()
    }
}

private fun AirQualityListItem.toListItem(): AirQualitiesListItem =
    AirQualitiesListItem(
        stationId = stationId,
        address = address,
        index = index,
        isCurrentLocation = false,
    )
