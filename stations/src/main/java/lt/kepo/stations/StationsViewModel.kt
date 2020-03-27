package lt.kepo.stations

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.*

class StationsViewModel @ViewModelInject constructor(
    private val getStationsUseCase: GetStationsUseCase,
    private val addStationUseCase: AddStationUseCase
) : ViewModel() {
    private val _stations = MutableLiveData<List<Station>>(mutableListOf())
    private val _isLoading = MutableLiveData(false)
    private val _errorMessage = MutableLiveData<String>()

    val stations: LiveData<List<Station>> = _stations
    val isLoading: LiveData<Boolean> = _isLoading
    val errorMessage: LiveData<String> = _errorMessage

    private var getStationsJob: Job? = null

    fun getStations(query: String) {
        getStationsJob = viewModelScope.launch {
            _isLoading.value = true

            when (val result = getStationsUseCase(query)) {
                is GetStationsUseCase.Result.Success -> _stations.value = result.stations
                is GetStationsUseCase.Result.Error -> _errorMessage.value = ""
            }

            _isLoading.value = false
        }
    }

    fun clearStations() {
        getStationsJob?.cancel()
        _stations.value = emptyList()
    }

    fun addStation(stationId: Int) {
        viewModelScope.launch {
            _isLoading.value = true

            when (addStationUseCase(stationId)) {
                is AddStationUseCase.Result.Success -> {
                    _stations.value = _stations.value?.filterNot { it.id == stationId }
                }
                is AddStationUseCase.Result.Error -> _errorMessage.value = "response.error"
            }

            _isLoading.value = false
        }
    }
}