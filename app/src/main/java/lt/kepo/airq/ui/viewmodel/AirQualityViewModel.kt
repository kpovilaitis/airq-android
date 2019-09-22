package lt.kepo.airq.ui.viewmodel

import lt.kepo.airq.utility.isLocationEnabled
import lt.kepo.airq.db.model.AirQuality
import lt.kepo.airq.repository.AirQualityRepository
import retrofit2.HttpException
import java.net.UnknownHostException
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

/**
 * The ViewModel used in [AirQualityFragment].
 */
class AirQualityViewModel(
    private val airQualityRepository: AirQualityRepository,
    application: Application
) : AndroidViewModel(application), CoroutineScope {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    val airQuality = MutableLiveData<AirQuality>()
    val isLoading = MutableLiveData<Boolean>()
    val isError = MutableLiveData<Boolean>()

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
        launch {
            airQuality.value = withContext(Dispatchers.IO) { airQualityRepository.getLocalHere() }
        }
    }

    fun getRemoteAirQualityHere(context: Context) {
        try {
            if (isLocationEnabled(context)) {
                fusedLocationClient.locationAvailability.addOnSuccessListener { locationAvailability ->
                    if (locationAvailability.isLocationAvailable) {
                        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                            launch {
                                isLoading.value = true
                                airQuality.value = withContext(Dispatchers.IO) { airQualityRepository.getRemoteHereByLocation(loc) }
                                isLoading.value = false
                            }
                        }
                    } else launch {
                        isLoading.value = true
                        airQuality.value = withContext(Dispatchers.IO) { airQualityRepository.getRemoteHere() }
                        isLoading.value = false
                    }
                }

            } else {
                launch {
                    isLoading.value = true
                    airQuality.value = withContext(Dispatchers.IO) { airQualityRepository.getRemoteHere() }
                    isLoading.value = false
                }
            }

            isError.value = false
        } catch (e: HttpException) {
            println(e)

            isError.value = true
            isLoading.value = false
        } catch (e: UnknownHostException) {
            println(e)

            isError.value = true
            isLoading.value = false
        }
    }
}