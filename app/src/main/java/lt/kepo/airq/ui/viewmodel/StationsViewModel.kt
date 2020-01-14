package lt.kepo.airq.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.*
import lt.kepo.airq.data.api.ApiSuccessResponse
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.model.Station
import lt.kepo.airq.data.repository.airquality.AirQualityRepository
import lt.kepo.airq.domain.GetFilteredStationsUseCase

class StationsViewModel(
    private val airQualityRepository: AirQualityRepository,
    private val getStations: GetFilteredStationsUseCase
) : ViewModel() {
    private val _stations = MutableLiveData<MutableList<Station>>(mutableListOf())
    private val _isLoading = MutableLiveData<Boolean>(false)

    private lateinit var airQualityHere: AirQuality

    val stations: LiveData<MutableList<Station>> get() = _stations
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        viewModelScope.launch {
            airQualityHere = airQualityRepository.getLocalAirQualityHere()
        }
    }

    fun getRemoteStations(query: String) {
        viewModelScope.launch {
            when (val response = getStations(query, airQualityHere.stationId)) {
                is ApiSuccessResponse -> _stations.value = response.data
            }
        }
    }

    fun clearStations() {
        _stations.value = mutableListOf()
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