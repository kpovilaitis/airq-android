package lt.kepo.airq.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.*
import lt.kepo.airq.data.api.ApiSuccessResponse
import lt.kepo.airq.data.model.Station
import lt.kepo.airq.data.repository.stations.StationsRepository

class StationsViewModel(private val stationsRepository: StationsRepository) : ViewModel() {
    private val _stations = MutableLiveData<MutableList<Station>>()

    val stations: LiveData<MutableList<Station>> get() = _stations

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }

    fun getRemoteStations(query: String) {
        viewModelScope.launch {
            when (val response = stationsRepository.getRemoteStations(query)) {
                is ApiSuccessResponse -> _stations.value = response.data
            }
        }
    }

    fun updateLocalStations() {
        viewModelScope.launch {
            val localStations = stationsRepository.getLocalAllStations()

            localStations.forEach { station ->
                when (val response = stationsRepository.getRemoteStation(station.id)) {
                    is ApiSuccessResponse -> {
                        val responseStation = response.data

                        station.airQualityIndex = responseStation.airQualityIndex

                        stationsRepository.updateLocalStation(station)

                        _stations.value = localStations
                    }
                }
            }
        }
    }

    fun addLocalStation(station: Station) {
        viewModelScope.launch { stationsRepository.upsertLocalStation(station) }
    }

    fun getLocalAllStations() {
        viewModelScope.launch { _stations.value = stationsRepository.getLocalAllStations() }
    }

    fun removeLocalStation(station: Station) {
        viewModelScope.launch { stationsRepository.deleteLocalStation(station) }
    }
}