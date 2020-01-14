package lt.kepo.airq.ui.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import lt.kepo.airq.data.api.ApiErrorResponse
import lt.kepo.airq.data.api.ApiSuccessResponse
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.data.repository.airquality.AirQualityRepository
import lt.kepo.airq.domain.UpdateAirQualityHereUseCase
import lt.kepo.airq.utility.isLocationEnabled
import java.util.*

abstract class BaseAirQualityViewModel(
    context: Application,
    private val fusedLocationClient : FusedLocationProviderClient,
    private val updateAirQualityHereUseCase: UpdateAirQualityHereUseCase
) : AndroidViewModel(context) {
    val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun updateLocalAirQualityHere(force: Boolean, airQualityHere: AirQuality?) {
        _isLoading.value = true

        if (isLocationEnabled(getApplication())) {
            fusedLocationClient.locationAvailability.addOnSuccessListener { locationAvailability ->
                if (locationAvailability.isLocationAvailable)
                    fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                        viewModelScope.launch { invokeUseCase(force, airQualityHere, loc) }
                    }
                else
                    viewModelScope.launch { invokeUseCase(force, airQualityHere,null) }
            }
        } else {
            viewModelScope.launch { invokeUseCase(force, airQualityHere,null) }
        }
    }

    private suspend fun invokeUseCase(
        force: Boolean,
        airQualityHere: AirQuality?,
        location: Location?
    ) {
        when (val result = updateAirQualityHereUseCase(force, airQualityHere, location)) {
            is ApiSuccessResponse -> _isLoading.value = false
            is ApiErrorResponse<*> -> {
                _isLoading.value = false
                _errorMessage.value = result.error
            }
        }
    }
}