package lt.kepo.airquality.airqualities

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lt.kepo.airquality.AirQualitiesRepository
import lt.kepo.airquality.AirQuality
import lt.kepo.airquality.IsAirQualitiesExpired
import javax.inject.Inject

@HiltViewModel
class AirQualitiesViewModel @Inject constructor(
    private val airQualitiesRepository: AirQualitiesRepository,
    private val isAirQualitiesExpired: IsAirQualitiesExpired
) : ViewModel() {

    private val _isProgressVisible = MutableLiveData(false)
    private val _error = MutableLiveData<Error>(null)

    val isProgressVisible: LiveData<Boolean> = _isProgressVisible
    val errorMessage: LiveData<Error> = _error
    val airQualities: LiveData<List<AirQuality>> = airQualitiesRepository
        .getAll()
        .asLiveData(viewModelScope.coroutineContext)

    fun refreshAirQualities(isForced: Boolean) {
        viewModelScope.launch {
            _isProgressVisible.value = true
            _error.value = null

            if (isForced || isAirQualitiesExpired()) {
                airQualitiesRepository
                    .refresh()
                    .let { refreshResult ->
                        when (refreshResult) {
                            is AirQualitiesRepository.RefreshResult.Error -> {
                                _error.value = Error.RefreshAirQualities
                            }
                        }
                    }
            }

            _isProgressVisible.value = false
        }
    }

    sealed class Error {

        object RefreshAirQualities : Error()
    }
}
