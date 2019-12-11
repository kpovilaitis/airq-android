package lt.kepo.airq.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.*
import lt.kepo.airq.data.api.ApiSuccessResponse
import lt.kepo.airq.data.model.Station
import lt.kepo.airq.data.repository.airquality.AirQualityRepository
import lt.kepo.airq.data.repository.stations.StationsRepository

class StationsViewModel(
    private val airQualityRepository: AirQualityRepository,
    private val stationsRepository: StationsRepository
) : ViewModel() {
    private val _stations = MutableLiveData<MutableList<Station>>(mutableListOf())
    private val _isLoading = MutableLiveData<Boolean>(false)

    val stations: LiveData<MutableList<Station>> get() = _stations
    val isLoading: LiveData<Boolean> get() = _isLoading

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

    fun clearStations() {
        _stations.value = arrayListOf()
    }

    fun addAirQuality(station: Station) {
        viewModelScope.launch {
            _isLoading.value = true

            when (val response = airQualityRepository.getRemoteAirQuality(station.id)) {
                is ApiSuccessResponse -> airQualityRepository.upsertLocalAirQuality(response.data)
            }

            _isLoading.value = false
        }
    }
}