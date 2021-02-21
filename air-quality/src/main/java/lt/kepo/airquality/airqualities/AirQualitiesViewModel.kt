package lt.kepo.airquality.airqualities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import lt.kepo.airquality.AirQualitiesRepository
import lt.kepo.airquality.AirQuality
import lt.kepo.airquality.RefreshAirQualitiesCacheUseCase

class AirQualitiesViewModel @ViewModelInject constructor(
    private val airQualitiesRepository: AirQualitiesRepository,
    private val refreshCacheUseCase: RefreshAirQualitiesCacheUseCase
) : ViewModel() {

    private val _isProgressVisible = MutableLiveData(false)
    private val _error = MutableLiveData<Error>(null)

    val isProgressVisible: LiveData<Boolean> = _isProgressVisible
    val errorMessage: LiveData<Error> = _error
    val airQualities: LiveData<List<AirQuality>> = airQualitiesRepository
        .getAll()
        .asLiveData(viewModelScope.coroutineContext)

    init {
        viewModelScope.launch {
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
    }

    fun refreshAirQualities() {
        viewModelScope.launch {
            _isProgressVisible.value = true
            _error.value = null

            refreshCacheUseCase()
                .let { refreshResult ->
                    when (refreshResult) {
                        is RefreshAirQualitiesCacheUseCase.Result.Error -> _error.value = Error.RefreshAirQualities
                    }
                }

            _isProgressVisible.value = false
        }
    }

    sealed class Error {

        object RefreshAirQualities : Error()
    }
}
