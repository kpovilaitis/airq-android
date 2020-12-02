package lt.kepo.airquality.airqualities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import lt.kepo.airquality.AirQuality

class AirQualitiesViewModel @ViewModelInject constructor(
    private val airQualitiesRepository: AirQualitiesRepository
) : ViewModel() {

    private val _isProgressVisible = MutableLiveData(false)
    private val _error = MutableLiveData<Error>(null)

    val isProgressVisible: LiveData<Boolean> = _isProgressVisible
    val errorMessage: LiveData<Error> = _error
    val airQualities: LiveData<List<AirQuality>> = airQualitiesRepository
        .getAirQualities()
        .asLiveData(viewModelScope.coroutineContext)

    init {
        viewModelScope.launch {
            refreshRepositoryAirQualities()
        }
    }

    fun refreshAirQualities() {
        viewModelScope.launch {
            refreshRepositoryAirQualities()
        }
    }

    private suspend fun refreshRepositoryAirQualities() {
        _isProgressVisible.value = true
        _error.value = null

        airQualitiesRepository
            .refresh()
            .let { refreshResult ->
                 when (refreshResult) {
                    is AirQualitiesRepository.RefreshResult.Error -> _error.value = Error.RefreshAirQualities
                }
            }

        _isProgressVisible.value = false
    }

    sealed class Error {

        object RefreshAirQualities : Error()
    }
}

//fun lt.kepo.airqualityapi.response.AirQuality.shouldUpdate() = updatedAt?.before(Date(Date().time - 1000 * 60 * 10)) == true