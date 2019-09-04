package lt.kepo.airq.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import lt.kepo.airq.data.models.AirQuality
import lt.kepo.airq.data.airquality.AirQualityRepository
import kotlin.coroutines.CoroutineContext

/**
 * The ViewModel used in [PlantDetailFragment].
 */
class AirQualityViewModel(private val airQualityRepository: AirQualityRepository) : ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    val airQuality = MutableLiveData<AirQuality>()

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
        job.cancel()
    }

    init {
    }

    fun getAirQuality() {
        launch {
            val result = withContext(Dispatchers.IO) { airQualityRepository.getHere() }

            when (result.status) {
                "ok" -> airQuality.value = result.data
                else -> {

                }
            }
        }
    }
}
