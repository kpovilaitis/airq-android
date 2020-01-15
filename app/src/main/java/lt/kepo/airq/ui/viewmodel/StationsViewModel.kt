package lt.kepo.airq.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.*
import lt.kepo.airq.data.api.ApiSuccessResponse
import lt.kepo.airq.data.model.Station
import lt.kepo.airq.data.repository.airquality.AirQualityRepository
import lt.kepo.airq.domain.GetFilteredStationsUseCase

class StationsViewModel(
    private val airQualityRepository: AirQualityRepository,
    private val getStations: GetFilteredStationsUseCase
) : ViewModel() {
    private val _stations = MutableLiveData<MutableList<Station>>(mutableListOf())
    private val _isLoading = MutableLiveData<Boolean>(false)

    val stations: LiveData<MutableList<Station>> get() = _stations
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var getStationsJob: Job? = null

    fun getRemoteStations(query: String) {
        getStationsJob = viewModelScope.launch {
            _isLoading.value = true

            when (val response = getStations(query)) {
                is ApiSuccessResponse -> _stations.value = response.data
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

            airQualityRepository.addAirQualityWithTimestamp(station.id)

            _isLoading.value = false
        }
    }
}