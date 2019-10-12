package lt.kepo.airq.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import lt.kepo.airq.api.ApiEmptyResponse
import lt.kepo.airq.api.ApiErrorResponse
import lt.kepo.airq.api.ApiSuccessResponse
import lt.kepo.airq.db.model.Station
import lt.kepo.airq.repository.stations.StationsRepository
import kotlin.coroutines.CoroutineContext

class StationsViewModel(private val stationsRepository: StationsRepository) : ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    val stations = MutableLiveData<MutableList<Station>>()
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
        job.cancel()
    }

    fun getRemoteStations(query: String) {
        launch {
            isLoading.value = true

            when (val response = stationsRepository.getRemoteStations(query)) {
                is ApiSuccessResponse -> {
                    errorMessage.value = ""
                    stations.value = response.data.map{ stationDto -> Station.build(stationDto) }.toMutableList()
                }
                is ApiErrorResponse -> errorMessage.value = response.errorMessage
                is ApiEmptyResponse -> errorMessage.value = "Api returned empty response"
            }

            isLoading.value = false
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
        launch { stationsRepository.insertLocalStation(station) }
    }

    fun getLocalAllStations() {
        launch { stations.value = stationsRepository.getLocalAllStations().toMutableList() }
    }

    fun removeLocalStation(station: Station) {
        launch { stationsRepository.deleteLocalStation(station) }
    }
}