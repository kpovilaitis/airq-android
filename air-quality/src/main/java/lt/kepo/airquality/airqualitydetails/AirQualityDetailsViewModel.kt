package lt.kepo.airquality.airqualitydetails

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lt.kepo.airquality.AirQualitiesRepository
import lt.kepo.airquality.AirQuality
import lt.kepo.airquality.RefreshAirQualitiesCacheUseCase
import javax.inject.Inject

@HiltViewModel
class AirQualityDetailsViewModel @Inject constructor(
    private val airQualitiesRepository: AirQualitiesRepository,
    private val refreshCacheUseCase: RefreshAirQualitiesCacheUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _isProgressVisible = MutableLiveData(false)
    private val _error = MutableLiveData<Error>(null)
    private val airQualityStationId: Int = requireNotNull(savedStateHandle.get("station_id"))

    val isProgressVisible: LiveData<Boolean> = _isProgressVisible
    val errorMessage: LiveData<Error> = _error
    val airQuality: LiveData<AirQuality> = airQualitiesRepository
        .getAirQuality(airQualityStationId)
        .asLiveData(viewModelScope.coroutineContext)
    val isRemoveAirQualityVisible: LiveData<Boolean> = airQuality.map { it.isCurrentLocationQuality.not() }

    init {
        viewModelScope.launch {
            _isProgressVisible.value = true
            _error.value = null

            airQualitiesRepository
                .refresh()
                .let { refreshResult ->
                    when (refreshResult) {
                        is AirQualitiesRepository.RefreshResult.Error -> _error.value = Error.RefreshAirQuality
                    }
                }

            _isProgressVisible.value = false
        }
    }

    fun removeAirQuality() {
        viewModelScope.launch {
            airQualitiesRepository.remove(airQualityStationId)
        }
    }

    fun refreshAirQuality() {
        viewModelScope.launch {
            _isProgressVisible.value = true
            _error.value = null

            refreshCacheUseCase()
                .let { refreshResult ->
                    when (refreshResult) {
                        is RefreshAirQualitiesCacheUseCase.Result.Error -> _error.value = Error.RefreshAirQuality
                    }
                }

            _isProgressVisible.value = false
        }
    }

    sealed class Error {

        object RefreshAirQuality : Error()
    }
}