package lt.kepo.airq.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import lt.kepo.airq.db.model.AirQuality
import lt.kepo.airq.repository.AirQualityRepository
import retrofit2.HttpException
import java.net.UnknownHostException
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
        getLocalAirQualityHere()
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
        job.cancel()
    }

    fun getRemoteAirQualityHere() {
        launch {
            isLoading.value = true

            try {
                airQuality.value = withContext(Dispatchers.IO) { airQualityRepository.getRemoteHere() }
            } catch (e: HttpException) {
                println(e)
            } catch (e: UnknownHostException) {
                println(e)
            }

            isLoading.value = false
        }
    }

    fun getLocalAirQualityHere() {
        launch { airQuality.value = withContext(Dispatchers.IO) { airQualityRepository.getLocalHere() } }
    }
}
