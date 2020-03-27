package lt.kepo.airquality.airqualitydetails

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import lt.kepo.airquality.AirQuality
import lt.kepo.airquality.airqualities.AirQualitiesRepository

class AirQualityDetailsViewModel @ViewModelInject constructor(
    private val airQualityRepository: AirQualityRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _isProgressVisible = MutableLiveData(false)
    private val _error = MutableLiveData<Error>()
    private val airQualityStationId: Int = requireNotNull(savedStateHandle.get("station_id"))

    val isProgressVisible: LiveData<Boolean> = _isProgressVisible
    val errorMessage: LiveData<Error> = _error
    val airQuality: LiveData<AirQuality> = airQualityRepository
        .getAirQuality(airQualityStationId)
        .asLiveData(viewModelScope.coroutineContext)

    init {
        viewModelScope.launch {
            refreshRepositoryAirQualities()
        }
    }

    fun removeAirQuality() {
        viewModelScope.launch {
            airQualityRepository.remove(airQualityStationId)
        }
    }

    fun refreshAirQuality(
        force: Boolean = false
    ) {
        viewModelScope.launch {
            _isProgressVisible.value = true



            _isProgressVisible.value = false
        }
    }

    private suspend fun refreshRepositoryAirQualities() {
        _isProgressVisible.value = true
        _error.value = null

        airQualityRepository
            .refresh(airQualityStationId)
            .let { refreshResult ->
                when (refreshResult) {
                    is AirQualityRepository.RefreshResult.Error -> {
                        _error.value = Error.RefreshAirQuality(
                            message = "error"
                        )
                    }
                }
            }

        _isProgressVisible.value = false
    }

    sealed class Error {

        data class RefreshAirQuality(
            val message: String
        ) : Error()
    }
}