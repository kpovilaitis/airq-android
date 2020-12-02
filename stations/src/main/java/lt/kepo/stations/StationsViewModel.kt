package lt.kepo.stations

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.android.synthetic.main.fragment_stations.*
import kotlinx.coroutines.*

class StationsViewModel @ViewModelInject constructor(
    private val stationsRepository: StationsRepository
) : ViewModel() {

    private val _isProgressVisible = MutableLiveData(false)
    private val _query = MutableLiveData("")
    private val _error = MutableLiveData<Error>(null)

    val stations: LiveData<List<Station>> = stationsRepository.stations.asLiveData(viewModelScope.coroutineContext)
    val isProgressVisible: LiveData<Boolean> = _isProgressVisible
    val error: LiveData<Error> = _error
    val isTypingHintVisible: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        value = true
        addSource(stations) { value = it.isEmpty() && _query.value?.isEmpty() == true }
        addSource(_query) { value = it.isEmpty() && stations.value?.isEmpty() == true }
    }
    val isNoResultVisible: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        value = false
        addSource(stations) { value = it.isEmpty() && _query.value?.isNotEmpty() == true }
        addSource(_query) { value = it.isNotEmpty() && stations.value?.isEmpty() == true }
    }

    private var getStationsJob: Job? = null

    fun getStations(query: String) {
        getStationsJob = viewModelScope.launch {
            _query.value = query
            _isProgressVisible.value = true
            _error.value = null

            when (stationsRepository.search(query)) {
                is StationsRepository.SearchResult.Error -> _error.value = Error.GetStations
            }

            _isProgressVisible.value = false
        }
    }

    fun clearStations() {
        getStationsJob?.cancel()
        _isProgressVisible.value = false
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