package lt.kepo.stations.ui

import androidx.lifecycle.*
import kotlinx.coroutines.*
import lt.kepo.core.model.Station
import lt.kepo.stations.repository.StationsRepository

class StationsViewModel(private val stationsRepository: StationsRepository) : ViewModel() {
    private val _stations = MutableLiveData<MutableList<Station>>(mutableListOf())
    private val _isLoading = MutableLiveData<Boolean>(false)

    val stations: LiveData<MutableList<Station>> get() = _stations
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var getStationsJob: Job? = null

    fun getRemoteStations(query: String) {
        getStationsJob = viewModelScope.launch {
            _isLoading.value = true

            when (val response = stationsRepository.getRemoteStations(query)) {
                is lt.kepo.core.network.ApiSuccessResponse -> _stations.value = response.data
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

            stationsRepository.addAirQuality(station.id)

            _isLoading.value = false
        }
    }
}