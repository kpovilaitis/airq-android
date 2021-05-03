package lt.kepo.stations

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class StationsViewModel @Inject constructor(
    private val stationsRepository: StationsRepository
) : ViewModel() {

    private val _isProgressVisible = MutableLiveData(false)
    private val _isNothingFoundVisible = MutableLiveData(false)
    private val _error = MutableLiveData<Error>(null)

    val stations: LiveData<List<Station>> = stationsRepository.stations
        .asLiveData(viewModelScope.coroutineContext)
        .map { it.take(7) }
    val isProgressVisible: LiveData<Boolean> = _isProgressVisible
    val error: LiveData<Error> = _error
    val isNothingFoundVisible: LiveData<Boolean> = _isNothingFoundVisible

    fun getStations(query: String) {
        if (query.isBlank() || query.length < 2) {
            return
        }
        viewModelScope.launch {
            when (stationsRepository.search(query)) {
                is StationsRepository.SearchResult.Success -> {
                    _isNothingFoundVisible.value = false
                    _error.value = null
                }
                is StationsRepository.SearchResult.NothingFound -> {
                    _isNothingFoundVisible.value = true
                    _error.value = null
                }
                is StationsRepository.SearchResult.Error -> {
                    _isNothingFoundVisible.value = false
                    _error.value = Error.GetStations
                }
            }
        }
    }

    fun clearSearch() {
        _error.value = null
        _isNothingFoundVisible.value = false
        viewModelScope.launch {
            stationsRepository.clear()
        }
    }

    fun addStation(stationId: Int) {
        viewModelScope.launch {
            _isProgressVisible.value = true
            _error.value = null

            when (stationsRepository.save(stationId)) {
                is StationsRepository.SaveResult.Error -> _error.value = Error.AddStation
            }

            _isProgressVisible.value = false
        }
    }

    sealed class Error {

        object GetStations : Error()

        object AddStation : Error()
    }
}