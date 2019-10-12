package lt.kepo.airq.ui.viewmodel

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import lt.kepo.airq.api.ApiEmptyResponse
import lt.kepo.airq.api.ApiErrorResponse
import lt.kepo.airq.api.ApiSuccessResponse
import lt.kepo.airq.db.model.AirQuality
import lt.kepo.airq.repository.airquality.AirQualityRepository
import lt.kepo.airq.utility.isLocationEnabled
import kotlin.coroutines.CoroutineContext

class AirQualityViewModel(
    private val airQualityRepository: AirQualityRepository,
    application: Application
) : AndroidViewModel(application), CoroutineScope {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    val airQuality = MutableLiveData<AirQuality>()
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val isLocationFound = MutableLiveData<Boolean>()

    init {
        if (isLocationEnabled(application)) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
        }
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
        job.cancel()
    }

    fun getLocalAirQualityHere() {
        launch { airQuality.value = airQualityRepository.getLocalAirQualityHere() }
    }

    fun getRemoteAirQualityHere(context: Context) {
        if (isLocationEnabled(context)) {
            fusedLocationClient.locationAvailability.addOnSuccessListener { locationAvailability ->
                if (locationAvailability.isLocationAvailable)
                    fusedLocationClient.lastLocation.addOnSuccessListener { loc -> fetchRemoteAirQualityHere(loc) }
                else
                    fetchRemoteAirQualityHere(null)
            }
        } else {
            fetchRemoteAirQualityHere(null)
        }
    }

    private fun fetchRemoteAirQualityHere(location: Location?) {
        launch {
            isLoading.value = true
            isLocationFound.value = location != null

            when (val response = if (location == null)
                    airQualityRepository.getRemoteAirQualityHere()
                else
                    airQualityRepository.getRemoteAirQualityHere(location)
            ) {
                is ApiSuccessResponse -> {
                    errorMessage.value = ""
                    airQuality.value = AirQuality.build(response.data)

                    launch (Dispatchers.IO) {
                        airQuality.value!!.isCurrentLocationQuality = true
                        airQualityRepository.upsertLocalAirQualityHere(airQuality.value!!)
                    }
                }
                is ApiErrorResponse -> errorMessage.value = response.errorMessage
                is ApiEmptyResponse -> errorMessage.value = "Api returned empty response"
            }

            isLoading.value = false
        }
    }
}