package lt.kepo.airq.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import lt.kepo.airq.data.models.AirQuality
import lt.kepo.airq.db.airquality.AirQualityRepository
import kotlin.coroutines.CoroutineContext

/**
 * The ViewModel used in [AirQualityFragment].
 */
class AirQualityViewModel(private val airQualityRepository: AirQualityRepository) : ViewModel(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    val airQuality = MutableLiveData<AirQuality>()
    var isLoading = MutableLiveData<Boolean>()

    init {
        getAirQualityHereFromDb()
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
        job.cancel()
    }

    fun getAirQualityHereFromApi() {
        isLoading.value = true

        launch {
            val result = withContext(Dispatchers.IO) { airQualityRepository.getHereFromApi() }

            when (result.status) {
                "ok" -> {
                    airQuality.value = result.data
                    isLoading.value = false

                    insertAirQuality(airQuality.value!!)
                }
                else -> {

                }
            }
        }
    }

    fun getAirQualityHereFromDb() {
        isLoading.value = true

        launch { airQuality.value = withContext(Dispatchers.IO) { airQualityRepository.getHereFromDb() } }

        isLoading.value = false
    }

    private fun insertAirQuality(airQuality: AirQuality) {
        launch {
            withContext(Dispatchers.IO) {
                airQualityRepository.insert(airQuality)
            }
        }
    }
}
