package lt.kepo.airq.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import lt.kepo.airq.api.ApiSuccessResponse
import lt.kepo.airq.db.model.Station
import lt.kepo.airq.repository.stations.StationsRepository

class StationsViewModel(private val stationsRepository: StationsRepository) : ViewModel() {
    val stations = MutableLiveData<MutableList<Station>>()

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }

    fun getRemoteStations(query: String) {
        viewModelScope.launch {
            when (val response = stationsRepository.getRemoteStations(query)) {
                is ApiSuccessResponse -> stations.value = response.data.map{ stationDto -> Station.build(stationDto) }.toMutableList()
            }
        }
    }

//    fun getAndUpdateLocalStations() {
//        launch {
//            stations.value = withContext(Dispatchers.IO) { stationsRepository.getLocalAllStations().toMutableList() }
//
//            stations.value?.forEach { station ->
//                when (val response = withContext(Dispatchers.IO) { stationsRepository.getRemoteStation(station.id) }) {
//                    is ApiSuccessResponse -> {
//                        errorMessage.value = ""
//
//                        val responseStation = Station.build(response.data)
//                        stations.value?.find { it.id == responseStation.id }?.airQualityIndex = responseStation.airQualityIndex
//
//                        launch (Dispatchers.IO) {
//                            stationsRepository.insertLocalStation(station)
//                        }
//                    }
//                    is ApiErrorResponse -> errorMessage.value = response.errorMessage
//                    is ApiEmptyResponse -> errorMessage.value = "Api returned empty response"
//                }
//            }
//        }
//    }

    fun addStationToLocalStorage(station: Station) {
        viewModelScope.launch { stationsRepository.insertLocalStation(station) }
    }

    fun getLocalAllStations() {
        viewModelScope.launch { stations.value = stationsRepository.getLocalAllStations().toMutableList() }
    }

    fun removeLocalStation(station: Station) {
        viewModelScope.launch { stationsRepository.deleteLocalStation(station) }
    }
}