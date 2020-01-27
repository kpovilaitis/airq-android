package lt.kepo.stations.ui

import androidx.lifecycle.*
import kotlinx.coroutines.*
import lt.kepo.core.model.Station
import lt.kepo.core.network.ApiErrorResponse
import lt.kepo.core.network.ApiSuccessResponse
import lt.kepo.stations.repository.StationsRepository

class StationsViewModel(private val stationsRepository: StationsRepository) : ViewModel() {
    private val _stations = MutableLiveData<MutableList<Station>>(mutableListOf())
    private val _isLoading = MutableLiveData<Boolean>(false)
    private val _errorMessage = MutableLiveData<String>()

    val stations: LiveData<MutableList<Station>> get() = _stations
    val isLoading: LiveData<Boolean> get() = _isLoading
    val errorMessage: LiveData<String> get() = _errorMessage

    private var getStationsJob: Job? = null

    fun getRemoteStations(query: String) {
        getStationsJob = viewModelScope.launch {
            _isLoading.value = true

            when (val response = stationsRepository.getRemoteStations(query)) {
                is ApiSuccessResponse -> _stations.value = response.data
                is ApiErrorResponse -> _errorMessage.value = response.error
            }

            _isLoading.value = false
        }
    }

    fun clearStations() {
        getStationsJob?.cancel()
        _stations.value = mutableListOf()
    }

    fun addStationAirQuality(station: Station) {
        viewModelScope.launch {
            _isLoading.value = true

            when (val response = stationsRepository.addAirQuality(station.id)) {
                is ApiSuccessResponse -> {
                    _stations.value?.remove(station)
                    _stations.value = _stations.value
                }
                is ApiErrorResponse -> _errorMessage.value = response.error
            }

            _isLoading.value = false
        }
    }
}